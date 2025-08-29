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
import dao.MetodosAux;

@WebServlet(name = "ServletProgreso", urlPatterns = { "/servletprogreso" })
public class ServletProgreso extends HttpServlet {

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession(true);
        JSONObject jsonResponse = new JSONObject();

        try {
            // Validar sesión
            if (session.getAttribute("empr") == null) {
                sendErrorResponse(out, "Sesión no válida",
                        HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String empr = session.getAttribute("empr").toString();
            MetodosAux dao = new MetodosAux(empr);

            // Obtener parámetro codinv
            int codinv;
            try {
                codinv = Integer.parseInt(req.getParameter("codinv"));
            } catch (NumberFormatException e) {
                sendErrorResponse(out, "Parámetro codinv no válido", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            JSONObject almacenes = dao.obtenerCodinvalmsSeparados(codinv);

            JSONArray codinvalms = almacenes.getJSONArray("codinvalms");

            boolean hasCaptura = dao.hasCaptura(codinv);

            JSONObject estadisticasGlobales = dao.obtenerEstadisticasGlobales(codinvalms, hasCaptura);

            int totalItemsContabilizados = estadisticasGlobales.getInt("totalContabilizados");
            int totalItemConStock = estadisticasGlobales.getInt("totalConStock");

            double progresoGlobal = (double) totalItemsContabilizados / (double) totalItemConStock * 100;
            progresoGlobal = Math.round(progresoGlobal * 100.0) / 100.0;

            JSONObject resultado = new JSONObject();
            resultado.put("progresoGlobal", progresoGlobal);
            resultado.put("totalItemsContabilizados", totalItemsContabilizados);
            resultado.put("totalItemConStock", totalItemConStock);
            jsonResponse = construirRespuestaExitosa(resultado);
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(out, "Error interno del servidor: " + e.getMessage(),
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        out.print(jsonResponse.toString());
    }

    private JSONObject construirRespuestaExitosa(JSONObject resultado) {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", true);
        jsonResponse.put("progresoGlobal", resultado.getDouble("progresoGlobal"));
        jsonResponse.put("totalItemsContabilizados", resultado.getInt("totalItemsContabilizados"));
        jsonResponse.put("totalItemConStock", resultado.getInt("totalItemConStock"));
        jsonResponse.put("message", "Progreso calculado exitosamente");
        return jsonResponse;
    }

    private void sendErrorResponse(PrintWriter out, String message, int statusCode) {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", false);
        jsonResponse.put("message", message);
        jsonResponse.put("statusCode", statusCode);
        out.print(jsonResponse.toString());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

}