/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dao;

import dao.JpaPadre;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author USUARIO
 */
public class CombosConciliacionTarjetas extends JpaPadre {

    public CombosConciliacionTarjetas(String empresa) {
        super(empresa);
    }

    public String ComboSucursal() {
        EntityManager em = null;
        JSONArray json = new JSONArray();
        try {
            em = getEntityManager();

            String sql = "select * from Operaciones_Tarjeta_Sucursal";
            Query q = em.createNativeQuery(sql);

            List<Object[]> resultados = q.getResultList();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("id", fila[0] + "-" + fila[2]);
                jsonObj.put("sucursal", fila[1]);
                json.put(jsonObj);
            }
            return json.toString();
        } catch (Exception e) {
            json = new JSONArray();
            return json.toString();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public String ComboFecha(String sucursal) {
        EntityManager em = null;
        JSONArray json = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            em = getEntityManager();
            String sql = "SELECT DISTINCT Fecha_Abono FROM Operaciones_Tarjeta WHERE Fecha_Abono IS NOT NULL AND Codigo = ? AND Estado = 0 ORDER BY Fecha_Abono ASC";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, sucursal);
            List<Object> resultados = q.getResultList();
            for (Object fecha : resultados) {
                /*JSONObject jsonObj = new JSONObject();
                jsonObj.put("id", fila.toString());
                jsonObj.put("fecha", fila.toString());
                json.put(jsonObj);*/

                JSONObject jsonObj = new JSONObject();
                String fechaFormateada = "";

                try {
                    if (fecha instanceof Date) {
                        fechaFormateada = sdf.format((Date) fecha);
                    } else if (fecha instanceof java.sql.Date) {
                        fechaFormateada = sdf.format((java.sql.Date) fecha);
                    } else if (fecha instanceof java.sql.Timestamp) {
                        fechaFormateada = sdf.format(new Date(((java.sql.Timestamp) fecha).getTime()));
                    } else {
                        fechaFormateada = fecha.toString();
                    }

                    jsonObj.put("id", fechaFormateada);
                    jsonObj.put("fecha", fechaFormateada);
                    json.put(jsonObj);

                } catch (Exception dateEx) {
                    System.out.println("Error formateando fecha: " + fecha + " - " + dateEx.getMessage());
                    // Usar el valor original si hay error
                    jsonObj.put("id", fecha.toString());
                    jsonObj.put("fecha", fecha.toString());
                    json.put(jsonObj);
                }
            }
            return json.toString();
        } catch (Exception e) {
            json = new JSONArray();
            return json.toString();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public String ComboMonto(String sucursal, String fecha) {
        EntityManager em = null;
        JSONArray json = new JSONArray();
        try {
            em = getEntityManager();
            String sql = "select DISTINCT Neto_Total from Operaciones_Tarjeta where Neto_Total is not null AND Codigo = ? AND Fecha_Abono = ? AND Estado = 0 order by Neto_Total asc";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, sucursal);
            q.setParameter(2, fecha);
            List<Object> resultados = q.getResultList();
            for (Object fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("id", fila.toString());
                jsonObj.put("total", fila.toString());
                json.put(jsonObj);
            }
            return json.toString();
        } catch (Exception e) {
            json = new JSONArray();
            return json.toString();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public static String calcularMesAnterior(String mesActual) {
        // Dividir el string "YYYY-MM" en año y mes
        String[] partes = mesActual.split("-");
        int año = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]);

        // Decrementar mes
        mes--;

        // Si el mes es 0 (enero anterior), cambiar a diciembre del año anterior
        if (mes == 0) {
            mes = 12;
            año--;
        }

        // Formatear de vuelta a string con formato "YYYY-MM"
        return String.format("%04d-%02d", año, mes);
    }

