/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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
@Table(name = "fa_movimientos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FaMovimientos.findAll", query = "SELECT f FROM FaMovimientos f"),
    @NamedQuery(name = "FaMovimientos.findByInvnum", query = "SELECT f FROM FaMovimientos f WHERE f.invnum = :invnum"),
    @NamedQuery(name = "FaMovimientos.findByInvnummayor", query = "SELECT f.invnum FROM FaMovimientos f WHERE f.invnum >= :invnum and f.tipkar='DE'  and f.movsta='G'"),
    @NamedQuery(name = "FaMovimientos.findByInvnumInRange", query = "SELECT f.invnum FROM FaMovimientos f WHERE f.invnum >= :invnumMin AND f.invnum <= :invnumMax and f.tipkar='DE'  and f.movsta='G'"),
    @NamedQuery(name = "FaMovimientos.findByNumdoc", query = "SELECT f FROM FaMovimientos f WHERE f.numdoc = :numdoc"),
    @NamedQuery(name = "FaMovimientos.findByPurnum", query = "SELECT f FROM FaMovimientos f WHERE f.purnum = :purnum"),
    @NamedQuery(name = "FaMovimientos.findByFecdoc", query = "SELECT f FROM FaMovimientos f WHERE f.fecdoc = :fecdoc"),
    @NamedQuery(name = "FaMovimientos.findByFecmov", query = "SELECT f FROM FaMovimientos f WHERE f.fecmov = :fecmov"),
    @NamedQuery(name = "FaMovimientos.findByTotmov", query = "SELECT f FROM FaMovimientos f WHERE f.totmov = :totmov"),
    @NamedQuery(name = "FaMovimientos.findByDtomov", query = "SELECT f FROM FaMovimientos f WHERE f.dtomov = :dtomov"),
    @NamedQuery(name = "FaMovimientos.findByBonmov", query = "SELECT f FROM FaMovimientos f WHERE f.bonmov = :bonmov"),
    @NamedQuery(name = "FaMovimientos.findByIgvmov", query = "SELECT f FROM FaMovimientos f WHERE f.igvmov = :igvmov"),
    @NamedQuery(name = "FaMovimientos.findByVvfmov", query = "SELECT f FROM FaMovimientos f WHERE f.vvfmov = :vvfmov"),
    @NamedQuery(name = "FaMovimientos.findByObsmov", query = "SELECT f FROM FaMovimientos f WHERE f.obsmov = :obsmov"),
    @NamedQuery(name = "FaMovimientos.findByCodalm2", query = "SELECT f FROM FaMovimientos f WHERE f.codalm2 = :codalm2"),
    @NamedQuery(name = "FaMovimientos.findByUbipro", query = "SELECT f FROM FaMovimientos f WHERE f.ubipro = :ubipro"),
    @NamedQuery(name = "FaMovimientos.findByDtopro1", query = "SELECT f FROM FaMovimientos f WHERE f.dtopro1 = :dtopro1"),
    @NamedQuery(name = "FaMovimientos.findByDtopro2", query = "SELECT f FROM FaMovimientos f WHERE f.dtopro2 = :dtopro2"),
    @NamedQuery(name = "FaMovimientos.findByMoncod", query = "SELECT f FROM FaMovimientos f WHERE f.moncod = :moncod"),
    @NamedQuery(name = "FaMovimientos.findByCodccs", query = "SELECT f FROM FaMovimientos f WHERE f.codccs = :codccs"),
    @NamedQuery(name = "FaMovimientos.findByTipcam", query = "SELECT f FROM FaMovimientos f WHERE f.tipcam = :tipcam"),
    @NamedQuery(name = "FaMovimientos.findByInvkar", query = "SELECT f FROM FaMovimientos f WHERE f.invkar = :invkar"),
    @NamedQuery(name = "FaMovimientos.findByEstado", query = "SELECT f FROM FaMovimientos f WHERE f.estado = :estado"),
    @NamedQuery(name = "FaMovimientos.findByFeccre", query = "SELECT f FROM FaMovimientos f WHERE f.feccre = :feccre"),
    @NamedQuery(name = "FaMovimientos.findByFecumv", query = "SELECT f FROM FaMovimientos f WHERE f.fecumv = :fecumv"),
    @NamedQuery(name = "FaMovimientos.findByUsecod", query = "SELECT f FROM FaMovimientos f WHERE f.usecod = :usecod"),
    @NamedQuery(name = "FaMovimientos.findByUsenam", query = "SELECT f FROM FaMovimientos f WHERE f.usenam = :usenam"),
    @NamedQuery(name = "FaMovimientos.findByHostname", query = "SELECT f FROM FaMovimientos f WHERE f.hostname = :hostname"),
    @NamedQuery(name = "FaMovimientos.findByFecpag", query = "SELECT f FROM FaMovimientos f WHERE f.fecpag = :fecpag"),
    @NamedQuery(name = "FaMovimientos.findBySerdoc", query = "SELECT f FROM FaMovimientos f WHERE f.serdoc = :serdoc"),
    @NamedQuery(name = "FaMovimientos.findByMovndias", query = "SELECT f FROM FaMovimientos f WHERE f.movndias = :movndias"),
    @NamedQuery(name = "FaMovimientos.findByMovsta", query = "SELECT f FROM FaMovimientos f WHERE f.movsta = :movsta"),
    @NamedQuery(name = "FaMovimientos.findBySiscodD", query = "SELECT f FROM FaMovimientos f WHERE f.siscodD = :siscodD"),
    @NamedQuery(name = "FaMovimientos.findByInvkarEst", query = "SELECT f FROM FaMovimientos f WHERE f.invkarEst = :invkarEst"),
    @NamedQuery(name = "FaMovimientos.findByInvnumEst", query = "SELECT f FROM FaMovimientos f WHERE f.invnumEst = :invnumEst"),
    @NamedQuery(name = "FaMovimientos.findByAlmtrsta", query = "SELECT f FROM FaMovimientos f WHERE f.almtrsta = :almtrsta"),
    @NamedQuery(name = "FaMovimientos.findByCodalmTra", query = "SELECT f FROM FaMovimientos f WHERE f.codalmTra = :codalmTra"),
    @NamedQuery(name = "FaMovimientos.findByCargmov", query = "SELECT f FROM FaMovimientos f WHERE f.cargmov = :cargmov"),
    @NamedQuery(name = "FaMovimientos.findByStaproc", query = "SELECT f FROM FaMovimientos f WHERE f.staproc = :staproc"),
    @NamedQuery(name = "FaMovimientos.findByFecanu", query = "SELECT f FROM FaMovimientos f WHERE f.fecanu = :fecanu"),
    @NamedQuery(name = "FaMovimientos.findByUseanu", query = "SELECT f FROM FaMovimientos f WHERE f.useanu = :useanu"),
    @NamedQuery(name = "FaMovimientos.findByInvnumCen", query = "SELECT f FROM FaMovimientos f WHERE f.invnumCen = :invnumCen"),
    @NamedQuery(name = "FaMovimientos.findByRefmov1", query = "SELECT f FROM FaMovimientos f WHERE f.refmov1 = :refmov1"),
    @NamedQuery(name = "FaMovimientos.findByRefmov2", query = "SELECT f FROM FaMovimientos f WHERE f.refmov2 = :refmov2"),
    @NamedQuery(name = "FaMovimientos.findByRefmov3", query = "SELECT f FROM FaMovimientos f WHERE f.refmov3 = :refmov3"),
    @NamedQuery(name = "FaMovimientos.findByRefmov4", query = "SELECT f FROM FaMovimientos f WHERE f.refmov4 = :refmov4"),
    @NamedQuery(name = "FaMovimientos.findByMovstadis", query = "SELECT f FROM FaMovimientos f WHERE f.movstadis = :movstadis"),
    @NamedQuery(name = "FaMovimientos.findByVvfmovA", query = "SELECT f FROM FaMovimientos f WHERE f.vvfmovA = :vvfmovA"),
    @NamedQuery(name = "FaMovimientos.findByVvfmovI", query = "SELECT f FROM FaMovimientos f WHERE f.vvfmovI = :vvfmovI"),
    @NamedQuery(name = "FaMovimientos.findByOrdvalFlag", query = "SELECT f FROM FaMovimientos f WHERE f.ordvalFlag = :ordvalFlag"),
    @NamedQuery(name = "FaMovimientos.findByOrdvalCom", query = "SELECT f FROM FaMovimientos f WHERE f.ordvalCom = :ordvalCom"),
    @NamedQuery(name = "FaMovimientos.findByCcnoprgsta", query = "SELECT f FROM FaMovimientos f WHERE f.ccnoprgsta = :ccnoprgsta"),
    @NamedQuery(name = "FaMovimientos.findByEstadoMigracion", query = "SELECT f FROM FaMovimientos f WHERE f.estadoMigracion = :estadoMigracion"),
    @NamedQuery(name = "FaMovimientos.findByFecval", query = "SELECT f FROM FaMovimientos f WHERE f.fecval = :fecval"),
    @NamedQuery(name = "FaMovimientos.findByUseval", query = "SELECT f FROM FaMovimientos f WHERE f.useval = :useval"),
    @NamedQuery(name = "FaMovimientos.findByInvnumRef", query = "SELECT f FROM FaMovimientos f WHERE f.invnumRef = :invnumRef"),
    @NamedQuery(name = "FaMovimientos.findByInvnumAsoc", query = "SELECT f FROM FaMovimientos f WHERE f.invnumAsoc = :invnumAsoc"),
    @NamedQuery(name = "FaMovimientos.findByTdoser", query = "SELECT f FROM FaMovimientos f WHERE f.tdoser = :tdoser"),
    @NamedQuery(name = "FaMovimientos.findByPbruto", query = "SELECT f FROM FaMovimientos f WHERE f.pbruto = :pbruto"),
    @NamedQuery(name = "FaMovimientos.findByPlacavehic", query = "SELECT f FROM FaMovimientos f WHERE f.placavehic = :placavehic"),
    @NamedQuery(name = "FaMovimientos.findByInvnumDoce", query = "SELECT f FROM FaMovimientos f WHERE f.invnumDoce = :invnumDoce"),
    @NamedQuery(name = "FaMovimientos.findByFectras", query = "SELECT f FROM FaMovimientos f WHERE f.fectras = :fectras")})
