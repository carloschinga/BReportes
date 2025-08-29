package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.InventarioListaProductosJpaController;
import dto.InventarioListaProductos;

@WebServlet(name = "CRUDInventarioListaProductos", urlPatterns = { "/CRUDinvlispro" })
public class CRUDInventarioListaProductos extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        // Validar sesión
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("empr") == null) {
            sendError(resp, "Sesión no válida");
            return;
        }

        String empr = session.getAttribute("empr").toString();

        InventarioListaProductosJpaController dao = new InventarioListaProductosJpaController(empr);

        String opcionStr = req.getParameter("opcion");
        int opcion = Integer.parseInt(opcionStr);

        JSONObject jsonResponse = new JSONObject();

        switch (opcion) {
            case 1:
                String codinvalmStr = req.getParameter("codinvalm");
                int codinvalm = Integer.parseInt(codinvalmStr);
                String lista = dao.findProductosConNombreByInventario(codinvalm);
                JSONArray array = new JSONArray(lista);
                if (array.isEmpty()) {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "No se encontraron registros");
                } else {
                    jsonResponse.put("success", true);
                    jsonResponse.put("data", array);
                }
                break;
            case 2:
                String codinvStr = req.getParameter("codinv");
                int codinv = Integer.parseInt(codinvStr);
                boolean hasList = dao.isInventarioDireccionado(codinv);
                jsonResponse.put("success", hasList);
                break;
            default:
                jsonResponse.put("success", false);
                jsonResponse.put("message", "Opción no válida");
                break;
        }

        out.print(jsonResponse.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();
        JSONObject responseJson = new JSONObject();

        try {
            // Validar sesión
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("empr") == null) {
                sendError(resp, "Sesión no válida");
                return;
            }

            String empr = session.getAttribute("empr").toString();

            // Leer el cuerpo de la petición JSON
            String requestBody = req.getReader().lines().collect(Collectors.joining());
            JSONObject requestData = new JSONObject(requestBody);

            // Obtener parámetros
            String codinvalmStr = requestData.optString("codinvalm", "");
            JSONArray codigosArray = requestData.optJSONArray("codigos");

            // Validar parámetros
            if (codinvalmStr.isEmpty() || codigosArray == null || codigosArray.length() == 0) {
                sendError(resp, "Parámetros incompletos");
                return;
            }

            // Convertir codinvcab a int
            int codinvalm;
            try {
                codinvalm = Integer.parseInt(codinvalmStr);
            } catch (NumberFormatException e) {
                sendError(resp, "Formato inválido para codinvcab. Debe ser un número.");
                return;
            }

            // Procesar cada producto
            InventarioListaProductosJpaController dao = new InventarioListaProductosJpaController(empr);

            JSONArray errores = new JSONArray();
            int exitosos = 0;

            for (int i = 0; i < codigosArray.length(); i++) {
                String codpro = codigosArray.optString(i, "").trim();
                if (codpro.isEmpty())
                    continue;

                boolean existe = dao.existeDuo(codpro, codinvalm);

                if (existe) {
                    errores.put("El producto " + codpro + " ya existe en la tabla de inventario");
                    continue;
                }

                try {
                    InventarioListaProductos relacion = new InventarioListaProductos(codinvalm,
                            codpro);
                    dao.create(relacion);
                    exitosos++;
                } catch (Exception e) {
                    errores.put("Error al asociar producto " + codpro + ": " + e.getMessage());
                }
            }

            responseJson.put("success", exitosos > 0);
            responseJson.put("exitosos", exitosos);
            responseJson.put("errores", errores);
            responseJson.put("message", exitosos + " productos asociados correctamente");
            responseJson.put("total", codigosArray.length());

            out.print(responseJson.toString());

        } catch (Exception e) {
            e.printStackTrace();
            sendError(resp, "Error en el servidor: " + e.getMessage());
        }
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        JSONObject errorResponse = new JSONObject();
        errorResponse.put("success", false);
        errorResponse.put("error", message);
        response.getWriter().print(errorResponse.toString());
    }
}