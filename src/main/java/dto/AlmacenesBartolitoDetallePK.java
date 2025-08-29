/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author USUARIO
 */
@Embeddable
public class AlmacenesBartolitoDetallePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "codalm")
    private String codalm;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "codpro")
    private String codpro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "ubicacion")
    private String ubicacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "lote")
    private String lote;

    public AlmacenesBartolitoDetallePK() {
    }

    public AlmacenesBartolitoDetallePK(String codalm, String codpro, String ubicacion, String lote) {
        this.codalm = codalm;
        this.codpro = codpro;
        this.ubicacion = ubicacion;
        this.lote = lote;
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
    }

    public String getCodpro() {
        return codpro;
    }

    public void setCodpro(String codpro) {
        this.codpro = codpro;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codalm != null ? codalm.hashCode() : 0);
        hash += (codpro != null ? codpro.hashCode() : 0);
        hash += (ubicacion != null ? ubicacion.hashCode() : 0);
        hash += (lote != null ? lote.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AlmacenesBartolitoDetallePK)) {
            return false;
        }
        AlmacenesBartolitoDetallePK other = (AlmacenesBartolitoDetallePK) object;
        if ((this.codalm == null && other.codalm != null) || (this.codalm != null && !this.codalm.equals(other.codalm))) {
            return false;
        }
        if ((this.codpro == null && other.codpro != null) || (this.codpro != null && !this.codpro.equals(other.codpro))) {
            return false;
        }
        if ((this.ubicacion == null && other.ubicacion != null) || (this.ubicacion != null && !this.ubicacion.equals(other.ubicacion))) {
            return false;
        }
        if ((this.lote == null && other.lote != null) || (this.lote != null && !this.lote.equals(other.lote))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.AlmacenesBartolitoDetallePK[ codalm=" + codalm + ", codpro=" + codpro + ", ubicacion=" + ubicacion + ", lote=" + lote + " ]";
    }
    
}
