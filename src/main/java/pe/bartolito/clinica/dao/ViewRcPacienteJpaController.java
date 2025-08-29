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
import pe.bartolito.clinica.dto.ViewRcPaciente;

/**
 *
 * @author USUARIO
 */
public class ViewRcPacienteJpaController implements Serializable {

    public ViewRcPacienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ViewRcPaciente viewRcPaciente) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(viewRcPaciente);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findViewRcPaciente(viewRcPaciente.getNumdocId()) != null) {
                throw new PreexistingEntityException("ViewRcPaciente " + viewRcPaciente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ViewRcPaciente viewRcPaciente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            viewRcPaciente = em.merge(viewRcPaciente);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = viewRcPaciente.getNumdocId();
                if (findViewRcPaciente(id) == null) {
                    throw new NonexistentEntityException("The viewRcPaciente with id " + id + " no longer exists.");
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
            ViewRcPaciente viewRcPaciente;
            try {
                viewRcPaciente = em.getReference(ViewRcPaciente.class, id);
                viewRcPaciente.getNumdocId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The viewRcPaciente with id " + id + " no longer exists.", enfe);
            }
            em.remove(viewRcPaciente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ViewRcPaciente> findViewRcPacienteEntities() {
        return findViewRcPacienteEntities(true, -1, -1);
    }

    public List<ViewRcPaciente> findViewRcPacienteEntities(int maxResults, int firstResult) {
        return findViewRcPacienteEntities(false, maxResults, firstResult);
    }

    private List<ViewRcPaciente> findViewRcPacienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ViewRcPaciente.class));
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

    public ViewRcPaciente findViewRcPaciente(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ViewRcPaciente.class, id);
        } finally {
            em.close();
        }
    }

    public int getViewRcPacienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ViewRcPaciente> rt = cq.from(ViewRcPaciente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
