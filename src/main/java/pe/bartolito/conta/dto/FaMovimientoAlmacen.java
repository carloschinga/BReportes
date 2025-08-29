/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "fa_movimiento_almacen")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FaMovimientoAlmacen.findAll", query = "SELECT f FROM FaMovimientoAlmacen f"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByInvnum", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.faMovimientoAlmacenPK.invnum = :invnum"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByCodpro", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.faMovimientoAlmacenPK.codpro = :codpro"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByEmpresaId", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.empresaId = :empresaId"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByDocid", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.docid = :docid"),
    @NamedQuery(name = "FaMovimientoAlmacen.findBySerdoc", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.serdoc = :serdoc"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByNumdoc", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.numdoc = :numdoc"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByFecmov", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.fecmov = :fecmov"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByTipoOpe", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.tipoOpe = :tipoOpe"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByDetItem", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.faMovimientoAlmacenPK.detItem = :detItem"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByDespro", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.despro = :despro"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByCant", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.cant = :cant"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByCantF", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.cantF = :cantF"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByCoscom", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.coscom = :coscom"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByCospro", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.cospro = :cospro"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByCodalm", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.faMovimientoAlmacenPK.codalm = :codalm"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByStkfra", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.stkfra = :stkfra"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByTipMov", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.tipMov = :tipMov"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByCosproCal", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.cosproCal = :cosproCal"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByOricodDoc", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.faMovimientoAlmacenPK.oricodDoc = :oricodDoc"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByDiaCabCompId", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.diaCabCompId = :diaCabCompId"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByTotval", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.totval = :totval"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByFeccre", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.feccre = :feccre"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByUsenam", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.usenam = :usenam"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByHostname", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.hostname = :hostname"),
    @NamedQuery(name = "FaMovimientoAlmacen.findByCodalm2", query = "SELECT f FROM FaMovimientoAlmacen f WHERE f.codalm2 = :codalm2")})
public class FaMovimientoAlmacen implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FaMovimientoAlmacenPK faMovimientoAlmacenPK;
    @Size(max = 2)
    @Column(name = "EmpresaId")
    private String empresaId;
    @Size(max = 2)
    @Column(name = "docid")
    private String docid;
    @Size(max = 4)
    @Column(name = "serdoc")
    private String serdoc;
    @Size(max = 10)
    @Column(name = "numdoc")
    private String numdoc;
    @Column(name = "fecmov")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecmov;
    @Size(max = 2)
    @Column(name = "tipo_ope")
    private String tipoOpe;
    @Size(max = 30)
    @Column(name = "despro")
    private String despro;
    @Column(name = "cant")
    private Integer cant;
    @Column(name = "cant_f")
    private Integer cantF;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "coscom")
    private BigDecimal coscom;
    @Column(name = "cospro")
    private BigDecimal cospro;
    @Column(name = "stkfra")
    private Integer stkfra;
    @Size(max = 1)
    @Column(name = "tip_mov")
    private String tipMov;
    @Column(name = "cospro_cal")
    private BigDecimal cosproCal;
    @Column(name = "DiaCabCompId")
    private BigInteger diaCabCompId;
    @Column(name = "totval")
    private BigDecimal totval;
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;
    @Size(max = 30)
    @Column(name = "usenam")
    private String usenam;
    @Size(max = 30)
    @Column(name = "hostname")
    private String hostname;
    @Size(max = 2)
    @Column(name = "codalm2")
    private String codalm2;

    public FaMovimientoAlmacen() {
    }

    public FaMovimientoAlmacen(FaMovimientoAlmacenPK faMovimientoAlmacenPK) {
        this.faMovimientoAlmacenPK = faMovimientoAlmacenPK;
    }

    public FaMovimientoAlmacen(int invnum, String codpro, int detItem, String codalm, String oricodDoc) {
        this.faMovimientoAlmacenPK = new FaMovimientoAlmacenPK(invnum, codpro, detItem, codalm, oricodDoc);
    }

    public FaMovimientoAlmacenPK getFaMovimientoAlmacenPK() {
        return faMovimientoAlmacenPK;
    }

    public void setFaMovimientoAlmacenPK(FaMovimientoAlmacenPK faMovimientoAlmacenPK) {
        this.faMovimientoAlmacenPK = faMovimientoAlmacenPK;
    }

    public String getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(String empresaId) {
        this.empresaId = empresaId;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getSerdoc() {
        return serdoc;
    }

    public void setSerdoc(String serdoc) {
        this.serdoc = serdoc;
    }

    public String getNumdoc() {
        return numdoc;
    }

    public void setNumdoc(String numdoc) {
        this.numdoc = numdoc;
    }

    public Date getFecmov() {
        return fecmov;
    }

    public void setFecmov(Date fecmov) {
        this.fecmov = fecmov;
    }

    public String getTipoOpe() {
        return tipoOpe;
    }

    public void setTipoOpe(String tipoOpe) {
        this.tipoOpe = tipoOpe;
    }

    public String getDespro() {
        return despro;
    }

    public void setDespro(String despro) {
        this.despro = despro;
    }

    public Integer getCant() {
        return cant;
    }

    public void setCant(Integer cant) {
        this.cant = cant;
    }

    public Integer getCantF() {
        return cantF;
    }

    public void setCantF(Integer cantF) {
        this.cantF = cantF;
    }

    public BigDecimal getCoscom() {
        return coscom;
    }

    public void setCoscom(BigDecimal coscom) {
        this.coscom = coscom;
    }

    public BigDecimal getCospro() {
        return cospro;
    }

    public void setCospro(BigDecimal cospro) {
        this.cospro = cospro;
    }

    public Integer getStkfra() {
        return stkfra;
    }

    public void setStkfra(Integer stkfra) {
        this.stkfra = stkfra;
    }

    public String getTipMov() {
        return tipMov;
    }

    public void setTipMov(String tipMov) {
        this.tipMov = tipMov;
    }

    public BigDecimal getCosproCal() {
        return cosproCal;
    }

    public void setCosproCal(BigDecimal cosproCal) {
        this.cosproCal = cosproCal;
    }

    public BigInteger getDiaCabCompId() {
        return diaCabCompId;
    }

    public void setDiaCabCompId(BigInteger diaCabCompId) {
        this.diaCabCompId = diaCabCompId;
    }

    public BigDecimal getTotval() {
        return totval;
    }

    public void setTotval(BigDecimal totval) {
        this.totval = totval;
    }

    public Date getFeccre() {
        return feccre;
    }

    public void setFeccre(Date feccre) {
        this.feccre = feccre;
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

    public String getCodalm2() {
        return codalm2;
    }

    public void setCodalm2(String codalm2) {
        this.codalm2 = codalm2;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (faMovimientoAlmacenPK != null ? faMovimientoAlmacenPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaMovimientoAlmacen)) {
            return false;
        }
        FaMovimientoAlmacen other = (FaMovimientoAlmacen) object;
        if ((this.faMovimientoAlmacenPK == null && other.faMovimientoAlmacenPK != null) || (this.faMovimientoAlmacenPK != null && !this.faMovimientoAlmacenPK.equals(other.faMovimientoAlmacenPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dao.conta.FaMovimientoAlmacen[ faMovimientoAlmacenPK=" + faMovimientoAlmacenPK + " ]";
    }
    
}
