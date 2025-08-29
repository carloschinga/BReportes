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
import dto.InventarioAlmacen;

/**
 *
 * @author USUARIO
 */
public class InventarioAlmacenJpaController extends JpaPadre {

    public InventarioAlmacenJpaController(String empresa) {
        super(empresa);
    }

    public String listaragregar(int codinv) {// para picking las cajas y su itm
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select a.codalm,a.desalm,i.codinvalm,CONVERT(VARCHAR(10), i.fecape, 23),CONVERT(VARCHAR(10), i.feccir, 23),i.estdet from fa_almacenes a left join inventario_almacen i on a.codalm=i.codalm and i.codinv=?");
            query.setParameter(1, codinv);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                int i = 0;
                jsonObj.put("codalm", fila[i++]);
                jsonObj.put("desalm", fila[i++]);
                jsonObj.put("codinvalm", fila[i++]);
                jsonObj.put("fecape", fila[i++]);
                jsonObj.put("feccir", fila[i++]);
                jsonObj.put("estdet", fila[i++]);
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

    public String listar(int codinvcab, int usecod, String grucod) { // para picking las cajas y su itm
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select DISTINCT a.codalm,a.desalm,i.numitm,i.desinvalm,i.tipdet,i.codinvalm,CONVERT(VARCHAR(10), i.fecape, 23),CONVERT(VARCHAR(10), i.feccir, 23),i.estdet,d.codrol from fa_almacenes a inner join inventario_almacen i on a.codalm=i.codalm and i.codinvcab=? left join inventario_detalle d on d.codinvalm=i.codinvalm where d.usecod=? or 'SUPINV'=? or 'SUPERV'=? or 'JEFALM'=?");
            int i = 1;
            query.setParameter(i++, codinvcab);
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
                jsonObj.put("numitm", fila[i++]);
                jsonObj.put("desinvalm", fila[i++]);
                jsonObj.put("tipdet", fila[i++]);
                jsonObj.put("codinvalm", fila[i++]);
                jsonObj.put("fecape", fila[i++]);
                jsonObj.put("feccir", fila[i++]);
                jsonObj.put("estdet", fila[i++]);
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
            query = em.createStoredProcedureQuery("agregar_establecimiento_inventario");

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

    public String obtenerEstado(int codinvalm) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<String> query = em.createNamedQuery("InventarioAlmacen.findest", String.class);
            query.setParameter("codinvalm", codinvalm);
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

    public int obtenerUltInvnum(int codinvcab) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Integer> query = em.createNamedQuery("InventarioAlmacen.obtenerultinvnum", Integer.class);
            query.setParameter("codinvcab", codinvcab);
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

    public int obtenerUltInvnumcodinvalm(int codinvcab) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Integer> query = em.createNamedQuery("InventarioAlmacen.obtenerultinvnumcodinvalm",
                    Integer.class);
            query.setParameter("codinvcab", codinvcab);
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

    public void create(InventarioAlmacen inventarioAlmacen) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(inventarioAlmacen);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(InventarioAlmacen inventarioAlmacen) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            inventarioAlmacen = em.merge(inventarioAlmacen);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = inventarioAlmacen.getCodinvalm();
                if (findInventarioAlmacen(id) == null) {
                    throw new NonexistentEntityException("The inventarioAlmacen with id " + id + " no longer exists.");
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
            InventarioAlmacen inventarioAlmacen;
            try {
                inventarioAlmacen = em.getReference(InventarioAlmacen.class, id);
                inventarioAlmacen.getCodinvalm();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The inventarioAlmacen with id " + id + " no longer exists.",
                        enfe);
            }
            em.remove(inventarioAlmacen);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<InventarioAlmacen> findInventarioAlmacenEntities() {
        return findInventarioAlmacenEntities(true, -1, -1);
    }

    public List<InventarioAlmacen> findInventarioAlmacenEntities(int maxResults, int firstResult) {
        return findInventarioAlmacenEntities(false, maxResults, firstResult);
    }

    private List<InventarioAlmacen> findInventarioAlmacenEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(InventarioAlmacen.class));
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

    public InventarioAlmacen findInventarioAlmacen(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(InventarioAlmacen.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getInventarioAlmacenCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<InventarioAlmacen> rt = cq.from(InventarioAlmacen.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
