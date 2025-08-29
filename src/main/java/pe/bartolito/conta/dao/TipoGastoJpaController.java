package pe.bartolito.conta.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import dao.JpaPadre;
import pe.bartolito.conta.dao.exceptions.NonexistentEntityException;
import pe.bartolito.conta.dao.exceptions.PreexistingEntityException;
import pe.bartolito.conta.dto.TipoGasto;

public class TipoGastoJpaController extends JpaPadre {

    public TipoGastoJpaController(String empresa) {
        super(empresa);
    }

    public void create(TipoGasto tipoGasto) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            // Usar consulta nativa directa
            String sql = "EXEC sp_InsTipoGasto @Id = ?, @Descripcion = ?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, tipoGasto.getTipoGastold().toString());
            query.setParameter(2, tipoGasto.getTipoGastoDescripcion());

            query.executeUpdate();
            em.getTransaction().commit();

        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void edit(TipoGasto tipoGasto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            // Usar consulta nativa directa para UPDATE
            String sql = "EXEC sp_UdpTipoGasto @Id = ?, @Descripcion = ?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, tipoGasto.getTipoGastold().toString());
            query.setParameter(2, tipoGasto.getTipoGastoDescripcion());

            query.executeUpdate();
            em.getTransaction().commit();

        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            if (findTipoGasto(tipoGasto.getTipoGastold()) == null) {
                throw new NonexistentEntityException("TipoGasto " + tipoGasto.getTipoGastold() + " no longer exists.");
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            String sql = "EXEC sp_DelTipoGasto @Id = ?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, id);
            query.executeUpdate();

            query.executeUpdate();
            em.getTransaction().commit();

        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    // Métodos findTipoGastoEntities y getTipoGastoCount permanecen igual
    public List<TipoGasto> findTipoGastoEntities() {
        return findTipoGastoEntities(true, -1, -1);
    }

    public List<TipoGasto> findTipoGastoEntities(int maxResults, int firstResult) {
        return findTipoGastoEntities(false, maxResults, firstResult);
    }

    private List<TipoGasto> findTipoGastoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoGasto.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public TipoGasto findTipoGasto(String id) {
        EntityManager em = getEntityManager();
        try {
            StoredProcedureQuery sp = em.createNamedStoredProcedureQuery("sp_SelTipoGasto");
            sp.setParameter("Id", id);
            sp.execute();
            List<TipoGasto> result = sp.getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public int getTipoGastoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoGasto> rt = cq.from(TipoGasto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}