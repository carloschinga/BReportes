package dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.ListaFamilia;
import dto.ListaGenerico;
import dto.ListaLab;
import dto.ListaProducto;
import dto.RestriccionesDistribucion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

public class RestriccionesJpaController extends JpaPadre {

    public RestriccionesJpaController(String empresa) {
        super(empresa);
    }

    public String listarAlmacenes() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sql = "SELECT codalm, desalm FROM listaalmacenes WHERE codalm NOT IN ('A1', 'A2') FOR JSON PATH";
            return (String) em.createNativeQuery(sql).getSingleResult();

        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarLaboratorios() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sql = "SELECT codlab, deslab FROM view_lista_laboratorio";
            Query query = em.createNativeQuery(sql);

            @SuppressWarnings("unchecked")
            List<Object[]> resultados = query.getResultList();
            List<ListaLab> listaLaboratorios = new ArrayList<>();

            for (Object[] resultado : resultados) {
                String codlab = (String) resultado[0];
                String deslab = (String) resultado[1];
                listaLaboratorios.add(new ListaLab(codlab, deslab));
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(listaLaboratorios);

        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarFamilias() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sql = "SELECT codfam, desfam FROM fa_familias";
            Query query = em.createNativeQuery(sql);

            @SuppressWarnings("unchecked")
            List<Object[]> resultados = query.getResultList();
            List<ListaFamilia> listaFamilias = new ArrayList<>();

            for (Object[] resultado : resultados) {
                String codfam = (String) resultado[0];
                String desfam = (String) resultado[1];
                listaFamilias.add(new ListaFamilia(codfam, desfam));
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(listaFamilias);

        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarGenericos() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sql = "SELECT codgen, desgen FROM fa_genericos WHERE estado='S'";
            Query query = em.createNativeQuery(sql);

            @SuppressWarnings("unchecked")
            List<Object[]> resultados = query.getResultList();
            List<ListaGenerico> listaGenericos = new ArrayList<>();

            for (Object[] resultado : resultados) {
                String codgen = (String) resultado[0];
                String desgen = (String) resultado[1];
                listaGenericos.add(new ListaGenerico(codgen, desgen));
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(listaGenericos);

        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarProductos(String codlab, String codgen, String codfam, String codtip, String codpro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            // Construir la consulta JPQL dinámicamente
            StringBuilder jpql = new StringBuilder(
                    "SELECT f.codpro, f.despro FROM FaProductos f WHERE f.estado='S' and 1=1");

            if (!codlab.equals("")) {
                jpql.append(" AND f.codlab = :codlab");
            }
            if (!codgen.equals("")) {
                jpql.append(" AND f.codgen = :codgen");
            }
            if (!codfam.equals("")) {
                jpql.append(" AND f.codfam = :codfam");
            }
            if (!codtip.equals("")) {
                jpql.append(" AND f.codtip = :codtip");
            }
            if (!codpro.equals("")) {
                jpql.append(" AND f.codpro = :codpro");
            }
            Query query = em.createQuery(jpql.toString());

            // Establecer parámetros en la consulta
            if (!codlab.equals("")) {
                query.setParameter("codlab", codlab);
            }
            if (!codgen.equals("")) {
                query.setParameter("codgen", codgen);
            }
            if (!codfam.equals("")) {
                query.setParameter("codfam", codfam);
            }
            if (!codtip.equals("")) {
                query.setParameter("codtip", codtip);
            }

            if (!codpro.equals("")) {
                query.setParameter("codpro", codpro);
            }
            List<Object[]> productos = query.getResultList();
            List<ListaProducto> listaProducto = new ArrayList<>();

            for (Object[] resultado : productos) {
                String codpro1 = (String) resultado[0];
                String despro = (String) resultado[1];
                listaProducto.add(new ListaProducto(codpro1, despro));
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.toJson(listaProducto);

        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarRestriciones(String codlab, String codgen, String codfam, String codtip, String codpro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            StringBuilder jpql = new StringBuilder(
                    "SELECT r.codpro, r.codalm FROM restricciones_distribucion r WITH (NOLOCK) INNER JOIN fa_productos p WITH (NOLOCK) ON r.codpro = p.codpro WHERE 1=1");

            if (!codlab.isEmpty()) {
                jpql.append(" AND p.codlab = ?");
            }
            if (!codgen.isEmpty()) {
                jpql.append(" AND p.codgen = ?");
            }
            if (!codfam.isEmpty()) {
                jpql.append(" AND p.codfam = ?");
            }
            if (!codtip.isEmpty()) {
                jpql.append(" AND p.codtip = ?");
            }
            if (!codpro.isEmpty()) {
                jpql.append(" AND p.codpro = ?");
            }

            Query query = em.createNativeQuery(jpql.toString());
            int parameterIndex = 1;

            // Establecer parámetros en la consulta
            if (!codlab.isEmpty()) {
                query.setParameter(parameterIndex++, codlab);
            }
            if (!codgen.isEmpty()) {
                query.setParameter(parameterIndex++, codgen);
            }
            if (!codfam.isEmpty()) {
                query.setParameter(parameterIndex++, codfam);
            }
            if (!codtip.isEmpty()) {
                query.setParameter(parameterIndex++, codtip);
            }
            if (!codpro.isEmpty()) {
                query.setParameter(parameterIndex++, codpro);
            }

            List<Object[]> resultados = query.getResultList();

            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codpro", fila[0]);
                jsonObj.put("codalm", fila[1]);
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

    public String aplicarRestriccionJSON(String codpro, String codalm, String estado) {
        try {
            RestriccionesDistribucionJpaController dao = new RestriccionesDistribucionJpaController(empresa);
            if (estado.equals("true")) {
                RestriccionesDistribucion obj = dao.encontrarRestricciones(codpro, codalm);
                if (obj != null) {
                    dao.destroy(obj.getRestriccionesDistribucionPK());
                    return "{\"resultado\":\"ok\"}";
                } else {
                    return "{\"resultado\":\"error\",\"mensaje\":\"No se encontro el registro\"}";
                }
            } else {
                RestriccionesDistribucion obj = dao.encontrarRestricciones(codpro, codalm);
                if (obj == null) {
                    RestriccionesDistribucion obj1 = new RestriccionesDistribucion(codpro, codalm);
                    dao.create(obj1);
                    return "{\"resultado\":\"ok\"}";
                } else {
                    return "{\"resultado\":\"error\",\"mensaje\":\"Ya se encontro el registro\"}";
                }
            }
        } catch (Exception e) {
            return "{\"resultado\":\"error\",\"mensaje\":\"" + e + "\"}";
        }
    }

    public String aplicarRestriccionLista(String json, String estado) {
        String result = "E"; // Default to error
        EntityManager em = null;
        try {
            em = getEntityManager();

            StoredProcedureQuery query = em.createStoredProcedureQuery("InsertOrDeleteRestriccionesDistribucion");
            query.registerStoredProcedureParameter("xml", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("action", String.class, ParameterMode.IN);

            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("item", jsonArray);
            String xml = XML.toString(jsonObject, "items");
            query.setParameter("xml", xml);
            query.setParameter("action", estado.equals("true") ? "D" : "I");

            query.execute();
            result = "S";
        } catch (Exception e) {
            e.printStackTrace();
            result = "E"; // Error
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
        return result;
    }
}
