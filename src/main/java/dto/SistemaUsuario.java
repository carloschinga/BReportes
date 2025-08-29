/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "sistema_usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SistemaUsuario.findAll", query = "SELECT s FROM SistemaUsuario s"),
    @NamedQuery(name = "SistemaUsuario.findByUsecod", query = "SELECT s FROM SistemaUsuario s WHERE s.usecod = :usecod"),
    @NamedQuery(name = "SistemaUsuario.findByCodalmVen", query = "SELECT s FROM SistemaUsuario s WHERE s.codalmVen = :codalmVen"),
    @NamedQuery(name = "SistemaUsuario.findByCodprvVen", query = "SELECT s FROM SistemaUsuario s WHERE s.codprvVen = :codprvVen"),
    @NamedQuery(name = "SistemaUsuario.findByCodalmInv", query = "SELECT s FROM SistemaUsuario s WHERE s.codalmInv = :codalmInv"),
    @NamedQuery(name = "SistemaUsuario.findByCodprvInv", query = "SELECT s FROM SistemaUsuario s WHERE s.codprvInv = :codprvInv"),
    @NamedQuery(name = "SistemaUsuario.findByCodalmCom", query = "SELECT s FROM SistemaUsuario s WHERE s.codalmCom = :codalmCom"),
    @NamedQuery(name = "SistemaUsuario.findByCodprvCom", query = "SELECT s FROM SistemaUsuario s WHERE s.codprvCom = :codprvCom"),
    @NamedQuery(name = "SistemaUsuario.findByTipkarCom", query = "SELECT s FROM SistemaUsuario s WHERE s.tipkarCom = :tipkarCom"),
    @NamedQuery(name = "SistemaUsuario.findByCodcajCaj", query = "SELECT s FROM SistemaUsuario s WHERE s.codcajCaj = :codcajCaj"),
    @NamedQuery(name = "SistemaUsuario.findByUseusrCaj", query = "SELECT s FROM SistemaUsuario s WHERE s.useusrCaj = :useusrCaj"),
    @NamedQuery(name = "SistemaUsuario.findByTpacodCaj", query = "SELECT s FROM SistemaUsuario s WHERE s.tpacodCaj = :tpacodCaj"),
    @NamedQuery(name = "SistemaUsuario.findByDocpagCaj", query = "SELECT s FROM SistemaUsuario s WHERE s.docpagCaj = :docpagCaj"),
    @NamedQuery(name = "SistemaUsuario.findByTdoserCaj", query = "SELECT s FROM SistemaUsuario s WHERE s.tdoserCaj = :tdoserCaj"),
    @NamedQuery(name = "SistemaUsuario.findByTipcajCaj", query = "SELECT s FROM SistemaUsuario s WHERE s.tipcajCaj = :tipcajCaj"),
    @NamedQuery(name = "SistemaUsuario.findByImpsegCaj", query = "SELECT s FROM SistemaUsuario s WHERE s.impsegCaj = :impsegCaj"),
    @NamedQuery(name = "SistemaUsuario.findByImpparCaj", query = "SELECT s FROM SistemaUsuario s WHERE s.impparCaj = :impparCaj"),
    @NamedQuery(name = "SistemaUsuario.findByImpextCaj", query = "SELECT s FROM SistemaUsuario s WHERE s.impextCaj = :impextCaj"),
    @NamedQuery(name = "SistemaUsuario.findByTipkarInv", query = "SELECT s FROM SistemaUsuario s WHERE s.tipkarInv = :tipkarInv"),
    @NamedQuery(name = "SistemaUsuario.findByPrfvigstaVen", query = "SELECT s FROM SistemaUsuario s WHERE s.prfvigstaVen = :prfvigstaVen"),
    @NamedQuery(name = "SistemaUsuario.findBySismodprecVen", query = "SELECT s FROM SistemaUsuario s WHERE s.sismodprecVen = :sismodprecVen"),
    @NamedQuery(name = "SistemaUsuario.findBySislimprecVen", query = "SELECT s FROM SistemaUsuario s WHERE s.sislimprecVen = :sislimprecVen"),
    @NamedQuery(name = "SistemaUsuario.findByModplnVen", query = "SELECT s FROM SistemaUsuario s WHERE s.modplnVen = :modplnVen"),
    @NamedQuery(name = "SistemaUsuario.findBySisscdCaj", query = "SELECT s FROM SistemaUsuario s WHERE s.sisscdCaj = :sisscdCaj"),
    @NamedQuery(name = "SistemaUsuario.findBySisinfadiVen", query = "SELECT s FROM SistemaUsuario s WHERE s.sisinfadiVen = :sisinfadiVen"),
    @NamedQuery(name = "SistemaUsuario.findByTipkarBco", query = "SELECT s FROM SistemaUsuario s WHERE s.tipkarBco = :tipkarBco"),
    @NamedQuery(name = "SistemaUsuario.findBySiscod", query = "SELECT s FROM SistemaUsuario s WHERE s.siscod = :siscod"),
    @NamedQuery(name = "SistemaUsuario.findByResvtafecVen", query = "SELECT s FROM SistemaUsuario s WHERE s.resvtafecVen = :resvtafecVen"),
    @NamedQuery(name = "SistemaUsuario.findByResdocfecCaj", query = "SELECT s FROM SistemaUsuario s WHERE s.resdocfecCaj = :resdocfecCaj"),
    @NamedQuery(name = "SistemaUsuario.findByAsocdocVen", query = "SELECT s FROM SistemaUsuario s WHERE s.asocdocVen = :asocdocVen"),
    @NamedQuery(name = "SistemaUsuario.findByTdoserVen", query = "SELECT s FROM SistemaUsuario s WHERE s.tdoserVen = :tdoserVen"),
    @NamedQuery(name = "SistemaUsuario.findByTdoserCli", query = "SELECT s FROM SistemaUsuario s WHERE s.tdoserCli = :tdoserCli"),
    @NamedQuery(name = "SistemaUsuario.findByResnlfcotVen", query = "SELECT s FROM SistemaUsuario s WHERE s.resnlfcotVen = :resnlfcotVen"),
    @NamedQuery(name = "SistemaUsuario.findByModdtovtaInv", query = "SELECT s FROM SistemaUsuario s WHERE s.moddtovtaInv = :moddtovtaInv"),
    @NamedQuery(name = "SistemaUsuario.findByModutivtaInv", query = "SELECT s FROM SistemaUsuario s WHERE s.modutivtaInv = :modutivtaInv"),
    @NamedQuery(name = "SistemaUsuario.findByTipkardflInv", query = "SELECT s FROM SistemaUsuario s WHERE s.tipkardflInv = :tipkardflInv"),
    @NamedQuery(name = "SistemaUsuario.findByCodalmAdm", query = "SELECT s FROM SistemaUsuario s WHERE s.codalmAdm = :codalmAdm"),
    @NamedQuery(name = "SistemaUsuario.findByCodprvAdm", query = "SELECT s FROM SistemaUsuario s WHERE s.codprvAdm = :codprvAdm"),
    @NamedQuery(name = "SistemaUsuario.findByTdoserdeInv", query = "SELECT s FROM SistemaUsuario s WHERE s.tdoserdeInv = :tdoserdeInv"),
    @NamedQuery(name = "SistemaUsuario.findByModcoscomCom", query = "SELECT s FROM SistemaUsuario s WHERE s.modcoscomCom = :modcoscomCom"),
    @NamedQuery(name = "SistemaUsuario.findByVarcoscomCom", query = "SELECT s FROM SistemaUsuario s WHERE s.varcoscomCom = :varcoscomCom"),
    @NamedQuery(name = "SistemaUsuario.findByUtilimprecVen", query = "SELECT s FROM SistemaUsuario s WHERE s.utilimprecVen = :utilimprecVen"),
    @NamedQuery(name = "SistemaUsuario.findByImpcotVen", query = "SELECT s FROM SistemaUsuario s WHERE s.impcotVen = :impcotVen"),
    @NamedQuery(name = "SistemaUsuario.findBySismodprecCot", query = "SELECT s FROM SistemaUsuario s WHERE s.sismodprecCot = :sismodprecCot"),
    @NamedQuery(name = "SistemaUsuario.findBySislimprecCot", query = "SELECT s FROM SistemaUsuario s WHERE s.sislimprecCot = :sislimprecCot"),
    @NamedQuery(name = "SistemaUsuario.findByUtilimprecCot", query = "SELECT s FROM SistemaUsuario s WHERE s.utilimprecCot = :utilimprecCot"),
    @NamedQuery(name = "SistemaUsuario.findBySismodprerefctVen", query = "SELECT s FROM SistemaUsuario s WHERE s.sismodprerefctVen = :sismodprerefctVen"),
    @NamedQuery(name = "SistemaUsuario.findBySismodprereffcVen", query = "SELECT s FROM SistemaUsuario s WHERE s.sismodprereffcVen = :sismodprereffcVen"),
    @NamedQuery(name = "SistemaUsuario.findByStavistotcalcCaj", query = "SELECT s FROM SistemaUsuario s WHERE s.stavistotcalcCaj = :stavistotcalcCaj"),
    @NamedQuery(name = "SistemaUsuario.findByModdetlotVen", query = "SELECT s FROM SistemaUsuario s WHERE s.moddetlotVen = :moddetlotVen"),
    @NamedQuery(name = "SistemaUsuario.findByTdoserncCaj", query = "SELECT s FROM SistemaUsuario s WHERE s.tdoserncCaj = :tdoserncCaj"),
    @NamedQuery(name = "SistemaUsuario.findByAnuvtacobcienVen", query = "SELECT s FROM SistemaUsuario s WHERE s.anuvtacobcienVen = :anuvtacobcienVen"),
    @NamedQuery(name = "SistemaUsuario.findByDocmonceroCaj", query = "SELECT s FROM SistemaUsuario s WHERE s.docmonceroCaj = :docmonceroCaj"),
    @NamedQuery(name = "SistemaUsuario.findByVisualsustautVen", query = "SELECT s FROM SistemaUsuario s WHERE s.visualsustautVen = :visualsustautVen"),
    @NamedQuery(name = "SistemaUsuario.findBySisficVen", query = "SELECT s FROM SistemaUsuario s WHERE s.sisficVen = :sisficVen"),
    @NamedQuery(name = "SistemaUsuario.findByCodrepVen", query = "SELECT s FROM SistemaUsuario s WHERE s.codrepVen = :codrepVen"),
    @NamedQuery(name = "SistemaUsuario.findBySisvisrepmesactCaj", query = "SELECT s FROM SistemaUsuario s WHERE s.sisvisrepmesactCaj = :sisvisrepmesactCaj"),
    @NamedQuery(name = "SistemaUsuario.findByGruvtaVen", query = "SELECT s FROM SistemaUsuario s WHERE s.gruvtaVen = :gruvtaVen"),
    @NamedQuery(name = "SistemaUsuario.findByTdoserenvFmg", query = "SELECT s FROM SistemaUsuario s WHERE s.tdoserenvFmg = :tdoserenvFmg"),
    @NamedQuery(name = "SistemaUsuario.findByVtaprdsstockVen", query = "SELECT s FROM SistemaUsuario s WHERE s.vtaprdsstockVen = :vtaprdsstockVen"),
    @NamedQuery(name = "SistemaUsuario.findByRegcantmayocCom", query = "SELECT s FROM SistemaUsuario s WHERE s.regcantmayocCom = :regcantmayocCom")})
