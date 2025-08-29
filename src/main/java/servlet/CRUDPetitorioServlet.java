package servlet;

import dao.PetitorioTecnicoJpaController;
import dto.PetitorioTecnico;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "CRUDPetitorioServlet", urlPatterns = { "/CRUDpetitorioservlet" })
public class CRUDPetitorioServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "application/json";
    private static final String CHARSET = "UTF-8";
    private static final Logger logger = Logger.getLogger(CRUDPetitorioServlet.class.getName());
    private PetitorioTecnicoJpaController controller;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE);
        resp.setCharacterEncoding(CHARSET);
        PrintWriter out = resp.getWriter();
        JSONObject jsonResponse = new JSONObject();

        try {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("empr") == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonResponse.put("error", "No autorizado");
                out.print(jsonResponse.toString());
                return;
            }

            String empresa = (String) session.getAttribute("empr");
            controller = new PetitorioTecnicoJpaController(empresa);

            String opcStr = req.getParameter("opcion");
            if (opcStr == null || opcStr.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("error", "Parámetro 'opcion' requerido");
                out.print(jsonResponse.toString());
                return;
            }

            int opcion = Integer.parseInt(opcStr);
            JSONObject objPeti;

            switch (opcion) {
                case 1:
                    // Obtener un item específico por código
                    String codigo = req.getParameter("codigo");
                    if (codigo == null || codigo.isEmpty()) {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        jsonResponse.put("error", "Parámetro 'codigo' requerido");
                        break;
                    }
                    JSONObject obj = controller.getPTWithTieneHijos(codigo);
                    if (obj.isEmpty()) {
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        jsonResponse.put("error", "Item no encontrado");
                    } else {
                        JSONObject datita = objToJson(obj);
                        jsonResponse.put("data", objToJson(obj));
                    }
                    break;
                case 2:
                    // Listar todos los items
                    List<PetitorioTecnico> items = controller.findPetitorioTecnicoEntities();
                    JSONArray arrPeti = new JSONArray();
                    for (PetitorioTecnico object : items) {
                        arrPeti.put(itemToJson(object));
                    }
                    jsonResponse.put("data", arrPeti);
                    break;
                case 3:
                    // Listar todos los padres
                    String padres = controller.getPadres();
                    JSONArray arrPadres = new JSONArray(padres);
                    jsonResponse.put("data", arrPadres);
                    break;
                case 4:
                    // Obtener items raíz
                    List<PetitorioTecnico> raices = controller.findItemsRaiz();
                    JSONArray arrRaices = new JSONArray();
                    for (PetitorioTecnico object : raices) {
                        objPeti = new JSONObject();
                        objPeti.put("codigo", object.getCodigo());
                        objPeti.put("nombre", object.getNombre());
                        arrRaices.put(objPeti);
                    }
                    jsonResponse.put("data", arrRaices);
                    break;
                case 5:
                    // Obtener hijos de un item
                    String padre = req.getParameter("padre");
                    if (padre == null || padre.isEmpty()) {
                        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        jsonResponse.put("error", "Parámetro 'padre' requerido");
                        break;
                    }
                    List<PetitorioTecnico> hijos = controller.findHijos(padre);
                    JSONArray arrHijos = new JSONArray();
                    for (PetitorioTecnico object : hijos) {
                        objPeti = new JSONObject();
                        objPeti.put("codigo", object.getCodigo());
                        objPeti.put("nombre", object.getNombre());
                        arrHijos.put(objPeti);
                    }
                    jsonResponse.put("data", arrHijos);
                    break;
                case 6:
                    // Select de genericos
                    String arrGenStr = controller.obtenerGenericos();
                    JSONArray arraGenericos = new JSONArray(arrGenStr);
                    jsonResponse.put("data", arraGenericos);
                    break;
                case 7:
                    // listar todos los productos
                    String arrProStr = controller.obtenerProductos();
                    JSONArray arrProductos = new JSONArray(arrProStr);
                    jsonResponse.put("data", arrProductos);
                    break;
                default:
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    jsonResponse.put("error", "Opción no soportada");
                    break;
            }
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.put("error", "Opción debe ser un número");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error en doGet", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("error", "Error interno del servidor");
        }

        out.print(jsonResponse.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE);
        resp.setCharacterEncoding(CHARSET);
        PrintWriter out = resp.getWriter();
        JSONObject jsonResponse = new JSONObject();
        JSONArray resultados = new JSONArray();
        boolean hasErrors = false;

        try {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("empr") == null) {
                sendErrorResponse(resp, out, jsonResponse, HttpServletResponse.SC_UNAUTHORIZED,
                        "auth-required", "Autenticación requerida", "Debe iniciar sesión para realizar esta acción");
                return;
            }

            String empresa = (String) session.getAttribute("empr");
            controller = new PetitorioTecnicoJpaController(empresa);

            JSONObject jsonObj = new JSONObject(obtenerCuerpoRequest(req));

            if (jsonObj.has("items")) {
                // Procesamiento batch
                JSONArray items = jsonObj.getJSONArray("items");
                String padreComun = jsonObj.optString("padre", null);

                for (int i = 0; i < items.length(); i++) {
                    JSONObject itemObj = items.getJSONObject(i);
                    JSONObject itemResult = processItem(itemObj);

                    if (!itemResult.getBoolean("success")) {
                        hasErrors = true;
                    }
                    resultados.put(itemResult);
                }

                jsonResponse.put("results", resultados);
                jsonResponse.put("summary", createSummary(resultados));
                resp.setStatus(HttpServletResponse.SC_CREATED);

            } else {
                // Procesamiento de un solo registro
                JSONObject itemResult = processItem(jsonObj);

                if (itemResult.getBoolean("success")) {
                    jsonResponse = itemResult; // Usamos directamente el resultado
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                } else {
                    sendErrorResponse(resp, out, itemResult, HttpServletResponse.SC_BAD_REQUEST,
                            itemResult.getString("errorCode"),
                            itemResult.getString("error"),
                            itemResult.getString("detail"));
                    return;
                }
            }

        } catch (JSONException e) {
            sendErrorResponse(resp, out, jsonResponse, HttpServletResponse.SC_BAD_REQUEST,
                    "invalid-json", "JSON inválido", "El formato de la solicitud no es válido");
            return;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error en doPost", e);
            sendErrorResponse(resp, out, jsonResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "server-error", "Error del servidor", "Ocurrió un error inesperado");
            return;
        }

        out.print(jsonResponse.toString());
    }

    // Métodos auxiliares nuevos:

    private JSONObject processItem(JSONObject itemObj) throws Exception {
        JSONObject result = new JSONObject();

        String nombre = itemObj.optString("nombre", "").trim();
        String codigoProducto = itemObj.optString("codigo", null);
        String padre = itemObj.optString("padre", null);

        // Validaciones
        if (nombre.isEmpty()) {
            return buildErrorResult(result, "missing-name", "Nombre requerido", "El campo nombre es obligatorio");
        }

        if (codigoProducto != null && !codigoProducto.isEmpty() && controller.validarProducto(codigoProducto, padre)) {
            return buildErrorResult(result, "duplicate-code", "Código duplicado",
                    "El código de producto '" + codigoProducto + "' ya existe");
        }

        if (padre != null && !padre.isEmpty()) {
            PetitorioTecnico padreItem = controller.findPetitorioTecnicoByCodigo(padre);
            if (padreItem == null) {
                return buildErrorResult(result, "invalid-parent", "Padre inválido",
                        "El código padre '" + padre + "' no existe");
            }
        }

        // Creación del ítem
        try {
            PetitorioTecnico nuevoItem = new PetitorioTecnico();
            nuevoItem.setNombre(nombre);
            nuevoItem.setCncntr(itemObj.optString("cncntr", ""));
            nuevoItem.setFormFarma(itemObj.optString("formFarma", ""));
            nuevoItem.setPrsntcn(itemObj.optString("prsntcn", ""));
            nuevoItem.setCodigoProducto(codigoProducto);

            if (padre != null && !padre.isEmpty()) {
                nuevoItem.setPadre(padre);
                nuevoItem.setCodigo(controller.generarSiguienteCodigoHijo(padre));
            } else {
                nuevoItem.setCodigo(controller.generarSiguienteCodigoRaiz());
            }

            controller.create(nuevoItem);

            result.put("success", true);
            result.put("message", "Ítem creado exitosamente");

        } catch (PreexistingEntityException e) {
            return buildErrorResult(result, "duplicate-id", "ID duplicado",
                    "El código generado ya existe en el sistema");
        } catch (Exception e) {
            return buildErrorResult(result, "creation-error", "Error al crear",
                    "No se pudo crear el ítem: " + e.getMessage());
        }

        return result;
    }

    private JSONObject buildErrorResult(JSONObject result, String code, String title, String detail) {
        result.put("success", false);
        result.put("errorCode", code);
        result.put("error", title);
        result.put("detail", detail);
        return result;
    }

    private JSONObject createSummary(JSONArray resultados) {
        JSONObject summary = new JSONObject();
        int success = 0;
        int errors = 0;

        for (int i = 0; i < resultados.length(); i++) {
            if (resultados.getJSONObject(i).getBoolean("success")) {
                success++;
            } else {
                errors++;
            }
        }

        summary.put("total", resultados.length());
        summary.put("success", success);
        summary.put("errors", errors);
        return summary;
    }

    private void sendErrorResponse(HttpServletResponse resp, PrintWriter out, JSONObject response,
            int status, String code, String title, String detail) {
        resp.setStatus(status);
        response.put("errorCode", code);
        response.put("error", title);
        response.put("detail", detail);
        out.print(response.toString());
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE);
        resp.setCharacterEncoding(CHARSET);
        PrintWriter out = resp.getWriter();
        JSONObject jsonResponse = new JSONObject();
        EntityManager em = null;
        EntityTransaction tx = null;

        try {
            // Validación de sesión
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("empr") == null) {
                sendErrorResponse(resp, jsonResponse, out, HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");
                return;
            }

            String empresa = (String) session.getAttribute("empr");
            controller = new PetitorioTecnicoJpaController(empresa);
            em = controller.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            JSONObject jsonObj = new JSONObject(obtenerCuerpoRequest(req));

            // Obtener parámetros
            boolean cambioCodigo = jsonObj.has("codigoNuevo");
            String codigoActual = jsonObj.getString("codigoAntiguo");
            String nombre = jsonObj.getString("nombre");
            String nuevoPadre = jsonObj.optString("padre", "");
            String cncntr = jsonObj.optString("cncntr", "");
            String formFarma = jsonObj.optString("formFarma", "");
            String prsntcn = jsonObj.optString("prsntcn", "");
            String codigoProducto = jsonObj.optString("codigoProducto", "");
            String codigoNuevo = cambioCodigo ? jsonObj.getString("codigoNuevo") : null;

            // Validaciones básicas
            if (codigoActual == null || codigoActual.isEmpty() || nombre == null || nombre.trim().isEmpty()) {
                sendErrorResponse(resp, jsonResponse, out, HttpServletResponse.SC_BAD_REQUEST,
                        "Código y nombre son requeridos");
                rollbackTransaction(tx);
                return;
            }

            PetitorioTecnico item = controller.findPetitorioTecnicoByCodigo(codigoActual);
            if (item == null) {
                sendErrorResponse(resp, jsonResponse, out, HttpServletResponse.SC_NOT_FOUND, "El item no existe");
                rollbackTransaction(tx);
                return;
            }

            boolean tieneHijos = controller.tieneHijos(codigoActual);
            String padreActual = item.getPadre();
            boolean cambioPadre = !nuevoPadre.equals(padreActual != null ? padreActual : "");

            // Manejar cambio de padre (tiene prioridad sobre cambio de código)
            if (cambioPadre) {
                if (!nuevoPadre.isEmpty()) {
                    PetitorioTecnico padreItem = controller.findPetitorioTecnicoByCodigo(nuevoPadre);
                    if (padreItem == null) {
                        sendErrorResponse(resp, jsonResponse, out, HttpServletResponse.SC_BAD_REQUEST,
                                "El código padre no existe");
                        rollbackTransaction(tx);
                        return;
                    }
                }

                // Generar nuevo código basado en el padre (ignoramos codigoNuevo si vino)
                codigoNuevo = controller.obtenerSiguienteCodigoParaPadre(nuevoPadre);
                if (codigoNuevo == null) {
                    sendErrorResponse(resp, jsonResponse, out, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Error al generar nuevo código");
                    rollbackTransaction(tx);
                    return;
                }

                item.setPadre(nuevoPadre.isEmpty() ? null : nuevoPadre);
            }
            // Manejar solo cambio de código (si no hubo cambio de padre)
            else if (cambioCodigo) {
                // Validar que el código nuevo no exista
                if (controller.validarFormatoCodigo(codigoNuevo)) {
                    sendErrorResponse(resp, jsonResponse, out, HttpServletResponse.SC_BAD_REQUEST,
                            "El código nuevo ya existe o es superior a la última raíz");
                    rollbackTransaction(tx);
                    return;
                }

                // Determinar el nuevo padre basado en el código nuevo
                if (esCodigoRaiz(codigoNuevo)) {
                    // Si es raíz, padre debe ser null o vacío
                    item.setPadre(null);
                } else {
                    // Si no es raíz, extraer el padre del nuevo código
                    String posiblePadre = extraerPadreDeCodigo(codigoNuevo);
                    PetitorioTecnico padreItem = controller.findPetitorioTecnicoByCodigo(posiblePadre);
                    if (padreItem == null) {
                        sendErrorResponse(resp, jsonResponse, out, HttpServletResponse.SC_BAD_REQUEST,
                                "El código padre implícito no existe");
                        rollbackTransaction(tx);
                        return;
                    }
                    item.setPadre(posiblePadre);
                }
            }

            // Aplicar cambio de código si hubo cambioPadre o cambioCodigo
            if (cambioPadre || cambioCodigo) {
                String codigoAnterior = item.getCodigo();
                item.setCodigo(codigoNuevo);

                if (tieneHijos) {
                    controller.actualizarJerarquiaCompleta(codigoAnterior, codigoNuevo);
                }

                if (cambioPadre) {
                    jsonResponse.put("nuevoCodigo", codigoNuevo);
                }
            }

            // Actualizar campos comunes
            item.setNombre(nombre.trim());
            item.setCncntr(cncntr);
            item.setFormFarma(formFarma);
            item.setPrsntcn(prsntcn);
            item.setCodigoProducto(codigoProducto);
            em.merge(item);

            tx.commit();

            jsonResponse.put("success", true);
            jsonResponse.put("tieneHijos", tieneHijos);
            jsonResponse.put("message", "Item actualizado correctamente");

        } catch (JSONException e) {
            sendErrorResponse(resp, jsonResponse, out, HttpServletResponse.SC_BAD_REQUEST, "Formato JSON inválido");
            rollbackTransaction(tx);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error en doPut", e);
            sendErrorResponse(resp, jsonResponse, out, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error interno del servidor: " + e.getMessage());
            rollbackTransaction(tx);
        } finally {
            if (em != null) {
                em.close();
            }
            out.print(jsonResponse.toString());
        }
    }

    public boolean esCodigoRaiz(String codigo) {
        // Implementación según tu formato de códigos (ej: si no tiene puntos)
        return !codigo.contains(".");
    }

    public String extraerPadreDeCodigo(String codigo) {
        // Ejemplo para códigos con formato "padre.hijo"
        int lastDot = codigo.lastIndexOf('.');
        return lastDot > 0 ? codigo.substring(0, lastDot) : "";
    }

    // Métodos auxiliares
    private void sendErrorResponse(HttpServletResponse resp, JSONObject jsonResponse, PrintWriter out, int status,
            String message) {
        resp.setStatus(status);
        jsonResponse.put("error", message);
    }

    private void rollbackTransaction(EntityTransaction tx) {
        if (tx != null && tx.isActive()) {
            tx.rollback();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE);
        resp.setCharacterEncoding(CHARSET);
        PrintWriter out = resp.getWriter();
        JSONObject jsonResponse = new JSONObject();

        try {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("empr") == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonResponse.put("error", "No autorizado");
                out.print(jsonResponse.toString());
                return;
            }

            String empresa = (String) session.getAttribute("empr");
            controller = new PetitorioTecnicoJpaController(empresa);

            String codigo = req.getParameter("codigo");
            if (codigo == null || codigo.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.put("error", "Parámetro 'codigo' requerido");
                out.print(jsonResponse.toString());
                return;
            }

            // Buscar el item por su código (no por ID)
            PetitorioTecnico item = controller.findPetitorioTecnicoByCodigo(codigo);
            if (item == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                jsonResponse.put("error", "El item no existe");
                out.print(jsonResponse.toString());
                return;
            }

            // Verificar si tiene hijos antes de eliminar
            if (controller.tieneHijos(codigo)) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                jsonResponse.put("error", "No se puede eliminar, el item tiene elementos hijos");
                out.print(jsonResponse.toString());
                return;
            }

            // Eliminar usando el ID (cod) en lugar del código
            controller.destroy(item.getCod());
            jsonResponse.put("success", true);
            jsonResponse.put("message", "Item eliminado correctamente");

        } catch (NonexistentEntityException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            jsonResponse.put("error", "El item no existe");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error en doDelete", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.put("error", "Error interno del servidor");
        }

        out.print(jsonResponse.toString());
    }

    private String obtenerCuerpoRequest(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        String linea;
        try (BufferedReader br = req.getReader()) {
            while ((linea = br.readLine()) != null) {
                sb.append(linea);
            }
        }
        return sb.toString();
    }

    private JSONObject itemToJson(PetitorioTecnico item) {
        JSONObject json = new JSONObject();
        json.put("cod", item.getCod());
        json.put("codigo", item.getCodigo());
        json.put("nombre", item.getNombre());
        json.put("padre", item.getPadre());
        json.put("prsntcn", item.getPrsntcn());
        json.put("codigoProducto", item.getCodigoProducto());
        // si tiene el campo tieneHijos, se agrega
        return json;
    }

    private JSONObject objToJson(JSONObject item) {
        JSONObject json = new JSONObject();
        json.put("cod", item.optString("cod"));
        json.put("codigo", item.optString("codigo"));
        json.put("nombre", item.optString("nombre"));
        json.put("padre", item.optString("padre"));
        json.put("prsntcn", item.optString("prsntcn"));
        json.put("codigoProducto", item.optString("codigoProducto"));
        json.put("tieneHijos", item.getBoolean("tieneHijos"));
        return json;
    }
}