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
import pe.bartolito.conta.dto.CuentaCorriente;
import pe.bartolito.conta.dto.TipoPersona;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author USUARIO
 */
public class TipoPersonaJpaController implements Serializable {

    public TipoPersonaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoPersona tipoPersona) throws PreexistingEntityException, Exception {
        if (tipoPersona.getCuentaCorrienteList() == null) {
            tipoPersona.setCuentaCorrienteList(new ArrayList<CuentaCorriente>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<CuentaCorriente> attachedCuentaCorrienteList = new ArrayList<CuentaCorriente>();
            for (CuentaCorriente cuentaCorrienteListCuentaCorrienteToAttach : tipoPersona.getCuentaCorrienteList()) {
                cuentaCorrienteListCuentaCorrienteToAttach = em.getReference(cuentaCorrienteListCuentaCorrienteToAttach.getClass(), cuentaCorrienteListCuentaCorrienteToAttach.getCtaCteId());
                attachedCuentaCorrienteList.add(cuentaCorrienteListCuentaCorrienteToAttach);
            }
            tipoPersona.setCuentaCorrienteList(attachedCuentaCorrienteList);
            em.persist(tipoPersona);
            for (CuentaCorriente cuentaCorrienteListCuentaCorriente : tipoPersona.getCuentaCorrienteList()) {
                TipoPersona oldPersonaIdOfCuentaCorrienteListCuentaCorriente = cuentaCorrienteListCuentaCorriente.getPersonaId();
                cuentaCorrienteListCuentaCorriente.setPersonaId(tipoPersona);
                cuentaCorrienteListCuentaCorriente = em.merge(cuentaCorrienteListCuentaCorriente);
                if (oldPersonaIdOfCuentaCorrienteListCuentaCorriente != null) {
                    oldPersonaIdOfCuentaCorrienteListCuentaCorriente.getCuentaCorrienteList().remove(cuentaCorrienteListCuentaCorriente);
                    oldPersonaIdOfCuentaCorrienteListCuentaCorriente = em.merge(oldPersonaIdOfCuentaCorrienteListCuentaCorriente);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTipoPersona(tipoPersona.getPersonaId()) != null) {
                throw new PreexistingEntityException("TipoPersona " + tipoPersona + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoPersona tipoPersona) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoPersona persistentTipoPersona = em.find(TipoPersona.class, tipoPersona.getPersonaId());
            List<CuentaCorriente> cuentaCorrienteListOld = persistentTipoPersona.getCuentaCorrienteList();
            List<CuentaCorriente> cuentaCorrienteListNew = tipoPersona.getCuentaCorrienteList();
            List<CuentaCorriente> attachedCuentaCorrienteListNew = new ArrayList<CuentaCorriente>();
            for (CuentaCorriente cuentaCorrienteListNewCuentaCorrienteToAttach : cuentaCorrienteListNew) {
                cuentaCorrienteListNewCuentaCorrienteToAttach = em.getReference(cuentaCorrienteListNewCuentaCorrienteToAttach.getClass(), cuentaCorrienteListNewCuentaCorrienteToAttach.getCtaCteId());
                attachedCuentaCorrienteListNew.add(cuentaCorrienteListNewCuentaCorrienteToAttach);
            }
            cuentaCorrienteListNew = attachedCuentaCorrienteListNew;
            tipoPersona.setCuentaCorrienteList(cuentaCorrienteListNew);
            tipoPersona = em.merge(tipoPersona);
            for (CuentaCorriente cuentaCorrienteListOldCuentaCorriente : cuentaCorrienteListOld) {
                if (!cuentaCorrienteListNew.contains(cuentaCorrienteListOldCuentaCorriente)) {
                    cuentaCorrienteListOldCuentaCorriente.setPersonaId(null);
                    cuentaCorrienteListOldCuentaCorriente = em.merge(cuentaCorrienteListOldCuentaCorriente);
                }
            }
            for (CuentaCorriente cuentaCorrienteListNewCuentaCorriente : cuentaCorrienteListNew) {
                if (!cuentaCorrienteListOld.contains(cuentaCorrienteListNewCuentaCorriente)) {
                    TipoPersona oldPersonaIdOfCuentaCorrienteListNewCuentaCorriente = cuentaCorrienteListNewCuentaCorriente.getPersonaId();
                    cuentaCorrienteListNewCuentaCorriente.setPersonaId(tipoPersona);
                    cuentaCorrienteListNewCuentaCorriente = em.merge(cuentaCorrienteListNewCuentaCorriente);
                    if (oldPersonaIdOfCuentaCorrienteListNewCuentaCorriente != null && !oldPersonaIdOfCuentaCorrienteListNewCuentaCorriente.equals(tipoPersona)) {
                        oldPersonaIdOfCuentaCorrienteListNewCuentaCorriente.getCuentaCorrienteList().remove(cuentaCorrienteListNewCuentaCorriente);
                        oldPersonaIdOfCuentaCorrienteListNewCuentaCorriente = em.merge(oldPersonaIdOfCuentaCorrienteListNewCuentaCorriente);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = tipoPersona.getPersonaId();
                if (findTipoPersona(id) == null) {
                    throw new NonexistentEntityException("The tipoPersona with id " + id + " no longer exists.");
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
            TipoPersona tipoPersona;
            try {
                tipoPersona = em.getReference(TipoPersona.class, id);
                tipoPersona.getPersonaId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoPersona with id " + id + " no longer exists.", enfe);
            }
            List<CuentaCorriente> cuentaCorrienteList = tipoPersona.getCuentaCorrienteList();
            for (CuentaCorriente cuentaCorrienteListCuentaCorriente : cuentaCorrienteList) {
                cuentaCorrienteListCuentaCorriente.setPersonaId(null);
                cuentaCorrienteListCuentaCorriente = em.merge(cuentaCorrienteListCuentaCorriente);
            }
            em.remove(tipoPersona);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoPersona> findTipoPersonaEntities() {
        return findTipoPersonaEntities(true, -1, -1);
    }

    public List<TipoPersona> findTipoPersonaEntities(int maxResults, int firstResult) {
        return findTipoPersonaEntities(false, maxResults, firstResult);
    }

    private List<TipoPersona> findTipoPersonaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoPersona.class));
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

    public TipoPersona findTipoPersona(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoPersona.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoPersonaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoPersona> rt = cq.from(TipoPersona.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
