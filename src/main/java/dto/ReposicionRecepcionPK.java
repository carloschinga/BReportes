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
public class ReposicionRecepcionPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "orden")
    private int orden;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "codpro")
    private String codpro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "siscod")
    private int siscod;

    public ReposicionRecepcionPK() {
    }

    public ReposicionRecepcionPK(int orden, String codpro, int siscod) {
        this.orden = orden;
        this.codpro = codpro;
        this.siscod = siscod;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getCodpro() {
        return codpro;
    }

    public void setCodpro(String codpro) {
        this.codpro = codpro;
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
        hash += (int) orden;
        hash += (codpro != null ? codpro.hashCode() : 0);
        hash += (int) siscod;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReposicionRecepcionPK)) {
            return false;
        }
        ReposicionRecepcionPK other = (ReposicionRecepcionPK) object;
        if (this.orden != other.orden) {
            return false;
        }
        if ((this.codpro == null && other.codpro != null) || (this.codpro != null && !this.codpro.equals(other.codpro))) {
            return false;
        }
        if (this.siscod != other.siscod) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.ReposicionRecepcionPK[ orden=" + orden + ", codpro=" + codpro + ", siscod=" + siscod + " ]";
    }
    
}
