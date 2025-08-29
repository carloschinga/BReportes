/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.FontFactory;

import dao.AlmacenesBartolitoDetalleJpaController;
import dao.FaStockVencimientosJpaController;
import dto.AlmacenesBartolitoDetalle;

/**
 *
 * @author USUARIO
 */
@WebServlet(name = "CRUDseteo", urlPatterns = { "/seteoMasivo" })
public class CRUDseteo extends HttpServlet {
    private static final String CONTENT_TYPE = "application/json";
    private static final String ENCODING = "utf-8";

    private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
    private static final Font HEADER_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
    private static final Font NORMAL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 9);
    private static final Font SUCCESS_FONT = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD,
            new com.itextpdf.text.BaseColor(0, 100, 0));
    private static final Font ERROR_FONT = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD,
            new com.itextpdf.text.BaseColor(200, 0, 0));

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType(CONTENT_TYPE);
        resp.setCharacterEncoding(ENCODING);

        HttpSession session = req.getSession(false);
        PrintWriter out = resp.getWriter();
        JSONObject jsonResponse = new JSONObject();

        // Validar sesión
        if (session == null || session.getAttribute("empr") == null || session.getAttribute("codalminv") == null) {
            jsonResponse.put("success", "error");
            jsonResponse.put("message", "Sesión no válida");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.print(jsonResponse.toString());
            return;
        }

        String empr = session.getAttribute("empr").toString();
        String alma = session.getAttribute("codalminv").toString();

        try {
            // Leer el cuerpo de la petición
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            JSONArray productos = new JSONArray(sb.toString());
            AlmacenesBartolitoDetalleJpaController dao = new AlmacenesBartolitoDetalleJpaController(empr);

            FaStockVencimientosJpaController faDAO = new FaStockVencimientosJpaController(empr);

            int exitosos = 0;
            int fallidos = 0;
            JSONArray errores = new JSONArray();
            List<JSONObject> todosLosRegistros = new ArrayList<>();

            // Procesar cada producto
            for (int i = 0; i < productos.length(); i++) {
                JSONObject producto = productos.getJSONObject(i);
                String lote = producto.getString("lote");
                String codpro = producto.getString("codpro");
                String nombre = producto.optString("nombre", "Sin nombre");
                int qtymov = producto.getInt("qtymov");
                int qtymov_m = producto.getInt("qtymov_m");

                JSONObject registro = new JSONObject();
                registro.put("producto", nombre);
                registro.put("lote", lote);
                registro.put("codpro", codpro);
                registro.put("difEntero", qtymov);
                registro.put("difFraccion", qtymov_m);

                try {
                    // Buscar primera ubicación existente
                    AlmacenesBartolitoDetalle detaRegi = dao.buscarOneDetallePorLoteAlmacenProducto(lote, alma, codpro);
                    int canteDeta = detaRegi.getCante();
                    int cantfDeta = detaRegi.getCantf();

                    int nuevaDeta = canteDeta - qtymov;
                    int nuevafDeta = cantfDeta - qtymov_m;

                    registro.put("cantidad_anterior_e", canteDeta);
                    registro.put("cantidad_anterior_f", cantfDeta);
                    registro.put("cantidad_nueva_e", nuevaDeta);
                    registro.put("cantidad_nueva_f", nuevafDeta);
                    registro.put("estado", "ACTUALIZADO");
                    registro.put("error", JSONObject.NULL);

                    detaRegi.setCante(nuevaDeta);
                    detaRegi.setCantf(nuevafDeta);
                    dao.edit(detaRegi);

                    exitosos++;
                } catch (Exception e) {
                    fallidos++;
                    registro.put("estado", "FALLÓ");
                    registro.put("error", e.getMessage());

                    JSONObject error = new JSONObject();
                    error.put("producto", nombre);
                    error.put("error", e.getMessage());
                    errores.put(error);
                }
                todosLosRegistros.add(registro);
            }

            int ramdom = faDAO.actualizarUbicacionesDesdeAlmacen();

            // Preparar respuesta
            jsonResponse.put("success", "ok");
            jsonResponse.put("exitosos", exitosos);
            jsonResponse.put("fallidos", fallidos);
            jsonResponse.put("ramdom", ramdom);

            if (fallidos > 0) {
                jsonResponse.put("errores", errores);
            }

            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Document document = new Document();
                PdfWriter.getInstance(document, baos);

                document.open();

                // Encabezado del reporte
                Paragraph title = new Paragraph("REPORTE DETALLADO DE SINCRONIZACIÓN", TITLE_FONT);
                title.setAlignment(Paragraph.ALIGN_CENTER);
                document.add(title);

                document.add(new Paragraph(" "));

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                document.add(new Paragraph("Fecha de generación: " + sdf.format(new Date()), NORMAL_FONT));
                document.add(new Paragraph("Almacén: " + alma, NORMAL_FONT));
                document.add(new Paragraph("Empresa: " + empr, NORMAL_FONT));
                document.add(new Paragraph("Total procesados: " + productos.length(), NORMAL_FONT));
                document.add(new Paragraph("Actualizados: " + exitosos, SUCCESS_FONT));
                document.add(new Paragraph("Fallidos: " + fallidos, fallidos > 0 ? ERROR_FONT : NORMAL_FONT));

                document.add(new Paragraph(" "));
                document.add(new Paragraph("----------------------------------------------------"));
                document.add(new Paragraph(" "));

                // Detalle de cada producto
                for (JSONObject reg : todosLosRegistros) {
                    Font estadoFont = "ACTUALIZADO".equals(reg.getString("estado")) ? SUCCESS_FONT : ERROR_FONT;

                    document.add(new Paragraph("Producto: " + reg.getString("producto"), NORMAL_FONT));
                    document.add(new Paragraph("Lote: " + reg.getString("lote"), NORMAL_FONT));
                    document.add(new Paragraph("Código: " + reg.getString("codpro"), NORMAL_FONT));

                    if (reg.has("cantidad_anterior_e")) {
                        document.add(new Paragraph("Cant. Entero: " + reg.getInt("cantidad_anterior_e") + " → " +
                                reg.getInt("cantidad_nueva_e"), NORMAL_FONT));
                        document.add(new Paragraph("Cant. Fracción: " + reg.getInt("cantidad_anterior_f") + " → " +
                                reg.getInt("cantidad_nueva_f"), NORMAL_FONT));
                    }

                    document.add(new Paragraph("Estado: " + reg.getString("estado"), estadoFont));

                    if (!reg.isNull("error")) {
                        document.add(new Paragraph("Error: " + reg.getString("error"), ERROR_FONT));
                    }

                    document.add(new Paragraph("----------------------------------------------------"));
                }

                document.close();

                String pdfBase64 = Base64.getEncoder().encodeToString(baos.toByteArray());
                jsonResponse.put("pdf", pdfBase64);
            } catch (DocumentException e) {
                System.err.println("Error al generar PDF: " + e.getMessage());
                jsonResponse.put("pdf_error", "Error al generar el reporte PDF");
            }

            out.print(jsonResponse.toString());

        } catch (JSONException e) {
            jsonResponse.put("success", "error");
            jsonResponse.put("message", "Error en el formato de los datos: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(jsonResponse.toString());
        } catch (Exception e) {
            jsonResponse.put("success", "error");
            jsonResponse.put("message", "Error en el servidor: " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(jsonResponse.toString());
        }
    }
}
