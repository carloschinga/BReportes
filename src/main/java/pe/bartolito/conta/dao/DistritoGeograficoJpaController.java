/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dao;

import pe.bartolito.conta.dao.exceptions.NonexistentEntityException;
import pe.bartolito.conta.dao.exceptions.PreexistingEntityException;
import pe.bartolito.conta.dto.DistritoGeografico;
import pe.bartolito.conta.dto.DistritoGeograficoPK;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pe.bartolito.conta.dto.ProvinciaGeografica;
import pe.bartolito.conta.dto.Empresa;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author USUARIO
 */
public class DistritoGeograficoJpaController implements Serializable {

    public DistritoGeograficoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DistritoGeografico distritoGeografico) throws PreexistingEntityException, Exception {
        if (distritoGeografico.getDistritoGeograficoPK() == null) {
            distritoGeografico.setDistritoGeograficoPK(new DistritoGeograficoPK());
        }
        if (distritoGeografico.getEmpresaCollection() == null) {
            distritoGeografico.setEmpresaCollection(new ArrayList<Empresa>());
        }
        distritoGeografico.getDistritoGeograficoPK().setProvGeoId(distritoGeografico.getProvinciaGeografica().getProvinciaGeograficaPK().getProvGeoId());
        distritoGeografico.getDistritoGeograficoPK().setDptoGeoId(distritoGeografico.getProvinciaGeografica().getProvinciaGeograficaPK().getDptoGeoId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProvinciaGeografica provinciaGeografica = distritoGeografico.getProvinciaGeografica();
            if (provinciaGeografica != null) {
                provinciaGeografica = em.getReference(provinciaGeografica.getClass(), provinciaGeografica.getProvinciaGeograficaPK());
                distritoGeografico.setProvinciaGeografica(provinciaGeografica);
            }
            Collection<Empresa> attachedEmpresaCollection = new ArrayList<Empresa>();
            for (Empresa empresaCollectionEmpresaToAttach : distritoGeografico.getEmpresaCollection()) {
                empresaCollectionEmpresaToAttach = em.getReference(empresaCollectionEmpresaToAttach.getClass(), empresaCollectionEmpresaToAttach.getEmpresaId());
                attachedEmpresaCollection.add(empresaCollectionEmpresaToAttach);
            }
            distritoGeografico.setEmpresaCollection(attachedEmpresaCollection);
            em.persist(distritoGeografico);
            if (provinciaGeografica != null) {
                provinciaGeografica.getDistritoGeograficoCollection().add(distritoGeografico);
                provinciaGeografica = em.merge(provinciaGeografica);
            }
            for (Empresa empresaCollectionEmpresa : distritoGeografico.getEmpresaCollection()) {
                DistritoGeografico oldDistritoGeograficoOfEmpresaCollectionEmpresa = empresaCollectionEmpresa.getDistritoGeografico();
                empresaCollectionEmpresa.setDistritoGeografico(distritoGeografico);
                empresaCollectionEmpresa = em.merge(empresaCollectionEmpresa);
                if (oldDistritoGeograficoOfEmpresaCollectionEmpresa != null) {
                    oldDistritoGeograficoOfEmpresaCollectionEmpresa.getEmpresaCollection().remove(empresaCollectionEmpresa);
                    oldDistritoGeograficoOfEmpresaCollectionEmpresa = em.merge(oldDistritoGeograficoOfEmpresaCollectionEmpresa);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDistritoGeografico(distritoGeografico.getDistritoGeograficoPK()) != null) {
                throw new PreexistingEntityException("DistritoGeografico " + distritoGeografico + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DistritoGeografico distritoGeografico) throws NonexistentEntityException, Exception {
        distritoGeografico.getDistritoGeograficoPK().setProvGeoId(distritoGeografico.getProvinciaGeografica().getProvinciaGeograficaPK().getProvGeoId());
        distritoGeografico.getDistritoGeograficoPK().setDptoGeoId(distritoGeografico.getProvinciaGeografica().getProvinciaGeograficaPK().getDptoGeoId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DistritoGeografico persistentDistritoGeografico = em.find(DistritoGeografico.class, distritoGeografico.getDistritoGeograficoPK());
            ProvinciaGeografica provinciaGeograficaOld = persistentDistritoGeografico.getProvinciaGeografica();
            ProvinciaGeografica provinciaGeograficaNew = distritoGeografico.getProvinciaGeografica();
            Collection<Empresa> empresaCollectionOld = persistentDistritoGeografico.getEmpresaCollection();
            Collection<Empresa> empresaCollectionNew = distritoGeografico.getEmpresaCollection();
            if (provinciaGeograficaNew != null) {
                provinciaGeograficaNew = em.getReference(provinciaGeograficaNew.getClass(), provinciaGeograficaNew.getProvinciaGeograficaPK());
                distritoGeografico.setProvinciaGeografica(provinciaGeograficaNew);
            }
            Collection<Empresa> attachedEmpresaCollectionNew = new ArrayList<Empresa>();
            for (Empresa empresaCollectionNewEmpresaToAttach : empresaCollectionNew) {
                empresaCollectionNewEmpresaToAttach = em.getReference(empresaCollectionNewEmpresaToAttach.getClass(), empresaCollectionNewEmpresaToAttach.getEmpresaId());
                attachedEmpresaCollectionNew.add(empresaCollectionNewEmpresaToAttach);
            }
            empresaCollectionNew = attachedEmpresaCollectionNew;
            distritoGeografico.setEmpresaCollection(empresaCollectionNew);
            distritoGeografico = em.merge(distritoGeografico);
            if (provinciaGeograficaOld != null && !provinciaGeograficaOld.equals(provinciaGeograficaNew)) {
                provinciaGeograficaOld.getDistritoGeograficoCollection().remove(distritoGeografico);
                provinciaGeograficaOld = em.merge(provinciaGeograficaOld);
            }
            if (provinciaGeograficaNew != null && !provinciaGeograficaNew.equals(provinciaGeograficaOld)) {
                provinciaGeograficaNew.getDistritoGeograficoCollection().add(distritoGeografico);
                provinciaGeograficaNew = em.merge(provinciaGeograficaNew);
            }
            for (Empresa empresaCollectionOldEmpresa : empresaCollectionOld) {
                if (!empresaCollectionNew.contains(empresaCollectionOldEmpresa)) {
                    empresaCollectionOldEmpresa.setDistritoGeografico(null);
                    empresaCollectionOldEmpresa = em.merge(empresaCollectionOldEmpresa);
                }
            }
            for (Empresa empresaCollectionNewEmpresa : empresaCollectionNew) {
                if (!empresaCollectionOld.contains(empresaCollectionNewEmpresa)) {
                    DistritoGeografico oldDistritoGeograficoOfEmpresaCollectionNewEmpresa = empresaCollectionNewEmpresa.getDistritoGeografico();
                    empresaCollectionNewEmpresa.setDistritoGeografico(distritoGeografico);
                    empresaCollectionNewEmpresa = em.merge(empresaCollectionNewEmpresa);
                    if (oldDistritoGeograficoOfEmpresaCollectionNewEmpresa != null && !oldDistritoGeograficoOfEmpresaCollectionNewEmpresa.equals(distritoGeografico)) {
                        oldDistritoGeograficoOfEmpresaCollectionNewEmpresa.getEmpresaCollection().remove(empresaCollectionNewEmpresa);
                        oldDistritoGeograficoOfEmpresaCollectionNewEmpresa = em.merge(oldDistritoGeograficoOfEmpresaCollectionNewEmpresa);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                DistritoGeograficoPK id = distritoGeografico.getDistritoGeograficoPK();
                if (findDistritoGeografico(id) == null) {
                    throw new NonexistentEntityException("The distritoGeografico with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(DistritoGeograficoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DistritoGeografico distritoGeografico;
            try {
                distritoGeografico = em.getReference(DistritoGeografico.class, id);
                distritoGeografico.getDistritoGeograficoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The distritoGeografico with id " + id + " no longer exists.", enfe);
            }
            ProvinciaGeografica provinciaGeografica = distritoGeografico.getProvinciaGeografica();
            if (provinciaGeografica != null) {
                provinciaGeografica.getDistritoGeograficoCollection().remove(distritoGeografico);
                provinciaGeografica = em.merge(provinciaGeografica);
            }
            Collection<Empresa> empresaCollection = distritoGeografico.getEmpresaCollection();
            for (Empresa empresaCollectionEmpresa : empresaCollection) {
                empresaCollectionEmpresa.setDistritoGeografico(null);
                empresaCollectionEmpresa = em.merge(empresaCollectionEmpresa);
            }
            em.remove(distritoGeografico);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DistritoGeografico> findDistritoGeograficoEntities() {
        return findDistritoGeograficoEntities(true, -1, -1);
    }

    public List<DistritoGeografico> findDistritoGeograficoEntities(int maxResults, int firstResult) {
        return findDistritoGeograficoEntities(false, maxResults, firstResult);
    }

    private List<DistritoGeografico> findDistritoGeograficoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DistritoGeografico.class));
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

    public DistritoGeografico findDistritoGeografico(DistritoGeograficoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DistritoGeografico.class, id);
        } finally {
            em.close();
        }
    }

    public int getDistritoGeograficoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DistritoGeografico> rt = cq.from(DistritoGeografico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
