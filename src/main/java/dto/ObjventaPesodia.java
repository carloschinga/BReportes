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
 * @author sbdeveloperw
 */
@Entity
@Table(name = "objventa_pesodia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ObjventaPesodia.findAll", query = "SELECT o FROM ObjventaPesodia o"),
    @NamedQuery(name = "ObjventaPesodia.findByCodopd", query = "SELECT o FROM ObjventaPesodia o WHERE o.codopd = :codopd"),
    @NamedQuery(name = "ObjventaPesodia.findByCodobj", query = "SELECT o FROM ObjventaPesodia o WHERE o.codobj = :codobj"),
    @NamedQuery(name = "ObjventaPesodia.findBySiscod", query = "SELECT o FROM ObjventaPesodia o WHERE o.siscod = :siscod"),
    @NamedQuery(name = "ObjventaPesodia.findByDia", query = "SELECT o FROM ObjventaPesodia o WHERE o.dia = :dia"),
    @NamedQuery(name = "ObjventaPesodia.findByNumdia", query = "SELECT o FROM ObjventaPesodia o WHERE o.numdia = :numdia"),
    @NamedQuery(name = "ObjventaPesodia.findByPeso", query = "SELECT o FROM ObjventaPesodia o WHERE o.peso = :peso"),
    @NamedQuery(name = "ObjventaPesodia.findByUnid", query = "SELECT o FROM ObjventaPesodia o WHERE o.unid = :unid"),
    @NamedQuery(name = "ObjventaPesodia.findByCuotmes", query = "SELECT o FROM ObjventaPesodia o WHERE o.cuotmes = :cuotmes"),
    @NamedQuery(name = "ObjventaPesodia.findByCuotdia", query = "SELECT o FROM ObjventaPesodia o WHERE o.cuotdia = :cuotdia"),
    @NamedQuery(name = "ObjventaPesodia.findByUsecre", query = "SELECT o FROM ObjventaPesodia o WHERE o.usecre = :usecre"),
    @NamedQuery(name = "ObjventaPesodia.findByFeccre", query = "SELECT o FROM ObjventaPesodia o WHERE o.feccre = :feccre"),
    @NamedQuery(name = "ObjventaPesodia.findByUsemod", query = "SELECT o FROM ObjventaPesodia o WHERE o.usemod = :usemod"),
    @NamedQuery(name = "ObjventaPesodia.findByFecmod", query = "SELECT o FROM ObjventaPesodia o WHERE o.fecmod = :fecmod"),
    @NamedQuery(name = "ObjventaPesodia.findByEliminado", query = "SELECT o FROM ObjventaPesodia o WHERE o.eliminado = :eliminado")})
public class ObjventaPesodia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codopd")
    private Integer codopd;
    @Column(name = "codobj")
    private Integer codobj;
    @Column(name = "siscod")
    private Integer siscod;
    @Size(max = 50)
    @Column(name = "dia")
    private String dia;
    @Column(name = "numdia")
    private Integer numdia;
    @Column(name = "peso")
    private Integer peso;
    @Column(name = "unid")
    private Integer unid;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cuotmes")
    private BigDecimal cuotmes;
    @Column(name = "cuotdia")
    private BigDecimal cuotdia;
    @Column(name = "usecre")
    private Integer usecre;
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;
    @Column(name = "usemod")
    private Integer usemod;
    @Column(name = "fecmod")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecmod;
    @Column(name = "eliminado")
    private Boolean eliminado;

    public ObjventaPesodia() {
    }

    public ObjventaPesodia(Integer codopd) {
        this.codopd = codopd;
    }
    
    public ObjventaPesodia(Integer codobj, Integer siscod) {
        this.codobj = codobj;
        this.siscod = siscod;
    } 

    public Integer getCodopd() {
        return codopd;
    }

    public void setCodopd(Integer codopd) {
        this.codopd = codopd;
    }

    public Integer getCodobj() {
        return codobj;
    }

    public void setCodobj(Integer codobj) {
        this.codobj = codobj;
    }

    public Integer getSiscod() {
        return siscod;
    }

    public void setSiscod(Integer siscod) {
        this.siscod = siscod;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public Integer getNumdia() {
        return numdia;
    }

    public void setNumdia(Integer numdia) {
        this.numdia = numdia;
    }

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    public Integer getUnid() {
        return unid;
    }

    public void setUnid(Integer unid) {
        this.unid = unid;
    }

    public BigDecimal getCuotmes() {
        return cuotmes;
    }

    public void setCuotmes(BigDecimal cuotmes) {
        this.cuotmes = cuotmes;
    }

    public BigDecimal getCuotdia() {
        return cuotdia;
    }

    public void setCuotdia(BigDecimal cuotdia) {
        this.cuotdia = cuotdia;
    }

    public Integer getUsecre() {
        return usecre;
    }

    public void setUsecre(Integer usecre) {
        this.usecre = usecre;
    }

    public Date getFeccre() {
        return feccre;
    }

    public void setFeccre(Date feccre) {
        this.feccre = feccre;
    }

    public Integer getUsemod() {
        return usemod;
    }

    public void setUsemod(Integer usemod) {
        this.usemod = usemod;
    }

    public Date getFecmod() {
        return fecmod;
    }

    public void setFecmod(Date fecmod) {
        this.fecmod = fecmod;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codopd != null ? codopd.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ObjventaPesodia)) {
            return false;
        }
        ObjventaPesodia other = (ObjventaPesodia) object;
        if ((this.codopd == null && other.codopd != null) || (this.codopd != null && !this.codopd.equals(other.codopd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.ObjventaPesodia[ codopd=" + codopd + " ]";
    }
    
}
