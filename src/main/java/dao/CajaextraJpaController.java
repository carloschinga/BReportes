
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
import dto.Cajaextra;
import dto.CajaextraPK;

/**
 *
 * @author USUARIO
 */
public class CajaextraJpaController extends JpaPadre {

    public CajaextraJpaController(String empresa) {
        super(empresa);
    }

    public String listardetJson(String caja) {
        EntityManager em = null;
        JSONArray jsonArray = new JSONArray();
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "SELECT c.caja, c.codpro, c.cante, c.cantf, p.despro, p.stkfra "
                            + "FROM cajaextra c "
                            + "INNER JOIN fa_productos p ON p.codpro = c.codpro "
                            + "WHERE c.estado = 'S' AND c.caja = ?");
            query.setParameter(1, caja);

            List<Object[]> resultados = query.getResultList();

            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("caja", fila[0]);
                jsonObj.put("codpro", fila[1]);
                jsonObj.put("cante", fila[2]);
                jsonObj.put("cantf", fila[3]);
                jsonObj.put("despro", fila[4]);
                jsonObj.put("stkfra", fila[5]);
                jsonArray.put(jsonObj);
            }

        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
        return jsonArray.toString();
    }

    public void create(Cajaextra cajaextra) throws PreexistingEntityException, Exception {
        if (cajaextra.getCajaextraPK() == null) {
            cajaextra.setCajaextraPK(new CajaextraPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(cajaextra);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCajaextra(cajaextra.getCajaextraPK()) != null) {
                throw new PreexistingEntityException("Cajaextra " + cajaextra + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(Cajaextra cajaextra) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            cajaextra = em.merge(cajaextra);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                CajaextraPK id = cajaextra.getCajaextraPK();
                if (findCajaextra(id) == null) {
                    throw new NonexistentEntityException("The cajaextra with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(CajaextraPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cajaextra cajaextra;
            try {
                cajaextra = em.getReference(Cajaextra.class, id);
                cajaextra.getCajaextraPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cajaextra with id " + id + " no longer exists.", enfe);
            }
            em.remove(cajaextra);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<Cajaextra> findCajaextraEntities() {
        return findCajaextraEntities(true, -1, -1);
    }

    public List<Cajaextra> findCajaextraEntities(int maxResults, int firstResult) {
        return findCajaextraEntities(false, maxResults, firstResult);
    }

    private List<Cajaextra> findCajaextraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cajaextra.class));
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

    public Cajaextra findCajaextra(CajaextraPK id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(Cajaextra.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getCajaextraCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cajaextra> rt = cq.from(Cajaextra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
