package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.Petitorio;
import dto.PetitorioPK;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

/**
 *
 * @author USUARIO
 */
public class PetitorioJpaController extends JpaPadre {

    public PetitorioJpaController(String empresa) {
        super(empresa);
    }

    public String getProductosPorMedicos(String codigosMedicos) {
        EntityManager em = null;
        JSONArray resultados = new JSONArray();

        try {
            JSONArray medicosArray = new JSONArray(codigosMedicos);

            // Crear estructura XML que coincide con lo que espera el SP
            JSONObject medicosJson = new JSONObject();
            medicosJson.put("Medico", medicosArray);

            // Convertir a XML con el nodo raíz correcto
            String xmlMedicos = XML.toString(medicosJson, "Medicos");

            em = getEntityManager();

            // Llamar al procedimiento almacenado
            StoredProcedureQuery query = em.createStoredProcedureQuery("usp_GetProductosPorMedicos");
            query.registerStoredProcedureParameter("XmlMedicos", String.class, ParameterMode.IN);
            query.setParameter("XmlMedicos", xmlMedicos);

            // Ejecutar y obtener resultados
            List<Object[]> resultList = query.getResultList();

            // Mapear resultados a JSON
            for (Object[] row : resultList) {
                JSONObject producto = new JSONObject();
                producto.put("codpro", row[0]);
                producto.put("despro", row[1]);
                producto.put("codgen", row[2]);
                producto.put("desgen", row[3]);
                producto.put("codlab", row[4]);
                producto.put("categvta", row[5]);
                producto.put("predesac", row[6]);
                producto.put("stock_total", row[7] != null ? row[7].toString() : "0");

                // Procesar el XML de médicos
                if (row[8] != null) {
                    String medicosXml = row[8].toString();
                    JSONObject medicosJsonObj = XML.toJSONObject(medicosXml);
                    JSONArray medicosArrayResult = medicosJsonObj.getJSONObject("Medicos").optJSONArray("Medico");
                    if (medicosArrayResult == null) {
                        // Si solo hay un médico, no viene como array
                        JSONObject singleMedico = medicosJsonObj.getJSONObject("Medicos").getJSONObject("Medico");
                        medicosArrayResult = new JSONArray();
                        medicosArrayResult.put(singleMedico);
                    }
                    producto.put("medicos", medicosArrayResult);
                } else {
                    producto.put("medicos", new JSONArray());
                }

                resultados.put(producto);
            }
            return resultados.toString();
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject error = new JSONObject();
            error.put("error", e.getMessage());
            return error.toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<String> medXespecialidad(String sercod) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sqlQuery = "select medcod from medicos_servicios where sercod = ?";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, sercod);

            List<String> resultados = query.getResultList();
            return resultados;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<String> medicosDeLaEspecialidad(String sercod) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sqlQuery = "select medcod from medicos where espcod = ? and medsta = 'S' and staff = 'S'";
            Query query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, sercod);

