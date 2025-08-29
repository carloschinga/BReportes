package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import dao.FaProductosJpaController;
import dao.InventarioListaProductosJpaController;
import dto.InventarioListaProductos;

@WebServlet(name = "ExcelXListPro", urlPatterns = { "/ExcelXListPro" })
@MultipartConfig
public class ExcelXListPro extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject jsonResponse = new JSONObject();
        PrintWriter out = response.getWriter();

        try {

            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("empr") == null) {
                out.print("Sesión no válida o expirada");
                return;
            }

            String empr = session.getAttribute("empr").toString();

            String codinvalmStr = request.getParameter("codinvalm");
            int codinvalm = Integer.parseInt(codinvalmStr);
            Part filePart = request.getPart("excelFile");

            if (filePart == null || filePart.getSize() == 0) {
                jsonResponse.put("success", false);
                jsonResponse.put("message", "No se ha subido ningún archivo");
                out.print(jsonResponse.toString());
                return;
            }

            // Procesar el archivo Excel
            InputStream fileContent = filePart.getInputStream();
            List<String> codigosProducto = leerExcel(fileContent);
            FaProductosJpaController dao = new FaProductosJpaController(empr);
            InventarioListaProductosJpaController daoInvLis = new InventarioListaProductosJpaController(empr);

            int exitosos = 0;
            JSONArray errores = new JSONArray();
            for (String codpro : codigosProducto) {
                if (!dao.getProductos(codpro)) {
                    errores.put("El producto " + codpro + " no existe");
                    continue;
                }

                if (daoInvLis.existeDuo(codpro, codinvalm)) {
                    errores.put("El producto " + codpro + " ya está asociado a este inventario");
                    continue;
                }

                InventarioListaProductos registro = new InventarioListaProductos(codinvalm, codpro);
                try {
                    daoInvLis.createOrSkip(registro);
                } catch (Exception e) {
                    errores.put("Error al asociar producto " + codpro + ": " + e.getMessage());
                }
                exitosos++;
            }

            jsonResponse.put("success", exitosos > 0);
            jsonResponse.put("errores", errores);
            jsonResponse.put("message", "Archivo procesado correctamente");
            jsonResponse.put("exitosos", exitosos);
            jsonResponse.put("total", codigosProducto.size());

        } catch (Exception e) {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Error al procesar el archivo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            out.print(jsonResponse.toString());
            out.flush();
        }
    }

    private List<String> leerExcel(InputStream inputStream) throws IOException {
        List<String> codigos = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); // Primera hoja

            // Buscar la columna con el encabezado "SKU"
            int skuColumnIndex = -1;
            Row headerRow = sheet.getRow(0); // Asumimos que la primera fila tiene los encabezados

            if (headerRow != null) {
                for (Cell cell : headerRow) {
                    if (cell.getCellType() == CellType.STRING &&
                            "SKU".equalsIgnoreCase(cell.getStringCellValue().trim())) {
                        skuColumnIndex = cell.getColumnIndex();
                        break;
                    }
                }
            }

            if (skuColumnIndex == -1) {
                throw new IOException("No se encontró la columna 'SKU' en el archivo Excel");
            }

            // Leer solo la columna SKU, empezando desde la fila 1 (omitir encabezado)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell cell = row.getCell(skuColumnIndex);
                    if (cell != null) {
                        String codigo = "";

                        switch (cell.getCellType()) {
                            case STRING:
                                codigo = cell.getStringCellValue().trim();
                                break;
                            case NUMERIC:
                                // Si es numérico pero está formateado como texto
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    codigo = "";
                                } else {
                                    codigo = String.valueOf((long) cell.getNumericCellValue());
                                }
                                break;
                            default:
                                continue;
                        }

                        if (!codigo.isEmpty()) {
                            codigos.add(codigo);
                        }
                    }
                }
            }
        }

        return codigos;
    }

}
