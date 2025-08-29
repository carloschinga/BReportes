package dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.FaMovimientos;
import dto.FaMovimientosDetalle;

/**
 *
 * @author USUARIO
 */
public class FaMovimientosJpaController extends JpaPadre {

    public FaMovimientosJpaController(String empresa) {
        super(empresa);
    }

    public String listarPickingListDestinos_tot(String sec, String fecha_ini, String fecha_fin, String usecod,
            String codalmant) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            StringBuilder sqlQuery = new StringBuilder(
                    "SELECT v.Destino,v.operario, COUNT(v.Destino), v.siscod_d,COUNT(CASE WHEN v.chkpick = 'S' THEN 1 END) * 100.0 / COUNT(v.Destino) AS avance,completado FROM view_orden_trabajo v where v.codalmant=? ");

            // Agregar restricción de fecha si se proporcionan fechas válidas
            if (!fecha_ini.equals("") && !fecha_fin.equals("")) {
                sqlQuery.append(" AND CONVERT(char(10), v.feccre, 126) BETWEEN ? AND ?");
            }

            // Agregar restricciones de secuencia si se proporcionan secuencias válidas
            if (!sec.equals("")) {
                sqlQuery.append(" AND v.orden = ?");
            }
            if (!usecod.equals("")) {
                sqlQuery.append(" AND v.usecod = ?");
            }
            sqlQuery.append(" GROUP BY v.Destino, v.siscod_d,v.operario,completado ORDER BY v.Destino");

            Query query = em.createNativeQuery(sqlQuery.toString());

            int parameterIndex = 1;

            query.setParameter(parameterIndex++, codalmant);
            // Establecer parámetros de fecha si se proporcionan fechas válidas
            if (!fecha_ini.equals("") && !fecha_fin.equals("")) {
                query.setParameter(parameterIndex++, fecha_ini);
                query.setParameter(parameterIndex++, fecha_fin);
            }

            // Establecer parámetros de secuencia si se proporcionan secuencias válidas
            if (!sec.equals("")) {
                query.setParameter(parameterIndex++, Integer.parseInt(sec));
            }

            if (!usecod.equals("")) {
                query.setParameter(parameterIndex++, Integer.parseInt(usecod));
            }

            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("destino", fila[0]);
                jsonObj.put("operario", fila[1]);
                jsonObj.put("cantidad", fila[2]);
                jsonObj.put("codigo", fila[3]);
                jsonObj.put("avance", fila[4]);
                jsonObj.put("completado", fila[5]);
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

    public String listarPickingListDestinosDet(String sec, String fecha_ini, String fecha_fin, int siscod,
            String orden) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            StringBuilder sqlQuery = new StringBuilder(
                    "select v.Descripcion,v.codpro,v.codlab,v.lote,v.Secuencia,v.chkean13,v.chkpick,case when (cantE=entero or (cantE=0 and entero is null)) and (cantF=fraccion or (cantF=0 and fraccion is null)) then 'S' else 'N' end FROM view_guia_transferencia v INNER JOIN fa_orden_trabajo p ON v.Secuencia = p.invnum");
            if (!fecha_ini.equals("") && !fecha_fin.equals("")) {
                sqlQuery.append(" AND CONVERT(char(10), p.feccre, 126) BETWEEN ? AND ?");
            }

            // Agregar restricciones de secuencia si se proporcionan secuencias válidas
            if (!sec.equals("")) {
                sqlQuery.append(" AND p.ortrcod = ?");
            }

            sqlQuery.append(" AND v.siscod_d=?");
            if (orden.equals("sec")) {
                sqlQuery.append(" ORDER BY v.Secuencia");
            } else {
                sqlQuery.append(" ORDER BY v.Descripcion");
            }
            Query query = em.createNativeQuery(sqlQuery.toString());

            int parameterIndex = 1;

            // Establecer parámetros de fecha si se proporcionan fechas válidas
            if (!fecha_ini.equals("") && !fecha_fin.equals("")) {
                query.setParameter(parameterIndex++, fecha_ini);
                query.setParameter(parameterIndex++, fecha_fin);
            }

            // Establecer parámetros de secuencia si se proporcionan secuencias válidas
            if (!sec.equals("")) {
                query.setParameter(parameterIndex++, Integer.parseInt(sec));
            }
            query.setParameter(parameterIndex++, siscod);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("despro", fila[0]);
                jsonObj.put("codpro", fila[1]);
                jsonObj.put("codlab", fila[2]);
                jsonObj.put("codlot", fila[3]);
                jsonObj.put("secuencia", fila[4]);
                jsonObj.put("check1", fila[5]);
                jsonObj.put("check2", fila[6]);
                jsonObj.put("check3", fila[7]);
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

