package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.Secuencias;

/**
 *
 * @author USUARIO
 */
public class SecuenciasJpaController extends JpaPadre {

    public SecuenciasJpaController(String empresa) {
        super(empresa);
    }

    public int obtenerUltInvnum() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Integer> query = em.createNamedQuery("Secuencias.findnumero", Integer.class);
            query.setParameter("tsecod", "MF");
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return 0;
        } catch (Exception e) {
            return -1;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void create(Secuencias secuencias) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(secuencias);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSecuencias(secuencias.getTsecod()) != null) {
                throw new PreexistingEntityException("Secuencias " + secuencias + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(Secuencias secuencias) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            secuencias = em.merge(secuencias);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = secuencias.getTsecod();
                if (findSecuencias(id) == null) {
                    throw new NonexistentEntityException("The secuencias with id " + id + " no longer exists.");
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
            Secuencias secuencias;
            try {
                secuencias = em.getReference(Secuencias.class, id);
                secuencias.getTsecod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The secuencias with id " + id + " no longer exists.", enfe);
            }
            em.remove(secuencias);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<Secuencias> findSecuenciasEntities() {
        return findSecuenciasEntities(true, -1, -1);
    }

    public List<Secuencias> findSecuenciasEntities(int maxResults, int firstResult) {
        return findSecuenciasEntities(false, maxResults, firstResult);
    }

    private List<Secuencias> findSecuenciasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Secuencias.class));
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

    public Secuencias findSecuencias(String id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(Secuencias.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getSecuenciasCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Secuencias> rt = cq.from(Secuencias.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
