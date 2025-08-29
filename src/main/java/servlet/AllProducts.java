package servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.FaProductosJpaController;

@WebServlet(name = "AllProducts", urlPatterns = { "/allproducts" })
public class AllProducts extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("empr") == null) {
                sendError(response, "Sesión no válida");
                return;
            }

            String searchTerm = request.getParameter("q");
            String pageParam = request.getParameter("page");
            int page = (pageParam != null && !pageParam.isEmpty()) ? Integer.parseInt(pageParam) : 1;
            int pageSize = 10;

            String empresa = session.getAttribute("empr").toString();
            FaProductosJpaController productosDao = new FaProductosJpaController(empresa);

            JSONObject responseData = new JSONObject();
            JSONArray productosArray = new JSONArray();

            List<Map<String, Object>> productos = productosDao.searchProducts(searchTerm, page, pageSize);
            long totalItems = productosDao.countSearchProducts(searchTerm);

            for (Map<String, Object> producto : productos) {
                JSONObject productoJson = new JSONObject();
                productoJson.put("codigo", producto.get("codigo"));
                productoJson.put("nombre", producto.get("nombre"));
                productosArray.put(productoJson);
            }

            responseData.put("data", productosArray);
            responseData.put("currentPage", page);
            responseData.put("totalItems", totalItems);
            responseData.put("pageSize", pageSize);
            responseData.put("success", true);

            response.getWriter().print(responseData.toString());

        } catch (Exception e) {
            sendError(response, "Error en la búsqueda: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        JSONObject errorResponse = new JSONObject();
        errorResponse.put("success", false);
        errorResponse.put("error", message);
        response.getWriter().print(errorResponse.toString());
    }
}