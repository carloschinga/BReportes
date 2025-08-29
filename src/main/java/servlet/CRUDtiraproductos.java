/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.TiraproductocabeceraJpaController;
import dao.TiraproductodetalleJpaController;
import dto.Tiraproductocabecera;
import dto.Tiraproductodetalle;
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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author LOQ
 */
@WebServlet(name = "CRUDtiraproductos", urlPatterns = {"/CRUDtiraproductos"})
public class CRUDtiraproductos extends HttpServlet {

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
                request.setCharacterEncoding("UTF-8");
                HttpSession session = request.getSession(true);
                Object emprObj = session.getAttribute("empr");
                if (emprObj != null) {
                    String empr = emprObj.toString();
                    String opcion = request.getParameter("opcion");
                    TiraproductocabeceraJpaController dao = new TiraproductocabeceraJpaController(empr);
                    
                    switch (opcion) {
                        case "1"://listar
                            out.print("{\"resultado\":\"ok\",\"data\":" + dao.listarJson() + "}");
                            break;
                        case "2"://listar
                            String codtir = request.getParameter("codtir");
                            out.print("{\"resultado\":\"ok\",\"data\":" + dao.listarJson(Integer.parseInt(codtir)) + "}");
                            break;
                        case "3"://agregar
                            StringBuilder sb = new StringBuilder();
                            try (BufferedReader reader = request.getReader()) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            }
                            String body = sb.toString();
                            
                            try {
                                JSONObject json = new JSONObject(body);
                                String codigoLista = json.getString("codigo");
                                JSONArray codpros = json.getJSONArray("codpros");
                                
                                Tiraproductocabecera cab = new Tiraproductocabecera();
                                TiraproductodetalleJpaController daodet = new TiraproductodetalleJpaController(empr);
                                
                                cab.setTirnam(codigoLista);
                                cab.setFeccre(new Date());
                                dao.create(cab);
                                
                                int codtirint = dao.obtenerultcodtir();
                                
                                for (int i = 0; i < codpros.length(); i++) {
                                    String codpro = codpros.getString(i);
                                    
                                    Tiraproductodetalle detalle = new Tiraproductodetalle();
                                    detalle.setCodtir(codtirint);
                                    detalle.setCodpro(codpro);
                                    daodet.create(detalle);
                                }
                                response.setStatus(HttpServletResponse.SC_OK);
                                response.getWriter().write("{\"resultado\":\"ok\"}");
                            } catch (Exception e) {
                                e.printStackTrace();
                                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                response.getWriter().write("{\"resultado\":\"error\",\"mensaje\":\"" + e.getMessage() + "\"}");
                            }
                            break;
                        case "4":
                            codtir = request.getParameter("codtir");
                            dao.destroy(Integer.parseInt(codtir));
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
