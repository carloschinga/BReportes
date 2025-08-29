package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.AlmacenesBartolitoDetalle;
import dto.AlmacenesBartolitoDetallePK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author USUARIO
 */
public class AlmacenesBartolitoDetalleJpaController extends JpaPadre {

    public AlmacenesBartolitoDetalleJpaController(String empresa) {
        super(empresa);
    }

    public void create(AlmacenesBartolitoDetalle almacenesBartolitoDetalle)
            throws PreexistingEntityException, Exception {
        if (almacenesBartolitoDetalle.getAlmacenesBartolitoDetallePK() == null) {
            almacenesBartolitoDetalle.setAlmacenesBartolitoDetallePK(new AlmacenesBartolitoDetallePK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(almacenesBartolitoDetalle);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAlmacenesBartolitoDetalle(almacenesBartolitoDetalle.getAlmacenesBartolitoDetallePK()) != null) {
                throw new PreexistingEntityException(
                        "AlmacenesBartolitoDetalle " + almacenesBartolitoDetalle + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(AlmacenesBartolitoDetalle almacenesBartolitoDetalle) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            almacenesBartolitoDetalle = em.merge(almacenesBartolitoDetalle);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                AlmacenesBartolitoDetallePK id = almacenesBartolitoDetalle.getAlmacenesBartolitoDetallePK();
                if (findAlmacenesBartolitoDetalle(id) == null) {
                    throw new NonexistentEntityException(
                            "The almacenesBartolitoDetalle with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(AlmacenesBartolitoDetallePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AlmacenesBartolitoDetalle almacenesBartolitoDetalle;
            try {
                almacenesBartolitoDetalle = em.getReference(AlmacenesBartolitoDetalle.class, id);
                almacenesBartolitoDetalle.getAlmacenesBartolitoDetallePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException(
                        "The almacenesBartolitoDetalle with id " + id + " no longer exists.", enfe);
            }
            em.remove(almacenesBartolitoDetalle);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<AlmacenesBartolitoDetalle> findAlmacenesBartolitoDetalleEntities() {
        return findAlmacenesBartolitoDetalleEntities(true, -1, -1);
    }

    public List<AlmacenesBartolitoDetalle> findAlmacenesBartolitoDetalleEntities(int maxResults, int firstResult) {
        return findAlmacenesBartolitoDetalleEntities(false, maxResults, firstResult);
    }

    private List<AlmacenesBartolitoDetalle> findAlmacenesBartolitoDetalleEntities(boolean all, int maxResults,
            int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AlmacenesBartolitoDetalle.class));
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

    public AlmacenesBartolitoDetalle findAlmacenesBartolitoDetalle(AlmacenesBartolitoDetallePK id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(AlmacenesBartolitoDetalle.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getAlmacenesBartolitoDetalleCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AlmacenesBartolitoDetalle> rt = cq.from(AlmacenesBartolitoDetalle.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String buscarDetallePorLoteAlmacenProducto(String lote, String codalm, String codpro) {
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            // Definir la consulta SQL nativa
            String sql = "SELECT * FROM almacenes_bartolito_detalle "
                    + "WHERE lote = ? "
                    + "AND codalm = ? "
                    + "AND codpro = ? "
                    + "AND (cante <> 0 OR cantf <> 0)";

            // Crear la consulta
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, lote); // Parámetro 1: lote
            query.setParameter(2, codalm); // Parámetro 2: codalm
            query.setParameter(3, codpro); // Parámetro 3: codpro

            // Ejecutar la consulta y obtener los resultados
            List<Object[]> resultados = query.getResultList();

            // Convertir los resultados a JSON
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codalm", fila[0]); // codalm
                jsonObj.put("codpro", fila[1]); // codpro
                jsonObj.put("ubicacion", fila[2]); // ubicacion
                jsonObj.put("lote", fila[3]); // lote
                jsonObj.put("cante", fila[4]); // cante
                jsonObj.put("cantf", fila[5]); // cantf
                jsonArray.put(jsonObj);
            }

            // Devolver el JSON como una cadena
            return jsonArray.toString();
        } catch (Exception e) {
            // Manejar cualquier excepción y devolver un mensaje de error
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String buscarAllDetallePorLoteAlmacenProducto(String lote, String codalm, String codpro) {
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            // Definir la consulta SQL nativa
            String sql = "SELECT * FROM almacenes_bartolito_detalle "
                    + "WHERE lote = ? "
                    + "AND codalm = ? "
                    + "AND codpro = ? ";

            // Crear la consulta
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, lote); // Parámetro 1: lote
            query.setParameter(2, codalm); // Parámetro 2: codalm
            query.setParameter(3, codpro); // Parámetro 3: codpro

            // Ejecutar la consulta y obtener los resultados
            List<Object[]> resultados = query.getResultList();

            // Convertir los resultados a JSON
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codalm", fila[0]); // codalm
                jsonObj.put("codpro", fila[1]); // codpro
                jsonObj.put("ubicacion", fila[2]); // ubicacion
                jsonObj.put("lote", fila[3]); // lote
                jsonObj.put("cante", fila[4]); // cante
                jsonObj.put("cantf", fila[5]); // cantf
                jsonArray.put(jsonObj);
            }

            // Devolver el JSON como una cadena
            return jsonArray.toString();
        } catch (Exception e) {
            // Manejar cualquier excepción y devolver un mensaje de error
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public AlmacenesBartolitoDetalle buscarOneDetallePorLoteAlmacenProducto(String lote, String codalm, String codpro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String jpql = "SELECT d FROM AlmacenesBartolitoDetalle d "
                    + "WHERE d.almacenesBartolitoDetallePK.lote = :lote "
                    + "AND d.almacenesBartolitoDetallePK.codalm = :codalm "
                    + "AND d.almacenesBartolitoDetallePK.codpro = :codpro "
                    + "AND (d.cante <> 0 OR d.cantf <> 0)";

            List<AlmacenesBartolitoDetalle> resultados = em.createQuery(jpql, AlmacenesBartolitoDetalle.class)
                    .setParameter("lote", lote)
                    .setParameter("codalm", codalm)
                    .setParameter("codpro", codpro)
                    .setMaxResults(1)
                    .getResultList();

            return resultados.isEmpty() ? null : resultados.get(0);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public AlmacenesBartolitoDetalle buscarOneuDetallePorLoteAlmacenProducto(String lote, String codalm,
            String codpro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String jpql = "SELECT d FROM AlmacenesBartolitoDetalle d "
                    + "WHERE d.almacenesBartolitoDetallePK.lote = :lote "
                    + "AND d.almacenesBartolitoDetallePK.codalm = :codalm "
                    + "AND d.almacenesBartolitoDetallePK.codpro = :codpro ";

            List<AlmacenesBartolitoDetalle> resultados = em.createQuery(jpql, AlmacenesBartolitoDetalle.class)
                    .setParameter("lote", lote)
                    .setParameter("codalm", codalm)
                    .setParameter("codpro", codpro)
                    .setMaxResults(1)
                    .getResultList();

            return resultados.isEmpty() ? null : resultados.get(0);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
