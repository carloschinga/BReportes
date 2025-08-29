/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package pe.bartolito.conta.servlet;

import com.google.gson.Gson;
import java.util.List;
import pe.bartolito.conta.dao.DiarioCabeceraJpaController;
import pe.bartolito.conta.dao.DiarioDetalleJpaController;
import pe.bartolito.conta.dto.DiarioCabecera;
import pe.bartolito.conta.dto.DiarioDetalle;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author USUARIO
 */
@WebServlet(name = "DiarioServlet", urlPatterns = {"/diarioservlet"})
public class DiarioServlet extends HttpServlet {
    private final DiarioCabeceraJpaController diarioCabeceraJpaController;
    private final DiarioDetalleJpaController diarioDetalleJpaController;
    private final EntityManagerFactory emf;

  
    public DiarioServlet() {
         this.emf = Persistence.createEntityManagerFactory("sigol");
       
        this.diarioCabeceraJpaController =new DiarioCabeceraJpaController(emf);
        this.diarioDetalleJpaController = new DiarioDetalleJpaController(emf);
    }
    
    

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtiene la fecha desde el parámetro de la solicitud
        String fecha = request.getParameter("fecha");

        // Si la fecha está presente, filtra las cabeceras de diario por fecha
        if (fecha != null && !fecha.isEmpty()) {
            try {
                // Convierte la fecha a formato Date (o LocalDate si es necesario)
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date fechaDate = sdf.parse(fecha);

                // Obtiene las cabeceras y detalles del diario
                List<DiarioCabecera> cabeceras = diarioCabeceraJpaController.findDiarioCabeceraByFecha(fechaDate);

                // Llenamos los detalles de cada cabecera
                for (DiarioCabecera cabecera : cabeceras) {
                    List<DiarioDetalle> detalles = diarioDetalleJpaController.findDiarioDetalleByCabecera(cabecera.getDiaCabCompId());
                    cabecera.setDiarioDetalleList(detalles);
                }

                // Establece las cabeceras y detalles en la solicitud para ser accesibles desde el JSP
                request.setAttribute("cabeceras", cabeceras);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/diario.jsp");
                dispatcher.forward(request, response);

            } catch (ParseException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Fecha inválida.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Fecha es requerida.");
        }
    }

}
