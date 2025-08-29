package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.FaInmovilizados;
import dto.FaInmovilizadosPK;

/**
 *
 * @author USUARIO
 */
public class FaInmovilizadosJpaController extends JpaPadre {

    public FaInmovilizadosJpaController(String empresa) {
        super(empresa);
    }

    public String aplicarinmovilizadosJSON(String codpro, String codlot, int codsubalm, String estado, Integer cante,
            Integer cantf, String codalm) {
        try {
            FaInmovilizadosJpaController dao = new FaInmovilizadosJpaController(empresa);
            if (estado.equals("false")) {
                FaInmovilizadosPK pk = new FaInmovilizadosPK(codpro, codlot, codsubalm, codalm);
                FaInmovilizados obj = dao.findFaInmovilizados(pk);
                if (obj != null) {
                    dao.destroy(obj.getFaInmovilizadosPK());
                    return "{\"resultado\":\"ok\"}";
                } else {
                    return "{\"resultado\":\"error\",\"mensaje\":\"No se encontro el registro\"}";
                }
            } else {
                FaInmovilizadosPK pk = new FaInmovilizadosPK(codpro, codlot, codsubalm, codalm);
                FaInmovilizados obj = dao.findFaInmovilizados(pk);
                if (obj == null) {
                    if (cante == null && cantf == null) {
                        deleteSubalmacen(codpro, codlot, codalm);
                    }
                    FaInmovilizados obj1 = new FaInmovilizados(codpro, codlot, codsubalm, codalm);
                    obj1.setCantE(cante);
                    obj1.setCantF(cantf);
                    dao.create(obj1);
                    return "{\"resultado\":\"ok\"}";
                } else {
                    return "{\"resultado\":\"error\",\"mensaje\":\"Ya se encontro el registro\"}";
                }
            }
        } catch (Exception e) {
            return "{\"resultado\":\"error\",\"mensaje\":\"" + e + "\"}";
        }
    }

    public void deleteSubalmacen(String codpro, String codlot, String codalm) {
        EntityManager entityManager = null;
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();

            // Crear el objeto StoredProcedureQuery
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_DeleteInmovilizados");

            query.registerStoredProcedureParameter("codpro", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("codlot", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("codalm", String.class, ParameterMode.IN);
            // Establecer el parámetro de entrada
            query.setParameter("codpro", codpro);
            query.setParameter("codlot", codlot);
            query.setParameter("codalm", codalm);

            // Ejecutar el procedimiento
            query.execute();

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    public void create(FaInmovilizados faInmovilizados) throws PreexistingEntityException, Exception {
        if (faInmovilizados.getFaInmovilizadosPK() == null) {
            faInmovilizados.setFaInmovilizadosPK(new FaInmovilizadosPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(faInmovilizados);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFaInmovilizados(faInmovilizados.getFaInmovilizadosPK()) != null) {
                throw new PreexistingEntityException("FaInmovilizados " + faInmovilizados + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(FaInmovilizados faInmovilizados) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            faInmovilizados = em.merge(faInmovilizados);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                FaInmovilizadosPK id = faInmovilizados.getFaInmovilizadosPK();
                if (findFaInmovilizados(id) == null) {
                    throw new NonexistentEntityException("The faInmovilizados with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(FaInmovilizadosPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FaInmovilizados faInmovilizados;
            try {
                faInmovilizados = em.getReference(FaInmovilizados.class, id);
                faInmovilizados.getFaInmovilizadosPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The faInmovilizados with id " + id + " no longer exists.", enfe);
            }
            em.remove(faInmovilizados);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<FaInmovilizados> findFaInmovilizadosEntities() {
        return findFaInmovilizadosEntities(true, -1, -1);
    }

    public List<FaInmovilizados> findFaInmovilizadosEntities(int maxResults, int firstResult) {
        return findFaInmovilizadosEntities(false, maxResults, firstResult);
    }

    private List<FaInmovilizados> findFaInmovilizadosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FaInmovilizados.class));
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

    public FaInmovilizados findFaInmovilizados(FaInmovilizadosPK id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(FaInmovilizados.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getFaInmovilizadosCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FaInmovilizados> rt = cq.from(FaInmovilizados.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
