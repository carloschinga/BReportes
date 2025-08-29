/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.InventarioAlmacenCabeceraJpaController;
import dto.InventarioAlmacenCabecera;
import java.io.BufferedReader;
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
 * @author shaho
 */
@WebServlet(name = "CRUDInventarioAlmacenCabecera", urlPatterns = { "/CRUDInventarioAlmacenCabecera" })
public class CRUDInventarioAlmacenCabecera extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
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
                    InventarioAlmacenCabeceraJpaController dao = new InventarioAlmacenCabeceraJpaController(empr);
                    switch (opcion) {
                        case "1":// listar
                            String estado = request.getParameter("estado");
                            String codinv = request.getParameter("codinv");
                            if ("edit".equals(estado)) {
                                String grucod = session.getAttribute("grucod").toString();
                                if ("SUPINV".equals(grucod) || "SUPERV".equals(grucod) || "JEFALM".equals(grucod)) {
                                    out.print("{\"resultado\":\"ok\",\"data\":"
                                            + dao.listaragregar(Integer.parseInt(codinv)) + "}");
                                }
                            } else {
                                String codi = session.getAttribute("codinventario").toString();
                                String grucod = session.getAttribute("grucod").toString();
                                out.print("{\"resultado\":\"ok\",\"data\":"
                                        + dao.listar(Integer.parseInt(codinv), Integer.parseInt(codi), grucod) + "}");
                            }
                            break;
                        case "2":// asignar

                            StringBuilder sb = new StringBuilder();
                            try (BufferedReader reader = request.getReader()) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            }
                            String body = sb.toString();
                            codinv = request.getParameter("codinv");
                            String respuesta = dao.actualizar_almacenes(Integer.parseInt(codinv), body);
                            if ("S".equals(respuesta)) {
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"errorproce\"}");
                            }
                            break;
                        case "3":// capturar

                            codinv = request.getParameter("codinvalm");
                            respuesta = dao.capturarStocks(Integer.parseInt(codinv));
                            if ("S".equals(respuesta)) {
                                InventarioAlmacenCabecera obj = dao
                                        .findInventarioAlmacenCabecera(Integer.parseInt(codinv));
                                obj.setCaptura("C");
                                dao.edit(obj);
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"errorproce\"}");
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

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
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
