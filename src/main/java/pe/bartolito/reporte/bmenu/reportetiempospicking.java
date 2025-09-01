package pe.bartolito.reporte.bmenu;

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

@WebServlet(name = "reportetiempospicking", urlPatterns = {"/reportetiempospicking"})
public class reportetiempospicking extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            // Placeholder method - actual processing is in doGet/doPost
        }
    }

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
                            String username = usua.getString("useusr");
                            String empr = "a";
                            String de = "b";
                            int siscod = usua.getInt("siscod");
                            String sisent = usua.getString("sisent");
                            String codalminv = usua.getString("codalm_inv");
                            String grudes = usua.getString("grudes");
                            String grucod = usua.getString("grucod");
                            int codinventario = 0;

                            //Aqui el desarrollo del reporte
                            // Parámetros del reporte
                            String orden = request.getParameter("orden");
                            String fechainicio = request.getParameter("fechainicio");
                            String fechafin = request.getParameter("fechafin");
                            String operario = request.getParameter("operario");
                            String tipo = request.getParameter("tipo");

                            if ((orden == null || orden.trim().isEmpty())
                                    && (fechainicio == null || fechainicio.trim().isEmpty())
                                    && (fechafin == null || fechafin.trim().isEmpty())
                                    && (operario == null || operario.trim().isEmpty())) {
                                response.setContentType("application/json");
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"Se requiere al menos un parámetro de filtro\"}");
                                return;
                            }
                             DAO = new JpaPadre(empr);
                            em = DAO.getEntityManager();
                            em.getTransaction().begin();

                            cn = em.unwrap(Connection.class);

                            // Reporte Jasper
                            String reportFileName = "/reporte_tiempopicking.jasper";
                            InputStream reportStream = getServletConfig().getServletContext().getResourceAsStream(reportFileName);

                            if (reportStream == null) {
                                throw new IOException("No se pudo encontrar el archivo " + reportFileName);
                            }
                            HashMap<String, Object> paramMap = new HashMap<>();
                            paramMap.put("orden", (orden != null && !orden.isEmpty()) ? Integer.parseInt(orden.trim()) : null);
                            paramMap.put("fechaInicio", (fechainicio != null && !fechainicio.trim().isEmpty()) ? fechainicio.trim() : null);
                            paramMap.put("fechaFin", (fechafin != null && !fechafin.trim().isEmpty()) ? fechafin.trim() : null);
                            paramMap.put("operario", (operario != null && !operario.trim().isEmpty()) ? operario.trim() : null);

                            String nombre = username;
                            String[] nombre2 = nombre.split("-", 2);
                            paramMap.put("usuarioReporte", nombre2[0]);

                            paramMap.put("siscod", siscod);

                            // Contexto Jasper
                            JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
                            jasperReportsContext.setProperty("net.sf.jasperreports.language", "es");
                            jasperReportsContext.setProperty("net.sf.jasperreports.export.character.encoding", "UTF-8");

                            JasperFillManager fillManager = JasperFillManager.getInstance(jasperReportsContext);
                            JasperPrint jasperPrint = fillManager.fill(reportStream, paramMap, cn);

                            if (em.getTransaction().isActive()) {
                                em.getTransaction().commit();
                            }
                            out = response.getOutputStream();
                            if ("pdf".equalsIgnoreCase(tipo)) {
                                response.resetBuffer();
                                response.setContentType("application/pdf");
                                response.setHeader("Content-Disposition", "inline; filename=ReporteTiempoPicking.pdf");
                                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
                            } else {
                                response.resetBuffer();
                                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                                response.setHeader("Content-Disposition", "attachment; filename=ReporteTiempoPicking.xlsx");

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

    @Override
    public String getServletInfo() {
        return "Servlet para generar reportes de tiempos de picking en PDF y Excel";
    }
}

