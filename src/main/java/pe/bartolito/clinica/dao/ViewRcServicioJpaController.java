/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.clinica.dao;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pe.bartolito.clinica.dao.exceptions.NonexistentEntityException;
import pe.bartolito.clinica.dao.exceptions.PreexistingEntityException;
import pe.bartolito.clinica.dto.ViewRcServicio;

/**
 *
 * @author USUARIO
 */
public class ViewRcServicioJpaController implements Serializable {

    public ViewRcServicioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ViewRcServicio viewRcServicio) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(viewRcServicio);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findViewRcServicio(viewRcServicio.getSercod()) != null) {
                throw new PreexistingEntityException("ViewRcServicio " + viewRcServicio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ViewRcServicio viewRcServicio) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            viewRcServicio = em.merge(viewRcServicio);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = viewRcServicio.getSercod();
                if (findViewRcServicio(id) == null) {
                    throw new NonexistentEntityException("The viewRcServicio with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ViewRcServicio viewRcServicio;
            try {
                viewRcServicio = em.getReference(ViewRcServicio.class, id);
                viewRcServicio.getSercod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The viewRcServicio with id " + id + " no longer exists.", enfe);
            }
            em.remove(viewRcServicio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ViewRcServicio> findViewRcServicioEntities() {
        return findViewRcServicioEntities(true, -1, -1);
    }

    public List<ViewRcServicio> findViewRcServicioEntities(int maxResults, int firstResult) {
        return findViewRcServicioEntities(false, maxResults, firstResult);
    }

    private List<ViewRcServicio> findViewRcServicioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ViewRcServicio.class));
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

    public ViewRcServicio findViewRcServicio(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ViewRcServicio.class, id);
        } finally {
            em.close();
        }
    }

    public int getViewRcServicioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ViewRcServicio> rt = cq.from(ViewRcServicio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
