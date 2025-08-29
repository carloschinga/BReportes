package dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import dto.Pickinglist;

/**
 *
 * @author USUARIO
 */
public class PickinglistJpaController extends JpaPadre {

    public PickinglistJpaController(String empresa) {
        super(empresa);
    }

    public String matchpickmov(int codpicklist) {
        String result = "E"; // Por defecto, error
        EntityManager em = null;

        try {
            em = getEntityManager();

            // Crear la consulta para llamar al procedimiento almacenado
            StoredProcedureQuery query = em.createStoredProcedureQuery("matchpickmov");

            // Registrar los parámetros del procedimiento
            query.registerStoredProcedureParameter("codpicklist", Integer.class, ParameterMode.IN);

            // Establecer los valores de los parámetros
            query.setParameter("codpicklist", codpicklist);

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

    public String checkCajasRecepcion(int usecod, String checkcom, int orden, int siscod) {
        String result = "E"; // Por defecto, error
        EntityManager em = null;

        try {
            em = getEntityManager();

            // Crear la consulta para llamar al procedimiento almacenado
            StoredProcedureQuery query = em.createStoredProcedureQuery("checkcajasrecepcion_picking");

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

    public String verificarComInvnum(int orden, int siscod) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            // Ejecutar la consulta usando la NamedQuery
            Query query = em.createNativeQuery(
                    "select top 1 d.chkcomrep from picking p inner join picking_detalle d on d.pickcod=p.pickcod where p.codpicklist=? and d.siscod=?");
            query.setParameter(1, orden);
            query.setParameter(2, siscod);

            query.setHint("javax.persistence.query.timeout", 5000); // Agregar timeout
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

    public int obtenerCodpikMax() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Integer> query = em.createNamedQuery("Pickinglist.findultcodpicklist", Integer.class);
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

    public String vistaPrevia(int invnum) {// lista en json del detalle del movimiento segun secuencia
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select p.despro,pd.cante,pd.cantf,pd.lote,s.sisent from picking pi inner join picking_detalle pd on pd.pickcod=pi.pickcod inner join fa_productos p on p.codpro=pd.codpro inner join sistema s on s.siscod=pd.siscod where pi.pickcod=?");
            query.setParameter(1, invnum);
            List<Object[]> resultados = query.getResultList();
            String cadena = "[";
            boolean s = true;
            for (Object[] fila : resultados) {
                s = false;
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("despro", fila[0]);
                jsonObj.put("cante", fila[1]);
                jsonObj.put("cantf", fila[2]);
                jsonObj.put("codlot", fila[3]);
                jsonObj.put("sisent", fila[4]);
                cadena = cadena + jsonObj.toString() + ",";
            }
            if (s) {
                cadena = "[]";
            } else {
                cadena = cadena.substring(0, cadena.length() - 1) + "]";
            }
            return cadena;

        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String Listar(String invnumMin, String invnumMax, String fecMin, String fecMax, String codalminv) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // Construir la consulta JPQL dinámicamente
            StringBuilder jpql = new StringBuilder(
                    "SELECT m.pickcod,m.feccre, '','',m.codpicklist,m.estado FROM picking m ");
            jpql.append("WHERE m.codalm = ? ");

            if (!invnumMin.equals("") && !invnumMax.equals("")) {
                jpql.append("AND m.pickcod BETWEEN ? AND ? ");
            }
            if (!invnumMin.equals("") && invnumMax.equals("")) {
                jpql.append("AND m.pickcod >= ? ");
            }
            if (!"".equals(fecMin) && !"".equals(fecMax)) {
                jpql.append("AND m.feccre BETWEEN ? AND ? ");
            }
            Query query = em.createNativeQuery(jpql.toString());

            int parameterIndex = 1;
            query.setParameter(parameterIndex++, codalminv);
            // Establecer parámetros en la consulta
            if (!invnumMin.equals("") && !invnumMax.equals("")) {
                query.setParameter(parameterIndex++, Integer.parseInt(invnumMin));
                query.setParameter(parameterIndex++, Integer.parseInt(invnumMax));
            }
            if (!invnumMin.equals("") && invnumMax.equals("")) {
                query.setParameter(parameterIndex++, Integer.parseInt(invnumMin));
            }
            if (!"".equals(fecMin) && !"".equals(fecMax)) {
                Date fecMind = sdf.parse(fecMin);
                Date fecMaxd = sdf.parse(fecMax);

                // Ajustar fecMax a las 23:59:59
                Calendar calMax = Calendar.getInstance();
                calMax.setTime(fecMaxd);
                calMax.set(Calendar.HOUR_OF_DAY, 23);
                calMax.set(Calendar.MINUTE, 59);
                calMax.set(Calendar.SECOND, 59);
                calMax.set(Calendar.MILLISECOND, 999);
                fecMaxd = calMax.getTime();

                query.setParameter(parameterIndex++, fecMind);
                query.setParameter(parameterIndex++, fecMaxd);
            }

            List<Object[]> movimientos = query.getResultList();
            JSONArray jsonArray = new JSONArray();

            for (Object[] resultado : movimientos) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("invnum", resultado[0]);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String fechaFormateada = formatter.format(resultado[1]);
                jsonObj.put("fecmov", fechaFormateada);
                jsonObj.put("destino", resultado[2]);
                jsonObj.put("obsmov", resultado[3]);
                jsonObj.put("usado", resultado[4]);
                jsonObj.put("estado", resultado[5]);
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

    public void create(Pickinglist pickinglist) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(pickinglist);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(Pickinglist pickinglist) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            pickinglist = em.merge(pickinglist);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pickinglist.getCodpicklist();
                if (findPickinglist(id) == null) {
                    throw new NonexistentEntityException("The pickinglist with id " + id + " no longer exists.");
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
            Pickinglist pickinglist;
            try {
                pickinglist = em.getReference(Pickinglist.class, id);
                pickinglist.getCodpicklist();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pickinglist with id " + id + " no longer exists.", enfe);
            }
            em.remove(pickinglist);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<Pickinglist> findPickinglistEntities() {
        return findPickinglistEntities(true, -1, -1);
    }

    public List<Pickinglist> findPickinglistEntities(int maxResults, int firstResult) {
        return findPickinglistEntities(false, maxResults, firstResult);
    }

    private List<Pickinglist> findPickinglistEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pickinglist.class));
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

    public Pickinglist findPickinglist(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(Pickinglist.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getPickinglistCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pickinglist> rt = cq.from(Pickinglist.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
