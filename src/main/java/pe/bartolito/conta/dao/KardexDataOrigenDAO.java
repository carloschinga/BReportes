/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.StoredProcedureQuery;

/**
 *
 * @author USUARIO
 */
public class KardexDataOrigenDAO {

    private final EntityManagerFactory emfLolfar;

    public KardexDataOrigenDAO(EntityManagerFactory emfLolfar) {
        this.emfLolfar = emfLolfar;
    }
     public EntityManager getEntityManager() {
        return emfLolfar.createEntityManager();
    }

    public List<Object[]> obtenerDatosKardex(int parametro, int parametro1, String parametro2) {
    // Crear el EntityManager
    EntityManager emProcedimientos = getEntityManager();

    try {
        // Crear el StoredProcedureQuery usando emProcedimientos
        StoredProcedureQuery storedProcedure = emProcedimientos.createStoredProcedureQuery("pa_SelData_MovAlm_kardex_sunat");

        // Registrar los parámetros
        storedProcedure.registerStoredProcedureParameter("Parametro", Integer.class, javax.persistence.ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("Parametro1", Integer.class, javax.persistence.ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("Parametro2", String.class, javax.persistence.ParameterMode.IN);

        // Establecer los valores de los parámetros
        storedProcedure.setParameter("Parametro", parametro);
        storedProcedure.setParameter("Parametro1", parametro1);
        storedProcedure.setParameter("Parametro2", parametro2);

        // Ejecutar el procedimiento y obtener los resultados
        return storedProcedure.getResultList();
    } finally {
        // Cerrar el EntityManager para liberar recursos
        if (emProcedimientos != null && emProcedimientos.isOpen()) {
            emProcedimientos.close();
        }
    }
}
    
}
