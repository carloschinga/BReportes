/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "Operaciones_Tarjeta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OperacionesTarjeta.findAll", query = "SELECT o FROM OperacionesTarjeta o"),
    @NamedQuery(name = "OperacionesTarjeta.findById", query = "SELECT o FROM OperacionesTarjeta o WHERE o.id = :id"),
    @NamedQuery(name = "OperacionesTarjeta.findByCodigo", query = "SELECT o FROM OperacionesTarjeta o WHERE o.codigo = :codigo"),
    @NamedQuery(name = "OperacionesTarjeta.findByProducto", query = "SELECT o FROM OperacionesTarjeta o WHERE o.producto = :producto"),
    @NamedQuery(name = "OperacionesTarjeta.findByTipoMov", query = "SELECT o FROM OperacionesTarjeta o WHERE o.tipoMov = :tipoMov"),
    @NamedQuery(name = "OperacionesTarjeta.findByFechaProceso", query = "SELECT o FROM OperacionesTarjeta o WHERE o.fechaProceso = :fechaProceso"),
    @NamedQuery(name = "OperacionesTarjeta.findByFechaLote", query = "SELECT o FROM OperacionesTarjeta o WHERE o.fechaLote = :fechaLote"),
    @NamedQuery(name = "OperacionesTarjeta.findByLoteManual", query = "SELECT o FROM OperacionesTarjeta o WHERE o.loteManual = :loteManual"),
    @NamedQuery(name = "OperacionesTarjeta.findByLotePos", query = "SELECT o FROM OperacionesTarjeta o WHERE o.lotePos = :lotePos"),
    @NamedQuery(name = "OperacionesTarjeta.findByTerminal", query = "SELECT o FROM OperacionesTarjeta o WHERE o.terminal = :terminal"),
    @NamedQuery(name = "OperacionesTarjeta.findByVoucher", query = "SELECT o FROM OperacionesTarjeta o WHERE o.voucher = :voucher"),
    @NamedQuery(name = "OperacionesTarjeta.findByAutorizacion", query = "SELECT o FROM OperacionesTarjeta o WHERE o.autorizacion = :autorizacion"),
    @NamedQuery(name = "OperacionesTarjeta.findByCuotas", query = "SELECT o FROM OperacionesTarjeta o WHERE o.cuotas = :cuotas"),
    @NamedQuery(name = "OperacionesTarjeta.findByTarjeta", query = "SELECT o FROM OperacionesTarjeta o WHERE o.tarjeta = :tarjeta"),
    @NamedQuery(name = "OperacionesTarjeta.findByOrigen", query = "SELECT o FROM OperacionesTarjeta o WHERE o.origen = :origen"),
    @NamedQuery(name = "OperacionesTarjeta.findByTransaccion", query = "SELECT o FROM OperacionesTarjeta o WHERE o.transaccion = :transaccion"),
    @NamedQuery(name = "OperacionesTarjeta.findByFechaConsumo", query = "SELECT o FROM OperacionesTarjeta o WHERE o.fechaConsumo = :fechaConsumo"),
    @NamedQuery(name = "OperacionesTarjeta.findByImporte", query = "SELECT o FROM OperacionesTarjeta o WHERE o.importe = :importe"),
    @NamedQuery(name = "OperacionesTarjeta.findByStatusPinpad", query = "SELECT o FROM OperacionesTarjeta o WHERE o.statusPinpad = :statusPinpad"),
    @NamedQuery(name = "OperacionesTarjeta.findByComision", query = "SELECT o FROM OperacionesTarjeta o WHERE o.comision = :comision"),
    @NamedQuery(name = "OperacionesTarjeta.findByComisionAfecta", query = "SELECT o FROM OperacionesTarjeta o WHERE o.comisionAfecta = :comisionAfecta"),
    @NamedQuery(name = "OperacionesTarjeta.findByIgv", query = "SELECT o FROM OperacionesTarjeta o WHERE o.igv = :igv"),
    @NamedQuery(name = "OperacionesTarjeta.findByNetoParcial", query = "SELECT o FROM OperacionesTarjeta o WHERE o.netoParcial = :netoParcial"),
    @NamedQuery(name = "OperacionesTarjeta.findByNetoTotal", query = "SELECT o FROM OperacionesTarjeta o WHERE o.netoTotal = :netoTotal"),
    @NamedQuery(name = "OperacionesTarjeta.findByFechaAbono", query = "SELECT o FROM OperacionesTarjeta o WHERE o.fechaAbono = :fechaAbono"),
    @NamedQuery(name = "OperacionesTarjeta.findByFechaAbono8Dig", query = "SELECT o FROM OperacionesTarjeta o WHERE o.fechaAbono8Dig = :fechaAbono8Dig"),
    @NamedQuery(name = "OperacionesTarjeta.findByObservaciones", query = "SELECT o FROM OperacionesTarjeta o WHERE o.observaciones = :observaciones"),
    @NamedQuery(name = "OperacionesTarjeta.findByTipoTarjeta", query = "SELECT o FROM OperacionesTarjeta o WHERE o.tipoTarjeta = :tipoTarjeta"),
    @NamedQuery(name = "OperacionesTarjeta.findBySerieTerminal", query = "SELECT o FROM OperacionesTarjeta o WHERE o.serieTerminal = :serieTerminal"),
    @NamedQuery(name = "OperacionesTarjeta.findByBancoEmisor", query = "SELECT o FROM OperacionesTarjeta o WHERE o.bancoEmisor = :bancoEmisor"),
    @NamedQuery(name = "OperacionesTarjeta.findByCuotasininteres", query = "SELECT o FROM OperacionesTarjeta o WHERE o.cuotasininteres = :cuotasininteres"),
    @NamedQuery(name = "OperacionesTarjeta.findByEntidad", query = "SELECT o FROM OperacionesTarjeta o WHERE o.entidad = :entidad"),
    @NamedQuery(name = "OperacionesTarjeta.findByHoraTRX", query = "SELECT o FROM OperacionesTarjeta o WHERE o.horaTRX = :horaTRX"),
    @NamedQuery(name = "OperacionesTarjeta.findByNombrecomercial", query = "SELECT o FROM OperacionesTarjeta o WHERE o.nombrecomercial = :nombrecomercial")})
