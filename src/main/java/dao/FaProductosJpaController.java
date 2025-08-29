package dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.PreexistingEntityException;
import dto.FaMovimientosDetalle;
import dto.FaProductos;
import dto.FaStockAlmacenes;

/**
 *
 * @author USUARIO
 */
public class FaProductosJpaController extends JpaPadre {

    public FaProductosJpaController(String empresa) {
        super(empresa);
    }

    public void create(FaProductos faProductos) throws PreexistingEntityException, Exception {
        if (faProductos.getFaMovimientosDetalleList() == null) {
            faProductos.setFaMovimientosDetalleList(new ArrayList<FaMovimientosDetalle>());
        }
        if (faProductos.getFaStockAlmacenesList() == null) {
            faProductos.setFaStockAlmacenesList(new ArrayList<FaStockAlmacenes>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<FaMovimientosDetalle> attachedFaMovimientosDetalleList = new ArrayList<FaMovimientosDetalle>();
            for (FaMovimientosDetalle faMovimientosDetalleListFaMovimientosDetalleToAttach : faProductos
                    .getFaMovimientosDetalleList()) {
                faMovimientosDetalleListFaMovimientosDetalleToAttach = em.getReference(
                        faMovimientosDetalleListFaMovimientosDetalleToAttach.getClass(),
                        faMovimientosDetalleListFaMovimientosDetalleToAttach.getFaMovimientosDetallePK());
                attachedFaMovimientosDetalleList.add(faMovimientosDetalleListFaMovimientosDetalleToAttach);
            }
            faProductos.setFaMovimientosDetalleList(attachedFaMovimientosDetalleList);
            List<FaStockAlmacenes> attachedFaStockAlmacenesList = new ArrayList<FaStockAlmacenes>();
            for (FaStockAlmacenes faStockAlmacenesListFaStockAlmacenesToAttach : faProductos
                    .getFaStockAlmacenesList()) {
                faStockAlmacenesListFaStockAlmacenesToAttach = em.getReference(
                        faStockAlmacenesListFaStockAlmacenesToAttach.getClass(),
                        faStockAlmacenesListFaStockAlmacenesToAttach.getFaStockAlmacenesPK());
                attachedFaStockAlmacenesList.add(faStockAlmacenesListFaStockAlmacenesToAttach);
            }
            faProductos.setFaStockAlmacenesList(attachedFaStockAlmacenesList);
            em.persist(faProductos);
            for (FaMovimientosDetalle faMovimientosDetalleListFaMovimientosDetalle : faProductos
                    .getFaMovimientosDetalleList()) {
                FaProductos oldCodproOfFaMovimientosDetalleListFaMovimientosDetalle = faMovimientosDetalleListFaMovimientosDetalle
                        .getCodpro();
                faMovimientosDetalleListFaMovimientosDetalle.setCodpro(faProductos);
                faMovimientosDetalleListFaMovimientosDetalle = em.merge(faMovimientosDetalleListFaMovimientosDetalle);
                if (oldCodproOfFaMovimientosDetalleListFaMovimientosDetalle != null) {
                    oldCodproOfFaMovimientosDetalleListFaMovimientosDetalle.getFaMovimientosDetalleList()
                            .remove(faMovimientosDetalleListFaMovimientosDetalle);
                    oldCodproOfFaMovimientosDetalleListFaMovimientosDetalle = em
                            .merge(oldCodproOfFaMovimientosDetalleListFaMovimientosDetalle);
                }
            }
            for (FaStockAlmacenes faStockAlmacenesListFaStockAlmacenes : faProductos.getFaStockAlmacenesList()) {
                FaProductos oldFaProductosOfFaStockAlmacenesListFaStockAlmacenes = faStockAlmacenesListFaStockAlmacenes
                        .getFaProductos();
                faStockAlmacenesListFaStockAlmacenes.setFaProductos(faProductos);
                faStockAlmacenesListFaStockAlmacenes = em.merge(faStockAlmacenesListFaStockAlmacenes);
                if (oldFaProductosOfFaStockAlmacenesListFaStockAlmacenes != null) {
                    oldFaProductosOfFaStockAlmacenesListFaStockAlmacenes.getFaStockAlmacenesList()
                            .remove(faStockAlmacenesListFaStockAlmacenes);
                    oldFaProductosOfFaStockAlmacenesListFaStockAlmacenes = em
                            .merge(oldFaProductosOfFaStockAlmacenesListFaStockAlmacenes);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFaProductos(faProductos.getCodpro()) != null) {
                throw new PreexistingEntityException("FaProductos " + faProductos + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public void edit(FaProductos faProductos) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FaProductos persistentFaProductos = em.find(FaProductos.class, faProductos.getCodpro());
            List<FaMovimientosDetalle> faMovimientosDetalleListOld = persistentFaProductos
                    .getFaMovimientosDetalleList();
            List<FaMovimientosDetalle> faMovimientosDetalleListNew = faProductos.getFaMovimientosDetalleList();
            List<FaStockAlmacenes> faStockAlmacenesListOld = persistentFaProductos.getFaStockAlmacenesList();
            List<FaStockAlmacenes> faStockAlmacenesListNew = faProductos.getFaStockAlmacenesList();
            List<String> illegalOrphanMessages = null;
            for (FaMovimientosDetalle faMovimientosDetalleListOldFaMovimientosDetalle : faMovimientosDetalleListOld) {
                if (!faMovimientosDetalleListNew.contains(faMovimientosDetalleListOldFaMovimientosDetalle)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add(
                            "You must retain FaMovimientosDetalle " + faMovimientosDetalleListOldFaMovimientosDetalle
                                    + " since its codpro field is not nullable.");
                }
            }
            for (FaStockAlmacenes faStockAlmacenesListOldFaStockAlmacenes : faStockAlmacenesListOld) {
                if (!faStockAlmacenesListNew.contains(faStockAlmacenesListOldFaStockAlmacenes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages
                            .add("You must retain FaStockAlmacenes " + faStockAlmacenesListOldFaStockAlmacenes
                                    + " since its faProductos field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<FaMovimientosDetalle> attachedFaMovimientosDetalleListNew = new ArrayList<FaMovimientosDetalle>();
            for (FaMovimientosDetalle faMovimientosDetalleListNewFaMovimientosDetalleToAttach : faMovimientosDetalleListNew) {
                faMovimientosDetalleListNewFaMovimientosDetalleToAttach = em.getReference(
                        faMovimientosDetalleListNewFaMovimientosDetalleToAttach.getClass(),
                        faMovimientosDetalleListNewFaMovimientosDetalleToAttach.getFaMovimientosDetallePK());
                attachedFaMovimientosDetalleListNew.add(faMovimientosDetalleListNewFaMovimientosDetalleToAttach);
            }
            faMovimientosDetalleListNew = attachedFaMovimientosDetalleListNew;
            faProductos.setFaMovimientosDetalleList(faMovimientosDetalleListNew);
            List<FaStockAlmacenes> attachedFaStockAlmacenesListNew = new ArrayList<FaStockAlmacenes>();
            for (FaStockAlmacenes faStockAlmacenesListNewFaStockAlmacenesToAttach : faStockAlmacenesListNew) {
                faStockAlmacenesListNewFaStockAlmacenesToAttach = em.getReference(
                        faStockAlmacenesListNewFaStockAlmacenesToAttach.getClass(),
                        faStockAlmacenesListNewFaStockAlmacenesToAttach.getFaStockAlmacenesPK());
                attachedFaStockAlmacenesListNew.add(faStockAlmacenesListNewFaStockAlmacenesToAttach);
            }
            faStockAlmacenesListNew = attachedFaStockAlmacenesListNew;
            faProductos.setFaStockAlmacenesList(faStockAlmacenesListNew);
            faProductos = em.merge(faProductos);
            for (FaMovimientosDetalle faMovimientosDetalleListNewFaMovimientosDetalle : faMovimientosDetalleListNew) {
                if (!faMovimientosDetalleListOld.contains(faMovimientosDetalleListNewFaMovimientosDetalle)) {
                    FaProductos oldCodproOfFaMovimientosDetalleListNewFaMovimientosDetalle = faMovimientosDetalleListNewFaMovimientosDetalle
                            .getCodpro();
                    faMovimientosDetalleListNewFaMovimientosDetalle.setCodpro(faProductos);
                    faMovimientosDetalleListNewFaMovimientosDetalle = em
                            .merge(faMovimientosDetalleListNewFaMovimientosDetalle);
                    if (oldCodproOfFaMovimientosDetalleListNewFaMovimientosDetalle != null
                            && !oldCodproOfFaMovimientosDetalleListNewFaMovimientosDetalle.equals(faProductos)) {
                        oldCodproOfFaMovimientosDetalleListNewFaMovimientosDetalle.getFaMovimientosDetalleList()
                                .remove(faMovimientosDetalleListNewFaMovimientosDetalle);
                        oldCodproOfFaMovimientosDetalleListNewFaMovimientosDetalle = em
                                .merge(oldCodproOfFaMovimientosDetalleListNewFaMovimientosDetalle);
                    }
                }
            }
            for (FaStockAlmacenes faStockAlmacenesListNewFaStockAlmacenes : faStockAlmacenesListNew) {
                if (!faStockAlmacenesListOld.contains(faStockAlmacenesListNewFaStockAlmacenes)) {
                    FaProductos oldFaProductosOfFaStockAlmacenesListNewFaStockAlmacenes = faStockAlmacenesListNewFaStockAlmacenes
                            .getFaProductos();
                    faStockAlmacenesListNewFaStockAlmacenes.setFaProductos(faProductos);
                    faStockAlmacenesListNewFaStockAlmacenes = em.merge(faStockAlmacenesListNewFaStockAlmacenes);
                    if (oldFaProductosOfFaStockAlmacenesListNewFaStockAlmacenes != null
                            && !oldFaProductosOfFaStockAlmacenesListNewFaStockAlmacenes.equals(faProductos)) {
                        oldFaProductosOfFaStockAlmacenesListNewFaStockAlmacenes.getFaStockAlmacenesList()
                                .remove(faStockAlmacenesListNewFaStockAlmacenes);
                        oldFaProductosOfFaStockAlmacenesListNewFaStockAlmacenes = em
                                .merge(oldFaProductosOfFaStockAlmacenesListNewFaStockAlmacenes);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = faProductos.getCodpro();
                if (findFaProductos(id) == null) {
                    throw new NonexistentEntityException("The faProductos with id " + id + " no longer exists.");
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
            FaProductos faProductos;
            try {
                faProductos = em.getReference(FaProductos.class, id);
                faProductos.getCodpro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The faProductos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<FaMovimientosDetalle> faMovimientosDetalleListOrphanCheck = faProductos.getFaMovimientosDetalleList();
            for (FaMovimientosDetalle faMovimientosDetalleListOrphanCheckFaMovimientosDetalle : faMovimientosDetalleListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add(
                        "This FaProductos (" + faProductos + ") cannot be destroyed since the FaMovimientosDetalle "
                                + faMovimientosDetalleListOrphanCheckFaMovimientosDetalle
                                + " in its faMovimientosDetalleList field has a non-nullable codpro field.");
            }
            List<FaStockAlmacenes> faStockAlmacenesListOrphanCheck = faProductos.getFaStockAlmacenesList();
            for (FaStockAlmacenes faStockAlmacenesListOrphanCheckFaStockAlmacenes : faStockAlmacenesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages
                        .add("This FaProductos (" + faProductos + ") cannot be destroyed since the FaStockAlmacenes "
                                + faStockAlmacenesListOrphanCheckFaStockAlmacenes
                                + " in its faStockAlmacenesList field has a non-nullable faProductos field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(faProductos);
            em.getTransaction().commit();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<FaProductos> findFaProductosEntities() {
        return findFaProductosEntities(true, -1, -1);
    }

    public List<FaProductos> findFaProductosEntities(int maxResults, int firstResult) {
        return findFaProductosEntities(false, maxResults, firstResult);
    }

    private List<FaProductos> findFaProductosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FaProductos.class));
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

    public FaProductos findFaProductos(String id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            return em.find(FaProductos.class, id);
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public int getFaProductosCount() {
        EntityManager em = null;
        try {
            em = getEntityManager();

            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FaProductos> rt = cq.from(FaProductos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String listarProductosDistribuir(int tipoStkmin, String tipoDistrib, String codtip, int secuencia,
            char indicaFecha, String fecha1, String fecha2, String fecha3) throws ParseException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            StoredProcedureQuery storedProcedure = em
                    .createNamedStoredProcedureQuery("sel_productos_distribuir_almacencentral_parametros");

            storedProcedure.setParameter("tipo_stkmin", tipoStkmin);
            storedProcedure.setParameter("tipo_distrib", tipoDistrib);
            storedProcedure.setParameter("codtip", codtip);
            storedProcedure.setParameter("secuencia", secuencia);
            storedProcedure.setParameter("indica_fecha", indicaFecha);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha1Date = sdf.parse(fecha1);
            Date fecha2Date = sdf.parse(fecha2);
            Date fecha3Date = sdf.parse(fecha3);

            storedProcedure.setParameter("fecha1", fecha1Date);
            storedProcedure.setParameter("fecha2", fecha2Date);
            storedProcedure.setParameter("fecha3", fecha3Date);

            storedProcedure.execute();

            List<Object[]> resultados = storedProcedure.getResultList();

            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(resultados);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al convertir resultados a JSON: " + e.getMessage();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public List<Map<String, Object>> searchProducts(String searchTerm, int page, int pageSize) {
        EntityManager em = null;

        try {
            em = getEntityManager();

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<FaProductos> cq = cb.createQuery(FaProductos.class);
            Root<FaProductos> producto = cq.from(FaProductos.class);

            List<Predicate> predicates = new ArrayList<>();

            // Filtro por estado 'S' (siempre aplica)
            predicates.add(cb.equal(producto.get("estado"), "S"));

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                String likeTerm = "%" + searchTerm.trim() + "%";
                predicates.add(cb.or(
                        cb.like(producto.get("despro"), likeTerm),
                        cb.like(producto.get("codpro"), likeTerm)));
            }

            cq.where(predicates.toArray(new Predicate[0]));
            cq.orderBy(cb.asc(producto.get("despro")));

            TypedQuery<FaProductos> query = em.createQuery(cq);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);

            List<FaProductos> productos = query.getResultList();
            List<Map<String, Object>> result = new ArrayList<>();

            for (FaProductos p : productos) {
                Map<String, Object> productMap = new HashMap<>();
                productMap.put("codigo", p.getCodpro());
                productMap.put("nombre", p.getDespro());
                result.add(productMap);
            }

            return result;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public long countSearchProducts(String searchTerm) {
        EntityManager em = null;
        try {
            em = getEntityManager();

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<FaProductos> producto = cq.from(FaProductos.class);

            cq.select(cb.count(producto));

            List<Predicate> predicates = new ArrayList<>();

            // Filtro por estado 'S' (siempre aplica)
            predicates.add(cb.equal(producto.get("estado"), "S"));

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                String likeTerm = "%" + searchTerm.trim() + "%";
                predicates.add(cb.or(
                        cb.like(producto.get("despro"), likeTerm),
                        cb.like(producto.get("codpro"), likeTerm)));
            }

            cq.where(predicates.toArray(new Predicate[0]));

            return em.createQuery(cq).getSingleResult();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }

    public String obtenerProductosPorLab(String laboratorioId) {
        EntityManager em = null;

        JSONArray jsonArray = new JSONArray();
        try {
            em = getEntityManager();
            Query q = em.createNamedQuery("FaProductos.findByCodlab");
            q.setParameter("codlab", laboratorioId);
            List<Object[]> resultados = q.getResultList();
            for (Object[] fila : resultados) {
                JSONObject item = new JSONObject();
                item.put("codigo", fila[0]);
                item.put("nombre", fila[1]);
                jsonArray.put(item);
            }
            return jsonArray.toString();
        } catch (Exception e) {
            return jsonArray.toString();
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }

    }

    public boolean getProductos(String codpro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            String sql = "SELECT count(*) FROM fa_productos WHERE codpro = ? and estado = 'S'";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, codpro);

            Object result = query.getSingleResult();

            // Convertir el resultado BIT (1/0) a boolean
            if (result instanceof Number) {
                return ((Number) result).intValue() > 0;
            } else if (result instanceof Boolean) {
                return (Boolean) result;
            }
            return false;

        } catch (Exception e) {
            return false;
        } finally {
            if (em != null && em.isOpen())
                em.close();
        }
    }
}
