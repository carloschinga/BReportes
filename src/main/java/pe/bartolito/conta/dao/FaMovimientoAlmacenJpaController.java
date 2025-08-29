/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dao;

import pe.bartolito.conta.dao.exceptions.NonexistentEntityException;
import pe.bartolito.conta.dao.exceptions.PreexistingEntityException;
import pe.bartolito.conta.dto.FaMovimientoAlmacen;
import pe.bartolito.conta.dto.FaMovimientoAlmacenPK;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author USUARIO
 */
public class FaMovimientoAlmacenJpaController implements Serializable {

    public FaMovimientoAlmacenJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FaMovimientoAlmacen faMovimientoAlmacen) throws PreexistingEntityException, Exception {
        if (faMovimientoAlmacen.getFaMovimientoAlmacenPK() == null) {
            faMovimientoAlmacen.setFaMovimientoAlmacenPK(new FaMovimientoAlmacenPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(faMovimientoAlmacen);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFaMovimientoAlmacen(faMovimientoAlmacen.getFaMovimientoAlmacenPK()) != null) {
                throw new PreexistingEntityException("FaMovimientoAlmacen " + faMovimientoAlmacen + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FaMovimientoAlmacen faMovimientoAlmacen) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            faMovimientoAlmacen = em.merge(faMovimientoAlmacen);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                FaMovimientoAlmacenPK id = faMovimientoAlmacen.getFaMovimientoAlmacenPK();
                if (findFaMovimientoAlmacen(id) == null) {
                    throw new NonexistentEntityException("The faMovimientoAlmacen with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(FaMovimientoAlmacenPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FaMovimientoAlmacen faMovimientoAlmacen;
            try {
                faMovimientoAlmacen = em.getReference(FaMovimientoAlmacen.class, id);
                faMovimientoAlmacen.getFaMovimientoAlmacenPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The faMovimientoAlmacen with id " + id + " no longer exists.", enfe);
            }
            em.remove(faMovimientoAlmacen);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FaMovimientoAlmacen> findFaMovimientoAlmacenEntities() {
        return findFaMovimientoAlmacenEntities(true, -1, -1);
    }

    public List<FaMovimientoAlmacen> findFaMovimientoAlmacenEntities(int maxResults, int firstResult) {
        return findFaMovimientoAlmacenEntities(false, maxResults, firstResult);
    }

    private List<FaMovimientoAlmacen> findFaMovimientoAlmacenEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FaMovimientoAlmacen.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public FaMovimientoAlmacen findFaMovimientoAlmacen(FaMovimientoAlmacenPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FaMovimientoAlmacen.class, id);
        } finally {
            em.close();
        }
    }

    public int getFaMovimientoAlmacenCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FaMovimientoAlmacen> rt = cq.from(FaMovimientoAlmacen.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
