/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dao;

import pe.bartolito.conta.dao.exceptions.IllegalOrphanException;
import pe.bartolito.conta.dao.exceptions.NonexistentEntityException;
import pe.bartolito.conta.dao.exceptions.PreexistingEntityException;
import pe.bartolito.conta.dto.DepartamentoGeografico;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pe.bartolito.conta.dto.ProvinciaGeografica;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author USUARIO
 */
public class DepartamentoGeograficoJpaController implements Serializable {

    public DepartamentoGeograficoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DepartamentoGeografico departamentoGeografico) throws PreexistingEntityException, Exception {
        if (departamentoGeografico.getProvinciaGeograficaCollection() == null) {
            departamentoGeografico.setProvinciaGeograficaCollection(new ArrayList<ProvinciaGeografica>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<ProvinciaGeografica> attachedProvinciaGeograficaCollection = new ArrayList<ProvinciaGeografica>();
            for (ProvinciaGeografica provinciaGeograficaCollectionProvinciaGeograficaToAttach : departamentoGeografico.getProvinciaGeograficaCollection()) {
                provinciaGeograficaCollectionProvinciaGeograficaToAttach = em.getReference(provinciaGeograficaCollectionProvinciaGeograficaToAttach.getClass(), provinciaGeograficaCollectionProvinciaGeograficaToAttach.getProvinciaGeograficaPK());
                attachedProvinciaGeograficaCollection.add(provinciaGeograficaCollectionProvinciaGeograficaToAttach);
            }
            departamentoGeografico.setProvinciaGeograficaCollection(attachedProvinciaGeograficaCollection);
            em.persist(departamentoGeografico);
            for (ProvinciaGeografica provinciaGeograficaCollectionProvinciaGeografica : departamentoGeografico.getProvinciaGeograficaCollection()) {
                DepartamentoGeografico oldDepartamentoGeograficoOfProvinciaGeograficaCollectionProvinciaGeografica = provinciaGeograficaCollectionProvinciaGeografica.getDepartamentoGeografico();
                provinciaGeograficaCollectionProvinciaGeografica.setDepartamentoGeografico(departamentoGeografico);
                provinciaGeograficaCollectionProvinciaGeografica = em.merge(provinciaGeograficaCollectionProvinciaGeografica);
                if (oldDepartamentoGeograficoOfProvinciaGeograficaCollectionProvinciaGeografica != null) {
                    oldDepartamentoGeograficoOfProvinciaGeograficaCollectionProvinciaGeografica.getProvinciaGeograficaCollection().remove(provinciaGeograficaCollectionProvinciaGeografica);
                    oldDepartamentoGeograficoOfProvinciaGeograficaCollectionProvinciaGeografica = em.merge(oldDepartamentoGeograficoOfProvinciaGeograficaCollectionProvinciaGeografica);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDepartamentoGeografico(departamentoGeografico.getDptoGeoId()) != null) {
                throw new PreexistingEntityException("DepartamentoGeografico " + departamentoGeografico + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DepartamentoGeografico departamentoGeografico) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DepartamentoGeografico persistentDepartamentoGeografico = em.find(DepartamentoGeografico.class, departamentoGeografico.getDptoGeoId());
            Collection<ProvinciaGeografica> provinciaGeograficaCollectionOld = persistentDepartamentoGeografico.getProvinciaGeograficaCollection();
            Collection<ProvinciaGeografica> provinciaGeograficaCollectionNew = departamentoGeografico.getProvinciaGeograficaCollection();
            List<String> illegalOrphanMessages = null;
            for (ProvinciaGeografica provinciaGeograficaCollectionOldProvinciaGeografica : provinciaGeograficaCollectionOld) {
                if (!provinciaGeograficaCollectionNew.contains(provinciaGeograficaCollectionOldProvinciaGeografica)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProvinciaGeografica " + provinciaGeograficaCollectionOldProvinciaGeografica + " since its departamentoGeografico field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<ProvinciaGeografica> attachedProvinciaGeograficaCollectionNew = new ArrayList<ProvinciaGeografica>();
            for (ProvinciaGeografica provinciaGeograficaCollectionNewProvinciaGeograficaToAttach : provinciaGeograficaCollectionNew) {
                provinciaGeograficaCollectionNewProvinciaGeograficaToAttach = em.getReference(provinciaGeograficaCollectionNewProvinciaGeograficaToAttach.getClass(), provinciaGeograficaCollectionNewProvinciaGeograficaToAttach.getProvinciaGeograficaPK());
                attachedProvinciaGeograficaCollectionNew.add(provinciaGeograficaCollectionNewProvinciaGeograficaToAttach);
            }
            provinciaGeograficaCollectionNew = attachedProvinciaGeograficaCollectionNew;
            departamentoGeografico.setProvinciaGeograficaCollection(provinciaGeograficaCollectionNew);
            departamentoGeografico = em.merge(departamentoGeografico);
            for (ProvinciaGeografica provinciaGeograficaCollectionNewProvinciaGeografica : provinciaGeograficaCollectionNew) {
                if (!provinciaGeograficaCollectionOld.contains(provinciaGeograficaCollectionNewProvinciaGeografica)) {
                    DepartamentoGeografico oldDepartamentoGeograficoOfProvinciaGeograficaCollectionNewProvinciaGeografica = provinciaGeograficaCollectionNewProvinciaGeografica.getDepartamentoGeografico();
                    provinciaGeograficaCollectionNewProvinciaGeografica.setDepartamentoGeografico(departamentoGeografico);
                    provinciaGeograficaCollectionNewProvinciaGeografica = em.merge(provinciaGeograficaCollectionNewProvinciaGeografica);
                    if (oldDepartamentoGeograficoOfProvinciaGeograficaCollectionNewProvinciaGeografica != null && !oldDepartamentoGeograficoOfProvinciaGeograficaCollectionNewProvinciaGeografica.equals(departamentoGeografico)) {
                        oldDepartamentoGeograficoOfProvinciaGeograficaCollectionNewProvinciaGeografica.getProvinciaGeograficaCollection().remove(provinciaGeograficaCollectionNewProvinciaGeografica);
                        oldDepartamentoGeograficoOfProvinciaGeograficaCollectionNewProvinciaGeografica = em.merge(oldDepartamentoGeograficoOfProvinciaGeograficaCollectionNewProvinciaGeografica);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = departamentoGeografico.getDptoGeoId();
                if (findDepartamentoGeografico(id) == null) {
                    throw new NonexistentEntityException("The departamentoGeografico with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DepartamentoGeografico departamentoGeografico;
            try {
                departamentoGeografico = em.getReference(DepartamentoGeografico.class, id);
                departamentoGeografico.getDptoGeoId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The departamentoGeografico with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<ProvinciaGeografica> provinciaGeograficaCollectionOrphanCheck = departamentoGeografico.getProvinciaGeograficaCollection();
            for (ProvinciaGeografica provinciaGeograficaCollectionOrphanCheckProvinciaGeografica : provinciaGeograficaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DepartamentoGeografico (" + departamentoGeografico + ") cannot be destroyed since the ProvinciaGeografica " + provinciaGeograficaCollectionOrphanCheckProvinciaGeografica + " in its provinciaGeograficaCollection field has a non-nullable departamentoGeografico field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(departamentoGeografico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DepartamentoGeografico> findDepartamentoGeograficoEntities() {
        return findDepartamentoGeograficoEntities(true, -1, -1);
    }

    public List<DepartamentoGeografico> findDepartamentoGeograficoEntities(int maxResults, int firstResult) {
        return findDepartamentoGeograficoEntities(false, maxResults, firstResult);
    }

    private List<DepartamentoGeografico> findDepartamentoGeograficoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DepartamentoGeografico.class));
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

    public DepartamentoGeografico findDepartamentoGeografico(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DepartamentoGeografico.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartamentoGeograficoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DepartamentoGeografico> rt = cq.from(DepartamentoGeografico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
