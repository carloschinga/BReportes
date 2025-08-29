/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.JpaPadre;
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
@WebServlet(name = "reportefaltante_excedente", urlPatterns = {"/reportefaltante_excedente"})
public class reportefaltante_excedente extends HttpServlet {

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
                out.print("{\"resultado\":\"error\",\"mensaje\":\"nosesion\"}");
                return;
            }

            String empr = emprObj.toString();
            DAO = new JpaPadre(empr);
            em = DAO.getEntityManager();
            em.getTransaction().begin();
            cn = em.unwrap(Connection.class);

            String ordenini = request.getParameter("ordenini");
            String ordenfin = request.getParameter("ordenfin");
            String fechainicio = request.getParameter("fechainicio");
            String fechafin = request.getParameter("fechafin");

            String central = (String) session.getAttribute("central");
            String siscod = (String) session.getAttribute("siscod");

            InputStream reportStream = getServletConfig().getServletContext()
                    .getResourceAsStream("reporteFaltanteExcedente.jasper");

            if (reportStream == null) {
                throw new IOException("No se pudo encontrar el archivo reporteFaltanteExcedente.jasper");
            }

            // Parámetros
            HashMap<String, Object> paramMap = new HashMap<>();
            String subreportePath = getServletContext().getRealPath("/");
            paramMap.put("SUBREPORT_DIR", subreportePath);

            if ("N".equals(central)) {
                paramMap.put("siscod", siscod);
            } else {
                paramMap.put("siscod", null);
            }

            paramMap.put("ordenini", (ordenini != null && !ordenini.isEmpty()) ? Integer.parseInt(ordenini) : null);
            paramMap.put("ordenfin", (ordenfin != null && !ordenfin.isEmpty()) ? Integer.parseInt(ordenfin) : null);
            paramMap.put("fechaInicio", (fechainicio != null && !fechainicio.isEmpty()) ? fechainicio : null);
            paramMap.put("fechaFin", (fechafin != null && !fechafin.isEmpty()) ? fechafin : null);

            // Configuración JasperReports
            JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
            jasperReportsContext.setProperty("net.sf.jasperreports.language", "es");
            jasperReportsContext.setProperty("net.sf.jasperreports.export.character.encoding", "UTF-8");

            JasperFillManager fillManager = JasperFillManager.getInstance(jasperReportsContext);
            JasperPrint jasperPrint = fillManager.fill(reportStream, paramMap, cn);

            String tipo = request.getParameter("tipo");
            if ("pdf".equalsIgnoreCase(tipo)) {
                response.resetBuffer();
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline; filename=reporteFaltanteExcedente.pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            } else {
                response.resetBuffer();
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader("Content-Disposition", "attachment; filename=reporteFaltanteExcedente.xlsx");

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
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (em != null && em.isOpen()) em.close();
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Genera reporte de faltantes y excedentes en PDF o Excel";
    }
}