    public String listarPickingListDestinosDetTodo(String sec, String fecha_ini, String fecha_fin, int siscod,
            String orden) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            StringBuilder sqlQuery = new StringBuilder(
                    "select v.Descripcion,v.codpro,v.codlab,v.lote,v.Secuencia,v.chkean13,v.chkpick,v.cantE,v.cantF,v.fecven,v.codalt,v.codlab,v.ubipro FROM view_guia_transferencia v INNER JOIN fa_orden_trabajo p ON v.Secuencia = p.invnum");
            if (!fecha_ini.equals("") && !fecha_fin.equals("")) {
                sqlQuery.append(" AND CONVERT(char(10), p.feccre, 126) BETWEEN ? AND ?");
            }

            // Agregar restricciones de secuencia si se proporcionan secuencias válidas
            if (!sec.equals("")) {
                sqlQuery.append(" AND p.ortrcod = ?");
            }

            sqlQuery.append(" AND v.siscod_d=?");
            if (orden.equals("sec")) {
                sqlQuery.append(" ORDER BY v.Secuencia");
            } else {
                sqlQuery.append(" ORDER BY v.Descripcion");
            }
            Query query = em.createNativeQuery(sqlQuery.toString());

            int parameterIndex = 1;

            // Establecer parámetros de fecha si se proporcionan fechas válidas
            if (!fecha_ini.equals("") && !fecha_fin.equals("")) {
                query.setParameter(parameterIndex++, fecha_ini);
                query.setParameter(parameterIndex++, fecha_fin);
            }

            // Establecer parámetros de secuencia si se proporcionan secuencias válidas
            if (!sec.equals("")) {
                query.setParameter(parameterIndex++, Integer.parseInt(sec));
            }
            query.setParameter(parameterIndex++, siscod);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("despro", fila[0]);
                jsonObj.put("codpro", fila[1]);
                jsonObj.put("codlab", fila[2]);
                jsonObj.put("codlot", fila[3]);
                jsonObj.put("secuencia", fila[4]);
                jsonObj.put("check1", fila[5]);
                jsonObj.put("check2", fila[6]);
                jsonObj.put("cante", fila[7]);
                jsonObj.put("cantf", fila[8]);
                jsonObj.put("fecven", fila[9]);
                jsonObj.put("codalt", fila[10]);
                jsonObj.put("codlab", fila[11]);
                jsonObj.put("ubipro", fila[12]);
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

    public String listarPickingListDet(int siscod, String codpro, String codlot, int secuencia) {

        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = null;
            String sqlQuery = "select cantE,cantF,Descripcion,fecven,Secuencia,chkean13,chkpick,codalt,lote,entero,fraccion,caja,codlab from view_guia_transferencia where siscod_d=? and codpro=? and lote=? and Secuencia=? ";

            query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, siscod);
            query.setParameter(2, codpro);
            query.setParameter(3, codlot);
            query.setParameter(4, secuencia);
            List<Object[]> resultados = query.getResultList();

            // Si no hay resultados, devolver un JSON vacío o un mensaje apropiado
            if (resultados.isEmpty()) {
                return "{}"; // Devuelve un JSON vacío
            }

            // Asumimos que solo hay un resultado
            Object[] fila = resultados.get(0);

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("cante", fila[0]);
            jsonObj.put("cantf", fila[1]);
            jsonObj.put("despro", fila[2]);
            jsonObj.put("fecven", fila[3]);
            jsonObj.put("secuencia", fila[4]);
            jsonObj.put("check1", fila[5]);
            jsonObj.put("check2", fila[6]);
            jsonObj.put("codalt", fila[7]);
            jsonObj.put("lote", fila[8]);
            jsonObj.put("entero", fila[9]);
            jsonObj.put("fraccion", fila[10]);
            jsonObj.put("caja", fila[11]);
            jsonObj.put("codlab", fila[12]);

