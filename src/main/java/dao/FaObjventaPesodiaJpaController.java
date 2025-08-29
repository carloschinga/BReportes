package dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import dao.exceptions.NonexistentEntityException;
import dto.FaObjventaPesodia;

/**
 *
 * @author sbdeveloperw
 */
public class FaObjventaPesodiaJpaController extends JpaPadre {

    public FaObjventaPesodiaJpaController(String empresa) {
        super(empresa);
    }

    public void create(FaObjventaPesodia faObjventaPesodia) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(faObjventaPesodia);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(FaObjventaPesodia faObjventaPesodia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            faObjventaPesodia = em.merge(faObjventaPesodia);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = faObjventaPesodia.getCodopd();
                if (findFaObjventaPesodia(id) == null) {
                    throw new NonexistentEntityException("El registro con id " + id + " ya no existe.");
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
            FaObjventaPesodia entidad;
            try {
                entidad = em.getReference(FaObjventaPesodia.class, id);
                entidad.getCodopd(); // asegura que se accede a su ID
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("El registro con id " + id + " ya no existe.", enfe);
            }
            em.remove(entidad);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<FaObjventaPesodia> findFaObjventaPesodiaEntities() {
        return findFaObjventaPesodiaEntities(true, -1, -1);
    }

    public List<FaObjventaPesodia> findFaObjventaPesodiaEntities(int maxResults, int firstResult) {
        return findFaObjventaPesodiaEntities(false, maxResults, firstResult);
    }

    private List<FaObjventaPesodia> findFaObjventaPesodiaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FaObjventaPesodia.class));
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

    public FaObjventaPesodia findFaObjventaPesodia(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(FaObjventaPesodia.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getFaObjventaPesodiaCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FaObjventaPesodia> rt = cq.from(FaObjventaPesodia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<FaObjventaPesodia> findByCodobjAndSiscod(int codobj, int siscod) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query q = em.createQuery(
                    "SELECT f FROM FaObjventaPesodia f WHERE f.codobj = :codobj AND f.siscod = :siscod AND (f.eliminado IS NULL OR f.eliminado = false) ORDER BY f.numdia ASC");
            q.setParameter("codobj", codobj);
            q.setParameter("siscod", siscod);
            return q.getResultList();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void guardarOActualizarLote(List<FaObjventaPesodia> lista) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            for (FaObjventaPesodia item : lista) {
                Query q = em.createQuery(
                        "SELECT f FROM FaObjventaPesodia f WHERE f.codobj = :codobj AND f.siscod = :siscod AND f.numdia = :numdia");
                q.setParameter("codobj", item.getCodobj());
                q.setParameter("siscod", item.getSiscod());
                q.setParameter("numdia", item.getNumdia());

                List<FaObjventaPesodia> encontrados = q.getResultList();

                if (!encontrados.isEmpty()) {
                    FaObjventaPesodia existente = encontrados.get(0);
                    if (Boolean.TRUE.equals(existente.getEliminado())) {
                        existente.setEliminado(false);
                        existente.setPeso(item.getPeso());
                        existente.setFecmod(new Date());
                        existente.setUsemod(item.getUsemod());
                        em.merge(existente);
                    } else {
                        existente.setPeso(item.getPeso());
                        existente.setFecmod(new Date());
                        existente.setUsemod(item.getUsemod());
                        em.merge(existente);
                    }
                } else {
                    item.setFeccre(new Date());
                    item.setEliminado(false);
                    em.persist(item);
                }
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void softDelete(Integer id, Integer userId) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FaObjventaPesodia entidad = em.find(FaObjventaPesodia.class, id);

            if (entidad == null) {
                throw new NonexistentEntityException("No existe el registro con id: " + id);
            }

            entidad.setEliminado(true);
            entidad.setFecmod(new Date());
            entidad.setUsemod(userId);
            em.merge(entidad);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }
}
