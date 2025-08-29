package dao;

import dao.exceptions.NonexistentEntityException;
import dto.AlmacenesBartolito;
import java.io.Serializable;
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
public class AlmacenesBartolitoJpaController extends JpaPadre {

    public AlmacenesBartolitoJpaController(String empresa) {
        super(empresa);
    }

    public String listarAlmacenesJson() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "SELECT codalmbar, descripcion, codalm, m3, num FROM almacenes_bartolito where estado='S' ORDER BY descripcion ASC");
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codalmbar", fila[0]);
                jsonObj.put("descripcion", fila[1]);
                jsonObj.put("codalm", fila[2]);
                jsonObj.put("m3", fila[3]);
                jsonObj.put("num", fila[4]);
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

    public void create(AlmacenesBartolito almacenesBartolito) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(almacenesBartolito);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(AlmacenesBartolito almacenesBartolito) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            almacenesBartolito = em.merge(almacenesBartolito);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = almacenesBartolito.getCodalmbar();
                if (findAlmacenesBartolito(id) == null) {
                    throw new NonexistentEntityException("The almacenesBartolito with id " + id + " no longer exists.");
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
            AlmacenesBartolito almacenesBartolito;
            try {
                almacenesBartolito = em.getReference(AlmacenesBartolito.class, id);
                almacenesBartolito.getCodalmbar();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The almacenesBartolito with id " + id + " no longer exists.",
                        enfe);
            }
            em.remove(almacenesBartolito);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<AlmacenesBartolito> findAlmacenesBartolitoEntities() {
        return findAlmacenesBartolitoEntities(true, -1, -1);
    }

    public List<AlmacenesBartolito> findAlmacenesBartolitoEntities(int maxResults, int firstResult) {
        return findAlmacenesBartolitoEntities(false, maxResults, firstResult);
    }

    private List<AlmacenesBartolito> findAlmacenesBartolitoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AlmacenesBartolito.class));
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

    public AlmacenesBartolito findAlmacenesBartolito(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(AlmacenesBartolito.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getAlmacenesBartolitoCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AlmacenesBartolito> rt = cq.from(AlmacenesBartolito.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
