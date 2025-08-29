package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.exceptions.NonexistentEntityException;
import dto.Picking;

/**
 *
 * @author USUARIO
 */
public class PickingJpaController extends JpaPadre {

    public PickingJpaController(String empresa) {
        super(empresa);
    }

    public int obtenerUlt() {// metodo que te devuelve la ultima secuencia del tipo ingresado
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Integer> query = em.createNamedQuery("Picking.obtenerultpickcod", Integer.class);
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

    public String listarfechas(String fecha_ini, String fecha_fin, String codalminv) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sqlQuery = "SELECT o.codpicklist, CONVERT(CHAR(10), pl.feccre, 126) AS fecha_creacion, COUNT(CASE WHEN d.chkpick = 'S' THEN 1 END) * 100.0 / COUNT(o.pickcod) AS porcentaje_pickeado, pl.completado,txtdescarga,chktxt FROM picking o INNER JOIN picking_detalle d ON d.pickcod = o.pickcod inner join pickinglist pl on pl.codpicklist=o.codpicklist WHERE o.codalm = ? and CONVERT(CHAR(10), pl.feccre, 126) BETWEEN ? AND ? GROUP BY o.codpicklist, CONVERT(CHAR(10), pl.feccre, 126), pl.completado, txtdescarga,chktxt ORDER BY o.codpicklist;";

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
                jsonObj.put("txtdescarga", fila[4]);
                jsonObj.put("chktxt", fila[5]);
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

    public String listarfechasmatch(String fecha_ini, String fecha_fin, String codalminv) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sqlQuery = "SELECT o.codpicklist, CONVERT(CHAR(10), pl.feccre, 126) AS fecha_creacion, COUNT(CASE WHEN d.chkpick = 'S' THEN 1 END) * 100.0 / COUNT(o.pickcod) AS porcentaje_pickeado, pl.completado,pl.chktxt FROM picking o INNER JOIN picking_detalle d ON d.pickcod = o.pickcod inner join pickinglist pl on pl.codpicklist=o.codpicklist WHERE o.codalm = ? and CONVERT(CHAR(10), pl.feccre, 126) BETWEEN ? AND ? GROUP BY o.codpicklist, CONVERT(CHAR(10), pl.feccre, 126), pl.completado,pl.chktxt ORDER BY o.codpicklist;";

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
                jsonObj.put("chktxt", fila[4]);
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

    public String generartxt(int pickcod, String codalm) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sqlQuery = "select pd.invnum,pd.codpro,0,0,0,isnull(caj.cante,0),isnull(caj.cantf,0),p.costod,CAST((costod * (1+(p.igvpro / 100))) AS DECIMAL(18,2)),CAST(((costod * (1+(igvpro / 100))) * (1 + (utipro / 100))) AS DECIMAL(18,2)),0,0,0,0,FORMAT(fecven, 'dd/MM/yyyy HH:mm:ss.fff'),descu.utipro2,descu.dtopro,pd.invnum,pc.codalm,'DE',ds.tdonum+ind.indice,0,FORMAT(GETDATE(), 'dd/MM/yyyy HH:mm:ss.fff'),FORMAT(GETDATE(), 'dd/MM/yyyy HH:mm:ss.fff'),'CO',CAST(sum(p.coscom*caj.cante*(p.igvpro+1)) over(partition by pd.invnum) AS DECIMAL(18,2)),0,0,CAST(sum(p.coscom*caj.cante*p.igvpro) over(partition by pd.invnum) AS DECIMAL(18,2)),sum(p.coscom*caj.cante) over(partition by pd.invnum),CONCAT('DIST.',pc.codpicklist,' E:',pd.siscod),'','CENT',0,0,'SO','',tipmov.invkar+ind.indice,FORMAT(GETDATE(), 'dd/MM/yyyy HH:mm:ss.fff'),ds.tdoidser,'',0,'GR',pd.siscod,'N',pd.lote,'','','','','0000','N','','' from picking_detalle pd inner join fa_productos p on pd.codpro=p.codpro inner join fa_stock_vencimientos venc on venc.codlot=pd.lote and pd.codpro=venc.codpro and codalm=? inner join fa_descuentos_productos_est descu on descu.codpro=pd.codpro and descu.siscod=pd.siscod inner join picking pc on pc.pickcod=pd.pickcod inner join fa_almacenes a on a.codalm=pc.codalm inner join documentos_series ds on ds.siscod=a.siscod and tdoser = 'GE' and tdofac = 'GR' inner join (select m.invnum,ROW_NUMBER() OVER ( ORDER BY m.invnum) as indice FROM (SELECT DISTINCT invnum FROM picking_detalle pd inner join picking pc on pc.pickcod=pd.pickcod where pc.codpicklist=?) m ) ind on ind.invnum=pd.invnum inner join fa_tipo_movimientos tipmov on tipmov.tipkar = 'DE' inner join (select c.pickdetcod,sum(c.cante) as cante,sum(c.cantf) as cantf from picking_cajas c inner join picking_detalle d on c.pickdetcod=d.pickdetcod inner join picking pc on pc.pickcod=d.pickcod where pc.codpicklist=? group by c.pickdetcod) caj on caj.pickdetcod=pd.pickdetcod where pc.codpicklist=? order by pd.invnum";

            Query query = em.createNativeQuery(sqlQuery);

            int parameterIndex = 1;
            query.setParameter(parameterIndex++, codalm);
            query.setParameter(parameterIndex++, pickcod);
            query.setParameter(parameterIndex++, pickcod);
            query.setParameter(parameterIndex++, pickcod);

            List<Object[]> resultados = query.getResultList();

            // Usamos un StringBuilder para construir el resultado en formato JSON
            JSONObject resultadoJSON = new JSONObject();

            // Mapa para agrupar por invnum
            Map<String, StringBuilder> invnumMap = new HashMap<>();
            Map<String, Integer> siscodmap = new HashMap<>();

            // Iteramos sobre los resultados
            for (Object[] fila : resultados) {
                // Construimos la fila como un String con tabuladores
                StringBuilder filaString = new StringBuilder();
                for (Object fila1 : fila) {
                    filaString.append(fila1 != null ? fila1.toString() : "").append("\t");
                }
                // Quitamos el último tabulador extra
                filaString.setLength(filaString.length() - 1);

                // Obtener el invnum (asumimos que está en la primera columna)
                String invnum = fila[0].toString();

                // Si no existe en el mapa, lo creamos
                if (!invnumMap.containsKey(invnum)) {
                    invnumMap.put(invnum, new StringBuilder());
                }
                if (!siscodmap.containsKey(invnum)) {
                    siscodmap.put(invnum, (Integer) fila[43]);
                }

                // Añadimos la fila a la lista correspondiente de invnum
                invnumMap.get(invnum).append(filaString).append("\n");
            }

            // Agregamos los datos de cada invnum al JSON
            for (Map.Entry<String, StringBuilder> entry : invnumMap.entrySet()) {
                String invnum = entry.getKey();
                StringBuilder data = entry.getValue();

                // Creando el objeto JSON para cada invnum
                JSONObject invnumJSON = new JSONObject();
                invnumJSON.put("texto", data.toString());
                invnumJSON.put("siscod", siscodmap.get(invnum));

                // Agregamos el objeto JSON bajo la clave invnum
                resultadoJSON.put(invnum, invnumJSON);
            }

            // Devolvemos el resultado como un String en formato JSON
            return resultadoJSON.toString();

        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarPickingListDestinos_tot(String sec, String fecha_ini, String fecha_fin, String usecod,
            String codalmant) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            StringBuilder sqlQuery = new StringBuilder(
                    "SELECT s.sisent, '', COUNT(s.siscod), s.siscod, COUNT(CASE WHEN pd.chkpick = 'S' THEN 1 END) * 100.0 / COUNT(s.siscod) AS avance, p.completado,CASE WHEN EXISTS (SELECT 1 FROM picking_cajas c INNER JOIN picking_detalle pd2 ON pd2.pickdetcod = c.pickdetcod INNER JOIN picking p2 ON p2.pickcod = pd2.pickcod WHERE p2.codpicklist = p.codpicklist AND pd2.siscod = s.siscod AND (ISNULL(c.cante, 0) != ISNULL(pd2.cante, 0) OR ISNULL(c.cantf, 0) != ISNULL(pd2.cantf, 0))) THEN 'N' ELSE 'S' END AS estado FROM picking_detalle pd INNER JOIN sistema s ON pd.siscod = s.siscod INNER JOIN picking p ON p.pickcod = pd.pickcod WHERE p.codalm = ?");

            // Agregar restricción de fecha si se proporcionan fechas válidas
            if (!fecha_ini.equals("") && !fecha_fin.equals("")) {
                sqlQuery.append(" AND CONVERT(char(10), p.feccre, 126) BETWEEN ? AND ?");
            }

            // Agregar restricciones de secuencia si se proporcionan secuencias válidas
            if (!sec.equals("")) {
                sqlQuery.append(" AND p.codpicklist = ?");
            }
            // if (!usecod.equals("")) {
            // sqlQuery.append(" AND v.usecod = ?");
            // }
            sqlQuery.append(" GROUP BY s.sisent, s.siscod,p.completado, p.codpicklist ORDER BY s.sisent");

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
                jsonObj.put("estado", fila[6]);
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
            String orden, String codalmant) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            StringBuilder sqlQuery = new StringBuilder(
                    "select p.despro, pd.codpro, p.codlab, pd.lote, pd.pickdetcod, pd.pickcod, pd.chkean13, pd.chkpick, pd.cante, pd.cantf, venc.fecven, qr.codalt, "
                            +
                            "(SELECT TOP 1 ubicacion_final FROM (" +
                            "SELECT abd.ubicacion as ubicacion_final FROM fa_stock_vencimientos v2 " +
                            "INNER JOIN fa_productos p2 ON p2.codpro=v2.codpro " +
                            "INNER JOIN almacenes_bartolito_detalle abd ON abd.codalm=v2.codalm AND abd.codpro=v2.codpro AND abd.lote=v2.codlot "
                            +
                            "WHERE v2.codpro=pd.codpro AND v2.codalm=? AND v2.codlot=pd.lote AND (abd.cante>0 OR abd.cantf>0) AND (v2.qtymov>0 OR v2.qtymov_m>0) "
                            +
                            "UNION " +
                            "SELECT 'Recepcion' as ubicacion_final FROM fa_stock_vencimientos v3 " +
                            "INNER JOIN fa_productos p3 ON p3.codpro=v3.codpro " +
                            "LEFT JOIN almacenes_bartolito_detalle abd2 ON abd2.codalm=v3.codalm AND abd2.codpro=v3.codpro AND abd2.lote=v3.codlot AND (abd2.cante>0 OR abd2.cantf>0) "
                            +
                            "WHERE v3.codpro=pd.codpro AND v3.codalm=? AND v3.codlot=pd.lote AND (v3.qtymov>0 OR v3.qtymov_m>0) "
                            +
                            "GROUP BY v3.codlot, v3.fecven, p3.stkfra, v3.qtymov_m, v3.qtymov " +
                            "HAVING (ISNULL(SUM(abd2.cante),0)*p3.stkfra)+ISNULL(SUM(abd2.cantf),0)<(v3.qtymov*p3.stkfra)+v3.qtymov_m"
                            +
                            ") AS ubicaciones) as ubicacion, " +
                            "(isnull(pd.cante,0)-isnull(sum(pca.cante),0))*p.stkfra+isnull(pd.cantf,0)-isnull(sum(pca.cantf),0) "
                            +
                            "from picking_detalle pd " +
                            "inner join fa_productos p on p.codpro=pd.codpro " +
                            "left join fa_codigos_alternos qr with (nolock) on qr.codpro = pd.codpro and qr.idcalt = 'B' "
                            +
                            "left join fa_stock_vencimientos venc with (nolock) on venc.codpro = pd.codpro and venc.codalm = ? and venc.codlot = pd.lote "
                            +
                            "left join fa_stock_almacenes stock with (nolock) on stock.codpro=pd.codpro and stock.codalm=? "
                            +
                            "inner join picking pc on pc.pickcod=pd.pickcod " +
                            "left join picking_cajas pca on pca.pickdetcod=pd.pickdetcod " +
                            "where 1=1");

