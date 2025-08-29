package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import dao.MetodosAux;

@WebServlet(name = "CRUDInventarioAlmacenDetalle", urlPatterns = { "/CRUDinvalmdetalle" })
public class CRUDInventarioAlmacenDetalle extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");

        PrintWriter out = resp.getWriter();

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("empr") == null) {
            sendError(resp, "Sesion no valida");
            return;
        }

        String empr = session.getAttribute("empr").toString();
        MetodosAux dao = new MetodosAux(empr);
        JSONObject jsonResponse = new JSONObject();
        String opcionStr = req.getParameter("opcion");
        int opcion;
        try {
            opcion = Integer.parseInt(opcionStr);
        } catch (Exception e) {
            sendError(resp, "No tiene opcion");
            return;
        }

        switch (opcion) {
            case 1:
                String codinvcabStr = req.getParameter("codinvcab");
                String codinvStr = req.getParameter("codinv");
                int codinv;
                try {
                    codinv = Integer.parseInt(codinvStr);
                } catch (Exception e) {
                    sendError(resp, "codinv no valido");
                    return;
                }
                int codinvcab;
                try {
                    codinvcab = Integer.parseInt(codinvcabStr);
                } catch (Exception e) {
                    sendError(resp, "codinvcab no valido");
                    return;
                }
                boolean tieneFotografia = dao.validarFotoCabecera(codinv, codinvcab);
                jsonResponse.put("success", tieneFotografia);
                break;
            default:
                break;
        }

        out.print(jsonResponse.toString());
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        JSONObject errorResponse = new JSONObject();
        errorResponse.put("success", false);
        errorResponse.put("error", message);
        response.getWriter().print(errorResponse.toString());
    }
}
