package pe.bartolito.conta.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.JpaPadre;

public class ClasificadoCentroCostoDao extends JpaPadre {
    public ClasificadoCentroCostoDao(String empresa) {
        super(empresa);
    }

    public String getRegistrosJson() {
        EntityManager em = null;
        JSONArray json = new JSONArray();
        try {
            em = getEntityManager();
            String sql = "SELECT " +
                    "    c.ClasiUnidOperaId, " + // 0
                    "    c.Cuenta, " + // 1
                    "    u.UnidOperaDescripcion, " + // 2
                    "    c.UnidOperaId, " + // 03
                    "    t.TipoGastoDescripcion, " + // 04
                    "    c.TipoGastoId, " + // 05
                    "    pro.ProcDescripcion, " + // 06
                    "    c.CS, " + // 07
                    "    a.ActivDescripcion, " + // 09
                    "    c.AC, " + // 10
                    "    ta.TareaDescripcion, " +
                    "    c.TA, " +
                    "    act.ActivoDescripcion, " +
                    "    c.ACT, " +
                    "    pr.ProdDescripcion, " +
                    "    c.PR, " +
                    "    cr.CenCostRespDescripcion, " +
                    "    c.CR " +
                    "FROM ClasificadorCentroCosto c " +
                    "LEFT JOIN UnidadOperativa u ON c.UnidOperaId = u.UnidOperald " +
                    "LEFT JOIN TipoGasto t ON c.TipoGastoId = t.TipoGastold " +
                    "LEFT JOIN Proceso pro ON c.CS = pro.ProcId AND pro.ProcEstado = 1 " +
                    "LEFT JOIN Actividad a ON c.AC = a.ActivId AND a.ProcId = pro.ProcId AND a.ActivEstado = 1 " +
                    "LEFT JOIN Tarea ta ON c.TA = ta.TareaId AND ta.ProcId = pro.ProcId AND ta.ActivId = a.ActivId AND ta.EstadoTarea = 1 "
                    +
                    "LEFT JOIN Activo act ON c.ACT = act.ActivoId AND act.ActivoEstado = 1 " +
                    "LEFT JOIN ProductoServicio pr ON c.PR = pr.ProdId AND pr.ProdEstado = 1 " +
                    "LEFT JOIN CentroCostoResponsabilidad cr ON c.CR = cr.CenCostResp AND cr.CenCostRespEstado = 1";

            Query q = em.createNativeQuery(sql);
            List<Object[]> resultados = q.getResultList();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("ClasiUnidOperaId", fila[0]);
                jsonObj.put("Cuenta", fila[1]);
                jsonObj.put("UnidOperaDescripcion", fila[2]);
                jsonObj.put("UnidOperaId", fila[3]);
                jsonObj.put("TipoGastoDescripcion", fila[4]);
                jsonObj.put("TipoGastoId", fila[5]);
                jsonObj.put("ProcesoDescripcion", fila[6]);
                jsonObj.put("ProcesoId", fila[7]);
                jsonObj.put("ActividadDescripcion", fila[8]);
                jsonObj.put("ActividadId", fila[9]);
                jsonObj.put("TareaDescripcion", fila[10]);
                jsonObj.put("TareaId", fila[11]);
                jsonObj.put("ActivoDescripcion", fila[12]);
                jsonObj.put("ActivoId", fila[13]);
                jsonObj.put("ProductoDescripcion", fila[14]);
                jsonObj.put("ProductoId", fila[15]);
                jsonObj.put("CentroCostoResponsabilidadDescripcion", fila[16]);
                jsonObj.put("CentroCostoResponsabilidadId", fila[17]);
                json.put(jsonObj);
            }
            return json.toString();
        } catch (Exception e) {
            json = new JSONArray();
            JSONObject obj = new JSONObject();
            obj.put("error", e.getMessage());
            json.put(obj);
            return json.toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String crearMultiplesClasificadores(String json) {
        String result = "E"; // Valor por defecto es error
        EntityManager em = null;

        try {
            JSONObject jsonobj = new JSONObject(json);

            // Obtener el array de clasificadores del JSON
            JSONArray clasificadoresArray = jsonobj.getJSONArray("clasificadores");

            // Convertir JSONArray a formato XML para el parámetro XML
            StringBuilder xmlBuilder = new StringBuilder();
            xmlBuilder.append("<Registros>");

            for (int i = 0; i < clasificadoresArray.length(); i++) {
                JSONObject clasificador = clasificadoresArray.getJSONObject(i);
                xmlBuilder.append("<Registro>");
                xmlBuilder.append("<UnidOperaId>").append(clasificador.getString("UnidOperaId"))
                        .append("</UnidOperaId>");
                xmlBuilder.append("<TipoGastoId>").append(clasificador.getString("TipoGastoId"))
                        .append("</TipoGastoId>");
                xmlBuilder.append("<Cuenta>").append(clasificador.getString("Cuenta")).append("</Cuenta>");
                xmlBuilder.append("<CS>").append(clasificador.optString("CS", "")).append("</CS>");
                xmlBuilder.append("<AC>").append(clasificador.optString("AC", "")).append("</AC>");
                xmlBuilder.append("<TA>").append(clasificador.optString("TA", "")).append("</TA>");
                xmlBuilder.append("<ACT>").append(clasificador.optString("ACT", "")).append("</ACT>");
                xmlBuilder.append("<PR>").append(clasificador.optString("PR", "")).append("</PR>");
                xmlBuilder.append("<CR>").append(clasificador.optString("CR", "")).append("</CR>");
                xmlBuilder.append("</Registro>");
            }
            xmlBuilder.append("</Registros>");

            em = getEntityManager();

            // Llamar al procedimiento almacenado con parámetro XML y parámetro OUTPUT
            StoredProcedureQuery spQuery = em.createStoredProcedureQuery("sp_InsClasificadorCentroCosto");

            // Registrar parámetros
            spQuery.registerStoredProcedureParameter("XmlRegistros", String.class, ParameterMode.IN);
            spQuery.registerStoredProcedureParameter("TotalInsertados", Integer.class, ParameterMode.OUT);

            // Establecer valor del parámetro XML
            spQuery.setParameter("XmlRegistros", xmlBuilder.toString());

            // Ejecutar el procedimiento almacenado
            spQuery.execute();

            // Obtener el valor del parámetro OUTPUT
            Integer totalInsertados = (Integer) spQuery.getOutputParameterValue("TotalInsertados");

            System.out.println("Total de registros insertados: " + totalInsertados);

            result = "S"; // Éxito

        } catch (Exception e) {
            e.printStackTrace();
            result = e.getMessage(); // Error
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }

        return result;
    }

    public String actualizarMultiplesClasificadores(String json) {
        String result = "E"; // Valor por defecto es error
        EntityManager em = null;

        try {
            JSONObject jsonobj = new JSONObject(json);
            // Obtener el array de clasificadores del JSON
            JSONArray clasificadoresArray = jsonobj.getJSONArray("clasificadores");

            // Convertir JSONArray a formato XML para el parámetro XML
            StringBuilder xmlBuilder = new StringBuilder();
            xmlBuilder.append("<Registros>");

            for (int i = 0; i < clasificadoresArray.length(); i++) {
                JSONObject clasificador = clasificadoresArray.getJSONObject(i);
                xmlBuilder.append("<Registro>");
                xmlBuilder.append("<ClasiUnidOperaId>").append(clasificador.getString("ClasiUnidOperaId"))
                        .append("</ClasiUnidOperaId>");
                xmlBuilder.append("<UnidOperaId>").append(clasificador.getString("UnidOperaId"))
                        .append("</UnidOperaId>");
                xmlBuilder.append("<TipoGastoId>").append(clasificador.getString("TipoGastoId"))
                        .append("</TipoGastoId>");
                xmlBuilder.append("<Cuenta>").append(clasificador.getString("Cuenta")).append("</Cuenta>");
                xmlBuilder.append("<CS>").append(clasificador.optString("CS", "")).append("</CS>");
                xmlBuilder.append("<AC>").append(clasificador.optString("AC", "")).append("</AC>");
                xmlBuilder.append("<TA>").append(clasificador.optString("TA", "")).append("</TA>");
                xmlBuilder.append("<ACT>").append(clasificador.optString("ACT", "")).append("</ACT>");
                xmlBuilder.append("<PR>").append(clasificador.optString("PR", "")).append("</PR>");
                xmlBuilder.append("<CR>").append(clasificador.optString("CR", "")).append("</CR>");
                xmlBuilder.append("</Registro>");
            }
            xmlBuilder.append("</Registros>");

            em = getEntityManager();

            // Llamar al procedimiento almacenado con parámetro XML y parámetro OUTPUT
            StoredProcedureQuery spQuery = em.createStoredProcedureQuery("sp_UpdClasificadorCentroCosto");

            // Registrar parámetros
            spQuery.registerStoredProcedureParameter("XmlRegistros", String.class, ParameterMode.IN);
            spQuery.registerStoredProcedureParameter("TotalActualizados", Integer.class, ParameterMode.OUT);

            // Establecer valor del parámetro XML
            spQuery.setParameter("XmlRegistros", xmlBuilder.toString());

            // Ejecutar el procedimiento almacenado
            spQuery.execute();

            // Obtener el valor del parámetro OUTPUT
            Integer totalActualizados = (Integer) spQuery.getOutputParameterValue("TotalActualizados");

            System.out.println("Total de registros actualizados: " + totalActualizados);

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

    public String eliminarMultiplesClasificadores(String json) {
        String result = "E"; // Valor por defecto es error
        EntityManager em = null;

        try {
            JSONObject jsonobj = new JSONObject(json);
            // Obtener el array de IDs del JSON
            JSONArray idsArray = jsonobj.getJSONArray("ids");

            // Convertir JSONArray a formato XML para el parámetro XML
            StringBuilder xmlBuilder = new StringBuilder();
            xmlBuilder.append("<root>");

            for (int i = 0; i < idsArray.length(); i++) {
                String id = idsArray.getString(i);
                xmlBuilder.append("<row>");
                xmlBuilder.append("<ClasiUnidOperaId>").append(id).append("</ClasiUnidOperaId>");
                xmlBuilder.append("</row>");
            }
            xmlBuilder.append("</root>");

            em = getEntityManager();

            // Crear StoredProcedureQuery para llamar al procedimiento almacenado
            StoredProcedureQuery spQuery = em.createStoredProcedureQuery("sp_DelClasificadorCentroCosto");

            // Registrar parámetros
            spQuery.registerStoredProcedureParameter("XmlRegistros", String.class, ParameterMode.IN);
            spQuery.registerStoredProcedureParameter("TotalEliminados", Integer.class, ParameterMode.OUT);

            // Establecer valor del parámetro XML
            spQuery.setParameter("XmlRegistros", xmlBuilder.toString());

            // Ejecutar el procedimiento almacenado
            spQuery.execute();

            // Obtener el valor del parámetro OUTPUT
            Integer totalEliminados = (Integer) spQuery.getOutputParameterValue("TotalEliminados");

            System.out.println("Total de registros eliminados: " + totalEliminados);

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

    public String getReporteClasificadores(String empresa, int ano, int mes) {
        EntityManager em = null;
        JSONArray json = new JSONArray();

        try {
            em = getEntityManager();

            // Crear la consulta nativa para llamar al procedimiento almacenado
            Query query = em.createNativeQuery(
                    "EXEC dbo.sp_SelReporteMayorAuxiliarConClasificador ?, ?, ?");

            // Establecer los parámetros
            query.setParameter(1, empresa);
            query.setParameter(2, ano);
            query.setParameter(3, mes);

            // Ejecutar la consulta y obtener los resultados
            @SuppressWarnings("unchecked")
            List<Object[]> resultados = query.getResultList();

            // Procesar los resultados y convertirlos a JSON
            for (Object[] fila : resultados) {
                JSONObject obj = new JSONObject();

                // Mapear cada columna del resultado (según el ORDER del SELECT en tu SP)
                obj.put("cuenta", fila[0] != null ? fila[0].toString() : "");
                obj.put("procId", fila[1] != null ? fila[1].toString() : "");
                obj.put("activId", fila[2] != null ? fila[2].toString() : "");
                obj.put("tareaId", fila[3] != null ? fila[3].toString() : "");
                obj.put("activoId", fila[4] != null ? fila[4].toString() : "");
                obj.put("prodId", fila[5] != null ? fila[5].toString() : "");
                obj.put("cenCostResp", fila[6] != null ? fila[6].toString() : "");
                obj.put("ctaCte", fila[7] != null ? fila[7].toString() : "");
                obj.put("tp", fila[8] != null ? fila[8].toString() : "");
                obj.put("subMov", fila[9] != null ? fila[9].toString() : "");
                obj.put("numComp", fila[10] != null ? fila[10].toString() : "");
                obj.put("diaDetItem", fila[11] != null ? fila[11].toString() : "");
                obj.put("fecha", fila[12] != null ? fila[12].toString() : "");
                obj.put("glosa", fila[13] != null ? fila[13].toString() : "");
                obj.put("documento", fila[14] != null ? fila[14].toString() : "");
                obj.put("numDocumento", fila[15] != null ? fila[15].toString() : "");
                obj.put("importe", fila[16] != null ? fila[16].toString() : "0.00");
                obj.put("mes", fila[17] != null ? fila[17].toString() : "");
                obj.put("centroCosto", fila[18] != null ? fila[18].toString() : "");
                obj.put("cenCostDescripcion", fila[19] != null ? fila[19].toString() : "");
                obj.put("clasiUnidOperaId", fila[20] != null ? fila[20].toString() : "");
                obj.put("unidOperaId", fila[21] != null ? fila[21].toString() : "");
                obj.put("tipoGastoId", fila[22] != null ? fila[22].toString() : "");

                json.put(obj);
            }

        } catch (Exception e) {
            json = new JSONArray();
            JSONObject obj = new JSONObject();
            obj.put("error", "Error al ejecutar el reporte: " + e.getMessage());
            json.put(obj);
            e.printStackTrace(); // Para debugging

        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return json.toString();
    }
}
