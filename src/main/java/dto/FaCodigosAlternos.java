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
@Table(name = "fa_codigos_alternos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FaCodigosAlternos.findAll", query = "SELECT f FROM FaCodigosAlternos f"),
    @NamedQuery(name = "FaCodigosAlternos.findByCodalt", query = "SELECT f FROM FaCodigosAlternos f WHERE f.codalt = :codalt"),
    @NamedQuery(name = "FaCodigosAlternos.findByEstado", query = "SELECT f FROM FaCodigosAlternos f WHERE f.estado = :estado"),
    @NamedQuery(name = "FaCodigosAlternos.findByFeccre", query = "SELECT f FROM FaCodigosAlternos f WHERE f.feccre = :feccre"),
    @NamedQuery(name = "FaCodigosAlternos.findByFecumv", query = "SELECT f FROM FaCodigosAlternos f WHERE f.fecumv = :fecumv"),
    @NamedQuery(name = "FaCodigosAlternos.findByUsecod", query = "SELECT f FROM FaCodigosAlternos f WHERE f.usecod = :usecod"),
    @NamedQuery(name = "FaCodigosAlternos.findByUsenam", query = "SELECT f FROM FaCodigosAlternos f WHERE f.usenam = :usenam"),
    @NamedQuery(name = "FaCodigosAlternos.findByHostname", query = "SELECT f FROM FaCodigosAlternos f WHERE f.hostname = :hostname"),
    @NamedQuery(name = "FaCodigosAlternos.findByIdcalt", query = "SELECT f FROM FaCodigosAlternos f WHERE f.idcalt = :idcalt")})
public class FaCodigosAlternos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 21)
    @Column(name = "codalt")
    private String codalt;
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "idcalt")
    private String idcalt;

    public FaCodigosAlternos() {
    }

    public FaCodigosAlternos(String codalt) {
        this.codalt = codalt;
    }

    public FaCodigosAlternos(String codalt, String estado, Date feccre, Date fecumv, int usecod, String usenam, String hostname, String idcalt) {
        this.codalt = codalt;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
        this.idcalt = idcalt;
    }

    public String getCodalt() {
        return codalt;
    }

    public void setCodalt(String codalt) {
        this.codalt = codalt;
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

    public String getIdcalt() {
        return idcalt;
    }

    public void setIdcalt(String idcalt) {
        this.idcalt = idcalt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codalt != null ? codalt.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaCodigosAlternos)) {
            return false;
        }
        FaCodigosAlternos other = (FaCodigosAlternos) object;
        if ((this.codalt == null && other.codalt != null) || (this.codalt != null && !this.codalt.equals(other.codalt))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaCodigosAlternos[ codalt=" + codalt + " ]";
    }
    
}
