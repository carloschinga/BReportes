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
@Table(name = "fa_proveedores")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FaProveedores.findAll", query = "SELECT f FROM FaProveedores f"),
    @NamedQuery(name = "FaProveedores.findAllJSON", query = "SELECT f.codprv,f.desprv,f.dpaprv FROM FaProveedores f where f.estado='S'"),
    @NamedQuery(name = "FaProveedores.findByCodprv", query = "SELECT f FROM FaProveedores f WHERE f.codprv = :codprv"),
    @NamedQuery(name = "FaProveedores.findByDesprv", query = "SELECT f FROM FaProveedores f WHERE f.desprv = :desprv"),
    @NamedQuery(name = "FaProveedores.findByObsprv", query = "SELECT f FROM FaProveedores f WHERE f.obsprv = :obsprv"),
    @NamedQuery(name = "FaProveedores.findByRucprv", query = "SELECT f FROM FaProveedores f WHERE f.rucprv = :rucprv"),
    @NamedQuery(name = "FaProveedores.findByDirprv", query = "SELECT f FROM FaProveedores f WHERE f.dirprv = :dirprv"),
    @NamedQuery(name = "FaProveedores.findByTelprv", query = "SELECT f FROM FaProveedores f WHERE f.telprv = :telprv"),
    @NamedQuery(name = "FaProveedores.findByBosprv", query = "SELECT f FROM FaProveedores f WHERE f.bosprv = :bosprv"),
    @NamedQuery(name = "FaProveedores.findByBroprv", query = "SELECT f FROM FaProveedores f WHERE f.broprv = :broprv"),
    @NamedQuery(name = "FaProveedores.findByUbicod", query = "SELECT f FROM FaProveedores f WHERE f.ubicod = :ubicod"),
    @NamedQuery(name = "FaProveedores.findByTelprv2", query = "SELECT f FROM FaProveedores f WHERE f.telprv2 = :telprv2"),
    @NamedQuery(name = "FaProveedores.findByAneprv", query = "SELECT f FROM FaProveedores f WHERE f.aneprv = :aneprv"),
    @NamedQuery(name = "FaProveedores.findByFaxprv", query = "SELECT f FROM FaProveedores f WHERE f.faxprv = :faxprv"),
    @NamedQuery(name = "FaProveedores.findByCreprv", query = "SELECT f FROM FaProveedores f WHERE f.creprv = :creprv"),
    @NamedQuery(name = "FaProveedores.findByDpaprv", query = "SELECT f FROM FaProveedores f WHERE f.dpaprv = :dpaprv"),
    @NamedQuery(name = "FaProveedores.findByCreprvD", query = "SELECT f FROM FaProveedores f WHERE f.creprvD = :creprvD"),
    @NamedQuery(name = "FaProveedores.findByEstado", query = "SELECT f FROM FaProveedores f WHERE f.estado = :estado"),
    @NamedQuery(name = "FaProveedores.findByFeccre", query = "SELECT f FROM FaProveedores f WHERE f.feccre = :feccre"),
    @NamedQuery(name = "FaProveedores.findByFecumv", query = "SELECT f FROM FaProveedores f WHERE f.fecumv = :fecumv"),
    @NamedQuery(name = "FaProveedores.findByUsecod", query = "SELECT f FROM FaProveedores f WHERE f.usecod = :usecod"),
    @NamedQuery(name = "FaProveedores.findByUsenam", query = "SELECT f FROM FaProveedores f WHERE f.usenam = :usenam"),
    @NamedQuery(name = "FaProveedores.findByHostname", query = "SELECT f FROM FaProveedores f WHERE f.hostname = :hostname"),
    @NamedQuery(name = "FaProveedores.findByMonlimcom", query = "SELECT f FROM FaProveedores f WHERE f.monlimcom = :monlimcom"),
    @NamedQuery(name = "FaProveedores.findByStaagreten", query = "SELECT f FROM FaProveedores f WHERE f.staagreten = :staagreten"),
    @NamedQuery(name = "FaProveedores.findByStaapldetra", query = "SELECT f FROM FaProveedores f WHERE f.staapldetra = :staapldetra"),
    @NamedQuery(name = "FaProveedores.findByDiralmprv", query = "SELECT f FROM FaProveedores f WHERE f.diralmprv = :diralmprv"),
    @NamedQuery(name = "FaProveedores.findByUbicodalm", query = "SELECT f FROM FaProveedores f WHERE f.ubicodalm = :ubicodalm"),
    @NamedQuery(name = "FaProveedores.findByComentprv", query = "SELECT f FROM FaProveedores f WHERE f.comentprv = :comentprv"),
    @NamedQuery(name = "FaProveedores.findByDiasvigoc", query = "SELECT f FROM FaProveedores f WHERE f.diasvigoc = :diasvigoc")})
