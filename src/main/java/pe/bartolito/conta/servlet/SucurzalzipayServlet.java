/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package pe.bartolito.conta.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pe.bartolito.conta.dao.SucuralzipayDAO;

/**
 *
 * @author USUARIO
 */
@WebServlet(name = "SucurzalzipayServlet", urlPatterns = {"/sucurzaizipayservlet"})
public class SucurzalzipayServlet extends HttpServlet {

   @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Configurar la respuesta como JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
         

            SucuralzipayDAO dao = new SucuralzipayDAO("e");
            String json = dao.obtenerSucursalesJSON();

            // Enviar el JSON como respuesta
            response.getWriter().write(json);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Error obteniendo sucursales\"}");
        }
    }

}
