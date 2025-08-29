/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.FaOrdenReposicionJpaController;
import dto.FaOrdenReposicion;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
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
@WebServlet(name = "CRUDFaOrdenReposicion", urlPatterns = {"/CRUDFaOrdenReposicion"})
public class CRUDFaOrdenReposicion extends HttpServlet {

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
                    FaOrdenReposicionJpaController dao = new FaOrdenReposicionJpaController(empr);
                    switch (opcion) {
                        case "1"://listar por siscod
                            int siscod = Integer.parseInt(session.getAttribute("siscod").toString());
                            String data = dao.buscarSiscod(siscod);
                            out.print("{\"resultado\":\"ok\",\"data\":" + data + "}");
                            break;
                        case "2"://agregar
                            siscod = Integer.parseInt(session.getAttribute("siscod").toString());
                            FaOrdenReposicion obj = new FaOrdenReposicion(dao.obtenerUltInvnum() + 1);
                            obj.setSiscod(siscod);
                            String codi = session.getAttribute("codi").toString();
                            obj.setUsecodcre(Integer.parseInt(codi));
                            obj.setFeccre(new Date());
                            obj.setEstado("S");
                            dao.create(obj);
                            out.print("{\"resultado\":\"ok\"}");
                            break;
                        case "3":
                            int invnum = Integer.parseInt(request.getParameter("invnum"));
                            obj = dao.findFaOrdenReposicion(invnum);
                            obj.setEstado("N");
                            dao.edit(obj);
                            out.print("{\"resultado\":\"ok\"}");
                            break;
                        case "4"://enviar
                            invnum = Integer.parseInt(request.getParameter("invnum"));
                            obj = dao.findFaOrdenReposicion(invnum);
                            obj.setEnviado("S");
                            obj.setFecenv(new Date());
                            codi = session.getAttribute("codi").toString();
                            obj.setUsecodenv(Integer.parseInt(codi));
                            dao.edit(obj);
                            out.print("{\"resultado\":\"ok\"}");
                            break;
                        case "5"://listar para central
                             data = dao.listarcentral();
                            out.print("{\"resultado\":\"ok\",\"data\":" + data + "}");
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
