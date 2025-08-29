package pe.bartolito.conta.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pe.bartolito.conta.dao.CombosParaClasi;

@WebServlet(name = "CombosForClasiServlet", urlPatterns = { "/combosForClasi" })
public class CombosForClasiServlet extends HttpServlet {

    private CombosParaClasi comboDAO = new CombosParaClasi("e");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            String opcion = req.getParameter("opcion");
            String resultado = "";

            switch (opcion) {
                case "1":
                    // Combo de unidad operativa
                    resultado = comboDAO.getComboUniOpera();
                    break;

                case "2":
                    // Combo de tipo gasto
                    resultado = comboDAO.getComboTipoGasto();
                    break;

                case "3":
                    // Combo de proceso
                    resultado = comboDAO.getComboProceso();
                    break;

                case "4":
                    // Combo de actividad (requiere parámetro proceso)
                    String proceso = req.getParameter("proceso");
                    if (proceso != null && !proceso.isEmpty()) {
                        resultado = comboDAO.getComboActividad(proceso);
                    } else {
                        resultado = "[]"; // JSON vacío si no hay parámetro
                    }
                    break;

                case "5":
                    // Combo de tarea (requiere parámetros proceso y actividad)
                    String procesoTarea = req.getParameter("proceso");
                    String actividad = req.getParameter("actividad");
                    if (procesoTarea != null && !procesoTarea.isEmpty() &&
                            actividad != null && !actividad.isEmpty()) {
                        resultado = comboDAO.getComboTarea(procesoTarea, actividad);
                    } else {
                        resultado = "[]"; // JSON vacío si faltan parámetros
                    }
                    break;

                case "6":
                    // Combo de activo
                    resultado = comboDAO.getComboActivo();
                    break;

                case "7":
                    // Combo de producto/servicio
                    resultado = comboDAO.getComboProductoServicio();
                    break;

                case "8":
                    // Combo de centro de costo responsabilidad
                    resultado = comboDAO.getComboCentroCostoResponsabilidad();
                    break;
                case "9":
                    // Combo de empresa
                    resultado = comboDAO.getComboEmpresa();
                    break;
                default:
                    resultado = "[]"; // JSON vacío para opciones no válidas
                    break;
            }

            out.print(resultado);
            out.flush();

        } catch (Exception e) {
            // Manejo de errores - devolver JSON vacío
            out.print("[]");
            out.flush();
            e.printStackTrace(); // Para debugging
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
