/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "fa_cajas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FaCajas.findAll", query = "SELECT f FROM FaCajas f"),
    @NamedQuery(name = "FaCajas.findByInvnum", query = "SELECT f FROM FaCajas f WHERE f.faCajasPK.invnum = :invnum"),
    @NamedQuery(name = "FaCajas.findByNumitm", query = "SELECT f FROM FaCajas f WHERE f.faCajasPK.numitm = :numitm"),
    @NamedQuery(name = "FaCajas.findByInvnumNumitm", query = "SELECT f.faCajasPK.caja,f.cante,f.cantf FROM FaCajas f WHERE f.faCajasPK.invnum = :invnum and f.faCajasPK.numitm = :numitm"),
    @NamedQuery(name = "FaCajas.findByCaja", query = "SELECT f FROM FaCajas f WHERE f.faCajasPK.caja = :caja"),
    @NamedQuery(name = "FaCajas.findByCante", query = "SELECT f FROM FaCajas f WHERE f.cante = :cante"),
    @NamedQuery(name = "FaCajas.findByCantf", query = "SELECT f FROM FaCajas f WHERE f.cantf = :cantf"),
    @NamedQuery(name = "FaCajas.findByCheckenvio", query = "SELECT f FROM FaCajas f WHERE f.checkenvio = :checkenvio"),
    @NamedQuery(name = "FaCajas.findByUsecod", query = "SELECT f FROM FaCajas f WHERE f.usecod = :usecod"),
    @NamedQuery(name = "FaCajas.findByFecchk", query = "SELECT f FROM FaCajas f WHERE f.fecchk = :fecchk")})
public class FaCajas implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FaCajasPK faCajasPK;
    @Column(name = "cante")
    private Integer cante;
    @Column(name = "cantf")
    private Integer cantf;
    @Size(max = 1)
    @Column(name = "checkenvio")
    private String checkenvio;
    @Column(name = "usecod")
    private Integer usecod;
    @Column(name = "fecchk")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecchk;
    @Column(name = "canter")
    private Integer canter;
    @Column(name = "cantfr")
    private Integer cantfr;
    @Column(name = "fecinirecep")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecinirecep;
    @Column(name = "fecfinrecep")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecfinrecep;

    public Date getFecinirecep() {
        return fecinirecep;
    }

    public void setFecinirecep(Date fecinirecep) {
        this.fecinirecep = fecinirecep;
    }

    public Date getFecfinrecep() {
        return fecfinrecep;
    }

    public void setFecfinrecep(Date fecfinrecep) {
        this.fecfinrecep = fecfinrecep;
    }

    public Integer getCanter() {
        return canter;
    }

    public void setCanter(Integer canter) {
        this.canter = canter;
    }

    public Integer getCantfr() {
        return cantfr;
    }

    public void setCantfr(Integer cantfr) {
        this.cantfr = cantfr;
    }

    public FaCajas() {
    }

    public FaCajas(FaCajasPK faCajasPK) {
        this.faCajasPK = faCajasPK;
    }

    public FaCajas(int invnum, int numitm, String caja) {
        this.faCajasPK = new FaCajasPK(invnum, numitm, caja);
    }

    public FaCajasPK getFaCajasPK() {
        return faCajasPK;
    }

    public void setFaCajasPK(FaCajasPK faCajasPK) {
        this.faCajasPK = faCajasPK;
    }

    public Integer getCante() {
        return cante;
    }

    public void setCante(Integer cante) {
        this.cante = cante;
    }

    public Integer getCantf() {
        return cantf;
    }

    public void setCantf(Integer cantf) {
        this.cantf = cantf;
    }

    public String getCheckenvio() {
        return checkenvio;
    }

    public void setCheckenvio(String checkenvio) {
        this.checkenvio = checkenvio;
    }

    public Integer getUsecod() {
        return usecod;
    }

    public void setUsecod(Integer usecod) {
        this.usecod = usecod;
    }

    public Date getFecchk() {
        return fecchk;
    }

    public void setFecchk(Date fecchk) {
        this.fecchk = fecchk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (faCajasPK != null ? faCajasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaCajas)) {
            return false;
        }
        FaCajas other = (FaCajas) object;
        if ((this.faCajasPK == null && other.faCajasPK != null) || (this.faCajasPK != null && !this.faCajasPK.equals(other.faCajasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaCajas[ faCajasPK=" + faCajasPK + " ]";
    }
    
}
