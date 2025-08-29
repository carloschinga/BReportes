package dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import dto.DistribuirAlmacenCentral;
import dto.DistribuirAlmacenEstablecimientos;
import dto.FaProductos;
import dto.Inventario;
import dto.TiposProducto;

/**
 *
 * @author USUARIO
 */
public class FaProductosSPJpaController extends FaProductosJpaController {

    public FaProductosSPJpaController(String empresa) {
        super(empresa);
    }

    public String listar(int tipoStkMin, String tipoDistrib, List<String> codTip, int secuencia, String indicaFecha,
            Date fecha1, String solorojos, String soloalm, List<String> codalm, String distpor, String solo0,
            BigDecimal multiplicador, String codalminv) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String tiposParam = String.join(",", codTip);
            String almparam = String.join(",", codalm);

            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT p.codpro, p.despro, p.codlab, p.stkfra, s.stkalm, s.stkalm_m, s.codalm, ");
            queryBuilder.append("(SUM(x.stkalm) * p.stkfra + SUM(x.stkalm_m)) / p.stkfra as stock_estab, ");
            queryBuilder.append("p.codtip, t.destip, ");
            if (distpor.equals("STKMIN")) {
                if (solorojos.equals("N")) {
                    queryBuilder.append("CASE ");
                    queryBuilder.append("WHEN COUNT(cs.codpro) > 0 ");
                    queryBuilder.append(" THEN 'S' ");
                    queryBuilder.append("ELSE 'N' ");
                    queryBuilder.append("END AS tienerojos ");
                } else {
                    queryBuilder.append("'S' AS tienerojos ");
                }
            } else {
                if (solorojos.equals("N")) {
                    queryBuilder.append("CASE ");
                    queryBuilder.append("WHEN COUNT(cs.codpro) > 0 ");
                    queryBuilder.append(" THEN 'S' ");
                    queryBuilder.append("ELSE 'N' ");
                    queryBuilder.append("END AS tienerojos ");
                } else {
                    queryBuilder.append("'S' AS tienerojos ");
                }
            }
            queryBuilder.append("FROM fa_productos p WITH (NOLOCK) ");
            queryBuilder.append("JOIN fa_stock_almacenes s WITH (NOLOCK) ON p.codpro = s.codpro ");
            queryBuilder.append("LEFT JOIN fa_tipos t ON p.codtip = t.codtip ");

            queryBuilder.append("LEFT JOIN fa_stock_almacenes x WITH (NOLOCK) ON x.codpro = s.codpro ");
            if (solorojos.equals("S") && distpor.equals("REPOS")) {
                queryBuilder.append("INNER JOIN ( ");
                queryBuilder.append("SELECT x1.codpro ");
                queryBuilder.append("FROM fa_stock_almacenes x1 ");
                queryBuilder.append("inner JOIN fa_productos pro ON pro.codpro=x1.codpro ");
                queryBuilder.append("left JOIN parametros_bartolito param ON param.codparam='dipro' ");
                queryBuilder.append("LEFT JOIN view_productos_transito trans ");
                queryBuilder.append("ON x1.codpro = trans.codpro ");
                queryBuilder.append("AND x1.codalm = trans.codalm ");
                queryBuilder.append("LEFT JOIN ( ");
                queryBuilder.append("SELECT ");
                queryBuilder.append("vd.codpro,vd.codalm, ");
                queryBuilder.append("SUM(vd.qtypro + (CAST(vd.qtypro_m AS DECIMAL(8, 2)) / vd.stkfra)) AS suma ");
                queryBuilder.append("from fa_ventas_detalle vd ");
                if (soloalm.equals("N")) {
                    queryBuilder.append(
                            "INNER JOIN fa_almacenes central1 WITH (NOLOCK) ON central1.central='N' and vd.codalm=central1.codalm ");
                }
                queryBuilder.append("JOIN fa_ventas_cabecera vc ON vd.invnum = vc.invnum ");
                queryBuilder.append("WHERE ");
                queryBuilder.append("vc.invfec >= DATEADD(DAY, ?, GETDATE()) ");
                if (soloalm.equals("S")) {
                    queryBuilder.append("AND ? LIKE CONCAT('%', vd.codalm, '%') ");
                }
                queryBuilder.append("GROUP BY ");
                queryBuilder.append("vd.codpro,vd.codalm ");
                queryBuilder.append(") ventas ON x1.codpro = ventas.codpro and ventas.codalm = x1.codalm ");
                if (soloalm.equals("N")) {
                    queryBuilder.append(
                            "INNER JOIN fa_almacenes central WITH (NOLOCK) ON central.central='N' and x1.codalm=central.codalm ");
                }

                queryBuilder.append(
                        "WHERE ((CAST((x1.stkalm + ISNULL(trans.total_qtypro, 0)) AS decimal(18, 2)) * CAST(pro.stkfra AS decimal(18, 2)) ");
                queryBuilder.append(
                        "+ CAST((x1.stkalm_m + ISNULL(trans.total_qtypro, 0)) AS decimal(18, 2))) / CAST(pro.stkfra AS decimal(18, 2))) ");
                queryBuilder.append("< ventas.suma*? ");
                if (soloalm.equals("S")) {
                    queryBuilder.append("AND ? LIKE CONCAT('%', x1.codalm, '%') ");
                }
                queryBuilder.append(") cs ");
                queryBuilder.append("ON p.codpro = cs.codpro ");

            } else if (solorojos.equals("N") && distpor.equals("REPOS")) {

                queryBuilder.append("LEFT JOIN ( ");
                queryBuilder.append("SELECT x1.codpro ");
                queryBuilder.append("FROM fa_stock_almacenes x1 ");
                queryBuilder.append("inner JOIN fa_productos pro ON pro.codpro=x1.codpro ");
                queryBuilder.append("left JOIN parametros_bartolito param ON param.codparam='dipro' ");
                queryBuilder.append("LEFT JOIN view_productos_transito trans ");
                queryBuilder.append("ON x1.codpro = trans.codpro ");
                queryBuilder.append("AND x1.codalm = trans.codalm ");
                queryBuilder.append("LEFT JOIN ( ");
                queryBuilder.append("SELECT ");
                queryBuilder.append("vd.codpro,vd.codalm, ");
                queryBuilder.append("SUM(vd.qtypro + (CAST(vd.qtypro_m AS DECIMAL(8, 2)) / vd.stkfra)) AS suma ");
                queryBuilder.append("from fa_ventas_detalle vd ");
                queryBuilder.append("JOIN fa_ventas_cabecera vc ON vd.invnum = vc.invnum ");
                if (soloalm.equals("N")) {
                    queryBuilder.append(
                            "INNER JOIN fa_almacenes central1 WITH (NOLOCK) ON central1.central='N' and vd.codalm=central1.codalm ");
                }

                queryBuilder.append("WHERE ");
                queryBuilder.append("vc.invfec >= DATEADD(DAY, ?, GETDATE()) ");
                if (soloalm.equals("S")) {
                    queryBuilder.append("AND ? LIKE CONCAT('%', vd.codalm, '%') ");
                }
                queryBuilder.append("GROUP BY ");
                queryBuilder.append("vd.codpro,vd.codalm ");
                queryBuilder.append(") ventas ON x1.codpro = ventas.codpro and ventas.codalm = x1.codalm ");
                if (soloalm.equals("N")) {
                    queryBuilder.append(
                            "INNER JOIN fa_almacenes central WITH (NOLOCK) ON central.central='N' and x1.codalm=central.codalm ");
                }

                queryBuilder.append(
                        "WHERE ((CAST((x1.stkalm + ISNULL(trans.total_qtypro, 0)) AS decimal(18, 2)) * CAST(pro.stkfra AS decimal(18, 2)) ");
                queryBuilder.append(
                        "+ CAST((x1.stkalm_m + ISNULL(trans.total_qtypro, 0)) AS decimal(18, 2))) / CAST(pro.stkfra AS decimal(18, 2))) ");
                queryBuilder.append("< ventas.suma*? ");
                if (soloalm.equals("S")) {
                    queryBuilder.append("AND ? LIKE CONCAT('%', x1.codalm, '%') ");
                }
                queryBuilder.append(") cs ");
                queryBuilder.append("ON p.codpro = cs.codpro ");

            } else if (solorojos.equals("N") && distpor.equals("STKMIN")) {
                queryBuilder.append("LEFT JOIN ( ");
                queryBuilder.append("SELECT x1.codpro ");
                queryBuilder.append("FROM fa_stock_almacenes x1 ");
                queryBuilder.append("inner JOIN fa_productos pro ON pro.codpro=x1.codpro ");
                queryBuilder.append("left JOIN parametros_bartolito param ON param.codparam='dipro' ");
                queryBuilder.append("LEFT JOIN view_productos_transito trans ");
                queryBuilder.append("ON x1.codpro = trans.codpro ");
                queryBuilder.append("AND x1.codalm = trans.codalm ");

                if (soloalm.equals("N")) {
                    queryBuilder.append(
                            "INNER JOIN fa_almacenes central WITH (NOLOCK) ON central.central='N' and x1.codalm=central.codalm ");
                }

                queryBuilder.append("WHERE x1.stkmin2 <>0 ");
                queryBuilder.append("AND ((CAST((((x1.stkalm + ISNULL(trans.total_qtypro, 0)) * pro.stkfra) ");
                queryBuilder.append(
                        "+ x1.stkalm_m + ISNULL(trans.total_qtypro_m, 0)) AS decimal(18, 2))) / CAST(pro.stkfra AS decimal(18, 2))) ");
                queryBuilder
                        .append("/ (CAST(x1.stkmin2 AS decimal(18, 2)) / param.valor * CAST(? AS decimal(18, 2)))< ? ");
                if (soloalm.equals("S")) {
                    queryBuilder.append("AND ? LIKE CONCAT('%', x1.codalm, '%') ");
                }
                queryBuilder.append(") cs ");
                queryBuilder.append("ON p.codpro = cs.codpro ");
            } else if (solorojos.equals("S") && distpor.equals("STKMIN")) {
                queryBuilder.append("INNER JOIN ( ");
                queryBuilder.append("SELECT x1.codpro ");
                queryBuilder.append("FROM fa_stock_almacenes x1 ");
                queryBuilder.append("inner JOIN fa_productos pro ON pro.codpro=x1.codpro ");
                queryBuilder.append("left JOIN parametros_bartolito param ON param.codparam='dipro' ");
                queryBuilder.append("LEFT JOIN view_productos_transito trans ");
                queryBuilder.append("ON x1.codpro = trans.codpro ");
                queryBuilder.append("AND x1.codalm = trans.codalm ");
                if (soloalm.equals("N")) {
                    queryBuilder.append(
                            "INNER JOIN fa_almacenes central WITH (NOLOCK) ON central.central='N' and x1.codalm=central.codalm ");
                }
                queryBuilder.append("WHERE x1.stkmin2 <>0 ");
                queryBuilder.append("AND ((CAST((((x1.stkalm + ISNULL(trans.total_qtypro, 0)) * pro.stkfra) ");
                queryBuilder.append(
                        "+ x1.stkalm_m + ISNULL(trans.total_qtypro_m, 0)) AS decimal(18, 2))) / CAST(pro.stkfra AS decimal(18, 2))) ");
                queryBuilder.append(
                        "/ ((CAST(x1.stkmin2 AS decimal(18, 2)) / param.valor) * CAST(? AS decimal(18, 2)))< ? ");
                if (soloalm.equals("S")) {
                    queryBuilder.append("AND ? LIKE CONCAT('%', x1.codalm, '%') ");
                }
                queryBuilder.append(") cs ");
                queryBuilder.append("ON p.codpro = cs.codpro ");
            }
            if (solo0.equals("S") && distpor.equals("REPOS")) {
                queryBuilder.append("left JOIN ( ");
                queryBuilder.append("SELECT ");
                queryBuilder.append("DISTINCT vd.codpro ");
                queryBuilder.append("from fa_ventas_detalle vd ");
                queryBuilder.append("JOIN fa_ventas_cabecera vc ON vd.invnum = vc.invnum ");
                if (soloalm.equals("N")) {
                    queryBuilder.append(
                            "INNER JOIN fa_almacenes central WITH (NOLOCK) ON central.central='N' and vd.codalm=central.codalm ");
                }
                queryBuilder.append("WHERE ");
                queryBuilder.append("vc.invfec >= DATEADD(DAY, ?, GETDATE()) ");
                if (soloalm.equals("S")) {
                    queryBuilder.append("AND ? LIKE CONCAT('%', vd.codalm, '%') ");
                }
                queryBuilder.append("GROUP BY ");
                queryBuilder.append("vd.codpro ");
                queryBuilder.append(") ventas2 ON p.codpro = ventas2.codpro ");
            }
            if (solo0.equals("S") && distpor.equals("STKMIN")) {
                queryBuilder.append("left JOIN ( ");
                queryBuilder.append("SELECT ");
                queryBuilder.append("x2.codpro ");
                queryBuilder.append("from fa_stock_almacenes x2 ");
                if (soloalm.equals("N")) {
                    queryBuilder.append(
                            "INNER JOIN fa_almacenes central3 WITH (NOLOCK) ON central3.central='N' and x2.codalm=central3.codalm ");
                }
                queryBuilder.append("WHERE 1=1 ");
                if (soloalm.equals("S")) {
                    queryBuilder.append(" ? LIKE CONCAT('%', x2.codalm, '%') ");
                }
                queryBuilder.append("AND x2.stkmin2>0 ");

                queryBuilder.append("GROUP BY ");
                queryBuilder.append("x2.codpro ");
                queryBuilder.append(") stkmin0 ON p.codpro = stkmin0.codpro ");
            }

            if (soloalm.equals("N")) {
                queryBuilder.append(
                        "INNER JOIN fa_almacenes cent WITH (NOLOCK) ON cent.central='N' and x.codalm=cent.codalm ");
            }
            queryBuilder.append("WHERE s.codalm = ? AND (s.stkalm + s.stkalm_m) > 0 ");

            if (soloalm.equals("S")) {
                queryBuilder.append("AND ? LIKE CONCAT('%',x.codalm,'%') ");
            }
            if (solo0.equals("S") && distpor.equals("STKMIN")) {
                queryBuilder.append("AND stkmin0.codpro is null ");
            }
            if (solo0.equals("S") && distpor.equals("REPOS")) {
                queryBuilder.append("AND ventas2.codpro is null ");
            }
            if (tipoDistrib.equals("TIPO")) {
                queryBuilder.append("AND ? LIKE CONCAT('%', p.codtip, '%') ");
            } else if (tipoDistrib.equals("SECUENCIA")) {
                queryBuilder.append("AND p.codpro IN ( ");
                queryBuilder.append("SELECT d.codpro ");
                queryBuilder.append("FROM fa_movimientos c WITH (NOLOCK) ");
                queryBuilder.append("INNER JOIN fa_movimientos_detalle d WITH (NOLOCK) ON c.invnum = d.invnum ");
                queryBuilder.append(
                        "INNER JOIN fa_almacenes central WITH (NOLOCK) ON central.central='S' and c.codalm=central.codalm ");
                queryBuilder.append("WHERE c.invnum = ? ");
                queryBuilder.append("AND c.tipkar IN ('CC', 'CQ', 'C+', 'CE') ");
                queryBuilder.append("AND c.movsta <> 'A' ");
                queryBuilder.append(") ");
            }

