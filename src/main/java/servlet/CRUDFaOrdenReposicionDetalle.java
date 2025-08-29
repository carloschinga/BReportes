/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.FaOrdenReposicionDetalleJpaController;
import dao.FaOrdenReposicionJpaController;
import dto.FaOrdenReposicionDetalle;
import dto.FaOrdenReposicionDetallePK;
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
@WebServlet(name = "CRUDFaOrdenReposicionDetalle", urlPatterns = {"/CRUDFaOrdenReposicionDetalle"})
public class CRUDFaOrdenReposicionDetalle extends HttpServlet {

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
                    FaOrdenReposicionDetalleJpaController dao = new FaOrdenReposicionDetalleJpaController(empr);
                    FaOrdenReposicionJpaController cabdao = new FaOrdenReposicionJpaController(empr);
                    switch (opcion) {
                        case "1"://listado del detalle
                            int invnum = Integer.parseInt(request.getParameter("invnum"));
                            String data = dao.buscarInvnum(invnum);
                            out.print("{\"resultado\":\"ok\",\"data\":" + data + "}");
                            break;
                        case "2"://agregar
                            invnum = Integer.parseInt(request.getParameter("invnum"));
                            if (!"".equals(cabdao.verificarenviado(invnum)) && !"S".equals(cabdao.verificarenviado(invnum))) {
                                int numitm = dao.obtenerUltNumitm(invnum) + 1;
                                String codpro = request.getParameter("codpro");
                                String cante = request.getParameter("cante");
                                String cantf = request.getParameter("cantf");
                                FaOrdenReposicionDetallePK objpk = new FaOrdenReposicionDetallePK(invnum, numitm);
                                FaOrdenReposicionDetalle obj = new FaOrdenReposicionDetalle(objpk);
                                if (!"".equals(cante)) {
                                    obj.setCante(Integer.parseInt(cante));
                                } else {
                                    obj.setCante(null);
                                }
                                if (!"".equals(cantf)) {
                                    obj.setCantf(Integer.parseInt(cantf));
                                } else {
                                    obj.setCantf(null);
                                }
                                obj.setEstado("S");
                                obj.setCodpro(codpro);
                                obj.setFeccre(new Date());
                                String codi = session.getAttribute("codi").toString();
                                obj.setUsecod(Integer.parseInt(codi));
                                dao.create(obj);
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"cerrado\"}");
                            }
                            break;
                        case "3"://Modificar
                            invnum = Integer.parseInt(request.getParameter("invnum"));
                            if (!"".equals(cabdao.verificarenviado(invnum)) && !"S".equals(cabdao.verificarenviado(invnum))) {
                                int numitm = Integer.parseInt(request.getParameter("numitm"));
                                String codpro = request.getParameter("codpro");
                                String cante = request.getParameter("cante");
                                String cantf = request.getParameter("cantf");
                                FaOrdenReposicionDetallePK objpk = new FaOrdenReposicionDetallePK(invnum, numitm);
                                FaOrdenReposicionDetalle obj = dao.findFaOrdenReposicionDetalle(objpk);
                                obj.setCodpro(codpro);
                                if (!"".equals(cante)) {
                                    obj.setCante(Integer.parseInt(cante));
                                } else {
                                    obj.setCante(null);
                                }
                                if (!"".equals(cantf)) {
                                    obj.setCantf(Integer.parseInt(cantf));
                                } else {
                                    obj.setCantf(null);
                                }
                                dao.edit(obj);
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"cerrado\"}");
                            }
                            break;
                        case "4"://eliminar
                            invnum = Integer.parseInt(request.getParameter("invnum"));
                            if (!"".equals(cabdao.verificarenviado(invnum)) && !"S".equals(cabdao.verificarenviado(invnum))) {
                                int numitm = Integer.parseInt(request.getParameter("numitm"));
                                FaOrdenReposicionDetallePK objpk = new FaOrdenReposicionDetallePK(invnum, numitm);
                                dao.destroy(objpk);
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"cerrado\"}");
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
