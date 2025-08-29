package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.CigoDAO;
import dao.MetodosRolObje;

@WebServlet(name = "CRUDrolobje", urlPatterns = { "/CRUDrolobje" })
public class CRUDrolobje extends HttpServlet {

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("json/application");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();
        JSONObject response = new JSONObject();
        try {
            String opcion = req.getParameter("opcion");

            MetodosRolObje rolObjeDao = new MetodosRolObje("d");
            CigoDAO cidoDao = null;
            switch (opcion) {
                case "1":
                    String codobjStr = req.getParameter("codobj");
                    int codobj = Integer.parseInt(codobjStr);
                    String json = rolObjeDao.getObjetivo(codobj);
                    JSONObject objetivo = new JSONObject(json);
                    response.put("resultado", "ok");
                    response.put("nombre", objetivo.getString("nombre"));
                    response.put("tipo", objetivo.getString("tipo"));
                    // obtener mesano que es el periodo y separar por "-"
                    String mesano = objetivo.getString("mesano").toString();
                    String[] mesanoSplit = mesano.split("-");
                    // convertir mesanoSplit[0] a int
                    int mes = Integer.parseInt(mesanoSplit[1]);
                    // obtener el nombre del mes
                    String[] meses = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto",
                            "Septiembre", "Octubre", "Noviembre", "Diciembre" };
                    response.put("mes", meses[mes - 1]);
                    response.put("ano", mesanoSplit[0]);
                    response.put("feccre", objetivo.getString("feccre"));
                    response.put("montototal", objetivo.getDouble("montototal"));
                    out.print(response.toString());
                    break;
                case "2":
                    String arrayRoles = rolObjeDao.listarRoles();
                    JSONArray roles = new JSONArray(arrayRoles);
                    response.put("resultado", "ok");
                    response.put("roles", roles);
                    out.print(response.toString());
                    break;
                case "3": // ? para obtener roles asignados de acuerdo al sucursal
                    // parsemos el vtaMesId
                    String VtaMesId = req.getParameter("VtaMesId");
                    int vtaMesId = Integer.parseInt(VtaMesId);

                    // obtener roles asignados al objetivo
                    String rolesAsignados = rolObjeDao.listarRolesAsignados(vtaMesId);

                    // parseamos los roles asignados a JSONArray
                    JSONArray rolesAsignadosJson = new JSONArray(rolesAsignados);

                    if (rolesAsignadosJson.length() == 0) {
                        // traemos la info de monto de la sucursal
                        String montoSucursal = rolObjeDao.obtenerMontoSucursal(vtaMesId);
                        JSONArray montoSucursalJson = new JSONArray(montoSucursal);
                        response.put("mensaje", "No hay roles asignados para esta sucursal");
                        response.put("roles", montoSucursalJson);
                    } else {
                        response.put("roles", rolesAsignadosJson);
                    }

                    response.put("resultado", "ok");
                    out.print(response.toString());
                    break;
                case "4":
                    // para combo de sucursales
                    cidoDao = new CigoDAO("a");
                    // obtener los nombre de las sucursales
                    String listado = cidoDao.Listado();

                    // parsemos el codobj
                    codobjStr = req.getParameter("codobj");
                    codobj = Integer.parseInt(codobjStr);

                    // obtener el codigo de la sucursal con el codigo del HechCuotaVentaMes
                    String vtaMesSucur = rolObjeDao.listarSucursalesConMesId(codobj);

                    // convertimos ambos a JSONArray
                    JSONArray vtaMesSucurJson = new JSONArray(vtaMesSucur);
                    JSONArray listadoJson = new JSONArray(listado);

                    // Crear mapa de montos por ID para búsqueda rápida
                    Map<Integer, JSONObject> sucurMap = new HashMap<>();
                    for (int i = 0; i < vtaMesSucurJson.length(); i++) {
                        JSONObject sucurObj = vtaMesSucurJson.getJSONObject(i);
                        sucurMap.put(sucurObj.getInt("SucurId"), sucurObj);
                    }

                    // Combinar los datos
                    JSONArray combinacion = new JSONArray();
                    for (int i = 0; i < listadoJson.length(); i++) {
                        JSONObject listadoObj = listadoJson.getJSONObject(i);
                        int codigo = listadoObj.getInt("codigo");

                        if (sucurMap.containsKey(codigo)) {
                            JSONObject sucurObj = sucurMap.get(codigo);

                            // Crear objeto combinado
                            JSONObject combinado = new JSONObject();
                            combinado.put("VtaMesId", sucurObj.getInt("VtaMesId"));
                            combinado.put("nombre", listadoObj.getString("nombre"));
                            combinacion.put(combinado);
                        }
                    }
                    response.put("resultado", "ok");
                    response.put("sucursales", combinacion);
                    out.print(response.toString());
                    break;
                case "5":
                    StringBuilder sb = new StringBuilder();
                    try (BufferedReader reader = req.getReader()) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                    }
                    String body = sb.toString();
                    JSONObject jsonData = new JSONObject(body);

