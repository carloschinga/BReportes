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
import dto.ParametrosBartolito;

/**
 *
 * @author USUARIO
 */
public class ParametrosBartolitoJpaController extends JpaPadre {

    public int obtenerDisPro() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Integer> query = em.createNamedQuery("ParametrosBartolito.findDisPro", Integer.class);
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

    public ParametrosBartolitoJpaController(String empresa) {
        super(empresa);
    }

    public void create(ParametrosBartolito parametrosBartolito) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(parametrosBartolito);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findParametrosBartolito(parametrosBartolito.getCodparam()) != null) {
                throw new PreexistingEntityException("ParametrosBartolito " + parametrosBartolito + " already exists.",
                        ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(ParametrosBartolito parametrosBartolito) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            parametrosBartolito = em.merge(parametrosBartolito);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = parametrosBartolito.getCodparam();
                if (findParametrosBartolito(id) == null) {
                    throw new NonexistentEntityException(
                            "The parametrosBartolito with id " + id + " no longer exists.");
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
            ParametrosBartolito parametrosBartolito;
            try {
                parametrosBartolito = em.getReference(ParametrosBartolito.class, id);
                parametrosBartolito.getCodparam();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The parametrosBartolito with id " + id + " no longer exists.",
                        enfe);
            }
            em.remove(parametrosBartolito);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<ParametrosBartolito> findParametrosBartolitoEntities() {
        return findParametrosBartolitoEntities(true, -1, -1);
    }

    public List<ParametrosBartolito> findParametrosBartolitoEntities(int maxResults, int firstResult) {
        return findParametrosBartolitoEntities(false, maxResults, firstResult);
    }

    private List<ParametrosBartolito> findParametrosBartolitoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ParametrosBartolito.class));
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

    public ParametrosBartolito findParametrosBartolito(String id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(ParametrosBartolito.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getParametrosBartolitoCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ParametrosBartolito> rt = cq.from(ParametrosBartolito.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
