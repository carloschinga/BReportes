/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.DocumentosSeriesJpaController;
import dao.FaTipoMovimientosJpaController;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author USUARIO
 */
@WebServlet(name = "CRUPFaTipoMovimientos", urlPatterns = {"/CRUPFaTipoMovimientos"})
public class CRUPFaTipoMovimientos extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            try {
                HttpSession session = request.getSession(true);
                Object emprObj = session.getAttribute("empr");
                if (emprObj != null) {
                    String empr = emprObj.toString();
                    String opcion = request.getParameter("opcion");
                    FaTipoMovimientosJpaController tipmovDao = new FaTipoMovimientosJpaController(empr);
                    switch (opcion) {
                        case "1": //Listar los permisos de movimiento de un determinado usuario en su sistema
                            String usu = session.getAttribute("codi").toString();
                            String siscod = session.getAttribute("siscod").toString();
                            String codalm = request.getParameter("codalm");
                            String data = tipmovDao.listarTiposJson(Integer.parseInt(siscod), codalm, Integer.parseInt(usu));
                            out.print("{\"resultado\":\"ok\",\"data\":" + data + "}");
                            break;
                        case "2": //listar la ultima secuencia del tipo del movimiento junto a las series que incorpora
                            String tipkar = request.getParameter("tipkar");
                            data = String.valueOf(tipmovDao.obtenerUltInvkar(tipkar)+1);
                            DocumentosSeriesJpaController docDao = new DocumentosSeriesJpaController(empr);
                            out.print("{\"resultado\":\"ok\",\"data\":" + data + ",\"doc\":" + docDao.listarDocJson(tipkar) + "}");

                            break;
                        default:
                            out.print("{\"resultado\":\"error\",\"mensaje\":\"noproce\"}");
                            break;
                    }
                } else {
                    out.print("{\"resultado\":\"error\",\"mensaje\":\"nosession\"}");
                }
            } catch (Exception e) {
                out.print("{\"resultado\":\"error\",\"mensaje\":\"errorgeneral\"}");
            }
        }
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
        processRequest(request, response);
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
