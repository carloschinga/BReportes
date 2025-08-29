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
import org.json.JSONObject;

/**
 *
 * @author USUARIO
 */
@WebServlet(name = "ValidarSesion", urlPatterns = { "/validarsesion" })
public class ValidarSesion extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            try {
                HttpSession session = request.getSession(false);
                if (session != null) {
                    String logueado = session.getAttribute("logueado").toString();
                    String de = session.getAttribute("de").toString();
                    String logi = session.getAttribute("logi").toString();
                    String empr = session.getAttribute("empr").toString();
                    String codalminv = session.getAttribute("codalminv").toString();
                    String grudes = session.getAttribute("grudes").toString();
                    String siscod = session.getAttribute("siscod").toString();
                    String sisent = session.getAttribute("sisent").toString();
                    String grucod = session.getAttribute("grucod").toString();
                    if (de.equals("l")) {
                        String codi = session.getAttribute("codi").toString();

                        String central = session.getAttribute("central").toString();
                        if (session.getAttribute("logueado").toString().equals("1")) {
                            JSONObject jsonObj = new JSONObject();
                            jsonObj.put("resultado", "ok");
                            jsonObj.put("codi", codi);
                            jsonObj.put("logi", logi);
                            jsonObj.put("empr", empr);
                            jsonObj.put("grucod", grucod);
                            jsonObj.put("grudes", grudes);
                            jsonObj.put("siscod", siscod);
                            jsonObj.put("sisent", sisent);
                            jsonObj.put("central", central);
                            jsonObj.put("codalminv", codalminv);
                            jsonObj.put("de", de);
                            out.println(jsonObj.toString());
                        } else {
                            out.println("{\"resultado\":\"error\"}");
                        }
                    } else if (de.equals("b")) {
                        String codbar = session.getAttribute("codbar").toString();
                        if (session.getAttribute("logueado").toString().equals("1")) {
                            JSONObject jsonObj = new JSONObject();
                            jsonObj.put("resultado", "ok");
                            jsonObj.put("codbar", codbar);
                            jsonObj.put("logi", logi);
                            jsonObj.put("empr", empr);
                            jsonObj.put("de", de);
                            jsonObj.put("codalminv", codalminv);
                            jsonObj.put("grudes", grudes);
                            jsonObj.put("grucod", grucod);
                            jsonObj.put("siscod", siscod);
                            jsonObj.put("sisent", sisent);
                            out.println(jsonObj.toString());
                        } else {
                            out.println("{\"resultado\":\"error\"}");
                        }
                    } else if (de.equals("i")) {
                        String codinventario = session.getAttribute("codinventario").toString();
                        if (session.getAttribute("logueado").toString().equals("1")) {
                            JSONObject jsonObj = new JSONObject();
                            jsonObj.put("resultado", "ok");
                            jsonObj.put("codinventario", codinventario);
                            jsonObj.put("logi", logi);
                            jsonObj.put("empr", empr);
                            jsonObj.put("de", de);
                            jsonObj.put("grudes", grudes);
                            jsonObj.put("grucod", grucod);
                            jsonObj.put("siscod", siscod);
                            jsonObj.put("sisent", sisent);
                            out.println(jsonObj.toString());
                        }
                    } else {
                        out.println("{\"resultado\":\"error\"}");

                    }
                } else {
                    out.println("{\"resultado\":\"error\",\"mensaje\":\"no hay una sesion\"}");
                }
            } catch (Exception ex) {
                out.println("{\"resultado\":\"error\",\"mensaje\":\"" + ex + "\"}");
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
