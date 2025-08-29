package dao;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Clase base para el acceso a datos con JPA.
 * Implementa caché thread-safe de EntityManagerFactory por empresa con gestión de recursos mejorada.
 */
public class JpaPadre implements Serializable, AutoCloseable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(JpaPadre.class.getName());

    protected final String empresa;
    protected final EntityManagerFactory emf;
    
    // Para tracking de EntityManagers activos (opcional para debugging)
    private static final Map<String, AtomicInteger> activeEntityManagers = new ConcurrentHashMap<>();

    // Cache thread-safe de EMFs con contadores de referencia
    private static final Map<String, EntityManagerFactory> emfCache = new ConcurrentHashMap<>();
    private static final Map<String, AtomicInteger> emfReferenceCount = new ConcurrentHashMap<>();

    // Lock para operaciones sincronizadas en el cache
    private static final Object cacheLock = new Object();

    public JpaPadre(String empresa) {
        if (empresa == null || empresa.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de empresa no puede ser nulo o vacío");
        }
        this.empresa = empresa.trim().toLowerCase();
        this.emf = obtenerEntityManagerFactory(this.empresa);
        
        // Incrementar contador de referencias
        synchronized (cacheLock) {
            emfReferenceCount.computeIfAbsent(this.empresa, k -> new AtomicInteger(0)).incrementAndGet();
        }
    }

    private EntityManagerFactory obtenerEntityManagerFactory(String empresa) {
        return emfCache.computeIfAbsent(empresa, emp -> {
            String puName = determinarUnidadPersistencia(empresa);
            try {
                LOGGER.log(Level.INFO, "Creando EntityManagerFactory para empresa: {0}", empresa);
                
                // Configurar propiedades adicionales para optimización
                Map<String, Object> properties = getEntityManagerFactoryProperties();
                
                EntityManagerFactory factory = Persistence.createEntityManagerFactory(puName, properties);
                
                // Inicializar contador de EntityManagers activos
                activeEntityManagers.put(empresa, new AtomicInteger(0));
                
                return factory;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error al crear EntityManagerFactory para empresa: " + empresa, e);
                throw new RuntimeException("Error al inicializar JPA para empresa: " + empresa, e);
            }
        });
    }

    /**
     * Configura propiedades optimizadas para el EntityManagerFactory
     */
    private Map<String, Object> getEntityManagerFactoryProperties() {
        Map<String, Object> properties = new ConcurrentHashMap<>();
        
        // Configuraciones de conexión optimizadas
        properties.put("hibernate.connection.provider_disables_autocommit", "true");
        properties.put("hibernate.jdbc.batch_size", "25");
        properties.put("hibernate.order_inserts", "true");
        properties.put("hibernate.order_updates", "true");
        properties.put("hibernate.jdbc.batch_versioned_data", "true");
        
        // Configuraciones de cache de segundo nivel (opcional)
        properties.put("hibernate.cache.use_second_level_cache", "false");
        properties.put("hibernate.cache.use_query_cache", "false");
        
        // Configuraciones de logging (para producción desactivar)
        properties.put("hibernate.show_sql", "false");
        properties.put("hibernate.format_sql", "false");
        
        // Configuración de pool de conexiones (si usas HikariCP)
        properties.put("hibernate.hikari.maximumPoolSize", "20");
        properties.put("hibernate.hikari.minimumIdle", "5");
        properties.put("hibernate.hikari.idleTimeout", "300000"); // 5 minutos
        properties.put("hibernate.hikari.connectionTimeout", "30000"); // 30 segundos
        properties.put("hibernate.hikari.leakDetectionThreshold", "60000"); // 1 minuto
        
        return properties;
    }

    private String determinarUnidadPersistencia(String empresa) {
        switch (empresa) {
            case "a":
                return "logistica";
            case "b":
                return "logistica2";
            case "c":
                return "clinica";
            case "d":
                return "sigoldbi";
            case "e":
                return "sigolD";
            default:
                throw new IllegalArgumentException("Empresa no válida: " + empresa + 
                    ". Empresas válidas: a, b, c, d, e");
        }
    }

    /**
     * Crea un EntityManager con tracking opcional
     */
    public EntityManager getEntityManager() {
        try {
            EntityManager em = emf.createEntityManager();
            if (em == null) {
                throw new RuntimeException("No se pudo crear EntityManager para empresa: " + empresa);
            }
            
            // Incrementar contador de EntityManagers activos (para debugging)
            activeEntityManagers.get(empresa).incrementAndGet();
            
            // Crear wrapper para tracking de cierre
            return new TrackedEntityManager(em, empresa);
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al crear EntityManager para empresa: " + empresa, e);
            throw new RuntimeException("Error al crear EntityManager", e);
        }
    }

    /**
     * Verifica si el EntityManagerFactory está disponible
     */
    public boolean isEntityManagerFactoryOpen() {
        return emf != null && emf.isOpen();
    }

    /**
     * Obtiene información de estado para debugging
     */
    public String getStatusInfo() {
        AtomicInteger activeCount = activeEntityManagers.get(empresa);
        int active = activeCount != null ? activeCount.get() : 0;
        
        return String.format("Empresa: %s, EMF Open: %s, EntityManagers activos: %d", 
            empresa, isEntityManagerFactoryOpen(), active);
    }

    /**
     * Implementación de AutoCloseable para uso con try-with-resources
     */
    @Override
    public void close() {
        synchronized (cacheLock) {
            AtomicInteger refCount = emfReferenceCount.get(empresa);
            if (refCount != null && refCount.decrementAndGet() <= 0) {
                // Si no hay más referencias, cerrar el EMF
                EntityManagerFactory factory = emfCache.remove(empresa);
                emfReferenceCount.remove(empresa);
                activeEntityManagers.remove(empresa);
                
                if (factory != null && factory.isOpen()) {
                    try {
                        factory.close();
                        LOGGER.log(Level.INFO, "EntityManagerFactory cerrado para empresa: {0}", empresa);
                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, "Error al cerrar EntityManagerFactory para empresa: " + empresa, e);
                    }
                }
            }
        }
    }

    /**
     * Cierra todos los EntityManagerFactory en caché.
     * Para uso exclusivo del ServletContextListener.
     */
    public static void closeAllEntityManagerFactories() {
        synchronized (cacheLock) {
            LOGGER.log(Level.INFO, "Cerrando todos los EntityManagerFactory...");
            
            emfCache.forEach((empresa, emf) -> {
                try {
                    if (emf != null && emf.isOpen()) {
                        emf.close();
                        LOGGER.log(Level.INFO, "EntityManagerFactory cerrado para empresa: {0}", empresa);
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error al cerrar EntityManagerFactory para empresa: " + empresa, e);
                }
            });
            
            emfCache.clear();
            emfReferenceCount.clear();
            activeEntityManagers.clear();
            
            LOGGER.log(Level.INFO, "Limpieza de cache JPA completada");
        }
    }

    /**
     * Fuerza el cierre de un EntityManagerFactory específico
     */
    public static void closeEntityManagerFactory(String empresa) {
        if (empresa == null || empresa.trim().isEmpty()) {
            return;
        }
        
        empresa = empresa.trim().toLowerCase();
        
        synchronized (cacheLock) {
            EntityManagerFactory emf = emfCache.remove(empresa);
            emfReferenceCount.remove(empresa);
            activeEntityManagers.remove(empresa);
            
            if (emf != null && emf.isOpen()) {
                try {
                    emf.close();
                    LOGGER.log(Level.INFO, "EntityManagerFactory forzado a cerrar para empresa: {0}", empresa);
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error al cerrar forzadamente EntityManagerFactory para empresa: " + empresa, e);
                }
            }
        }
    }

    /**
     * Obtiene estadísticas del cache
     */
    public static Map<String, String> getCacheStatistics() {
        Map<String, String> stats = new ConcurrentHashMap<>();
        
        synchronized (cacheLock) {
            stats.put("total_emf_cached", String.valueOf(emfCache.size()));
            
            emfCache.forEach((empresa, emf) -> {
                AtomicInteger activeCount = activeEntityManagers.get(empresa);
                AtomicInteger refCount = emfReferenceCount.get(empresa);
                
                stats.put("empresa_" + empresa + "_emf_open", String.valueOf(emf.isOpen()));
                stats.put("empresa_" + empresa + "_active_em", String.valueOf(activeCount != null ? activeCount.get() : 0));
                stats.put("empresa_" + empresa + "_references", String.valueOf(refCount != null ? refCount.get() : 0));
            });
        }
        
        return stats;
    }

    /**
     * Obtiene una vista de solo lectura de la caché.
     */
    public static Map<String, EntityManagerFactory> getCacheView() {
        return Collections.unmodifiableMap(emfCache);
    }

    /**
     * Wrapper para EntityManager que incluye tracking
     */
    private static class TrackedEntityManager implements EntityManager {
        private final EntityManager delegate;
        private final String empresa;
        private volatile boolean closed = false;

        public TrackedEntityManager(EntityManager delegate, String empresa) {
            this.delegate = delegate;
            this.empresa = empresa;
        }

        @Override
        public void close() {
            if (!closed) {
                closed = true;
                try {
                    delegate.close();
                    // Decrementar contador de EntityManagers activos
                    AtomicInteger activeCount = activeEntityManagers.get(empresa);
                    if (activeCount != null) {
                        activeCount.decrementAndGet();
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error al cerrar EntityManager para empresa: " + empresa, e);
                }
            }
        }

        @Override
        public boolean isOpen() {
            return !closed && delegate.isOpen();
        }

        // Delegar todos los demás métodos al EntityManager original
        @Override
        public void persist(Object entity) { delegate.persist(entity); }

        @Override
        public <T> T merge(T entity) { return delegate.merge(entity); }

        @Override
        public void remove(Object entity) { delegate.remove(entity); }

        @Override
        public <T> T find(Class<T> entityClass, Object primaryKey) { 
            return delegate.find(entityClass, primaryKey); 
        }

        @Override
        public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
            return delegate.find(entityClass, primaryKey, properties);
        }

        @Override
        public <T> T find(Class<T> entityClass, Object primaryKey, javax.persistence.LockModeType lockMode) {
            return delegate.find(entityClass, primaryKey, lockMode);
        }

        @Override
        public <T> T find(Class<T> entityClass, Object primaryKey, javax.persistence.LockModeType lockMode, Map<String, Object> properties) {
            return delegate.find(entityClass, primaryKey, lockMode, properties);
        }

        @Override
        public <T> T getReference(Class<T> entityClass, Object primaryKey) {
            return delegate.getReference(entityClass, primaryKey);
        }

        @Override
        public void flush() { delegate.flush(); }

        @Override
        public void setFlushMode(javax.persistence.FlushModeType flushMode) { delegate.setFlushMode(flushMode); }

        @Override
        public javax.persistence.FlushModeType getFlushMode() { return delegate.getFlushMode(); }

        @Override
        public void lock(Object entity, javax.persistence.LockModeType lockMode) { delegate.lock(entity, lockMode); }

        @Override
        public void lock(Object entity, javax.persistence.LockModeType lockMode, Map<String, Object> properties) {
            delegate.lock(entity, lockMode, properties);
        }

        @Override
        public void refresh(Object entity) { delegate.refresh(entity); }

        @Override
        public void refresh(Object entity, Map<String, Object> properties) { delegate.refresh(entity, properties); }

        @Override
        public void refresh(Object entity, javax.persistence.LockModeType lockMode) { delegate.refresh(entity, lockMode); }

        @Override
        public void refresh(Object entity, javax.persistence.LockModeType lockMode, Map<String, Object> properties) {
            delegate.refresh(entity, lockMode, properties);
        }

        @Override
        public void clear() { delegate.clear(); }

        @Override
        public void detach(Object entity) { delegate.detach(entity); }

        @Override
        public boolean contains(Object entity) { return delegate.contains(entity); }

        @Override
        public javax.persistence.LockModeType getLockMode(Object entity) { return delegate.getLockMode(entity); }

        @Override
        public void setProperty(String propertyName, Object value) { delegate.setProperty(propertyName, value); }

        @Override
        public Map<String, Object> getProperties() { return delegate.getProperties(); }

        @Override
        public javax.persistence.Query createQuery(String qlString) { return delegate.createQuery(qlString); }

        @Override
        public <T> javax.persistence.TypedQuery<T> createQuery(javax.persistence.criteria.CriteriaQuery<T> criteriaQuery) {
            return delegate.createQuery(criteriaQuery);
        }

        @Override
        public javax.persistence.Query createQuery(javax.persistence.criteria.CriteriaUpdate updateQuery) {
            return delegate.createQuery(updateQuery);
        }

        @Override
        public javax.persistence.Query createQuery(javax.persistence.criteria.CriteriaDelete deleteQuery) {
            return delegate.createQuery(deleteQuery);
        }

        @Override
        public <T> javax.persistence.TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
            return delegate.createQuery(qlString, resultClass);
        }

        @Override
        public javax.persistence.Query createNamedQuery(String name) { return delegate.createNamedQuery(name); }

        @Override
        public <T> javax.persistence.TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
            return delegate.createNamedQuery(name, resultClass);
        }

        @Override
        public javax.persistence.Query createNativeQuery(String sqlString) { return delegate.createNativeQuery(sqlString); }

        @Override
        public javax.persistence.Query createNativeQuery(String sqlString, Class resultClass) {
            return delegate.createNativeQuery(sqlString, resultClass);
        }

        @Override
        public javax.persistence.Query createNativeQuery(String sqlString, String resultSetMapping) {
            return delegate.createNativeQuery(sqlString, resultSetMapping);
        }

        @Override
        public javax.persistence.StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
            return delegate.createNamedStoredProcedureQuery(name);
        }

        @Override
        public javax.persistence.StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
            return delegate.createStoredProcedureQuery(procedureName);
        }

        @Override
        public javax.persistence.StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
            return delegate.createStoredProcedureQuery(procedureName, resultClasses);
        }

        @Override
        public javax.persistence.StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
            return delegate.createStoredProcedureQuery(procedureName, resultSetMappings);
        }

        @Override
        public void joinTransaction() { delegate.joinTransaction(); }

        @Override
        public boolean isJoinedToTransaction() { return delegate.isJoinedToTransaction(); }

        @Override
        public <T> T unwrap(Class<T> cls) { return delegate.unwrap(cls); }

        @Override
        public Object getDelegate() { return delegate.getDelegate(); }

        @Override
        public javax.persistence.EntityTransaction getTransaction() { return delegate.getTransaction(); }

        @Override
        public javax.persistence.EntityManagerFactory getEntityManagerFactory() { return delegate.getEntityManagerFactory(); }

        @Override
        public javax.persistence.criteria.CriteriaBuilder getCriteriaBuilder() { return delegate.getCriteriaBuilder(); }

        @Override
        public javax.persistence.metamodel.Metamodel getMetamodel() { return delegate.getMetamodel(); }

        @Override
        public <T> javax.persistence.EntityGraph<T> createEntityGraph(Class<T> rootType) {
            return delegate.createEntityGraph(rootType);
        }

        @Override
        public javax.persistence.EntityGraph<?> createEntityGraph(String graphName) {
            return delegate.createEntityGraph(graphName);
        }

        @Override
        public javax.persistence.EntityGraph<?> getEntityGraph(String graphName) {
            return delegate.getEntityGraph(graphName);
        }

        @Override
        public <T> java.util.List<javax.persistence.EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
            return delegate.getEntityGraphs(entityClass);
        }
    }
}