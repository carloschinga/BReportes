/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 *
 * @author USUARIO
 */
public class FaMovimientoAlmacenDAO extends FaMovimientoAlmacenJpaController {

    public FaMovimientoAlmacenDAO(EntityManagerFactory emf) {
        super(emf);
    }

    public int eliminarRegistros(int año, int mes, String codAlm) {
        String sql = "DELETE FROM fa_movimiento_almacen WHERE YEAR(fecmov) = ? AND MONTH(fecmov) = ? AND codalm = ?";

        EntityManager em = getEntityManager();
        EntityTransaction transaction = null;

        try {
            transaction = em.getTransaction();
            transaction.begin(); // Start the transaction

            Query q = em.createNativeQuery(sql);
            q.setParameter(1, año);
            q.setParameter(2, mes);
            q.setParameter(3, codAlm);

            int result = q.executeUpdate(); // Execute the DELETE query

            transaction.commit(); // Commit the transaction
            return result;

        } catch (Exception ex) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback(); // Rollback in case of error
            }
            ex.printStackTrace(); // Log the exception for debugging
            return -1;
        } finally {
            if (em != null) {
                em.close(); // Close the EntityManager
            }
        }
    }

}
