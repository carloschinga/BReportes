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

/**
 *
 * @author USUARIO
 */
@WebServlet(name = "CombosConcialisacionTargetaServlet", urlPatterns = {"/combosConciliacionTarjetaServlet"})
public class CombosConciliacionTarjetaServlet extends HttpServlet {

    private CombosConciliacionTarjetas consiDAO = new CombosConciliacionTarjetas("e");
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String opcion = req.getParameter("opcion");
            String resultado = "";

            switch (opcion) {
                case "1":
                    // Combo de sucursal
                    resultado = consiDAO.ComboSucursal();
                    break;

                case "2":
                    // Combo de fecha
                    String proceso = req.getParameter("sucursal");
                    proceso= proceso.substring(0, 9);
                    if (proceso != null && !proceso.isEmpty()) {
                        resultado = consiDAO.ComboFecha(proceso);
                    } else {
                        resultado = "[]"; // JSON vacío si no hay parámetro
                    }
                    break;

                case "3":
                    // Combo de monto
                    String procesoTarea = req.getParameter("sucursal");
                    procesoTarea= procesoTarea.substring(0, 9);
                    String actividad = req.getParameter("fecha");
                    if (procesoTarea != null && !procesoTarea.isEmpty() &&
                            actividad != null && !actividad.isEmpty()) {
                        resultado = consiDAO.ComboMonto(procesoTarea, actividad);
                    } else {
                        resultado = "[]"; // JSON vacío si faltan parámetros
                    }
                    break;
                case "4":
                    // Combo de monto
                    String sucursal = req.getParameter("sucursal");
                    String fecha = req.getParameter("fecha");
                    double monto = Double.parseDouble(req.getParameter("monto"));
                    if (sucursal != null && !sucursal.isEmpty() &&
                            fecha != null && !fecha.isEmpty()) {
                        resultado = consiDAO.TraerDataFiltrada(sucursal, fecha, monto);
                    } else {
                        resultado = "[]"; // JSON vacío si faltan parámetros
                    }
                    break;
                default:
                    resultado = "[]"; // JSON vacío para opciones no válidas
                    break;
            }

            out.print(resultado);
            out.flush();

        } catch (Exception e) {
            // Manejo de errores - devolver JSON vacío
            out.print("[]");
            out.flush();
            e.printStackTrace(); // Para debugging
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
