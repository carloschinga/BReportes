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
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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

/**
 *
 * @author USUARIO
 */
@WebServlet(name = "ReporteGuiaTransferencia", urlPatterns = {"/ReporteGuiaTransferencia"})
public class ReporteGuiaTransferencia extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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

            // Obtener conexión JDBC
            try (Connection cn = em.unwrap(Connection.class); ServletOutputStream out = response.getOutputStream(); InputStream reportStream = getServletContext().getResourceAsStream("/guia_transferencia.jasper")) {

                if (reportStream == null) {
                    throw new IOException("No se pudo encontrar el archivo guia_transferencia.jasper");
                }

                // Leer parámetros
                String secini = request.getParameter("secini");
                String secfin = request.getParameter("secfin");
                String fechainicio = request.getParameter("fechainicio");
                String fechafin = request.getParameter("fechafin");
                String res = request.getParameter("res");
                if (res == null) {
                    res = "";
                }

                // Parámetros para Jasper
                HashMap<String, Object> paramMap = new HashMap<>();
                if (secini != null && !secini.isEmpty()) {
                    paramMap.put("invnumMin", Integer.parseInt(secini));
                }
                if (secfin != null && !secfin.isEmpty()) {
                    paramMap.put("invnumMax", Integer.parseInt(secfin));
                }
                if (fechainicio != null && !fechainicio.isEmpty()) {
                    paramMap.put("fechaInicio", fechainicio);
                }
                if (fechafin != null && !fechafin.isEmpty()) {
                    paramMap.put("fechaFin", fechafin);
                }
                paramMap.put("res", res);

                // Configuración de JasperReports local
                JasperReportsContext jasperContext = DefaultJasperReportsContext.getInstance();
                jasperContext.setProperty("net.sf.jasperreports.language", "es");
                jasperContext.setProperty("net.sf.jasperreports.export.character.encoding", "UTF-8");
                jasperContext.setProperty("net.sf.jasperreports.compiler.temp.dir", System.getProperty("java.io.tmpdir"));

                JasperFillManager fillManager = JasperFillManager.getInstance(jasperContext);
                JasperPrint jasperPrint = fillManager.fill(reportStream, paramMap, cn);

                // Exportar a PDF
                response.resetBuffer();
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline; filename=PickingList.pdf");
                JasperExportManager.exportReportToPdfStream(jasperPrint, out);

            }

        } catch (Exception ex) {
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

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
