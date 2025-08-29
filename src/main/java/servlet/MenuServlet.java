package servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.MetodoForLoginRol;

@WebServlet(name = "MenuServlet", urlPatterns = { "/menu" })
public class MenuServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();
        Object empre = session.getAttribute("empr");
        Object grucod = session.getAttribute("grucod");

        if (empre == null || grucod == null) {
            resp.getWriter().write("<p>Error: Usuario no autenticado.</p>");
            return;
        }

        String codgru = grucod.toString();
        String empr = empre.toString();

        MetodoForLoginRol dao = new MetodoForLoginRol(empr);

        JSONArray listadepaginas = dao.obtenerPaginasPorGrucod(codgru);

        String menuHtml = generarMenuHtml(listadepaginas);
        resp.getWriter().write(menuHtml);
    }

    private String generarMenuHtml(JSONArray listadepaginas) {
        StringBuilder html = new StringBuilder();
        html.append("<ul class='navbar-nav' id='menuDinamico'>");

        Map<Integer, List<JSONObject>> mapaPaginas = new HashMap<Integer, List<JSONObject>>();
        for (int i = 0; i < listadepaginas.length(); i++) {
            JSONObject obj = listadepaginas.getJSONObject(i);
            int key = obj.getInt("padre");
            if (!mapaPaginas.containsKey(key)) {
                mapaPaginas.put(key, new ArrayList<JSONObject>());
            }
            mapaPaginas.get(key).add(obj);
        }

        List<JSONObject> menusPrincipales = mapaPaginas.get(0);
        if (menusPrincipales != null) {
            for (int i = 0; i < menusPrincipales.size(); i++) {
                JSONObject menu = menusPrincipales.get(i);
                html.append(generarMenuItem(menu, mapaPaginas, true));
            }
        }
        html.append("</ul>");
        return html.toString();
    }

    private String generarMenuItem(JSONObject menu, Map<Integer, List<JSONObject>> mapaPaginas, boolean esPrincipal) {

        StringBuilder html = new StringBuilder();

        if (menu.getString("ruta") != null && menu.getString("ruta").equalsIgnoreCase("hr")) {
            html.append("<hr/>");
        }

        List<JSONObject> hijos = mapaPaginas.get(menu.getInt("codiPagi"));
        boolean tieneHijos = hijos != null && hijos.size() > 0;

        if (esPrincipal) {
            // Elemento de menú principal (nivel raíz)\n
            html.append("<li class='nav-item dropdown'>");
            html.append("<a class='nav-link dropdown-toggle' href='#' id='menu")
                    .append(menu.getInt("codiPagi"))
                    .append("' role='button' data-bs-toggle='dropdown' >")
                    .append(menu.getString("nombPagi"))
                    .append("</a>");
            if (tieneHijos) {
                html.append(
                        "<div class='dropdown-menu dropdown-menu-left shadow animated--grow-in' aria-labelledby='menu")
                        .append(menu.getInt("codiPagi"))
                        .append("'>");
                for (int i = 0; i < hijos.size(); i++) {
                    JSONObject hijo = hijos.get(i);
                    html.append(generarMenuItem(hijo, mapaPaginas, false));
                }
                html.append("</div>");
            }
            html.append("</li>");
        } else {
            // Elemento de submenú\n
            if (tieneHijos) {
                // Si tiene hijos, se genera un dropdown-submenu\n
                html.append("<div class='dropdown-submenu'>");
                html.append("<a class='dropdown-item dropdown-toggle' href='#' data-toggle='dropdown'>")
                        .append(menu.getString("nombPagi"))
                        .append(" <i class='fas fa-chevron-right'></i>") // Icono de flecha cerrada\n")
                        .append("</a>");
                html.append("<div class='dropdown-menu'>");
                for (int i = 0; i < hijos.size(); i++) {
                    JSONObject nieto = hijos.get(i);
                    html.append(generarMenuItem(nieto, mapaPaginas, false));
                }
                html.append("</div>");
                html.append("</div>");
            } else {// Si no tiene hijos, es un enlace simple\n");
                html.append("<a class='dropdown-item' href='#' data-page='")
                        .append(menu.getString("ruta"))
                        .append("'>")
                        .append(menu.getString("nombPagi"))
                        .append("</a>");
            }
        }

        return html.toString();
    }
}
