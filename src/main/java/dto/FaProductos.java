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
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.OneToMany;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
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
@Table(name = "fa_productos")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "FaProductos.findAll", query = "SELECT f FROM FaProductos f"),
        @NamedQuery(name = "FaProductos.findByCodpro", query = "SELECT f FROM FaProductos f WHERE f.codpro = :codpro"),
        @NamedQuery(name = "FaProductos.ListaSoloProducto", query = "SELECT f.codpro,f.despro FROM FaProductos f WHERE f.codlab = :codlab and f.codgen = :codgen and f.codfam = :codfam and f.codtip=:codtip"),
        @NamedQuery(name = "FaProductos.findByCodlab", query = "SELECT f.codpro, f.despro FROM FaProductos f WHERE f.codlab = :codlab and f.estado = 'S'"),
        @NamedQuery(name = "FaProductos.findByDespro", query = "SELECT f FROM FaProductos f WHERE f.despro = :despro"),
        @NamedQuery(name = "FaProductos.findByCodgen", query = "SELECT f FROM FaProductos f WHERE f.codgen = :codgen"),
        @NamedQuery(name = "FaProductos.findByCodfam", query = "SELECT f FROM FaProductos f WHERE f.codfam = :codfam"),
        @NamedQuery(name = "FaProductos.findByPrisal", query = "SELECT f FROM FaProductos f WHERE f.prisal = :prisal"),
        @NamedQuery(name = "FaProductos.findByCostod", query = "SELECT f FROM FaProductos f WHERE f.costod = :costod"),
        @NamedQuery(name = "FaProductos.findByCospro", query = "SELECT f FROM FaProductos f WHERE f.cospro = :cospro"),
        @NamedQuery(name = "FaProductos.findByDatmov", query = "SELECT f FROM FaProductos f WHERE f.datmov = :datmov"),
        @NamedQuery(name = "FaProductos.findByDatpri", query = "SELECT f FROM FaProductos f WHERE f.datpri = :datpri"),
        @NamedQuery(name = "FaProductos.findByDatinc", query = "SELECT f FROM FaProductos f WHERE f.datinc = :datinc"),
        @NamedQuery(name = "FaProductos.findByDatven", query = "SELECT f FROM FaProductos f WHERE f.datven = :datven"),
        @NamedQuery(name = "FaProductos.findByProsta", query = "SELECT f FROM FaProductos f WHERE f.prosta = :prosta"),
        @NamedQuery(name = "FaProductos.findByDatind", query = "SELECT f FROM FaProductos f WHERE f.datind = :datind"),
        @NamedQuery(name = "FaProductos.findByDatfid", query = "SELECT f FROM FaProductos f WHERE f.datfid = :datfid"),
        @NamedQuery(name = "FaProductos.findByCodbar", query = "SELECT f FROM FaProductos f WHERE f.codbar = :codbar"),
        @NamedQuery(name = "FaProductos.findByCostodR", query = "SELECT f FROM FaProductos f WHERE f.costodR = :costodR"),
        @NamedQuery(name = "FaProductos.findByStkfra", query = "SELECT f FROM FaProductos f WHERE f.stkfra = :stkfra"),
        @NamedQuery(name = "FaProductos.findByIgvpro", query = "SELECT f FROM FaProductos f WHERE f.igvpro = :igvpro"),
        @NamedQuery(name = "FaProductos.findByUtipro", query = "SELECT f FROM FaProductos f WHERE f.utipro = :utipro"),
        @NamedQuery(name = "FaProductos.findByCoscom", query = "SELECT f FROM FaProductos f WHERE f.coscom = :coscom"),
        @NamedQuery(name = "FaProductos.findByCodproA", query = "SELECT f FROM FaProductos f WHERE f.codproA = :codproA"),
        @NamedQuery(name = "FaProductos.findByRecmen", query = "SELECT f FROM FaProductos f WHERE f.recmen = :recmen"),
        @NamedQuery(name = "FaProductos.findByDesproL", query = "SELECT f FROM FaProductos f WHERE f.desproL = :desproL"),
        @NamedQuery(name = "FaProductos.findByModfar", query = "SELECT f FROM FaProductos f WHERE f.modfar = :modfar"),
        @NamedQuery(name = "FaProductos.findByPromoSta", query = "SELECT f FROM FaProductos f WHERE f.promoSta = :promoSta"),
        @NamedQuery(name = "FaProductos.findByPromoCan", query = "SELECT f FROM FaProductos f WHERE f.promoCan = :promoCan"),
        @NamedQuery(name = "FaProductos.findByPromoDto", query = "SELECT f FROM FaProductos f WHERE f.promoDto = :promoDto"),
        @NamedQuery(name = "FaProductos.findByEstado", query = "SELECT f FROM FaProductos f WHERE f.estado = :estado"),
        @NamedQuery(name = "FaProductos.findByFeccre", query = "SELECT f FROM FaProductos f WHERE f.feccre = :feccre"),
        @NamedQuery(name = "FaProductos.findByFecumv", query = "SELECT f FROM FaProductos f WHERE f.fecumv = :fecumv"),
        @NamedQuery(name = "FaProductos.findByUsecod", query = "SELECT f FROM FaProductos f WHERE f.usecod = :usecod"),
        @NamedQuery(name = "FaProductos.findByUsenam", query = "SELECT f FROM FaProductos f WHERE f.usenam = :usenam"),
        @NamedQuery(name = "FaProductos.findByHostname", query = "SELECT f FROM FaProductos f WHERE f.hostname = :hostname"),
        @NamedQuery(name = "FaProductos.findByDtoproA", query = "SELECT f FROM FaProductos f WHERE f.dtoproA = :dtoproA"),
        @NamedQuery(name = "FaProductos.findByCanven", query = "SELECT f FROM FaProductos f WHERE f.canven = :canven"),
        @NamedQuery(name = "FaProductos.findByCompro", query = "SELECT f FROM FaProductos f WHERE f.compro = :compro"),
        @NamedQuery(name = "FaProductos.findByComproP", query = "SELECT f FROM FaProductos f WHERE f.comproP = :comproP"),
        @NamedQuery(name = "FaProductos.findByTcompro", query = "SELECT f FROM FaProductos f WHERE f.tcompro = :tcompro"),
        @NamedQuery(name = "FaProductos.findByPuntos", query = "SELECT f FROM FaProductos f WHERE f.puntos = :puntos"),
        @NamedQuery(name = "FaProductos.findByIgvproC", query = "SELECT f FROM FaProductos f WHERE f.igvproC = :igvproC"),
        @NamedQuery(name = "FaProductos.findByCodproS", query = "SELECT f FROM FaProductos f WHERE f.codproS = :codproS"),
        @NamedQuery(name = "FaProductos.findByCodclass", query = "SELECT f FROM FaProductos f WHERE f.codclass = :codclass"),
        @NamedQuery(name = "FaProductos.findByStacontr", query = "SELECT f FROM FaProductos f WHERE f.stacontr = :stacontr"),
        @NamedQuery(name = "FaProductos.findByCondcod", query = "SELECT f FROM FaProductos f WHERE f.condcod = :condcod"),
        @NamedQuery(name = "FaProductos.findByStains", query = "SELECT f FROM FaProductos f WHERE f.stains = :stains"),
        @NamedQuery(name = "FaProductos.findByTippro", query = "SELECT f FROM FaProductos f WHERE f.tippro = :tippro"),
        @NamedQuery(name = "FaProductos.findByTiprep", query = "SELECT f FROM FaProductos f WHERE f.tiprep = :tiprep"),
        @NamedQuery(name = "FaProductos.findByStanarcot", query = "SELECT f FROM FaProductos f WHERE f.stanarcot = :stanarcot"),
        @NamedQuery(name = "FaProductos.findByIgvmod", query = "SELECT f FROM FaProductos f WHERE f.igvmod = :igvmod"),
        @NamedQuery(name = "FaProductos.findByIgvmodC", query = "SELECT f FROM FaProductos f WHERE f.igvmodC = :igvmodC"),
        @NamedQuery(name = "FaProductos.findByUbidispro", query = "SELECT f FROM FaProductos f WHERE f.ubidispro = :ubidispro"),
        @NamedQuery(name = "FaProductos.findByStaprdcanj", query = "SELECT f FROM FaProductos f WHERE f.staprdcanj = :staprdcanj"),
        @NamedQuery(name = "FaProductos.findByPtosdtocanj", query = "SELECT f FROM FaProductos f WHERE f.ptosdtocanj = :ptosdtocanj"),
        @NamedQuery(name = "FaProductos.findByCodproDigemid", query = "SELECT f FROM FaProductos f WHERE f.codproDigemid = :codproDigemid"),
        @NamedQuery(name = "FaProductos.findByEstadoDigemid", query = "SELECT f FROM FaProductos f WHERE f.estadoDigemid = :estadoDigemid"),
        @NamedQuery(name = "FaProductos.findByFecumvDigemid", query = "SELECT f FROM FaProductos f WHERE f.fecumvDigemid = :fecumvDigemid"),
        @NamedQuery(name = "FaProductos.findByTipoprodDigemid", query = "SELECT f FROM FaProductos f WHERE f.tipoprodDigemid = :tipoprodDigemid"),
        @NamedQuery(name = "FaProductos.findByCodcovidDigemid", query = "SELECT f FROM FaProductos f WHERE f.codcovidDigemid = :codcovidDigemid"),
        @NamedQuery(name = "FaProductos.findByEstadocovidDigemid", query = "SELECT f FROM FaProductos f WHERE f.estadocovidDigemid = :estadocovidDigemid"),
        @NamedQuery(name = "FaProductos.findByCategvta", query = "SELECT f FROM FaProductos f WHERE f.categvta = :categvta"),
        @NamedQuery(name = "FaProductos.findByStaimptobol", query = "SELECT f FROM FaProductos f WHERE f.staimptobol = :staimptobol"),
        @NamedQuery(name = "FaProductos.findByImptobol", query = "SELECT f FROM FaProductos f WHERE f.imptobol = :imptobol"),
        @NamedQuery(name = "FaProductos.findByCodimgqull", query = "SELECT f FROM FaProductos f WHERE f.codimgqull = :codimgqull"),
        @NamedQuery(name = "FaProductos.findByStareqrefrig", query = "SELECT f FROM FaProductos f WHERE f.stareqrefrig = :stareqrefrig"),
        @NamedQuery(name = "FaProductos.findByRegsanit", query = "SELECT f FROM FaProductos f WHERE f.regsanit = :regsanit"),
        @NamedQuery(name = "FaProductos.findByRegsanitFvenc", query = "SELECT f FROM FaProductos f WHERE f.regsanitFvenc = :regsanitFvenc"),
        @NamedQuery(name = "FaProductos.findByPlazoentr", query = "SELECT f FROM FaProductos f WHERE f.plazoentr = :plazoentr"),
        @NamedQuery(name = "FaProductos.findByPrisalbas", query = "SELECT f FROM FaProductos f WHERE f.prisalbas = :prisalbas"),
        @NamedQuery(name = "FaProductos.findByCosbas", query = "SELECT f FROM FaProductos f WHERE f.cosbas = :cosbas"),
        @NamedQuery(name = "FaProductos.findByStadispvta", query = "SELECT f FROM FaProductos f WHERE f.stadispvta = :stadispvta"),
        @NamedQuery(name = "FaProductos.findByDesproLAnt", query = "SELECT f FROM FaProductos f WHERE f.desproLAnt = :desproLAnt"),
        @NamedQuery(name = "FaProductos.findByDiasvigins", query = "SELECT f FROM FaProductos f WHERE f.diasvigins = :diasvigins"),
        @NamedQuery(name = "FaProductos.findByFactorins", query = "SELECT f FROM FaProductos f WHERE f.factorins = :factorins"),
        @NamedQuery(name = "FaProductos.findByQtyminvtains", query = "SELECT f FROM FaProductos f WHERE f.qtyminvtains = :qtyminvtains"),
        @NamedQuery(name = "FaProductos.findByQtymaxconcent", query = "SELECT f FROM FaProductos f WHERE f.qtymaxconcent = :qtymaxconcent"),
        @NamedQuery(name = "FaProductos.findByCodtri", query = "SELECT f FROM FaProductos f WHERE f.codtri = :codtri"),
        @NamedQuery(name = "FaProductos.findByTipptosprd", query = "SELECT f FROM FaProductos f WHERE f.tipptosprd = :tipptosprd"),
        @NamedQuery(name = "FaProductos.findByMontobasptos", query = "SELECT f FROM FaProductos f WHERE f.montobasptos = :montobasptos"),
        @NamedQuery(name = "FaProductos.findByEqvptos", query = "SELECT f FROM FaProductos f WHERE f.eqvptos = :eqvptos") })
