package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.json.JSONObject;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.Grupos;
import dto.SistemaUsuario;
import dto.Usuarios;

/**
 *
 * @author USUARIO
 */
public class UsuariosJpaController extends JpaPadre {

    public UsuariosJpaController(String empresa) {
        super(empresa);
    }

    public Usuarios findByusecod(int usecod) {
        EntityManager em = null;
        Usuarios resultado = null;
        try {
            em = getEntityManager();
            TypedQuery<Usuarios> query = em.createNamedQuery("Usuarios.findByUsecod", Usuarios.class);
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

    public String validar(String useusr, String usepas, byte[] usepasc) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            StringBuilder string = new StringBuilder();
            string.append(
                    "SELECT 'l' AS de, u.usecod, u.useusr, u.grucod, g.grudes, s.siscod, s.sisent,su.codalm_inv,a.central,u.usenam ");
            string.append("FROM usuarios u ");
            string.append("LEFT JOIN grupos g ON u.grucod = g.grucod ");
            string.append("INNER JOIN sistema_usuario su ON su.usecod=u.usecod ");
            string.append("INNER JOIN fa_almacenes a ON a.codalm=su.codalm_inv ");
            string.append("INNER JOIN sistema s ON s.siscod=a.siscod ");
            string.append("WHERE u.useusr = ? AND u.usepas = ? ");
            Query query = em.createNativeQuery(string.toString());
            query.setParameter(1, useusr);
            query.setParameter(2, usepas);
            Object[] resultados = (Object[]) query.getSingleResult();
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("resultado", "OK");
            jsonObj.put("de", resultados[0]);
            jsonObj.put("usecod", resultados[1]);
            jsonObj.put("useusr", resultados[2]);
            jsonObj.put("grucod", resultados[3]);
            jsonObj.put("grudes", resultados[4]);
            jsonObj.put("siscod", resultados[5]);
            jsonObj.put("sisent", resultados[6]);
            jsonObj.put("codalm_inv", resultados[7]);
            jsonObj.put("central", resultados[8]);
            jsonObj.put("nombre", resultados[9]);
            return jsonObj.toString();
        } catch (NoResultException e) {
            return "{\"resultado\":\"Error\",\"mensaje\":\"" + "Error de usuario o contraseña" + "\"}";
        } catch (Exception e) {
            return "{\"resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String validarbartolito(String useusr, String usepas, byte[] usepasc) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            StringBuilder string = new StringBuilder();
            string.append(
                    "SELECT 'b', b.usecod, b.useusr, b.grucod, 'Bartolito', a.siscod, s.sisent,b.codalm, b.grucod ")
                    .append("FROM usuarios_bartolito b ")
                    .append("INNER JOIN fa_almacenes a ON a.codalm=b.codalm ")
                    .append("INNER JOIN sistema s ON s.siscod=a.siscod ")
                    .append("where b.useusr=? and b.usepas=? and b.estado='S'");
            Query query = em.createNativeQuery(string.toString());
            query.setParameter(1, useusr);
            query.setParameter(2, usepasc);
            Object[] resultados = (Object[]) query.getSingleResult();
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("resultado", "OK");
            jsonObj.put("de", resultados[0]);
            jsonObj.put("usecod", resultados[1]);
            jsonObj.put("useusr", resultados[2]);
            jsonObj.put("grucod", resultados[3]);
            jsonObj.put("grudes", resultados[4]);
            jsonObj.put("siscod", resultados[5]);
            jsonObj.put("sisent", resultados[6]);
            jsonObj.put("codalm_inv", resultados[7]);
            return jsonObj.toString();
        } catch (NoResultException e) {
            return "{\"resultado\":\"Error\",\"mensaje\":\"" + "Error de usuario o contraseña" + "\"}";
        } catch (Exception e) {
            return "{\"resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String validarInventario(String useusr, String usepas, byte[] usepasc) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            StringBuilder string = new StringBuilder();
            string.append("SELECT 'i', b.usecod, b.useusr, b.grucod, 'Inventario', 0, '','' ")
                    .append("FROM usuarios_inventario b ")
                    .append("where b.useusr=? and b.usepas=? and b.estado='S'");
            Query query = em.createNativeQuery(string.toString());
            query.setParameter(1, useusr);
            query.setParameter(2, usepasc);
            Object[] resultados = (Object[]) query.getSingleResult();
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("resultado", "OK");
            jsonObj.put("de", resultados[0]);
            jsonObj.put("usecod", resultados[1]);
            jsonObj.put("useusr", resultados[2]);
            jsonObj.put("grucod", resultados[3]);
            jsonObj.put("grudes", resultados[4]);
            jsonObj.put("siscod", resultados[5]);
            jsonObj.put("sisent", resultados[6]);
            jsonObj.put("codalm_inv", resultados[7]);
            return jsonObj.toString();
        } catch (NoResultException e) {
            return "{\"resultado\":\"Error\",\"mensaje\":\"" + "Error de usuario o contraseña" + "\"}";
        } catch (Exception e) {
            return "{\"resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String buscarLogiBarto(String useusr) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            StringBuilder string = new StringBuilder();
            string.append("SELECT 'l',u.usecod,u.useusr ")
                    .append("FROM usuarios u ")
                    .append("where u.useusr=? ")
                    .append("UNION ")
                    .append("SELECT 'b',usecod, useusr ")
                    .append("FROM usuarios_bartolito where useusr=? and estado='S'")
                    .append("UNION ")
                    .append("SELECT 'i',usecod, useusr ")
                    .append("FROM usuarios_inventario where useusr=? and estado='S'");
            Query query = em.createNativeQuery(string.toString());
            query.setParameter(1, useusr);
            query.setParameter(2, useusr);
            query.setParameter(3, useusr);
            Object[] resultados = (Object[]) query.getSingleResult();
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("resultado", "OK");
            jsonObj.put("de", resultados[0]);
            return String.valueOf(resultados[1]);
        } catch (NoResultException e) {
            return "noencontrado";
        } catch (Exception e) {
            return "error";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String Listar() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNamedQuery("Usuarios.findAll");
            List<Object[]> resultados = query.getResultList();
            String cadena = "[";
            boolean s = true;
            for (Object[] fila : resultados) {
                s = false;
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("usecod", fila[0]);
                jsonObj.put("usenam", fila[1]);
                jsonObj.put("usedoc", fila[2]);
                cadena = cadena + jsonObj.toString() + ",";
            }
            if (s) {
                cadena = "[]";
            } else {
                cadena = cadena.substring(0, cadena.length() - 1) + "]";
            }
            return cadena;

        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String ListDistr() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNamedQuery("Usuarios.findAllDist");
            List<Object[]> resultados = query.getResultList();
            String cadena = "[";
            boolean s = true;
            for (Object[] fila : resultados) {
                s = false;
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("usecod", fila[0]);
                jsonObj.put("usenam", fila[1]);
                cadena = cadena + jsonObj.toString() + ",";
            }
            if (s) {
                cadena = "[]";
            } else {
                cadena = cadena.substring(0, cadena.length() - 1) + "]";
            }
            return cadena;

        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void create(Usuarios usuarios) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SistemaUsuario sistemaUsuario = usuarios.getSistemaUsuario();
            if (sistemaUsuario != null) {
                sistemaUsuario = em.getReference(sistemaUsuario.getClass(), sistemaUsuario.getUsecod());
                usuarios.setSistemaUsuario(sistemaUsuario);
            }
            Grupos grucod = usuarios.getGrucod();
            if (grucod != null) {
                grucod = em.getReference(grucod.getClass(), grucod.getGrucod());
                usuarios.setGrucod(grucod);
            }
            em.persist(usuarios);
            if (sistemaUsuario != null) {
                Usuarios oldUsuariosOfSistemaUsuario = sistemaUsuario.getUsuarios();
                if (oldUsuariosOfSistemaUsuario != null) {
                    oldUsuariosOfSistemaUsuario.setSistemaUsuario(null);
                    oldUsuariosOfSistemaUsuario = em.merge(oldUsuariosOfSistemaUsuario);
                }
                sistemaUsuario.setUsuarios(usuarios);
                sistemaUsuario = em.merge(sistemaUsuario);
            }
            if (grucod != null) {
                grucod.getUsuariosList().add(usuarios);
                grucod = em.merge(grucod);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuarios(usuarios.getUsecod()) != null) {
                throw new PreexistingEntityException("Usuarios " + usuarios + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(Usuarios usuarios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getUsecod());
            SistemaUsuario sistemaUsuarioOld = persistentUsuarios.getSistemaUsuario();
            SistemaUsuario sistemaUsuarioNew = usuarios.getSistemaUsuario();
            Grupos grucodOld = persistentUsuarios.getGrucod();
            Grupos grucodNew = usuarios.getGrucod();
            List<String> illegalOrphanMessages = null;
            if (sistemaUsuarioOld != null && !sistemaUsuarioOld.equals(sistemaUsuarioNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain SistemaUsuario " + sistemaUsuarioOld
                        + " since its usuarios field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (sistemaUsuarioNew != null) {
                sistemaUsuarioNew = em.getReference(sistemaUsuarioNew.getClass(), sistemaUsuarioNew.getUsecod());
                usuarios.setSistemaUsuario(sistemaUsuarioNew);
            }
            if (grucodNew != null) {
                grucodNew = em.getReference(grucodNew.getClass(), grucodNew.getGrucod());
                usuarios.setGrucod(grucodNew);
            }
            usuarios = em.merge(usuarios);
            if (sistemaUsuarioNew != null && !sistemaUsuarioNew.equals(sistemaUsuarioOld)) {
                Usuarios oldUsuariosOfSistemaUsuario = sistemaUsuarioNew.getUsuarios();
                if (oldUsuariosOfSistemaUsuario != null) {
                    oldUsuariosOfSistemaUsuario.setSistemaUsuario(null);
                    oldUsuariosOfSistemaUsuario = em.merge(oldUsuariosOfSistemaUsuario);
                }
                sistemaUsuarioNew.setUsuarios(usuarios);
                sistemaUsuarioNew = em.merge(sistemaUsuarioNew);
            }
            if (grucodOld != null && !grucodOld.equals(grucodNew)) {
                grucodOld.getUsuariosList().remove(usuarios);
                grucodOld = em.merge(grucodOld);
            }
            if (grucodNew != null && !grucodNew.equals(grucodOld)) {
                grucodNew.getUsuariosList().add(usuarios);
                grucodNew = em.merge(grucodNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuarios.getUsecod();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getUsecod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            SistemaUsuario sistemaUsuarioOrphanCheck = usuarios.getSistemaUsuario();
            if (sistemaUsuarioOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuarios (" + usuarios
                        + ") cannot be destroyed since the SistemaUsuario " + sistemaUsuarioOrphanCheck
                        + " in its sistemaUsuario field has a non-nullable usuarios field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Grupos grucod = usuarios.getGrucod();
            if (grucod != null) {
                grucod.getUsuariosList().remove(usuarios);
                grucod = em.merge(grucod);
            }
            em.remove(usuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
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

    public Usuarios findUsuarios(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(Usuarios.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
