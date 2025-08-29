package pe.bartolito.conta.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.JpaPadre;

public class CombosParaClasi extends JpaPadre {
    public CombosParaClasi(String empresa) {
        super(empresa);
    }

    public String getComboUniOpera() {
        EntityManager em = null;
        JSONArray json = new JSONArray();
        try {
            em = getEntityManager();

            String sql = "select * from UnidadOperativa";
            Query q = em.createNativeQuery(sql);

            List<Object[]> resultados = q.getResultList();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("id", fila[0]);
                jsonObj.put("unidad", fila[1]);
                json.put(jsonObj);
            }
            return json.toString();
        } catch (Exception e) {
            json = new JSONArray();
            return json.toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String getComboTipoGasto() {
        EntityManager em = null;
        JSONArray json = new JSONArray();
        try {
            em = getEntityManager();
            String sql = "select * from TipoGasto";
            Query q = em.createNativeQuery(sql);
            List<Object[]> resultados = q.getResultList();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("id", fila[0]);
                jsonObj.put("tipo", fila[1]);
                json.put(jsonObj);
            }
            return json.toString();
        } catch (Exception e) {
            json = new JSONArray();
            return json.toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String getComboProceso() {
        EntityManager em = null;
        JSONArray json = new JSONArray();
        try {
            em = getEntityManager();
            String sql = "select ProcId,ProcDescripcion from Proceso where ProcEstado = 1";
            Query q = em.createNativeQuery(sql);
            List<Object[]> resultados = q.getResultList();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("id", fila[0]);
                jsonObj.put("proceso", fila[1]);
                json.put(jsonObj);
            }
            return json.toString();
        } catch (Exception e) {
            json = new JSONArray();
            return json.toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String getComboActividad(String proceso) {
        EntityManager em = null;
        JSONArray json = new JSONArray();
        try {
            em = getEntityManager();
            String sql = "select ActivId,ActivDescripcion from Actividad where ProcId = ? and ActivEstado = 1";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, proceso);
            List<Object[]> resultados = q.getResultList();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("id", fila[0]);
                jsonObj.put("actividad", fila[1]);
                json.put(jsonObj);
            }
            return json.toString();
        } catch (Exception e) {
            json = new JSONArray();
            return json.toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String getComboTarea(String proceso, String actividad) {
        EntityManager em = null;
        JSONArray json = new JSONArray();
        try {
            em = getEntityManager();
            String sql = "select TareaId,TareaDescripcion from Tarea where ProcId = ? and ActivId = ? and EstadoTarea = 1";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, proceso);
            q.setParameter(2, actividad);
            List<Object[]> resultados = q.getResultList();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("id", fila[0]);
                jsonObj.put("tarea", fila[1]);
                json.put(jsonObj);
            }
            return json.toString();
        } catch (Exception e) {
            json = new JSONArray();
            return json.toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String getComboActivo() {
        EntityManager em = null;
        JSONArray json = new JSONArray();
        try {
            em = getEntityManager();
            String sql = "select ActivoId,ActivoDescripcion from Activo where ActivoEstado=1";
            Query q = em.createNativeQuery(sql);
            List<Object[]> resultados = q.getResultList();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("id", fila[0]);
                jsonObj.put("activo", fila[1]);
                json.put(jsonObj);
            }
            return json.toString();
        } catch (Exception e) {
            json = new JSONArray();
            return json.toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String getComboProductoServicio() {
        EntityManager em = null;
        JSONArray json = new JSONArray();
        try {
            em = getEntityManager();
            String sql = "select ProdId,ProdDescripcion from ProductoServicio where ProdEstado=1";
            Query q = em.createNativeQuery(sql);
            List<Object[]> resultados = q.getResultList();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("id", fila[0]);
                jsonObj.put("producto", fila[1]);
                json.put(jsonObj);
            }
            return json.toString();
        } catch (Exception e) {
            json = new JSONArray();
            return json.toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String getComboCentroCostoResponsabilidad() {
        EntityManager em = null;
        JSONArray json = new JSONArray();
        try {
            em = getEntityManager();
            String sql = "select CenCostResp,CenCostRespDescripcion from CentroCostoResponsabilidad where CenCostRespEstado = 1";
            Query q = em.createNativeQuery(sql);
            List<Object[]> resultados = q.getResultList();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("id", fila[0]);
                jsonObj.put("costo", fila[1]);
                json.put(jsonObj);
            }
            return json.toString();
        } catch (Exception e) {
            json = new JSONArray();
            return json.toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String getComboEmpresa() {
        EntityManager em = null;
        JSONObject response = new JSONObject();
        JSONArray dataArray = new JSONArray();

        try {
            em = getEntityManager();
            String sql = "select EmpresaId, EmpresaDescripcion from Empresa";
            Query q = em.createNativeQuery(sql);
            List<Object[]> resultados = q.getResultList();

            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("id", fila[0]);
                jsonObj.put("empresa", fila[1]);
                dataArray.put(jsonObj);
            }

            // Estructura la respuesta como espera el frontend
            response.put("resultado", "ok");
            response.put("data", dataArray);

            return response.toString();

        } catch (Exception e) {
            response.put("resultado", "error");
            response.put("mensaje", e.getMessage());
            return response.toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }
}
