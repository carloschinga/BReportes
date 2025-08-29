package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.FaDescuentosProductosEst;
import dto.FaDescuentosProductosEstPK;

/**
 *
 * @author USUARIO
 */
public class FaDescuentosProductosEstJpaController extends JpaPadre {

    public FaDescuentosProductosEstJpaController(String empresa) {
        super(empresa);
    }

    public void create(FaDescuentosProductosEst faDescuentosProductosEst) throws PreexistingEntityException, Exception {
        if (faDescuentosProductosEst.getFaDescuentosProductosEstPK() == null) {
            faDescuentosProductosEst.setFaDescuentosProductosEstPK(new FaDescuentosProductosEstPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(faDescuentosProductosEst);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFaDescuentosProductosEst(faDescuentosProductosEst.getFaDescuentosProductosEstPK()) != null) {
                throw new PreexistingEntityException(
                        "FaDescuentosProductosEst " + faDescuentosProductosEst + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(FaDescuentosProductosEst faDescuentosProductosEst) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            faDescuentosProductosEst = em.merge(faDescuentosProductosEst);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                FaDescuentosProductosEstPK id = faDescuentosProductosEst.getFaDescuentosProductosEstPK();
                if (findFaDescuentosProductosEst(id) == null) {
                    throw new NonexistentEntityException(
                            "The faDescuentosProductosEst with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(FaDescuentosProductosEstPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FaDescuentosProductosEst faDescuentosProductosEst;
            try {
                faDescuentosProductosEst = em.getReference(FaDescuentosProductosEst.class, id);
                faDescuentosProductosEst.getFaDescuentosProductosEstPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException(
                        "The faDescuentosProductosEst with id " + id + " no longer exists.", enfe);
            }
            em.remove(faDescuentosProductosEst);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<FaDescuentosProductosEst> findFaDescuentosProductosEstEntities() {
        return findFaDescuentosProductosEstEntities(true, -1, -1);
    }

    public List<FaDescuentosProductosEst> findFaDescuentosProductosEstEntities(int maxResults, int firstResult) {
        return findFaDescuentosProductosEstEntities(false, maxResults, firstResult);
    }

    private List<FaDescuentosProductosEst> findFaDescuentosProductosEstEntities(boolean all, int maxResults,
            int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FaDescuentosProductosEst.class));
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

    public FaDescuentosProductosEst findFaDescuentosProductosEst(FaDescuentosProductosEstPK id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(FaDescuentosProductosEst.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getFaDescuentosProductosEstCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FaDescuentosProductosEst> rt = cq.from(FaDescuentosProductosEst.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
