package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.MetodoForLoginRol;

@WebServlet(name = "LoginPaginaServlet", urlPatterns = { "/loginpaginaservlet" })
public class LoginPaginaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();
        JSONArray data = new JSONArray();

        try {
            HttpSession session = req.getSession();
            Object empre = session.getAttribute("empr");
            if (empre == null) {
                JSONObject obj = new JSONObject();
                obj.put("resultado", "error");
                data.put(obj);
                return;
            }
            String empr = empre.toString();

            MetodoForLoginRol metodo = new MetodoForLoginRol(empr);
            data = metodo.getLoginPagina();

        } catch (Exception e) {
            JSONObject obj = new JSONObject();
            obj.put("resultado", "error");
            obj.put("mensaje", "Error al obtener los datos");
            out.print(obj.toString());
            e.printStackTrace();
        } finally {
            out.print(data.toString());
            out.flush();
            out.close();
        }
    }
}
