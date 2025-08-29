/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dao;

import pe.bartolito.conta.dao.exceptions.IllegalOrphanException;
import pe.bartolito.conta.dao.exceptions.NonexistentEntityException;
import pe.bartolito.conta.dao.exceptions.PreexistingEntityException;
import pe.bartolito.conta.dto.DiarioCabecera;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import pe.bartolito.conta.dto.SubMovimientoContable;
import pe.bartolito.conta.dto.DiarioDetalle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author USUARIO
 */
public class DiarioCabeceraJpaController implements Serializable {

    public DiarioCabeceraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public List<DiarioCabecera> findDiarioCabeceraByFecha(Date fecha) {
        EntityManager em = getEntityManager();
        try {
            String query = "SELECT c FROM DiarioCabecera c WHERE c.diaCabFec = :fecha";
            return em.createQuery(query, DiarioCabecera.class)
                    .setParameter("fecha", fecha)
                    .getResultList();
        }
        catch(Exception ex){
            String mensaje=ex.getMessage();
            return null;
        }
        finally {
            em.close();
        }
    }

    public void create(DiarioCabecera diarioCabecera) throws PreexistingEntityException, Exception {
        if (diarioCabecera.getDiarioDetalleList() == null) {
            diarioCabecera.setDiarioDetalleList(new ArrayList<DiarioDetalle>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SubMovimientoContable subMovimientoContable = diarioCabecera.getSubMovimientoContable();
            if (subMovimientoContable != null) {
                subMovimientoContable = em.getReference(subMovimientoContable.getClass(), subMovimientoContable.getSubMovimientoContablePK());
                diarioCabecera.setSubMovimientoContable(subMovimientoContable);
            }
            List<DiarioDetalle> attachedDiarioDetalleList = new ArrayList<DiarioDetalle>();
            for (DiarioDetalle diarioDetalleListDiarioDetalleToAttach : diarioCabecera.getDiarioDetalleList()) {
                diarioDetalleListDiarioDetalleToAttach = em.getReference(diarioDetalleListDiarioDetalleToAttach.getClass(), diarioDetalleListDiarioDetalleToAttach.getDiarioDetallePK());
                attachedDiarioDetalleList.add(diarioDetalleListDiarioDetalleToAttach);
            }
            diarioCabecera.setDiarioDetalleList(attachedDiarioDetalleList);
            em.persist(diarioCabecera);
            if (subMovimientoContable != null) {
                subMovimientoContable.getDiarioCabeceraCollection().add(diarioCabecera);
                subMovimientoContable = em.merge(subMovimientoContable);
            }
            for (DiarioDetalle diarioDetalleListDiarioDetalle : diarioCabecera.getDiarioDetalleList()) {
                DiarioCabecera oldDiarioCabeceraOfDiarioDetalleListDiarioDetalle = diarioDetalleListDiarioDetalle.getDiarioCabecera();
                diarioDetalleListDiarioDetalle.setDiarioCabecera(diarioCabecera);
                diarioDetalleListDiarioDetalle = em.merge(diarioDetalleListDiarioDetalle);
                if (oldDiarioCabeceraOfDiarioDetalleListDiarioDetalle != null) {
                    oldDiarioCabeceraOfDiarioDetalleListDiarioDetalle.getDiarioDetalleList().remove(diarioDetalleListDiarioDetalle);
                    oldDiarioCabeceraOfDiarioDetalleListDiarioDetalle = em.merge(oldDiarioCabeceraOfDiarioDetalleListDiarioDetalle);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDiarioCabecera(diarioCabecera.getDiaCabCompId()) != null) {
                throw new PreexistingEntityException("DiarioCabecera " + diarioCabecera + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DiarioCabecera diarioCabecera) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DiarioCabecera persistentDiarioCabecera = em.find(DiarioCabecera.class, diarioCabecera.getDiaCabCompId());
            SubMovimientoContable subMovimientoContableOld = persistentDiarioCabecera.getSubMovimientoContable();
            SubMovimientoContable subMovimientoContableNew = diarioCabecera.getSubMovimientoContable();
            List<DiarioDetalle> diarioDetalleListOld = persistentDiarioCabecera.getDiarioDetalleList();
            List<DiarioDetalle> diarioDetalleListNew = diarioCabecera.getDiarioDetalleList();
            List<String> illegalOrphanMessages = null;
            for (DiarioDetalle diarioDetalleListOldDiarioDetalle : diarioDetalleListOld) {
                if (!diarioDetalleListNew.contains(diarioDetalleListOldDiarioDetalle)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DiarioDetalle " + diarioDetalleListOldDiarioDetalle + " since its diarioCabecera field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (subMovimientoContableNew != null) {
                subMovimientoContableNew = em.getReference(subMovimientoContableNew.getClass(), subMovimientoContableNew.getSubMovimientoContablePK());
                diarioCabecera.setSubMovimientoContable(subMovimientoContableNew);
            }
            List<DiarioDetalle> attachedDiarioDetalleListNew = new ArrayList<DiarioDetalle>();
            for (DiarioDetalle diarioDetalleListNewDiarioDetalleToAttach : diarioDetalleListNew) {
                diarioDetalleListNewDiarioDetalleToAttach = em.getReference(diarioDetalleListNewDiarioDetalleToAttach.getClass(), diarioDetalleListNewDiarioDetalleToAttach.getDiarioDetallePK());
                attachedDiarioDetalleListNew.add(diarioDetalleListNewDiarioDetalleToAttach);
            }
            diarioDetalleListNew = attachedDiarioDetalleListNew;
            diarioCabecera.setDiarioDetalleList(diarioDetalleListNew);
            diarioCabecera = em.merge(diarioCabecera);
            if (subMovimientoContableOld != null && !subMovimientoContableOld.equals(subMovimientoContableNew)) {
                subMovimientoContableOld.getDiarioCabeceraCollection().remove(diarioCabecera);
                subMovimientoContableOld = em.merge(subMovimientoContableOld);
            }
            if (subMovimientoContableNew != null && !subMovimientoContableNew.equals(subMovimientoContableOld)) {
                subMovimientoContableNew.getDiarioCabeceraCollection().add(diarioCabecera);
                subMovimientoContableNew = em.merge(subMovimientoContableNew);
            }
            for (DiarioDetalle diarioDetalleListNewDiarioDetalle : diarioDetalleListNew) {
                if (!diarioDetalleListOld.contains(diarioDetalleListNewDiarioDetalle)) {
                    DiarioCabecera oldDiarioCabeceraOfDiarioDetalleListNewDiarioDetalle = diarioDetalleListNewDiarioDetalle.getDiarioCabecera();
                    diarioDetalleListNewDiarioDetalle.setDiarioCabecera(diarioCabecera);
                    diarioDetalleListNewDiarioDetalle = em.merge(diarioDetalleListNewDiarioDetalle);
                    if (oldDiarioCabeceraOfDiarioDetalleListNewDiarioDetalle != null && !oldDiarioCabeceraOfDiarioDetalleListNewDiarioDetalle.equals(diarioCabecera)) {
                        oldDiarioCabeceraOfDiarioDetalleListNewDiarioDetalle.getDiarioDetalleList().remove(diarioDetalleListNewDiarioDetalle);
                        oldDiarioCabeceraOfDiarioDetalleListNewDiarioDetalle = em.merge(oldDiarioCabeceraOfDiarioDetalleListNewDiarioDetalle);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = diarioCabecera.getDiaCabCompId();
                if (findDiarioCabecera(id) == null) {
                    throw new NonexistentEntityException("The diarioCabecera with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DiarioCabecera diarioCabecera;
            try {
                diarioCabecera = em.getReference(DiarioCabecera.class, id);
                diarioCabecera.getDiaCabCompId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The diarioCabecera with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<DiarioDetalle> diarioDetalleListOrphanCheck = diarioCabecera.getDiarioDetalleList();
            for (DiarioDetalle diarioDetalleListOrphanCheckDiarioDetalle : diarioDetalleListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DiarioCabecera (" + diarioCabecera + ") cannot be destroyed since the DiarioDetalle " + diarioDetalleListOrphanCheckDiarioDetalle + " in its diarioDetalleList field has a non-nullable diarioCabecera field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            SubMovimientoContable subMovimientoContable = diarioCabecera.getSubMovimientoContable();
            if (subMovimientoContable != null) {
                subMovimientoContable.getDiarioCabeceraCollection().remove(diarioCabecera);
                subMovimientoContable = em.merge(subMovimientoContable);
            }
            em.remove(diarioCabecera);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DiarioCabecera> findDiarioCabeceraEntities() {
        return findDiarioCabeceraEntities(true, -1, -1);
    }

    public List<DiarioCabecera> findDiarioCabeceraEntities(int maxResults, int firstResult) {
        return findDiarioCabeceraEntities(false, maxResults, firstResult);
    }

    private List<DiarioCabecera> findDiarioCabeceraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DiarioCabecera.class));
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

    public DiarioCabecera findDiarioCabecera(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DiarioCabecera.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiarioCabeceraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DiarioCabecera> rt = cq.from(DiarioCabecera.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
