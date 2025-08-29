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

import dao.DetalleInventarioDAO;
import dao.MetodosAux;

@WebServlet(name = "DetalleInventario", urlPatterns = { "/DetalleInventario" })
public class DetalleInventario extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession(true);
        JSONObject jsonDetalles = new JSONObject();

        try {
            if (session.getAttribute("empr") == null) {
                sendErrorResponse(out, "Sesión no válida", HttpServletResponse.SC_UNAUTHORIZED);
                return;

            }
            String empr = session.getAttribute("empr").toString();
            MetodosAux metDao = new MetodosAux(empr);
            DetalleInventarioDAO detInvDao = new DetalleInventarioDAO(empr);

            int codinv;
            try {
                codinv = Integer.parseInt(req.getParameter("codinv"));
            } catch (NumberFormatException e) {
                sendErrorResponse(out, "Parámetro codinv no válido", HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            // ? obtenemos todos los codinvalm del codinv
            JSONArray codinvalms = detInvDao.obtenerCodinvalmsSeparados(codinv);
            if (codinvalms.length() == 0) {
                sendErrorResponse(out, "No se encontraron almacenes para el codinv proporcionado",
                        HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // ? obtenemos el estado de la captura del codinv
            boolean tieneCaptura = metDao.hasCaptura(codinv);

            // ? obtenemos las estadisticas globales de este inventario
            JSONObject estadisticasGlobales = metDao.obtenerEstadisticasGlobales(codinvalms, tieneCaptura);
            int totalItemsContabilizados = estadisticasGlobales.getInt("totalContabilizados");
            int totalItemConStock = estadisticasGlobales.getInt("totalConStock");

            // ? calculamos el progreso global
            double progresoGlobal = (double) totalItemsContabilizados / (double) totalItemConStock * 100;
            progresoGlobal = Math.round(progresoGlobal * 100.0) / 100.0;

            // ? estadisticas globales
            jsonDetalles.put("totalContabilizados", totalItemsContabilizados);
            jsonDetalles.put("totalConStock", totalItemConStock);
            jsonDetalles.put("progresoGlobal", progresoGlobal);

            // ? obtenemos las farmacias
            JSONArray farmacias = detInvDao.obtenerFarmacias(codinv);
            jsonDetalles.put("totalFarmacias", farmacias.length());

            // ? procesamos los farmacias
            JSONArray estatsFarmacias = procesarFarmacias(farmacias, tieneCaptura, detInvDao, codinv);
            jsonDetalles.put("detalleFarmacias", estatsFarmacias);

            // ? procesamos los productos coincidentes por cada conteo
            JSONArray detallesIdenticos = detInvDao.detallesIdenticos(codinv, tieneCaptura);
            jsonDetalles.put("detalleIdenticos", detallesIdenticos);
            jsonDetalles.put("success", true);

        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(out, "Error interno del servidor: " + e.getMessage(),
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        out.print(jsonDetalles.toString());
    }

    private void sendErrorResponse(PrintWriter out, String message, int statusCode) {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", false);
        jsonResponse.put("message", message);
        jsonResponse.put("statusCode", statusCode);
        out.print(jsonResponse.toString());
    }

    public JSONArray procesarFarmacias(JSONArray farmacias, boolean tieneCaptura, DetalleInventarioDAO detInvDao,
            int codinv) {
        JSONArray resultado = new JSONArray();

        for (int i = 0; i < farmacias.length(); i++) {
            // ? parseamos el objeto a JSONObject
            JSONObject farmacia = farmacias.getJSONObject(i);
            // ? obtenemos el codalm y nombre
            String codalm = farmacia.getString("codalm");
            String nombre = farmacia.getString("nombre");
            // ? creamos el objeto farm que correspondera a la farmacia
            JSONObject farm = new JSONObject();
            // ? agregamos los datos de la farmacia
            farm.put("codalm", codalm);
            farm.put("nombreAlmacen", nombre);
            // ? obtener conteo por farmacia
            int conteos = detInvDao.obtenerConteosPorFarmacia(codalm, codinv);
            farm.put("totalConteos", conteos);
            // ? obtener total de productos por farmacia
            int totalProductos = detInvDao.obtenerTotalProductosPorFarmacia(codalm, codinv);
            farm.put("totalProductos", totalProductos);
            // ? obtener total de productos con stock por farmacia
            int totalProductosConStock = detInvDao.obtenerTotalProductosConStockPorFarmacia(codalm, codinv,
                    tieneCaptura);
            farm.put("totalProductosConStock", totalProductosConStock);
            // ? obtener total de productos contabilizados por farmacia
            int totalProductosContabilizados = detInvDao
                    .obtenerTotalProductosContabilizadosPorFarmacia(codalm, codinv, tieneCaptura);
            farm.put("totalProductosContabilizados", totalProductosContabilizados);
            // ? calculamos el progreso
            double progreso = (double) totalProductosContabilizados / (double) totalProductosConStock * 100;
            progreso = Math.round(progreso * 100.0) / 100.0;
            farm.put("progreso", progreso);
            // ? procesamos los conteos
            JSONArray conteosDetalle = detInvDao.detallesPorConteo(codinv, codalm, tieneCaptura);
            farm.put("conteos", conteosDetalle);
            // ? procesamos las categorias por farmacia
            JSONArray categorias = detInvDao.detallesPorCategoriaPorFarmacia(codalm, codinv, tieneCaptura);
            farm.put("categorias", categorias);

            resultado.put(farm);
        }
        return resultado;
    };

}
