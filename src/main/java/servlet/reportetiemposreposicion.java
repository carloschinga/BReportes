package servlet;

import dao.JpaPadre;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;

@WebServlet(name = "reportetiemposreposicion", urlPatterns = { "/reportetiemposreposicion" })
public class reportetiemposreposicion extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServletOutputStream out = response.getOutputStream();
        EntityManager em = null;
        Connection cn = null;
        JpaPadre DAO = null;

        try {
            HttpSession session = request.getSession(true);
            Object emprObj = session.getAttribute("empr");

            if (emprObj == null) {
                response.setContentType("application/json");
                out.print("{\"resultado\":\"error\",\"mensaje\":\"nosession\"}");
                return;
            }

            String orden = request.getParameter("orden");
            if (orden == null || orden.trim().isEmpty()) {
                response.setContentType("application/json");
                out.print("{\"resultado\":\"error\",\"mensaje\":\"noorden\"}");
                return;
            }

            String empr = emprObj.toString();
            DAO = new JpaPadre(empr);
            em = DAO.getEntityManager();
            em.getTransaction().begin();
            cn = em.unwrap(Connection.class);

            // Timeout de red
            cn.setNetworkTimeout(Executors.newSingleThreadExecutor(), 120000);

            // Cargar archivo del reporte
            String reportFile = "/guia_transferencia_orden_caja_tiempos.jasper";
            InputStream reportStream = getServletContext().getResourceAsStream(reportFile);
            if (reportStream == null) {
                throw new IOException("No se pudo encontrar el archivo: " + reportFile);
            }

            // Parámetros adicionales
            String de = session.getAttribute("de").toString();
            String central = "b".equalsIgnoreCase(de) ? "S" : session.getAttribute("central").toString();
            String siscod = "S".equals(central)
                    ? request.getParameter("siscod")
                    : session.getAttribute("siscod").toString();

            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("orden", orden);
            paramMap.put("SUBREPORT_DIR", getServletContext().getRealPath("/") + "/");

            if (siscod != null && !siscod.isEmpty()) {
                paramMap.put("siscod", Integer.parseInt(siscod));
            }

            // Configuración JasperReports
            JasperReportsContext jrc = DefaultJasperReportsContext.getInstance();
            jrc.setProperty("net.sf.jasperreports.language", "es");
            jrc.setProperty("net.sf.jasperreports.export.character.encoding", "UTF-8");
            jrc.setProperty("net.sf.jasperreports.default.pdf.font.name", "Helvetica");
            jrc.setProperty("net.sf.jasperreports.default.pdf.encoding", "UTF-8");

            // Llenar reporte
            JasperPrint jasperPrint = JasperFillManager.getInstance(jrc)
                    .fill(reportStream, paramMap, cn);

            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }

            String tipo = request.getParameter("tipo");
            if ("pdf".equalsIgnoreCase(tipo)) {
                configurePdfResponse(response, "ReporteTiemposReposicion.pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            } else {
                configureExcelResponse(response, "ReporteTiemposReposicion.xlsx");

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

        } catch (Exception ex) {
            ex.printStackTrace();
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            response.setContentType("application/json");
            out.print("{\"resultado\":\"error\",\"mensaje\":\"" + ex.getMessage().replace("\"", "'") + "\"}");
        } finally {
            try {
                if (cn != null && !cn.isClosed())
                    cn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (em != null && em.isOpen())
                    em.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // --- Métodos auxiliares ---
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

    @Override
    public String getServletInfo() {
        return "Genera el reporte de tiempos de reposición";
    }
}