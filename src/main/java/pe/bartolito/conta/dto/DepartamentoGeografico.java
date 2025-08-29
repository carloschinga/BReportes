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
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "DepartamentoGeografico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DepartamentoGeografico.findAll", query = "SELECT d FROM DepartamentoGeografico d"),
    @NamedQuery(name = "DepartamentoGeografico.findByDptoGeoId", query = "SELECT d FROM DepartamentoGeografico d WHERE d.dptoGeoId = :dptoGeoId"),
    @NamedQuery(name = "DepartamentoGeografico.findByDptoGeoDescripcion", query = "SELECT d FROM DepartamentoGeografico d WHERE d.dptoGeoDescripcion = :dptoGeoDescripcion"),
    @NamedQuery(name = "DepartamentoGeografico.findByDptoGeoEst", query = "SELECT d FROM DepartamentoGeografico d WHERE d.dptoGeoEst = :dptoGeoEst"),
    @NamedQuery(name = "DepartamentoGeografico.findByUsrSistema", query = "SELECT d FROM DepartamentoGeografico d WHERE d.usrSistema = :usrSistema"),
    @NamedQuery(name = "DepartamentoGeografico.findByFecSistema", query = "SELECT d FROM DepartamentoGeografico d WHERE d.fecSistema = :fecSistema"),
    @NamedQuery(name = "DepartamentoGeografico.findByHrsSistema", query = "SELECT d FROM DepartamentoGeografico d WHERE d.hrsSistema = :hrsSistema")})
public class DepartamentoGeografico implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "DptoGeoDescripcion")
    private String dptoGeoDescripcion;
    @Size(max = 10)
    @Column(name = "UsrSistema")
    private String usrSistema;
    @Size(max = 10)
    @Column(name = "HrsSistema")
    private String hrsSistema;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "DptoGeoId")
    private String dptoGeoId;
    @Column(name = "DptoGeoEst")
    private Boolean dptoGeoEst;
    @Column(name = "FecSistema")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecSistema;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "departamentoGeografico")
    private Collection<ProvinciaGeografica> provinciaGeograficaCollection;

    public DepartamentoGeografico() {
    }

    public DepartamentoGeografico(String dptoGeoId) {
        this.dptoGeoId = dptoGeoId;
    }

    public DepartamentoGeografico(String dptoGeoId, String dptoGeoDescripcion) {
        this.dptoGeoId = dptoGeoId;
        this.dptoGeoDescripcion = dptoGeoDescripcion;
    }

    public String getDptoGeoId() {
        return dptoGeoId;
    }

    public void setDptoGeoId(String dptoGeoId) {
        this.dptoGeoId = dptoGeoId;
    }


    public Boolean getDptoGeoEst() {
        return dptoGeoEst;
    }

    public void setDptoGeoEst(Boolean dptoGeoEst) {
        this.dptoGeoEst = dptoGeoEst;
    }


    public Date getFecSistema() {
        return fecSistema;
    }

    public void setFecSistema(Date fecSistema) {
        this.fecSistema = fecSistema;
    }


    @XmlTransient
    public Collection<ProvinciaGeografica> getProvinciaGeograficaCollection() {
        return provinciaGeograficaCollection;
    }

    public void setProvinciaGeograficaCollection(Collection<ProvinciaGeografica> provinciaGeograficaCollection) {
        this.provinciaGeograficaCollection = provinciaGeograficaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dptoGeoId != null ? dptoGeoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DepartamentoGeografico)) {
            return false;
        }
        DepartamentoGeografico other = (DepartamentoGeografico) object;
        if ((this.dptoGeoId == null && other.dptoGeoId != null) || (this.dptoGeoId != null && !this.dptoGeoId.equals(other.dptoGeoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.conta.DepartamentoGeografico[ dptoGeoId=" + dptoGeoId + " ]";
    }

    public String getDptoGeoDescripcion() {
        return dptoGeoDescripcion;
    }

    public void setDptoGeoDescripcion(String dptoGeoDescripcion) {
        this.dptoGeoDescripcion = dptoGeoDescripcion;
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
