/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.ArchivosPDFJpaController;
import dto.ArchivosPDF;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 *
 * @author LOQ
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB antes de escribir en disco
        maxFileSize = 1024 * 1024 * 10, // Tamaño máximo de archivo 10MB
        maxRequestSize = 1024 * 1024 * 50 // Tamaño máximo de la petición 50MB
)
@WebServlet(name = "CRUDManuales", urlPatterns = {"/CRUDManuales"})
public class CRUDManuales extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static final String UPLOAD_DIR = "pdfs";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            try {
                HttpSession session = request.getSession(true);
                Object emprObj = session.getAttribute("empr");

                if (emprObj != null) {
                    String empr = emprObj.toString();
                    String opcion = request.getParameter("opcion");
                    ArchivosPDFJpaController dao = new ArchivosPDFJpaController(empr);

                    switch (opcion) {
                        case "1": // Subir archivo PDF
                            Part filePart = request.getPart("file");
                            String descripcion = request.getParameter("descripcion");

                            if (filePart == null || descripcion == null) {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"Datos incompletos\"}");
                                return;
                            }

                            String fileName = filePart.getSubmittedFileName();
                            if (!fileName.endsWith(".pdf")) {
                                out.print("{\"resultado\":\"error\",\"mensaje\":\"Solo se permiten archivos PDF\"}");
                                return;
                            }

                            // Crear objeto y guardar en la BD
                            ArchivosPDF archivo = new ArchivosPDF();
                            archivo.setNombreOriginal(fileName);
                            archivo.setDescripcion(descripcion);
                            archivo.setFechaSubida(new Date());
                            dao.create(archivo);

                            // Guardar archivo en el servidor con ID como nombre
                            String nuevoNombre = archivo.getId() + ".pdf";
                            String uploadPath = getServletContext().getRealPath("/") + File.separator + UPLOAD_DIR;
                            File uploadDir = new File(uploadPath);
                            if (!uploadDir.exists()) {
                                uploadDir.mkdir();
                            }

                            File archivoDestino = new File(uploadDir, nuevoNombre);
                            try (InputStream input = filePart.getInputStream(); FileOutputStream output = new FileOutputStream(archivoDestino)) {
                                byte[] buffer = new byte[1024];
                                int bytesRead;
                                while ((bytesRead = input.read(buffer)) != -1) {
                                    output.write(buffer, 0, bytesRead);
                                }
                            }

                            archivo.setNombreGuardado(nuevoNombre);
                            dao.edit(archivo);

                            out.print("{\"resultado\":\"ok\",\"mensaje\":\"Archivo subido con éxito\"}");
                            break;

                        case "2": // Listar archivos
                            out.print("{\"resultado\":\"ok\",\"data\":" + dao.listarArchivosJson() + "}");

                            break;

                        case "3": // Descargar archivo
                            String idStr = request.getParameter("id");
                            if (idStr == null) {
                                response.setContentType("application/json;charset=UTF-8");
                                response.getWriter().write("{\"resultado\":\"error\",\"mensaje\":\"ID no proporcionado\"}");
                                return;
                            }

                            int id = Integer.parseInt(idStr);
                            ArchivosPDF archivoDescargar = dao.findArchivosPDF(id);
                            if (archivoDescargar == null) {
                                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Archivo no encontrado");
                                return;
                            }

                            // Obtener la ruta real del archivo en el servidor
                            File file = new File(getServletContext().getRealPath("/") + File.separator + UPLOAD_DIR + File.separator + archivoDescargar.getNombreGuardado());

                            if (!file.exists()) {
                                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Archivo no encontrado");
                                return;
                            }

                            // Configurar la respuesta HTTP para descarga rápida
                            response.reset(); // Eliminar cabeceras previas
                            response.setContentType("application/pdf");
                            response.setHeader("Content-Disposition", "attachment; filename=\"" + archivoDescargar.getNombreOriginal().replace(" ", "_") + ".pdf\"");
                            response.setContentLengthLong(file.length());
                            response.setBufferSize(8192); // Tamaño del buffer para mejorar velocidad

                            // Enviar el archivo al cliente
                            try (BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(file), 8192); BufferedOutputStream responseOutputStream = new BufferedOutputStream(response.getOutputStream(), 8192)) {

                                byte[] buffer = new byte[8192];
                                int bytesRead;
                                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                                    responseOutputStream.write(buffer, 0, bytesRead);
                                }
                                responseOutputStream.flush(); // Asegurar que se envían todos los bytes
                            } catch (IOException e) {
                                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al leer el archivo");
                            }
                            break;

                        default:
                            out.print("{\"resultado\":\"error\",\"mensaje\":\"Opción inválida\"}");
                            break;
                    }
                } else {
                    out.print("{\"resultado\":\"error\",\"mensaje\":\"Sesión no válida\"}");
                }
            } catch (Exception e) {
                out.print("{\"resultado\":\"error\",\"mensaje\":\"" + e.getMessage() + "\"}");
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
