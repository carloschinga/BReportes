/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.CigoDAO;
import dao.HechCuotaVentaJpaController;
import dto.HechCuotaVenta;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
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

/**
 *
 * @author shaho
 */
@WebServlet(name = "CRUDsigoobje", urlPatterns = { "/CRUDsigoobje" })
public class CRUDsigoobje extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            try {
                request.setCharacterEncoding("UTF-8");
                HttpSession session = request.getSession(true);
                Object emprObj = session.getAttribute("empr");
                if (emprObj != null) {
                    String opcion = request.getParameter("opcion");
                    HechCuotaVentaJpaController dao = new HechCuotaVentaJpaController("d");

                    CigoDAO sistemaDao = new CigoDAO("a");

                    switch (opcion) {
                        case "1":// listar
                            out.print("{\"resultado\":\"ok\",\"data\":" + dao.listarJson() + "}");
                            break;
                        case "2":// listar para los checkbox
                            StringBuilder res = new StringBuilder("{\"resultado\":\"ok\",\"lab\":");
                            int codobj = Integer.parseInt(request.getParameter("codobj"));
                            res.append("}");
                            out.print(res.toString());
                            break;
                        case "3":// agregar
                            String tipo = request.getParameter("tipo");
                            String periodo = request.getParameter("periodo");
                            String mesano = request.getParameter("mesano");

                            StringBuilder sb = new StringBuilder();
                            try (BufferedReader reader = request.getReader()) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            }
                            String body = sb.toString();
                            JSONObject json = new JSONObject(body);
                            String data = json.get("nomb").toString();
                            codobj = dao.obtenerUltInvnum() + 1;
                            if (codobj != 0) {
                                HechCuotaVenta obj = new HechCuotaVenta(codobj);
                                obj.setDesobj(data);
                                obj.setFeccre(new Date());
                                obj.setTipo(tipo);
                                obj.setMesano(periodo);
                                String codi = session.getAttribute("codi").toString();
                                obj.setUsecod(Integer.parseInt(codi));
                                obj.setEstado("S");
                                obj.setHecAct(false);
                                dao.create(obj);

                                // crear array de sucursales
                                JSONArray array = new JSONArray();
                                array.put("3");
                                array.put("4");
                                array.put("5");
                                array.put("6");
                                array.put("7");
                                array.put("8");
                                array.put("9");
                                array.put("10");
                                array.put("11");
                                array.put("12");

                                String arrStr = array.toString();

                                String resultado = dao.actualizar_productos(arrStr, codobj);

                                if ("DUPLICADO".equals(resultado)) {
                                    out.print("{\"resultado\":\"error\",\"mensaje\":\"duplicado\"}");
                                } else if ("E".equals(resultado)) {
                                    out.print("{\"resultado\":\"error\",\"mensaje\":\"procedimiento\"}");
                                } else if ("OK".equals(resultado)) {
                                    out.print("{\"resultado\":\"ok\",\"codobj\":" + codobj + "}");
                                } else {
                                    out.print("{\"resultado\":\"error\",\"mensaje\":\"no se creo la lista\"}");
                                }
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"consulta\"}");
                            }
                            break;
                        case "4":
                            codobj = Integer.parseInt(request.getParameter("codobj"));
                            HechCuotaVenta obj = dao.findHechCuotaVenta(codobj);
                            if (obj == null) {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"noencontrado\"}");
                            } else {
                                obj.setEstado("N");
                                obj.setFecumv(new Date());
                                String codi = session.getAttribute("codi").toString();
                                obj.setUsecod(Integer.parseInt(codi));
                                dao.actualizar_productos(codobj, "producto", "[]", null);
                                dao.edit(obj);
                                out.print("{\"resultado\":\"ok\"}");
                            }
                            break;
                        case "5":// vista previa
                            sb = new StringBuilder();
                            try (BufferedReader reader = request.getReader()) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            }
                            body = sb.toString();
                            json = new JSONObject(body);

                            out.print(
                                    "{\"resultado\":\"ok\",\"data\":"
                                            + dao.vista_previa(json.get("lab").toString(), json.get("tip").toString(),
                                                    json.get("estra").toString(), json.get("estrasolo").toString())
                                            + "}");
                            break;
                        case "6":// modificar
                            tipo = request.getParameter("tipo");
                            periodo = request.getParameter("periodo");
                            mesano = periodo.replace("-", "");
                            sb = new StringBuilder();
                            try (BufferedReader reader = request.getReader()) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            }
                            body = sb.toString();
                            json = new JSONObject(body);
                            data = json.get("nomb").toString();
                            codobj = Integer.parseInt(request.getParameter("codobj"));
                            if (codobj != 0) {
                                obj = dao.findHechCuotaVenta(codobj);
                                obj.setDesobj(data);
                                obj.setFecumv(new Date());
                                obj.setTipo(tipo);
                                obj.setMesano(periodo);
                                String codi = session.getAttribute("codi").toString();
                                obj.setUsecod(Integer.parseInt(codi));
                                obj.setEstado("S");
                                dao.edit(obj);

                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"consulta\"}");
                            }
                            break;
                        case "7":
                            // buscar hech cuota
                            codobj = Integer.parseInt(request.getParameter("codobj"));
                            obj = dao.findHechCuotaVenta(codobj);
                            if (obj != null) {
                                json = new JSONObject();
                                json.put("resultado", "ok");
                                json.put("codobj", codobj);
                                json.put("desobj", obj.getDesobj());
                                json.put("periodo", obj.getMesano());
                                json.put("tipo", obj.getTipo());
                                out.print(json.toString());
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"noencontrado\"}");
                            }
                            break;
                        case "8":// listar establecimientos monto
                            try {
                                codobj = Integer.parseInt(request.getParameter("codobj"));

                                // Obtener datos de sistemas (Listado)
                                String jsonSistemas = sistemaDao.Listado();
                                JSONArray sistemasArray = new JSONArray(jsonSistemas);

                                // Obtener datos de montos
                                String jsonMontos = dao.listarsiscodmontos(codobj);
                                JSONArray montosArray = new JSONArray(jsonMontos);

                                // Crear mapa de montos por ID para búsqueda rápida
                                Map<Integer, JSONObject> montosMap = new HashMap<>();
                                for (int i = 0; i < montosArray.length(); i++) {
                                    JSONObject montoObj = montosArray.getJSONObject(i);
                                    montosMap.put(montoObj.getInt("id"), montoObj);
                                }

                                // Combinar los datos
                                JSONArray resultado = new JSONArray();
                                for (int i = 0; i < sistemasArray.length(); i++) {
                                    JSONObject sistemaObj = sistemasArray.getJSONObject(i);
                                    int codigo = sistemaObj.getInt("codigo");

                                    if (montosMap.containsKey(codigo)) {
                                        JSONObject montoObj = montosMap.get(codigo);

                                        // Crear objeto combinado
                                        JSONObject combinado = new JSONObject();
                                        combinado.put("codigo", codigo);
                                        combinado.put("nombre", sistemaObj.getString("nombre"));
                                        combinado.put("monto", montoObj.get("monto"));
                                        combinado.put("cantidad", montoObj.get("cantidad"));
                                        combinado.put("sumsoles", montoObj.get("sumsoles"));
                                        combinado.put("sumentero", montoObj.get("sumentero"));
                                        combinado.put("porc_estra", montoObj.get("porc_estra"));
                                        resultado.put(combinado);
                                    }
                                }

                                out.print("{\"resultado\":\"ok\",\"data\":" + resultado.toString() + "}");

                            } catch (Exception e) {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"" + e.getMessage() + "\"}");
                            }
                            break;
                        case "9":// modificar montos
                            tipo = request.getParameter("tipo");

                            sb = new StringBuilder();
                            try (BufferedReader reader = request.getReader()) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            }
                            body = sb.toString();
                            codobj = Integer.parseInt(request.getParameter("codobj"));
                            if (codobj != 0) {
                                String respuesta = dao.editarValoresObjetosSoles(codobj, tipo, body);
                                if (respuesta.equals("S")) {
                                    out.print("{\"resultado\":\"ok\"}");
                                } else {
                                    out.print("{\"resultado\":\"error\",\"mensaje\":\"procedimiento\"}");
                                }
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"consulta\"}");
                            }
                            break;
                        case "10":// agregar solo productos
                            tipo = request.getParameter("tipo");
                            periodo = request.getParameter("periodo");
                            mesano = periodo.replace("-", "");

                            sb = new StringBuilder();
                            try (BufferedReader reader = request.getReader()) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            }
                            body = sb.toString();
                            json = new JSONObject(body);
                            data = json.get("nomb").toString();
                            codobj = dao.obtenerUltInvnum() + 1;
                            if (codobj != 0) {
                                obj = new HechCuotaVenta(codobj);
                                obj.setDesobj(data);
                                obj.setFeccre(new Date());
                                obj.setTipo(tipo);
                                obj.setMesano(periodo);
                                String codi = session.getAttribute("codi").toString();
                                obj.setUsecod(Integer.parseInt(codi));
                                obj.setEstado("S");

                                data = json.get("producto").toString();
                                String result = dao.actualizar_productos(codobj, "producto", data, null);
                                if ("N".equals(result)) {
                                    out.print("{\"resultado\":\"error\",\"mensaje\":\"yaexistecodpro\"}");
                                } else {
                                    dao.create(obj);
                                    data = json.get("esta").toString();
                                    dao.actualizar_productos(codobj, "estab", data, mesano);
                                    out.print("{\"resultado\":\"ok\",\"codobj\":" + codobj + "}");
                                }
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"consulta\"}");
                            }
                            break;
                        case "11":// modificar producto especifico
                            tipo = request.getParameter("tipo");
                            periodo = request.getParameter("periodo");
                            mesano = periodo.replace("-", "");

                            sb = new StringBuilder();
                            try (BufferedReader reader = request.getReader()) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            }
                            body = sb.toString();
                            json = new JSONObject(body);
                            data = json.get("nomb").toString();
                            codobj = Integer.parseInt(request.getParameter("codobj"));
                            if (codobj != 0) {
                                obj = dao.findHechCuotaVenta(codobj);
                                obj.setDesobj(data);
                                obj.setFecumv(new Date());
                                obj.setTipo(tipo);
                                obj.setMesano(periodo);
                                String codi = session.getAttribute("codi").toString();
                                obj.setUsecod(Integer.parseInt(codi));
                                obj.setEstado("S");

                                data = json.get("producto").toString();
                                String result = dao.actualizar_productos(codobj, "producto", data, null);
                                if ("N".equals(result)) {
                                    out.print("{\"resultado\":\"error\",\"mensaje\":\"yaexistecodpro\"}");
                                } else if ("E".equals(result)) {
                                    out.print("{\"resultado\":\"error\",\"mensaje\":\"error\"}");
                                } else {
                                    dao.edit(obj);
                                    dao.actualizar_productos(codobj, "lab", "[]", null);
                                    dao.actualizar_productos(codobj, "tip", "[]", null);
                                    data = json.get("esta").toString();
                                    dao.actualizar_productos(codobj, "estab", data, mesano);
                                    dao.actualizar_productos(codobj, "estra", "[]", null);
                                    dao.actualizar_productos(codobj, "estrasolo", "[]", null);
                                    out.print("{\"resultado\":\"ok\"}");
                                }
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"consulta\"}");
                            }
                            break;
                        case "12":// listar productos
                            codobj = Integer.parseInt(request.getParameter("codobj"));
                            out.print("{\"resultado\":\"ok\",\"data\":" + dao.listarSoloProductosJson(codobj)
                                    + ",\"detalle\":" + dao.listarSoloProductosJsonmontosesta(codobj)
                                    + ",\"farmacias\":" + dao.listarSolosiscodJson(codobj) + "}");
                            break;
                        case "13":// modificar montos especifico
                            tipo = request.getParameter("tipo");

                            sb = new StringBuilder();
                            try (BufferedReader reader = request.getReader()) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            }
                            body = sb.toString();
                            codobj = Integer.parseInt(request.getParameter("codobj"));
                            if (codobj != 0) {
                                String respuesta = dao.editarValoresObjetosSolesEspecifico(codobj, tipo, body);
                                if (respuesta.equals("S")) {
                                    out.print("{\"resultado\":\"ok\"}");
                                } else {
                                    out.print("{\"resultado\":\"error\",\"mensaje\":\"procedimiento\"}");
                                }
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"consulta\"}");
                            }
                            break;
                        case "14":
                            codobj = Integer.parseInt(request.getParameter("codobj"));
                            String resultado = dao.activarHechCuotaVenta(codobj);
                            if ("OK".equals(resultado)) {
                                out.print("{\"resultado\":\"ok\"}");
                            } else if ("E".equals(resultado)) {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"consulta\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"no se activa\"}");
                            }
                            break;
                        default:
                            out.print("{\"resultado\":\"error\",\"mensaje\":\"noproce\"}");
                            break;
                    }
                } else {
                    out.print("{\"resultado\":\"error\",\"mensaje\":\"nosession\"}");
                }
            } catch (Exception e) {
                out.print("{\"resultado\":\"error\",\"mensaje\":\"errorgeneral\"}");
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}