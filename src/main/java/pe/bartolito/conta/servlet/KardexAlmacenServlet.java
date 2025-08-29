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
import pe.bartolito.conta.dao.KardexAlmacenDAO;

/**
 *
 * @author USUARIO
 */
@WebServlet(name = "KardexAlmacenServlet", urlPatterns = {"/kardexalmacenservlet"})
public class KardexAlmacenServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        try (PrintWriter out = resp.getWriter()) {

            try {
                String aniomes = req.getParameter("aniomes");
                String codalm = req.getParameter("codalm");

                // Validación de parámetros obligatorios
                if (aniomes == null || aniomes.trim().isEmpty()
                        || codalm == null || codalm.trim().isEmpty()) {

                    JSONObject error = new JSONObject();
                    error.put("error", "Parámetros 'aniomes' y 'codalm' son obligatorios");
                    out.print(error.toString());
                    return;
                }

                // Llamada a DAO con el nuevo SP
                KardexAlmacenDAO dao = new KardexAlmacenDAO("e");
                String data = dao.ComboKardexPorMes(aniomes, codalm);

                // Si hay datos, el SP ya devuelve un arreglo JSON; si no, devolvemos arreglo vacío
                out.print((data != null && !data.trim().isEmpty()) ? data : "[]");

            } catch (Exception e) {
                e.printStackTrace();
                JSONObject error = new JSONObject();
                error.put("error", "Error al procesar la consulta: " + e.getMessage());
                out.print(error.toString());
            }
        }
    }

}
