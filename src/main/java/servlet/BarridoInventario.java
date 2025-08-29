package servlet;

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

import dao.FaProductosJpaController;
import dao.InventarioListaProductosJpaController;
import dao.MetodosBarrido;
import dto.InventarioListaProductos;

@WebServlet(name = "BarridoInventario", urlPatterns = { "/barrido" })
public class BarridoInventario extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("json/application");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();
        JSONObject jsonResponse = new JSONObject();

        try {

            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("empr") == null) {
                out.print("Sesión no válida o expirada");
                return;
            }

            String empr = session.getAttribute("empr").toString();
            MetodosBarrido metodosBarrido = new MetodosBarrido(empr);
            FaProductosJpaController dao = new FaProductosJpaController(empr);
            InventarioListaProductosJpaController daoInvLis = new InventarioListaProductosJpaController(empr);

            String codinvalmStr = req.getParameter("codinvalm");
            int codinvalm = Integer.parseInt(codinvalmStr);
            List<String> codpros = metodosBarrido.listaProductosActivos();

            int exitosos = 0;
            JSONArray errores = new JSONArray();
            for (String codpro : codpros) {
                if (!dao.getProductos(codpro)) {
                    errores.put("El producto " + codpro + " no existe");
                    continue;
                }

                if (daoInvLis.existeDuo(codpro, codinvalm)) {
                    errores.put("El producto " + codpro + " ya está asociado a este inventario");
                    continue;
                }

                InventarioListaProductos registro = new InventarioListaProductos(codinvalm, codpro);
                try {
                    daoInvLis.createOrSkip(registro);
                } catch (Exception e) {
                    errores.put("Error al asociar producto " + codpro + ": " + e.getMessage());
                }
                exitosos++;
            }

            jsonResponse.put("success", exitosos > 0);
            jsonResponse.put("errores", errores);
            jsonResponse.put("message", "Archivo procesado correctamente");
            jsonResponse.put("exitosos", exitosos);
            jsonResponse.put("total", codpros.size());
        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Error al procesar todos los productos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            out.print(jsonResponse.toString());
            out.flush();
        }
    }
}
