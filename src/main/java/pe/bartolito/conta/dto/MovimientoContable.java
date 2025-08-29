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
@Table(name = "MovimientoContable")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MovimientoContable.findAll", query = "SELECT m FROM MovimientoContable m"),
    @NamedQuery(name = "MovimientoContable.findByMovConId", query = "SELECT m FROM MovimientoContable m WHERE m.movConId = :movConId"),
    @NamedQuery(name = "MovimientoContable.findByMovConDescripcion", query = "SELECT m FROM MovimientoContable m WHERE m.movConDescripcion = :movConDescripcion"),
    @NamedQuery(name = "MovimientoContable.findByMovConAbrev", query = "SELECT m FROM MovimientoContable m WHERE m.movConAbrev = :movConAbrev"),
    @NamedQuery(name = "MovimientoContable.findByMovConEstado", query = "SELECT m FROM MovimientoContable m WHERE m.movConEstado = :movConEstado"),
    @NamedQuery(name = "MovimientoContable.findByUsrSistema", query = "SELECT m FROM MovimientoContable m WHERE m.usrSistema = :usrSistema"),
    @NamedQuery(name = "MovimientoContable.findByFecSistema", query = "SELECT m FROM MovimientoContable m WHERE m.fecSistema = :fecSistema"),
    @NamedQuery(name = "MovimientoContable.findByHrsSistema", query = "SELECT m FROM MovimientoContable m WHERE m.hrsSistema = :hrsSistema")})
public class MovimientoContable implements Serializable {

    @Size(max = 150)
    @Column(name = "MovConDescripcion")
    private String movConDescripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 18)
    @Column(name = "MovConAbrev")
    private String movConAbrev;
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
    @Column(name = "MovConId")
    private Integer movConId;
    @Column(name = "MovConEstado")
    private Boolean movConEstado;
    @Column(name = "FecSistema")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecSistema;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "movimientoContable")
    private Collection<SubMovimientoContable> subMovimientoContableCollection;

    public MovimientoContable() {
    }

    public MovimientoContable(Integer movConId) {
        this.movConId = movConId;
    }

    public MovimientoContable(Integer movConId, String movConAbrev) {
        this.movConId = movConId;
        this.movConAbrev = movConAbrev;
    }

    public Integer getMovConId() {
        return movConId;
    }

    public void setMovConId(Integer movConId) {
        this.movConId = movConId;
    }


    public Boolean getMovConEstado() {
        return movConEstado;
    }

    public void setMovConEstado(Boolean movConEstado) {
        this.movConEstado = movConEstado;
    }


    public Date getFecSistema() {
        return fecSistema;
    }

    public void setFecSistema(Date fecSistema) {
        this.fecSistema = fecSistema;
    }


    @XmlTransient
    public Collection<SubMovimientoContable> getSubMovimientoContableCollection() {
        return subMovimientoContableCollection;
    }

    public void setSubMovimientoContableCollection(Collection<SubMovimientoContable> subMovimientoContableCollection) {
        this.subMovimientoContableCollection = subMovimientoContableCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (movConId != null ? movConId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovimientoContable)) {
            return false;
        }
        MovimientoContable other = (MovimientoContable) object;
        if ((this.movConId == null && other.movConId != null) || (this.movConId != null && !this.movConId.equals(other.movConId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.conta.MovimientoContable[ movConId=" + movConId + " ]";
    }

    public String getMovConDescripcion() {
        return movConDescripcion;
    }

    public void setMovConDescripcion(String movConDescripcion) {
        this.movConDescripcion = movConDescripcion;
    }

    public String getMovConAbrev() {
        return movConAbrev;
    }

    public void setMovConAbrev(String movConAbrev) {
        this.movConAbrev = movConAbrev;
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
