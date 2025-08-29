/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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

import dao.JpaPadre;
import java.util.List;
import java.util.ArrayList;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

/**
 *
 * @author USUARIO
 */

@WebServlet(name = "reportepetitorioproducto", urlPatterns = { "/reportepetitorioproducto" })
public class reportepetitorioproducto extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServletOutputStream out = response.getOutputStream();
        Connection cn = null;
        EntityManager em = null;
        JpaPadre DAO = null;

        try {
            HttpSession session = request.getSession(true);
            Object emprObj = session.getAttribute("empr");

            if (emprObj == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                out.print("{\"resultado\":\"error\",\"mensaje\":\"nosession\"}");
                return;
            }

            String empr = emprObj.toString();
            DAO = new JpaPadre(empr);
            em = DAO.getEntityManager();
            em.getTransaction().begin();
            cn = em.unwrap(Connection.class);

            String medcod = request.getParameter("medcod");
            String nombre = request.getParameter("medcodText");

            // Cargar el archivo de reporte
            InputStream reportStream = getServletConfig().getServletContext()
                    .getResourceAsStream("petitorioproductos.jasper");

            if (reportStream == null) {
                throw new IOException("No se pudo encontrar el archivo petitorioproductos.jasper");
            }

            // Parámetros para el reporte
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("medcod", medcod);
            paramMap.put("nombre", nombre);

            // Configuración Jasper
            JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
            jasperReportsContext.setProperty("net.sf.jasperreports.language", "es");
            jasperReportsContext.setProperty("net.sf.jasperreports.export.character.encoding", "UTF-8");

            JasperFillManager fillManager = JasperFillManager.getInstance(jasperReportsContext);
            JasperPrint jasperPrint = fillManager.fill(reportStream, paramMap, cn);

            // Exportar según el tipo
            String tipo = request.getParameter("tipo");
            if (tipo != null && tipo.equalsIgnoreCase("pdf")) {
                response.resetBuffer();
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline; filename=petitorioproductos.pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);

            } else {
                response.resetBuffer();
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader("Content-Disposition", "attachment; filename=petitorioproductos.xlsx");

                // Limpieza de cabeceras en páginas > 1
                for (int i = 1; i < jasperPrint.getPages().size(); i++) {
                    net.sf.jasperreports.engine.JRPrintPage page = jasperPrint.getPages().get(i);
                    List<net.sf.jasperreports.engine.JRPrintElement> elementsToRemove = new ArrayList<>();
                    for (Object element : page.getElements()) {
                        net.sf.jasperreports.engine.JRPrintElement printElement =
                                (net.sf.jasperreports.engine.JRPrintElement) element;
                        if (printElement.getY() < 52) {
                            elementsToRemove.add(printElement);
                        }
                    }
                    page.getElements().removeAll(elementsToRemove);
                }

                // Exportador a Excel
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
            }

            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }

        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            out.print("{\"resultado\":\"error\",\"mensaje\":\"" + ex.getMessage().replace("\"", "'") + "\"}");
        } finally {
            try {
                if (cn != null && !cn.isClosed()) cn.close();
            } catch (Exception e) { e.printStackTrace(); }

            try {
                if (em != null && em.isOpen()) em.close();
            } catch (Exception e) { e.printStackTrace(); }

            try {
                out.flush();
                out.close();
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            // No-op
        }
    }

    @Override
    public String getServletInfo() {
        return "Genera reporte de Petitorio por Producto en PDF o Excel";
    }
}