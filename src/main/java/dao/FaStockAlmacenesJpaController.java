package dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.json.JSONObject;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.FaProductos;
import dto.FaStockAlmacenes;
import dto.FaStockAlmacenesPK;

/**
 *
 * @author USUARIO
 */
public class FaStockAlmacenesJpaController extends JpaPadre {

    public FaStockAlmacenesJpaController(String empresa) {
        super(empresa);
    }

    public String obtenerstkalm(String codalm, String codpro) {
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            Query query = em.createNamedQuery("FaStockAlmacenes.findstkalm");
            query.setParameter("codalm", codalm);
            query.setParameter("codpro", codpro);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("stkalm", fila[0]);
                jsonObj.put("stkalm_m", fila[1]);
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

    public String obtenerstkalmproducto(String codpro, String codalm) {
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select a.desalm,s.stkalm,s.stkalm_m from fa_stock_almacenes s inner join fa_almacenes a on s.codalm=a.codalm where s.codpro=? and a.codalm=?");
            query.setParameter(1, codpro);
            query.setParameter(2, codalm);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("desalm", fila[0]);
                jsonObj.put("stkalm", fila[1]);
                jsonObj.put("stkalm_m", fila[2]);
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

    public String obtenerstkalmproductotodos(String codpro) {
        EntityManager em = null;
        String resultado = "";
        try {
            em = getEntityManager();
            Query query = em.createNativeQuery(
                    "select s.codalm,a.desalm,s.stkalm,s.stkalm_m,case when s.stkmin2=0 then null else ((s.stkalm*p.stkfra+s.stkalm_m)/s.stkmin2)/p.stkfra end,s.stkmin2 from fa_stock_almacenes s inner join fa_almacenes a on s.codalm=a.codalm inner join fa_productos p on p.codpro=s.codpro where s.codpro=? ");
            query.setParameter(1, codpro);
            List<Object[]> resultados = query.getResultList();
            JSONArray jsonArray = new JSONArray();
            for (Object[] fila : resultados) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("codalm", fila[0]);
                jsonObj.put("desalm", fila[1]);
                jsonObj.put("stkalm", fila[2]);
                jsonObj.put("stkalm_m", fila[3]);
                jsonObj.put("meses", fila[4]);
                jsonObj.put("stkmin2", fila[5]);
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

    // metodo para los inmovilizados
    public String listarProductosStocks(String codlab, String codgen, String codfam, String codtip, String codpro,
            int codsubalm, String codalm) {
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
            jpql.append("from fa_stock_vencimientos v ");
            jpql.append("inner join fa_productos p on p.codpro = v.codpro ");
            jpql.append(
                    "left join fa_inmovilizados i on i.codpro = v.codpro and i.codlot = v.codlot and i.codsubalm=? and i.codalm=? ");
            jpql.append(
                    "left join (select codpro,codlot,sum(cantE)as cantE,sum(cantF)as cantF from fa_inmovilizados where codsubalm!=? and codalm=? group by codpro,codlot) subalm on v.codpro=subalm.codpro and v.codlot=subalm.codlot ");

            jpql.append("where v.codalm = ? and (v.qtymov <> 0 or v.qtymov_m <> 0)");
            if (!codlab.equals("")) {
                jpql.append(" AND p.codlab = ?");
            }
            if (!codgen.equals("")) {
                jpql.append(" AND p.codgen = ?");
            }
            if (!codfam.equals("")) {
                jpql.append(" AND p.codfam = ?");
            }
            if (!codtip.equals("")) {
                jpql.append(" AND p.codtip = ?");
            }
            if (!codpro.equals("")) {
                jpql.append(" AND p.codpro = ?");
            }
            Query query = em.createNativeQuery(jpql.toString());

            // Establecer parámetros en la consulta
            int cant = 1;
            query.setParameter(cant++, codsubalm);
            query.setParameter(cant++, codalm);
            query.setParameter(cant++, codsubalm);
            query.setParameter(cant++, codalm);
            query.setParameter(cant++, codalm);
            if (!codlab.equals("")) {
                query.setParameter(cant++, codlab);
            }
            if (!codgen.equals("")) {
                query.setParameter(cant++, codgen);
            }
            if (!codfam.equals("")) {
                query.setParameter(cant++, codfam);
            }
            if (!codtip.equals("")) {
                query.setParameter(cant++, codtip);
            }

            if (!codpro.equals("")) {
                query.setParameter(cant++, codpro);
            }
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

    public static void main(String[] args) {
        FaStockAlmacenesJpaController dao = new FaStockAlmacenesJpaController("a");
    }

    public String indicadoresventa(String codpro, int tipo_stkmin) {
        EntityManager em = null;
        try {
            StoredProcedureQuery storedProcedure = em.createStoredProcedureQuery("sel_indicadores_venta");

            storedProcedure.registerStoredProcedureParameter("tipo_stkmin", Integer.class, ParameterMode.IN);
            storedProcedure.registerStoredProcedureParameter("codpro", String.class, ParameterMode.IN);
            storedProcedure.setParameter("codpro", codpro);
            storedProcedure.setParameter("tipo_stkmin", tipo_stkmin);

            // Ejecutar el procedimiento
            storedProcedure.execute();

            // Obtener los resultados
            Object[] result = (Object[]) storedProcedure.getSingleResult();
            Double stkalm = (Double) result[0];
            Double stockminimo = ((BigDecimal) result[1]).doubleValue();

            System.out.println("Resultado: stkalm = " + stkalm + ", stockminimo = " + stockminimo);

            return "\"resultado\":\"ok\",\"stkalm\":" + stkalm + ",\"stkmin2\":" + stockminimo;
        } catch (Exception e) {
            e.printStackTrace(); // Esto imprimirá el stack trace para depuración
            return "\"resultado\":\"error\",\"mensaje\":\"" + e.getMessage() + "\"";
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void create(FaStockAlmacenes faStockAlmacenes) throws PreexistingEntityException, Exception {
        if (faStockAlmacenes.getFaStockAlmacenesPK() == null) {
            faStockAlmacenes.setFaStockAlmacenesPK(new FaStockAlmacenesPK());
        }
        faStockAlmacenes.getFaStockAlmacenesPK().setCodpro(faStockAlmacenes.getFaProductos().getCodpro());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FaProductos faProductos = faStockAlmacenes.getFaProductos();
            if (faProductos != null) {
                faProductos = em.getReference(faProductos.getClass(), faProductos.getCodpro());
                faStockAlmacenes.setFaProductos(faProductos);
            }
            em.persist(faStockAlmacenes);
            if (faProductos != null) {
                faProductos.getFaStockAlmacenesList().add(faStockAlmacenes);
                faProductos = em.merge(faProductos);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFaStockAlmacenes(faStockAlmacenes.getFaStockAlmacenesPK()) != null) {
                throw new PreexistingEntityException("FaStockAlmacenes " + faStockAlmacenes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(FaStockAlmacenes faStockAlmacenes) throws NonexistentEntityException, Exception {
        faStockAlmacenes.getFaStockAlmacenesPK().setCodpro(faStockAlmacenes.getFaProductos().getCodpro());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FaStockAlmacenes persistentFaStockAlmacenes = em.find(FaStockAlmacenes.class,
                    faStockAlmacenes.getFaStockAlmacenesPK());
            FaProductos faProductosOld = persistentFaStockAlmacenes.getFaProductos();
            FaProductos faProductosNew = faStockAlmacenes.getFaProductos();
            if (faProductosNew != null) {
                faProductosNew = em.getReference(faProductosNew.getClass(), faProductosNew.getCodpro());
                faStockAlmacenes.setFaProductos(faProductosNew);
            }
            faStockAlmacenes = em.merge(faStockAlmacenes);
            if (faProductosOld != null && !faProductosOld.equals(faProductosNew)) {
                faProductosOld.getFaStockAlmacenesList().remove(faStockAlmacenes);
                faProductosOld = em.merge(faProductosOld);
            }
            if (faProductosNew != null && !faProductosNew.equals(faProductosOld)) {
                faProductosNew.getFaStockAlmacenesList().add(faStockAlmacenes);
                faProductosNew = em.merge(faProductosNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                FaStockAlmacenesPK id = faStockAlmacenes.getFaStockAlmacenesPK();
                if (findFaStockAlmacenes(id) == null) {
                    throw new NonexistentEntityException("The faStockAlmacenes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void destroy(FaStockAlmacenesPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FaStockAlmacenes faStockAlmacenes;
            try {
                faStockAlmacenes = em.getReference(FaStockAlmacenes.class, id);
                faStockAlmacenes.getFaStockAlmacenesPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The faStockAlmacenes with id " + id + " no longer exists.", enfe);
            }
            FaProductos faProductos = faStockAlmacenes.getFaProductos();
            if (faProductos != null) {
                faProductos.getFaStockAlmacenesList().remove(faStockAlmacenes);
                faProductos = em.merge(faProductos);
            }
            em.remove(faStockAlmacenes);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<FaStockAlmacenes> findFaStockAlmacenesEntities() {
        return findFaStockAlmacenesEntities(true, -1, -1);
    }

    public List<FaStockAlmacenes> findFaStockAlmacenesEntities(int maxResults, int firstResult) {
        return findFaStockAlmacenesEntities(false, maxResults, firstResult);
    }

    private List<FaStockAlmacenes> findFaStockAlmacenesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FaStockAlmacenes.class));
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

    public FaStockAlmacenes findFaStockAlmacenes(FaStockAlmacenesPK id) {
        EntityManager em = null;
        try {
            em = getEntityManager();

            return em.find(FaStockAlmacenes.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getFaStockAlmacenesCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();

            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FaStockAlmacenes> rt = cq.from(FaStockAlmacenes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

}
