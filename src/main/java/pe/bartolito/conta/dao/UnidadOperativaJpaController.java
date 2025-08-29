package pe.bartolito.conta.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import dao.JpaPadre;
import javax.persistence.Query;
import pe.bartolito.conta.dao.exceptions.NonexistentEntityException;
import pe.bartolito.conta.dao.exceptions.PreexistingEntityException;
import pe.bartolito.conta.dto.UnidadOperativa;

public class UnidadOperativaJpaController extends JpaPadre {

    public UnidadOperativaJpaController(String empresa) {
        super(empresa);
    }

    public void create(UnidadOperativa unidadOperativa) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            String sql = "EXEC sp_InsUnidadOperativa @Id = ?, @Descripcion = ?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, unidadOperativa.getUnidOperald());
            query.setParameter(2, unidadOperativa.getUnidOperaDescripcion());
            query.executeUpdate();

            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex; // Manejo adicional de errores si es necesario
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void edit(UnidadOperativa unidadOperativa) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            String sql = "EXEC sp_UdpUnidadOperativa @Id = ?, @Descripcion = ?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, unidadOperativa.getUnidOperald());
            query.setParameter(2, unidadOperativa.getUnidOperaDescripcion());
            query.executeUpdate();

            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex; // Manejo adicional de errores si es necesario
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

            String sql = "EXEC sp_DelUnidadOperativa @Id = ?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, id);
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

    public List<UnidadOperativa> findUnidadOperativaEntities() {
        return findUnidadOperativaEntities(true, -1, -1);
    }

    public List<UnidadOperativa> findUnidadOperativaEntities(int maxResults, int firstResult) {
        return findUnidadOperativaEntities(false, maxResults, firstResult);
    }

    private List<UnidadOperativa> findUnidadOperativaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UnidadOperativa.class));
            javax.persistence.Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public UnidadOperativa findUnidadOperativa(String id) {
        EntityManager em = getEntityManager();
        try {
            StoredProcedureQuery sp = em.createNamedStoredProcedureQuery("sp_SelUnidadOperativa");
            sp.setParameter("Id", id);
            sp.execute();
            List<UnidadOperativa> result = sp.getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    public int getUnidadOperativaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UnidadOperativa> rt = cq.from(UnidadOperativa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            javax.persistence.Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}