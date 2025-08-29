package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import dao.exceptions.NonexistentEntityException;
import dto.InventarioDetalle;

/**
 *
 * @author USUARIO
 */
public class InventarioDetalleJpaController extends JpaPadre {

    public InventarioDetalleJpaController(String empresa) {
        super(empresa);
    }

    public String listar(int codinvalm) {// para picking las cajas y su itm
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select d.coddeta,u.usecod,u.usenam,u.usedoc,d.codrol from inventario_detalle d inner join usuarios_inventario u on d.usecod=u.usecod where d.codinvalm=?");
            query.setParameter(1, codinvalm);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                int i = 0;
                jsonObj.put("coddetau", fila[i++]);
                jsonObj.put("usecod", fila[i++]);
                jsonObj.put("usenam", fila[i++]);
                jsonObj.put("usedoc", fila[i++]);
                jsonObj.put("codrol", fila[i++]);
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

    public String obtenerRol(int codinvalm, int usecod) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<String> query = em.createNamedQuery("InventarioDetalle.findrol", String.class);
            query.setParameter("codinvalm", codinvalm);
            query.setParameter("usecod", usecod);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return "N";
        } catch (Exception e) {
            return "E";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String actualizar_usuarios(int codinvalm, String json) {
        String result = "E"; // Valor por defecto es error
        EntityManager em = null;

        try {
            // Convertir el JSON a un JSONArray
            JSONArray jsonArray = new JSONArray(json);

            // Crear un nuevo JSONObject con el nombre de la raíz "Registros"
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Registro", jsonArray);

            // Convertir el JSONObject a XML con la raíz "Registros"
            String xml = XML.toString(jsonObject, "Registros");

            em = getEntityManager();

            StoredProcedureQuery query;
            query = em.createStoredProcedureQuery("actualizar_inventario_detalle");

            query.registerStoredProcedureParameter("XmlData", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("codinvalm", Integer.class, ParameterMode.IN);
            // Configurar los parámetros del procedimiento almacenado
            query.setParameter("XmlData", xml); // XML generado a partir del JSON
            query.setParameter("codinvalm", codinvalm);

            // Ejecutar el procedimiento almacenado
            query.execute();

            return "S";

        } catch (Exception e) {
            e.printStackTrace();
            result = "E"; // Error
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
        return result;
    }

    public void create(InventarioDetalle inventarioDetalle) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(inventarioDetalle);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(InventarioDetalle inventarioDetalle) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            inventarioDetalle = em.merge(inventarioDetalle);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = inventarioDetalle.getCoddeta();
                if (findInventarioDetalle(id) == null) {
                    throw new NonexistentEntityException("The inventarioDetalle with id " + id + " no longer exists.");
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
            InventarioDetalle inventarioDetalle;
            try {
                inventarioDetalle = em.getReference(InventarioDetalle.class, id);
                inventarioDetalle.getCoddeta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The inventarioDetalle with id " + id + " no longer exists.",
                        enfe);
            }
            em.remove(inventarioDetalle);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<InventarioDetalle> findInventarioDetalleEntities() {
        return findInventarioDetalleEntities(true, -1, -1);
    }

    public List<InventarioDetalle> findInventarioDetalleEntities(int maxResults, int firstResult) {
        return findInventarioDetalleEntities(false, maxResults, firstResult);
    }

    private List<InventarioDetalle> findInventarioDetalleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(InventarioDetalle.class));
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

    public InventarioDetalle findInventarioDetalle(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(InventarioDetalle.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getInventarioDetalleCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<InventarioDetalle> rt = cq.from(InventarioDetalle.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
