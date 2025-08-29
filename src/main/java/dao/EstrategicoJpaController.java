/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.Estrategico;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author shaho
 */
public class EstrategicoJpaController extends JpaPadre {

    public EstrategicoJpaController(String empresa) {
        super(empresa);
    }

    public String listarJson() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery("select compro,descripcion from estrategico");
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("compro", fila[0]);
                jsonObj.put("descripcion", fila[1]);
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

    public void create(Estrategico estrategico) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(estrategico);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEstrategico(estrategico.getCompro()) != null) {
                throw new PreexistingEntityException("Estrategico " + estrategico + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(Estrategico estrategico) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            estrategico = em.merge(estrategico);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = estrategico.getCompro();
                if (findEstrategico(id) == null) {
                    throw new NonexistentEntityException("The estrategico with id " + id + " no longer exists.");
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
            Estrategico estrategico;
            try {
                estrategico = em.getReference(Estrategico.class, id);
                estrategico.getCompro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estrategico with id " + id + " no longer exists.", enfe);
            }
            em.remove(estrategico);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<Estrategico> findEstrategicoEntities() {
        return findEstrategicoEntities(true, -1, -1);
    }

    public List<Estrategico> findEstrategicoEntities(int maxResults, int firstResult) {
        return findEstrategicoEntities(false, maxResults, firstResult);
    }

    private List<Estrategico> findEstrategicoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estrategico.class));
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

    public Estrategico findEstrategico(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(Estrategico.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getEstrategicoCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estrategico> rt = cq.from(Estrategico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
