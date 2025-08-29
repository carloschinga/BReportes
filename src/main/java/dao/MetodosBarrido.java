package dao;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class MetodosBarrido extends JpaPadre {
    public MetodosBarrido(String empresa) {
        super(empresa);
    }

    public List<String> listaProductosActivos() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sql = "SELECT codpro FROM fa_productos WHERE estado = 'S'";
            Query q = em.createNativeQuery(sql);

            @SuppressWarnings("unchecked")
            List<String> result = q.getResultList();
            return result != null ? result : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
