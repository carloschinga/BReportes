package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.exceptions.NonexistentEntityException;
import dto.Tiraproductocabecera;

/**
 *
 * @author USUARIO
 */
public class TiraproductocabeceraJpaController extends JpaPadre {

    public TiraproductocabeceraJpaController(String empresa) {
        super(empresa);
    }

    public void create(Tiraproductocabecera tiraproductocabecera) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tiraproductocabecera);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarJson() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery("select codtir,tirnam from tiraproductocabecera");
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codtir", fila[0]);
                jsonObj.put("tirnam", fila[1]);
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

    public String listarJson(int codtir) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery("select codpro,codtiritm from tiraproductodetalle where codtir = ?");
            int i = 1;
            query.setParameter(i++, codtir);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                jsonArray.put(fila[0]);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int obtenerultcodtir() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Integer> query = em.createNamedQuery("Tiraproductocabecera.findUltCodtir", Integer.class);
            query.setMaxResults(1);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return 0;
        } catch (Exception e) {
            return -1;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(Tiraproductocabecera tiraproductocabecera) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tiraproductocabecera = em.merge(tiraproductocabecera);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tiraproductocabecera.getCodtir();
                if (findTiraproductocabecera(id) == null) {
                    throw new NonexistentEntityException(
                            "The tiraproductocabecera with id " + id + " no longer exists.");
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
            Tiraproductocabecera tiraproductocabecera;
            try {
                tiraproductocabecera = em.getReference(Tiraproductocabecera.class, id);
                tiraproductocabecera.getCodtir();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tiraproductocabecera with id " + id + " no longer exists.",
                        enfe);
            }
            em.remove(tiraproductocabecera);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<Tiraproductocabecera> findTiraproductocabeceraEntities() {
        return findTiraproductocabeceraEntities(true, -1, -1);
    }

    public List<Tiraproductocabecera> findTiraproductocabeceraEntities(int maxResults, int firstResult) {
        return findTiraproductocabeceraEntities(false, maxResults, firstResult);
    }

    private List<Tiraproductocabecera> findTiraproductocabeceraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tiraproductocabecera.class));
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

    public Tiraproductocabecera findTiraproductocabecera(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(Tiraproductocabecera.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getTiraproductocabeceraCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tiraproductocabecera> rt = cq.from(Tiraproductocabecera.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
