/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.clinica.dao;

import dao.JpaPadre;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import pe.bartolito.clinica.dao.exceptions.NonexistentEntityException;
import pe.bartolito.clinica.dao.exceptions.PreexistingEntityException;
import pe.bartolito.clinica.dto.HechRecetasCli;
import pe.bartolito.clinica.dto.HechRecetasCliPK;

/**
 *
 * @author USUARIO
 */
public class HechRecetasCliJpaController extends JpaPadre {

    public HechRecetasCliJpaController(String empresa) {
        super(empresa);
    }

    public String actualizar_cant_impr(int actomedico) {
        String result = "E"; // Valor por defecto es error
        EntityManager em = null;

        try {

            em = getEntityManager();

            StoredProcedureQuery query;
            query = em.createStoredProcedureQuery("IncrementarCantImpr");

            query.registerStoredProcedureParameter("actomedico", Integer.class, ParameterMode.IN);
            // Configurar los parámetros del procedimiento almacenado
            query.setParameter("actomedico", actomedico);

            // Ejecutar el procedimiento almacenado
            query.execute();

            return "S";

        } catch (Exception e) {
            e.printStackTrace();
            result = "E"; // Error
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return result;
    }
    
    public String listar_diagnostico(String body) {
        EntityManager em = null;

        try {
            JSONObject json = new JSONObject(body);

            String xmlservicios = XML.toString(new JSONObject().put("Registro", new JSONArray(json.get("servicios").toString())), "Registros");
            String xmlmedicos = XML.toString(new JSONObject().put("Registro", new JSONArray(json.get("medicos").toString())), "Registros");
            String fechainicio = json.get("fechainicio").toString();
            String fechafin = json.get("fechafin").toString();
            String paciente = json.get("paciente").toString();

            em = getEntityManager();

            StoredProcedureQuery query = em.createStoredProcedureQuery("select_diagnostico");
            query.registerStoredProcedureParameter("servicios", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("paciente", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("medicos", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("fechainicio", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("fechafin", String.class, ParameterMode.IN);

            query.setParameter("servicios", xmlservicios);
            query.setParameter("paciente", paciente);
            query.setParameter("medicos", xmlmedicos);
            query.setParameter("fechainicio", fechainicio);
            query.setParameter("fechafin", fechafin);

            List<Object[]> results = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : results) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("FechaAtencion", fila[0]);
                jsonObj.put("ActoMedico", fila[1]);
                jsonObj.put("Prefactura", fila[2]);
                jsonObj.put("Servicio", fila[3]);
                jsonObj.put("Medico", fila[4]);
                jsonObj.put("Paciente", fila[5]);
                jsonObj.put("TelfPac", fila[6]);
                jsonObj.put("PlanAtencion", fila[7]);
                jsonObj.put("cantimpr", fila[8]);
                jsonArray.put(jsonObj);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "E"; // Error
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public String listarpacientesDNI() {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNativeQuery("SELECT distinct NumdocId, Paciente from HechRecetasCli");
            List<Object[]> resultados = query.getResultList();

            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("dni", fila[0]);
                jsonObj.put("paciente", fila[1]);
                jsonArray.put(jsonObj);
            }

            // Convertir el JSONArray a String y devolver
            return jsonArray.toString();

        } catch (Exception e) {
            // Manejar el error y devolver un JSON de error
            JSONObject errorObj = new JSONObject();
            errorObj.put("resultado", "Error");
            errorObj.put("mensaje", e.getMessage());
            return errorObj.toString();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public String listarfarmacos(int ActoMedico) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNativeQuery("SELECT generico, producto, cantidad, CodproLolfar FROM HechRecetasCli rece INNER JOIN HechDiagnosRecetasCli diag ON rece.ActoMedico = diag.ActoMedico WHERE rece.ActoMedico = ? GROUP BY generico, producto, cantidad,CodproLolfar;");
            query.setParameter(1, ActoMedico);
            List<Object[]> resultados = query.getResultList();

            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("generico", fila[0]);
                jsonObj.put("producto", fila[1]);
                jsonObj.put("cantidad", fila[2]);
                jsonObj.put("CodproLolfar", fila[3]);
                jsonArray.put(jsonObj);
            }

            // Convertir el JSONArray a String y devolver
            return jsonArray.toString();

        } catch (Exception e) {
            // Manejar el error y devolver un JSON de error
            JSONObject errorObj = new JSONObject();
            errorObj.put("resultado", "Error");
            errorObj.put("mensaje", e.getMessage());
            return errorObj.toString();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    public void create(HechRecetasCli hechRecetasCli) throws PreexistingEntityException, Exception {
        if (hechRecetasCli.getHechRecetasCliPK() == null) {
            hechRecetasCli.setHechRecetasCliPK(new HechRecetasCliPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(hechRecetasCli);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHechRecetasCli(hechRecetasCli.getHechRecetasCliPK()) != null) {
                throw new PreexistingEntityException("HechRecetasCli " + hechRecetasCli + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(HechRecetasCli hechRecetasCli) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            hechRecetasCli = em.merge(hechRecetasCli);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                HechRecetasCliPK id = hechRecetasCli.getHechRecetasCliPK();
                if (findHechRecetasCli(id) == null) {
                    throw new NonexistentEntityException("The hechRecetasCli with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(HechRecetasCliPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            HechRecetasCli hechRecetasCli;
            try {
                hechRecetasCli = em.getReference(HechRecetasCli.class, id);
                hechRecetasCli.getHechRecetasCliPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The hechRecetasCli with id " + id + " no longer exists.", enfe);
            }
            em.remove(hechRecetasCli);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<HechRecetasCli> findHechRecetasCliEntities() {
        return findHechRecetasCliEntities(true, -1, -1);
    }

    public List<HechRecetasCli> findHechRecetasCliEntities(int maxResults, int firstResult) {
        return findHechRecetasCliEntities(false, maxResults, firstResult);
    }

    private List<HechRecetasCli> findHechRecetasCliEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(HechRecetasCli.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public HechRecetasCli findHechRecetasCli(HechRecetasCliPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(HechRecetasCli.class, id);
        } finally {
            em.close();
        }
    }

    public int getHechRecetasCliCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<HechRecetasCli> rt = cq.from(HechRecetasCli.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