public class FaMovimientos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "invnum")
    private Integer invnum;
    @Size(max = 10)
    @Column(name = "numdoc")
    private String numdoc;
    @Size(max = 2)
    @Column(name = "tipkar")
    private String tipkar;
    @Size(max = 2)
    @NotNull
    @Column(name = "codalm")
    private String codalm;
    @Column(name = "purnum")
    private Integer purnum;
    @Column(name = "fecdoc")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecdoc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecmov")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecmov;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "totmov")
    private BigDecimal totmov;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtomov")
    private BigDecimal dtomov;
    @Basic(optional = false)
    @NotNull
    @Column(name = "bonmov")
    private BigDecimal bonmov;
    @Basic(optional = false)
    @NotNull
    @Column(name = "igvmov")
    private BigDecimal igvmov;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vvfmov")
    private BigDecimal vvfmov;
    @Size(max = 40)
    @Column(name = "obsmov")
    private String obsmov;
    @Size(max = 2)
    @Column(name = "codalm2")
    private String codalm2;
    @Size(max = 2)
    @Column(name = "ubipro")
    private String ubipro;
    @Column(name = "dtopro1")
    private BigDecimal dtopro1;
    @Column(name = "dtopro2")
    private BigDecimal dtopro2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "moncod")
    private String moncod;
    @Size(max = 2)
    @Column(name = "codccs")
    private String codccs;
    @Column(name = "tipcam")
    private BigDecimal tipcam;
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
    @Column(name = "fecpag")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecpag;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "serdoc")
    private String serdoc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "movndias")
    private int movndias;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "movsta")
    private String movsta;
    @Column(name = "siscod_d")
    private Integer siscodD;
    @Basic(optional = false)
    @NotNull
    @Column(name = "invkar_est")
    private int invkarEst;
    @Basic(optional = false)
    @NotNull
    @Column(name = "invnum_est")
    private int invnumEst;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "almtrsta")
    private String almtrsta;
    @Size(max = 2)
    @Column(name = "codalm_tra")
    private String codalmTra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cargmov")
    private BigDecimal cargmov;
    @Size(max = 1)
    @Column(name = "staproc")
    private String staproc;
    @Column(name = "fecanu")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecanu;
    @Column(name = "useanu")
    private Integer useanu;
    @Basic(optional = false)
    @NotNull
    @Column(name = "invnum_cen")
    private int invnumCen;
    @Size(max = 60)
    @Column(name = "refmov1")
    private String refmov1;
    @Size(max = 60)
    @Column(name = "refmov2")
    private String refmov2;
    @Size(max = 60)
    @Column(name = "refmov3")
    private String refmov3;
    @Size(max = 60)
    @Column(name = "refmov4")
    private String refmov4;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "movstadis")
    private String movstadis;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vvfmov_a")
    private BigDecimal vvfmovA;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vvfmov_i")
    private BigDecimal vvfmovI;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "ordval_flag")
    private String ordvalFlag;
    @Size(max = 200)
    @Column(name = "ordval_com")
    private String ordvalCom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "ccnoprgsta")
    private String ccnoprgsta;
    @Column(name = "EstadoMigracion")
    private Boolean estadoMigracion;
    @Column(name = "fecval")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecval;
    @Column(name = "useval")
    private Integer useval;
    @Basic(optional = false)
    @NotNull
    @Column(name = "invnum_ref")
    private int invnumRef;
    @Basic(optional = false)
    @NotNull
    @Column(name = "invnum_asoc")
    private int invnumAsoc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "tdoser")
    private String tdoser;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pbruto")
    private BigDecimal pbruto;
    @Size(max = 20)
    @Column(name = "placavehic")
    private String placavehic;
    @Basic(optional = false)
    @NotNull
    @Column(name = "invnum_doce")
    private int invnumDoce;
    @Column(name = "fectras")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fectras;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "faMovimientos")
    private List<FaMovimientosDetalle> faMovimientosDetalleList;

    public FaMovimientos() {
    }

    public FaMovimientos(Integer invnum) {
        this.invnum = invnum;
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
    }

    public FaMovimientos(Integer invnum, Date fecmov, BigDecimal totmov, BigDecimal dtomov, BigDecimal bonmov, BigDecimal igvmov, BigDecimal vvfmov, String moncod, String estado, Date feccre, Date fecumv, int usecod, String usenam, String hostname, String serdoc, int movndias, String movsta, int invkarEst, int invnumEst, String almtrsta, BigDecimal cargmov, int invnumCen, String movstadis, BigDecimal vvfmovA, BigDecimal vvfmovI, String ordvalFlag, String ccnoprgsta, int invnumRef, int invnumAsoc, String tdoser, BigDecimal pbruto, int invnumDoce) {
        this.invnum = invnum;
        this.fecmov = fecmov;
        this.totmov = totmov;
        this.dtomov = dtomov;
        this.bonmov = bonmov;
        this.igvmov = igvmov;
        this.vvfmov = vvfmov;
        this.moncod = moncod;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
        this.serdoc = serdoc;
        this.movndias = movndias;
        this.movsta = movsta;
        this.invkarEst = invkarEst;
        this.invnumEst = invnumEst;
        this.almtrsta = almtrsta;
        this.cargmov = cargmov;
        this.invnumCen = invnumCen;
        this.movstadis = movstadis;
        this.vvfmovA = vvfmovA;
        this.vvfmovI = vvfmovI;
        this.ordvalFlag = ordvalFlag;
        this.ccnoprgsta = ccnoprgsta;
        this.invnumRef = invnumRef;
        this.invnumAsoc = invnumAsoc;
        this.tdoser = tdoser;
        this.pbruto = pbruto;
        this.invnumDoce = invnumDoce;
    }

    public Integer getInvnum() {
        return invnum;
    }

    public void setInvnum(Integer invnum) {
        this.invnum = invnum;
    }

    public String getNumdoc() {
        return numdoc;
    }

    public void setNumdoc(String numdoc) {
        this.numdoc = numdoc;
    }

    public Integer getPurnum() {
        return purnum;
    }

    public void setPurnum(Integer purnum) {
        this.purnum = purnum;
    }

    public Date getFecdoc() {
        return fecdoc;
    }

    public void setFecdoc(Date fecdoc) {
        this.fecdoc = fecdoc;
    }

    public Date getFecmov() {
        return fecmov;
    }

    public void setFecmov(Date fecmov) {
        this.fecmov = fecmov;
    }

    public BigDecimal getTotmov() {
        return totmov;
    }

    public void setTotmov(BigDecimal totmov) {
        this.totmov = totmov;
    }

    public BigDecimal getDtomov() {
        return dtomov;
    }

    public void setDtomov(BigDecimal dtomov) {
        this.dtomov = dtomov;
    }

    public BigDecimal getBonmov() {
        return bonmov;
    }

    public void setBonmov(BigDecimal bonmov) {
        this.bonmov = bonmov;
    }

    public BigDecimal getIgvmov() {
        return igvmov;
    }

    public void setIgvmov(BigDecimal igvmov) {
        this.igvmov = igvmov;
    }

    public BigDecimal getVvfmov() {
        return vvfmov;
    }

    public void setVvfmov(BigDecimal vvfmov) {
        this.vvfmov = vvfmov;
    }

    public String getObsmov() {
        return obsmov;
    }

    public void setObsmov(String obsmov) {
        this.obsmov = obsmov;
    }

    public String getCodalm2() {
        return codalm2;
    }

    public void setCodalm2(String codalm2) {
        this.codalm2 = codalm2;
    }

    public String getUbipro() {
        return ubipro;
    }

    public void setUbipro(String ubipro) {
        this.ubipro = ubipro;
    }

    public BigDecimal getDtopro1() {
        return dtopro1;
    }

    public void setDtopro1(BigDecimal dtopro1) {
        this.dtopro1 = dtopro1;
    }

    public BigDecimal getDtopro2() {
        return dtopro2;
    }

    public void setDtopro2(BigDecimal dtopro2) {
        this.dtopro2 = dtopro2;
    }

    public String getMoncod() {
        return moncod;
    }

    public void setMoncod(String moncod) {
        this.moncod = moncod;
    }

    public String getCodccs() {
        return codccs;
    }

    public void setCodccs(String codccs) {
        this.codccs = codccs;
    }

    public BigDecimal getTipcam() {
        return tipcam;
    }

    public void setTipcam(BigDecimal tipcam) {
        this.tipcam = tipcam;
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

    public String getTipkar() {
        return tipkar;
    }

    public void setTipkar(String tipkar) {
        this.tipkar = tipkar;
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

    public Date getFecpag() {
        return fecpag;
    }

    public void setFecpag(Date fecpag) {
        this.fecpag = fecpag;
    }

    public String getSerdoc() {
        return serdoc;
    }

    public void setSerdoc(String serdoc) {
        this.serdoc = serdoc;
    }

    public int getMovndias() {
        return movndias;
    }

    public void setMovndias(int movndias) {
        this.movndias = movndias;
    }

    public String getMovsta() {
        return movsta;
    }

    public void setMovsta(String movsta) {
        this.movsta = movsta;
    }

    public Integer getSiscodD() {
        return siscodD;
    }

    public void setSiscodD(Integer siscodD) {
        this.siscodD = siscodD;
    }

    public int getInvkarEst() {
        return invkarEst;
    }

    public void setInvkarEst(int invkarEst) {
        this.invkarEst = invkarEst;
    }

    public int getInvnumEst() {
        return invnumEst;
    }

    public void setInvnumEst(int invnumEst) {
        this.invnumEst = invnumEst;
    }

    public String getAlmtrsta() {
        return almtrsta;
    }

    public void setAlmtrsta(String almtrsta) {
        this.almtrsta = almtrsta;
    }

    public String getCodalmTra() {
        return codalmTra;
    }

    public void setCodalmTra(String codalmTra) {
        this.codalmTra = codalmTra;
    }

    public BigDecimal getCargmov() {
        return cargmov;
    }

    public void setCargmov(BigDecimal cargmov) {
        this.cargmov = cargmov;
    }

    public String getStaproc() {
        return staproc;
    }

    public void setStaproc(String staproc) {
        this.staproc = staproc;
    }

    public Date getFecanu() {
        return fecanu;
    }

    public void setFecanu(Date fecanu) {
        this.fecanu = fecanu;
    }

    public Integer getUseanu() {
        return useanu;
    }

    public void setUseanu(Integer useanu) {
        this.useanu = useanu;
    }

    public int getInvnumCen() {
        return invnumCen;
    }

    public void setInvnumCen(int invnumCen) {
        this.invnumCen = invnumCen;
    }

    public String getRefmov1() {
        return refmov1;
    }

    public void setRefmov1(String refmov1) {
        this.refmov1 = refmov1;
    }

    public String getRefmov2() {
        return refmov2;
    }

    public void setRefmov2(String refmov2) {
        this.refmov2 = refmov2;
    }

    public String getRefmov3() {
        return refmov3;
    }

    public void setRefmov3(String refmov3) {
        this.refmov3 = refmov3;
    }

    public String getRefmov4() {
        return refmov4;
    }

    public void setRefmov4(String refmov4) {
        this.refmov4 = refmov4;
    }

    public String getMovstadis() {
        return movstadis;
    }

    public void setMovstadis(String movstadis) {
        this.movstadis = movstadis;
    }

    public BigDecimal getVvfmovA() {
        return vvfmovA;
    }

    public void setVvfmovA(BigDecimal vvfmovA) {
        this.vvfmovA = vvfmovA;
    }

    public BigDecimal getVvfmovI() {
        return vvfmovI;
    }

    public void setVvfmovI(BigDecimal vvfmovI) {
        this.vvfmovI = vvfmovI;
    }

    public String getOrdvalFlag() {
        return ordvalFlag;
    }

    public void setOrdvalFlag(String ordvalFlag) {
        this.ordvalFlag = ordvalFlag;
    }

    public String getOrdvalCom() {
        return ordvalCom;
    }

    public void setOrdvalCom(String ordvalCom) {
        this.ordvalCom = ordvalCom;
    }

    public String getCcnoprgsta() {
        return ccnoprgsta;
    }

    public void setCcnoprgsta(String ccnoprgsta) {
        this.ccnoprgsta = ccnoprgsta;
    }

    public Boolean getEstadoMigracion() {
        return estadoMigracion;
    }

    public void setEstadoMigracion(Boolean estadoMigracion) {
        this.estadoMigracion = estadoMigracion;
    }

    public Date getFecval() {
        return fecval;
    }

    public void setFecval(Date fecval) {
        this.fecval = fecval;
    }

    public Integer getUseval() {
        return useval;
    }

    public void setUseval(Integer useval) {
        this.useval = useval;
    }

    public int getInvnumRef() {
        return invnumRef;
    }

    public void setInvnumRef(int invnumRef) {
        this.invnumRef = invnumRef;
    }

    public int getInvnumAsoc() {
        return invnumAsoc;
    }

    public void setInvnumAsoc(int invnumAsoc) {
        this.invnumAsoc = invnumAsoc;
    }

    public String getTdoser() {
        return tdoser;
    }

    public void setTdoser(String tdoser) {
        this.tdoser = tdoser;
    }

    public BigDecimal getPbruto() {
        return pbruto;
    }

    public void setPbruto(BigDecimal pbruto) {
        this.pbruto = pbruto;
    }

    public String getPlacavehic() {
        return placavehic;
    }

    public void setPlacavehic(String placavehic) {
        this.placavehic = placavehic;
    }

    public int getInvnumDoce() {
        return invnumDoce;
    }

    public void setInvnumDoce(int invnumDoce) {
        this.invnumDoce = invnumDoce;
    }

    public Date getFectras() {
        return fectras;
    }

    public void setFectras(Date fectras) {
        this.fectras = fectras;
    }

    @XmlTransient
    public List<FaMovimientosDetalle> getFaMovimientosDetalleList() {
        return faMovimientosDetalleList;
    }

    public void setFaMovimientosDetalleList(List<FaMovimientosDetalle> faMovimientosDetalleList) {
        this.faMovimientosDetalleList = faMovimientosDetalleList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invnum != null ? invnum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaMovimientos)) {
            return false;
        }
        FaMovimientos other = (FaMovimientos) object;
        if ((this.invnum == null && other.invnum != null) || (this.invnum != null && !this.invnum.equals(other.invnum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaMovimientos[ invnum=" + invnum + " ]";
    }

}
