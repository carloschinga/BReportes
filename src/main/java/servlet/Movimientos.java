/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import com.google.gson.Gson;
import dao.FaMovimientosDetalleJpaController;
import dao.FaMovimientosJpaController;
import dao.FaProductosSPJpaController;
import dto.Dato;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
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
@WebServlet(name = "Movimientos", urlPatterns = {"/Movimientos"})
public class Movimientos extends HttpServlet {

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
                    FaMovimientosJpaController movDao = new FaMovimientosJpaController(empr);
                    switch (opcion) {
                        case "1":
                            int invnum = Integer.parseInt(request.getParameter("invnum"));
                            FaMovimientosDetalleJpaController movDetDao = new FaMovimientosDetalleJpaController(empr);
                            out.print("{\"resultado\":\"ok\",\"data\":"+movDetDao.vistaPrevia(invnum)+"}");
                            
                            break;
                        case "7":
                            int invnumMin = Integer.parseInt(request.getParameter("invnumMin"));
                            int invnumMax = Integer.parseInt(request.getParameter("invnumMax"));
                            String tipo = request.getParameter("tipo");
                            boolean tip = tipo.equals("S");
                            List<Integer> lista = movDao.findByInvnumInRange(invnumMin, invnumMax, tip);
                            Gson gson = new Gson();
                            String json = gson.toJson(lista);
                            out.print(json);
                            break;
                        case "8":
                            String invnumMin2 = request.getParameter("invnumMin");
                            String invnumMax2 = request.getParameter("invnumMax");
                            String fecMin = request.getParameter("fecMin");
                            String fecMax = request.getParameter("fecMax");
                            String filtro = request.getParameter("filtro");
                            String codalminv = session.getAttribute("codalminv").toString();
                            String resp = movDao.Listar(invnumMin2, invnumMax2, fecMin,fecMax,filtro,codalminv);
                            
                            out.print("{\"resultado\":\"ok\",\"data\":"+resp+"}");
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
