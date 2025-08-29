/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import util.JwtUtil;

import dao.UsuariosJpaController;
import util.Sesion;

/**
 *
 * @author USUARIO
 */
@WebServlet(name = "Validar", urlPatterns = {"/validar"})
public class Validar extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            String token = request.getParameter("token");
            JwtUtil jwt = new JwtUtil();
            String usuario = jwt.extractUsername(token);
            UsuariosJpaController usuDAO = new UsuariosJpaController("a");
            String deOfJson = usuDAO.buscarLogiBarto(usuario);
            String usu = null;
            if (deOfJson != null) {
                try {
                    URL url = new URL("http://181.224.248.20:8080/bauth/auth/loginByUsername"); // Reemplaza con tu URL real
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);

                    // 2. Enviar credenciales al endpoint
                    String requestBody = "{\"username\":\"" + usuario + "\"}";
                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = requestBody.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    // 3. Leer respuesta del endpoint
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                            StringBuilder responseSb = new StringBuilder();
                            String responseLine;
                            while ((responseLine = br.readLine()) != null) {
                                responseSb.append(responseLine.trim());
                            }
                            usu = responseSb.toString();
                            String jsonResponse = responseSb.toString();
                            System.out.println("Respuesta del endpoint: " + jsonResponse);

                            // 4. Parsear JSON y validar
                            JSONObject usua = new JSONObject(jsonResponse); // Usamos org.json.JSONObject
                            if (usua.getString("resultado").equals("OK")) {
                                // 5. Configurar sesión con los datos del endpoint
                                // 5. Configurar sesión con los datos del endpoint
                                HttpSession sesion = request.getSession(true);
                                sesion.removeAttribute("logueado");
                                sesion.removeAttribute("codi");
                                sesion.removeAttribute("codbar");
                                sesion.removeAttribute("codinventario");
                                sesion.removeAttribute("logi");
                                sesion.removeAttribute("empr");
                                sesion.removeAttribute("grucod");
                                sesion.removeAttribute("grudes");
                                sesion.removeAttribute("siscod");
                                sesion.removeAttribute("sisent");
                                sesion.removeAttribute("de");
                                sesion.removeAttribute("codalminv");
                                sesion.removeAttribute("central");
                                sesion.removeAttribute("nombre");
                                sesion.removeAttribute("username");

                                sesion.setAttribute("codalminv", usua.getString("codalm_inv"));
                                sesion.setAttribute("codinventario", 0);
                                sesion.setAttribute("central", usua.getString("central"));
                                sesion.setAttribute("nombre", usua.getString("nombre"));
                                sesion.setAttribute("username", usua.getString("nombre"));

                                Sesion.crearsesion(request.getSession(true), usua.getInt("usecod"),
                                        usuario, "a", usua.getString("grucod"), usua.getString("grudes"),
                                        usua.getInt("siscod"),
                                        usua.getString("sisent"), usua.getString("de"));
                            } else {
                                // Manejar error de autenticación (ej: credenciales incorrectas)
                                System.err.println("Error en autenticación: " + usua.optString("mensaje", "Sin detalles"));
                            }
                        }
                    } else {
                        System.err.println("Error HTTP: " + responseCode);
                        usu = "{\"resultado\":\"error\",\"mensaje\":\"Codigo de endpoint diferente a 200\"}";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // Manejar error de conexión (ej: endpoint caído)
                    usu = "{\"resultado\":\"error\",\"mensaje\":\"Endpoint caido\"}";
                } finally {
                    out.print(usu);
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
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
