/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.SistemaJpaController;
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
@WebServlet(name = "ComboSistema", urlPatterns = {"/combosistema"})
public class ComboSistema extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");

        HttpSession session = req.getSession(false);

        try {
            Object emprObj = session.getAttribute("empr");
            String empresa = emprObj.toString();

            SistemaJpaController dao = new SistemaJpaController(empresa);
            List<Sistema> sis = dao.findSistemaEntities();
            JSONArray array = new JSONArray();
            for (Sistema si : sis) {
                if(!(si.getSiscod()==1 ||  si.getSiscod()==2 )){
                JSONObject obj = new JSONObject();
                obj.put("siscod", si.getSiscod());
                obj.put("sisent", si.getSisent());
                array.put(obj);}
            }
            resp.getWriter().write(array.toString());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter()
                    .write(new JSONObject().put("error", "Error al obtener los datos: " + e.getMessage()).toString());
        }

    }

}
