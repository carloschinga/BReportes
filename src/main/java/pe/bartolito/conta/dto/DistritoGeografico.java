/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
@Table(name = "DistritoGeografico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DistritoGeografico.findAll", query = "SELECT d FROM DistritoGeografico d"),
    @NamedQuery(name = "DistritoGeografico.findByDptoGeoId", query = "SELECT d FROM DistritoGeografico d WHERE d.distritoGeograficoPK.dptoGeoId = :dptoGeoId"),
    @NamedQuery(name = "DistritoGeografico.findByProvGeoId", query = "SELECT d FROM DistritoGeografico d WHERE d.distritoGeograficoPK.provGeoId = :provGeoId"),
    @NamedQuery(name = "DistritoGeografico.findByDistGeoId", query = "SELECT d FROM DistritoGeografico d WHERE d.distritoGeograficoPK.distGeoId = :distGeoId"),
    @NamedQuery(name = "DistritoGeografico.findByDistGeoDescripcion", query = "SELECT d FROM DistritoGeografico d WHERE d.distGeoDescripcion = :distGeoDescripcion"),
    @NamedQuery(name = "DistritoGeografico.findByDistGeoEst", query = "SELECT d FROM DistritoGeografico d WHERE d.distGeoEst = :distGeoEst"),
    @NamedQuery(name = "DistritoGeografico.findByHrsSistema", query = "SELECT d FROM DistritoGeografico d WHERE d.hrsSistema = :hrsSistema"),
    @NamedQuery(name = "DistritoGeografico.findByUsrSistema", query = "SELECT d FROM DistritoGeografico d WHERE d.usrSistema = :usrSistema"),
    @NamedQuery(name = "DistritoGeografico.findByFecSistema", query = "SELECT d FROM DistritoGeografico d WHERE d.fecSistema = :fecSistema")})
public class DistritoGeografico implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "DistGeoDescripcion")
    private String distGeoDescripcion;
    @Size(max = 10)
    @Column(name = "HrsSistema")
    private String hrsSistema;
    @Size(max = 10)
    @Column(name = "UsrSistema")
    private String usrSistema;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DistritoGeograficoPK distritoGeograficoPK;
    @Column(name = "DistGeoEst")
    private Boolean distGeoEst;
    @Column(name = "FecSistema")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecSistema;
    @OneToMany(mappedBy = "distritoGeografico")
    private Collection<Empresa> empresaCollection;
    @JoinColumns({
        @JoinColumn(name = "DptoGeoId", referencedColumnName = "DptoGeoId", insertable = false, updatable = false),
        @JoinColumn(name = "ProvGeoId", referencedColumnName = "ProvGeoId", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private ProvinciaGeografica provinciaGeografica;

    public DistritoGeografico() {
    }

    public DistritoGeografico(DistritoGeograficoPK distritoGeograficoPK) {
        this.distritoGeograficoPK = distritoGeograficoPK;
    }

    public DistritoGeografico(DistritoGeograficoPK distritoGeograficoPK, String distGeoDescripcion) {
        this.distritoGeograficoPK = distritoGeograficoPK;
        this.distGeoDescripcion = distGeoDescripcion;
    }

    public DistritoGeografico(String dptoGeoId, String provGeoId, String distGeoId) {
        this.distritoGeograficoPK = new DistritoGeograficoPK(dptoGeoId, provGeoId, distGeoId);
    }

    public DistritoGeograficoPK getDistritoGeograficoPK() {
        return distritoGeograficoPK;
    }

    public void setDistritoGeograficoPK(DistritoGeograficoPK distritoGeograficoPK) {
        this.distritoGeograficoPK = distritoGeograficoPK;
    }


    public Boolean getDistGeoEst() {
        return distGeoEst;
    }

    public void setDistGeoEst(Boolean distGeoEst) {
        this.distGeoEst = distGeoEst;
    }


    public Date getFecSistema() {
        return fecSistema;
    }

    public void setFecSistema(Date fecSistema) {
        this.fecSistema = fecSistema;
    }

    @XmlTransient
    public Collection<Empresa> getEmpresaCollection() {
        return empresaCollection;
    }

    public void setEmpresaCollection(Collection<Empresa> empresaCollection) {
        this.empresaCollection = empresaCollection;
    }

    public ProvinciaGeografica getProvinciaGeografica() {
        return provinciaGeografica;
    }

    public void setProvinciaGeografica(ProvinciaGeografica provinciaGeografica) {
        this.provinciaGeografica = provinciaGeografica;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (distritoGeograficoPK != null ? distritoGeograficoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DistritoGeografico)) {
            return false;
        }
        DistritoGeografico other = (DistritoGeografico) object;
        if ((this.distritoGeograficoPK == null && other.distritoGeograficoPK != null) || (this.distritoGeograficoPK != null && !this.distritoGeograficoPK.equals(other.distritoGeograficoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.conta.DistritoGeografico[ distritoGeograficoPK=" + distritoGeograficoPK + " ]";
    }

    public String getDistGeoDescripcion() {
        return distGeoDescripcion;
    }

    public void setDistGeoDescripcion(String distGeoDescripcion) {
        this.distGeoDescripcion = distGeoDescripcion;
    }

    public String getHrsSistema() {
        return hrsSistema;
    }

    public void setHrsSistema(String hrsSistema) {
        this.hrsSistema = hrsSistema;
    }

    public String getUsrSistema() {
        return usrSistema;
    }

    public void setUsrSistema(String usrSistema) {
        this.usrSistema = usrSistema;
    }
    
}
