package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.EstablecimientosAccesos;
import dto.EstablecimientosAccesosPK;

/**
 *
 * @author USUARIO
 */
public class EstablecimientosAccesosJpaController extends JpaPadre {

    public EstablecimientosAccesosJpaController(String empresa) {
        super(empresa);
    }

    public static void main(String[] args) {
        EstablecimientosAccesosJpaController dao = new EstablecimientosAccesosJpaController("a");

    }

    public void create(EstablecimientosAccesos establecimientosAccesos) throws PreexistingEntityException, Exception {
        if (establecimientosAccesos.getEstablecimientosAccesosPK() == null) {
            establecimientosAccesos.setEstablecimientosAccesosPK(new EstablecimientosAccesosPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(establecimientosAccesos);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEstablecimientosAccesos(establecimientosAccesos.getEstablecimientosAccesosPK()) != null) {
                throw new PreexistingEntityException(
                        "EstablecimientosAccesos " + establecimientosAccesos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(EstablecimientosAccesos establecimientosAccesos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            establecimientosAccesos = em.merge(establecimientosAccesos);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                EstablecimientosAccesosPK id = establecimientosAccesos.getEstablecimientosAccesosPK();
                if (findEstablecimientosAccesos(id) == null) {
                    throw new NonexistentEntityException(
                            "The establecimientosAccesos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(EstablecimientosAccesosPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EstablecimientosAccesos establecimientosAccesos;
            try {
                establecimientosAccesos = em.getReference(EstablecimientosAccesos.class, id);
                establecimientosAccesos.getEstablecimientosAccesosPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The establecimientosAccesos with id " + id + " no longer exists.",
                        enfe);
            }
            em.remove(establecimientosAccesos);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<EstablecimientosAccesos> findEstablecimientosAccesosEntities() {
        return findEstablecimientosAccesosEntities(true, -1, -1);
    }

    public List<EstablecimientosAccesos> findEstablecimientosAccesosEntities(int maxResults, int firstResult) {
        return findEstablecimientosAccesosEntities(false, maxResults, firstResult);
    }

    private List<EstablecimientosAccesos> findEstablecimientosAccesosEntities(boolean all, int maxResults,
            int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EstablecimientosAccesos.class));
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

    public EstablecimientosAccesos findEstablecimientosAccesos(EstablecimientosAccesosPK id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(EstablecimientosAccesos.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getEstablecimientosAccesosCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EstablecimientosAccesos> rt = cq.from(EstablecimientosAccesos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
