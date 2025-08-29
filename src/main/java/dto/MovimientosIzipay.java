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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "MovimientosIzipay")
@NamedQueries({
    @NamedQuery(name = "MovimientosIzipay.findAll", query = "SELECT m FROM MovimientosIzipay m"),
    @NamedQuery(name = "MovimientosIzipay.findByCodigo", query = "SELECT m FROM MovimientosIzipay m WHERE m.movimientosIzipayPK.codigo = :codigo"),
    @NamedQuery(name = "MovimientosIzipay.findByProducto", query = "SELECT m FROM MovimientosIzipay m WHERE m.producto = :producto"),
    @NamedQuery(name = "MovimientosIzipay.findByTipoMov", query = "SELECT m FROM MovimientosIzipay m WHERE m.tipoMov = :tipoMov"),
    @NamedQuery(name = "MovimientosIzipay.findByFechaProceso", query = "SELECT m FROM MovimientosIzipay m WHERE m.fechaProceso = :fechaProceso"),
    @NamedQuery(name = "MovimientosIzipay.findByFechaLote", query = "SELECT m FROM MovimientosIzipay m WHERE m.fechaLote = :fechaLote"),
    @NamedQuery(name = "MovimientosIzipay.findByLoteManual", query = "SELECT m FROM MovimientosIzipay m WHERE m.loteManual = :loteManual"),
    @NamedQuery(name = "MovimientosIzipay.findByLotePos", query = "SELECT m FROM MovimientosIzipay m WHERE m.lotePos = :lotePos"),
    @NamedQuery(name = "MovimientosIzipay.findByTerminal", query = "SELECT m FROM MovimientosIzipay m WHERE m.terminal = :terminal"),
    @NamedQuery(name = "MovimientosIzipay.findByVoucher", query = "SELECT m FROM MovimientosIzipay m WHERE m.movimientosIzipayPK.voucher = :voucher"),
    @NamedQuery(name = "MovimientosIzipay.findByAutorizacion", query = "SELECT m FROM MovimientosIzipay m WHERE m.autorizacion = :autorizacion"),
    @NamedQuery(name = "MovimientosIzipay.findByCuotas", query = "SELECT m FROM MovimientosIzipay m WHERE m.cuotas = :cuotas"),
    @NamedQuery(name = "MovimientosIzipay.findByTarjeta", query = "SELECT m FROM MovimientosIzipay m WHERE m.tarjeta = :tarjeta"),
    @NamedQuery(name = "MovimientosIzipay.findByOrigen", query = "SELECT m FROM MovimientosIzipay m WHERE m.origen = :origen"),
    @NamedQuery(name = "MovimientosIzipay.findByTransaccion", query = "SELECT m FROM MovimientosIzipay m WHERE m.transaccion = :transaccion"),
    @NamedQuery(name = "MovimientosIzipay.findByFechaConsumo", query = "SELECT m FROM MovimientosIzipay m WHERE m.fechaConsumo = :fechaConsumo"),
    @NamedQuery(name = "MovimientosIzipay.findByImporte", query = "SELECT m FROM MovimientosIzipay m WHERE m.importe = :importe"),
    @NamedQuery(name = "MovimientosIzipay.findByStatus", query = "SELECT m FROM MovimientosIzipay m WHERE m.status = :status"),
    @NamedQuery(name = "MovimientosIzipay.findByComision", query = "SELECT m FROM MovimientosIzipay m WHERE m.comision = :comision"),
    @NamedQuery(name = "MovimientosIzipay.findByComisionAfecta", query = "SELECT m FROM MovimientosIzipay m WHERE m.comisionAfecta = :comisionAfecta"),
    @NamedQuery(name = "MovimientosIzipay.findByIgv", query = "SELECT m FROM MovimientosIzipay m WHERE m.igv = :igv"),
    @NamedQuery(name = "MovimientosIzipay.findByNetoParcial", query = "SELECT m FROM MovimientosIzipay m WHERE m.netoParcial = :netoParcial"),
    @NamedQuery(name = "MovimientosIzipay.findByNetoTotal", query = "SELECT m FROM MovimientosIzipay m WHERE m.netoTotal = :netoTotal"),
    @NamedQuery(name = "MovimientosIzipay.findByFechaAbono", query = "SELECT m FROM MovimientosIzipay m WHERE m.fechaAbono = :fechaAbono"),
    @NamedQuery(name = "MovimientosIzipay.findByFechaAbono8Dig", query = "SELECT m FROM MovimientosIzipay m WHERE m.fechaAbono8Dig = :fechaAbono8Dig")})
