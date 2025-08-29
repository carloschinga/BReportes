/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "fa_descuentos_productos_est")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FaDescuentosProductosEst.findAll", query = "SELECT f FROM FaDescuentosProductosEst f"),
    @NamedQuery(name = "FaDescuentosProductosEst.findBySiscod", query = "SELECT f FROM FaDescuentosProductosEst f WHERE f.faDescuentosProductosEstPK.siscod = :siscod"),
    @NamedQuery(name = "FaDescuentosProductosEst.findByCodpro", query = "SELECT f FROM FaDescuentosProductosEst f WHERE f.faDescuentosProductosEstPK.codpro = :codpro"),
    @NamedQuery(name = "FaDescuentosProductosEst.findByDtopro", query = "SELECT f FROM FaDescuentosProductosEst f WHERE f.dtopro = :dtopro"),
    @NamedQuery(name = "FaDescuentosProductosEst.findByUtipro2", query = "SELECT f FROM FaDescuentosProductosEst f WHERE f.utipro2 = :utipro2"),
    @NamedQuery(name = "FaDescuentosProductosEst.findByDatind", query = "SELECT f FROM FaDescuentosProductosEst f WHERE f.datind = :datind"),
    @NamedQuery(name = "FaDescuentosProductosEst.findByDatfid", query = "SELECT f FROM FaDescuentosProductosEst f WHERE f.datfid = :datfid"),
    @NamedQuery(name = "FaDescuentosProductosEst.findByEstado", query = "SELECT f FROM FaDescuentosProductosEst f WHERE f.estado = :estado"),
    @NamedQuery(name = "FaDescuentosProductosEst.findByFeccre", query = "SELECT f FROM FaDescuentosProductosEst f WHERE f.feccre = :feccre"),
    @NamedQuery(name = "FaDescuentosProductosEst.findByFecumv", query = "SELECT f FROM FaDescuentosProductosEst f WHERE f.fecumv = :fecumv"),
    @NamedQuery(name = "FaDescuentosProductosEst.findByUsecod", query = "SELECT f FROM FaDescuentosProductosEst f WHERE f.usecod = :usecod"),
    @NamedQuery(name = "FaDescuentosProductosEst.findByUsenam", query = "SELECT f FROM FaDescuentosProductosEst f WHERE f.usenam = :usenam"),
    @NamedQuery(name = "FaDescuentosProductosEst.findByHostname", query = "SELECT f FROM FaDescuentosProductosEst f WHERE f.hostname = :hostname"),
    @NamedQuery(name = "FaDescuentosProductosEst.findByModfar", query = "SELECT f FROM FaDescuentosProductosEst f WHERE f.modfar = :modfar")})
public class FaDescuentosProductosEst implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FaDescuentosProductosEstPK faDescuentosProductosEstPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtopro")
    private BigDecimal dtopro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "utipro2")
    private BigDecimal utipro2;
    @Column(name = "datind")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datind;
    @Column(name = "datfid")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datfid;
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
    @Size(min = 1, max = 2)
    @Column(name = "modfar")
    private String modfar;

    public FaDescuentosProductosEst() {
    }

    public FaDescuentosProductosEst(FaDescuentosProductosEstPK faDescuentosProductosEstPK) {
        this.faDescuentosProductosEstPK = faDescuentosProductosEstPK;
    }

    public FaDescuentosProductosEst(FaDescuentosProductosEstPK faDescuentosProductosEstPK, BigDecimal dtopro, BigDecimal utipro2, String estado, Date feccre, Date fecumv, int usecod, String usenam, String hostname, String modfar) {
        this.faDescuentosProductosEstPK = faDescuentosProductosEstPK;
        this.dtopro = dtopro;
        this.utipro2 = utipro2;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
        this.modfar = modfar;
    }

    public FaDescuentosProductosEst(int siscod, String codpro) {
        this.faDescuentosProductosEstPK = new FaDescuentosProductosEstPK(siscod, codpro);
    }

    public FaDescuentosProductosEstPK getFaDescuentosProductosEstPK() {
        return faDescuentosProductosEstPK;
    }

    public void setFaDescuentosProductosEstPK(FaDescuentosProductosEstPK faDescuentosProductosEstPK) {
        this.faDescuentosProductosEstPK = faDescuentosProductosEstPK;
    }

    public BigDecimal getDtopro() {
        return dtopro;
    }

    public void setDtopro(BigDecimal dtopro) {
        this.dtopro = dtopro;
    }

    public BigDecimal getUtipro2() {
        return utipro2;
    }

    public void setUtipro2(BigDecimal utipro2) {
        this.utipro2 = utipro2;
    }

    public Date getDatind() {
        return datind;
    }

    public void setDatind(Date datind) {
        this.datind = datind;
    }

    public Date getDatfid() {
        return datfid;
    }

    public void setDatfid(Date datfid) {
        this.datfid = datfid;
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

    public String getModfar() {
        return modfar;
    }

    public void setModfar(String modfar) {
        this.modfar = modfar;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (faDescuentosProductosEstPK != null ? faDescuentosProductosEstPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaDescuentosProductosEst)) {
            return false;
        }
        FaDescuentosProductosEst other = (FaDescuentosProductosEst) object;
        if ((this.faDescuentosProductosEstPK == null && other.faDescuentosProductosEstPK != null) || (this.faDescuentosProductosEstPK != null && !this.faDescuentosProductosEstPK.equals(other.faDescuentosProductosEstPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaDescuentosProductosEst[ faDescuentosProductosEstPK=" + faDescuentosProductosEstPK + " ]";
    }
    
}
