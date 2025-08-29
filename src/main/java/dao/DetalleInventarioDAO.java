package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.json.JSONArray;
import org.json.JSONObject;

import dto.InventarioAlmacen;

public class DetalleInventarioDAO extends JpaPadre {
    public DetalleInventarioDAO(String empresa) {
        super(empresa);
    }

    InventarioAlmacenJpaController dao = new InventarioAlmacenJpaController("a");

    public JSONArray obtenerCategoriasPorFarmacia(String codalm, int codinv) {
        EntityManager em = null;
        JSONArray arrayCategorias = new JSONArray();

        try {
            em = getEntityManager();
            String sql = "SELECT DISTINCT c.codcat, c.descat " +
                    "FROM inventario_almacen ia " +
                    "JOIN fa_almacenes a ON ia.codalm = a.codalm " +
                    "JOIN fa_categoria c ON c.codcat = ia.codcat " +
                    "WHERE ia.codinv = ? AND a.codalm = ? AND a.estado = 'S' " +
                    "ORDER BY c.descat";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, codinv);
            query.setParameter(2, codalm);
            List<Object[]> resultados = query.getResultList();

            for (Object[] fila : resultados) {
                if (fila != null && fila.length == 2) {
                    JSONObject categoria = new JSONObject();
                    categoria.put("codcat", fila[0]);
                    categoria.put("nombre", fila[1]);
                    arrayCategorias.put(categoria);
                }
            }

        } catch (Exception e) {
            // En caso de error, devolver arrays vacíos
            arrayCategorias.put(new JSONArray());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return arrayCategorias;
    }

