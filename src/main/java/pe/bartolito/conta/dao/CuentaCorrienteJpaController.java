/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dao;

import pe.bartolito.conta.dao.exceptions.NonexistentEntityException;
import pe.bartolito.conta.dao.exceptions.PreexistingEntityException;
import pe.bartolito.conta.dto.CuentaCorriente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pe.bartolito.conta.dto.TipoPersona;
import pe.bartolito.conta.dto.DiarioDetalle;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author USUARIO
 */
public class CuentaCorrienteJpaController implements Serializable {

    public CuentaCorrienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CuentaCorriente cuentaCorriente) throws PreexistingEntityException, Exception {
        if (cuentaCorriente.getDiarioDetalleList() == null) {
            cuentaCorriente.setDiarioDetalleList(new ArrayList<DiarioDetalle>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TipoPersona personaId = cuentaCorriente.getPersonaId();
            if (personaId != null) {
                personaId = em.getReference(personaId.getClass(), personaId.getPersonaId());
                cuentaCorriente.setPersonaId(personaId);
            }
            List<DiarioDetalle> attachedDiarioDetalleList = new ArrayList<DiarioDetalle>();
            for (DiarioDetalle diarioDetalleListDiarioDetalleToAttach : cuentaCorriente.getDiarioDetalleList()) {
                diarioDetalleListDiarioDetalleToAttach = em.getReference(diarioDetalleListDiarioDetalleToAttach.getClass(), diarioDetalleListDiarioDetalleToAttach.getDiarioDetallePK());
                attachedDiarioDetalleList.add(diarioDetalleListDiarioDetalleToAttach);
            }
            cuentaCorriente.setDiarioDetalleList(attachedDiarioDetalleList);
            em.persist(cuentaCorriente);
            if (personaId != null) {
                personaId.getCuentaCorrienteList().add(cuentaCorriente);
                personaId = em.merge(personaId);
            }
            for (DiarioDetalle diarioDetalleListDiarioDetalle : cuentaCorriente.getDiarioDetalleList()) {
                CuentaCorriente oldCtaCteIdOfDiarioDetalleListDiarioDetalle = diarioDetalleListDiarioDetalle.getCtaCteId();
                diarioDetalleListDiarioDetalle.setCtaCteId(cuentaCorriente);
                diarioDetalleListDiarioDetalle = em.merge(diarioDetalleListDiarioDetalle);
                if (oldCtaCteIdOfDiarioDetalleListDiarioDetalle != null) {
                    oldCtaCteIdOfDiarioDetalleListDiarioDetalle.getDiarioDetalleList().remove(diarioDetalleListDiarioDetalle);
                    oldCtaCteIdOfDiarioDetalleListDiarioDetalle = em.merge(oldCtaCteIdOfDiarioDetalleListDiarioDetalle);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCuentaCorriente(cuentaCorriente.getCtaCteId()) != null) {
                throw new PreexistingEntityException("CuentaCorriente " + cuentaCorriente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CuentaCorriente cuentaCorriente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CuentaCorriente persistentCuentaCorriente = em.find(CuentaCorriente.class, cuentaCorriente.getCtaCteId());
            TipoPersona personaIdOld = persistentCuentaCorriente.getPersonaId();
            TipoPersona personaIdNew = cuentaCorriente.getPersonaId();
            List<DiarioDetalle> diarioDetalleListOld = persistentCuentaCorriente.getDiarioDetalleList();
            List<DiarioDetalle> diarioDetalleListNew = cuentaCorriente.getDiarioDetalleList();
            if (personaIdNew != null) {
                personaIdNew = em.getReference(personaIdNew.getClass(), personaIdNew.getPersonaId());
                cuentaCorriente.setPersonaId(personaIdNew);
            }
            List<DiarioDetalle> attachedDiarioDetalleListNew = new ArrayList<DiarioDetalle>();
            for (DiarioDetalle diarioDetalleListNewDiarioDetalleToAttach : diarioDetalleListNew) {
                diarioDetalleListNewDiarioDetalleToAttach = em.getReference(diarioDetalleListNewDiarioDetalleToAttach.getClass(), diarioDetalleListNewDiarioDetalleToAttach.getDiarioDetallePK());
                attachedDiarioDetalleListNew.add(diarioDetalleListNewDiarioDetalleToAttach);
            }
            diarioDetalleListNew = attachedDiarioDetalleListNew;
            cuentaCorriente.setDiarioDetalleList(diarioDetalleListNew);
            cuentaCorriente = em.merge(cuentaCorriente);
            if (personaIdOld != null && !personaIdOld.equals(personaIdNew)) {
                personaIdOld.getCuentaCorrienteList().remove(cuentaCorriente);
                personaIdOld = em.merge(personaIdOld);
            }
            if (personaIdNew != null && !personaIdNew.equals(personaIdOld)) {
                personaIdNew.getCuentaCorrienteList().add(cuentaCorriente);
                personaIdNew = em.merge(personaIdNew);
            }
            for (DiarioDetalle diarioDetalleListOldDiarioDetalle : diarioDetalleListOld) {
                if (!diarioDetalleListNew.contains(diarioDetalleListOldDiarioDetalle)) {
                    diarioDetalleListOldDiarioDetalle.setCtaCteId(null);
                    diarioDetalleListOldDiarioDetalle = em.merge(diarioDetalleListOldDiarioDetalle);
                }
            }
            for (DiarioDetalle diarioDetalleListNewDiarioDetalle : diarioDetalleListNew) {
                if (!diarioDetalleListOld.contains(diarioDetalleListNewDiarioDetalle)) {
                    CuentaCorriente oldCtaCteIdOfDiarioDetalleListNewDiarioDetalle = diarioDetalleListNewDiarioDetalle.getCtaCteId();
                    diarioDetalleListNewDiarioDetalle.setCtaCteId(cuentaCorriente);
                    diarioDetalleListNewDiarioDetalle = em.merge(diarioDetalleListNewDiarioDetalle);
                    if (oldCtaCteIdOfDiarioDetalleListNewDiarioDetalle != null && !oldCtaCteIdOfDiarioDetalleListNewDiarioDetalle.equals(cuentaCorriente)) {
                        oldCtaCteIdOfDiarioDetalleListNewDiarioDetalle.getDiarioDetalleList().remove(diarioDetalleListNewDiarioDetalle);
                        oldCtaCteIdOfDiarioDetalleListNewDiarioDetalle = em.merge(oldCtaCteIdOfDiarioDetalleListNewDiarioDetalle);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = cuentaCorriente.getCtaCteId();
                if (findCuentaCorriente(id) == null) {
                    throw new NonexistentEntityException("The cuentaCorriente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CuentaCorriente cuentaCorriente;
            try {
                cuentaCorriente = em.getReference(CuentaCorriente.class, id);
                cuentaCorriente.getCtaCteId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cuentaCorriente with id " + id + " no longer exists.", enfe);
            }
            TipoPersona personaId = cuentaCorriente.getPersonaId();
            if (personaId != null) {
                personaId.getCuentaCorrienteList().remove(cuentaCorriente);
                personaId = em.merge(personaId);
            }
            List<DiarioDetalle> diarioDetalleList = cuentaCorriente.getDiarioDetalleList();
            for (DiarioDetalle diarioDetalleListDiarioDetalle : diarioDetalleList) {
                diarioDetalleListDiarioDetalle.setCtaCteId(null);
                diarioDetalleListDiarioDetalle = em.merge(diarioDetalleListDiarioDetalle);
            }
            em.remove(cuentaCorriente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CuentaCorriente> findCuentaCorrienteEntities() {
        return findCuentaCorrienteEntities(true, -1, -1);
    }

    public List<CuentaCorriente> findCuentaCorrienteEntities(int maxResults, int firstResult) {
        return findCuentaCorrienteEntities(false, maxResults, firstResult);
    }

    private List<CuentaCorriente> findCuentaCorrienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CuentaCorriente.class));
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

    public CuentaCorriente findCuentaCorriente(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CuentaCorriente.class, id);
        } finally {
            em.close();
        }
    }

    public int getCuentaCorrienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CuentaCorriente> rt = cq.from(CuentaCorriente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
