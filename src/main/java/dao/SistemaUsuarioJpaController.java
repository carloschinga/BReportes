package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.SistemaUsuario;
import dto.Usuarios;

/**
 *
 * @author USUARIO
 */
public class SistemaUsuarioJpaController extends JpaPadre {

    public SistemaUsuarioJpaController(String empresa) {
        super(empresa);
    }

    public SistemaUsuario findByUsecod(int usecod) {
        EntityManager em = null;
        SistemaUsuario resultado = null;
        try {
            em = getEntityManager();
            TypedQuery<SistemaUsuario> query = em.createNamedQuery("SistemaUsuario.findByUsecod", SistemaUsuario.class);
            query.setParameter("usecod", usecod);
            resultado = query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
        return resultado;
    }

    public void create(SistemaUsuario sistemaUsuario)
            throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Usuarios usuariosOrphanCheck = sistemaUsuario.getUsuarios();
        if (usuariosOrphanCheck != null) {
            SistemaUsuario oldSistemaUsuarioOfUsuarios = usuariosOrphanCheck.getSistemaUsuario();
            if (oldSistemaUsuarioOfUsuarios != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Usuarios " + usuariosOrphanCheck
                        + " already has an item of type SistemaUsuario whose usuarios column cannot be null. Please make another selection for the usuarios field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios usuarios = sistemaUsuario.getUsuarios();
            if (usuarios != null) {
                usuarios = em.getReference(usuarios.getClass(), usuarios.getUsecod());
                sistemaUsuario.setUsuarios(usuarios);
            }
            em.persist(sistemaUsuario);
            if (usuarios != null) {
                usuarios.setSistemaUsuario(sistemaUsuario);
                usuarios = em.merge(usuarios);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSistemaUsuario(sistemaUsuario.getUsecod()) != null) {
                throw new PreexistingEntityException("SistemaUsuario " + sistemaUsuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(SistemaUsuario sistemaUsuario)
            throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SistemaUsuario persistentSistemaUsuario = em.find(SistemaUsuario.class, sistemaUsuario.getUsecod());
            Usuarios usuariosOld = persistentSistemaUsuario.getUsuarios();
            Usuarios usuariosNew = sistemaUsuario.getUsuarios();
            List<String> illegalOrphanMessages = null;
            if (usuariosNew != null && !usuariosNew.equals(usuariosOld)) {
                SistemaUsuario oldSistemaUsuarioOfUsuarios = usuariosNew.getSistemaUsuario();
                if (oldSistemaUsuarioOfUsuarios != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Usuarios " + usuariosNew
                            + " already has an item of type SistemaUsuario whose usuarios column cannot be null. Please make another selection for the usuarios field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (usuariosNew != null) {
                usuariosNew = em.getReference(usuariosNew.getClass(), usuariosNew.getUsecod());
                sistemaUsuario.setUsuarios(usuariosNew);
            }
            sistemaUsuario = em.merge(sistemaUsuario);
            if (usuariosOld != null && !usuariosOld.equals(usuariosNew)) {
                usuariosOld.setSistemaUsuario(null);
                usuariosOld = em.merge(usuariosOld);
            }
            if (usuariosNew != null && !usuariosNew.equals(usuariosOld)) {
                usuariosNew.setSistemaUsuario(sistemaUsuario);
                usuariosNew = em.merge(usuariosNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = sistemaUsuario.getUsecod();
                if (findSistemaUsuario(id) == null) {
                    throw new NonexistentEntityException("The sistemaUsuario with id " + id + " no longer exists.");
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
            SistemaUsuario sistemaUsuario;
            try {
                sistemaUsuario = em.getReference(SistemaUsuario.class, id);
                sistemaUsuario.getUsecod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sistemaUsuario with id " + id + " no longer exists.", enfe);
            }
            Usuarios usuarios = sistemaUsuario.getUsuarios();
            if (usuarios != null) {
                usuarios.setSistemaUsuario(null);
                usuarios = em.merge(usuarios);
            }
            em.remove(sistemaUsuario);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<SistemaUsuario> findSistemaUsuarioEntities() {
        return findSistemaUsuarioEntities(true, -1, -1);
    }

    public List<SistemaUsuario> findSistemaUsuarioEntities(int maxResults, int firstResult) {
        return findSistemaUsuarioEntities(false, maxResults, firstResult);
    }

    private List<SistemaUsuario> findSistemaUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SistemaUsuario.class));
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

    public SistemaUsuario findSistemaUsuario(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(SistemaUsuario.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getSistemaUsuarioCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SistemaUsuario> rt = cq.from(SistemaUsuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
