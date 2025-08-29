
package pe.bartolito.login.dao;

import dao.JpaPadre;
import dao.exceptions.NonexistentEntityException;
import pe.bartolito.login.dto.UsuariosBartolito;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.json.JSONObject;

/**
 *
 * @author USUARIO
 */
public class UsuariosBartolitoJpaController extends JpaPadre {
    public String Listar() {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNativeQuery("SELECT u.usecod,u.useusr,u.usenam,a.desalm,u.codalm FROM usuarios_bartolito u inner join fa_almacenes a on a.codalm=u.codalm where u.estado='S'");
            List<Object[]> resultados = query.getResultList();
            String cadena = "[";
            boolean s = true;
            for (Object[] fila : resultados) {
                s = false;
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("usecod", fila[0]);
                jsonObj.put("useusr", fila[1]);
                jsonObj.put("usenam", fila[2]);
                jsonObj.put("desalm", fila[3]);
                jsonObj.put("codalm", fila[4]);
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
            if (em != null) {
                em.close();
            }
        }
    }
    
    
    public UsuariosBartolitoJpaController(String empresa) {
        super(empresa);
    }

    public void create(UsuariosBartolito usuariosBartolito) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(usuariosBartolito);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsuariosBartolito usuariosBartolito) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            usuariosBartolito = em.merge(usuariosBartolito);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuariosBartolito.getUsecod();
                if (findUsuariosBartolito(id) == null) {
                    throw new NonexistentEntityException("The usuariosBartolito with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuariosBartolito usuariosBartolito;
            try {
                usuariosBartolito = em.getReference(UsuariosBartolito.class, id);
                usuariosBartolito.getUsecod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuariosBartolito with id " + id + " no longer exists.", enfe);
            }
            em.remove(usuariosBartolito);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsuariosBartolito> findUsuariosBartolitoEntities() {
        return findUsuariosBartolitoEntities(true, -1, -1);
    }

    public List<UsuariosBartolito> findUsuariosBartolitoEntities(int maxResults, int firstResult) {
        return findUsuariosBartolitoEntities(false, maxResults, firstResult);
    }

    private List<UsuariosBartolito> findUsuariosBartolitoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuariosBartolito.class));
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

    public UsuariosBartolito findUsuariosBartolito(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsuariosBartolito.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosBartolitoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuariosBartolito> rt = cq.from(UsuariosBartolito.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
