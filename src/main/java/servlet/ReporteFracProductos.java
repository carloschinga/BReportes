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

@WebServlet(name = "ReporteFracProductos", urlPatterns = {"/ReporteFracProductos"})
public class ReporteFracProductos extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        Object emprObj = session.getAttribute("empr");

        if (emprObj == null) {
            response.setContentType("application/json");
            try (ServletOutputStream out = response.getOutputStream()) {
                out.print("{\"resultado\":\"error\",\"mensaje\":\"nosession\"}");
            }
            return;
        }

        String empr = emprObj.toString();
        JpaPadre DAO = new JpaPadre(empr);

        EntityManager em = null;
        try {
            em = DAO.getEntityManager();

            // Obtener conexión JDBC sin transacción
            try (Connection cn = em.unwrap(Connection.class); ServletOutputStream out = response.getOutputStream(); InputStream reportStream = getServletContext().getResourceAsStream("/FracProductos.jasper")) {

                if (reportStream == null) {
                    throw new IOException("No se pudo encontrar el archivo FracProductos.jasper");
                }

                // Parámetros del reporte
                HashMap<String, Object> paramMap = new HashMap<>();
                // Ejemplo: agregar parámetros solo si tienen valor
                String tipo = request.getParameter("tipo");
                // Puedes agregar otros parámetros según tu lógica

                // Configuración de JasperReports local
                JasperReportsContext jasperContext = DefaultJasperReportsContext.getInstance();
                jasperContext.setProperty("net.sf.jasperreports.language", "es");
                jasperContext.setProperty("net.sf.jasperreports.export.character.encoding", "UTF-8");
                jasperContext.setProperty("net.sf.jasperreports.query.timeout", "600");
                jasperContext.setProperty("net.sf.jasperreports.export.pdf.compressed", "true");
                jasperContext.setProperty("net.sf.jasperreports.compiler.temp.dir", System.getProperty("java.io.tmpdir"));
                jasperContext.setProperty("net.sf.jasperreports.default.pdf.font.name", "Helvetica");
                jasperContext.setProperty("net.sf.jasperreports.default.pdf.encoding", "UTF-8");

                JasperFillManager fillManager = JasperFillManager.getInstance(jasperContext);
                JasperPrint jasperPrint = fillManager.fill(reportStream, paramMap, cn);

                if ("pdf".equalsIgnoreCase(tipo)) {
                    response.resetBuffer();
                    response.setContentType("application/pdf");
                    response.setHeader("Content-Disposition", "inline; filename=ReporteFracProductos.pdf");
                    JasperExportManager.exportReportToPdfStream(jasperPrint, out);

                } else {
                    response.resetBuffer();
                    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                    response.setHeader("Content-Disposition", "attachment; filename=ReporteFracProductos.xlsx");

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

            }

        } catch (Exception ex) {
            System.err.println("Error en ReporteFracProductos: " + ex.getMessage());
            ex.printStackTrace();

            response.setContentType("application/json");
            try (ServletOutputStream out = response.getOutputStream()) {
                out.print("{\"resultado\":\"error\",\"mensaje\":\"" + ex.getMessage().replace("\"", "'") + "\"}");
            }

        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Genera reporte de fracciones de productos en PDF o Excel.";
    }
}
