/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dto;

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
public class SubMovimientoContablePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "MovConId")
    private int movConId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SubMovId")
    private int subMovId;

    public SubMovimientoContablePK() {
    }

    public SubMovimientoContablePK(int movConId, int subMovId) {
        this.movConId = movConId;
        this.subMovId = subMovId;
    }

    public int getMovConId() {
        return movConId;
    }

    public void setMovConId(int movConId) {
        this.movConId = movConId;
    }

    public int getSubMovId() {
        return subMovId;
    }

    public void setSubMovId(int subMovId) {
        this.subMovId = subMovId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) movConId;
        hash += (int) subMovId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubMovimientoContablePK)) {
            return false;
        }
        SubMovimientoContablePK other = (SubMovimientoContablePK) object;
        if (this.movConId != other.movConId) {
            return false;
        }
        if (this.subMovId != other.subMovId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.conta.SubMovimientoContablePK[ movConId=" + movConId + ", subMovId=" + subMovId + " ]";
    }
    
}
