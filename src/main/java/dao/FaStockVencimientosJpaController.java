package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.FaStockVencimientos;
import dto.FaStockVencimientosPK;

/**
 *
 * @author USUARIO
 */
public class FaStockVencimientosJpaController extends JpaPadre {

    public FaStockVencimientosJpaController(String empresa) {
        super(empresa);
    }

    public String actualizarInventario(String body, String codalm) {
        EntityManager em = null;

        try {
            JSONObject json = new JSONObject(body);

            String codpro = json.getString("codpro");
            String lote = json.getString("lote");
            String tipo = json.getString("tipo");
            String ubicacion = json.getString("ubicacion");
            int cante = json.getInt("cante");
            int cantf = json.getInt("cantf");
            String xmlUbicaciones = XML.toString(
                    new JSONObject().put("Registro", new JSONArray(json.get("ubicaciones").toString())), "Registros");

            em = getEntityManager();

            StoredProcedureQuery query = em.createStoredProcedureQuery("actualizar_inventario_bartolito_detalle");
            query.registerStoredProcedureParameter("XmlData", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("codalm", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("codpro", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("lote", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("tipo", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("ubicacion", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("cante", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("cantf", Integer.class, ParameterMode.IN);

            query.setParameter("XmlData", xmlUbicaciones);
            query.setParameter("codalm", codalm);
            query.setParameter("codpro", codpro);
            query.setParameter("lote", lote);
            query.setParameter("tipo", tipo);
            query.setParameter("ubicacion", ubicacion);
            query.setParameter("cante", cante);
            query.setParameter("cantf", cantf);

            query.execute();

            return "OK"; // Indica éxito
        } catch (Exception e) {
            e.printStackTrace();
            return "E"; // Indica error
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String buscar(String codpro, String codalm) {
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();

            Query query = em.createNativeQuery(
                    "SELECT v.codlot, CAST(fecven AS DATE), CAST((abd.cante * stkfra + abd.cantf) AS DECIMAL(18,3))/stkfra, abd.ubicacion, abd.cante, abd.cantf, p.stkfra,'1' FROM fa_stock_vencimientos v INNER JOIN fa_productos p ON p.codpro=v.codpro INNER JOIN almacenes_bartolito_detalle abd ON abd.codalm=v.codalm AND abd.codpro=v.codpro AND abd.lote=v.codlot WHERE v.codpro=? AND v.codalm=? AND (abd.cante>0 OR abd.cantf>0) AND (v.qtymov>0 OR v.qtymov_m>0) UNION SELECT v.codlot, CAST(fecven AS DATE), CAST((-(ISNULL(SUM(abd.cante),0)*p.stkfra)-ISNULL(SUM(abd.cantf),0)+(v.qtymov*p.stkfra)+v.qtymov_m) AS DECIMAL(18,3))/stkfra, 'Recepcion', (-(ISNULL(SUM(abd.cante),0)*p.stkfra)-ISNULL(SUM(abd.cantf),0)+(v.qtymov*p.stkfra)+v.qtymov_m)/stkfra, (-(ISNULL(SUM(abd.cante),0)*p.stkfra)-ISNULL(SUM(abd.cantf),0)+(v.qtymov*p.stkfra)+v.qtymov_m)%stkfra, p.stkfra,'2' FROM fa_stock_vencimientos v INNER JOIN fa_productos p ON p.codpro=v.codpro LEFT JOIN almacenes_bartolito_detalle abd ON abd.codalm=v.codalm AND abd.codpro=v.codpro AND abd.lote=v.codlot AND (abd.cante>0 OR abd.cantf>0) WHERE v.codpro=? AND v.codalm=? AND (v.qtymov>0 OR v.qtymov_m>0) GROUP BY v.codlot, fecven, p.stkfra, v.qtymov_m, v.qtymov HAVING (ISNULL(SUM(abd.cante),0)*p.stkfra)+ISNULL(SUM(abd.cantf),0)<(v.qtymov*p.stkfra)+v.qtymov_m;");
            int i = 1;
            query.setParameter(i++, codpro);
            query.setParameter(i++, codalm);
            query.setParameter(i++, codpro);
            query.setParameter(i++, codalm);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                i = 0;
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("lote", fila[i++]);
                jsonObj.put("fecven", fila[i++]);
                jsonObj.put("cant", fila[i++]);
                jsonObj.put("ubic", fila[i++]);
                jsonObj.put("cante", fila[i++]);
                jsonObj.put("cantf", fila[i++]);
                jsonObj.put("stkfra", fila[i++]);
                jsonObj.put("tipo", fila[i++]);
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

    public int setearNegativos() {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            // Consulta con sintaxis específica para SQL Server
            String query = "UPDATE almacenes_bartolito_detalle "
                    + // Corchetes para nombres con espacios
                    "SET [cante] = CASE WHEN [cante] < 0 THEN 0 ELSE [cante] END, "
                    + "    [cantf] = CASE WHEN [cantf] < 0 THEN 0 ELSE [cantf] END "
                    + "FROM almacenes_bartolito_detalle "
                    + // FROM requerido
                    "WHERE [cante] < 0 OR [cantf] < 0";

            int result = em.createNativeQuery(query).executeUpdate();
            tx.commit();
            return result;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new PersistenceException("Error en SQL Server al actualizar: " + e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int actualizarUbicacionesDesdeAlmacen() {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            // Consulta nativa para SQL Server sin parámetros
            String query = "UPDATE f " +
                    "SET f.ubicacion = a.ubicacion " +
                    "FROM fa_stock_vencimientos f " +
                    "JOIN ( " +
                    "    SELECT codalm, codpro, ubicacion " +
                    "    FROM ( " +
                    "        SELECT codalm, codpro, ubicacion, " +
                    "               ROW_NUMBER() OVER (PARTITION BY codalm, codpro ORDER BY RIGHT(ubicacion, 1) ASC) AS rn "
                    +
                    "        FROM almacenes_bartolito_detalle " +
                    "        WHERE codpro <> '' " + // Filtro fijo para excluir códigos vacíos
                    "    ) sub " +
                    "    WHERE rn = 1 " +
                    ") a ON f.codalm = a.codalm AND f.codpro = a.codpro";

            int result = em.createNativeQuery(query).executeUpdate();
            tx.commit();
            return result;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new PersistenceException("Error al actualizar ubicaciones: " + e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String buscarPorAlmacen(String codalm) {
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            // Definir la consulta SQL nativa
            String sql = "SELECT u.lote, s.codpro, p.despro AS nombre, s.qtymov, s.qtymov_m, "
                    + "SUM(u.cante) AS cante, SUM(u.cantf) AS cantf "
                    + "FROM fa_stock_vencimientos s "
                    + "INNER JOIN fa_productos p ON s.codpro = p.codpro "
                    + "LEFT JOIN almacenes_bartolito_detalle u ON s.codalm = u.codalm AND s.codpro = u.codpro AND s.codlot = u.lote "
                    + "WHERE s.codalm = ? "
                    + "AND (s.qtymov <> 0 OR s.qtymov_m <> 0) "
                    + "GROUP BY u.lote, s.codpro, p.despro, s.qtymov, s.qtymov_m "
                    + "HAVING (SUM(u.cante) IS NOT NULL OR SUM(u.cantf) IS NOT NULL) "
                    + "AND (s.qtymov <> SUM(u.cante) OR s.qtymov_m <> SUM(u.cantf))";

            // Crear la consulta
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, codalm);

            // Ejecutar la consulta y obtener los resultados
            List<Object[]> resultados = query.getResultList();

            // Convertir los resultados a JSON
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("lote", fila[0]);
                jsonObj.put("codpro", fila[1]);
                jsonObj.put("nombre", fila[2]);
                jsonObj.put("qtymov", fila[3]);
                jsonObj.put("qtymov_m", fila[4]);
                jsonObj.put("cante", fila[5]);
                jsonObj.put("cantf", fila[6]);
                jsonArray.put(jsonObj);
            }

            // Devolver el JSON como una cadena
            return jsonArray.toString();
        } catch (Exception e) {
            // Manejar cualquier excepción y devolver un mensaje de error
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void create(FaStockVencimientos faStockVencimientos) throws PreexistingEntityException, Exception {
        if (faStockVencimientos.getFaStockVencimientosPK() == null) {
            faStockVencimientos.setFaStockVencimientosPK(new FaStockVencimientosPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(faStockVencimientos);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFaStockVencimientos(faStockVencimientos.getFaStockVencimientosPK()) != null) {
                throw new PreexistingEntityException("FaStockVencimientos " + faStockVencimientos + " already exists.",
                        ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(FaStockVencimientos faStockVencimientos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            faStockVencimientos = em.merge(faStockVencimientos);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                FaStockVencimientosPK id = faStockVencimientos.getFaStockVencimientosPK();
                if (findFaStockVencimientos(id) == null) {
                    throw new NonexistentEntityException(
                            "The faStockVencimientos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(FaStockVencimientosPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FaStockVencimientos faStockVencimientos;
            try {
                faStockVencimientos = em.getReference(FaStockVencimientos.class, id);
                faStockVencimientos.getFaStockVencimientosPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The faStockVencimientos with id " + id + " no longer exists.",
                        enfe);
            }
            em.remove(faStockVencimientos);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<FaStockVencimientos> findFaStockVencimientosEntities() {
        return findFaStockVencimientosEntities(true, -1, -1);
    }

    public List<FaStockVencimientos> findFaStockVencimientosEntities(int maxResults, int firstResult) {
        return findFaStockVencimientosEntities(false, maxResults, firstResult);
    }

    private List<FaStockVencimientos> findFaStockVencimientosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();

            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FaStockVencimientos.class));
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

    public FaStockVencimientos findFaStockVencimientos(FaStockVencimientosPK id) {
        EntityManager em = null;
        try {
            em = getEntityManager();

            return em.find(FaStockVencimientos.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getFaStockVencimientosCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();

            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FaStockVencimientos> rt = cq.from(FaStockVencimientos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
