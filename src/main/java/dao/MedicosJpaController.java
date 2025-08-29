package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.Medicos;

/**
 *
 * @author USUARIO
 */
public class MedicosJpaController extends JpaPadre {

    public MedicosJpaController(String empresa) {
        super(empresa);
    }

    public String listarjsonspecialidades() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sql = "select sercod, serdes from servicios where estado = 'S'";
            Query q = em.createNativeQuery(sql);
            List<Object[]> resultados = q.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("sercod", fila[0]);
                jsonObj.put("serdes", fila[1]);
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

    public String listarjsonespcods() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sql = "select espcod, espdes from especialidades where estado = 'S'";
            Query q = em.createNativeQuery(sql);
            List<Object[]> resultados = q.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("espcod", fila[0]);
                jsonObj.put("espdes", fila[1]);
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

    public String listar_porlistaservicios(String body) {
        EntityManager em = null;

        try {
            JSONObject json = new JSONObject(body);

            String xmlservicios = XML.toString(
                    new JSONObject().put("Registro", new JSONArray(json.get("servicios").toString())), "Registros");

            em = getEntityManager();

            StoredProcedureQuery query = em.createStoredProcedureQuery("select_medicos_by_servicios");
            query.registerStoredProcedureParameter("servicios", String.class, ParameterMode.IN);

            query.setParameter("servicios", xmlservicios);

            List<Object[]> results = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : results) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("medcod", fila[0]);
                jsonObj.put("mednam", fila[1]);
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

    public String listar_porlistaespcod(String body) {
        EntityManager em = null;

        try {
            JSONObject json = new JSONObject(body);

            String xmlservicios = XML.toString(
                    new JSONObject().put("Registro", new JSONArray(json.get("servicios").toString())), "Registros");

            em = getEntityManager();

            StoredProcedureQuery query = em.createStoredProcedureQuery("select_medicos_by_especialidades");
            query.registerStoredProcedureParameter("servicios", String.class, ParameterMode.IN);

            query.setParameter("servicios", xmlservicios);

            List<Object[]> results = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : results) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("medcod", fila[0]);
                jsonObj.put("mednam", fila[1]);
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

    public String listarporservicio(String sercod) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sqlQuery = "select c.medcod,c.mednam from medicos c inner join medicos_servicios s on s.medcod=c.medcod where s.sercod=? order by c.mednam";

            Query query = em.createNativeQuery(sqlQuery);

            int parameterIndex = 1;

            query.setParameter(parameterIndex++, sercod);

            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("medcod", fila[0]);
                jsonObj.put("mednam", fila[1]);
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

    public String listarjson() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sqlQuery = "select c.medcod,c.mednam from medicos c where c.estado = 'S' order by c.mednam";

            Query query = em.createNativeQuery(sqlQuery);

            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("medcod", fila[0]);
                jsonObj.put("mednam", fila[1]);
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

    public String listadoMedicoStaffjson() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sqlQuery = "select c.medcod,c.mednam from medicos c where medsta = 'S' and staff = 'S'";

            Query query = em.createNativeQuery(sqlQuery);

            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("medcod", fila[0]);
                jsonObj.put("mednam", fila[1]);
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

    public ResultadoMapas listar() { // Método exclusivo para concatenar con medcod
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sqlQuery = "select c.medcod, c.mednam, se.serdes "
                    + "from medicos c "
                    + "inner join medicos_servicios s on s.medcod = c.medcod and s.estado = 'S' "
                    + "inner join servicios se on se.sercod = s.sercod "
                    + "order by c.mednam";

            Query query = em.createNativeQuery(sqlQuery);

            List<Object[]> resultados = query.getResultList();
            Map<String, String> medicosMap = new HashMap<>();
            Map<String, String> serviciosMap = new HashMap<>();

            for (Object[] fila : resultados) {
                medicosMap.put((String) fila[0], (String) fila[1]); // medcod -> mednam
                serviciosMap.put((String) fila[0], (String) fila[2]); // medcod -> serdes
            }

            return new ResultadoMapas(medicosMap, serviciosMap);
        } catch (Exception e) {
            return null; // Manejar excepciones adecuadamente en un entorno real
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public ResultadoMapasEspecialidades listarParaEspecialidades() { // Método exclusivo para concatenar con medcod
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sqlQuery = "select c.medcod, c.mednam, es.espdes "
                    + "from medicos c "
                    + "inner join especialidades es on es.espcod = c.espcod "
                    + "where es.estado = 'S' and c.estado = 'S' and c.medsta = 'S' and c.staff = 'S' "
                    + "order by c.mednam";

            Query query = em.createNativeQuery(sqlQuery);

            List<Object[]> resultados = query.getResultList();
            Map<String, String> medicosMap = new HashMap<>();
            Map<String, String> especialidadesMap = new HashMap<>();

            for (Object[] fila : resultados) {
                medicosMap.put((String) fila[0], (String) fila[1]); // medcod -> mednam
                especialidadesMap.put((String) fila[0], (String) fila[2]); // medcod -> espdes
            }

            return new ResultadoMapasEspecialidades(medicosMap, especialidadesMap);
        } catch (Exception e) {
            return null; // Manejar excepciones adecuadamente en un entorno real
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public class ResultadoMapas {

        private Map<String, String> medicosMap;
        private Map<String, String> serviciosMap;

        public ResultadoMapas(Map<String, String> medicosMap, Map<String, String> serviciosMap) {
            this.medicosMap = medicosMap;
            this.serviciosMap = serviciosMap;
        }

        public Map<String, String> getMedicosMap() {
            return medicosMap;
        }

        public Map<String, String> getServiciosMap() {
            return serviciosMap;
        }
    }

    public class ResultadoMapasEspecialidades {

        private Map<String, String> medicosMap;
        private Map<String, String> especialidadesMap;

        public ResultadoMapasEspecialidades(Map<String, String> medicosMap, Map<String, String> especialidadesMap) {
            this.medicosMap = medicosMap;
            this.especialidadesMap = especialidadesMap;
        }

        public Map<String, String> getMedicosMap() {
            return medicosMap;
        }

        public Map<String, String> getEspecialidadesMap() {
            return especialidadesMap;
        }
    }

    public void create(Medicos medicos) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(medicos);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMedicos(medicos.getMedcod()) != null) {
                throw new PreexistingEntityException("Medicos " + medicos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(Medicos medicos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            medicos = em.merge(medicos);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = medicos.getMedcod();
                if (findMedicos(id) == null) {
                    throw new NonexistentEntityException("The medicos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Medicos medicos;
            try {
                medicos = em.getReference(Medicos.class, id);
                medicos.getMedcod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medicos with id " + id + " no longer exists.", enfe);
            }
            em.remove(medicos);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<Medicos> findMedicosEntities() {
        return findMedicosEntities(true, -1, -1);
    }

    public List<Medicos> findMedicosEntities(int maxResults, int firstResult) {
        return findMedicosEntities(false, maxResults, firstResult);
    }

    private List<Medicos> findMedicosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Medicos.class));
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

    public Medicos findMedicos(String id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(Medicos.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getMedicosCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Medicos> rt = cq.from(Medicos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
