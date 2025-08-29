/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
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
@Table(name = "inventario_almacen")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InventarioAlmacen.findAll", query = "SELECT i FROM InventarioAlmacen i"),
    @NamedQuery(name = "InventarioAlmacen.obtenerultinvnum", query = "SELECT i.numitm FROM InventarioAlmacen i WHERE i.codinvcab = :codinvcab order by i.numitm desc"),
    @NamedQuery(name = "InventarioAlmacen.obtenerultinvnumcodinvalm", query = "SELECT i.codinvalm FROM InventarioAlmacen i WHERE i.codinvcab = :codinvcab order by i.numitm desc"),
    @NamedQuery(name = "InventarioAlmacen.findByCodinvalm", query = "SELECT i FROM InventarioAlmacen i WHERE i.codinvalm = :codinvalm"),
    @NamedQuery(name = "InventarioAlmacen.findest", query = "SELECT i.estdet FROM InventarioAlmacen i WHERE i.codinvalm = :codinvalm"),
    @NamedQuery(name = "InventarioAlmacen.findByCodinv", query = "SELECT i FROM InventarioAlmacen i WHERE i.codinv = :codinv"),
    @NamedQuery(name = "InventarioAlmacen.findByCodalm", query = "SELECT i FROM InventarioAlmacen i WHERE i.codalm = :codalm"),
    @NamedQuery(name = "InventarioAlmacen.findByFecape", query = "SELECT i FROM InventarioAlmacen i WHERE i.fecape = :fecape"),
    @NamedQuery(name = "InventarioAlmacen.findByFeccir", query = "SELECT i FROM InventarioAlmacen i WHERE i.feccir = :feccir"),
    @NamedQuery(name = "InventarioAlmacen.findByEstdet", query = "SELECT i FROM InventarioAlmacen i WHERE i.estdet = :estdet")})
public class InventarioAlmacen implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codinvalm")
    private Integer codinvalm;
    @Column(name = "codinv")
    private Integer codinv;
    @Size(max = 2)
    @Column(name = "codalm")
    private String codalm;
    @Column(name = "fecape")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecape;
    @Column(name = "feccir")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccir;
    @Size(max = 1)
    @Column(name = "estdet")
    private String estdet;
    @Column(name = "codinvcab")
    private Integer codinvcab;
    @Size(max = 1)
    @Column(name = "tipdet")
    private String tipdet;
    @Column(name = "numitm")
    private Integer numitm;
    @Size(max = 100)
    @Column(name = "desinvalm")
    private String desinvalm;

    public String getDesinvalm() {
        return desinvalm;
    }

    public void setDesinvalm(String desinvalm) {
        this.desinvalm = desinvalm;
    }

    public String getTipdet() {
        return tipdet;
    }

    public void setTipdet(String tipdet) {
        this.tipdet = tipdet;
    }

    public Integer getNumitm() {
        return numitm;
    }

    public void setNumitm(Integer numitm) {
        this.numitm = numitm;
    }

    public Integer getCodinvcab() {
        return codinvcab;
    }

    public void setCodinvcab(Integer codinvcab) {
        this.codinvcab = codinvcab;
    }

    public InventarioAlmacen() {
    }

    public InventarioAlmacen(Integer codinvalm) {
        this.codinvalm = codinvalm;
    }

    public Integer getCodinvalm() {
        return codinvalm;
    }

    public void setCodinvalm(Integer codinvalm) {
        this.codinvalm = codinvalm;
    }

    public Integer getCodinv() {
        return codinv;
    }

    public void setCodinv(Integer codinv) {
        this.codinv = codinv;
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
    }

    public Date getFecape() {
        return fecape;
    }

    public void setFecape(Date fecape) {
        this.fecape = fecape;
    }

    public Date getFeccir() {
        return feccir;
    }

    public void setFeccir(Date feccir) {
        this.feccir = feccir;
    }

    public String getEstdet() {
        return estdet;
    }

    public void setEstdet(String estdet) {
        this.estdet = estdet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codinvalm != null ? codinvalm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InventarioAlmacen)) {
            return false;
        }
        InventarioAlmacen other = (InventarioAlmacen) object;
        if ((this.codinvalm == null && other.codinvalm != null) || (this.codinvalm != null && !this.codinvalm.equals(other.codinvalm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.InventarioAlmacen[ codinvalm=" + codinvalm + " ]";
    }
    
}
