/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dao;

import dao.JpaPadre;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author USUARIO
 */
public class ConciliacionTarjetasIzapayComprobanteDAO extends JpaPadre {

    public ConciliacionTarjetasIzapayComprobanteDAO(String empresa) {
        super(empresa);
    }

    public String obtenerConciliacionJSON(String empresaId, int unidComId, String periodo) {
        EntityManager em = null;
        StringBuilder json = new StringBuilder();

        try {
            em = getEntityManager();

            Query query = em.createNativeQuery("EXEC sp_bart_conciliacion_comprobantes_tarjetas_izipay ?, ?, ?");
            query.setParameter(1, empresaId);
            query.setParameter(2, unidComId);
            query.setParameter(3, periodo);

            List<?> resultList = query.getResultList();

            for (Object row : resultList) {
                json.append(row.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            json.setLength(0);
            json.append("{\"error\":\"Error al obtener datos\"}");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return json.toString();
    }

    public String conciliarTarjetasJSON(String codigo, String fechaAbono, double netoTotal, int unidComId) {
        EntityManager em = null;
        StringBuilder json = new StringBuilder();

        try {
            em = getEntityManager();

            Query query = em.createNativeQuery(
                    "EXEC sp_bart_conciliar_tarjetasizipay_comprobante ?, ?, ?, ?"
            );
            query.setParameter(1, codigo);
            query.setParameter(2, fechaAbono);
            query.setParameter(3, netoTotal);
            query.setParameter(4, unidComId);

            Object result = query.getSingleResult(); // Solo devuelve el BIT de @Conciliado

            boolean conciliado = false;
            if (result != null) {
                if (result instanceof Number) {
                    conciliado = ((Number) result).intValue() == 1;
                } else if (result instanceof Boolean) {
                    conciliado = (Boolean) result;
                }
            }

            // Construir JSON manualmente
            json.append("{");
            json.append("\"estadoConciliacion\":").append(conciliado ? 1 : 0).append(",");
            json.append("\"mensaje\":\"").append(conciliado ? "Conciliación exitosa" : "No se pudo conciliar").append("\"");
            json.append("}");

        } catch (Exception e) {
            e.printStackTrace();
            json.setLength(0);
            json.append("{\"estadoConciliacion\":0,\"mensaje\":\"Error al procesar la solicitud\"}");
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return json.toString();
    }

}