/*package pe.bartolito.reporte.bmenu;

import dao.JpaPadre;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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

@WebServlet(name = "reportetiempospicking", urlPatterns = {"/reportetiempospicking"})
public class reportetiempospicking extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EntityManager em = null;
        Connection cn = null;
        JpaPadre DAO = null;

        try {
            // 1️⃣ Validar parámetros principales
            String token = request.getParameter("token");
            String tipo = request.getParameter("tipo"); // "pdf" o "xlsx"
            if (token == null || token.trim().isEmpty()) {
                sendJsonError(response, "Token requerido");
                return;
            }
            if (tipo == null || tipo.trim().isEmpty()) {
                sendJsonError(response, "Tipo de archivo requerido (pdf o xlsx)");
                return;
            }

            // 2️⃣ Llamar al endpoint de autenticación
            JSONObject usuarioJson = autenticarUsuario(token);
            if (usuarioJson == null) {
                sendJsonError(response, "Error autenticando usuario");
                return;
            }

            // 3️⃣ Extraer datos necesarios del JSON
            String username = usuarioJson.getString("useusr");
            int siscod = usuarioJson.getInt("siscod");

            // 4️⃣ Validar filtros de reporte
            String orden = request.getParameter("orden");
            String fechainicio = request.getParameter("fechainicio");
            String fechafin = request.getParameter("fechafin");
            String operario = request.getParameter("operario");

            if ((orden == null || orden.trim().isEmpty()) &&
                (fechainicio == null || fechainicio.trim().isEmpty()) &&
                (fechafin == null || fechafin.trim().isEmpty()) &&
                (operario == null || operario.trim().isEmpty())) {
                sendJsonError(response, "Se requiere al menos un parámetro de filtro");
                return;
            }

            // 5️⃣ Configurar JPA y conexión
            DAO = new JpaPadre("a"); // reemplaza "a" por tu empresa o datasource
            em = DAO.getEntityManager();
            em.getTransaction().begin();
            cn = em.unwrap(Connection.class);

            // 6️⃣ Cargar reporte Jasper
            String reportFileName = "/reporte_tiempopicking.jasper";
            InputStream reportStream = getServletConfig().getServletContext().getResourceAsStream(reportFileName);
            if (reportStream == null) {
                throw new IOException("No se pudo encontrar el archivo " + reportFileName);
            }

            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("orden", (orden != null && !orden.trim().isEmpty()) ? Integer.parseInt(orden.trim()) : null);
            paramMap.put("fechaInicio", (fechainicio != null && !fechainicio.trim().isEmpty()) ? fechainicio.trim() : null);
            paramMap.put("fechaFin", (fechafin != null && !fechafin.trim().isEmpty()) ? fechafin.trim() : null);
            paramMap.put("operario", (operario != null && !operario.trim().isEmpty()) ? operario.trim() : null);
            paramMap.put("usuarioReporte", username.split("-", 2)[0]);
            paramMap.put("siscod", siscod);

            // 7️⃣ Generar JasperPrint
            JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
            jasperReportsContext.setProperty("net.sf.jasperreports.language", "es");
            jasperReportsContext.setProperty("net.sf.jasperreports.export.character.encoding", "UTF-8");

            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, paramMap, cn);

            em.getTransaction().commit();

            // 8️⃣ Exportar según tipo
            try (ServletOutputStream out = response.getOutputStream()) {
                if ("pdf".equalsIgnoreCase(tipo)) {
                    response.resetBuffer();
                    response.setContentType("application/pdf");
                    response.setHeader("Content-Disposition",
                        "inline; filename=\"" + URLEncoder.encode("ReporteTiempoPicking.pdf", "UTF-8").replaceAll("\\+", "%20") + "\"");
                    JasperExportManager.exportReportToPdfStream(jasperPrint, out);
                } else if ("xlsx".equalsIgnoreCase(tipo)) {
                    response.resetBuffer();
                    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    response.setHeader("Content-Disposition",
                        "attachment; filename=\"" + URLEncoder.encode("ReporteTiempoPicking.xlsx", "UTF-8").replaceAll("\\+", "%20") + "\"");

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
                    sendJsonError(response, "Tipo de archivo inválido");
                }
                out.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (!response.isCommitted()) {
                sendJsonError(response, e.getMessage());
            }
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            if (cn != null) try { cn.close(); } catch (Exception ignore) {}
            if (em != null && em.isOpen()) em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private JSONObject autenticarUsuario(String token) {
        try {
            URL url = new URL("http://181.224.248.20:8080/bauth/auth/getUser");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String requestBody = "{\"token\":\"" + token + "\"}";
            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestBody.getBytes("utf-8"));
            }

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) sb.append(line.trim());
                    return new JSONObject(sb.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendJsonError(HttpServletResponse response, String mensaje) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        try (java.io.PrintWriter pw = response.getWriter()) {
            pw.print("{\"resultado\":\"error\",\"mensaje\":\"" + mensaje.replace("\"", "'") + "\"}");
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet para generar reportes de tiempos de picking en PDF y Excel";
    }
}
*/


