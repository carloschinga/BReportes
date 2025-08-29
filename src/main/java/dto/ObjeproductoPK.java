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

/**
 *
 * @author USUARIO
 */
@Embeddable
public class ObjeproductoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "codobj")
    private int codobj;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codpro")
    private int codpro;

    public ObjeproductoPK() {
    }

    public ObjeproductoPK(int codobj, int codpro) {
        this.codobj = codobj;
        this.codpro = codpro;
    }

    public int getCodobj() {
        return codobj;
    }

    public void setCodobj(int codobj) {
        this.codobj = codobj;
    }

    public int getCodpro() {
        return codpro;
    }

    public void setCodpro(int codpro) {
        this.codpro = codpro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codobj;
        hash += (int) codpro;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ObjeproductoPK)) {
            return false;
        }
        ObjeproductoPK other = (ObjeproductoPK) object;
        if (this.codobj != other.codobj) {
            return false;
        }
        if (this.codpro != other.codpro) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.ObjeproductoPK[ codobj=" + codobj + ", codpro=" + codpro + " ]";
    }
    
}
