package servlet;

import dao.JpaPadre;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
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

@WebServlet(name = "ReporteQR", urlPatterns = { "/ReporteQR" })
public class ReporteQR extends HttpServlet {

    // ⚡ ThreadPool global para manejar timeouts sin fugas de hilos
    private static final ExecutorService executorService =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final int NETWORK_TIMEOUT_MS = 120000; // 2 minutos
    private static final int QUERY_TIMEOUT_SECONDS = 600; // 10 minutos

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServletOutputStream out = null;
        Connection cn = null;
        EntityManager em = null;
        JpaPadre DAO = null;
        InputStream reportStream = null;
        JasperPrint jasperPrint = null;

        try {
            out = response.getOutputStream();

            // Validar sesión
            HttpSession session = request.getSession(false);
            Object emprObj = (session != null) ? session.getAttribute("empr") : null;

            if (emprObj == null) {
                response.setContentType("application/json");
                out.print("{\"resultado\":\"error\",\"mensaje\":\"nosession\"}");
                return;
            }

            // DB
            String empr = emprObj.toString();
            DAO = new JpaPadre(empr);
            em = DAO.getEntityManager();

            if (em == null || !em.isOpen()) {
                response.setContentType("application/json");
                out.print("{\"resultado\":\"error\",\"mensaje\":\"Error de conexión a BD\"}");
                return;
            }

            cn = em.unwrap(Connection.class);
            cn.setNetworkTimeout(executorService, NETWORK_TIMEOUT_MS);

            // Reporte
            reportStream = getServletContext().getResourceAsStream("qrproductos.jasper");
            if (reportStream == null) {
                throw new IOException("No se encontró el archivo qrproductos.jasper");
            }

            HashMap<String, Object> paramMap = new HashMap<>();

            JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
            jasperReportsContext.setProperty("net.sf.jasperreports.language", "es");
            jasperReportsContext.setProperty("net.sf.jasperreports.export.character.encoding", "UTF-8");
            jasperReportsContext.setProperty("net.sf.jasperreports.query.timeout", String.valueOf(QUERY_TIMEOUT_SECONDS));
            jasperReportsContext.setProperty("net.sf.jasperreports.export.pdf.compressed", "true");
            jasperReportsContext.setProperty("net.sf.jasperreports.compiler.temp.dir",
                    System.getProperty("java.io.tmpdir"));
            jasperReportsContext.setProperty("net.sf.jasperreports.default.pdf.font.name", "Helvetica");
            jasperReportsContext.setProperty("net.sf.jasperreports.default.pdf.encoding", "UTF-8");

            JasperFillManager fillManager = JasperFillManager.getInstance(jasperReportsContext);
            jasperPrint = fillManager.fill(reportStream, paramMap, cn);

            String tipo = request.getParameter("tipo");

            if ("pdf".equalsIgnoreCase(tipo)) {
                response.resetBuffer();
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline; filename=ReporteQRProductos.pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);
            } else {
                response.resetBuffer();
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader("Content-Disposition", "attachment; filename=ReporteQRProductos.xlsx");

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
            System.err.println("Error al generar el reporte: " + ex.getMessage());
            ex.printStackTrace();
            if (!response.isCommitted() && out != null) {
                response.setContentType("application/json");
                String msg = ex.getMessage() != null ? ex.getMessage().replace("\"", "'") : "Error desconocido";
                out.print("{\"resultado\":\"error\",\"mensaje\":\"" + msg + "\"}");
            }
        } finally {
            if (jasperPrint != null) {
                jasperPrint.getPages().clear();
            }
            if (reportStream != null) {
                try { reportStream.close(); } catch (IOException ignored) {}
            }
            if (cn != null) {
                try { cn.close(); } catch (Exception ignored) {}
            }
            if (em != null && em.isOpen()) {
                try { em.close(); } catch (Exception ignored) {}
            }
            if (out != null) {
                try { out.flush(); } catch (Exception ignored) {}
                // ⚠️ No cerramos `out`, Tomcat lo maneja
            }
        }
    }

    @Override
    public void destroy() {
        if (!executorService.isShutdown()) {
            executorService.shutdown();
        }
        super.destroy();
    }

    @Override
    public String getServletInfo() {
        return "Genera reportes QR en PDF o Excel";
    }
}
