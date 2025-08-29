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
public class FaMovimientosDetallePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "invnum")
    private int invnum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numitm")
    private int numitm;

    public FaMovimientosDetallePK() {
    }

    public FaMovimientosDetallePK(int invnum, int numitm) {
        this.invnum = invnum;
        this.numitm = numitm;
    }

    public int getInvnum() {
        return invnum;
    }

    public void setInvnum(int invnum) {
        this.invnum = invnum;
    }

    public int getNumitm() {
        return numitm;
    }

    public void setNumitm(int numitm) {
        this.numitm = numitm;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) invnum;
        hash += (int) numitm;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaMovimientosDetallePK)) {
            return false;
        }
        FaMovimientosDetallePK other = (FaMovimientosDetallePK) object;
        if (this.invnum != other.invnum) {
            return false;
        }
        if (this.numitm != other.numitm) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaMovimientosDetallePK[ invnum=" + invnum + ", numitm=" + numitm + " ]";
    }
    
}
