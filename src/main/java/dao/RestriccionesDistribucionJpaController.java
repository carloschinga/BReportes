package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.RestriccionesDistribucion;
import dto.RestriccionesDistribucionPK;

/**
 *
 * @author USUARIO
 */
public class RestriccionesDistribucionJpaController extends JpaPadre {

    public RestriccionesDistribucionJpaController(String empresa) {
        super(empresa);
    }

    public void create(RestriccionesDistribucion restriccionesDistribucion)
            throws PreexistingEntityException, Exception {
        if (restriccionesDistribucion.getRestriccionesDistribucionPK() == null) {
            restriccionesDistribucion.setRestriccionesDistribucionPK(new RestriccionesDistribucionPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(restriccionesDistribucion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRestriccionesDistribucion(restriccionesDistribucion.getRestriccionesDistribucionPK()) != null) {
                throw new PreexistingEntityException(
                        "RestriccionesDistribucion " + restriccionesDistribucion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(RestriccionesDistribucion restriccionesDistribucion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            restriccionesDistribucion = em.merge(restriccionesDistribucion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                RestriccionesDistribucionPK id = restriccionesDistribucion.getRestriccionesDistribucionPK();
                if (findRestriccionesDistribucion(id) == null) {
                    throw new NonexistentEntityException(
                            "The restriccionesDistribucion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(RestriccionesDistribucionPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RestriccionesDistribucion restriccionesDistribucion;
            try {
                restriccionesDistribucion = em.getReference(RestriccionesDistribucion.class, id);
                restriccionesDistribucion.getRestriccionesDistribucionPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException(
                        "The restriccionesDistribucion with id " + id + " no longer exists.", enfe);
            }
            em.remove(restriccionesDistribucion);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<RestriccionesDistribucion> findRestriccionesDistribucionEntities() {
        return findRestriccionesDistribucionEntities(true, -1, -1);
    }

    public List<RestriccionesDistribucion> findRestriccionesDistribucionEntities(int maxResults, int firstResult) {
        return findRestriccionesDistribucionEntities(false, maxResults, firstResult);
    }

    private List<RestriccionesDistribucion> findRestriccionesDistribucionEntities(boolean all, int maxResults,
            int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RestriccionesDistribucion.class));
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

    public RestriccionesDistribucion findRestriccionesDistribucion(RestriccionesDistribucionPK id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(RestriccionesDistribucion.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getRestriccionesDistribucionCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RestriccionesDistribucion> rt = cq.from(RestriccionesDistribucion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public RestriccionesDistribucion encontrarRestricciones(String codpro, String codalm) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNamedQuery("RestriccionesDistribucion.encontrar");
            query.setParameter("codpro", codpro);
            query.setParameter("codalm", codalm);
            return (RestriccionesDistribucion) query.getSingleResult();
        } catch (NoResultException e) {
            // Si no se encuentra ningún resultado, devuelve nulo
            return null;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
