/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "DiarioDetalle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DiarioDetalle.findAll", query = "SELECT d FROM DiarioDetalle d"),
    @NamedQuery(name = "DiarioDetalle.findByDiaDetItem", query = "SELECT d FROM DiarioDetalle d WHERE d.diarioDetallePK.diaDetItem = :diaDetItem"),
    @NamedQuery(name = "DiarioDetalle.findByDiaCabCompId", query = "SELECT d FROM DiarioDetalle d WHERE d.diarioDetallePK.diaCabCompId = :diaCabCompId"),
    @NamedQuery(name = "DiarioDetalle.findByDocId", query = "SELECT d FROM DiarioDetalle d WHERE d.docId = :docId"),
    @NamedQuery(name = "DiarioDetalle.findByProcId", query = "SELECT d FROM DiarioDetalle d WHERE d.procId = :procId"),
    @NamedQuery(name = "DiarioDetalle.findByActivId", query = "SELECT d FROM DiarioDetalle d WHERE d.activId = :activId"),
    @NamedQuery(name = "DiarioDetalle.findByTareaId", query = "SELECT d FROM DiarioDetalle d WHERE d.tareaId = :tareaId"),
    @NamedQuery(name = "DiarioDetalle.findByActivoId", query = "SELECT d FROM DiarioDetalle d WHERE d.activoId = :activoId"),
    @NamedQuery(name = "DiarioDetalle.findByProdId", query = "SELECT d FROM DiarioDetalle d WHERE d.prodId = :prodId"),
    @NamedQuery(name = "DiarioDetalle.findByCenCostResp", query = "SELECT d FROM DiarioDetalle d WHERE d.cenCostResp = :cenCostResp"),
    @NamedQuery(name = "DiarioDetalle.findByGerenciaId", query = "SELECT d FROM DiarioDetalle d WHERE d.gerenciaId = :gerenciaId"),
    @NamedQuery(name = "DiarioDetalle.findByDptoId", query = "SELECT d FROM DiarioDetalle d WHERE d.dptoId = :dptoId"),
    @NamedQuery(name = "DiarioDetalle.findBySeccId", query = "SELECT d FROM DiarioDetalle d WHERE d.seccId = :seccId"),
    @NamedQuery(name = "DiarioDetalle.findByCuentaId", query = "SELECT d FROM DiarioDetalle d WHERE d.cuentaId = :cuentaId"),
    @NamedQuery(name = "DiarioDetalle.findBySubCtaId", query = "SELECT d FROM DiarioDetalle d WHERE d.subCtaId = :subCtaId"),
    @NamedQuery(name = "DiarioDetalle.findByDivisioId", query = "SELECT d FROM DiarioDetalle d WHERE d.divisioId = :divisioId"),
    @NamedQuery(name = "DiarioDetalle.findBySubDivId", query = "SELECT d FROM DiarioDetalle d WHERE d.subDivId = :subDivId"),
    @NamedQuery(name = "DiarioDetalle.findByDiaDetDebe", query = "SELECT d FROM DiarioDetalle d WHERE d.diaDetDebe = :diaDetDebe"),
    @NamedQuery(name = "DiarioDetalle.findByDiaDetHaber", query = "SELECT d FROM DiarioDetalle d WHERE d.diaDetHaber = :diaDetHaber"),
    @NamedQuery(name = "DiarioDetalle.findByDiaDetNumDoc", query = "SELECT d FROM DiarioDetalle d WHERE d.diaDetNumDoc = :diaDetNumDoc"),
    @NamedQuery(name = "DiarioDetalle.findByDiaDetMovRefCabId", query = "SELECT d FROM DiarioDetalle d WHERE d.diaDetMovRefCabId = :diaDetMovRefCabId"),
    @NamedQuery(name = "DiarioDetalle.findByDiaDetMovRefDetId", query = "SELECT d FROM DiarioDetalle d WHERE d.diaDetMovRefDetId = :diaDetMovRefDetId"),
    @NamedQuery(name = "DiarioDetalle.findByUsrSistema", query = "SELECT d FROM DiarioDetalle d WHERE d.usrSistema = :usrSistema"),
    @NamedQuery(name = "DiarioDetalle.findByFecSistema", query = "SELECT d FROM DiarioDetalle d WHERE d.fecSistema = :fecSistema"),
    @NamedQuery(name = "DiarioDetalle.findByHrsSistema", query = "SELECT d FROM DiarioDetalle d WHERE d.hrsSistema = :hrsSistema"),
    @NamedQuery(name = "DiarioDetalle.findByUp", query = "SELECT d FROM DiarioDetalle d WHERE d.up = :up"),
    @NamedQuery(name = "DiarioDetalle.findByDiaDetSerieDoc", query = "SELECT d FROM DiarioDetalle d WHERE d.diaDetSerieDoc = :diaDetSerieDoc"),
    @NamedQuery(name = "DiarioDetalle.findByMonedaId", query = "SELECT d FROM DiarioDetalle d WHERE d.monedaId = :monedaId"),
    @NamedQuery(name = "DiarioDetalle.findByFecsistema2", query = "SELECT d FROM DiarioDetalle d WHERE d.fecsistema2 = :fecsistema2"),
    @NamedQuery(name = "DiarioDetalle.findByFecsistema3", query = "SELECT d FROM DiarioDetalle d WHERE d.fecsistema3 = :fecsistema3")})
