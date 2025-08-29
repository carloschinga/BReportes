/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.PetitorioJpaController;
import dto.Petitorio;
import dto.PetitorioPK;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

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
 * @author USUARIO
 */
@WebServlet(name = "CRUDPetitorio", urlPatterns = { "/CRUDPetitorio" })
public class CRUDPetitorio extends HttpServlet {

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
                HttpSession session = request.getSession(true);
                Object emprObj = session.getAttribute("empr");
                if (emprObj != null) {
                    String empr = emprObj.toString();
                    String opcion = request.getParameter("opcion");
                    PetitorioJpaController dao = new PetitorioJpaController(empr);
                    PetitorioJpaController daoClinica = new PetitorioJpaController("c");
                    switch (opcion) {
                        case "1":// listar por medcod
                            String medcod = request.getParameter("medcod");
                            String data = dao.listarpormedcod(medcod);
                            out.print("{\"resultado\":\"ok\",\"data\":" + data + "}");
                            break;
                        case "2":// agregar
                            medcod = request.getParameter("medcod");
                            String codpro = request.getParameter("codpro");
                            PetitorioPK objpk = new PetitorioPK(medcod, codpro);
                            Petitorio obj = new Petitorio(objpk);
                            obj.setEstado("S");
                            obj.setFeccre(new Date());
                            String codi = session.getAttribute("codi").toString();
                            obj.setUsecod(Integer.parseInt(codi));
                            Petitorio objant = dao.findPetitorio(objpk);
                            if (objant == null) {
                                dao.create(obj);
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                if (objant.getEstado().equals("N")) {
                                    dao.edit(obj);
                                    out.print("{\"resultado\":\"ok\"}");
                                } else {
                                    out.print("{\"resultado\":\"error\",\"mensaje\":\"repetido\"}");
                                }
                            }
                            break;
                        case "3":// eliminar
                            medcod = request.getParameter("medcod");
                            codpro = request.getParameter("codpro");
                            objpk = new PetitorioPK(medcod, codpro);
                            obj = dao.findPetitorio(objpk);
                            obj.setEstado("N");
                            dao.edit(obj);
                            out.print("{\"resultado\":\"ok\"}");
                            break;
                        case "4":// agregar lista
                            medcod = request.getParameter("medcod");
                            codi = session.getAttribute("codi").toString();

                            StringBuilder sb = new StringBuilder();
                            try (BufferedReader reader = request.getReader()) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            }
                            String body = sb.toString();
                            String resultado = dao.agregar_petitorio(medcod, Integer.parseInt(codi), body);
                            if ("S".equals(resultado)) {
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"errorprocedimiento\"}");
                            }
                            break;
                        case "5":
                            codi = session.getAttribute("codi").toString();
                            String sercod = request.getParameter("sercod");

                            // Leer el cuerpo de la petición (JSON)
                            sb = new StringBuilder();
                            try (BufferedReader reader = request.getReader()) {
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            }
                            body = sb.toString();

                            // Parsear el JSON recibido
                            JSONObject jsonRequest = new JSONObject(body);
                            JSONArray productosArray = jsonRequest.getJSONArray("productos");

                            // Obtener médicos (si no se envió "medicos", consultar todos los de la
                            // especialidad)
                            JSONArray medArray;
                            if (jsonRequest.isNull("medicos")) {
                                List<String> medXesp = daoClinica.medicosDeLaEspecialidad(sercod);
                                medArray = new JSONArray();
                                for (String medcodi : medXesp) {
                                    medArray.put(medcodi);
                                }
                            } else {
                                medArray = jsonRequest.getJSONArray("medicos");
                            }

                            // Llamar al DAO
                            resultado = dao.agregar_petitorio_especialidad(
                                    Integer.parseInt(codi),
                                    productosArray.toString(), // Envía solo el array de productos
                                    medArray.toString() // Envía el array de médicos (todos o seleccionados)
                            );

                            if ("S".equals(resultado)) {
                                out.print("{\"resultado\":\"ok\"}");
                            } else {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"errorprocedimiento\"}");
                            }
                            break;
                        case "6":
                            sercod = request.getParameter("sercod");
                            List<String> medXesp = daoClinica.medicosDeLaEspecialidad(sercod);
                            medArray = new JSONArray();
                            for (String medcodi : medXesp) {
                                medArray.put(medcodi);
                            }

                            data = dao.getProductosPorMedicos(medArray.toString());
                            out.print("{\"resultado\":\"ok\",\"data\":" + data + "}");
                            break;
                        case "7":// eliminar muchos
                            codpro = request.getParameter("codpro");
                            String medcods = request.getParameter("medcods");
                            JSONArray medcodsArray = new JSONArray(medcods);

                            for (int i = 0; i < medcodsArray.length(); i++) {
                                medcod = medcodsArray.getString(i);
                                objpk = new PetitorioPK(medcod, codpro);
                                obj = dao.findPetitorio(objpk);
                                if (obj != null) {
                                    obj.setEstado("N");
                                    dao.edit(obj);
                                }
                            }
                            out.print("{\"resultado\":\"ok\"}");
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
