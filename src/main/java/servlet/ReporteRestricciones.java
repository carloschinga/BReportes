package servlet;

import dao.JpaPadre;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
@WebServlet(name = "ReporteRestricciones", urlPatterns = { "/ReporteRestricciones" })
public class ReporteRestricciones extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServletOutputStream out = response.getOutputStream();
        Connection cn = null;
        EntityManager em = null;
        JpaPadre DAO = null;
        InputStream reportStream = null;

        try {
            // Validación de sesión
            HttpSession session = request.getSession(false);
            Object emprObj = (session != null) ? session.getAttribute("empr") : null;

            if (emprObj == null) {
                sendErrorResponse(response, "nosession", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String codalm = request.getParameter("codalm");
            if (codalm == null) codalm = "";

            String tipo = request.getParameter("tipo");
            if (tipo == null) tipo = "pdf";

            String empr = emprObj.toString();
            DAO = new JpaPadre(empr);
            em = DAO.getEntityManager();
            em.getTransaction().begin();
            cn = em.unwrap(Connection.class);

            // Archivo del reporte
            String fileName = codalm.isEmpty() ? "restriccionestodos.jasper" : "restricciones.jasper";
            reportStream = getServletConfig().getServletContext().getResourceAsStream(fileName);

            if (reportStream == null) {
                throw new IOException("No se pudo encontrar el archivo " + fileName);
            }

            // Parámetros
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("codalm", codalm);

            // Configuración Jasper
            JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
            jasperReportsContext.setProperty("net.sf.jasperreports.language", "es");
            jasperReportsContext.setProperty("net.sf.jasperreports.export.character.encoding", "UTF-8");
            jasperReportsContext.setProperty("net.sf.jasperreports.query.timeout", "600");
            jasperReportsContext.setProperty("net.sf.jasperreports.default.pdf.font.name", "Helvetica");
            jasperReportsContext.setProperty("net.sf.jasperreports.default.pdf.encoding", "UTF-8");

            JasperPrint jasperPrint = JasperFillManager.getInstance(jasperReportsContext)
                    .fill(reportStream, paramMap, cn);

            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }

            // Generar salida según el tipo
            if ("pdf".equalsIgnoreCase(tipo)) {
                configurePdfResponse(response, "ReporteRestricciones.pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            } else if ("xlsx".equalsIgnoreCase(tipo)) {
                configureExcelResponse(response, "ReporteRestricciones.xlsx");
                exportToExcel(jasperPrint, out);
            } else {
                sendErrorResponse(response, "Formato no soportado", HttpServletResponse.SC_BAD_REQUEST);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            sendErrorResponse(response, "Error al generar reporte: " + ex.getMessage(),
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

        } finally {
            // Cierre de recursos
            if (reportStream != null) {
                try { reportStream.close(); } catch (IOException ignored) {}
            }
            if (cn != null) {
                try { cn.close(); } catch (SQLException ignored) {}
            }
            if (em != null && em.isOpen()) {
                try {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                } catch (Exception ignored) {}
                em.close();
            }
            try { out.flush(); out.close(); } catch (IOException ignored) {}
        }
    }

    // ===== Métodos auxiliares =====

    private void sendErrorResponse(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"resultado\":\"error\",\"mensaje\":\"" + message + "\"}");
    }

    private void configurePdfResponse(HttpServletResponse response, String filename) {
        response.reset();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + filename);
        response.setCharacterEncoding("UTF-8");
    }

    private void configureExcelResponse(HttpServletResponse response, String filename) {
        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
    }

    private void exportToExcel(JasperPrint jasperPrint, ServletOutputStream out) throws JRException {
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));

        SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
        config.setOnePagePerSheet(false);
        config.setRemoveEmptySpaceBetweenRows(true);
        config.setDetectCellType(true);
        config.setWhitePageBackground(false);

        exporter.setConfiguration(config);
        exporter.exportReport();
    }

    @Override
    public String getServletInfo() {
        return "Genera reporte de restricciones en PDF o Excel";
    }
}