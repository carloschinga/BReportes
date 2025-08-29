/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.AlmacenesBartolitoUbicacionesJpaController;
import dto.AlmacenesBartolitoUbicaciones;
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
@WebServlet(name = "CRUDAlmacenesBartolitoUbicaciones", urlPatterns = {"/CRUDAlmacenesBartolitoUbicaciones"})
public class CRUDAlmacenesBartolitoUbicaciones extends HttpServlet {

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
            AlmacenesBartolitoUbicacionesJpaController DAO;
            try {
                HttpSession session = request.getSession(true);
                Object emprObj = session.getAttribute("empr");
                if (emprObj != null) {
                    String empr = emprObj.toString();
                    String opcion = request.getParameter("opcion");
                    DAO = new AlmacenesBartolitoUbicacionesJpaController(empr);

                    switch (opcion) {
                        case "1": // Listar ubicaciones
                            String codalmbar = request.getParameter("codalmbar");
                            out.print("{\"resultado\":\"ok\",\"data\":" + DAO.listarUbicacionesJson(Integer.parseInt(codalmbar)) + "}");
                            break;

                        case "2": // Insertar nueva ubicación
                            String codigo = request.getParameter("codigo");
                            String codbar = request.getParameter("codbar");
                            String rotacion = request.getParameter("rotacion");
                            String m3 = request.getParameter("m3");
                            codalmbar = request.getParameter("codalmbar");

                            AlmacenesBartolitoUbicaciones obj = new AlmacenesBartolitoUbicaciones();
                            obj.setCodigo(codigo);
                            obj.setCodbar(codbar);
                            obj.setRotacion(rotacion);
                            obj.setM3(new BigDecimal(m3));
                            obj.setCodalmbar(Integer.parseInt(codalmbar));

                            String codusu = session.getAttribute("codi").toString();
                            obj.setUsecod(Integer.parseInt(codusu));
                            obj.setFeccre(new Date());
                            obj.setEstado("S");

                            DAO.create(obj);
                            out.print("{\"resultado\":\"ok\"}");
                            break;

                        case "3": // Modificar ubicación existente
                            String codubi = request.getParameter("codubi");
                            codigo = request.getParameter("codigo");
                            codbar = request.getParameter("codbar");
                            rotacion = request.getParameter("rotacion");
                            m3 = request.getParameter("m3");

                            AlmacenesBartolitoUbicaciones ubiModificada = DAO.findAlmacenesBartolitoUbicaciones(Integer.parseInt(codubi));
                            if (ubiModificada != null) {
                                ubiModificada.setCodigo(codigo);
                                ubiModificada.setCodbar(codbar);
                                ubiModificada.setRotacion(rotacion);
                                ubiModificada.setM3(new BigDecimal(m3));
                                ubiModificada.setFeccreumv(new Date());

                                codusu = session.getAttribute("codi").toString();
                                ubiModificada.setUsecodumv(Integer.parseInt(codusu));

                                DAO.edit(ubiModificada);
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"Ubicación no encontrada\"}");
                            }
                            break;

                        case "4": // Eliminar ubicación (cambio de estado a "N")
                            codubi = request.getParameter("codubi");

                            ubiModificada = DAO.findAlmacenesBartolitoUbicaciones(Integer.parseInt(codubi));
                            if (ubiModificada != null) {
                                ubiModificada.setEstado("N");
                                ubiModificada.setFeccreumv(new Date());

                                codusu = session.getAttribute("codi").toString();
                                ubiModificada.setUsecodumv(Integer.parseInt(codusu));

                                DAO.edit(ubiModificada);
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"Ubicación no encontrada\"}");
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
