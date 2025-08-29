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
import dto.FaTransportistas;

/**
 *
 * @author USUARIO
 */
public class FaTransportistasJpaController extends JpaPadre {

    public FaTransportistasJpaController(String empresa) {
        super(empresa);
    }

    public String listarJson() {
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            Query query = em.createNamedQuery("FaTransportistas.findAllJSON");
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codtrans", fila[0]);
                jsonObj.put("namtrans", fila[1]);
                jsonObj.put("placavehic", fila[2]);
                jsonObj.put("pattrans", fila[3]);
                jsonObj.put("mattrans", fila[4]);
                jsonObj.put("nomtrans", fila[5]);
                jsonObj.put("codmodt", fila[6]);
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

    public void create(FaTransportistas faTransportistas) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(faTransportistas);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFaTransportistas(faTransportistas.getCodtrans()) != null) {
                throw new PreexistingEntityException("FaTransportistas " + faTransportistas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(FaTransportistas faTransportistas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            faTransportistas = em.merge(faTransportistas);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = faTransportistas.getCodtrans();
                if (findFaTransportistas(id) == null) {
                    throw new NonexistentEntityException("The faTransportistas with id " + id + " no longer exists.");
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
            FaTransportistas faTransportistas;
            try {
                faTransportistas = em.getReference(FaTransportistas.class, id);
                faTransportistas.getCodtrans();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The faTransportistas with id " + id + " no longer exists.", enfe);
            }
            em.remove(faTransportistas);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<FaTransportistas> findFaTransportistasEntities() {
        return findFaTransportistasEntities(true, -1, -1);
    }

    public List<FaTransportistas> findFaTransportistasEntities(int maxResults, int firstResult) {
        return findFaTransportistasEntities(false, maxResults, firstResult);
    }

    private List<FaTransportistas> findFaTransportistasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FaTransportistas.class));
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

    public FaTransportistas findFaTransportistas(String id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(FaTransportistas.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getFaTransportistasCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FaTransportistas> rt = cq.from(FaTransportistas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
