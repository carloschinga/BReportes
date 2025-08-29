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
public class ObjetipoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "codobj")
    private int codobj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "codtip")
    private String codtip;

    public ObjetipoPK() {
    }

    public ObjetipoPK(int codobj, String codtip) {
        this.codobj = codobj;
        this.codtip = codtip;
    }

    public int getCodobj() {
        return codobj;
    }

    public void setCodobj(int codobj) {
        this.codobj = codobj;
    }

    public String getCodtip() {
        return codtip;
    }

    public void setCodtip(String codtip) {
        this.codtip = codtip;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codobj;
        hash += (codtip != null ? codtip.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ObjetipoPK)) {
            return false;
        }
        ObjetipoPK other = (ObjetipoPK) object;
        if (this.codobj != other.codobj) {
            return false;
        }
        if ((this.codtip == null && other.codtip != null) || (this.codtip != null && !this.codtip.equals(other.codtip))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.ObjetipoPK[ codobj=" + codobj + ", codtip=" + codtip + " ]";
    }
    
}
