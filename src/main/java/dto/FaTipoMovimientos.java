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
@Table(name = "fa_tipo_movimientos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FaTipoMovimientos.findAll", query = "SELECT f FROM FaTipoMovimientos f"),
    @NamedQuery(name = "FaTipoMovimientos.findByTipkar", query = "SELECT f FROM FaTipoMovimientos f WHERE f.tipkar = :tipkar"),
    @NamedQuery(name = "FaTipoMovimientos.tipkarsec", query = "SELECT f.invkar FROM FaTipoMovimientos f WHERE f.tipkar = :tipkar"),
    @NamedQuery(name = "FaTipoMovimientos.findByDestka", query = "SELECT f FROM FaTipoMovimientos f WHERE f.destka = :destka"),
    @NamedQuery(name = "FaTipoMovimientos.findByInvkar", query = "SELECT f FROM FaTipoMovimientos f WHERE f.invkar = :invkar"),
    @NamedQuery(name = "FaTipoMovimientos.findByEstado", query = "SELECT f FROM FaTipoMovimientos f WHERE f.estado = :estado"),
    @NamedQuery(name = "FaTipoMovimientos.findByFeccre", query = "SELECT f FROM FaTipoMovimientos f WHERE f.feccre = :feccre"),
    @NamedQuery(name = "FaTipoMovimientos.findByFecumv", query = "SELECT f FROM FaTipoMovimientos f WHERE f.fecumv = :fecumv"),
    @NamedQuery(name = "FaTipoMovimientos.findByUsecod", query = "SELECT f FROM FaTipoMovimientos f WHERE f.usecod = :usecod"),
    @NamedQuery(name = "FaTipoMovimientos.findByUsenam", query = "SELECT f FROM FaTipoMovimientos f WHERE f.usenam = :usenam"),
    @NamedQuery(name = "FaTipoMovimientos.findByHostname", query = "SELECT f FROM FaTipoMovimientos f WHERE f.hostname = :hostname"),
    @NamedQuery(name = "FaTipoMovimientos.findByTipkardw", query = "SELECT f FROM FaTipoMovimientos f WHERE f.tipkardw = :tipkardw"),
    @NamedQuery(name = "FaTipoMovimientos.findBySerkarsta", query = "SELECT f FROM FaTipoMovimientos f WHERE f.serkarsta = :serkarsta"),
    @NamedQuery(name = "FaTipoMovimientos.findByCodtri", query = "SELECT f FROM FaTipoMovimientos f WHERE f.codtri = :codtri"),
    @NamedQuery(name = "FaTipoMovimientos.findByAsocmovsta", query = "SELECT f FROM FaTipoMovimientos f WHERE f.asocmovsta = :asocmovsta")})
public class FaTipoMovimientos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "tipkar")
    private String tipkar;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "destka")
    private String destka;
    @Column(name = "invkar")
    private Integer invkar;
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
    @Size(min = 1, max = 70)
    @Column(name = "tipkardw")
    private String tipkardw;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "serkarsta")
    private String serkarsta;
    @Size(max = 2)
    @Column(name = "codtri")
    private String codtri;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "asocmovsta")
    private String asocmovsta;

    public FaTipoMovimientos() {
    }

    public FaTipoMovimientos(String tipkar) {
        this.tipkar = tipkar;
    }

    public FaTipoMovimientos(String tipkar, String destka, String estado, Date feccre, Date fecumv, int usecod, String usenam, String hostname, String tipkardw, String serkarsta, String asocmovsta) {
        this.tipkar = tipkar;
        this.destka = destka;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
        this.tipkardw = tipkardw;
        this.serkarsta = serkarsta;
        this.asocmovsta = asocmovsta;
    }

    public String getTipkar() {
        return tipkar;
    }

    public void setTipkar(String tipkar) {
        this.tipkar = tipkar;
    }

    public String getDestka() {
        return destka;
    }

    public void setDestka(String destka) {
        this.destka = destka;
    }

    public Integer getInvkar() {
        return invkar;
    }

    public void setInvkar(Integer invkar) {
        this.invkar = invkar;
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

    public String getTipkardw() {
        return tipkardw;
    }

    public void setTipkardw(String tipkardw) {
        this.tipkardw = tipkardw;
    }

    public String getSerkarsta() {
        return serkarsta;
    }

    public void setSerkarsta(String serkarsta) {
        this.serkarsta = serkarsta;
    }

    public String getCodtri() {
        return codtri;
    }

    public void setCodtri(String codtri) {
        this.codtri = codtri;
    }

    public String getAsocmovsta() {
        return asocmovsta;
    }

    public void setAsocmovsta(String asocmovsta) {
        this.asocmovsta = asocmovsta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipkar != null ? tipkar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaTipoMovimientos)) {
            return false;
        }
        FaTipoMovimientos other = (FaTipoMovimientos) object;
        if ((this.tipkar == null && other.tipkar != null) || (this.tipkar != null && !this.tipkar.equals(other.tipkar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaTipoMovimientos[ tipkar=" + tipkar + " ]";
    }
    
}
