package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolationException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.HechCuotaVenta;

/**
 *
 * @author USUARIO
 */
public class HechCuotaVentaJpaController extends JpaPadre {

    public HechCuotaVentaJpaController(String empresa) {
        super(empresa);
    }

    public void create(HechCuotaVenta hechCuota) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(hechCuota);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHechCuotaVenta(hechCuota.getCuotVtaId()) != null) {
                throw new PreexistingEntityException("HechCuotaVenta " + hechCuota + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(HechCuotaVenta hechCuota) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            hechCuota = em.merge(hechCuota);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = hechCuota.getCuotVtaId();
                if (findHechCuotaVenta(id) == null) {
                    throw new NonexistentEntityException("The hechCuota with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HechCuotaVenta hechCuota;
            try {
                hechCuota = em.getReference(HechCuotaVenta.class, id);
                hechCuota.getCuotVtaId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The hechCuota with id " + id + " no longer exists.", enfe);
            }
            em.remove(hechCuota);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<HechCuotaVenta> findHechCuotaEntities() {
        return findHechCuotaEntities(true, -1, -1);
    }

    public List<HechCuotaVenta> findHechCuotaEntities(int maxResults, int firstResult) {
        return findHechCuotaEntities(false, maxResults, firstResult);
    }

    private List<HechCuotaVenta> findHechCuotaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HechCuotaVenta.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public HechCuotaVenta findHechCuotaVenta(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(HechCuotaVenta.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getHechCuotaCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HechCuotaVenta> rt = cq.from(HechCuotaVenta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarJson() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "SELECT CuotVtaId, desobj, tipo, feccre, mesano, CAST(hecAct AS INT) as hecAct " +
                            "FROM HechCuotaVenta WHERE estado = 'S'");

            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();

            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codobj", fila[0]);
                jsonObj.put("desobj", fila[1]);
                jsonObj.put("tipo", fila[2]);
                jsonObj.put("feccre", fila[3]);
                jsonObj.put("mesano", fila[4]);
                jsonObj.put("hecAct", ((Number) fila[5]).intValue() == 1);
                jsonArray.put(jsonObj);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            return "{\"resultado\":\"error\",\"mensaje\":\"" +
                    e.getMessage().replace("\"", "\\\"") + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarsiscodJson(int codobj) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "SELECT s.siscod, s.sisent, CASE WHEN EXISTS (SELECT 1 FROM objesiscod WHERE codobj = ?) THEN CASE WHEN o.siscod IS NULL THEN 'N' ELSE 'S' END ELSE CASE WHEN a.central = 'S' THEN 'N' ELSE 'S' END END AS resultado from sistema s left join objesiscod o on s.siscod=o.siscod and o.codobj=? inner join fa_almacenes a on a.siscod=s.siscod where s.estado='S' group by s.siscod, s.sisent,a.central,o.siscod");
            query.setParameter(1, codobj);
            query.setParameter(2, codobj);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("siscod", fila[0]);
                jsonObj.put("sisent", fila[1]);
                jsonObj.put("selected", fila[2]);
                jsonArray.put(jsonObj);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarSolosiscodJson(int codobj) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select s.siscod,s.sisent from sistema s inner join objesiscod o on s.siscod=o.siscod and o.codobj=? where s.estado='S'");
            query.setParameter(1, codobj);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("siscod", fila[0]);
                jsonObj.put("sisent", fila[1]);
                jsonArray.put(jsonObj);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarsiscodmontos(int obj) {
        EntityManager em = null;
        JSONArray jsonArray = new JSONArray(); // Siempre devolverá un array (vacío si hay error)

        try {
            em = getEntityManager();
            // Consulta SQL con manejo de NULLs usando COALESCE
            String sql = "SELECT " +
                    "    COALESCE(SucurId, '') AS SucurId, " +
                    "    COALESCE(CuotVtaMeta, 0) AS CuotVtaMeta, " +
                    "    COALESCE(CuotVtaCant, 0) AS CuotVtaCant, " +
                    "    COALESCE(SUM(CuotVtaMeta) OVER(), 0) AS TotalMeta, " +
                    "    COALESCE(SUM(CuotVtaCant) OVER(), 0) AS TotalCant, " +
                    "    COALESCE(porc_estra, 0) AS porc_estra " +
                    "FROM HechCuotaVentaMes where CuotVtaId = ?";

            Query query = em.createNativeQuery(sql);
            query.setParameter(1, obj);
            List<Object[]> resultados = query.getResultList();

            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                // Manejo seguro de cada campo (incluso si viene null desde la consulta)
                jsonObj.put("id", fila[0] != null ? fila[0] : 0);
                jsonObj.put("monto", fila[1] != null ? fila[1] : 0);
                jsonObj.put("cantidad", fila[2] != null ? fila[2] : 0);
                jsonObj.put("sumsoles", fila[3] != null ? fila[3] : 0);
                jsonObj.put("sumentero", fila[4] != null ? fila[4] : 0);
                jsonObj.put("porc_estra", fila[5] != null ? fila[5] : 0);
                jsonArray.put(jsonObj);
            }

        } catch (Exception e) {
            System.err.println("Error en listarsiscodmontos: " + e.getMessage());
            // Devuelve array con un mensaje de error
            jsonArray.put(new JSONObject().put("error", "Error al obtener los datos: " + e.getMessage()));
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
        return jsonArray.toString(); // Siempre será un JSON array válido
    }

    public static void main(String[] args) {
        HechCuotaVentaJpaController controller = new HechCuotaVentaJpaController("d");
        String array = controller.listarsiscodmontos(6);

        System.out.println(array);
    }

    public String actualizar_productos(String json, int obj) {
        String result = "E";
        EntityManager em = null;

        try {
            JSONArray jsonArray = new JSONArray(json);
            StringBuilder xmlBuilder = new StringBuilder();
            xmlBuilder.append("<Registros>");
            for (int i = 0; i < jsonArray.length(); i++) {
                xmlBuilder.append("<Registro>")
                        .append(jsonArray.getInt(i))
                        .append("</Registro>");
            }
            xmlBuilder.append("</Registros>");

            em = getEntityManager();
            StoredProcedureQuery query = em.createStoredProcedureQuery("InsertarCuotasVentaMes");

            // Registrar parámetros
            query.registerStoredProcedureParameter("CuotVtaId", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("XmlSucursales", String.class, ParameterMode.IN);

            // Establecer valores
            query.setParameter("CuotVtaId", obj); // obj se asigna a CuotVtaId
            query.setParameter("XmlSucursales", xmlBuilder.toString());

            query.execute();
            result = "OK";

        } catch (Exception e) {
            e.printStackTrace();
            result = "E: " + e.getMessage();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
        return result;
    }

    public JSONObject obtenerMesYAño(int codobj) {
        EntityManager em = null;
        JSONObject jsonObj = new JSONObject();
        try {
            em = getEntityManager();
            String sql = "SELECT mesano FROM HechCuotaVenta WHERE codobj = ?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, codobj);

            Object result = query.getSingleResult();
            if (result == null) {
                throw new Exception("No se encontró registro para codobj: " + codobj);
            }

            String mesano = result.toString();

            // Validar formato (AAAA-MM)
            if (!mesano.matches("^\\d{4}-(0[1-9]|1[0-2])$")) {
                throw new Exception("Formato de mesano inválido. Debe ser AAAA-MM: " + mesano);
            }

            // Extraer y convertir partes
            String[] partes = mesano.split("-");
            int ano = Integer.parseInt(partes[0]); // Año como entero (ej: 2025)
            int mes = Integer.parseInt(partes[1]); // Mes como entero (ej: "01" → 1)

            // Validar rangos
            if (mes < 1 || mes > 12) {
                throw new Exception("Mes inválido. Debe ser entre 1 y 12: " + mes);
            }

            jsonObj.put("mes", mes);
            jsonObj.put("ano", ano);

        } catch (NoResultException e) {
            System.err.println("No se encontró resultado: " + e.getMessage());
            jsonObj.put("error", "Registro no encontrado");
        } catch (NumberFormatException e) {
            System.err.println("Error en conversión numérica: " + e.getMessage());
            jsonObj.put("error", "Formato de fecha inválido");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            jsonObj.put("error", e.getMessage());
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
        return jsonObj;
    }

    public String listarTipoJson(int codtip) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select s.codtip,s.destip,case when o.codtip is null then 'N' else 'S' end from fa_tipos s left join objetipo o on s.codtip=o.codtip and o.codobj=? where s.estado='S'");
            query.setParameter(1, codtip);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codtip", fila[0]);
                jsonObj.put("destip", fila[1]);
                jsonObj.put("selected", fila[2]);
                jsonArray.put(jsonObj);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String activarHechCuotaVenta(int codobj) {
        String result = "E"; // Valor por defecto es error
        EntityManager em = null;

        try {
            em = getEntityManager();
            StoredProcedureQuery query = em.createStoredProcedureQuery("ActivarHechCuota");

            // Registrar parámetros
            query.registerStoredProcedureParameter("codobj", Integer.class, ParameterMode.IN);

            // Establecer valores
            query.setParameter("codobj", codobj);

            // Ejecutar el procedimiento
            query.execute();

            // Obtener resultados
            List<Object[]> results = query.getResultList();
            if (!results.isEmpty()) {
                Object[] row = results.get(0);
                int registrosActivados = ((Number) row[0]).intValue();
                int totalActivos = ((Number) row[1]).intValue();

                if (registrosActivados > 0) {
                    result = "OK";
                } else {
                    result = "E: No se encontró el registro con codobj=" + codobj;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = "E: " + e.getMessage();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return result;
    }

    public String listarProductosJson(int codobj) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select p.codpro,p.despro,case when o.codpro is null then 'N' else 'S' end from fa_productos p left join objeespmontos o on o.codpro=p.codpro and o.codobj=? where p.estado='S' ");
            query.setParameter(1, codobj);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codpro", fila[0]);
                jsonObj.put("despro", fila[1]);
                jsonObj.put("selected", fila[2]);
                jsonArray.put(jsonObj);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int obtenerUltInvnum() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Integer> query = em.createNamedQuery("HechCuotaVenta.obtenerultcodobj", Integer.class);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return 0;
        } catch (Exception e) {
            return -1;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public String actualizar_productos(int codobj, String tipo, String json, String mesano) {
        String result = "E"; // Valor por defecto es error
        EntityManager em = null;

        try {
            // Convertir el JSON a un JSONArray
            JSONArray jsonArray = new JSONArray(json);

            // Crear un nuevo JSONObject con el nombre de la raíz "Registros"
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Registro", jsonArray);

            // Convertir el JSONObject a XML con la raíz "Registros"
            String xml = XML.toString(jsonObject, "Registros");

            em = getEntityManager();

            StoredProcedureQuery query;
            switch (tipo) {
                case "lab":
                    query = em.createStoredProcedureQuery("agregar_obj_lab");
                    break;
                case "tip":
                    query = em.createStoredProcedureQuery("agregar_obj_tip");
                    break;
                case "estab":
                    query = em.createStoredProcedureQuery("agregar_obj_siscod");
                    break;
                case "estra":
                    query = em.createStoredProcedureQuery("agregar_obj_estrategico");
                    break;
                case "estrasolo":
                    query = em.createStoredProcedureQuery("agregar_obj_estrategicosolo");
                    break;
                case "producto":
                    query = em.createStoredProcedureQuery("agregar_obj_producto");
                    break;
                default:
                    return "E";
            }

            query.registerStoredProcedureParameter("XmlData", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("codobj", Integer.class, ParameterMode.IN);

            if ("estab".equals(tipo)) {
                query.registerStoredProcedureParameter("mesano", String.class, ParameterMode.IN);
            }

            if ("producto".equals(tipo)) {
                query.registerStoredProcedureParameter("resultMessage", String.class, ParameterMode.OUT);
            }

            if ("estab".equals(tipo)) {
                query.setParameter("mesano", mesano);
            }

            // Configurar los parámetros del procedimiento almacenado
            query.setParameter("XmlData", xml); // XML generado a partir del JSON
            query.setParameter("codobj", codobj);

            // Ejecutar el procedimiento almacenado
            query.execute();

            // Recuperar el valor del parámetro de salida
            if ("producto".equals(tipo)) {
                return (String) query.getOutputParameterValue("resultMessage");
            } else {
                return "S";
            }
        } catch (PersistenceException e) {
            // Verificar si es una violación de UNIQUE KEY
            if (e.getCause() instanceof ConstraintViolationException ||
                    e.getMessage().contains("Violation of UNIQUE KEY constraint")) {
                return "DUPLICADO"; // Código especial para duplicados
            }
            e.printStackTrace();
            result = "E";
        } catch (Exception e) {
            e.printStackTrace();
            result = "E"; // Error
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
        return result;
    }

    public String vista_previa(String jsonlab, String jsontip, String jsonestra, String jsonestrasolo) {
        EntityManager em = null;

        try {
            JSONArray jsonArray = new JSONArray(jsonlab);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Registro", jsonArray);
            String xmllab = XML.toString(jsonObject, "Registros");
            jsonArray = new JSONArray(jsontip);
            jsonObject = new JSONObject();
            jsonObject.put("Registro", jsonArray);
            String xmltip = XML.toString(jsonObject, "Registros");
            jsonArray = new JSONArray(jsonestra);
            jsonObject = new JSONObject();
            jsonObject.put("Registro", jsonArray);
            String xmlestra = XML.toString(jsonObject, "Registros");
            jsonArray = new JSONArray(jsonestrasolo);
            jsonObject = new JSONObject();
            jsonObject.put("Registro", jsonArray);
            String xmlestrasolo = XML.toString(jsonObject, "Registros");

            em = getEntityManager();

            StoredProcedureQuery query = em.createStoredProcedureQuery("vista_previa_objetivo");
            query.registerStoredProcedureParameter("lab", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("tip", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("estra", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("estrasolo", String.class, ParameterMode.IN);

            // Configurar los parámetros del procedimiento almacenado
            query.setParameter("lab", xmllab);
            query.setParameter("tip", xmltip);
            query.setParameter("estra", xmlestra);
            query.setParameter("estrasolo", xmlestrasolo);
            List<Object[]> results = query.getResultList();
            jsonArray = new JSONArray();
            for (Object[] fila : results) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codpro", fila[0]);
                jsonObj.put("despro", fila[1]);
                jsonArray.put(jsonObj);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "E"; // Error
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String editarValoresObjetosSoles(int codobj, String tipo, String json) {
        String result = "E"; // Valor por defecto es error
        EntityManager em = null;

        try {
            // Convertir el JSON a un JSONArray
            JSONArray jsonArray = new JSONArray(json);

            // Crear un nuevo JSONObject con el nombre de la raíz "Registros"
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Registro", jsonArray);

            // Convertir el JSONObject a XML con la raíz "Registros"
            String xml = XML.toString(jsonObject, "Registros");

            em = getEntityManager();

            StoredProcedureQuery query;
            switch (tipo) {
                case "entero":
                    query = em.createStoredProcedureQuery("editarvaloresobj");
                    break;
                case "soles":
                    query = em.createStoredProcedureQuery("editarvaloresobjsolessigol");
                    break;
                default:
                    return "N"; // Tipo no válido
            }

            query.registerStoredProcedureParameter("XmlData", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("codobj", Integer.class, ParameterMode.IN);

            // Configurar los parámetros del procedimiento almacenado
            query.setParameter("XmlData", xml); // XML generado a partir del JSON
            query.setParameter("codobj", codobj);

            // Ejecutar el procedimiento almacenado
            query.execute();
            result = "S"; // Éxito
        } catch (Exception e) {
            e.printStackTrace();
            result = "E"; // Error
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
        return result;
    }

    public String editarValoresObjetosSolesEspecifico(int codobj, String tipo, String json) {
        String result = "E"; // Valor por defecto es error
        EntityManager em = null;

        try {
            // Convertir el JSON a un JSONArray
            JSONArray jsonArray = new JSONArray(json);

            // Crear un nuevo JSONObject con el nombre de la raíz "Registros"
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Registro", jsonArray);

            // Convertir el JSONObject a XML con la raíz "Registros"
            String xml = XML.toString(jsonObject, "Registros");

            em = getEntityManager();

            StoredProcedureQuery query;
            switch (tipo) {
                case "entero":
                    query = em.createStoredProcedureQuery("editarvaloresobjdetalle");
                    break;
                case "soles":
                    query = em.createStoredProcedureQuery("editarvaloresobjdetallesoles");
                    break;
                default:
                    return "N"; // Tipo no válido
            }

            query.registerStoredProcedureParameter("XmlData", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("codobj", Integer.class, ParameterMode.IN);

            // Configurar los parámetros del procedimiento almacenado
            query.setParameter("XmlData", xml); // XML generado a partir del JSON
            query.setParameter("codobj", codobj);

            // Ejecutar el procedimiento almacenado
            query.execute();
            result = "S"; // Éxito
        } catch (Exception e) {
            e.printStackTrace();
            result = "E"; // Error
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
        return result;
    }

    public String listarSoloProductosJson(int codobj) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select p.codpro,p.despro from fa_productos p inner join objeespmontos o on o.codpro=p.codpro and o.codobj=? where p.estado='S' ");
            query.setParameter(1, codobj);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codpro", fila[0]);
                jsonObj.put("despro", fila[1]);
                jsonArray.put(jsonObj);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarSoloProductosJsonmontosesta(int codobj) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select p.codpro,p.codobj,p.siscod,p.entero,p.soles from objeespmontosesta p where p.codobj=?");
            query.setParameter(1, codobj);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codpro", fila[0]);
                jsonObj.put("codobj", fila[1]);
                jsonObj.put("siscod", fila[2]);
                jsonObj.put("entero", fila[3]);
                jsonObj.put("soles", fila[4]);
                jsonArray.put(jsonObj);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
