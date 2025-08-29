package pe.bartolito.conta.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import pe.bartolito.conta.dao.InalambricoPinpadDAO;

@WebServlet(name = "InalambricoEndpoint", urlPatterns = { "/inalambrico" })
public class InalambricoEndpoint extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter out = resp.getWriter();
        JSONObject result = new JSONObject();

        try {
            // Leer el body como JSON
            StringBuilder sb = new StringBuilder();
            String line;
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject body = new JSONObject(sb.toString());

            String fechaInicioStr = body.getString("fechaInicio");
            String fechaFinStr = body.getString("fechaFin");

            // Parsear las fechas
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date fechaInicio = sdf.parse(fechaInicioStr);
            Date fechaFin = sdf.parse(fechaFinStr);

            InalambricoPinpadDAO dao = new InalambricoPinpadDAO("e");
            String data = dao.findInalambricoXlsByFechaProceso(fechaInicio, fechaFin);

            out.print(data.toString());
        } catch (Exception e) {
            e.printStackTrace();
            result.put("error", "Error al procesar la consulta: " + e.getMessage());
            out.print(result.toString());
        } finally {
            out.close();
        }
    }
}
