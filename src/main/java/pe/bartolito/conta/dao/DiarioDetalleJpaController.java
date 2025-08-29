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
import pe.bartolito.conta.dto.DiarioCabecera;
import pe.bartolito.conta.dto.DiarioDetalle;
import pe.bartolito.conta.dto.DiarioDetallePK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author USUARIO
 */
public class DiarioDetalleJpaController implements Serializable {

    public DiarioDetalleJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public List<DiarioDetalle> findDiarioDetalleByCabecera(Long DiaCabCompId) {
        EntityManager em = getEntityManager();
        try {
            // Definimos la consulta SQL nativa para obtener los detalles asociados al DiaCabCompId
            String sql = "SELECT * FROM DiarioDetalle d WHERE d.DiaCabCompId = :DiaCabCompId";

            // Ejecutamos la consulta nativa
            Query query = em.createNativeQuery(sql, DiarioDetalle.class);
            query.setParameter("DiaCabCompId", DiaCabCompId);

            // Obtenemos los resultados
            return query.getResultList();
        } catch (Exception ex) {
            String mensaje = ex.getMessage();
            return null;
        } finally {
            em.close(); // Aseguramos que el EntityManager se cierre para liberar recursos
        }
    }

    public void create(DiarioDetalle diarioDetalle) throws PreexistingEntityException, Exception {
        if (diarioDetalle.getDiarioDetallePK() == null) {
            diarioDetalle.setDiarioDetallePK(new DiarioDetallePK());
        }
        diarioDetalle.getDiarioDetallePK().setDiaCabCompId(diarioDetalle.getDiarioCabecera().getDiaCabCompId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CuentaCorriente ctaCteId = diarioDetalle.getCtaCteId();
            if (ctaCteId != null) {
                ctaCteId = em.getReference(ctaCteId.getClass(), ctaCteId.getCtaCteId());
                diarioDetalle.setCtaCteId(ctaCteId);
            }
            DiarioCabecera diarioCabecera = diarioDetalle.getDiarioCabecera();
            if (diarioCabecera != null) {
                diarioCabecera = em.getReference(diarioCabecera.getClass(), diarioCabecera.getDiaCabCompId());
                diarioDetalle.setDiarioCabecera(diarioCabecera);
            }
            em.persist(diarioDetalle);
            if (ctaCteId != null) {
                ctaCteId.getDiarioDetalleList().add(diarioDetalle);
                ctaCteId = em.merge(ctaCteId);
            }
            if (diarioCabecera != null) {
                diarioCabecera.getDiarioDetalleList().add(diarioDetalle);
                diarioCabecera = em.merge(diarioCabecera);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDiarioDetalle(diarioDetalle.getDiarioDetallePK()) != null) {
                throw new PreexistingEntityException("DiarioDetalle " + diarioDetalle + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DiarioDetalle diarioDetalle) throws NonexistentEntityException, Exception {
        diarioDetalle.getDiarioDetallePK().setDiaCabCompId(diarioDetalle.getDiarioCabecera().getDiaCabCompId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DiarioDetalle persistentDiarioDetalle = em.find(DiarioDetalle.class, diarioDetalle.getDiarioDetallePK());
            CuentaCorriente ctaCteIdOld = persistentDiarioDetalle.getCtaCteId();
            CuentaCorriente ctaCteIdNew = diarioDetalle.getCtaCteId();
            DiarioCabecera diarioCabeceraOld = persistentDiarioDetalle.getDiarioCabecera();
            DiarioCabecera diarioCabeceraNew = diarioDetalle.getDiarioCabecera();
            if (ctaCteIdNew != null) {
                ctaCteIdNew = em.getReference(ctaCteIdNew.getClass(), ctaCteIdNew.getCtaCteId());
                diarioDetalle.setCtaCteId(ctaCteIdNew);
            }
            if (diarioCabeceraNew != null) {
                diarioCabeceraNew = em.getReference(diarioCabeceraNew.getClass(), diarioCabeceraNew.getDiaCabCompId());
                diarioDetalle.setDiarioCabecera(diarioCabeceraNew);
            }
            diarioDetalle = em.merge(diarioDetalle);
            if (ctaCteIdOld != null && !ctaCteIdOld.equals(ctaCteIdNew)) {
                ctaCteIdOld.getDiarioDetalleList().remove(diarioDetalle);
                ctaCteIdOld = em.merge(ctaCteIdOld);
            }
            if (ctaCteIdNew != null && !ctaCteIdNew.equals(ctaCteIdOld)) {
                ctaCteIdNew.getDiarioDetalleList().add(diarioDetalle);
                ctaCteIdNew = em.merge(ctaCteIdNew);
            }
            if (diarioCabeceraOld != null && !diarioCabeceraOld.equals(diarioCabeceraNew)) {
                diarioCabeceraOld.getDiarioDetalleList().remove(diarioDetalle);
                diarioCabeceraOld = em.merge(diarioCabeceraOld);
            }
            if (diarioCabeceraNew != null && !diarioCabeceraNew.equals(diarioCabeceraOld)) {
                diarioCabeceraNew.getDiarioDetalleList().add(diarioDetalle);
                diarioCabeceraNew = em.merge(diarioCabeceraNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                DiarioDetallePK id = diarioDetalle.getDiarioDetallePK();
                if (findDiarioDetalle(id) == null) {
                    throw new NonexistentEntityException("The diarioDetalle with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(DiarioDetallePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DiarioDetalle diarioDetalle;
            try {
                diarioDetalle = em.getReference(DiarioDetalle.class, id);
                diarioDetalle.getDiarioDetallePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The diarioDetalle with id " + id + " no longer exists.", enfe);
            }
            CuentaCorriente ctaCteId = diarioDetalle.getCtaCteId();
            if (ctaCteId != null) {
                ctaCteId.getDiarioDetalleList().remove(diarioDetalle);
                ctaCteId = em.merge(ctaCteId);
            }
            DiarioCabecera diarioCabecera = diarioDetalle.getDiarioCabecera();
            if (diarioCabecera != null) {
                diarioCabecera.getDiarioDetalleList().remove(diarioDetalle);
                diarioCabecera = em.merge(diarioCabecera);
            }
            em.remove(diarioDetalle);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DiarioDetalle> findDiarioDetalleEntities() {
        return findDiarioDetalleEntities(true, -1, -1);
    }

    public List<DiarioDetalle> findDiarioDetalleEntities(int maxResults, int firstResult) {
        return findDiarioDetalleEntities(false, maxResults, firstResult);
    }

    private List<DiarioDetalle> findDiarioDetalleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DiarioDetalle.class));
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

    public DiarioDetalle findDiarioDetalle(DiarioDetallePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DiarioDetalle.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiarioDetalleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DiarioDetalle> rt = cq.from(DiarioDetalle.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