    public String TraerDataFiltrada(String sucursalYUnidComId, String fecha, double monto) {

        String[] partes = sucursalYUnidComId.split("\\-");  // el pipe es carácter especial en regex, por eso \\|

        String sucursal = "";
        String unidComId = "";

        if (partes.length == 2) {
            sucursal = partes[0];   // "001041426"
            unidComId = partes[1]; // "8"
        }

        EntityManager em = null;
        StringBuilder json = new StringBuilder();

        try {
            em = getEntityManager();

            Query q = em.createNativeQuery("EXEC sp_bart_conciliacion_tarjetasizipay_comprobante ?, ?, ?, ?");

            q.setParameter(1, sucursal);
            q.setParameter(2, fecha);
            q.setParameter(3, monto);
            q.setParameter(4, Integer.parseInt(unidComId));

            List<?> resultList = q.getResultList();

            for (Object row : resultList) {
                json.append(row.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            json.setLength(0);
            json.append("{\"error\":\"Error al obtener datos\"}");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return json.toString();

    }


    /*
    public String TraerDataFiltrada(String sucursalYUnidComId, String fecha, double monto) {
        String[] partes = sucursalYUnidComId.split("\\|");
        String sucursal = "";
        String unidComId = "";
        if (partes.length == 2) {
            sucursal = partes[0];
            unidComId = partes[1];
        }

        EntityManager em = null;
        JSONArray json = new JSONArray();
        String mesActual = fecha.substring(0, 7); // '2025-06'

// Calcular mes anterior (mismo código de arriba)
        String mesAnterior = calcularMesAnterior(mesActual);
        try {
            em = getEntityManager();

            
            String sql
                    = "WITH FilaObjetivo AS ( "
                    + "    SELECT id, Codigo, Neto_Total, Neto_Parcial "
                    + "    FROM Operaciones_Tarjeta "
                    + "    WHERE Codigo = ? "
                    + "      AND Fecha_Abono = ? "
                    + "      AND ROUND(Neto_Total, 2) = ? "
                    + "), "
                    + "FilasAnteriores AS ( "
                    + "    SELECT "
                    + "        ot.id, "
                    + "        ot.Codigo, "
                    + "        ot.Neto_Parcial, "
                    + "        ot.Neto_Total, "
                    + "        fo.id AS fila_objetivo_id, "
                    + "        fo.Neto_Total AS objetivo_neto_total, "
                    + "        fo.Neto_Parcial AS objetivo_neto_parcial "
                    + "    FROM Operaciones_Tarjeta ot "
                    + "    JOIN FilaObjetivo fo ON ot.Codigo = fo.Codigo "
                    + "    WHERE ot.id < fo.id "
                    + "), "
                    + "FilasValidas AS ( "
                    + "    SELECT "
                    + "        fa.*, "
                    + "        CASE "
                    + "            WHEN EXISTS ( "
                    + "                SELECT 1 "
                    + "                FROM FilasAnteriores fa2 "
                    + "                WHERE fa2.id > fa.id "
                    + "                  AND fa2.id < fa.fila_objetivo_id "
                    + "                  AND fa2.Neto_Total > 0.00 "
                    + "            ) THEN 0 "
                    + "            ELSE 1 "
                    + "        END AS es_valida "
                    + "    FROM FilasAnteriores fa "
                    + "    WHERE fa.Neto_Total = 0.00 "
                    + "), "
                    + "ResultadoFinal AS ( "
                    + "    SELECT ot.* "
                    + "    FROM Operaciones_Tarjeta ot "
                    + "    JOIN FilaObjetivo fo ON ot.id = fo.id "
                    + "    WHERE fo.Neto_Parcial = fo.Neto_Total "
                    + "    UNION ALL "
                    + "    SELECT ot.* "
                    + "    FROM Operaciones_Tarjeta ot "
                    + "    JOIN FilaObjetivo fo ON ot.Codigo = fo.Codigo "
                    + "    JOIN FilasValidas fv ON ot.id = fv.id "
                    + "    WHERE fo.Neto_Parcial != fo.Neto_Total "
                    + "      AND fv.es_valida = 1 "
                    + "    UNION ALL "
                    + "    SELECT ot.* "
                    + "    FROM Operaciones_Tarjeta ot "
                    + "    JOIN FilaObjetivo fo ON ot.id = fo.id "
                    + "    WHERE fo.Neto_Parcial != fo.Neto_Total "
                    + "), "
                    + "FormaPagoRef AS ( "
                    + "    SELECT "
                    + "        CONVERT(date, cvc.VtaCabFecha) AS fecha, "
                    + "        fp.VtaCabNumComp, "
                    + "        TipFormPagId, "
                    + "        ForPagnum_ope, "
                    + "        ForPagMonto, "
                    + "        CASE  "
                    + "            WHEN CHARINDEX('REF:', ForPagnum_ope) > 0  "
                    + "            THEN SUBSTRING(ForPagnum_ope, CHARINDEX('REF:', ForPagnum_ope) + 4, 4) "
                    + "            ELSE NULL "
                    + "        END AS REF "
                    + "    FROM FormaPago fp "
                    + "    INNER JOIN ComprobanteVentaCabecera cvc "
                    + "      ON fp.EmpresaId = cvc.EmpresaId  and fp.EmpresaId='03' "
                    + "     AND fp.UnidComId = cvc.UnidComId and cvc.UnidComId=? "
                    + "     AND fp.DocId = cvc.DocId "
                    + "     AND fp.VtaCabNumComp = cvc.VtaCabNumComp "
                    + "    WHERE "
                    + "    (SUBSTRING(CONVERT(varchar, cvc.VtaCabFecha, 23), 1, 7) = '" + mesActual + "' "
                    + "     OR SUBSTRING(CONVERT(varchar, cvc.VtaCabFecha, 23), 1, 7) = '" + mesAnterior + "') "
                    + "      AND TipFormPagId = 19 "
                    + ") "
                    + "SELECT DISTINCT "
                    + "    rf.id, "
                    + "    rf.Codigo, "
                    + "    rf.Voucher, "
                    + "    rf.Fecha_Consumo, "
                    + "    rf.Importe, "
                    + "    rf.Comision, "
                    + "    RIGHT('0000' + rf.Voucher, 4) as REFIZIPAY, "
                    + "    fp.fecha AS FecSistema, "
                    + "    fp.VtaCabNumComp, "
                    + "    fp.ForPagnum_ope, "
                    + "    fp.REF, "
                    + "    fp.ForPagMonto "
                    + "FROM ResultadoFinal rf "
                    + "LEFT JOIN FormaPagoRef fp "
                    + "  ON fp.REF = RIGHT('0000' + rf.Voucher, 4) "
                    + " AND rf.Fecha_Consumo = fp.fecha "
                    + "ORDER BY rf.id ASC;";

            Query q = em.createNativeQuery(sql);
            q.setParameter(1, sucursal);
            q.setParameter(2, fecha);
            q.setParameter(3, monto);
            q.setParameter(4, unidComId);
            List<Object[]> resultados = q.getResultList();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                // Mapear columnas según la nueva consulta SELECT
                jsonObj.put("id", fila[0]); // rf.id
                jsonObj.put("codigo", fila[1]); // rf.Codigo
                jsonObj.put("voucher", fila[2]); // rf.Voucher
                jsonObj.put("fecha_consumo", fila[3]); // rf.Fecha_Consumo
                jsonObj.put("importe", fila[4]); // rf.Importe
                jsonObj.put("comision", fila[5]); // rf.Comision
                jsonObj.put("refizipay", fila[6]); // RIGHT(rf.Voucher, 4) as REFIZIPAY
                jsonObj.put("fec_sistema", fila[7]); // fp.fecha AS FecSistema
                jsonObj.put("vta_cab_num_comp", fila[8]); // fp.VtaCabNumComp
                jsonObj.put("for_pag_num_ope", fila[9]); // fp.ForPagnum_ope
                jsonObj.put("ref", fila[10]); // fp.REF
                jsonObj.put("forPagMonto", fila[11]); // fp.REF

                json.put(jsonObj);
            }
            return json.toString();
        } catch (Exception e) {
            json = new JSONArray();
            return json.toString();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    } */
}
