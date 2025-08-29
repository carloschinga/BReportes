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
import dto.Grupos;
import dto.Usuarios;

/**
 *
 * @author USUARIO
 */
public class GruposJpaController extends JpaPadre {

    public GruposJpaController(String empresa) {
        super(empresa);
    }

    public Grupos findByGrucod(String usecod) {
        EntityManager em = null;
        Grupos resultado = null;
        try {
            em = getEntityManager();
            TypedQuery<Grupos> query = em.createNamedQuery("Grupos.findByGrucod", Grupos.class);
            query.setParameter("grucod", usecod);
            resultado = query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
        return resultado;
    }

    public void create(Grupos grupos) throws PreexistingEntityException, Exception {
        if (grupos.getUsuariosList() == null) {
            grupos.setUsuariosList(new ArrayList<Usuarios>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Usuarios> attachedUsuariosList = new ArrayList<Usuarios>();
            for (Usuarios usuariosListUsuariosToAttach : grupos.getUsuariosList()) {
                usuariosListUsuariosToAttach = em.getReference(usuariosListUsuariosToAttach.getClass(),
                        usuariosListUsuariosToAttach.getUsecod());
                attachedUsuariosList.add(usuariosListUsuariosToAttach);
            }
            grupos.setUsuariosList(attachedUsuariosList);
            em.persist(grupos);
            for (Usuarios usuariosListUsuarios : grupos.getUsuariosList()) {
                Grupos oldGrucodOfUsuariosListUsuarios = usuariosListUsuarios.getGrucod();
                usuariosListUsuarios.setGrucod(grupos);
                usuariosListUsuarios = em.merge(usuariosListUsuarios);
                if (oldGrucodOfUsuariosListUsuarios != null) {
                    oldGrucodOfUsuariosListUsuarios.getUsuariosList().remove(usuariosListUsuarios);
                    oldGrucodOfUsuariosListUsuarios = em.merge(oldGrucodOfUsuariosListUsuarios);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGrupos(grupos.getGrucod()) != null) {
                throw new PreexistingEntityException("Grupos " + grupos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(Grupos grupos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupos persistentGrupos = em.find(Grupos.class, grupos.getGrucod());
            List<Usuarios> usuariosListOld = persistentGrupos.getUsuariosList();
            List<Usuarios> usuariosListNew = grupos.getUsuariosList();
            List<String> illegalOrphanMessages = null;
            for (Usuarios usuariosListOldUsuarios : usuariosListOld) {
                if (!usuariosListNew.contains(usuariosListOldUsuarios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Usuarios " + usuariosListOldUsuarios
                            + " since its grucod field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Usuarios> attachedUsuariosListNew = new ArrayList<Usuarios>();
            for (Usuarios usuariosListNewUsuariosToAttach : usuariosListNew) {
                usuariosListNewUsuariosToAttach = em.getReference(usuariosListNewUsuariosToAttach.getClass(),
                        usuariosListNewUsuariosToAttach.getUsecod());
                attachedUsuariosListNew.add(usuariosListNewUsuariosToAttach);
            }
            usuariosListNew = attachedUsuariosListNew;
            grupos.setUsuariosList(usuariosListNew);
            grupos = em.merge(grupos);
            for (Usuarios usuariosListNewUsuarios : usuariosListNew) {
                if (!usuariosListOld.contains(usuariosListNewUsuarios)) {
                    Grupos oldGrucodOfUsuariosListNewUsuarios = usuariosListNewUsuarios.getGrucod();
                    usuariosListNewUsuarios.setGrucod(grupos);
                    usuariosListNewUsuarios = em.merge(usuariosListNewUsuarios);
                    if (oldGrucodOfUsuariosListNewUsuarios != null
                            && !oldGrucodOfUsuariosListNewUsuarios.equals(grupos)) {
                        oldGrucodOfUsuariosListNewUsuarios.getUsuariosList().remove(usuariosListNewUsuarios);
                        oldGrucodOfUsuariosListNewUsuarios = em.merge(oldGrucodOfUsuariosListNewUsuarios);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = grupos.getGrucod();
                if (findGrupos(id) == null) {
                    throw new NonexistentEntityException("The grupos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Grupos grupos;
            try {
                grupos = em.getReference(Grupos.class, id);
                grupos.getGrucod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grupos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Usuarios> usuariosListOrphanCheck = grupos.getUsuariosList();
            for (Usuarios usuariosListOrphanCheckUsuarios : usuariosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Grupos (" + grupos + ") cannot be destroyed since the Usuarios "
                        + usuariosListOrphanCheckUsuarios
                        + " in its usuariosList field has a non-nullable grucod field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(grupos);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<Grupos> findGruposEntities() {
        return findGruposEntities(true, -1, -1);
    }

    public List<Grupos> findGruposEntities(int maxResults, int firstResult) {
        return findGruposEntities(false, maxResults, firstResult);
    }

    private List<Grupos> findGruposEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grupos.class));
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

    public Grupos findGrupos(String id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(Grupos.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getGruposCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grupos> rt = cq.from(Grupos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
