package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.FaOrdenReposicionDetalle;
import dto.FaOrdenReposicionDetallePK;

/**
 *
 * @author USUARIO
 */
public class FaOrdenReposicionDetalleJpaController extends JpaPadre {

    public FaOrdenReposicionDetalleJpaController(String empresa) {
        super(empresa);
    }

    public String buscarInvnum(int invnum) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select c.invnum,d.numitm,d.codpro,p.despro,d.cante,d.cantf from fa_orden_reposicion_detalle d inner join fa_productos p on p.codpro=d.codpro inner join fa_orden_reposicion c on c.invnum=d.invnum and c.estado='S' where d.estado='S'");
            query.setParameter(1, invnum);
            List<Object[]> resultados = query.getResultList();

            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("invnum", fila[0]);
                jsonObj.put("numitm", fila[1]);
                jsonObj.put("codpro", fila[2]);
                jsonObj.put("despro", fila[3]);
                jsonObj.put("cante", fila[4]);
                jsonObj.put("cantf", fila[5]);
                jsonArray.put(jsonObj);
            }

            // Convertir el JSONArray a String y devolver
            return jsonArray.toString();

        } catch (Exception e) {
            // Manejar el error y devolver un JSON de error
            JSONObject errorObj = new JSONObject();
            errorObj.put("resultado", "Error");
            errorObj.put("mensaje", e.getMessage());
            return errorObj.toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int obtenerUltNumitm(int invnum) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Integer> query = em.createNamedQuery("FaOrdenReposicionDetalle.encontrarultnumitm",
                    Integer.class);
            query.setParameter("invnum", invnum);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return 0;
        } catch (Exception e) {
            return -1;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void create(FaOrdenReposicionDetalle faOrdenReposicionDetalle) throws PreexistingEntityException, Exception {
        if (faOrdenReposicionDetalle.getFaOrdenReposicionDetallePK() == null) {
            faOrdenReposicionDetalle.setFaOrdenReposicionDetallePK(new FaOrdenReposicionDetallePK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(faOrdenReposicionDetalle);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFaOrdenReposicionDetalle(faOrdenReposicionDetalle.getFaOrdenReposicionDetallePK()) != null) {
                throw new PreexistingEntityException(
                        "FaOrdenReposicionDetalle " + faOrdenReposicionDetalle + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(FaOrdenReposicionDetalle faOrdenReposicionDetalle) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            faOrdenReposicionDetalle = em.merge(faOrdenReposicionDetalle);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                FaOrdenReposicionDetallePK id = faOrdenReposicionDetalle.getFaOrdenReposicionDetallePK();
                if (findFaOrdenReposicionDetalle(id) == null) {
                    throw new NonexistentEntityException(
                            "The faOrdenReposicionDetalle with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(FaOrdenReposicionDetallePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FaOrdenReposicionDetalle faOrdenReposicionDetalle;
            try {
                faOrdenReposicionDetalle = em.getReference(FaOrdenReposicionDetalle.class, id);
                faOrdenReposicionDetalle.getFaOrdenReposicionDetallePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException(
                        "The faOrdenReposicionDetalle with id " + id + " no longer exists.", enfe);
            }
            em.remove(faOrdenReposicionDetalle);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<FaOrdenReposicionDetalle> findFaOrdenReposicionDetalleEntities() {
        return findFaOrdenReposicionDetalleEntities(true, -1, -1);
    }

    public List<FaOrdenReposicionDetalle> findFaOrdenReposicionDetalleEntities(int maxResults, int firstResult) {
        return findFaOrdenReposicionDetalleEntities(false, maxResults, firstResult);
    }

    private List<FaOrdenReposicionDetalle> findFaOrdenReposicionDetalleEntities(boolean all, int maxResults,
            int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FaOrdenReposicionDetalle.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public FaOrdenReposicionDetalle findFaOrdenReposicionDetalle(FaOrdenReposicionDetallePK id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(FaOrdenReposicionDetalle.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getFaOrdenReposicionDetalleCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FaOrdenReposicionDetalle> rt = cq.from(FaOrdenReposicionDetalle.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
