package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.Objesiscod;
import dto.ObjesiscodPK;

/**
 *
 * @author shaho
 */
public class ObjesiscodJpaController extends JpaPadre {

    public ObjesiscodJpaController(String empresa) {
        super(empresa);
    }

    public void create(Objesiscod objesiscod) throws PreexistingEntityException, Exception {
        if (objesiscod.getObjesiscodPK() == null) {
            objesiscod.setObjesiscodPK(new ObjesiscodPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(objesiscod);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findObjesiscod(objesiscod.getObjesiscodPK()) != null) {
                throw new PreexistingEntityException("Objesiscod " + objesiscod + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(Objesiscod objesiscod) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            objesiscod = em.merge(objesiscod);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ObjesiscodPK id = objesiscod.getObjesiscodPK();
                if (findObjesiscod(id) == null) {
                    throw new NonexistentEntityException("The objesiscod with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(ObjesiscodPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Objesiscod objesiscod;
            try {
                objesiscod = em.getReference(Objesiscod.class, id);
                objesiscod.getObjesiscodPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The objesiscod with id " + id + " no longer exists.", enfe);
            }
            em.remove(objesiscod);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<Objesiscod> findObjesiscodEntities() {
        return findObjesiscodEntities(true, -1, -1);
    }

    public List<Objesiscod> findObjesiscodEntities(int maxResults, int firstResult) {
        return findObjesiscodEntities(false, maxResults, firstResult);
    }

    private List<Objesiscod> findObjesiscodEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Objesiscod.class));
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

    public Objesiscod findObjesiscod(ObjesiscodPK id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(Objesiscod.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getObjesiscodCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Objesiscod> rt = cq.from(Objesiscod.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<Objesiscod> findByCodobj(int codobj) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            TypedQuery<Objesiscod> query = em.createNamedQuery("Objesiscod.findByCodobj", Objesiscod.class);
            query.setParameter("codobj", codobj);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public Objesiscod findByCodobjAndSiscod(int codobj, int siscod) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(Objesiscod.class, new ObjesiscodPK(codobj, siscod));
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String buscarMontoAsignadoJson(int codobj, int siscod) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em
                    .createNativeQuery("SELECT soles FROM fa_objventa_farmacia WHERE codobj = ? AND siscod = ?");
            query.setParameter(1, codobj);
            query.setParameter(2, siscod);

            Object resultado = query.getSingleResult();
            return resultado != null ? resultado.toString() : "0";
        } catch (NoResultException e) {
            return "0";
        } catch (Exception e) {
            return "-1"; // Puedes manejar errores con otro valor especial
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