@NamedStoredProcedureQuery(name = "sel_productos_distribuir_almacencentral_parametros", procedureName = "sel_productos_distribuir_almacencentral_parametros", parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "tipo_stkmin", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "tipo_distrib", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "codtip", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "secuencia", type = Integer.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "indica_fecha", type = Character.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "fecha1", type = Date.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "fecha2", type = Date.class),
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "fecha3", type = Date.class)
})
public class FaProductos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "codpro")
    private String codpro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "despro")
    private String despro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "codtip")
    private String codtip;
    @NotNull
    @Basic(optional = false)
    @Size(min = 1, max = 4)
    @Column(name = "codlab")
    private String codlab;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "codgen")
    private String codgen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "codfam")
    private String codfam;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "prisal")
    private BigDecimal prisal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "costod")
    private BigDecimal costod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cospro")
    private BigDecimal cospro;
    @Column(name = "datmov")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datmov;
    @Column(name = "datpri")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datpri;
    @Column(name = "datinc")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datinc;
    @Column(name = "datven")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datven;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "prosta")
    private String prosta;
    @Column(name = "datind")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datind;
    @Column(name = "datfid")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datfid;
    @Size(max = 16)
    @Column(name = "codbar")
    private String codbar;
    @Column(name = "costod_r")
    private BigDecimal costodR;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stkfra")
    private int stkfra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "igvpro")
    private BigDecimal igvpro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "utipro")
    private BigDecimal utipro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "coscom")
    private BigDecimal coscom;
    @Size(max = 16)
    @Column(name = "codpro_a")
    private String codproA;
    @Column(name = "recmen")
    private BigDecimal recmen;
    @Size(max = 120)
    @Column(name = "despro_l")
    private String desproL;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "modfar")
    private String modfar;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "promo_sta")
    private String promoSta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "promo_can")
    private BigDecimal promoCan;
    @Basic(optional = false)
    @NotNull
    @Column(name = "promo_dto")
    private BigDecimal promoDto;
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
    @Column(name = "dtopro_a")
    private BigDecimal dtoproA;
    @Basic(optional = false)
    @NotNull
    @Column(name = "canven")
    private int canven;
    @Basic(optional = false)
    @NotNull
    @Column(name = "compro")
    private BigDecimal compro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "compro_p")
    private BigDecimal comproP;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "tcompro")
    private String tcompro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "puntos")
    private BigDecimal puntos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "igvpro_c")
    private BigDecimal igvproC;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "codpro_s")
    private String codproS;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "codclass")
    private String codclass;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "stacontr")
    private String stacontr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "condcod")
    private String condcod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "stains")
    private String stains;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "tippro")
    private String tippro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "tiprep")
    private String tiprep;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "stanarcot")
    private String stanarcot;
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
    @Size(max = 8)
    @Column(name = "ubidispro")
    private String ubidispro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "staprdcanj")
    private String staprdcanj;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ptosdtocanj")
    private BigDecimal ptosdtocanj;
    @Column(name = "codpro_digemid")
    private Integer codproDigemid;
    @Column(name = "estado_digemid")
    private Character estadoDigemid;
    @Column(name = "fecumv_digemid")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecumvDigemid;
    @Size(max = 2)
    @Column(name = "tipoprod_digemid")
    private String tipoprodDigemid;
    @Column(name = "codcovid_digemid")
    private Integer codcovidDigemid;
    @Column(name = "estadocovid_digemid")
    private Character estadocovidDigemid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "categvta")
    private String categvta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "staimptobol")
    private String staimptobol;
    @Basic(optional = false)
    @NotNull
    @Column(name = "imptobol")
    private BigDecimal imptobol;
    @Size(max = 20)
    @Column(name = "codimgqull")
    private String codimgqull;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "stareqrefrig")
    private String stareqrefrig;
    @Size(max = 30)
    @Column(name = "regsanit")
    private String regsanit;
    @Column(name = "regsanit_fvenc")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regsanitFvenc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "plazoentr")
    private int plazoentr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "prisalbas")
    private BigDecimal prisalbas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cosbas")
    private BigDecimal cosbas;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "stadispvta")
    private String stadispvta;
    @Size(max = 120)
    @Column(name = "despro_l_ant")
    private String desproLAnt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "diasvigins")
    private int diasvigins;
    @Basic(optional = false)
    @NotNull
    @Column(name = "factorins")
    private BigDecimal factorins;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qtyminvtains")
    private int qtyminvtains;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qtymaxconcent")
    private int qtymaxconcent;
    @Size(max = 20)
    @Column(name = "codtri")
    private String codtri;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "tipptosprd")
    private String tipptosprd;
    @Basic(optional = false)
    @NotNull
    @Column(name = "montobasptos")
    private BigDecimal montobasptos;
    @Basic(optional = false)
    @NotNull
    @Column(name = "eqvptos")
    private BigDecimal eqvptos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "codpro")
    private List<FaMovimientosDetalle> faMovimientosDetalleList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "faProductos")
    private List<FaStockAlmacenes> faStockAlmacenesList;
    @Column(name = "aplicfrac")
    @Size(max = 1)
    private String aplicfrac;
    @Column(name = "blister")
    private Integer blister;
    @Column(name = "masterpack")
    private Integer masterpack;
    @Column(name = "aplicmastpack")
    @Size(max = 1)
    private String aplicmastpack;
    @Column(name = "predesac")
    @Size(max = 1)
    private String predesac;

    public String getPredesac() {
        return predesac;
    }

    public void setPredesac(String predesac) {
        this.predesac = predesac;
    }

    public Integer getBlister() {
        return blister;
    }

    public String getAplicmastpack() {
        return aplicmastpack;
    }

    public void setAplicmastpack(String aplicmastpack) {
        this.aplicmastpack = aplicmastpack;
    }

    public void setBlister(Integer blister) {
        this.blister = blister;
    }

    public Integer getMasterpack() {
        return masterpack;
    }

    public void setMasterpack(Integer masterpack) {
        this.masterpack = masterpack;
    }

    public FaProductos() {
    }

    public FaProductos(String codpro) {
        this.codpro = codpro;
    }

    public String getCodtip() {
        return codtip;
    }

    public void setCodtip(String codtip) {
        this.codtip = codtip;
    }

    public String getCodlab() {
        return codlab;
    }

    public void setCodlab(String codlab) {
        this.codlab = codlab;
    }

    public String getAplicfrac() {
        return aplicfrac;
    }

    public void setAplicfrac(String aplicfrac) {
        this.aplicfrac = aplicfrac;
    }

    public FaProductos(String codpro, String despro, String codgen, String codfam, BigDecimal prisal, BigDecimal costod,
            BigDecimal cospro, String prosta, int stkfra, BigDecimal igvpro, BigDecimal utipro, BigDecimal coscom,
            String modfar, String promoSta, BigDecimal promoCan, BigDecimal promoDto, String estado, Date feccre,
            Date fecumv, int usecod, String usenam, String hostname, BigDecimal dtoproA, int canven, BigDecimal compro,
            BigDecimal comproP, String tcompro, BigDecimal puntos, BigDecimal igvproC, String codproS, String codclass,
            String stacontr, String condcod, String stains, String tippro, String tiprep, String stanarcot,
            String igvmod, String igvmodC, String staprdcanj, BigDecimal ptosdtocanj, String categvta,
            String staimptobol, BigDecimal imptobol, String stareqrefrig, int plazoentr, BigDecimal prisalbas,
            BigDecimal cosbas, String stadispvta, int diasvigins, BigDecimal factorins, int qtyminvtains,
            int qtymaxconcent, String tipptosprd, BigDecimal montobasptos, BigDecimal eqvptos) {
        this.codpro = codpro;
        this.despro = despro;
        this.codgen = codgen;
        this.codfam = codfam;
        this.prisal = prisal;
        this.costod = costod;
        this.cospro = cospro;
        this.prosta = prosta;
        this.stkfra = stkfra;
        this.igvpro = igvpro;
        this.utipro = utipro;
        this.coscom = coscom;
        this.modfar = modfar;
        this.promoSta = promoSta;
        this.promoCan = promoCan;
        this.promoDto = promoDto;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
        this.dtoproA = dtoproA;
        this.canven = canven;
        this.compro = compro;
        this.comproP = comproP;
        this.tcompro = tcompro;
        this.puntos = puntos;
        this.igvproC = igvproC;
        this.codproS = codproS;
        this.codclass = codclass;
        this.stacontr = stacontr;
        this.condcod = condcod;
        this.stains = stains;
        this.tippro = tippro;
        this.tiprep = tiprep;
        this.stanarcot = stanarcot;
        this.igvmod = igvmod;
        this.igvmodC = igvmodC;
        this.staprdcanj = staprdcanj;
        this.ptosdtocanj = ptosdtocanj;
        this.categvta = categvta;
        this.staimptobol = staimptobol;
        this.imptobol = imptobol;
        this.stareqrefrig = stareqrefrig;
        this.plazoentr = plazoentr;
        this.prisalbas = prisalbas;
        this.cosbas = cosbas;
        this.stadispvta = stadispvta;
        this.diasvigins = diasvigins;
        this.factorins = factorins;
        this.qtyminvtains = qtyminvtains;
        this.qtymaxconcent = qtymaxconcent;
        this.tipptosprd = tipptosprd;
        this.montobasptos = montobasptos;
        this.eqvptos = eqvptos;
    }

    public String getCodpro() {
        return codpro;
    }

    public void setCodpro(String codpro) {
        this.codpro = codpro;
    }

    public String getDespro() {
        return despro;
    }

    public void setDespro(String despro) {
        this.despro = despro;
    }

    public String getCodgen() {
        return codgen;
    }

    public void setCodgen(String codgen) {
        this.codgen = codgen;
    }

    public String getCodfam() {
        return codfam;
    }

    public void setCodfam(String codfam) {
        this.codfam = codfam;
    }

    public BigDecimal getPrisal() {
        return prisal;
    }

    public void setPrisal(BigDecimal prisal) {
        this.prisal = prisal;
    }

    public BigDecimal getCostod() {
        return costod;
    }

    public void setCostod(BigDecimal costod) {
        this.costod = costod;
    }

    public BigDecimal getCospro() {
        return cospro;
    }

    public void setCospro(BigDecimal cospro) {
        this.cospro = cospro;
    }

    public Date getDatmov() {
        return datmov;
    }

    public void setDatmov(Date datmov) {
        this.datmov = datmov;
    }

    public Date getDatpri() {
        return datpri;
    }

    public void setDatpri(Date datpri) {
        this.datpri = datpri;
    }

    public Date getDatinc() {
        return datinc;
    }

    public void setDatinc(Date datinc) {
        this.datinc = datinc;
    }

    public Date getDatven() {
        return datven;
    }

    public void setDatven(Date datven) {
        this.datven = datven;
    }

    public String getProsta() {
        return prosta;
    }

    public void setProsta(String prosta) {
        this.prosta = prosta;
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

    public String getCodbar() {
        return codbar;
    }

    public void setCodbar(String codbar) {
        this.codbar = codbar;
    }

    public BigDecimal getCostodR() {
        return costodR;
    }

    public void setCostodR(BigDecimal costodR) {
        this.costodR = costodR;
    }

    public int getStkfra() {
        return stkfra;
    }

    public void setStkfra(int stkfra) {
        this.stkfra = stkfra;
    }

    public BigDecimal getIgvpro() {
        return igvpro;
    }

    public void setIgvpro(BigDecimal igvpro) {
        this.igvpro = igvpro;
    }

    public BigDecimal getUtipro() {
        return utipro;
    }

    public void setUtipro(BigDecimal utipro) {
        this.utipro = utipro;
    }

    public BigDecimal getCoscom() {
        return coscom;
    }

    public void setCoscom(BigDecimal coscom) {
        this.coscom = coscom;
    }

    public String getCodproA() {
        return codproA;
    }

    public void setCodproA(String codproA) {
        this.codproA = codproA;
    }

    public BigDecimal getRecmen() {
        return recmen;
    }

    public void setRecmen(BigDecimal recmen) {
        this.recmen = recmen;
    }

    public String getDesproL() {
        return desproL;
    }

    public void setDesproL(String desproL) {
        this.desproL = desproL;
    }

    public String getModfar() {
        return modfar;
    }

    public void setModfar(String modfar) {
        this.modfar = modfar;
    }

    public String getPromoSta() {
        return promoSta;
    }

    public void setPromoSta(String promoSta) {
        this.promoSta = promoSta;
    }

    public BigDecimal getPromoCan() {
        return promoCan;
    }

    public void setPromoCan(BigDecimal promoCan) {
        this.promoCan = promoCan;
    }

    public BigDecimal getPromoDto() {
        return promoDto;
    }

    public void setPromoDto(BigDecimal promoDto) {
        this.promoDto = promoDto;
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

    public BigDecimal getDtoproA() {
        return dtoproA;
    }

    public void setDtoproA(BigDecimal dtoproA) {
        this.dtoproA = dtoproA;
    }

    public int getCanven() {
        return canven;
    }

    public void setCanven(int canven) {
        this.canven = canven;
    }

    public BigDecimal getCompro() {
        return compro;
    }

    public void setCompro(BigDecimal compro) {
        this.compro = compro;
    }

    public BigDecimal getComproP() {
        return comproP;
    }

    public void setComproP(BigDecimal comproP) {
        this.comproP = comproP;
    }

    public String getTcompro() {
        return tcompro;
    }

    public void setTcompro(String tcompro) {
        this.tcompro = tcompro;
    }

    public BigDecimal getPuntos() {
        return puntos;
    }

    public void setPuntos(BigDecimal puntos) {
        this.puntos = puntos;
    }

    public BigDecimal getIgvproC() {
        return igvproC;
    }

    public void setIgvproC(BigDecimal igvproC) {
        this.igvproC = igvproC;
    }

    public String getCodproS() {
        return codproS;
    }

    public void setCodproS(String codproS) {
        this.codproS = codproS;
    }

    public String getCodclass() {
        return codclass;
    }

    public void setCodclass(String codclass) {
        this.codclass = codclass;
    }

    public String getStacontr() {
        return stacontr;
    }

    public void setStacontr(String stacontr) {
        this.stacontr = stacontr;
    }

    public String getCondcod() {
        return condcod;
    }

    public void setCondcod(String condcod) {
        this.condcod = condcod;
    }

    public String getStains() {
        return stains;
    }

    public void setStains(String stains) {
        this.stains = stains;
    }

    public String getTippro() {
        return tippro;
    }

    public void setTippro(String tippro) {
        this.tippro = tippro;
    }

    public String getTiprep() {
        return tiprep;
    }

    public void setTiprep(String tiprep) {
        this.tiprep = tiprep;
    }

    public String getStanarcot() {
        return stanarcot;
    }

    public void setStanarcot(String stanarcot) {
        this.stanarcot = stanarcot;
    }

    public String getIgvmod() {
        return igvmod;
    }

    public void setIgvmod(String igvmod) {
        this.igvmod = igvmod;
    }

    public String getIgvmodC() {
        return igvmodC;
    }

    public void setIgvmodC(String igvmodC) {
        this.igvmodC = igvmodC;
    }

    public String getUbidispro() {
        return ubidispro;
    }

    public void setUbidispro(String ubidispro) {
        this.ubidispro = ubidispro;
    }

    public String getStaprdcanj() {
        return staprdcanj;
    }

    public void setStaprdcanj(String staprdcanj) {
        this.staprdcanj = staprdcanj;
    }

    public BigDecimal getPtosdtocanj() {
        return ptosdtocanj;
    }

    public void setPtosdtocanj(BigDecimal ptosdtocanj) {
        this.ptosdtocanj = ptosdtocanj;
    }

    public Integer getCodproDigemid() {
        return codproDigemid;
    }

    public void setCodproDigemid(Integer codproDigemid) {
        this.codproDigemid = codproDigemid;
    }

    public Character getEstadoDigemid() {
        return estadoDigemid;
    }

    public void setEstadoDigemid(Character estadoDigemid) {
        this.estadoDigemid = estadoDigemid;
    }

    public Date getFecumvDigemid() {
        return fecumvDigemid;
    }

    public void setFecumvDigemid(Date fecumvDigemid) {
        this.fecumvDigemid = fecumvDigemid;
    }

    public String getTipoprodDigemid() {
        return tipoprodDigemid;
    }

    public void setTipoprodDigemid(String tipoprodDigemid) {
        this.tipoprodDigemid = tipoprodDigemid;
    }

    public Integer getCodcovidDigemid() {
        return codcovidDigemid;
    }

    public void setCodcovidDigemid(Integer codcovidDigemid) {
        this.codcovidDigemid = codcovidDigemid;
    }

    public Character getEstadocovidDigemid() {
        return estadocovidDigemid;
    }

    public void setEstadocovidDigemid(Character estadocovidDigemid) {
        this.estadocovidDigemid = estadocovidDigemid;
    }

    public String getCategvta() {
        return categvta;
    }

    public void setCategvta(String categvta) {
        this.categvta = categvta;
    }

    public String getStaimptobol() {
        return staimptobol;
    }

    public void setStaimptobol(String staimptobol) {
        this.staimptobol = staimptobol;
    }

    public BigDecimal getImptobol() {
        return imptobol;
    }

    public void setImptobol(BigDecimal imptobol) {
        this.imptobol = imptobol;
    }

    public String getCodimgqull() {
        return codimgqull;
    }

    public void setCodimgqull(String codimgqull) {
        this.codimgqull = codimgqull;
    }

    public String getStareqrefrig() {
        return stareqrefrig;
    }

    public void setStareqrefrig(String stareqrefrig) {
        this.stareqrefrig = stareqrefrig;
    }

    public String getRegsanit() {
        return regsanit;
    }

    public void setRegsanit(String regsanit) {
        this.regsanit = regsanit;
    }

    public Date getRegsanitFvenc() {
        return regsanitFvenc;
    }

    public void setRegsanitFvenc(Date regsanitFvenc) {
        this.regsanitFvenc = regsanitFvenc;
    }

    public int getPlazoentr() {
        return plazoentr;
    }

    public void setPlazoentr(int plazoentr) {
        this.plazoentr = plazoentr;
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

    public String getStadispvta() {
        return stadispvta;
    }

    public void setStadispvta(String stadispvta) {
        this.stadispvta = stadispvta;
    }

    public String getDesproLAnt() {
        return desproLAnt;
    }

    public void setDesproLAnt(String desproLAnt) {
        this.desproLAnt = desproLAnt;
    }

    public int getDiasvigins() {
        return diasvigins;
    }

    public void setDiasvigins(int diasvigins) {
        this.diasvigins = diasvigins;
    }

    public BigDecimal getFactorins() {
        return factorins;
    }

    public void setFactorins(BigDecimal factorins) {
        this.factorins = factorins;
    }

    public int getQtyminvtains() {
        return qtyminvtains;
    }

    public void setQtyminvtains(int qtyminvtains) {
        this.qtyminvtains = qtyminvtains;
    }

    public int getQtymaxconcent() {
        return qtymaxconcent;
    }

    public void setQtymaxconcent(int qtymaxconcent) {
        this.qtymaxconcent = qtymaxconcent;
    }

    public String getCodtri() {
        return codtri;
    }

    public void setCodtri(String codtri) {
        this.codtri = codtri;
    }

    public String getTipptosprd() {
        return tipptosprd;
    }

    public void setTipptosprd(String tipptosprd) {
        this.tipptosprd = tipptosprd;
    }

    public BigDecimal getMontobasptos() {
        return montobasptos;
    }

    public void setMontobasptos(BigDecimal montobasptos) {
        this.montobasptos = montobasptos;
    }

    public BigDecimal getEqvptos() {
        return eqvptos;
    }

    public void setEqvptos(BigDecimal eqvptos) {
        this.eqvptos = eqvptos;
    }

    @XmlTransient
    public List<FaMovimientosDetalle> getFaMovimientosDetalleList() {
        return faMovimientosDetalleList;
    }

    public void setFaMovimientosDetalleList(List<FaMovimientosDetalle> faMovimientosDetalleList) {
        this.faMovimientosDetalleList = faMovimientosDetalleList;
    }

    @XmlTransient
    public List<FaStockAlmacenes> getFaStockAlmacenesList() {
        return faStockAlmacenesList;
    }

    public void setFaStockAlmacenesList(List<FaStockAlmacenes> faStockAlmacenesList) {
        this.faStockAlmacenesList = faStockAlmacenesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codpro != null ? codpro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaProductos)) {
            return false;
        }
        FaProductos other = (FaProductos) object;
        if ((this.codpro == null && other.codpro != null)
                || (this.codpro != null && !this.codpro.equals(other.codpro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaProductos[ codpro=" + codpro + " ]";
    }

}
