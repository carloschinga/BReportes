package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.HechCuotaVentaMes;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author USUARIO
 */
public class HechCuotaVentaMesJpaController extends JpaPadre {

    public HechCuotaVentaMesJpaController(String empresa) {
        super(empresa);
    }

    public void create(HechCuotaVentaMes hechCuotaVentaMes) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(hechCuotaVentaMes);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHechCuotaVentaMes(hechCuotaVentaMes.getCuotVtaId()) != null) {
                throw new PreexistingEntityException("HechCuotaVentaMes " + hechCuotaVentaMes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(HechCuotaVentaMes hechCuotaVentaMes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            hechCuotaVentaMes = em.merge(hechCuotaVentaMes);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = hechCuotaVentaMes.getCuotVtaId();
                if (findHechCuotaVentaMes(id) == null) {
                    throw new NonexistentEntityException("The hechCuotaVentaMes with id " + id + " no longer exists.");
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
            HechCuotaVentaMes hechCuotaVentaMes;
            try {
                hechCuotaVentaMes = em.getReference(HechCuotaVentaMes.class, id);
                hechCuotaVentaMes.getCuotVtaId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The hechCuotaVentaMes with id " + id + " no longer exists.",
                        enfe);
            }
            em.remove(hechCuotaVentaMes);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<HechCuotaVentaMes> findHechCuotaVentaMesEntities() {
        return findHechCuotaVentaMesEntities(true, -1, -1);
    }

    public List<HechCuotaVentaMes> findHechCuotaVentaMesEntities(int maxResults, int firstResult) {
        return findHechCuotaVentaMesEntities(false, maxResults, firstResult);
    }

    private List<HechCuotaVentaMes> findHechCuotaVentaMesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HechCuotaVentaMes.class));
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

    public HechCuotaVentaMes findHechCuotaVentaMes(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(HechCuotaVentaMes.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getHechCuotaVentaMesCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HechCuotaVentaMes> rt = cq.from(HechCuotaVentaMes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
