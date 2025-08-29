/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dao;

import pe.bartolito.conta.dto.FaMovimientoAlmacen;
import pe.bartolito.conta.dto.FaMovimientoAlmacenPK;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *
 * @author USUARIO
 */
public class KardexMovimientoAlmacenDestinoDAO {

    private final EntityManagerFactory emfSigol;
    private final EntityManagerFactory emfLolfar;

    public KardexMovimientoAlmacenDestinoDAO(EntityManagerFactory emfSigol, EntityManagerFactory emfLolfar) {
        this.emfSigol = emfSigol;
        this.emfLolfar = emfLolfar;
    }

    public void insertarDatosKardex(int parametro, int parametro1, String parametro2) {
        EntityManager em = emfSigol.createEntityManager();
        FaMovimientoAlmacenDAO faMovAlmDAO = new FaMovimientoAlmacenDAO(emfSigol);
        int cant = faMovAlmDAO.eliminarRegistros(parametro, parametro1, parametro2);

        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            KardexDataOrigenDAO kdoDAO = new KardexDataOrigenDAO(emfLolfar);

            // Obtener los datos del procedimiento almacenado
            List<Object[]> resultados = kdoDAO.obtenerDatosKardex(parametro, parametro1, parametro2);

            for (Object[] resultado : resultados) {
                // Asignar los valores obtenidos a los campos correspondientes
                FaMovimientoAlmacenPK pk = new FaMovimientoAlmacenPK(
                        (int) resultado[0], // invnum
                        (String) resultado[7], // codpro
                        (int) resultado[6], // detItem
                        (String) resultado[13], // codalm
                        (String) resultado[16] // oricodDoc
                );
                

                FaMovimientoAlmacen faMovimientoAlmacen = new FaMovimientoAlmacen();
                faMovimientoAlmacen.setFaMovimientoAlmacenPK(pk);
                faMovimientoAlmacen.setEmpresaId("03");
                faMovimientoAlmacen.setDocid("07");       //OJO AQUI
                faMovimientoAlmacen.setSerdoc((String) resultado[2]);
                faMovimientoAlmacen.setNumdoc((String) resultado[3]);
                faMovimientoAlmacen.setFecmov((Date) resultado[4]);
                faMovimientoAlmacen.setTipoOpe((String) resultado[5]);
                faMovimientoAlmacen.setDespro((String) resultado[8]);
                faMovimientoAlmacen.setCant((Integer) resultado[9]);
                faMovimientoAlmacen.setCantF((Integer) resultado[10]);
                faMovimientoAlmacen.setCoscom((BigDecimal) resultado[11]);
                faMovimientoAlmacen.setCospro((BigDecimal) resultado[12]);
                faMovimientoAlmacen.setStkfra((Integer) resultado[14]);
                faMovimientoAlmacen.setTipMov((String) resultado[15]);
                faMovimientoAlmacen.setCosproCal((BigDecimal) new BigDecimal(0));  //OJO AQUI
                faMovimientoAlmacen.setDiaCabCompId(null);  //OJO AQUI
                faMovimientoAlmacen.setTotval((BigDecimal) new BigDecimal(0)); //OJO AQUI
                faMovimientoAlmacen.setFeccre(new Date());
                faMovimientoAlmacen.setUsenam("heberbs");
                faMovimientoAlmacen.setHostname(null);
                faMovimientoAlmacen.setCodalm2(null);

                // Persistir la entidad en la base de datos
                em.persist(faMovimientoAlmacen);
            }

            // Confirmar la transacción
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        EntityManagerFactory emfSigol;
        EntityManagerFactory emfLolfar;

        emfSigol = Persistence.createEntityManagerFactory("sigolD");
        emfLolfar = Persistence.createEntityManagerFactory("lolfarP3");

        KardexMovimientoAlmacenDestinoDAO dao = new KardexMovimientoAlmacenDestinoDAO(emfSigol, emfLolfar);
        dao.insertarDatosKardex(2024, 1, "A1");

    }
}
