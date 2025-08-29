/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dao;

import pe.bartolito.conta.dao.exceptions.IllegalOrphanException;
import pe.bartolito.conta.dao.exceptions.NonexistentEntityException;
import pe.bartolito.conta.dao.exceptions.PreexistingEntityException;
import pe.bartolito.conta.dto.MovimientoContable;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pe.bartolito.conta.dto.SubMovimientoContable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author USUARIO
 */
public class MovimientoContableJpaController implements Serializable {

    public MovimientoContableJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MovimientoContable movimientoContable) throws PreexistingEntityException, Exception {
        if (movimientoContable.getSubMovimientoContableCollection() == null) {
            movimientoContable.setSubMovimientoContableCollection(new ArrayList<SubMovimientoContable>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<SubMovimientoContable> attachedSubMovimientoContableCollection = new ArrayList<SubMovimientoContable>();
            for (SubMovimientoContable subMovimientoContableCollectionSubMovimientoContableToAttach : movimientoContable.getSubMovimientoContableCollection()) {
                subMovimientoContableCollectionSubMovimientoContableToAttach = em.getReference(subMovimientoContableCollectionSubMovimientoContableToAttach.getClass(), subMovimientoContableCollectionSubMovimientoContableToAttach.getSubMovimientoContablePK());
                attachedSubMovimientoContableCollection.add(subMovimientoContableCollectionSubMovimientoContableToAttach);
            }
            movimientoContable.setSubMovimientoContableCollection(attachedSubMovimientoContableCollection);
            em.persist(movimientoContable);
            for (SubMovimientoContable subMovimientoContableCollectionSubMovimientoContable : movimientoContable.getSubMovimientoContableCollection()) {
                MovimientoContable oldMovimientoContableOfSubMovimientoContableCollectionSubMovimientoContable = subMovimientoContableCollectionSubMovimientoContable.getMovimientoContable();
                subMovimientoContableCollectionSubMovimientoContable.setMovimientoContable(movimientoContable);
                subMovimientoContableCollectionSubMovimientoContable = em.merge(subMovimientoContableCollectionSubMovimientoContable);
                if (oldMovimientoContableOfSubMovimientoContableCollectionSubMovimientoContable != null) {
                    oldMovimientoContableOfSubMovimientoContableCollectionSubMovimientoContable.getSubMovimientoContableCollection().remove(subMovimientoContableCollectionSubMovimientoContable);
                    oldMovimientoContableOfSubMovimientoContableCollectionSubMovimientoContable = em.merge(oldMovimientoContableOfSubMovimientoContableCollectionSubMovimientoContable);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMovimientoContable(movimientoContable.getMovConId()) != null) {
                throw new PreexistingEntityException("MovimientoContable " + movimientoContable + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MovimientoContable movimientoContable) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MovimientoContable persistentMovimientoContable = em.find(MovimientoContable.class, movimientoContable.getMovConId());
            Collection<SubMovimientoContable> subMovimientoContableCollectionOld = persistentMovimientoContable.getSubMovimientoContableCollection();
            Collection<SubMovimientoContable> subMovimientoContableCollectionNew = movimientoContable.getSubMovimientoContableCollection();
            List<String> illegalOrphanMessages = null;
            for (SubMovimientoContable subMovimientoContableCollectionOldSubMovimientoContable : subMovimientoContableCollectionOld) {
                if (!subMovimientoContableCollectionNew.contains(subMovimientoContableCollectionOldSubMovimientoContable)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SubMovimientoContable " + subMovimientoContableCollectionOldSubMovimientoContable + " since its movimientoContable field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<SubMovimientoContable> attachedSubMovimientoContableCollectionNew = new ArrayList<SubMovimientoContable>();
            for (SubMovimientoContable subMovimientoContableCollectionNewSubMovimientoContableToAttach : subMovimientoContableCollectionNew) {
                subMovimientoContableCollectionNewSubMovimientoContableToAttach = em.getReference(subMovimientoContableCollectionNewSubMovimientoContableToAttach.getClass(), subMovimientoContableCollectionNewSubMovimientoContableToAttach.getSubMovimientoContablePK());
                attachedSubMovimientoContableCollectionNew.add(subMovimientoContableCollectionNewSubMovimientoContableToAttach);
            }
            subMovimientoContableCollectionNew = attachedSubMovimientoContableCollectionNew;
            movimientoContable.setSubMovimientoContableCollection(subMovimientoContableCollectionNew);
            movimientoContable = em.merge(movimientoContable);
            for (SubMovimientoContable subMovimientoContableCollectionNewSubMovimientoContable : subMovimientoContableCollectionNew) {
                if (!subMovimientoContableCollectionOld.contains(subMovimientoContableCollectionNewSubMovimientoContable)) {
                    MovimientoContable oldMovimientoContableOfSubMovimientoContableCollectionNewSubMovimientoContable = subMovimientoContableCollectionNewSubMovimientoContable.getMovimientoContable();
                    subMovimientoContableCollectionNewSubMovimientoContable.setMovimientoContable(movimientoContable);
                    subMovimientoContableCollectionNewSubMovimientoContable = em.merge(subMovimientoContableCollectionNewSubMovimientoContable);
                    if (oldMovimientoContableOfSubMovimientoContableCollectionNewSubMovimientoContable != null && !oldMovimientoContableOfSubMovimientoContableCollectionNewSubMovimientoContable.equals(movimientoContable)) {
                        oldMovimientoContableOfSubMovimientoContableCollectionNewSubMovimientoContable.getSubMovimientoContableCollection().remove(subMovimientoContableCollectionNewSubMovimientoContable);
                        oldMovimientoContableOfSubMovimientoContableCollectionNewSubMovimientoContable = em.merge(oldMovimientoContableOfSubMovimientoContableCollectionNewSubMovimientoContable);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = movimientoContable.getMovConId();
                if (findMovimientoContable(id) == null) {
                    throw new NonexistentEntityException("The movimientoContable with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MovimientoContable movimientoContable;
            try {
                movimientoContable = em.getReference(MovimientoContable.class, id);
                movimientoContable.getMovConId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The movimientoContable with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<SubMovimientoContable> subMovimientoContableCollectionOrphanCheck = movimientoContable.getSubMovimientoContableCollection();
            for (SubMovimientoContable subMovimientoContableCollectionOrphanCheckSubMovimientoContable : subMovimientoContableCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MovimientoContable (" + movimientoContable + ") cannot be destroyed since the SubMovimientoContable " + subMovimientoContableCollectionOrphanCheckSubMovimientoContable + " in its subMovimientoContableCollection field has a non-nullable movimientoContable field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(movimientoContable);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MovimientoContable> findMovimientoContableEntities() {
        return findMovimientoContableEntities(true, -1, -1);
    }

    public List<MovimientoContable> findMovimientoContableEntities(int maxResults, int firstResult) {
        return findMovimientoContableEntities(false, maxResults, firstResult);
    }

    private List<MovimientoContable> findMovimientoContableEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MovimientoContable.class));
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

    public MovimientoContable findMovimientoContable(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MovimientoContable.class, id);
        } finally {
            em.close();
        }
    }

    public int getMovimientoContableCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MovimientoContable> rt = cq.from(MovimientoContable.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
