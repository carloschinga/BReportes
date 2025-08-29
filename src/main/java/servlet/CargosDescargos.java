/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.FaAlmacenesJpaController;
import dao.FaProveedoresJpaController;
import dao.FaReferenciaMovimientoJpaController;
import dao.FaTipoMovimientosJpaController;
import dao.FaTransportistasJpaController;
import dao.FaUnidadMedidaJpaController;
import dao.MonedaJpaController;
import dao.MotivoTrasladoJpaController;
import dao.SecuenciasJpaController;
import dao.SistemaJpaController;
import dao.TipoPagoFacturacionJpaController;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "CargosDescargos", urlPatterns = {"/CargosDescargos"})
public class CargosDescargos extends HttpServlet {

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
                        case "1": //enviar todos los datos necesarios al ingresar a la ventana cargos descargos
                            String siscod = session.getAttribute("siscod").toString();

                            FaAlmacenesJpaController almDAO = new FaAlmacenesJpaController(empr);
                            String almsis = almDAO.buscarSiscod(Integer.parseInt(siscod));//almacenes del sistema del usuario

                            SistemaJpaController sisDAO = new SistemaJpaController(empr);

                            FaReferenciaMovimientoJpaController refmovDAO = new FaReferenciaMovimientoJpaController(empr);

                            FaProveedoresJpaController provDAO = new FaProveedoresJpaController(empr);

                            TipoPagoFacturacionJpaController tippagfacDAO = new TipoPagoFacturacionJpaController(empr);

                            MonedaJpaController monDAO = new MonedaJpaController(empr);

                            FaTransportistasJpaController transpDAO = new FaTransportistasJpaController(empr);

                            SecuenciasJpaController secDAO = new SecuenciasJpaController(empr);

                            MotivoTrasladoJpaController mottraDAO = new MotivoTrasladoJpaController(empr);

                            FaUnidadMedidaJpaController uniDAO = new FaUnidadMedidaJpaController(empr);

                            StringBuilder jsonBuilder = new StringBuilder();
                            jsonBuilder.append("{\"resultado\":\"ok\",\"almsis\":")
                                    .append(almsis)
                                    .append(",\"listsis\":")
                                    .append(sisDAO.listarJson())
                                    .append(",\"listalm\":")
                                    .append(almDAO.listarJson())
                                    .append(",\"listrefmov\":")
                                    .append(refmovDAO.listarJson())
                                    .append(",\"listprov\":")
                                    .append(provDAO.listarJson())
                                    .append(",\"listtippagfac\":")
                                    .append(tippagfacDAO.listarJson())
                                    .append(",\"listmon\":")
                                    .append(monDAO.listarJson())
                                    .append(",\"listtransp\":")
                                    .append(transpDAO.listarJson())
                                    .append(",\"secuencia\":")
                                    .append(String.valueOf(secDAO.obtenerUltInvnum() + 1))
                                    .append(",\"siscod\":")
                                    .append(siscod)
                                    .append(",\"listmottrans\":")
                                    .append(mottraDAO.listarJson())
                                    .append(",\"listunid\":")
                                    .append(uniDAO.listarJson())
                                    .append("}");
                            out.print(jsonBuilder.toString());
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
