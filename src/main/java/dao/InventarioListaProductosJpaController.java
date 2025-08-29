package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.exceptions.NonexistentEntityException;
import dto.InventarioListaProductos;

/**
 *
 * @author USUARIO
 */
public class InventarioListaProductosJpaController extends JpaPadre {

    public InventarioListaProductosJpaController(String empresa) {
        super(empresa);
    }

    public void create(InventarioListaProductos inventarioListaProductos) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(inventarioListaProductos);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // Verificar si es error de duplicado (depende del driver SQL Server)
            if (ex.getMessage().contains("UNIQUE") || ex.getMessage().contains("duplicate")) {
                throw new Exception("El producto ya está asociado a este inventario");
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public boolean createOrSkip(InventarioListaProductos inventarioListaProductos) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            // Verificación y creación en una sola transacción
            String sql = "INSERT INTO inventario_lista_productos (codinvalm, codpro) " +
                    "SELECT ?1, ?2 " +
                    "WHERE NOT EXISTS (SELECT 1 FROM inventario_lista_productos " +
                    "WHERE codpro = ?2 AND codinvalm = ?1)";

            Query query = em.createNativeQuery(sql);
            query.setParameter(1, inventarioListaProductos.getCodinvalm());
            query.setParameter(2, inventarioListaProductos.getCodpro());

            int affectedRows = query.executeUpdate();
            em.getTransaction().commit();

            return affectedRows > 0;
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(InventarioListaProductos inventarioListaProductos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            inventarioListaProductos = em.merge(inventarioListaProductos);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = inventarioListaProductos.getCodinvlis();
                if (findInventarioListaProductos(id) == null) {
                    throw new NonexistentEntityException(
                            "The inventarioListaProductos with id " + id + " no longer exists.");
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
            InventarioListaProductos inventarioListaProductos;
            try {
                inventarioListaProductos = em.getReference(InventarioListaProductos.class, id);
                inventarioListaProductos.getCodinvlis();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException(
                        "The inventarioListaProductos with id " + id + " no longer exists.", enfe);
            }
            em.remove(inventarioListaProductos);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<InventarioListaProductos> findInventarioListaProductosEntities() {
        return findInventarioListaProductosEntities(true, -1, -1);
    }

    public List<InventarioListaProductos> findInventarioListaProductosEntities(int maxResults, int firstResult) {
        return findInventarioListaProductosEntities(false, maxResults, firstResult);
    }

    private List<InventarioListaProductos> findInventarioListaProductosEntities(boolean all, int maxResults,
            int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(InventarioListaProductos.class));
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

    public String findProductosConNombreByInventario(int codinvcab) {
        EntityManager em = null;
        JSONArray jsonArray = new JSONArray();

        try {
            em = getEntityManager();
            String sql = "SELECT ivp.codpro, p.despro FROM inventario_lista_productos ivp "
                    + "LEFT JOIN fa_productos p ON ivp.codpro = p.codpro "
                    + "WHERE ivp.codinvalm= ?";

            Query query = em.createNativeQuery(sql);
            query.setParameter(1, codinvcab);

            List<Object[]> resultados = query.getResultList();

            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codigo", fila[0]);
                jsonObj.put("nombre", fila[1]);
                jsonArray.put(jsonObj);
            }

            return jsonArray.toString();

        } catch (Exception e) {
            return jsonArray.toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public boolean isInventarioDireccionado(int codinv) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sql = "SELECT direccionado FROM inventario WHERE codinv = ?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, codinv);

            // Ejecutar consulta y obtener el resultado
            Object result = query.getSingleResult();

            // Convertir el resultado BIT (1/0) a boolean
            if (result instanceof Number) {
                return ((Number) result).intValue() == 1;
            } else if (result instanceof Boolean) {
                return (Boolean) result;
            }
            return false;

        } catch (NoResultException e) {
            return false; // Si no encuentra registro, asumir false
        } catch (Exception e) {
            e.printStackTrace();
            return false; // En caso de error, retornar false
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public InventarioListaProductos findInventarioListaProductos(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(InventarioListaProductos.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getInventarioListaProductosCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<InventarioListaProductos> rt = cq.from(InventarioListaProductos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    // Metodo para validar que el duo codinvalm y codpro exista en la tabla
    public boolean existeDuo(String codpro, int codinvalm) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sql = "SELECT COUNT(*) FROM inventario_lista_productos WHERE codpro = ? AND codinvalm = ?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, codpro);
            query.setParameter(2, codinvalm);

            // Ejecutar consulta y obtener el resultado
            Object result = query.getSingleResult();

            // Convertir el resultado BIT (1/0) a boolean
            if (result instanceof Number) {
                return ((Number) result).intValue() > 0;
            } else if (result instanceof Boolean) {
                return (Boolean) result;
            }
            return false;

        } catch (NoResultException e) {
            return false; // Si no encuentra registro, asumir false
        } catch (Exception e) {
            e.printStackTrace();
            return false; // En caso de error, retornar false
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
