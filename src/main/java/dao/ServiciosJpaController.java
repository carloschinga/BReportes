package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.Servicios;

/**
 *
 * @author USUARIO
 */
public class ServiciosJpaController extends JpaPadre {

    public ServiciosJpaController(String empresa) {
        super(empresa);
    }

    public String listarJson() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNamedQuery("Servicios.findAll");
            List<Object[]> resultados = query.getResultList();

            // Crea un JSONArray para almacenar los objetos JSON
            JSONArray jsonArray = new JSONArray();

            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("sercod", fila[0]);
                jsonObj.put("serdes", fila[1]);
                jsonArray.put(jsonObj);
            }

            // Convierte el JSONArray a cadena JSON
            return jsonArray.toString();

        } catch (Exception e) {
            return new JSONObject()
                    .put("resultado", "Error")
                    .put("mensaje", e.getMessage())
                    .toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void create(Servicios servicios) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(servicios);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findServicios(servicios.getSercod()) != null) {
                throw new PreexistingEntityException("Servicios " + servicios + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(Servicios servicios) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            servicios = em.merge(servicios);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = servicios.getSercod();
                if (findServicios(id) == null) {
                    throw new NonexistentEntityException("The servicios with id " + id + " no longer exists.");
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
            Servicios servicios;
            try {
                servicios = em.getReference(Servicios.class, id);
                servicios.getSercod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The servicios with id " + id + " no longer exists.", enfe);
            }
            em.remove(servicios);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<Servicios> findServiciosEntities() {
        return findServiciosEntities(true, -1, -1);
    }

    public List<Servicios> findServiciosEntities(int maxResults, int firstResult) {
        return findServiciosEntities(false, maxResults, firstResult);
    }

    private List<Servicios> findServiciosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Servicios.class));
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

    public Servicios findServicios(String id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(Servicios.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getServiciosCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Servicios> rt = cq.from(Servicios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
