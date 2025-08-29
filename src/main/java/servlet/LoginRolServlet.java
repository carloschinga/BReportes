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

@WebServlet(name = "LoginRolServlet", urlPatterns = { "/loginrolservlet" })
public class LoginRolServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            HttpSession session = req.getSession();
            Object empre = session.getAttribute("empr");
            if (empre == null) {
                JSONObject obj = new JSONObject();
                obj.put("resultado", "error");
                out.print(obj.toString());
                return;
            }

            String empr = empre.toString();

            MetodoForLoginRol metodo = new MetodoForLoginRol(empr);

            JSONArray data = metodo.getRols();
            out.print(data.toString());

        } catch (Exception e) {
            JSONObject obj = new JSONObject();
            obj.put("resultado", "error");
            obj.put("mensaje", "Error al obtener los roles");
            out.print(obj.toString());
        }
    }
}
