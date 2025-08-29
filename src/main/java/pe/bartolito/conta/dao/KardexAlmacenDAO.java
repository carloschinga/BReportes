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
public class KardexAlmacenDAO extends JpaPadre {

    public KardexAlmacenDAO(String empresa) {
        super(empresa);
    }

    public String ComboKardexPorMes(String mes, String codalm) {
        EntityManager em = null;
        try {
            em = getEntityManager();

            // Llamada al procedimiento almacenado
            String sql = "EXEC sp_bart_kardex_json_por_mes_almacen ?, ?";

            Query q = em.createNativeQuery(sql);
            q.setParameter(1, mes);     // formato 'yyyyMM', ej: "202408"
            q.setParameter(2, codalm);  // código de almacén, ej: "A1"

            @SuppressWarnings("unchecked")
            List<String> resultados = q.getResultList();

            if (!resultados.isEmpty() && resultados.get(0) != null) {
                return resultados.get(0); // JSON generado por el SP
            } else {
                return "[]"; // Sin datos
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "[]";
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

}

/*
    public String ComboKardexPorMes(String mes) {
        EntityManager em = null;
        JSONArray json = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            em = getEntityManager();

            // Consulta filtrando por el año-mes
            String sql = "WITH Movimientos AS ( "
                    + "    SELECT "
                    + "        despro, "
                    + "        fecmov, "
                    + "        docid, "
                    + "        serdoc, "
                    + "        numdoc, "
                    + "        tipo_ope, "
                    + "        tip_mov, "
                    + "        cant, "
                    + "        coscom, "
                    + "        CASE WHEN tip_mov = 'I' THEN cant ELSE 0 END AS cant_entrada, "
                    + "        CASE WHEN tip_mov = 'I' THEN coscom ELSE 0 END AS coscom_entrada, "
                    + "        CASE WHEN tip_mov = 'I' THEN cant * coscom ELSE 0 END AS total_entrada, "
                    + "        CASE WHEN tip_mov = 'S' THEN cant ELSE 0 END AS cant_salida, "
                    + "        CASE WHEN tip_mov = 'S' THEN coscom ELSE 0 END AS coscom_salida, "
                    + "        CASE WHEN tip_mov = 'S' THEN cant * coscom ELSE 0 END AS total_salida "
                    + "    FROM fa_movimiento_almacen "
                    + "    WHERE CONVERT(CHAR(6), fecmov, 112) = ? "
                    + // Filtra por año-mes
                    ") "
                    + "SELECT "
                    + "    despro, fecmov, docid, serdoc, numdoc, tipo_ope, "
                    + "    cant_entrada, coscom_entrada, total_entrada, "
                    + "    cant_salida, coscom_salida, total_salida, "
                    + "    SUM(cant_entrada - cant_salida) OVER (PARTITION BY despro ORDER BY fecmov, docid, serdoc, numdoc ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS cant_saldo, "
                    + "    SUM(total_entrada - total_salida) OVER (PARTITION BY despro ORDER BY fecmov, docid, serdoc, numdoc ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS total_saldo "
                    + "FROM Movimientos "
                    + "ORDER BY despro, fecmov, docid, serdoc, numdoc";

            Query q = em.createNativeQuery(sql);
            q.setParameter(1, mes); // Formato 'yyyyMM' (ejemplo: "202508")

            List<Object[]> resultados = q.getResultList();

            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();

                // Formatear fecha
                String fechaFormateada = "";
                try {
                    Object fecha = fila[1];
                    if (fecha instanceof Date) {
                        fechaFormateada = sdf.format((Date) fecha);
                    } else if (fecha instanceof java.sql.Date) {
                        fechaFormateada = sdf.format((java.sql.Date) fecha);
                    } else if (fecha instanceof java.sql.Timestamp) {
                        fechaFormateada = sdf.format(new Date(((java.sql.Timestamp) fecha).getTime()));
                    } else {
                        fechaFormateada = fecha.toString();
                    }
                } catch (Exception dateEx) {
                    fechaFormateada = fila[1] != null ? fila[1].toString() : "";
                }

                // Armar JSON
                jsonObj.put("despro", fila[0]);
                jsonObj.put("fecmov", fechaFormateada);
                jsonObj.put("docid", fila[2]);
                jsonObj.put("serdoc", fila[3]);
                jsonObj.put("numdoc", fila[4]);
                jsonObj.put("tipo_ope", fila[5]);
                jsonObj.put("cant_entrada", fila[6]);
                jsonObj.put("coscom_entrada", fila[7]);
                jsonObj.put("total_entrada", fila[8]);
                jsonObj.put("cant_salida", fila[9]);
                jsonObj.put("coscom_salida", fila[10]);
                jsonObj.put("total_salida", fila[11]);
                jsonObj.put("cant_saldo", fila[12]);
                jsonObj.put("total_saldo", fila[13]);

                json.put(jsonObj);
            }

            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONArray().toString();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
 */

