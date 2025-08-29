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
import dto.FaSubalmacenes;

/**
 *
 * @author USUARIO
 */
public class FaSubalmacenesJpaController extends JpaPadre {

    public FaSubalmacenesJpaController(String empresa) {
        super(empresa);
    }

    public String listar() {
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            Query query = em.createNamedQuery("FaSubalmacenes.findAll");
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codsubalm", fila[0]);
                jsonObj.put("dessubalm", fila[1]);
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

    public String listarinmov(int codsubalm, String codalm) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();

            // Construir la consulta JPQL dinámicamente
            StringBuilder jpql = new StringBuilder(
                    "select v.codpro, v.codlot, p.despro, FORMAT(v.fecven, 'dd-MM-yyyy'), ");
            jpql.append(
                    "case when v.qtymov - ISNULL(subalm.cantE, 0)>0 then v.qtymov - ISNULL(subalm.cantE, 0) else 0 end AS qtymov, ");
            jpql.append(
                    "case when v.qtymov_m - ISNULL(subalm.cantF, 0)>0 then v.qtymov_m - ISNULL(subalm.cantF, 0) else 0 end AS qtymov_m, ");
            jpql.append(
                    "CASE WHEN subalm.codpro is not null and subalm.cantE is null and subalm.cantF is null THEN 'S' ELSE 'N' END AS inmovil, ");
            jpql.append(
                    "p.stkfra, (case when i.codpro is not null then 'S' else 'N' end) as inmovilizado,i.cantE,i.cantF ");
            jpql.append("from view_stock_vencimientos_a1 v ");
            jpql.append("inner join fa_productos p on p.codpro = v.codpro ");
            jpql.append(
                    "inner join fa_inmovilizados i on i.codpro = v.codpro and i.codlot = v.codlot and i.codsubalm=? and i.codalm=? ");
            jpql.append(
                    "left join (select codpro,codlot,sum(cantE)as cantE,sum(cantF)as cantF from fa_inmovilizados where codsubalm!=? and codalm=? group by codpro,codlot) subalm on v.codpro=subalm.codpro and v.codlot=subalm.codlot ");

            jpql.append("where v.codalm in ('A1', 'A2') and (v.qtymov <> 0 or v.qtymov_m <> 0)");
            Query query = em.createNativeQuery(jpql.toString());

            // Establecer parámetros en la consulta
            int cant = 1;
            query.setParameter(cant++, codsubalm);
            query.setParameter(cant++, codalm);
            query.setParameter(cant++, codsubalm);
            query.setParameter(cant++, codalm);
            List<Object[]> productos = query.getResultList();
            JSONArray lista = new JSONArray();
            for (Object[] resultado : productos) {
                JSONObject obj = new JSONObject();
                obj.put("codpro", resultado[0]);
                obj.put("codlot", resultado[1]);
                obj.put("despro", resultado[2]);
                obj.put("fecven", resultado[3]);
                obj.put("qtymov", resultado[4]);
                obj.put("qtymov_m", resultado[5]);
                obj.put("inmovil", resultado[6]);
                obj.put("stkfra", resultado[7]);
                obj.put("inmov", resultado[8]);
                obj.put("cante", resultado[9]);
                obj.put("cantf", resultado[10]);
                lista.put(obj);
            }
            return lista.toString();
        } catch (Exception e) {
            return "{\"Resultado\":\"Error\",\"mensaje\":\"" + e.getMessage() + "\"}";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void create(FaSubalmacenes faSubalmacenes) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(faSubalmacenes);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(FaSubalmacenes faSubalmacenes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            faSubalmacenes = em.merge(faSubalmacenes);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = faSubalmacenes.getCodsubalm();
                if (findFaSubalmacenes(id) == null) {
                    throw new NonexistentEntityException("The faSubalmacenes with id " + id + " no longer exists.");
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
            FaSubalmacenes faSubalmacenes;
            try {
                faSubalmacenes = em.getReference(FaSubalmacenes.class, id);
                faSubalmacenes.getCodsubalm();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The faSubalmacenes with id " + id + " no longer exists.", enfe);
            }
            em.remove(faSubalmacenes);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<FaSubalmacenes> findFaSubalmacenesEntities() {
        return findFaSubalmacenesEntities(true, -1, -1);
    }

    public List<FaSubalmacenes> findFaSubalmacenesEntities(int maxResults, int firstResult) {
        return findFaSubalmacenesEntities(false, maxResults, firstResult);
    }

    private List<FaSubalmacenes> findFaSubalmacenesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FaSubalmacenes.class));
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

    public FaSubalmacenes findFaSubalmacenes(Integer id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(FaSubalmacenes.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getFaSubalmacenesCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FaSubalmacenes> rt = cq.from(FaSubalmacenes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