public class MovimientosIzipay implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MovimientosIzipayPK movimientosIzipayPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Producto")
    private String producto;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Tipo_Mov")
    private Character tipoMov;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Fecha_Proceso")
    @Temporal(TemporalType.DATE)
    private Date fechaProceso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Fecha_Lote")
    @Temporal(TemporalType.DATE)
    private Date fechaLote;
    @Size(max = 50)
    @Column(name = "Lote_Manual")
    private String loteManual;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Lote_Pos")
    private String lotePos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Terminal")
    private String terminal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Autorizacion")
    private String autorizacion;
    @Column(name = "Cuotas")
    private Integer cuotas;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Tarjeta")
    private String tarjeta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Origen")
    private String origen;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Transaccion")
    private String transaccion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Fecha_Consumo")
    @Temporal(TemporalType.DATE)
    private Date fechaConsumo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "Importe")
    private BigDecimal importe;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Status")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Comision")
    private BigDecimal comision;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Comision_Afecta")
    private BigDecimal comisionAfecta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "IGV")
    private BigDecimal igv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Neto_Parcial")
    private BigDecimal netoParcial;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Neto_Total")
    private BigDecimal netoTotal;
    @Column(name = "Fecha_Abono")
    @Temporal(TemporalType.DATE)
    private Date fechaAbono;
    @Size(max = 8)
    @Column(name = "Fecha_Abono_8Dig")
    private String fechaAbono8Dig;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "Observaciones")
    private String observaciones;
    @Size(min = 1, max = 1)
    @Column(name = "estado")
    private String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public MovimientosIzipay() {
    }

    public MovimientosIzipay(MovimientosIzipayPK movimientosIzipayPK) {
        this.movimientosIzipayPK = movimientosIzipayPK;
    }

    public MovimientosIzipay(MovimientosIzipayPK movimientosIzipayPK, String producto, Character tipoMov, Date fechaProceso, Date fechaLote, String lotePos, String terminal, String autorizacion, String tarjeta, String origen, String transaccion, Date fechaConsumo, BigDecimal importe, String status, BigDecimal comision, BigDecimal comisionAfecta, BigDecimal igv, BigDecimal netoParcial, BigDecimal netoTotal) {
        this.movimientosIzipayPK = movimientosIzipayPK;
        this.producto = producto;
        this.tipoMov = tipoMov;
        this.fechaProceso = fechaProceso;
        this.fechaLote = fechaLote;
        this.lotePos = lotePos;
        this.terminal = terminal;
        this.autorizacion = autorizacion;
        this.tarjeta = tarjeta;
        this.origen = origen;
        this.transaccion = transaccion;
        this.fechaConsumo = fechaConsumo;
        this.importe = importe;
        this.status = status;
        this.comision = comision;
        this.comisionAfecta = comisionAfecta;
        this.igv = igv;
        this.netoParcial = netoParcial;
        this.netoTotal = netoTotal;
    }

    public MovimientosIzipay(String codigo, String voucher) {
        this.movimientosIzipayPK = new MovimientosIzipayPK(codigo, voucher);
    }

    public MovimientosIzipayPK getMovimientosIzipayPK() {
        return movimientosIzipayPK;
    }

    public void setMovimientosIzipayPK(MovimientosIzipayPK movimientosIzipayPK) {
        this.movimientosIzipayPK = movimientosIzipayPK;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Character getTipoMov() {
        return tipoMov;
    }

    public void setTipoMov(Character tipoMov) {
        this.tipoMov = tipoMov;
    }

    public Date getFechaProceso() {
        return fechaProceso;
    }

    public void setFechaProceso(Date fechaProceso) {
        this.fechaProceso = fechaProceso;
    }

    public Date getFechaLote() {
        return fechaLote;
    }

    public void setFechaLote(Date fechaLote) {
        this.fechaLote = fechaLote;
    }

    public String getLoteManual() {
        return loteManual;
    }

    public void setLoteManual(String loteManual) {
        this.loteManual = loteManual;
    }

    public String getLotePos() {
        return lotePos;
    }

    public void setLotePos(String lotePos) {
        this.lotePos = lotePos;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getAutorizacion() {
        return autorizacion;
    }

    public void setAutorizacion(String autorizacion) {
        this.autorizacion = autorizacion;
    }

    public Integer getCuotas() {
        return cuotas;
    }

    public void setCuotas(Integer cuotas) {
        this.cuotas = cuotas;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(String transaccion) {
        this.transaccion = transaccion;
    }

    public Date getFechaConsumo() {
        return fechaConsumo;
    }

    public void setFechaConsumo(Date fechaConsumo) {
        this.fechaConsumo = fechaConsumo;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getComision() {
        return comision;
    }

    public void setComision(BigDecimal comision) {
        this.comision = comision;
    }

    public BigDecimal getComisionAfecta() {
        return comisionAfecta;
    }

    public void setComisionAfecta(BigDecimal comisionAfecta) {
        this.comisionAfecta = comisionAfecta;
    }

    public BigDecimal getIgv() {
        return igv;
    }

    public void setIgv(BigDecimal igv) {
        this.igv = igv;
    }

    public BigDecimal getNetoParcial() {
        return netoParcial;
    }

    public void setNetoParcial(BigDecimal netoParcial) {
        this.netoParcial = netoParcial;
    }

    public BigDecimal getNetoTotal() {
        return netoTotal;
    }

    public void setNetoTotal(BigDecimal netoTotal) {
        this.netoTotal = netoTotal;
    }

    public Date getFechaAbono() {
        return fechaAbono;
    }

    public void setFechaAbono(Date fechaAbono) {
        this.fechaAbono = fechaAbono;
    }

    public String getFechaAbono8Dig() {
        return fechaAbono8Dig;
    }

    public void setFechaAbono8Dig(String fechaAbono8Dig) {
        this.fechaAbono8Dig = fechaAbono8Dig;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (movimientosIzipayPK != null ? movimientosIzipayPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovimientosIzipay)) {
            return false;
        }
        MovimientosIzipay other = (MovimientosIzipay) object;
        if ((this.movimientosIzipayPK == null && other.movimientosIzipayPK != null) || (this.movimientosIzipayPK != null && !this.movimientosIzipayPK.equals(other.movimientosIzipayPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.MovimientosIzipay[ movimientosIzipayPK=" + movimientosIzipayPK + " ]";
    }
    
}
