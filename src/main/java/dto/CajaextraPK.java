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
public class CajaextraPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "caja")
    private String caja;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "codpro")
    private String codpro;

    public CajaextraPK() {
    }

    public CajaextraPK(String caja, String codpro) {
        this.caja = caja;
        this.codpro = codpro;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public String getCodpro() {
        return codpro;
    }

    public void setCodpro(String codpro) {
        this.codpro = codpro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (caja != null ? caja.hashCode() : 0);
        hash += (codpro != null ? codpro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CajaextraPK)) {
            return false;
        }
        CajaextraPK other = (CajaextraPK) object;
        if ((this.caja == null && other.caja != null) || (this.caja != null && !this.caja.equals(other.caja))) {
            return false;
        }
        if ((this.codpro == null && other.codpro != null) || (this.codpro != null && !this.codpro.equals(other.codpro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.CajaextraPK[ caja=" + caja + ", codpro=" + codpro + " ]";
    }
    
}
