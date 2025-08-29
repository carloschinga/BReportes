/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import dto.InventarioToma;

/**
 *
 * @author USUARIO
 */
public class InventarioTomaJpaController extends JpaPadre {

    public InventarioTomaJpaController(String empresa) {
        super(empresa);
    }

    public int obtenercoddeta(int codinvalm, int usecod) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Integer> query = em.createNamedQuery("InventarioDetalle.findcoddeta", Integer.class);
            query.setParameter("codinvalm", codinvalm);
            query.setParameter("usecod", usecod);
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

    public String actualizar_toma(int codinvalm, String json, int usecod) {
        String result = "E"; // Valor por defecto es error
        EntityManager em = null;

        try {
            // Convertir el JSON a un JSONArray
            JSONObject jsonobj = new JSONObject(json);
            JSONArray jsonArray = jsonobj.getJSONArray("agregar");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Registro", jsonArray);

            String xml = XML.toString(jsonObject, "Registros");

            jsonArray = jsonobj.getJSONArray("eliminar");

            jsonObject = new JSONObject();
            jsonObject.put("Registro", jsonArray);

            String xml2 = XML.toString(jsonObject, "Registros");

            em = getEntityManager();

            StoredProcedureQuery query;
            query = em.createStoredProcedureQuery("agregar_o_modificar_toma");

            query.registerStoredProcedureParameter("XmlData", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("XmlEliminar", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("codinvalm", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("usecod", Integer.class, ParameterMode.IN);
            // Configurar los parámetros del procedimiento almacenado
            query.setParameter("XmlData", xml);
            query.setParameter("XmlEliminar", xml2);
            query.setParameter("codinvalm", codinvalm);
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

    public int obtenercodtomaajuste(int codinvalm, String codpro, String lote) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Integer> query = em.createNamedQuery("InventarioToma.findcodtom", Integer.class);
            query.setParameter("codinvalm", codinvalm);
            query.setParameter("codpro", codpro);
            query.setParameter("lote", lote);
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

    public String actualizar_toma_coddeta(int codinvalm, String json, int usecod, int coddeta) {
        String result = "E"; // Valor por defecto es error
        EntityManager em = null;

        try {
            JSONObject jsonobj = new JSONObject(json);
            JSONArray jsonArray = jsonobj.getJSONArray("agregar");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Registro", jsonArray);

            String xml = XML.toString(jsonObject, "Registros");

            jsonArray = jsonobj.getJSONArray("eliminar");

            jsonObject = new JSONObject();
            jsonObject.put("Registro", jsonArray);

            String xml2 = XML.toString(jsonObject, "Registros");

            em = getEntityManager();

            StoredProcedureQuery query;
            query = em.createStoredProcedureQuery("agregar_o_modificar_toma_coddeta");

            query.registerStoredProcedureParameter("XmlData", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("XmlEliminar", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("codinvalm", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("usecod", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("coddeta", Integer.class, ParameterMode.IN);
            // Configurar los parámetros del procedimiento almacenado
            query.setParameter("XmlData", xml); // XML generado a partir del JSON
            query.setParameter("XmlEliminar", xml2);
            query.setParameter("codinvalm", codinvalm);
            query.setParameter("usecod", usecod);
            query.setParameter("coddeta", coddeta);

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

    public String listar(int codinvalm, int coddeta) {// para picking las cajas y su itm
        EntityManager em = null;
        try {
            em = getEntityManager();
            if (coddeta > 0) {
                Query query = em.createNativeQuery(
                        "select t.codpro,p.despro from inventario_toma t inner join fa_productos p on p.codpro=t.codpro where t.coddeta=? and t.codinvalm=? group by t.codpro,p.despro");
                query.setParameter(1, coddeta);
                query.setParameter(2, codinvalm);
                List<Object[]> resultados = query.getResultList();
                JSONArray jsonArray = new JSONArray();
                for (Object[] fila : resultados) {
                    JSONObject jsonObj = new JSONObject();
                    int i = 0;
                    jsonObj.put("codpro", fila[i++]);
                    jsonObj.put("despro", fila[i++]);
                    jsonArray.put(jsonObj);
                }
                return jsonArray.toString();
            } else {
                Query query = em.createNativeQuery(
                        "select t.codpro,p.despro from inventario_toma t inner join fa_productos p on p.codpro=t.codpro where t.codinvalm=? group by t.codpro,p.despro");
                query.setParameter(1, codinvalm);
                List<Object[]> resultados = query.getResultList();
                JSONArray jsonArray = new JSONArray();
                for (Object[] fila : resultados) {
                    JSONObject jsonObj = new JSONObject();
                    int i = 0;
                    jsonObj.put("codpro", fila[i++]);
                    jsonObj.put("despro", fila[i++]);
                    jsonArray.put(jsonObj);
                }
                return jsonArray.toString();
            }

        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    // Método auxiliar para procesar resultados
    private void procesarResultados(Query queryConStock, Query queryVacios,
            JSONArray lotesArray, JSONArray lotesVaciosArray) {

        // Procesar lotes con stock
        List<Object[]> resultadosConStock = queryConStock.getResultList();
        for (Object[] fila : resultadosConStock) {
            if (fila[0] != null) { // Aseguramos que el lote no sea nulo
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("lote", fila[0]);
                jsonObj.put("tome", fila[1] != null ? fila[1] : 0);
                jsonObj.put("tomf", fila[2] != null ? fila[2] : 0);
                jsonObj.put("stkalm", fila[3] != null ? fila[3] : 0);
                jsonObj.put("stkalm_m", fila[4] != null ? fila[4] : 0);
                lotesArray.put(jsonObj);
            }
        }

        // Procesar lotes vacíos (ahora con todos los campos)
        List<String> resultadosVacios = queryVacios.getResultList();
        for (String lote : resultadosVacios) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("lote", lote);
            jsonObj.put("tome", 0);
            jsonObj.put("tomf", 0);
            jsonObj.put("stkalm", 0);
            jsonObj.put("stkalm_m", 0);
            lotesVaciosArray.put(jsonObj);
        }
    }

    public String listarlotes(int codinvalm, String codpro, String codalm) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            JSONObject response = new JSONObject();
            JSONArray lotesArray = new JSONArray(); // Lotes con stock (del producto especificado)
            JSONArray lotesVaciosArray = new JSONArray(); // Lotes vacíos (solo del producto especificado)

            // Lógica similar para SUPINV (omitiendo coddeta)
            Query queryConStock = em.createNativeQuery(
                    "SELECT " +
                            "   COALESCE(it.lote, c.codlot) AS lote, " +
                            "   COALESCE(it.tome, 0) AS tome, " +
                            "   COALESCE(it.tomf, 0) AS tomf, " +
                            "   CASE " +
                            "     WHEN it.lote IS NOT NULL THEN COALESCE(it.stkalm, 0) " + // Si existe en
                                                                                           // inventario_toma, usa tome
                                                                                           // de
                                                                                           // allí
                            "     ELSE COALESCE(c.qtymov, 0) " + // Si no existe, usa qtymov de stock
                            "   END AS stkalm, " +
                            "   CASE " +
                            "     WHEN it.lote IS NOT NULL THEN COALESCE(it.stkalm_m, 0) " + // Si existe en
                            // inventario_toma, usa tomf de
                            // allí
                            "     ELSE COALESCE(c.qtymov_m, 0) " + // Si no existe, usa qtymov_m de stock
                            "   END AS stkalm_m " +
                            "FROM ( " +
                            "   SELECT s.codlot, s.qtymov, s.qtymov_m " +
                            "   FROM fa_stock_vencimientos s " +
                            "   INNER JOIN inventario_almacen ia ON ia.codalm = s.codalm AND ia.codinvalm = ? " +
                            "   WHERE (s.qtymov > 0 OR s.qtymov_m > 0) AND s.codpro = ? " +
                            ") c " +
                            "FULL OUTER JOIN ( " +
                            "   SELECT lote, tome, tomf, stkalm, stkalm_m FROM inventario_toma " +
                            "   WHERE codinvalm = ? AND codpro = ? " +
                            ") it ON it.lote = c.codlot");

            Query queryVacios = em.createNativeQuery(
                    "SELECT s.codlot " +
                            "FROM fa_stock_vencimientos s " +
                            "WHERE s.fecven > GETDATE() " +
                            "  AND s.codpro = ?1 " +
                            "  AND s.codalm = ?2 " +
                            "  AND (s.qtymov = 0 OR s.qtymov IS NULL) " +
                            "  AND (s.qtymov_m = 0 OR s.qtymov_m IS NULL) " +
                            "  AND NOT EXISTS ( " +
                            "      SELECT 1 FROM inventario_toma it " +
                            "      WHERE it.lote = s.codlot " +
                            "        AND it.codpro = ?3 " +
                            "        AND it.codinvalm = ?4 " +
                            ")");

            // Parámetros (similar al caso anterior)
            int p = 1;
            queryConStock.setParameter(p++, codinvalm);
            queryConStock.setParameter(p++, codpro);
            queryConStock.setParameter(p++, codinvalm);
            queryConStock.setParameter(p++, codpro);

            // Parámetros para lotes vacíos (reiniciar contador)
            p = 1;
            queryVacios.setParameter(p++, codpro);
            queryVacios.setParameter(p++, codalm);
            queryVacios.setParameter(p++, codpro);
            queryVacios.setParameter(p++, codinvalm);
            // ... (implementación similar omitida por brevedad)
            procesarResultados(queryConStock, queryVacios, lotesArray, lotesVaciosArray);

            response.put("lotes", lotesArray);
            response.put("lotes_vacios", lotesVaciosArray);
            return response.toString();

        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarlotescaptura(int codinvalm, String codpro, String codalm) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            JSONObject response = new JSONObject();
            JSONArray lotesArray = new JSONArray(); // Todos los lotes (con stock y vacíos)
            JSONArray lotesVaciosArray = new JSONArray(); // Solo códigos de lotes vacíos (para compatibilidad)

            // Consulta para lotes con stock (sin coddeta)
            Query queryConStock = em.createNativeQuery(
                    "SELECT ISNULL(c.codlot,it.lote), it.tome, it.tomf, ISNULL(c.qtymov,0), ISNULL(c.qtymov_m,0) "
                            +
                            "FROM ( " +
                            "    SELECT s.codlot, s.cante as qtymov, s.cantf as qtymov_m " +
                            "    FROM capturastocksinventario s " +
                            "    INNER JOIN inventario_almacen ia ON ia.codinvcab = s.codinvcab AND ia.codinvalm = ? "
                            +
                            "    WHERE (s.cante > 0 OR s.cantf > 0) AND s.codpro = ? " +
                            ") c " +
                            "FULL OUTER JOIN ( " +
                            "    SELECT lote, tome, tomf FROM inventario_toma " +
                            "    WHERE codinvalm = ? AND codpro = ? " +
                            ") it ON it.lote = c.codlot");

            // Consulta modificada para lotes vacíos (sin coddeta)
            Query queryVacios = em.createNativeQuery(
                    "SELECT s.codlot " +
                            "FROM fa_stock_vencimientos s " +
                            "WHERE s.fecven > GETDATE() " +
                            "  AND s.codpro = ?1 " +
                            "  AND s.codalm = ?2 " +
                            "  AND (s.qtymov = 0 OR s.qtymov IS NULL) " +
                            "  AND (s.qtymov_m = 0 OR s.qtymov_m IS NULL) " +
                            "  AND NOT EXISTS ( " +
                            "      SELECT 1 FROM inventario_toma it " +
                            "      WHERE it.lote = s.codlot " +
                            "        AND it.codpro = ?3 " +
                            "        AND it.codinvalm = ?4 " +
                            ")");

            // Parámetros para consulta con stock
            int o = 1;
            queryConStock.setParameter(o++, codinvalm);
            queryConStock.setParameter(o++, codpro);
            queryConStock.setParameter(o++, codinvalm);
            queryConStock.setParameter(o++, codpro);

            // Parámetros para consulta de vacíos
            o = 1;
            queryVacios.setParameter(o++, codpro);
            queryVacios.setParameter(o++, codalm);
            queryVacios.setParameter(o++, codpro);
            queryVacios.setParameter(o++, codinvalm);

            // Procesar resultados
            procesarResultados(queryConStock, queryVacios, lotesArray, lotesVaciosArray);

            response.put("lotes", lotesArray);
            response.put("lotes_vacios", lotesVaciosArray); // Mantenemos esto por compatibilidad
            return response.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String getAlmacen(int codinvalm) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sql = "SELECT codalm FROM inventario_almacen WHERE codinvalm = ?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, codinvalm);

            Object result = query.getSingleResult();
            if (result != null) {
                return result.toString();
            } else {
                return null; // o algún valor por defecto
            }
        } catch (NoResultException e) {
            // No hay resultados para ese codinvalm
            return null;
        } catch (Exception e) {
            // Loguear el error para diagnóstico
            e.printStackTrace();
            return null; // o lanzar excepción personalizada
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void create(InventarioToma inventarioToma) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(inventarioToma);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(InventarioToma inventarioToma) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            inventarioToma = em.merge(inventarioToma);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = inventarioToma.getCodtom();
                if (findInventarioToma(id) == null) {
                    throw new NonexistentEntityException("The inventarioToma with id " + id + " no longer exists.");
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
            InventarioToma inventarioToma;
            try {
                inventarioToma = em.getReference(InventarioToma.class, id);
                inventarioToma.getCodtom();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The inventarioToma with id " + id + " no longer exists.", enfe);
            }
            em.remove(inventarioToma);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<InventarioToma> findInventarioTomaEntities() {
        return findInventarioTomaEntities(true, -1, -1);
    }

    public List<InventarioToma> findInventarioTomaEntities(int maxResults, int firstResult) {
        return findInventarioTomaEntities(false, maxResults, firstResult);
    }

    private List<InventarioToma> findInventarioTomaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(InventarioToma.class));
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

    public InventarioToma findInventarioToma(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(InventarioToma.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getInventarioTomaCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<InventarioToma> rt = cq.from(InventarioToma.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
