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
 * @author USUARIO
 */

@WebServlet(name = "reporte_petitorio_todo", urlPatterns = { "/reporte_petitorio_todo" })
public class reporte_petitorio_todo extends HttpServlet {

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

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setCharacterEncoding("UTF-8");

        try (ServletOutputStream out = response.getOutputStream()) {

            Connection cn = null;
            EntityManager em = null;
            JpaPadre DAO = null;

            try {
                HttpSession session = request.getSession(false);
                if (session == null || session.getAttribute("empr") == null) {
                    sendErrorJson(response, out, HttpServletResponse.SC_UNAUTHORIZED, "nosession");
                    return;
                }

                String empr = session.getAttribute("empr").toString();
                DAO = new JpaPadre(empr);
                em = DAO.getEntityManager();
                em.getTransaction().begin();
                cn = em.unwrap(Connection.class);

                // Cargar archivo jasper
                InputStream reportStream = getServletContext().getResourceAsStream("/petitoriotodo.jasper");
                if (reportStream == null) {
                    throw new IOException("No se encontró el archivo petitoriotodo.jasper");
                }

                // Obtener data
                PetitorioJpaController dao = new PetitorioJpaController(empr);
                JSONArray jsonArray = dao.listarTodoEspc();
                String json = jsonArray.toString();

                HashMap<String, Object> paramMap = new HashMap<>();
                JsonDataSource datasource = new JsonDataSource(new ByteArrayInputStream(json.getBytes("UTF-8")));
                paramMap.put("CollectionParam", datasource);

                // Configuración JasperReports
                JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
                jasperReportsContext.setProperty("net.sf.jasperreports.language", "es");
                jasperReportsContext.setProperty("net.sf.jasperreports.export.character.encoding", "UTF-8");

                JasperFillManager fillManager = JasperFillManager.getInstance(jasperReportsContext);
                JasperPrint jasperPrint = fillManager.fill(reportStream, paramMap, datasource);

                // Exportar según el tipo solicitado
                String tipo = request.getParameter("tipo");
                if ("pdf".equalsIgnoreCase(tipo)) {
                    configurePdfResponse(response, "PetitorioConsolidado.pdf");
                    JasperExportManager.exportReportToPdfStream(jasperPrint, out);
                } else {
                    configureExcelResponse(response, "PetitorioConsolidado.xlsx");

                    JRXlsxExporter exporter = new JRXlsxExporter();
                    SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                    configuration.setOnePagePerSheet(false);
                    configuration.setRemoveEmptySpaceBetweenRows(true);
                    configuration.setDetectCellType(true);
                    configuration.setWhitePageBackground(false);

                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
                    exporter.setConfiguration(configuration);

                    exporter.exportReport();
                }

                if (em != null && em.getTransaction().isActive()) {
                    em.getTransaction().commit();
                }

            } catch (Exception ex) {
                if (em != null && em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                ex.printStackTrace();
                sendErrorJson(response, out, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
            } finally {
                if (cn != null) try { cn.close(); } catch (Exception ignored) {}
                if (em != null) try { em.close(); } catch (Exception ignored) {}
            }
        }
    }

    // --- Métodos auxiliares ---
    private void sendErrorJson(HttpServletResponse response, ServletOutputStream out, int status, String message)
            throws IOException {
        response.reset();
        response.setStatus(status);
        response.setContentType("application/json");
        out.print("{\"resultado\":\"error\",\"mensaje\":\"" + message.replace("\"", "'") + "\"}");
        out.flush();
    }

    private void configurePdfResponse(HttpServletResponse response, String filename) {
        response.reset();
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + filename);
    }

    private void configureExcelResponse(HttpServletResponse response, String filename) {
        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
    }

    @Override
    public String getServletInfo() {
        return "Reporte Petitorio Todo";
    }
}