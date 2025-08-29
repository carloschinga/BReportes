package dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class MetodosVerificacion extends JpaPadre {
    public MetodosVerificacion(String empresa) {
        super(empresa);
    }

    public int validarExcedente(String fechainicio, String fechafin, Integer ordenini, Integer ordenfin,
            String siscod) {
        EntityManager em = null;
        try {
            em = getEntityManager();

            String sql = "IF EXISTS (" +
                    "    SELECT 1 FROM (" +
                    "        SELECT 1 " +
                    "        FROM picking_cajas c " +
                    "        INNER JOIN picking_detalle d ON d.pickdetcod = c.pickdetcod " +
                    "        INNER JOIN picking m ON m.pickcod = d.pickcod " +
                    "        INNER JOIN pickinglist o ON o.codpicklist = m.codpicklist " +
                    "        INNER JOIN fa_productos p ON d.codpro = p.codpro " +
                    "        INNER JOIN sistema s ON s.siscod = d.siscod " +
                    "        LEFT JOIN reposicion_recepcion r ON r.codpro = d.codpro AND r.orden = m.codpicklist AND r.siscod = d.siscod AND r.estado = 'S' "
                    +
                    "        WHERE d.chkcomrep = 'S' " +
                    "          AND (? IS NULL OR m.codpicklist >= ?) " +
                    "          AND (? IS NULL OR m.codpicklist <= ?) " +
                    "          AND (? IS NULL OR o.feccre >= ?) " +
                    "          AND (? IS NULL OR o.feccre <= ?) " +
                    "          AND (ISNULL(c.canter, 0) - ISNULL(d.cante, 0) + ISNULL(c.cantfr, 0) - ISNULL(d.cantf, 0) + ISNULL(r.cantidad, 0)) != 0 "
                    +
                    "          AND (? IS NULL OR d.siscod = ?) " +
                    "        UNION " +
                    "        SELECT 1 " +
                    "        FROM cajaextra e " +
                    "        INNER JOIN fa_productos p ON e.codpro = p.codpro " +
                    "        LEFT JOIN reposicion_recepcion r ON r.codpro = e.codpro AND r.orden = e.orden AND r.estado = 'S' "
                    +
                    "        WHERE e.estado = 'S' " +
                    "          AND (? IS NULL OR e.orden >= ?) " +
                    "          AND (? IS NULL OR e.orden <= ?) " +
                    "          AND (? IS NULL OR EXISTS (SELECT 1 FROM pickinglist o WHERE o.codpicklist = e.orden AND o.feccre >= ?)) "
                    +
                    "          AND (? IS NULL OR EXISTS (SELECT 1 FROM pickinglist o WHERE o.codpicklist = e.orden AND o.feccre <= ?)) "
                    +
                    "          AND (? IS NULL OR EXISTS (SELECT 1 FROM picking_detalle d INNER JOIN picking_cajas c ON d.pickdetcod = c.pickdetcod WHERE c.caja = e.caja AND d.siscod = ?)) "
                    +
                    "    ) AS t" +
                    ") SELECT 1 ELSE SELECT 0";

            Query query = em.createNativeQuery(sql);
            query.setParameter(1, ordenini);
            query.setParameter(2, ordenini);
            query.setParameter(3, ordenfin);
            query.setParameter(4, ordenfin);
            query.setParameter(5, fechainicio);
            query.setParameter(6, fechainicio);
            query.setParameter(7, fechafin);
            query.setParameter(8, fechafin);
            query.setParameter(9, siscod);
            query.setParameter(10, siscod);
            query.setParameter(11, ordenini);
            query.setParameter(12, ordenini);
            query.setParameter(13, ordenfin);
            query.setParameter(14, ordenfin);
            query.setParameter(15, fechainicio);
            query.setParameter(16, fechainicio);
            query.setParameter(17, fechafin);
            query.setParameter(18, fechafin);
            query.setParameter(19, siscod);
            query.setParameter(20, siscod);

            Object result = query.getSingleResult();
            if (result != null && Integer.parseInt(result.toString()) == 1) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            return -1;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

    }

}
