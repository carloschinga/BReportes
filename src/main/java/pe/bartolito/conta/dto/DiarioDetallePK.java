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
public class DiarioDetallePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "DiaDetItem")
    private int diaDetItem;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DiaCabCompId")
    private long diaCabCompId;

    public DiarioDetallePK() {
    }

    public DiarioDetallePK(int diaDetItem, long diaCabCompId) {
        this.diaDetItem = diaDetItem;
        this.diaCabCompId = diaCabCompId;
    }

    public int getDiaDetItem() {
        return diaDetItem;
    }

    public void setDiaDetItem(int diaDetItem) {
        this.diaDetItem = diaDetItem;
    }

    public long getDiaCabCompId() {
        return diaCabCompId;
    }

    public void setDiaCabCompId(long diaCabCompId) {
        this.diaCabCompId = diaCabCompId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) diaDetItem;
        hash += (int) diaCabCompId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DiarioDetallePK)) {
            return false;
        }
        DiarioDetallePK other = (DiarioDetallePK) object;
        if (this.diaDetItem != other.diaDetItem) {
            return false;
        }
        if (this.diaCabCompId != other.diaCabCompId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.conta.DiarioDetallePK[ diaDetItem=" + diaDetItem + ", diaCabCompId=" + diaCabCompId + " ]";
    }
    
}
