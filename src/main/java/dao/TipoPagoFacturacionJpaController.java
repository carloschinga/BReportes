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
import dto.TipoPagoFacturacion;

/**
 *
 * @author USUARIO
 */
public class TipoPagoFacturacionJpaController extends JpaPadre {

    public TipoPagoFacturacionJpaController(String empresa) {
        super(empresa);
    }

    public String listarJson() {
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            Query query = em.createNamedQuery("TipoPagoFacturacion.findAllJSON");
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("tpacod", fila[0]);
                jsonObj.put("tpades", fila[1]);
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

    public void create(TipoPagoFacturacion tipoPagoFacturacion) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tipoPagoFacturacion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipoPagoFacturacion(tipoPagoFacturacion.getTpacod()) != null) {
                throw new PreexistingEntityException("TipoPagoFacturacion " + tipoPagoFacturacion + " already exists.",
                        ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(TipoPagoFacturacion tipoPagoFacturacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tipoPagoFacturacion = em.merge(tipoPagoFacturacion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = tipoPagoFacturacion.getTpacod();
                if (findTipoPagoFacturacion(id) == null) {
                    throw new NonexistentEntityException(
                            "The tipoPagoFacturacion with id " + id + " no longer exists.");
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
            TipoPagoFacturacion tipoPagoFacturacion;
            try {
                tipoPagoFacturacion = em.getReference(TipoPagoFacturacion.class, id);
                tipoPagoFacturacion.getTpacod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoPagoFacturacion with id " + id + " no longer exists.",
                        enfe);
            }
            em.remove(tipoPagoFacturacion);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<TipoPagoFacturacion> findTipoPagoFacturacionEntities() {
        return findTipoPagoFacturacionEntities(true, -1, -1);
    }

    public List<TipoPagoFacturacion> findTipoPagoFacturacionEntities(int maxResults, int firstResult) {
        return findTipoPagoFacturacionEntities(false, maxResults, firstResult);
    }

    private List<TipoPagoFacturacion> findTipoPagoFacturacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoPagoFacturacion.class));
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

    public TipoPagoFacturacion findTipoPagoFacturacion(String id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(TipoPagoFacturacion.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getTipoPagoFacturacionCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoPagoFacturacion> rt = cq.from(TipoPagoFacturacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
