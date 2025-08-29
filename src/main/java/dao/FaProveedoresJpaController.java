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
import dto.FaProveedores;

/**
 *
 * @author USUARIO
 */
public class FaProveedoresJpaController extends JpaPadre {

    public FaProveedoresJpaController(String empresa) {
        super(empresa);
    }

    public String listarJson() {
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            Query query = em.createNamedQuery("FaProveedores.findAllJSON");
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codprv", fila[0]);
                jsonObj.put("desprv", fila[1]);
                jsonObj.put("dpaprv", fila[2]);
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

    public void create(FaProveedores faProveedores) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(faProveedores);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFaProveedores(faProveedores.getCodprv()) != null) {
                throw new PreexistingEntityException("FaProveedores " + faProveedores + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(FaProveedores faProveedores) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            faProveedores = em.merge(faProveedores);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = faProveedores.getCodprv();
                if (findFaProveedores(id) == null) {
                    throw new NonexistentEntityException("The faProveedores with id " + id + " no longer exists.");
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
            FaProveedores faProveedores;
            try {
                faProveedores = em.getReference(FaProveedores.class, id);
                faProveedores.getCodprv();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The faProveedores with id " + id + " no longer exists.", enfe);
            }
            em.remove(faProveedores);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<FaProveedores> findFaProveedoresEntities() {
        return findFaProveedoresEntities(true, -1, -1);
    }

    public List<FaProveedores> findFaProveedoresEntities(int maxResults, int firstResult) {
        return findFaProveedoresEntities(false, maxResults, firstResult);
    }

    private List<FaProveedores> findFaProveedoresEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();

            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FaProveedores.class));
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

    public FaProveedores findFaProveedores(String id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(FaProveedores.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getFaProveedoresCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();

            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FaProveedores> rt = cq.from(FaProveedores.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
