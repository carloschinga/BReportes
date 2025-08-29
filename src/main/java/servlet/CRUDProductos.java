/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.FaProductosSPJpaController;
import dto.FaProductos;

/**
 *
 * @author USUARIO
 */
@WebServlet(name = "CRUDProductos", urlPatterns = { "/CRUDProductos" })
public class CRUDProductos extends HttpServlet {

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
                    FaProductosSPJpaController dao = new FaProductosSPJpaController(empr);

                    switch (opcion) {
                        case "1":// listar productos y sus fracciones
                            String codlab = request.getParameter("codlab");
                            String codgen = request.getParameter("codgen");
                            String codfam = request.getParameter("codfam");
                            String codtip = request.getParameter("codtip");
                            String codpro = request.getParameter("codpro");
                            out.print(
                                    "{\"data\":" + dao.listarProductosAplicFrac(codlab, codgen, codfam, codtip, codpro)
                                            + ",\"resultado\":\"ok\"}");
                            break;
                        case "2":// aplicar preferencia entero fraccion
                            codpro = request.getParameter("codpro");
                            String estado = request.getParameter("estado");
                            Integer blister;
                            try {
                                blister = Integer.parseInt(request.getParameter("blister"));
                            } catch (Exception e) {
                                blister = null;
                            }
                            out.print(dao.aplicarFracJSON(codpro, estado, blister));
                            break;
                        case "3":// aplicar preferencia masterpack
                            codpro = request.getParameter("codpro");
                            estado = request.getParameter("estado");
                            try {
                                blister = Integer.parseInt(request.getParameter("masterpack"));
                            } catch (Exception e) {
                                blister = null;
                            }
                            out.print(dao.aplicarMasterPackJSON(codpro, estado, blister));
                            break;
                        case "4":// listar productos y sus fracciones pero al enviar vacio solo pasa los
                                 // fraccionados
                            codlab = request.getParameter("codlab");
                            codgen = request.getParameter("codgen");
                            codfam = request.getParameter("codfam");
                            codtip = request.getParameter("codtip");
                            codpro = request.getParameter("codpro");

                            if ("".equals(codlab) & "".equals(codgen) & "".equals(codfam) & "".equals(codtip)
                                    & "".equals(codpro)) {
                                String data = dao.listarProductosAplicSoloFrac();
                                out.print("{\"resultado\":\"ok\",\"data\":" + data + "}");
                            } else {
                                out.print("{\"data\":"
                                        + dao.listarProductosAplicFrac(codlab, codgen, codfam, codtip, codpro)
                                        + ",\"resultado\":\"ok\"}");
                            }
                            break;
                        case "5":// listar productos y sus fracciones
                            out.print("{\"data\":" + dao.listarProductos()
                                    + ",\"resultado\":\"ok\"}");
                            break;
                        case "6":// listar productos qr y precios
                            out.print("{\"data\":" + dao.listarProductosQRPrecios()
                                    + ",\"resultado\":\"ok\"}");
                            break;
                        case "7":// listar productos lab y generico
                            out.print("{\"data\":" + dao.listarproductoslabgen()
                                    + ",\"resultado\":\"ok\"}");
                            break;
                        case "8":// listar productos para las compras
                            codlab = request.getParameter("codlab");
                            codgen = request.getParameter("codgen");
                            codtip = request.getParameter("codtip");
                            String estr = request.getParameter("estr");
                            String pet = request.getParameter("pet");

                            out.print("{\"data\":" + dao.listarProductoscompras(codtip, codlab, codgen, estr, pet)
                                    + ",\"resultado\":\"ok\"}");
                            break;
                        case "9":// listar productos para las compras
                            codpro = request.getParameter("codpro");

                            out.print("{\"data\":" + dao.obtenerbasico(codpro)
                                    + ",\"resultado\":\"ok\"}");
                            break;
                        case "10":// listar productos para recepcion picking y en inventario producto- hacia la
                                  // tabla inventario-toma
                            int codinv = Integer.parseInt(request.getParameter("codinv"));
                            int codinvalm = Integer.parseInt(request.getParameter("codinvalm"));
                            out.print("{\"data\":" + dao.listarProductosQRstkfraSinLista(codinv, codinvalm)
                                    + ",\"resultado\":\"ok\"}");
                            break;
                        case "11":
                            codpro = request.getParameter("codpro");
                            FaProductos obj = dao.findFaProductos(codpro);
                            if ("S".equals(obj.getPredesac())) {
                                obj.setPredesac("N");
                            } else {
                                obj.setPredesac("S");
                            }
                            dao.edit(obj);
                            out.print("{\"resultado\":\"ok\"}");
                            break;
                        case "12":// listar productos para las compras
                            codpro = request.getParameter("codpro");
                            obj = dao.findFaProductos(codpro);

                            out.print("{\"data\":" + dao.indicadorcompra(codpro)
                                    + ",\"resultado\":\"ok\",\"despro\":\"" + obj.getDespro() + "\"}");
                            break;
                        case "13":// listar productos para las compras
                            codpro = request.getParameter("codpro");

                            out.print("{\"data\":" + dao.ultimascompras(codpro)
                                    + ",\"resultado\":\"ok\"}");
                            break;
                        case "14":
                            codinv = Integer.parseInt(request.getParameter("codinv"));
                            String codinvalmStr = request.getParameter("codinvalm");
                            codinvalm = Integer.parseInt(codinvalmStr);
                            out.print("{\"data\":" + dao.listarProductosQRstkfraConLista(codinv, codinvalm)
                                    + ",\"resultado\":\"ok\"}");
                            break;
                        case "15":
                            out.print("{\"data\":" + dao.listarProductosQRstkfra()
                                    + ",\"resultado\":\"ok\"}");
                            break;
                        default:
                            out.print("{\"resultado\":\"error\",\"mensaje\":\"noproce\"}");
                            break;
                    }
                } else {
                    out.print("{\"resultado\":\"error\",\"mensaje\":\"nosession\"}");
                }
            } catch (Exception e) {
                out.print("{\"resultado\":\"error\",\"mensaje\":\"" + e + "\"}");
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