            if (!fecha_ini.equals("") && !fecha_fin.equals("")) {
                sqlQuery.append(" AND CONVERT(char(10), pc.feccre, 126) BETWEEN ? AND ?");
            }

            // Agregar restricciones de secuencia si se proporcionan secuencias válidas
            if (!sec.equals("")) {
                sqlQuery.append(" AND pc.codpicklist = ?");
            }

            sqlQuery.append(
                    " AND pd.siscod=? " +
                            "group by p.despro, pd.codpro, p.codlab, pd.lote, pd.pickdetcod, pd.pickcod, pd.chkean13, pd.chkpick, pd.cante, pd.cantf, venc.fecven, qr.codalt, p.stkfra");

            if (orden.equals("sec")) {
                sqlQuery.append(" ORDER BY pd.pickdetcod");
            } else {
                sqlQuery.append(" ORDER BY p.despro");
            }

            Query query = em.createNativeQuery(sqlQuery.toString());
            int parameterIndex = 1;
            query.setParameter(parameterIndex++, codalmant); // para v2.codalm en primera parte del UNION
            query.setParameter(parameterIndex++, codalmant); // para v3.codalm en segunda parte del UNION
            query.setParameter(parameterIndex++, codalmant); // para venc.codalm
            query.setParameter(parameterIndex++, codalmant); // para stock.codalm

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
                jsonObj.put("pickcod", fila[5]);
                jsonObj.put("check1", fila[6]);
                jsonObj.put("check2", fila[7]);
                jsonObj.put("cante", fila[8]);
                jsonObj.put("cantf", fila[9]);
                jsonObj.put("fecven", fila[10]);
                jsonObj.put("codalt", fila[11]);
                jsonObj.put("ubipro", fila[12]); // Ahora viene de la subconsulta de ubicacion oficial
                jsonObj.put("equis", fila[13]);
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

