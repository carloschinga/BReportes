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
import org.json.JSONObject;
import pe.bartolito.conta.dao.ReportecajaDAO;

/**
 *
 * @author USUARIO
 */
@WebServlet(name = "CombosCajaServlet", urlPatterns = {"/comboscajaservlet"})
public class CombosCajaServlet extends HttpServlet {

    private final ReportecajaDAO dao = new ReportecajaDAO("a");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            String opcion = req.getParameter("opcion");
            if (opcion == null) {
                opcion = "1";
            }

            switch (opcion) {
                case "1":
                    out.print(dao.getSucursales());
                    break;

                case "2":
                    String siscodStr = req.getParameter("siscod");
                    if (siscodStr == null || siscodStr.trim().isEmpty()) {
                        JSONObject err = new JSONObject();
                        err.put("error", "Falta parámetro 'siscod' para obtener usuarios");
                        out.print(err.toString());
                    } else {
                        try {
                            int siscod = Integer.parseInt(siscodStr);
                            out.print(dao.getUsuariosPorSucursal(siscod));
                        } catch (NumberFormatException nfe) {
                            JSONObject err = new JSONObject();
                            err.put("error", "siscod inválido: " + siscodStr);
                            out.print(err.toString());
                        }
                    }
                    break;

                case "3":
                    // Parámetros esperados: siscod, fecha1, fecha2
                    String siscodStr3 = req.getParameter("siscod");
                    String fecha1 = req.getParameter("fecha1"); // espera 'YYYY-MM-DD HH:mm:ss'
                    String fecha2 = req.getParameter("fecha2");
                    if (siscodStr3 == null || siscodStr3.trim().isEmpty()
                            || fecha1 == null || fecha2 == null || fecha1.trim().isEmpty() || fecha2.trim().isEmpty()) {
                        out.print("[]");
                    } else {
                        try {
                            int siscod3 = Integer.parseInt(siscodStr3);
                            ReportecajaDAO dao = new ReportecajaDAO("e");
                            String resultado = dao.getAperturasPorSucursalFechas(siscod3, fecha1, fecha2);
                            out.print(resultado != null ? resultado : "[]");
                        } catch (NumberFormatException nfe) {
                            out.print("[]");
                        }
                    }
                    break;
                default:
                    out.print("[]");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().print("{\"error\":\"Error en servlet: " + e.getMessage() + "\"}");
        }
    }

}
