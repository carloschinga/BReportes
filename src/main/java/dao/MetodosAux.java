package dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.json.JSONArray;
import org.json.JSONObject;

public class MetodosAux extends JpaPadre {

    public MetodosAux(String empresa) {
        super(empresa);
    }

    public boolean hasCaptura(int codinv) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query q = em.createNativeQuery("select captura from inventario where codinv = ?");
            q.setParameter(1, codinv);

            String result = (String) q.getSingleResult();
            return result != null && result.equals("S");
        } catch (Exception e) {
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public JSONObject obtenerCodinvalmsSeparados(int codinv) {
        EntityManager em = null;
        JSONObject resultado = new JSONObject();
        JSONArray arrayCodinvalm = new JSONArray();
        JSONArray arrayCodalm = new JSONArray();

        try {
            em = getEntityManager();
            String sql = "SELECT codinvalm, codalm FROM inventario_almacen WHERE codinv = ?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, codinv);
            List<Object[]> resultados = query.getResultList();

            for (Object[] fila : resultados) {
                if (fila[0] != null) {
                    arrayCodinvalm.put(fila[0].toString());
                }
                if (fila[1] != null) {
                    arrayCodalm.put(fila[1].toString());
                }
            }

            resultado.put("codinvalms", arrayCodinvalm);
            resultado.put("codalms", arrayCodalm);

        } catch (Exception e) {
            // En caso de error, devolver arrays vacíos
            resultado.put("codinvalms", new JSONArray());
            resultado.put("codalms", new JSONArray());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return resultado;
    }

    public String obtenerTodosLosLaboratorios() {
        EntityManager em = null;
        JSONArray jsonArray = new JSONArray();
        try {
            em = getEntityManager();
            String sql = "SELECT codlab, deslab FROM fa_laboratorios";
            Query query = em.createNativeQuery(sql);
            List<Object[]> resultados = query.getResultList();
            for (Object[] fila : resultados) {
                JSONObject item = new JSONObject();
                item.put("id", fila[0]);
                item.put("nombre", fila[1]);
                jsonArray.put(item);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            return jsonArray.toString();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public boolean validarFotoCabecera(int codinv, int codinvcab) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            // 1. Verificar si el inventario requiere fotos
            String sqlInventario = "SELECT captura FROM inventario WHERE codinv = ?";
            Query qInventario = em.createNativeQuery(sqlInventario);
            qInventario.setParameter(1, codinv);

            Object resultado = qInventario.getSingleResult();

            // Si el inventario no trabaja con fotos, siempre es válido
            if (resultado == null) {
                return true;
            }

            // Si el inventario trabaja con fotos, validar esta cabecera
            String sqlCabecera = "SELECT captura FROM inventario_almacen_cabecera " +
                    "WHERE codinvcab = ?";
            Query qCabecera = em.createNativeQuery(sqlCabecera);
            qCabecera.setParameter(1, codinvcab);

            return qCabecera.getSingleResult() != null;
        } catch (Exception e) {
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public JSONObject obtenerEstadisticasGlobales(JSONArray codinvalms, boolean hasCaptura) {
        EntityManager em = null;
        JSONObject resultado = new JSONObject();

        try {
            em = getEntityManager();
            // 1. Convertir JSONArray a lista de Strings
            List<String> codinvalmList = new ArrayList<>();
            for (int i = 0; i < codinvalms.length(); i++) {
                codinvalmList.add(codinvalms.getString(i));
            }

            // Inicializar contadores totales
            int totalContabilizadosGeneral = 0;
            int totalConStockGeneral = 0;

            for (String codinvalm : codinvalmList) {
                Number totalContabilizados = 0;
                // 2. Consulta para items contabilizados (combinaciones únicas de producto-lote)
                if (hasCaptura) {

                    String sqlContabilizados = "SELECT COUNT(DISTINCT it.codpro) " +
                            " FROM inventario_toma it" +
                            " INNER JOIN inventario_almacen ia ON ia.codinvalm = it.codinvalm " +
                            " INNER JOIN capturastocksinventario c ON it.codpro = c.codpro " +
                            " AND  c.codinvcab = ia.codinvcab " +
                            " WHERE ia.codinvalm = ? " +
                            " AND (it.tome > 0 OR it.tomf > 0)" +
                            " AND (c.cante > 0 OR c.cantf > 0)";

                    totalContabilizados = (Number) em.createNativeQuery(sqlContabilizados)
                            .setParameter(1, codinvalm)
                            .getSingleResult();
                } else {

                    String sqlContabilizados = "SELECT COUNT(distinct it.codpro) " +
                            " FROM inventario_toma it" +
                            " INNER JOIN inventario_almacen ia ON ia.codinvalm = it.codinvalm " +
                            " INNER JOIN fa_stock_vencimientos fs ON it.codpro = fs.codpro " +
                            " AND fs.codalm = ia.codalm " +
                            " WHERE ia.codinvalm = ? " +
                            " AND (it.tome > 0 OR it.tomf > 0) " +
                            " AND (fs.qtymov > 0 OR fs.qtymov_m > 0) " +
                            " AND fs.fecven > CONVERT(date, GETDATE())";

                    totalContabilizados = (Number) em.createNativeQuery(sqlContabilizados)
                            .setParameter(1, codinvalm)
                            .getSingleResult();
                }

                int contabilizadosValue = (totalContabilizados != null) ? totalContabilizados.intValue() : 0;
                totalContabilizadosGeneral += contabilizadosValue;

                // 3. Consulta para items con stock (según hasCaptura y hasLista)
                Number totalConStock = 0;

                // CASO: Tiene lista de productos
                if (hasCaptura) {
                    // Tiene captura y tiene lista
                    String sqlConStock = "SELECT COUNT(DISTINCT il.codpro) " +
                            "FROM inventario_lista_productos il " +
                            "INNER JOIN capturastocksinventario c ON il.codpro = c.codpro " +
                            "INNER JOIN inventario_almacen ia ON ia.codinvcab = c.codinvcab " +
                            "WHERE il.codinvalm = ? " +
                            "AND ia.codinvalm = ? " +
                            "AND (c.cante > 0 OR c.cantf > 0)";

                    totalConStock = (Number) em.createNativeQuery(sqlConStock)
                            .setParameter(1, codinvalm)
                            .setParameter(2, codinvalm)
                            .getSingleResult();
                } else {
                    // No tiene captura pero sí tiene lista
                    String sqlConStock = "SELECT COUNT(DISTINCT il.codpro) " +
                            "FROM inventario_lista_productos il " +
                            "INNER JOIN fa_stock_vencimientos fs ON il.codpro = fs.codpro " +
                            "INNER JOIN inventario_almacen ia ON ia.codalm = fs.codalm " +
                            "WHERE il.codinvalm = ? " +
                            "AND ia.codinvalm = ? " +
                            "AND (fs.qtymov > 0 OR fs.qtymov_m > 0) " +
                            "AND fs.fecven > CONVERT(date, GETDATE())";

                    totalConStock = (Number) em.createNativeQuery(sqlConStock)
                            .setParameter(1, codinvalm)
                            .setParameter(2, codinvalm)
                            .getSingleResult();
                }

                int conStockValue = (totalConStock != null) ? totalConStock.intValue() : 0;
                totalConStockGeneral += conStockValue;

                // Añadir estadística individual por codinvalm si es necesario
                resultado.put("contabilizados_" + codinvalm, contabilizadosValue);
                resultado.put("conStock_" + codinvalm, conStockValue);
            }

            // 4. Añadir totales generales al JSONObject
            resultado.put("totalContabilizados", totalContabilizadosGeneral);
            resultado.put("totalConStock", totalConStockGeneral);

        } catch (Exception e) {
            e.printStackTrace();
            resultado.put("error", e.getMessage());
            resultado.put("totalContabilizados", 0);
            resultado.put("totalConStock", 0);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return resultado;
    }

    public JSONObject getCantidadProductosPorConteoContabilizado(int codinvalm, int codinv) {
        EntityManager em = null;
        JSONObject resultado = new JSONObject();

        boolean hasCaptura = hasCaptura(codinv);
        int totalContabilizadosGeneral = 0;

        try {
            em = getEntityManager();
            Number totalContabilizados = 0;
            // 2. Consulta para items contabilizados (combinaciones únicas de producto-lote)
            if (hasCaptura) {

                String sqlContabilizados = "SELECT COUNT(DISTINCT it.codpro) " +
                        " FROM inventario_toma it" +
                        " INNER JOIN inventario_almacen ia ON ia.codinvalm = it.codinvalm " +
                        " INNER JOIN capturastocksinventario c ON it.codpro = c.codpro " +
                        " AND  c.codinvcab = ia.codinvcab " +
                        " WHERE ia.codinvalm = ? " +
                        " AND (it.tome > 0 OR it.tomf > 0)" +
                        " AND (c.cante > 0 OR c.cantf > 0)";

                totalContabilizados = (Number) em.createNativeQuery(sqlContabilizados)
                        .setParameter(1, codinvalm)
                        .getSingleResult();
            } else {

                String sqlContabilizados = "SELECT COUNT(distinct it.codpro) " +
                        " FROM inventario_toma it" +
                        " INNER JOIN inventario_almacen ia ON ia.codinvalm = it.codinvalm " +
                        " INNER JOIN fa_stock_vencimientos fs ON it.codpro = fs.codpro " +
                        " AND fs.codalm = ia.codalm " +
                        " WHERE ia.codinvalm = ? " +
                        " AND (it.tome > 0 OR it.tomf > 0) " +
                        " AND (fs.qtymov > 0 OR fs.qtymov_m > 0) " +
                        " AND fs.fecven > CONVERT(date, GETDATE())";

                totalContabilizados = (Number) em.createNativeQuery(sqlContabilizados)
                        .setParameter(1, codinvalm)
                        .getSingleResult();
            }

            int contabilizadosValue = (totalContabilizados != null) ? totalContabilizados.intValue() : 0;
            totalContabilizadosGeneral += contabilizadosValue;

            return resultado.put("totalContabilizados", totalContabilizadosGeneral);
        } catch (Exception e) {
            e.printStackTrace();
            resultado.put("error", e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return resultado;
    }

    public JSONObject getCantidadProductosPorConteo(int codinvalm, int codinv) {
        EntityManager em = null;
        JSONObject resultado = new JSONObject();
        int totalGeneral = 0;

        try {
            em = getEntityManager();
            // 3. Consulta para items con stock (según hasCaptura y hasLista)
            Number totalConStock = 0;

            // Tiene captura y tiene lista
            String sqlConStock = "SELECT COUNT(DISTINCT il.codpro) " +
                    "FROM inventario_lista_productos il " +
                    "WHERE il.codinvalm = ? ";

            totalConStock = (Number) em.createNativeQuery(sqlConStock)
                    .setParameter(1, codinvalm)
                    .setParameter(2, codinvalm)
                    .getSingleResult();

            int conStockValue = (totalConStock != null) ? totalConStock.intValue() : 0;
            totalGeneral += conStockValue;

            resultado.put("totalProductos", totalGeneral);
        } catch (Exception e) {
            e.printStackTrace();
            resultado.put("error", e.getMessage());
            resultado.put("totalProductos", 0);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return resultado;
    }

    public JSONObject obtenerProductosPorFarmacia(int codinvalm, boolean hasCaptura) {
        EntityManager em = null;
        JSONObject resultado = new JSONObject();

        try {
            em = getEntityManager();
            // Listas para almacenar los productos
            JSONArray productosContabilizados = new JSONArray();
            JSONArray productosConStock = new JSONArray();

            // 1. Consulta para items contabilizados
            if (hasCaptura) {
                String sqlContabilizados = "SELECT DISTINCT it.codpro " +
                        "FROM inventario_toma it " +
                        "INNER JOIN inventario_almacen ia ON ia.codinvalm = it.codinvalm " +
                        "INNER JOIN capturastocksinventario c ON it.codpro = c.codpro " +
                        "AND c.codinvcab = ia.codinvcab " +
                        "WHERE ia.codinvalm = ? " +
                        "AND (it.tome > 0 OR it.tomf > 0) " +
                        "AND (c.cante > 0 OR c.cantf > 0)";

                List<String> resultados = em.createNativeQuery(sqlContabilizados)
                        .setParameter(1, codinvalm)
                        .getResultList();

                for (Object codpro : resultados) {
                    productosContabilizados.put(codpro.toString());
                }
            } else {
                String sqlContabilizados = "SELECT DISTINCT it.codpro " +
                        "FROM inventario_toma it " +
                        "INNER JOIN inventario_almacen ia ON ia.codinvalm = it.codinvalm " +
                        "INNER JOIN fa_stock_vencimientos fs ON it.codpro = fs.codpro " +
                        "AND fs.codalm = ia.codalm " +
                        "WHERE ia.codinvalm = ? " +
                        "AND (it.tome > 0 OR it.tomf > 0) " +
                        "AND (fs.qtymov > 0 OR fs.qtymov_m > 0) " +
                        "AND fs.fecven > CONVERT(date, GETDATE())";

                List<String> resultados = em.createNativeQuery(sqlContabilizados)
                        .setParameter(1, codinvalm)
                        .getResultList();

                for (Object codpro : resultados) {
                    productosContabilizados.put(codpro.toString());
                }
            }

            // 2. Consulta para items con stock
            if (hasCaptura) {
                String sqlConStock = "SELECT DISTINCT il.codpro " +
                        "FROM inventario_lista_productos il " +
                        "INNER JOIN capturastocksinventario c ON il.codpro = c.codpro " +
                        "INNER JOIN inventario_almacen ia ON ia.codinvcab = c.codinvcab " +
                        "WHERE il.codinvalm = ? " +
                        "AND ia.codinvalm = ? " +
                        "AND (c.cante > 0 OR c.cantf > 0)";

                List<String> resultados = em.createNativeQuery(sqlConStock)
                        .setParameter(1, codinvalm)
                        .setParameter(2, codinvalm)
                        .getResultList();

                for (Object codpro : resultados) {
                    productosConStock.put(codpro.toString());
                }
            } else {
                String sqlConStock = "SELECT DISTINCT il.codpro " +
                        "FROM inventario_lista_productos il " +
                        "INNER JOIN fa_stock_vencimientos fs ON il.codpro = fs.codpro " +
                        "INNER JOIN inventario_almacen ia ON ia.codalm = fs.codalm " +
                        "WHERE il.codinvalm = ? " +
                        "AND ia.codinvalm = ? " +
                        "AND (fs.qtymov > 0 OR fs.qtymov_m > 0) " +
                        "AND fs.fecven > CONVERT(date, GETDATE())";

                List<String> resultados = em.createNativeQuery(sqlConStock)
                        .setParameter(1, codinvalm)
                        .setParameter(2, codinvalm)
                        .getResultList();

                for (Object codpro : resultados) {
                    productosConStock.put(codpro.toString());
                }
            }

            // Agregar las listas al resultado
            resultado.put("productosContabilizados", productosContabilizados);
            resultado.put("productosConStock", productosConStock);

        } catch (Exception e) {
            e.printStackTrace();
            resultado.put("error", e.getMessage());
            resultado.put("productosContabilizados", new JSONArray());
            resultado.put("productosConStock", new JSONArray());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return resultado;
    }

    /**
     * Obtiene todas las farmacias (almacenes) participantes en un inventario
     * específico
     */
    public String obtenerFarmaciasPorInventario(int codinv) {
        EntityManager em = null;
        JSONArray jsonArray = new JSONArray();
        try {
            em = getEntityManager();
            String sql = "SELECT DISTINCT a.codalm, a.desalm, a.estado " +
                    "FROM inventario_almacen ia " +
                    "JOIN fa_almacenes a ON ia.codalm = a.codalm " +
                    "WHERE ia.codinv = ? " +
                    "ORDER BY a.desalm";

            List<Object[]> resultados = em.createNativeQuery(sql)
                    .setParameter(1, codinv)
                    .getResultList();

            for (Object[] fila : resultados) {
                JSONObject item = new JSONObject();
                item.put("codalm", fila[0]);
                item.put("nombre", fila[1]);
                item.put("estado", fila[2]);
                jsonArray.put(item);
            }

            return jsonArray.toString();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Obtiene todos los conteos de inventario para una farmacia específica
     */
    public String obtenerConteosPorFarmacia(int codinv, String codalm) {
        EntityManager em = null;
        JSONArray jsonArray = new JSONArray();
        try {
            em = getEntityManager();
            String sql = "SELECT codinvalm, desinvalm, estdet, fecape, feccir, tipdet, numitm "
                    + "FROM inventario_almacen "
                    + "WHERE codinv = ? AND codalm = ? "
                    + "ORDER BY fecape DESC";

            List<Object[]> resultados = em.createNativeQuery(sql)
                    .setParameter(1, codinv)
                    .setParameter(2, codalm)
                    .getResultList();

            // Si no hay resultados pero existen conteos registrados (totalConteos > 0)
            if (resultados.isEmpty()) {
                // Consultar solo los IDs de los conteos existentes
                String sqlIds = "SELECT codinvalm FROM inventario_almacen WHERE codinv = ? AND codalm = ?";
                List<Integer> ids = em.createNativeQuery(sqlIds)
                        .setParameter(1, codinv)
                        .setParameter(2, codalm)
                        .getResultList();

                // Crear objetos vacíos para cada conteo
                for (Integer id : ids) {
                    JSONObject item = new JSONObject();
                    item.put("codinvalm", id);
                    item.put("nombre", "Conteo " + id); // Nombre por defecto
                    item.put("estado", "N/A");
                    jsonArray.put(item);
                }
            } else {
                // Procesar resultados normales
                for (Object[] fila : resultados) {
                    JSONObject item = new JSONObject();
                    item.put("codinvalm", fila[0]);
                    item.put("nombre", fila[1] != null ? fila[1] : "Conteo " + fila[0]);
                    item.put("estado", fila[2]);
                    jsonArray.put(item);
                }
            }

            return jsonArray.toString();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Obtiene todos los productos asociados a un conteo específico
     */
    public String obtenerProductosPorConteo(int codinvalm) {
        EntityManager em = null;
        JSONArray jsonArray = new JSONArray();

        // Obtenemos los flags del inventario asociado
        boolean hasCaptura = false;
        boolean hasLista = false;

        try {
            em = getEntityManager();
            // Primero obtenemos el codinv asociado a este codinvalm
            String sqlGetInv = "SELECT codinv, direccionado, captura FROM inventario_almacen ia " +
                    "JOIN inventario i ON ia.codinv = i.codinv " +
                    "WHERE ia.codinvalm = ?";
            Object[] invData = (Object[]) em.createNativeQuery(sqlGetInv)
                    .setParameter(1, codinvalm)
                    .getSingleResult();

            int codinv = (Integer) invData[0];
            hasLista = invData[1] != null && (Boolean) invData[1];
            hasCaptura = invData[2] != null && ((String) invData[2]).equals("S");

            String sql;
            if (hasLista) {
                sql = "SELECT DISTINCT ilp.codpro, p.despro, p.codtip, t.destip " +
                        "FROM inventario_lista_productos ilp " +
                        "JOIN fa_productos p ON ilp.codpro = p.codpro " +
                        "LEFT JOIN fa_tipos t ON p.codtip = t.codtip " +
                        "WHERE ilp.codinvalm = ? " +
                        "ORDER BY p.despro";
            } else {
                if (hasCaptura) {
                    sql = "SELECT DISTINCT p.codpro, p.despro, p.codtip, t.destip " +
                            "FROM fa_productos p " +
                            "LEFT JOIN fa_tipos t ON p.codtip = t.codtip " +
                            "JOIN capturastocksinventario c ON p.codpro = c.codpro " +
                            "JOIN inventario_almacen ia ON ia.codinvcab = c.codinvcab " +
                            "WHERE ia.codinvalm = ? " +
                            "AND (c.cante > 0 OR c.cantf > 0) " +
                            "ORDER BY p.despro";
                } else {
                    sql = "SELECT DISTINCT p.codpro, p.despro, p.codtip, t.destip " +
                            "FROM fa_productos p " +
                            "LEFT JOIN fa_tipos t ON p.codtip = t.codtip " +
                            "JOIN fa_stock_vencimientos fs ON p.codpro = fs.codpro " +
                            "JOIN inventario_almacen ia ON ia.codalm = fs.codalm " +
                            "WHERE ia.codinvalm = ? " +
                            "AND (fs.qtymov > 0 OR fs.qtymov_m > 0) " +
                            "AND fs.fecven > CONVERT(date, GETDATE()) " +
                            "ORDER BY p.despro";
                }
            }

            List<Object[]> resultados = em.createNativeQuery(sql)
                    .setParameter(1, codinvalm)
                    .getResultList();

            for (Object[] fila : resultados) {
                JSONObject item = new JSONObject();
                item.put("codpro", fila[0]);
                item.put("despro", fila[1]);
                item.put("codtip", fila[2]);
                item.put("destip", fila[3]);
                jsonArray.put(item);
            }

            return jsonArray.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return jsonArray.toString();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Obtiene estadísticas de productos con coincidencias en el inventario
     */
    public String obtenerEstadisticasPorProductosIdenticos(int codinv, int totalItemConStock) {
        EntityManager em = null;
        JSONArray jsonArray = new JSONArray();

        try {
            em = getEntityManager();
            String sql = "SELECT " +
                    "ia.desinvalm AS nombre_conteo, " +
                    "fa.desalm AS nombre_farmacia, " +
                    "COUNT(*) AS total_registros, " +
                    "SUM(CASE WHEN it.stkalm = it.tome AND it.stkalm_m = it.tomf " +
                    "     AND (it.stkalm != 0 OR it.tome != 0 OR it.stkalm_m != 0 OR it.tomf != 0) " +
                    "     THEN 1 ELSE 0 END) AS coincidencias, " +
                    "it.codinvalm " +
                    "FROM inventario_toma it " +
                    "JOIN inventario_almacen ia ON it.codinvalm = ia.codinvalm " +
                    "JOIN fa_almacenes fa ON ia.codalm = fa.codalm " +
                    "WHERE ia.codinv = ? " +
                    "GROUP BY ia.desinvalm, fa.desalm, it.codinvalm " +
                    "ORDER BY fa.desalm, ia.desinvalm";

            List<Object[]> resultados = em.createNativeQuery(sql)
                    .setParameter(1, codinv)
                    .getResultList();

            for (Object[] fila : resultados) {
                JSONObject item = new JSONObject();
                item.put("conteo", fila[0]);
                item.put("farmacia", fila[1]);
                item.put("total_registros", fila[2]);
                item.put("coincidencias", fila[3]);

                double total = ((Number) fila[2]).doubleValue();
                double coincidencias = ((Number) fila[3]).doubleValue();
                double progreso = total > 0 ? (total / totalItemConStock) * 100 : 0;

                item.put("progreso", Math.round(progreso * 100.0) / 100.0);
                jsonArray.put(item);
            }

            return jsonArray.toString();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    /**
     * Calcula el progreso completo del inventario para un conjunto de productos y
     * conteos
     */
    public JSONObject calcularProgresoCompleto(String[] productosArray, Integer[] codinvalmArray) {
        JSONObject resultado = new JSONObject();

        // Inicializar con valores por defecto
        resultado.put("progreso", 0.0);
        resultado.put("categorias", new JSONArray());

        // Validación de parámetros
        if (productosArray == null || productosArray.length == 0 ||
                codinvalmArray == null || codinvalmArray.length == 0) {
            return resultado;
        }

        JSONArray todasCategorias = new JSONArray();
        double sumaPorcentajes = 0.0;
        int totalConteosProcesados = 0;

        // Dividir productos en lotes para mejor rendimiento
        int batchSize = 500;
        List<String[]> batches = splitIntoBatches(productosArray, batchSize);

        for (Integer codinvalm : codinvalmArray) {
            try {
                int totalInventariadosConteo = 0;
                int totalProductosConteo = 0;
                JSONArray categoriasConteo = new JSONArray();

                // Procesar cada batch
                for (String[] batch : batches) {
                    JSONObject batchResult = calcularProgresoYCategoriasPorConteo(batch, codinvalm);

                    // Sumar valores usando optInt para manejar posibles valores faltantes
                    totalInventariadosConteo += batchResult.optInt("productosInventariados", 0);
                    totalProductosConteo += batch.length; // Usamos el tamaño del batch como total

                    // Consolidar categorías si existen
                    if (batchResult.has("categorias")) {
                        consolidarCategoriasInAux(categoriasConteo, batchResult.getJSONArray("categorias"));
                    }
                }

                // Calcular progreso para este conteo
                if (totalProductosConteo > 0) {
                    double progresoConteo = (totalInventariadosConteo * 100.0) / totalProductosConteo;
                    sumaPorcentajes += progresoConteo;
                    totalConteosProcesados++;
                }

                // Consolidar categorías a nivel global
                consolidarCategoriasInAux(todasCategorias, categoriasConteo);

            } catch (Exception e) {
                System.err.println("Error procesando conteo " + codinvalm + ": " + e.getMessage());
                continue; // Continuar con el siguiente conteo si hay error
            }
        }

        // Calcular promedio general
        if (totalConteosProcesados > 0) {
            double progresoGlobal = sumaPorcentajes / totalConteosProcesados;
            resultado.put("progreso", Math.round(progresoGlobal * 100.0) / 100.0); // Redondear a 2 decimales
        }

        // Consolidar categorías finales
        resultado.put("categorias", consolidarCategoriasFinal(todasCategorias));

        return resultado;
    }

    private List<String[]> splitIntoBatches(String[] array, int batchSize) {
        List<String[]> batches = new ArrayList<>();
        for (int i = 0; i < array.length; i += batchSize) {
            batches.add(Arrays.copyOfRange(array, i, Math.min(array.length, i + batchSize)));
        }
        return batches;
    }

    /**
     * Calcula el progreso y categorías para un conteo específico
     */
    private JSONObject calcularProgresoYCategoriasPorConteo(String[] productosArray, int codinvalm) {
        EntityManager em = null;
        JSONObject respuesta = new JSONObject();

        // Valores por defecto
        respuesta.put("productosInventariados", 0);
        respuesta.put("totalProductos", productosArray.length);
        respuesta.put("categorias", new JSONArray());

        try {
            if (productosArray == null || productosArray.length == 0) {
                return respuesta;
            }

            em = getEntityManager();
            // Obtener flags hasLista y hasCaptura para este conteo
            boolean hasLista = false;
            boolean hasCaptura = false;

            String sqlFlags = "SELECT i.direccionado, i.captura " +
                    "FROM inventario_almacen ia " +
                    "JOIN inventario i ON ia.codinv = i.codinv " +
                    "WHERE ia.codinvalm = ?";

            Object[] flags = (Object[]) em.createNativeQuery(sqlFlags)
                    .setParameter(1, codinvalm)
                    .getSingleResult();

            hasLista = flags[0] != null && (Boolean) flags[0];
            hasCaptura = flags[1] != null && ((String) flags[1]).equals("S");

            String sql = construirConsultaUnificada(productosArray.length, hasLista, hasCaptura);
            Query query = em.createNativeQuery(sql);

            // Establecer parámetros
            int paramIndex = 1;
            for (String producto : productosArray) {
                query.setParameter(paramIndex++, producto);
            }
            query.setParameter(paramIndex++, codinvalm);
            for (String producto : productosArray) {
                query.setParameter(paramIndex++, producto);
            }

            List<Object[]> resultados = query.getResultList();

            JSONArray categorias = new JSONArray();
            int totalInventariados = 0;

            for (Object[] fila : resultados) {
                if (fila == null || fila.length < 4)
                    continue;

                JSONObject categoria = new JSONObject();

                // Asegurar valores no nulos
                categoria.put("codtip", fila[0] != null ? fila[0].toString() : "");
                categoria.put("nombreTipo", fila[1] != null ? fila[1].toString() : "Sin nombre");

                int total = fila[2] != null ? ((Number) fila[2]).intValue() : 0;
                int inventariados = fila[3] != null ? ((Number) fila[3]).intValue() : 0;

                categoria.put("totalProductos", total);
                categoria.put("productosInventariados", inventariados);

                totalInventariados += inventariados;
                categorias.put(categoria);
            }

            // Actualizar valores en la respuesta
            respuesta.put("productosInventariados", totalInventariados);
            respuesta.put("categorias", categorias);

        } catch (Exception e) {
            System.err.println("Error en calcularProgresoYCategoriasPorConteo: " + e.getMessage());
            // Mantener los valores por defecto ya establecidos
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return respuesta;
    }

    /**
     * Construye la consulta SQL unificada para calcular progreso por categorías
     */
    private String construirConsultaUnificada(int numProductos, boolean hasLista, boolean hasCaptura) {
        StringBuilder sql = new StringBuilder();

        sql.append("WITH productos_totales AS ( ");
        sql.append("  SELECT t.codtip, t.destip, COUNT(DISTINCT p.codpro) as total ");
        sql.append("  FROM fa_tipos t ");
        sql.append("  JOIN fa_productos p ON t.codtip = p.codtip ");
        sql.append("  WHERE p.codpro IN (");
        appendPlaceholders(sql, numProductos);
        sql.append("  ) GROUP BY t.codtip, t.destip ");
        sql.append("), productos_inventariados AS ( ");
        sql.append("  SELECT t.codtip, COUNT(DISTINCT it.codpro) as inventariados ");
        sql.append("  FROM fa_tipos t ");
        sql.append("  JOIN fa_productos p ON t.codtip = p.codtip ");
        sql.append("  JOIN inventario_toma it ON p.codpro = it.codpro ");

        if (hasLista) {
            sql.append(
                    "  JOIN inventario_lista_productos ilp ON it.codpro = ilp.codpro AND it.codinvalm = ilp.codinvalm ");
        }

        if (hasCaptura) {
            sql.append("  JOIN capturastocksinventario c ON it.codpro = c.codpro ");
            sql.append("  JOIN inventario_almacen ia ON ia.codinvcab = c.codinvcab AND it.codinvalm = ia.codinvalm ");
        } else {
            sql.append("  JOIN fa_stock_vencimientos fs ON it.codpro = fs.codpro ");
            sql.append("  JOIN inventario_almacen ia ON ia.codalm = fs.codalm AND it.codinvalm = ia.codinvalm ");
        }

        sql.append("  WHERE it.codinvalm = ? ");
        sql.append("  AND it.coddeta IS NULL ");
        sql.append("  AND p.codpro IN (");
        appendPlaceholders(sql, numProductos);
        sql.append("  ) ");

        if (!hasCaptura) {
            sql.append("  AND fs.fecven > CONVERT(date, GETDATE()) ");
        }

        sql.append("  GROUP BY t.codtip ");
        sql.append(") ");
        sql.append("SELECT pt.codtip, pt.destip, pt.total, ");
        sql.append("COALESCE(pi.inventariados, 0) as inventariados ");
        sql.append("FROM productos_totales pt ");
        sql.append("LEFT JOIN productos_inventariados pi ON pt.codtip = pi.codtip");

        return sql.toString();
    }

    /**
     * Procesa los resultados de la consulta de progreso por categorías
     */
    private JSONObject procesarResultadosUnificados(List<Object[]> resultados, int totalProductos) {
        JSONObject respuesta = new JSONObject();
        JSONArray categorias = new JSONArray();
        int totalInventariados = 0;

        for (Object[] fila : resultados) {
            JSONObject categoria = new JSONObject();
            categoria.put("codtip", fila[0].toString());
            categoria.put("nombreTipo", fila[1].toString());
            categoria.put("totalProductos", ((Number) fila[2]).intValue());
            categoria.put("productosInventariados", ((Number) fila[3]).intValue());

            totalInventariados += ((Number) fila[3]).intValue();
            categorias.put(categoria);
        }

        double progreso = totalProductos > 0 ? (totalInventariados * 100.0) / totalProductos : 0.0;
        respuesta.put("progreso", progreso);
        respuesta.put("categorias", categorias);
        return respuesta;
    }

    /**
     * Valida si los arrays de productos o conteos están vacíos
     */
    private boolean validarArraysVacios(Integer[] productosArray, Integer[] codinvalmArray) {
        return productosArray == null || productosArray.length == 0 ||
                codinvalmArray == null || codinvalmArray.length == 0;
    }

    /**
     * Retorna un objeto JSON con valores por defecto para casos de error
     */
    private JSONObject resultadoConValoresPorDefecto() {
        JSONObject resultado = new JSONObject();
        resultado.put("progreso", 0.0);
        resultado.put("categorias", new JSONArray());
        return resultado;
    }

    /**
     * Calcula el promedio de porcentajes
     */
    private double calcularPromedio(double suma, int divisor) {
        return divisor > 0 ? suma / divisor : 0.0;
    }

    /**
     * Agrega placeholders (?) a una consulta SQL
     */
    private void appendPlaceholders(StringBuilder sb, int count) {
        for (int i = 0; i < count; i++) {
            if (i > 0)
                sb.append(", ");
            sb.append("?");
        }
    }

    /**
     * Consolida categorías de múltiples conteos
     */
    private void consolidarCategoriasInAux(JSONArray todasCategorias, JSONArray categoriasConteo) {
        if (categoriasConteo == null)
            return;

        for (int i = 0; i < categoriasConteo.length(); i++) {
            JSONObject categoria = categoriasConteo.getJSONObject(i);
            String codtip = categoria.getString("codtip");
            boolean encontrada = false;

            for (int j = 0; j < todasCategorias.length(); j++) {
                JSONObject catExistente = todasCategorias.getJSONObject(j);
                if (catExistente.getString("codtip").equals(codtip)) {
                    // Consolidar todos los campos relevantes
                    catExistente.put("productosInventariados",
                            catExistente.getInt("productosInventariados") +
                                    categoria.getInt("productosInventariados"));

                    catExistente.put("totalProductos",
                            catExistente.getInt("totalProductos") +
                                    categoria.getInt("totalProductos"));

                    // Mantener los datos descriptivos de la primera ocurrencia
                    if (!catExistente.has("nombreTipo") && categoria.has("nombreTipo")) {
                        catExistente.put("nombreTipo", categoria.getString("nombreTipo"));
                    }

                    encontrada = true;
                    break;
                }
            }

            if (!encontrada) {
                // Asegurar que la nueva categoría tiene todos los campos necesarios
                JSONObject nuevaCategoria = new JSONObject();
                nuevaCategoria.put("codtip", categoria.getString("codtip"));
                nuevaCategoria.put("nombreTipo", categoria.optString("nombreTipo", ""));
                nuevaCategoria.put("productosInventariados", categoria.getInt("productosInventariados"));
                nuevaCategoria.put("totalProductos", categoria.getInt("totalProductos"));
                todasCategorias.put(nuevaCategoria);
            }
        }
    }

    /**
     * Calcula los porcentajes finales de avance por categoría
     */
    private JSONArray consolidarCategoriasFinal(JSONArray todasCategorias) {
        JSONArray resultado = new JSONArray();

        for (int i = 0; i < todasCategorias.length(); i++) {
            JSONObject cat = todasCategorias.getJSONObject(i);

            // Validar que tenemos los datos necesarios
            if (!cat.has("totalProductos") || !cat.has("productosInventariados")) {
                continue;
            }

            int total = cat.getInt("totalProductos");
            int inventariados = cat.getInt("productosInventariados");

            // Cálculo seguro del porcentaje
            double porcentaje = 0.0;
            if (total > 0) {
                porcentaje = (inventariados * 100.0) / total;
                // Redondear a 2 decimales
                porcentaje = Math.round(porcentaje * 100.0) / 100.0;
            }

            // Actualizar objeto categoría
            cat.put("porcentajeAvance", porcentaje);
            cat.put("pendientes", total - inventariados);

            // Validar campos requeridos
            if (!cat.has("nombreTipo")) {
                cat.put("nombreTipo", "Categoría " + cat.getString("codtip"));
            }

            resultado.put(cat);
        }

        return resultado;
    }

    /**
     * Obtiene el nombre de un almacén por su código
     */
    public String obtenerNombreAlmacen(int codalm) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sql = "SELECT desalm FROM fa_almacenes WHERE codalm = ?";
            Query query = em.createNativeQuery(sql)
                    .setParameter(1, codalm);
            String nombreAlmacen = (String) query.getSingleResult();
            return nombreAlmacen != null ? nombreAlmacen : "Almacén no encontrado";
        } catch (NoResultException e) {
            return "Almacén no encontrado";
        } catch (NonUniqueResultException e) {
            System.err.println("Error: múltiples resultados para codalm: " + codalm);
            return "Error: múltiples almacenes";
        } catch (Exception e) {
            System.err.println("Error al obtener nombre de almacén: " + e.getMessage());
            return "Error al consultar";
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}