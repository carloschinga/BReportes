package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;

public class MetodoForLoginRol extends JpaPadre {
    public MetodoForLoginRol(String empresa) {
        super(empresa);
    }

    public JSONArray getLoginPagina() {
        EntityManager em = null;
        JSONArray data = new JSONArray();

        try {
            em = getEntityManager();
            String sql = "select pag_id, pag_nombre, pag_ruta, tip_menu, pag_padre from paginas";
            Query q = em.createNativeQuery(sql);
            List<Object[]> list = q.getResultList();
            for (Object[] objects : list) {
                JSONObject obj = new JSONObject();
                obj.put("id", objects[0]);
                obj.put("tipoMenu", objects[3]);
                obj.put("text", objects[1]);
                obj.put("codMas", objects[4]);
                obj.put("parent", (((Number) objects[4]).intValue() == 0) ? "#" : objects[4]);
                data.put(obj);
            }
            return data;
        } catch (Exception e) {
            return data;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    @Transactional
    public void actualizarPermisos(String rolId, List<Object[]> permisos) {

        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            String sql = "delete from grupos_permisos where grucod = ?";
            Query q = em.createNativeQuery(sql);
            q.setParameter(1, rolId);
            q.executeUpdate();

            String sql2 = "insert into grupos_permisos (grucod, pag_id, asigPerm) values (?, ?, ?)";
            Query q2 = em.createNativeQuery(sql2);
            for (Object[] objects : permisos) {
                q2.setParameter(1, rolId);
                q2.setParameter(2, ((Number) objects[0]).intValue());
                q2.setParameter(3, ((Number) objects[2]).intValue());
                q2.executeUpdate();
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public JSONArray getRols() {
        EntityManager em = null;
        JSONArray data = new JSONArray();

        try {
            em = getEntityManager();
            String sql = "select grucod, grudes from grupos where estado = 'S'";
            Query q = em.createNativeQuery(sql);
            List<Object[]> list = q.getResultList();
            for (Object[] objects : list) {
                JSONObject obj = new JSONObject();
                obj.put("codiRol", objects[0]);
                obj.put("nomRol", objects[1]);
                data.put(obj);
            }
            return data;
        } catch (Exception e) {
            return data;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public JSONArray obtenerPaginasPermitidas(String rolId) {
        EntityManager em = null;
        JSONArray data = new JSONArray();

        try {
            em = getEntityManager();
            String sql = "SELECT p.pag_id, gp.asigPerm FROM grupos_permisos gp " +
                    "INNER JOIN paginas p ON p.pag_id = gp.pag_id WHERE gp.grucod = ?";

            Query q = em.createNativeQuery(sql);
            q.setParameter(1, rolId);

            List<Object[]> list = q.getResultList();
            for (Object[] objects : list) {
                JSONObject obj = new JSONObject();
                obj.put("codiPagi", objects[0]);

                // Convertir Boolean (BIT) a 1 o 0
                boolean permiso = (Boolean) objects[1];
                obj.put("asigPerm", permiso ? 1 : 0); // true → 1, false → 0

                data.put(obj);
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace(); // Loggear el error para diagnóstico
            return data; // Retornar array vacío en caso de error
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public JSONArray obtenerPaginasPorGrucod(String rolId) {
        EntityManager em = null;
        JSONArray data = new JSONArray();

        try {
            em = getEntityManager();
            String sql = "SELECT p.pag_id, p.pag_nombre, p.pag_ruta, tip_menu, p.pag_padre, p.iclass FROM grupos_permisos gp "
                    +
                    "INNER JOIN paginas p ON p.pag_id = gp.pag_id WHERE gp.grucod = ? order by p.pag_nombre";

            Query q = em.createNativeQuery(sql);
            q.setParameter(1, rolId);

            List<Object[]> list = q.getResultList();
            for (Object[] objects : list) {
                JSONObject obj = new JSONObject();
                obj.put("codiPagi", objects[0]);
                obj.put("nombPagi", objects[1]);
                obj.put("ruta", objects[2]);
                obj.put("tipMenu", objects[3]);
                obj.put("padre", objects[4]);
                obj.put("iclass", objects[5]);
                data.put(obj);
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace(); // Loggear el error para diagnóstico
            return data; // Retornar array vacío en caso de error
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }
}
