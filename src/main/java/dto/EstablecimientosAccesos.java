/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "establecimientos_accesos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstablecimientosAccesos.findAll", query = "SELECT e FROM EstablecimientosAccesos e"),
    @NamedQuery(name = "EstablecimientosAccesos.findBySiscod", query = "SELECT e FROM EstablecimientosAccesos e WHERE e.establecimientosAccesosPK.siscod = :siscod"),
    @NamedQuery(name = "EstablecimientosAccesos.findByCodalm", query = "SELECT e FROM EstablecimientosAccesos e WHERE e.establecimientosAccesosPK.codalm = :codalm"),
    @NamedQuery(name = "EstablecimientosAccesos.findByUsecod", query = "SELECT e FROM EstablecimientosAccesos e WHERE e.establecimientosAccesosPK.usecod = :usecod"),
    @NamedQuery(name = "EstablecimientosAccesos.findByTipkar", query = "SELECT e FROM EstablecimientosAccesos e WHERE e.tipkar = :tipkar"),
    @NamedQuery(name = "EstablecimientosAccesos.findByEstado", query = "SELECT e FROM EstablecimientosAccesos e WHERE e.estado = :estado"),
    @NamedQuery(name = "EstablecimientosAccesos.findByFeccre", query = "SELECT e FROM EstablecimientosAccesos e WHERE e.feccre = :feccre"),
    @NamedQuery(name = "EstablecimientosAccesos.findByFecumv", query = "SELECT e FROM EstablecimientosAccesos e WHERE e.fecumv = :fecumv"),
    @NamedQuery(name = "EstablecimientosAccesos.findByUsecodx", query = "SELECT e FROM EstablecimientosAccesos e WHERE e.usecodx = :usecodx"),
    @NamedQuery(name = "EstablecimientosAccesos.findByUsenam", query = "SELECT e FROM EstablecimientosAccesos e WHERE e.usenam = :usenam"),
    @NamedQuery(name = "EstablecimientosAccesos.findByHostname", query = "SELECT e FROM EstablecimientosAccesos e WHERE e.hostname = :hostname")})
public class EstablecimientosAccesos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EstablecimientosAccesosPK establecimientosAccesosPK;
    @Size(max = 150)
    @Column(name = "tipkar")
    private String tipkar;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecumv")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecumv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usecodx")
    private int usecodx;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "usenam")
    private String usenam;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "hostname")
    private String hostname;

    public EstablecimientosAccesos() {
    }

    public EstablecimientosAccesos(EstablecimientosAccesosPK establecimientosAccesosPK) {
        this.establecimientosAccesosPK = establecimientosAccesosPK;
    }

    public EstablecimientosAccesos(EstablecimientosAccesosPK establecimientosAccesosPK, String estado, Date feccre, Date fecumv, int usecodx, String usenam, String hostname) {
        this.establecimientosAccesosPK = establecimientosAccesosPK;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecodx = usecodx;
        this.usenam = usenam;
        this.hostname = hostname;
    }

    public EstablecimientosAccesos(int siscod, String codalm, int usecod) {
        this.establecimientosAccesosPK = new EstablecimientosAccesosPK(siscod, codalm, usecod);
    }

    public EstablecimientosAccesosPK getEstablecimientosAccesosPK() {
        return establecimientosAccesosPK;
    }

    public void setEstablecimientosAccesosPK(EstablecimientosAccesosPK establecimientosAccesosPK) {
        this.establecimientosAccesosPK = establecimientosAccesosPK;
    }

    public String getTipkar() {
        return tipkar;
    }

    public void setTipkar(String tipkar) {
        this.tipkar = tipkar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public int getUsecodx() {
        return usecodx;
    }

    public void setUsecodx(int usecodx) {
        this.usecodx = usecodx;
    }

    public String getUsenam() {
        return usenam;
    }

    public void setUsenam(String usenam) {
        this.usenam = usenam;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (establecimientosAccesosPK != null ? establecimientosAccesosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstablecimientosAccesos)) {
            return false;
        }
        EstablecimientosAccesos other = (EstablecimientosAccesos) object;
        if ((this.establecimientosAccesosPK == null && other.establecimientosAccesosPK != null) || (this.establecimientosAccesosPK != null && !this.establecimientosAccesosPK.equals(other.establecimientosAccesosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.EstablecimientosAccesos[ establecimientosAccesosPK=" + establecimientosAccesosPK + " ]";
    }
    
}
