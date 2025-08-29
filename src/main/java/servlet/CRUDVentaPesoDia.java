/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.ObjventaPesodiaJpaController;
import dto.ObjventaPesodia;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
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
 * @author sbdeveloperw
 */
@WebServlet(name = "CRUDVentaPesoDia", urlPatterns = {"/ventapesodia"})
public class CRUDVentaPesoDia extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        // Leer el cuerpo de la solicitud (JSON)
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        try {
            String empresa = (String) session.getAttribute("empr");
            String codi = session.getAttribute("codi").toString();

            JSONObject jsonObj = new JSONObject(sb.toString());

            int codobj = Integer.parseInt(jsonObj.getString("codObj"));
            int siscod = Integer.parseInt(jsonObj.getString("sisCod"));

            ObjventaPesodiaJpaController dao = new ObjventaPesodiaJpaController(empresa);
            JSONObject respuesta = new JSONObject();

            JSONArray jsonArray = jsonObj.getJSONArray("datosTabla");
            ObjventaPesodia registro = new ObjventaPesodia(codobj, siscod);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objI = jsonArray.getJSONObject(i);
                registro.setDia(objI.getString("dia"));
                registro.setNumdia(objI.getInt("numeroDias"));
                registro.setPeso(objI.getInt("peso"));
                registro.setUnid(objI.getInt("unidades"));
                double cuotaMes = objI.getDouble("cuotasDiasMes");
                BigDecimal doubleCuotaMes = new BigDecimal(Double.toString(cuotaMes));
                registro.setCuotmes(doubleCuotaMes);
                double cuotaDiaria = objI.getDouble("cuotaPorDia");
                BigDecimal doubleCuotaDiaria = new BigDecimal(Double.toString(cuotaDiaria));
                registro.setCuotdia(doubleCuotaDiaria);
                registro.setUsecre(Integer.parseInt(codi));
                registro.setFeccre(new Date());
                registro.setEliminado(Boolean.FALSE);

                dao.create(registro);
                respuesta.put("success", "ok");
            }
            response.getWriter().write(respuesta.toString());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter()
                    .write(new JSONObject().put("error", "Error al obtener los datos: " + e.getMessage()).toString());
        }
    }

}
