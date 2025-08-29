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
@Table(name = "almacenes_bartolito_ubicaciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AlmacenesBartolitoUbicaciones.findAll", query = "SELECT a FROM AlmacenesBartolitoUbicaciones a"),
    @NamedQuery(name = "AlmacenesBartolitoUbicaciones.findByCodubi", query = "SELECT a FROM AlmacenesBartolitoUbicaciones a WHERE a.codubi = :codubi"),
    @NamedQuery(name = "AlmacenesBartolitoUbicaciones.findByCodalmbar", query = "SELECT a FROM AlmacenesBartolitoUbicaciones a WHERE a.codalmbar = :codalmbar"),
    @NamedQuery(name = "AlmacenesBartolitoUbicaciones.findByCodigo", query = "SELECT a FROM AlmacenesBartolitoUbicaciones a WHERE a.codigo = :codigo"),
    @NamedQuery(name = "AlmacenesBartolitoUbicaciones.findByCodbar", query = "SELECT a FROM AlmacenesBartolitoUbicaciones a WHERE a.codbar = :codbar"),
    @NamedQuery(name = "AlmacenesBartolitoUbicaciones.findByRotacion", query = "SELECT a FROM AlmacenesBartolitoUbicaciones a WHERE a.rotacion = :rotacion"),
    @NamedQuery(name = "AlmacenesBartolitoUbicaciones.findByM3", query = "SELECT a FROM AlmacenesBartolitoUbicaciones a WHERE a.m3 = :m3"),
    @NamedQuery(name = "AlmacenesBartolitoUbicaciones.findByUsecod", query = "SELECT a FROM AlmacenesBartolitoUbicaciones a WHERE a.usecod = :usecod"),
    @NamedQuery(name = "AlmacenesBartolitoUbicaciones.findByFeccre", query = "SELECT a FROM AlmacenesBartolitoUbicaciones a WHERE a.feccre = :feccre"),
    @NamedQuery(name = "AlmacenesBartolitoUbicaciones.findByUsecodumv", query = "SELECT a FROM AlmacenesBartolitoUbicaciones a WHERE a.usecodumv = :usecodumv"),
    @NamedQuery(name = "AlmacenesBartolitoUbicaciones.findByFeccreumv", query = "SELECT a FROM AlmacenesBartolitoUbicaciones a WHERE a.feccreumv = :feccreumv")})
public class AlmacenesBartolitoUbicaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codubi")
    private Integer codubi;
    @Column(name = "codalmbar")
    private Integer codalmbar;
    @Size(max = 50)
    @Column(name = "codigo")
    private String codigo;
    @Size(max = 50)
    @Column(name = "codbar")
    private String codbar;
    @Size(max = 20)
    @Column(name = "rotacion")
    private String rotacion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "m3")
    private BigDecimal m3;
    @Column(name = "usecod")
    private Integer usecod;
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;
    @Column(name = "usecodumv")
    private Integer usecodumv;
    @Column(name = "feccreumv")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccreumv;

    @Size(max = 1)
    @Column(name = "estado")
    private String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public AlmacenesBartolitoUbicaciones() {
    }

    public AlmacenesBartolitoUbicaciones(Integer codubi) {
        this.codubi = codubi;
    }

    public Integer getCodubi() {
        return codubi;
    }

    public void setCodubi(Integer codubi) {
        this.codubi = codubi;
    }

    public Integer getCodalmbar() {
        return codalmbar;
    }

    public void setCodalmbar(Integer codalmbar) {
        this.codalmbar = codalmbar;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodbar() {
        return codbar;
    }

    public void setCodbar(String codbar) {
        this.codbar = codbar;
    }

    public String getRotacion() {
        return rotacion;
    }

    public void setRotacion(String rotacion) {
        this.rotacion = rotacion;
    }

    public BigDecimal getM3() {
        return m3;
    }

    public void setM3(BigDecimal m3) {
        this.m3 = m3;
    }

    public Integer getUsecod() {
        return usecod;
    }

    public void setUsecod(Integer usecod) {
        this.usecod = usecod;
    }

    public Date getFeccre() {
        return feccre;
    }

    public void setFeccre(Date feccre) {
        this.feccre = feccre;
    }

    public Integer getUsecodumv() {
        return usecodumv;
    }

    public void setUsecodumv(Integer usecodumv) {
        this.usecodumv = usecodumv;
    }

    public Date getFeccreumv() {
        return feccreumv;
    }

    public void setFeccreumv(Date feccreumv) {
        this.feccreumv = feccreumv;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codubi != null ? codubi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AlmacenesBartolitoUbicaciones)) {
            return false;
        }
        AlmacenesBartolitoUbicaciones other = (AlmacenesBartolitoUbicaciones) object;
        if ((this.codubi == null && other.codubi != null) || (this.codubi != null && !this.codubi.equals(other.codubi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.AlmacenesBartolitoUbicaciones[ codubi=" + codubi + " ]";
    }
    
}
