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
import dto.FaAlmacenes;

/**
 *
 * @author USUARIO
 */
public class FaAlmacenesJpaController extends JpaPadre {

    public FaAlmacenesJpaController(String empresa) {
        super(empresa);
    }

    public String buscarSiscod(int siscod) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNamedQuery("FaAlmacenes.findBySiscod");
            query.setParameter("siscod", siscod);
            List<Object[]> resultados = query.getResultList();

            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codalm", fila[0]);
                jsonObj.put("desalm", fila[1]);
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

    public String listarJson() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNamedQuery("FaAlmacenes.findAlljson");
            List<Object[]> resultados = query.getResultList();

            // Crea un JSONArray para almacenar los objetos JSON
            JSONArray jsonArray = new JSONArray();

            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codalm", fila[0]);
                jsonObj.put("desalm", fila[1]);
                jsonObj.put("siscod", fila[2]);
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

    public String listarcentralJson() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNamedQuery("FaAlmacenes.findAllcentraljson");
            List<Object[]> resultados = query.getResultList();

            // Crea un JSONArray para almacenar los objetos JSON
            JSONArray jsonArray = new JSONArray();

            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codalm", fila[0]);
                jsonObj.put("desalm", fila[1]);
                jsonObj.put("siscod", fila[2]);
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

    public String listarJsonSisent() {// lista de los codigos de almacenes y sus establecimientos
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select s.sisent,a.codalm from fa_almacenes a inner join sistema s on s.siscod=a.siscod");
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();

            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("sisent", fila[0]);
                jsonObj.put("codalm", fila[1]);
                jsonArray.put(jsonObj);
            }

            // Convertir el JSONArray a String
            return jsonArray.toString();

        } catch (Exception e) {
            return "{\"resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarJsonRestricciones() {// lista solo los almacenes que tenga productos restringidos
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select DISTINCT r.codalm,a.desalm,siscod from restricciones_distribucion r inner join fa_almacenes a on r.codalm=a.codalm");
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();

            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codalm", fila[0]);
                jsonObj.put("desalm", fila[1]);
                jsonObj.put("siscod", fila[2]);
                jsonArray.put(jsonObj);
            }

            // Convertir el JSONArray a String
            return jsonArray.toString();

        } catch (Exception e) {
            // Retornar un JSON con el error en caso de excepción
            JSONObject errorObj = new JSONObject();
            errorObj.put("resultado", "Error");
            errorObj.put("mensaje", e.getMessage());
            return errorObj.toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void create(FaAlmacenes faAlmacenes) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(faAlmacenes);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFaAlmacenes(faAlmacenes.getCodalm()) != null) {
                throw new PreexistingEntityException("FaAlmacenes " + faAlmacenes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(FaAlmacenes faAlmacenes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            faAlmacenes = em.merge(faAlmacenes);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = faAlmacenes.getCodalm();
                if (findFaAlmacenes(id) == null) {
                    throw new NonexistentEntityException("The faAlmacenes with id " + id + " no longer exists.");
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
            FaAlmacenes faAlmacenes;
            try {
                faAlmacenes = em.getReference(FaAlmacenes.class, id);
                faAlmacenes.getCodalm();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The faAlmacenes with id " + id + " no longer exists.", enfe);
            }
            em.remove(faAlmacenes);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<FaAlmacenes> findFaAlmacenesEntities() {
        return findFaAlmacenesEntities(true, -1, -1);
    }

    public List<FaAlmacenes> findFaAlmacenesEntities(int maxResults, int firstResult) {
        return findFaAlmacenesEntities(false, maxResults, firstResult);
    }

    private List<FaAlmacenes> findFaAlmacenesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FaAlmacenes.class));
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

    public FaAlmacenes findFaAlmacenes(String id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(FaAlmacenes.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getFaAlmacenesCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FaAlmacenes> rt = cq.from(FaAlmacenes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
