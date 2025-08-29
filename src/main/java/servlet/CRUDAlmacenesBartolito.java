/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.AlmacenesBartolitoJpaController;
import dto.AlmacenesBartolito;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author LOQ
 */
@WebServlet(name = "CRUDAlmacenesBartolito", urlPatterns = {"/CRUDAlmacenesBartolito"})
public class CRUDAlmacenesBartolito extends HttpServlet {

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

            AlmacenesBartolitoJpaController DAO;
            try {
                HttpSession session = request.getSession(true);
                Object emprObj = session.getAttribute("empr");
                if (emprObj != null) {
                    String empr = emprObj.toString();
                    String opcion = request.getParameter("opcion");
                    DAO = new AlmacenesBartolitoJpaController(empr);
                    switch (opcion) {
                        case "1":
                            out.print("{\"resultado\":\"ok\",\"data\":" + DAO.listarAlmacenesJson() + "}");
                            break;
                        case "2": // Insertar nuevo almacén
                            String descripcion = request.getParameter("descripcion");
                            String m3 = request.getParameter("m3");
                            int num = Integer.parseInt(request.getParameter("num"));
                            AlmacenesBartolito obj = new AlmacenesBartolito();
                            obj.setDescripcion(descripcion);
                            obj.setM3(new BigDecimal(m3));
                            String codusu = session.getAttribute("codi").toString();
                            obj.setUsecod(Integer.parseInt(codusu));
                            obj.setNum(num);
                            String codalminv = session.getAttribute("codalminv").toString();
                            obj.setCodalm(codalminv);
                            obj.setEstado("S");
                            DAO.create(obj);
                            out.print("{\"resultado\":\"ok\"}");
                            break;
                        case "3": // Modificar almacén existente
                            String codalmbar = request.getParameter("codalmbar");
                            descripcion = request.getParameter("descripcion");
                            m3 = request.getParameter("m3");
                            num = Integer.parseInt(request.getParameter("num"));

                            AlmacenesBartolito almacModificado = DAO.findAlmacenesBartolito(Integer.parseInt(codalmbar));
                            if (almacModificado != null) {
                                almacModificado.setDescripcion(descripcion);
                                almacModificado.setM3(new BigDecimal(m3));
                                almacModificado.setNum(num);
                                almacModificado.setFeccreumv(new Date());
                                codusu = session.getAttribute("codi").toString();
                                almacModificado.setUsecodumv(Integer.parseInt(codusu));
                                DAO.edit(almacModificado);
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"Almacén no encontrado\"}");
                            }
                            break;
                        case "4": // Modificar almacén existente
                            codalmbar = request.getParameter("codalmbar");

                            almacModificado = DAO.findAlmacenesBartolito(Integer.parseInt(codalmbar));
                            if (almacModificado != null) {
                                almacModificado.setFeccreumv(new Date());
                                codusu = session.getAttribute("codi").toString();
                                almacModificado.setUsecodumv(Integer.parseInt(codusu));
                                almacModificado.setEstado("N");
                                DAO.edit(almacModificado);
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"Almacén no encontrado\"}");
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
