/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.FaProductosSPJpaController;
import dao.RestriccionesJpaController;
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
 * @author USUARIO
 */
@WebServlet(name = "restricciones", urlPatterns = { "/restricciones" })
public class restricciones extends HttpServlet {

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
                    RestriccionesJpaController dao = new RestriccionesJpaController(empr);

                    switch (opcion) {
                        case "1": // Lista Almacenes
                            out.print("{\"mensaje\":" + dao.listarAlmacenes() + ",\"resultado\":\"ok\"}");
                            break;
                        case "2": // Lista Laboratorios
                            out.print("{\"data\":" + dao.listarLaboratorios() + ",\"resultado\":\"ok\"}");
                            break;
                        case "3": // Lista Familias
                            out.print("{\"data\":" + dao.listarFamilias() + ",\"resultado\":\"ok\"}");
                            break;
                        case "4": // Lista Generico
                            out.print("{\"data\":" + dao.listarGenericos() + ",\"resultado\":\"ok\"}");
                            break;
                        case "5":
                            String codlab = request.getParameter("codlab");
                            String codgen = request.getParameter("codgen");
                            String codfam = request.getParameter("codfam");
                            String codtip = request.getParameter("codtip");
                            String codpro = request.getParameter("codpro");
                            out.print("{\"data\":" + dao.listarProductos(codlab, codgen, codfam, codtip, codpro)
                                    + ",\"resultado\":\"ok\",\"restricciones\":"
                                    + dao.listarRestriciones(codlab, codgen, codfam, codtip, codpro) + "}");
                            break;
                        case "6":// aplicar restricciones
                            codpro = request.getParameter("codpro");
                            String codalm = request.getParameter("codalm");
                            String estado = request.getParameter("estado");
                            out.print(dao.aplicarRestriccionJSON(codpro, codalm, estado));
                            break;
                        case "7":// listar productos solo codigo y nombre
                            FaProductosSPJpaController prodao = new FaProductosSPJpaController(empr);
                            out.print("{\"data\":" + prodao.listarProductosNomb() + ",\"resultado\":\"ok\"}");
                            break;
                        case "8":
                            codalm = request.getParameter("codalm");
                            estado = request.getParameter("estado");
                            StringBuilder sb = new StringBuilder();
                            try (BufferedReader reader = request.getReader()) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            }
                            String body = sb.toString();
                            // Gson gson = new Gson();
                            // RequestData requestData = gson.fromJson(body, RequestData.class);
                            boolean error = false;
                            String res = dao.aplicarRestriccionLista(body, estado);
                            if (res.equals("E")) {
                                error = true;
                            }
                            if (error) {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"aplicres\"}");
                            } else {
                                out.print("{\"resultado\":\"ok\"}");
                            }
                            break;
                        case "9":
                            codlab = request.getParameter("codlab");
                            codgen = request.getParameter("codgen");
                            codfam = request.getParameter("codfam");
                            codtip = request.getParameter("codtip");
                            codpro = request.getParameter("codpro");
                            out.print("{\"data\":" + dao.listarProductos(codlab, codgen, codfam, codtip, codpro)
                                    + ",\"resultado\":\"ok\"}");
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

    private static class RequestData {

        String codpro;
    }

    private static class Obj {

        String codpro;
        String codalm;
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
