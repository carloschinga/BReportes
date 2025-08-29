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
@Table(name = "fa_movimientos_detalle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FaMovimientosDetalle.findAll", query = "SELECT f FROM FaMovimientosDetalle f"),
    @NamedQuery(name = "FaMovimientosDetalle.findByInvnum", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.faMovimientosDetallePK.invnum = :invnum"),
    @NamedQuery(name = "FaMovimientosDetalle.findDetByInvnum", query = "SELECT f.codpro.despro,f.qtypro,f.qtpproM,f.codlot FROM FaMovimientosDetalle f WHERE f.faMovimientosDetallePK.invnum = :invnum and f.movsta='G'"),
    @NamedQuery(name = "FaMovimientosDetalle.findByInvnumcodprocodlot", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.faMovimientosDetallePK.invnum = :invnum AND f.codpro.codpro = :codpro AND f.codlot = :codlot and f.movsta='G'"),
    @NamedQuery(name = "FaMovimientosDetalle.findByNumitm", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.faMovimientosDetallePK.numitm = :numitm"),
    @NamedQuery(name = "FaMovimientosDetalle.findByQtypro", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.qtypro = :qtypro"),
    @NamedQuery(name = "FaMovimientosDetalle.findByQtppro", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.qtppro = :qtppro"),
    @NamedQuery(name = "FaMovimientosDetalle.findByQtbpro", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.qtbpro = :qtbpro"),
    @NamedQuery(name = "FaMovimientosDetalle.findByPvpsal", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.pvpsal = :pvpsal"),
    @NamedQuery(name = "FaMovimientosDetalle.findByVvfsal", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.vvfsal = :vvfsal"),
    @NamedQuery(name = "FaMovimientosDetalle.findByPvfsal", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.pvfsal = :pvfsal"),
    @NamedQuery(name = "FaMovimientosDetalle.findByDtopro", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.dtopro = :dtopro"),
    @NamedQuery(name = "FaMovimientosDetalle.findByFecven", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.fecven = :fecven"),
    @NamedQuery(name = "FaMovimientosDetalle.findByStkalm", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.stkalm = :stkalm"),
    @NamedQuery(name = "FaMovimientosDetalle.findByStkalmM", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.stkalmM = :stkalmM"),
    @NamedQuery(name = "FaMovimientosDetalle.findByParpro", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.parpro = :parpro"),
    @NamedQuery(name = "FaMovimientosDetalle.findByTotpro", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.totpro = :totpro"),
    @NamedQuery(name = "FaMovimientosDetalle.findByCovtip", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.covtip = :covtip"),
    @NamedQuery(name = "FaMovimientosDetalle.findByIgvpro", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.igvpro = :igvpro"),
    @NamedQuery(name = "FaMovimientosDetalle.findByDtopro1", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.dtopro1 = :dtopro1"),
    @NamedQuery(name = "FaMovimientosDetalle.findByDtopro2", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.dtopro2 = :dtopro2"),
    @NamedQuery(name = "FaMovimientosDetalle.findByDtopro3", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.dtopro3 = :dtopro3"),
    @NamedQuery(name = "FaMovimientosDetalle.findByDtopro4", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.dtopro4 = :dtopro4"),
    @NamedQuery(name = "FaMovimientosDetalle.findByCostod", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.costod = :costod"),
    @NamedQuery(name = "FaMovimientosDetalle.findByCoscom", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.coscom = :coscom"),
    @NamedQuery(name = "FaMovimientosDetalle.findByPrisal", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.prisal = :prisal"),
    @NamedQuery(name = "FaMovimientosDetalle.findByStamod", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.stamod = :stamod"),
    @NamedQuery(name = "FaMovimientosDetalle.findByQtyproM", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.qtyproM = :qtyproM"),
    @NamedQuery(name = "FaMovimientosDetalle.findByUsecod", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.usecod = :usecod"),
    @NamedQuery(name = "FaMovimientosDetalle.findByStkfra", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.stkfra = :stkfra"),
    @NamedQuery(name = "FaMovimientosDetalle.findByCodalm", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.codalm = :codalm"),
    @NamedQuery(name = "FaMovimientosDetalle.findByCospro", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.cospro = :cospro"),
    @NamedQuery(name = "FaMovimientosDetalle.findByCvvf", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.cvvf = :cvvf"),
    @NamedQuery(name = "FaMovimientosDetalle.findByCpvf", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.cpvf = :cpvf"),
    @NamedQuery(name = "FaMovimientosDetalle.findByCprisal2", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.cprisal2 = :cprisal2"),
    @NamedQuery(name = "FaMovimientosDetalle.findByUtipro2", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.utipro2 = :utipro2"),
    @NamedQuery(name = "FaMovimientosDetalle.findByQtpproM", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.qtpproM = :qtpproM"),
    @NamedQuery(name = "FaMovimientosDetalle.findByIgvproC", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.igvproC = :igvproC"),
    @NamedQuery(name = "FaMovimientosDetalle.findByStamodP", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.stamodP = :stamodP"),
    @NamedQuery(name = "FaMovimientosDetalle.findByCodlot", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.codlot = :codlot"),
    @NamedQuery(name = "FaMovimientosDetalle.findByCosproA", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.cosproA = :cosproA"),
    @NamedQuery(name = "FaMovimientosDetalle.findByMovsta", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.movsta = :movsta"),
    @NamedQuery(name = "FaMovimientosDetalle.findByObsmov", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.obsmov = :obsmov"),
    @NamedQuery(name = "FaMovimientosDetalle.findByIgvpar", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.igvpar = :igvpar"),
    @NamedQuery(name = "FaMovimientosDetalle.findByIgvmod", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.igvmod = :igvmod"),
    @NamedQuery(name = "FaMovimientosDetalle.findByIgvmodC", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.igvmodC = :igvmodC"),
    @NamedQuery(name = "FaMovimientosDetalle.findByOrdvalFlag", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.ordvalFlag = :ordvalFlag"),
    @NamedQuery(name = "FaMovimientosDetalle.findByQtyproVal", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.qtyproVal = :qtyproVal"),
    @NamedQuery(name = "FaMovimientosDetalle.findByQtyproMVal", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.qtyproMVal = :qtyproMVal"),
    @NamedQuery(name = "FaMovimientosDetalle.findByFecval", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.fecval = :fecval"),
    @NamedQuery(name = "FaMovimientosDetalle.findByUseval", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.useval = :useval"),
    @NamedQuery(name = "FaMovimientosDetalle.findByPrisalbassal", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.prisalbassal = :prisalbassal"),
    @NamedQuery(name = "FaMovimientosDetalle.findByPrisalbas", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.prisalbas = :prisalbas"),
    @NamedQuery(name = "FaMovimientosDetalle.findByCosbas", query = "SELECT f FROM FaMovimientosDetalle f WHERE f.cosbas = :cosbas")})
public class FaMovimientosDetalle implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "qtypro")
    private int qtypro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qtppro")
    private int qtppro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qtbpro")
    private int qtbpro;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "pvpsal")
    private BigDecimal pvpsal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "vvfsal")
    private BigDecimal vvfsal;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "pvfsal")
    private BigDecimal pvfsal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtopro")
    private BigDecimal dtopro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stkalm")
    private int stkalm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stkalm_m")
    private int stkalmM;
    @Basic(optional = false)
    @Column(name = "parpro")
    private BigDecimal parpro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totpro")
    private BigDecimal totpro;
    @Size(max = 1)
    @Column(name = "covtip")
    private String covtip;
    @Basic(optional = false)
    @NotNull
    @Column(name = "costod")
    private BigDecimal costod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "coscom")
    private BigDecimal coscom;
    @Basic(optional = false)
    @NotNull()
    @Column(name = "prisal")
    private BigDecimal prisal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "stamod")
    private String stamod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qtypro_m")
    private int qtyproM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usecod")
    private int usecod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stkfra")
    private int stkfra;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "codalm")
    private String codalm;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cospro")
    private BigDecimal cospro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cvvf")
    private BigDecimal cvvf;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cpvf")
    private BigDecimal cpvf;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cprisal2")
    private BigDecimal cprisal2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "utipro2")
    private BigDecimal utipro2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qtppro_m")
    private int qtpproM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "igvpro_c")
    private BigDecimal igvproC;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "stamod_p")
    private String stamodP;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "codlot")
    private String codlot;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cospro_a")
    private BigDecimal cosproA;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "movsta")
    private String movsta;
    @Size(max = 60)
    @Column(name = "obsmov")
    private String obsmov;
    @Basic(optional = false)
    @NotNull
    @Column(name = "igvpar")
    private BigDecimal igvpar;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "igvmod")
    private String igvmod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "igvmod_c")
    private String igvmodC;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "ordval_flag")
    private String ordvalFlag;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qtypro_val")
    private int qtyproVal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qtypro_m_val")
    private int qtyproMVal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "prisalbassal")
    private BigDecimal prisalbassal;
    @Basic(optional = false)
    @NotNull()
    @Column(name = "prisalbas")
    private BigDecimal prisalbas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cosbas")
    private BigDecimal cosbas;
    @Size(max = 1)
    @Column(name = "chkean13")
    private String chkean13;
    @Size(max = 1)
    @Column(name = "chkpick")
    private String chkpick;
    @Column(name = "fecipick")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecipick;
    @Column(name = "fecfpick")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecfpick;
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FaMovimientosDetallePK faMovimientosDetallePK;
    @Column(name = "fecven")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecven;
    @Column(name = "igvpro")
    private BigDecimal igvpro;
    @Column(name = "dtopro1")
    private BigDecimal dtopro1;
    @Column(name = "dtopro2")
    private BigDecimal dtopro2;
    @Column(name = "dtopro3")
    private BigDecimal dtopro3;
    @Column(name = "dtopro4")
    private BigDecimal dtopro4;
    @Column(name = "fecval")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecval;
    @Column(name = "useval")
    private Integer useval;
    @JoinColumn(name = "invnum", referencedColumnName = "invnum", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private FaMovimientos faMovimientos;
    @JoinColumn(name = "codpro", referencedColumnName = "codpro")
    @ManyToOne(optional = false)
    private FaProductos codpro;
    @Column(name = "cante")
    private int cante;
    @Column(name = "cantf")
    private int cantf;
    @Column(name = "caja")
    private String caja;

    @Column(name = "usuean13")
    private Integer usuean13;
    @Column(name = "usuean13adm")
    private Integer usuean13adm;
    @Column(name = "usupick")
    private Integer usupick;
    @Column(name = "usupickadm")
    private Integer usupickadm;
    @Column(name = "usudelpick")
    private Integer usudelpick;
    @Column(name = "usudelpickadm")
    private Integer usudelpickadm;
    @Size(max = 1)
    @Column(name = "pickmod")
    
    private String pickmod;

    public Integer getUsuean13() {
        return usuean13;
    }

    public void setUsuean13(Integer usuean13) {
        this.usuean13 = usuean13;
    }

    public Integer getUsuean13adm() {
        return usuean13adm;
    }

    public void setUsuean13adm(Integer usuean13adm) {
        this.usuean13adm = usuean13adm;
    }

    public Integer getUsupick() {
        return usupick;
    }

    public void setUsupick(Integer usupick) {
        this.usupick = usupick;
    }

    public Integer getUsupickadm() {
        return usupickadm;
    }

    public void setUsupickadm(Integer usupickadm) {
        this.usupickadm = usupickadm;
    }

    public Integer getUsudelpick() {
        return usudelpick;
    }

    public void setUsudelpick(Integer usudelpick) {
        this.usudelpick = usudelpick;
    }

    public Integer getUsudelpickadm() {
        return usudelpickadm;
    }

    public void setUsudelpickadm(Integer usudelpickadm) {
        this.usudelpickadm = usudelpickadm;
    }

    public String getPickmod() {
        return pickmod;
    }

    public void setPickmod(String pickmod) {
        this.pickmod = pickmod;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public int getCante() {
        return cante;
    }

    public void setCante(int cante) {
        this.cante = cante;
    }

    public int getCantf() {
        return cantf;
    }

    public void setCantf(int cantf) {
        this.cantf = cantf;
    }

    public FaProductos getCodpro() {
        return codpro;
    }

    public void setCodpro(FaProductos codpro) {
        this.codpro = codpro;
    }

    public FaMovimientosDetalle() {
    }

    public FaMovimientosDetalle(FaMovimientosDetallePK faMovimientosDetallePK) {
        this.faMovimientosDetallePK = faMovimientosDetallePK;
    }

    public FaMovimientosDetalle(FaMovimientosDetallePK faMovimientosDetallePK, int qtypro, int qtppro, int qtbpro, BigDecimal pvpsal, BigDecimal vvfsal, BigDecimal pvfsal, BigDecimal dtopro, int stkalm, int stkalmM, BigDecimal parpro, BigDecimal totpro, BigDecimal costod, BigDecimal coscom, BigDecimal prisal, String stamod, int qtyproM, int usecod, int stkfra, String codalm, BigDecimal cospro, BigDecimal cvvf, BigDecimal cpvf, BigDecimal cprisal2, BigDecimal utipro2, int qtpproM, BigDecimal igvproC, String stamodP, String codlot, BigDecimal cosproA, String movsta, BigDecimal igvpar, String igvmod, String igvmodC, String ordvalFlag, int qtyproVal, int qtyproMVal, BigDecimal prisalbassal, BigDecimal prisalbas, BigDecimal cosbas) {
        this.faMovimientosDetallePK = faMovimientosDetallePK;
        this.qtypro = qtypro;
        this.qtppro = qtppro;
        this.qtbpro = qtbpro;
        this.pvpsal = pvpsal;
        this.vvfsal = vvfsal;
        this.pvfsal = pvfsal;
        this.dtopro = dtopro;
        this.stkalm = stkalm;
        this.stkalmM = stkalmM;
        this.parpro = parpro;
        this.totpro = totpro;
        this.costod = costod;
        this.coscom = coscom;
        this.prisal = prisal;
        this.stamod = stamod;
        this.qtyproM = qtyproM;
        this.usecod = usecod;
        this.stkfra = stkfra;
        this.codalm = codalm;
        this.cospro = cospro;
        this.cvvf = cvvf;
        this.cpvf = cpvf;
        this.cprisal2 = cprisal2;
        this.utipro2 = utipro2;
        this.qtpproM = qtpproM;
        this.igvproC = igvproC;
        this.stamodP = stamodP;
        this.codlot = codlot;
        this.cosproA = cosproA;
        this.movsta = movsta;
        this.igvpar = igvpar;
        this.igvmod = igvmod;
        this.igvmodC = igvmodC;
        this.ordvalFlag = ordvalFlag;
        this.qtyproVal = qtyproVal;
        this.qtyproMVal = qtyproMVal;
        this.prisalbassal = prisalbassal;
        this.prisalbas = prisalbas;
        this.cosbas = cosbas;
    }

    public FaMovimientosDetalle(int invnum, int numitm) {
        this.faMovimientosDetallePK = new FaMovimientosDetallePK(invnum, numitm);
    }

    public FaMovimientosDetallePK getFaMovimientosDetallePK() {
        return faMovimientosDetallePK;
    }

    public void setFaMovimientosDetallePK(FaMovimientosDetallePK faMovimientosDetallePK) {
        this.faMovimientosDetallePK = faMovimientosDetallePK;
    }

    public Date getFecven() {
        return fecven;
    }

    public void setFecven(Date fecven) {
        this.fecven = fecven;
    }

    public int getStkalmM() {
        return stkalmM;
    }

    public void setStkalmM(int stkalmM) {
        this.stkalmM = stkalmM;
    }

    public BigDecimal getIgvpro() {
        return igvpro;
    }

    public void setIgvpro(BigDecimal igvpro) {
        this.igvpro = igvpro;
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

    public BigDecimal getDtopro3() {
        return dtopro3;
    }

    public void setDtopro3(BigDecimal dtopro3) {
        this.dtopro3 = dtopro3;
    }

    public BigDecimal getDtopro4() {
        return dtopro4;
    }

    public void setDtopro4(BigDecimal dtopro4) {
        this.dtopro4 = dtopro4;
    }

    public int getQtyproM() {
        return qtyproM;
    }

    public void setQtyproM(int qtyproM) {
        this.qtyproM = qtyproM;
    }

    public int getQtpproM() {
        return qtpproM;
    }

    public void setQtpproM(int qtpproM) {
        this.qtpproM = qtpproM;
    }

    public BigDecimal getIgvproC() {
        return igvproC;
    }

    public void setIgvproC(BigDecimal igvproC) {
        this.igvproC = igvproC;
    }

    public String getStamodP() {
        return stamodP;
    }

    public void setStamodP(String stamodP) {
        this.stamodP = stamodP;
    }

    public BigDecimal getCosproA() {
        return cosproA;
    }

    public void setCosproA(BigDecimal cosproA) {
        this.cosproA = cosproA;
    }

    public String getIgvmodC() {
        return igvmodC;
    }

    public void setIgvmodC(String igvmodC) {
        this.igvmodC = igvmodC;
    }

    public String getOrdvalFlag() {
        return ordvalFlag;
    }

    public void setOrdvalFlag(String ordvalFlag) {
        this.ordvalFlag = ordvalFlag;
    }

    public int getQtyproVal() {
        return qtyproVal;
    }

    public void setQtyproVal(int qtyproVal) {
        this.qtyproVal = qtyproVal;
    }

    public int getQtyproMVal() {
        return qtyproMVal;
    }

    public void setQtyproMVal(int qtyproMVal) {
        this.qtyproMVal = qtyproMVal;
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

    public FaMovimientos getFaMovimientos() {
        return faMovimientos;
    }

    public void setFaMovimientos(FaMovimientos faMovimientos) {
        this.faMovimientos = faMovimientos;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (faMovimientosDetallePK != null ? faMovimientosDetallePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaMovimientosDetalle)) {
            return false;
        }
        FaMovimientosDetalle other = (FaMovimientosDetalle) object;
        if ((this.faMovimientosDetallePK == null && other.faMovimientosDetallePK != null) || (this.faMovimientosDetallePK != null && !this.faMovimientosDetallePK.equals(other.faMovimientosDetallePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaMovimientosDetalle[ faMovimientosDetallePK=" + faMovimientosDetallePK + " ]";
    }

    public int getQtypro() {
        return qtypro;
    }

    public void setQtypro(int qtypro) {
        this.qtypro = qtypro;
    }

    public int getQtppro() {
        return qtppro;
    }

    public void setQtppro(int qtppro) {
        this.qtppro = qtppro;
    }

    public int getQtbpro() {
        return qtbpro;
    }

    public void setQtbpro(int qtbpro) {
        this.qtbpro = qtbpro;
    }

    public BigDecimal getPvpsal() {
        return pvpsal;
    }

    public void setPvpsal(BigDecimal pvpsal) {
        this.pvpsal = pvpsal;
    }

    public BigDecimal getVvfsal() {
        return vvfsal;
    }

    public void setVvfsal(BigDecimal vvfsal) {
        this.vvfsal = vvfsal;
    }

    public BigDecimal getPvfsal() {
        return pvfsal;
    }

    public void setPvfsal(BigDecimal pvfsal) {
        this.pvfsal = pvfsal;
    }

    public BigDecimal getDtopro() {
        return dtopro;
    }

    public void setDtopro(BigDecimal dtopro) {
        this.dtopro = dtopro;
    }

    public int getStkalm() {
        return stkalm;
    }

    public void setStkalm(int stkalm) {
        this.stkalm = stkalm;
    }

    public BigDecimal getParpro() {
        return parpro;
    }

    public void setParpro(BigDecimal parpro) {
        this.parpro = parpro;
    }

    public BigDecimal getTotpro() {
        return totpro;
    }

    public void setTotpro(BigDecimal totpro) {
        this.totpro = totpro;
    }

    public String getCovtip() {
        return covtip;
    }

    public void setCovtip(String covtip) {
        this.covtip = covtip;
    }

    public BigDecimal getCostod() {
        return costod;
    }

    public void setCostod(BigDecimal costod) {
        this.costod = costod;
    }

    public BigDecimal getCoscom() {
        return coscom;
    }

    public void setCoscom(BigDecimal coscom) {
        this.coscom = coscom;
    }

    public BigDecimal getPrisal() {
        return prisal;
    }

    public void setPrisal(BigDecimal prisal) {
        this.prisal = prisal;
    }

    public String getStamod() {
        return stamod;
    }

    public void setStamod(String stamod) {
        this.stamod = stamod;
    }

    public int getUsecod() {
        return usecod;
    }

    public void setUsecod(int usecod) {
        this.usecod = usecod;
    }

    public int getStkfra() {
        return stkfra;
    }

    public void setStkfra(int stkfra) {
        this.stkfra = stkfra;
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
    }

    public BigDecimal getCospro() {
        return cospro;
    }

    public void setCospro(BigDecimal cospro) {
        this.cospro = cospro;
    }

    public BigDecimal getCvvf() {
        return cvvf;
    }

    public void setCvvf(BigDecimal cvvf) {
        this.cvvf = cvvf;
    }

    public BigDecimal getCpvf() {
        return cpvf;
    }

    public void setCpvf(BigDecimal cpvf) {
        this.cpvf = cpvf;
    }

    public BigDecimal getCprisal2() {
        return cprisal2;
    }

    public void setCprisal2(BigDecimal cprisal2) {
        this.cprisal2 = cprisal2;
    }

    public BigDecimal getUtipro2() {
        return utipro2;
    }

    public void setUtipro2(BigDecimal utipro2) {
        this.utipro2 = utipro2;
    }

    public String getCodlot() {
        return codlot;
    }

    public void setCodlot(String codlot) {
        this.codlot = codlot;
    }

    public String getMovsta() {
        return movsta;
    }

    public void setMovsta(String movsta) {
        this.movsta = movsta;
    }

    public String getObsmov() {
        return obsmov;
    }

    public void setObsmov(String obsmov) {
        this.obsmov = obsmov;
    }

    public BigDecimal getIgvpar() {
        return igvpar;
    }

    public void setIgvpar(BigDecimal igvpar) {
        this.igvpar = igvpar;
    }

    public String getIgvmod() {
        return igvmod;
    }

    public void setIgvmod(String igvmod) {
        this.igvmod = igvmod;
    }

    public BigDecimal getPrisalbassal() {
        return prisalbassal;
    }

    public void setPrisalbassal(BigDecimal prisalbassal) {
        this.prisalbassal = prisalbassal;
    }

    public BigDecimal getPrisalbas() {
        return prisalbas;
    }

    public void setPrisalbas(BigDecimal prisalbas) {
        this.prisalbas = prisalbas;
    }

    public BigDecimal getCosbas() {
        return cosbas;
    }

    public void setCosbas(BigDecimal cosbas) {
        this.cosbas = cosbas;
    }

    public String getChkean13() {
        return chkean13;
    }

    public void setChkean13(String chkean13) {
        this.chkean13 = chkean13;
    }

    public String getChkpick() {
        return chkpick;
    }

    public void setChkpick(String chkpick) {
        this.chkpick = chkpick;
    }

    public Date getFecipick() {
        return fecipick;
    }

    public void setFecipick(Date fecipick) {
        this.fecipick = fecipick;
    }

    public Date getFecfpick() {
        return fecfpick;
    }

    public void setFecfpick(Date fecfpick) {
        this.fecfpick = fecfpick;
    }

}
