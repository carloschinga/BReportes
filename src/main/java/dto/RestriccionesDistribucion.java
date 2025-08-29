/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "restricciones_distribucion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RestriccionesDistribucion.findAll", query = "SELECT r FROM RestriccionesDistribucion r"),
    @NamedQuery(name = "RestriccionesDistribucion.findByCodpro", query = "SELECT r FROM RestriccionesDistribucion r WHERE r.restriccionesDistribucionPK.codpro = :codpro"),
    @NamedQuery(name = "RestriccionesDistribucion.encontrar", query = "SELECT r FROM RestriccionesDistribucion r WHERE r.restriccionesDistribucionPK.codpro = :codpro and r.restriccionesDistribucionPK.codalm = :codalm"),
    @NamedQuery(name = "RestriccionesDistribucion.findByCodalm", query = "SELECT r FROM RestriccionesDistribucion r WHERE r.restriccionesDistribucionPK.codalm = :codalm")})
public class RestriccionesDistribucion implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RestriccionesDistribucionPK restriccionesDistribucionPK;

    public RestriccionesDistribucion() {
    }

    public RestriccionesDistribucion(RestriccionesDistribucionPK restriccionesDistribucionPK) {
        this.restriccionesDistribucionPK = restriccionesDistribucionPK;
    }

    public RestriccionesDistribucion(String codpro, String codalm) {
        this.restriccionesDistribucionPK = new RestriccionesDistribucionPK(codpro, codalm);
    }

    public RestriccionesDistribucionPK getRestriccionesDistribucionPK() {
        return restriccionesDistribucionPK;
    }

    public void setRestriccionesDistribucionPK(RestriccionesDistribucionPK restriccionesDistribucionPK) {
        this.restriccionesDistribucionPK = restriccionesDistribucionPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (restriccionesDistribucionPK != null ? restriccionesDistribucionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RestriccionesDistribucion)) {
            return false;
        }
        RestriccionesDistribucion other = (RestriccionesDistribucion) object;
        if ((this.restriccionesDistribucionPK == null && other.restriccionesDistribucionPK != null) || (this.restriccionesDistribucionPK != null && !this.restriccionesDistribucionPK.equals(other.restriccionesDistribucionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.RestriccionesDistribucion[ restriccionesDistribucionPK=" + restriccionesDistribucionPK + " ]";
    }
    
}
