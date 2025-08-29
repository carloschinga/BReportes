/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package pe.bartolito.clinica.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pe.bartolito.clinica.dto.ViewRc;

/**
 *
 * @author USUARIO
 */
@WebServlet(name = "SeguimientoReceta", urlPatterns = {"/seguimientoreceta"})
public class SeguimientoReceta extends HttpServlet {

    private EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            this.emf = Persistence.createEntityManagerFactory("sigoldbi");
        } catch (Exception e) {
            throw new ServletException("Error al conectar con la base de datos: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        EntityManager em = null;

        try {
            em = emf.createEntityManager();
            String fechaInicio = request.getParameter("fechaInicio");
            String fechaFin = request.getParameter("fechaFin");
            String servicio = request.getParameter("servicio");
            String medico = request.getParameter("medico");
            String paciente = request.getParameter("paciente");
            String chekRecetaStr = request.getParameter("chekReceta");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaInicioDate = (fechaInicio != null && !fechaInicio.isEmpty()) ? sdf.parse(fechaInicio) : null;
            Date fechaFinDate = (fechaFin != null && !fechaFin.isEmpty()) ? sdf.parse(fechaFin) : null;
            Boolean chekReceta = (chekRecetaStr != null && !chekRecetaStr.isEmpty()) ? Boolean.parseBoolean(chekRecetaStr) : null;

            StringBuilder jpql = new StringBuilder("SELECT v FROM ViewRc v WHERE ");
            if (fechaInicioDate != null) {
                jpql.append("  v.fechaAtencion >= :fechaInicio");
            }
            if (fechaFinDate != null) {
                jpql.append(" AND v.fechaAtencion <= :fechaFin");
            }
            if (servicio != null && !servicio.isEmpty()) {
                jpql.append(" AND v.servicio = :servicio");
            }
            if (medico != null && !medico.isEmpty()) {
                jpql.append(" AND v.medico = :medico");
            }
            if (paciente != null && !paciente.isEmpty()) {
                jpql.append(" AND v.paciente = :paciente");
            }
            if (chekReceta != null) {
                jpql.append(" AND v.chekReceta = :chekReceta");
            }

            TypedQuery<ViewRc> query = em.createQuery(jpql.toString(), ViewRc.class);
            if (fechaInicioDate != null) {
                query.setParameter("fechaInicio", fechaInicioDate);
            }
            if (fechaFinDate != null) {
                query.setParameter("fechaFin", fechaFinDate);
            }
            if (servicio != null && !servicio.isEmpty()) {
                query.setParameter("servicio", servicio);
            }
            if (medico != null && !medico.isEmpty()) {
                query.setParameter("medico", medico);
            }
            if (paciente != null && !paciente.isEmpty()) {
                query.setParameter("paciente", paciente);
            }
            if (chekReceta != null) {
                query.setParameter("chekReceta", chekReceta);
            }

            List<ViewRc> recetas = query.getResultList();

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("draw", request.getParameter("draw") != null ? Integer.parseInt(request.getParameter("draw")) : 1);
            jsonResponse.addProperty("recordsTotal", recetas.size());
            jsonResponse.addProperty("recordsFiltered", recetas.size());
            jsonResponse.add("data", new Gson().toJsonTree(recetas));

            out.print(jsonResponse.toString());

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"Error al recuperar datos: " + e.getMessage() + "\"}");
        } finally {
            if (em != null) {
                em.close();
            }
            out.flush();
        }
    }

    @Override
    public void destroy() {
        if (emf != null) {
            emf.close();
        }
    }

}
