package pe.bartolito.conta.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pe.bartolito.conta.dao.ClasificadoCentroCostoDao;

@WebServlet(name = "ClasifiCentCostServlet", urlPatterns = { "/clasiCentCost" })
public class ClasifiCentCostServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();
        ClasificadoCentroCostoDao dao = new ClasificadoCentroCostoDao("e");
        JSONObject response = new JSONObject();

        try {
            String data = dao.getRegistrosJson();
            JSONArray dataJson = new JSONArray(data);
            response.put("resultado", "ok");
            response.put("mensaje", "Datos obtenidos exitosamente");
            response.put("data", dataJson);
            out.print(response.toString());
        } catch (Exception e) {
            response.put("resultado", "error");
            response.put("mensaje", "Error al obtener datos: " + e.getMessage());
            out.print(response.toString());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();
        ClasificadoCentroCostoDao dao = new ClasificadoCentroCostoDao("e");
        JSONObject response = new JSONObject();

        try {
            // Leer el cuerpo de la solicitud
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject requestJson = new JSONObject(sb.toString());

            // Validar campos obligatorios
            if (!requestJson.has("clasificadores")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.put("resultado", "error");
                response.put("mensaje", "Faltan campos obligatorios");
                out.print(response.toString());
                return;
            }

            JSONArray clasificadores = requestJson.getJSONArray("clasificadores");

            // Validar que haya al menos un clasificador
            if (clasificadores.length() == 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.put("resultado", "error");
                response.put("mensaje", "Debe proporcionar al menos un clasificador");
                out.print(response.toString());
                return;
            }

            // Validar campos obligatorios para cada clasificador
            for (int i = 0; i < clasificadores.length(); i++) {
                JSONObject clasificador = clasificadores.getJSONObject(i);
                if (!clasificador.has("UnidOperaId") || !clasificador.has("TipoGastoId")
                        || !clasificador.has("Cuenta")) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.put("resultado", "error");
                    response.put("mensaje", "Faltan campos obligatorios en el clasificador " + (i + 1));
                    out.print(response.toString());
                    return;
                }
            }

            String resultado = dao.crearMultiplesClasificadores(requestJson.toString());

            if ("S".equals(resultado)) {
                response.put("resultado", "ok");
                response.put("mensaje", "Clasificador creado exitosamente");
                out.print(response.toString());
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.put("resultado", "error");
                response.put("mensaje", resultado);
                out.print(response.toString());
            }
        } catch (JSONException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.put("resultado", "error");
            response.put("mensaje", "Error en el formato JSON: " + e.getMessage());
            out.print(response.toString());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.put("resultado", "error");
            response.put("mensaje", "Error interno del servidor: " + e.getMessage());
            out.print(response.toString());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();
        ClasificadoCentroCostoDao dao = new ClasificadoCentroCostoDao("e");
        JSONObject response = new JSONObject();

        try {
            // Leer el cuerpo de la solicitud
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject requestJson = new JSONObject(sb.toString());

            // Validar que el JSON tenga la estructura esperada
            if (!requestJson.has("clasificadores")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.put("resultado", "error");
                response.put("mensaje", "Estructura JSON incorrecta. Se espera un array 'clasificadores'");
                out.print(response.toString());
                return;
            }

            JSONArray clasificadores = requestJson.getJSONArray("clasificadores");

            // Validar que haya al menos un clasificador
            if (clasificadores.length() == 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.put("resultado", "error");
                response.put("mensaje", "Debe proporcionar al menos un clasificador");
                out.print(response.toString());
                return;
            }

            // Validar campos obligatorios para cada clasificador
            for (int i = 0; i < clasificadores.length(); i++) {
                JSONObject clasificador = clasificadores.getJSONObject(i);
                if (!clasificador.has("ClasiUnidOperaId") || !clasificador.has("UnidOperaId") ||
                        !clasificador.has("TipoGastoId") || !clasificador.has("Cuenta")) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.put("resultado", "error");
                    response.put("mensaje", "Faltan campos obligatorios en el clasificador " + (i + 1));
                    out.print(response.toString());
                    return;
                }
            }

            // Crear el JSON para el DAO
            JSONObject daoRequest = new JSONObject();
            daoRequest.put("clasificadores", clasificadores);

            String resultado = dao.actualizarMultiplesClasificadores(daoRequest.toString());

            if ("S".equals(resultado)) {
                response.put("resultado", "ok");
                response.put("mensaje", "Clasificadores actualizados exitosamente");
                out.print(response.toString());
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.put("resultado", "error");
                response.put("mensaje", "Error al actualizar clasificadores");
                out.print(response.toString());
            }
        } catch (JSONException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.put("resultado", "error");
            response.put("mensaje", "Error en el formato JSON: " + e.getMessage());
            out.print(response.toString());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.put("resultado", "error");
            response.put("mensaje", "Error interno del servidor: " + e.getMessage());
            out.print(response.toString());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();
        ClasificadoCentroCostoDao dao = new ClasificadoCentroCostoDao("e");
        JSONObject response = new JSONObject();

        try {
            // Leer el cuerpo de la solicitud
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject requestJson = new JSONObject(sb.toString());

            // Validar que el JSON tenga la estructura esperada
            if (!requestJson.has("ids")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.put("resultado", "error");
                response.put("mensaje", "Estructura JSON incorrecta. Se espera un array 'ids'");
                out.print(response.toString());
                return;
            }

            JSONArray ids = requestJson.getJSONArray("ids");

            // Validar que haya al menos un ID
            if (ids.length() == 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.put("resultado", "error");
                response.put("mensaje", "Debe proporcionar al menos un ID para eliminar");
                out.print(response.toString());
                return;
            }

            // Crear el JSON para el DAO
            JSONObject daoRequest = new JSONObject();
            daoRequest.put("ids", ids);

            String resultado = dao.eliminarMultiplesClasificadores(daoRequest.toString());

            if ("S".equals(resultado)) {
                response.put("resultado", "ok");
                response.put("mensaje", "Clasificadores eliminados exitosamente");
                out.print(response.toString());
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.put("resultado", "error");
                response.put("mensaje", "Error al eliminar clasificadores");
                out.print(response.toString());
            }
        } catch (JSONException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.put("resultado", "error");
            response.put("mensaje", "Error en el formato JSON: " + e.getMessage());
            out.print(response.toString());
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.put("resultado", "error");
            response.put("mensaje", "Error interno del servidor: " + e.getMessage());
            out.print(response.toString());
        }
    }
}
