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
import pe.bartolito.clinica.dto.ViewRcMedico;

/**
 *
 * @author USUARIO
 */
public class ViewRcMedicoJpaController implements Serializable {

    public ViewRcMedicoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ViewRcMedico viewRcMedico) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(viewRcMedico);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findViewRcMedico(viewRcMedico.getMedcod()) != null) {
                throw new PreexistingEntityException("ViewRcMedico " + viewRcMedico + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ViewRcMedico viewRcMedico) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            viewRcMedico = em.merge(viewRcMedico);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = viewRcMedico.getMedcod();
                if (findViewRcMedico(id) == null) {
                    throw new NonexistentEntityException("The viewRcMedico with id " + id + " no longer exists.");
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
            ViewRcMedico viewRcMedico;
            try {
                viewRcMedico = em.getReference(ViewRcMedico.class, id);
                viewRcMedico.getMedcod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The viewRcMedico with id " + id + " no longer exists.", enfe);
            }
            em.remove(viewRcMedico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ViewRcMedico> findViewRcMedicoEntities() {
        return findViewRcMedicoEntities(true, -1, -1);
    }

    public List<ViewRcMedico> findViewRcMedicoEntities(int maxResults, int firstResult) {
        return findViewRcMedicoEntities(false, maxResults, firstResult);
    }

    private List<ViewRcMedico> findViewRcMedicoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ViewRcMedico.class));
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

    public ViewRcMedico findViewRcMedico(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ViewRcMedico.class, id);
        } finally {
            em.close();
        }
    }

    public int getViewRcMedicoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ViewRcMedico> rt = cq.from(ViewRcMedico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