public class DiarioDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DiarioDetallePK diarioDetallePK;
    @Size(max = 2)
    @Column(name = "DocId")
    private String docId;
    @Size(max = 2)
    @Column(name = "ProcId")
    private String procId;
    @Size(max = 2)
    @Column(name = "ActivId")
    private String activId;
    @Size(max = 2)
    @Column(name = "TareaId")
    private String tareaId;
    @Size(max = 5)
    @Column(name = "ActivoId")
    private String activoId;
    @Size(max = 2)
    @Column(name = "ProdId")
    private String prodId;
    @Size(max = 2)
    @Column(name = "CenCostResp")
    private String cenCostResp;
    @Size(max = 2)
    @Column(name = "GerenciaId")
    private String gerenciaId;
    @Size(max = 2)
    @Column(name = "DptoId")
    private String dptoId;
    @Size(max = 2)
    @Column(name = "SeccId")
    private String seccId;
    @Size(max = 2)
    @Column(name = "CuentaId")
    private String cuentaId;
    @Column(name = "SubCtaId")
    private Character subCtaId;
    @Column(name = "DivisioId")
    private Character divisioId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "SubDivId")
    private String subDivId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "DiaDetDebe")
    private BigDecimal diaDetDebe;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DiaDetHaber")
    private BigDecimal diaDetHaber;
    @Size(max = 15)
    @Column(name = "DiaDetNumDoc")
    private String diaDetNumDoc;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "DiaDetTexOpe")
    private String diaDetTexOpe;
    @Column(name = "DiaDetMovRefCabId")
    private BigInteger diaDetMovRefCabId;
    @Column(name = "DiaDetMovRefDetId")
    private BigInteger diaDetMovRefDetId;
    @Size(max = 10)
    @Column(name = "UsrSistema")
    private String usrSistema;
    @Column(name = "FecSistema")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecSistema;
    @Size(max = 10)
    @Column(name = "HrsSistema")
    private String hrsSistema;
    @Column(name = "up")
    private Integer up;
    @Size(max = 20)
    @Column(name = "DiaDetSerieDoc")
    private String diaDetSerieDoc;
    @Size(max = 2)
    @Column(name = "MonedaId")
    private String monedaId;
    @Column(name = "fecsistema2")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecsistema2;
    @Column(name = "fecsistema3")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecsistema3;
    @JoinColumn(name = "CtaCteId", referencedColumnName = "CtaCteId")
    @ManyToOne
    private CuentaCorriente ctaCteId;
    @JoinColumn(name = "DiaCabCompId", referencedColumnName = "DiaCabCompId", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private DiarioCabecera diarioCabecera;

    public DiarioDetalle() {
    }

    public DiarioDetalle(DiarioDetallePK diarioDetallePK) {
        this.diarioDetallePK = diarioDetallePK;
    }

    public DiarioDetalle(DiarioDetallePK diarioDetallePK, String subDivId, BigDecimal diaDetDebe, BigDecimal diaDetHaber) {
        this.diarioDetallePK = diarioDetallePK;
        this.subDivId = subDivId;
        this.diaDetDebe = diaDetDebe;
        this.diaDetHaber = diaDetHaber;
    }

    public DiarioDetalle(int diaDetItem, long diaCabCompId) {
        this.diarioDetallePK = new DiarioDetallePK(diaDetItem, diaCabCompId);
    }

    public DiarioDetallePK getDiarioDetallePK() {
        return diarioDetallePK;
    }

    public void setDiarioDetallePK(DiarioDetallePK diarioDetallePK) {
        this.diarioDetallePK = diarioDetallePK;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public String getActivId() {
        return activId;
    }

    public void setActivId(String activId) {
        this.activId = activId;
    }

    public String getTareaId() {
        return tareaId;
    }

    public void setTareaId(String tareaId) {
        this.tareaId = tareaId;
    }

    public String getActivoId() {
        return activoId;
    }

    public void setActivoId(String activoId) {
        this.activoId = activoId;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getCenCostResp() {
        return cenCostResp;
    }

    public void setCenCostResp(String cenCostResp) {
        this.cenCostResp = cenCostResp;
    }

    public String getGerenciaId() {
        return gerenciaId;
    }

    public void setGerenciaId(String gerenciaId) {
        this.gerenciaId = gerenciaId;
    }

    public String getDptoId() {
        return dptoId;
    }

    public void setDptoId(String dptoId) {
        this.dptoId = dptoId;
    }

    public String getSeccId() {
        return seccId;
    }

    public void setSeccId(String seccId) {
        this.seccId = seccId;
    }

    public String getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(String cuentaId) {
        this.cuentaId = cuentaId;
    }

    public Character getSubCtaId() {
        return subCtaId;
    }

    public void setSubCtaId(Character subCtaId) {
        this.subCtaId = subCtaId;
    }

    public Character getDivisioId() {
        return divisioId;
    }

    public void setDivisioId(Character divisioId) {
        this.divisioId = divisioId;
    }

    public String getSubDivId() {
        return subDivId;
    }

    public void setSubDivId(String subDivId) {
        this.subDivId = subDivId;
    }

    public BigDecimal getDiaDetDebe() {
        return diaDetDebe;
    }

    public void setDiaDetDebe(BigDecimal diaDetDebe) {
        this.diaDetDebe = diaDetDebe;
    }

    public BigDecimal getDiaDetHaber() {
        return diaDetHaber;
    }

    public void setDiaDetHaber(BigDecimal diaDetHaber) {
        this.diaDetHaber = diaDetHaber;
    }

    public String getDiaDetNumDoc() {
        return diaDetNumDoc;
    }

    public void setDiaDetNumDoc(String diaDetNumDoc) {
        this.diaDetNumDoc = diaDetNumDoc;
    }

    public String getDiaDetTexOpe() {
        return diaDetTexOpe;
    }

    public void setDiaDetTexOpe(String diaDetTexOpe) {
        this.diaDetTexOpe = diaDetTexOpe;
    }

    public BigInteger getDiaDetMovRefCabId() {
        return diaDetMovRefCabId;
    }

    public void setDiaDetMovRefCabId(BigInteger diaDetMovRefCabId) {
        this.diaDetMovRefCabId = diaDetMovRefCabId;
    }

    public BigInteger getDiaDetMovRefDetId() {
        return diaDetMovRefDetId;
    }

    public void setDiaDetMovRefDetId(BigInteger diaDetMovRefDetId) {
        this.diaDetMovRefDetId = diaDetMovRefDetId;
    }

    public String getUsrSistema() {
        return usrSistema;
    }

    public void setUsrSistema(String usrSistema) {
        this.usrSistema = usrSistema;
    }

    public Date getFecSistema() {
        return fecSistema;
    }

    public void setFecSistema(Date fecSistema) {
        this.fecSistema = fecSistema;
    }

    public String getHrsSistema() {
        return hrsSistema;
    }

    public void setHrsSistema(String hrsSistema) {
        this.hrsSistema = hrsSistema;
    }

    public Integer getUp() {
        return up;
    }

    public void setUp(Integer up) {
        this.up = up;
    }

    public String getDiaDetSerieDoc() {
        return diaDetSerieDoc;
    }

    public void setDiaDetSerieDoc(String diaDetSerieDoc) {
        this.diaDetSerieDoc = diaDetSerieDoc;
    }

    public String getMonedaId() {
        return monedaId;
    }

    public void setMonedaId(String monedaId) {
        this.monedaId = monedaId;
    }

    public Date getFecsistema2() {
        return fecsistema2;
    }

    public void setFecsistema2(Date fecsistema2) {
        this.fecsistema2 = fecsistema2;
    }

    public Date getFecsistema3() {
        return fecsistema3;
    }

    public void setFecsistema3(Date fecsistema3) {
        this.fecsistema3 = fecsistema3;
    }

    public CuentaCorriente getCtaCteId() {
        return ctaCteId;
    }

    public void setCtaCteId(CuentaCorriente ctaCteId) {
        this.ctaCteId = ctaCteId;
    }

    public DiarioCabecera getDiarioCabecera() {
        return diarioCabecera;
    }

    public void setDiarioCabecera(DiarioCabecera diarioCabecera) {
        this.diarioCabecera = diarioCabecera;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (diarioDetallePK != null ? diarioDetallePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DiarioDetalle)) {
            return false;
        }
        DiarioDetalle other = (DiarioDetalle) object;
        if ((this.diarioDetallePK == null && other.diarioDetallePK != null) || (this.diarioDetallePK != null && !this.diarioDetallePK.equals(other.diarioDetallePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.conta.DiarioDetalle[ diarioDetallePK=" + diarioDetallePK + " ]";
    }
    
}
