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
public class FaOrdenTrabajoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "ortrcod")
    private int ortrcod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "invnum")
    private int invnum;

    public FaOrdenTrabajoPK() {
    }

    public FaOrdenTrabajoPK(int ortrcod, int invnum) {
        this.ortrcod = ortrcod;
        this.invnum = invnum;
    }

    public int getOrtrcod() {
        return ortrcod;
    }

    public void setOrtrcod(int ortrcod) {
        this.ortrcod = ortrcod;
    }

    public int getInvnum() {
        return invnum;
    }

    public void setInvnum(int invnum) {
        this.invnum = invnum;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ortrcod;
        hash += (int) invnum;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaOrdenTrabajoPK)) {
            return false;
        }
        FaOrdenTrabajoPK other = (FaOrdenTrabajoPK) object;
        if (this.ortrcod != other.ortrcod) {
            return false;
        }
        if (this.invnum != other.invnum) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaOrdenTrabajoPK[ ortrcod=" + ortrcod + ", invnum=" + invnum + " ]";
    }
    
}
