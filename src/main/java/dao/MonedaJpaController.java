package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.Moneda;

/**
 *
 * @author USUARIO
 */
public class MonedaJpaController extends JpaPadre {

    public MonedaJpaController(String empresa) {
        super(empresa);
    }

    public String listarJson() {
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            Query query = em.createNamedQuery("Moneda.findAllJSON");
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("moncod", fila[0]);
                jsonObj.put("mondes", fila[1]);
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

    public void create(Moneda moneda) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(moneda);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMoneda(moneda.getMoncod()) != null) {
                throw new PreexistingEntityException("Moneda " + moneda + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(Moneda moneda) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            moneda = em.merge(moneda);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = moneda.getMoncod();
                if (findMoneda(id) == null) {
                    throw new NonexistentEntityException("The moneda with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Moneda moneda;
            try {
                moneda = em.getReference(Moneda.class, id);
                moneda.getMoncod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The moneda with id " + id + " no longer exists.", enfe);
            }
            em.remove(moneda);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<Moneda> findMonedaEntities() {
        return findMonedaEntities(true, -1, -1);
    }

    public List<Moneda> findMonedaEntities(int maxResults, int firstResult) {
        return findMonedaEntities(false, maxResults, firstResult);
    }

    private List<Moneda> findMonedaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Moneda.class));
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

    public Moneda findMoneda(String id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(Moneda.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getMonedaCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Moneda> rt = cq.from(Moneda.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
