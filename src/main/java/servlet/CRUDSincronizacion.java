/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.FaStockVencimientosJpaController;
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
@WebServlet(name = "CRUDSincronizacion", urlPatterns = { "/sincronizacion" })
public class CRUDSincronizacion extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");

        HttpSession session = req.getSession(true);
        Object emprObj = session.getAttribute("empr");
        Object almaObj = session.getAttribute("codalminv");
        JSONObject jsonRespuesta = new JSONObject();

        if (emprObj == null || almaObj == null) {
            resp.getWriter().write(jsonRespuesta.put("success", "error").put("message", "Sesion no valida").toString());
        }

        String empr = emprObj.toString();
        String alma = almaObj.toString();
        try {
            FaStockVencimientosJpaController dao = new FaStockVencimientosJpaController(empr);
            int resul = dao.setearNegativos();

            String resultado = dao.buscarPorAlmacen(alma);

            resp.getWriter().write(resultado);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter()
                    .write(new JSONObject().put("message", "Error al obtener los datos: " + e.getMessage()).toString());
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");

        HttpSession session = req.getSession(true);
        Object emprObj = session.getAttribute("empr");
        Object almaObj = session.getAttribute("codalminv");
        JSONObject jsonRespuesta = new JSONObject();

        if (emprObj == null || almaObj == null) {
            resp.getWriter().write(jsonRespuesta.put("success", "error").put("message", "Sesion no valida").toString());
        }

        String empr = emprObj.toString();
        String alma = almaObj.toString();
        try {
            FaStockVencimientosJpaController dao = new FaStockVencimientosJpaController(empr);

            String resultado = dao.buscarPorAlmacen(alma);

            resp.getWriter().write(resultado);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter()
                    .write(new JSONObject().put("message", "Error al obtener los datos: " + e.getMessage()).toString());
        }
    }

}
