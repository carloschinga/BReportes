package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import dto.Datosgenerartxt;
import dto.generardatosdetalle;

/**
 *
 * @author USUARIO
 */
public class GenerarTXTController extends JpaPadre {

    public GenerarTXTController(String empresa) {
        super(empresa);
    }

    public String sel_generadatos_para_descargoenvio_cab_2(int[] datos, int siscod) {
        EntityManager em = null;
        StringBuilder cadena = new StringBuilder();
        boolean pri = true;

        try {
            em = getEntityManager();

            for (int dato : datos) {
                try {
                    StoredProcedureQuery storedProcedure = em
                            .createStoredProcedureQuery("sel_generadatos_para_descargoenvio_cab_2_3");

                    storedProcedure.registerStoredProcedureParameter("siscod_d", Integer.class, ParameterMode.IN);
                    storedProcedure.registerStoredProcedureParameter("siscod", Integer.class, ParameterMode.IN);

                    storedProcedure.setParameter("siscod_d", dato);
                    storedProcedure.setParameter("siscod", siscod);

                    // Ejecutar el procedimiento almacenado y obtener resultados
                    List<Object[]> results = storedProcedure.getResultList();

                    for (Object[] res : results) {
                        if (res.length == 0 || res[0] == null) {
                            continue; // Ignorar resultados vacíos o nulos
                        }

                        String dat = res[0].toString();

                        // Validar que la cadena tenga al menos 2 caracteres para evitar errores en
                        // substring
                        if (dat.length() > 2) {
                            if (!pri) {
                                cadena.append(",");
                            } else {
                                pri = false;
                            }
                            cadena.append(dat.substring(1, dat.length() - 1));
                        }
                    }
                } catch (Exception e) {
                    // Devolver mensaje de error JSON bien formado
                    return "{\"resultado\":\"error\",\"mensaje\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}";
                }
            }
        } catch (Exception e) {
            return "{\"resultado\":\"error\",\"mensaje\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}";
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return "{\"listacabecera\":[" + cadena.toString() + "]}";
    }

    public String sel_generadatos_para_descargoenvio_det2(List<generardatosdetalle> lista) {
        EntityManager em = null;
        StringBuilder cadena = new StringBuilder();
        boolean pri = true;

        try {
            // Abrimos el EntityManager una sola vez para todo el proceso
            em = getEntityManager();

            for (generardatosdetalle dato : lista) {
                for (Datosgenerartxt producto : dato.getDatos()) {
                    // Solo procesamos productos con cantidades positivas
                    if (producto.getCantE() > 0 || producto.getCantF() > 0) {
                        try {
                            StoredProcedureQuery storedProcedure = em
                                    .createStoredProcedureQuery("sel_generadatos_para_descargoenvio_det2");

                            // Registro de parámetros de entrada
                            storedProcedure.registerStoredProcedureParameter("siscod_d", Integer.class,
                                    ParameterMode.IN);
                            storedProcedure.registerStoredProcedureParameter("codpro", String.class, ParameterMode.IN);
                            storedProcedure.registerStoredProcedureParameter("qtypro", Integer.class, ParameterMode.IN);
                            storedProcedure.registerStoredProcedureParameter("qtypro_m", Integer.class,
                                    ParameterMode.IN);
                            storedProcedure.registerStoredProcedureParameter("codlotentra", String.class,
                                    ParameterMode.IN);

                            // Establecer valores de parámetros de entrada
                            storedProcedure.setParameter("siscod_d", dato.getSiscod());
                            storedProcedure.setParameter("codpro", producto.getCodpro());
                            storedProcedure.setParameter("qtypro", producto.getCantE());
                            storedProcedure.setParameter("qtypro_m", producto.getCantF());
                            storedProcedure.setParameter("codlotentra", producto.getLote());

                            // Registro de parámetros de salida
                            storedProcedure.registerStoredProcedureParameter("qtppro", Integer.class,
                                    ParameterMode.OUT);
                            storedProcedure.registerStoredProcedureParameter("qtppro_m", Integer.class,
                                    ParameterMode.OUT);
                            storedProcedure.registerStoredProcedureParameter("qtbpro", Integer.class,
                                    ParameterMode.OUT);
                            storedProcedure.registerStoredProcedureParameter("vvfsal", Double.class, ParameterMode.OUT);
                            storedProcedure.registerStoredProcedureParameter("pvfsal", Double.class, ParameterMode.OUT);
                            storedProcedure.registerStoredProcedureParameter("pvpsal", Double.class, ParameterMode.OUT);
                            storedProcedure.registerStoredProcedureParameter("dtopro1", Double.class,
                                    ParameterMode.OUT);
                            storedProcedure.registerStoredProcedureParameter("dtopro2", Double.class,
                                    ParameterMode.OUT);
                            storedProcedure.registerStoredProcedureParameter("dtopro3", Double.class,
                                    ParameterMode.OUT);
                            storedProcedure.registerStoredProcedureParameter("dtopro4", Double.class,
                                    ParameterMode.OUT);
                            storedProcedure.registerStoredProcedureParameter("fecven", java.sql.Timestamp.class,
                                    ParameterMode.OUT);
                            storedProcedure.registerStoredProcedureParameter("util_vta", Double.class,
                                    ParameterMode.OUT);
                            storedProcedure.registerStoredProcedureParameter("dscto_vta", Double.class,
                                    ParameterMode.OUT);
                            storedProcedure.registerStoredProcedureParameter("codlot", String.class, ParameterMode.OUT);
                            storedProcedure.registerStoredProcedureParameter("siscod", Integer.class,
                                    ParameterMode.OUT);
                            storedProcedure.registerStoredProcedureParameter("codpro_out", String.class,
                                    ParameterMode.OUT);
                            storedProcedure.registerStoredProcedureParameter("codlotsalida", String.class,
                                    ParameterMode.OUT);

                            // Ejecutar el procedimiento almacenado y obtener resultados
                            List<Object[]> results = storedProcedure.getResultList();

                            // Ejemplo: obtener valores de salida si los necesitas
                            Integer qtppro = (Integer) storedProcedure.getOutputParameterValue("qtppro");
                            // ... otros parámetros de salida pueden usarse según necesidad

                            // Procesar resultados para construir la cadena JSON
                            for (Object[] res : results) {
                                if (res.length == 0 || res[0] == null) {
                                    continue; // Ignorar resultados vacíos o nulos
                                }
                                String dat = res[0].toString();

                                if (dat.length() > 2) { // Evitar errores en substring
                                    if (!pri) {
                                        cadena.append(",");
                                    } else {
                                        pri = false;
                                    }
                                    cadena.append(dat.substring(1, dat.length() - 1));
                                }
                            }

                        } catch (Exception e) {
                            // Retornar error en formato JSON escapando comillas
                            return "{\"resultado\":\"error\",\"mensaje\":\"" + e.getMessage().replace("\"", "\\\"")
                                    + "\"}";
                        }
                    }
                }
            }
        } catch (Exception e) {
            return "{\"resultado\":\"error\",\"mensaje\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}";
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        // Retornar resultado JSON completo
        return "{\"listadetalle\":[" + cadena.toString() + "]}";
    }

}
