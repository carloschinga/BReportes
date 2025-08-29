package dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.PetitorioTecnico;

public class PetitorioTecnicoJpaController extends JpaPadre {

    public PetitorioTecnicoJpaController(String empresa) {
        super(empresa);
    }

    public void create(PetitorioTecnico petitorioTecnico) throws PreexistingEntityException, Exception {
        if (petitorioTecnico.getNombre() == null || petitorioTecnico.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre no puede estar vacío");
        }

        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            // Generate code if not provided
            if (petitorioTecnico.getCodigo() == null || petitorioTecnico.getCodigo().isEmpty()) {
                String nuevoCodigo;
                if (petitorioTecnico.getPadre() == null) {
                    nuevoCodigo = generarSiguienteCodigoRaiz();
                } else {
                    PetitorioTecnico padre = findPetitorioTecnicoByCodigo(petitorioTecnico.getPadre());
                    if (padre == null) {
                        throw new Exception("El código padre " + petitorioTecnico.getPadre() + " no existe");
                    }
                    nuevoCodigo = generarSiguienteCodigoHijo(petitorioTecnico.getPadre());
                }
                petitorioTecnico.setCodigo(nuevoCodigo);
            }
            if (petitorioTecnico.getCncntr() == null)
                petitorioTecnico.setCncntr("");
            if (petitorioTecnico.getFormFarma() == null)
                petitorioTecnico.setFormFarma("");
            if (petitorioTecnico.getPrsntcn() == null)
                petitorioTecnico.setPrsntcn("");
            if (petitorioTecnico.getCodigoProducto() == null)
                petitorioTecnico.setCodigoProducto("");

            em.persist(petitorioTecnico);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPetitorioTecnicoByCodigo(petitorioTecnico.getCodigo()) != null) {
                throw new PreexistingEntityException("PetitorioTecnico " + petitorioTecnico + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String generarSiguienteCodigoRaiz() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createQuery(
                    "SELECT p.codigo FROM PetitorioTecnico p " +
                            "WHERE p.padre IS NULL AND p.codigo NOT LIKE '%.%' " +
                            "ORDER BY LENGTH(p.codigo) DESC, p.codigo DESC",
                    String.class).setMaxResults(1);

            String ultimoCodigo = (String) query.getResultList().stream().findFirst().orElse(null);

            if (ultimoCodigo == null) {
                return "1";
            } else {
                try {
                    int ultimoNumero = Integer.parseInt(ultimoCodigo);
                    return String.valueOf(ultimoNumero + 1);
                } catch (NumberFormatException e) {
                    return "1";
                }
            }
        } catch (Exception e) {
            return "1";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String generarSiguienteCodigoHijo(String padreCodigo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createQuery(
                    "SELECT p.codigo FROM PetitorioTecnico p " +
                            "WHERE p.padre = :padre " +
                            "ORDER BY LENGTH(p.codigo) DESC, p.codigo DESC",
                    String.class).setParameter("padre", padreCodigo);

            List<String> resultados = query.getResultList();

            if (resultados.isEmpty()) {
                return padreCodigo + ".1";
            } else {
                String ultimoCodigo = resultados.get(0);
                String[] partes = ultimoCodigo.split(Pattern.quote("."));
                try {
                    int ultimoNumero = Integer.parseInt(partes[partes.length - 1]);
                    return padreCodigo + "." + (ultimoNumero + 1);
                } catch (NumberFormatException e) {
                    return padreCodigo + ".1";
                }
            }
        } catch (Exception e) {
            return padreCodigo + ".1";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<PetitorioTecnico> findItemsRaiz() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createQuery(
                    "SELECT p FROM PetitorioTecnico p WHERE p.padre IS NULL",
                    PetitorioTecnico.class);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<PetitorioTecnico> findHijos(String padreCodigo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createQuery(
                    "SELECT p FROM PetitorioTecnico p WHERE p.padre = :padre",
                    PetitorioTecnico.class).setParameter("padre", padreCodigo);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public boolean tieneHijos(String codigo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Long count = em.createQuery(
                    "SELECT COUNT(p) FROM PetitorioTecnico p WHERE p.padre = :codigo",
                    Long.class)
                    .setParameter("codigo", codigo)
                    .getSingleResult();
            return count > 0;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    private String getLastRoot() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "SELECT codigo FROM petitorio_tecnico WHERE padre IS NULL ORDER BY CAST(codigo AS DECIMAL(18, 4)) DESC");
            List<String> resultados = query.getResultList();
            return resultados.get(0);
        } catch (Exception e) {
            return null;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    // validar que si es raiz si codigo no tiene "."
    private boolean validarSiEsPosible(String codigo) {
        String lastRoot = getLastRoot();
        double lastRootDouble = Double.parseDouble(lastRoot) + 1;
        double codigoDouble = Double.parseDouble(codigo);
        if (lastRootDouble < codigoDouble) {
            return false;
        } else {
            return true;
        }
    }

    public boolean validarFormatoCodigo(String codigo) {

        EntityManager em = null;
        try {
            em = getEntityManager();
            if (validarSiEsPosible(codigo)) {
                Query query = em.createNativeQuery("SELECT COUNT(*) FROM petitorio_tecnico WHERE codigo = ?");
                query.setParameter(1, codigo);
                Long count = (Long) query.getSingleResult();
                return count == 0;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void edit(PetitorioTecnico petitorioTecnico) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            petitorioTecnico = em.merge(petitorioTecnico);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = petitorioTecnico.getCod();
                if (findPetitorioTecnico(id) == null) {
                    throw new NonexistentEntityException("The petitorioTecnico with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PetitorioTecnico petitorioTecnico;
            try {
                petitorioTecnico = em.getReference(PetitorioTecnico.class, id);
                petitorioTecnico.getCod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The petitorioTecnico with id " + id + " no longer exists.", enfe);
            }
            em.remove(petitorioTecnico);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<PetitorioTecnico> findPetitorioTecnicoEntities() {
        return findPetitorioTecnicoEntities(true, -1, -1);
    }

    public List<PetitorioTecnico> findPetitorioTecnicoEntities(int maxResults, int firstResult) {
        return findPetitorioTecnicoEntities(false, maxResults, firstResult);
    }

    private List<PetitorioTecnico> findPetitorioTecnicoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PetitorioTecnico.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public PetitorioTecnico findPetitorioTecnico(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(PetitorioTecnico.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public PetitorioTecnico findPetitorioTecnicoByCodigo(String codigo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createQuery(
                    "SELECT p FROM PetitorioTecnico p WHERE p.codigo = :codigo",
                    PetitorioTecnico.class)
                    .setParameter("codigo", codigo);
            return (PetitorioTecnico) query.getResultList().stream().findFirst().orElse(null);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getPetitorioTecnicoCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PetitorioTecnico> rt = cq.from(PetitorioTecnico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String getPadres() {
        EntityManager em = null;
        JSONArray arrPadres = new JSONArray();
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "SELECT DISTINCT codigo, nombre FROM petitorio_tecnico " +
                            "WHERE (cncntr IS NULL OR cncntr = '') " +
                            "AND (form_farma IS NULL OR form_farma = '') " +
                            "AND (prsntcn IS NULL OR prsntcn = '') " +
                            "AND (codigoProducto IS NULL OR codigoProducto = '')");

            List<Object[]> resultados = query.getResultList();

            for (Object[] item : resultados) {
                JSONObject objPadre = new JSONObject();
                objPadre.put("codigo", item[0]);
                objPadre.put("nombre", item[1]);
                arrPadres.put(objPadre);
            }
            return arrPadres.toString();
        } catch (Exception e) {
            return arrPadres.toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public boolean esDescendiente(String posiblePadre, String posibleHijo) {
        if (posiblePadre == null || posibleHijo == null)
            return false;

        EntityManager em = null;
        try {
            em = getEntityManager();
            PetitorioTecnico item = findPetitorioTecnicoByCodigo(posibleHijo);
            while (item != null && item.getPadre() != null) {
                if (item.getPadre().equals(posiblePadre)) {
                    return true;
                }
                item = findPetitorioTecnicoByCodigo(item.getPadre());
            }
            return false;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String obtenerGenericos() {
        EntityManager em = null;
        JSONArray arrGen = new JSONArray();
        try {
            em = getEntityManager();
            Query q = em.createNativeQuery("select codgen, desgen from fa_genericos where estado ='S'");
            List<Object[]> genericos = q.getResultList();

            for (Object[] objects : genericos) {
                JSONObject objGen = new JSONObject();
                objGen.put("codigo", objects[0]);
                objGen.put("nombre", objects[1]);
                arrGen.put(objGen);
            }
            return arrGen.toString();
        } catch (Exception e) {
            return arrGen.toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public JSONObject getPTWithTieneHijos(String codigo) {
        EntityManager em = null;
        JSONObject objPro = new JSONObject();
        try {
            em = getEntityManager();
            // Consulta compatible con SQL Server usando CAST(1/0 AS BIT) para booleanos
            Query q = em.createNativeQuery(
                    "SELECT cod, codigo, nombre, padre, prsntcn, codigoProducto, " +
                            "CASE WHEN EXISTS (SELECT 1 FROM petitorio_tecnico pt2 WHERE pt2.padre = pt1.codigo) " +
                            "THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END AS tieneHijos " +
                            "FROM petitorio_tecnico pt1 WHERE codigo = ?");

            q.setParameter(1, codigo);

            Object[] result = (Object[]) q.getSingleResult();

            // Mapeo de resultados
            objPro.put("cod", result[0]);
            objPro.put("codigo", result[1]);
            objPro.put("nombre", result[2]);
            objPro.put("padre", result[3]);
            objPro.put("prsntcn", result[4]);
            objPro.put("codigoProducto", result[5]);
            objPro.put("tieneHijos", result[6]); // SQL Server devuelve true/false como BIT

            return objPro;
        } catch (Exception e) {
            return objPro;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String obtenerSiguienteCodigoParaPadre(String codigoPadre) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String queryStr;

            if (codigoPadre == null || codigoPadre.isEmpty()) {
                // Para elementos raíz (sin padre)
                queryStr = "SELECT ISNULL(MAX(CAST(codigo AS INT)), 0) FROM petitorio_tecnico WHERE padre IS NULL OR padre = ''";
            } else {
                // Para elementos con padre - enfoque más directo
                queryStr = "SELECT codigo FROM petitorio_tecnico WHERE padre = ? ORDER BY LEN(codigo), codigo";
            }

            Query query = em.createNativeQuery(queryStr);

            if (codigoPadre != null && !codigoPadre.isEmpty()) {
                query.setParameter(1, codigoPadre);
            }

            int siguienteNumero;

            if (codigoPadre == null || codigoPadre.isEmpty()) {
                // Para elementos raíz
                Integer maxNumero = ((Number) query.getSingleResult()).intValue();
                siguienteNumero = maxNumero + 1;
            } else {
                // Para elementos con padre
                List<String> resultados = query.getResultList();
                siguienteNumero = 1; // Valor por defecto si no hay hijos

                if (!resultados.isEmpty()) {
                    // Extraer el número más alto
                    int maxHijo = 0;
                    for (String codigo : resultados) {
                        // Obtener la última parte del código después del último punto
                        String[] partes = codigo.split("\\.");
                        int ultimaParte = Integer.parseInt(partes[partes.length - 1]);
                        if (ultimaParte > maxHijo) {
                            maxHijo = ultimaParte;
                        }
                    }
                    siguienteNumero = maxHijo + 1;
                }
            }

            return (codigoPadre == null || codigoPadre.isEmpty()) ? String.valueOf(siguienteNumero)
                    : codigoPadre + "." + siguienteNumero;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String obtenerProductos() {
        EntityManager em = null;
        JSONArray arrPro = new JSONArray();
        try {
            em = getEntityManager();
            Query q = em.createNativeQuery(
                    "select p.codpro, p.despro, p.codgen, g.desgen from fa_productos p left join fa_genericos g on g.codgen = p.codgen where p.estado = 'S'");
            List<Object[]> productos = q.getResultList();
            for (Object[] objects : productos) {
                JSONObject objPro = new JSONObject();
                objPro.put("codigo", objects[0]);
                objPro.put("nombre", objects[1]);
                objPro.put("codgen", objects[2]);
                objPro.put("generico", objects[3]);
                arrPro.put(objPro);
            }
            return arrPro.toString();
        } catch (Exception e) {
            return arrPro.toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public Boolean validarProducto(String codigoProducto, String padre) {
        if (codigoProducto == null || codigoProducto.isEmpty()) {
            return false;
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em
                    .createNativeQuery("select * from petitorio_tecnico where codigoProducto = ? and padre = ?");
            query.setParameter(1, codigoProducto);
            query.setParameter(2, padre);
            query.getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    //
    public void actualizarJerarquiaCompleta(String codigoAnterior, String nuevoCodigo) {
        if (codigoAnterior == null || nuevoCodigo == null) {
            throw new IllegalArgumentException("Los códigos no pueden ser nulos");
        }

        // Mapa para rastrear las actualizaciones de código (viejo -> nuevo)
        final ConcurrentHashMap<String, String> mapaActualizaciones = new ConcurrentHashMap<>();

        EntityManager em = null;
        try {
            em = getEntityManager();
            // 1. Primero, obtenemos todos los ítems que necesitan actualización (el nodo y
            // todos sus descendientes)
            // Esta operación no es costosa y nos permite trabajar con un conjunto definido
            List<PetitorioTecnico> itemsAActualizar = obtenerJerarquiaCompleta(codigoAnterior);

            if (itemsAActualizar.isEmpty()) {
                return; // No hay nada que actualizar
            }

            // 2. Organizamos los ítems por niveles para procesar en orden
            Map<Integer, List<PetitorioTecnico>> itemsPorNivel = organizarPorNiveles(itemsAActualizar, codigoAnterior);

            // 3. Actualizamos por niveles, para mantener la integridad de la jerarquía
            EntityTransaction tx = em.getTransaction();
            try {
                tx.begin();

                // Primero actualizamos el nodo raíz
                for (PetitorioTecnico item : itemsPorNivel.getOrDefault(0, Collections.emptyList())) {
                    if (item.getCodigo().equals(codigoAnterior)) {
                        String codigoOriginal = item.getCodigo();
                        item.setCodigo(nuevoCodigo);
                        em.merge(item);
                        mapaActualizaciones.put(codigoOriginal, nuevoCodigo);
                        break;
                    }
                }

                // Procesamos el resto de niveles
                int maxNivel = itemsPorNivel.keySet().stream().max(Integer::compare).orElse(0);
                for (int nivel = 1; nivel <= maxNivel; nivel++) {
                    final List<PetitorioTecnico> itemsNivel = itemsPorNivel.getOrDefault(nivel,
                            Collections.emptyList());
                    if (itemsNivel.isEmpty())
                        continue;

                    // Procesamos por lotes para esta transacción
                    procesarLoteItems(em, itemsNivel, mapaActualizaciones);

                    // Hacemos flush periódicamente para evitar problemas de memoria
                    if (nivel % 3 == 0) {
                        em.flush();
                    }
                }

                tx.commit();
            } catch (Exception e) {
                if (tx != null && tx.isActive()) {
                    tx.rollback();
                }
                throw new RuntimeException("Error al actualizar jerarquía: " + e.getMessage(), e);
            }

        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    /**
     * Obtiene todos los ítems en la jerarquía de un nodo, incluyendo el nodo mismo.
     */
    private List<PetitorioTecnico> obtenerJerarquiaCompleta(String codigoRaiz) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            // Primero obtenemos el ítem raíz
            Query queryRaiz = em.createQuery("SELECT p FROM PetitorioTecnico p WHERE p.codigo = :codigo");
            queryRaiz.setParameter("codigo", codigoRaiz);
            List<PetitorioTecnico> resultados = queryRaiz.getResultList();

            if (resultados.isEmpty()) {
                return Collections.emptyList();
            }

            // Luego todos sus descendientes (usamos LIKE para buscar todos los ítems que
            // comienzan con el código o son hijos)
            Query queryHijos;
            if (codigoRaiz.contains(".")) {
                // Si es un código con puntos, necesitamos asegurar que buscamos descendientes
                // correctos
                queryHijos = em.createQuery(
                        "SELECT p FROM PetitorioTecnico p WHERE p.codigo = :exactCode OR " +
                                "(p.codigo LIKE :patron AND (p.padre = :exactCode OR p.padre LIKE :patronPadre))");
                queryHijos.setParameter("exactCode", codigoRaiz);
                queryHijos.setParameter("patron", codigoRaiz + ".%");
                queryHijos.setParameter("patronPadre", codigoRaiz + "%");
            } else {
                // Para códigos simples sin puntos
                queryHijos = em.createQuery(
                        "SELECT p FROM PetitorioTecnico p WHERE p.codigo = :exactCode OR " +
                                "p.codigo LIKE :patron OR p.padre = :exactCode");
                queryHijos.setParameter("exactCode", codigoRaiz);
                queryHijos.setParameter("patron", codigoRaiz + ".%");
            }

            List<PetitorioTecnico> todosItems = queryHijos.getResultList();

            // Ordenamos por código para procesar en orden lógico
            todosItems.sort(Comparator.comparing(PetitorioTecnico::getCodigo));

            return todosItems;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    /**
     * Organiza los ítems por nivel jerárquico basado en la estructura de su código.
     */
    private Map<Integer, List<PetitorioTecnico>> organizarPorNiveles(List<PetitorioTecnico> items, String codigoRaiz) {
        Map<Integer, List<PetitorioTecnico>> itemsPorNivel = new HashMap<>();

        for (PetitorioTecnico item : items) {
            int nivel;

            if (item.getCodigo().equals(codigoRaiz)) {
                nivel = 0; // El nodo raíz siempre es nivel 0
            } else {
                // Calculamos el nivel basado en la diferencia entre su código y el código raíz
                String codigo = item.getCodigo();
                nivel = contarPuntos(codigo) - contarPuntos(codigoRaiz) + (codigo.startsWith(codigoRaiz) ? 0 : 1);

                if (nivel < 0)
                    nivel = 1; // Ajuste de seguridad
            }

            // Agregamos el ítem al nivel correspondiente
            if (!itemsPorNivel.containsKey(nivel)) {
                itemsPorNivel.put(nivel, new ArrayList<>());
            }
            itemsPorNivel.get(nivel).add(item);
        }

        return itemsPorNivel;
    }

    /**
     * Cuenta el número de puntos en un código para determinar su nivel jerárquico.
     */
    private int contarPuntos(String codigo) {
        if (codigo == null)
            return 0;
        return (int) codigo.chars().filter(c -> c == '.').count();
    }

    /**
     * Procesa un lote de ítems, actualizando sus códigos según la jerarquía.
     */
    private void procesarLoteItems(EntityManager em, List<PetitorioTecnico> items,
            ConcurrentHashMap<String, String> mapaActualizaciones) {
        for (PetitorioTecnico item : items) {
            String codigoPadreOriginal = item.getPadre();
            String codigoOriginal = item.getCodigo();

            // Si el padre ha sido actualizado, usamos el nuevo código del padre
            String nuevoPadre = mapaActualizaciones.getOrDefault(codigoPadreOriginal, codigoPadreOriginal);

            // Calculamos el nuevo código basado en el código del padre
            String nuevoCodigo = calcularNuevoCodigo(codigoOriginal, codigoPadreOriginal, nuevoPadre);

            // Actualizamos el ítem
            item.setPadre(nuevoPadre);
            item.setCodigo(nuevoCodigo);
            em.merge(item);

            // Guardamos la actualización en el mapa
            mapaActualizaciones.put(codigoOriginal, nuevoCodigo);
        }
    }

    /**
     * Calcula el nuevo código para un ítem basado en su código actual y los cambios
     * del padre.
     */
    private String calcularNuevoCodigo(String codigoActual, String padrePrevio, String nuevoPadre) {
        // Si no hay padre previo o nuevo, el código no cambia (es un nodo raíz)
        if ((padrePrevio == null || padrePrevio.isEmpty()) && (nuevoPadre == null || nuevoPadre.isEmpty())) {
            return codigoActual;
        }

        // Si el padre no ha cambiado, el código tampoco cambia
        if (Objects.equals(padrePrevio, nuevoPadre)) {
            return codigoActual;
        }

        // Si el código actual es un hijo directo del padre previo
        if (padrePrevio != null && !padrePrevio.isEmpty() && codigoActual.startsWith(padrePrevio)) {
            String sufijo = codigoActual.substring(padrePrevio.length());
            // Si el sufijo comienza con un punto, lo mantenemos
            if (sufijo.startsWith(".")) {
                return nuevoPadre + sufijo;
            } else {
                // Caso especial: el sufijo no comienza con un punto (quizás sea un ítem raíz)
                return nuevoPadre + "." + sufijo;
            }
        }

        // Si el código actual tiene una estructura de puntos, mantenemos esa estructura
        if (codigoActual.contains(".")) {
            // Extraemos el último segmento del código actual
            String ultimoSegmento = codigoActual.substring(codigoActual.lastIndexOf('.') + 1);
            return nuevoPadre + "." + ultimoSegmento;
        }

        // Para códigos simples, agregamos como hijo directo
        return nuevoPadre + "." + codigoActual;
    }

}