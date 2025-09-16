/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package pe.bartolito.reporte.farmacia.servlet;

import dao.JpaPadre;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.json.JSONObject;

/**
 *
 * @author USUARIO
 */
@WebServlet(name = "ReporteCierreCaja", urlPatterns = {"/ReporteCierreCaja"})
public class ReporteCierreCaja extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        //try (PrintWriter out = response.getWriter()) {
        String token = request.getParameter("token");
        String respuestaEndPoint = "";
        //String usu = "";

        ServletOutputStream out = null;
        try {

            URL url = new URL("http://181.224.248.20:8080/bauth/auth/getUser"); // Reemplaza con tu URL real
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // 2. Enviar credenciales al endpoint
            String requestBody = "{\"token\":\"" + token + "\"}";
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // 3. Leer respuesta del endpoint
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder responseSb = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        responseSb.append(responseLine.trim());
                    }
                    respuestaEndPoint = responseSb.toString();

                    // 4. Parsear JSON y validar
                    JSONObject data = new JSONObject(respuestaEndPoint); // Usamos org.json.JSONObject
                    //if (usua.getString("resultado").equals("OK")) {
                    if (data != null) {

                        // 5. Configurar sesión con los datos del endpoint
                        EntityManager em = null;
                        Connection cn = null;
                        JpaPadre DAO = null;

                        // ===== Validación contra endpoint de autenticación =====
                        String usecod = data.get("usecod").toString();
                        String siscod = data.get("siscod").toString();

                        String username = null;

                        // ===== Validación de parámetros =====
                        //String siscodParam = request.getParameter("siscod");
                        String fecha1 = request.getParameter("fecha1");
                        String fecha2 = request.getParameter("fecha2");
                        String invnumAper = request.getParameter("invnum_aper");
                        //String usecodParam = request.getParameter("usecod");
                        String formato = request.getParameter("tipo");

                        if (siscod == null || fecha1 == null || fecha2 == null || invnumAper == null || usecod == null || formato == null) {
                            sendJsonError(response, "Faltan parámetros requeridos");
                            return;
                        }

                        // ===== Conexión a BD =====
                        DAO = new JpaPadre("a"); // ⚠️ cambia "a" por tu persistence-unit
                        em = DAO.getEntityManager();
                        em.getTransaction().begin();
                        cn = em.unwrap(Connection.class);

                        // ===== Plantilla Jasper =====
                        String reportFileName = "/conta/reporte_cajaresumen.jasper"; // ⚠️ ajusta la ruta
                        InputStream reportStream = getServletContext().getResourceAsStream(reportFileName);
                        if (reportStream == null) {
                            throw new IOException("No se encontró el archivo: " + reportFileName);
                        }

                        // ===== Parámetros Jasper =====
                        HashMap<String, Object> paramMap = new HashMap<>();
                        paramMap.put("P_siscod", Integer.parseInt(siscod));
                        paramMap.put("P_fecha1", fecha1);
                        paramMap.put("P_fecha2", fecha2);
                        paramMap.put("P_invnum_aper", Integer.parseInt(invnumAper));
                        paramMap.put("P_usecod", Integer.parseInt(usecod));
                        paramMap.put("cajero", username);

                        String basePath = getServletContext().getRealPath("/");
                        paramMap.put("rutalogo", basePath + "/img/logo5.jpg");
                        paramMap.put("rutalogo2", basePath + "/img/logo3.jpg");

                        JasperReportsContext ctx = DefaultJasperReportsContext.getInstance();
                        ctx.setProperty("net.sf.jasperreports.export.character.encoding", "UTF-8");
                        ctx.setProperty("net.sf.jasperreports.query.timeout", "600");

                        JasperPrint jasperPrint = JasperFillManager.getInstance(ctx).fill(reportStream, paramMap, cn);

                        if (em.getTransaction().isActive()) {
                            em.getTransaction().commit();
                        }

                        // ===== Exportación =====
                        out = response.getOutputStream();
                        if ("pdf".equalsIgnoreCase(formato)) {
                            response.setContentType("application/pdf");
                            response.setHeader("Content-Disposition", "inline; filename=ReporteCierreCaja.pdf");
                            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
                        } else if ("xlsx".equalsIgnoreCase(formato)) {
                            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                            response.setHeader("Content-Disposition", "attachment; filename=ReporteCaja.xlsx");

                            JRXlsxExporter exporter = new JRXlsxExporter();
                            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));

                            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                            configuration.setOnePagePerSheet(false);
                            configuration.setRemoveEmptySpaceBetweenRows(true);
                            configuration.setDetectCellType(true);
                            configuration.setWhitePageBackground(false);
                            exporter.setConfiguration(configuration);

                            exporter.exportReport();
                        } else {
                            sendJsonError(response, "Formato no soportado: " + formato);
                            return;
                        }

                    }
                }
            } else {
                System.err.println("Error HTTP: " + responseCode);
                out.print("{\"resultado\":\"error\",\"mensaje\":\"Codigo de endpoint diferente a 200\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar error de conexión (ej: endpoint caído)
            out.print("{\"resultado\":\"error\",\"mensaje\":\"Endpoint caido\"}");
        } finally {
            //out.print(respuestaEndPoint);
        }

        //}
        ////////////////////
    }

    private void sendJsonError(HttpServletResponse response, String mensaje) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter pw = response.getWriter()) {
            pw.print("{\"resultado\":\"error\",\"mensaje\":\"" + mensaje.replace("\"", "'") + "\"}");
        }
    }

}
