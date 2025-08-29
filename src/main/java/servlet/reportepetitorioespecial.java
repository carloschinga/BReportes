package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.JpaPadre;
import dao.PetitorioJpaController;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

@WebServlet(name = "reportepetitorioespecial", urlPatterns = { "/reportepetitorioespecial" })
public class reportepetitorioespecial extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServletOutputStream out = response.getOutputStream();
        Connection cn = null;
        EntityManager em = null;
        JpaPadre DAO = null;
        PetitorioJpaController daoClinica = null;

        try {
            HttpSession session = request.getSession(false); // false: no crear nueva sesión
            Object emprObj = (session != null) ? session.getAttribute("empr") : null;

            if (emprObj == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                out.print("{\"resultado\":\"error\",\"mensaje\":\"nosession\"}");
                return;
            }

            String sercod = request.getParameter("sercod");
            if (sercod == null || sercod.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                out.print("{\"resultado\":\"error\",\"mensaje\":\"Parametro sercod requerido\"}");
                return;
            }

            String nombre = request.getParameter("sercodText");
            if (nombre == null || nombre.isEmpty()) {
                nombre = "Reporte de Petitorio Especial";
            }

            String empr = emprObj.toString();
            DAO = new JpaPadre(empr);
            em = DAO.getEntityManager();
            em.getTransaction().begin();
            cn = em.unwrap(Connection.class);

            // Timeout en operaciones largas
            try {
                cn.setNetworkTimeout(Executors.newSingleThreadExecutor(), 120000); // 120s
            } catch (Exception ignored) {}

            daoClinica = new PetitorioJpaController("c");

            // 1. Obtener médicos por especialidad
            List<String> medXesp = daoClinica.medicosDeLaEspecialidad(sercod);

            // 2. Crear XML para SP
            JSONArray medArray = new JSONArray();
            for (String medcodi : medXesp) {
                medArray.put(medcodi);
            }
            JSONObject medicosJson = new JSONObject();
            medicosJson.put("Medico", medArray);
            String xmlMedicos = XML.toString(medicosJson, "Medicos");

            // 3. Cargar reporte Jasper
            String reportFileName = "petitorioespecialidad.jasper";
            InputStream reportStream = getServletConfig().getServletContext().getResourceAsStream(reportFileName);

            if (reportStream == null) {
                throw new IOException("No se encontró el archivo: " + reportFileName);
            }

            // 4. Parámetros
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("XmlMedicos", xmlMedicos);
            paramMap.put("NombreEspecialidad", nombre);

            // 5. Config Jasper
            JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
            jasperReportsContext.setProperty("net.sf.jasperreports.language", "es");
            jasperReportsContext.setProperty("net.sf.jasperreports.export.character.encoding", "UTF-8");
            jasperReportsContext.setProperty("net.sf.jasperreports.query.timeout", "600"); // 10 min
            jasperReportsContext.setProperty("net.sf.jasperreports.export.pdf.compressed", "true");

            // 6. Generar reporte
            JasperFillManager fillManager = JasperFillManager.getInstance(jasperReportsContext);
            JasperPrint jasperPrint = fillManager.fill(reportStream, paramMap, cn);

            if (em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }

            // 7. Exportar
            String tipo = request.getParameter("tipo");
            if ("pdf".equalsIgnoreCase(tipo)) {
                response.resetBuffer();
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline; filename=petitorio_especial.pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            } else {
                response.resetBuffer();
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader("Content-Disposition", "attachment; filename=petitorio_especial.xlsx");

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

        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            try {
                out.print("{\"resultado\":\"error\",\"mensaje\":\"" + ex.getMessage().replace("\"", "'") + "\"}");
            } catch (Exception e) {
                out.print("{\"resultado\":\"error\",\"mensaje\":\"Error inesperado\"}");
            }
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
            // opcional: retornar mensaje simple
            out.print("Servlet reportepetitorioespecial activo");
        }
    }

    @Override
    public String getServletInfo() {
        return "Reporte de Petitorio por Especialidad";
    }
}
