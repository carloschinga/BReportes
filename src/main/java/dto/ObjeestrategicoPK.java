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
public class ObjeestrategicoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "codobj")
    private int codobj;
    @Basic(optional = false)
    @NotNull
    @Column(name = "compro")
    private int compro;

    public ObjeestrategicoPK() {
    }

    public ObjeestrategicoPK(int codobj, int compro) {
        this.codobj = codobj;
        this.compro = compro;
    }

    public int getCodobj() {
        return codobj;
    }

    public void setCodobj(int codobj) {
        this.codobj = codobj;
    }

    public int getCompro() {
        return compro;
    }

    public void setCompro(int compro) {
        this.compro = compro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codobj;
        hash += (int) compro;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ObjeestrategicoPK)) {
            return false;
        }
        ObjeestrategicoPK other = (ObjeestrategicoPK) object;
        if (this.codobj != other.codobj) {
            return false;
        }
        if (this.compro != other.compro) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.ObjeestrategicoPK[ codobj=" + codobj + ", compro=" + compro + " ]";
    }
    
}
