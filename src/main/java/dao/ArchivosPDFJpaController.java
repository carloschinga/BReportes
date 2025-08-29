package dao;

import dao.exceptions.NonexistentEntityException;
import dto.ArchivosPDF;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author USUARIO
 */
public class ArchivosPDFJpaController extends JpaPadre {

    public ArchivosPDFJpaController(String empresa) {
        super(empresa);
    }

    public String listarArchivosJson() {
        EntityManager em = null;
        JSONArray jsonArray = new JSONArray();
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "SELECT id, nombre_original, nombre_guardado, descripcion, fecha_subida FROM ArchivosPDF");

            List<Object[]> resultados = query.getResultList();

            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("id", fila[0]);
                jsonObj.put("nombreOriginal", fila[1]);
                jsonObj.put("nombreGuardado", fila[2]);
                jsonObj.put("descripcion", fila[3]);
                jsonObj.put("fechaSubida", fila[4]);
                jsonArray.put(jsonObj);
            }

        } catch (Exception e) {
            return "{\"resultado\":\"error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return jsonArray.toString();
    }

    public void create(ArchivosPDF archivosPDF) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(archivosPDF);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(ArchivosPDF archivosPDF) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            archivosPDF = em.merge(archivosPDF);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = archivosPDF.getId();
                if (findArchivosPDF(id) == null) {
                    throw new NonexistentEntityException("The archivosPDF with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ArchivosPDF archivosPDF;
            try {
                archivosPDF = em.getReference(ArchivosPDF.class, id);
                archivosPDF.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The archivosPDF with id " + id + " no longer exists.", enfe);
            }
            em.remove(archivosPDF);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<ArchivosPDF> findArchivosPDFEntities() {
        return findArchivosPDFEntities(true, -1, -1);
    }

    public List<ArchivosPDF> findArchivosPDFEntities(int maxResults, int firstResult) {
        return findArchivosPDFEntities(false, maxResults, firstResult);
    }

    private List<ArchivosPDF> findArchivosPDFEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ArchivosPDF.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public ArchivosPDF findArchivosPDF(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(ArchivosPDF.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getArchivosPDFCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ArchivosPDF> rt = cq.from(ArchivosPDF.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