            if (indicaFecha.equals("S")) {
                queryBuilder.append("AND p.codpro NOT IN ( ");
                queryBuilder.append("SELECT d.codpro ");
                queryBuilder.append("FROM fa_movimientos c WITH (NOLOCK) ");
                queryBuilder.append("JOIN fa_movimientos_detalle d WITH (NOLOCK) ");
                queryBuilder.append("ON c.invnum = d.invnum ");
                queryBuilder.append(
                        "INNER JOIN fa_almacenes central WITH (NOLOCK) ON central.central='S' and c.codalm=central.codalm ");
                queryBuilder.append("WHERE c.feccre > ? ");
                queryBuilder.append("AND c.tipkar IN ('CC', 'CQ', 'C+', 'CE') ");
                queryBuilder.append("AND c.movsta <> 'A' ");
                queryBuilder.append(") ");
            }
            queryBuilder.append(
                    " and p.estado = 'S' GROUP BY p.codpro, p.despro, p.codlab, p.stkfra, s.stkalm, s.stkalm_m, s.codalm, p.codtip, t.destip ");

            queryBuilder.append("ORDER BY p.despro;");

            Query query = em.createNativeQuery(queryBuilder.toString());

            int position = 1; // Posición inicial para parámetros

            if (solorojos.equals("S") && distpor.equals("REPOS")) {
                query.setParameter(position++, tipoStkMin * -1);
                if (soloalm.equals("S")) {
                    query.setParameter(position++, almparam);
                }
                query.setParameter(position++, multiplicador);
                if (soloalm.equals("S")) {
                    query.setParameter(position++, almparam);
                }
            } else if (solorojos.equals("N") && distpor.equals("REPOS")) {
                query.setParameter(position++, tipoStkMin * -1);
                if (soloalm.equals("S")) {
                    query.setParameter(position++, almparam);
                }
                query.setParameter(position++, multiplicador);
                if (soloalm.equals("S")) {
                    query.setParameter(position++, almparam);
                }
            } else if (distpor.equals("STKMIN")) {
                if (solorojos.equals("N")) {
                    query.setParameter(position++, tipoStkMin);
                    query.setParameter(position++, multiplicador);
                    if (soloalm.equals("S")) {
                        query.setParameter(position++, almparam);
                    }
                } else if (solorojos.equals("S") && distpor.equals("STKMIN")) {
                    query.setParameter(position++, tipoStkMin);
                    query.setParameter(position++, multiplicador);
                    if (soloalm.equals("S")) {
                        query.setParameter(position++, almparam);
                    }
                }
            }
            if (solo0.equals("S") && distpor.equals("REPOS")) {
                query.setParameter(position++, tipoStkMin * -1);
                if (soloalm.equals("S")) {
                    query.setParameter(position++, almparam);
                }
            }
            if (solo0.equals("S") && distpor.equals("STKMIN")) {
                if (soloalm.equals("S")) {
                    query.setParameter(position++, almparam);
                }
            }
            query.setParameter(position++, codalminv);
            if (soloalm.equals("S")) {
                query.setParameter(position++, almparam);
            }
            if (tipoDistrib.equals("TIPO")) {
                query.setParameter(position++, tiposParam);
            } else if (tipoDistrib.equals("SECUENCIA")) {
                query.setParameter(position++, secuencia);
            }
            if ("S".equals(indicaFecha)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM");
                query.setParameter(position++, dateFormat.format(fecha1));
            }

            // Ejecutar el procedimiento almacenado
            List<Object[]> results = query.getResultList();
            List<DistribuirAlmacenCentral> datosList = new ArrayList<>();
            for (Object[] res : results) {
                DistribuirAlmacenCentral datos = new DistribuirAlmacenCentral(
                        (String) res[0], // codpro
                        (String) res[1], // despro
                        (String) res[2], // codlab
                        (int) res[3], // stkfra
                        (int) res[4], // stkalm
                        (int) res[5], // stkalm_m
                        (String) res[6], // codalm
                        // (BigDecimal) res[7], // dif_stock_min
                        (int) res[7], // stock_estab
                        // s(BigDecimal) res[9], // stock_min_estab
                        (String) res[8], // codtip
                        (String) res[9], // destip
                        (String) res[10]);

                // Agregar el objeto DistribuirAlmacenCentral a la lista
                datosList.add(datos);

            }
            Gson gson = new Gson();
            String json = gson.toJson(datosList);

