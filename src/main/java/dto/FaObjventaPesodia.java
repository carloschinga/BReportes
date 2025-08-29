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
@Table(name = "fa_objventa_pesodia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FaObjventaPesodia.findAll", query = "SELECT f FROM FaObjventaPesodia f"),
    @NamedQuery(name = "FaObjventaPesodia.findByCodopd", query = "SELECT f FROM FaObjventaPesodia f WHERE f.codopd = :codopd"),
    @NamedQuery(name = "FaObjventaPesodia.findByCodobj", query = "SELECT f FROM FaObjventaPesodia f WHERE f.codobj = :codobj"),
    @NamedQuery(name = "FaObjventaPesodia.findBySiscod", query = "SELECT f FROM FaObjventaPesodia f WHERE f.siscod = :siscod"),
    @NamedQuery(name = "FaObjventaPesodia.findByDia", query = "SELECT f FROM FaObjventaPesodia f WHERE f.dia = :dia"),
    @NamedQuery(name = "FaObjventaPesodia.findByNumdia", query = "SELECT f FROM FaObjventaPesodia f WHERE f.numdia = :numdia"),
    @NamedQuery(name = "FaObjventaPesodia.findByPeso", query = "SELECT f FROM FaObjventaPesodia f WHERE f.peso = :peso"),
    @NamedQuery(name = "FaObjventaPesodia.findByUnid", query = "SELECT f FROM FaObjventaPesodia f WHERE f.unid = :unid"),
    @NamedQuery(name = "FaObjventaPesodia.findByCuotmes", query = "SELECT f FROM FaObjventaPesodia f WHERE f.cuotmes = :cuotmes"),
    @NamedQuery(name = "FaObjventaPesodia.findByCuotdia", query = "SELECT f FROM FaObjventaPesodia f WHERE f.cuotdia = :cuotdia"),
    @NamedQuery(name = "FaObjventaPesodia.findByUsecre", query = "SELECT f FROM FaObjventaPesodia f WHERE f.usecre = :usecre"),
    @NamedQuery(name = "FaObjventaPesodia.findByFeccre", query = "SELECT f FROM FaObjventaPesodia f WHERE f.feccre = :feccre"),
    @NamedQuery(name = "FaObjventaPesodia.findByUsemod", query = "SELECT f FROM FaObjventaPesodia f WHERE f.usemod = :usemod"),
    @NamedQuery(name = "FaObjventaPesodia.findByFecmod", query = "SELECT f FROM FaObjventaPesodia f WHERE f.fecmod = :fecmod"),
    @NamedQuery(name = "FaObjventaPesodia.findByEliminado", query = "SELECT f FROM FaObjventaPesodia f WHERE f.eliminado = :eliminado")})
public class FaObjventaPesodia implements Serializable {

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
    @Size(max = 10)
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

    public FaObjventaPesodia() {
    }

    public FaObjventaPesodia(Integer codopd) {
        this.codopd = codopd;
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
        if (!(object instanceof FaObjventaPesodia)) {
            return false;
        }
        FaObjventaPesodia other = (FaObjventaPesodia) object;
        if ((this.codopd == null && other.codopd != null) || (this.codopd != null && !this.codopd.equals(other.codopd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaObjventaPesodia[ codopd=" + codopd + " ]";
    }
    
}
