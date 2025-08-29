package dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.json.JSONArray;
import org.json.JSONObject;

public class MetodosRolObje extends JpaPadre {
    public MetodosRolObje(String empresa) {
        super(empresa);
    }

    public String getDataDashVendedor(int SucurId, String userVendedor) {
        EntityManager em = null;
        JSONArray json = new JSONArray();
        try {
            em = getEntityManager();
            String sql = "SELECT "
                    + "v.VendedId, "
                    + "v.VendedDesc, "
                    + "cv.CuotVtaId, "
                    + "cvm.CuotVtaMesId, "
                    + "cvm.CuotVtaMeta AS ObjetivoSucursal, "
                    + "cr.CuotVtaRolMonto AS ObjetivoRol, "
                    + "cvd.CuotVentVendMont AS ObjetivoVendedor, "
                    + "cvd.CuotVentVendPorc AS PorcentajeObjetivoVendedor, "
                    + "cv.estado AS EstadoCuota, "
                    + "ISNULL(SUM(hv.VtasMontConIGVReal), 0) AS VentasReales, "
                    + "CASE "
                    + "    WHEN cvd.CuotVentVendMont > 0 THEN "
                    + "        (ISNULL(SUM(hv.VtasMontConIGVReal), 0) / cvd.CuotVentVendMont) * 100 "
                    + "    ELSE 0 "
                    + "END AS PorcAvanceVendedor, "
                    + "cvm.porc_estra as porc_estra, "
                    + "cv.mesano as mesano "
                    + "FROM HechCuotaVenta cv "
                    + "INNER JOIN HechCuotaVentaMes cvm ON cv.CuotVtaId = cvm.CuotVtaId "
                    + "INNER JOIN HechCuotRol cr ON cvm.CuotVtaMesId = cr.CuotVtaMesId "
                    + "INNER JOIN HechCuotVend cvd ON cr.CuotVtaRolId = cvd.CuotVtaRolId "
                    + "INNER JOIN DimVendedor v ON cvd.VendId = v.VendedId "
                    + "LEFT JOIN HechVentas hv "
                    + "    ON hv.VendedId = v.VendedId "
                    + "    AND hv.SucurId = cvm.SucurId "
                    + "    AND hv.VtasFech BETWEEN cvd.FechInic AND cvd.FechFin "
                    + "WHERE "
                    + "    cv.hecAct = 1 "
                    + "    AND cvm.SucurId = ? "
                    + "    AND v.VendedDesc = ? "
                    + "GROUP BY "
                    + "    v.VendedId, "
                    + "    v.VendedDesc, "
                    + "    cv.CuotVtaId, "
                    + "    cvm.CuotVtaMesId, "
                    + "    cvm.CuotVtaMeta, "
                    + "    cr.CuotVtaRolMonto, "
                    + "    cvd.CuotVentVendMont, "
                    + "    cvd.CuotVentVendPorc, "
                    + "    cv.estado, "
                    + "    cvm.porc_estra, "
                    + "    cv.mesano "
                    + "ORDER BY "
                    + "    cvm.CuotVtaMesId DESC";

            Query q = em.createNativeQuery(sql);
            q.setParameter(1, SucurId);
            q.setParameter(2, userVendedor);

            List<Object[]> resultados = q.getResultList();
            if (resultados.size() > 0) {
                for (Object[] fila : resultados) {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("VendedId", fila[0]);
                    jsonObj.put("VendedDesc", fila[1]);
                    jsonObj.put("CuotVtaId", fila[2]);
                    jsonObj.put("CuotVtaMesId", fila[3]);
                    jsonObj.put("ObjetivoSucursal", fila[4]);
                    jsonObj.put("ObjetivoRol", fila[5]);
                    jsonObj.put("ObjetivoVendedor", fila[6]);
                    jsonObj.put("PorcentajeObjetivoVendedor", fila[7]);
                    jsonObj.put("EstadoCuota", fila[8]);
                    jsonObj.put("VentasReales", fila[9]);
                    jsonObj.put("PorcAvanceVendedor", fila[10]);
                    // devolver 0 si no hay datos
                    jsonObj.put("porc_estra", fila[11] == null ? 0 : fila[11]);
                    jsonObj.put("mesano", fila[12]);
                    // devolver un campo de fecha con el formato yyyy-MM-dd pero con un dia menos
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new java.util.Date());
                    cal.add(Calendar.DAY_OF_MONTH, -1);
                    jsonObj.put("fechaCorte", sdf.format(cal.getTime()));
                    json.put(jsonObj);
                }
            }
            return json.toString();
        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("error", true);
            error.put("mensaje", "Error al obtener datos del dashboard: " + e.getMessage());
            return error.toString();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public String getObjetivo(int codobj) {
        EntityManager em = null;
        JSONObject json = new JSONObject();

        try {
            em = getEntityManager();
            String sql = "SELECT hv.desobj, hv.tipo, hv.feccre, " +
                    "hv.mesano, hm.montototal " +
                    "FROM HechCuotaVenta hv " +
                    "INNER JOIN ( SELECT CuotVtaId, " +
                    "SUM(CuotVtaMeta) AS montototal " +
                    "FROM HechCuotaVentaMes " +
                    "GROUP BY CuotVtaId " +
                    ") hm ON hm.CuotVtaId = hv.CuotVtaId " +
                    "WHERE hv.CuotVtaId = ?";

            Query q = em.createNativeQuery(sql);
            q.setParameter(1, codobj);

            List<Object[]> resultados = q.getResultList();
            if (resultados.size() > 0) {
                Object[] fila = resultados.get(0);
                json.put("nombre", fila[0]);
                json.put("tipo", fila[1]);
                json.put("feccre", fila[2]);
                json.put("mesano", fila[3]);
                json.put("montototal", fila[4]);
            } else {
                json.put("error", "No se encontro el objetivo");
            }
        } catch (Exception e) {
            json.put("error", e.getMessage());
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
        return json.toString();
    }

    public String listarRoles() {
        EntityManager em = null;
        JSONArray json = new JSONArray();

        try {
            em = getEntityManager();
            String sql = "select RolId, RolDes from DimRol";
            Query q = em.createNativeQuery(sql);
            List<Object[]> resultados = q.getResultList();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("rolId", fila[0]);
                jsonObj.put("rolDes", fila[1]);
                json.put(jsonObj);
            }
        } catch (Exception e) {
            json = new JSONArray();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
        return json.toString();
    }

    public String listarSucursalesConMesId(int codobj) {
        EntityManager em = null;
        JSONArray json = new JSONArray();

        try {
            em = getEntityManager();
            String sql = "select CuotVtaMesId, SucurId from HechCuotaVentaMes where CuotVtaId = ?";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, codobj);
            List<Object[]> resultados = q.getResultList();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("VtaMesId", fila[0]);
                jsonObj.put("SucurId", fila[1]);
                json.put(jsonObj);
            }
        } catch (Exception e) {
            json = new JSONArray();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return json.toString();
    }

    public String listarRolesAsignados(int vtaMesId) {
        EntityManager em = null;
        JSONArray json = new JSONArray();

        try {
            em = getEntityManager();
            String sql = "SELECT hcr.CuotVtaRolId, hcr.IdRol, hcr.CuotVtaRolMonto, hcr.CuotVtaRolPorce, " +
                    "hcvm.CuotVtaMeta AS CuotVtaRolMontoSucursal, " +
                    "(SELECT COUNT(*) FROM HechCuotVend hcv WHERE hcv.CuotVtaRolId = hcr.CuotVtaRolId) AS vendedores " +
                    "FROM HechCuotRol hcr " +
                    "INNER JOIN HechCuotaVentaMes hcvm ON hcr.CuotVtaMesId = hcvm.CuotVtaMesId " +
                    "WHERE hcr.CuotVtaMesId = ?";

            Query q = em.createNativeQuery(sql);
            q.setParameter(1, vtaMesId);
            List<Object[]> resultados = q.getResultList();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("CuotVtaRolId", fila[0]);
                jsonObj.put("IdRol", fila[1]);
                jsonObj.put("CuotVtaRolMonto", fila[2]);
                jsonObj.put("CuotVtaRolPorce", fila[3]);
                // falta agregar el campo de monto de la sucursal
                jsonObj.put("CuotVtaRolMontoSucursal", fila[4]);
                // falta agregar el campo de cantidad de vendedores asignados al rol
                jsonObj.put("vendedores", fila[5]);
                json.put(jsonObj);
            }
        } catch (Exception e) {
            json = new JSONArray();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return json.toString();
    }

    public String obtenerMontoSucursal(int vtaMesId) {
        EntityManager em = null;
        JSONArray json = new JSONArray();

        try {
            em = getEntityManager();
            String sql = "SELECT CuotVtaMeta FROM HechCuotaVentaMes WHERE CuotVtaMesId = ?";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, vtaMesId);
            List<Object> resultados = q.getResultList();
            for (Object fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("CuotVtaRolMontoSucursal", fila);
                json.put(jsonObj);
            }
        } catch (Exception e) {
            json = new JSONArray();
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("error", "Error de catch en la consulta");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return json.toString();
    }

    public void createCuotRol(JSONArray rolesParaAgregar) throws Exception {
        EntityManager em = null;
        JSONObject json = new JSONObject();

        try {
            em = getEntityManager();
            em.getTransaction().begin();

            for (int i = 0; i < rolesParaAgregar.length(); i++) {
                JSONObject rol = rolesParaAgregar.getJSONObject(i);

                // Validación básica (opcional)
                if (!rol.has("vtaMesId") || !rol.has("rolId") || !rol.has("monto") || !rol.has("porcentaje")) {
                    throw new IllegalArgumentException("Faltan campos requeridos en el JSON.");
                }

                String sql = "INSERT INTO HechCuotRol (CuotVtaMesId, IdRol, CuotVtaRolMonto, CuotVtaRolPorce) VALUES (?, ?, ?, ?)";
                Query q = em.createNativeQuery(sql);
                q.setParameter(1, Integer.parseInt(rol.getString("vtaMesId")));
                q.setParameter(2, Double.parseDouble(rol.getString("rolId")));
                q.setParameter(3, rol.getDouble("monto"));
                q.setParameter(4, rol.getDouble("porcentaje"));
                q.executeUpdate();
            }

            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // Rollback explícito
            }
            json.put("error", ex.getMessage());
            throw new RuntimeException("Error al insertar roles: " + ex.getMessage(), ex);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void editCuotRol(JSONArray rolesParaEditar) throws Exception {
        EntityManager em = null;
        JSONObject json = new JSONObject();

        try {
            em = getEntityManager();
            em.getTransaction().begin();

            for (int i = 0; i < rolesParaEditar.length(); i++) {
                JSONObject rol = rolesParaEditar.getJSONObject(i);

                // Validación de campos requeridos
                if (!rol.has("id") || !rol.has("vtaMesId") || !rol.has("rolId")
                        || !rol.has("monto") || !rol.has("porcentaje")) {
                    throw new IllegalArgumentException("Faltan campos requeridos en el JSON para edición.");
                }

                String sql = "UPDATE HechCuotRol SET "
                        + "CuotVtaMesId = ?, "
                        + "IdRol = ?, "
                        + "CuotVtaRolMonto = ?, "
                        + "CuotVtaRolPorce = ? "
                        + "WHERE CuotVtaRolId = ?";

                Query q = em.createNativeQuery(sql);
                q.setParameter(1, Integer.parseInt(rol.getString("vtaMesId")));
                q.setParameter(2, rol.getInt("rolId"));
                q.setParameter(3, rol.getDouble("monto"));
                // convertir de String a Double
                q.setParameter(4, rol.getDouble("porcentaje"));
                q.setParameter(5, rol.getInt("id"));
                q.executeUpdate();
            }

            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            json.put("error", ex.getMessage());
            throw new RuntimeException("Error al editar roles: " + ex.getMessage(), ex);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void deleteCuotRol(JSONArray rolesEliminados) throws Exception {
        EntityManager em = null;

        try {
            em = getEntityManager();
            em.getTransaction().begin();

            // Verificar si hay elementos para eliminar
            if (rolesEliminados.length() > 0) {
                // Obtener el ID directamente del array (formato [2])
                int idEliminar = rolesEliminados.getInt(0); // Obtiene el primer elemento (índice 0)

                String sql = "DELETE FROM HechCuotRol WHERE CuotVtaRolId = ?";
                Query q = em.createNativeQuery(sql);
                q.setParameter(1, idEliminar);
                q.executeUpdate();
            }

            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar rol: " + ex.getMessage(), ex);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public String listarVendedores() {
        EntityManager em = null;
        JSONArray json = new JSONArray();

        try {
            em = getEntityManager();
            String sql = "select VendedId, VendedDesc from DimVendedor";
            Query q = em.createNativeQuery(sql);
            List<Object[]> resultados = q.getResultList();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("VendedId", fila[0]);
                jsonObj.put("VendedDesc", fila[1]);
                json.put(jsonObj);
            }

        } catch (Exception e) {
            json = new JSONArray();
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("error", "Error de catch en la consulta");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return json.toString();
    }

    public String listarVendedoresAsignados(int vtaRol) {
        EntityManager em = null;
        JSONArray json = new JSONArray();

        try {
            em = getEntityManager();
            String sql = "select CuotVtaVendId, RolId, VendId, FechInic, FechFin, CuotVentVendMont, CuotVentVendPorc from HechCuotVend where CuotVtaRolId = ?";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, vtaRol);
            List<Object[]> resultados = q.getResultList();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("CuotVtaVendId", fila[0]);
                jsonObj.put("RolId", fila[1]);
                jsonObj.put("VendId", fila[2]);
                jsonObj.put("FechInic", fila[3]);
                jsonObj.put("FechFin", fila[4]);
                jsonObj.put("CuotVentVendMont", fila[5]);
                jsonObj.put("CuotVentVendPorc", fila[6]);
                json.put(jsonObj);
            }
        } catch (Exception e) {
            json = new JSONArray();
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("error", "Error de catch en la consulta");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return json.toString();
    }

    public String getRolIdFromCuotRol(int cuotVentRol) {
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            String sql = "SELECT IdRol FROM HechCuotRol WHERE CuotVtaRolId = ?";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, cuotVentRol);
            List<Object> resultados = q.getResultList();
            for (Object fila : resultados) {
                resultado = fila.toString();
            }
        } catch (Exception e) {
            resultado = "E";
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return resultado;
    }

    public void deleteCuotVend(JSONArray rolesEliminados) throws Exception {
        EntityManager em = null;

        try {
            em = getEntityManager();
            em.getTransaction().begin();

            // Verificar si hay elementos para eliminar
            if (rolesEliminados.length() > 0) {
                // Obtener el ID directamente del array (formato [2])
                int idEliminar = rolesEliminados.getInt(0); // Obtiene el primer elemento (índice 0)

                String sql = "DELETE FROM HechCuotVend WHERE CuotVtaVendId = ?";
                Query q = em.createNativeQuery(sql);
                q.setParameter(1, idEliminar);
                q.executeUpdate();
            }

            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar rol: " + ex.getMessage(), ex);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void createCuotVendConSqlDate(JSONArray rolesParaAgregar, int rolId, int cuotVentRol) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            for (int i = 0; i < rolesParaAgregar.length(); i++) {
                JSONObject rol = rolesParaAgregar.getJSONObject(i);

                // Validaciones...
                if (!rol.has("VendedId") || rol.isNull("VendedId") ||
                        !rol.has("FechInic") || rol.isNull("FechInic") ||
                        !rol.has("FechFin") || rol.isNull("FechFin") ||
                        !rol.has("CuotVentVendPorc") || rol.isNull("CuotVentVendPorc") ||
                        !rol.has("CuotVentVendMont") || rol.isNull("CuotVentVendMont")) {
                    throw new IllegalArgumentException("Faltan campos requeridos o tienen valores nulos en el JSON.");
                }

                // Convertir a java.sql.Date en lugar de java.util.Date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date utilDateInic = sdf.parse(rol.getString("FechInic"));
                java.util.Date utilDateFin = sdf.parse(rol.getString("FechFin"));

                java.sql.Date sqlDateInic = new java.sql.Date(utilDateInic.getTime());
                java.sql.Date sqlDateFin = new java.sql.Date(utilDateFin.getTime());

                String sql = "INSERT INTO HechCuotVend (CuotVtaRolId, RolId, VendId, FechInic, FechFin, CuotVentVendMont, CuotVentVendPorc) "
                        +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";

                Query q = em.createNativeQuery(sql);
                q.setParameter(1, cuotVentRol);
                q.setParameter(2, rolId);
                q.setParameter(3, rol.getInt("VendedId"));
                q.setParameter(4, sqlDateInic); // java.sql.Date
                q.setParameter(5, sqlDateFin); // java.sql.Date
                q.setParameter(6, rol.getDouble("CuotVentVendMont"));
                q.setParameter(7, rol.getDouble("CuotVentVendPorc"));

                q.executeUpdate();
            }

            em.getTransaction().commit();

        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al insertar en HechCuotVend: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException("Error al insertar vendedores: " + ex.getMessage(), ex);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public void editCuotVend(JSONArray rolesParaEditar) throws Exception {
        EntityManager em = null;

        try {
            em = getEntityManager();
            em.getTransaction().begin();

            for (int i = 0; i < rolesParaEditar.length(); i++) {
                JSONObject rol = rolesParaEditar.getJSONObject(i);

                // Validacion de campos requeridos
                if (!rol.has("CuotVtaVendId") || !rol.has("VendedId") || !rol.has("FechInic") || !rol.has("FechFin")
                        || !rol.has("CuotVentVendPorc") || !rol.has("CuotVentVendMont")) {
                    throw new IllegalArgumentException("Faltan campos requeridos en el JSON para edición.");
                }

                // Convertir a java.sql.Date en lugar de java.util.Date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date utilDateInic = sdf.parse(rol.getString("FechInic"));
                java.util.Date utilDateFin = sdf.parse(rol.getString("FechFin"));

                java.sql.Date sqlDateInic = new java.sql.Date(utilDateInic.getTime());
                java.sql.Date sqlDateFin = new java.sql.Date(utilDateFin.getTime());

                String sql = "update HechCuotVend set " +
                        "VendId = ?, " +
                        "FechInic = ?, " +
                        "FechFin = ?, " +
                        "CuotVentVendPorc = ?, " +
                        "CuotVentVendMont = ? " +
                        "where CuotVtaVendId = ?";

                Query q = em.createNativeQuery(sql);
                q.setParameter(1, rol.getInt("VendedId"));
                q.setParameter(2, sqlDateInic);
                q.setParameter(3, sqlDateFin);
                q.setParameter(4, rol.getDouble("CuotVentVendPorc"));
                q.setParameter(5, rol.getDouble("CuotVentVendMont"));
                q.setParameter(6, rol.getInt("CuotVtaVendId"));

                q.executeUpdate();

            }

            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al editar en HechCuotVend: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException("Error al editar vendedores: " + ex.getMessage(), ex);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

}
