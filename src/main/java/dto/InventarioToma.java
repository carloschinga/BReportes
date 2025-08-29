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
@Table(name = "inventario_toma")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InventarioToma.findAll", query = "SELECT i FROM InventarioToma i"),
    @NamedQuery(name = "InventarioToma.findByCodtom", query = "SELECT i FROM InventarioToma i WHERE i.codtom = :codtom"),
    @NamedQuery(name = "InventarioToma.findByCoddeta", query = "SELECT i FROM InventarioToma i WHERE i.coddeta = :coddeta"),
    @NamedQuery(name = "InventarioToma.findByCodinvalm", query = "SELECT i FROM InventarioToma i WHERE i.codinvalm = :codinvalm"),
    @NamedQuery(name = "InventarioToma.findcodtom", query = "SELECT i.codtom FROM InventarioToma i WHERE i.codinvalm = :codinvalm and i.codpro = :codpro and i.lote = :lote and i.tiptom='A'"),
    @NamedQuery(name = "InventarioToma.findByCodpro", query = "SELECT i FROM InventarioToma i WHERE i.codpro = :codpro"),
    @NamedQuery(name = "InventarioToma.findByLote", query = "SELECT i FROM InventarioToma i WHERE i.lote = :lote"),
    @NamedQuery(name = "InventarioToma.findByStkalm", query = "SELECT i FROM InventarioToma i WHERE i.stkalm = :stkalm"),
    @NamedQuery(name = "InventarioToma.findByStkalmM", query = "SELECT i FROM InventarioToma i WHERE i.stkalmM = :stkalmM"),
    @NamedQuery(name = "InventarioToma.findByTome", query = "SELECT i FROM InventarioToma i WHERE i.tome = :tome"),
    @NamedQuery(name = "InventarioToma.findByTomf", query = "SELECT i FROM InventarioToma i WHERE i.tomf = :tomf"),
    @NamedQuery(name = "InventarioToma.findByFeccre", query = "SELECT i FROM InventarioToma i WHERE i.feccre = :feccre"),
    @NamedQuery(name = "InventarioToma.findByUsecodcree", query = "SELECT i FROM InventarioToma i WHERE i.usecodcree = :usecodcree"),
    @NamedQuery(name = "InventarioToma.findByFecmod", query = "SELECT i FROM InventarioToma i WHERE i.fecmod = :fecmod"),
    @NamedQuery(name = "InventarioToma.findByUsecodmod", query = "SELECT i FROM InventarioToma i WHERE i.usecodmod = :usecodmod")})
public class InventarioToma implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codtom")
    private Integer codtom;
    @Column(name = "coddeta")
    private Integer coddeta;
    @Column(name = "codinvalm")
    private Integer codinvalm;
    @Size(max = 5)
    @Column(name = "codpro")
    private String codpro;
    @Size(max = 15)
    @Column(name = "lote")
    private String lote;
    @Column(name = "stkalm")
    private Integer stkalm;
    @Column(name = "stkalm_m")
    private Integer stkalmM;
    @Column(name = "tome")
    private Integer tome;
    @Column(name = "tomf")
    private Integer tomf;
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;
    @Column(name = "usecodcree")
    private Integer usecodcree;
    @Column(name = "fecmod")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecmod;
    @Column(name = "usecodmod")
    private Integer usecodmod;
    @Column(name = "usecodinvcree")
    private Integer usecodinvcree;
    @Size(max = 1)
    @Column(name = "tiptom")
    private String tiptom;

    public Integer getUsecodinvcree() {
        return usecodinvcree;
    }

    public void setUsecodinvcree(Integer usecodinvcree) {
        this.usecodinvcree = usecodinvcree;
    }

    public String getTiptom() {
        return tiptom;
    }

    public void setTiptom(String tiptom) {
        this.tiptom = tiptom;
    }

    public InventarioToma() {
    }

    public InventarioToma(Integer codtom) {
        this.codtom = codtom;
    }

    public Integer getCodtom() {
        return codtom;
    }

    public void setCodtom(Integer codtom) {
        this.codtom = codtom;
    }

    public Integer getCoddeta() {
        return coddeta;
    }

    public void setCoddeta(Integer coddeta) {
        this.coddeta = coddeta;
    }

    public Integer getCodinvalm() {
        return codinvalm;
    }

    public void setCodinvalm(Integer codinvalm) {
        this.codinvalm = codinvalm;
    }

    public String getCodpro() {
        return codpro;
    }

    public void setCodpro(String codpro) {
        this.codpro = codpro;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Integer getStkalm() {
        return stkalm;
    }

    public void setStkalm(Integer stkalm) {
        this.stkalm = stkalm;
    }

    public Integer getStkalmM() {
        return stkalmM;
    }

    public void setStkalmM(Integer stkalmM) {
        this.stkalmM = stkalmM;
    }

    public Integer getTome() {
        return tome;
    }

    public void setTome(Integer tome) {
        this.tome = tome;
    }

    public Integer getTomf() {
        return tomf;
    }

    public void setTomf(Integer tomf) {
        this.tomf = tomf;
    }

    public Date getFeccre() {
        return feccre;
    }

    public void setFeccre(Date feccre) {
        this.feccre = feccre;
    }

    public Integer getUsecodcree() {
        return usecodcree;
    }

    public void setUsecodcree(Integer usecodcree) {
        this.usecodcree = usecodcree;
    }

    public Date getFecmod() {
        return fecmod;
    }

    public void setFecmod(Date fecmod) {
        this.fecmod = fecmod;
    }

    public Integer getUsecodmod() {
        return usecodmod;
    }

    public void setUsecodmod(Integer usecodmod) {
        this.usecodmod = usecodmod;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codtom != null ? codtom.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InventarioToma)) {
            return false;
        }
        InventarioToma other = (InventarioToma) object;
        if ((this.codtom == null && other.codtom != null) || (this.codtom != null && !this.codtom.equals(other.codtom))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.InventarioToma[ codtom=" + codtom + " ]";
    }
    
}
