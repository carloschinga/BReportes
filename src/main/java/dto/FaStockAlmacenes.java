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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "fa_stock_almacenes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FaStockAlmacenes.findAll", query = "SELECT f FROM FaStockAlmacenes f"),
    @NamedQuery(name = "FaStockAlmacenes.findstkalm", query = "SELECT f.stkalm,f.stkalmM FROM FaStockAlmacenes f where f.faStockAlmacenesPK.codalm = :codalm and f.faStockAlmacenesPK.codpro = :codpro"),
    @NamedQuery(name = "FaStockAlmacenes.findByCodalm", query = "SELECT f FROM FaStockAlmacenes f WHERE f.faStockAlmacenesPK.codalm = :codalm"),
    @NamedQuery(name = "FaStockAlmacenes.findByCodpro", query = "SELECT f FROM FaStockAlmacenes f WHERE f.faStockAlmacenesPK.codpro = :codpro"),
    @NamedQuery(name = "FaStockAlmacenes.findByStkalm", query = "SELECT f FROM FaStockAlmacenes f WHERE f.stkalm = :stkalm"),
    @NamedQuery(name = "FaStockAlmacenes.findByStkalmM", query = "SELECT f FROM FaStockAlmacenes f WHERE f.stkalmM = :stkalmM"),
    @NamedQuery(name = "FaStockAlmacenes.findByStkmin", query = "SELECT f FROM FaStockAlmacenes f WHERE f.stkmin = :stkmin"),
    @NamedQuery(name = "FaStockAlmacenes.findByStkmax", query = "SELECT f FROM FaStockAlmacenes f WHERE f.stkmax = :stkmax"),
    @NamedQuery(name = "FaStockAlmacenes.findBySecinv", query = "SELECT f FROM FaStockAlmacenes f WHERE f.secinv = :secinv"),
    @NamedQuery(name = "FaStockAlmacenes.findByUbipro", query = "SELECT f FROM FaStockAlmacenes f WHERE f.ubipro = :ubipro"),
    @NamedQuery(name = "FaStockAlmacenes.findByEstado", query = "SELECT f FROM FaStockAlmacenes f WHERE f.estado = :estado"),
    @NamedQuery(name = "FaStockAlmacenes.findByFeccre", query = "SELECT f FROM FaStockAlmacenes f WHERE f.feccre = :feccre"),
    @NamedQuery(name = "FaStockAlmacenes.findByFecumv", query = "SELECT f FROM FaStockAlmacenes f WHERE f.fecumv = :fecumv"),
    @NamedQuery(name = "FaStockAlmacenes.findByUsecod", query = "SELECT f FROM FaStockAlmacenes f WHERE f.usecod = :usecod"),
    @NamedQuery(name = "FaStockAlmacenes.findByUsenam", query = "SELECT f FROM FaStockAlmacenes f WHERE f.usenam = :usenam"),
    @NamedQuery(name = "FaStockAlmacenes.findByHostname", query = "SELECT f FROM FaStockAlmacenes f WHERE f.hostname = :hostname"),
    @NamedQuery(name = "FaStockAlmacenes.findByCanven", query = "SELECT f FROM FaStockAlmacenes f WHERE f.canven = :canven"),
    @NamedQuery(name = "FaStockAlmacenes.findByDatven1", query = "SELECT f FROM FaStockAlmacenes f WHERE f.datven1 = :datven1"),
    @NamedQuery(name = "FaStockAlmacenes.findByDatven2", query = "SELECT f FROM FaStockAlmacenes f WHERE f.datven2 = :datven2"),
    @NamedQuery(name = "FaStockAlmacenes.findByDatven3", query = "SELECT f FROM FaStockAlmacenes f WHERE f.datven3 = :datven3"),
    @NamedQuery(name = "FaStockAlmacenes.findByStkhis", query = "SELECT f FROM FaStockAlmacenes f WHERE f.stkhis = :stkhis"),
    @NamedQuery(name = "FaStockAlmacenes.findByStkhisM", query = "SELECT f FROM FaStockAlmacenes f WHERE f.stkhisM = :stkhisM"),
    @NamedQuery(name = "FaStockAlmacenes.findByModfar", query = "SELECT f FROM FaStockAlmacenes f WHERE f.modfar = :modfar"),
    @NamedQuery(name = "FaStockAlmacenes.findByStkmin2", query = "SELECT f FROM FaStockAlmacenes f WHERE f.stkmin2 = :stkmin2"),
    @NamedQuery(name = "FaStockAlmacenes.findByStkmax2", query = "SELECT f FROM FaStockAlmacenes f WHERE f.stkmax2 = :stkmax2"),
    @NamedQuery(name = "FaStockAlmacenes.findByStkseg", query = "SELECT f FROM FaStockAlmacenes f WHERE f.stkseg = :stkseg")})
