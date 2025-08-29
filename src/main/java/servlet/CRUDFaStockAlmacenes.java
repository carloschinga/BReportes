/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.FaInmovilizadosJpaController;
import dao.FaStockAlmacenesJpaController;
import dao.FaSubalmacenesJpaController;
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
@WebServlet(name = "CRUDFaStockAlmacenes", urlPatterns = {"/CRUDFaStockAlmacenes"})
public class CRUDFaStockAlmacenes extends HttpServlet {

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
                    FaStockAlmacenesJpaController almDao = new FaStockAlmacenesJpaController(empr);
                    switch (opcion) {
                        case "1"://listado del codigo de los almacenes, producto, stock, lote, y si esta inmovilizado
                            String codlab = request.getParameter("codlab");
                            String codgen = request.getParameter("codgen");
                            String codfam = request.getParameter("codfam");
                            String codtip = request.getParameter("codtip");
                            String codpro = request.getParameter("codpro");
                            String codalminv = session.getAttribute("codalminv").toString();
                            int codsubalm = Integer.parseInt(request.getParameter("codsubalm"));
                            if ("".equals(codlab) & "".equals(codgen) & "".equals(codfam) & "".equals(codtip) & "".equals(codpro)) {
                                FaSubalmacenesJpaController subDao = new FaSubalmacenesJpaController(empr);
                                String data = subDao.listarinmov(codsubalm,codalminv);
                                
                                out.print("{\"resultado\":\"ok\",\"data\":" + data + "}");
                            } else {
                                String data = almDao.listarProductosStocks(codlab, codgen, codfam, codtip, codpro, codsubalm,codalminv);
                                out.print("{\"resultado\":\"ok\",\"data\":" + data + "}");
                            }
                            break;
                        case "2"://aplicar inmovilizados
                            codpro = request.getParameter("codpro");
                            String codlot = request.getParameter("codlot");
                            codsubalm = Integer.parseInt(request.getParameter("codsubalm"));
                            String estado = request.getParameter("estado");
                            codalminv = session.getAttribute("codalminv").toString();
                            Integer cantf;
                            Integer cante;
                            try {
                                cantf = Integer.parseInt(request.getParameter("cantf"));
                            } catch (Exception e) {
                                cantf = null;
                            }
                            try {
                                cante = Integer.parseInt(request.getParameter("cante"));
                            } catch (Exception e) {
                                cante = null;
                            }
                            FaInmovilizadosJpaController inmovDao = new FaInmovilizadosJpaController(empr);
                            out.print(inmovDao.aplicarinmovilizadosJSON(codpro, codlot, codsubalm, estado, cante, cantf, codalminv));
                            
                            break;
                        case "3"://Listar stocks de los establecimientos por producto y almacen
                            codpro = request.getParameter("codpro");
                            String codalm = request.getParameter("codalm");
                            out.print("{\"resultado\":\"ok\",\"data\":" + almDao.obtenerstkalmproducto(codpro, codalm) + "}");
                            break;
                        case "4"://Listar stocks de los establecimientos por producto y almacen
                            codpro = request.getParameter("codpro");
                            out.print("{\"resultado\":\"ok\",\"data\":" + almDao.obtenerstkalmproductotodos(codpro) + "}");
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
