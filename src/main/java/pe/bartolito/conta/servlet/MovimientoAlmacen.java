/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package pe.bartolito.conta.servlet;

import pe.bartolito.conta.dto.FaMovimientoAlmacen;
import pe.bartolito.conta.dto.FaMovimientoAlmacenPK;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author USUARIO
 */
@WebServlet(name = "MovimientoAlmacen", urlPatterns = {"/movimientoalmacen"})
public class MovimientoAlmacen extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final EntityManagerFactory emfSigol;
    private final EntityManagerFactory emfLolfar;

    public MovimientoAlmacen() {
        this.emfSigol = Persistence.createEntityManagerFactory("sigolD");
        this.emfLolfar = Persistence.createEntityManagerFactory("lolfarP3");

    }

    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {

            out.write("{\"status\":\"success\", \"message\":\"Movimiento almacenado correctamente\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
        } finally {
        }
    }

   

}
