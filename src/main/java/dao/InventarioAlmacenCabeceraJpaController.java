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
import dto.InventarioAlmacenCabecera;

/**
 *
 * @author shaho
 */
public class InventarioAlmacenCabeceraJpaController extends JpaPadre {

    public InventarioAlmacenCabeceraJpaController(String empresa) {
        super(empresa);
    }

    public String listaragregar(int codinv) {// para picking las cajas y su itm
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select a.codalm,a.desalm,i.codinvcab from fa_almacenes a left join inventario_almacen_cabecera i on a.codalm=i.codalm and i.codinv=?");
            query.setParameter(1, codinv);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                int i = 0;
                jsonObj.put("codalm", fila[i++]);
                jsonObj.put("desalm", fila[i++]);
                jsonObj.put("codinvcab", fila[i++]);
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

    public String listar(int codinv, int usecod, String grucod) { // para picking las cajas y su itm
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select DISTINCT a.codalm,a.desalm,i.codinvcab,inv.captura as invcaptura,i.captura as cabcaptura from fa_almacenes a inner join inventario_almacen_cabecera i on a.codalm=i.codalm and i.codinv=? left join inventario_almacen ia on ia.codinvcab=i.codinvcab left join inventario_detalle d on d.codinvalm=ia.codinvalm left join inventario inv on inv.codinv=i.codinv where d.usecod=? or 'SUPINV'=? or 'SUPERV'=? or 'JEFALM'=?");
            int i = 1;
            query.setParameter(i++, codinv);
            query.setParameter(i++, usecod);
            query.setParameter(i++, grucod);
            query.setParameter(i++, grucod);
            query.setParameter(i++, grucod);

            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                i = 0;
                jsonObj.put("codalm", fila[i++]);
                jsonObj.put("desalm", fila[i++]);
                jsonObj.put("codinvcab", fila[i++]);
                jsonObj.put("invcaptura", fila[i++]);
                jsonObj.put("cabcaptura", fila[i++]);
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

    public String actualizar_almacenes(int codinv, String json) {
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
            query = em.createStoredProcedureQuery("agregar_establecimiento_inventario_cabecera");

            query.registerStoredProcedureParameter("XmlData", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("codinv", Integer.class, ParameterMode.IN);
            // Configurar los parámetros del procedimiento almacenado
            query.setParameter("XmlData", xml); // XML generado a partir del JSON
            query.setParameter("codinv", codinv);

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

    public String capturarStocks(int codinvcab) {
        String result = "E"; // Valor por defecto es error
        EntityManager em = null;

        try {
            em = getEntityManager();

            StoredProcedureQuery query;
            query = em.createStoredProcedureQuery("CapturarStocksInventario");

            query.registerStoredProcedureParameter("codinvcab", Integer.class, ParameterMode.IN);
            // Configurar los parámetros del procedimiento almacenado
            query.setParameter("codinvcab", codinvcab);

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

    public void create(InventarioAlmacenCabecera inventarioAlmacenCabecera) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(inventarioAlmacenCabecera);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(InventarioAlmacenCabecera inventarioAlmacenCabecera) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            inventarioAlmacenCabecera = em.merge(inventarioAlmacenCabecera);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = inventarioAlmacenCabecera.getCodinvcab();
                if (findInventarioAlmacenCabecera(id) == null) {
                    throw new NonexistentEntityException(
                            "The inventarioAlmacenCabecera with id " + id + " no longer exists.");
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
            InventarioAlmacenCabecera inventarioAlmacenCabecera;
            try {
                inventarioAlmacenCabecera = em.getReference(InventarioAlmacenCabecera.class, id);
                inventarioAlmacenCabecera.getCodinvcab();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException(
                        "The inventarioAlmacenCabecera with id " + id + " no longer exists.", enfe);
            }
            em.remove(inventarioAlmacenCabecera);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<InventarioAlmacenCabecera> findInventarioAlmacenCabeceraEntities() {
        return findInventarioAlmacenCabeceraEntities(true, -1, -1);
    }

    public List<InventarioAlmacenCabecera> findInventarioAlmacenCabeceraEntities(int maxResults, int firstResult) {
        return findInventarioAlmacenCabeceraEntities(false, maxResults, firstResult);
    }

    private List<InventarioAlmacenCabecera> findInventarioAlmacenCabeceraEntities(boolean all, int maxResults,
            int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(InventarioAlmacenCabecera.class));
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

    public InventarioAlmacenCabecera findInventarioAlmacenCabecera(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(InventarioAlmacenCabecera.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getInventarioAlmacenCabeceraCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<InventarioAlmacenCabecera> rt = cq.from(InventarioAlmacenCabecera.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
