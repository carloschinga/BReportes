/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import dto.Inventario;

/**
 *
 * @author USUARIO
 */
public class InventarioJpaController extends JpaPadre {

    public InventarioJpaController(String empresa) {
        super(empresa);
    }

    public String listarJson(int usecod, String grucod) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select DISTINCT i.codinv,i.desinv,i.estinv from inventario i left join inventario_almacen a on a.codinv=i.codinv left join inventario_detalle d on d.codinvalm=a.codinvalm where i.estado='S' and (d.usecod=? or 'SUPINV'=? or 'SUPERV'=? or 'JEFALM'=?)");
            int i = 1;
            query.setParameter(i++, usecod);
            query.setParameter(i++, grucod);
            query.setParameter(i++, grucod);
            query.setParameter(i++, grucod);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codinv", fila[0]);
                jsonObj.put("desinv", fila[1]);
                jsonObj.put("estinv", fila[2]);
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

    public String cerrarinventario(int codinv) {
        String result = "E"; // Valor por defecto es error
        EntityManager em = null;

        try {
            em = getEntityManager();

            StoredProcedureQuery query;
            query = em.createStoredProcedureQuery("cerrar_inventario");

            query.registerStoredProcedureParameter("codinv", Integer.class, ParameterMode.IN);
            // Configurar los parámetros del procedimiento almacenado
            query.setParameter("codinv", codinv);

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

    public String listarconsolidado(int codinv, int usecod, String grucod, String codalm) {// para picking las cajas y
                                                                                           // su itm
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            String codalmstring = "";
            if (!"".equals(codalm)) {
                codalmstring = " and codalm=?";
            }

            Query query = em.createNativeQuery(
                    "SELECT t.codpro, p.despro, t.lote, al.desalm, sum(t.tome) as tome, sum(t.tomf) as tomf, max(t.stkalm) as stkalm, max(t.stkalm_m) as stkalm_m,  (-MAX(t.stkalm) * p.stkfra - MAX(t.stkalm_m) + SUM(t.tome) * p.stkfra + SUM(t.tomf)) / p.stkfra AS total_stock_fracciones, (-MAX(t.stkalm) * p.stkfra - MAX(t.stkalm_m) + SUM(t.tome) * p.stkfra + SUM(t.tomf)) % p.stkfra AS remanente_fracciones , u.usenam,p.cospro FROM inventario_toma t INNER JOIN (SELECT DISTINCT t.codinvalm, a.codinvcab, a.numitm, t.codpro, t.lote, DENSE_RANK() OVER (PARTITION BY codinvcab, codpro, lote ORDER BY numitm DESC) AS rn FROM inventario_toma t INNER JOIN inventario_almacen a ON a.codinvalm = t.codinvalm WHERE codinv = ?"
                            + codalmstring
                            + ") ia ON ia.codinvalm = t.codinvalm AND t.codpro = ia.codpro AND t.lote = ia.lote AND ia.rn = 1 INNER JOIN fa_productos p ON p.codpro = t.codpro INNER JOIN inventario_almacen a ON a.codinvalm = t.codinvalm INNER JOIN fa_almacenes al ON al.codalm = a.codalm LEFT JOIN usuarios_inventario u ON u.usecod = t.usecodcree WHERE (t.usecodcree = ? OR 'SUPINV' = ?) GROUP BY t.codpro, p.despro, t.lote, al.desalm, p.stkfra,u.usenam,p.cospro having SUM(t.tome) >0 or SUM(t.tomf)>0");
            int i = 1;
            query.setParameter(i++, codinv);

            if (!"".equals(codalm)) {
                query.setParameter(i++, codalm);
            }
            query.setParameter(i++, usecod);
            query.setParameter(i++, grucod);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                i = 0;
                jsonObj.put("codpro", fila[i++]);
                jsonObj.put("despro", fila[i++]);
                jsonObj.put("lote", fila[i++]);
                jsonObj.put("codalm", fila[i++]);
                jsonObj.put("cante", fila[i++]);
                jsonObj.put("cantf", fila[i++]);
                jsonObj.put("stkalm", fila[i++]);
                jsonObj.put("stkalm_m", fila[i++]);
                jsonObj.put("dife", fila[i++]);
                jsonObj.put("diff", fila[i++]);
                jsonObj.put("usenam", fila[i++]);
                jsonObj.put("cospro", fila[i++]);
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

    public String listarconsolidadoagrupado(int codinv, int usecod, String grucod, String codalm) {// para picking las
                                                                                                   // cajas y su itm
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            String codalmstring = "";
            if (!"".equals(codalm)) {
                codalmstring = " and a.codalm=?";
            }
            Query query = em.createNativeQuery(
                    "SELECT t.codpro, p.despro, t.lote, al.desalm, SUM(t.tome) AS tome, SUM(t.tomf) AS tomf, MAX(t.stkalm) AS stkalm, MAX(t.stkalm_m) AS stkalm_m, (-MAX(t.stkalm) * p.stkfra - MAX(t.stkalm_m) + SUM(t.tome) * p.stkfra + SUM(t.tomf)) / p.stkfra AS total_stock_fracciones, (-MAX(t.stkalm) * p.stkfra - MAX(t.stkalm_m) + SUM(t.tome) * p.stkfra + SUM(t.tomf)) % p.stkfra AS remanente_fracciones,cospro FROM inventario_toma t INNER JOIN (SELECT DISTINCT t.codinvalm, a.codinvcab, a.numitm, t.codpro, t.lote, DENSE_RANK() OVER (PARTITION BY codinvcab, codpro, lote ORDER BY numitm DESC) AS rn FROM inventario_toma t INNER JOIN inventario_almacen a ON a.codinvalm = t.codinvalm WHERE codinv = ?"
                            + codalmstring
                            + ") ia ON ia.codinvalm = t.codinvalm AND t.codpro = ia.codpro AND t.lote = ia.lote AND ia.rn = 1 INNER JOIN fa_productos p ON p.codpro = t.codpro INNER JOIN inventario_almacen a ON a.codinvalm = t.codinvalm INNER JOIN fa_almacenes al ON al.codalm = a.codalm WHERE t.usecodinvcree = ? OR 'SUPINV' = ? GROUP BY t.codpro, p.despro, t.lote, al.desalm, p.stkfra,p.cospro having SUM(t.tome) >0 or SUM(t.tomf)>0");
            int i = 1;
            query.setParameter(i++, codinv);
            if (!"".equals(codalm)) {
                query.setParameter(i++, codalm);
            }
            query.setParameter(i++, usecod);
            query.setParameter(i++, grucod);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                i = 0;
                jsonObj.put("codpro", fila[i++]);
                jsonObj.put("despro", fila[i++]);
                jsonObj.put("lote", fila[i++]);
                jsonObj.put("codalm", fila[i++]);
                jsonObj.put("cante", fila[i++]);
                jsonObj.put("cantf", fila[i++]);
                jsonObj.put("stkalm", fila[i++]);
                jsonObj.put("stkalm_m", fila[i++]);
                jsonObj.put("dife", fila[i++]);
                jsonObj.put("diff", fila[i++]);
                jsonObj.put("cospro", fila[i++]);
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

    public String listarconsolidadoagrupadoproducto(int codinv, int usecod, String grucod, String codalm) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String codalmstring = "";
            String codalmstring2 = "";
            if (!"".equals(codalm)) {
                codalmstring = " and a.codalm=?";
                codalmstring2 = " and a2.codalm=?";
            }

            Query query = em.createNativeQuery(
                    "SELECT " +
                            "sub.codpro, sub.despro, sub.desalm, " +
                            "SUM(sub.tome) AS tome, SUM(sub.tomf) AS tomf, " +
                            "SUM(sub.stkalm) AS stkalm, SUM(sub.stkalm_m) AS stkalm_m, " +
                            "SUM(-sub.stkalm * sub.stkfra - sub.stkalm_m + sub.tome * sub.stkfra + sub.tomf) / sub.stkfra AS total_stock_fracciones, "
                            +
                            "SUM(-sub.stkalm * sub.stkfra - sub.stkalm_m + sub.tome * sub.stkfra + sub.tomf) % sub.stkfra AS remanente_fracciones, "
                            +
                            "sub.cospro, " +
                            "sub.cantidad_lotes " +
                            "FROM ( " +
                            "    SELECT " +
                            "        t.codpro, p.despro, t.lote, al.desalm, " +
                            "        MAX(t.stkalm) AS stkalm, MAX(t.stkalm_m) AS stkalm_m, " +
                            "        SUM(t.tome) AS tome, SUM(t.tomf) AS tomf, " +
                            "        p.stkfra, p.cospro, " +
                            "        (SELECT COUNT(DISTINCT t2.lote) " +
                            "         FROM inventario_toma t2 " +
                            "         INNER JOIN inventario_almacen a2 ON a2.codinvalm = t2.codinvalm " +
                            "         WHERE t2.codpro = t.codpro AND a2.codalm = a.codalm) AS cantidad_lotes " +
                            "    FROM inventario_toma t " +
                            "    INNER JOIN ( " +
                            "        SELECT t.codinvalm, a.codinvcab, a.numitm, t.codpro, t.lote, " +
                            "        DENSE_RANK() OVER (PARTITION BY a.codinvcab, t.codpro, t.lote ORDER BY a.numitm DESC) AS rn "
                            +
                            "        FROM inventario_toma t " +
                            "        INNER JOIN inventario_almacen a ON a.codinvalm = t.codinvalm " +
                            "        LEFT JOIN ( " +
                            "            SELECT a2.codinvcab, t2.codpro, max(a2.numitm) as numitm " +
                            "            FROM inventario_toma t2 " +
                            "            INNER JOIN inventario_almacen a2 ON a2.codinvalm = t2.codinvalm " +
                            "            WHERE t2.tome = 0 AND t2.tomf = 0 and a2.codinv = ?" + codalmstring2 +
                            "            GROUP BY a2.codinvcab, t2.codpro " +
                            "        ) filtro ON filtro.codinvcab = a.codinvcab AND filtro.codpro = t.codpro and filtro.numitm!=a.numitm "
                            +
                            "        WHERE filtro.codinvcab is null " + codalmstring +
                            "        AND a.codinv = ? GROUP BY t.codinvalm, a.codinvcab, a.numitm, t.codpro, t.lote" +
                            "    ) ia ON ia.codinvalm = t.codinvalm AND t.codpro = ia.codpro AND t.lote = ia.lote AND ia.rn = 1 "
                            +
                            "    INNER JOIN fa_productos p ON p.codpro = t.codpro " +
                            "    INNER JOIN inventario_almacen a ON a.codinvalm = t.codinvalm " +
                            "    INNER JOIN fa_almacenes al ON al.codalm = a.codalm " +
                            "    WHERE t.usecodinvcree = ? OR 'SUPINV' = ? " +
                            "    GROUP BY t.codpro, p.despro, t.lote, al.desalm, p.stkfra, p.cospro, a.codalm " +
                            ") sub " +
                            "GROUP BY sub.codpro, sub.despro, sub.desalm, sub.stkfra, sub.cospro, sub.cantidad_lotes");

            int i = 1;
            query.setParameter(i++, codinv);
            if (!"".equals(codalm)) {
                query.setParameter(i++, codalm);
                query.setParameter(i++, codalm);
            }
            query.setParameter(i++, codinv);
            query.setParameter(i++, usecod);
            query.setParameter(i++, grucod);

            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                int index = 0;
                jsonObj.put("codpro", fila[index++]);
                jsonObj.put("despro", fila[index++]);
                jsonObj.put("codalm", fila[index++]);
                jsonObj.put("cante", fila[index++]);
                jsonObj.put("cantf", fila[index++]);
                jsonObj.put("stkalm", fila[index++]);
                jsonObj.put("stkalm_m", fila[index++]);
                jsonObj.put("dife", fila[index++]);
                jsonObj.put("diff", fila[index++]);
                jsonObj.put("cospro", fila[index++]);
                jsonObj.put("cantidad_lotes", fila[index++]);
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

    public String listarconsolidadoagrupadoproductocapturas(int codinv, int usecod, String grucod, String codalm) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String codalmstring = "";
            if (!"".equals(codalm)) {
                codalmstring = " and a.codalm=?";
            }

            Query query = em.createNativeQuery(
                    "SELECT " +
                            "sub.codpro, " +
                            "sub.despro, " +
                            "sub.desalm, " +
                            "SUM(sub.tome) AS tome, " +
                            "SUM(sub.tomf) AS tomf, " +
                            "MAX(sub.stkalm) AS stkalm, " +
                            "MAX(sub.stkalm_m) AS stkalm_m, " +
                            "(MAX(-sub.stkalm) * sub.stkfra - MAX(sub.stkalm_m) + SUM(sub.tome) * sub.stkfra + SUM(sub.tomf)) / sub.stkfra AS total_stock_fracciones, "
                            +
                            "(MAX(-sub.stkalm) * sub.stkfra - MAX(sub.stkalm_m) + SUM(sub.tome) * sub.stkfra + SUM(sub.tomf)) % sub.stkfra AS remanente_fracciones, "
                            +
                            "sub.cospro, " +
                            "sub.cantidad_lotes " +
                            "FROM ( " +
                            "    SELECT " +
                            "        t.codpro, " +
                            "        p.despro, " +
                            "        t.lote, " +
                            "        al.desalm, " +
                            "        SUM(t.tome) AS tome, " +
                            "        SUM(t.tomf) AS tomf, " +
                            "        p.stkfra, " +
                            "        ISNULL(MAX(cap.stkalm), 0) AS stkalm, " +
                            "        ISNULL(MAX(cap.stkalm_m), 0) AS stkalm_m, " +
                            "        p.cospro, " +
                            "        (SELECT COUNT(DISTINCT t2.lote) " +
                            "         FROM inventario_toma t2 " +
                            "         INNER JOIN inventario_almacen a2 ON a2.codinvalm = t2.codinvalm " +
                            "         WHERE t2.codpro = t.codpro AND a2.codalm = a.codalm) AS cantidad_lotes " +
                            "    FROM inventario_toma t " +
                            "    INNER JOIN ( " +
                            "        SELECT DISTINCT " +
                            "            t.codinvalm, " +
                            "            a.codinvcab, " +
                            "            a.numitm, " +
                            "            t.codpro, " +
                            "            t.lote, " +
                            "            DENSE_RANK() OVER (PARTITION BY codinvcab, codpro, lote ORDER BY numitm DESC) AS rn "
                            +
                            "        FROM inventario_toma t " +
                            "        INNER JOIN inventario_almacen a ON a.codinvalm = t.codinvalm " +
                            "        WHERE codinv = ?" + codalmstring +
                            "    ) ia ON ia.codinvalm = t.codinvalm " +
                            "        AND t.codpro = ia.codpro " +
                            "        AND t.lote = ia.lote " +
                            "        AND ia.rn = 1 " +
                            "    INNER JOIN fa_productos p ON p.codpro = t.codpro " +
                            "    LEFT JOIN ( " +
                            "        SELECT " +
                            "            ca.codinvcab, " +
                            "            ca.codpro, " +
                            "            SUM(cante) AS stkalm, " +
                            "            SUM(cantf) AS stkalm_m " +
                            "        FROM capturastocksinventario ca " +
                            "        INNER JOIN inventario_almacen_cabecera c ON c.codinvcab = ca.codinvcab " +
                            "        WHERE c.codinv = ? " +
                            "        GROUP BY ca.codinvcab, ca.codpro " +
                            "    ) cap ON cap.codpro = t.codpro AND cap.codinvcab = ia.codinvcab " +
                            "    INNER JOIN inventario_almacen a ON a.codinvalm = t.codinvalm " +
                            "    INNER JOIN fa_almacenes al ON al.codalm = a.codalm " +
                            "    WHERE t.usecodinvcree = ? OR 'SUPINV' = ? " +
                            "    GROUP BY t.codpro, p.despro, t.lote, al.desalm, p.stkfra, p.cospro, a.codalm " +
                            ") sub " +
                            "GROUP BY sub.codpro, sub.despro, sub.desalm, sub.stkfra, sub.cospro, sub.cantidad_lotes");

            int i = 1;
            query.setParameter(i++, codinv);
            if (!"".equals(codalm)) {
                query.setParameter(i++, codalm);
            }
            query.setParameter(i++, codinv);
            query.setParameter(i++, usecod);
            query.setParameter(i++, grucod);

            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                int index = 0;
                jsonObj.put("codpro", fila[index++]);
                jsonObj.put("despro", fila[index++]);
                jsonObj.put("codalm", fila[index++]);
                jsonObj.put("cante", fila[index++]);
                jsonObj.put("cantf", fila[index++]);
                jsonObj.put("stkalm", fila[index++]);
                jsonObj.put("stkalm_m", fila[index++]);
                jsonObj.put("dife", fila[index++]);
                jsonObj.put("diff", fila[index++]);
                jsonObj.put("cospro", fila[index++]);
                jsonObj.put("cantidad_lotes", fila[index++]);
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

    public String listarconsolidadoalmacen(int codinvalm, int usecod, String grucod) {// para picking las cajas y su itm
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "SELECT t.codpro, p.despro, t.lote, al.desalm, t.tome, t.tomf, t.stkalm, t.stkalm_m, u.usenam, calc.total/p.stkfra AS total_stock_fracciones, calc.total%p.stkfra AS remanente_fracciones,p.cospro FROM inventario_toma t INNER JOIN fa_productos p ON p.codpro = t.codpro INNER JOIN inventario_almacen a ON a.codinvalm = t.codinvalm AND t.codinvalm= ? INNER JOIN fa_almacenes al ON al.codalm = a.codalm LEFT JOIN usuarios_inventario u ON u.usecod = t.usecodinvcree CROSS APPLY (SELECT (-ISNULL(t.stkalm,0)*p.stkfra-ISNULL(t.stkalm_m,0) + ISNULL(t.tome,0)*p.stkfra+ISNULL(t.tomf,0)) AS total) AS calc WHERE (t.usecodinvcree = ? OR 'SUPINV' = ?)  and (t.tome>0 or t.tomf>0);");
            int i = 1;
            query.setParameter(i++, codinvalm);
            query.setParameter(i++, usecod);
            query.setParameter(i++, grucod);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                i = 0;
                jsonObj.put("codpro", fila[i++]);
                jsonObj.put("despro", fila[i++]);
                jsonObj.put("lote", fila[i++]);
                jsonObj.put("codalm", fila[i++]);
                jsonObj.put("cante", fila[i++]);
                jsonObj.put("cantf", fila[i++]);
                jsonObj.put("stkalm", fila[i++]);
                jsonObj.put("stkalm_m", fila[i++]);
                jsonObj.put("usenam", fila[i++]);
                jsonObj.put("dife", fila[i++]);
                jsonObj.put("diff", fila[i++]);
                jsonObj.put("cospro", fila[i++]);
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

    public String listarconsolidadoalmacengeneral(int codinvalm) {// para picking las cajas y su itm
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "SELECT t.codpro, p.despro, t.lote, al.desalm, SUM(t.tome) AS total_tome, SUM(t.tomf) AS total_tomf, sr.stkalm_reciente, sr.stkalm_m_reciente, SUM(ISNULL(t.tome, 0)+ISNULL(t.tomf, 0))+sr.stkalm_reciente*p.stkfra+sr.stkalm_m_reciente / p.stkfra AS total_stock_fracciones, SUM(ISNULL(t.tome, 0)+ISNULL(t.tomf, 0))+sr.stkalm_reciente*p.stkfra+sr.stkalm_m_reciente % p.stkfra AS remanente_fracciones, ISNULL(MAX(adj.tome), 0) AS ajuste_tome, ISNULL(MAX(adj.tomf), 0) AS ajuste_tomf,p.cospro FROM inventario_toma t INNER JOIN fa_productos p ON p.codpro = t.codpro INNER JOIN inventario_almacen a ON a.codinvalm = t.codinvalm AND t.codinvalm = ? INNER JOIN fa_almacenes al ON al.codalm = a.codalm LEFT JOIN inventario_toma adj ON adj.codpro = t.codpro AND adj.codinvalm = t.codinvalm AND adj.lote = t.lote AND adj.tiptom = 'A' LEFT JOIN (SELECT t.codpro, t.lote, t.stkalm AS stkalm_reciente, t.stkalm_m AS stkalm_m_reciente, ROW_NUMBER() OVER (PARTITION BY t.codpro, t.lote ORDER BY t.feccre DESC) AS rn FROM inventario_toma t WHERE t.tiptom IS NULL and t.codinvalm = ?) sr ON sr.codpro = t.codpro AND sr.lote = t.lote GROUP BY t.codpro, p.despro, t.lote, al.desalm, p.stkfra, sr.stkalm_reciente, sr.stkalm_m_reciente,p.cospro ORDER BY t.codpro, t.lote;");
            int i = 1;
            query.setParameter(i++, codinvalm);
            query.setParameter(i++, codinvalm);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                i = 0;
                jsonObj.put("codpro", fila[i++]);
                jsonObj.put("despro", fila[i++]);
                jsonObj.put("lote", fila[i++]);
                jsonObj.put("codalm", fila[i++]);
                jsonObj.put("cante", fila[i++]);
                jsonObj.put("cantf", fila[i++]);
                jsonObj.put("stkalm", fila[i++]);
                jsonObj.put("stkalm_m", fila[i++]);
                jsonObj.put("dife", fila[i++]);
                jsonObj.put("diff", fila[i++]);
                jsonObj.put("cantea", fila[i++]);
                jsonObj.put("cantfa", fila[i++]);
                jsonObj.put("cospro", fila[i++]);
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

    public void create(Inventario inventario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(inventario);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(Inventario inventario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            inventario = em.merge(inventario);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = inventario.getCodinv();
                if (findInventario(id) == null) {
                    throw new NonexistentEntityException("The inventario with id " + id + " no longer exists.");
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
            Inventario inventario;
            try {
                inventario = em.getReference(Inventario.class, id);
                inventario.getCodinv();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The inventario with id " + id + " no longer exists.", enfe);
            }
            em.remove(inventario);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<Inventario> findInventarioEntities() {
        return findInventarioEntities(true, -1, -1);
    }

    public List<Inventario> findInventarioEntities(int maxResults, int firstResult) {
        return findInventarioEntities(false, maxResults, firstResult);
    }

    private List<Inventario> findInventarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Inventario.class));
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

    public Inventario findInventario(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(Inventario.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getInventarioCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Inventario> rt = cq.from(Inventario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