    public JSONArray obtenerCodinvalmsSeparados(int codinv) {
        EntityManager em = null;
        JSONArray arrayCodinvalm = new JSONArray();

        try {
            em = getEntityManager();
            String sql = "SELECT codinvalm FROM inventario_almacen WHERE codinv = ?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, codinv);
            List<Object> resultados = query.getResultList();

            for (Object fila : resultados) {
                if (fila != null) {
                    arrayCodinvalm.put(fila.toString());
                }
            }

        } catch (Exception e) {
            // En caso de error, devolver arrays vacíos
            arrayCodinvalm.put(new JSONArray());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return arrayCodinvalm;
    }

    public JSONArray obtenerFarmacias(int codinv) {
        EntityManager em = null;
        JSONArray arrayFarmacias = new JSONArray();

        try {
            em = getEntityManager();
            String sql = "SELECT DISTINCT a.codalm, a.desalm " +
                    "FROM inventario_almacen ia " +
                    "JOIN fa_almacenes a ON ia.codalm = a.codalm " +
                    "WHERE ia.codinv = ? " +
                    "and a.estado = 'S' " +
                    "ORDER BY a.desalm";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, codinv);
            List<Object[]> resultados = query.getResultList();

            for (Object[] fila : resultados) {
                if (fila != null && fila.length == 2) {
                    JSONObject farmacia = new JSONObject();
                    farmacia.put("codalm", fila[0]);
                    farmacia.put("nombre", fila[1]);
                    arrayFarmacias.put(farmacia);
                }
            }

        } catch (Exception e) {
            // En caso de error, devolver arrays vacíos
            arrayFarmacias.put(new JSONArray());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return arrayFarmacias;
    }

    public int obtenerConteosPorFarmacia(String codalm, int codinv) {
        EntityManager em = null;
        int conteos = 0;

        try {
            em = getEntityManager();
            String sql = "SELECT COUNT(*) FROM inventario_almacen WHERE codalm = ? AND codinv = ?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, codalm);
            query.setParameter(2, codinv);
            conteos = ((Number) query.getSingleResult()).intValue();

        } catch (Exception e) {
            // En caso de error, devolver 0
            conteos = 0;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return conteos;
    }

    public JSONArray obtenerCodinvalmsPorFarmacia(String codalm, int codinv) {
        EntityManager em = null;
        JSONArray arrayCodinvalm = new JSONArray();

        try {
            em = getEntityManager();
            String sql = "SELECT DISTINCT codinvalm FROM inventario_almacen WHERE codalm = ? AND codinv = ?";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, codalm);
            query.setParameter(2, codinv);
            List<Object> resultados = query.getResultList();

            for (Object fila : resultados) {
                if (fila != null) {
                    arrayCodinvalm.put(fila.toString());
                }
            }

        } catch (Exception e) {
            // En caso de error, devolver arrays vacíos
            arrayCodinvalm.put(new JSONArray());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return arrayCodinvalm;
    }

    public int obtenerTotalProductosPorFarmacia(String codalm, int codinv) {
        EntityManager em = null;
        int totalProductos = 0;

        try {
            em = getEntityManager();
            JSONArray arrayCodinvalm = obtenerCodinvalmsPorFarmacia(codalm, codinv);

            // Convertir JSONArray a lista de Strings
            List<String> codinvalms = new ArrayList<String>();
            for (int i = 0; i < arrayCodinvalm.length(); i++) {
                codinvalms.add(arrayCodinvalm.getString(i));
            }

            if (!codinvalms.isEmpty()) {
                // Crear la parte IN con parámetros dinámicos
                StringBuilder sql = new StringBuilder(
                        "SELECT COUNT(distinct codpro) FROM inventario_lista_productos WHERE codinvalm IN (");

                // Agregar marcadores de posición (?)
                for (int i = 0; i < codinvalms.size(); i++) {
                    if (i > 0) {
                        sql.append(",");
                    }
                    sql.append("?");
                }
                sql.append(")");

                Query query = em.createNativeQuery(sql.toString());

                // Establecer cada parámetro individualmente
                for (int i = 0; i < codinvalms.size(); i++) {
                    query.setParameter(i + 1, codinvalms.get(i));
                }

                Object result = query.getSingleResult();
                totalProductos = result != null ? ((Number) result).intValue() : 0;
            }

        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
            totalProductos = 0;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return totalProductos;
    }

    public int obtenerTotalProductosContabilizadosPorFarmacia(String codalm, int codinv, boolean tieneCaptura) {
        EntityManager em = null;
        int totalProductosContabilizados = 0;

        try {
            em = getEntityManager();
            JSONArray arrayCodinvalm = obtenerCodinvalmsPorFarmacia(codalm, codinv);

            if (arrayCodinvalm == null || arrayCodinvalm.length() == 0) {
                return 0;
            }

            // Convertir JSONArray a lista de Strings
            List<String> codinvalms = new ArrayList<String>();
            for (int i = 0; i < arrayCodinvalm.length(); i++) {
                if (!arrayCodinvalm.isNull(i)) {
                    codinvalms.add(arrayCodinvalm.getString(i));
                }
            }

            if (codinvalms.isEmpty()) {
                return 0;
            }

            // Construir la consulta base
            String baseQuery = tieneCaptura ? "SELECT COUNT(DISTINCT it.codpro) FROM inventario_toma it " +
                    "INNER JOIN inventario_almacen ia ON ia.codinvalm = it.codinvalm " +
                    "INNER JOIN capturastocksinventario c ON it.codpro = c.codpro AND c.codinvcab = ia.codinvcab " +
                    "WHERE (it.tome > 0 OR it.tomf > 0) AND (c.cante > 0 OR c.cantf > 0) AND ia.codinvalm IN (" :

                    "SELECT COUNT(DISTINCT it.codpro) FROM inventario_toma it " +
                            "INNER JOIN inventario_almacen ia ON ia.codinvalm = it.codinvalm " +
                            "INNER JOIN fa_stock_vencimientos fs ON it.codpro = fs.codpro AND fs.codalm = ia.codalm " +
                            "WHERE (it.tome > 0 OR it.tomf > 0) AND (fs.qtymov > 0 OR fs.qtymov_m > 0) " +
                            "AND fs.fecven > CONVERT(DATE, GETDATE()) AND ia.codinvalm IN (";

            // Construir la consulta completa
            StringBuilder sql = new StringBuilder(baseQuery);
            StringJoiner placeholders = new StringJoiner(",");
            for (int i = 0; i < codinvalms.size(); i++) {
                placeholders.add("?");
            }
            sql.append(placeholders).append(")");

            // Ejecutar la consulta
            Query query = em.createNativeQuery(sql.toString());
            for (int i = 0; i < codinvalms.size(); i++) {
                query.setParameter(i + 1, codinvalms.get(i));
            }

            Object result = query.getSingleResult();
            totalProductosContabilizados = result != null ? ((Number) result).intValue() : 0;

        } catch (Exception e) {
            totalProductosContabilizados = 0;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return totalProductosContabilizados;
    }

    public int obtenerTotalProductosConStockPorFarmacia(String codalm, int codinv, boolean tieneCaptura) {
        EntityManager em = null;
        int totalProductosConStock = 0;

        try {
            em = getEntityManager();
            JSONArray arrayCodinvalm = obtenerCodinvalmsPorFarmacia(codalm, codinv);

            // Validación temprana
            if (arrayCodinvalm == null || arrayCodinvalm.length() == 0) {
                return 0;
            }

            // Convertir JSONArray a lista de Strings
            List<String> codinvalms = new ArrayList<String>();
            for (int i = 0; i < arrayCodinvalm.length(); i++) {
                if (!arrayCodinvalm.isNull(i)) {
                    codinvalms.add(arrayCodinvalm.getString(i));
                }
            }

            if (codinvalms.isEmpty()) {
                return 0;
            }

            // Construir la consulta base
            String baseQuery = tieneCaptura
                    ? "SELECT COUNT(DISTINCT il.codpro) FROM inventario_lista_productos il " +
                            "INNER JOIN capturastocksinventario c ON il.codpro = c.codpro " +
                            "INNER JOIN inventario_almacen ia ON ia.codinvcab = c.codinvcab " +
                            "WHERE (c.cante > 0 OR c.cantf > 0) AND ia.codinvalm IN ("
                    : "SELECT COUNT(DISTINCT il.codpro) FROM inventario_lista_productos il " +
                            "INNER JOIN fa_stock_vencimientos fs ON il.codpro = fs.codpro " +
                            "INNER JOIN inventario_almacen ia ON ia.codalm = fs.codalm " +
                            "WHERE (fs.qtymov > 0 OR fs.qtymov_m > 0) " +
                            "AND fs.fecven > CONVERT(DATE, GETDATE()) AND ia.codinvalm IN (";

            // Construir la consulta completa
            StringBuilder sql = new StringBuilder(baseQuery);

            // Primer IN clause (ia.codinvalm)
            StringJoiner placeholders1 = new StringJoiner(",");
            for (int i = 0; i < codinvalms.size(); i++) {
                placeholders1.add("?");
            }
            sql.append(placeholders1).append(")");

            // Segundo IN clause (il.codinvalm)
            sql.append(" AND il.codinvalm IN (");
            StringJoiner placeholders2 = new StringJoiner(",");
            for (int i = 0; i < codinvalms.size(); i++) {
                placeholders2.add("?");
            }
            sql.append(placeholders2).append(")");

            // Ejecutar la consulta
            Query query = em.createNativeQuery(sql.toString());

            // Establecer parámetros para el primer IN
            int paramIndex = 1;
            for (String codinvalm : codinvalms) {
                query.setParameter(paramIndex++, codinvalm);
            }

            // Establecer parámetros para el segundo IN
            for (String codinvalm : codinvalms) {
                query.setParameter(paramIndex++, codinvalm);
            }

            Object result = query.getSingleResult();
            totalProductosConStock = result != null ? ((Number) result).intValue() : 0;

        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
            totalProductosConStock = 0;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return totalProductosConStock;
    }

    public JSONArray detallesPorConteo(int codinv, String codalm, boolean tieneCaptura) {
        JSONArray jsonDetalles = new JSONArray();
        JSONArray conteosArray = obtenerCodinvalmsPorFarmacia(codalm, codinv);

        // ? convertir JSONArray a lista de Strings
        List<String> codinvalms = new ArrayList<String>();
        for (int i = 0; i < conteosArray.length(); i++) {
            codinvalms.add(conteosArray.getString(i));
        }

        for (String codinvalm : codinvalms) {
            JSONObject resultado = new JSONObject();
            resultado.put("codinvalm", codinvalm);
            int codinvalmInt = Integer.parseInt(codinvalm);
            // ? obtenemos el objeto inventarioalmacen
            InventarioAlmacen inventarioAlmacen = dao.findInventarioAlmacen(codinvalmInt);
            resultado.put("nombreConteo", inventarioAlmacen.getDesinvalm());
            resultado.put("estado", inventarioAlmacen.getEstdet());
            // ? obtenemos el total de productos contabilizados por este conteo
            int totalProductosContabilizados = obtenerTotalProductosContabilizadosPorConteo(codinvalm, tieneCaptura);
            resultado.put("totalItemsContabilizados", totalProductosContabilizados);
            // ? obtenemos el total de productos en este conteo
            int totalProductos = obtenerTotalProductosPorConteo(codinvalm, tieneCaptura);
            resultado.put("totalItemsGeneral", totalProductos);
            // ? obtenemos el total de productos con stock por este conteo
            int totalProductosConStock = obtenerTotalProductosConStockPorConteo(codinvalm, tieneCaptura);
            resultado.put("totalItemsConStock", totalProductosConStock);
            // ? calculamos el progreso
            double progreso = (double) totalProductosContabilizados / (double) totalProductos * 100;
            progreso = Math.round(progreso * 100.0) / 100.0;
            resultado.put("progresoConteo", progreso);
            // ? obtenemos el detalle por categoria de este conteo
            JSONArray categorias = detallesPorCategoriaPorConteo(codinvalm, tieneCaptura);
            resultado.put("categorias", categorias);
            jsonDetalles.put(resultado);
        }

        return jsonDetalles;
    }

    public int obtenerTotalProductosContabilizadosPorConteo(String codinvalm, boolean tieneCaptura) {
        EntityManager em = null;
        int totalProductosContabilizados = 0;

        try {
            em = getEntityManager();

            // Construir la consulta base
            String baseQuery = tieneCaptura ? "SELECT COUNT(DISTINCT it.codpro) FROM inventario_toma it " +
                    "INNER JOIN inventario_almacen ia ON ia.codinvalm = it.codinvalm " +
                    "INNER JOIN capturastocksinventario c ON it.codpro = c.codpro AND c.codinvcab = ia.codinvcab " +
                    "WHERE (it.tome > 0 OR it.tomf > 0) AND (c.cante > 0 OR c.cantf > 0) AND ia.codinvalm = ? " :

                    "SELECT COUNT(DISTINCT it.codpro) FROM inventario_toma it " +
                            "INNER JOIN inventario_almacen ia ON ia.codinvalm = it.codinvalm " +
                            "INNER JOIN fa_stock_vencimientos fs ON it.codpro = fs.codpro AND fs.codalm = ia.codalm " +
                            "WHERE (it.tome > 0 OR it.tomf > 0) AND (fs.qtymov > 0 OR fs.qtymov_m > 0) " +
                            "AND fs.fecven > CONVERT(DATE, GETDATE()) AND ia.codinvalm = ?";

            // Construir la consulta completa
            StringBuilder sql = new StringBuilder(baseQuery);

            // Ejecutar la consulta
            Query query = em.createNativeQuery(sql.toString());
            query.setParameter(1, codinvalm);

            Object result = query.getSingleResult();
            totalProductosContabilizados = result != null ? ((Number) result).intValue() : 0;

        } catch (Exception e) {
            totalProductosContabilizados = 0;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return totalProductosContabilizados;
    }

    public int obtenerTotalProductosPorConteo(String codinvalm, boolean tieneCaptura) {
        EntityManager em = null;
        int totalProductos = 0;

        try {
            em = getEntityManager();
            StringBuilder sql = new StringBuilder(
                    "SELECT COUNT(distinct codpro) FROM inventario_lista_productos WHERE codinvalm = ?");

            Query query = em.createNativeQuery(sql.toString());
            query.setParameter(1, codinvalm);

            Object result = query.getSingleResult();
            totalProductos = result != null ? ((Number) result).intValue() : 0;

        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
            totalProductos = 0;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return totalProductos;
    }

    public int obtenerTotalProductosConStockPorConteo(String codinvalm, boolean tieneCaptura) {
        EntityManager em = null;
        int totalProductosConStock = 0;

        try {
            em = getEntityManager();

            // Construir la consulta base
            String baseQuery = tieneCaptura
                    ? "SELECT COUNT(DISTINCT il.codpro) FROM inventario_lista_productos il " +
                            "INNER JOIN capturastocksinventario c ON il.codpro = c.codpro " +
                            "INNER JOIN inventario_almacen ia ON ia.codinvcab = c.codinvcab " +
                            "WHERE (c.cante > 0 OR c.cantf > 0) AND ia.codinvalm = ? AND il.codinvalm = ?"
                    : "SELECT COUNT(DISTINCT il.codpro) FROM inventario_lista_productos il " +
                            "INNER JOIN fa_stock_vencimientos fs ON il.codpro = fs.codpro " +
                            "INNER JOIN inventario_almacen ia ON ia.codalm = fs.codalm " +
                            "WHERE (fs.qtymov > 0 OR fs.qtymov_m > 0) " +
                            "AND fs.fecven > CONVERT(DATE, GETDATE()) AND ia.codinvalm = ? AND il.codinvalm = ?";

            // Construir la consulta completa
            StringBuilder sql = new StringBuilder(baseQuery);

            // Ejecutar la consulta
            Query query = em.createNativeQuery(sql.toString());

            // Establecer parámetros
            query.setParameter(1, codinvalm);
            query.setParameter(2, codinvalm);

            Object result = query.getSingleResult();
            totalProductosConStock = result != null ? ((Number) result).intValue() : 0;

        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
            totalProductosConStock = 0;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return totalProductosConStock;
    }

    public JSONArray detallesPorCategoriaPorFarmacia(String codalm, int codinv, boolean tieneCaptura) {
        JSONArray resultados = new JSONArray();

        try {
            // 1. Obtener todos los productos asociados a la farmacia
            List<String> todosProductosConStock = obtenerProductosConStockPorFarmacia(codalm, codinv, tieneCaptura);
            if (todosProductosConStock.isEmpty()) {
                return resultados;
            }

            // 2. Obtener productos ya contabilizados/inventariados
            List<String> productosContabilizados = obtenerProductosContabilizadosPorFarmacia(codalm, codinv,
                    tieneCaptura);

            // 3. Obtener las categorías con su conteo total de productos con stock
            JSONArray categorias = obtenerCategoriasPorLista(todosProductosConStock);

            // 4. Obtener las categorías de productos contabilizados
            JSONArray categoriasContabilizadas = new JSONArray();
            if (!productosContabilizados.isEmpty()) {
                categoriasContabilizadas = obtenerCategoriasPorLista(productosContabilizados);
            }

            // 5. Combinar los resultados
            for (int i = 0; i < categorias.length(); i++) {
                JSONObject categoria = categorias.getJSONObject(i);
                String codtip = categoria.getString("codtip");
                String nombreTipo = categoria.getString("destip");
                int totalProductos = categoria.getInt("cantidad");

                // Buscar si hay productos contabilizados para esta categoría
                int productosInventariados = 0;
                for (int j = 0; j < categoriasContabilizadas.length(); j++) {
                    JSONObject categoriaContabilizada = categoriasContabilizadas.getJSONObject(j);
                    if (codtip.equals(categoriaContabilizada.getString("codtip"))) {
                        productosInventariados = categoriaContabilizada.getInt("cantidad");
                        break;
                    }
                }

                // Calcular porcentaje de avance
                double porcentajeAvance = totalProductos > 0 ? (double) productosInventariados / totalProductos * 100
                        : 0;

                // Crear objeto de resultado
                JSONObject detalle = new JSONObject();
                detalle.put("codtip", codtip);
                detalle.put("nombreTipo", nombreTipo);
                detalle.put("totalProductos", totalProductos);
                detalle.put("productosInventariados", productosInventariados);
                detalle.put("porcentajeAvance", Math.round(porcentajeAvance * 100) / 100.0); // Redondear a 2 decimales

                resultados.put(detalle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultados;
    }

    public JSONArray obtenerCategoriasPorLista(List<String> productos) {
        EntityManager em = null;
        JSONArray resultados = new JSONArray();

        if (productos == null || productos.isEmpty()) {
            return resultados;
        }

        try {
            em = getEntityManager(); // Asumiendo que tienes un método getEntityManager como en los otros métodos

            // Limitar el tamaño de los lotes para evitar problemas con consultas muy
            // grandes
            int batchSize = 500;
            int totalBatches = (int) Math.ceil(productos.size() / (double) batchSize);

            // Procesar en lotes si hay muchos productos
            for (int batch = 0; batch < totalBatches; batch++) {
                int fromIndex = batch * batchSize;
                int toIndex = Math.min((batch + 1) * batchSize, productos.size());
                List<String> batchProductos = productos.subList(fromIndex, toIndex);

                StringBuilder sql = new StringBuilder(
                        "SELECT t.codtip, t.destip, COUNT(p.codpro) AS cantidad " +
                                "FROM fa_tipos t " +
                                "INNER JOIN fa_productos p ON t.codtip = p.codtip " +
                                "WHERE p.codpro IN (");

                StringJoiner placeholders = new StringJoiner(",");
                for (int i = 0; i < batchProductos.size(); i++) {
                    placeholders.add("?");
                }

                sql.append(placeholders).append(")");
                sql.append(" GROUP BY t.codtip, t.destip");

                Query query = em.createNativeQuery(sql.toString());

                // Establecer cada parámetro individualmente
                for (int i = 0; i < batchProductos.size(); i++) {
                    query.setParameter(i + 1, batchProductos.get(i));
                }

                // La consulta devuelve múltiples filas
                List<Object[]> results = query.getResultList();

                // Convertir los resultados a JSONArray
                for (Object[] row : results) {
                    // Verificar si ya existe un objeto con el mismo codtip
                    boolean encontrado = false;
                    for (int i = 0; i < resultados.length(); i++) {
                        JSONObject existente = resultados.getJSONObject(i);
                        if (existente.getString("codtip").equals(row[0].toString())) {
                            // Si existe, sumar la cantidad
                            int cantidadExistente = existente.getInt("cantidad");
                            int cantidadNueva = ((Number) row[2]).intValue();
                            existente.put("cantidad", cantidadExistente + cantidadNueva);
                            encontrado = true;
                            break;
                        }
                    }

                    // Si no existe, crear un nuevo objeto
                    if (!encontrado) {
                        JSONObject resultado = new JSONObject();
                        resultado.put("codtip", row[0].toString());
                        resultado.put("destip", row[1].toString());
                        resultado.put("cantidad", ((Number) row[2]).intValue());
                        resultados.put(resultado);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return resultados;
    }

    public List<String> obtenerProductosConStockPorFarmacia(String codalm, int codinv, boolean tieneCaptura) {
        EntityManager em = null;
        List<String> productosConStock = new ArrayList<>();

        try {
            em = getEntityManager();
            JSONArray arrayCodinvalm = obtenerCodinvalmsPorFarmacia(codalm, codinv);

            // Validación temprana
            if (arrayCodinvalm == null || arrayCodinvalm.length() == 0) {
                return productosConStock;
            }

            // Convertir JSONArray a lista de Strings
            List<String> codinvalms = new ArrayList<>();
            for (int i = 0; i < arrayCodinvalm.length(); i++) {
                if (!arrayCodinvalm.isNull(i)) {
                    codinvalms.add(arrayCodinvalm.getString(i));
                }
            }

            if (codinvalms.isEmpty()) {
                return productosConStock;
            }

            // Construir la consulta base
            String baseQuery = tieneCaptura
                    ? "SELECT DISTINCT il.codpro FROM inventario_lista_productos il " +
                            "INNER JOIN capturastocksinventario c ON il.codpro = c.codpro " +
                            "INNER JOIN inventario_almacen ia ON ia.codinvcab = c.codinvcab " +
                            "WHERE (c.cante > 0 OR c.cantf > 0) AND ia.codinvalm IN ("
                    : "SELECT DISTINCT il.codpro FROM inventario_lista_productos il " +
                            "INNER JOIN fa_stock_vencimientos fs ON il.codpro = fs.codpro " +
                            "INNER JOIN inventario_almacen ia ON ia.codalm = fs.codalm " +
                            "WHERE (fs.qtymov > 0 OR fs.qtymov_m > 0) " +
                            "AND fs.fecven > CONVERT(DATE, GETDATE()) AND ia.codinvalm IN (";

            // Construir la consulta completa
            StringBuilder sql = new StringBuilder(baseQuery);

            // Primer IN clause (ia.codinvalm)
            StringJoiner placeholders1 = new StringJoiner(",");
            for (int i = 0; i < codinvalms.size(); i++) {
                placeholders1.add("?");
            }
            sql.append(placeholders1).append(")");

            // Segundo IN clause (il.codinvalm)
            sql.append(" AND il.codinvalm IN (");
            StringJoiner placeholders2 = new StringJoiner(",");
            for (int i = 0; i < codinvalms.size(); i++) {
                placeholders2.add("?");
            }
            sql.append(placeholders2).append(")");

            // Ejecutar la consulta
            Query query = em.createNativeQuery(sql.toString());

            // Establecer parámetros para el primer IN
            int paramIndex = 1;
            for (String codinvalm : codinvalms) {
                query.setParameter(paramIndex++, codinvalm);
            }

            // Establecer parámetros para el segundo IN
            for (String codinvalm : codinvalms) {
                query.setParameter(paramIndex++, codinvalm);
            }

            // Obtener resultados como lista de códigos de producto
            List<Object> resultados = query.getResultList();
            for (Object codpro : resultados) {
                if (codpro != null) {
                    productosConStock.add(codpro.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return productosConStock;
    }

    public List<String> obtenerProductosPorFarmacia(String codalm, int codinv) {
        EntityManager em = null;
        List<String> listaProductos = new ArrayList<>();

        try {
            em = getEntityManager();
            JSONArray arrayCodinvalm = obtenerCodinvalmsPorFarmacia(codalm, codinv);

            // Validación temprana
            if (arrayCodinvalm == null || arrayCodinvalm.length() == 0) {
                return listaProductos;
            }

            // Convertir JSONArray a lista de Strings
            List<String> codinvalms = new ArrayList<>();
            for (int i = 0; i < arrayCodinvalm.length(); i++) {
                if (!arrayCodinvalm.isNull(i)) {
                    codinvalms.add(arrayCodinvalm.getString(i));
                }
            }

            if (codinvalms.isEmpty()) {
                return listaProductos;
            }

            // Construir la consulta SQL
            StringBuilder sql = new StringBuilder(
                    "SELECT DISTINCT codpro FROM inventario_lista_productos WHERE codinvalm IN (");

            // Agregar marcadores de posición
            StringJoiner placeholders = new StringJoiner(",");
            for (int i = 0; i < codinvalms.size(); i++) {
                placeholders.add("?");
            }
            sql.append(placeholders.toString()).append(")");

            // Crear y ejecutar la consulta
            Query query = em.createNativeQuery(sql.toString());

            // Establecer parámetros
            for (int i = 0; i < codinvalms.size(); i++) {
                query.setParameter(i + 1, codinvalms.get(i));
            }

            // Obtener resultados
            List<Object> resultados = query.getResultList();
            for (Object item : resultados) {
                if (item != null) {
                    listaProductos.add(item.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return listaProductos;
    }

    public List<String> obtenerProductosContabilizadosPorFarmacia(String codalm, int codinv, boolean tieneCaptura) {
        EntityManager em = null;
        List<String> productosContabilizados = new ArrayList<>();

        try {
            em = getEntityManager();
            JSONArray arrayCodinvalm = obtenerCodinvalmsPorFarmacia(codalm, codinv);

            // Validación temprana
            if (arrayCodinvalm == null || arrayCodinvalm.length() == 0) {
                return productosContabilizados;
            }

            // Convertir JSONArray a lista de Strings
            List<String> codinvalms = new ArrayList<>();
            for (int i = 0; i < arrayCodinvalm.length(); i++) {
                if (!arrayCodinvalm.isNull(i)) {
                    codinvalms.add(arrayCodinvalm.getString(i));
                }
            }

            if (codinvalms.isEmpty()) {
                return productosContabilizados;
            }

            // Construir la consulta base
            String baseQuery = tieneCaptura
                    ? "SELECT DISTINCT it.codpro FROM inventario_toma it " +
                            "INNER JOIN inventario_almacen ia ON ia.codinvalm = it.codinvalm " +
                            "INNER JOIN capturastocksinventario c ON it.codpro = c.codpro AND c.codinvcab = ia.codinvcab "
                            +
                            "WHERE (it.tome > 0 OR it.tomf > 0) AND (c.cante > 0 OR c.cantf > 0) AND ia.codinvalm IN ("
                    : "SELECT DISTINCT it.codpro FROM inventario_toma it " +
                            "INNER JOIN inventario_almacen ia ON ia.codinvalm = it.codinvalm " +
                            "INNER JOIN fa_stock_vencimientos fs ON it.codpro = fs.codpro AND fs.codalm = ia.codalm " +
                            "WHERE (it.tome > 0 OR it.tomf > 0) AND (fs.qtymov > 0 OR fs.qtymov_m > 0) " +
                            "AND fs.fecven > CONVERT(DATE, GETDATE()) AND ia.codinvalm IN (";

            // Construir la consulta completa
            StringBuilder sql = new StringBuilder(baseQuery);
            StringJoiner placeholders = new StringJoiner(",");
            for (int i = 0; i < codinvalms.size(); i++) {
                placeholders.add("?");
            }
            sql.append(placeholders).append(")");

            // Ejecutar la consulta
            Query query = em.createNativeQuery(sql.toString());
            for (int i = 0; i < codinvalms.size(); i++) {
                query.setParameter(i + 1, codinvalms.get(i));
            }

            // Obtener resultados como lista de códigos de producto
            List<Object> resultados = query.getResultList();
            for (Object item : resultados) {
                if (item != null) {
                    productosContabilizados.add(item.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return productosContabilizados;
    }

    public JSONArray detallesPorCategoriaPorConteo(String codinvalm, boolean tieneCaptura) {
        JSONArray resultados = new JSONArray();

        try {
            // 1. Obtener todos los productos asociados a la farmacia
            List<String> todosProductosConStock = obtenerProductosConStockPorConteo(codinvalm, tieneCaptura);
            if (todosProductosConStock.isEmpty()) {
                return resultados;
            }

            // 2. Obtener productos ya contabilizados/inventariados
            List<String> productosContabilizados = obtenerProductosContabilizadosPorConteo(codinvalm, tieneCaptura);

            // 3. Obtener las categorías con su conteo total de productos con stock
            JSONArray categorias = obtenerCategoriasPorLista(todosProductosConStock);

            // 4. Obtener las categorías de productos contabilizados
            JSONArray categoriasContabilizadas = new JSONArray();
            if (!productosContabilizados.isEmpty()) {
                categoriasContabilizadas = obtenerCategoriasPorLista(productosContabilizados);
            }

            // 5. Combinar los resultados
            for (int i = 0; i < categorias.length(); i++) {
                JSONObject categoria = categorias.getJSONObject(i);
                String codtip = categoria.getString("codtip");
                String nombreTipo = categoria.getString("destip");
                int totalProductos = categoria.getInt("cantidad");

                // Buscar si hay productos contabilizados para esta categoría
                int productosInventariados = 0;
                for (int j = 0; j < categoriasContabilizadas.length(); j++) {
                    JSONObject categoriaContabilizada = categoriasContabilizadas.getJSONObject(j);
                    if (codtip.equals(categoriaContabilizada.getString("codtip"))) {
                        productosInventariados = categoriaContabilizada.getInt("cantidad");
                        break;
                    }
                }

                // Calcular porcentaje de avance
                double porcentajeAvance = totalProductos > 0 ? (double) productosInventariados / totalProductos * 100
                        : 0;

                // Crear objeto de resultado
                JSONObject detalle = new JSONObject();
                detalle.put("codtip", codtip);
                detalle.put("nombreTipo", nombreTipo);
                detalle.put("totalProductos", totalProductos);
                detalle.put("productosInventariados", productosInventariados);
                detalle.put("porcentajeAvance", Math.round(porcentajeAvance * 100) / 100.0); // Redondear a 2 decimales

                resultados.put(detalle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultados;
    }

    public List<String> obtenerProductosContabilizadosPorConteo(String codinvalm, boolean tieneCaptura) {
        EntityManager em = null;
        List<String> productos = new ArrayList<>();

        try {
            em = getEntityManager();

            String queryStr = tieneCaptura
                    ? "SELECT DISTINCT it.codpro FROM inventario_toma it " +
                            "INNER JOIN inventario_almacen ia ON ia.codinvalm = it.codinvalm " +
                            "INNER JOIN capturastocksinventario c ON it.codpro = c.codpro AND c.codinvcab = ia.codinvcab "
                            +
                            "WHERE (it.tome > 0 OR it.tomf > 0) AND (c.cante > 0 OR c.cantf > 0) AND ia.codinvalm = ?"
                    : "SELECT DISTINCT it.codpro FROM inventario_toma it " +
                            "INNER JOIN inventario_almacen ia ON ia.codinvalm = it.codinvalm " +
                            "INNER JOIN fa_stock_vencimientos fs ON it.codpro = fs.codpro AND fs.codalm = ia.codalm " +
                            "WHERE (it.tome > 0 OR it.tomf > 0) AND (fs.qtymov > 0 OR fs.qtymov_m > 0) " +
                            "AND fs.fecven > CONVERT(DATE, GETDATE()) AND ia.codinvalm = ?";

            Query query = em.createNativeQuery(queryStr);
            query.setParameter(1, codinvalm);

            List<Object> resultados = query.getResultList();
            for (Object item : resultados) {
                if (item != null) {
                    productos.add(item.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return productos;
    }

    public List<String> obtenerProductosPorConteo(String codinvalm) {
        EntityManager em = null;
        List<String> productos = new ArrayList<>();

        try {
            em = getEntityManager();
            String queryStr = "SELECT DISTINCT codpro FROM inventario_lista_productos WHERE codinvalm = ?";

            Query query = em.createNativeQuery(queryStr);
            query.setParameter(1, codinvalm);

            List<Object> resultados = query.getResultList();
            for (Object item : resultados) {
                if (item != null) {
                    productos.add(item.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return productos;
    }

    public List<String> obtenerProductosConStockPorConteo(String codinvalm, boolean tieneCaptura) {
        EntityManager em = null;
        List<String> productos = new ArrayList<>();

        try {
            em = getEntityManager();

            String queryStr = tieneCaptura
                    ? "SELECT DISTINCT il.codpro FROM inventario_lista_productos il " +
                            "INNER JOIN capturastocksinventario c ON il.codpro = c.codpro " +
                            "INNER JOIN inventario_almacen ia ON ia.codinvcab = c.codinvcab " +
                            "WHERE (c.cante > 0 OR c.cantf > 0) AND ia.codinvalm = ? AND il.codinvalm = ?"
                    : "SELECT DISTINCT il.codpro FROM inventario_lista_productos il " +
                            "INNER JOIN fa_stock_vencimientos fs ON il.codpro = fs.codpro " +
                            "INNER JOIN inventario_almacen ia ON ia.codalm = fs.codalm " +
                            "WHERE (fs.qtymov > 0 OR fs.qtymov_m > 0) " +
                            "AND fs.fecven > CONVERT(DATE, GETDATE()) AND ia.codinvalm = ? AND il.codinvalm = ?";

            Query query = em.createNativeQuery(queryStr);
            query.setParameter(1, codinvalm);
            query.setParameter(2, codinvalm);

            List<Object> resultados = query.getResultList();
            for (Object item : resultados) {
                if (item != null) {
                    productos.add(item.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return productos;
    }

    public JSONArray detallesIdenticos(int codinv, boolean tieneCaptura) {
        JSONArray jsonArray = new JSONArray();

        // ? obtener todos los codinvalm de codinv
        JSONArray codinvalms = obtenerCodinvalmsSeparados(codinv);

        for (int i = 0; i < codinvalms.length(); i++) {
            // ? ptocesar cada codinvalm
            JSONObject conteo = new JSONObject();
            // ? obtener objeto inventarioalmacen
            String codinvalm = codinvalms.getString(i);
            int conteoId = Integer.parseInt(codinvalms.getString(i));
            InventarioAlmacen inventarioAlmacen = dao.findInventarioAlmacen(conteoId);
            conteo.put("conteo", inventarioAlmacen.getDesinvalm());
            // ? obtener nombre de la farmacia
            String farmacia = obtenerFarmaciaPorConteo(conteoId);
            conteo.put("farmacia", farmacia);
            // ? obtener el total de coincidencias
            int totalCoincidencias = obtenerTotalCoincidencias(conteoId);
            conteo.put("coincidencias", totalCoincidencias);
            // ? obtener el total de productos contabilizados
            int totalProductosContabilizados = obtenerTotalProductosContabilizadosPorConteo(codinvalm, tieneCaptura);
            conteo.put("total_registros", totalProductosContabilizados);
            // ? obtener productos con stock de inventario
            int totalProductosConStock = obtenerTotalProductosConStockPorConteo(codinvalm, tieneCaptura);
            // ? calculamos el progreso: total de item contabilizados / total de item con
            // ? stock
            double progreso = (double) totalProductosContabilizados / (double) totalProductosConStock * 100;
            progreso = Math.round(progreso * 100.0) / 100.0;
            conteo.put("progreso", progreso);
            jsonArray.put(conteo);
        }

        return jsonArray;
    }

    public int obtenerTotalCoincidencias(int codinvalm) {
        EntityManager em = null;
        int totalCoincidencias = 0;

        try {
            em = getEntityManager();
            String queryStr = "SELECT COUNT(*) FROM inventario_toma WHERE codinvalm = ? AND stkalm = tome AND stkalm_m = tomf AND (stkalm > 0 OR tome > 0 OR stkalm_m > 0 OR tomf > 0)";

            Query query = em.createNativeQuery(queryStr);
            query.setParameter(1, codinvalm);
            query.setParameter(2, codinvalm);

            Object result = query.getSingleResult();
            totalCoincidencias = result != null ? ((Number) result).intValue() : 0;

        } catch (Exception e) {
            e.printStackTrace();
            totalCoincidencias = 0;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return totalCoincidencias;
    }

    public String obtenerFarmaciaPorConteo(int codinvalm) {
        EntityManager em = null;
        String farmacia = "";

        try {
            em = getEntityManager();
            String queryStr = "SELECT a.desalm FROM inventario_almacen ia inner join fa_almacenes a on ia.codalm = a.codalm WHERE ia.codinvalm = ?";

            Query query = em.createNativeQuery(queryStr);
            query.setParameter(1, codinvalm);

            Object result = query.getSingleResult();
            farmacia = result != null ? result.toString() : "";

        } catch (Exception e) {
            e.printStackTrace();
            farmacia = "";
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return farmacia;
    }

}
