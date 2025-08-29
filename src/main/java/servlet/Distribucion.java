/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import com.google.gson.Gson;
import dao.FaProductosSPJpaController;
import dao.FaStockAlmacenesJpaController;
import dto.Dato;
import java.util.Date;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author USUARIO
 */
@WebServlet(name = "Distribucion", urlPatterns = {"/distribucion"})
public class Distribucion extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
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
                    switch (opcion) {
                        case "1": //central
                            int tipoStkMin = Integer.parseInt(request.getParameter("tipoStkMin"));
                            String tipoDistrib = request.getParameter("tipoDistrib");
                            String codTip = request.getParameter("codtip");
                            String codalm = request.getParameter("codalm");
                            String solorojos = request.getParameter("solorojos");
                            codTip = "{\"cadenas\":" + codTip + "}";
                            codalm = "{\"cadenas\":" + codalm + "}";
                            int secuencia = Integer.parseInt(request.getParameter("secuencia"));
                            String indicaFecha = request.getParameter("indicaFecha");
                            String soloalm = request.getParameter("soloalm");
                            String valorFecha1 = request.getParameter("fecha1");
                            String distpor = request.getParameter("distpor");
                            String mostrar0estab = request.getParameter("mostrar0central");
                            BigDecimal multiplicador = new BigDecimal(request.getParameter("multiplicador"));
                            String codalminv = session.getAttribute("codalminv").toString();
                            String resultado = "";
                            try {
                                FaProductosSPJpaController productosDAO = new FaProductosSPJpaController(empr);
                                Gson gson = new Gson();
                                Dato datos = gson.fromJson(codTip, Dato.class);
                                Dato datosalm = gson.fromJson(codalm, Dato.class);
                                List<String> listaCadenas = datos.getCadenas();
                                List<String> listaCadenasalm = datosalm.getCadenas();
                                System.out.println(valorFecha1);
                                Date fecha2 = productosDAO.parsearFecha(valorFecha1);

                                // Instanciar FaProductosSPJpaController y llamar al método listar con los parámetros
                                resultado = productosDAO.listar(tipoStkMin, tipoDistrib, listaCadenas, secuencia, indicaFecha, fecha2, solorojos, soloalm, listaCadenasalm, distpor, mostrar0estab, multiplicador, codalminv);
                       
                                out.print(resultado);
                            } catch (Exception ex) {
                                resultado = "{\"resultado\":\"error\",\"mensaje\":\"No se puede listar debido a problemas con el servicio\"}";
                                out.print(resultado);
                            }
                            break;
                        case "2": //establecimientos

                            tipoStkMin = Integer.parseInt(request.getParameter("tipoStkMin"));
                            String tipo_distrib = request.getParameter("tipo_distrib");
                            String codpro = request.getParameter("codpro");
                            String mostrarrojos = request.getParameter("mostrarrojos");
                            codTip = request.getParameter("codtip");
                            codalm = request.getParameter("codalm");
                            codTip = "{\"cadenas\":" + codTip + "}";
                            soloalm = request.getParameter("soloalm");
                            codalm = "{\"cadenas\":" + codalm + "}";
                            distpor = request.getParameter("distpor");
                            multiplicador = new BigDecimal(request.getParameter("multiplicador"));
                            mostrar0estab = request.getParameter("mostrar0estab");
                            codalminv = session.getAttribute("codalminv").toString();

                            resultado = "";
                            try {
                                Gson gson = new Gson();
                                Dato datos = gson.fromJson(codTip, Dato.class);
                                Dato datosalm = gson.fromJson(codalm, Dato.class);
                                List<String> listaCadenas = datos.getCadenas();
                                List<String> listaCadenasalm = datosalm.getCadenas();
                                FaProductosSPJpaController productosDAO = new FaProductosSPJpaController(empr);
                                resultado = productosDAO.distribucionEstablecimientos(tipoStkMin, tipo_distrib, codpro, mostrarrojos, soloalm, listaCadenasalm, listaCadenas, distpor, multiplicador, mostrar0estab, codalminv);

                                out.print(resultado);
                            } catch (Exception ex) {
                                resultado = "{\"resultado\":\"error\",\"mensaje\":\"No se puede listar debido a problemas con el servicio\"}";
                                out.print(resultado);
                            }
                            break;
                        case "3": //Grafico Ventas
                            codpro = request.getParameter("codpro");
                            try {
                                FaProductosSPJpaController productosDAO = new FaProductosSPJpaController(empr);

                                resultado = "{\"data\":" + productosDAO.mostrargraficoventas(codpro) + ",\"resultado\":\"ok\"}";
                                out.print(resultado);
                            } catch (Exception ex) {
                                resultado = "{\"resultado\":\"error\",\"mensaje\":\"No se puede listar debido a problemas con el servicio\"}";
                                out.print(resultado);
                            }
                            break;
                        case "4": //Prediccion ventas
                            codpro = request.getParameter("codpro");
                            try {
                                FaProductosSPJpaController productosDAO = new FaProductosSPJpaController(empr);

                                resultado = productosDAO.mostrarprediccion(codpro);
                                out.print(resultado);
                            } catch (Exception ex) {
                                resultado = "{\"resultado\":\"error\",\"mensaje\":\"No se puede listar debido a problemas con el servicio\"}";
                                out.print(resultado);
                            }
                            break;
                        case "5": //central generico
                            tipoStkMin = Integer.parseInt(request.getParameter("tipoStkMin"));
                            tipoDistrib = request.getParameter("tipoDistrib");
                            codTip = request.getParameter("codtip");
                            codalm = request.getParameter("codalm");
                            solorojos = request.getParameter("solorojos");
                            codTip = "{\"cadenas\":" + codTip + "}";
                            codalm = "{\"cadenas\":" + codalm + "}";
                            secuencia = Integer.parseInt(request.getParameter("secuencia"));
                            indicaFecha = request.getParameter("indicaFecha");
                            soloalm = request.getParameter("soloalm");
                            valorFecha1 = request.getParameter("fecha1");
                            distpor = request.getParameter("distpor");
                            mostrar0estab = request.getParameter("mostrar0central");
                            String generico = request.getParameter("generico");
                            multiplicador = new BigDecimal(request.getParameter("multiplicador"));
                            codalminv = session.getAttribute("codalminv").toString();
                            resultado = "";
                            try {
                                FaProductosSPJpaController productosDAO = new FaProductosSPJpaController(empr);
                                Gson gson = new Gson();
                                Dato datos = gson.fromJson(codTip, Dato.class);
                                Dato datosalm = gson.fromJson(codalm, Dato.class);
                                List<String> listaCadenas = datos.getCadenas();
                                List<String> listaCadenasalm = datosalm.getCadenas();
                                System.out.println(valorFecha1);
                                Date fecha2 = productosDAO.parsearFecha(valorFecha1);

                                // Instanciar FaProductosSPJpaController y llamar al método listar con los parámetros
                                resultado = productosDAO.listarporgenerico(tipoStkMin, tipoDistrib, listaCadenas, secuencia, indicaFecha, fecha2, solorojos, soloalm, listaCadenasalm, distpor, mostrar0estab, multiplicador, codalminv, generico);

                                out.print(resultado);
                            } catch (Exception ex) {
                                resultado = "{\"resultado\":\"error\",\"mensaje\":\"No se puede listar debido a problemas con el servicio\"}";
                                out.print(resultado);
                            }
                            break;
                        case "6": //central generico producto
                            tipoStkMin = Integer.parseInt(request.getParameter("tipoStkMin"));
                            tipoDistrib = request.getParameter("tipoDistrib");
                            codTip = request.getParameter("codtip");
                            codalm = request.getParameter("codalm");
                            solorojos = request.getParameter("solorojos");
                            codTip = "{\"cadenas\":" + codTip + "}";
                            codalm = "{\"cadenas\":" + codalm + "}";
                            secuencia = Integer.parseInt(request.getParameter("secuencia"));
                            indicaFecha = request.getParameter("indicaFecha");
                            soloalm = request.getParameter("soloalm");
                            valorFecha1 = request.getParameter("fecha1");
                            distpor = request.getParameter("distpor");
                            mostrar0estab = request.getParameter("mostrar0central");
                            generico = request.getParameter("generico");
                            String objgenerico = request.getParameter("objgenerico");
                            multiplicador = new BigDecimal(request.getParameter("multiplicador"));
                            codalminv = session.getAttribute("codalminv").toString();
                            resultado = "";
                            try {
                                FaProductosSPJpaController productosDAO = new FaProductosSPJpaController(empr);
                                Gson gson = new Gson();
                                Dato datos = gson.fromJson(codTip, Dato.class);
                                Dato datosalm = gson.fromJson(codalm, Dato.class);
                                List<String> listaCadenas = datos.getCadenas();
                                List<String> listaCadenasalm = datosalm.getCadenas();
                                System.out.println(valorFecha1);
                                Date fecha2 = productosDAO.parsearFecha(valorFecha1);

                                // Instanciar FaProductosSPJpaController y llamar al método listar con los parámetros
                                resultado = productosDAO.listargenericoproductos2(tipoStkMin, tipoDistrib, listaCadenas, secuencia, indicaFecha, fecha2, solorojos, soloalm, listaCadenasalm, distpor, mostrar0estab, multiplicador, codalminv, generico,objgenerico);

                                out.print(resultado);
                            } catch (Exception ex) {
                                resultado = "{\"resultado\":\"error\",\"mensaje\":\"No se puede listar debido a problemas con el servicio\"}";
                                out.print(resultado);
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

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
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
