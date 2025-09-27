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
import java.util.concurrent.Executors;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
 * @author LOQ
 */
@WebServlet(name = "ReporteOrdenTrabajoSerdoc", urlPatterns = {"/ReporteOrdenTrabajoSerdoc"})
public class ReporteOrdenTrabajoSerdoc extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EntityManager em = null;
        Connection cn = null;
        JpaPadre DAO = null;
        ServletOutputStream out = null;

        try {
            String token = request.getParameter("token");
            String usu = null;
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
                        usu = responseSb.toString();
                        String jsonResponse = responseSb.toString();
                        System.out.println("Respuesta del endpoint: " + jsonResponse);

                        // 4. Parsear JSON y validar
                        JSONObject usua = new JSONObject(jsonResponse); // Usamos org.json.JSONObject
                        if (usua != null) {
                            // 5. Cargamos datos localmente
                            int codbar = usua.getInt("usecod");
                            String logi = usua.getString("useusr");
                            String username = usua.getString("usenam");
                            String empr = "a";
                            String de = "b";
                            int siscod = usua.getInt("siscod");
                            String sisent = usua.getString("sisent");
                            String codalminv = usua.getString("codalm_inv");
                            String grudes = usua.getString("grudes");
                            String grucod = usua.getString("grucod");
                            int codinventario = 0;

                            //Aqui el desarrollo del reporte
                            String orden = request.getParameter("orden");
                            if (orden == null || orden.isEmpty()) {
                                sendJsonError(response, "noorden");
                                return;
                            }

                            DAO = new JpaPadre(empr);
                            em = DAO.getEntityManager();
                            em.getTransaction().begin();

                            cn = em.unwrap(Connection.class);

                            // ⚠️ no crear hilos por request, usar timeout con Statement mejor
                            // cn.setNetworkTimeout(...)
                            // Validar plantilla
                            String reportFileName = "/guia_transferencia_serdoc.jasper";
                            InputStream reportStream = getServletContext().getResourceAsStream(reportFileName);
                            if (reportStream == null) {
                                throw new IOException("No se encontró el archivo " + reportFileName);
                            }

                            // Parámetros del reporte
                            HashMap<String, Object> paramMap = new HashMap<>();
                            String nombre = String.valueOf(username);
                            if (nombre.contains("-")) {
                                nombre = nombre.split("-", 2)[0];
                            }
                            paramMap.put("orden", orden);
                            paramMap.put("nombre", nombre);

                            String central = "N";
                            if (siscod == 1) {
                                central = "S";
                            }

                            String basePath = getServletContext().getRealPath("/");
                            paramMap.put("rutaimagen", basePath + "/firmas/" + siscod + ".jpeg");
                            paramMap.put("rutalogo", basePath + "/img/logo5.jpg");
                            paramMap.put("rutalogo2", basePath + "/img/logo3.jpg");

                            if ("S".equals(central)) {
                                String siscodReq = request.getParameter("siscod");
                                paramMap.put("siscod", (siscodReq == null || siscodReq.isEmpty()) ? null : Integer.parseInt(siscodReq));
                            } else {
                                paramMap.put("siscod", siscod);
                            }

                            JasperReportsContext ctx = DefaultJasperReportsContext.getInstance();
                            ctx.setProperty("net.sf.jasperreports.export.character.encoding", "UTF-8");
                            ctx.setProperty("net.sf.jasperreports.query.timeout", "600"); // 10 min

                            JasperPrint jasperPrint = JasperFillManager.getInstance(ctx).fill(reportStream, paramMap, cn);

                            if (em.getTransaction().isActive()) {
                                em.getTransaction().commit();
                            }

                            // Generar salida
                            String tipo = request.getParameter("tipo");
                            out = response.getOutputStream();

                            if ("pdf".equalsIgnoreCase(tipo)) {
                                response.setContentType("application/pdf");
                                response.setHeader("Content-Disposition", "inline; filename=ReporteOrdenRegistroProductos.pdf");
                                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
                            } else {
                                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                                response.setHeader("Content-Disposition", "attachment; filename=ReporteOrdenRegistroProductos.xlsx");

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
                            }

                        } else {
                            // Manejar error de autenticación (ej: credenciales incorrectas)
                            System.err.println("Error en autenticación: " + usua.optString("mensaje", "Sin detalles"));
                        }
                    }
                } else {
                    System.err.println("Error HTTP: " + responseCode);
                    usu = "{\"resultado\":\"error\",\"mensaje\":\"Codigo de endpoint diferente a 200\"}";
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Manejar error de conexión (ej: endpoint caído)
                usu = "{\"resultado\":\"error\",\"mensaje\":\"Endpoint caido\"}";
            } finally {
                out.print(usu);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            if (!response.isCommitted()) { // si aún no se envió nada
                sendJsonError(response, ex.getMessage());
            }
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            try {
                if (cn != null && !cn.isClosed()) {
                    cn.close();
                }
            } catch (Exception ignore) {
            }
            try {
                if (em != null && em.isOpen()) {
                    em.close();
                }
            } catch (Exception ignore) {
            }
            try {
                if (out != null) {
                    out.flush();
                }
            } catch (Exception ignore) {
            }
        }
    }

    private void sendJsonError(HttpServletResponse response, String mensaje) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter pw = response.getWriter()) {
            pw.print("{\"resultado\":\"error\",\"mensaje\":\"" + mensaje.replace("\"", "'") + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
