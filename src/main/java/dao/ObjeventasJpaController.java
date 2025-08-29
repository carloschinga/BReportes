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
import dto.Objeventas;

/**
 *
 * @author shaho
 */
public class ObjeventasJpaController extends JpaPadre {

    public ObjeventasJpaController(String empresa) {
        super(empresa);
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

    public String listarLaboratorioJson(int codobj) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select s.codlab,s.deslab,case when o.codlab is null then 'N' else 'S' end from fa_laboratorios s left join objelaboratorio o on s.codlab=o.codlab and o.codobj=? where s.estado='S'");
            query.setParameter(1, codobj);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codlab", fila[0]);
                jsonObj.put("deslab", fila[1]);
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

    public String listarEstrategicoJson(int codobj) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select s.compro,s.descripcion,case when o.compro is null then 'N' else 'S' end from estrategico s left join objeestrategico o on s.compro=o.compro and o.codobj=?");
            query.setParameter(1, codobj);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("compro", fila[0]);
                jsonObj.put("descripcion", fila[1]);
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

    public String listarEstrategicoSoloJson(int codobj) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select estra.categvta,case when o.categvta is null then 'N' else 'S' end from (select categvta from fa_productos where estado='S' group by categvta) estra left join objeestrasolo o on o.categvta=estra.categvta and o.codobj=?");
            query.setParameter(1, codobj);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("categvta", fila[0]);
                jsonObj.put("selected", fila[1]);
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

    public String listarsiscodmontos(int codobj) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select s.siscod,s.sisent,soles,entero,sum(soles) over() as sum_soles,sum(entero) over() as sum_entero from sistema s inner join objesiscod o on s.siscod=o.siscod and o.codobj=? where s.estado='S'");
            query.setParameter(1, codobj);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("siscod", fila[0]);
                jsonObj.put("sisent", fila[1]);
                jsonObj.put("soles", fila[2]);
                jsonObj.put("entero", fila[3]);
                jsonObj.put("sumsoles", fila[4]);
                jsonObj.put("sumentero", fila[5]);
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

    public String listarJson() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select o.codobj,o.desobj,o.tipo,o.feccre,o.mesano from objeventas o where o.estado='S' ");
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codobj", fila[0]);
                jsonObj.put("desobj", fila[1]);
                jsonObj.put("tipo", fila[2]);
                jsonObj.put("feccre", fila[3]);
                jsonObj.put("mesano", fila[4]);
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

    public int obtenerUltInvnum() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Integer> query = em.createNamedQuery("Objeventas.obtenerultcodobj", Integer.class);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return 0;
        } catch (Exception e) {
            return -1;
        } finally {
            if (em != null && em.isOpen())
                em.close();
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
                    query = em.createStoredProcedureQuery("editarvaloresobjsoles");
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

    public void create(Objeventas objeventas) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(objeventas);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findObjeventas(objeventas.getCodobj()) != null) {
                throw new PreexistingEntityException("Objeventas " + objeventas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(Objeventas objeventas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            objeventas = em.merge(objeventas);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = objeventas.getCodobj();
                if (findObjeventas(id) == null) {
                    throw new NonexistentEntityException("The objeventas with id " + id + " no longer exists.");
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
            Objeventas objeventas;
            try {
                objeventas = em.getReference(Objeventas.class, id);
                objeventas.getCodobj();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The objeventas with id " + id + " no longer exists.", enfe);
            }
            em.remove(objeventas);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<Objeventas> findObjeventasEntities() {
        return findObjeventasEntities(true, -1, -1);
    }

    public List<Objeventas> findObjeventasEntities(int maxResults, int firstResult) {
        return findObjeventasEntities(false, maxResults, firstResult);
    }

    private List<Objeventas> findObjeventasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Objeventas.class));
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

    public Objeventas findObjeventas(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(Objeventas.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getObjeventasCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Objeventas> rt = cq.from(Objeventas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}