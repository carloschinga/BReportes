package pe.bartolito.reporte.bmenu;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import dao.JpaPadre;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.json.JSONObject;

@WebServlet(name = "ReporteOrdenTransporteCaja", urlPatterns = {"/ReporteOrdenTransporteCaja"})
public class ReporteOrdenTransporteCaja extends HttpServlet {

    static {
        System.setProperty("net.sf.jasperreports.compiler.keep.java.file", "false");
        System.setProperty("net.sf.jasperreports.compiler.temporary.hold", "false");
        System.setProperty("net.sf.jasperreports.compiler.classloader.cache", "false");
    }

    private static final int QUERY_TIMEOUT_SECONDS = 600; // 10 min

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EntityManager em = null;
        Connection cn = null;
        ServletOutputStream out = null;

        try {
            // 1. Validar token en auth server
            String token = request.getParameter("token");
            JSONObject usuario = autenticarUsuario(token);
            if (usuario == null) {
                sendJsonError(response, "Error de autenticación");
                return;
            }

            // 2. Validar parámetro orden
            String orden = request.getParameter("orden");
            if (orden == null || orden.trim().isEmpty()) {
                sendJsonError(response, "noorden");
                return;
            }

            // 3. Preparar JPA / conexión
            em = new JpaPadre("a").getEntityManager(); // Empresa fija "a" en tu código
            em.getTransaction().begin();
            cn = em.unwrap(Connection.class);

            // 4. Seleccionar plantilla
            String central = usuario.optString("central");
            String siscod = request.getParameter("siscod");
            String reportName = determineReportName(central, siscod);
            InputStream reportStream = getServletContext().getResourceAsStream(reportName);
            if (reportStream == null) {
                throw new IOException("No se encontró el archivo: " + reportName);
            }

            // 5. Parámetros Jasper
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("siscod", usuario.optInt("siscod"));
            paramMap.put("orden", orden);
            paramMap.put("SUBREPORT_DIR", getServletContext().getRealPath("/"));

            JasperReportsContext jasperContext = DefaultJasperReportsContext.getInstance();
            jasperContext.setProperty("net.sf.jasperreports.language", "es");
            jasperContext.setProperty("net.sf.jasperreports.query.timeout", String.valueOf(QUERY_TIMEOUT_SECONDS));

            JasperPrint jasperPrint = JasperFillManager.getInstance(jasperContext)
                    .fill(reportStream, paramMap, cn);

            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }

            // 6. Exportar
            out = response.getOutputStream();
            exportReport(jasperPrint, request, response, out);

        } catch (Exception ex) {
            handleException(ex, response);
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            closeQuietly(cn);
            closeQuietly(em);
            // ⚡ No cierres el OutputStream, Tomcat lo gestiona
        }
    }

    // ======================== AUXILIARES ======================== //

    private JSONObject autenticarUsuario(String token) {
        try {
            URL url = new URL("http://181.224.248.20:8080/bauth/auth/getUser");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String requestBody = "{\"token\":\"" + token + "\"}";
            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestBody.getBytes("utf-8"));
            }

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder responseSb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    responseSb.append(line.trim());
                }
                return new JSONObject(responseSb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String determineReportName(String central, String siscod) {
        if ("S".equals(central)) {
            return (siscod == null || siscod.trim().isEmpty())
                    ? "guia_transferencia_orden_caja.jasper"
                    : "guia_transferencia_orden_caja_almacen.jasper";
        }
        return "guia_transferencia_orden_caja_almacen.jasper";
    }

    private void exportReport(JasperPrint jasperPrint, HttpServletRequest request,
                              HttpServletResponse response, ServletOutputStream out) throws Exception {
        String tipo = request.getParameter("tipo");
        if ("pdf".equalsIgnoreCase(tipo)) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=reporteOrdenTransporteCaja.pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);
        } else {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=ReporteOrdenTrabajoCaja.xlsx");

            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));

            SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
            config.setDetectCellType(true);
            config.setRemoveEmptySpaceBetweenRows(true);
            config.setWhitePageBackground(false);

            exporter.setConfiguration(config);
            exporter.exportReport();
        }
    }

    private void handleException(Exception ex, HttpServletResponse response) throws IOException {
        ex.printStackTrace();
        if (!response.isCommitted()) {
            sendJsonError(response, ex.getMessage() != null ? ex.getMessage() : "Error interno");
        }
    }

    private void sendJsonError(HttpServletResponse response, String mensaje) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter pw = response.getWriter()) {
            pw.print("{\"resultado\":\"error\",\"mensaje\":\"" + mensaje.replace("\"", "'") + "\"}");
        }
    }

    private void closeQuietly(Connection cn) {
        if (cn != null) {
            try {
                cn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    private void closeQuietly(EntityManager em) {
        if (em != null && em.isOpen()) {
            try {
                em.close();
            } catch (Exception ignored) {
            }
        }
    }
}
