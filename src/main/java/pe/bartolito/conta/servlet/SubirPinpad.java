/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package pe.bartolito.conta.servlet;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pe.bartolito.conta.dao.InalambricoPinpadDAO;

/**
 *
 * @author USUARIO
 */
@WebServlet(name = "SubirPinpad", urlPatterns = {"/subirPinpad"})
@MultipartConfig
public class SubirPinpad extends HttpServlet {

    private InalambricoPinpadDAO dao;

    @Override
    public void init() {
        dao = new InalambricoPinpadDAO("e");
    }
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> result = new HashMap<>();
        Gson gson = new Gson();
        PrintWriter out = response.getWriter();

        try {
            // Obtener parámetros
            int batch = Integer.parseInt(request.getParameter("batch"));
            int totalBatches = Integer.parseInt(request.getParameter("totalBatches"));
            List<Map<String, String>> data = gson.fromJson(request.getParameter("data"), List.class);

            // Si es el primer lote, truncamos la tabla
            //si existe un registro en BD se cancela la operacion  OJOOOOOOOO
            if (!data.isEmpty()) {
                String codigo = data.get(0).get("Codigo");
                String voucher= data.get(0).get("Voucher");
                 String fechaConsumo= data.get(0).get("Fecha_Consumo");
                System.out.println(codigo);

                if (!dao.verificarSiExiste(codigo, voucher, fechaConsumo)) {
                    /*if (batch == 0) {
                    dao.truncatePinpadCsv();
                    }*/

                    // Procesar cada registro
                    int processed = 0;
                    for (Map<String, String> row : data) {
                        try {
                            dao.createPinpadCsv(
                                    row.get("Codigo"),
                                    row.get("Producto"),
                                    row.get("Tipo_Mov"),
                                    parseDate(row.get("Fecha_Proceso")),
                                    parseDate(row.get("Fecha_Lote")),
                                    row.get("Lote_Manual"),
                                    row.get("Lote_Pos"),
                                    row.get("Terminal"),
                                    row.get("Voucher"),
                                    row.get("Autorizacion"),
                                    parseInt(row.get("Cuotas")),
                                    row.get("Tarjeta"),
                                    row.get("Origen"),
                                    row.get("Transaccion"),
                                    parseDate(row.get("Fecha_Consumo")),
                                    parseBigDecimal(row.get("Importe")),
                                    row.get("Status"),
                                    parseBigDecimal(row.get("Comision")),
                                    parseBigDecimal(row.get("Comision_Afecta")),
                                    parseBigDecimal(row.get("IGV")),
                                    parseBigDecimal(row.get("Neto_Parcial")),
                                    parseBigDecimal(row.get("Neto_Total")),
                                    parseDate(row.get("Fecha_Abono")),
                                    row.get("Fecha_Abono_8Dig")
                            );
                            processed++;
                        } catch (Exception e) {
                            System.err.println("Error procesando fila: " + e.getMessage());
                        }
                    }

                    result.put("success", true);
                    result.put("processed", processed);
                    result.put("batch", batch);
                    result.put("totalBatches", totalBatches);
                    result.put("message", "Lote " + (batch + 1) + " de " + totalBatches + " procesado");

                }
                else{
                    result.put("success", false);
                    result.put("message", "Registro ya existe");
                    
                }
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            out.print(gson.toJson(result));
            out.flush();
            out.close();
        }
    }

    private Date parseDate(String dateStr) throws ParseException {
        return dateStr != null && !dateStr.isEmpty() ? dateFormat.parse(dateStr) : null;
    }

    private Time parseTime(String timeStr) throws ParseException {
        return timeStr != null && !timeStr.isEmpty() ? new Time(timeFormat.parse(timeStr).getTime()) : null;
    }

    private int parseInt(String intStr) {
        try {
            return intStr != null && !intStr.isEmpty() ? Integer.parseInt(intStr) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private BigDecimal parseBigDecimal(String decimalStr) {
        try {
            return decimalStr != null && !decimalStr.isEmpty() ? new BigDecimal(decimalStr.replace(",", ".")) : BigDecimal.ZERO;
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
