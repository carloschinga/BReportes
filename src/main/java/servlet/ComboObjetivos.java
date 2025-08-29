/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.ObjeventasJpaController;
import dao.SistemaJpaController;
import dto.Objeventas;
import dto.Sistema;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
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
 * @author sbdeveloperw
 */
@WebServlet(name = "ComboObjetivos", urlPatterns = {"/comboobjetivos"})
public class ComboObjetivos extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");

        HttpSession session = req.getSession(false);

        try {
            Object emprObj = session.getAttribute("empr");
            String empresa = emprObj.toString();
            ObjeventasJpaController dao = new ObjeventasJpaController(empresa);
            
            
            List<Objeventas> objetivos = dao.findObjeventasEntities();
            JSONArray array = new JSONArray();
            for (Objeventas obje : objetivos) {
                JSONObject obj = new JSONObject();
                obj.put("codobj", obje.getCodobj());
                obj.put("desobj", obje.getDesobj());
                array.put(obj);
            }
            
            resp.getWriter().write(array.toString());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter()
                    .write(new JSONObject().put("error", "Error al obtener los datos: " + e.getMessage()).toString());
        }
    }
    

}
