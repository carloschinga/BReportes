package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.MovimientosIzipay;
import dto.MovimientosIzipayPK;

/**
 *
 * @author USER
 */
public class MovimientosIzipayJpaController extends JpaPadre {

    public MovimientosIzipayJpaController(String empresa) {
        super(empresa);
    }

    public String actualizarEstadoMovimientos(String fechaInicio, String fechaFin, String cod1, String cod2) {
        EntityManager em = null;
        String result = "E"; // Por defecto, "E" para error

        try {
            em = getEntityManager();

            // Crear el StoredProcedureQuery
            StoredProcedureQuery query = em.createStoredProcedureQuery("ActualizarEstadoMovimientosIzipay");

            // Registrar los parámetros
            query.registerStoredProcedureParameter("fechaInicio", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("fechaFin", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("cod1", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("cod2", String.class, ParameterMode.IN);

            // Asignar valores a los parámetros
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
            query.setParameter("cod1", cod1);
            query.setParameter("cod2", cod2);

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

    public String listar(String fechaInicio, String fechaFin) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sqlQuery = "select izi.Codigo,Voucher,fecha_Consumo,ffp.docpag,d.tdodes,concat(case when ffp.tdofac='BO' then 'B' when ffp.tdofac='FA' then 'F' else '' end,ffp.tdoidser,'-',facnum) as numcomp, importe,facnet as lolfarimporte, Status,Comision,IGV,Neto_Parcial,Neto_Total,Fecha_Abono, refpag,izi.estado "
                    + "from MovimientosIzipay izi "
                    + "left join ("
                    + "  select p.refpag,p.docpag,f.facnet,f.tdofac,f.tdoidser,f.facnum "
                    + "  from facturas f "
                    + "  inner join facturas_formas_pago p on p.invnum = f.invnum "
                    + "  where (p.docpag = 'yq' or p.docpag = 'tp') "
                    + "    and f.facdat >= ? and f.facdat < ?"
                    + ") ffp on ffp.refpag like concat('%REF:', SUBSTRING(Voucher, 4, 4)) "
                    + "left join documentos d on d.tdofac=ffp.tdofac "

                    + "where izi.fecha_Proceso >= ? and izi.fecha_Proceso < ?";

            Query query = em.createNativeQuery(sqlQuery);

            // Asignar parámetros posicionales
            int parameterIndex = 1;
            query.setParameter(parameterIndex++, fechaInicio); // Fecha de inicio facturas
            query.setParameter(parameterIndex++, fechaFin); // Fecha de fin facturas
            query.setParameter(parameterIndex++, fechaInicio); // Fecha de inicio MovimientosIzipay
            query.setParameter(parameterIndex++, fechaFin); // Fecha de fin MovimientosIzipay

            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                int i = 0;
                jsonObj.put("codigo", fila[i++]);
                jsonObj.put("Voucher", fila[i++]);
                jsonObj.put("fecha", fila[i++]);
                jsonObj.put("docpag", fila[i++]);
                jsonObj.put("tdodes", fila[i++]);
                jsonObj.put("numcomp", fila[i++]);
                jsonObj.put("importe", fila[i++]);
                jsonObj.put("importlolfar", fila[i++]);
                jsonObj.put("Status", fila[i++]);
                jsonObj.put("comision", fila[i++]);
                jsonObj.put("igv", fila[i++]);
                jsonObj.put("netoparcial", fila[i++]);
                jsonObj.put("netototal", fila[i++]);
                jsonObj.put("fechaabono", fila[i++]);
                jsonObj.put("refpag", fila[i++]);
                jsonObj.put("estado", fila[i++]);
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

    public void create(MovimientosIzipay movimientosIzipay) throws PreexistingEntityException, Exception {
        if (movimientosIzipay.getMovimientosIzipayPK() == null) {
            movimientosIzipay.setMovimientosIzipayPK(new MovimientosIzipayPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(movimientosIzipay);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMovimientosIzipay(movimientosIzipay.getMovimientosIzipayPK()) != null) {
                throw new PreexistingEntityException("MovimientosIzipay " + movimientosIzipay + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(MovimientosIzipay movimientosIzipay) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            movimientosIzipay = em.merge(movimientosIzipay);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                MovimientosIzipayPK id = movimientosIzipay.getMovimientosIzipayPK();
                if (findMovimientosIzipay(id) == null) {
                    throw new NonexistentEntityException("The movimientosIzipay with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(MovimientosIzipayPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MovimientosIzipay movimientosIzipay;
            try {
                movimientosIzipay = em.getReference(MovimientosIzipay.class, id);
                movimientosIzipay.getMovimientosIzipayPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The movimientosIzipay with id " + id + " no longer exists.",
                        enfe);
            }
            em.remove(movimientosIzipay);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<MovimientosIzipay> findMovimientosIzipayEntities() {
        return findMovimientosIzipayEntities(true, -1, -1);
    }

    public List<MovimientosIzipay> findMovimientosIzipayEntities(int maxResults, int firstResult) {
        return findMovimientosIzipayEntities(false, maxResults, firstResult);
    }

    private List<MovimientosIzipay> findMovimientosIzipayEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MovimientosIzipay.class));
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

    public MovimientosIzipay findMovimientosIzipay(MovimientosIzipayPK id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(MovimientosIzipay.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getMovimientosIzipayCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MovimientosIzipay> rt = cq.from(MovimientosIzipay.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
