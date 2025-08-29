package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.FaOrdenReposicion;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author USUARIO
 */
public class FaOrdenReposicionJpaController extends JpaPadre {

    public FaOrdenReposicionJpaController(String empresa) {
        super(empresa);
    }

    public String verificarenviado(int invnum) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<String> query = em.createNamedQuery("FaOrdenReposicion.verificacion", String.class);
            query.setParameter("invnum", invnum);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return "N";
        } catch (Exception e) {
            return "";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarcentral() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select r.invnum,r.siscod,u.usenam,r.feccre,s.sisent from fa_orden_reposicion r inner join usuarios u on u.usecod=r.usecodcre inner join sistema s on s.siscod=r.siscod where r.enviado='S' and r.estado='S'");

            List<Object[]> resultados = query.getResultList();

            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("invnum", fila[0]);
                jsonObj.put("siscod", fila[1]);
                jsonObj.put("usenam", fila[2]);
                jsonObj.put("feccre", fila[3]);
                jsonObj.put("sisent", fila[4]);
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

    public String buscarSiscod(int siscod) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select r.invnum,r.siscod,u.usenam,r.feccre,r.enviado from fa_orden_reposicion r inner join usuarios u on u.usecod=r.usecodcre where r.siscod=? and r.estado='S'");
            query.setParameter(1, siscod);
            List<Object[]> resultados = query.getResultList();

            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("invnum", fila[0]);
                jsonObj.put("siscod", fila[1]);
                jsonObj.put("usenam", fila[2]);
                jsonObj.put("feccre", fila[3]);
                jsonObj.put("enviado", fila[4]);
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

    public int obtenerUltInvnum() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Integer> query = em.createNamedQuery("FaOrdenReposicion.ultimoinvnum", Integer.class);
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

    public void create(FaOrdenReposicion faOrdenReposicion) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(faOrdenReposicion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFaOrdenReposicion(faOrdenReposicion.getInvnum()) != null) {
                throw new PreexistingEntityException("FaOrdenReposicion " + faOrdenReposicion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(FaOrdenReposicion faOrdenReposicion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            faOrdenReposicion = em.merge(faOrdenReposicion);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = faOrdenReposicion.getInvnum();
                if (findFaOrdenReposicion(id) == null) {
                    throw new NonexistentEntityException("The faOrdenReposicion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FaOrdenReposicion faOrdenReposicion;
            try {
                faOrdenReposicion = em.getReference(FaOrdenReposicion.class, id);
                faOrdenReposicion.getInvnum();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The faOrdenReposicion with id " + id + " no longer exists.",
                        enfe);
            }
            em.remove(faOrdenReposicion);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<FaOrdenReposicion> findFaOrdenReposicionEntities() {
        return findFaOrdenReposicionEntities(true, -1, -1);
    }

    public List<FaOrdenReposicion> findFaOrdenReposicionEntities(int maxResults, int firstResult) {
        return findFaOrdenReposicionEntities(false, maxResults, firstResult);
    }

    private List<FaOrdenReposicion> findFaOrdenReposicionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FaOrdenReposicion.class));
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

    public FaOrdenReposicion findFaOrdenReposicion(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(FaOrdenReposicion.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getFaOrdenReposicionCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FaOrdenReposicion> rt = cq.from(FaOrdenReposicion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
