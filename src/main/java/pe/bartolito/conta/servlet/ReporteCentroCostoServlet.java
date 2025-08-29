/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package pe.bartolito.conta.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pe.bartolito.conta.dao.ClasificadoCentroCostoDao;

/**
 *
 * @author USUARIO
 */
@WebServlet(name = "ReporteCentroCostoServlet", urlPatterns = { "/reporteCentroCosto" })
public class ReporteCentroCostoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // Leer el cuerpo de la petición JSON
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            // Parsear JSON
            JSONObject requestJson = new JSONObject(sb.toString());

            // Obtener parámetros del JSON
            String empresa = requestJson.optString("empresa", null);
            int ano = requestJson.optInt("ano", -1);
            int mes = requestJson.optInt("mes", -1);

            // Validar parámetros requeridos
            if (empresa == null || ano == -1 || mes == -1) {
                JSONObject error = new JSONObject();
                error.put("resultado", "error");
                error.put("mensaje", "Parámetros requeridos en el JSON: empresa, ano, mes");
                response.getWriter().write(error.toString());
                return;
            }

            // Validar rangos
            if (mes < 1 || mes > 12) {
                JSONObject error = new JSONObject();
                error.put("resultado", "error");
                error.put("mensaje", "El mes debe estar entre 1 y 12");
                response.getWriter().write(error.toString());
                return;
            }

            if (ano < 1900 || ano > 2100) {
                JSONObject error = new JSONObject();
                error.put("resultado", "error");
                error.put("mensaje", "El año debe estar en un rango válido");
                response.getWriter().write(error.toString());
                return;
            }

            // Crear instancia de tu clase que contiene el método
            ClasificadoCentroCostoDao servicio = new ClasificadoCentroCostoDao("e");

            // Llamar al método y obtener el resultado
            String resultado = servicio.getReporteClasificadores(empresa, ano, mes);

            // Crear respuesta estructurada
            JSONObject respuesta = new JSONObject();
            respuesta.put("resultado", "ok");
            respuesta.put("data", resultado);

            // Enviar respuesta
            response.getWriter().write(respuesta.toString());

        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("resultado", "error");
            error.put("mensaje", "Error al procesar la petición: " + e.getMessage());
            response.getWriter().write(error.toString());
            e.printStackTrace();
        }
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
