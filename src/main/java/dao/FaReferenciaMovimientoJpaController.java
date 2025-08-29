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
import dto.FaReferenciaMovimiento;

/**
 *
 * @author USUARIO
 */
public class FaReferenciaMovimientoJpaController extends JpaPadre {

    public FaReferenciaMovimientoJpaController(String empresa) {
        super(empresa);
    }

    public String listarJson() {
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();

            Query query = em.createNamedQuery("FaReferenciaMovimiento.findAllJSON");
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codrefmov", fila[0]);
                jsonObj.put("desrefmov", fila[1]);
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

    public void create(FaReferenciaMovimiento faReferenciaMovimiento) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(faReferenciaMovimiento);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFaReferenciaMovimiento(faReferenciaMovimiento.getCodrefmov()) != null) {
                throw new PreexistingEntityException(
                        "FaReferenciaMovimiento " + faReferenciaMovimiento + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(FaReferenciaMovimiento faReferenciaMovimiento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            faReferenciaMovimiento = em.merge(faReferenciaMovimiento);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = faReferenciaMovimiento.getCodrefmov();
                if (findFaReferenciaMovimiento(id) == null) {
                    throw new NonexistentEntityException(
                            "The faReferenciaMovimiento with id " + id + " no longer exists.");
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
            FaReferenciaMovimiento faReferenciaMovimiento;
            try {
                faReferenciaMovimiento = em.getReference(FaReferenciaMovimiento.class, id);
                faReferenciaMovimiento.getCodrefmov();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The faReferenciaMovimiento with id " + id + " no longer exists.",
                        enfe);
            }
            em.remove(faReferenciaMovimiento);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<FaReferenciaMovimiento> findFaReferenciaMovimientoEntities() {
        return findFaReferenciaMovimientoEntities(true, -1, -1);
    }

    public List<FaReferenciaMovimiento> findFaReferenciaMovimientoEntities(int maxResults, int firstResult) {
        return findFaReferenciaMovimientoEntities(false, maxResults, firstResult);
    }

    private List<FaReferenciaMovimiento> findFaReferenciaMovimientoEntities(boolean all, int maxResults,
            int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();

            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FaReferenciaMovimiento.class));
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

    public FaReferenciaMovimiento findFaReferenciaMovimiento(String id) {
        EntityManager em = null;
        try {
            em = getEntityManager();

            return em.find(FaReferenciaMovimiento.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getFaReferenciaMovimientoCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FaReferenciaMovimiento> rt = cq.from(FaReferenciaMovimiento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
