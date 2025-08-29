/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;

import dao.JpaPadre;
import dao.PetitorioJpaController;
import java.sql.SQLException;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

/**
 *
 * @author LOQ
 */

@WebServlet(name = "reporte_petitorio_especialidad", urlPatterns = { "/reporte_petitorio_especialidad" })
public class reporte_petitorio_especialidad extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
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

            String empr = emprObj.toString();
            DAO = new JpaPadre(empr);
            em = DAO.getEntityManager();
            em.getTransaction().begin();
            cn = em.unwrap(Connection.class);

            // Cargar reporte
            reportStream = getServletConfig().getServletContext()
                    .getResourceAsStream("petitoriotodoespecialidad.jasper");

            if (reportStream == null) {
                throw new IOException("No se pudo encontrar el archivo petitoriotodoespecialidad.jasper");
            }

            // Obtener datos como JSON
            PetitorioJpaController dao = new PetitorioJpaController(empr);
            JSONArray jsonArray = dao.listarporespecia();
            String json = jsonArray.toString();

            // Parámetros
            HashMap<String, Object> paramMap = new HashMap<>();
            JsonDataSource datasource = new JsonDataSource(new ByteArrayInputStream(json.getBytes("UTF-8")));
            paramMap.put("CollectionParam", datasource);

            // Config Jasper
            JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
            jasperReportsContext.setProperty("net.sf.jasperreports.language", "es");
            jasperReportsContext.setProperty("net.sf.jasperreports.export.character.encoding", "UTF-8");

            JasperFillManager fillManager = JasperFillManager.getInstance(jasperReportsContext);
            JasperPrint jasperPrint = fillManager.fill(reportStream, paramMap, datasource);

            String tipo = request.getParameter("tipo");
            if (tipo == null) tipo = "pdf";

            if ("pdf".equalsIgnoreCase(tipo)) {
                configurePdfResponse(response, "PetitorioConsolidadoEspecialidad.pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);

            } else if ("xlsx".equalsIgnoreCase(tipo)) {
                configureExcelResponse(response, "PetitorioConsolidadoEspecialidad.xlsx");

                // Quitar cabeceras en páginas > 1
                for (int i = 1; i < jasperPrint.getPages().size(); i++) {
                    net.sf.jasperreports.engine.JRPrintPage page = jasperPrint.getPages().get(i);
                    java.util.List<net.sf.jasperreports.engine.JRPrintElement> toRemove = new java.util.ArrayList<>();
                    for (Object element : page.getElements()) {
                        net.sf.jasperreports.engine.JRPrintElement printElement =
                                (net.sf.jasperreports.engine.JRPrintElement) element;
                        if (printElement.getY() < 36) {
                            toRemove.add(printElement);
                        }
                    }
                    page.getElements().removeAll(toRemove);
                }

                // Exportar a XLSX
                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));

                SimpleXlsxReportConfiguration config = new SimpleXlsxReportConfiguration();
                config.setOnePagePerSheet(false);
                config.setRemoveEmptySpaceBetweenRows(true);
                config.setDetectCellType(true);
                config.setWhitePageBackground(false);
                config.setIgnoreGraphics(false);
                config.setCollapseRowSpan(false);
                config.setIgnoreCellBorder(false);
                config.setFontSizeFixEnabled(true);

                exporter.setConfiguration(config);
                exporter.exportReport();
            } else {
                sendErrorResponse(response, "Formato no soportado", HttpServletResponse.SC_BAD_REQUEST);
            }

            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            sendErrorResponse(response, "Error al generar reporte: " + ex.getMessage(),
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        } finally {
            if (reportStream != null) try { reportStream.close(); } catch (IOException ignored) {}
            if (cn != null) try { cn.close(); } catch (SQLException ignored) {}
            if (em != null && em.isOpen()) {
                try {
                    if (em.getTransaction().isActive()) em.getTransaction().rollback();
                } catch (Exception ignored) {}
                em.close();
            }
            try { out.flush(); out.close(); } catch (IOException ignored) {}
        }
    }

    // ==== Métodos auxiliares ====

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

    // Métodos HTTP delegando a processRequest
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Genera reporte de petitorio consolidado por especialidad en PDF o Excel";
    }
}