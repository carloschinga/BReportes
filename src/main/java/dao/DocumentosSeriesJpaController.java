package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.DocumentosSeries;
import dto.DocumentosSeriesPK;

/**
 *
 * @author USUARIO
 */
public class DocumentosSeriesJpaController extends JpaPadre {

    public DocumentosSeriesJpaController(String empresa) {
        super(empresa);
    }

    // metodo que lista las series de un tipo de movimiento especifico
    public String listarDocJson(String tipkar) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "SELECT d.tdoser,d.destse,d.estado FROM documentos_series d WHERE len(d.tipkar_cad) > 0 AND d.siscod = 1 AND CHARINDEX(?,d.tipkar_cad ) > 0 ORDER BY d.destse ASC");
            query.setParameter(1, tipkar);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("tdoser", fila[0]);
                jsonObj.put("destse", fila[1]);
                jsonObj.put("estado", fila[2]);
                jsonArray.put(jsonObj);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    // metodo que muestra el numero de documento para la serie y el tipo
    public String obtenerdoc(int siscod, String tdoser, String tipkar) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "SELECT tdofac, tdoidser, tdonum, tdoemi FROM documentos_series WHERE siscod = ? AND tdoser = ? AND CHARINDEX(?, tipkar_cad) > 0 AND estado = 'S'");
            query.setParameter(1, siscod);
            query.setParameter(2, tdoser);
            query.setParameter(3, tipkar);

            // Ejecutar la consulta y obtener resultados
            List<Object[]> resultados = query.getResultList();

            // Verificar si se encontraron resultados
            if (resultados != null && !resultados.isEmpty()) {
                Object[] resultado = resultados.get(0); // Tomar el primer resultado
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("resultado", "ok");
                jsonObj.put("tdofac", resultado[0]);
                jsonObj.put("tdoidser", resultado[1]);
                jsonObj.put("tdonum", resultado[2]);
                jsonObj.put("tdoemi", resultado[3]); // Aquí corregí el índice, debe ser 3 para tdoemi

                return jsonObj.toString();
            } else {
                // Manejo cuando no hay resultados
                return "{\"resultado\":\"Error\",\"mensaje\":\"No se encontraron resultados.\"}";
            }
        } catch (NoResultException e) {
            // Manejo específico para cuando no hay resultados
            return "{\"resultado\":\"Error\",\"mensaje\":\"No se encontraron resultados.\"}";
        } catch (Exception e) {
            // Manejo de errores generales
            return "{\"resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void create(DocumentosSeries documentosSeries) throws PreexistingEntityException, Exception {
        if (documentosSeries.getDocumentosSeriesPK() == null) {
            documentosSeries.setDocumentosSeriesPK(new DocumentosSeriesPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(documentosSeries);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDocumentosSeries(documentosSeries.getDocumentosSeriesPK()) != null) {
                throw new PreexistingEntityException("DocumentosSeries " + documentosSeries + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(DocumentosSeries documentosSeries) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            documentosSeries = em.merge(documentosSeries);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                DocumentosSeriesPK id = documentosSeries.getDocumentosSeriesPK();
                if (findDocumentosSeries(id) == null) {
                    throw new NonexistentEntityException("The documentosSeries with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(DocumentosSeriesPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DocumentosSeries documentosSeries;
            try {
                documentosSeries = em.getReference(DocumentosSeries.class, id);
                documentosSeries.getDocumentosSeriesPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The documentosSeries with id " + id + " no longer exists.", enfe);
            }
            em.remove(documentosSeries);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<DocumentosSeries> findDocumentosSeriesEntities() {
        return findDocumentosSeriesEntities(true, -1, -1);
    }

    public List<DocumentosSeries> findDocumentosSeriesEntities(int maxResults, int firstResult) {
        return findDocumentosSeriesEntities(false, maxResults, firstResult);
    }

    private List<DocumentosSeries> findDocumentosSeriesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DocumentosSeries.class));
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

    public DocumentosSeries findDocumentosSeries(DocumentosSeriesPK id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(DocumentosSeries.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getDocumentosSeriesCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DocumentosSeries> rt = cq.from(DocumentosSeries.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
