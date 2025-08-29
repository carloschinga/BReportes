/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "petitorio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Petitorio.findAll", query = "SELECT p FROM Petitorio p"),
    @NamedQuery(name = "Petitorio.findByMedcod", query = "SELECT p FROM Petitorio p WHERE p.petitorioPK.medcod = :medcod"),
    @NamedQuery(name = "Petitorio.findByCodpro", query = "SELECT p FROM Petitorio p WHERE p.petitorioPK.codpro = :codpro"),
    @NamedQuery(name = "Petitorio.findByUsecod", query = "SELECT p FROM Petitorio p WHERE p.usecod = :usecod"),
    @NamedQuery(name = "Petitorio.findByFeccre", query = "SELECT p FROM Petitorio p WHERE p.feccre = :feccre"),
    @NamedQuery(name = "Petitorio.findByEstado", query = "SELECT p FROM Petitorio p WHERE p.estado = :estado")})
public class Petitorio implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PetitorioPK petitorioPK;
    @Column(name = "usecod")
    private Integer usecod;
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;
    @Size(max = 1)
    @Column(name = "estado")
    private String estado;

    public Petitorio() {
    }

    public Petitorio(PetitorioPK petitorioPK) {
        this.petitorioPK = petitorioPK;
    }

    public Petitorio(String medcod, String codpro) {
        this.petitorioPK = new PetitorioPK(medcod, codpro);
    }

    public PetitorioPK getPetitorioPK() {
        return petitorioPK;
    }

    public void setPetitorioPK(PetitorioPK petitorioPK) {
        this.petitorioPK = petitorioPK;
    }

    public Integer getUsecod() {
        return usecod;
    }

    public void setUsecod(Integer usecod) {
        this.usecod = usecod;
    }

    public Date getFeccre() {
        return feccre;
    }

    public void setFeccre(Date feccre) {
        this.feccre = feccre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (petitorioPK != null ? petitorioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Petitorio)) {
            return false;
        }
        Petitorio other = (Petitorio) object;
        if ((this.petitorioPK == null && other.petitorioPK != null) || (this.petitorioPK != null && !this.petitorioPK.equals(other.petitorioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Petitorio[ petitorioPK=" + petitorioPK + " ]";
    }
    
}
