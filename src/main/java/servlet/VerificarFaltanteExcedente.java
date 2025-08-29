package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import dao.MetodosVerificacion;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "verificarFaltanteExcedente", urlPatterns = { "/verificarFaltanteExcedente" })
public class VerificarFaltanteExcedente extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        JSONObject resp = new JSONObject();
        try (PrintWriter out = response.getWriter()) {

            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("empr") == null) {
                out.print("{\"resultado\":\"error\", \"mensaje\":\"nosesion\"}");
                return;
            }

            String siscod = null;
            Integer ordenini = null;
            Integer ordenfin = null;
            String fechainicio = null;
            String fechafin = null;

            String empr = session.getAttribute("empr").toString();
            String central = session.getAttribute("central").toString();
            if (central.equals("N")) {
                siscod = session.getAttribute("siscod").toString();
            }

            String ordeniniStr = request.getParameter("ordenini");
            if (!ordeniniStr.isEmpty()) {
                ordenini = Integer.parseInt(ordeniniStr);
            }
            String ordenfinStr = request.getParameter("ordenfin");
            if (!ordenfinStr.isEmpty()) {
                ordenfin = Integer.parseInt(ordenfinStr);
            }
            String fechainicioStr = request.getParameter("fechainicio");
            if (!fechainicioStr.isEmpty()) {
                fechainicio = fechainicioStr;
            }
            String fechafinStr = request.getParameter("fechafin");
            if (!fechafinStr.isEmpty()) {
                fechafin = fechafinStr;
            }

            MetodosVerificacion dao = new MetodosVerificacion(empr);

            int tiene = dao.validarExcedente(fechainicio, fechafin, ordenini, ordenfin, siscod);

            if (tiene == 1) {
                resp.put("resultado", "ok");
            } else if (tiene == 0) {
                resp.put("resultado", "nodata");
            } else {
                resp.put("resultado", "error");
            }
            out.print(resp.toString());
        }

    }
}
