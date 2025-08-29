/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dao;

import pe.bartolito.conta.dao.exceptions.IllegalOrphanException;
import pe.bartolito.conta.dao.exceptions.NonexistentEntityException;
import pe.bartolito.conta.dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pe.bartolito.conta.dto.DepartamentoGeografico;
import pe.bartolito.conta.dto.DistritoGeografico;
import pe.bartolito.conta.dto.ProvinciaGeografica;
import pe.bartolito.conta.dto.ProvinciaGeograficaPK;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author USUARIO
 */
public class ProvinciaGeograficaJpaController implements Serializable {

    public ProvinciaGeograficaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProvinciaGeografica provinciaGeografica) throws PreexistingEntityException, Exception {
        if (provinciaGeografica.getProvinciaGeograficaPK() == null) {
            provinciaGeografica.setProvinciaGeograficaPK(new ProvinciaGeograficaPK());
        }
        if (provinciaGeografica.getDistritoGeograficoCollection() == null) {
            provinciaGeografica.setDistritoGeograficoCollection(new ArrayList<DistritoGeografico>());
        }
        provinciaGeografica.getProvinciaGeograficaPK().setDptoGeoId(provinciaGeografica.getDepartamentoGeografico().getDptoGeoId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DepartamentoGeografico departamentoGeografico = provinciaGeografica.getDepartamentoGeografico();
            if (departamentoGeografico != null) {
                departamentoGeografico = em.getReference(departamentoGeografico.getClass(), departamentoGeografico.getDptoGeoId());
                provinciaGeografica.setDepartamentoGeografico(departamentoGeografico);
            }
            Collection<DistritoGeografico> attachedDistritoGeograficoCollection = new ArrayList<DistritoGeografico>();
            for (DistritoGeografico distritoGeograficoCollectionDistritoGeograficoToAttach : provinciaGeografica.getDistritoGeograficoCollection()) {
                distritoGeograficoCollectionDistritoGeograficoToAttach = em.getReference(distritoGeograficoCollectionDistritoGeograficoToAttach.getClass(), distritoGeograficoCollectionDistritoGeograficoToAttach.getDistritoGeograficoPK());
                attachedDistritoGeograficoCollection.add(distritoGeograficoCollectionDistritoGeograficoToAttach);
            }
            provinciaGeografica.setDistritoGeograficoCollection(attachedDistritoGeograficoCollection);
            em.persist(provinciaGeografica);
            if (departamentoGeografico != null) {
                departamentoGeografico.getProvinciaGeograficaCollection().add(provinciaGeografica);
                departamentoGeografico = em.merge(departamentoGeografico);
            }
            for (DistritoGeografico distritoGeograficoCollectionDistritoGeografico : provinciaGeografica.getDistritoGeograficoCollection()) {
                ProvinciaGeografica oldProvinciaGeograficaOfDistritoGeograficoCollectionDistritoGeografico = distritoGeograficoCollectionDistritoGeografico.getProvinciaGeografica();
                distritoGeograficoCollectionDistritoGeografico.setProvinciaGeografica(provinciaGeografica);
                distritoGeograficoCollectionDistritoGeografico = em.merge(distritoGeograficoCollectionDistritoGeografico);
                if (oldProvinciaGeograficaOfDistritoGeograficoCollectionDistritoGeografico != null) {
                    oldProvinciaGeograficaOfDistritoGeograficoCollectionDistritoGeografico.getDistritoGeograficoCollection().remove(distritoGeograficoCollectionDistritoGeografico);
                    oldProvinciaGeograficaOfDistritoGeograficoCollectionDistritoGeografico = em.merge(oldProvinciaGeograficaOfDistritoGeograficoCollectionDistritoGeografico);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProvinciaGeografica(provinciaGeografica.getProvinciaGeograficaPK()) != null) {
                throw new PreexistingEntityException("ProvinciaGeografica " + provinciaGeografica + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProvinciaGeografica provinciaGeografica) throws IllegalOrphanException, NonexistentEntityException, Exception {
        provinciaGeografica.getProvinciaGeograficaPK().setDptoGeoId(provinciaGeografica.getDepartamentoGeografico().getDptoGeoId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProvinciaGeografica persistentProvinciaGeografica = em.find(ProvinciaGeografica.class, provinciaGeografica.getProvinciaGeograficaPK());
            DepartamentoGeografico departamentoGeograficoOld = persistentProvinciaGeografica.getDepartamentoGeografico();
            DepartamentoGeografico departamentoGeograficoNew = provinciaGeografica.getDepartamentoGeografico();
            Collection<DistritoGeografico> distritoGeograficoCollectionOld = persistentProvinciaGeografica.getDistritoGeograficoCollection();
            Collection<DistritoGeografico> distritoGeograficoCollectionNew = provinciaGeografica.getDistritoGeograficoCollection();
            List<String> illegalOrphanMessages = null;
            for (DistritoGeografico distritoGeograficoCollectionOldDistritoGeografico : distritoGeograficoCollectionOld) {
                if (!distritoGeograficoCollectionNew.contains(distritoGeograficoCollectionOldDistritoGeografico)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DistritoGeografico " + distritoGeograficoCollectionOldDistritoGeografico + " since its provinciaGeografica field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (departamentoGeograficoNew != null) {
                departamentoGeograficoNew = em.getReference(departamentoGeograficoNew.getClass(), departamentoGeograficoNew.getDptoGeoId());
                provinciaGeografica.setDepartamentoGeografico(departamentoGeograficoNew);
            }
            Collection<DistritoGeografico> attachedDistritoGeograficoCollectionNew = new ArrayList<DistritoGeografico>();
            for (DistritoGeografico distritoGeograficoCollectionNewDistritoGeograficoToAttach : distritoGeograficoCollectionNew) {
                distritoGeograficoCollectionNewDistritoGeograficoToAttach = em.getReference(distritoGeograficoCollectionNewDistritoGeograficoToAttach.getClass(), distritoGeograficoCollectionNewDistritoGeograficoToAttach.getDistritoGeograficoPK());
                attachedDistritoGeograficoCollectionNew.add(distritoGeograficoCollectionNewDistritoGeograficoToAttach);
            }
            distritoGeograficoCollectionNew = attachedDistritoGeograficoCollectionNew;
            provinciaGeografica.setDistritoGeograficoCollection(distritoGeograficoCollectionNew);
            provinciaGeografica = em.merge(provinciaGeografica);
            if (departamentoGeograficoOld != null && !departamentoGeograficoOld.equals(departamentoGeograficoNew)) {
                departamentoGeograficoOld.getProvinciaGeograficaCollection().remove(provinciaGeografica);
                departamentoGeograficoOld = em.merge(departamentoGeograficoOld);
            }
            if (departamentoGeograficoNew != null && !departamentoGeograficoNew.equals(departamentoGeograficoOld)) {
                departamentoGeograficoNew.getProvinciaGeograficaCollection().add(provinciaGeografica);
                departamentoGeograficoNew = em.merge(departamentoGeograficoNew);
            }
            for (DistritoGeografico distritoGeograficoCollectionNewDistritoGeografico : distritoGeograficoCollectionNew) {
                if (!distritoGeograficoCollectionOld.contains(distritoGeograficoCollectionNewDistritoGeografico)) {
                    ProvinciaGeografica oldProvinciaGeograficaOfDistritoGeograficoCollectionNewDistritoGeografico = distritoGeograficoCollectionNewDistritoGeografico.getProvinciaGeografica();
                    distritoGeograficoCollectionNewDistritoGeografico.setProvinciaGeografica(provinciaGeografica);
                    distritoGeograficoCollectionNewDistritoGeografico = em.merge(distritoGeograficoCollectionNewDistritoGeografico);
                    if (oldProvinciaGeograficaOfDistritoGeograficoCollectionNewDistritoGeografico != null && !oldProvinciaGeograficaOfDistritoGeograficoCollectionNewDistritoGeografico.equals(provinciaGeografica)) {
                        oldProvinciaGeograficaOfDistritoGeograficoCollectionNewDistritoGeografico.getDistritoGeograficoCollection().remove(distritoGeograficoCollectionNewDistritoGeografico);
                        oldProvinciaGeograficaOfDistritoGeograficoCollectionNewDistritoGeografico = em.merge(oldProvinciaGeograficaOfDistritoGeograficoCollectionNewDistritoGeografico);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ProvinciaGeograficaPK id = provinciaGeografica.getProvinciaGeograficaPK();
                if (findProvinciaGeografica(id) == null) {
                    throw new NonexistentEntityException("The provinciaGeografica with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ProvinciaGeograficaPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProvinciaGeografica provinciaGeografica;
            try {
                provinciaGeografica = em.getReference(ProvinciaGeografica.class, id);
                provinciaGeografica.getProvinciaGeograficaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The provinciaGeografica with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DistritoGeografico> distritoGeograficoCollectionOrphanCheck = provinciaGeografica.getDistritoGeograficoCollection();
            for (DistritoGeografico distritoGeograficoCollectionOrphanCheckDistritoGeografico : distritoGeograficoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ProvinciaGeografica (" + provinciaGeografica + ") cannot be destroyed since the DistritoGeografico " + distritoGeograficoCollectionOrphanCheckDistritoGeografico + " in its distritoGeograficoCollection field has a non-nullable provinciaGeografica field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            DepartamentoGeografico departamentoGeografico = provinciaGeografica.getDepartamentoGeografico();
            if (departamentoGeografico != null) {
                departamentoGeografico.getProvinciaGeograficaCollection().remove(provinciaGeografica);
                departamentoGeografico = em.merge(departamentoGeografico);
            }
            em.remove(provinciaGeografica);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProvinciaGeografica> findProvinciaGeograficaEntities() {
        return findProvinciaGeograficaEntities(true, -1, -1);
    }

    public List<ProvinciaGeografica> findProvinciaGeograficaEntities(int maxResults, int firstResult) {
        return findProvinciaGeograficaEntities(false, maxResults, firstResult);
    }

    private List<ProvinciaGeografica> findProvinciaGeograficaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProvinciaGeografica.class));
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

    public ProvinciaGeografica findProvinciaGeografica(ProvinciaGeograficaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProvinciaGeografica.class, id);
        } finally {
            em.close();
        }
    }

    public int getProvinciaGeograficaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProvinciaGeografica> rt = cq.from(ProvinciaGeografica.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
