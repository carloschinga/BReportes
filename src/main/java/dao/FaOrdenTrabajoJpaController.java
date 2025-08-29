package dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.FaOrdenTrabajo;
import dto.FaOrdenTrabajoPK;

/**
 *
 * @author USUARIO
 */
public class FaOrdenTrabajoJpaController extends JpaPadre {

    public FaOrdenTrabajoJpaController(String empresa) {
        super(empresa);
    }

    public int obtenerCodpikMax() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Integer> query = em.createNamedQuery("FaOrdenTrabajo.codpikmax", Integer.class);
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

    public String verificarCompletadoInvnum(int invnum) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            // Ejecutar la consulta usando la NamedQuery
            Query query = em
                    .createNativeQuery("select max(completado) from fa_orden_trabajo where invnum=? and estado='S'");
            query.setParameter(1, invnum);

            // Obtener un solo resultado
            String resultado = query.getSingleResult().toString();

            // Retornar el resultado
            return resultado;
        } catch (Exception e) {
            return null; // O maneja el error de la forma que necesites
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String verificarComInvnum(int orden, int siscod) {
        EntityManager em = null;

        try {
            em = getEntityManager();
            // Ejecutar la consulta usando la NamedQuery
            Query query = em.createNativeQuery(
                    "select top 1 o.chkcomrep from fa_orden_trabajo o inner join fa_movimientos m on m.invnum=o.invnum where o.ortrcod=? and m.siscod_d=? and o.estado='S'");
            query.setParameter(1, orden);
            query.setParameter(2, siscod);

            // Obtener un solo resultado
            String resultado = query.getSingleResult().toString();

            // Retornar el resultado
            return resultado;
        } catch (Exception e) {
            return null; // O maneja el error de la forma que necesites
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String verificarComInvnuminvnum(int invnum) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            // Ejecutar la consulta usando la NamedQuery
            Query query = em
                    .createNativeQuery("select top 1 chkcomrep from fa_orden_trabajo where invnum=? and estado='S'");
            query.setParameter(1, invnum);

            // Obtener un solo resultado
            String resultado = query.getSingleResult().toString();

            // Retornar el resultado
            return resultado;
        } catch (Exception e) {
            return null; // O maneja el error de la forma que necesites
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarfechas(String fecha_ini, String fecha_fin, String codalminv) {
        EntityManager em = null;
        try {
            em = getEntityManager();

            String sqlQuery = "select o.ortrcod, CONVERT(char(10),o.feccre,126),COUNT(CASE WHEN d.chkpick = 'S' THEN 1 END) * 100.0/COUNT(o.ortrcod),completado  from fa_orden_trabajo o inner join fa_movimientos m on m.invnum=o.invnum and m.codalm=? inner join fa_movimientos_detalle d on d.invnum=o.invnum  AND CONVERT(char(10), o.feccre, 126) BETWEEN ? AND ? where o.estado='S' group by o.ortrcod, CONVERT(char(10),o.feccre,126),completado order by o.ortrcod";

            Query query = em.createNativeQuery(sqlQuery);

            int parameterIndex = 1;

            query.setParameter(parameterIndex++, codalminv);
            query.setParameter(parameterIndex++, fecha_ini);
            query.setParameter(parameterIndex++, fecha_fin);

            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("ortrcod", fila[0]);
                jsonObj.put("fecha", fila[1]);
                jsonObj.put("avance", fila[2]);
                jsonObj.put("completado", fila[3]);
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

    public String listarOtabrirot(int orden) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            StringBuilder sqlQuery = new StringBuilder();
            sqlQuery.append("SELECT o.ortrcod, m.siscod_d, s.sisent, ")
                    .append("COUNT(CASE WHEN c.checkenvio = 'S' THEN 1 END) * 100.0/COUNT(o.ortrcod), o.chkcomrep ")
                    .append("FROM fa_orden_trabajo o ")
                    .append("INNER JOIN fa_movimientos m ON m.invnum = o.invnum ")
                    .append("INNER JOIN fa_cajas c ON c.invnum = o.invnum ")
                    .append("INNER JOIN sistema s ON s.siscod = m.siscod_d ")
                    .append("WHERE o.estado = 'S' AND o.ortrcod = ? ")
                    .append("GROUP BY o.ortrcod, m.siscod_d, o.chkcomrep, s.sisent ")
                    .append("ORDER BY s.sisent;");
            Query query = em.createNativeQuery(sqlQuery.toString());

            int parameterIndex = 1;

            query.setParameter(parameterIndex++, orden);

            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("ortrcod", fila[0]);
                jsonObj.put("siscod", fila[1]);
                jsonObj.put("sisent", fila[2]);
                jsonObj.put("avance", fila[3]);
                jsonObj.put("completado", fila[4]);
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

    public String checkCajasRecepcion(int usecod, String checkcom, int orden, int siscod) {
        String result = "E"; // Por defecto, error
        EntityManager em = null;

        try {
            em = getEntityManager();

            // Crear la consulta para llamar al procedimiento almacenado
            StoredProcedureQuery query = em.createStoredProcedureQuery("checkcajasrecepcion");

            // Registrar los parámetros del procedimiento
            query.registerStoredProcedureParameter("usecod", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("fecha", Date.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("checkcom", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("orden", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("siscod", Integer.class, ParameterMode.IN);

            // Establecer los valores de los parámetros
            query.setParameter("usecod", usecod);
            query.setParameter("fecha", new Date()); // Si tienes un objeto Date, conviértelo a String en el formato
                                                     // correcto
            query.setParameter("checkcom", checkcom);
            query.setParameter("orden", orden);
            query.setParameter("siscod", siscod);

            // Ejecutar el procedimiento
            query.execute();
            result = "S"; // Indicar éxito
        } catch (Exception e) {
            e.printStackTrace();
            result = "E"; // Error
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }

        return result;
    }

    // metodo de listado de ordenes segun el codigo de la orden y el establecimiento
    public List<FaOrdenTrabajo> listarsiscodcodord(int codord, int siscod) {// lista de ordenes segun el codigo del
                                                                            // orden y el siscod

        EntityManager em = null;
        try {
            em = getEntityManager();

            String sqlQuery = "select o.* from fa_orden_trabajo o inner join fa_movimientos c on o.invnum=c.invnum and o.ortrcod=? and c.siscod_d=? and o.estado='S'";

            Query query = em.createNativeQuery(sqlQuery, FaOrdenTrabajo.class);

            query.setParameter(1, codord);
            query.setParameter(2, siscod);

            List<FaOrdenTrabajo> resultados = query.getResultList();
            return resultados;
        } catch (Exception e) {
            return null;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String EliminarOt(int orden) {
        String result = "E"; // Default to error
        EntityManager em = null;
        try {
            em = getEntityManager(); // Asumiendo que tienes un método para obtener el EntityManager

            // Crear el StoredProcedureQuery para llamar al procedimiento almacenado
            StoredProcedureQuery query = em.createStoredProcedureQuery("EliminarOT");

            // Registrar los parámetros del procedimiento almacenado
            query.registerStoredProcedureParameter("ortrcod", Integer.class, ParameterMode.IN);

            // Establecer los valores de los parámetros
            query.setParameter("ortrcod", orden);

            // Ejecutar el procedimiento almacenado
            query.execute();

            // Si todo va bien, establecer el resultado como éxito
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

    public String CerrarOT(int orden) {
        String result = "E"; // Default to error
        EntityManager em = null;
        try {
            em = getEntityManager(); // Asumiendo que tienes un método para obtener el EntityManager

            // Crear el StoredProcedureQuery para llamar al procedimiento almacenado
            StoredProcedureQuery query = em.createStoredProcedureQuery("CerrarOT");

            // Registrar los parámetros del procedimiento almacenado
            query.registerStoredProcedureParameter("ortrcod", Integer.class, ParameterMode.IN);

            // Establecer los valores de los parámetros
            query.setParameter("ortrcod", orden);

            // Ejecutar el procedimiento almacenado
            query.execute();

            // Si todo va bien, establecer el resultado como éxito
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

    public void create(FaOrdenTrabajo faOrdenTrabajo) throws PreexistingEntityException, Exception {
        if (faOrdenTrabajo.getFaOrdenTrabajoPK() == null) {
            faOrdenTrabajo.setFaOrdenTrabajoPK(new FaOrdenTrabajoPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(faOrdenTrabajo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFaOrdenTrabajo(faOrdenTrabajo.getFaOrdenTrabajoPK()) != null) {
                throw new PreexistingEntityException("FaOrdenTrabajo " + faOrdenTrabajo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(FaOrdenTrabajo faOrdenTrabajo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            faOrdenTrabajo = em.merge(faOrdenTrabajo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                FaOrdenTrabajoPK id = faOrdenTrabajo.getFaOrdenTrabajoPK();
                if (findFaOrdenTrabajo(id) == null) {
                    throw new NonexistentEntityException("The faOrdenTrabajo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(FaOrdenTrabajoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FaOrdenTrabajo faOrdenTrabajo;
            try {
                faOrdenTrabajo = em.getReference(FaOrdenTrabajo.class, id);
                faOrdenTrabajo.getFaOrdenTrabajoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The faOrdenTrabajo with id " + id + " no longer exists.", enfe);
            }
            em.remove(faOrdenTrabajo);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<FaOrdenTrabajo> findFaOrdenTrabajoEntities() {
        return findFaOrdenTrabajoEntities(true, -1, -1);
    }

    public List<FaOrdenTrabajo> findFaOrdenTrabajoEntities(int maxResults, int firstResult) {
        return findFaOrdenTrabajoEntities(false, maxResults, firstResult);
    }

    private List<FaOrdenTrabajo> findFaOrdenTrabajoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FaOrdenTrabajo.class));
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

    public FaOrdenTrabajo findFaOrdenTrabajo(FaOrdenTrabajoPK id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(FaOrdenTrabajo.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getFaOrdenTrabajoCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();

            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FaOrdenTrabajo> rt = cq.from(FaOrdenTrabajo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
