/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.json.JSONArray;
import org.json.JSONObject;

public class CigoDAO extends JpaPadre {

    public CigoDAO(String empresa) {
        super(empresa);
    }

    public String Listado() {
        EntityManager em = null;
        JSONArray respuesta = new JSONArray();
        try {
            em = getEntityManager();
            String sql = "SELECT siscod,sisent FROM sistema";
            Query q = em.createNativeQuery(sql);
            List<Object[]> dato = q.getResultList();

            for (Object[] completo : dato) {
                JSONObject fullrow = new JSONObject();
                fullrow.put("codigo", completo[0]);
                fullrow.put("nombre", completo[1]);
                respuesta.put(fullrow);
            }

            return respuesta.toString();

        } catch (Exception e) {
            return respuesta.toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
