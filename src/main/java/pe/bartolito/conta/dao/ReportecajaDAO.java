/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dao;

import dao.JpaPadre;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author USUARIO
 */
public class ReportecajaDAO extends JpaPadre{
    public ReportecajaDAO(String empresa) {
        super(empresa);
    }

    // Devuelve todas las sucursales en JSON [{"id", "nombre"}, ...]
    public String getSucursales() {
        EntityManager em = null;
        JSONArray json = new JSONArray();
        try {
            em = getEntityManager();
            String sql = "SELECT siscod, sisent FROM dbo.sistema ORDER BY sisent";
            Query q = em.createNativeQuery(sql);

            @SuppressWarnings("unchecked")
            List<Object[]> resultados = q.getResultList();
            for (Object[] fila : resultados) {
                JSONObject obj = new JSONObject();
                obj.put("id", fila[0]);
                obj.put("nombre", fila[1]);
                json.put(obj);
            }
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject err = new JSONObject();
            err.put("error", "Error en DAO.getSucursales: " + e.getMessage());
            return err.toString();
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    // Devuelve usuarios/cajeros que pertenecen a la sucursal (según join sistema.usecod = usuarios.usecod)
    public String getUsuariosPorSucursal(int siscod) {
        EntityManager em = null;
        JSONArray json = new JSONArray();
        try {
            em = getEntityManager();

            // Usamos join por usecod en sistema (según tu output SQL)
            String sql = "select  u.usecod, u.usenam from usuarios  u where siscod=? ";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, siscod);

            @SuppressWarnings("unchecked")
            List<Object[]> resultados = q.getResultList();
            for (Object[] fila : resultados) {
                JSONObject obj = new JSONObject();
                obj.put("id", fila[0]);
                obj.put("nombre", fila[1]);
                json.put(obj);
            }
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject err = new JSONObject();
            err.put("error", "Error en DAO.getUsuariosPorSucursal: " + e.getMessage());
            return err.toString();
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }
    
    // Devuelve aperturas (invnum_aper) para siscod entre fecha1 y fecha2 en JSON [{id, label}, ...]
public String getAperturasPorSucursalFechas(int siscod, String fecha1, String fecha2) {
    EntityManager em = null;
    JSONArray json = new JSONArray();
    try {
        em = getEntityManager();

        // fecha1/fecha2 deben venir en formato 'YYYY-MM-DD HH:mm:ss' (estilo 120)
        String sql = "SELECT DISTINCT invnum_aper FROM facturas " +
                     "WHERE siscod = ? AND facdat BETWEEN ? AND ? " +
                     "ORDER BY invnum_aper";
        Query q = em.createNativeQuery(sql);
        q.setParameter(1, siscod);
        q.setParameter(2, fecha1);
        q.setParameter(3, fecha2);

        @SuppressWarnings("unchecked")
        List<Object> resultados = q.getResultList();
        for (Object fila : resultados) {
            JSONObject obj = new JSONObject();
            // invnum_aper puede venir como BigDecimal/Integer. Convertimos a string para label.
            String id = fila == null ? "" : fila.toString();
            obj.put("id", id);
            obj.put("label", "Apertura #" + id);
            json.put(obj);
        }
        return json.toString();
    } catch (Exception e) {
        e.printStackTrace();
        return "[]";
    } finally {
        if (em != null && em.isOpen()) em.close();
    }
}

}
