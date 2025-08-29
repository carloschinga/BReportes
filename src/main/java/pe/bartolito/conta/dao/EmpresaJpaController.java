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
import pe.bartolito.conta.dto.DistritoGeografico;
import pe.bartolito.conta.dto.Empresa;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author USUARIO
 */
public class EmpresaJpaController implements Serializable {

    public EmpresaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empresa empresa) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DistritoGeografico distritoGeografico = empresa.getDistritoGeografico();
            if (distritoGeografico != null) {
                distritoGeografico = em.getReference(distritoGeografico.getClass(), distritoGeografico.getDistritoGeograficoPK());
                empresa.setDistritoGeografico(distritoGeografico);
            }
            em.persist(empresa);
            if (distritoGeografico != null) {
                distritoGeografico.getEmpresaCollection().add(empresa);
                distritoGeografico = em.merge(distritoGeografico);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmpresa(empresa.getEmpresaId()) != null) {
                throw new PreexistingEntityException("Empresa " + empresa + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empresa empresa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa persistentEmpresa = em.find(Empresa.class, empresa.getEmpresaId());
            DistritoGeografico distritoGeograficoOld = persistentEmpresa.getDistritoGeografico();
            DistritoGeografico distritoGeograficoNew = empresa.getDistritoGeografico();
            if (distritoGeograficoNew != null) {
                distritoGeograficoNew = em.getReference(distritoGeograficoNew.getClass(), distritoGeograficoNew.getDistritoGeograficoPK());
                empresa.setDistritoGeografico(distritoGeograficoNew);
            }
            empresa = em.merge(empresa);
            if (distritoGeograficoOld != null && !distritoGeograficoOld.equals(distritoGeograficoNew)) {
                distritoGeograficoOld.getEmpresaCollection().remove(empresa);
                distritoGeograficoOld = em.merge(distritoGeograficoOld);
            }
            if (distritoGeograficoNew != null && !distritoGeograficoNew.equals(distritoGeograficoOld)) {
                distritoGeograficoNew.getEmpresaCollection().add(empresa);
                distritoGeograficoNew = em.merge(distritoGeograficoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = empresa.getEmpresaId();
                if (findEmpresa(id) == null) {
                    throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.");
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
            Empresa empresa;
            try {
                empresa = em.getReference(Empresa.class, id);
                empresa.getEmpresaId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.", enfe);
            }
            DistritoGeografico distritoGeografico = empresa.getDistritoGeografico();
            if (distritoGeografico != null) {
                distritoGeografico.getEmpresaCollection().remove(empresa);
                distritoGeografico = em.merge(distritoGeografico);
            }
            em.remove(empresa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empresa> findEmpresaEntities() {
        return findEmpresaEntities(true, -1, -1);
    }

    public List<Empresa> findEmpresaEntities(int maxResults, int firstResult) {
        return findEmpresaEntities(false, maxResults, firstResult);
    }

    private List<Empresa> findEmpresaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empresa.class));
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

    public Empresa findEmpresa(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empresa.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpresaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empresa> rt = cq.from(Empresa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
