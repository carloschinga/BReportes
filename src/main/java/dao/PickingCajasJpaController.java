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
import dto.PickingCajas;

/**
 *
 * @author USUARIO
 */
public class PickingCajasJpaController extends JpaPadre {

    public PickingCajasJpaController(String empresa) {
        super(empresa);
    }

    public String listarporcentajesJson(int orden, int siscod) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "SELECT pd.invnum, "
                            + "COUNT(CASE WHEN checkenvio = 'S' THEN 1 END) * 100.0 / COUNT(pd.pickcod), "
                            + "COUNT(pd.pickcod), "
                            + "COUNT(CASE WHEN (ISNULL(pc.cante, 0) = ISNULL(canter, 0)) "
                            + "AND (ISNULL(pc.cantf, 0) = ISNULL(cantfr, 0)) THEN 1 END) AS porcentaje_S "
                            + "FROM picking_detalle pd "
                            + "INNER JOIN picking_cajas pc ON pc.pickdetcod = pd.pickdetcod "
                            + "INNER JOIN picking p ON pd.pickcod = p.pickcod "
                            + "WHERE p.codpicklist = ? AND pd.siscod = ? "
                            + "GROUP BY pd.invnum;");
            query.setParameter(1, orden);
            query.setParameter(2, siscod);
            query.setHint("javax.persistence.query.timeout", 5000); // Agregar timeout

            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("invnum", fila[0]);
                jsonObj.put("avance", fila[1]);
                jsonObj.put("total", fila[2]);
                jsonObj.put("conform", fila[3]);
                jsonArray.put(jsonObj);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            e.printStackTrace(); // Agregar logging
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarporcentajesJson(int orden, int siscod, String caja) {
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "SELECT pd.invnum, COUNT(CASE WHEN checkenvio = 'S' THEN 1 END) * 100.0 / COUNT(pd.pickcod), count(pd.pickcod), count(case when (ISNULL(pc.cante, 0) = ISNULL(canter, 0)) and (ISNULL(pc.cantf, 0) = ISNULL(cantfr, 0)) then 1 end) AS porcentaje_S FROM picking_detalle pd inner join picking_cajas pc on pc.pickdetcod=pd.pickdetcod inner join picking p on pd.pickcod=p.pickcod WHERE p.codpicklist = ? and pd.siscod=? and pc.caja=? GROUP BY pd.invnum;");
            query.setParameter(1, orden);
            query.setParameter(2, siscod);
            query.setParameter(3, caja);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("invnum", fila[0]);
                jsonObj.put("avance", fila[1]);
                jsonObj.put("total", fila[2]);
                jsonObj.put("conform", fila[3]);
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

    private void replaceNullsWithZero(JSONArray jsonArray, String[] fields) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            for (String field : fields) {
                if (obj.isNull(field)) { // Verificar si el campo es null
                    obj.put(field, 0); // Reemplazar null con 0
                }
            }
        }
    }

    public String recepcionar_guardar(String json, int usecod) {
        String result = "E"; // Valor por defecto es error
        EntityManager em = null;

        try {
            JSONObject jsonobj = new JSONObject(json);
            String caja = jsonobj.getString("caja");
            int orden = jsonobj.getInt("orden");
            // Convertir el JSON a un JSONArray
            JSONArray jsonArray = jsonobj.getJSONArray("recepcion");
            replaceNullsWithZero(jsonArray, new String[] { "cante", "cantf" });

            // Crear un nuevo JSONObject con el nombre de la raíz "Registros"
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Registro", jsonArray);

            // Convertir el JSONObject a XML con la raíz "Registros"
            String xmlrecepcion = XML.toString(jsonObject, "Registros");

            jsonArray = jsonobj.getJSONArray("extra");
            replaceNullsWithZero(jsonArray, new String[] { "cante", "cantf" });
            jsonObject = new JSONObject();
            jsonObject.put("Registro", jsonArray);
            String xmlextra = XML.toString(jsonObject, "Registros");

            jsonArray = jsonobj.getJSONArray("eliminar");
            jsonObject = new JSONObject();
            jsonObject.put("Registro", jsonArray);
            String xmleliminar = XML.toString(jsonObject, "Registros");

            em = getEntityManager();

            StoredProcedureQuery query;
            query = em.createStoredProcedureQuery("guardar_recepcion_picking");

            query.registerStoredProcedureParameter("xmlrecepcion", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("xmlextra", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("xmleliminar", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("caja", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("orden", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("usecod", Integer.class, ParameterMode.IN);

            // Configurar los parámetros del procedimiento almacenado
            query.setParameter("xmlrecepcion", xmlrecepcion); // XML generado a partir del JSON
            query.setParameter("xmlextra", xmlextra); // XML generado a partir del JSON
            query.setParameter("xmleliminar", xmleliminar); // XML generado a partir del JSON
            query.setParameter("caja", caja);
            query.setParameter("orden", orden);
            query.setParameter("usecod", usecod);

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

    public String listarJson(int invnum) {// para picking las cajas y su itm
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery("select caja,cante,cantf from picking_cajas where pickdetcod=?");
            query.setParameter(1, invnum);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("caja", fila[0]);
                jsonObj.put("cante", fila[1]);
                jsonObj.put("cantf", fila[2]);
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

    public String EliminarCajas(int pickdetcod) {
        String result = "E"; // Default to error
        EntityManager em = null;
        try {
            em = getEntityManager(); // Asumiendo que tienes un método para obtener el EntityManager

            // Crear el StoredProcedureQuery para llamar al procedimiento almacenado
            StoredProcedureQuery query = em.createStoredProcedureQuery("EliminarCajasPicking");

            // Registrar los parámetros del procedimiento almacenado
            query.registerStoredProcedureParameter("pickdetcod", Integer.class, ParameterMode.IN);

            // Establecer los valores de los parámetros
            query.setParameter("pickdetcod", pickdetcod);

            // Ejecutar el procedimiento almacenado
            query.execute();

            // Si todo va bien, establecer el resultado como éxito
            result = "S";
        } catch (Exception e) {
            e.printStackTrace();
            result = "E"; // Error
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
        return result;
    }

    public void create(PickingCajas pickingCajas) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(pickingCajas);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(PickingCajas pickingCajas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            pickingCajas = em.merge(pickingCajas);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pickingCajas.getPickcajacod();
                if (findPickingCajas(id) == null) {
                    throw new NonexistentEntityException("The pickingCajas with id " + id + " no longer exists.");
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
            PickingCajas pickingCajas;
            try {
                pickingCajas = em.getReference(PickingCajas.class, id);
                pickingCajas.getPickcajacod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pickingCajas with id " + id + " no longer exists.", enfe);
            }
            em.remove(pickingCajas);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<PickingCajas> findPickingCajasEntities() {
        return findPickingCajasEntities(true, -1, -1);
    }

    public List<PickingCajas> findPickingCajasEntities(int maxResults, int firstResult) {
        return findPickingCajasEntities(false, maxResults, firstResult);
    }

    private List<PickingCajas> findPickingCajasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PickingCajas.class));
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

    public PickingCajas findPickingCajas(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(PickingCajas.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getPickingCajasCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PickingCajas> rt = cq.from(PickingCajas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
