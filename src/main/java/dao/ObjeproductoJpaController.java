package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.Objeproducto;
import dto.ObjeproductoPK;

/**
 *
 * @author shaho
 */
public class ObjeproductoJpaController extends JpaPadre {

    public ObjeproductoJpaController(String empresa) {
        super(empresa);
    }

    public void create(Objeproducto objeproducto) throws PreexistingEntityException, Exception {
        if (objeproducto.getObjeproductoPK() == null) {
            objeproducto.setObjeproductoPK(new ObjeproductoPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(objeproducto);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findObjeproducto(objeproducto.getObjeproductoPK()) != null) {
                throw new PreexistingEntityException("Objeproducto " + objeproducto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(Objeproducto objeproducto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            objeproducto = em.merge(objeproducto);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ObjeproductoPK id = objeproducto.getObjeproductoPK();
                if (findObjeproducto(id) == null) {
                    throw new NonexistentEntityException("The objeproducto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(ObjeproductoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Objeproducto objeproducto;
            try {
                objeproducto = em.getReference(Objeproducto.class, id);
                objeproducto.getObjeproductoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The objeproducto with id " + id + " no longer exists.", enfe);
            }
            em.remove(objeproducto);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<Objeproducto> findObjeproductoEntities() {
        return findObjeproductoEntities(true, -1, -1);
    }

    public List<Objeproducto> findObjeproductoEntities(int maxResults, int firstResult) {
        return findObjeproductoEntities(false, maxResults, firstResult);
    }

    private List<Objeproducto> findObjeproductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Objeproducto.class));
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

    public Objeproducto findObjeproducto(ObjeproductoPK id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(Objeproducto.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getObjeproductoCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Objeproducto> rt = cq.from(Objeproducto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
