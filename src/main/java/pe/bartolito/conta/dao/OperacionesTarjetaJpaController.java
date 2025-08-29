/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dao;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pe.bartolito.conta.dao.exceptions.NonexistentEntityException;
import pe.bartolito.conta.dto.OperacionesTarjeta;

/**
 *
 * @author USUARIO
 */
public class OperacionesTarjetaJpaController implements Serializable {

    public OperacionesTarjetaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(OperacionesTarjeta operacionesTarjeta) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(operacionesTarjeta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(OperacionesTarjeta operacionesTarjeta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            operacionesTarjeta = em.merge(operacionesTarjeta);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = operacionesTarjeta.getId();
                if (findOperacionesTarjeta(id) == null) {
                    throw new NonexistentEntityException("The operacionesTarjeta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            OperacionesTarjeta operacionesTarjeta;
            try {
                operacionesTarjeta = em.getReference(OperacionesTarjeta.class, id);
                operacionesTarjeta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The operacionesTarjeta with id " + id + " no longer exists.", enfe);
            }
            em.remove(operacionesTarjeta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<OperacionesTarjeta> findOperacionesTarjetaEntities() {
        return findOperacionesTarjetaEntities(true, -1, -1);
    }

    public List<OperacionesTarjeta> findOperacionesTarjetaEntities(int maxResults, int firstResult) {
        return findOperacionesTarjetaEntities(false, maxResults, firstResult);
    }

    private List<OperacionesTarjeta> findOperacionesTarjetaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(OperacionesTarjeta.class));
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

    public OperacionesTarjeta findOperacionesTarjeta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(OperacionesTarjeta.class, id);
        } finally {
            em.close();
        }
    }

    public int getOperacionesTarjetaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<OperacionesTarjeta> rt = cq.from(OperacionesTarjeta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
