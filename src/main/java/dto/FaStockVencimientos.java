/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

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
@Table(name = "fa_stock_vencimientos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FaStockVencimientos.findAll", query = "SELECT f FROM FaStockVencimientos f"),
    @NamedQuery(name = "FaStockVencimientos.findByCodalm", query = "SELECT f FROM FaStockVencimientos f WHERE f.faStockVencimientosPK.codalm = :codalm"),
    @NamedQuery(name = "FaStockVencimientos.findByCodpro", query = "SELECT f FROM FaStockVencimientos f WHERE f.faStockVencimientosPK.codpro = :codpro"),
    @NamedQuery(name = "FaStockVencimientos.findByCodlot", query = "SELECT f FROM FaStockVencimientos f WHERE f.faStockVencimientosPK.codlot = :codlot"),
    @NamedQuery(name = "FaStockVencimientos.findByFecven", query = "SELECT f FROM FaStockVencimientos f WHERE f.fecven = :fecven"),
    @NamedQuery(name = "FaStockVencimientos.findByQtymov", query = "SELECT f FROM FaStockVencimientos f WHERE f.qtymov = :qtymov"),
    @NamedQuery(name = "FaStockVencimientos.findByQtymovM", query = "SELECT f FROM FaStockVencimientos f WHERE f.qtymovM = :qtymovM"),
    @NamedQuery(name = "FaStockVencimientos.findByFecmov", query = "SELECT f FROM FaStockVencimientos f WHERE f.fecmov = :fecmov"),
    @NamedQuery(name = "FaStockVencimientos.findByNumdoc", query = "SELECT f FROM FaStockVencimientos f WHERE f.numdoc = :numdoc"),
    @NamedQuery(name = "FaStockVencimientos.findByFumlot", query = "SELECT f FROM FaStockVencimientos f WHERE f.fumlot = :fumlot"),
    @NamedQuery(name = "FaStockVencimientos.findByStkhis", query = "SELECT f FROM FaStockVencimientos f WHERE f.stkhis = :stkhis"),
    @NamedQuery(name = "FaStockVencimientos.findByStkhisM", query = "SELECT f FROM FaStockVencimientos f WHERE f.stkhisM = :stkhisM"),
    @NamedQuery(name = "FaStockVencimientos.findByUbicacion", query = "SELECT f FROM FaStockVencimientos f WHERE f.ubicacion = :ubicacion")})
public class FaStockVencimientos implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FaStockVencimientosPK faStockVencimientosPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecven")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecven;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qtymov")
    private int qtymov;
    @Basic(optional = false)
    @NotNull
    @Column(name = "qtymov_m")
    private int qtymovM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecmov")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecmov;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "numdoc")
    private String numdoc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fumlot")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fumlot;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stkhis")
    private int stkhis;
    @Basic(optional = false)
    @NotNull
    @Column(name = "stkhis_m")
    private int stkhisM;
    @Size(max = 50)
    @Column(name = "ubicacion")
    private String ubicacion;

    public FaStockVencimientos() {
    }

    public FaStockVencimientos(FaStockVencimientosPK faStockVencimientosPK) {
        this.faStockVencimientosPK = faStockVencimientosPK;
    }

    public FaStockVencimientos(FaStockVencimientosPK faStockVencimientosPK, Date fecven, int qtymov, int qtymovM, Date fecmov, String numdoc, Date fumlot, int stkhis, int stkhisM) {
        this.faStockVencimientosPK = faStockVencimientosPK;
        this.fecven = fecven;
        this.qtymov = qtymov;
        this.qtymovM = qtymovM;
        this.fecmov = fecmov;
        this.numdoc = numdoc;
        this.fumlot = fumlot;
        this.stkhis = stkhis;
        this.stkhisM = stkhisM;
    }

    public FaStockVencimientos(String codalm, String codpro, String codlot) {
        this.faStockVencimientosPK = new FaStockVencimientosPK(codalm, codpro, codlot);
    }

    public FaStockVencimientosPK getFaStockVencimientosPK() {
        return faStockVencimientosPK;
    }

    public void setFaStockVencimientosPK(FaStockVencimientosPK faStockVencimientosPK) {
        this.faStockVencimientosPK = faStockVencimientosPK;
    }

    public Date getFecven() {
        return fecven;
    }

    public void setFecven(Date fecven) {
        this.fecven = fecven;
    }

    public int getQtymov() {
        return qtymov;
    }

    public void setQtymov(int qtymov) {
        this.qtymov = qtymov;
    }

    public int getQtymovM() {
        return qtymovM;
    }

    public void setQtymovM(int qtymovM) {
        this.qtymovM = qtymovM;
    }

    public Date getFecmov() {
        return fecmov;
    }

    public void setFecmov(Date fecmov) {
        this.fecmov = fecmov;
    }

    public String getNumdoc() {
        return numdoc;
    }

    public void setNumdoc(String numdoc) {
        this.numdoc = numdoc;
    }

    public Date getFumlot() {
        return fumlot;
    }

    public void setFumlot(Date fumlot) {
        this.fumlot = fumlot;
    }

    public int getStkhis() {
        return stkhis;
    }

    public void setStkhis(int stkhis) {
        this.stkhis = stkhis;
    }

    public int getStkhisM() {
        return stkhisM;
    }

    public void setStkhisM(int stkhisM) {
        this.stkhisM = stkhisM;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (faStockVencimientosPK != null ? faStockVencimientosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaStockVencimientos)) {
            return false;
        }
        FaStockVencimientos other = (FaStockVencimientos) object;
        if ((this.faStockVencimientosPK == null && other.faStockVencimientosPK != null) || (this.faStockVencimientosPK != null && !this.faStockVencimientosPK.equals(other.faStockVencimientosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaStockVencimientos[ faStockVencimientosPK=" + faStockVencimientosPK + " ]";
    }
    
}