public class FaProveedores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "codprv")
    private String codprv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "desprv")
    private String desprv;
    @Size(max = 40)
    @Column(name = "obsprv")
    private String obsprv;
    @Size(max = 15)
    @Column(name = "rucprv")
    private String rucprv;
    @Size(max = 45)
    @Column(name = "dirprv")
    private String dirprv;
    @Size(max = 14)
    @Column(name = "telprv")
    private String telprv;
    @Size(max = 30)
    @Column(name = "bosprv")
    private String bosprv;
    @Size(max = 30)
    @Column(name = "broprv")
    private String broprv;
    @Size(max = 6)
    @Column(name = "ubicod")
    private String ubicod;
    @Size(max = 14)
    @Column(name = "telprv2")
    private String telprv2;
    @Size(max = 4)
    @Column(name = "aneprv")
    private String aneprv;
    @Size(max = 14)
    @Column(name = "faxprv")
    private String faxprv;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "creprv")
    private BigDecimal creprv;
    @Column(name = "dpaprv")
    private Integer dpaprv;
    @Column(name = "creprv_d")
    private BigDecimal creprvD;
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
    @Column(name = "monlimcom")
    private BigDecimal monlimcom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "staagreten")
    private String staagreten;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "staapldetra")
    private String staapldetra;
    @Size(max = 70)
    @Column(name = "diralmprv")
    private String diralmprv;
    @Size(max = 6)
    @Column(name = "ubicodalm")
    private String ubicodalm;
    @Size(max = 200)
    @Column(name = "comentprv")
    private String comentprv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "diasvigoc")
    private int diasvigoc;

    public FaProveedores() {
    }

    public FaProveedores(String codprv) {
        this.codprv = codprv;
    }

    public FaProveedores(String codprv, String desprv, String estado, Date feccre, Date fecumv, int usecod, String usenam, String hostname, BigDecimal monlimcom, String staagreten, String staapldetra, int diasvigoc) {
        this.codprv = codprv;
        this.desprv = desprv;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
        this.monlimcom = monlimcom;
        this.staagreten = staagreten;
        this.staapldetra = staapldetra;
        this.diasvigoc = diasvigoc;
    }

    public String getCodprv() {
        return codprv;
    }

    public void setCodprv(String codprv) {
        this.codprv = codprv;
    }

    public String getDesprv() {
        return desprv;
    }

    public void setDesprv(String desprv) {
        this.desprv = desprv;
    }

    public String getObsprv() {
        return obsprv;
    }

    public void setObsprv(String obsprv) {
        this.obsprv = obsprv;
    }

    public String getRucprv() {
        return rucprv;
    }

    public void setRucprv(String rucprv) {
        this.rucprv = rucprv;
    }

    public String getDirprv() {
        return dirprv;
    }

    public void setDirprv(String dirprv) {
        this.dirprv = dirprv;
    }

    public String getTelprv() {
        return telprv;
    }

    public void setTelprv(String telprv) {
        this.telprv = telprv;
    }

    public String getBosprv() {
        return bosprv;
    }

    public void setBosprv(String bosprv) {
        this.bosprv = bosprv;
    }

    public String getBroprv() {
        return broprv;
    }

    public void setBroprv(String broprv) {
        this.broprv = broprv;
    }

    public String getUbicod() {
        return ubicod;
    }

    public void setUbicod(String ubicod) {
        this.ubicod = ubicod;
    }

    public String getTelprv2() {
        return telprv2;
    }

    public void setTelprv2(String telprv2) {
        this.telprv2 = telprv2;
    }

    public String getAneprv() {
        return aneprv;
    }

    public void setAneprv(String aneprv) {
        this.aneprv = aneprv;
    }

    public String getFaxprv() {
        return faxprv;
    }

    public void setFaxprv(String faxprv) {
        this.faxprv = faxprv;
    }

    public BigDecimal getCreprv() {
        return creprv;
    }

    public void setCreprv(BigDecimal creprv) {
        this.creprv = creprv;
    }

    public Integer getDpaprv() {
        return dpaprv;
    }

    public void setDpaprv(Integer dpaprv) {
        this.dpaprv = dpaprv;
    }

    public BigDecimal getCreprvD() {
        return creprvD;
    }

    public void setCreprvD(BigDecimal creprvD) {
        this.creprvD = creprvD;
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

    public BigDecimal getMonlimcom() {
        return monlimcom;
    }

    public void setMonlimcom(BigDecimal monlimcom) {
        this.monlimcom = monlimcom;
    }

    public String getStaagreten() {
        return staagreten;
    }

    public void setStaagreten(String staagreten) {
        this.staagreten = staagreten;
    }

    public String getStaapldetra() {
        return staapldetra;
    }

    public void setStaapldetra(String staapldetra) {
        this.staapldetra = staapldetra;
    }

    public String getDiralmprv() {
        return diralmprv;
    }

    public void setDiralmprv(String diralmprv) {
        this.diralmprv = diralmprv;
    }

    public String getUbicodalm() {
        return ubicodalm;
    }

    public void setUbicodalm(String ubicodalm) {
        this.ubicodalm = ubicodalm;
    }

    public String getComentprv() {
        return comentprv;
    }

    public void setComentprv(String comentprv) {
        this.comentprv = comentprv;
    }

    public int getDiasvigoc() {
        return diasvigoc;
    }

    public void setDiasvigoc(int diasvigoc) {
        this.diasvigoc = diasvigoc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codprv != null ? codprv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaProveedores)) {
            return false;
        }
        FaProveedores other = (FaProveedores) object;
        if ((this.codprv == null && other.codprv != null) || (this.codprv != null && !this.codprv.equals(other.codprv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaProveedores[ codprv=" + codprv + " ]";
    }
    
}
