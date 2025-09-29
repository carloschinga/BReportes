/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package pe.bartolito.reporte.farmacia.servlet;

import dao.JpaPadre;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import pe.bartolito.clinica.dao.HechRecetasCliJpaController;

/**
 *
 * @author LOQ
 */
@WebServlet(name = "RerportReceta", urlPatterns = {"/reportreceta"})
public class ReportReceta extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String actomedico = request.getParameter("actomedico");
        String tipo = request.getParameter("tipo");

        if (actomedico == null || actomedico.isEmpty() || tipo == null || tipo.isEmpty()) {
            sendErrorResponse(response, "Parámetros incompletos", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        /*HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("empr") == null) {
            sendErrorResponse(response, "Sesión no válida", HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }*/

        Connection cn = null;
        EntityManager em = null;
        JpaPadre DAO = null;
        HechRecetasCliJpaController dao = null;

        try (ServletOutputStream out = response.getOutputStream()) {
            DAO = new JpaPadre("d");
            em = DAO.getEntityManager();
            em.getTransaction().begin();
            cn = em.unwrap(Connection.class);

            try (InputStream reportStream = getServletContext().getResourceAsStream("/recetas.jasper")) {
                if (reportStream == null) {
                    throw new IOException("No se pudo encontrar el archivo recetas.jasper");
                }

                // Actualizar contador de impresión
                dao = new HechRecetasCliJpaController("d");
                String resultado = dao.actualizar_cant_impr(Integer.parseInt(actomedico));
                if (!"S".equals(resultado)) {
                    sendErrorResponse(response, "Error al actualizar contador de impresión",
                            HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    return;
                }

                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("actomedico", actomedico);
                paramMap.put("SUBREPORT_DIR", getServletContext().getRealPath("/"));

                JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
                jasperReportsContext.setProperty("net.sf.jasperreports.default.locale", "es_PE");
                jasperReportsContext.setProperty("net.sf.jasperreports.export.character.encoding", "UTF-8");
                jasperReportsContext.setProperty("net.sf.jasperreports.export.pdf.force.linebreak.policy", "true");

                JasperPrint jasperPrint = JasperFillManager.getInstance(jasperReportsContext)
                        .fill(reportStream, paramMap, cn);

                if ("pdf".equalsIgnoreCase(tipo)) {
                    configurePdfResponse(response, "Receta.pdf");
                    JasperExportManager.exportReportToPdfStream(jasperPrint, out);
                } else if ("xlsx".equalsIgnoreCase(tipo)) {
                    configureExcelResponse(response, "Receta.xlsx");
                    exportToExcel(jasperPrint, out);
                } else {
                    sendErrorResponse(response, "Formato no soportado", HttpServletResponse.SC_BAD_REQUEST);
                }

                // Commit seguro
                if (em.getTransaction().isActive()) {
                    em.getTransaction().commit();
                }
            }

        } catch (NumberFormatException e) {
            sendErrorResponse(response, "Número de acto médico inválido", HttpServletResponse.SC_BAD_REQUEST);
        } catch (JRException e) {
            sendErrorResponse(response, "Error al generar el reporte: " + e.getMessage(),
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            rollbackIfActive(em);
        } catch (Exception e) {
            sendErrorResponse(response, "Error interno del servidor", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            rollbackIfActive(em);
        } finally {
            closeQuietly(cn);
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    // --- Métodos auxiliares ---

    private void rollbackIfActive(EntityManager em) {
        try {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void closeQuietly(Connection cn) {
        try {
            if (cn != null && !cn.isClosed()) {
                cn.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String message, int statusCode) throws IOException {
        if (!response.isCommitted()) {
            response.reset();
            response.setStatus(statusCode);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"resultado\":\"error\",\"mensaje\":\"" + message + "\"}");
        }
    }

    private void configurePdfResponse(HttpServletResponse response, String filename) {
        response.setContentType("application/pdf");
        response.setHeader("Content-disposition", "inline; filename=" + filename);
        response.setCharacterEncoding("UTF-8");
    }

    private void configureExcelResponse(HttpServletResponse response, String filename) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
    }

    private void exportToExcel(JasperPrint jasperPrint, OutputStream outputStream) throws JRException {
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setOnePagePerSheet(false);
        configuration.setRemoveEmptySpaceBetweenRows(true);
        configuration.setDetectCellType(true);
        configuration.setWhitePageBackground(false);
        configuration.setCollapseRowSpan(true);

        exporter.setConfiguration(configuration);
        exporter.exportReport();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Genera reportes de Recetas en PDF o Excel";
    }
}
