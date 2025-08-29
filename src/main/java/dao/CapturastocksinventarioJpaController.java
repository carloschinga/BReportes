package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import dao.exceptions.NonexistentEntityException;
import dto.Capturastocksinventario;

/**
 *
 * @author USUARIO
 */
public class CapturastocksinventarioJpaController extends JpaPadre {

    public CapturastocksinventarioJpaController(String empresa) {
        super(empresa);
    }

    public void create(Capturastocksinventario capturastocksinventario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(capturastocksinventario);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(Capturastocksinventario capturastocksinventario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            capturastocksinventario = em.merge(capturastocksinventario);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = capturastocksinventario.getId();
                if (findCapturastocksinventario(id) == null) {
                    throw new NonexistentEntityException(
                            "The capturastocksinventario with id " + id + " no longer exists.");
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
            Capturastocksinventario capturastocksinventario;
            try {
                capturastocksinventario = em.getReference(Capturastocksinventario.class, id);
                capturastocksinventario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The capturastocksinventario with id " + id + " no longer exists.",
                        enfe);
            }
            em.remove(capturastocksinventario);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<Capturastocksinventario> findCapturastocksinventarioEntities() {
        return findCapturastocksinventarioEntities(true, -1, -1);
    }

    public List<Capturastocksinventario> findCapturastocksinventarioEntities(int maxResults, int firstResult) {
        return findCapturastocksinventarioEntities(false, maxResults, firstResult);
    }

    private List<Capturastocksinventario> findCapturastocksinventarioEntities(boolean all, int maxResults,
            int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Capturastocksinventario.class));
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

    public Capturastocksinventario findCapturastocksinventario(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(Capturastocksinventario.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getCapturastocksinventarioCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Capturastocksinventario> rt = cq.from(Capturastocksinventario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
