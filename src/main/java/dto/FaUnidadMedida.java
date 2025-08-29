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
@Table(name = "fa_unidad_medida")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FaUnidadMedida.findAll", query = "SELECT f FROM FaUnidadMedida f"),
    @NamedQuery(name = "FaUnidadMedida.findAllJSON", query = "SELECT f.codund,f.desund FROM FaUnidadMedida f where f.estado='S' order by f.desund asc"),
    @NamedQuery(name = "FaUnidadMedida.findByCodund", query = "SELECT f FROM FaUnidadMedida f WHERE f.codund = :codund"),
    @NamedQuery(name = "FaUnidadMedida.findByDesund", query = "SELECT f FROM FaUnidadMedida f WHERE f.desund = :desund"),
    @NamedQuery(name = "FaUnidadMedida.findByObsund", query = "SELECT f FROM FaUnidadMedida f WHERE f.obsund = :obsund"),
    @NamedQuery(name = "FaUnidadMedida.findByEstado", query = "SELECT f FROM FaUnidadMedida f WHERE f.estado = :estado"),
    @NamedQuery(name = "FaUnidadMedida.findByFeccre", query = "SELECT f FROM FaUnidadMedida f WHERE f.feccre = :feccre"),
    @NamedQuery(name = "FaUnidadMedida.findByFecumv", query = "SELECT f FROM FaUnidadMedida f WHERE f.fecumv = :fecumv"),
    @NamedQuery(name = "FaUnidadMedida.findByUsecod", query = "SELECT f FROM FaUnidadMedida f WHERE f.usecod = :usecod"),
    @NamedQuery(name = "FaUnidadMedida.findByUsenam", query = "SELECT f FROM FaUnidadMedida f WHERE f.usenam = :usenam"),
    @NamedQuery(name = "FaUnidadMedida.findByHostname", query = "SELECT f FROM FaUnidadMedida f WHERE f.hostname = :hostname"),
    @NamedQuery(name = "FaUnidadMedida.findByCodtri", query = "SELECT f FROM FaUnidadMedida f WHERE f.codtri = :codtri"),
    @NamedQuery(name = "FaUnidadMedida.findByAbrvund", query = "SELECT f FROM FaUnidadMedida f WHERE f.abrvund = :abrvund"),
    @NamedQuery(name = "FaUnidadMedida.findByTipund", query = "SELECT f FROM FaUnidadMedida f WHERE f.tipund = :tipund"),
    @NamedQuery(name = "FaUnidadMedida.findByOpchabund", query = "SELECT f FROM FaUnidadMedida f WHERE f.opchabund = :opchabund")})
public class FaUnidadMedida implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "codund")
    private String codund;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "desund")
    private String desund;
    @Size(max = 60)
    @Column(name = "obsund")
    private String obsund;
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
    @Size(min = 1, max = 4)
    @Column(name = "codtri")
    private String codtri;
    @Size(max = 10)
    @Column(name = "abrvund")
    private String abrvund;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "tipund")
    private String tipund;
    @Size(max = 120)
    @Column(name = "opchabund")
    private String opchabund;

    public FaUnidadMedida() {
    }

    public FaUnidadMedida(String codund) {
        this.codund = codund;
    }

    public FaUnidadMedida(String codund, String desund, String estado, Date feccre, Date fecumv, int usecod, String usenam, String hostname, String codtri, String tipund) {
        this.codund = codund;
        this.desund = desund;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
        this.codtri = codtri;
        this.tipund = tipund;
    }

    public String getCodund() {
        return codund;
    }

    public void setCodund(String codund) {
        this.codund = codund;
    }

    public String getDesund() {
        return desund;
    }

    public void setDesund(String desund) {
        this.desund = desund;
    }

    public String getObsund() {
        return obsund;
    }

    public void setObsund(String obsund) {
        this.obsund = obsund;
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

    public String getCodtri() {
        return codtri;
    }

    public void setCodtri(String codtri) {
        this.codtri = codtri;
    }

    public String getAbrvund() {
        return abrvund;
    }

    public void setAbrvund(String abrvund) {
        this.abrvund = abrvund;
    }

    public String getTipund() {
        return tipund;
    }

    public void setTipund(String tipund) {
        this.tipund = tipund;
    }

    public String getOpchabund() {
        return opchabund;
    }

    public void setOpchabund(String opchabund) {
        this.opchabund = opchabund;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codund != null ? codund.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaUnidadMedida)) {
            return false;
        }
        FaUnidadMedida other = (FaUnidadMedida) object;
        if ((this.codund == null && other.codund != null) || (this.codund != null && !this.codund.equals(other.codund))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaUnidadMedida[ codund=" + codund + " ]";
    }
    
}
