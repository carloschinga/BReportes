package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.DocumentosPago;
import dto.Sistema;
import dto.TipoDocumentoIdentidad;
import dto.Ubigeo;

/**
 *
 * @author USUARIO
 */
public class SistemaJpaController extends JpaPadre {

    public SistemaJpaController(String empresa) {
        super(empresa);
    }

    public String listarJson() {// este metodo devuelve un json con el codigo y el nombre del establecimiento
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            Query query = em.createNamedQuery("Sistema.findAllJSON");
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("siscod", fila[0]);
                jsonObj.put("sisent", fila[1]);
                jsonObj.put("sisdlvsta", fila[2]);
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

    public void create(Sistema sistema) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DocumentosPago sisforpagVen = sistema.getSisforpagVen();
            if (sisforpagVen != null) {
                sisforpagVen = em.getReference(sisforpagVen.getClass(), sisforpagVen.getDocpag());
                sistema.setSisforpagVen(sisforpagVen);
            }
            DocumentosPago sisforpagcrecliVen = sistema.getSisforpagcrecliVen();
            if (sisforpagcrecliVen != null) {
                sisforpagcrecliVen = em.getReference(sisforpagcrecliVen.getClass(), sisforpagcrecliVen.getDocpag());
                sistema.setSisforpagcrecliVen(sisforpagcrecliVen);
            }
            TipoDocumentoIdentidad sistidcod = sistema.getSistidcod();
            if (sistidcod != null) {
                sistidcod = em.getReference(sistidcod.getClass(), sistidcod.getTidcod());
                sistema.setSistidcod(sistidcod);
            }
            Ubigeo sisubicod = sistema.getSisubicod();
            if (sisubicod != null) {
                sisubicod = em.getReference(sisubicod.getClass(), sisubicod.getUbicod());
                sistema.setSisubicod(sisubicod);
            }
            em.persist(sistema);
            if (sisforpagVen != null) {
                sisforpagVen.getSistemaCollection().add(sistema);
                sisforpagVen = em.merge(sisforpagVen);
            }
            if (sisforpagcrecliVen != null) {
                sisforpagcrecliVen.getSistemaCollection().add(sistema);
                sisforpagcrecliVen = em.merge(sisforpagcrecliVen);
            }
            if (sistidcod != null) {
                sistidcod.getSistemaCollection().add(sistema);
                sistidcod = em.merge(sistidcod);
            }
            if (sisubicod != null) {
                sisubicod.getSistemaCollection().add(sistema);
                sisubicod = em.merge(sisubicod);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSistema(sistema.getSiscod()) != null) {
                throw new PreexistingEntityException("Sistema " + sistema + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(Sistema sistema) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Sistema persistentSistema = em.find(Sistema.class, sistema.getSiscod());
            DocumentosPago sisforpagVenOld = persistentSistema.getSisforpagVen();
            DocumentosPago sisforpagVenNew = sistema.getSisforpagVen();
            DocumentosPago sisforpagcrecliVenOld = persistentSistema.getSisforpagcrecliVen();
            DocumentosPago sisforpagcrecliVenNew = sistema.getSisforpagcrecliVen();
            TipoDocumentoIdentidad sistidcodOld = persistentSistema.getSistidcod();
            TipoDocumentoIdentidad sistidcodNew = sistema.getSistidcod();
            Ubigeo sisubicodOld = persistentSistema.getSisubicod();
            Ubigeo sisubicodNew = sistema.getSisubicod();
            if (sisforpagVenNew != null) {
                sisforpagVenNew = em.getReference(sisforpagVenNew.getClass(), sisforpagVenNew.getDocpag());
                sistema.setSisforpagVen(sisforpagVenNew);
            }
            if (sisforpagcrecliVenNew != null) {
                sisforpagcrecliVenNew = em.getReference(sisforpagcrecliVenNew.getClass(),
                        sisforpagcrecliVenNew.getDocpag());
                sistema.setSisforpagcrecliVen(sisforpagcrecliVenNew);
            }
            if (sistidcodNew != null) {
                sistidcodNew = em.getReference(sistidcodNew.getClass(), sistidcodNew.getTidcod());
                sistema.setSistidcod(sistidcodNew);
            }
            if (sisubicodNew != null) {
                sisubicodNew = em.getReference(sisubicodNew.getClass(), sisubicodNew.getUbicod());
                sistema.setSisubicod(sisubicodNew);
            }
            sistema = em.merge(sistema);
            if (sisforpagVenOld != null && !sisforpagVenOld.equals(sisforpagVenNew)) {
                sisforpagVenOld.getSistemaCollection().remove(sistema);
                sisforpagVenOld = em.merge(sisforpagVenOld);
            }
            if (sisforpagVenNew != null && !sisforpagVenNew.equals(sisforpagVenOld)) {
                sisforpagVenNew.getSistemaCollection().add(sistema);
                sisforpagVenNew = em.merge(sisforpagVenNew);
            }
            if (sisforpagcrecliVenOld != null && !sisforpagcrecliVenOld.equals(sisforpagcrecliVenNew)) {
                sisforpagcrecliVenOld.getSistemaCollection().remove(sistema);
                sisforpagcrecliVenOld = em.merge(sisforpagcrecliVenOld);
            }
            if (sisforpagcrecliVenNew != null && !sisforpagcrecliVenNew.equals(sisforpagcrecliVenOld)) {
                sisforpagcrecliVenNew.getSistemaCollection().add(sistema);
                sisforpagcrecliVenNew = em.merge(sisforpagcrecliVenNew);
            }
            if (sistidcodOld != null && !sistidcodOld.equals(sistidcodNew)) {
                sistidcodOld.getSistemaCollection().remove(sistema);
                sistidcodOld = em.merge(sistidcodOld);
            }
            if (sistidcodNew != null && !sistidcodNew.equals(sistidcodOld)) {
                sistidcodNew.getSistemaCollection().add(sistema);
                sistidcodNew = em.merge(sistidcodNew);
            }
            if (sisubicodOld != null && !sisubicodOld.equals(sisubicodNew)) {
                sisubicodOld.getSistemaCollection().remove(sistema);
                sisubicodOld = em.merge(sisubicodOld);
            }
            if (sisubicodNew != null && !sisubicodNew.equals(sisubicodOld)) {
                sisubicodNew.getSistemaCollection().add(sistema);
                sisubicodNew = em.merge(sisubicodNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = sistema.getSiscod();
                if (findSistema(id) == null) {
                    throw new NonexistentEntityException("The sistema with id " + id + " no longer exists.");
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
            Sistema sistema;
            try {
                sistema = em.getReference(Sistema.class, id);
                sistema.getSiscod();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The sistema with id " + id + " no longer exists.", enfe);
            }
            DocumentosPago sisforpagVen = sistema.getSisforpagVen();
            if (sisforpagVen != null) {
                sisforpagVen.getSistemaCollection().remove(sistema);
                sisforpagVen = em.merge(sisforpagVen);
            }
            DocumentosPago sisforpagcrecliVen = sistema.getSisforpagcrecliVen();
            if (sisforpagcrecliVen != null) {
                sisforpagcrecliVen.getSistemaCollection().remove(sistema);
                sisforpagcrecliVen = em.merge(sisforpagcrecliVen);
            }
            TipoDocumentoIdentidad sistidcod = sistema.getSistidcod();
            if (sistidcod != null) {
                sistidcod.getSistemaCollection().remove(sistema);
                sistidcod = em.merge(sistidcod);
            }
            Ubigeo sisubicod = sistema.getSisubicod();
            if (sisubicod != null) {
                sisubicod.getSistemaCollection().remove(sistema);
                sisubicod = em.merge(sisubicod);
            }
            em.remove(sistema);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<Sistema> findSistemaEntities() {
        return findSistemaEntities(true, -1, -1);
    }

    public List<Sistema> findSistemaEntities(int maxResults, int firstResult) {
        return findSistemaEntities(false, maxResults, firstResult);
    }

    private List<Sistema> findSistemaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Sistema.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Sistema findSistema(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(Sistema.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getSistemaCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Sistema> rt = cq.from(Sistema.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
