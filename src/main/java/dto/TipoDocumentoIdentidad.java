/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

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
@Table(name = "tipo_documento_identidad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoDocumentoIdentidad.findAll", query = "SELECT t FROM TipoDocumentoIdentidad t"),
    @NamedQuery(name = "TipoDocumentoIdentidad.findByTidcod", query = "SELECT t FROM TipoDocumentoIdentidad t WHERE t.tidcod = :tidcod"),
    @NamedQuery(name = "TipoDocumentoIdentidad.findByTiddes", query = "SELECT t FROM TipoDocumentoIdentidad t WHERE t.tiddes = :tiddes"),
    @NamedQuery(name = "TipoDocumentoIdentidad.findByTidobs", query = "SELECT t FROM TipoDocumentoIdentidad t WHERE t.tidobs = :tidobs"),
    @NamedQuery(name = "TipoDocumentoIdentidad.findByCodleg", query = "SELECT t FROM TipoDocumentoIdentidad t WHERE t.codleg = :codleg"),
    @NamedQuery(name = "TipoDocumentoIdentidad.findByEstado", query = "SELECT t FROM TipoDocumentoIdentidad t WHERE t.estado = :estado"),
    @NamedQuery(name = "TipoDocumentoIdentidad.findByFeccre", query = "SELECT t FROM TipoDocumentoIdentidad t WHERE t.feccre = :feccre"),
    @NamedQuery(name = "TipoDocumentoIdentidad.findByFecumv", query = "SELECT t FROM TipoDocumentoIdentidad t WHERE t.fecumv = :fecumv"),
    @NamedQuery(name = "TipoDocumentoIdentidad.findByUsecod", query = "SELECT t FROM TipoDocumentoIdentidad t WHERE t.usecod = :usecod"),
    @NamedQuery(name = "TipoDocumentoIdentidad.findByUsenam", query = "SELECT t FROM TipoDocumentoIdentidad t WHERE t.usenam = :usenam"),
    @NamedQuery(name = "TipoDocumentoIdentidad.findByHostname", query = "SELECT t FROM TipoDocumentoIdentidad t WHERE t.hostname = :hostname"),
    @NamedQuery(name = "TipoDocumentoIdentidad.findByStavaltidws", query = "SELECT t FROM TipoDocumentoIdentidad t WHERE t.stavaltidws = :stavaltidws")})
public class TipoDocumentoIdentidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "tidcod")
    private String tidcod;
    @Size(max = 50)
    @Column(name = "tiddes")
    private String tiddes;
    @Size(max = 25)
    @Column(name = "tidobs")
    private String tidobs;
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "stavaltidws")
    private String stavaltidws;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sistidcod")
    private Collection<Sistema> sistemaCollection;

    public TipoDocumentoIdentidad() {
    }

    public TipoDocumentoIdentidad(String tidcod) {
        this.tidcod = tidcod;
    }

    public TipoDocumentoIdentidad(String tidcod, String estado, Date feccre, Date fecumv, int usecod, String usenam, String hostname, String stavaltidws) {
        this.tidcod = tidcod;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
        this.stavaltidws = stavaltidws;
    }

    public String getTidcod() {
        return tidcod;
    }

    public void setTidcod(String tidcod) {
        this.tidcod = tidcod;
    }

    public String getTiddes() {
        return tiddes;
    }

    public void setTiddes(String tiddes) {
        this.tiddes = tiddes;
    }

    public String getTidobs() {
        return tidobs;
    }

    public void setTidobs(String tidobs) {
        this.tidobs = tidobs;
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

    public String getStavaltidws() {
        return stavaltidws;
    }

    public void setStavaltidws(String stavaltidws) {
        this.stavaltidws = stavaltidws;
    }

    @XmlTransient
    public Collection<Sistema> getSistemaCollection() {
        return sistemaCollection;
    }

    public void setSistemaCollection(Collection<Sistema> sistemaCollection) {
        this.sistemaCollection = sistemaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tidcod != null ? tidcod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoDocumentoIdentidad)) {
            return false;
        }
        TipoDocumentoIdentidad other = (TipoDocumentoIdentidad) object;
        if ((this.tidcod == null && other.tidcod != null) || (this.tidcod != null && !this.tidcod.equals(other.tidcod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.TipoDocumentoIdentidad[ tidcod=" + tidcod + " ]";
    }
    
}
