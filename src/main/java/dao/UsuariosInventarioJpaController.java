package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.json.JSONObject;

import dao.exceptions.NonexistentEntityException;
import dto.UsuariosInventario;

/**
 *
 * @author USUARIO
 */
public class UsuariosInventarioJpaController extends JpaPadre {
    public String Listar() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "SELECT u.usecod,u.useusr,u.usenam,u.rol FROM usuarios_inventario u where u.estado='S'");
            List<Object[]> resultados = query.getResultList();
            String cadena = "[";
            boolean s = true;
            for (Object[] fila : resultados) {
                s = false;
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("usecod", fila[0]);
                jsonObj.put("useusr", fila[1]);
                jsonObj.put("usenam", fila[2]);
                jsonObj.put("rol", fila[3]);
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

    public UsuariosInventarioJpaController(String empresa) {
        super(empresa);
    }

    public void create(UsuariosInventario usuariosInventario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(usuariosInventario);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(UsuariosInventario usuariosInventario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            usuariosInventario = em.merge(usuariosInventario);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuariosInventario.getUsecod();
                if (findUsuariosInventario(id) == null) {
                    throw new NonexistentEntityException("The usuariosInventario with id " + id + " no longer exists.");
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
            UsuariosInventario usuariosInventario;
            try {
                usuariosInventario = em.getReference(UsuariosInventario.class, id);
                usuariosInventario.getUsecod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuariosInventario with id " + id + " no longer exists.",
                        enfe);
            }
            em.remove(usuariosInventario);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<UsuariosInventario> findUsuariosInventarioEntities() {
        return findUsuariosInventarioEntities(true, -1, -1);
    }

    public List<UsuariosInventario> findUsuariosInventarioEntities(int maxResults, int firstResult) {
        return findUsuariosInventarioEntities(false, maxResults, firstResult);
    }

    private List<UsuariosInventario> findUsuariosInventarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuariosInventario.class));
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

    public UsuariosInventario findUsuariosInventario(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(UsuariosInventario.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getUsuariosInventarioCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuariosInventario> rt = cq.from(UsuariosInventario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
