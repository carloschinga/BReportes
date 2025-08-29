/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dao;

import pe.bartolito.conta.dao.exceptions.NonexistentEntityException;
import pe.bartolito.conta.dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pe.bartolito.conta.dto.MovimientoContable;
import pe.bartolito.conta.dto.DiarioCabecera;
import pe.bartolito.conta.dto.SubMovimientoContable;
import pe.bartolito.conta.dto.SubMovimientoContablePK;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author USUARIO
 */
public class SubMovimientoContableJpaController implements Serializable {

    public SubMovimientoContableJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SubMovimientoContable subMovimientoContable) throws PreexistingEntityException, Exception {
        if (subMovimientoContable.getSubMovimientoContablePK() == null) {
            subMovimientoContable.setSubMovimientoContablePK(new SubMovimientoContablePK());
        }
        if (subMovimientoContable.getDiarioCabeceraCollection() == null) {
            subMovimientoContable.setDiarioCabeceraCollection(new ArrayList<DiarioCabecera>());
        }
        subMovimientoContable.getSubMovimientoContablePK().setMovConId(subMovimientoContable.getMovimientoContable().getMovConId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MovimientoContable movimientoContable = subMovimientoContable.getMovimientoContable();
            if (movimientoContable != null) {
                movimientoContable = em.getReference(movimientoContable.getClass(), movimientoContable.getMovConId());
                subMovimientoContable.setMovimientoContable(movimientoContable);
            }
            Collection<DiarioCabecera> attachedDiarioCabeceraCollection = new ArrayList<DiarioCabecera>();
            for (DiarioCabecera diarioCabeceraCollectionDiarioCabeceraToAttach : subMovimientoContable.getDiarioCabeceraCollection()) {
                diarioCabeceraCollectionDiarioCabeceraToAttach = em.getReference(diarioCabeceraCollectionDiarioCabeceraToAttach.getClass(), diarioCabeceraCollectionDiarioCabeceraToAttach.getDiaCabCompId());
                attachedDiarioCabeceraCollection.add(diarioCabeceraCollectionDiarioCabeceraToAttach);
            }
            subMovimientoContable.setDiarioCabeceraCollection(attachedDiarioCabeceraCollection);
            em.persist(subMovimientoContable);
            if (movimientoContable != null) {
                movimientoContable.getSubMovimientoContableCollection().add(subMovimientoContable);
                movimientoContable = em.merge(movimientoContable);
            }
            for (DiarioCabecera diarioCabeceraCollectionDiarioCabecera : subMovimientoContable.getDiarioCabeceraCollection()) {
                SubMovimientoContable oldSubMovimientoContableOfDiarioCabeceraCollectionDiarioCabecera = diarioCabeceraCollectionDiarioCabecera.getSubMovimientoContable();
                diarioCabeceraCollectionDiarioCabecera.setSubMovimientoContable(subMovimientoContable);
                diarioCabeceraCollectionDiarioCabecera = em.merge(diarioCabeceraCollectionDiarioCabecera);
                if (oldSubMovimientoContableOfDiarioCabeceraCollectionDiarioCabecera != null) {
                    oldSubMovimientoContableOfDiarioCabeceraCollectionDiarioCabecera.getDiarioCabeceraCollection().remove(diarioCabeceraCollectionDiarioCabecera);
                    oldSubMovimientoContableOfDiarioCabeceraCollectionDiarioCabecera = em.merge(oldSubMovimientoContableOfDiarioCabeceraCollectionDiarioCabecera);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSubMovimientoContable(subMovimientoContable.getSubMovimientoContablePK()) != null) {
                throw new PreexistingEntityException("SubMovimientoContable " + subMovimientoContable + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SubMovimientoContable subMovimientoContable) throws NonexistentEntityException, Exception {
        subMovimientoContable.getSubMovimientoContablePK().setMovConId(subMovimientoContable.getMovimientoContable().getMovConId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SubMovimientoContable persistentSubMovimientoContable = em.find(SubMovimientoContable.class, subMovimientoContable.getSubMovimientoContablePK());
            MovimientoContable movimientoContableOld = persistentSubMovimientoContable.getMovimientoContable();
            MovimientoContable movimientoContableNew = subMovimientoContable.getMovimientoContable();
            Collection<DiarioCabecera> diarioCabeceraCollectionOld = persistentSubMovimientoContable.getDiarioCabeceraCollection();
            Collection<DiarioCabecera> diarioCabeceraCollectionNew = subMovimientoContable.getDiarioCabeceraCollection();
            if (movimientoContableNew != null) {
                movimientoContableNew = em.getReference(movimientoContableNew.getClass(), movimientoContableNew.getMovConId());
                subMovimientoContable.setMovimientoContable(movimientoContableNew);
            }
            Collection<DiarioCabecera> attachedDiarioCabeceraCollectionNew = new ArrayList<DiarioCabecera>();
            for (DiarioCabecera diarioCabeceraCollectionNewDiarioCabeceraToAttach : diarioCabeceraCollectionNew) {
                diarioCabeceraCollectionNewDiarioCabeceraToAttach = em.getReference(diarioCabeceraCollectionNewDiarioCabeceraToAttach.getClass(), diarioCabeceraCollectionNewDiarioCabeceraToAttach.getDiaCabCompId());
                attachedDiarioCabeceraCollectionNew.add(diarioCabeceraCollectionNewDiarioCabeceraToAttach);
            }
            diarioCabeceraCollectionNew = attachedDiarioCabeceraCollectionNew;
            subMovimientoContable.setDiarioCabeceraCollection(diarioCabeceraCollectionNew);
            subMovimientoContable = em.merge(subMovimientoContable);
            if (movimientoContableOld != null && !movimientoContableOld.equals(movimientoContableNew)) {
                movimientoContableOld.getSubMovimientoContableCollection().remove(subMovimientoContable);
                movimientoContableOld = em.merge(movimientoContableOld);
            }
            if (movimientoContableNew != null && !movimientoContableNew.equals(movimientoContableOld)) {
                movimientoContableNew.getSubMovimientoContableCollection().add(subMovimientoContable);
                movimientoContableNew = em.merge(movimientoContableNew);
            }
            for (DiarioCabecera diarioCabeceraCollectionOldDiarioCabecera : diarioCabeceraCollectionOld) {
                if (!diarioCabeceraCollectionNew.contains(diarioCabeceraCollectionOldDiarioCabecera)) {
                    diarioCabeceraCollectionOldDiarioCabecera.setSubMovimientoContable(null);
                    diarioCabeceraCollectionOldDiarioCabecera = em.merge(diarioCabeceraCollectionOldDiarioCabecera);
                }
            }
            for (DiarioCabecera diarioCabeceraCollectionNewDiarioCabecera : diarioCabeceraCollectionNew) {
                if (!diarioCabeceraCollectionOld.contains(diarioCabeceraCollectionNewDiarioCabecera)) {
                    SubMovimientoContable oldSubMovimientoContableOfDiarioCabeceraCollectionNewDiarioCabecera = diarioCabeceraCollectionNewDiarioCabecera.getSubMovimientoContable();
                    diarioCabeceraCollectionNewDiarioCabecera.setSubMovimientoContable(subMovimientoContable);
                    diarioCabeceraCollectionNewDiarioCabecera = em.merge(diarioCabeceraCollectionNewDiarioCabecera);
                    if (oldSubMovimientoContableOfDiarioCabeceraCollectionNewDiarioCabecera != null && !oldSubMovimientoContableOfDiarioCabeceraCollectionNewDiarioCabecera.equals(subMovimientoContable)) {
                        oldSubMovimientoContableOfDiarioCabeceraCollectionNewDiarioCabecera.getDiarioCabeceraCollection().remove(diarioCabeceraCollectionNewDiarioCabecera);
                        oldSubMovimientoContableOfDiarioCabeceraCollectionNewDiarioCabecera = em.merge(oldSubMovimientoContableOfDiarioCabeceraCollectionNewDiarioCabecera);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                SubMovimientoContablePK id = subMovimientoContable.getSubMovimientoContablePK();
                if (findSubMovimientoContable(id) == null) {
                    throw new NonexistentEntityException("The subMovimientoContable with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(SubMovimientoContablePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SubMovimientoContable subMovimientoContable;
            try {
                subMovimientoContable = em.getReference(SubMovimientoContable.class, id);
                subMovimientoContable.getSubMovimientoContablePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The subMovimientoContable with id " + id + " no longer exists.", enfe);
            }
            MovimientoContable movimientoContable = subMovimientoContable.getMovimientoContable();
            if (movimientoContable != null) {
                movimientoContable.getSubMovimientoContableCollection().remove(subMovimientoContable);
                movimientoContable = em.merge(movimientoContable);
            }
            Collection<DiarioCabecera> diarioCabeceraCollection = subMovimientoContable.getDiarioCabeceraCollection();
            for (DiarioCabecera diarioCabeceraCollectionDiarioCabecera : diarioCabeceraCollection) {
                diarioCabeceraCollectionDiarioCabecera.setSubMovimientoContable(null);
                diarioCabeceraCollectionDiarioCabecera = em.merge(diarioCabeceraCollectionDiarioCabecera);
            }
            em.remove(subMovimientoContable);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SubMovimientoContable> findSubMovimientoContableEntities() {
        return findSubMovimientoContableEntities(true, -1, -1);
    }

    public List<SubMovimientoContable> findSubMovimientoContableEntities(int maxResults, int firstResult) {
        return findSubMovimientoContableEntities(false, maxResults, firstResult);
    }

    private List<SubMovimientoContable> findSubMovimientoContableEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SubMovimientoContable.class));
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

    public SubMovimientoContable findSubMovimientoContable(SubMovimientoContablePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SubMovimientoContable.class, id);
        } finally {
            em.close();
        }
    }

    public int getSubMovimientoContableCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SubMovimientoContable> rt = cq.from(SubMovimientoContable.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
