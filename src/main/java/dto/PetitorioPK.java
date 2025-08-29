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
public class PetitorioPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "medcod")
    private String medcod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "codpro")
    private String codpro;

    public PetitorioPK() {
    }

    public PetitorioPK(String medcod, String codpro) {
        this.medcod = medcod;
        this.codpro = codpro;
    }

    public String getMedcod() {
        return medcod;
    }

    public void setMedcod(String medcod) {
        this.medcod = medcod;
    }

    public String getCodpro() {
        return codpro;
    }

    public void setCodpro(String codpro) {
        this.codpro = codpro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (medcod != null ? medcod.hashCode() : 0);
        hash += (codpro != null ? codpro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PetitorioPK)) {
            return false;
        }
        PetitorioPK other = (PetitorioPK) object;
        if ((this.medcod == null && other.medcod != null) || (this.medcod != null && !this.medcod.equals(other.medcod))) {
            return false;
        }
        if ((this.codpro == null && other.codpro != null) || (this.codpro != null && !this.codpro.equals(other.codpro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.PetitorioPK[ medcod=" + medcod + ", codpro=" + codpro + " ]";
    }
    
}
