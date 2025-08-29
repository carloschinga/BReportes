/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "SubMovimientoContable")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SubMovimientoContable.findAll", query = "SELECT s FROM SubMovimientoContable s"),
    @NamedQuery(name = "SubMovimientoContable.findByMovConId", query = "SELECT s FROM SubMovimientoContable s WHERE s.subMovimientoContablePK.movConId = :movConId"),
    @NamedQuery(name = "SubMovimientoContable.findBySubMovId", query = "SELECT s FROM SubMovimientoContable s WHERE s.subMovimientoContablePK.subMovId = :subMovId"),
    @NamedQuery(name = "SubMovimientoContable.findBySubMovDescripcion", query = "SELECT s FROM SubMovimientoContable s WHERE s.subMovDescripcion = :subMovDescripcion"),
    @NamedQuery(name = "SubMovimientoContable.findByCosteoId", query = "SELECT s FROM SubMovimientoContable s WHERE s.costeoId = :costeoId"),
    @NamedQuery(name = "SubMovimientoContable.findBySubMovConEstado", query = "SELECT s FROM SubMovimientoContable s WHERE s.subMovConEstado = :subMovConEstado"),
    @NamedQuery(name = "SubMovimientoContable.findByUsrSistema", query = "SELECT s FROM SubMovimientoContable s WHERE s.usrSistema = :usrSistema"),
    @NamedQuery(name = "SubMovimientoContable.findByFecSistema", query = "SELECT s FROM SubMovimientoContable s WHERE s.fecSistema = :fecSistema"),
    @NamedQuery(name = "SubMovimientoContable.findByHrsSistema", query = "SELECT s FROM SubMovimientoContable s WHERE s.hrsSistema = :hrsSistema")})
public class SubMovimientoContable implements Serializable {

    @Size(max = 100)
    @Column(name = "SubMovDescripcion")
    private String subMovDescripcion;
    @Size(max = 2)
    @Column(name = "CosteoId")
    private String costeoId;
    @Size(max = 10)
    @Column(name = "UsrSistema")
    private String usrSistema;
    @Size(max = 10)
    @Column(name = "HrsSistema")
    private String hrsSistema;

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected SubMovimientoContablePK subMovimientoContablePK;
    @Column(name = "SubMovConEstado")
    private Boolean subMovConEstado;
    @Column(name = "FecSistema")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecSistema;
    @OneToMany(mappedBy = "subMovimientoContable")
    private Collection<DiarioCabecera> diarioCabeceraCollection;
    @JoinColumn(name = "MovConId", referencedColumnName = "MovConId", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private MovimientoContable movimientoContable;

    public SubMovimientoContable() {
    }

    public SubMovimientoContable(SubMovimientoContablePK subMovimientoContablePK) {
        this.subMovimientoContablePK = subMovimientoContablePK;
    }

    public SubMovimientoContable(int movConId, int subMovId) {
        this.subMovimientoContablePK = new SubMovimientoContablePK(movConId, subMovId);
    }

    public SubMovimientoContablePK getSubMovimientoContablePK() {
        return subMovimientoContablePK;
    }

    public void setSubMovimientoContablePK(SubMovimientoContablePK subMovimientoContablePK) {
        this.subMovimientoContablePK = subMovimientoContablePK;
    }


    public Boolean getSubMovConEstado() {
        return subMovConEstado;
    }

    public void setSubMovConEstado(Boolean subMovConEstado) {
        this.subMovConEstado = subMovConEstado;
    }


    public Date getFecSistema() {
        return fecSistema;
    }

    public void setFecSistema(Date fecSistema) {
        this.fecSistema = fecSistema;
    }


    @XmlTransient
    public Collection<DiarioCabecera> getDiarioCabeceraCollection() {
        return diarioCabeceraCollection;
    }

    public void setDiarioCabeceraCollection(Collection<DiarioCabecera> diarioCabeceraCollection) {
        this.diarioCabeceraCollection = diarioCabeceraCollection;
    }

    public MovimientoContable getMovimientoContable() {
        return movimientoContable;
    }

    public void setMovimientoContable(MovimientoContable movimientoContable) {
        this.movimientoContable = movimientoContable;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (subMovimientoContablePK != null ? subMovimientoContablePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubMovimientoContable)) {
            return false;
        }
        SubMovimientoContable other = (SubMovimientoContable) object;
        if ((this.subMovimientoContablePK == null && other.subMovimientoContablePK != null) || (this.subMovimientoContablePK != null && !this.subMovimientoContablePK.equals(other.subMovimientoContablePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.conta.SubMovimientoContable[ subMovimientoContablePK=" + subMovimientoContablePK + " ]";
    }

    public String getSubMovDescripcion() {
        return subMovDescripcion;
    }

    public void setSubMovDescripcion(String subMovDescripcion) {
        this.subMovDescripcion = subMovDescripcion;
    }

    public String getCosteoId() {
        return costeoId;
    }

    public void setCosteoId(String costeoId) {
        this.costeoId = costeoId;
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
