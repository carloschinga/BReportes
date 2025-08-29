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
@Table(name = "HechCuotaVenta")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "HechCuotaVenta.findAll", query = "SELECT h FROM HechCuotaVenta h"),
        @NamedQuery(name = "HechCuotaVenta.findByCuotVtaId", query = "SELECT h FROM HechCuotaVenta h WHERE h.cuotVtaId = :cuotVtaId"),
        @NamedQuery(name = "HechCuotaVenta.obtenerultcodobj", query = "SELECT o.cuotVtaId FROM HechCuotaVenta o order by o.cuotVtaId desc"),
        @NamedQuery(name = "HechCuotaVenta.findByDesobj", query = "SELECT h FROM HechCuotaVenta h WHERE h.desobj = :desobj"),
        @NamedQuery(name = "HechCuotaVenta.findByUsecod", query = "SELECT h FROM HechCuotaVenta h WHERE h.usecod = :usecod"),
        @NamedQuery(name = "HechCuotaVenta.findByFeccre", query = "SELECT h FROM HechCuotaVenta h WHERE h.feccre = :feccre"),
        @NamedQuery(name = "HechCuotaVenta.findByFecumv", query = "SELECT h FROM HechCuotaVenta h WHERE h.fecumv = :fecumv"),
        @NamedQuery(name = "HechCuotaVenta.findByMesano", query = "SELECT h FROM HechCuotaVenta h WHERE h.mesano = :mesano"),
        @NamedQuery(name = "HechCuotaVenta.findByEstado", query = "SELECT h FROM HechCuotaVenta h WHERE h.estado = :estado"),
        @NamedQuery(name = "HechCuotaVenta.findByTipo", query = "SELECT h FROM HechCuotaVenta h WHERE h.tipo = :tipo"),
        @NamedQuery(name = "HechCuotaVenta.findByHecAct", query = "SELECT h FROM HechCuotaVenta h WHERE h.hecAct = :hecAct") })
public class HechCuotaVenta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CuotVtaId")
    private Integer cuotVtaId;
    @Size(max = 100)
    @Column(name = "desobj")
    private String desobj;
    @Column(name = "usecod")
    private Integer usecod;
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;
    @Column(name = "fecumv")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecumv;
    @Size(max = 10)
    @Column(name = "mesano")
    private String mesano;
    @Size(max = 1)
    @Column(name = "estado")
    private String estado;
    @Size(max = 1)
    @Column(name = "tipo")
    private String tipo;
    @Column(name = "hecAct")
    private Boolean hecAct;

    public HechCuotaVenta() {
    }

    public HechCuotaVenta(Integer cuotVtaId) {
        this.cuotVtaId = cuotVtaId;
    }

    public Integer getCuotVtaId() {
        return cuotVtaId;
    }

    public void setCuotVtaId(Integer cuotVtaId) {
        this.cuotVtaId = cuotVtaId;
    }

    public String getDesobj() {
        return desobj;
    }

    public void setDesobj(String desobj) {
        this.desobj = desobj;
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

    public Date getFecumv() {
        return fecumv;
    }

    public void setFecumv(Date fecumv) {
        this.fecumv = fecumv;
    }

    public String getMesano() {
        return mesano;
    }

    public void setMesano(String mesano) {
        this.mesano = mesano;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getHecAct() {
        return hecAct;
    }

    public void setHecAct(Boolean hecAct) {
        this.hecAct = hecAct;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cuotVtaId != null ? cuotVtaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HechCuotaVenta)) {
            return false;
        }
        HechCuotaVenta other = (HechCuotaVenta) object;
        if ((this.cuotVtaId == null && other.cuotVtaId != null)
                || (this.cuotVtaId != null && !this.cuotVtaId.equals(other.cuotVtaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.HechCuotaVenta[ cuotVtaId=" + cuotVtaId + " ]";
    }

}
