/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.clinica.dto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "HechRecetasCli")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HechRecetasCli.findAll", query = "SELECT h FROM HechRecetasCli h"),
    @NamedQuery(name = "HechRecetasCli.findByOrigenAtencion", query = "SELECT h FROM HechRecetasCli h WHERE h.origenAtencion = :origenAtencion"),
    @NamedQuery(name = "HechRecetasCli.findByActoMedico", query = "SELECT h FROM HechRecetasCli h WHERE h.hechRecetasCliPK.actoMedico = :actoMedico"),
    @NamedQuery(name = "HechRecetasCli.findByPrefactura", query = "SELECT h FROM HechRecetasCli h WHERE h.prefactura = :prefactura"),
    @NamedQuery(name = "HechRecetasCli.findByFechaAtencion", query = "SELECT h FROM HechRecetasCli h WHERE h.fechaAtencion = :fechaAtencion"),
    @NamedQuery(name = "HechRecetasCli.findByCoditm", query = "SELECT h FROM HechRecetasCli h WHERE h.hechRecetasCliPK.coditm = :coditm"),
    @NamedQuery(name = "HechRecetasCli.findByCodproLolcli", query = "SELECT h FROM HechRecetasCli h WHERE h.hechRecetasCliPK.codproLolcli = :codproLolcli"),
    @NamedQuery(name = "HechRecetasCli.findByCodproLolfar", query = "SELECT h FROM HechRecetasCli h WHERE h.codproLolfar = :codproLolfar"),
    @NamedQuery(name = "HechRecetasCli.findByProducto", query = "SELECT h FROM HechRecetasCli h WHERE h.producto = :producto"),
    @NamedQuery(name = "HechRecetasCli.findByCantidad", query = "SELECT h FROM HechRecetasCli h WHERE h.cantidad = :cantidad"),
    @NamedQuery(name = "HechRecetasCli.findByGenerico", query = "SELECT h FROM HechRecetasCli h WHERE h.generico = :generico"),
    @NamedQuery(name = "HechRecetasCli.findByIndicProducto", query = "SELECT h FROM HechRecetasCli h WHERE h.indicProducto = :indicProducto"),
    @NamedQuery(name = "HechRecetasCli.findByDosis", query = "SELECT h FROM HechRecetasCli h WHERE h.dosis = :dosis"),
    @NamedQuery(name = "HechRecetasCli.findByPaciente", query = "SELECT h FROM HechRecetasCli h WHERE h.paciente = :paciente"),
    @NamedQuery(name = "HechRecetasCli.findByNumdocId", query = "SELECT h FROM HechRecetasCli h WHERE h.numdocId = :numdocId"),
    @NamedQuery(name = "HechRecetasCli.findByNumHc", query = "SELECT h FROM HechRecetasCli h WHERE h.numHc = :numHc"),
    @NamedQuery(name = "HechRecetasCli.findByFechaNac", query = "SELECT h FROM HechRecetasCli h WHERE h.fechaNac = :fechaNac"),
    @NamedQuery(name = "HechRecetasCli.findByTelfPac", query = "SELECT h FROM HechRecetasCli h WHERE h.telfPac = :telfPac"),
    @NamedQuery(name = "HechRecetasCli.findByPlanAtencion", query = "SELECT h FROM HechRecetasCli h WHERE h.planAtencion = :planAtencion"),
    @NamedQuery(name = "HechRecetasCli.findByServicio", query = "SELECT h FROM HechRecetasCli h WHERE h.servicio = :servicio"),
    @NamedQuery(name = "HechRecetasCli.findByMedico", query = "SELECT h FROM HechRecetasCli h WHERE h.medico = :medico"),
    @NamedQuery(name = "HechRecetasCli.findByFechaRegistro", query = "SELECT h FROM HechRecetasCli h WHERE h.fechaRegistro = :fechaRegistro"),
    @NamedQuery(name = "HechRecetasCli.findByMedcod", query = "SELECT h FROM HechRecetasCli h WHERE h.medcod = :medcod"),
    @NamedQuery(name = "HechRecetasCli.findBySercod", query = "SELECT h FROM HechRecetasCli h WHERE h.sercod = :sercod"),
    @NamedQuery(name = "HechRecetasCli.findByCheck1", query = "SELECT h FROM HechRecetasCli h WHERE h.check1 = :check1")})
