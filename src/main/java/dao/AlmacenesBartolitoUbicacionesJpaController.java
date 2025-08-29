package dao;

import dao.exceptions.NonexistentEntityException;
import dto.AlmacenesBartolitoUbicaciones;
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
public class AlmacenesBartolitoUbicacionesJpaController extends JpaPadre {

    public AlmacenesBartolitoUbicacionesJpaController(String empresa) {
        super(empresa);
    }

    public String listarUbicacionesJson(int codalmbar) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "SELECT u.codubi, u.codigo, u.codbar, u.rotacion, u.m3, COALESCE(SUM(precio_producto), 0) AS balance_precio FROM almacenes_bartolito_ubicaciones u inner join almacenes_bartolito c on c.codalmbar=u.codalmbar LEFT JOIN (SELECT d.ubicacion,d.codalm, SUM((p.cospro * ((d.cante * p.stkfra) + d.cantf)) / p.stkfra) AS precio_producto FROM almacenes_bartolito_detalle d INNER JOIN fa_productos p ON p.codpro = d.codpro GROUP BY d.ubicacion, d.codpro,d.codalm) sub ON sub.ubicacion = u.codigo and sub.codalm=c.codalm WHERE u.estado = 'S' AND u.codalmbar = ? GROUP BY u.codubi, u.codigo, u.codbar, u.rotacion, u.m3 ORDER BY u.codigo ASC;");
            query.setParameter(1, codalmbar);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();

            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codubi", fila[0]); // ID de la ubicación
                jsonObj.put("codigo", fila[1]); // Código de ubicación
                jsonObj.put("codbar", fila[2]); // Código de barras
                jsonObj.put("rotacion", fila[3]); // Rotación
                jsonObj.put("m3", fila[4]); // M3
                jsonObj.put("balance", fila[5]); // M3
                jsonArray.put(jsonObj);
            }

            return jsonArray.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void create(AlmacenesBartolitoUbicaciones almacenesBartolitoUbicaciones) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(almacenesBartolitoUbicaciones);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(AlmacenesBartolitoUbicaciones almacenesBartolitoUbicaciones)
            throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            almacenesBartolitoUbicaciones = em.merge(almacenesBartolitoUbicaciones);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = almacenesBartolitoUbicaciones.getCodubi();
                if (findAlmacenesBartolitoUbicaciones(id) == null) {
                    throw new NonexistentEntityException(
                            "The almacenesBartolitoUbicaciones with id " + id + " no longer exists.");
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
            AlmacenesBartolitoUbicaciones almacenesBartolitoUbicaciones;
            try {
                almacenesBartolitoUbicaciones = em.getReference(AlmacenesBartolitoUbicaciones.class, id);
                almacenesBartolitoUbicaciones.getCodubi();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException(
                        "The almacenesBartolitoUbicaciones with id " + id + " no longer exists.", enfe);
            }
            em.remove(almacenesBartolitoUbicaciones);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<AlmacenesBartolitoUbicaciones> findAlmacenesBartolitoUbicacionesEntities() {
        return findAlmacenesBartolitoUbicacionesEntities(true, -1, -1);
    }

    public List<AlmacenesBartolitoUbicaciones> findAlmacenesBartolitoUbicacionesEntities(int maxResults,
            int firstResult) {
        return findAlmacenesBartolitoUbicacionesEntities(false, maxResults, firstResult);
    }

    private List<AlmacenesBartolitoUbicaciones> findAlmacenesBartolitoUbicacionesEntities(boolean all, int maxResults,
            int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AlmacenesBartolitoUbicaciones.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public AlmacenesBartolitoUbicaciones findAlmacenesBartolitoUbicaciones(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(AlmacenesBartolitoUbicaciones.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getAlmacenesBartolitoUbicacionesCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AlmacenesBartolitoUbicaciones> rt = cq.from(AlmacenesBartolitoUbicaciones.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
