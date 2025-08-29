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
@Table(name = "motivo_traslado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MotivoTraslado.findAll", query = "SELECT m FROM MotivoTraslado m"),
    @NamedQuery(name = "MotivoTraslado.findAllJSON", query = "SELECT m.desmot,m.codmot FROM MotivoTraslado m where m.estado='S' order by m.desmot asc"),
    @NamedQuery(name = "MotivoTraslado.findByCodmot", query = "SELECT m FROM MotivoTraslado m WHERE m.codmot = :codmot"),
    @NamedQuery(name = "MotivoTraslado.findByDesmot", query = "SELECT m FROM MotivoTraslado m WHERE m.desmot = :desmot"),
    @NamedQuery(name = "MotivoTraslado.findByCodleg", query = "SELECT m FROM MotivoTraslado m WHERE m.codleg = :codleg"),
    @NamedQuery(name = "MotivoTraslado.findByEstado", query = "SELECT m FROM MotivoTraslado m WHERE m.estado = :estado"),
    @NamedQuery(name = "MotivoTraslado.findByFeccre", query = "SELECT m FROM MotivoTraslado m WHERE m.feccre = :feccre"),
    @NamedQuery(name = "MotivoTraslado.findByFecumv", query = "SELECT m FROM MotivoTraslado m WHERE m.fecumv = :fecumv"),
    @NamedQuery(name = "MotivoTraslado.findByUsecod", query = "SELECT m FROM MotivoTraslado m WHERE m.usecod = :usecod"),
    @NamedQuery(name = "MotivoTraslado.findByUsenam", query = "SELECT m FROM MotivoTraslado m WHERE m.usenam = :usenam"),
    @NamedQuery(name = "MotivoTraslado.findByHostname", query = "SELECT m FROM MotivoTraslado m WHERE m.hostname = :hostname")})
public class MotivoTraslado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "codmot")
    private String codmot;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "desmot")
    private String desmot;
    @Size(max = 2)
    @Column(name = "codleg")
    private String codleg;
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

    public MotivoTraslado() {
    }

    public MotivoTraslado(String codmot) {
        this.codmot = codmot;
    }

    public MotivoTraslado(String codmot, String desmot, String estado, Date feccre, Date fecumv, int usecod, String usenam, String hostname) {
        this.codmot = codmot;
        this.desmot = desmot;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
    }

    public String getCodmot() {
        return codmot;
    }

    public void setCodmot(String codmot) {
        this.codmot = codmot;
    }

    public String getDesmot() {
        return desmot;
    }

    public void setDesmot(String desmot) {
        this.desmot = desmot;
    }

    public String getCodleg() {
        return codleg;
    }

    public void setCodleg(String codleg) {
        this.codleg = codleg;
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
        hash += (codmot != null ? codmot.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MotivoTraslado)) {
            return false;
        }
        MotivoTraslado other = (MotivoTraslado) object;
        if ((this.codmot == null && other.codmot != null) || (this.codmot != null && !this.codmot.equals(other.codmot))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.MotivoTraslado[ codmot=" + codmot + " ]";
    }
    
}
