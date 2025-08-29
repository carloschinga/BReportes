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
import pe.bartolito.conta.dao.CombosConciliacionTarjetas;
import pe.bartolito.conta.dao.ConciliacionTarjetasIzapayComprobanteDAO;

/**
 *
 * @author USUARIO
 */
@WebServlet(name = "ConciliacionComprobanteTarjetasIzipay", urlPatterns = {"/conciliacioncomprobantetarjetasizipay"})
public class ConciliacionComprobanteTarjetasIzipay extends HttpServlet {
 private CombosConciliacionTarjetas consiDAO = new CombosConciliacionTarjetas("e");
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json; charset=UTF-8");

       // String empresa = request.getParameter("empresa");   // por ejemplo: "mi_empresa"
        String empresaId = request.getParameter("empresaId"); // por ejemplo: "03"
        String unidComIdStr = request.getParameter("unidComId"); // por ejemplo: "3"
        String periodo = request.getParameter("periodo");       // por ejemplo: "202507"

        // Validar parámetros
        if (empresaId == null || unidComIdStr == null || periodo == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Faltan parámetros\"}");
            return;
        }

        try (PrintWriter out = response.getWriter()) {
            int unidComId = Integer.parseInt(unidComIdStr);

            // Instanciar el DAO
            ConciliacionTarjetasIzapayComprobanteDAO dao =
                    new ConciliacionTarjetasIzapayComprobanteDAO("e");

            // Obtener JSON desde el procedimiento almacenado
            String json = dao.obtenerConciliacionJSON(empresaId, unidComId, periodo);

            out.write(json);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"unidComId debe ser numérico\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Error interno del servidor\"}");
        }
    }
}


