/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "he_tipo_productos_filtra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HeTipoProductosFiltra.findAll", query = "SELECT h FROM HeTipoProductosFiltra h"),
    @NamedQuery(name = "HeTipoProductosFiltra.findByCodtip", query = "SELECT h FROM HeTipoProductosFiltra h WHERE h.codtip = :codtip"),
    @NamedQuery(name = "HeTipoProductosFiltra.findByFeccre", query = "SELECT h FROM HeTipoProductosFiltra h WHERE h.feccre = :feccre")})
public class HeTipoProductosFiltra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "codtip")
    private String codtip;
    @Basic(optional = false)
    @NotNull
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;

    public HeTipoProductosFiltra() {
    }

    public HeTipoProductosFiltra(String codtip) {
        this.codtip = codtip;
    }

    public HeTipoProductosFiltra(String codtip, Date feccre) {
        this.codtip = codtip;
        this.feccre = feccre;
    }

    public String getCodtip() {
        return codtip;
    }

    public void setCodtip(String codtip) {
        this.codtip = codtip;
    }

    public Date getFeccre() {
        return feccre;
    }

    public void setFeccre(Date feccre) {
        this.feccre = feccre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codtip != null ? codtip.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HeTipoProductosFiltra)) {
            return false;
        }
        HeTipoProductosFiltra other = (HeTipoProductosFiltra) object;
        if ((this.codtip == null && other.codtip != null) || (this.codtip != null && !this.codtip.equals(other.codtip))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.HeTipoProductosFiltra[ codtip=" + codtip + " ]";
    }
    
}
