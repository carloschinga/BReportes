/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.InventarioAlmacenJpaController;
import dao.InventarioJpaController;
import dao.InventarioTomaJpaController;
import dto.Inventario;
import dto.InventarioToma;

/**
 *
 * @author shaho
 */
@WebServlet(name = "CRUDInventarioToma", urlPatterns = { "/CRUDInventarioToma" })
public class CRUDInventarioToma extends HttpServlet {

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
                    InventarioTomaJpaController dao = new InventarioTomaJpaController(empr);
                    InventarioAlmacenJpaController daoalm = new InventarioAlmacenJpaController(empr);
                    switch (opcion) {
                        case "1":// listar
                            String codinvalm = request.getParameter("codinvalm");
                            String codi = session.getAttribute("codinventario").toString();
                            String estado = daoalm.obtenerEstado(Integer.parseInt(codinvalm));
                            if ("A".equals(estado)) {
                                int coddeta = dao.obtenercoddeta(Integer.parseInt(codinvalm), Integer.parseInt(codi));
                                if (coddeta == -1) {
                                    out.print("{\"resultado\":\"error\",\"mensaje\":\"errorbuscar\"}");
                                } else {
                                    out.print("{\"resultado\":\"ok\",\"data\":"
                                            + dao.listar(Integer.parseInt(codinvalm), coddeta) + "}");
                                }
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"cerrado\"}");
                            }
                            break;
                        case "2":// listar
                            codinvalm = request.getParameter("codinvalm");
                            String codpro = request.getParameter("codpro");
                            String codinv = request.getParameter("codinv");
                            InventarioJpaController daoinv = new InventarioJpaController(empr);
                            Inventario objinv = daoinv.findInventario(Integer.parseInt(codinv));
                            String codiAlmacen = dao.getAlmacen(Integer.parseInt(codinvalm));
                            estado = daoalm.obtenerEstado(Integer.parseInt(codinvalm));
                            if ("A".equals(estado)) {
                                if ("S".equals(objinv.getCaptura())) {
                                    out.print(
                                            "{\"resultado\":\"ok\",\"what\":\"if\",\"data\":"
                                                    + dao.listarlotescaptura(
                                                            Integer.parseInt(codinvalm), codpro, codiAlmacen)
                                                    + "}");
                                } else {
                                    out.print("{\"resultado\":\"ok\",\"what\": \"else\",\"data\":"
                                            + dao.listarlotes(Integer.parseInt(codinvalm), codpro, codiAlmacen)
                                            + "}");
                                }

                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"cerrado\"}");
                            }
                            break;
                        case "3":// agregar modificar
                            StringBuilder sb = new StringBuilder();
                            try (BufferedReader reader = request.getReader()) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            }
                            String respuesta;
                            String body = sb.toString();
                            codinvalm = request.getParameter("codinvalm");
                            codi = session.getAttribute("codinventario").toString();
                            estado = daoalm.obtenerEstado(Integer.parseInt(codinvalm));
                            if ("A".equals(estado)) {
                                respuesta = dao.actualizar_toma(Integer.parseInt(codinvalm), body,
                                        Integer.parseInt(codi));

                                if ("S".equals(respuesta)) {
                                    out.print("{\"resultado\":\"ok\"}");
                                } else {
                                    out.print("{\"resultado\":\"error\",\"mensaje\":\"errorproce\"}");
                                }
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"cerrado\"}");
                            }
                            break;
                        case "4":// ajustar version antigua
                            codinvalm = request.getParameter("codinvalm");
                            codpro = request.getParameter("codpro");
                            String lote = request.getParameter("lote");
                            String cante = request.getParameter("cante");
                            String cantf = request.getParameter("cantf");
                            codi = session.getAttribute("codinventario").toString();
                            int codtom = dao.obtenercodtomaajuste(Integer.parseInt(codinvalm), codpro, lote);
                            InventarioToma obj;
                            if (codtom > 0) {
                                obj = dao.findInventarioToma(codtom);
                                obj.setUsecodmod(Integer.parseInt(codi));
                                obj.setFecmod(new Date());
                            } else {
                                obj = new InventarioToma();
                                obj.setCodpro(codpro);
                                obj.setCodinvalm(Integer.parseInt(codinvalm));
                                obj.setLote(lote);
                                obj.setFeccre(new Date());
                                obj.setUsecodinvcree(Integer.parseInt(codi));
                                obj.setTiptom("A");
                            }
                            obj.setStkalm(0);
                            obj.setStkalmM(0);
                            int coddeta = dao.obtenercoddeta(Integer.parseInt(codinvalm), Integer.parseInt(codi));
                            if (coddeta > 0) {
                                obj.setCoddeta(coddeta);
                            }
                            if (!"".equals(cante)) {
                                obj.setTome(Integer.parseInt(cante));
                            }
                            if (!"".equals(cantf)) {
                                obj.setTomf(Integer.parseInt(cantf));
                            }
                            if (codtom > 0) {
                                dao.edit(obj);
                            } else {
                                dao.create(obj);
                            }
                            out.print("{\"resultado\":\"ok\"}");
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
