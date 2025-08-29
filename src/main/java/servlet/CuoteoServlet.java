/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.ObjesiscodJpaController;
import dao.ObjeventasJpaController;
import dto.Objesiscod;
import dto.Objeventas;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
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
 * @author sbdeveloperw
 */
@WebServlet(name = "CuoteoServlet", urlPatterns = {"/cuoteoservlet"})
public class CuoteoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");

        HttpSession session = req.getSession(false);
        PrintWriter out = resp.getWriter();
        try {
            int codObj = Integer.parseInt(req.getParameter("codObj"));
            int sisCod = Integer.parseInt(req.getParameter("sisCod"));
            String periodo = req.getParameter("periodo");
            Object emprObj = session.getAttribute("empr");
            String empresa = emprObj.toString();//(String) session.getAttribute("empresa");

            ObjesiscodJpaController dao = new ObjesiscodJpaController(empresa);
            Objesiscod obj = dao.findByCodobjAndSiscod(codObj, sisCod);
           // String periodo = obj.getMesano();
            Periodo peri = getMesAndYear(periodo);
            YearMonth yearMonth = YearMonth.of(peri.getAño(), peri.getMes());
            LocalDate primerDiaDelMes = yearMonth.atDay(1);
            LocalDate ultimoDiaDelMes = yearMonth.atEndOfMonth();

            HashMap<DayOfWeek, Integer> diasDeSemana = new HashMap<>();
            for (DayOfWeek dia : DayOfWeek.values()) {
                diasDeSemana.put(dia, 0);
            }

            LocalDate fecha = primerDiaDelMes;
            while (!fecha.isAfter(ultimoDiaDelMes)) {
                DayOfWeek diaSemana = fecha.getDayOfWeek();
                diasDeSemana.put(diaSemana, diasDeSemana.get(diaSemana) + 1);
                fecha = fecha.plusDays(1);
            }

            // Crear un JSON con los datos
            JSONObject jsonResponse = new JSONObject();
            JSONArray diasArray = new JSONArray();

            // Mapear los días de la semana a nombres en español
            HashMap<DayOfWeek, String> nombresDias = new HashMap<>();
            nombresDias.put(DayOfWeek.MONDAY, "Lunes");
            nombresDias.put(DayOfWeek.TUESDAY, "Martes");
            nombresDias.put(DayOfWeek.WEDNESDAY, "Miércoles");
            nombresDias.put(DayOfWeek.THURSDAY, "Jueves");
            nombresDias.put(DayOfWeek.FRIDAY, "Viernes");
            nombresDias.put(DayOfWeek.SATURDAY, "Sábado");
            nombresDias.put(DayOfWeek.SUNDAY, "Domingo");

            // Agregar los días y sus conteos al JSON
            for (DayOfWeek dia : DayOfWeek.values()) {
                JSONObject diaJson = new JSONObject();
                diaJson.put("dia", nombresDias.get(dia));
                diaJson.put("numeroDias", diasDeSemana.get(dia));
                diasArray.put(diaJson);
            }

            
            BigDecimal solesSuma = obj.getSoles();
            

            // Construir la respuesta final
            jsonResponse.put("dias", diasArray);
            jsonResponse.put("soles", solesSuma);

            // Enviar la respuesta JSON
            out.print(jsonResponse.toString());

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JSONObject errorJson = new JSONObject();
            errorJson.put("error", "Ocurrió un error al procesar la solicitud.");
            out.print(errorJson.toString());
        }

    }

    public Periodo getMesAndYear(String periodo) {
        String[] partes = periodo.split("-");
        String anio = partes[0];
        String mes = partes[1];
        int anioEntero = Integer.parseInt(anio);
        int mesEntero = Integer.parseInt(mes);
        Periodo peri = new Periodo(anioEntero, mesEntero);
        return peri;
    }

    public class Periodo {

        private int año;
        private int mes;

        public Periodo(int año, int mes) {
            this.año = año;
            this.mes = mes;
        }

        public int getAño() {
            return año;
        }

        public int getMes() {
            return mes;
        }

        @Override
        public String toString() {
            return "Año: " + año + ", Mes: " + mes;
        }
    }

}
