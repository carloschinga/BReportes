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
public class FaCajasPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "invnum")
    private int invnum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "numitm")
    private int numitm;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "caja")
    private String caja;

    public FaCajasPK() {
    }

    public FaCajasPK(int invnum, int numitm, String caja) {
        this.invnum = invnum;
        this.numitm = numitm;
        this.caja = caja;
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

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) invnum;
        hash += (int) numitm;
        hash += (caja != null ? caja.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaCajasPK)) {
            return false;
        }
        FaCajasPK other = (FaCajasPK) object;
        if (this.invnum != other.invnum) {
            return false;
        }
        if (this.numitm != other.numitm) {
            return false;
        }
        if ((this.caja == null && other.caja != null) || (this.caja != null && !this.caja.equals(other.caja))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaCajasPK[ invnum=" + invnum + ", numitm=" + numitm + ", caja=" + caja + " ]";
    }
    
}
