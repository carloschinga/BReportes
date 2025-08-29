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
import dto.FaUnidadMedida;

/**
 *
 * @author USUARIO
 */
public class FaUnidadMedidaJpaController extends JpaPadre {

    public FaUnidadMedidaJpaController(String empresa) {
        super(empresa);
    }

    public String listarJson() {
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            Query query = em.createNamedQuery("FaUnidadMedida.findAllJSON");
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codund", fila[0]);
                jsonObj.put("desund", fila[1]);
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

    public void create(FaUnidadMedida faUnidadMedida) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(faUnidadMedida);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFaUnidadMedida(faUnidadMedida.getCodund()) != null) {
                throw new PreexistingEntityException("FaUnidadMedida " + faUnidadMedida + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(FaUnidadMedida faUnidadMedida) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            faUnidadMedida = em.merge(faUnidadMedida);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = faUnidadMedida.getCodund();
                if (findFaUnidadMedida(id) == null) {
                    throw new NonexistentEntityException("The faUnidadMedida with id " + id + " no longer exists.");
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
            FaUnidadMedida faUnidadMedida;
            try {
                faUnidadMedida = em.getReference(FaUnidadMedida.class, id);
                faUnidadMedida.getCodund();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The faUnidadMedida with id " + id + " no longer exists.", enfe);
            }
            em.remove(faUnidadMedida);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<FaUnidadMedida> findFaUnidadMedidaEntities() {
        return findFaUnidadMedidaEntities(true, -1, -1);
    }

    public List<FaUnidadMedida> findFaUnidadMedidaEntities(int maxResults, int firstResult) {
        return findFaUnidadMedidaEntities(false, maxResults, firstResult);
    }

    private List<FaUnidadMedida> findFaUnidadMedidaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FaUnidadMedida.class));
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

    public FaUnidadMedida findFaUnidadMedida(String id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(FaUnidadMedida.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getFaUnidadMedidaCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FaUnidadMedida> rt = cq.from(FaUnidadMedida.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
