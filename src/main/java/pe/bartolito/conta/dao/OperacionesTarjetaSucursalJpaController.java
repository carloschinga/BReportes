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
import pe.bartolito.conta.dto.OperacionesTarjetaSucursal;

/**
 *
 * @author USUARIO
 */
public class OperacionesTarjetaSucursalJpaController implements Serializable {

    public OperacionesTarjetaSucursalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(OperacionesTarjetaSucursal operacionesTarjetaSucursal) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(operacionesTarjetaSucursal);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(OperacionesTarjetaSucursal operacionesTarjetaSucursal) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            operacionesTarjetaSucursal = em.merge(operacionesTarjetaSucursal);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = operacionesTarjetaSucursal.getCodigo();
                if (findOperacionesTarjetaSucursal(id) == null) {
                    throw new NonexistentEntityException("The operacionesTarjetaSucursal with id " + id + " no longer exists.");
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
            OperacionesTarjetaSucursal operacionesTarjetaSucursal;
            try {
                operacionesTarjetaSucursal = em.getReference(OperacionesTarjetaSucursal.class, id);
                operacionesTarjetaSucursal.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The operacionesTarjetaSucursal with id " + id + " no longer exists.", enfe);
            }
            em.remove(operacionesTarjetaSucursal);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<OperacionesTarjetaSucursal> findOperacionesTarjetaSucursalEntities() {
        return findOperacionesTarjetaSucursalEntities(true, -1, -1);
    }

    public List<OperacionesTarjetaSucursal> findOperacionesTarjetaSucursalEntities(int maxResults, int firstResult) {
        return findOperacionesTarjetaSucursalEntities(false, maxResults, firstResult);
    }

    private List<OperacionesTarjetaSucursal> findOperacionesTarjetaSucursalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(OperacionesTarjetaSucursal.class));
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

    public OperacionesTarjetaSucursal findOperacionesTarjetaSucursal(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(OperacionesTarjetaSucursal.class, id);
        } finally {
            em.close();
        }
    }

    public int getOperacionesTarjetaSucursalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<OperacionesTarjetaSucursal> rt = cq.from(OperacionesTarjetaSucursal.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
