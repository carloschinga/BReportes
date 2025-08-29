/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "reposicion_recepcion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReposicionRecepcion.findAll", query = "SELECT r FROM ReposicionRecepcion r"),
    @NamedQuery(name = "ReposicionRecepcion.findByOrden", query = "SELECT r FROM ReposicionRecepcion r WHERE r.reposicionRecepcionPK.orden = :orden"),
    @NamedQuery(name = "ReposicionRecepcion.findByCodpro", query = "SELECT r FROM ReposicionRecepcion r WHERE r.reposicionRecepcionPK.codpro = :codpro"),
    @NamedQuery(name = "ReposicionRecepcion.findBySiscod", query = "SELECT r FROM ReposicionRecepcion r WHERE r.reposicionRecepcionPK.siscod = :siscod"),
    @NamedQuery(name = "ReposicionRecepcion.findByCantidad", query = "SELECT r FROM ReposicionRecepcion r WHERE r.cantidad = :cantidad"),
    @NamedQuery(name = "ReposicionRecepcion.findByUsecod", query = "SELECT r FROM ReposicionRecepcion r WHERE r.usecod = :usecod"),
    @NamedQuery(name = "ReposicionRecepcion.findByFeccre", query = "SELECT r FROM ReposicionRecepcion r WHERE r.feccre = :feccre"),
    @NamedQuery(name = "ReposicionRecepcion.findByFecumv", query = "SELECT r FROM ReposicionRecepcion r WHERE r.fecumv = :fecumv"),
    @NamedQuery(name = "ReposicionRecepcion.findByEstado", query = "SELECT r FROM ReposicionRecepcion r WHERE r.estado = :estado"),
    @NamedQuery(name = "ReposicionRecepcion.findByEstobs", query = "SELECT r FROM ReposicionRecepcion r WHERE r.estobs = :estobs"),
    @NamedQuery(name = "ReposicionRecepcion.findByEstobsbot", query = "SELECT r FROM ReposicionRecepcion r WHERE r.estobsbot = :estobsbot"),
    @NamedQuery(name = "ReposicionRecepcion.findByUsecodobs", query = "SELECT r FROM ReposicionRecepcion r WHERE r.usecodobs = :usecodobs"),
    @NamedQuery(name = "ReposicionRecepcion.findByUsecodobsbot", query = "SELECT r FROM ReposicionRecepcion r WHERE r.usecodobsbot = :usecodobsbot"),
    @NamedQuery(name = "ReposicionRecepcion.findByFeccodobs", query = "SELECT r FROM ReposicionRecepcion r WHERE r.feccodobs = :feccodobs"),
    @NamedQuery(name = "ReposicionRecepcion.findByFecestobsbot", query = "SELECT r FROM ReposicionRecepcion r WHERE r.fecestobsbot = :fecestobsbot")})
public class ReposicionRecepcion implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ReposicionRecepcionPK reposicionRecepcionPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad")
    private BigDecimal cantidad;
    @Column(name = "usecod")
    private Integer usecod;
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;
    @Column(name = "fecumv")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecumv;
    @Size(max = 1)
    @Column(name = "estado")
    private String estado;
    @Size(max = 1)
    @Column(name = "estobs")
    private String estobs;
    @Size(max = 1)
    @Column(name = "estobsbot")
    private String estobsbot;
    @Column(name = "usecodobs")
    private int usecodobs;
    @Column(name = "usecodobsbot")
    private int usecodobsbot;
    @Column(name = "feccodobs")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccodobs;
    @Column(name = "fecestobsbot")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecestobsbot;

    public ReposicionRecepcion() {
    }

    public ReposicionRecepcion(ReposicionRecepcionPK reposicionRecepcionPK) {
        this.reposicionRecepcionPK = reposicionRecepcionPK;
    }

    public ReposicionRecepcion(int orden, String codpro, int siscod) {
        this.reposicionRecepcionPK = new ReposicionRecepcionPK(orden, codpro, siscod);
    }

    public ReposicionRecepcionPK getReposicionRecepcionPK() {
        return reposicionRecepcionPK;
    }

    public void setReposicionRecepcionPK(ReposicionRecepcionPK reposicionRecepcionPK) {
        this.reposicionRecepcionPK = reposicionRecepcionPK;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEstobs() {
        return estobs;
    }

    public void setEstobs(String estobs) {
        this.estobs = estobs;
    }

    public String getEstobsbot() {
        return estobsbot;
    }

    public void setEstobsbot(String estobsbot) {
        this.estobsbot = estobsbot;
    }

    public int getUsecodobs() {
        return usecodobs;
    }

    public void setUsecodobs(int usecodobs) {
        this.usecodobs = usecodobs;
    }

    public int getUsecodobsbot() {
        return usecodobsbot;
    }

    public void setUsecodobsbot(int usecodobsbot) {
        this.usecodobsbot = usecodobsbot;
    }

    public Date getFeccodobs() {
        return feccodobs;
    }

    public void setFeccodobs(Date feccodobs) {
        this.feccodobs = feccodobs;
    }

    public Date getFecestobsbot() {
        return fecestobsbot;
    }

    public void setFecestobsbot(Date fecestobsbot) {
        this.fecestobsbot = fecestobsbot;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reposicionRecepcionPK != null ? reposicionRecepcionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReposicionRecepcion)) {
            return false;
        }
        ReposicionRecepcion other = (ReposicionRecepcion) object;
        if ((this.reposicionRecepcionPK == null && other.reposicionRecepcionPK != null) || (this.reposicionRecepcionPK != null && !this.reposicionRecepcionPK.equals(other.reposicionRecepcionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.ReposicionRecepcion[ reposicionRecepcionPK=" + reposicionRecepcionPK + " ]";
    }
    
}
