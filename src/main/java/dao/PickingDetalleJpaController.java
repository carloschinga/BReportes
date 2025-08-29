package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import dao.exceptions.NonexistentEntityException;
import dto.PickingDetalle;

/**
 *
 * @author USUARIO
 */
public class PickingDetalleJpaController extends JpaPadre {

    public PickingDetalleJpaController(String empresa) {
        super(empresa);
    }

    public String agregar(String json, int usecod, int pickcod) {
        String result = "E"; // Valor por defecto es error
        EntityManager em = null;

        try {
            // Convertir el JSON a un JSONArray
            JSONArray jsonArray = new JSONArray(json);

            // Crear un nuevo JSONObject con la raíz "Registros"
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Registro", jsonArray);

            // Convertir el JSONObject a XML con la raíz "Registros"
            String xml = XML.toString(jsonObject, "Registros");

            em = getEntityManager();

            StoredProcedureQuery query;
            query = em.createStoredProcedureQuery("insertarPickingDetalle");
            query.registerStoredProcedureParameter("XmlData", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("pickcod", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("usecod", Integer.class, ParameterMode.IN);

            // Configurar los parámetros del procedimiento almacenado
            query.setParameter("XmlData", xml); // XML generado a partir del JSON
            query.setParameter("pickcod", pickcod);
            query.setParameter("usecod", usecod);

            // Ejecutar el procedimiento almacenado
            query.execute();
            result = "S"; // Éxito
        } catch (Exception e) {
            e.printStackTrace();
            result = "E"; // Error
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
        return result;
    }

    public String asignarsecuencia(int pickcod) {
        String result = "E"; // Valor por defecto es error
        EntityManager em = null;

        try {
            em = getEntityManager();

            StoredProcedureQuery query;
            query = em.createStoredProcedureQuery("asignarsecuencia");
            query.registerStoredProcedureParameter("pickcod", Integer.class, ParameterMode.IN);

            // Configurar los parámetros del procedimiento almacenado
            query.setParameter("pickcod", pickcod);

            // Ejecutar el procedimiento almacenado
            query.execute();
            result = "S"; // Éxito
        } catch (Exception e) {
            e.printStackTrace();
            result = "E"; // Error
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
        return result;
    }

    public void create(PickingDetalle pickingDetalle) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(pickingDetalle);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(PickingDetalle pickingDetalle) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            pickingDetalle = em.merge(pickingDetalle);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pickingDetalle.getPickdetcod();
                if (findPickingDetalle(id) == null) {
                    throw new NonexistentEntityException("The pickingDetalle with id " + id + " no longer exists.");
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
            PickingDetalle pickingDetalle;
            try {
                pickingDetalle = em.getReference(PickingDetalle.class, id);
                pickingDetalle.getPickdetcod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pickingDetalle with id " + id + " no longer exists.", enfe);
            }
            em.remove(pickingDetalle);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<PickingDetalle> findPickingDetalleEntities() {
        return findPickingDetalleEntities(true, -1, -1);
    }

    public List<PickingDetalle> findPickingDetalleEntities(int maxResults, int firstResult) {
        return findPickingDetalleEntities(false, maxResults, firstResult);
    }

    private List<PickingDetalle> findPickingDetalleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PickingDetalle.class));
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

    public PickingDetalle findPickingDetalle(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(PickingDetalle.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getPickingDetalleCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PickingDetalle> rt = cq.from(PickingDetalle.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
