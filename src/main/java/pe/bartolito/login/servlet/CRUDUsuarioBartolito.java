/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package pe.bartolito.login.servlet;

import pe.bartolito.login.dao.UsuariosBartolitoJpaController;
import dao.UsuariosJpaController;
import pe.bartolito.login.dto.UsuariosBartolito;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sun.jvm.hotspot.runtime.Bytes;

/**
 *
 * @author USUARIO
 */
@WebServlet(name = "CRUDUsuarioBartolito", urlPatterns = {"/CRUDUsuarioBartolito"})
public class CRUDUsuarioBartolito extends HttpServlet {

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
                    UsuariosBartolitoJpaController usuDao = new UsuariosBartolitoJpaController(empr);
                    switch (opcion) {
                        case "1"://listar
                            String data = usuDao.Listar();
                            out.print("{\"resultado\":\"ok\",\"data\":" + data + "}");
                            break;
                        case "2"://agregar
                            String useusr = request.getParameter("useusr");
                            UsuariosJpaController usulolDao = new UsuariosJpaController(empr);
                            String usenam = request.getParameter("usenam");
                            String codalm = request.getParameter("codalm");
                            UsuariosBartolito usu = new UsuariosBartolito();
                            byte[] usepas = request.getParameter("usepas").getBytes();
                            String buscado = usulolDao.buscarLogiBarto(useusr);
                            if (buscado.equals("noencontrado")) {
                                usu.setFeccre(Timestamp.valueOf(LocalDateTime.now()));
                                usu.setUsecod(1);
                                usu.setEstado("S");
                                usu.setUsenam(usenam);
                                usu.setUseusr(useusr);
                                usu.setUsepas(usepas);
                                usu.setCodalm(codalm);
                                usuDao.create(usu);
                                out.print("{\"resultado\":\"ok\"}");
                            } else if (!buscado.equals("error")) {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"encontrado\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"errorconsulta\"}");
                            }
                            break;
                        case "3"://modificar
                            Integer codigo = Integer.parseInt(request.getParameter("usecod"));
                            useusr = request.getParameter("useusr");
                            usenam = request.getParameter("usenam");
                            codalm = request.getParameter("codalm");
                            usulolDao = new UsuariosJpaController(empr);
                            buscado = usulolDao.buscarLogiBarto(useusr);
                            if (buscado.equals(request.getParameter("usecod")) || (buscado.equals("noencontrado"))) {
                                usu = usuDao.findUsuariosBartolito(codigo);
                                usu.setUsenam(usenam);
                                usu.setUseusr(useusr);
                                usu.setCodalm(codalm);
                                usu.setFecumv(Timestamp.valueOf(LocalDateTime.now()));
                                usuDao.edit(usu);
                                out.print("{\"resultado\":\"ok\"}");
                            } else if (!buscado.equals("error")) {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"encontrado\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"errorconsulta\"}");
                            }
                            break;

                        case "4"://eliminar
                            codigo = Integer.parseInt(request.getParameter("usecod"));
                            usu = usuDao.findUsuariosBartolito(codigo);
                            usu.setEstado("N");
                            usuDao.edit(usu);
                            out.print("{\"resultado\":\"ok\"}");
                            break;
                        case "5"://cambiarclave
                            String de = session.getAttribute("de").toString();
                            if (de.equals("b")) {
                                codigo = Integer.parseInt(session.getAttribute("codbar").toString());
                                usepas = request.getParameter("usepas").getBytes();
                                byte[] usepas2 = request.getParameter("usepas2").getBytes();

                                usu = usuDao.findUsuariosBartolito(codigo);
                                if (Arrays.equals(usu.getUsepas(), usepas)) {
                                    usu.setUsepas(usepas2);
                                    usu.setFecumv(Timestamp.valueOf(LocalDateTime.now()));
                                    usuDao.edit(usu);
                                    out.print("{\"resultado\":\"ok\"}");
                                } else {
                                    out.print("{\"resultado\":\"error\",\"mensaje\":\"nocoinc\"}");
                                }
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"nobart\"}");
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
