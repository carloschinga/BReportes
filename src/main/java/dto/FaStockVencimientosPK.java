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
public class FaStockVencimientosPK implements Serializable {

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
    @Size(min = 1, max = 15)
    @Column(name = "codlot")
    private String codlot;

    public FaStockVencimientosPK() {
    }

    public FaStockVencimientosPK(String codalm, String codpro, String codlot) {
        this.codalm = codalm;
        this.codpro = codpro;
        this.codlot = codlot;
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

    public String getCodlot() {
        return codlot;
    }

    public void setCodlot(String codlot) {
        this.codlot = codlot;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codalm != null ? codalm.hashCode() : 0);
        hash += (codpro != null ? codpro.hashCode() : 0);
        hash += (codlot != null ? codlot.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaStockVencimientosPK)) {
            return false;
        }
        FaStockVencimientosPK other = (FaStockVencimientosPK) object;
        if ((this.codalm == null && other.codalm != null) || (this.codalm != null && !this.codalm.equals(other.codalm))) {
            return false;
        }
        if ((this.codpro == null && other.codpro != null) || (this.codpro != null && !this.codpro.equals(other.codpro))) {
            return false;
        }
        if ((this.codlot == null && other.codlot != null) || (this.codlot != null && !this.codlot.equals(other.codlot))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaStockVencimientosPK[ codalm=" + codalm + ", codpro=" + codpro + ", codlot=" + codlot + " ]";
    }
    
}
