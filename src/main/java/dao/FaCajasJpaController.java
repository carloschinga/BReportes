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
import org.json.XML;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.FaCajas;
import dto.FaCajasPK;

/**
 *
 * @author USUARIO
 */
public class FaCajasJpaController extends JpaPadre {

    public FaCajasJpaController(String empresa) {
        super(empresa);
    }

    public String listarJson(int invnum, int numitm) {// para picking las cajas y su itm
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            Query query = em.createNamedQuery("FaCajas.findByInvnumNumitm");
            query.setParameter("invnum", invnum);
            query.setParameter("numitm", numitm);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("caja", fila[0]);
                jsonObj.put("cante", fila[1]);
                jsonObj.put("cantf", fila[2]);
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

    public String listarSobrantesFaltantesJson(int ordenini, int ordenfin, int siscod) {// lista lo sobrantes faltantes
        EntityManager em = null;
        try {
            em = getEntityManager();
            StringBuilder queryString = new StringBuilder();
            queryString.append("SELECT mov.*, d.codpro, p.despro, o.ortrcod, o.feccre, s.sisent, ")
                    .append("ISNULL(mov.cante, 0) + ISNULL(mov.cantf / p.stkfra, 0) AS calculo, isnull(r.cantidad,0),r.estobs,r.estobsbot ")
                    .append("FROM ( ")
                    .append("  SELECT c.invnum, c.numitm, m.siscod_d, ")
                    .append("         ISNULL(SUM(c.canter), 0) - ISNULL(MAX(d.qtypro), 0) AS cante, ")
                    .append("         ISNULL(SUM(c.cantfr), 0) - ISNULL(MAX(d.qtypro_m), 0) AS cantf ")
                    .append("  FROM fa_cajas c ")
                    .append("  INNER JOIN fa_movimientos m ON m.invnum = c.invnum ")
                    .append("  INNER JOIN fa_orden_trabajo o ON o.invnum = m.invnum ")
                    .append("  INNER JOIN fa_movimientos_detalle d ON c.invnum = d.invnum AND c.numitm = d.numitm ")
                    .append("  WHERE o.chkcomrep = 'S' AND o.ortrcod BETWEEN ? AND ? ")
                    .append("  GROUP BY c.invnum, c.numitm, m.siscod_d ")
                    .append(") mov ")
                    .append("INNER JOIN fa_movimientos_detalle d ON d.invnum = mov.invnum AND d.numitm = mov.numitm ")
                    .append("INNER JOIN fa_productos p ON d.codpro = p.codpro ")
                    .append("INNER JOIN fa_orden_trabajo o ON o.invnum = mov.invnum ")
                    .append("INNER JOIN sistema s ON s.siscod = mov.siscod_d ")
                    .append("LEFT JOIN reposicion_recepcion r ON mov.siscod_d = r.siscod AND o.ortrcod = r.orden AND d.codpro = r.codpro AND r.estado = 'S' ")
                    .append("WHERE (mov.cante != 0 OR mov.cantf != 0) AND mov.siscod_d = ? ")
                    .append("UNION ")
                    .append("SELECT calculo.*, s.sisent, ")
                    .append("       calculo.cante + ISNULL(calculo.cantf / pr.stkfra, 0) AS calculo, isnull(r.cantidad,0),r.estobs,r.estobsbot ")
                    .append("FROM ( ")
                    .append("  SELECT '' AS invnum, '' AS numitm, ")
                    .append("         (SELECT TOP 1 m.siscod_d FROM fa_cajas c ")
                    .append("          INNER JOIN fa_movimientos m ON m.invnum = c.invnum ")
                    .append("          WHERE c.caja = e.caja) AS siscod_d, ")
                    .append("         e.cante, e.cantf, e.codpro, p.despro, e.orden, ")
                    .append("         (SELECT TOP 1 o.feccre FROM fa_orden_trabajo o WHERE e.orden = o.ortrcod) AS ortrcod ")
                    .append("  FROM cajaextra e ")
                    .append("  INNER JOIN fa_productos p ON p.codpro = e.codpro ")
                    .append("  WHERE e.orden BETWEEN ? AND ? and e.estado='S' ")
                    .append(") calculo ")
                    .append("INNER JOIN sistema s ON s.siscod = calculo.siscod_d ")
                    .append("INNER JOIN fa_productos pr ON calculo.codpro = pr.codpro AND calculo.siscod_d = ? ")
                    .append("LEFT JOIN reposicion_recepcion r ON calculo.siscod_d = r.siscod AND calculo.orden = r.orden AND calculo.codpro = r.codpro AND r.estado = 'S'  where (select top 1 o.chkcomrep from fa_orden_trabajo o where calculo.orden=o.ortrcod)='S'");

            Query query = em.createNativeQuery(queryString.toString());
            query.setParameter(1, ordenini);
            query.setParameter(2, ordenfin);
            query.setParameter(3, siscod);
            query.setParameter(4, ordenini);
            query.setParameter(5, ordenfin);
            query.setParameter(6, siscod);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("invnum", fila[0]);
                jsonObj.put("numitm", fila[1]);
                jsonObj.put("siscod", fila[2]);
                jsonObj.put("cante", fila[3]);
                jsonObj.put("cantf", fila[4]);
                jsonObj.put("codpro", fila[5]);
                jsonObj.put("despro", fila[6]);
                jsonObj.put("orden", fila[7]);
                jsonObj.put("feccre", fila[8]);
                jsonObj.put("sisent", fila[9]);
                jsonObj.put("calculo", fila[10]);
                jsonObj.put("cantidad", fila[11]);
                jsonObj.put("estobs", fila[12]);
                jsonObj.put("estobsbot", fila[13]);
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

    public String listarSobrantesFaltantesJson(int orden, int siscod) {// lista lo sobrantes faltantes
        EntityManager em = null;
        try {
            em = getEntityManager();
            StringBuilder queryString = new StringBuilder();
            queryString.append("SELECT mov.*, d.codpro, p.despro, o.ortrcod, o.feccre, s.sisent, ")
                    .append("ISNULL(mov.cante, 0) + (CAST(ISNULL(mov.cantf,0) AS DECIMAL(12, 5)) / p.stkfra) AS calculo, isnull(r.cantidad,0),r.estobs,r.estobsbot ")
                    .append("FROM ( ")
                    .append("  SELECT c.invnum, c.numitm, m.siscod_d, ")
                    .append("         ISNULL(SUM(c.canter), 0) - ISNULL(MAX(d.qtypro), 0) AS cante, ")
                    .append("         ISNULL(SUM(c.cantfr), 0) - ISNULL(MAX(d.qtypro_m), 0) AS cantf ")
                    .append("  FROM fa_cajas c ")
                    .append("  INNER JOIN fa_movimientos m ON m.invnum = c.invnum ")
                    .append("  INNER JOIN fa_orden_trabajo o ON o.invnum = m.invnum ")
                    .append("  INNER JOIN fa_movimientos_detalle d ON c.invnum = d.invnum AND c.numitm = d.numitm ")
                    .append("  WHERE o.chkcomrep = 'S' AND o.ortrcod = ? ")
                    .append("  GROUP BY c.invnum, c.numitm, m.siscod_d ")
                    .append(") mov ")
                    .append("INNER JOIN fa_movimientos_detalle d ON d.invnum = mov.invnum AND d.numitm = mov.numitm ")
                    .append("INNER JOIN fa_productos p ON d.codpro = p.codpro ")
                    .append("INNER JOIN fa_orden_trabajo o ON o.invnum = mov.invnum ")
                    .append("INNER JOIN sistema s ON s.siscod = mov.siscod_d ")
                    .append("LEFT JOIN reposicion_recepcion r ON mov.siscod_d = r.siscod AND o.ortrcod = r.orden AND d.codpro = r.codpro AND r.estado = 'S' ")
                    .append("WHERE (mov.cante != 0 OR mov.cantf != 0) AND mov.siscod_d = ? ")
                    .append("UNION ")
                    .append("SELECT calculo.*, s.sisent, ")
                    .append("       calculo.cante + ISNULL(calculo.cantf / pr.stkfra, 0) AS calculo, isnull(r.cantidad,0),r.estobs,r.estobsbot ")
                    .append("FROM ( ")
                    .append("  SELECT '' AS invnum, '' AS numitm, ")
                    .append("         (SELECT TOP 1 m.siscod_d FROM fa_cajas c ")
                    .append("          INNER JOIN fa_movimientos m ON m.invnum = c.invnum ")
                    .append("          WHERE c.caja = e.caja) AS siscod_d, ")
                    .append("         e.cante, e.cantf, e.codpro, p.despro, e.orden, ")
                    .append("         (SELECT TOP 1 o.feccre FROM fa_orden_trabajo o WHERE e.orden = o.ortrcod) AS ortrcod ")
                    .append("  FROM cajaextra e ")
                    .append("  INNER JOIN fa_productos p ON p.codpro = e.codpro ")
                    .append("  WHERE e.orden =? and e.estado='S' ")
                    .append(") calculo ")
                    .append("INNER JOIN sistema s ON s.siscod = calculo.siscod_d ")
                    .append("INNER JOIN fa_productos pr ON calculo.codpro = pr.codpro AND calculo.siscod_d = ? ")
                    .append("LEFT JOIN reposicion_recepcion r ON calculo.siscod_d = r.siscod AND calculo.orden = r.orden AND calculo.codpro = r.codpro AND r.estado = 'S'  where (select top 1 o.chkcomrep from fa_orden_trabajo o where calculo.orden=o.ortrcod)='S'");

            Query query = em.createNativeQuery(queryString.toString());
            query.setParameter(1, orden);
            query.setParameter(2, siscod);
            query.setParameter(3, orden);
            query.setParameter(4, siscod);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("invnum", fila[0]);
                jsonObj.put("numitm", fila[1]);
                jsonObj.put("siscod", fila[2]);
                jsonObj.put("cante", fila[3]);
                jsonObj.put("cantf", fila[4]);
                jsonObj.put("codpro", fila[5]);
                jsonObj.put("despro", fila[6]);
                jsonObj.put("orden", fila[7]);
                jsonObj.put("feccre", fila[8]);
                jsonObj.put("sisent", fila[9]);
                jsonObj.put("calculo", fila[10]);
                jsonObj.put("cantidad", fila[11]);
                jsonObj.put("estobs", fila[12]);
                jsonObj.put("estobsbot", fila[13]);
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

    public String listarporcentajesJson(int orden, int siscod) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "SELECT Secuencia, COUNT(CASE WHEN checkenvio = 'S' THEN 1 END) * 100.0 / COUNT(Secuencia),count(Secuencia),count(case when (ISNULL(cantecaja, 0) = ISNULL(canter, 0)) and (ISNULL(cantfcaja, 0) = ISNULL(cantfr, 0)) then 1 end) AS porcentaje_S FROM  view_orden_trabajo_caja WHERE orden = ? and siscod_d=? GROUP BY Secuencia;");
            query.setParameter(1, orden);
            query.setParameter(2, siscod);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("invnum", fila[0]);
                jsonObj.put("avance", fila[1]);
                jsonObj.put("total", fila[2]);
                jsonObj.put("conform", fila[3]);
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

    public String listarfaltantesexcedentes(int orden, int siscod) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            StringBuilder query = new StringBuilder();
            query.append("SELECT mov.*, d.codpro, p.despro, o.ortrcod, o.feccre, s.sisent, ");
            query.append("ISNULL(mov.cante,0) + ISNULL(mov.cantf / p.stkfra,0) + ISNULL(r.cantidad,0) AS calculo ");
            query.append("FROM ( ");
            query.append("SELECT c.invnum, c.numitm, m.siscod_d, ");
            query.append("ISNULL(SUM(c.canter),0) - ISNULL(MAX(d.qtypro),0) AS cante, ");
            query.append("ISNULL(SUM(c.cantfr),0) - ISNULL(MAX(d.qtypro_m),0) AS cantf ");
            query.append("FROM fa_cajas c ");
            query.append("INNER JOIN fa_movimientos m ON m.invnum = c.invnum ");
            query.append("INNER JOIN fa_orden_trabajo o ON o.invnum = m.invnum ");
            query.append("INNER JOIN fa_movimientos_detalle d ON c.invnum = d.invnum AND c.numitm = d.numitm ");
            query.append("WHERE o.chkcomrep = 'S' AND o.ortrcod = ? ");
            query.append("GROUP BY c.invnum, c.numitm, m.siscod_d ");
            query.append(") mov ");
            query.append("INNER JOIN fa_movimientos_detalle d ON d.invnum = mov.invnum AND d.numitm = mov.numitm ");
            query.append("INNER JOIN fa_productos p ON d.codpro = p.codpro ");
            query.append("INNER JOIN fa_orden_trabajo o ON o.invnum = mov.invnum ");
            query.append("INNER JOIN sistema s ON s.siscod = mov.siscod_d ");
            query.append("LEFT JOIN reposicion_recepcion r ON r.codpro = d.codpro AND r.orden = o.ortrcod ");
            query.append("AND r.siscod = mov.siscod_d AND r.estado = 'S' ");
            query.append("WHERE ISNULL(mov.cante,0) + ISNULL(mov.cantf / p.stkfra,0) + ISNULL(r.cantidad,0) != 0 ");
            query.append("AND mov.siscod_d = ? ");
            query.append("UNION ");
            query.append("SELECT calculo.*, s.sisent, ");
            query.append(
                    "ISNULL(calculo.cante,0) + ISNULL(calculo.cantf / pr.stkfra,0) + ISNULL(r.cantidad,0) AS calculo ");
            query.append("FROM ( ");
            query.append("SELECT '' AS invnum, '' AS numitm, ");
            query.append(
                    "(SELECT TOP 1 m.siscod_d FROM fa_cajas c INNER JOIN fa_movimientos m ON m.invnum = c.invnum ");
            query.append("WHERE c.caja = e.caja) AS siscod_d, ");
            query.append("e.cante, e.cantf, e.codpro, p.despro, e.orden, ");
            query.append("(SELECT TOP 1 o.feccre FROM fa_orden_trabajo o WHERE e.orden = o.ortrcod) AS feccre ");
            query.append("FROM cajaextra e ");
            query.append("INNER JOIN fa_productos p ON p.codpro = e.codpro ");
            query.append("WHERE e.orden = ? AND e.estado = 'S' ");
            query.append(") calculo ");
            query.append("INNER JOIN sistema s ON s.siscod = calculo.siscod_d ");
            query.append("INNER JOIN fa_productos pr ON calculo.codpro = pr.codpro ");
            query.append("LEFT JOIN reposicion_recepcion r ON calculo.siscod_d = r.siscod ");
            query.append("AND calculo.orden = r.orden AND calculo.codpro = r.codpro AND r.estado = 'S' ");
            query.append("WHERE calculo.siscod_d = ? ");
            query.append(
                    "AND ISNULL(calculo.cante,0) + ISNULL(calculo.cantf / pr.stkfra,0) + ISNULL(r.cantidad,0) != 0 ");
            query.append(
                    "AND (SELECT TOP 1 o.chkcomrep FROM fa_orden_trabajo o WHERE calculo.orden = o.ortrcod) = 'S' ");
            Query querym = em.createNativeQuery(query.toString());
            querym.setParameter(1, orden);
            querym.setParameter(2, siscod);
            querym.setParameter(3, orden);
            querym.setParameter(4, siscod);
            List<Object[]> resultados = querym.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("invnum", fila[0]);
                // jsonObj.put("numitm", fila[1]);
                // jsonObj.put("siscod", fila[2]);
                // jsonObj.put("cante", fila[3]);
                // jsonObj.put("cantf", fila[4]);
                jsonObj.put("codpro", fila[5]);
                jsonObj.put("despro", fila[6]);
                // jsonObj.put("fecha", fila[7]);
                // jsonObj.put("sisent", fila[8]);
                jsonObj.put("calculo", fila[10]);
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

    public String listarfaltantesexcedentespicking(int orden, int siscod) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query querym = em.createNativeQuery(
                    "SELECT mov.pickcajacod, mov.codpro, p.despro, ISNULL(mov.cante,0) + ISNULL(mov.cantf / p.stkfra,0) + ISNULL(r.cantidad,0) AS calculo FROM ( SELECT c.pickcajacod, p.codpicklist, d.siscod, ISNULL(SUM(c.canter),0) - ISNULL(MAX(d.cante),0) AS cante, ISNULL(SUM(c.cantfr),0) - ISNULL(MAX(d.cantf),0) AS cantf, d.codpro FROM picking_cajas c INNER JOIN picking_detalle d ON d.pickdetcod=c.pickdetcod INNER JOIN picking p ON p.pickcod=d.pickcod WHERE d.chkcomrep = 'S' AND p.codpicklist = ? AND d.siscod=? GROUP BY c.pickcajacod, d.siscod,p.codpicklist,d.codpro ) mov INNER JOIN fa_productos p ON mov.codpro = p.codpro LEFT JOIN reposicion_recepcion r ON r.codpro = mov.codpro AND r.orden = mov.codpicklist AND r.siscod = mov.siscod AND r.estado = 'S' WHERE (r.estobs !='R' or r.estobsbot !='R') UNION SELECT '', calculo.codpro, calculo.despro, ISNULL(calculo.cante,0) + ISNULL(calculo.cantf / pr.stkfra,0) + ISNULL(r.cantidad,0) AS calculo FROM ( SELECT e.siscod, e.cante, e.cantf, e.codpro, p.despro, e.orden FROM cajaextra e INNER JOIN fa_productos p ON p.codpro = e.codpro WHERE e.orden = ? AND e.estado = 'S' ) calculo INNER JOIN fa_productos pr ON calculo.codpro = pr.codpro LEFT JOIN reposicion_recepcion r ON calculo.siscod = r.siscod AND calculo.orden = r.orden AND calculo.codpro = r.codpro AND r.estado = 'S' WHERE calculo.siscod = ? and (r.estobs !='R' or r.estobsbot !='R');");
            querym.setParameter(1, orden);
            querym.setParameter(2, siscod);
            querym.setParameter(3, orden);
            querym.setParameter(4, siscod);
            List<Object[]> resultados = querym.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("invnum", fila[0]);
                // jsonObj.put("numitm", fila[1]);
                // jsonObj.put("siscod", fila[2]);
                // jsonObj.put("cante", fila[3]);
                // jsonObj.put("cantf", fila[4]);
                jsonObj.put("codpro", fila[1]);
                jsonObj.put("despro", fila[2]);
                // jsonObj.put("fecha", fila[7]);
                // jsonObj.put("sisent", fila[8]);
                jsonObj.put("calculo", fila[3]);
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

    public String listarordenesseguimiento(String fecini, String fecfin, String orden, String siscod) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            StringBuilder queryString = new StringBuilder();
            queryString.append("SELECT o.ortrcod, MAX(o.feccre), m.siscod_d,s.sisent, o.chkcomrep, ");
            queryString.append(
                    "CASE WHEN mov.ortrcod IS NULL THEN 'S' ELSE 'N' END,case when sum(noregu)>0 then 'N' else 'S' end ");
            queryString.append("FROM fa_orden_trabajo o ");
            queryString.append("INNER JOIN fa_movimientos m ON o.invnum = m.invnum ");
            queryString.append("INNER JOIN sistema s on s.siscod = m.siscod_d ");
            queryString.append("LEFT JOIN ( ");
            queryString.append("  SELECT c.invnum, c.numitm, m.siscod_d, o.ortrcod, d.codpro, ");
            queryString.append(
                    "  sum(case when isnull(r.estobs,'N')<>'R' or isnull(r.estobsbot,'N')<>'R' then 1 else 0 end) as noregu ");
            queryString.append("  FROM fa_cajas c ");
            queryString.append("  INNER JOIN fa_movimientos m ON m.invnum = c.invnum ");
            queryString.append("  INNER JOIN fa_orden_trabajo o ON o.invnum = m.invnum ");
            queryString.append("  INNER JOIN fa_movimientos_detalle d ON c.invnum = d.invnum AND c.numitm = d.numitm ");
            queryString.append(
                    "  LEFT JOIN reposicion_recepcion r ON r.codpro=d.codpro and r.siscod=m.siscod_d and r.orden=o.ortrcod ");
            queryString.append("  WHERE o.chkcomrep = 'S' ");
            if (!"".equals(orden)) {
                queryString.append("  AND o.ortrcod = ? ");
            }
            queryString.append("  GROUP BY c.invnum, c.numitm, m.siscod_d, o.ortrcod, d.codpro ");
            queryString.append("  HAVING ISNULL(SUM(c.canter), 0) - ISNULL(MAX(d.qtypro), 0) <> 0 ");
            queryString.append("     or ISNULL(SUM(c.cantfr), 0) - ISNULL(MAX(d.qtypro_m), 0) <> 0 ");

            if (!"".equals(fecini)) {
                queryString.append("  AND CONVERT(char(10), MAX(o.feccre), 126) >=?");
            }

            if (!"".equals(fecfin)) {
                queryString.append("  AND CONVERT(char(10), MAX(o.feccre), 126) <=?");
            }
            queryString.append("  UNION ");
            queryString.append("  SELECT '' AS invnum, '' AS numitm, m.siscod_d, e.orden, e.codpro, ");
            queryString.append("  sum(case when r.estobs<>'R' or r.estobsbot<>'R' then 1 else 0 end) as noregu ");
            queryString.append("  FROM cajaextra e ");
            queryString.append("  INNER JOIN fa_productos p ON p.codpro = e.codpro ");
            queryString.append("  INNER JOIN fa_orden_trabajo o ON o.ortrcod = e.orden ");
            queryString.append("  INNER JOIN fa_cajas c ON c.caja = e.caja ");
            queryString.append("  INNER JOIN fa_movimientos m ON m.invnum = c.invnum ");
            queryString.append(
                    "  LEFT JOIN reposicion_recepcion r ON r.codpro=e.codpro and r.siscod=m.siscod_d and r.orden=e.orden ");
            queryString.append("  WHERE e.estado = 'S' AND o.chkcomrep = 'S' ");
            if (!"".equals(orden)) {
                queryString.append("  AND e.orden = ? ");
            }
            queryString.append("  GROUP BY m.siscod_d, e.orden, e.codpro ");
            queryString.append("  HAVING 1=1 ");
            if (!"".equals(fecini)) {
                queryString.append("  AND CONVERT(char(10), MAX(o.feccre), 126) >=?");
            }

            if (!"".equals(fecfin)) {
                queryString.append("  AND CONVERT(char(10), MAX(o.feccre), 126) <=?");
            }
            queryString.append(") mov ON mov.ortrcod = o.ortrcod AND m.siscod_d = mov.siscod_d ");
            queryString.append("WHERE 1=1 ");
            if (!"".equals(orden)) {
                queryString.append("and o.ortrcod = ? ");
            }
            if (!"".equals(siscod)) {
                queryString.append("and m.siscod_d = ? ");
            }

            queryString.append("GROUP BY o.ortrcod, m.siscod_d, o.chkcomrep, mov.ortrcod,s.sisent ");
            queryString.append("  HAVING 1=1 ");
            if (!"".equals(fecini)) {
                queryString.append("  AND CONVERT(char(10), MAX(o.feccre), 126) >=?");
            }

            if (!"".equals(fecfin)) {
                queryString.append("  AND CONVERT(char(10), MAX(o.feccre), 126) <=?");
            }
            Query querym = em.createNativeQuery(queryString.toString());
            int parametro = 1;
            if (!"".equals(orden)) {
                querym.setParameter(parametro++, Integer.parseInt(orden));
            }
            if (!"".equals(fecini)) {
                querym.setParameter(parametro++, fecini);
            }
            if (!"".equals(fecfin)) {
                querym.setParameter(parametro++, fecfin);
            }
            if (!"".equals(orden)) {
                querym.setParameter(parametro++, Integer.parseInt(orden));
            }
            if (!"".equals(fecini)) {
                querym.setParameter(parametro++, fecini);
            }

            if (!"".equals(fecfin)) {
                querym.setParameter(parametro++, fecfin);
            }
            if (!"".equals(orden)) {
                querym.setParameter(parametro++, Integer.parseInt(orden));
            }
            if (!"".equals(siscod)) {
                querym.setParameter(parametro++, Integer.parseInt(siscod));
            }
            if (!"".equals(fecini)) {
                querym.setParameter(parametro++, fecini);
            }
            if (!"".equals(fecfin)) {
                querym.setParameter(parametro++, fecfin);
            }
            List<Object[]> resultados = querym.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("orden", fila[0]);
                jsonObj.put("fecha", fila[1]);
                jsonObj.put("siscod", fila[2]);
                jsonObj.put("sisent", fila[3]);
                jsonObj.put("estatus", fila[4]);
                jsonObj.put("conforme", fila[5]);
                jsonObj.put("regul", fila[6]);
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

    public String listarporcentajesJson(int orden, int siscod, String caja) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "SELECT Secuencia, COUNT(CASE WHEN checkenvio = 'S' THEN 1 END) * 100.0 / COUNT(Secuencia),count(Secuencia),count(case when (ISNULL(cantecaja, 0) = ISNULL(canter, 0)) and (ISNULL(cantfcaja, 0) = ISNULL(cantfr, 0)) then 1 end) AS porcentaje_S FROM  view_orden_trabajo_caja WHERE orden = ? and siscod_d=? and caja=? GROUP BY Secuencia;");
            query.setParameter(1, orden);
            query.setParameter(2, siscod);
            query.setParameter(3, caja);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("invnum", fila[0]);
                jsonObj.put("avance", fila[1]);
                jsonObj.put("total", fila[2]);
                jsonObj.put("conform", fila[3]);
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

    public String recepcionar_guardar(String json, int usecod) {
        String result = "E"; // Valor por defecto es error
        EntityManager em = null;

        try {
            JSONObject jsonobj = new JSONObject(json);
            String caja = jsonobj.getString("caja");
            int orden = jsonobj.getInt("orden");
            // Convertir el JSON a un JSONArray
            JSONArray jsonArray = jsonobj.getJSONArray("recepcion");
            replaceNullsWithZero(jsonArray, new String[] { "cante", "cantf" });

            // Crear un nuevo JSONObject con el nombre de la raíz "Registros"
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Registro", jsonArray);

            // Convertir el JSONObject a XML con la raíz "Registros"
            String xmlrecepcion = XML.toString(jsonObject, "Registros");

            jsonArray = jsonobj.getJSONArray("extra");
            replaceNullsWithZero(jsonArray, new String[] { "cante", "cantf" });
            jsonObject = new JSONObject();
            jsonObject.put("Registro", jsonArray);
            String xmlextra = XML.toString(jsonObject, "Registros");

            jsonArray = jsonobj.getJSONArray("eliminar");
            jsonObject = new JSONObject();
            jsonObject.put("Registro", jsonArray);
            String xmleliminar = XML.toString(jsonObject, "Registros");

            em = getEntityManager();

            StoredProcedureQuery query;
            query = em.createStoredProcedureQuery("guardar_recepcion");

            query.registerStoredProcedureParameter("xmlrecepcion", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("xmlextra", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("xmleliminar", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("caja", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("orden", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("usecod", Integer.class, ParameterMode.IN);

            // Configurar los parámetros del procedimiento almacenado
            query.setParameter("xmlrecepcion", xmlrecepcion); // XML generado a partir del JSON
            query.setParameter("xmlextra", xmlextra); // XML generado a partir del JSON
            query.setParameter("xmleliminar", xmleliminar); // XML generado a partir del JSON
            query.setParameter("caja", caja);
            query.setParameter("orden", orden);
            query.setParameter("usecod", usecod);

            // Ejecutar el procedimiento almacenado
            query.execute();

            return "S";

        } catch (Exception e) {
            e.printStackTrace();
            result = "E"; // Error
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
        return result;
    }

    private void replaceNullsWithZero(JSONArray jsonArray, String[] fields) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            for (String field : fields) {
                if (obj.isNull(field)) { // Verificar si el campo es null
                    obj.put(field, 0); // Reemplazar null con 0
                }
            }
        }
    }

    public String EliminarCajas(int invnum, int numitm) {
        String result = "E"; // Default to error
        EntityManager em = null;
        try {
            em = getEntityManager(); // Asumiendo que tienes un método para obtener el EntityManager

            // Crear el StoredProcedureQuery para llamar al procedimiento almacenado
            StoredProcedureQuery query = em.createStoredProcedureQuery("EliminarCajas");

            // Registrar los parámetros del procedimiento almacenado
            query.registerStoredProcedureParameter("invnum", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("numitm", Integer.class, ParameterMode.IN);

            // Establecer los valores de los parámetros
            query.setParameter("invnum", invnum);
            query.setParameter("numitm", numitm);

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

    public String listarcantavanceJson(int siscod, int orden) {// para recepcion de cajas, lista las cajas y su cantidad
                                                               // de items segun establecimiento
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select count(caja) as cantidad,COUNT(CASE WHEN checkenvio = 'S' THEN 1 END)*100/count(caja) AS cantidad_envios,caja,orden,siscod_d from view_orden_trabajo_caja where siscod_d=? and orden=? group by caja,orden,siscod_d order by caja");
            query.setParameter(1, siscod);
            query.setParameter(2, orden);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("cantidad", fila[0]);
                jsonObj.put("avance", fila[1]);
                jsonObj.put("caja", fila[2]);
                jsonObj.put("orden", fila[3]);
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

    public String listarcantavanceJsonpicking(int siscod, int orden) {// para recepcion de cajas, lista las cajas y su
                                                                      // cantidad de items segun establecimiento
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select count(c.pickcajacod),count(CASE WHEN c.checkenvio = 'S' THEN 1 END)*100/count(c.pickcajacod),c.caja,pc.codpicklist,d.siscod from picking_cajas c inner join picking_detalle d on c.pickdetcod=d.pickdetcod inner join picking pc on pc.pickcod=d.pickcod inner join pickinglist pl on pl.codpicklist=pc.codpicklist where d.siscod=? and pc.codpicklist=? group by pc.codpicklist, c.caja, d.siscod");
            query.setParameter(1, siscod);
            query.setParameter(2, orden);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("cantidad", fila[0]);
                jsonObj.put("avance", fila[1]);
                jsonObj.put("caja", fila[2]);
                jsonObj.put("orden", fila[3]);
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

    public String comprobarCaja(int siscod, int orden, String caja) {// para recepcion de cajas, lista las cajas y su
                                                                     // cantidad de items segun establecimiento
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select COUNT(CASE WHEN checkenvio = 'S' THEN 1 END)*100/count(caja) AS cantidad_envios from view_orden_trabajo_caja where siscod_d=? and orden=? and caja=?");
            query.setParameter(1, siscod);
            query.setParameter(2, orden);
            query.setParameter(3, caja);
            Object resultados = query.getSingleResult();
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("avance", resultados);
            return jsonObj.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String comprobarCajapicking(int siscod, int orden, String caja) {// para recepcion de cajas, lista las cajas
                                                                            // y su cantidad de items segun
                                                                            // establecimiento
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select COUNT(CASE WHEN c.checkenvio = 'S' THEN 1 END)*100/count(c.caja) AS cantidad_envios from picking_cajas c inner join picking_detalle pd on pd.pickdetcod=c.pickdetcod inner join picking p on p.pickcod=pd.pickcod where pd.siscod=? and p.codpicklist=? and c.caja=?");
            query.setParameter(1, siscod);
            query.setParameter(2, orden);
            query.setParameter(3, caja);
            Object resultados = query.getSingleResult();
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("avance", resultados);
            return jsonObj.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String secuencias(int siscod, String caja, int orden) {// para recepcion de cajas, lista las cajas y su
                                                                  // cantidad de items segun establecimiento
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select c.invnum from fa_cajas c inner join fa_orden_trabajo o on c.invnum=o.invnum inner join fa_movimientos m on m.invnum=o.invnum and siscod_d=? where c.caja=? and o.ortrcod=? group by c.invnum");
            query.setParameter(1, siscod);
            query.setParameter(2, caja);
            query.setParameter(3, orden);
            List<Integer> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Integer fila : resultados) {
                jsonArray.put(fila);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarcajas(int orden) {// para recepcion de cajas, lista las cajas y su cantidad de items segun
                                          // establecimiento
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select caja,orden,Destino from view_orden_trabajo_caja_nuevo where orden=? group by caja,orden,Destino order by Destino,caja");
            query.setParameter(1, orden);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("caja", fila[0]);
                jsonObj.put("orden", fila[1]);
                jsonObj.put("destino", fila[2]);
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

    public String listarcajas(String caja) {// para recepcion de cajas, lista las cajas y su cantidad de items segun
                                            // establecimiento
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "SELECT TOP 1 t.caja, p1.codpicklist, sisent, (SELECT COUNT(DISTINCT c.caja) FROM picking_cajas c INNER JOIN picking_detalle pd ON pd.pickdetcod = c.pickdetcod INNER JOIN picking p ON p.pickcod = pd.pickcod WHERE p.codpicklist = p1.codpicklist AND pd.siscod = pd1.siscod) AS cajas FROM picking_cajas t INNER JOIN picking_detalle pd1 ON pd1.pickdetcod = t.pickdetcod INNER JOIN picking p1 ON p1.pickcod = pd1.pickcod INNER JOIN sistema s ON pd1.siscod = s.siscod WHERE t.caja = ? ORDER BY p1.codpicklist DESC;");
            query.setParameter(1, caja);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("caja", fila[0]);
                jsonObj.put("orden", fila[1]);
                jsonObj.put("destino", fila[2]);
                jsonObj.put("cant", fila[3]);
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

    public String listarcajas(int orden, String caja, int siscod) { // para recepcion de cajas, lista las cajas y su
                                                                    // cantidad de items segun establecimiento
        EntityManager em = null;
        try {// se cambio para la nueva version
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select caja,orden,Destino,(select count(DISTINCT caja) as cajas from view_orden_trabajo_caja_nuevo where orden=? and siscod=?) from view_orden_trabajo_caja_nuevo where orden=? and caja=? and siscod=? group by caja,orden,Destino order by Destino,caja");

            query.setParameter(1, orden);
            query.setParameter(2, siscod);
            query.setParameter(3, orden);
            query.setParameter(4, caja);
            query.setParameter(5, siscod);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("caja", fila[0]);
                jsonObj.put("orden", fila[1]);
                jsonObj.put("destino", fila[2]);
                jsonObj.put("cant", fila[3]);
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

    public String updateCheckEnvio(String caja, int orden, int siscod_d, String json, int usecod) {
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

            // Crear el StoredProcedureQuery
            StoredProcedureQuery query = em.createStoredProcedureQuery("sp_UpdateCheckEnvio");
            query.registerStoredProcedureParameter("XmlData", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("Caja", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("Orden", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("SisCod_D", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("Usecod", Integer.class, ParameterMode.IN);

            // Configurar los parámetros del procedimiento almacenado
            query.setParameter("XmlData", xml); // XML generado a partir del JSON
            query.setParameter("Caja", caja);
            query.setParameter("Orden", orden);
            query.setParameter("SisCod_D", siscod_d);
            query.setParameter("Usecod", usecod);

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

    public String listarfechas(String fecha_ini, String fecha_fin, int siscod) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sqlQuery = "select count(orden) as cantidad,COUNT(CASE WHEN checkenvio = 'S' THEN 1 END)*100/count(orden) AS cantidad_envios,orden,fecha,siscod_d from view_orden_trabajo_caja where  fecha BETWEEN ? AND ? and siscod_d=?  group by orden,siscod_d,fecha order by orden";

            Query query = em.createNativeQuery(sqlQuery);

            int parameterIndex = 1;

            query.setParameter(parameterIndex++, fecha_ini);
            query.setParameter(parameterIndex++, fecha_fin);
            query.setParameter(parameterIndex++, siscod);

            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("cantidad", fila[0]);
                jsonObj.put("ortrcod", fila[2]);
                jsonObj.put("fecha", fila[3]);
                jsonObj.put("avance", fila[1]);
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

    public String listarfechaspicking(String fecha_ini, String fecha_fin, int siscod) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sqlQuery = "select count(c.pickcajacod),count(CASE WHEN c.checkenvio = 'S' THEN 1 END)*100/count(c.pickcajacod),pc.codpicklist,pl.feccre,d.siscod from picking_cajas c inner join picking_detalle d on c.pickdetcod=d.pickdetcod inner join picking pc on pc.pickcod=d.pickcod inner join pickinglist pl on pl.codpicklist=pc.codpicklist where CONVERT(char(10), pl.feccre, 126) between ? and ? and d.siscod=? and pl.chktxt='S' group by pc.codpicklist, pl.feccre, d.siscod";

            Query query = em.createNativeQuery(sqlQuery);

            int parameterIndex = 1;

            query.setParameter(parameterIndex++, fecha_ini);
            query.setParameter(parameterIndex++, fecha_fin);
            query.setParameter(parameterIndex++, siscod);

            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("cantidad", fila[0]);
                jsonObj.put("ortrcod", fila[2]);
                jsonObj.put("fecha", fila[3]);
                jsonObj.put("avance", fila[1]);
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

    public String listarfechasnosiscod(String fecha_ini, String fecha_fin) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sqlQuery = "select orden,fecha from view_orden_trabajo_caja where  fecha BETWEEN ? AND ?   group by orden,fecha order by orden";

            Query query = em.createNativeQuery(sqlQuery);

            int parameterIndex = 1;

            query.setParameter(parameterIndex++, fecha_ini);
            query.setParameter(parameterIndex++, fecha_fin);

            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("ortrcod", fila[0]);
                jsonObj.put("fecha", fila[1]);
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

    public String listardetJson(String caja, int orden, int siscod) {// para recepcion de cajas, lista las cajas y su
                                                                     // cantidad de items segun establecimiento
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select codpro,Secuencia,Descripcion,Lote,codalt,cantecaja,cantfcaja,checkenvio,numitm,codalt,fecven,canter,cantfr,stkfra from view_orden_trabajo_caja where caja=? and orden=? and siscod_d=? order by Descripcion");
            query.setParameter(1, caja);
            query.setParameter(2, orden);
            query.setParameter(3, siscod);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codpro", fila[0]);
                jsonObj.put("Secuencia", fila[1]);
                jsonObj.put("Descripcion", fila[2]);
                jsonObj.put("Lote", fila[3]);
                jsonObj.put("codalt", fila[4]);
                jsonObj.put("cantecaja", fila[5]);
                jsonObj.put("cantfcaja", fila[6]);
                jsonObj.put("checkenvio", fila[7]);
                jsonObj.put("numitm", fila[8]);
                jsonObj.put("qr", fila[9]);
                jsonObj.put("fecven", fila[10]);
                jsonObj.put("canter", fila[11]);
                jsonObj.put("cantfr", fila[12]);
                jsonObj.put("stkfra", fila[13]);
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

    public String listardetJsonpicking(String caja, int orden, int siscod) {// para recepcion de cajas, lista las cajas
                                                                            // y su cantidad de items segun
                                                                            // establecimiento
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "SELECT pd.codpro, pd.pickdetcod, p.despro, pd.lote, codalt.codalt, pc.cante, pc.cantf, pc.checkenvio, pc.pickcajacod, codalt.codalt, venc.fecven, pc.canter, pc.cantfr, p.stkfra FROM picking_cajas pc INNER JOIN picking_detalle pd ON pd.pickdetcod = pc.pickdetcod INNER JOIN fa_productos p ON pd.codpro = p.codpro LEFT JOIN (SELECT ca.codpro, COALESCE(MAX(CASE WHEN ca.idcalt = 'B' THEN ca.codalt END), MAX(CASE WHEN ca.idcalt = 'C' THEN ca.codalt END)) AS codalt FROM fa_codigos_alternos ca WITH (NOLOCK) INNER JOIN picking_detalle pd ON pd.codpro = ca.codpro INNER JOIN picking p ON p.codpicklist = ? WHERE ca.idcalt IN ('B', 'C') GROUP BY ca.codpro) codalt ON codalt.codpro = pd.codpro INNER JOIN picking pik ON pik.pickcod = pd.pickcod INNER JOIN fa_stock_vencimientos venc ON venc.codpro = pd.codpro AND venc.codlot = pd.lote AND venc.codalm = pik.codalm WHERE pc.caja = ? AND pik.codpicklist = ? AND pd.siscod = ?");
            query.setParameter(1, orden);
            query.setParameter(2, caja);
            query.setParameter(3, orden);
            query.setParameter(4, siscod);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codpro", fila[0]);
                jsonObj.put("Secuencia", fila[1]);
                jsonObj.put("Descripcion", fila[2]);
                jsonObj.put("Lote", fila[3]);
                jsonObj.put("codalt", fila[4]);
                jsonObj.put("cantecaja", fila[5]);
                jsonObj.put("cantfcaja", fila[6]);
                jsonObj.put("checkenvio", fila[7]);
                jsonObj.put("numitm", fila[8]);
                jsonObj.put("qr", fila[9]);
                jsonObj.put("fecven", fila[10]);
                jsonObj.put("canter", fila[11]);
                jsonObj.put("cantfr", fila[12]);
                jsonObj.put("stkfra", fila[13]);
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

    public void create(FaCajas faCajas) throws PreexistingEntityException, Exception {
        if (faCajas.getFaCajasPK() == null) {
            faCajas.setFaCajasPK(new FaCajasPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(faCajas);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFaCajas(faCajas.getFaCajasPK()) != null) {
                throw new PreexistingEntityException("FaCajas " + faCajas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(FaCajas faCajas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            faCajas = em.merge(faCajas);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                FaCajasPK id = faCajas.getFaCajasPK();
                if (findFaCajas(id) == null) {
                    throw new NonexistentEntityException("The faCajas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(FaCajasPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FaCajas faCajas;
            try {
                faCajas = em.getReference(FaCajas.class, id);
                faCajas.getFaCajasPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The faCajas with id " + id + " no longer exists.", enfe);
            }
            em.remove(faCajas);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<FaCajas> findFaCajasEntities() {
        return findFaCajasEntities(true, -1, -1);
    }

    public List<FaCajas> findFaCajasEntities(int maxResults, int firstResult) {
        return findFaCajasEntities(false, maxResults, firstResult);
    }

    private List<FaCajas> findFaCajasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FaCajas.class));
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

    public FaCajas findFaCajas(FaCajasPK id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(FaCajas.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getFaCajasCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FaCajas> rt = cq.from(FaCajas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