            return "{\"data\":" + json + "}";
        } catch (Exception e) {
            return "{\"resultado\":\"error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarporgenerico(int tipoStkMin, String tipoDistrib, List<String> codTip, int secuencia,
            String indicaFecha, Date fecha1, String solorojos, String soloalm, List<String> codalm, String distpor,
            String solo0, BigDecimal multiplicador, String codalminv, String generico) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String tiposParam = String.join(",", codTip);
            String almparam = String.join(",", codalm);

            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT p.codgen,g.desgen, ");
            queryBuilder.append(
                    "ISNULL(SUM(s.stkalm+ISNULL(trans.total_qtypro, 0) +(CAST(s.stkalm_m AS DECIMAL(18, 2)) + CAST(ISNULL(trans.total_qtypro_m, 0) AS DECIMAL(18, 2)))/ p.stkfra), 0) AS stock, ");
            if (distpor.equals("REPOS")) {
                queryBuilder.append(
                        "ISNULL(SUM(vd.qtypro + CAST(vd.qtypro_m / p.stkfra AS DECIMAL(10, 2))), 0) AS ventas, ");
                queryBuilder.append(
                        "CASE WHEN ISNULL(SUM(vd.qtypro + CAST(vd.qtypro_m / p.stkfra AS DECIMAL(10, 2))), 0) = 0 THEN NULL ELSE ");
                queryBuilder.append("ISNULL(SUM(s.stkalm + CAST(s.stkalm_m / p.stkfra AS DECIMAL(10, 2))), 0) / ");
                queryBuilder.append(
                        "ISNULL(SUM(vd.qtypro + CAST(vd.qtypro_m / p.stkfra AS DECIMAL(10, 2))), 0) END AS cobertura ");
            } else {
                queryBuilder.append("isnull(sum(cast((s.stkmin2 / param.valor) AS DECIMAL(10, 2)) * ?),0) AS ventas, ");
                queryBuilder.append(
                        "CASE WHEN isnull(sum(cast((s.stkmin2 / param.valor) AS DECIMAL(10, 2)) * ?),0) = 0 THEN NULL ELSE ");
                queryBuilder.append(
                        "ISNULL(SUM(s.stkalm+ISNULL(trans.total_qtypro, 0) + (CAST(s.stkalm_m AS DECIMAL(18, 2)) + CAST(ISNULL(trans.total_qtypro_m, 0) AS DECIMAL(18, 2)))/ p.stkfra), 0) / ");
                queryBuilder.append("sum(cast((s.stkmin2 / param.valor) AS DECIMAL(10, 2)) * ?) END AS cobertura ");
            }
            queryBuilder.append("FROM fa_productos p ");
            queryBuilder.append("INNER JOIN fa_genericos g ON g.codgen = p.codgen AND g.estado = 'S' ");
            queryBuilder.append("inner JOIN fa_stock_almacenes s ON s.codpro = p.codpro ");
            queryBuilder.append("inner JOIN fa_stock_almacenes c ON c.codpro = p.codpro and c.codalm=? ");
            queryBuilder.append("LEFT JOIN view_productos_transito trans ");
            queryBuilder.append("ON p.codpro = trans.codpro ");
            queryBuilder.append("AND s.codalm = trans.codalm ");

            queryBuilder.append("left JOIN parametros_bartolito param ON param.codparam='dipro' ");
            if (distpor.equals("REPOS")) {
                queryBuilder.append("LEFT JOIN fa_ventas_detalle vd ON vd.codpro = p.codpro AND s.codalm = vd.codalm ");
                queryBuilder
                        .append("LEFT JOIN fa_ventas_cabecera vc ON s.codalm = vc.codalm AND vd.invnum = vc.invnum ");
            }
            queryBuilder.append("inner JOIN fa_almacenes a ON a.codalm = s.codalm ");
            if (soloalm.equals("N")) {
                queryBuilder.append("and a.central='N' ");
            }
            if (distpor.equals("REPOS")) {
                queryBuilder.append("WHERE vc.invfec >= DATEADD(DAY, ?, GETDATE()) and p.estado='S' ");
            } else {
                queryBuilder.append("WHERE p.estado='S' ");
            }
            queryBuilder.append("and (c.stkalm + c.stkalm_m) > 0 ");
            if (generico.equals("gen")) {
                queryBuilder.append("and p.categvta<>'E' and p.codtip='C' ");
            } else {
                queryBuilder.append("and p.categvta='E' and p.codtip='A' ");
            }
            if (soloalm.equals("S")) {
                queryBuilder.append("and ? LIKE CONCAT('%', s.codalm, '%') ");
            }
            queryBuilder.append("GROUP BY ");
            queryBuilder.append("p.codgen,g.desgen ");
            if (solorojos.equals("S") && distpor.equals("REPOS")) {
                queryBuilder
                        .append("HAVING ISNULL(SUM(s.stkalm + CAST(s.stkalm_m / p.stkfra AS DECIMAL(10, 2))), 0) < ");
                queryBuilder.append("ISNULL(SUM(vd.qtypro + CAST(vd.qtypro_m / p.stkfra AS DECIMAL(10, 2))), 0)");
            } else if (solorojos.equals("S") && distpor.equals("STKMIN")) {
                queryBuilder.append(
                        "HAVING ISNULL(SUM(s.stkalm+ ISNULL(trans.total_qtypro, 0) + (CAST(s.stkalm_m AS DECIMAL(18, 2)) + CAST(ISNULL(trans.total_qtypro_m, 0) AS DECIMAL(18, 2)))/ p.stkfra), 0) < ");
                queryBuilder.append("isnull(sum(cast((s.stkmin2 / param.valor) AS DECIMAL(10, 2)) * ?),0)");
            }

            Query query = em.createNativeQuery(queryBuilder.toString());

            int position = 1; // Posición inicial para parámetros
            if (distpor.equals("STKMIN")) {
                query.setParameter(position++, tipoStkMin);
                query.setParameter(position++, tipoStkMin);
                query.setParameter(position++, tipoStkMin);
            }

            query.setParameter(position++, codalminv);
            if (distpor.equals("REPOS")) {
                query.setParameter(position++, tipoStkMin * -1);
            }
            if (soloalm.equals("S")) {
                query.setParameter(position++, almparam);
            }
            if (solorojos.equals("S") && distpor.equals("STKMIN")) {
                query.setParameter(position++, tipoStkMin);

            }
            // Ejecutar el procedimiento almacenado
            List<Object[]> results = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : results) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codgen", fila[0]);
                jsonObj.put("desgen", fila[1]);
                jsonObj.put("stock", fila[2]);
                jsonObj.put("suma", fila[3]);
                jsonObj.put("cobertura", fila[4]);
                jsonArray.put(jsonObj);
            }
            return "{\"data\":" + jsonArray.toString() + "}";
        } catch (Exception e) {
            return "{\"resultado\":\"error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listargenericoproductos2(int tipoStkMin, String tipoDistrib, List<String> codTip, int secuencia,
            String indicaFecha, Date fecha1, String solorojos, String soloalm, List<String> codalm, String distpor,
            String solo0, BigDecimal multiplicador, String codalminv, String generico, String objgenerico) {
        EntityManager em = null;
        try {
            em = getEntityManager();

            String tiposParam = String.join(",", codTip);
            String almparam = String.join(",", codalm);

            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT p.codgen,g.desgen, ");
            queryBuilder.append(
                    "ISNULL(SUM(s.stkalm+ISNULL(trans.total_qtypro, 0) +(CAST(s.stkalm_m AS DECIMAL(18, 2)) + CAST(ISNULL(trans.total_qtypro_m, 0) AS DECIMAL(18, 2)))/ p.stkfra), 0) AS stock, ");
            queryBuilder.append("isnull(sum(cast((s.stkmin2 / param.valor) AS DECIMAL(10, 2)) * ?),0) AS ventas, ");
            queryBuilder.append(
                    "CASE WHEN isnull(sum(cast((s.stkmin2 / param.valor) AS DECIMAL(10, 2)) * ?),0) = 0 THEN NULL ELSE ");
            queryBuilder.append(
                    "ISNULL(SUM(s.stkalm+ISNULL(trans.total_qtypro, 0) + (CAST(s.stkalm_m AS DECIMAL(18, 2)) + CAST(ISNULL(trans.total_qtypro_m, 0) AS DECIMAL(18, 2)))/ p.stkfra), 0) / ");
            queryBuilder.append("sum(cast((s.stkmin2 / param.valor) AS DECIMAL(10, 2)) * ?) END AS cobertura ");
            queryBuilder.append(", s.codalm,s.desalm ");

            queryBuilder.append("FROM fa_productos p ");
            queryBuilder.append("INNER JOIN fa_genericos g ON g.codgen = p.codgen AND g.estado = 'S' ");
            queryBuilder.append("inner JOIN fa_stock_almacenes s ON s.codpro = p.codpro ");
            queryBuilder.append("inner JOIN fa_stock_almacenes c ON c.codpro = p.codpro and c.codalm=? ");
            queryBuilder.append("LEFT JOIN view_productos_transito trans ");
            queryBuilder.append("ON p.codpro = trans.codpro ");
            queryBuilder.append("AND s.codalm = trans.codalm ");

            queryBuilder.append("left JOIN parametros_bartolito param ON param.codparam='dipro' ");
            queryBuilder.append("inner JOIN fa_almacenes a ON a.codalm = s.codalm ");
            if (soloalm.equals("N")) {
                queryBuilder.append("and a.central='N' ");
            }
            queryBuilder.append("WHERE p.estado='S' ");
            queryBuilder.append("and (c.stkalm + c.stkalm_m) > 0 and  p.codgen=? ");
            if (generico.equals("gen")) {
                queryBuilder.append("and p.categvta<>'E' and p.codtip='C' ");
            } else {
                queryBuilder.append("and p.categvta='E' and p.codtip='A' ");
            }
            if (soloalm.equals("S")) {
                queryBuilder.append("and ? LIKE CONCAT('%', s.codalm, '%') ");
            }
            queryBuilder.append("GROUP BY ");
            queryBuilder.append("p.codgen,g.desgen, s.codalm,s.desalm ");
            if (solorojos.equals("S") && distpor.equals("REPOS")) {
                queryBuilder
                        .append("HAVING ISNULL(SUM(s.stkalm + CAST(s.stkalm_m / p.stkfra AS DECIMAL(10, 2))), 0) < ");
                queryBuilder.append("ISNULL(SUM(vd.qtypro + CAST(vd.qtypro_m / p.stkfra AS DECIMAL(10, 2))), 0)");
            } else if (solorojos.equals("S") && distpor.equals("STKMIN")) {
                queryBuilder.append(
                        "HAVING ISNULL(SUM(s.stkalm+ ISNULL(trans.total_qtypro, 0) + (CAST(s.stkalm_m AS DECIMAL(18, 2)) + CAST(ISNULL(trans.total_qtypro_m, 0) AS DECIMAL(18, 2)))/ p.stkfra), 0) < ");
                queryBuilder.append("isnull(sum(cast((s.stkmin2 / param.valor) AS DECIMAL(10, 2)) * ?),0)");
            }

            Query query = em.createNativeQuery(queryBuilder.toString());

            int position = 1; // Posición inicial para parámetros
            query.setParameter(position++, tipoStkMin);
            query.setParameter(position++, tipoStkMin);
            query.setParameter(position++, tipoStkMin);

            query.setParameter(position++, codalminv);
            query.setParameter(position++, objgenerico);
            if (soloalm.equals("S")) {
                query.setParameter(position++, almparam);
            }
            if (solorojos.equals("S") && distpor.equals("STKMIN")) {
                query.setParameter(position++, tipoStkMin);

            }
            // Ejecutar el procedimiento almacenado
            List<Object[]> results = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : results) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codgen", fila[0]);
                jsonObj.put("desgen", fila[1]);
                jsonObj.put("stock", fila[2]);
                jsonObj.put("suma", fila[3]);
                jsonObj.put("cobertura", fila[4]);
                jsonObj.put("codalm", fila[5]);
                jsonObj.put("desalm", fila[6]);
                jsonArray.put(jsonObj);
            }
            return "{\"data\":" + jsonArray.toString() + "}";
        } catch (Exception e) {
            return "{\"resultado\":\"error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listargenericoproductos3(int tipoStkMin, String tipoDistrib, List<String> codTip, int secuencia,
            String indicaFecha, Date fecha1, String solorojos, String soloalm, List<String> codalm, String distpor,
            String solo0, BigDecimal multiplicador, String codalminv, String generico, String objgenerico,
            String codalm2) {
        EntityManager em = null;
        try {
            em = getEntityManager();

            String tiposParam = String.join(",", codTip);
            String almparam = String.join(",", codalm);

            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT p.codpro,p.despro, ");
            queryBuilder.append(
                    "ISNULL(SUM(s.stkalm+ISNULL(trans.total_qtypro, 0) +(CAST(s.stkalm_m AS DECIMAL(18, 2)) + CAST(ISNULL(trans.total_qtypro_m, 0) AS DECIMAL(18, 2)))/ p.stkfra), 0) AS stock, ");
            queryBuilder.append("isnull(sum(cast((s.stkmin2 / param.valor) AS DECIMAL(10, 2)) * ?),0) AS ventas, ");
            queryBuilder.append(
                    "CASE WHEN isnull(sum(cast((s.stkmin2 / param.valor) AS DECIMAL(10, 2)) * ?),0) = 0 THEN NULL ELSE ");
            queryBuilder.append(
                    "ISNULL(SUM(s.stkalm+ISNULL(trans.total_qtypro, 0) + (CAST(s.stkalm_m AS DECIMAL(18, 2)) + CAST(ISNULL(trans.total_qtypro_m, 0) AS DECIMAL(18, 2)))/ p.stkfra), 0) / ");
            queryBuilder.append("sum(cast((s.stkmin2 / param.valor) AS DECIMAL(10, 2)) * ?) END AS cobertura ");
            queryBuilder.append(", s.codalm,s.desalm ");

            queryBuilder.append("FROM fa_productos p ");
            queryBuilder.append("INNER JOIN fa_genericos g ON g.codgen = p.codgen AND g.estado = 'S' ");
            queryBuilder.append("inner JOIN fa_stock_almacenes s ON s.codpro = p.codpro ");
            queryBuilder.append("inner JOIN fa_stock_almacenes c ON c.codpro = p.codpro and c.codalm=? ");
            queryBuilder.append("LEFT JOIN view_productos_transito trans ");
            queryBuilder.append("ON p.codpro = trans.codpro ");
            queryBuilder.append("AND s.codalm = trans.codalm ");

            queryBuilder.append("left JOIN parametros_bartolito param ON param.codparam='dipro' ");
            queryBuilder.append("inner JOIN fa_almacenes a ON a.codalm = s.codalm ");
            if (soloalm.equals("N")) {
                queryBuilder.append("and a.central='N' ");
            }
            queryBuilder.append("WHERE p.estado='S' ");
            queryBuilder.append("and (c.stkalm + c.stkalm_m) > 0 ");
            if (generico.equals("gen")) {
                queryBuilder.append("and p.categvta<>'E' and p.codtip='C' ");
            } else {
                queryBuilder.append("and p.categvta='E' and p.codtip='A' ");
            }
            if (soloalm.equals("S")) {
                queryBuilder.append("and ? LIKE CONCAT('%', s.codalm, '%') ");
            }
            queryBuilder.append("GROUP BY ");
            queryBuilder.append("p.codgen,g.desgen, s.codalm,s.desalm ");
            if (solorojos.equals("S") && distpor.equals("REPOS")) {
                queryBuilder
                        .append("HAVING ISNULL(SUM(s.stkalm + CAST(s.stkalm_m / p.stkfra AS DECIMAL(10, 2))), 0) < ");
                queryBuilder.append("ISNULL(SUM(vd.qtypro + CAST(vd.qtypro_m / p.stkfra AS DECIMAL(10, 2))), 0)");
            } else if (solorojos.equals("S") && distpor.equals("STKMIN")) {
                queryBuilder.append(
                        "HAVING ISNULL(SUM(s.stkalm+ ISNULL(trans.total_qtypro, 0) + (CAST(s.stkalm_m AS DECIMAL(18, 2)) + CAST(ISNULL(trans.total_qtypro_m, 0) AS DECIMAL(18, 2)))/ p.stkfra), 0) < ");
                queryBuilder.append("isnull(sum(cast((s.stkmin2 / param.valor) AS DECIMAL(10, 2)) * ?),0)");
            }

            Query query = em.createNativeQuery(queryBuilder.toString());

            int position = 1; // Posición inicial para parámetros
            query.setParameter(position++, tipoStkMin);
            query.setParameter(position++, tipoStkMin);
            query.setParameter(position++, tipoStkMin);

            query.setParameter(position++, codalminv);
            if (soloalm.equals("S")) {
                query.setParameter(position++, almparam);
            }
            if (solorojos.equals("S") && distpor.equals("STKMIN")) {
                query.setParameter(position++, tipoStkMin);

            }
            // Ejecutar el procedimiento almacenado
            List<Object[]> results = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : results) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codgen", fila[0]);
                jsonObj.put("desgen", fila[1]);
                jsonObj.put("stock", fila[2]);
                jsonObj.put("suma", fila[3]);
                jsonObj.put("cobertura", fila[4]);
                jsonObj.put("codalm", fila[5]);
                jsonObj.put("desalm", fila[6]);
                jsonArray.put(jsonObj);
            }
            return "{\"data\":" + jsonArray.toString() + "}";
        } catch (Exception e) {
            return "{\"resultado\":\"error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listargenericoproductos(int tipoStkMin, String tipoDistrib, List<String> codTip, int secuencia,
            String indicaFecha, Date fecha1, String solorojos, String soloalm, List<String> codalm, String distpor,
            String solo0, BigDecimal multiplicador, String codalminv, String generico, String objgenerico) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String tiposParam = String.join(",", codTip);
            String almparam = String.join(",", codalm);

            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT p.codpro, p.despro, p.codlab, p.stkfra, s.stkalm, s.stkalm_m, s.codalm, ");
            queryBuilder.append("(SUM(x.stkalm) * p.stkfra + SUM(x.stkalm_m)) / p.stkfra as stock_estab, ");
            queryBuilder.append(
                    "p.codtip, t.destip,avg(cs.stock),avg(cs.suma),avg(cs.stock)/avg(cs.suma) as cobertura,min(v.fecven), ");
            if (distpor.equals("STKMIN")) {
                if (solorojos.equals("N")) {
                    queryBuilder.append("CASE ");
                    queryBuilder.append("WHEN COUNT(cs.codpro) > 0 ");
                    queryBuilder.append(" THEN 'S' ");
                    queryBuilder.append("ELSE 'N' ");
                    queryBuilder.append("END AS tienerojos ");
                } else {
                    queryBuilder.append("'S' AS tienerojos ");
                }
            } else {
                if (solorojos.equals("N")) {
                    queryBuilder.append("CASE ");
                    queryBuilder.append("WHEN COUNT(cs.codpro) > 0 ");
                    queryBuilder.append(" THEN 'S' ");
                    queryBuilder.append("ELSE 'N' ");
                    queryBuilder.append("END AS tienerojos ");
                } else {
                    queryBuilder.append("'S' AS tienerojos ");
                }
            }
            queryBuilder.append("FROM fa_productos p WITH (NOLOCK) ");
            queryBuilder.append("JOIN fa_stock_almacenes s WITH (NOLOCK) ON p.codpro = s.codpro ");
            queryBuilder.append("LEFT JOIN fa_tipos t ON p.codtip = t.codtip ");

            queryBuilder.append("LEFT JOIN fa_stock_almacenes x WITH (NOLOCK) ON x.codpro = s.codpro ");
            if (solorojos.equals("S") && distpor.equals("REPOS")) {
                queryBuilder.append("INNER JOIN ( ");
                queryBuilder.append(
                        "SELECT x1.codpro,sum((CAST((x1.stkalm + ISNULL(trans.total_qtypro, 0)) AS decimal(18, 2)) * CAST(pro.stkfra AS decimal(18, 2)) ");
                queryBuilder.append(
                        "+ CAST((x1.stkalm_m + ISNULL(trans.total_qtypro, 0)) AS decimal(18, 2))) / CAST(pro.stkfra AS decimal(18, 2))) as stock,ventas.suma ");
                queryBuilder.append("FROM fa_stock_almacenes x1 ");
                queryBuilder.append("inner JOIN fa_productos pro ON pro.codpro=x1.codpro ");
                queryBuilder.append("left JOIN parametros_bartolito param ON param.codparam='dipro' ");
                queryBuilder.append("LEFT JOIN view_productos_transito trans ");
                queryBuilder.append("ON x1.codpro = trans.codpro ");
                queryBuilder.append("AND x1.codalm = trans.codalm ");
                queryBuilder.append("INNER JOIN ( ");
                queryBuilder.append("SELECT ");
                queryBuilder.append("vd.codpro, ");
                queryBuilder.append("SUM(vd.qtypro + (CAST(vd.qtypro_m AS DECIMAL(8, 2)) / vd.stkfra)) AS suma ");
                queryBuilder.append("from fa_ventas_detalle vd ");
                if (soloalm.equals("N")) {
                    queryBuilder.append(
                            "INNER JOIN fa_almacenes central1 WITH (NOLOCK) ON central1.central='N' and vd.codalm=central1.codalm ");
                }
                queryBuilder.append("JOIN fa_ventas_cabecera vc ON vd.invnum = vc.invnum ");
                queryBuilder.append("WHERE ");
                queryBuilder.append("vc.invfec >= DATEADD(DAY, ?, GETDATE()) ");
                if (soloalm.equals("S")) {
                    queryBuilder.append("AND ? LIKE CONCAT('%', vd.codalm, '%') ");
                }
                queryBuilder.append("GROUP BY ");
                queryBuilder.append("vd.codpro ");
                queryBuilder.append(") ventas ON x1.codpro = ventas.codpro and ventas.suma>0 ");
                if (soloalm.equals("N")) {
                    queryBuilder.append(
                            "INNER JOIN fa_almacenes central WITH (NOLOCK) ON central.central='N' and x1.codalm=central.codalm ");
                }

                if (soloalm.equals("S")) {
                    queryBuilder.append("WHERE ? LIKE CONCAT('%', x1.codalm, '%') ");
                }
                queryBuilder.append("group by x1.codpro,ventas.suma) cs ");
                queryBuilder.append("ON p.codpro = cs.codpro ");

            } else if (solorojos.equals("N") && distpor.equals("REPOS")) {

                queryBuilder.append("LEFT JOIN ( ");
                queryBuilder.append(
                        "SELECT x1.codpro,sum((CAST((x1.stkalm + ISNULL(trans.total_qtypro, 0)) AS decimal(18, 2)) * CAST(pro.stkfra AS decimal(18, 2)) ");
                queryBuilder.append(
                        "+ CAST((x1.stkalm_m + ISNULL(trans.total_qtypro, 0)) AS decimal(18, 2))) / CAST(pro.stkfra AS decimal(18, 2))) as stock,ventas.suma ");
                queryBuilder.append("FROM fa_stock_almacenes x1 ");
                queryBuilder.append("inner JOIN fa_productos pro ON pro.codpro=x1.codpro ");
                queryBuilder.append("left JOIN parametros_bartolito param ON param.codparam='dipro' ");
                queryBuilder.append("LEFT JOIN view_productos_transito trans ");
                queryBuilder.append("ON x1.codpro = trans.codpro ");
                queryBuilder.append("AND x1.codalm = trans.codalm ");
                queryBuilder.append("INNER JOIN ( ");
                queryBuilder.append("SELECT ");
                queryBuilder.append("vd.codpro, ");
                queryBuilder.append("SUM(vd.qtypro + (CAST(vd.qtypro_m AS DECIMAL(8, 2)) / vd.stkfra)) AS suma ");
                queryBuilder.append("from fa_ventas_detalle vd ");
                if (soloalm.equals("N")) {
                    queryBuilder.append(
                            "INNER JOIN fa_almacenes central1 WITH (NOLOCK) ON central1.central='N' and vd.codalm=central1.codalm ");
                }
                queryBuilder.append("JOIN fa_ventas_cabecera vc ON vd.invnum = vc.invnum ");
                queryBuilder.append("WHERE ");
                queryBuilder.append("vc.invfec >= DATEADD(DAY, ?, GETDATE()) ");
                if (soloalm.equals("S")) {
                    queryBuilder.append("AND ? LIKE CONCAT('%', vd.codalm, '%') ");
                }
                queryBuilder.append("GROUP BY ");
                queryBuilder.append("vd.codpro ");
                queryBuilder.append(") ventas ON x1.codpro = ventas.codpro and ventas.suma>0 ");
                if (soloalm.equals("N")) {
                    queryBuilder.append(
                            "INNER JOIN fa_almacenes central WITH (NOLOCK) ON central.central='N' and x1.codalm=central.codalm ");
                }

                if (soloalm.equals("S")) {
                    queryBuilder.append("WHERE ? LIKE CONCAT('%', x1.codalm, '%') ");
                }
                queryBuilder.append("group by x1.codpro,ventas.suma) cs ");
                queryBuilder.append("ON p.codpro = cs.codpro ");

            } else if (solorojos.equals("N") && distpor.equals("STKMIN")) {
                queryBuilder.append("LEFT JOIN ( ");
                queryBuilder.append(
                        "SELECT x1.codpro,sum((CAST((((x1.stkalm + ISNULL(trans.total_qtypro, 0)) * pro.stkfra) ");
                queryBuilder.append(
                        "+ x1.stkalm_m + ISNULL(trans.total_qtypro_m, 0)) AS decimal(18, 2))) / CAST(pro.stkfra AS decimal(18, 2))) as stock, ");
                queryBuilder.append(
                        "sum(CAST(x1.stkmin2 AS decimal(18, 2)) / param.valor * CAST(? AS decimal(18, 2))) as suma ");
                queryBuilder.append("FROM fa_stock_almacenes x1 ");
                queryBuilder.append("inner JOIN fa_productos pro ON pro.codpro=x1.codpro ");
                queryBuilder.append("left JOIN parametros_bartolito param ON param.codparam='dipro' ");
                queryBuilder.append("LEFT JOIN view_productos_transito trans ");
                queryBuilder.append("ON x1.codpro = trans.codpro ");
                queryBuilder.append("AND x1.codalm = trans.codalm ");

                if (soloalm.equals("N")) {
                    queryBuilder.append(
                            "INNER JOIN fa_almacenes central WITH (NOLOCK) ON central.central='N' and x1.codalm=central.codalm ");
                }

                if (soloalm.equals("S")) {
                    queryBuilder.append("WHERE ? LIKE CONCAT('%', x1.codalm, '%') ");
                }
                queryBuilder.append("group by x1.codpro) cs ");
                queryBuilder.append("ON p.codpro = cs.codpro and cs.suma<>0 ");
            } else if (solorojos.equals("S") && distpor.equals("STKMIN")) {

                queryBuilder.append("LEFT JOIN ( ");
                queryBuilder.append(
                        "SELECT x1.codpro,sum((CAST((((x1.stkalm + ISNULL(trans.total_qtypro, 0)) * pro.stkfra) ");
                queryBuilder.append(
                        "+ x1.stkalm_m + ISNULL(trans.total_qtypro, 0)) AS decimal(18, 2))) / CAST(pro.stkfra AS decimal(18, 2))) as stock, ");
                queryBuilder.append(
                        "sum(CAST(x1.stkmin2 AS decimal(18, 2)) / param.valor * CAST(? AS decimal(18, 2))) as suma ");
                queryBuilder.append("FROM fa_stock_almacenes x1 ");
                queryBuilder.append("inner JOIN fa_productos pro ON pro.codpro=x1.codpro ");
                queryBuilder.append("left JOIN parametros_bartolito param ON param.codparam='dipro' ");
                queryBuilder.append("LEFT JOIN view_productos_transito trans ");
                queryBuilder.append("ON x1.codpro = trans.codpro ");
                queryBuilder.append("AND x1.codalm = trans.codalm ");

                if (soloalm.equals("N")) {
                    queryBuilder.append(
                            "INNER JOIN fa_almacenes central WITH (NOLOCK) ON central.central='N' and x1.codalm=central.codalm ");
                }

                if (soloalm.equals("S")) {
                    queryBuilder.append("WHERE ? LIKE CONCAT('%', x1.codalm, '%') ");
                }
                queryBuilder.append("group by x1.codpro) cs ");
                queryBuilder.append("ON p.codpro = cs.codpro and cs.suma<>0 ");
            }
            if (soloalm.equals("N")) {
                queryBuilder.append(
                        "INNER JOIN fa_almacenes cent WITH (NOLOCK) ON cent.central='N' and x.codalm=cent.codalm ");
            }

            queryBuilder.append(
                    "left join fa_stock_vencimientos v on v.codalm=? and p.codpro=v.codpro AND (v.qtymov + v.qtymov_m) > 0");

            queryBuilder.append("WHERE s.codalm = ? AND (s.stkalm + s.stkalm_m) > 0 ");

            if (soloalm.equals("S")) {
                queryBuilder.append("AND ? LIKE CONCAT('%',x.codalm,'%') ");
            }
            if (generico.equals("gen")) {
                queryBuilder.append("and p.categvta<>'E' and p.codtip='C' ");
            } else {
                queryBuilder.append("and p.categvta='E' ");
            }

            queryBuilder.append("and p.codgen=? ");
            queryBuilder.append(
                    " and p.estado = 'S' GROUP BY p.codpro, p.despro, p.codlab, p.stkfra, s.stkalm, s.stkalm_m, s.codalm, p.codtip, t.destip ");

            queryBuilder.append("ORDER BY p.despro;");

            Query query = em.createNativeQuery(queryBuilder.toString());

            int position = 1; // Posición inicial para parámetros

            if (solorojos.equals("S") && distpor.equals("REPOS")) {
                query.setParameter(position++, tipoStkMin * -1);
                if (soloalm.equals("S")) {
                    query.setParameter(position++, almparam);
                    query.setParameter(position++, almparam);
                }
            } else if (solorojos.equals("N") && distpor.equals("REPOS")) {
                query.setParameter(position++, tipoStkMin * -1);
                if (soloalm.equals("S")) {
                    query.setParameter(position++, almparam);
                    query.setParameter(position++, almparam);
                }
            } else if (distpor.equals("STKMIN")) {
                if (solorojos.equals("N")) {
                    query.setParameter(position++, tipoStkMin);
                    if (soloalm.equals("S")) {
                        query.setParameter(position++, almparam);
                    }
                } else if (solorojos.equals("S") && distpor.equals("STKMIN")) {
                    query.setParameter(position++, tipoStkMin);
                    if (soloalm.equals("S")) {
                        query.setParameter(position++, almparam);
                    }
                }
            }
            if (solo0.equals("S") && distpor.equals("REPOS")) {
                query.setParameter(position++, tipoStkMin * -1);
                if (soloalm.equals("S")) {
                    query.setParameter(position++, almparam);
                }
            }
            if (solo0.equals("S") && distpor.equals("STKMIN")) {
                if (soloalm.equals("S")) {
                    query.setParameter(position++, almparam);
                }
            }
            query.setParameter(position++, codalminv);
            query.setParameter(position++, codalminv);
            if (soloalm.equals("S")) {
                query.setParameter(position++, almparam);
            }
            if (tipoDistrib.equals("TIPO")) {
                query.setParameter(position++, tiposParam);
            } else if (tipoDistrib.equals("SECUENCIA")) {
                query.setParameter(position++, secuencia);
            }
            if ("S".equals(indicaFecha)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM");
                query.setParameter(position++, dateFormat.format(fecha1));
            }
            query.setParameter(position++, objgenerico);

            // Ejecutar el procedimiento almacenado
            List<Object[]> results = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : results) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codpro", fila[0]);
                jsonObj.put("despro", fila[1]);
                jsonObj.put("codlab", fila[2]);
                jsonObj.put("stkfra", fila[3]);
                jsonObj.put("stkalm", fila[4]);
                jsonObj.put("stkalm_m", fila[5]);
                jsonObj.put("codalm", fila[6]);
                jsonObj.put("stock_estab", fila[7]);
                jsonObj.put("codtip", fila[8]);
                jsonObj.put("destip", fila[9]);
                jsonObj.put("stock", fila[10]);
                jsonObj.put("suma", fila[11]);
                jsonObj.put("cobertura", fila[12]);
                jsonObj.put("vence", fila[13]);
                jsonObj.put("tienerojos", fila[14]);
                jsonArray.put(jsonObj);
            }
            return "{\"data\":" + jsonArray.toString() + "}";
        } catch (Exception e) {
            return "{\"resultado\":\"error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    /*
     * public String listar(int tipoStkMin, String tipoDistrib, List<String> codTip,
     * int secuencia, String indicaFecha, Date fecha1, String solorojos, String
     * soloalm, List<String> codalm, String distpor) {
     * EntityManager em = getEntityManager();
     * try {
     * String tiposParam = String.join(",", codTip);
     * String almparam = String.join(",", codalm);
     * 
     * StringBuilder queryBuilder = new StringBuilder();
     * queryBuilder.
     * append("SELECT p.codpro, p.despro, p.codlab, p.stkfra, s.stkalm, s.stkalm_m, s.codalm, "
     * );
     * queryBuilder.
     * append("(SUM(x.stkalm) * p.stkfra + SUM(x.stkalm_m)) / p.stkfra as stock_estab, "
     * );
     * queryBuilder.append("p.codtip, t.destip, ");
     * if (distpor.equals("STKMIN")) {
     * if (solorojos.equals("N")) {
     * queryBuilder.append("CASE ");
     * queryBuilder.append("WHEN EXISTS ( ");
     * queryBuilder.append("SELECT 1 ");
     * queryBuilder.append("FROM fa_stock_almacenes x1 ");
     * 
     * queryBuilder.append("LEFT JOIN view_productos_transito ");
     * queryBuilder.
     * append("as trans ON p.codpro = trans.codpro and trans.codalm=x1.codalm ");
     * 
     * queryBuilder.append("WHERE x1.codpro = p.codpro ");
     * if (soloalm.equals("S")) {
     * queryBuilder.append("AND ? LIKE CONCAT('%', x1.codalm, '%') ");
     * } else {
     * queryBuilder.append("AND x1.codalm NOT IN ('A1', 'A2') ");
     * }
     * queryBuilder.
     * append("AND ((CAST((x1.stkalm + ISNULL(trans.total_qtypro, 0)) AS decimal(18, 2)) * CAST(p.stkfra AS decimal(18, 2)) "
     * );
     * queryBuilder.
     * append("+ CAST((x1.stkalm_m + ISNULL(trans.total_qtypro, 0)) AS decimal(18, 2))) / CAST(p.stkfra AS decimal(18, 2))) "
     * );
     * queryBuilder.
     * append("< (CAST(x1.stkmin2 AS decimal(18, 2)) / 45.0 * CAST(? AS decimal(18, 2))) "
     * );
     * queryBuilder.append(") THEN 'S' ");
     * queryBuilder.append("ELSE 'N' ");
     * queryBuilder.append("END AS tienerojos ");
     * } else {
     * queryBuilder.append("'S' AS tienerojos ");
     * }
     * } else {
     * queryBuilder.append("'N' AS tienerojos ");
     * }
     * queryBuilder.append("FROM fa_productos p WITH (NOLOCK) ");
     * queryBuilder.
     * append("JOIN fa_stock_almacenes s WITH (NOLOCK) ON p.codpro = s.codpro ");
     * queryBuilder.append("LEFT JOIN fa_tipos t ON p.codtip = t.codtip ");
     * 
     * queryBuilder.append("LEFT JOIN fa_stock_almacenes x ON x.codpro = s.codpro "
     * );
     * if (soloalm.equals("S")) {
     * queryBuilder.append("AND ? LIKE CONCAT('%',x.codalm,'%') ");
     * } else {
     * queryBuilder.append("AND x.codalm NOT IN ('A1', 'A2') ");
     * }
     * queryBuilder.append("WHERE s.codalm = 'A1' AND (s.stkalm + s.stkalm_m) > 0 "
     * );
     * if (solorojos.equals("S") && distpor.equals("STKMIN")) {
     * queryBuilder.append("AND p.codpro IN ( ");
     * queryBuilder.append("SELECT x1.codpro ");
     * queryBuilder.append("FROM fa_stock_almacenes x1 ");
     * 
     * queryBuilder.append("LEFT JOIN view_productos_transito ");
     * queryBuilder.
     * append("as trans ON p.codpro = trans.codpro and trans.codalm=x1.codalm ");
     * 
     * queryBuilder.append("WHERE x1.codpro = p.codpro ");
     * queryBuilder.
     * append("AND ((CAST((x1.stkalm + ISNULL(trans.total_qtypro, 0)) AS decimal(18, 2)) * CAST(p.stkfra AS decimal(18, 2)) "
     * );
     * queryBuilder.
     * append("+ CAST((x1.stkalm_m + ISNULL(trans.total_qtypro, 0)) AS decimal(18, 2))) / CAST(p.stkfra AS decimal(18, 2))) "
     * );
     * queryBuilder.
     * append("< (CAST(x1.stkmin2 AS decimal(18, 2)) / 45.0 * CAST(? AS decimal(18, 2))) "
     * );
     * if (soloalm.equals("S")) {
     * queryBuilder.append("AND ? LIKE CONCAT('%', x1.codalm, '%') ");
     * } else {
     * queryBuilder.append("AND x1.codalm NOT IN ('A1', 'A2') ");
     * }
     * queryBuilder.append(") ");
     * }
     * 
     * if (tipoDistrib.equals("TIPO")) {
     * queryBuilder.append("AND ? LIKE CONCAT('%', p.codtip, '%') ");
     * } else if (tipoDistrib.equals("SECUENCIA")) {
     * queryBuilder.append("AND p.codpro IN ( ");
     * queryBuilder.append("SELECT d.codpro ");
     * queryBuilder.append("FROM fa_movimientos c WITH (NOLOCK) ");
     * queryBuilder.
     * append("INNER JOIN fa_movimientos_detalle d WITH (NOLOCK) ON c.invnum = d.invnum "
     * );
     * queryBuilder.append("WHERE c.invnum = ? ");
     * queryBuilder.append("AND c.codalm = 'A1' ");
     * queryBuilder.append("AND c.tipkar IN ('CC', 'CQ', 'C+', 'CE') ");
     * queryBuilder.append("AND c.movsta <> 'A' ");
     * queryBuilder.append(") ");
     * }
     * 
     * if (indicaFecha.equals("S")) {
     * queryBuilder.append("AND p.codpro NOT IN ( ");
     * queryBuilder.append("SELECT d.codpro ");
     * queryBuilder.append("FROM fa_movimientos c WITH (NOLOCK) ");
     * queryBuilder.append("JOIN fa_movimientos_detalle d WITH (NOLOCK) ");
     * queryBuilder.append("ON c.invnum = d.invnum ");
     * queryBuilder.append("WHERE c.feccre > ? ");
     * queryBuilder.append("AND c.codalm = 'A1' ");
     * queryBuilder.append("AND c.tipkar IN ('CC', 'CQ', 'C+', 'CE') ");
     * queryBuilder.append("AND c.movsta <> 'A' ");
     * queryBuilder.append(") ");
     * }
     * queryBuilder.
     * append("GROUP BY p.codpro, p.despro, p.codlab, p.stkfra, s.stkalm, s.stkalm_m, s.codalm, p.codtip, t.destip "
     * );
     * queryBuilder.append("ORDER BY p.despro;");
     * 
     * Query query = em.createNativeQuery(queryBuilder.toString());
     * 
     * int position = 1; // Posición inicial para parámetros
     * 
     * if (solorojos.equals("N") && distpor.equals("STKMIN")) {
     * if (soloalm.equals("S")) {
     * query.setParameter(position++, almparam);
     * }
     * query.setParameter(position++, tipoStkMin);
     * }
     * if (soloalm.equals("S")) {
     * query.setParameter(position++, almparam);
     * }
     * 
     * if (solorojos.equals("S") && distpor.equals("STKMIN")) {
     * query.setParameter(position++, tipoStkMin);
     * if (soloalm.equals("S")) {
     * query.setParameter(position++, almparam);
     * }
     * }
     * if (tipoDistrib.equals("TIPO")) {
     * query.setParameter(position++, tiposParam);
     * } else if (tipoDistrib.equals("SECUENCIA")) {
     * query.setParameter(position++, secuencia);
     * }
     * if ("S".equals(indicaFecha)) {
     * SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-dd-MM");
     * query.setParameter(position++, dateFormat.format(fecha1));
     * }
     * 
     * // Ejecutar el procedimiento almacenado
     * List<Object[]> results = query.getResultList();
     * List<DistribuirAlmacenCentral> datosList = new ArrayList<>();
     * for (Object[] res : results) {
     * DistribuirAlmacenCentral datos = new DistribuirAlmacenCentral(
     * (String) res[0], // codpro
     * (String) res[1], // despro
     * (String) res[2], // codlab
     * (int) res[3], // stkfra
     * (int) res[4], // stkalm
     * (int) res[5], // stkalm_m
     * (String) res[6], // codalm
     * //(BigDecimal) res[7], // dif_stock_min
     * (int) res[7], // stock_estab
     * //s(BigDecimal) res[9], // stock_min_estab
     * (String) res[8], // codtip
     * (String) res[9], // destip
     * (String) res[10]
     * );
     * 
     * // Agregar el objeto DistribuirAlmacenCentral a la lista
     * datosList.add(datos);
     * 
     * }
     * Gson gson = new Gson();
     * String json = gson.toJson(datosList);
     * 
     * return "{\"data\":" + json + "}";
     * } catch (Exception e) {
     * return "{\"resultado\":\"error\",\"mensaje\":\"" + e.getMessage() + "\"}";
     * } finally {
     * em.close();
     * }
     * }
     */
    public String distribucionEstablecimientos(int tipo_stkmin, String tipo_distrib, String codpro, String mostrarrojos,
            String soloalm, List<String> codalm, List<String> codTip, String distpor, BigDecimal multiplicador,
            String solo0, String codalminv) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String tiposParam = String.join(",", codTip);
            String almparam = String.join(",", codalm);
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT s.siscod AS siscod, s.sisent AS sisent, t.codalm AS codalm, ");
            queryBuilder.append("t.codpro AS codpro, ");
            queryBuilder.append("t.stkalm  AS stkalm, ");// + ISNULL(trans.total_qtypro, 0)
            queryBuilder.append("t.stkalm_m  AS stkalm_m, ");// + ISNULL(trans.total_qtypro_m, 0)
            queryBuilder.append("t.stkmin AS stkmin, p.stkfra AS stkfra, ");
            if (distpor.equals("REPOS")) {
                queryBuilder.append("ROUND(ISNULL(ventas.suma, 0)*?,2) AS stkmin2, ");
            } else {
                queryBuilder.append("ROUND((t.stkmin2 / param.valor * ? ), 2) AS stkmin2, ");
            }
            queryBuilder.append("p.coscom AS coscom, ");
            queryBuilder.append("p.igvpro AS igvpro, v1.codlot AS lote, v1.fecven AS fecven, ");
            queryBuilder.append(
                    "case when v1.qtymov - ISNULL(inm.cantE, 0)>0 then v1.qtymov - ISNULL(inm.cantE, 0) else 0 end AS qtymov, ");
            queryBuilder.append(
                    "case when v1.qtymov_m - ISNULL(inm.cantF, 0)>0 then v1.qtymov_m - ISNULL(inm.cantF, 0) else 0 end AS qtymov_m, ");
            queryBuilder.append("CASE WHEN EXISTS (SELECT 1 FROM restricciones_distribucion rd ");
            queryBuilder.append(
                    "WHERE rd.codpro = t.codpro AND rd.codalm = t.codalm) THEN 'S' ELSE 'N' END AS restringido,p.aplicfrac, ");
            queryBuilder.append("CASE WHEN inm.codpro is not null and inm.cantE is null and inm.cantF is null ");
            queryBuilder.append(
                    "THEN 'S' ELSE 'N' END AS inmovil,ISNULL(inm.cantE, 0),ISNULL(inm.cantF, 0),p.blister,p.aplicmastpack ,p.masterpack,ISNULL(trans.total_qtypro, 0),ISNULL(trans.total_qtypro_m, 0) ");
            queryBuilder.append("FROM sistema s ");
            queryBuilder.append(" JOIN fa_almacenes a ON s.siscod = a.siscod ");
            queryBuilder.append("left JOIN fa_stock_almacenes t ON a.codalm = t.codalm ");
            queryBuilder.append("JOIN fa_productos p ON t.codpro = p.codpro ");
            queryBuilder.append(
                    "left JOIN (select v.codalm, v.codpro, v.codlot, v.fecven, v.qtymov, v.qtymov_m, v.fecmov from fa_stock_vencimientos v where v.codalm = ?) v1 ON t.codpro = v1.codpro ");
            queryBuilder.append("JOIN parametros_bartolito param ON param.codparam='dipro' ");

            queryBuilder.append("LEFT JOIN ");
            /*
             * queryBuilder.append("SELECT ");
             * queryBuilder.append("dtr.codpro, ");
             * queryBuilder.append("trm.siscod_d, ");
             * queryBuilder.append("SUM(dtr.qtypro) AS total_qtypro, ");
             * queryBuilder.append("SUM(dtr.qtypro_m) AS total_qtypro_m ");
             * queryBuilder.append("FROM fa_movimientos AS trm WITH (NOLOCK) ");
             * queryBuilder.
             * append("LEFT JOIN fa_movimientos AS trx WITH (NOLOCK) ON trm.invnum = trx.invnum_ref "
             * );
             * queryBuilder.append("AND trx.siscod_d = 1 ");
             * queryBuilder.append("AND trx.tipkar = 'CE' ");
             * queryBuilder.append("AND trm.invnum < trx.invnum ");
             * queryBuilder.
             * append("INNER JOIN fa_movimientos_detalle AS dtr WITH (NOLOCK) ON dtr.invnum = trm.invnum "
             * );
             * queryBuilder.append("WHERE trm.tipkar = 'DE' ");
             * queryBuilder.append("AND trm.codalm = 'A1' ");
             * queryBuilder.append("AND trm.movsta = 'G' ");
             * queryBuilder.append("AND trm.feccre >= DATEADD(month, -2, GETDATE()) ");
             * queryBuilder.append("AND trx.invnum_ref IS NULL ");
             * queryBuilder.append("GROUP BY dtr.codpro, trm.siscod_d ");
             */
            queryBuilder.append(
                    "view_productos_transito as trans ON t.codpro = trans.codpro AND s.siscod = trans.siscod_d and t.codalm=trans.codalm ");

            queryBuilder.append(
                    "left join (select codpro,codlot,sum(cantE)as cantE,sum(cantF)as cantF from fa_inmovilizados where codalm=? group by codpro,codlot) inm on t.codpro=inm.codpro and v1.codlot=inm.codlot ");

            if (distpor.equals("REPOS")) {
                queryBuilder.append("LEFT JOIN ( ");
                queryBuilder.append("SELECT ");
                queryBuilder.append("vd.codpro,vd.codalm, ");
                queryBuilder.append("SUM(vd.qtypro + (CAST(vd.qtypro_m AS DECIMAL(8, 2)) / vd.stkfra)) AS suma ");
                queryBuilder.append("FROM ");
                queryBuilder.append("fa_ventas_detalle vd ");
                queryBuilder.append("JOIN fa_ventas_cabecera vc ON vd.invnum = vc.invnum ");
                queryBuilder.append("WHERE ");
                queryBuilder.append("vc.invfec >= DATEADD(DAY, ?, GETDATE()) ");
                queryBuilder.append("GROUP BY ");
                queryBuilder.append("vd.codpro,vd.codalm ");
                queryBuilder.append(") ventas ON t.codpro = ventas.codpro and ventas.codalm = t.codalm ");
            }

            if (soloalm.equals("S")) {
                queryBuilder.append("WHERE ? LIKE CONCAT('%', a.codalm, '%') ");
            } else {
                queryBuilder.append(
                        "INNER JOIN fa_almacenes central WITH (NOLOCK) ON central.central='N' and a.codalm=central.codalm ");
                queryBuilder.append("WHERE 1=1 ");
            }

            queryBuilder.append("AND p.estado = 'S' AND (v1.qtymov + v1.qtymov_m) > 0 AND t.codpro = ? ");
            /*
             * if (mostrarrojos.equals("S") && distpor.equals("REPOS")) {
             * queryBuilder.append("AND  ISNULL(ventas.suma, 0)>0 ");
             * }
             */
            /*
             * if ("TIPO".equals(tipo_distrib)) {
             * queryBuilder.
             * append("AND t.codpro IN (SELECT x.codpro FROM fa_stock_almacenes x ");
             * queryBuilder.
             * append("JOIN fa_productos z ON x.codpro = z.codpro WHERE x.codalm = ? ");
             * queryBuilder.
             * append("AND z.estado = 'S' AND ? LIKE CONCAT('%', p.codtip, '%') ");
             * queryBuilder.append("AND (x.stkalm + x.stkalm_m) > 0) ");
             * } else {
             * queryBuilder.
             * append("AND t.codpro IN (SELECT x.codpro FROM fa_stock_almacenes x ");
             * queryBuilder.
             * append("JOIN fa_productos z ON x.codpro = z.codpro WHERE x.codalm = ? ");
             * queryBuilder.append("AND z.estado = 'S' AND (x.stkalm + x.stkalm_m) > 0) ");
             * }
             */
            Query query = em.createNativeQuery(queryBuilder.toString());
            int position = 1;
            // query.setParameter(1, tipo_stkmin);
            if (distpor.equals("REPOS")) {
                query.setParameter(position++, multiplicador);
            } else {
                query.setParameter(position++, tipo_stkmin);
            } // Posición inicial para parámetros después de tipo_stkmin

            query.setParameter(position++, codalminv);
            query.setParameter(position++, codalminv);
            if (distpor.equals("REPOS")) {
                query.setParameter(position++, tipo_stkmin * -1);
            }
            if ("S".equals(soloalm)) {
                query.setParameter(position++, almparam);
            }

            query.setParameter(position++, codpro);
            /*
             * if ("TIPO".equals(tipo_distrib)) {
             * query.setParameter(position++, codalminv);
             * query.setParameter(position++, tiposParam);
             * } else {
             * query.setParameter(position++, codalminv);
             * }
             */
            // Ejecutar el procedimiento almacenado
            List<Object[]> results = query.getResultList();
            List<DistribuirAlmacenEstablecimientos> datosList = new ArrayList<>();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            if (mostrarrojos.equals("N")) {
                for (Object[] res : results) {
                    String fechaFormateada = sdf.format((Date) res[12]);
                    DistribuirAlmacenEstablecimientos datos = new DistribuirAlmacenEstablecimientos(
                            (int) res[0], // siscod
                            (String) res[1], // sisent
                            (String) res[2], // codalm
                            (String) res[3], // codpro
                            (int) res[4], // stkalm
                            (int) res[5], // stkalm_m
                            (int) res[6], // stkmin
                            (int) res[18], // cant_e Se reutilizo para enviar las cantidades inmovilizadas
                            (int) res[19], // cant_f Se reutilizo para enviar las cantidades inmovilizadas
                            (int) res[7], // stkfra
                            (BigDecimal) res[8], // stkmin2
                            (BigDecimal) res[9], // coscom
                            (BigDecimal) res[10], // igvpro
                            (String) res[11], // lote
                            fechaFormateada, // fecven
                            (int) res[13], // qtymov
                            (int) res[14], // qtymov_m
                            (String) res[15] // restringido
                    );
                    datos.setAplicfrac((String) res[16]);
                    datos.setInmov((String) res[17]);
                    datos.setBlister((Integer) res[20]);
                    datos.setAplicmastpack((String) res[21]);
                    datos.setMasterpack((Integer) res[22]);
                    datos.setTransE((int) res[23]);
                    datos.setTransF((int) res[24]);
                    // Agregar el objeto TablaDatos a la lista
                    if (solo0.equals("N") && BigDecimal.valueOf(0).compareTo((BigDecimal) res[8]) == 0) {

                    } else {
                        datosList.add(datos);
                    }

                }
            } else {
                for (Object[] res : results) {
                    double fra = ((double) ((int) res[5] + (int) res[24])) / (int) res[7];
                    BigDecimal stkmin2 = (BigDecimal) res[8];
                    if (((int) res[4] + (int) res[23] + fra) / stkmin2.doubleValue() < multiplicador.doubleValue()) {
                        String fechaFormateada = sdf.format((Date) res[12]);
                        DistribuirAlmacenEstablecimientos datos = new DistribuirAlmacenEstablecimientos(
                                (int) res[0], // siscod
                                (String) res[1], // sisent
                                (String) res[2], // codalm
                                (String) res[3], // codpro
                                (int) res[4], // stkalm
                                (int) res[5], // stkalm_m
                                (int) res[6], // stkmin
                                (int) res[18], // cant_e Se reutilizo para enviar las cantidades inmovilizadas
                                (int) res[19], // cant_f Se reutilizo para enviar las cantidades inmovilizadas
                                (int) res[7], // stkfra
                                (BigDecimal) res[8], // stkmin2
                                (BigDecimal) res[9], // coscom
                                (BigDecimal) res[10], // igvpro
                                (String) res[11], // lote
                                fechaFormateada, // fecven
                                (int) res[13] - (int) res[18], // qtymov
                                (int) res[14] - (int) res[19], // qtymov_m
                                (String) res[15] // restringido
                        );
                        datos.setAplicfrac((String) res[16]);
                        datos.setInmov((String) res[17]);
                        datos.setBlister((Integer) res[20]);
                        datos.setAplicmastpack((String) res[21]);
                        datos.setMasterpack((Integer) res[22]);
                        datos.setTransE((int) res[23]);
                        datos.setTransF((int) res[24]);
                        // Agregar el objeto TablaDatos a la lista
                        if (solo0.equals("N") && BigDecimal.valueOf(0).compareTo((BigDecimal) res[8]) == 0) {

                        } else {
                            datosList.add(datos);
                        }
                    }
                }
            }
            // Convertir la lista de objetos a JSON
            Gson gson = new Gson();
            String json = gson.toJson(datosList);

            return "{\"data\":" + json + "}";
        } catch (Exception e) {
            return "{\"resultado\":\"error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    /*
     * public String distribucionEstablecimientos2(int tipo_stkmin, String
     * tipo_distrib, String codpro, String mostrarrojos, char soloalm, List<String>
     * codalm, List<String> codTip) {
     * EntityManager em = getEntityManager();
     * try {
     * String tiposParam = String.join(",", codTip);
     * String almparam = String.join(",", codalm);
     * StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery(
     * "sel_productos_distribuir_establecimientos_lotes_final");
     * 
     * storedProcedure.registerStoredProcedureParameter("tipo_stkmin",
     * Integer.class, ParameterMode.IN);
     * storedProcedure.registerStoredProcedureParameter("tipo_distrib",
     * String.class, ParameterMode.IN);
     * storedProcedure.registerStoredProcedureParameter("codtip", String.class,
     * ParameterMode.IN);
     * storedProcedure.registerStoredProcedureParameter("codpro", String.class,
     * ParameterMode.IN);
     * storedProcedure.registerStoredProcedureParameter("solo_alm", Character.class,
     * ParameterMode.IN);
     * storedProcedure.registerStoredProcedureParameter("codalm", String.class,
     * ParameterMode.IN);
     * 
     * storedProcedure.setParameter("tipo_stkmin", tipo_stkmin);
     * storedProcedure.setParameter("tipo_distrib", tipo_distrib);
     * storedProcedure.setParameter("codtip", tiposParam);
     * storedProcedure.setParameter("codpro", codpro);
     * storedProcedure.setParameter("solo_alm", soloalm);
     * storedProcedure.setParameter("codalm", almparam);
     * 
     * // Ejecutar el procedimiento almacenado
     * List<Object[]> results = storedProcedure.getResultList();
     * List<DistribuirAlmacenEstablecimientos> datosList = new ArrayList<>();
     * 
     * SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
     * if (mostrarrojos.equals("N")) {
     * for (Object[] res : results) {
     * String fechaFormateada = sdf.format((Date) res[12]);
     * DistribuirAlmacenEstablecimientos datos = new
     * DistribuirAlmacenEstablecimientos(
     * (int) res[0], // siscod
     * (String) res[1], // sisent
     * (String) res[2], // codalm
     * (String) res[3], // codpro
     * (int) res[4], // stkalm
     * (int) res[5], // stkalm_m
     * (int) res[6], // stkmin
     * 0, // cant_e
     * 0, // cant_f
     * (int) res[7], // stkfra
     * (BigDecimal) res[8], // stkmin2
     * (BigDecimal) res[9], // coscom
     * (BigDecimal) res[10], // igvpro
     * (String) res[11], // lote
     * fechaFormateada, // fecven
     * (int) res[13], // qtymov
     * (int) res[14], // qtymov_m
     * (String) res[15] // restringido
     * );
     * 
     * // Agregar el objeto TablaDatos a la lista
     * datosList.add(datos);
     * 
     * }
     * } else {
     * for (Object[] res : results) {
     * if (BigDecimal.valueOf((int) res[4] + (((double) (int) res[5]) / (int)
     * res[7])).compareTo((BigDecimal) res[8]) < 0) {
     * String fechaFormateada = sdf.format((Date) res[12]);
     * DistribuirAlmacenEstablecimientos datos = new
     * DistribuirAlmacenEstablecimientos(
     * (int) res[0], // siscod
     * (String) res[1], // sisent
     * (String) res[2], // codalm
     * (String) res[3], // codpro
     * (int) res[4], // stkalm
     * (int) res[5], // stkalm_m
     * (int) res[6], // stkmin
     * 0, // cant_e
     * 0, // cant_f
     * (int) res[7], // stkfra
     * (BigDecimal) res[8], // stkmin2
     * (BigDecimal) res[9], // coscom
     * (BigDecimal) res[10], // igvpro
     * (String) res[11], // lote
     * fechaFormateada, // fecven
     * (int) res[13], // qtymov
     * (int) res[14], // qtymov_m
     * (String) res[15] // restringido
     * );
     * 
     * // Agregar el objeto TablaDatos a la lista
     * datosList.add(datos);
     * }
     * }
     * }
     * // Convertir la lista de objetos a JSON
     * Gson gson = new Gson();
     * String json = gson.toJson(datosList);
     * 
     * return "{\"data\":" + json + "}";
     * } catch (Exception e) {
     * return "{\"resultado\":\"error\",\"mensaje\":\"" + e.getMessage() + "\"}";
     * } finally {
     * em.close();
     * }
     * }
     */
    public String listarProductosNomb() {
        EntityManager em = null;
        try {
            em = getEntityManager();

            String sql = "SELECT codpro, despro FROM fa_productos where estado='S'";
            Query query = em.createNativeQuery(sql);
            List<Object[]> resultados = query.getResultList();
            String cadena = "[";
            boolean s = true;
            for (Object[] resultado : resultados) {
                s = false;
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codpro", (String) resultado[0]);
                jsonObj.put("despro", (String) resultado[1]);
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

    public String listarProductoscompras(String codtip, String codlab, String codgen, String estr, String pet) {
        EntityManager em = null;
        try {
            em = getEntityManager();

            StringBuilder sql = new StringBuilder();
            sql.append("SELECT p.codpro, p.despro, p.codlab, stocks.stkalm AS stke, ")
                    .append("stocks.stkalm_m AS stkf, stocks.stkmin2, stocks.stkseg, ")
                    .append("case when stocks.stkmin2=0 then null else ((stocks.stkalm*p.stkfra+stocks.stkalm_m)/stocks.stkmin2)/p.stkfra end as meses, stkfra, ")
                    .append("stocks.stkmax2, p.coscom, p.cospro, p.codgen, gen.desgen,case when pet.codpro is null then 'N' else 'S' end,p.predesac, ")
                    .append("COALESCE(transito.transitoE, 0) as transitoE, COALESCE(transito.transitoF, 0) as transitoF ")
                    .append("FROM fa_productos p ")
                    .append("INNER JOIN ( ")
                    .append("    SELECT codpro, SUM(stkalm) AS stkalm, SUM(stkalm_m) AS stkalm_m, ")
                    .append("    SUM(stkmin2) AS stkmin2, SUM(stkmax2) AS stkmax2, SUM(stkseg) AS stkseg ")
                    .append("    FROM fa_stock_almacenes ")
                    .append("    WHERE estado='S' ")
                    .append("    GROUP BY codpro ")
                    .append(") stocks ON stocks.codpro = p.codpro ")
                    .append("INNER JOIN fa_genericos gen ON gen.codgen = p.codgen ")
                    .append("LEFT JOIN (select codpro from petitorio where estado='S' group by codpro) pet on pet.codpro=p.codpro ")
                    .append("LEFT JOIN ( ")
                    .append("    SELECT codpro, SUM(total_qtypro) as transitoE, SUM(total_qtypro_m) as transitoF ")
                    .append("    FROM view_productos_transito ")
                    .append("    GROUP BY codpro ")
                    .append(") transito ON transito.codpro = p.codpro ")
                    .append("WHERE p.estado = 'S' ");

            if (!"".equals(codtip)) {
                sql.append("and p.codtip=? ");
            }
            if (!"".equals(codlab)) {
                sql.append("and p.codlab=? ");
            }
            if (!"".equals(codgen)) {
                sql.append("and p.codgen=? ");
            }
            if (!"".equals(estr)) {
                sql.append("and p.categvta=? ");
            }
            if ("N".equals(pet)) {
                sql.append("and pet.codpro is null ");
            }
            if ("S".equals(pet)) {
                sql.append("and pet.codpro is not null ");
            }

            Query query = em.createNativeQuery(sql.toString());
            int i = 1;
            if (!"".equals(codtip)) {
                query.setParameter(i++, codtip);
            }
            if (!"".equals(codlab)) {
                query.setParameter(i++, codlab);
            }
            if (!"".equals(codgen)) {
                query.setParameter(i++, codgen);
            }
            if (!"".equals(estr)) {
                query.setParameter(i++, estr);
            }

            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codpro", fila[0]);
                jsonObj.put("despro", fila[1]);
                jsonObj.put("codlab", fila[2]);
                jsonObj.put("stke", fila[3]);
                jsonObj.put("stkf", fila[4]);
                jsonObj.put("stkmin2", fila[5]);
                jsonObj.put("stkseg", fila[6]);
                jsonObj.put("meses", fila[7]);
                jsonObj.put("stkfra", fila[8]);
                jsonObj.put("stkmax2", fila[9]);
                jsonObj.put("coscom", fila[10]);
                jsonObj.put("cospro", fila[11]);
                jsonObj.put("codgen", fila[12]);
                jsonObj.put("desgen", fila[13]);
                jsonObj.put("pet", fila[14]);
                jsonObj.put("predesac", fila[15]);
                jsonObj.put("transitoE", fila[16]); // Nuevo campo
                jsonObj.put("transitoF", fila[17]); // Nuevo campo
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

    public String indicadorcompra(String codpro) {
        EntityManager em = null;
        try {
            em = getEntityManager();

            Query query = em.createNativeQuery(
                    "select * from( select top 1 'PRECIO ACTUAL' as descr, isnull(p.desprv,'') as desprv, isnull((round((d.coscom * (1 + (d.igvpro / 100))),2)),0) as costo, (isnull(d.qtypro,0)*pr.stkfra+isnull(d.qtypro_m,0))/pr.stkfra as cant,m.feccre, isnull((m.tdofac +'-'+m.serdoc + '-' + m.numdoc),'') as doc from fa_movimientos m inner join fa_movimientos_detalle d on m.invnum=d.invnum inner join fa_proveedores p on p.codprv=m.codprv inner join fa_productos pr on pr.codpro =d.codpro where d.tipkar in('CC','CQ','C+') and m.movsta<>'A' and m.codalm in('A1','A2') and d.codpro=? order by m.invnum) a UNION all select * from ( select top 1 'PRECIO MAS BAJO' as descr, isnull(p.desprv,'') as desprv, isnull((round((d.coscom * (1 + (d.igvpro / 100))),2)),0) as costo, (isnull(d.qtypro,0)*pr.stkfra+isnull(d.qtypro_m,0))/pr.stkfra as cant,m.feccre, isnull((m.tdofac +'-'+m.serdoc + '-' + m.numdoc),'') as doc from fa_movimientos m inner join fa_movimientos_detalle d on d.invnum=m.invnum inner join fa_proveedores p on p.codprv=m.codprv inner join fa_productos pr on pr.codpro=d.codpro where m.feccre between (getdate() - 365) and getdate() and d.tipkar in('CC','CQ','C+') and m.movsta <> 'A' and m.codalm in ('A1','A2') and d.codpro=? order by round((d.coscom * (1 + (d.igvpro / 100))),2) asc, m.feccre desc ) a UNION all select * from ( select top 1 'PRECIO MAS ALTO' as descr, isnull(p.desprv,'') as desprv, isnull((round((d.coscom * (1 + (d.igvpro / 100))),2)),0) as costo, (isnull(d.qtypro,0)*pr.stkfra+isnull(d.qtypro_m,0))/pr.stkfra as cant,m.feccre, isnull((m.tdofac +'-'+m.serdoc + '-' + m.numdoc),'') as doc from fa_movimientos m inner join fa_movimientos_detalle d on d.invnum=m.invnum inner join fa_proveedores p on p.codprv=m.codprv inner join fa_productos pr on pr.codpro=d.codpro where m.feccre between (getdate() - 365) and getdate() and d.tipkar in('CC','CQ','C+') and m.movsta <> 'A' and m.codalm in ('A1','A2') and d.codpro=? order by round((d.coscom * (1 + (d.igvpro / 100))),2) desc, m.feccre desc ) a order by descr");
            int i = 1;
            query.setParameter(i++, codpro);
            query.setParameter(i++, codpro);
            query.setParameter(i++, codpro);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("descrip", fila[0]);
                jsonObj.put("desprv", fila[1]);
                jsonObj.put("costo", fila[2]);
                jsonObj.put("cant", fila[3]);
                jsonObj.put("feccre", fila[4]);
                jsonObj.put("doc", fila[5]);
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

    public String ultimascompras(String codpro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "SELECT p.desprv, d.qtypro, c.feccre as fecdoc, ISNULL(ROUND((d.coscom * (1 + (d.igvpro / 100))), 2), 0) AS precio, (c.tdofac + '-' + c.serdoc + '-' + c.numdoc) as documento FROM fa_movimientos c WITH (NOLOCK) INNER JOIN fa_movimientos_detalle d WITH (NOLOCK) ON c.invnum = d.invnum INNER JOIN fa_proveedores p WITH (NOLOCK) ON c.codprv = p.codprv WHERE c.feccre BETWEEN (GETDATE() - 365) AND GETDATE() AND d.codpro = ? AND d.tipkar IN('CC', 'CQ', 'C+') AND c.movsta <> 'A' AND c.codalm = 'A1' ORDER BY c.invnum DESC;");
            int i = 1;
            query.setParameter(i++, codpro);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("desprv", fila[0]);
                jsonObj.put("cante", fila[1]);
                jsonObj.put("feccre", fila[2]);
                jsonObj.put("precio", fila[3]);
                jsonObj.put("doc", fila[4]);
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

    public String obtenerbasico(String codpro) {
        EntityManager em = null;
        try {
            em = getEntityManager();

            Query query = em.createNativeQuery(
                    "select p.codpro, p.despro,p.codlab,COALESCE(qr.codalt, qr2.codalt) from fa_productos p left join fa_codigos_alternos qr on qr.codpro=p.codpro and qr.idcalt='B' left join fa_codigos_alternos qr2 on qr2.codpro=p.codpro and qr2.idcalt='C' where p.codpro=?");
            query.setParameter(1, codpro);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codpro", fila[0]);
                jsonObj.put("despro", fila[1]);
                jsonObj.put("codlab", fila[2]);
                jsonObj.put("qr", fila[3]);
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

    public List<DistribuirAlmacenEstablecimientos> distribucionEstablecimientosOBJ() {
        EntityManager em = null;
        try {
            em = getEntityManager();

            int siscod = 1;
            int tipo_stkmin = 45;
            String tipo_distrib = "TODOS";
            StoredProcedureQuery storedProcedure = em
                    .createStoredProcedureQuery("sel_productos_distribuir_establecimientos_lotes");

            storedProcedure.registerStoredProcedureParameter("siscod", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("tipo_stkmin", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("tipo_distrib", String.class, ParameterMode.IN);

            storedProcedure.setParameter("siscod", 1);
            storedProcedure.setParameter("tipo_stkmin", tipo_stkmin);
            storedProcedure.setParameter("tipo_distrib", "TODOS");

            // Ejecutar el procedimiento almacenado
            List<Object[]> results = storedProcedure.getResultList();
            List<DistribuirAlmacenEstablecimientos> datosList = new ArrayList<>();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            int cant = 0;
            for (Object[] res : results) {
                // Crear un nuevo objeto TablaDatos y asignar los valores del resultado
                if (cant < 30) {
                    cant++;
                    String fechaFormateada = sdf.format((Date) res[14]);
                    DistribuirAlmacenEstablecimientos datos = new DistribuirAlmacenEstablecimientos(
                            (int) res[0], // siscod
                            (String) res[1], // sisent
                            (String) res[2], // codalm
                            (String) res[3], // codpro
                            (int) res[4], // stkalm
                            (int) res[5], // stkalm_m
                            (int) res[6], // stkmin
                            (int) res[7], // cant_e
                            (int) res[8], // cant_f
                            (int) res[9], // stkfra
                            (BigDecimal) res[10], // stkmin2
                            (BigDecimal) res[11], // coscom
                            (BigDecimal) res[12], // igvpro
                            (String) res[13], // lote
                            fechaFormateada, // fecven
                            (int) res[15], // qtymov
                            (int) res[16] // qtymov_m
                    );

                    // Agregar el objeto TablaDatos a la lista
                    datosList.add(datos);
                }
            }
            // Convertir la lista de objetos a JSON
            Gson gson = new Gson();
            String json = gson.toJson(datosList);

            return datosList;
        } catch (Exception e) {
            return null;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String ListarTiposProductos() {
        EntityManager em = null;
        try {

            em = getEntityManager();
            StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("sel_tipoproductos_filtra");

            // Ejecutar el procedimiento almacenado
            List<Object[]> results = storedProcedure.getResultList();
            List<TiposProducto> datosList = new ArrayList<>();

            for (Object[] res : results) {
                // Crear un nuevo objeto TablaDatos y asignar los valores del resultado
                TiposProducto datos = new TiposProducto(
                        (String) res[1], // sisent
                        (String) res[2] // codalm
                );

                // Agregar el objeto TablaDatos a la lista
                datosList.add(datos);
            }

            // Convertir la lista de objetos a JSON
            Gson gson = new Gson();
            String json = gson.toJson(datosList);

            return "{\"data\":" + json + "}";
        } catch (Exception e) {
            return "{\"resultado\":\"error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String mostrargraficoventas(String codpro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sql = "SELECT año, mes, ventas,almacen FROM view_grafica_ventas_producto WHERE codigo = ? ORDER BY año, mes";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, codpro);

            // Crear un mapa para almacenar las ventas por año y mes
            Map<String, Map<String, BigDecimal>> ventasPorAlmacenYMes = new LinkedHashMap<>(); // Mapa para almacenar
                                                                                               // las ventas por almacén
                                                                                               // y mes
            Map<String, BigDecimal> ventasTotalesPorMes = new LinkedHashMap<>(); // Mapa para almacenar las ventas
                                                                                 // totales por mes

            // Obtener los resultados de la consulta
            List<Object[]> resultados = query.getResultList();

            // Recorrer los resultados y almacenar las ventas en el mapa
            for (Object[] resultado : resultados) {
                String año = (String) resultado[0];
                String mes = (String) resultado[1];
                BigDecimal ventas = (BigDecimal) resultado[2];
                String codalm = (String) resultado[3]; // Código de almacén
                String añoMes = mes + "/" + año;

                // Almacenar las ventas por almacén y mes
                ventasPorAlmacenYMes.putIfAbsent(codalm, new LinkedHashMap<>());
                ventasPorAlmacenYMes.get(codalm).put(añoMes, ventas);

                // Calcular las ventas totales por mes de manera incremental
                ventasTotalesPorMes.put(añoMes, ventasTotalesPorMes.getOrDefault(añoMes, BigDecimal.ZERO).add(ventas));
            }

            // Construir las cadenas JSON para ventas por almacén y totales
            StringBuilder jsonResult = new StringBuilder("{");

            for (Map.Entry<String, Map<String, BigDecimal>> entry : ventasPorAlmacenYMes.entrySet()) {
                String codalm = entry.getKey();
                Map<String, BigDecimal> ventasPorMes = entry.getValue();

                StringBuilder cadenamesaño = new StringBuilder("[");
                StringBuilder cadenaventas = new StringBuilder("[");
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, -12); // Retroceder 12 meses
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM");
                int totalMeses = 13; // Número total de meses que vamos a iterar
                while (totalMeses > 0) {
                    int año = calendar.get(Calendar.YEAR);
                    int mes = calendar.get(Calendar.MONTH) + 1;
                    String añoMes = String.format("%02d/%02d", mes, año % 100);
                    BigDecimal ventas = ventasPorMes.getOrDefault(añoMes, BigDecimal.ZERO);
                    cadenamesaño.append("\"").append(añoMes).append("\",");
                    cadenaventas.append(ventas).append(",");
                    calendar.add(Calendar.MONTH, 1); // Avanzar al siguiente mes
                    totalMeses--;
                }
                // Eliminar la última coma si existe
                if (cadenamesaño.charAt(cadenamesaño.length() - 1) == ',') {
                    cadenamesaño.deleteCharAt(cadenamesaño.length() - 1);
                }
                if (cadenaventas.charAt(cadenaventas.length() - 1) == ',') {
                    cadenaventas.deleteCharAt(cadenaventas.length() - 1);
                }
                cadenamesaño.append("]");
                cadenaventas.append("]");

                jsonResult.append("\"").append(codalm).append("\":{\"fec\":").append(cadenamesaño)
                        .append(",\"ventas\":").append(cadenaventas).append("},");
            }

            StringBuilder cadenamesañoTotales = new StringBuilder("[");
            StringBuilder cadenaventasTotales = new StringBuilder("[");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -12); // Retroceder 12 meses
            int totalMeses = 13; // Número total de meses que vamos a iterar
            while (totalMeses > 0) {
                int año = calendar.get(Calendar.YEAR);
                int mes = calendar.get(Calendar.MONTH) + 1;
                String añoMes = String.format("%02d/%02d", mes, año % 100);
                BigDecimal ventasTotales = ventasTotalesPorMes.getOrDefault(añoMes, BigDecimal.ZERO);
                cadenamesañoTotales.append("\"").append(añoMes).append("\",");
                cadenaventasTotales.append(ventasTotales).append(",");
                calendar.add(Calendar.MONTH, 1); // Avanzar al siguiente mes
                totalMeses--;
            }
            if (cadenamesañoTotales.charAt(cadenamesañoTotales.length() - 1) == ',') {
                cadenamesañoTotales.deleteCharAt(cadenamesañoTotales.length() - 1);
            }
            if (cadenaventasTotales.charAt(cadenaventasTotales.length() - 1) == ',') {
                cadenaventasTotales.deleteCharAt(cadenaventasTotales.length() - 1);
            }
            cadenamesañoTotales.append("]");
            cadenaventasTotales.append("]");

            jsonResult.append("\"TOTAL\":{\"fec\":").append(cadenamesañoTotales).append(",\"ventas\":")
                    .append(cadenaventasTotales).append("}");

            if (jsonResult.charAt(jsonResult.length() - 1) == ',') {
                jsonResult.deleteCharAt(jsonResult.length() - 1);
            }
            jsonResult.append("}");

            return jsonResult.toString();

        } catch (Exception e) {
            return "{\"Resultado\":\"error\",\"mensaje\":\"" + e + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String mostrarprediccion(String codpro) {
        EntityManager em = null;
        try {
            em = getEntityManager();

            String sql = "SELECT mesaño, ventas FROM view_prediccion_ventas WHERE codigo = ? ORDER BY año,mesaño";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, codpro);

            // Obtener los resultados de la consulta
            List<Object[]> resultados = query.getResultList();
            List<String> meses = new ArrayList<>();
            LocalDate fechaActual = LocalDate.now();
            fechaActual = fechaActual.minusMonths(12);
            // fechaActual = fechaActual.minusMonths(12);
            int o = 0;
            JSONArray fechas12 = new JSONArray();
            JSONArray ventas12 = new JSONArray();
            JSONObject json = new JSONObject();
            for (int i = 0; i < 13; i++) {
                // Formato MM/yy
                String mesaño = fechaActual.format(DateTimeFormatter.ofPattern("MM/yy"));
                meses.add(mesaño);
                fechas12.put(mesaño);
                fechaActual = fechaActual.plusMonths(1);
                if (o >= resultados.size()) {
                    ventas12.put(0);
                }
                while (o < resultados.size()) {
                    Object[] resultado = resultados.get(o);
                    String añoMesdb = (String) resultado[0];
                    BigDecimal ventasdb = (BigDecimal) resultado[1];
                    if (añoMesdb.equals(mesaño)) {
                        o++;
                        ventas12.put(ventasdb);
                        break;
                    } else {
                        ventas12.put(0);
                        break;
                    }
                }
            }
            sql = "select sum(s.stkmin2) as stockminimo, CAST(sum(s.stkalm)*p.stkfra+sum(s.stkalm_m) AS DECIMAL(10,2))/p.stkfra,case when isnull(sum(s.stkmin2),0) =0 then null else ((sum(s.stkalm)*p.stkfra+sum(s.stkalm_m))/sum(s.stkmin2))/p.stkfra end from fa_stock_almacenes s inner join fa_productos p on p.codpro=s.codpro where s.codpro=? group by p.stkfra";
            query = em.createNativeQuery(sql);
            query.setParameter(1, codpro);

            // Obtener los resultados de la consulta
            resultados = query.getResultList();
            try {
                Object[] resultado = (Object[]) query.getSingleResult();
                json.put("stkmin2", resultado[0]);
                json.put("stkalm", resultado[1]);
                json.put("meses", resultado[2]);
            } catch (NoResultException e) {
                System.out.println("No se encontraron registros para el producto.");
            } catch (NonUniqueResultException e) {
                System.out.println("Error: Se encontraron múltiples registros.");
            }
            json.put("resultado", "ok");
            json.put("fechas12", fechas12);
            json.put("ventas12", ventas12);
            return json.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"error\",\"mensaje\":\"" + e + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    /*
     * public String mostrarprediccion(String codpro) {
     * EntityManager em = getEntityManager();
     * try {
     * String sql =
     * "SELECT mesaño, ventas FROM view_prediccion_ventas WHERE codigo = ? ORDER BY año,mesaño"
     * ;
     * Query query = em.createNativeQuery(sql);
     * query.setParameter(1, codpro);
     * 
     * // Obtener los resultados de la consulta
     * List<Object[]> resultados = query.getResultList();
     * List<String> meses = new ArrayList<>();
     * LocalDate fechaActual = LocalDate.now();
     * fechaActual = fechaActual.minusMonths(24);
     * //fechaActual = fechaActual.minusMonths(12);
     * int o = 0;
     * JSONArray fechas12 = new JSONArray();
     * JSONArray ventas12 = new JSONArray();
     * JSONArray ventas24 = new JSONArray();
     * JSONObject json = new JSONObject();
     * for (int i = 0; i < 24; i++) {
     * // Formato MM/yy
     * String mesaño = fechaActual.format(DateTimeFormatter.ofPattern("MM/yy"));
     * meses.add(mesaño);
     * if (i >= 12) {
     * fechas12.put(mesaño);
     * }
     * fechaActual = fechaActual.plusMonths(1);
     * if (o >= resultados.size()) {
     * ventas24.put(0);
     * if (i >= 12) {
     * ventas12.put(0);
     * }
     * }
     * while (o < resultados.size()) {
     * Object[] resultado = resultados.get(o);
     * String añoMesdb = (String) resultado[0];
     * BigDecimal ventasdb = (BigDecimal) resultado[1];
     * if (añoMesdb.equals(mesaño)) {
     * o++;
     * ventas24.put(ventasdb);
     * if (i >= 12) {
     * ventas12.put(ventasdb);
     * }
     * break;
     * } else {
     * ventas24.put(0);
     * if (i >= 12) {
     * ventas12.put(0);
     * }
     * break;
     * }
     * }
     * }
     * json.put("resultado", "ok");
     * json.put("fechas12", fechas12);
     * json.put("ventas12", ventas12);
     * json.put("ventas24", ventas24);
     * return json.toString();
     * } catch (Exception e) {
     * return "{\"Resultado\":\"error\",\"mensaje\":\"" + e + "\"}";
     * } finally {
     * if (em != null) {
     * em.close();
     * }
     * }
     * }
     */
    public String listarProductosAplicFrac(String codlab, String codgen, String codfam, String codtip, String codpro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            // Construir la consulta JPQL dinámicamente
            StringBuilder jpql = new StringBuilder(
                    "SELECT f.codpro, f.despro,f.aplicfrac,COALESCE(qr.codalt, qr2.codalt),f.blister,f.masterpack,f.aplicmastpack,f.stkfra FROM fa_productos f left JOIN fa_codigos_alternos qr ON qr.codpro = f.codpro AND qr.idcalt = 'B' left JOIN fa_codigos_alternos qr2 ON qr2.codpro = f.codpro AND qr2.idcalt = 'C' WHERE f.estado='S'");

            if (!codlab.equals("")) {
                jpql.append(" AND f.codlab = ?");
            }
            if (!codgen.equals("")) {
                jpql.append(" AND f.codgen = ?");
            }
            if (!codfam.equals("")) {
                jpql.append(" AND f.codfam = ?");
            }
            if (!codtip.equals("")) {
                jpql.append(" AND f.codtip = ?");
            }
            if (!codpro.equals("")) {
                jpql.append(" AND f.codpro = ?");
            }
            Query query = em.createNativeQuery(jpql.toString());
            int cant = 1;
            // Establecer parámetros en la consulta
            if (!codlab.equals("")) {
                query.setParameter(cant++, codlab);
            }
            if (!codgen.equals("")) {
                query.setParameter(cant++, codgen);
            }
            if (!codfam.equals("")) {
                query.setParameter(cant++, codfam);
            }
            if (!codtip.equals("")) {
                query.setParameter(cant++, codtip);
            }

            if (!codpro.equals("")) {
                query.setParameter(cant++, codpro);
            }
            List<Object[]> productos = query.getResultList();
            JSONArray lista = new JSONArray();
            for (Object[] resultado : productos) {
                JSONObject obj = new JSONObject();
                obj.put("codpro", resultado[0]);
                obj.put("despro", resultado[1]);
                obj.put("aplicfrac", resultado[2]);
                obj.put("qr", resultado[3]);
                obj.put("blister", resultado[4]);
                obj.put("masterpack", resultado[5]);
                obj.put("aplicmastpack", resultado[6]);
                obj.put("stkfra", resultado[7]);
                lista.put(obj);
            }
            return lista.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarProductosQRPrecios() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            // Construir la consulta JPQL dinámicamente
            StringBuilder jpql = new StringBuilder(
                    "SELECT f.codpro, f.despro,COALESCE(qr.codalt, qr2.codalt),f.cospro FROM fa_productos f left JOIN fa_codigos_alternos qr ON qr.codpro = f.codpro AND qr.idcalt = 'B' left JOIN fa_codigos_alternos qr2 ON qr2.codpro = f.codpro AND qr2.idcalt = 'C' WHERE f.estado='S'");

            Query query = em.createNativeQuery(jpql.toString());
            List<Object[]> productos = query.getResultList();
            JSONArray lista = new JSONArray();
            for (Object[] resultado : productos) {
                JSONObject obj = new JSONObject();
                obj.put("codpro", resultado[0]);
                obj.put("despro", resultado[1]);
                obj.put("qr", resultado[2]);
                obj.put("precio", resultado[3]);
                lista.put(obj);
            }
            return lista.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarProductosQRstkfra() {
        EntityManager em = null;
        JSONArray lista = new JSONArray();
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            String sql = "SELECT f.codpro, f.despro, COALESCE(qr.codalt, qr2.codalt), f.stkfra " +
                    "FROM fa_productos f " +
                    "LEFT JOIN fa_codigos_alternos qr ON qr.codpro = f.codpro AND qr.idcalt = 'B' " +
                    "LEFT JOIN fa_codigos_alternos qr2 ON qr2.codpro = f.codpro AND qr2.idcalt = 'C' " +
                    "WHERE f.estado = 'S'";

            Query query = em.createNativeQuery(sql);
            List<Object[]> productos = query.getResultList();
            for (Object[] resultado : productos) {
                JSONObject obj = new JSONObject();
                obj.put("codpro", resultado[0]);
                obj.put("despro", resultado[1]);
                obj.put("qr", resultado[2]);
                obj.put("stkfra", resultado[3]);
                lista.put(obj);
            }
            return lista.toString();
        } catch (Exception e) {
            return lista.toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarProductosQRstkfraConLista(int codinv, int codinvalm) {
        EntityManager em = null;
        JSONArray lista = new JSONArray();
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            InventarioJpaController daoinv = new InventarioJpaController(empresa);
            Inventario objinv = daoinv.findInventario(codinv);

            boolean tieneCaptura = "S".equals(objinv.getCaptura());

            String sql = "SELECT f.codpro, f.despro, COALESCE(qr.codalt, qr2.codalt), f.stkfra, " +
                    "CASE WHEN EXISTS (" +
                    (tieneCaptura ? "SELECT 1 FROM capturastocksinventario c " +
                            "INNER JOIN inventario_almacen ia ON ia.codinvcab = c.codinvcab " +
                            "WHERE ia.codinvalm = ? AND c.codpro = f.codpro " +
                            "AND (c.cante > 0 OR c.cantf > 0)"
                            : "SELECT 1 FROM fa_stock_vencimientos s " +
                                    "INNER JOIN inventario_almacen ia ON ia.codalm = s.codalm " +
                                    "WHERE ia.codinvalm = ? AND s.codpro = f.codpro " +
                                    "AND (s.qtymov > 0 OR s.qtymov_m > 0) AND s.fecven > GETDATE()")
                    +
                    ") OR EXISTS (" +
                    "SELECT 1 FROM inventario_toma it " +
                    "WHERE it.codinvalm = ? AND it.codpro = f.codpro" +
                    ") THEN 1 ELSE 0 END AS tiene_lotes, " +
                    // Nuevo campo isInventariado
                    "CASE WHEN EXISTS (" +
                    "SELECT 1 FROM inventario_toma it " +
                    "WHERE it.codinvalm = ? AND it.codpro = f.codpro " +
                    // "AND (it.tome > 0 OR it.tomf > 0)" +
                    ") THEN 1 ELSE 0 END AS isInventariado " +
                    "FROM inventario_lista_productos ivp " +
                    "INNER JOIN fa_productos f ON ivp.codpro = f.codpro " +
                    "LEFT JOIN fa_codigos_alternos qr ON qr.codpro = f.codpro AND qr.idcalt = 'B' " +
                    "LEFT JOIN fa_codigos_alternos qr2 ON qr2.codpro = f.codpro AND qr2.idcalt = 'C' " +
                    "WHERE ivp.codinvalm = ? AND f.estado = 'S'";

            Query query = em.createNativeQuery(sql);
            query.setParameter(1, codinvalm);
            query.setParameter(2, codinvalm);
            query.setParameter(3, codinvalm); // Para tiene_lotes
            query.setParameter(4, codinvalm); // Para isInventariado
            query.setParameter(5, codinvalm); // Para el WHERE final

            List<Object[]> productos = query.getResultList();
            for (Object[] resultado : productos) {
                JSONObject obj = new JSONObject();
                obj.put("codpro", resultado[0]);
                obj.put("despro", resultado[1]);
                obj.put("qr", resultado[2]);
                obj.put("stkfra", resultado[3]);
                obj.put("tiene_lotes", resultado[4]);
                obj.put("isInventariado", resultado[5]); // Nuevo campo
                lista.put(obj);
            }
            return lista.toString();
        } catch (Exception e) {
            return lista.toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarProductosQRstkfraSinLista(int codinv, int codinvalm) {
        EntityManager em = null;
        JSONArray lista = new JSONArray();
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            InventarioJpaController daoinv = new InventarioJpaController(empresa);
            Inventario objinv = daoinv.findInventario(codinv);

            boolean tieneCaptura = "S".equals(objinv.getCaptura());

            String sql = "SELECT f.codpro, f.despro, COALESCE(qr.codalt, qr2.codalt), f.stkfra, " +
                    "CASE WHEN EXISTS (" +
                    (tieneCaptura ? "SELECT 1 FROM capturastocksinventario c " +
                            "INNER JOIN inventario_almacen ia ON ia.codinvcab = c.codinvcab " +
                            "WHERE ia.codinvalm = ? AND c.codpro = f.codpro " +
                            "AND (c.cante > 0 OR c.cantf > 0)"
                            : "SELECT 1 FROM fa_stock_vencimientos s " +
                                    "INNER JOIN inventario_almacen ia ON ia.codalm = s.codalm " +
                                    "WHERE ia.codinvalm = ? AND s.codpro = f.codpro " +
                                    "AND (s.qtymov > 0 OR s.qtymov_m > 0) AND s.fecven > GETDATE()")
                    +
                    ") OR EXISTS (" +
                    "SELECT 1 FROM inventario_toma it " +
                    "WHERE it.codinvalm = ? AND it.codpro = f.codpro" +
                    ") THEN 1 ELSE 0 END AS tiene_lotes, " +
                    // Nuevo campo isInventariado
                    "CASE WHEN EXISTS (" +
                    "SELECT 1 FROM inventario_toma it " +
                    "WHERE it.codinvalm = ? AND it.codpro = f.codpro " +
                    "AND (it.tome > 0 OR it.tomf > 0)" +
                    ") THEN 1 ELSE 0 END AS isInventariado " +
                    "FROM fa_productos f " +
                    "LEFT JOIN fa_codigos_alternos qr ON qr.codpro = f.codpro AND qr.idcalt = 'B' " +
                    "LEFT JOIN fa_codigos_alternos qr2 ON qr2.codpro = f.codpro AND qr2.idcalt = 'C' " +
                    "WHERE f.estado = 'S'";

            Query query = em.createNativeQuery(sql);
            query.setParameter(1, codinvalm);
            query.setParameter(2, codinvalm);
            query.setParameter(3, codinvalm); // Para isInventariado
            // No se necesita cuarto parámetro para el WHERE final en este caso

            List<Object[]> productos = query.getResultList();
            for (Object[] resultado : productos) {
                JSONObject obj = new JSONObject();
                obj.put("codpro", resultado[0]);
                obj.put("despro", resultado[1]);
                obj.put("qr", resultado[2]);
                obj.put("stkfra", resultado[3]);
                obj.put("tiene_lotes", resultado[4]);
                obj.put("isInventariado", resultado[5]); // Nuevo campo
                lista.put(obj);
            }
            return lista.toString();
        } catch (Exception e) {
            return lista.toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarproductoslabgen() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            String sql = "SELECT f.codpro, f.despro, f.codlab, f.codgen, f.categvta, pa.desgen, " +
                    "ROUND(CASE WHEN d.dtopro IS NULL THEN f.prisal ELSE (f.prisal - (f.prisal * (d.dtopro/100))) END, 2) as precio, "
                    +
                    "ROUND((f.coscom * (1 + (f.igvpro/100))), 2) as costo_con_igv, " +
                    "ROUND((CASE WHEN d.dtopro IS NULL THEN f.prisal ELSE (f.prisal - (f.prisal * (d.dtopro/100))) END) - "
                    +
                    "(f.coscom * (1 + (f.igvpro/100))), 2) as utilidad, " +
                    "CASE WHEN (CASE WHEN d.dtopro IS NULL THEN f.prisal ELSE (f.prisal - (f.prisal * (d.dtopro/100))) END) <= 0 "
                    +
                    "     OR (CASE WHEN d.dtopro IS NULL THEN f.prisal ELSE (f.prisal - (f.prisal * (d.dtopro/100))) END) IS NULL "
                    +
                    "     THEN -99999 " +
                    "     ELSE ROUND(((CASE WHEN d.dtopro IS NULL THEN f.prisal ELSE (f.prisal - (f.prisal * (d.dtopro/100))) END) - "
                    +
                    "     (f.coscom * (1 + (f.igvpro/100)))) * 100 / " +
                    "     (CASE WHEN d.dtopro IS NULL THEN f.prisal ELSE (f.prisal - (f.prisal * (d.dtopro/100))) END), 2) "
                    +
                    "END as porcentaje_utilidad " +
                    "FROM fa_productos f " +
                    "LEFT JOIN fa_genericos pa ON f.codgen = pa.codgen " +
                    "LEFT JOIN fa_descuentos_productos_est d ON d.codpro = f.codpro AND d.siscod = 1 " +
                    "WHERE f.estado='S'";

            Query query = em.createNativeQuery(sql);
            List<Object[]> productos = query.getResultList();
            JSONArray lista = new JSONArray();

            for (Object[] resultado : productos) {
                JSONObject obj = new JSONObject();
                obj.put("codpro", resultado[0]);
                obj.put("despro", resultado[1]);
                obj.put("codlab", resultado[2]);
                obj.put("codgen", resultado[3]);
                obj.put("estra", resultado[4]);
                obj.put("desgen", resultado[5]);
                obj.put("precio", resultado[6]);
                obj.put("costo_con_igv", resultado[7]);
                obj.put("utilidad", resultado[8]);
                obj.put("porcentaje_utilidad", resultado[9]);
                lista.put(obj);
            }

            return lista.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarProductos() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            // Construir la consulta JPQL dinámicamente
            String jpql = "SELECT f.codpro, f.despro,f.stkfra FROM fa_productos f WHERE f.estado='S'";

            Query query = em.createNativeQuery(jpql);
            List<Object[]> productos = query.getResultList();
            JSONArray lista = new JSONArray();
            for (Object[] resultado : productos) {
                JSONObject obj = new JSONObject();
                obj.put("codpro", resultado[0]);
                obj.put("despro", resultado[1]);
                obj.put("stockfra", resultado[2]);
                lista.put(obj);
            }
            return lista.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarProductosAplicSoloFrac() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            // Construir la consulta JPQL dinámicamente
            String jpql = "SELECT f.codpro, f.despro,f.aplicfrac,COALESCE(qr.codalt, qr2.codalt),f.blister,f.masterpack,f.aplicmastpack,f.stkfra FROM fa_productos f left JOIN fa_codigos_alternos qr ON qr.codpro = f.codpro AND qr.idcalt = 'B' left JOIN fa_codigos_alternos qr2 ON qr2.codpro = f.codpro AND qr2.idcalt = 'C' WHERE f.estado='S' and (f.aplicfrac='S' or f.aplicmastpack='S')";

            Query query = em.createNativeQuery(jpql);
            List<Object[]> productos = query.getResultList();
            JSONArray lista = new JSONArray();
            for (Object[] resultado : productos) {
                JSONObject obj = new JSONObject();
                obj.put("codpro", resultado[0]);
                obj.put("despro", resultado[1]);
                obj.put("aplicfrac", resultado[2]);
                obj.put("qr", resultado[3]);
                obj.put("blister", resultado[4]);
                obj.put("masterpack", resultado[5]);
                obj.put("aplicmastpack", resultado[6]);
                obj.put("stkfra", resultado[7]);
                lista.put(obj);
            }
            return lista.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String aplicarFracJSON(String codpro, String estado, Integer blister) {
        try {
            FaProductosSPJpaController dao = new FaProductosSPJpaController(empresa);
            FaProductos obj = dao.findFaProductos(codpro);
            if (estado.equals("false") && "S".equals(obj.getAplicfrac())) {
                obj.setAplicfrac("N");
                obj.setBlister(null);
                dao.edit(obj);
                return "{\"resultado\":\"ok\"}";
            } else if (estado.equals("true") && ("N".equals(obj.getAplicfrac()) || obj.getAplicfrac() == null)) {
                obj.setAplicfrac("S");
                obj.setBlister(blister);
                dao.edit(obj);
                return "{\"resultado\":\"ok\"}";
            }
            return "{\"resultado\":\"error\",\"mensaje\":\"" + "ocurrio un problema" + "\"}";
        } catch (Exception e) {
            return "{\"resultado\":\"error\",\"mensaje\":\"" + e + "\"}";
        }
    }

    public String aplicarMasterPackJSON(String codpro, String estado, Integer blister) {
        try {
            FaProductosSPJpaController dao = new FaProductosSPJpaController(empresa);
            FaProductos obj = dao.findFaProductos(codpro);
            if (estado.equals("false") && "S".equals(obj.getAplicmastpack())) {
                obj.setAplicmastpack("N");
                obj.setMasterpack(null);
                dao.edit(obj);
                return "{\"resultado\":\"ok\"}";
            } else if (estado.equals("true")
                    && ("N".equals(obj.getAplicmastpack()) || obj.getAplicmastpack() == null)) {
                obj.setAplicmastpack("S");
                obj.setMasterpack(blister);
                dao.edit(obj);
                return "{\"resultado\":\"ok\"}";
            }
            return "{\"resultado\":\"error\",\"mensaje\":\"" + "ocurrio un problema" + "\"}";
        } catch (Exception e) {
            return "{\"resultado\":\"error\",\"mensaje\":\"" + e + "\"}";
        }
    }

    public Date parsearFecha(String fechaStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(fechaStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