                    // Procesamiento seguro (verifica si existe cada array)
                    if (jsonData.has("rolesEliminados")) {
                        JSONArray rolesEliminados = jsonData.getJSONArray("rolesEliminados");
                        if (rolesEliminados.length() > 0) {
                            rolObjeDao.deleteCuotRol(rolesEliminados);
                        }
                    }

                    if (jsonData.has("rolesParaAgregar")) {
                        JSONArray rolesParaAgregar = jsonData.getJSONArray("rolesParaAgregar");
                        if (rolesParaAgregar.length() > 0) {
                            rolObjeDao.createCuotRol(rolesParaAgregar);
                        }
                    }

                    if (jsonData.has("rolesParaEditar")) {
                        JSONArray rolesParaEditar = jsonData.getJSONArray("rolesParaEditar");
                        if (rolesParaEditar.length() > 0) {
                            rolObjeDao.editCuotRol(rolesParaEditar);
                        }
                    }

                    response.put("resultado", "ok");
                    out.print(response.toString());
                    break;
                case "6":
                    // ? opcion para obtener todos los vendedores
                    String arrayVendedores = rolObjeDao.listarVendedores();
                    JSONArray vendedores = new JSONArray(arrayVendedores);
                    response.put("resultado", "ok");
                    response.put("vendedores", vendedores);
                    out.print(response.toString());
                    break;
                case "7":
                    // ? opcion para traer los vendedores asignados a un rol
                    String vtaRolStr = req.getParameter("rolId");
                    int vtaRol = Integer.parseInt(vtaRolStr);
                    String arrayVendedoresAsignados = rolObjeDao.listarVendedoresAsignados(vtaRol);
                    JSONArray vendedoresAsignados = new JSONArray(arrayVendedoresAsignados);
                    response.put("resultado", "ok");
                    response.put("vendedores", vendedoresAsignados);
                    out.print(response.toString());
                    break;
                case "8":
                    sb = new StringBuilder();
                    try (BufferedReader reader = req.getReader()) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                    }
                    body = sb.toString();
                    jsonData = new JSONObject(body);

                    int cuotVentRol = jsonData.getInt("rolId");
                    // obtener el rolid de la cuota de venta rol
                    String rolIdStr = rolObjeDao.getRolIdFromCuotRol(cuotVentRol);
                    int rolId = Integer.parseInt(rolIdStr);

                    // Procesamiento seguro (verifica si existe cada array)
                    if (jsonData.has("vendedoresParaEliminar")) {
                        JSONArray rolesEliminados = jsonData.getJSONArray("vendedoresParaEliminar");
                        if (rolesEliminados.length() > 0) {
                            rolObjeDao.deleteCuotVend(rolesEliminados);
                        }
                    }

                    if (jsonData.has("vendedoresParaAgregar")) {
                        JSONArray rolesParaAgregar = jsonData.getJSONArray("vendedoresParaAgregar");
                        if (rolesParaAgregar.length() > 0) {
                            rolObjeDao.createCuotVendConSqlDate(rolesParaAgregar, rolId, cuotVentRol);
                        }
                    }

                    if (jsonData.has("vendedoresParaEditar")) {
                        JSONArray rolesParaEditar = jsonData.getJSONArray("vendedoresParaEditar");
                        if (rolesParaEditar.length() > 0) {
                            rolObjeDao.editCuotVend(rolesParaEditar);
                        }
                    }

                    response.put("resultado", "ok");
                    out.print(response.toString());

                    break;
                case "9":
                    HttpSession session = req.getSession();
                    String sucurIdStr = session.getAttribute("siscod").toString();
                    int sucurId = Integer.parseInt(sucurIdStr);
                    String userVendedor = session.getAttribute("username").toString();
                    String dataDashVendedor = rolObjeDao.getDataDashVendedor(sucurId, userVendedor);
                    JSONArray dataDashVendedorJson = new JSONArray(dataDashVendedor);
                    response.put("resultado", "ok");
                    response.put("data", dataDashVendedorJson);
                    out.print(response.toString());
                    break;
                default:
                    response.put("resultado", "error");
                    response.put("mensaje", "opcion no válida");
                    out.print(response.toString());
                    break;
            }

        } catch (Exception e) {
            response.put("resultado", "error");
            response.put("mensaje", e.getMessage());
            out.print(response.toString());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

}
