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
import dto.MotivoTraslado;

/**
 *
 * @author USUARIO
 */
public class MotivoTrasladoJpaController extends JpaPadre {

    public MotivoTrasladoJpaController(String empresa) {
        super(empresa);
    }

    public String listarJson() {
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            Query query = em.createNamedQuery("MotivoTraslado.findAllJSON");
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("desmot", fila[0]);
                jsonObj.put("codmot", fila[1]);
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

    public void create(MotivoTraslado motivoTraslado) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(motivoTraslado);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMotivoTraslado(motivoTraslado.getCodmot()) != null) {
                throw new PreexistingEntityException("MotivoTraslado " + motivoTraslado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(MotivoTraslado motivoTraslado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            motivoTraslado = em.merge(motivoTraslado);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = motivoTraslado.getCodmot();
                if (findMotivoTraslado(id) == null) {
                    throw new NonexistentEntityException("The motivoTraslado with id " + id + " no longer exists.");
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
            MotivoTraslado motivoTraslado;
            try {
                motivoTraslado = em.getReference(MotivoTraslado.class, id);
                motivoTraslado.getCodmot();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The motivoTraslado with id " + id + " no longer exists.", enfe);
            }
            em.remove(motivoTraslado);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<MotivoTraslado> findMotivoTrasladoEntities() {
        return findMotivoTrasladoEntities(true, -1, -1);
    }

    public List<MotivoTraslado> findMotivoTrasladoEntities(int maxResults, int firstResult) {
        return findMotivoTrasladoEntities(false, maxResults, firstResult);
    }

    private List<MotivoTraslado> findMotivoTrasladoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MotivoTraslado.class));
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

    public MotivoTraslado findMotivoTraslado(String id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(MotivoTraslado.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getMotivoTrasladoCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MotivoTraslado> rt = cq.from(MotivoTraslado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