            return jsonObj.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public FaMovimientosDetalle findByInvnumCodproCodlot(int invnum, String codpro, String codlot) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.createNamedQuery("FaMovimientosDetalle.findByInvnumcodprocodlot", FaMovimientosDetalle.class)
                    .setParameter("invnum", invnum)
                    .setParameter("codpro", codpro)
                    .setParameter("codlot", codlot)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<Integer> findByInvnumInRange(int invnumMin, int invnumMax, boolean tipo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            if (tipo) {
                TypedQuery<Integer> query = em.createNamedQuery("FaMovimientos.findByInvnumInRange", Integer.class);
                query.setParameter("invnumMin", invnumMin);
                query.setParameter("invnumMax", invnumMax);
                return query.getResultList();
            } else {
                TypedQuery<Integer> query = em.createNamedQuery("FaMovimientos.findByInvnummayor", Integer.class);
                query.setParameter("invnum", invnumMin);
                return query.getResultList();
            }
        } catch (Exception e) {
            return null;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String Listar(String invnumMin, String invnumMax, String fecMin, String fecMax, String filtro,
            String codalminv) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // Construir la consulta JPQL dinámicamente
            StringBuilder jpql = new StringBuilder(
                    "SELECT m.invnum,m.fecmov , s.sisent,m.obsmov,o.faOrdenTrabajoPK.ortrcod FROM FaMovimientos m ");
            jpql.append("INNER JOIN Sistema s ON m.siscodD = s.siscod ");
            jpql.append("LEFT JOIN FaOrdenTrabajo o ON o.faOrdenTrabajoPK.invnum=m.invnum and o.estado='S' ");
            jpql.append("WHERE m.tipkar = 'DE' AND m.codalm = :codalm ");

            if (!invnumMin.equals("") && !invnumMax.equals("")) {
                jpql.append("AND m.invnum BETWEEN :invnumMin AND :invnumMax ");
            }
            if (!invnumMin.equals("") && invnumMax.equals("")) {
                jpql.append("AND m.invnum >= :invnumMin ");
            }
            if (!"".equals(fecMin) && !"".equals(fecMax)) {
                jpql.append("AND m.fecmov BETWEEN :fecMin AND :fecMax ");
            }
            if ("DISTRIBUCION".equals(filtro)) {
                jpql.append("AND m.obsmov = :obsmov ");
            } else if ("MAGISTRAL".equals(filtro)) {
                jpql.append("AND m.obsmov like 'MAGISTRAL%' ");
            }
            jpql.append("order by s.sisent");
            Query query = em.createQuery(jpql.toString());
            query.setParameter("codalm", codalminv);
            // Establecer parámetros en la consulta
            if (!invnumMin.equals("")) {
                query.setParameter("invnumMin", Integer.parseInt(invnumMin));
            }
            if (!invnumMax.equals("")) {
                query.setParameter("invnumMax", Integer.parseInt(invnumMax));
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

                query.setParameter("fecMin", fecMind);
                query.setParameter("fecMax", fecMaxd);
            }

            if ("DISTRIBUCION".equals(filtro)) {
                query.setParameter("obsmov", filtro);
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

    public void create(FaMovimientos faMovimientos) throws PreexistingEntityException, Exception {
        if (faMovimientos.getFaMovimientosDetalleList() == null) {
            faMovimientos.setFaMovimientosDetalleList(new ArrayList<FaMovimientosDetalle>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<FaMovimientosDetalle> attachedFaMovimientosDetalleList = new ArrayList<FaMovimientosDetalle>();
            for (FaMovimientosDetalle faMovimientosDetalleListFaMovimientosDetalleToAttach : faMovimientos
                    .getFaMovimientosDetalleList()) {
                faMovimientosDetalleListFaMovimientosDetalleToAttach = em.getReference(
                        faMovimientosDetalleListFaMovimientosDetalleToAttach.getClass(),
                        faMovimientosDetalleListFaMovimientosDetalleToAttach.getFaMovimientosDetallePK());
                attachedFaMovimientosDetalleList.add(faMovimientosDetalleListFaMovimientosDetalleToAttach);
            }
            faMovimientos.setFaMovimientosDetalleList(attachedFaMovimientosDetalleList);
            em.persist(faMovimientos);
            for (FaMovimientosDetalle faMovimientosDetalleListFaMovimientosDetalle : faMovimientos
                    .getFaMovimientosDetalleList()) {
                FaMovimientos oldFaMovimientosOfFaMovimientosDetalleListFaMovimientosDetalle = faMovimientosDetalleListFaMovimientosDetalle
                        .getFaMovimientos();
                faMovimientosDetalleListFaMovimientosDetalle.setFaMovimientos(faMovimientos);
                faMovimientosDetalleListFaMovimientosDetalle = em.merge(faMovimientosDetalleListFaMovimientosDetalle);
                if (oldFaMovimientosOfFaMovimientosDetalleListFaMovimientosDetalle != null) {
                    oldFaMovimientosOfFaMovimientosDetalleListFaMovimientosDetalle.getFaMovimientosDetalleList()
                            .remove(faMovimientosDetalleListFaMovimientosDetalle);
                    oldFaMovimientosOfFaMovimientosDetalleListFaMovimientosDetalle = em
                            .merge(oldFaMovimientosOfFaMovimientosDetalleListFaMovimientosDetalle);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFaMovimientos(faMovimientos.getInvnum()) != null) {
                throw new PreexistingEntityException("FaMovimientos " + faMovimientos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(FaMovimientos faMovimientos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FaMovimientos persistentFaMovimientos = em.find(FaMovimientos.class, faMovimientos.getInvnum());
            List<FaMovimientosDetalle> faMovimientosDetalleListOld = persistentFaMovimientos
                    .getFaMovimientosDetalleList();
            List<FaMovimientosDetalle> faMovimientosDetalleListNew = faMovimientos.getFaMovimientosDetalleList();
            List<String> illegalOrphanMessages = null;
            for (FaMovimientosDetalle faMovimientosDetalleListOldFaMovimientosDetalle : faMovimientosDetalleListOld) {
                if (!faMovimientosDetalleListNew.contains(faMovimientosDetalleListOldFaMovimientosDetalle)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add(
                            "You must retain FaMovimientosDetalle " + faMovimientosDetalleListOldFaMovimientosDetalle
                                    + " since its faMovimientos field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<FaMovimientosDetalle> attachedFaMovimientosDetalleListNew = new ArrayList<FaMovimientosDetalle>();
            for (FaMovimientosDetalle faMovimientosDetalleListNewFaMovimientosDetalleToAttach : faMovimientosDetalleListNew) {
                faMovimientosDetalleListNewFaMovimientosDetalleToAttach = em.getReference(
                        faMovimientosDetalleListNewFaMovimientosDetalleToAttach.getClass(),
                        faMovimientosDetalleListNewFaMovimientosDetalleToAttach.getFaMovimientosDetallePK());
                attachedFaMovimientosDetalleListNew.add(faMovimientosDetalleListNewFaMovimientosDetalleToAttach);
            }
            faMovimientosDetalleListNew = attachedFaMovimientosDetalleListNew;
            faMovimientos.setFaMovimientosDetalleList(faMovimientosDetalleListNew);
            faMovimientos = em.merge(faMovimientos);
            for (FaMovimientosDetalle faMovimientosDetalleListNewFaMovimientosDetalle : faMovimientosDetalleListNew) {
                if (!faMovimientosDetalleListOld.contains(faMovimientosDetalleListNewFaMovimientosDetalle)) {
                    FaMovimientos oldFaMovimientosOfFaMovimientosDetalleListNewFaMovimientosDetalle = faMovimientosDetalleListNewFaMovimientosDetalle
                            .getFaMovimientos();
                    faMovimientosDetalleListNewFaMovimientosDetalle.setFaMovimientos(faMovimientos);
                    faMovimientosDetalleListNewFaMovimientosDetalle = em
                            .merge(faMovimientosDetalleListNewFaMovimientosDetalle);
                    if (oldFaMovimientosOfFaMovimientosDetalleListNewFaMovimientosDetalle != null
                            && !oldFaMovimientosOfFaMovimientosDetalleListNewFaMovimientosDetalle
                                    .equals(faMovimientos)) {
                        oldFaMovimientosOfFaMovimientosDetalleListNewFaMovimientosDetalle.getFaMovimientosDetalleList()
                                .remove(faMovimientosDetalleListNewFaMovimientosDetalle);
                        oldFaMovimientosOfFaMovimientosDetalleListNewFaMovimientosDetalle = em
                                .merge(oldFaMovimientosOfFaMovimientosDetalleListNewFaMovimientosDetalle);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = faMovimientos.getInvnum();
                if (findFaMovimientos(id) == null) {
                    throw new NonexistentEntityException("The faMovimientos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FaMovimientos faMovimientos;
            try {
                faMovimientos = em.getReference(FaMovimientos.class, id);
                faMovimientos.getInvnum();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The faMovimientos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<FaMovimientosDetalle> faMovimientosDetalleListOrphanCheck = faMovimientos
                    .getFaMovimientosDetalleList();
            for (FaMovimientosDetalle faMovimientosDetalleListOrphanCheckFaMovimientosDetalle : faMovimientosDetalleListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add(
                        "This FaMovimientos (" + faMovimientos + ") cannot be destroyed since the FaMovimientosDetalle "
                                + faMovimientosDetalleListOrphanCheckFaMovimientosDetalle
                                + " in its faMovimientosDetalleList field has a non-nullable faMovimientos field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(faMovimientos);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<FaMovimientos> findFaMovimientosEntities() {
        return findFaMovimientosEntities(true, -1, -1);
    }

    public List<FaMovimientos> findFaMovimientosEntities(int maxResults, int firstResult) {
        return findFaMovimientosEntities(false, maxResults, firstResult);
    }

    private List<FaMovimientos> findFaMovimientosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FaMovimientos.class));
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

    public FaMovimientos findFaMovimientos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FaMovimientos.class, id);
        } finally {
            em.close();
        }
    }

    public int getFaMovimientosCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FaMovimientos> rt = cq.from(FaMovimientos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
