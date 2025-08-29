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
public class ObjelaboratorioPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "codobj")
    private int codobj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "codlab")
    private String codlab;

    public ObjelaboratorioPK() {
    }

    public ObjelaboratorioPK(int codobj, String codlab) {
        this.codobj = codobj;
        this.codlab = codlab;
    }

    public int getCodobj() {
        return codobj;
    }

    public void setCodobj(int codobj) {
        this.codobj = codobj;
    }

    public String getCodlab() {
        return codlab;
    }

    public void setCodlab(String codlab) {
        this.codlab = codlab;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codobj;
        hash += (codlab != null ? codlab.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ObjelaboratorioPK)) {
            return false;
        }
        ObjelaboratorioPK other = (ObjelaboratorioPK) object;
        if (this.codobj != other.codobj) {
            return false;
        }
        if ((this.codlab == null && other.codlab != null) || (this.codlab != null && !this.codlab.equals(other.codlab))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.ObjelaboratorioPK[ codobj=" + codobj + ", codlab=" + codlab + " ]";
    }
    
}
