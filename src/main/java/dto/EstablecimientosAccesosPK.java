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
public class EstablecimientosAccesosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "siscod")
    private int siscod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "codalm")
    private String codalm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usecod")
    private int usecod;

    public EstablecimientosAccesosPK() {
    }

    public EstablecimientosAccesosPK(int siscod, String codalm, int usecod) {
        this.siscod = siscod;
        this.codalm = codalm;
        this.usecod = usecod;
    }

    public int getSiscod() {
        return siscod;
    }

    public void setSiscod(int siscod) {
        this.siscod = siscod;
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
    }

    public int getUsecod() {
        return usecod;
    }

    public void setUsecod(int usecod) {
        this.usecod = usecod;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) siscod;
        hash += (codalm != null ? codalm.hashCode() : 0);
        hash += (int) usecod;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstablecimientosAccesosPK)) {
            return false;
        }
        EstablecimientosAccesosPK other = (EstablecimientosAccesosPK) object;
        if (this.siscod != other.siscod) {
            return false;
        }
        if ((this.codalm == null && other.codalm != null) || (this.codalm != null && !this.codalm.equals(other.codalm))) {
            return false;
        }
        if (this.usecod != other.usecod) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.EstablecimientosAccesosPK[ siscod=" + siscod + ", codalm=" + codalm + ", usecod=" + usecod + " ]";
    }
    
}
