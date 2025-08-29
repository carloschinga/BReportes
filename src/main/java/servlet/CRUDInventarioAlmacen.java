/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.InventarioAlmacenJpaController;
import dao.InventarioJpaController;
import dto.Inventario;
import dto.InventarioAlmacen;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

/**
 *
 * @author shaho
 */
@WebServlet(name = "CRUDInventarioAlmacen", urlPatterns = { "/CRUDInventarioAlmacen" })
public class CRUDInventarioAlmacen extends HttpServlet {

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
            InventarioAlmacenJpaController dao = null;
            try {
                HttpSession session = request.getSession(true);
                Object emprObj = session.getAttribute("empr");
                if (emprObj != null) {
                    String empr = emprObj.toString();
                    String opcion = request.getParameter("opcion");
                    dao = new InventarioAlmacenJpaController(empr);
                    switch (opcion) {
                        case "1":// listar
                            String estado = request.getParameter("estado");
                            String codinvcab = request.getParameter("codinvcab");
                            if ("edit".equals(estado)) {
                                String grucod = session.getAttribute("grucod").toString();
                                if ("SUPINV".equals(grucod) || "SUPERV".equals(grucod) || "JEFALM".equals(grucod)) {
                                    out.print("{\"resultado\":\"ok\",\"data\":"
                                            + dao.listaragregar(Integer.parseInt(codinvcab)) + "}");
                                }
                            } else {
                                String codi = session.getAttribute("codinventario").toString();
                                String grucod = session.getAttribute("grucod").toString();
                                out.print("{\"resultado\":\"ok\",\"data\":"
                                        + dao.listar(Integer.parseInt(codinvcab), Integer.parseInt(codi), grucod)
                                        + "}");
                            }
                            break;
                        case "2":
                            StringBuilder sb = new StringBuilder();
                            try (BufferedReader reader = request.getReader()) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            }
                            String body = sb.toString();
                            String codinv = request.getParameter("codinv");
                            String respuesta = dao.actualizar_almacenes(Integer.parseInt(codinv), body);
                            if ("S".equals(respuesta)) {
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"errorproce\"}");
                            }
                            break;
                        case "3":// cerrar
                            String codinvalm = request.getParameter("codinvalm");
                            InventarioAlmacen obj = dao.findInventarioAlmacen(Integer.parseInt(codinvalm));
                            obj.setFeccir(new Date());
                            obj.setEstdet("C");
                            dao.edit(obj);
                            out.print("{\"resultado\":\"ok\"}");
                            break;
                        case "4":
                            codinv = request.getParameter("codinv");
                            codinvcab = request.getParameter("codinvcab");
                            String codalm = request.getParameter("codalm");
                            String nombre = request.getParameter("nombre");
                            String tipo = request.getParameter("tipo");
                            String apertura = request.getParameter("apertura");
                            InventarioJpaController daoinv = new InventarioJpaController(empr);
                            Inventario objinv = daoinv.findInventario(Integer.parseInt(codinv));

                            if (!"A".equals(objinv.getEstinv())) {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"cerrado\"}");
                                
                                break;
                            }

                            obj = new InventarioAlmacen();
                            obj.setCodalm(codalm);
                            obj.setCodinv(Integer.parseInt(codinv));
                            obj.setCodinvcab(Integer.parseInt(codinvcab));
                            obj.setEstdet("A");
                            int numitm = dao.obtenerUltInvnum(Integer.parseInt(codinvcab));
                            /*
                             * int invalmant = dao.obtenerUltInvnumcodinvalm(Integer.parseInt(codinvcab));
                             * if ("A".equals(dao.obtenerEstado(invalmant))) {
                             * out.print("{\"resultado\":\"error\",\"mensaje\":\"abierto\"}");
                             * break;
                             * }
                             */
                            if ("A".equals(tipo)) {
                                obj.setNumitm(numitm);
                            } else {
                                obj.setNumitm(numitm + 1);
                            }
                            obj.setTipdet(tipo);
                            obj.setDesinvalm(nombre);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date fechaUtilDate = sdf.parse(apertura);
                            obj.setFecape(fechaUtilDate);
                            dao.create(obj);
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
