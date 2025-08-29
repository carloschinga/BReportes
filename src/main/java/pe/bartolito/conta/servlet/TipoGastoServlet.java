/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package pe.bartolito.conta.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.persistence.exceptions.JSONException;
import org.json.JSONObject;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import pe.bartolito.conta.dao.TipoGastoJpaController;
import pe.bartolito.conta.dto.TipoGasto;

/**
 *
 * @author sbdeveloperw
 */
@WebServlet(name = "TipoGastoServlet", urlPatterns = { "/tipogasto" })
public class TipoGastoServlet extends HttpServlet {

    private TipoGastoJpaController controller;

    @Override
    public void init() {
        controller = new TipoGastoJpaController("e");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");
        String id = request.getParameter("id");

        try {
            if ("list".equals(action)) {
                // Listar todos los tipos de gasto
                List<TipoGasto> tipos = controller.findTipoGastoEntities();
                out.print("[");
                for (int i = 0; i < tipos.size(); i++) {
                    TipoGasto tg = tipos.get(i);
                    out.print("{\"id\": \"" + tg.getTipoGastold() +
                            "\", \"descripcion\": \"" + tg.getTipoGastoDescripcion() + "\"}");
                    if (i < tipos.size() - 1)
                        out.print(",");
                }
                out.print("]");
            } else if ("get".equals(action) && id != null) {
                // Obtener un tipo de gasto específico
                TipoGasto tipo = controller.findTipoGasto(id);
                if (tipo != null) {
                    out.print("{\"id\": \"" + tipo.getTipoGastold() +
                            "\", \"descripcion\": \"" + tipo.getTipoGastoDescripcion() + "\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Tipo de gasto no encontrado\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Parámetros inválidos\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error en el servidor: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String id = request.getParameter("id");
        String descripcion = request.getParameter("descripcion");

        if (id == null || descripcion == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Parámetros faltantes\"}");
            return;
        }

        try {
            TipoGasto nuevoTipo = new TipoGasto(id, descripcion);
            controller.create(nuevoTipo);
            out.print("{\"message\": \"Tipo de gasto creado exitosamente\"}");
        } catch (PreexistingEntityException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            out.print("{\"error\": \"El tipo de gasto ya existe\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error al crear: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // Leer el cuerpo de la solicitud como JSON
            StringBuilder sb = new StringBuilder();
            String line;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONObject json = new JSONObject(sb.toString());
            String id = json.getString("id");
            String descripcion = json.getString("descripcion");

            if (id == null || descripcion == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\": \"Parámetros faltantes\"}");
                return;
            }

            TipoGasto tipoActualizado = new TipoGasto(id, descripcion);
            controller.edit(tipoActualizado);
            out.print("{\"message\": \"Tipo de gasto actualizado exitosamente\"}");
        } catch (JSONException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"Formato JSON inválido\"}");
        } catch (NonexistentEntityException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"error\": \"Tipo de gasto no encontrado\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error al actualizar: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String id = request.getParameter("id");

        if (id == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\": \"ID requerido\"}");
            return;
        }

        try {
            controller.destroy(id);
            out.print("{\"message\": \"Tipo de gasto eliminado exitosamente\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error al eliminar: " + e.getMessage() + "\"}");
        }
    }

}
