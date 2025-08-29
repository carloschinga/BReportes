package pe.bartolito.conta.servlet;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pe.bartolito.conta.dao.ConciliacionTarjetasIzapayComprobanteDAO;

// Importa tu DAO
// import tu.paquete.ConciliacionTarjetasIzapayComprobanteDAO;
@WebServlet(name = "ConciliarTarjetasServlet", urlPatterns = {"/conciliartarjetasservlet"})
public class ConciliarTarjetasServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Instancia del DAO
    private ConciliacionTarjetasIzapayComprobanteDAO dao = new ConciliacionTarjetasIzapayComprobanteDAO("e");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");

        // Obtener parámetros
        String codigo = request.getParameter("codigo");
        String fechaAbono = request.getParameter("fechaAbono");
        String netoTotalStr = request.getParameter("netoTotal");
        String unidComIdStr = request.getParameter("unidComId");

        // Validar parámetros
        if (codigo == null || fechaAbono == null || netoTotalStr == null || unidComIdStr == null) {
            response.getWriter().write("{\"error\":\"Faltan parámetros\"}");
            return;
        }

        try {
            double netoTotal = Double.parseDouble(netoTotalStr);
            int unidComId = Integer.parseInt(unidComIdStr);

            // Llamar al método del DAO
            String jsonResultado = dao.conciliarTarjetasJSON(codigo, fechaAbono, netoTotal, unidComId);

            // Enviar el resultado al frontend
            response.getWriter().write(jsonResultado);

        } catch (NumberFormatException e) {
            response.getWriter().write("{\"error\":\"Parámetros numéricos inválidos\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("{\"error\":\"Error al procesar la solicitud\"}");
        }
    }
}
