/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import dao.JpaPadre;
import dto.MovimientosIzipay;
import dto.MovimientosIzipayPK;
import org.json.JSONObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author USER
 */
@WebServlet(name = "Upload", urlPatterns = {"/upload"})
public class Upload extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Leer el cuerpo de la solicitud (el contenido del archivo)
        String fileContent = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        if (fileContent == null || fileContent.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El contenido del archivo es requerido.");
            return;
        }

        EntityManagerFactory emf = null;
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            // Parsear el JSON para obtener el campo "data"
            JSONObject jsonObject = new JSONObject(fileContent);
            String data = jsonObject.getString("data");

            // Dividir el contenido del campo "data" por líneas
            String[] lines = data.split("\\r?\\n");

            // Verificar que existen datos además de la cabecera
            if (lines.length <= 1) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El archivo no contiene datos válidos.");
                return;
            }

            // Configurar EntityManager
            JpaPadre per=new JpaPadre("a");
            em = per.getEntityManager();
            transaction = em.getTransaction();

            transaction.begin();

            // Procesar cada línea después de la cabecera
            for (int i = 1; i < lines.length; i++) { // Saltar la cabecera
                String line = lines[i].trim();
                if (line.isEmpty()) {
                    continue; // Ignorar líneas vacías
                }
                // Dividir cada línea por los campos separados por ';'
                String[] values = line.split(";");
                if (values.length < 24) {
                    System.err.println("Línea inválida: " + line);
                    continue; // Ignorar líneas inválidas
                }
                
                // Crear clave primaria compuesta
                MovimientosIzipayPK movimientoPK = new MovimientosIzipayPK(values[0], values[8]);
                MovimientosIzipay movimiento = new MovimientosIzipay(movimientoPK);
                
                if("A".equals(movimiento.getEstado())){
                    continue;
                }
                // Asignar valores al objeto MovimientosIzipay
                movimiento.setProducto(values[1]);
                movimiento.setTipoMov(values[2].charAt(0));
                movimiento.setFechaProceso(new SimpleDateFormat("dd/MM/yyyy").parse(values[3]));
                movimiento.setFechaLote(new SimpleDateFormat("dd/MM/yyyy").parse(values[4]));
                movimiento.setLoteManual(values[5].isEmpty() ? null : values[5]);
                movimiento.setLotePos(values[6]);
                movimiento.setTerminal(values[7]);
                movimiento.setAutorizacion(values[9]);
                movimiento.setCuotas(values[10].isEmpty() ? null : Integer.valueOf(values[10]));
                movimiento.setTarjeta(values[11]);
                movimiento.setOrigen(values[12]);
                movimiento.setTransaccion(values[13]);
                movimiento.setFechaConsumo(new SimpleDateFormat("dd/MM/yyyy").parse(values[14]));
                movimiento.setImporte(new BigDecimal(values[15]));
                movimiento.setStatus(values[16]);
                movimiento.setComision(new BigDecimal(values[17]));
                movimiento.setComisionAfecta(new BigDecimal(values[18]));
                movimiento.setIgv(new BigDecimal(values[19]));
                movimiento.setNetoParcial(new BigDecimal(values[20]));
                movimiento.setNetoTotal(new BigDecimal(values[21]));
                movimiento.setFechaAbono(values[22].isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").parse(values[22]));
                movimiento.setFechaAbono8Dig(values[23]);
                movimiento.setObservaciones(values.length > 24 ? values[24] : null);

                // Guardar en la base de datos
                em.merge(movimiento);
            }

            transaction.commit();
            response.getWriter().write("Datos importados exitosamente.");
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al importar los datos.");
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
            if (emf != null) {
                emf.close();
            }
        }
    }

}
