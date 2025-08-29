/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "ProvinciaGeografica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProvinciaGeografica.findAll", query = "SELECT p FROM ProvinciaGeografica p"),
    @NamedQuery(name = "ProvinciaGeografica.findByDptoGeoId", query = "SELECT p FROM ProvinciaGeografica p WHERE p.provinciaGeograficaPK.dptoGeoId = :dptoGeoId"),
    @NamedQuery(name = "ProvinciaGeografica.findByProvGeoId", query = "SELECT p FROM ProvinciaGeografica p WHERE p.provinciaGeograficaPK.provGeoId = :provGeoId"),
    @NamedQuery(name = "ProvinciaGeografica.findByProvGeoDescripcion", query = "SELECT p FROM ProvinciaGeografica p WHERE p.provGeoDescripcion = :provGeoDescripcion"),
    @NamedQuery(name = "ProvinciaGeografica.findByProvGeoEst", query = "SELECT p FROM ProvinciaGeografica p WHERE p.provGeoEst = :provGeoEst"),
    @NamedQuery(name = "ProvinciaGeografica.findByUsrSistema", query = "SELECT p FROM ProvinciaGeografica p WHERE p.usrSistema = :usrSistema"),
    @NamedQuery(name = "ProvinciaGeografica.findByFecSistema", query = "SELECT p FROM ProvinciaGeografica p WHERE p.fecSistema = :fecSistema"),
    @NamedQuery(name = "ProvinciaGeografica.findByHrsSistema", query = "SELECT p FROM ProvinciaGeografica p WHERE p.hrsSistema = :hrsSistema")})
public class ProvinciaGeografica implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "ProvGeoDescripcion")
    private String provGeoDescripcion;
    @Size(max = 10)
    @Column(name = "UsrSistema")
    private String usrSistema;
    @Size(max = 10)
    @Column(name = "HrsSistema")
    private String hrsSistema;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProvinciaGeograficaPK provinciaGeograficaPK;
    @Column(name = "ProvGeoEst")
    private Boolean provGeoEst;
    @Column(name = "FecSistema")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecSistema;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provinciaGeografica")
    private Collection<DistritoGeografico> distritoGeograficoCollection;
    @JoinColumn(name = "DptoGeoId", referencedColumnName = "DptoGeoId", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private DepartamentoGeografico departamentoGeografico;

    public ProvinciaGeografica() {
    }

    public ProvinciaGeografica(ProvinciaGeograficaPK provinciaGeograficaPK) {
        this.provinciaGeograficaPK = provinciaGeograficaPK;
    }

    public ProvinciaGeografica(ProvinciaGeograficaPK provinciaGeograficaPK, String provGeoDescripcion) {
        this.provinciaGeograficaPK = provinciaGeograficaPK;
        this.provGeoDescripcion = provGeoDescripcion;
    }

    public ProvinciaGeografica(String dptoGeoId, String provGeoId) {
        this.provinciaGeograficaPK = new ProvinciaGeograficaPK(dptoGeoId, provGeoId);
    }

    public ProvinciaGeograficaPK getProvinciaGeograficaPK() {
        return provinciaGeograficaPK;
    }

    public void setProvinciaGeograficaPK(ProvinciaGeograficaPK provinciaGeograficaPK) {
        this.provinciaGeograficaPK = provinciaGeograficaPK;
    }


    public Boolean getProvGeoEst() {
        return provGeoEst;
    }

    public void setProvGeoEst(Boolean provGeoEst) {
        this.provGeoEst = provGeoEst;
    }


    public Date getFecSistema() {
        return fecSistema;
    }

    public void setFecSistema(Date fecSistema) {
        this.fecSistema = fecSistema;
    }


    @XmlTransient
    public Collection<DistritoGeografico> getDistritoGeograficoCollection() {
        return distritoGeograficoCollection;
    }

    public void setDistritoGeograficoCollection(Collection<DistritoGeografico> distritoGeograficoCollection) {
        this.distritoGeograficoCollection = distritoGeograficoCollection;
    }

    public DepartamentoGeografico getDepartamentoGeografico() {
        return departamentoGeografico;
    }

    public void setDepartamentoGeografico(DepartamentoGeografico departamentoGeografico) {
        this.departamentoGeografico = departamentoGeografico;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (provinciaGeograficaPK != null ? provinciaGeograficaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProvinciaGeografica)) {
            return false;
        }
        ProvinciaGeografica other = (ProvinciaGeografica) object;
        if ((this.provinciaGeograficaPK == null && other.provinciaGeograficaPK != null) || (this.provinciaGeograficaPK != null && !this.provinciaGeograficaPK.equals(other.provinciaGeograficaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.conta.ProvinciaGeografica[ provinciaGeograficaPK=" + provinciaGeograficaPK + " ]";
    }

    public String getProvGeoDescripcion() {
        return provGeoDescripcion;
    }

    public void setProvGeoDescripcion(String provGeoDescripcion) {
        this.provGeoDescripcion = provGeoDescripcion;
    }

    public String getUsrSistema() {
        return usrSistema;
    }

    public void setUsrSistema(String usrSistema) {
        this.usrSistema = usrSistema;
    }

    public String getHrsSistema() {
        return hrsSistema;
    }

    public void setHrsSistema(String hrsSistema) {
        this.hrsSistema = hrsSistema;
    }
    
}
