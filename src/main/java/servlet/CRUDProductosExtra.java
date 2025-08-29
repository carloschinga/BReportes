/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.CajaextraJpaController;
import dto.Cajaextra;
import dto.CajaextraPK;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "CRUDProductosExtra", urlPatterns = {"/CRUDProductosExtra"})
public class CRUDProductosExtra extends HttpServlet {

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
                    CajaextraJpaController dao = new CajaextraJpaController(empr);
                    switch (opcion) {
                        case "1"://listar por caja
                            String caja = request.getParameter("caja");
                            String data = dao.listardetJson(caja);
                            out.print("{\"resultado\":\"ok\",\"fecha\":\""
                                    + Timestamp.valueOf(LocalDateTime.now()).toString() + "\",\"data\":" + data + "}");
                            break;
                        case "2"://agregar
                            caja = request.getParameter("caja");
                            String codpro = request.getParameter("codpro");
                            String orden = request.getParameter("orden");
                            String codi = session.getAttribute("codi").toString();
                            String fecha = request.getParameter("fecha");
                            CajaextraPK objpk = new CajaextraPK(caja, codpro);
                            Cajaextra obj = dao.findCajaextra(objpk);
                            if (obj == null) {
                                obj = new Cajaextra(objpk);
                                obj.setCante(1);
                                obj.setCantf(0);
                                obj.setEstado("S");
                                obj.setFeccre(Timestamp.valueOf(fecha));
                                obj.setUsecod(Integer.parseInt(codi));
                                obj.setOrden(Integer.parseInt(orden));
                                obj.setFecumv(new Date());
                                dao.create(obj);
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                if ("N".equals(obj.getEstado())) {
                                    obj = new Cajaextra(objpk);
                                    obj.setCante(1);
                                    obj.setCantf(0);
                                    obj.setEstado("S");
                                    obj.setFeccre(Timestamp.valueOf(fecha));
                                    obj.setUsecod(Integer.parseInt(codi));
                                    obj.setOrden(Integer.parseInt(orden));
                                    obj.setFecumv(new Date());
                                    dao.edit(obj);
                                    out.print("{\"resultado\":\"ok\"}");
                                } else {
                                    out.print("{\"resultado\":\"error\",\"mensaje\":\"yaexiste\"}");
                                }
                            }
                            break;
                        case "3"://editar
                            caja = request.getParameter("caja");
                            codpro = request.getParameter("codpro");
                            String cant = request.getParameter("cant");
                            int val = Integer.parseInt(request.getParameter("val"));
                            codi = session.getAttribute("codi").toString();
                            objpk = new CajaextraPK(caja, codpro);
                            obj = dao.findCajaextra(objpk);
                            if (obj == null) {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"noexiste\"}");
                            } else {
                                if ("N".equals(obj.getEstado())) {
                                    out.print("{\"resultado\":\"error\",\"mensaje\":\"noexiste\"}");
                                } else {
                                    if ("e".equals(cant)) {
                                        obj.setCante(val);
                                    } else if ("f".equals(cant)) {
                                        obj.setCantf(val);
                                    }
                                    obj.setFecumv(new Date());
                                    obj.setUsecod(Integer.parseInt(codi));
                                    dao.edit(obj);
                                    out.print("{\"resultado\":\"ok\"}");
                                }
                            }
                            break;
                        case "4"://eliminar
                            caja = request.getParameter("caja");
                            codpro = request.getParameter("codpro");
                            objpk = new CajaextraPK(caja, codpro);
                            obj = dao.findCajaextra(objpk);
                            if (obj == null) {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"noexiste\"}");
                            } else {
                                if ("N".equals(obj.getEstado())) {
                                    out.print("{\"resultado\":\"error\",\"mensaje\":\"noexiste\"}");
                                } else {
                                    obj.setEstado("N");
                                    dao.edit(obj);
                                    out.print("{\"resultado\":\"ok\"}");
                                }
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
