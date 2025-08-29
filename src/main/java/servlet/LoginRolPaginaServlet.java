package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.MetodoForLoginRol;

@WebServlet(name = "LoginRolPaginaServlet", urlPatterns = { "/loginrolpaginaservlet/*" })
public class LoginRolPaginaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();
        PrintWriter out = resp.getWriter();
        Object empre = session.getAttribute("empr");
        if (empre == null) {
            JSONObject obj = new JSONObject();
            obj.put("resultado", "no se reconoce la empresa");
            out.print(obj.toString());
            return;
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() < 1) {
            JSONObject obj = new JSONObject();
            obj.put("resultado", "error");
            out.print(obj.toString());
            return;
        }

        String rolId = pathInfo.substring(1);
        String empr = empre.toString();

        MetodoForLoginRol dao = new MetodoForLoginRol(empr);

        JSONArray paginasPermitidas = dao.obtenerPaginasPermitidas(rolId);

        out.print(paginasPermitidas.toString());

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            MetodoForLoginRol dao = new MetodoForLoginRol(empr);

            // Leer datos JSON del body
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = req.getReader().readLine()) != null) {
                sb.append(line);
            }
            JSONObject body = new JSONObject(sb.toString());

            String rolId = body.optString("codiRol", "");
            JSONArray permisosArray = body.optJSONArray("permisos");

            // Convertir JSONArray a List<Object[]>
            List<Object[]> permisos = new ArrayList<>();
            for (int i = 0; i < permisosArray.length(); i++) {
                JSONObject permiso = permisosArray.getJSONObject(i);
                String codiPagiStr = permiso.getString("codiPagi");
                int codiPagi = Integer.parseInt(codiPagiStr);
                String nombPagi = permiso.getString("nombPagi");
                int asigPerm = permiso.getInt("asigPerm");
                permisos.add(new Object[] { codiPagi, nombPagi, asigPerm });
            }

            dao.actualizarPermisos(rolId, permisos);

            JSONObject obj = new JSONObject();
            obj.put("success", true);
            obj.put("message", "Permisos actualizados correctamente");
            out.print(obj.toString());

        } catch (Exception e) {
            e.printStackTrace();
            JSONObject obj = new JSONObject();
            obj.put("success", false);
            obj.put("message", "Error al actualizar permisos");
            out.print(obj.toString());
        }
    }
}