public class SistemaUsuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "usecod")
    private Integer usecod;
    @Size(max = 2)
    @Column(name = "codalm_ven")
    private String codalmVen;
    @Size(max = 4)
    @Column(name = "codprv_ven")
    private String codprvVen;
    @Size(max = 2)
    @Column(name = "codalm_inv")
    private String codalmInv;
    @Size(max = 4)
    @Column(name = "codprv_inv")
    private String codprvInv;
    @Size(max = 2)
    @Column(name = "codalm_com")
    private String codalmCom;
    @Size(max = 4)
    @Column(name = "codprv_com")
    private String codprvCom;
    @Size(max = 150)
    @Column(name = "tipkar_com")
    private String tipkarCom;
    @Size(max = 2)
    @Column(name = "codcaj_caj")
    private String codcajCaj;
    @Size(max = 10)
    @Column(name = "useusr_caj")
    private String useusrCaj;
    @Size(max = 2)
    @Column(name = "tpacod_caj")
    private String tpacodCaj;
    @Size(max = 2)
    @Column(name = "docpag_caj")
    private String docpagCaj;
    @Size(max = 2)
    @Column(name = "tdoser_caj")
    private String tdoserCaj;
    @Size(max = 25)
    @Column(name = "tipcaj_caj")
    private String tipcajCaj;
    @Column(name = "impseg_caj")
    private Character impsegCaj;
    @Column(name = "imppar_caj")
    private Character impparCaj;
    @Column(name = "impext_caj")
    private Character impextCaj;
    @Size(max = 150)
    @Column(name = "tipkar_inv")
    private String tipkarInv;
    @Size(max = 1)
    @Column(name = "prfvigsta_ven")
    private String prfvigstaVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sismodprec_ven")
    private String sismodprecVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sislimprec_ven")
    private String sislimprecVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "modpln_ven")
    private String modplnVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisscd_caj")
    private String sisscdCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisinfadi_ven")
    private String sisinfadiVen;
    @Size(max = 150)
    @Column(name = "tipkar_bco")
    private String tipkarBco;
    @Basic(optional = false)
    @NotNull
    @Column(name = "siscod")
    private int siscod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "resvtafec_ven")
    private String resvtafecVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "resdocfec_caj")
    private String resdocfecCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "asocdoc_ven")
    private String asocdocVen;
    @Size(max = 2)
    @Column(name = "tdoser_ven")
    private String tdoserVen;
    @Size(max = 2)
    @Column(name = "tdoser_cli")
    private String tdoserCli;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "resnlfcot_ven")
    private String resnlfcotVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "moddtovta_inv")
    private String moddtovtaInv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "modutivta_inv")
    private String modutivtaInv;
    @Size(max = 2)
    @Column(name = "tipkardfl_inv")
    private String tipkardflInv;
    @Size(max = 2)
    @Column(name = "codalm_adm")
    private String codalmAdm;
    @Size(max = 4)
    @Column(name = "codprv_adm")
    private String codprvAdm;
    @Size(max = 2)
    @Column(name = "tdoserde_inv")
    private String tdoserdeInv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "modcoscom_com")
    private String modcoscomCom;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "varcoscom_com")
    private BigDecimal varcoscomCom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "utilimprec_ven")
    private BigDecimal utilimprecVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "impcot_ven")
    private String impcotVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sismodprec_cot")
    private String sismodprecCot;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sislimprec_cot")
    private String sislimprecCot;
    @Basic(optional = false)
    @NotNull
    @Column(name = "utilimprec_cot")
    private BigDecimal utilimprecCot;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sismodprerefct_ven")
    private String sismodprerefctVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sismodprereffc_ven")
    private String sismodprereffcVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "stavistotcalc_caj")
    private String stavistotcalcCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "moddetlot_ven")
    private String moddetlotVen;
    @Size(max = 2)
    @Column(name = "tdosernc_caj")
    private String tdoserncCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "anuvtacobcien_ven")
    private String anuvtacobcienVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "docmoncero_caj")
    private String docmonceroCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "visualsustaut_ven")
    private String visualsustautVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisfic_ven")
    private String sisficVen;
    @Size(max = 4)
    @Column(name = "codrep_ven")
    private String codrepVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sisvisrepmesact_caj")
    private String sisvisrepmesactCaj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "gruvta_ven")
    private String gruvtaVen;
    @Size(max = 2)
    @Column(name = "tdoserenv_fmg")
    private String tdoserenvFmg;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "vtaprdsstock_ven")
    private String vtaprdsstockVen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "regcantmayoc_com")
    private String regcantmayocCom;
    @JoinColumn(name = "usecod", referencedColumnName = "usecod", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Usuarios usuarios;

    public SistemaUsuario() {
    }

    public SistemaUsuario(Integer usecod) {
        this.usecod = usecod;
    }

    public SistemaUsuario(Integer usecod, String sismodprecVen, String sislimprecVen, String modplnVen, String sisscdCaj, String sisinfadiVen, int siscod, String resvtafecVen, String resdocfecCaj, String asocdocVen, String resnlfcotVen, String moddtovtaInv, String modutivtaInv, String modcoscomCom, BigDecimal varcoscomCom, BigDecimal utilimprecVen, String impcotVen, String sismodprecCot, String sislimprecCot, BigDecimal utilimprecCot, String sismodprerefctVen, String sismodprereffcVen, String stavistotcalcCaj, String moddetlotVen, String anuvtacobcienVen, String docmonceroCaj, String visualsustautVen, String sisficVen, String sisvisrepmesactCaj, String gruvtaVen, String vtaprdsstockVen, String regcantmayocCom) {
        this.usecod = usecod;
        this.sismodprecVen = sismodprecVen;
        this.sislimprecVen = sislimprecVen;
        this.modplnVen = modplnVen;
        this.sisscdCaj = sisscdCaj;
        this.sisinfadiVen = sisinfadiVen;
        this.siscod = siscod;
        this.resvtafecVen = resvtafecVen;
        this.resdocfecCaj = resdocfecCaj;
        this.asocdocVen = asocdocVen;
        this.resnlfcotVen = resnlfcotVen;
        this.moddtovtaInv = moddtovtaInv;
        this.modutivtaInv = modutivtaInv;
        this.modcoscomCom = modcoscomCom;
        this.varcoscomCom = varcoscomCom;
        this.utilimprecVen = utilimprecVen;
        this.impcotVen = impcotVen;
        this.sismodprecCot = sismodprecCot;
        this.sislimprecCot = sislimprecCot;
        this.utilimprecCot = utilimprecCot;
        this.sismodprerefctVen = sismodprerefctVen;
        this.sismodprereffcVen = sismodprereffcVen;
        this.stavistotcalcCaj = stavistotcalcCaj;
        this.moddetlotVen = moddetlotVen;
        this.anuvtacobcienVen = anuvtacobcienVen;
        this.docmonceroCaj = docmonceroCaj;
        this.visualsustautVen = visualsustautVen;
        this.sisficVen = sisficVen;
        this.sisvisrepmesactCaj = sisvisrepmesactCaj;
        this.gruvtaVen = gruvtaVen;
        this.vtaprdsstockVen = vtaprdsstockVen;
        this.regcantmayocCom = regcantmayocCom;
    }

    public Integer getUsecod() {
        return usecod;
    }

    public void setUsecod(Integer usecod) {
        this.usecod = usecod;
    }

    public String getCodalmVen() {
        return codalmVen;
    }

    public void setCodalmVen(String codalmVen) {
        this.codalmVen = codalmVen;
    }

    public String getCodprvVen() {
        return codprvVen;
    }

    public void setCodprvVen(String codprvVen) {
        this.codprvVen = codprvVen;
    }

    public String getCodalmInv() {
        return codalmInv;
    }

    public void setCodalmInv(String codalmInv) {
        this.codalmInv = codalmInv;
    }

    public String getCodprvInv() {
        return codprvInv;
    }

    public void setCodprvInv(String codprvInv) {
        this.codprvInv = codprvInv;
    }

    public String getCodalmCom() {
        return codalmCom;
    }

    public void setCodalmCom(String codalmCom) {
        this.codalmCom = codalmCom;
    }

    public String getCodprvCom() {
        return codprvCom;
    }

    public void setCodprvCom(String codprvCom) {
        this.codprvCom = codprvCom;
    }

    public String getTipkarCom() {
        return tipkarCom;
    }

    public void setTipkarCom(String tipkarCom) {
        this.tipkarCom = tipkarCom;
    }

    public String getCodcajCaj() {
        return codcajCaj;
    }

    public void setCodcajCaj(String codcajCaj) {
        this.codcajCaj = codcajCaj;
    }

    public String getUseusrCaj() {
        return useusrCaj;
    }

    public void setUseusrCaj(String useusrCaj) {
        this.useusrCaj = useusrCaj;
    }

    public String getTpacodCaj() {
        return tpacodCaj;
    }

    public void setTpacodCaj(String tpacodCaj) {
        this.tpacodCaj = tpacodCaj;
    }

    public String getDocpagCaj() {
        return docpagCaj;
    }

    public void setDocpagCaj(String docpagCaj) {
        this.docpagCaj = docpagCaj;
    }

    public String getTdoserCaj() {
        return tdoserCaj;
    }

    public void setTdoserCaj(String tdoserCaj) {
        this.tdoserCaj = tdoserCaj;
    }

    public String getTipcajCaj() {
        return tipcajCaj;
    }

    public void setTipcajCaj(String tipcajCaj) {
        this.tipcajCaj = tipcajCaj;
    }

    public Character getImpsegCaj() {
        return impsegCaj;
    }

    public void setImpsegCaj(Character impsegCaj) {
        this.impsegCaj = impsegCaj;
    }

    public Character getImpparCaj() {
        return impparCaj;
    }

    public void setImpparCaj(Character impparCaj) {
        this.impparCaj = impparCaj;
    }

    public Character getImpextCaj() {
        return impextCaj;
    }

    public void setImpextCaj(Character impextCaj) {
        this.impextCaj = impextCaj;
    }

    public String getTipkarInv() {
        return tipkarInv;
    }

    public void setTipkarInv(String tipkarInv) {
        this.tipkarInv = tipkarInv;
    }

    public String getPrfvigstaVen() {
        return prfvigstaVen;
    }

    public void setPrfvigstaVen(String prfvigstaVen) {
        this.prfvigstaVen = prfvigstaVen;
    }

    public String getSismodprecVen() {
        return sismodprecVen;
    }

    public void setSismodprecVen(String sismodprecVen) {
        this.sismodprecVen = sismodprecVen;
    }

    public String getSislimprecVen() {
        return sislimprecVen;
    }

    public void setSislimprecVen(String sislimprecVen) {
        this.sislimprecVen = sislimprecVen;
    }

    public String getModplnVen() {
        return modplnVen;
    }

    public void setModplnVen(String modplnVen) {
        this.modplnVen = modplnVen;
    }

    public String getSisscdCaj() {
        return sisscdCaj;
    }

    public void setSisscdCaj(String sisscdCaj) {
        this.sisscdCaj = sisscdCaj;
    }

    public String getSisinfadiVen() {
        return sisinfadiVen;
    }

    public void setSisinfadiVen(String sisinfadiVen) {
        this.sisinfadiVen = sisinfadiVen;
    }

    public String getTipkarBco() {
        return tipkarBco;
    }

    public void setTipkarBco(String tipkarBco) {
        this.tipkarBco = tipkarBco;
    }

    public int getSiscod() {
        return siscod;
    }

    public void setSiscod(int siscod) {
        this.siscod = siscod;
    }

    public String getResvtafecVen() {
        return resvtafecVen;
    }

    public void setResvtafecVen(String resvtafecVen) {
        this.resvtafecVen = resvtafecVen;
    }

    public String getResdocfecCaj() {
        return resdocfecCaj;
    }

    public void setResdocfecCaj(String resdocfecCaj) {
        this.resdocfecCaj = resdocfecCaj;
    }

    public String getAsocdocVen() {
        return asocdocVen;
    }

    public void setAsocdocVen(String asocdocVen) {
        this.asocdocVen = asocdocVen;
    }

    public String getTdoserVen() {
        return tdoserVen;
    }

    public void setTdoserVen(String tdoserVen) {
        this.tdoserVen = tdoserVen;
    }

    public String getTdoserCli() {
        return tdoserCli;
    }

    public void setTdoserCli(String tdoserCli) {
        this.tdoserCli = tdoserCli;
    }

    public String getResnlfcotVen() {
        return resnlfcotVen;
    }

    public void setResnlfcotVen(String resnlfcotVen) {
        this.resnlfcotVen = resnlfcotVen;
    }

    public String getModdtovtaInv() {
        return moddtovtaInv;
    }

    public void setModdtovtaInv(String moddtovtaInv) {
        this.moddtovtaInv = moddtovtaInv;
    }

    public String getModutivtaInv() {
        return modutivtaInv;
    }

    public void setModutivtaInv(String modutivtaInv) {
        this.modutivtaInv = modutivtaInv;
    }

    public String getTipkardflInv() {
        return tipkardflInv;
    }

    public void setTipkardflInv(String tipkardflInv) {
        this.tipkardflInv = tipkardflInv;
    }

    public String getCodalmAdm() {
        return codalmAdm;
    }

    public void setCodalmAdm(String codalmAdm) {
        this.codalmAdm = codalmAdm;
    }

    public String getCodprvAdm() {
        return codprvAdm;
    }

    public void setCodprvAdm(String codprvAdm) {
        this.codprvAdm = codprvAdm;
    }

    public String getTdoserdeInv() {
        return tdoserdeInv;
    }

    public void setTdoserdeInv(String tdoserdeInv) {
        this.tdoserdeInv = tdoserdeInv;
    }

    public String getModcoscomCom() {
        return modcoscomCom;
    }

    public void setModcoscomCom(String modcoscomCom) {
        this.modcoscomCom = modcoscomCom;
    }

    public BigDecimal getVarcoscomCom() {
        return varcoscomCom;
    }

    public void setVarcoscomCom(BigDecimal varcoscomCom) {
        this.varcoscomCom = varcoscomCom;
    }

    public BigDecimal getUtilimprecVen() {
        return utilimprecVen;
    }

    public void setUtilimprecVen(BigDecimal utilimprecVen) {
        this.utilimprecVen = utilimprecVen;
    }

    public String getImpcotVen() {
        return impcotVen;
    }

    public void setImpcotVen(String impcotVen) {
        this.impcotVen = impcotVen;
    }

    public String getSismodprecCot() {
        return sismodprecCot;
    }

    public void setSismodprecCot(String sismodprecCot) {
        this.sismodprecCot = sismodprecCot;
    }

    public String getSislimprecCot() {
        return sislimprecCot;
    }

    public void setSislimprecCot(String sislimprecCot) {
        this.sislimprecCot = sislimprecCot;
    }

    public BigDecimal getUtilimprecCot() {
        return utilimprecCot;
    }

    public void setUtilimprecCot(BigDecimal utilimprecCot) {
        this.utilimprecCot = utilimprecCot;
    }

    public String getSismodprerefctVen() {
        return sismodprerefctVen;
    }

    public void setSismodprerefctVen(String sismodprerefctVen) {
        this.sismodprerefctVen = sismodprerefctVen;
    }

    public String getSismodprereffcVen() {
        return sismodprereffcVen;
    }

    public void setSismodprereffcVen(String sismodprereffcVen) {
        this.sismodprereffcVen = sismodprereffcVen;
    }

    public String getStavistotcalcCaj() {
        return stavistotcalcCaj;
    }

    public void setStavistotcalcCaj(String stavistotcalcCaj) {
        this.stavistotcalcCaj = stavistotcalcCaj;
    }

    public String getModdetlotVen() {
        return moddetlotVen;
    }

    public void setModdetlotVen(String moddetlotVen) {
        this.moddetlotVen = moddetlotVen;
    }

    public String getTdoserncCaj() {
        return tdoserncCaj;
    }

    public void setTdoserncCaj(String tdoserncCaj) {
        this.tdoserncCaj = tdoserncCaj;
    }

    public String getAnuvtacobcienVen() {
        return anuvtacobcienVen;
    }

    public void setAnuvtacobcienVen(String anuvtacobcienVen) {
        this.anuvtacobcienVen = anuvtacobcienVen;
    }

    public String getDocmonceroCaj() {
        return docmonceroCaj;
    }

    public void setDocmonceroCaj(String docmonceroCaj) {
        this.docmonceroCaj = docmonceroCaj;
    }

    public String getVisualsustautVen() {
        return visualsustautVen;
    }

    public void setVisualsustautVen(String visualsustautVen) {
        this.visualsustautVen = visualsustautVen;
    }

    public String getSisficVen() {
        return sisficVen;
    }

    public void setSisficVen(String sisficVen) {
        this.sisficVen = sisficVen;
    }

    public String getCodrepVen() {
        return codrepVen;
    }

    public void setCodrepVen(String codrepVen) {
        this.codrepVen = codrepVen;
    }

    public String getSisvisrepmesactCaj() {
        return sisvisrepmesactCaj;
    }

    public void setSisvisrepmesactCaj(String sisvisrepmesactCaj) {
        this.sisvisrepmesactCaj = sisvisrepmesactCaj;
    }

    public String getGruvtaVen() {
        return gruvtaVen;
    }

    public void setGruvtaVen(String gruvtaVen) {
        this.gruvtaVen = gruvtaVen;
    }

    public String getTdoserenvFmg() {
        return tdoserenvFmg;
    }

    public void setTdoserenvFmg(String tdoserenvFmg) {
        this.tdoserenvFmg = tdoserenvFmg;
    }

    public String getVtaprdsstockVen() {
        return vtaprdsstockVen;
    }

    public void setVtaprdsstockVen(String vtaprdsstockVen) {
        this.vtaprdsstockVen = vtaprdsstockVen;
    }

    public String getRegcantmayocCom() {
        return regcantmayocCom;
    }

    public void setRegcantmayocCom(String regcantmayocCom) {
        this.regcantmayocCom = regcantmayocCom;
    }

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usecod != null ? usecod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SistemaUsuario)) {
            return false;
        }
        SistemaUsuario other = (SistemaUsuario) object;
        if ((this.usecod == null && other.usecod != null) || (this.usecod != null && !this.usecod.equals(other.usecod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.SistemaUsuario[ usecod=" + usecod + " ]";
    }
    
}
