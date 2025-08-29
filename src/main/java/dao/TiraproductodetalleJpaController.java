package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import dao.exceptions.NonexistentEntityException;
import dto.Tiraproductodetalle;

/**
 *
 * @author USUARIO
 */
public class TiraproductodetalleJpaController extends JpaPadre {

    public TiraproductodetalleJpaController(String empresa) {
        super(empresa);
    }

    public void create(Tiraproductodetalle tiraproductodetalle) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tiraproductodetalle);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(Tiraproductodetalle tiraproductodetalle) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tiraproductodetalle = em.merge(tiraproductodetalle);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tiraproductodetalle.getCodtiritm();
                if (findTiraproductodetalle(id) == null) {
                    throw new NonexistentEntityException(
                            "The tiraproductodetalle with id " + id + " no longer exists.");
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
            Tiraproductodetalle tiraproductodetalle;
            try {
                tiraproductodetalle = em.getReference(Tiraproductodetalle.class, id);
                tiraproductodetalle.getCodtiritm();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tiraproductodetalle with id " + id + " no longer exists.",
                        enfe);
            }
            em.remove(tiraproductodetalle);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<Tiraproductodetalle> findTiraproductodetalleEntities() {
        return findTiraproductodetalleEntities(true, -1, -1);
    }

    public List<Tiraproductodetalle> findTiraproductodetalleEntities(int maxResults, int firstResult) {
        return findTiraproductodetalleEntities(false, maxResults, firstResult);
    }

    private List<Tiraproductodetalle> findTiraproductodetalleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tiraproductodetalle.class));
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

    public Tiraproductodetalle findTiraproductodetalle(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(Tiraproductodetalle.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getTiraproductodetalleCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tiraproductodetalle> rt = cq.from(Tiraproductodetalle.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