            List<String> resultados = query.getResultList();
            return resultados;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String agregar_petitorio_especialidad(int usecod, String jsonProductos, String jsonMedicos) {
        String result = "E"; // Valor por defecto es error
        EntityManager em = null;

        try {
            // Convertir los JSON de productos y médicos a XML
            JSONArray productosArray = new JSONArray(jsonProductos);
            JSONArray medicosArray = new JSONArray(jsonMedicos);

            // Crear objetos JSON con la estructura adecuada para la conversión a XML
            JSONObject productosJson = new JSONObject();
            productosJson.put("Registro", productosArray);

            JSONObject medicosJson = new JSONObject();
            medicosJson.put("Registro", medicosArray);

            // Convertir a XML
            String xmlProductos = XML.toString(productosJson, "Registros");
            String xmlMedicos = XML.toString(medicosJson, "Registros");

            em = getEntityManager();

            // Crear el StoredProcedureQuery para el nuevo procedimiento
            StoredProcedureQuery query = em.createStoredProcedureQuery("agregar_petitorio_especialidad");

            // Registrar parámetros según el nuevo procedimiento
            query.registerStoredProcedureParameter("XmlProductos", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("XmlMedicos", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("usecod", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("feccre", Date.class, ParameterMode.IN);

            // Configurar los parámetros
            query.setParameter("XmlProductos", xmlProductos);
            query.setParameter("XmlMedicos", xmlMedicos);
            query.setParameter("usecod", usecod);
            query.setParameter("feccre", new Date());

            // Ejecutar el procedimiento almacenado
            query.execute();
            result = "S"; // Éxito
        } catch (Exception e) {
            e.printStackTrace();
            result = "E"; // Error
            // Opcional: Puedes agregar logging más detallado aquí
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }

        return result;
    }

    public String agregar_petitorio(String medcod, int usecod, String json) {
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

            // Crear el StoredProcedureQuery
            StoredProcedureQuery query = em.createStoredProcedureQuery("agregar_petitorio");
            query.registerStoredProcedureParameter("XmlData", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("medcod", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("usecod", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("feccre", Date.class, ParameterMode.IN);

            // Configurar los parámetros del procedimiento almacenado
            query.setParameter("XmlData", xml); // XML generado a partir del JSON
            query.setParameter("medcod", medcod);
            query.setParameter("usecod", usecod);
            query.setParameter("feccre", new Date());

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

    public String listarpormedcod(String medcod) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sqlQuery = "SELECT p.codpro, p.despro, p.codgen, pa.desgen, p.codlab, p.categvta, p.predesac, SUM(COALESCE(s.stkalm,0)) + SUM(COALESCE(s.stkalm_m,0))/p.stkfra FROM petitorio pe INNER JOIN fa_productos p ON p.codpro = pe.codpro LEFT JOIN fa_genericos pa ON p.codgen = pa.codgen LEFT JOIN fa_stock_almacenes s ON s.codpro = pe.codpro WHERE pe.estado = 'S' AND pe.medcod = ? GROUP BY p.predesac, p.codpro, p.despro, p.codgen, pa.desgen, p.codlab, p.stkfra, p.categvta ORDER BY p.despro";

            Query query = em.createNativeQuery(sqlQuery);

            int parameterIndex = 1;

            query.setParameter(parameterIndex++, medcod);

            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codpro", fila[0]);
                jsonObj.put("despro", fila[1]);
                jsonObj.put("codgen", fila[2]);
                jsonObj.put("desgen", fila[3]);
                jsonObj.put("codlab", fila[4]);
                jsonObj.put("estra", fila[5]);
                boolean predesacBoolean = (fila[6] != null && "S".equals(fila[6].toString()));
                jsonObj.put("predesac", predesacBoolean);
                jsonObj.put("stock", fila[7]);
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

    public JSONArray listar() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sqlQuery = "select p.codpro,p.despro,pe.medcod,g.desgen,p.codlab,sum(COALESCE(s.stkalm,0))+sum(COALESCE(s.stkalm_m,0))/p.stkfra from petitorio pe inner join fa_productos p on p.codpro=pe.codpro left join fa_stock_almacenes s on s.codpro=pe.codpro left join fa_genericos g on g.codgen=p.codgen where pe.estado='S' group by p.codpro,p.despro,g.desgen,p.codlab,p.stkfra,pe.medcod order by pe.medcod,p.despro";

            Query query = em.createNativeQuery(sqlQuery);

            String clinica = "c";
            MedicosJpaController dao = new MedicosJpaController(clinica);
            MedicosJpaController.ResultadoMapas resultado = dao.listar();

            Map<String, String> medicosMap = resultado.getMedicosMap();
            Map<String, String> serviciosMap = resultado.getServiciosMap();
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codpro", fila[0]);
                jsonObj.put("despro", fila[1]);
                jsonObj.put("medcod", fila[2]);
                jsonObj.put("codgen", fila[3]);
                jsonObj.put("codlab", fila[4]);
                jsonObj.put("stock", fila[5]);
                String medcod = (String) fila[2];
                String mednam = medicosMap.get(medcod);
                jsonObj.put("mednam", mednam != null ? mednam : "");
                String serdes = serviciosMap.get(medcod);
                jsonObj.put("serdes", serdes != null ? serdes : "");
                jsonArray.put(jsonObj);
            }
            return jsonArray;
        } catch (Exception e) {
            return new JSONArray();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public JSONArray listarTodoEspc() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sqlQuery = "select p.codpro,p.despro,pe.medcod,g.desgen,p.codlab,sum(COALESCE(s.stkalm,0))+sum(COALESCE(s.stkalm_m,0))/p.stkfra from petitorio pe inner join fa_productos p on p.codpro=pe.codpro left join fa_stock_almacenes s on s.codpro=pe.codpro left join fa_genericos g on g.codgen=p.codgen where pe.estado='S' group by p.codpro,p.despro,g.desgen,p.codlab,p.stkfra,pe.medcod order by pe.medcod,p.despro";

            Query query = em.createNativeQuery(sqlQuery);

            String clinica = "c";
            MedicosJpaController dao = new MedicosJpaController(clinica);
            MedicosJpaController.ResultadoMapasEspecialidades resultado = dao.listarParaEspecialidades();

            Map<String, String> medicosMap = resultado.getMedicosMap();
            Map<String, String> especialidadesMap = resultado.getEspecialidadesMap();
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codpro", fila[0]);
                jsonObj.put("despro", fila[1]);
                jsonObj.put("medcod", fila[2]);
                jsonObj.put("codgen", fila[3]);
                jsonObj.put("codlab", fila[4]);
                jsonObj.put("stock", fila[5]);
                String medcod = (String) fila[2];
                String mednam = medicosMap.get(medcod);
                jsonObj.put("mednam", mednam != null ? mednam : "");
                String espdes = especialidadesMap.get(medcod);
                jsonObj.put("espdes", espdes != null ? espdes : "");
                if (mednam != null && espdes != null) {
                    jsonArray.put(jsonObj);
                }
            }
            return jsonArray;
        } catch (Exception e) {
            return new JSONArray();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public JSONArray listarporservicio() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sqlQuery = "select p.codpro,p.despro,pe.medcod,g.desgen,p.codlab,sum(COALESCE(s.stkalm,0))+sum(COALESCE(s.stkalm_m,0))/p.stkfra from petitorio pe inner join fa_productos p on p.codpro=pe.codpro left join fa_stock_almacenes s on s.codpro=pe.codpro left join fa_genericos g on g.codgen=p.codgen where pe.estado='S' group by p.codpro,p.despro,g.desgen,p.codlab,p.stkfra,pe.medcod order by pe.medcod,p.despro";

            Query query = em.createNativeQuery(sqlQuery);

            String clinica = "c";
            MedicosJpaController dao = new MedicosJpaController(clinica);
            MedicosJpaController.ResultadoMapas resultado = dao.listar();
            Map<String, String> medicosMap = resultado.getMedicosMap();
            Map<String, String> serviciosMap = resultado.getServiciosMap();

            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            Map<String, Map<String, JSONObject>> agrupadosPorSerdes = new HashMap<>();

            for (Object[] fila : resultados) {
                String codpro = (String) fila[0];
                String despro = (String) fila[1];
                String medcod = (String) fila[2];
                String codgen = (String) fila[3];
                String codlab = (String) fila[4];
                Object stock = fila[5];

                String mednam = medicosMap.get(medcod);
                String serdes = serviciosMap.get(medcod);

                // Crear un objeto JSON del producto
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codpro", codpro);
                jsonObj.put("despro", despro);
                jsonObj.put("codgen", codgen);
                jsonObj.put("codlab", codlab);
                jsonObj.put("stock", stock);

                // Agrupar por serdes, eliminando duplicados
                serdes = serdes != null ? serdes : "Sin servicio";
                agrupadosPorSerdes
                        .computeIfAbsent(serdes, k -> new HashMap<>())
                        .put(codpro, jsonObj); // Usar codpro como clave para evitar duplicados
            }

            // Convertir el mapa agrupado a un JSONArray de JSONObjects
            for (Map.Entry<String, Map<String, JSONObject>> entry : agrupadosPorSerdes.entrySet()) {

                // Convertir los valores únicos del producto a un JSONArray
                JSONArray productos = new JSONArray();
                for (JSONObject producto : entry.getValue().values()) {
                    producto.put("serdes", entry.getKey());
                    jsonArray.put(producto);
                }
            }
            return jsonArray;
        } catch (Exception e) {
            return new JSONArray();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public JSONArray listarporespecia() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sqlQuery = "select p.codpro,p.despro,pe.medcod,g.desgen,p.codlab,sum(COALESCE(s.stkalm,0))+sum(COALESCE(s.stkalm_m,0))/p.stkfra from petitorio pe inner join fa_productos p on p.codpro=pe.codpro left join fa_stock_almacenes s on s.codpro=pe.codpro left join fa_genericos g on g.codgen=p.codgen where pe.estado='S' group by p.codpro,p.despro,g.desgen,p.codlab,p.stkfra,pe.medcod order by pe.medcod,p.despro";

            Query query = em.createNativeQuery(sqlQuery);

            String clinica = "c";
            MedicosJpaController dao = new MedicosJpaController(clinica);
            MedicosJpaController.ResultadoMapasEspecialidades resultado = dao.listarParaEspecialidades();
            Map<String, String> medicosMap = resultado.getMedicosMap();
            Map<String, String> especialidadesMap = resultado.getEspecialidadesMap();

            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            Map<String, Map<String, JSONObject>> agrupadosPorEspdes = new HashMap<>();

            for (Object[] fila : resultados) {
                String codpro = (String) fila[0];
                String despro = (String) fila[1];
                String medcod = (String) fila[2];
                String codgen = (String) fila[3];
                String codlab = (String) fila[4];
                Object stock = fila[5];

                String mednam = medicosMap.get(medcod);
                String espdes = especialidadesMap.get(medcod);

                // Crear un objeto JSON del producto
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codpro", codpro);
                jsonObj.put("despro", despro);
                jsonObj.put("codgen", codgen);
                jsonObj.put("codlab", codlab);
                jsonObj.put("stock", stock);

                // Agrupar por serdes, eliminando duplicados
                espdes = espdes != null ? espdes : "Sin servicio";
                agrupadosPorEspdes
                        .computeIfAbsent(espdes, k -> new HashMap<>())
                        .put(codpro, jsonObj); // Usar codpro como clave para evitar duplicados
            }

            // Convertir el mapa agrupado a un JSONArray de JSONObjects
            for (Map.Entry<String, Map<String, JSONObject>> entry : agrupadosPorEspdes.entrySet()) {

                // Convertir los valores únicos del producto a un JSONArray
                JSONArray productos = new JSONArray();
                for (JSONObject producto : entry.getValue().values()) {
                    producto.put("espdes", entry.getKey());
                    jsonArray.put(producto);
                }
            }
            return jsonArray;
        } catch (Exception e) {
            return new JSONArray();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void create(Petitorio petitorio) throws PreexistingEntityException, Exception {
        if (petitorio.getPetitorioPK() == null) {
            petitorio.setPetitorioPK(new PetitorioPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(petitorio);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPetitorio(petitorio.getPetitorioPK()) != null) {
                throw new PreexistingEntityException("Petitorio " + petitorio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(Petitorio petitorio) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            petitorio = em.merge(petitorio);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                PetitorioPK id = petitorio.getPetitorioPK();
                if (findPetitorio(id) == null) {
                    throw new NonexistentEntityException("The petitorio with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(PetitorioPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Petitorio petitorio;
            try {
                petitorio = em.getReference(Petitorio.class, id);
                petitorio.getPetitorioPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The petitorio with id " + id + " no longer exists.", enfe);
            }
            em.remove(petitorio);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<Petitorio> findPetitorioEntities() {
        return findPetitorioEntities(true, -1, -1);
    }

    public List<Petitorio> findPetitorioEntities(int maxResults, int firstResult) {
        return findPetitorioEntities(false, maxResults, firstResult);
    }

    private List<Petitorio> findPetitorioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Petitorio.class));
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

    public Petitorio findPetitorio(PetitorioPK id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(Petitorio.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getPetitorioCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Petitorio> rt = cq.from(Petitorio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
