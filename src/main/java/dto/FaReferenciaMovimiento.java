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
@Table(name = "fa_referencia_movimiento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FaReferenciaMovimiento.findAll", query = "SELECT f FROM FaReferenciaMovimiento f"),
    @NamedQuery(name = "FaReferenciaMovimiento.findAllJSON", query = "SELECT f.codrefmov,f.desrefmov FROM FaReferenciaMovimiento f where f.estado='S'"),
    @NamedQuery(name = "FaReferenciaMovimiento.findByCodrefmov", query = "SELECT f FROM FaReferenciaMovimiento f WHERE f.codrefmov = :codrefmov"),
    @NamedQuery(name = "FaReferenciaMovimiento.findByDesrefmov", query = "SELECT f FROM FaReferenciaMovimiento f WHERE f.desrefmov = :desrefmov"),
    @NamedQuery(name = "FaReferenciaMovimiento.findByObsrefmov", query = "SELECT f FROM FaReferenciaMovimiento f WHERE f.obsrefmov = :obsrefmov"),
    @NamedQuery(name = "FaReferenciaMovimiento.findByEstado", query = "SELECT f FROM FaReferenciaMovimiento f WHERE f.estado = :estado"),
    @NamedQuery(name = "FaReferenciaMovimiento.findByFeccre", query = "SELECT f FROM FaReferenciaMovimiento f WHERE f.feccre = :feccre"),
    @NamedQuery(name = "FaReferenciaMovimiento.findByFecumv", query = "SELECT f FROM FaReferenciaMovimiento f WHERE f.fecumv = :fecumv"),
    @NamedQuery(name = "FaReferenciaMovimiento.findByUsecod", query = "SELECT f FROM FaReferenciaMovimiento f WHERE f.usecod = :usecod"),
    @NamedQuery(name = "FaReferenciaMovimiento.findByUsenam", query = "SELECT f FROM FaReferenciaMovimiento f WHERE f.usenam = :usenam"),
    @NamedQuery(name = "FaReferenciaMovimiento.findByHostname", query = "SELECT f FROM FaReferenciaMovimiento f WHERE f.hostname = :hostname")})
public class FaReferenciaMovimiento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "codrefmov")
    private String codrefmov;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "desrefmov")
    private String desrefmov;
    @Size(max = 60)
    @Column(name = "obsrefmov")
    private String obsrefmov;
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
    @Column(name = "usecod")
    private int usecod;
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

    public FaReferenciaMovimiento() {
    }

    public FaReferenciaMovimiento(String codrefmov) {
        this.codrefmov = codrefmov;
    }

    public FaReferenciaMovimiento(String codrefmov, String desrefmov, String estado, Date feccre, Date fecumv, int usecod, String usenam, String hostname) {
        this.codrefmov = codrefmov;
        this.desrefmov = desrefmov;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
    }

    public String getCodrefmov() {
        return codrefmov;
    }

    public void setCodrefmov(String codrefmov) {
        this.codrefmov = codrefmov;
    }

    public String getDesrefmov() {
        return desrefmov;
    }

    public void setDesrefmov(String desrefmov) {
        this.desrefmov = desrefmov;
    }

    public String getObsrefmov() {
        return obsrefmov;
    }

    public void setObsrefmov(String obsrefmov) {
        this.obsrefmov = obsrefmov;
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

    public int getUsecod() {
        return usecod;
    }

    public void setUsecod(int usecod) {
        this.usecod = usecod;
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
        hash += (codrefmov != null ? codrefmov.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaReferenciaMovimiento)) {
            return false;
        }
        FaReferenciaMovimiento other = (FaReferenciaMovimiento) object;
        if ((this.codrefmov == null && other.codrefmov != null) || (this.codrefmov != null && !this.codrefmov.equals(other.codrefmov))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaReferenciaMovimiento[ codrefmov=" + codrefmov + " ]";
    }
    
}