public class FaStockAlmacenes implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FaStockAlmacenesPK faStockAlmacenesPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stkalm")
    private int stkalm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stkalm_m")
    private int stkalmM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stkmin")
    private int stkmin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stkmax")
    private int stkmax;
    @Basic(optional = false)
    @NotNull
    @Column(name = "secinv")
    private int secinv;
    @Size(max = 6)
    @Column(name = "ubipro")
    private String ubipro;
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
    @Column(name = "canven")
    private int canven;
    @Column(name = "datven1")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datven1;
    @Column(name = "datven2")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datven2;
    @Column(name = "datven3")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datven3;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stkhis")
    private int stkhis;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stkhis_m")
    private int stkhisM;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "modfar")
    private String modfar;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "stkmin2")
    private BigDecimal stkmin2;
    @Column(name = "stkmax2")
    private BigDecimal stkmax2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stkseg")
    private int stkseg;
    @JoinColumn(name = "codpro", referencedColumnName = "codpro", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FaProductos faProductos;

    public FaStockAlmacenes() {
    }

    public FaStockAlmacenes(FaStockAlmacenesPK faStockAlmacenesPK) {
        this.faStockAlmacenesPK = faStockAlmacenesPK;
    }

    public FaStockAlmacenes(FaStockAlmacenesPK faStockAlmacenesPK, int stkalm, int stkalmM, int stkmin, int stkmax, int secinv, String estado, Date feccre, Date fecumv, int usecod, String usenam, String hostname, int canven, int stkhis, int stkhisM, String modfar, int stkseg) {
        this.faStockAlmacenesPK = faStockAlmacenesPK;
        this.stkalm = stkalm;
        this.stkalmM = stkalmM;
        this.stkmin = stkmin;
        this.stkmax = stkmax;
        this.secinv = secinv;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
        this.canven = canven;
        this.stkhis = stkhis;
        this.stkhisM = stkhisM;
        this.modfar = modfar;
        this.stkseg = stkseg;
    }

    public FaStockAlmacenes(String codalm, String codpro) {
        this.faStockAlmacenesPK = new FaStockAlmacenesPK(codalm, codpro);
    }

    public FaStockAlmacenesPK getFaStockAlmacenesPK() {
        return faStockAlmacenesPK;
    }

    public void setFaStockAlmacenesPK(FaStockAlmacenesPK faStockAlmacenesPK) {
        this.faStockAlmacenesPK = faStockAlmacenesPK;
    }

    public int getStkalm() {
        return stkalm;
    }

    public void setStkalm(int stkalm) {
        this.stkalm = stkalm;
    }

    public int getStkalmM() {
        return stkalmM;
    }

    public void setStkalmM(int stkalmM) {
        this.stkalmM = stkalmM;
    }

    public int getStkmin() {
        return stkmin;
    }

    public void setStkmin(int stkmin) {
        this.stkmin = stkmin;
    }

    public int getStkmax() {
        return stkmax;
    }

    public void setStkmax(int stkmax) {
        this.stkmax = stkmax;
    }

    public int getSecinv() {
        return secinv;
    }

    public void setSecinv(int secinv) {
        this.secinv = secinv;
    }

    public String getUbipro() {
        return ubipro;
    }

    public void setUbipro(String ubipro) {
        this.ubipro = ubipro;
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

    public int getCanven() {
        return canven;
    }

    public void setCanven(int canven) {
        this.canven = canven;
    }

    public Date getDatven1() {
        return datven1;
    }

    public void setDatven1(Date datven1) {
        this.datven1 = datven1;
    }

    public Date getDatven2() {
        return datven2;
    }

    public void setDatven2(Date datven2) {
        this.datven2 = datven2;
    }

    public Date getDatven3() {
        return datven3;
    }

    public void setDatven3(Date datven3) {
        this.datven3 = datven3;
    }

    public int getStkhis() {
        return stkhis;
    }

    public void setStkhis(int stkhis) {
        this.stkhis = stkhis;
    }

    public int getStkhisM() {
        return stkhisM;
    }

    public void setStkhisM(int stkhisM) {
        this.stkhisM = stkhisM;
    }

    public String getModfar() {
        return modfar;
    }

    public void setModfar(String modfar) {
        this.modfar = modfar;
    }

    public BigDecimal getStkmin2() {
        return stkmin2;
    }

    public void setStkmin2(BigDecimal stkmin2) {
        this.stkmin2 = stkmin2;
    }

    public BigDecimal getStkmax2() {
        return stkmax2;
    }

    public void setStkmax2(BigDecimal stkmax2) {
        this.stkmax2 = stkmax2;
    }

    public int getStkseg() {
        return stkseg;
    }

    public void setStkseg(int stkseg) {
        this.stkseg = stkseg;
    }

    public FaProductos getFaProductos() {
        return faProductos;
    }

    public void setFaProductos(FaProductos faProductos) {
        this.faProductos = faProductos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (faStockAlmacenesPK != null ? faStockAlmacenesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaStockAlmacenes)) {
            return false;
        }
        FaStockAlmacenes other = (FaStockAlmacenes) object;
        if ((this.faStockAlmacenesPK == null && other.faStockAlmacenesPK != null) || (this.faStockAlmacenesPK != null && !this.faStockAlmacenesPK.equals(other.faStockAlmacenesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaStockAlmacenes[ faStockAlmacenesPK=" + faStockAlmacenesPK + " ]";
    }
    
}
