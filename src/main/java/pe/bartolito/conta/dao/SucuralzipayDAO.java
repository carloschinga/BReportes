/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dao;

import dao.JpaPadre;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author USUARIO
 */
public class SucuralzipayDAO extends JpaPadre {
    
    public SucuralzipayDAO(String empresa) {
        super(empresa);
    }
    public String obtenerSucursalesJSON() {
       EntityManager em = null;
        try {
            em = getEntityManager();
            // Ejecuta el procedimiento almacenado
            Query query = em.createNativeQuery("EXEC sp_bart_kardex_sel_sucursal_izipay_JSON");
            
            // Como el SP devuelve un JSON en formato texto, lo obtenemos como String
            String json = (String) query.getSingleResult();
            return json;
        } finally {
            em.close();
        }
    }
    
}
