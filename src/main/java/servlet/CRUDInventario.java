/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.InventarioJpaController;
import dto.Inventario;

/**
 * ""
 * 
 * @author shaho
 */
@WebServlet(name = "CRUDInventario", urlPatterns = { "/CRUDInventario" })
public class CRUDInventario extends HttpServlet {

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
                    InventarioJpaController dao = new InventarioJpaController(empr);
                    switch (opcion) {
                        case "1":// listar
                            String codi = session.getAttribute("codinventario").toString();
                            String grucod = session.getAttribute("grucod").toString();

                            out.print("{\"resultado\":\"ok\",\"data\":" + dao.listarJson(Integer.parseInt(codi), grucod)
                                    + "}");
                            break;
                        case "2":// agregar
                            Inventario obj = new Inventario();
                            String nomb = request.getParameter("nomb");
                            String tipoCaptura = request.getParameter("tipoInventario");
                            codi = session.getAttribute("codi").toString();
                            obj.setCodusu(Integer.parseInt(codi));
                            obj.setDesinv(nomb);
                            obj.setEstado("S");
                            obj.setFeccre(new Date());
                            obj.setEstinv("A");
                            if ("estatico".equals(tipoCaptura)) {
                                obj.setCaptura("S");
                            }
                            obj.setDireccionado(true);
                            dao.create(obj);
                            out.print("{\"resultado\":\"ok\"}");
                            break;
                        case "3":// listar
                            String codinv = request.getParameter("codinv");
                            codi = session.getAttribute("codinventario").toString();
                            grucod = session.getAttribute("grucod").toString();
                            String codalm = request.getParameter("codalm");
                            out.print("{\"resultado\":\"ok\",\"data\":" + dao.listarconsolidado(
                                    Integer.parseInt(codinv), Integer.parseInt(codi), grucod, codalm) + "}");
                            break;
                        case "4":// listar
                            String codinvalm = request.getParameter("codinvalm");
                            codi = session.getAttribute("codinventario").toString();
                            grucod = session.getAttribute("grucod").toString();
                            out.print("{\"resultado\":\"ok\",\"data\":" + dao.listarconsolidadoalmacen(
                                    Integer.parseInt(codinvalm), Integer.parseInt(codi), grucod) + "}");
                            break;
                        case "5":// listar
                            codinvalm = request.getParameter("codinvalm");
                            out.print("{\"resultado\":\"ok\",\"data\":"
                                    + dao.listarconsolidadoalmacengeneral(Integer.parseInt(codinvalm)) + "}");
                            break;
                        case "6":// Cerrar inventario
                            codinv = request.getParameter("codinv");
                            obj = dao.findInventario(Integer.parseInt(codinv));
                            obj.setEstinv("C");
                            codi = session.getAttribute("codi").toString();
                            obj.setUsecier(Integer.parseInt(codi));
                            dao.edit(obj);
                            dao.cerrarinventario(Integer.parseInt(codinv));
                            out.print("{\"resultado\":\"ok\"}");
                            break;
                        case "7":// listar consolidado agrupado usuarios
                            codinv = request.getParameter("codinv");
                            codi = session.getAttribute("codinventario").toString();
                            grucod = session.getAttribute("grucod").toString();
                            codalm = request.getParameter("codalm");
                            out.print("{\"resultado\":\"ok\",\"data\":" + dao.listarconsolidadoagrupado(
                                    Integer.parseInt(codinv), Integer.parseInt(codi), grucod, codalm) + "}");
                            break;
                        case "8":// listar consolidado agrupado productos
                            codinv = request.getParameter("codinv");
                            codi = session.getAttribute("codinventario").toString();
                            grucod = session.getAttribute("grucod").toString();
                            codalm = request.getParameter("codalm");
                            obj = dao.findInventario(Integer.parseInt(codinv));
                            if ("S".equals(obj.getCaptura())) {
                                out.print("{\"resultado\":\"ok\",\"data\":"
                                        + dao.listarconsolidadoagrupadoproductocapturas(Integer.parseInt(codinv),
                                                Integer.parseInt(codi), grucod, codalm)
                                        + "}");
                            } else {
                                out.print("{\"resultado\":\"ok\",\"data\":" + dao.listarconsolidadoagrupadoproducto(
                                        Integer.parseInt(codinv), Integer.parseInt(codi), grucod, codalm) + "}");
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
