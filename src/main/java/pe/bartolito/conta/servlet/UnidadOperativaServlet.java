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

import org.json.JSONObject;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import pe.bartolito.conta.dao.UnidadOperativaJpaController;
import pe.bartolito.conta.dto.UnidadOperativa;

@WebServlet(name = "UnidadOperativaServlet", urlPatterns = { "/unidadoperativa" })
public class UnidadOperativaServlet extends HttpServlet {

    private UnidadOperativaJpaController controller;

    @Override
    public void init() {
        controller = new UnidadOperativaJpaController("e");
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
                // Listar todas las unidades operativas
                List<UnidadOperativa> unidades = controller.findUnidadOperativaEntities();
                out.print("[");
                for (int i = 0; i < unidades.size(); i++) {
                    UnidadOperativa uo = unidades.get(i);
                    out.print("{\"id\": \"" + uo.getUnidOperald() +
                            "\", \"descripcion\": \"" + uo.getUnidOperaDescripcion() + "\"}");
                    if (i < unidades.size() - 1)
                        out.print(",");
                }
                out.print("]");
            } else if ("get".equals(action) && id != null) {
                // Obtener una unidad operativa específica
                UnidadOperativa unidad = controller.findUnidadOperativa(id);
                if (unidad != null) {
                    out.print("{\"id\": \"" + unidad.getUnidOperald() +
                            "\", \"descripcion\": \"" + unidad.getUnidOperaDescripcion() + "\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{\"error\": \"Unidad operativa no encontrada\"}");
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
            UnidadOperativa nuevaUnidad = new UnidadOperativa(id, descripcion);
            controller.create(nuevaUnidad);
            out.print("{\"message\": \"Unidad operativa creada exitosamente\"}");
        } catch (PreexistingEntityException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            out.print("{\"error\": \"La unidad operativa ya existe\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error al crear: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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

            UnidadOperativa unidadActualizada = new UnidadOperativa(id, descripcion);
            controller.edit(unidadActualizada);
            out.print("{\"message\": \"Unidad operativa actualizada exitosamente\"}");
        } catch (NonexistentEntityException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("{\"error\": \"Unidad operativa no encontrada\"}");
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
            out.print("{\"message\": \"Unidad operativa eliminada exitosamente\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error al eliminar: " + e.getMessage() + "\"}");
        }
    }

}
