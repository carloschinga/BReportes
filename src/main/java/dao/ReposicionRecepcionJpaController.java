package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.ReposicionRecepcion;
import dto.ReposicionRecepcionPK;

/**
 *
 * @author shaho
 */
public class ReposicionRecepcionJpaController extends JpaPadre {

    public ReposicionRecepcionJpaController(String empresa) {
        super(empresa);
    }

    public void create(ReposicionRecepcion reposicionRecepcion) throws PreexistingEntityException, Exception {
        if (reposicionRecepcion.getReposicionRecepcionPK() == null) {
            reposicionRecepcion.setReposicionRecepcionPK(new ReposicionRecepcionPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(reposicionRecepcion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findReposicionRecepcion(reposicionRecepcion.getReposicionRecepcionPK()) != null) {
                throw new PreexistingEntityException("ReposicionRecepcion " + reposicionRecepcion + " already exists.",
                        ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(ReposicionRecepcion reposicionRecepcion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            reposicionRecepcion = em.merge(reposicionRecepcion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ReposicionRecepcionPK id = reposicionRecepcion.getReposicionRecepcionPK();
                if (findReposicionRecepcion(id) == null) {
                    throw new NonexistentEntityException(
                            "The reposicionRecepcion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(ReposicionRecepcionPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ReposicionRecepcion reposicionRecepcion;
            try {
                reposicionRecepcion = em.getReference(ReposicionRecepcion.class, id);
                reposicionRecepcion.getReposicionRecepcionPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reposicionRecepcion with id " + id + " no longer exists.",
                        enfe);
            }
            em.remove(reposicionRecepcion);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<ReposicionRecepcion> findReposicionRecepcionEntities() {
        return findReposicionRecepcionEntities(true, -1, -1);
    }

    public List<ReposicionRecepcion> findReposicionRecepcionEntities(int maxResults, int firstResult) {
        return findReposicionRecepcionEntities(false, maxResults, firstResult);
    }

    private List<ReposicionRecepcion> findReposicionRecepcionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ReposicionRecepcion.class));
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

    public ReposicionRecepcion findReposicionRecepcion(ReposicionRecepcionPK id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(ReposicionRecepcion.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getReposicionRecepcionCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ReposicionRecepcion> rt = cq.from(ReposicionRecepcion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }
}
