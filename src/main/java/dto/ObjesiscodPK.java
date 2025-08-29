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
public class ObjesiscodPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "codobj")
    private int codobj;
    @Basic(optional = false)
    @NotNull
    @Column(name = "siscod")
    private int siscod;

    public ObjesiscodPK() {
    }

    public ObjesiscodPK(int codobj, int siscod) {
        this.codobj = codobj;
        this.siscod = siscod;
    }

    public int getCodobj() {
        return codobj;
    }

    public void setCodobj(int codobj) {
        this.codobj = codobj;
    }

    public int getSiscod() {
        return siscod;
    }

    public void setSiscod(int siscod) {
        this.siscod = siscod;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codobj;
        hash += (int) siscod;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ObjesiscodPK)) {
            return false;
        }
        ObjesiscodPK other = (ObjesiscodPK) object;
        if (this.codobj != other.codobj) {
            return false;
        }
        if (this.siscod != other.siscod) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.ObjesiscodPK[ codobj=" + codobj + ", siscod=" + siscod + " ]";
    }
    
}
