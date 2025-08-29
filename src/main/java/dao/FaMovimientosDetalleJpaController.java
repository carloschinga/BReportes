package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.json.JSONObject;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.FaMovimientos;
import dto.FaMovimientosDetalle;
import dto.FaMovimientosDetallePK;
import dto.FaProductos;

/**
 *
 * @author USUARIO
 */
public class FaMovimientosDetalleJpaController extends JpaPadre {

    public FaMovimientosDetalleJpaController(String empresa) {
        super(empresa);
    }

    public String vistaPrevia(int invnum) {// lista en json del detalle del movimiento segun secuencia
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNamedQuery("FaMovimientosDetalle.findDetByInvnum");
            query.setParameter("invnum", invnum);
            List<Object[]> resultados = query.getResultList();
            String cadena = "[";
            boolean s = true;
            for (Object[] fila : resultados) {
                s = false;
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("despro", fila[0]);
                jsonObj.put("cante", fila[1]);
                jsonObj.put("cantf", fila[2]);
                jsonObj.put("codlot", fila[3]);
                cadena = cadena + jsonObj.toString() + ",";
            }
            if (s) {
                cadena = "[]";
            } else {
                cadena = cadena.substring(0, cadena.length() - 1) + "]";
            }
            return cadena;

        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void create(FaMovimientosDetalle faMovimientosDetalle) throws PreexistingEntityException, Exception {
        if (faMovimientosDetalle.getFaMovimientosDetallePK() == null) {
            faMovimientosDetalle.setFaMovimientosDetallePK(new FaMovimientosDetallePK());
        }
        faMovimientosDetalle.getFaMovimientosDetallePK().setInvnum(faMovimientosDetalle.getFaMovimientos().getInvnum());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FaMovimientos faMovimientos = faMovimientosDetalle.getFaMovimientos();
            if (faMovimientos != null) {
                faMovimientos = em.getReference(faMovimientos.getClass(), faMovimientos.getInvnum());
                faMovimientosDetalle.setFaMovimientos(faMovimientos);
            }
            FaProductos codpro = faMovimientosDetalle.getCodpro();
            if (codpro != null) {
                codpro = em.getReference(codpro.getClass(), codpro.getCodpro());
                faMovimientosDetalle.setCodpro(codpro);
            }
            em.persist(faMovimientosDetalle);
            if (faMovimientos != null) {
                faMovimientos.getFaMovimientosDetalleList().add(faMovimientosDetalle);
                faMovimientos = em.merge(faMovimientos);
            }
            if (codpro != null) {
                codpro.getFaMovimientosDetalleList().add(faMovimientosDetalle);
                codpro = em.merge(codpro);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFaMovimientosDetalle(faMovimientosDetalle.getFaMovimientosDetallePK()) != null) {
                throw new PreexistingEntityException(
                        "FaMovimientosDetalle " + faMovimientosDetalle + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(FaMovimientosDetalle faMovimientosDetalle) throws NonexistentEntityException, Exception {
        faMovimientosDetalle.getFaMovimientosDetallePK().setInvnum(faMovimientosDetalle.getFaMovimientos().getInvnum());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FaMovimientosDetalle persistentFaMovimientosDetalle = em.find(FaMovimientosDetalle.class,
                    faMovimientosDetalle.getFaMovimientosDetallePK());
            FaMovimientos faMovimientosOld = persistentFaMovimientosDetalle.getFaMovimientos();
            FaMovimientos faMovimientosNew = faMovimientosDetalle.getFaMovimientos();
            FaProductos codproOld = persistentFaMovimientosDetalle.getCodpro();
            FaProductos codproNew = faMovimientosDetalle.getCodpro();
            if (faMovimientosNew != null) {
                faMovimientosNew = em.getReference(faMovimientosNew.getClass(), faMovimientosNew.getInvnum());
                faMovimientosDetalle.setFaMovimientos(faMovimientosNew);
            }
            if (codproNew != null) {
                codproNew = em.getReference(codproNew.getClass(), codproNew.getCodpro());
                faMovimientosDetalle.setCodpro(codproNew);
            }
            faMovimientosDetalle = em.merge(faMovimientosDetalle);
            if (faMovimientosOld != null && !faMovimientosOld.equals(faMovimientosNew)) {
                faMovimientosOld.getFaMovimientosDetalleList().remove(faMovimientosDetalle);
                faMovimientosOld = em.merge(faMovimientosOld);
            }
            if (faMovimientosNew != null && !faMovimientosNew.equals(faMovimientosOld)) {
                faMovimientosNew.getFaMovimientosDetalleList().add(faMovimientosDetalle);
                faMovimientosNew = em.merge(faMovimientosNew);
            }
            if (codproOld != null && !codproOld.equals(codproNew)) {
                codproOld.getFaMovimientosDetalleList().remove(faMovimientosDetalle);
                codproOld = em.merge(codproOld);
            }
            if (codproNew != null && !codproNew.equals(codproOld)) {
                codproNew.getFaMovimientosDetalleList().add(faMovimientosDetalle);
                codproNew = em.merge(codproNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                FaMovimientosDetallePK id = faMovimientosDetalle.getFaMovimientosDetallePK();
                if (findFaMovimientosDetalle(id) == null) {
                    throw new NonexistentEntityException(
                            "The faMovimientosDetalle with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(FaMovimientosDetallePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FaMovimientosDetalle faMovimientosDetalle;
            try {
                faMovimientosDetalle = em.getReference(FaMovimientosDetalle.class, id);
                faMovimientosDetalle.getFaMovimientosDetallePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The faMovimientosDetalle with id " + id + " no longer exists.",
                        enfe);
            }
            FaMovimientos faMovimientos = faMovimientosDetalle.getFaMovimientos();
            if (faMovimientos != null) {
                faMovimientos.getFaMovimientosDetalleList().remove(faMovimientosDetalle);
                faMovimientos = em.merge(faMovimientos);
            }
            FaProductos codpro = faMovimientosDetalle.getCodpro();
            if (codpro != null) {
                codpro.getFaMovimientosDetalleList().remove(faMovimientosDetalle);
                codpro = em.merge(codpro);
            }
            em.remove(faMovimientosDetalle);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<FaMovimientosDetalle> findFaMovimientosDetalleEntities() {
        return findFaMovimientosDetalleEntities(true, -1, -1);
    }

    public List<FaMovimientosDetalle> findFaMovimientosDetalleEntities(int maxResults, int firstResult) {
        return findFaMovimientosDetalleEntities(false, maxResults, firstResult);
    }

    private List<FaMovimientosDetalle> findFaMovimientosDetalleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FaMovimientosDetalle.class));
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

    public FaMovimientosDetalle findFaMovimientosDetalle(FaMovimientosDetallePK id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(FaMovimientosDetalle.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getFaMovimientosDetalleCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FaMovimientosDetalle> rt = cq.from(FaMovimientosDetalle.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
