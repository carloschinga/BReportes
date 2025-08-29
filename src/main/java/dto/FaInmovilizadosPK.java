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
public class FaInmovilizadosPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "codpro")
    private String codpro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "codlot")
    private String codlot;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codsubalm")
    private int codsubalm;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "codalm")
    private String codalm;

    public FaInmovilizadosPK() {
    }

    public FaInmovilizadosPK(String codpro, String codlot, int codsubalm, String codalm) {
        this.codpro = codpro;
        this.codlot = codlot;
        this.codsubalm = codsubalm;
        this.codalm = codalm;
    }

    public String getCodpro() {
        return codpro;
    }

    public void setCodpro(String codpro) {
        this.codpro = codpro;
    }

    public String getCodlot() {
        return codlot;
    }

    public void setCodlot(String codlot) {
        this.codlot = codlot;
    }

    public int getCodsubalm() {
        return codsubalm;
    }

    public void setCodsubalm(int codsubalm) {
        this.codsubalm = codsubalm;
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codpro != null ? codpro.hashCode() : 0);
        hash += (codlot != null ? codlot.hashCode() : 0);
        hash += (int) codsubalm;
        hash += (codalm != null ? codalm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaInmovilizadosPK)) {
            return false;
        }
        FaInmovilizadosPK other = (FaInmovilizadosPK) object;
        if ((this.codpro == null && other.codpro != null) || (this.codpro != null && !this.codpro.equals(other.codpro))) {
            return false;
        }
        if ((this.codlot == null && other.codlot != null) || (this.codlot != null && !this.codlot.equals(other.codlot))) {
            return false;
        }
        if (this.codsubalm != other.codsubalm) {
            return false;
        }
        if ((this.codalm == null && other.codalm != null) || (this.codalm != null && !this.codalm.equals(other.codalm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaInmovilizadosPK[ codpro=" + codpro + ", codlot=" + codlot + ", codsubalm=" + codsubalm + ", codalm=" + codalm + " ]";
    }
    
}
