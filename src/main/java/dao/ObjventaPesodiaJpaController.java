package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import dao.exceptions.NonexistentEntityException;
import dto.ObjventaPesodia;

/**
 *
 * @author sbdeveloperw
 */
public class ObjventaPesodiaJpaController extends JpaPadre {

    public ObjventaPesodiaJpaController(String empresa) {
        super(empresa);
    }

    public void create(ObjventaPesodia objventaPesodia) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(objventaPesodia);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(ObjventaPesodia objventaPesodia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            objventaPesodia = em.merge(objventaPesodia);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = objventaPesodia.getCodopd();
                if (findObjventaPesodia(id) == null) {
                    throw new NonexistentEntityException("The objventaPesodia with id " + id + " no longer exists.");
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
            ObjventaPesodia objventaPesodia;
            try {
                objventaPesodia = em.getReference(ObjventaPesodia.class, id);
                objventaPesodia.getCodopd();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The objventaPesodia with id " + id + " no longer exists.", enfe);
            }
            em.remove(objventaPesodia);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<ObjventaPesodia> findObjventaPesodiaEntities() {
        return findObjventaPesodiaEntities(true, -1, -1);
    }

    public List<ObjventaPesodia> findObjventaPesodiaEntities(int maxResults, int firstResult) {
        return findObjventaPesodiaEntities(false, maxResults, firstResult);
    }

    private List<ObjventaPesodia> findObjventaPesodiaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ObjventaPesodia.class));
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

    public ObjventaPesodia findObjventaPesodia(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(ObjventaPesodia.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getObjventaPesodiaCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ObjventaPesodia> rt = cq.from(ObjventaPesodia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
