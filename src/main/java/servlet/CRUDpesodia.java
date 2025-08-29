/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import dao.FaObjventaPesodiaJpaController;
import dao.ObjesiscodJpaController;
import dao.ObjeventasJpaController;
import dao.SistemaJpaController;
import dao.exceptions.NonexistentEntityException;
import dto.FaObjventaPesodia;
import dto.Objesiscod;
import dto.Objeventas;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author sbdeveloperw
 */
@WebServlet(name = "CRUDpesodia", urlPatterns = {"/CRUDpesodia"})
public class CRUDpesodia extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        HttpSession session = request.getSession(false);
        JSONObject respuesta = new JSONObject();

        try (PrintWriter out = response.getWriter()) {
            int opcion = Integer.parseInt(request.getParameter("opcion"));
            String empresa = (String) session.getAttribute("empresa");
            int userId = (int) session.getAttribute("idusuario");

            FaObjventaPesodiaJpaController controller = new FaObjventaPesodiaJpaController(empresa);
            ObjesiscodJpaController farmaCtrl = new ObjesiscodJpaController(empresa);

            switch (opcion) {
                case 1: // Listar registros por codobj y siscod
                    int codobj = Integer.parseInt(request.getParameter("codobj"));
                    int siscod = Integer.parseInt(request.getParameter("siscod"));
                    List<FaObjventaPesodia> lista = controller.findByCodobjAndSiscod(codobj, siscod);
                    respuesta.put("lista", new JSONArray(lista));
                    break;

                case 2: // Guardar o actualizar lote
                    JSONArray datos = new JSONArray(request.getParameter("lista"));
                    List<FaObjventaPesodia> lote = new ArrayList<>();

                    for (int i = 0; i < datos.length(); i++) {
                        JSONObject obj = datos.getJSONObject(i);
                        FaObjventaPesodia po = new FaObjventaPesodia();

                        po.setCodobj(obj.getInt("codobj"));
                        po.setSiscod(obj.getInt("siscod"));
                        po.setNumdia(obj.getInt("numdia"));
                        po.setPeso(obj.getInt("peso"));
                        po.setUsemod(userId);

                        lote.add(po);
                    }

                    controller.guardarOActualizarLote(lote);
                    respuesta.put("mensaje", "Guardado correctamente");
                    break;

                case 3: // Eliminar (soft delete)
                    int id = Integer.parseInt(request.getParameter("id"));
                    controller.softDelete(id, userId);
                    respuesta.put("mensaje", "Registro eliminado");
                    break;

                case 4: // Buscar monto asignado
                    try {
                        int idObjetivo = Integer.parseInt(request.getParameter("idObjetivo"));
                        int idFarmacia = Integer.parseInt(request.getParameter("idFarmacia"));

                        String montoAsignado = farmaCtrl.buscarMontoAsignadoJson(idObjetivo, idFarmacia);

                        if (montoAsignado.equals("-1")) {
                            respuesta.put("resultado", "error");
                            respuesta.put("mensaje", "No se pudo encontrar el monto asignado");
                        } else {
                            double monto = Double.parseDouble(montoAsignado);
                            respuesta.put("resultado", "ok");
                            respuesta.put("monto", monto);
                        }
                    } catch (Exception e) {
                        respuesta.put("resultado", "error");
                        respuesta.put("mensaje", "Excepción al obtener el monto: " + e.getMessage());
                    }
                    break;

                default:
                    respuesta.put("error", "Opción no válida");
            }

            out.print(respuesta.toString());
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", e.getMessage());
            response.getWriter().print(errorJson.toString());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "CRUD servlet para fa_objventa_pesodia";
    }
}
