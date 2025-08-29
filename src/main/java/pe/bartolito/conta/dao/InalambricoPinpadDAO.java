package pe.bartolito.conta.dao;

import dao.JpaPadre;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author USUARIO
 */
public class InalambricoPinpadDAO extends JpaPadre {

    public InalambricoPinpadDAO(String empresa) {
        super(empresa);
    }

    public void createPinpadCsv(
            String codigo,
            String producto,
            String tipoMov,
            Date fechaProceso,
            Date fechaLote,
            String loteManual,
            String lotePos,
            String terminal,
            String voucher,
            String autorizacion,
            int cuotas,
            String tarjeta,
            String origen,
            String transaccion,
            Date fechaConsumo,
            BigDecimal importe,
            String statusPinpad,
            BigDecimal comision,
            BigDecimal comisionAfecta,
            BigDecimal igv,
            BigDecimal netoParcial,
            BigDecimal netoTotal,
            Date fechaAbono,
            String fechaAbono8Dig) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            String sql = "INSERT INTO Operaciones_Tarjeta (Codigo, Producto, Tipo_Mov, Fecha_Proceso, Fecha_Lote, Lote_Manual, Lote_Pos, Terminal, Voucher, Autorizacion, Cuotas, Tarjeta, Origen, Transaccion, Fecha_Consumo, Importe, Status_Pinpad, Comision, Comision_Afecta, IGV, Neto_Parcial, Neto_Total, Fecha_Abono, Fecha_Abono_8Dig) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            Query query = em.createNativeQuery(sql);
            query.setParameter(1, codigo);
            query.setParameter(2, producto);
            query.setParameter(3, tipoMov);
            query.setParameter(4, fechaProceso);
            query.setParameter(5, fechaLote);
            query.setParameter(6, loteManual);
            query.setParameter(7, lotePos);
            query.setParameter(8, terminal);
            query.setParameter(9, voucher);
            query.setParameter(10, autorizacion);
            query.setParameter(11, cuotas);
            query.setParameter(12, tarjeta);
            query.setParameter(13, origen);
            query.setParameter(14, transaccion);
            query.setParameter(15, fechaConsumo);
            query.setParameter(16, importe);
            query.setParameter(17, statusPinpad);
            query.setParameter(18, comision);
            query.setParameter(19, comisionAfecta);
            query.setParameter(20, igv);
            query.setParameter(21, netoParcial);
            query.setParameter(22, netoTotal);
            query.setParameter(23, fechaAbono);
            query.setParameter(24, fechaAbono8Dig);

            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void truncatePinpadCsv() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            em.createNativeQuery("TRUNCATE TABLE Operaciones_Tarjeta").executeUpdate();

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public boolean verificarSiExiste(String codigo, String voucher, String fechaconsumoStr ) {
        EntityManager em = null;
        try {
            em = getEntityManager();

            // Convertir String "dd/MM/yyyy" a java.sql.Date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.parse(fechaconsumoStr, formatter);
        java.sql.Date fechaconsumo = java.sql.Date.valueOf(localDate);
            
            
            Long count = ((Number) em.createNativeQuery(
                    "SELECT COUNT(*) FROM Operaciones_Tarjeta WHERE codigo = ? and  Voucher = ? and Fecha_Consumo=?")
                    .setParameter(1, codigo)
                    .setParameter(2, voucher)
                    .setParameter(3, fechaconsumo)
                    .getSingleResult()).longValue();

            return count > 0;

        } catch (Exception e) {
           
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void createInalambricoXls(
            String codigo,
            String tipoDeMovimiento,
            String tipoDeCaptura,
            String transaccion,
            Date fechaDeTransaccion,
            Time horaDeTransaccion,
            Date fechaDeCierreDeLote,
            Date fechaDeProceso,
            Date fechaDeAbono,
            String estado,
            BigDecimal importe,
            BigDecimal comision,
            BigDecimal igv,
            BigDecimal importeNeto,
            BigDecimal abonoDelLote,
            String numDeLote,
            String terminal,
            String numDeRefVoucher,
            String marcaDeTarjeta,
            String numDeTarjeta,
            String codigoDeAutorizacion,
            String cuotas,
            String observaciones,
            String moneda,
            String serieTerminal) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            String sql = "INSERT INTO Operaciones_Inalambrico (CODIGO, TIPO_DE_MOVIMIENTO, TIPO_DE_CAPTURA, TRANSACCION, FECHA_DE_TRANSACCION, HORA_DE_TRANSACCION, FECHA_DE_CIERRE_DE_LOTE, FECHA_DE_PROCESO, FECHA_DE_ABONO, ESTADO, IMPORTE, COMISION, IGV, IMPORTE_NETO, ABONO_DEL_LOTE, NUM_DE_LOTE, TERMINAL, NUM_DE_REF_VOUCHER, MARCA_DE_TARJETA, NUM_DE_TARJETA, CODIGO_DE_AUTORIZACION, CUOTAS, OBSERVACIONES, MONEDA, SERIE_TERMINAL) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            Query query = em.createNativeQuery(sql);
            query.setParameter(1, codigo);
            query.setParameter(2, tipoDeMovimiento);
            query.setParameter(3, tipoDeCaptura);
            query.setParameter(4, transaccion);
            query.setParameter(5, fechaDeTransaccion);
            query.setParameter(6, horaDeTransaccion);
            query.setParameter(7, fechaDeCierreDeLote);
            query.setParameter(8, fechaDeProceso);
            query.setParameter(9, fechaDeAbono);
            query.setParameter(10, estado);
            query.setParameter(11, importe);
            query.setParameter(12, comision);
            query.setParameter(13, igv);
            query.setParameter(14, importeNeto);
            query.setParameter(15, abonoDelLote);
            query.setParameter(16, numDeLote);
            query.setParameter(17, terminal);
            query.setParameter(18, numDeRefVoucher);
            query.setParameter(19, marcaDeTarjeta);
            query.setParameter(20, numDeTarjeta);
            query.setParameter(21, codigoDeAutorizacion);
            query.setParameter(22, cuotas);
            query.setParameter(23, observaciones);
            query.setParameter(24, moneda);
            query.setParameter(25, serieTerminal);

            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void truncateInalambricoXls() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            em.createNativeQuery("TRUNCATE TABLE Operaciones_Inalambrico").executeUpdate();

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public String findInalambricoXlsByFechaProceso(Date fechaInicio, Date fechaFin) {
        EntityManager em = null;
        JSONArray jsonArray = new JSONArray();
        try {
            em = getEntityManager();
            String sql = "SELECT * FROM Operaciones_Inalambrico WHERE Fecha_de_Proceso BETWEEN ? AND ? ORDER BY Fecha_de_Proceso ASC";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaInicio);
            query.setParameter(2, fechaFin);

            List<Object[]> rows = query.getResultList();
            for (Object[] row : rows) {
                JSONObject map = new JSONObject();
                map.put("ID", row[0]);
                map.put("Codigo", row[1]);
                map.put("Tipo_de_Movimiento", row[2]);
                map.put("Tipo_de_Captura", row[3]);
                map.put("Transaccion", row[4]);
                map.put("Fecha_de_Transaccion", row[5]);
                map.put("Hora_de_Transaccion", row[6]);
                map.put("Fecha_de_Cierre_de_Lote", row[7]);
                map.put("Fecha_de_Proceso", row[8]);
                map.put("Fecha_de_Abono", row[9]);
                map.put("Estado", row[10]);
                map.put("Importe", row[11]);
                map.put("Comision", row[12]);
                map.put("IGV", row[13]);
                map.put("Importe_Neto", row[14]);
                map.put("Abono_del_Lote", row[15]);
                map.put("Num_de_Lote", row[16]);
                map.put("Terminal", row[17]);
                map.put("Num_de_Ref_Voucher", row[18]);
                map.put("Marca_de_Tarjeta", row[19]);
                map.put("Num_de_Tarjeta", row[20]);
                map.put("Codigo_de_Autorizacion", row[21]);
                map.put("Cuotas", row[22]);
                map.put("Observaciones", row[23]);
                map.put("Moneda", row[24]);
                map.put("Serie_Terminal", row[25]);
                jsonArray.put(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonArray = new JSONArray();
            JSONObject error = new JSONObject();
            error.put("error", "Error al consultar Operaciones_Inalambrico: " + e.getMessage());
            jsonArray.put(error);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return jsonArray.toString();
    }

    public String findPinpadCsvByFechaProceso(Date fechaInicio, Date fechaFin) {
        EntityManager em = null;
        JSONArray jsonArray = new JSONArray();
        try {
            em = getEntityManager();
            String sql = "SELECT * FROM Operaciones_Tarjeta WHERE Fecha_Proceso BETWEEN ? AND ? ORDER BY Fecha_Proceso ASC";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fechaInicio);
            query.setParameter(2, fechaFin);

            List<Object[]> rows = query.getResultList();
            for (Object[] row : rows) {
                JSONObject map = new JSONObject();
                map.put("ID", row[0]);
                map.put("Codigo", row[1]);
                map.put("Producto", row[2]);
                map.put("Tipo_Mov", row[3]);
                map.put("Fecha_Proceso", row[4]);
                map.put("Fecha_Lote", row[5]);
                map.put("Lote_Manual", row[6]);
                map.put("Lote_Pos", row[7]);
                map.put("Terminal", row[8]);
                map.put("Voucher", row[9]);
                map.put("Autorizacion", row[10]);
                map.put("Cuotas", row[11]);
                map.put("Tarjeta", row[12]);
                map.put("Origen", row[13]);
                map.put("Transaccion", row[14]);
                map.put("Fecha_Consumo", row[15]);
                map.put("Importe", row[16]);
                map.put("Status_Pinpad", row[17]);
                map.put("Comision", row[18]);
                map.put("Comision_Afecta", row[19]);
                map.put("IGV", row[20]);
                map.put("Neto_Parcial", row[21]);
                map.put("Neto_Total", row[22]);
                map.put("Fecha_Abono", row[23]);
                map.put("Fecha_Abono_8Dig", row[24]);
                jsonArray.put(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonArray = new JSONArray();
            JSONObject error = new JSONObject();
            error.put("error", "Error al consultar Operaciones_Tarjeta: " + e.getMessage());
            jsonArray.put(error);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return jsonArray.toString();
    }

}
