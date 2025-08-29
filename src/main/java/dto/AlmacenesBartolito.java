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
@Table(name = "almacenes_bartolito")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AlmacenesBartolito.findAll", query = "SELECT a FROM AlmacenesBartolito a"),
    @NamedQuery(name = "AlmacenesBartolito.findByCodalmbar", query = "SELECT a FROM AlmacenesBartolito a WHERE a.codalmbar = :codalmbar"),
    @NamedQuery(name = "AlmacenesBartolito.findByDescripcion", query = "SELECT a FROM AlmacenesBartolito a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "AlmacenesBartolito.findByCodalm", query = "SELECT a FROM AlmacenesBartolito a WHERE a.codalm = :codalm"),
    @NamedQuery(name = "AlmacenesBartolito.findByM3", query = "SELECT a FROM AlmacenesBartolito a WHERE a.m3 = :m3"),
    @NamedQuery(name = "AlmacenesBartolito.findByNum", query = "SELECT a FROM AlmacenesBartolito a WHERE a.num = :num"),
    @NamedQuery(name = "AlmacenesBartolito.findByUsecod", query = "SELECT a FROM AlmacenesBartolito a WHERE a.usecod = :usecod"),
    @NamedQuery(name = "AlmacenesBartolito.findByFeccre", query = "SELECT a FROM AlmacenesBartolito a WHERE a.feccre = :feccre"),
    @NamedQuery(name = "AlmacenesBartolito.findByUsecodumv", query = "SELECT a FROM AlmacenesBartolito a WHERE a.usecodumv = :usecodumv"),
    @NamedQuery(name = "AlmacenesBartolito.findByFeccreumv", query = "SELECT a FROM AlmacenesBartolito a WHERE a.feccreumv = :feccreumv")})
public class AlmacenesBartolito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codalmbar")
    private Integer codalmbar;
    @Size(max = 150)
    @Column(name = "descripcion")
    private String descripcion;
    @Size(max = 2)
    @Column(name = "codalm")
    private String codalm;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "m3")
    private BigDecimal m3;
    @Column(name = "num")
    private Integer num;
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

    public AlmacenesBartolito() {
    }

    public AlmacenesBartolito(Integer codalmbar) {
        this.codalmbar = codalmbar;
    }

    public Integer getCodalmbar() {
        return codalmbar;
    }

    public void setCodalmbar(Integer codalmbar) {
        this.codalmbar = codalmbar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
    }

    public BigDecimal getM3() {
        return m3;
    }

    public void setM3(BigDecimal m3) {
        this.m3 = m3;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
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
        hash += (codalmbar != null ? codalmbar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AlmacenesBartolito)) {
            return false;
        }
        AlmacenesBartolito other = (AlmacenesBartolito) object;
        if ((this.codalmbar == null && other.codalmbar != null) || (this.codalmbar != null && !this.codalmbar.equals(other.codalmbar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.AlmacenesBartolito[ codalmbar=" + codalmbar + " ]";
    }
    
}
