/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.ReposicionRecepcionJpaController;
import dto.ReposicionRecepcion;
import dto.ReposicionRecepcionPK;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author shaho
 */
@WebServlet(name = "CRUDReposicionRecepcion", urlPatterns = {"/CRUDReposicionRecepcion"})
public class CRUDReposicionRecepcion extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            try {
                HttpSession session = request.getSession(true);
                Object emprObj = session.getAttribute("empr");
                if (emprObj != null) {
                    String empr = emprObj.toString();
                    String opcion = request.getParameter("opcion");
                    ReposicionRecepcionJpaController dao = new ReposicionRecepcionJpaController(empr);
                    switch (opcion) {
                        case "1"://agregar
                            String orden = request.getParameter("orden");
                            String codpro = request.getParameter("codpro");
                            String siscod = request.getParameter("siscod");
                            String cant = request.getParameter("cant");
                            String codi = session.getAttribute("codi").toString();
                            ReposicionRecepcionPK objpk = new ReposicionRecepcionPK(Integer.parseInt(orden), codpro, Integer.parseInt(siscod));
                            ReposicionRecepcion obj = dao.findReposicionRecepcion(objpk);
                            if (obj == null) {
                                obj = new ReposicionRecepcion(objpk);
                                obj.setCantidad(new BigDecimal(cant));
                                obj.setFeccre(new Date());
                                obj.setEstado("S");
                                obj.setUsecod(Integer.parseInt(codi));
                                dao.create(obj);
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                if ("N".equals(obj.getEstado())) {
                                    obj.setEstado("S");
                                    obj.setUsecod(Integer.parseInt(codi));
                                    obj.setFecumv(new Date());
                                    obj.setCantidad(new BigDecimal(cant));
                                    dao.edit(obj);
                                    out.print("{\"resultado\":\"ok\"}");
                                } else {
                                    out.print("{\"resultado\":\"error\",\"mensaje\":\"yaexiste\"}");
                                }
                            }
                            break;
                        case "2"://eliminar
                            orden = request.getParameter("orden");
                            codpro = request.getParameter("codpro");
                            siscod = request.getParameter("siscod");
                            objpk = new ReposicionRecepcionPK(Integer.parseInt(orden), codpro, Integer.parseInt(siscod));
                            obj = dao.findReposicionRecepcion(objpk);

                            if (obj == null) {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"noexiste\"}");
                            } else {
                                obj.setFecumv(new Date());
                                codi = session.getAttribute("codi").toString();
                                obj.setUsecod(Integer.parseInt(codi));
                                obj.setEstado("N");
                                dao.edit(obj);
                                out.print("{\"resultado\":\"ok\"}");
                            }
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