    public String listarPickingListDet(String codalm, int secuencia) {

        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = null;
            String sqlQuery = "select pd.cante, pd.cantf, p.despro, venc.fecven, pd.pickdetcod, pd.pickcod, pd.chkean13, pd.chkpick, pd.lote, p.codlab from picking_detalle pd inner join fa_productos p on p.codpro=pd.codpro left join fa_stock_vencimientos venc with (nolock) on venc.codpro = pd.codpro and venc.codalm = ? and venc.codlot = pd.lote inner join picking pc on pc.pickcod=pd.pickcod where pd.pickdetcod=?";

            query = em.createNativeQuery(sqlQuery);
            query.setParameter(1, codalm);
            query.setParameter(2, secuencia);
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
            jsonObj.put("pickcod", fila[5]);
            jsonObj.put("check1", fila[6]);
            jsonObj.put("check2", fila[7]);
            jsonObj.put("lote", fila[8]);
            jsonObj.put("codlab", fila[9]);

            return jsonObj.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void create(Picking picking) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(picking);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(Picking picking) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            picking = em.merge(picking);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = picking.getPickcod();
                if (findPicking(id) == null) {
                    throw new NonexistentEntityException("The picking with id " + id + " no longer exists.");
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
            Picking picking;
            try {
                picking = em.getReference(Picking.class, id);
                picking.getPickcod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The picking with id " + id + " no longer exists.", enfe);
            }
            em.remove(picking);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<Picking> findPickingEntities() {
        return findPickingEntities(true, -1, -1);
    }

    public List<Picking> findPickingEntities(int maxResults, int firstResult) {
        return findPickingEntities(false, maxResults, firstResult);
    }

    private List<Picking> findPickingEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Picking.class));
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

    public Picking findPicking(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(Picking.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getPickingCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Picking> rt = cq.from(Picking.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
