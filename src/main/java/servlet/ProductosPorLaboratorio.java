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

import dao.FaProductosJpaController;
import dao.InventarioListaProductosJpaController;
import dao.MetodosAux;
import dto.InventarioListaProductos;

@WebServlet(name = "ProductosPorLaboratorio", urlPatterns = { "/productosPorLaboratorio" })
public class ProductosPorLaboratorio extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", 0);

        // Headers CORS para permitir peticiones desde el frontend
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");

        String opcionStr = req.getParameter("opcion");
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("empr") == null) {
            sendError(resp, "Sesión no válida o expirada");
            return;
        }

        String empr = session.getAttribute("empr").toString();

        try {
            int opcion = Integer.parseInt(opcionStr);
            PrintWriter out = resp.getWriter();

            switch (opcion) {
                case 1: // Obtener todos los laboratorios
                    MetodosAux daoMetodo = new MetodosAux(empr);
                    String labsStr = daoMetodo.obtenerTodosLosLaboratorios();

                    if (labsStr == null || labsStr.trim().isEmpty()) {
                        sendError(resp, "No se encontraron laboratorios");
                        return;
                    }

                    try {
                        JSONArray labs = new JSONArray(labsStr);
                        JSONObject allLabs = new JSONObject();
                        allLabs.put("laboratorios", labs);
                        out.print(allLabs.toString());
                    } catch (Exception e) {
                        sendError(resp, "Error al procesar laboratorios: " + e.getMessage());
                    }
                    break;

                case 2: // Obtener productos por laboratorio
                    FaProductosJpaController daoPro = new FaProductosJpaController(empr);
                    String laboratorioId = req.getParameter("laboratorioId");

                    if (laboratorioId == null || laboratorioId.isEmpty()) {
                        sendError(resp, "ID de laboratorio no especificado");
                        return;
                    }

                    String productosStr = daoPro.obtenerProductosPorLab(laboratorioId);

                    try {
                        JSONArray productos = new JSONArray(productosStr);
                        // Validar que sea un array de productos
                        if (productos.length() > 0 && !productos.getJSONObject(0).has("codigo")) {
                            sendError(resp, "Formato de productos inválido");
                            return;
                        }
                        out.print(productos.toString());
                    } catch (Exception e) {
                        sendError(resp, "Error al procesar productos: " + e.getMessage());
                    }
                    break;

                default:
                    sendError(resp, "Opción no válida");
                    break;
            }

            out.flush();
        } catch (NumberFormatException e) {
            sendError(resp, "El parámetro 'opcion' debe ser numérico");
        } catch (Exception e) {
            sendError(resp, "Error interno del servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");

        try {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("empr") == null) {
                sendError(resp, "Sesión no válida o expirada");
                return;
            }

            String empr = session.getAttribute("empr").toString();
            String laboratorioId = req.getParameter("laboratorioId");
            String codinvalmStr = req.getParameter("codinvalm");

            // Validaciones de parámetros
            if (laboratorioId == null || laboratorioId.isEmpty()) {
                sendError(resp, "ID de laboratorio no especificado");
                return;
            }

            if (codinvalmStr == null || codinvalmStr.isEmpty()) {
                sendError(resp, "ID de inventario no especificado");
                return;
            }

            int codinvalm;
            try {
                codinvalm = Integer.parseInt(codinvalmStr);
            } catch (NumberFormatException e) {
                sendError(resp, "ID de inventario debe ser numérico");
                return;
            }

            InventarioListaProductosJpaController daoInvPro = new InventarioListaProductosJpaController(empr);
            FaProductosJpaController daoPro = new FaProductosJpaController(empr);

            String productosStr = daoPro.obtenerProductosPorLab(laboratorioId);
            JSONArray productos = new JSONArray(productosStr);
            JSONObject response = new JSONObject();
            JSONArray errores = new JSONArray();
            int exitosos = 0;

            for (int i = 0; i < productos.length(); i++) {
                JSONObject producto = productos.getJSONObject(i);
                String codpro = producto.getString("codigo");

                if (codpro == null || codpro.isEmpty()) {
                    errores.put("Producto sin código en posición " + i);
                    continue;
                }

                // Verificar si ya existe la relación
                boolean existe = daoInvPro.existeDuo(codpro, codinvalm);
                if (existe) {
                    errores.put("El producto " + codpro + " ya está asociado a este inventario");
                    continue;
                }

                // Crear nueva relación
                InventarioListaProductos inventarioListaProductos = new InventarioListaProductos(codinvalm, codpro);

                try {
                    daoInvPro.create(inventarioListaProductos);
                    exitosos++;
                } catch (Exception e) {
                    errores.put("Error al asociar producto " + codpro + ": " + e.getMessage());
                }
            }

            // Construir respuesta
            response.put("success", exitosos > 0);
            response.put("exitosos", exitosos);
            response.put("errores", errores);
            response.put("message", exitosos + " productos asociados correctamente");
            response.put("total", productos.length());

            PrintWriter out = resp.getWriter();
            out.print(response.toString());
            out.flush();

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

        PrintWriter out = response.getWriter();
        out.print(errorResponse.toString());
        out.flush();
    }
}