public class HechRecetasCli implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HechRecetasCliPK hechRecetasCliPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "OrigenAtencion")
    private String origenAtencion;
    @Column(name = "Prefactura")
    private Integer prefactura;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FechaAtencion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAtencion;
    @Size(max = 12)
    @Column(name = "CodproLolfar")
    private String codproLolfar;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Producto")
    private String producto;
    @Column(name = "Cantidad")
    private Integer cantidad;
    @Size(max = 40)
    @Column(name = "Generico")
    private String generico;
    @Size(max = 300)
    @Column(name = "IndicProducto")
    private String indicProducto;
    @Size(max = 50)
    @Column(name = "Dosis")
    private String dosis;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 62)
    @Column(name = "Paciente")
    private String paciente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NumdocId")
    private String numdocId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 7)
    @Column(name = "NumHc")
    private String numHc;
    @Column(name = "FechaNac")
    @Temporal(TemporalType.DATE)
    private Date fechaNac;
    @Size(max = 60)
    @Column(name = "TelfPac")
    private String telfPac;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 70)
    @Column(name = "PlanAtencion")
    private String planAtencion;
    @Size(max = 70)
    @Column(name = "Servicio")
    private String servicio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 70)
    @Column(name = "Medico")
    private String medico;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FechaRegistro")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;
    @Size(max = 10)
    @Column(name = "medcod")
    private String medcod;
    @Size(max = 10)
    @Column(name = "sercod")
    private String sercod;
    @Size(max = 1)
    @Column(name = "check1")
    private String check1;

    public HechRecetasCli() {
    }

    public HechRecetasCli(HechRecetasCliPK hechRecetasCliPK) {
        this.hechRecetasCliPK = hechRecetasCliPK;
    }

    public HechRecetasCli(HechRecetasCliPK hechRecetasCliPK, String origenAtencion, Date fechaAtencion, String producto, String paciente, String numdocId, String numHc, String planAtencion, String medico, Date fechaRegistro) {
        this.hechRecetasCliPK = hechRecetasCliPK;
        this.origenAtencion = origenAtencion;
        this.fechaAtencion = fechaAtencion;
        this.producto = producto;
        this.paciente = paciente;
        this.numdocId = numdocId;
        this.numHc = numHc;
        this.planAtencion = planAtencion;
        this.medico = medico;
        this.fechaRegistro = fechaRegistro;
    }

    public HechRecetasCli(int actoMedico, int coditm, String codproLolcli) {
        this.hechRecetasCliPK = new HechRecetasCliPK(actoMedico, coditm, codproLolcli);
    }

    public HechRecetasCliPK getHechRecetasCliPK() {
        return hechRecetasCliPK;
    }

    public void setHechRecetasCliPK(HechRecetasCliPK hechRecetasCliPK) {
        this.hechRecetasCliPK = hechRecetasCliPK;
    }

    public String getOrigenAtencion() {
        return origenAtencion;
    }

    public void setOrigenAtencion(String origenAtencion) {
        this.origenAtencion = origenAtencion;
    }

    public Integer getPrefactura() {
        return prefactura;
    }

    public void setPrefactura(Integer prefactura) {
        this.prefactura = prefactura;
    }

    public Date getFechaAtencion() {
        return fechaAtencion;
    }

    public void setFechaAtencion(Date fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }

    public String getCodproLolfar() {
        return codproLolfar;
    }

    public void setCodproLolfar(String codproLolfar) {
        this.codproLolfar = codproLolfar;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getGenerico() {
        return generico;
    }

    public void setGenerico(String generico) {
        this.generico = generico;
    }

    public String getIndicProducto() {
        return indicProducto;
    }

    public void setIndicProducto(String indicProducto) {
        this.indicProducto = indicProducto;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getNumdocId() {
        return numdocId;
    }

    public void setNumdocId(String numdocId) {
        this.numdocId = numdocId;
    }

    public String getNumHc() {
        return numHc;
    }

    public void setNumHc(String numHc) {
        this.numHc = numHc;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getTelfPac() {
        return telfPac;
    }

    public void setTelfPac(String telfPac) {
        this.telfPac = telfPac;
    }

    public String getPlanAtencion() {
        return planAtencion;
    }

    public void setPlanAtencion(String planAtencion) {
        this.planAtencion = planAtencion;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getMedcod() {
        return medcod;
    }

    public void setMedcod(String medcod) {
        this.medcod = medcod;
    }

    public String getSercod() {
        return sercod;
    }

    public void setSercod(String sercod) {
        this.sercod = sercod;
    }

    public String getCheck1() {
        return check1;
    }

    public void setCheck1(String check1) {
        this.check1 = check1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (hechRecetasCliPK != null ? hechRecetasCliPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HechRecetasCli)) {
            return false;
        }
        HechRecetasCli other = (HechRecetasCli) object;
        if ((this.hechRecetasCliPK == null && other.hechRecetasCliPK != null) || (this.hechRecetasCliPK != null && !this.hechRecetasCliPK.equals(other.hechRecetasCliPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.bartolito.clinica.dto.HechRecetasCli[ hechRecetasCliPK=" + hechRecetasCliPK + " ]";
    }
    
}