public class OperacionesTarjeta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 20)
    @Column(name = "Codigo")
    private String codigo;
    @Size(max = 50)
    @Column(name = "Producto")
    private String producto;
    @Size(max = 10)
    @Column(name = "Tipo_Mov")
    private String tipoMov;
    @Column(name = "Fecha_Proceso")
    @Temporal(TemporalType.DATE)
    private Date fechaProceso;
    @Column(name = "Fecha_Lote")
    @Temporal(TemporalType.DATE)
    private Date fechaLote;
    @Size(max = 20)
    @Column(name = "Lote_Manual")
    private String loteManual;
    @Size(max = 20)
    @Column(name = "Lote_Pos")
    private String lotePos;
    @Size(max = 20)
    @Column(name = "Terminal")
    private String terminal;
    @Size(max = 20)
    @Column(name = "Voucher")
    private String voucher;
    @Size(max = 20)
    @Column(name = "Autorizacion")
    private String autorizacion;
    @Column(name = "Cuotas")
    private Integer cuotas;
    @Size(max = 20)
    @Column(name = "Tarjeta")
    private String tarjeta;
    @Size(max = 20)
    @Column(name = "Origen")
    private String origen;
    @Size(max = 20)
    @Column(name = "Transaccion")
    private String transaccion;
    @Column(name = "Fecha_Consumo")
    @Temporal(TemporalType.DATE)
    private Date fechaConsumo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Importe")
    private BigDecimal importe;
    @Size(max = 20)
    @Column(name = "Status_Pinpad")
    private String statusPinpad;
    @Column(name = "Comision")
    private BigDecimal comision;
    @Column(name = "Comision_Afecta")
    private BigDecimal comisionAfecta;
    @Column(name = "IGV")
    private BigDecimal igv;
    @Column(name = "Neto_Parcial")
    private BigDecimal netoParcial;
    @Column(name = "Neto_Total")
    private BigDecimal netoTotal;
    @Column(name = "Fecha_Abono")
    @Temporal(TemporalType.DATE)
    private Date fechaAbono;
    @Size(max = 8)
    @Column(name = "Fecha_Abono_8Dig")
    private String fechaAbono8Dig;
    @Size(max = 255)
    @Column(name = "Observaciones")
    private String observaciones;
    @Size(max = 20)
    @Column(name = "Tipo_Tarjeta")
    private String tipoTarjeta;
    @Size(max = 20)
    @Column(name = "Serie_Terminal")
    private String serieTerminal;
    @Size(max = 50)
    @Column(name = "Banco_Emisor")
    private String bancoEmisor;
    @Size(max = 10)
    @Column(name = "Cuota_sin_interes")
    private String cuotasininteres;
    @Size(max = 50)
    @Column(name = "Entidad")
    private String entidad;
    @Column(name = "Hora_TRX")
    @Temporal(TemporalType.TIME)
    private Date horaTRX;
    @Size(max = 100)
    @Column(name = "Nombre_comercial")
    private String nombrecomercial;

    public OperacionesTarjeta() {
    }

    public OperacionesTarjeta(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getTipoMov() {
        return tipoMov;
    }

    public void setTipoMov(String tipoMov) {
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

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
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

    public String getStatusPinpad() {
        return statusPinpad;
    }

    public void setStatusPinpad(String statusPinpad) {
        this.statusPinpad = statusPinpad;
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

    public String getTipoTarjeta() {
        return tipoTarjeta;
    }

    public void setTipoTarjeta(String tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }

    public String getSerieTerminal() {
        return serieTerminal;
    }

    public void setSerieTerminal(String serieTerminal) {
        this.serieTerminal = serieTerminal;
    }

    public String getBancoEmisor() {
        return bancoEmisor;
    }

    public void setBancoEmisor(String bancoEmisor) {
        this.bancoEmisor = bancoEmisor;
    }

    public String getCuotasininteres() {
        return cuotasininteres;
    }

    public void setCuotasininteres(String cuotasininteres) {
        this.cuotasininteres = cuotasininteres;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public Date getHoraTRX() {
        return horaTRX;
    }

    public void setHoraTRX(Date horaTRX) {
        this.horaTRX = horaTRX;
    }

    public String getNombrecomercial() {
        return nombrecomercial;
    }

    public void setNombrecomercial(String nombrecomercial) {
        this.nombrecomercial = nombrecomercial;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OperacionesTarjeta)) {
            return false;
        }
        OperacionesTarjeta other = (OperacionesTarjeta) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.bartolito.conta.dto.OperacionesTarjeta[ id=" + id + " ]";
    }
    
}
