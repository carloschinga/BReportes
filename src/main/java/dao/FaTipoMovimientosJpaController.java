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
import dto.FaTipoMovimientos;

/**
 *
 * @author USUARIO
 */
public class FaTipoMovimientosJpaController extends JpaPadre {

    public FaTipoMovimientosJpaController(String empresa) {
        super(empresa);
    }

    public int obtenerUltInvkar(String tipkar) {// metodo que te devuelve la ultima secuencia del tipo ingresado
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Integer> query = em.createNamedQuery("FaTipoMovimientos.tipkarsec", Integer.class);
            query.setParameter("tipkar", tipkar);
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

    // metodo para listar los tipos de movimiento que tiene permitido el usuario en
    // ese almacen y sistema
    public String listarTiposJson(int siscod, String codalm, int usecod) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "SELECT t.tipkar,t.destka FROM fa_tipo_movimientos t where t.estado='S' and (SELECT a.tipkar FROM establecimientos_accesos a WHERE a.siscod =? and codalm=? and a.usecod =?) like '%'+t.tipkar+'%'");
            query.setParameter(1, siscod);
            query.setParameter(2, codalm);
            query.setParameter(3, usecod);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("tipkar", fila[0]);
                jsonObj.put("destkar", fila[1]);
                jsonArray.put(jsonObj);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void create(FaTipoMovimientos faTipoMovimientos) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(faTipoMovimientos);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFaTipoMovimientos(faTipoMovimientos.getTipkar()) != null) {
                throw new PreexistingEntityException("FaTipoMovimientos " + faTipoMovimientos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(FaTipoMovimientos faTipoMovimientos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            faTipoMovimientos = em.merge(faTipoMovimientos);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = faTipoMovimientos.getTipkar();
                if (findFaTipoMovimientos(id) == null) {
                    throw new NonexistentEntityException("The faTipoMovimientos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FaTipoMovimientos faTipoMovimientos;
            try {
                faTipoMovimientos = em.getReference(FaTipoMovimientos.class, id);
                faTipoMovimientos.getTipkar();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The faTipoMovimientos with id " + id + " no longer exists.",
                        enfe);
            }
            em.remove(faTipoMovimientos);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<FaTipoMovimientos> findFaTipoMovimientosEntities() {
        return findFaTipoMovimientosEntities(true, -1, -1);
    }

    public List<FaTipoMovimientos> findFaTipoMovimientosEntities(int maxResults, int firstResult) {
        return findFaTipoMovimientosEntities(false, maxResults, firstResult);
    }

    private List<FaTipoMovimientos> findFaTipoMovimientosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FaTipoMovimientos.class));
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

    public FaTipoMovimientos findFaTipoMovimientos(String id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(FaTipoMovimientos.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getFaTipoMovimientosCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FaTipoMovimientos> rt = cq.from(FaTipoMovimientos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
