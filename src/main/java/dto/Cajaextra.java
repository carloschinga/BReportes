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
@Table(name = "cajaextra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cajaextra.findAll", query = "SELECT c FROM Cajaextra c"),
    @NamedQuery(name = "Cajaextra.findByCaja", query = "SELECT c FROM Cajaextra c WHERE c.cajaextraPK.caja = :caja"),
    @NamedQuery(name = "Cajaextra.findByCodpro", query = "SELECT c FROM Cajaextra c WHERE c.cajaextraPK.codpro = :codpro"),
    @NamedQuery(name = "Cajaextra.findByCante", query = "SELECT c FROM Cajaextra c WHERE c.cante = :cante"),
    @NamedQuery(name = "Cajaextra.findByCantf", query = "SELECT c FROM Cajaextra c WHERE c.cantf = :cantf"),
    @NamedQuery(name = "Cajaextra.findByFeccre", query = "SELECT c FROM Cajaextra c WHERE c.feccre = :feccre"),
    @NamedQuery(name = "Cajaextra.findByFecumv", query = "SELECT c FROM Cajaextra c WHERE c.fecumv = :fecumv"),
    @NamedQuery(name = "Cajaextra.findByEstado", query = "SELECT c FROM Cajaextra c WHERE c.estado = :estado")})
public class Cajaextra implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CajaextraPK cajaextraPK;
    @Column(name = "cante")
    private Integer cante;
    @Column(name = "cantf")
    private Integer cantf;
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;
    @Column(name = "fecumv")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecumv;
    @Size(max = 1)
    @Column(name = "estado")
    private String estado;
    @Column(name = "usecod")
    private Integer usecod;
    @Column(name = "orden")
    private Integer orden;
    @Column(name = "siscod")
    private Integer siscod;

    public Integer getSiscod() {
        return siscod;
    }

    public void setSiscod(Integer siscod) {
        this.siscod = siscod;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Integer getUsecod() {
        return usecod;
    }

    public void setUsecod(Integer usecod) {
        this.usecod = usecod;
    }

    public Cajaextra() {
    }

    public Cajaextra(CajaextraPK cajaextraPK) {
        this.cajaextraPK = cajaextraPK;
    }

    public Cajaextra(String caja, String codpro) {
        this.cajaextraPK = new CajaextraPK(caja, codpro);
    }

    public CajaextraPK getCajaextraPK() {
        return cajaextraPK;
    }

    public void setCajaextraPK(CajaextraPK cajaextraPK) {
        this.cajaextraPK = cajaextraPK;
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

    public Date getFeccre() {
        return feccre;
    }

    public void setFeccre(Date feccre) {
        this.feccre = feccre;
    }

    public Date getFecumv() {
        return fecumv;
    }

    public void setFecumv(Date fecumv) {
        this.fecumv = fecumv;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cajaextraPK != null ? cajaextraPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cajaextra)) {
            return false;
        }
        Cajaextra other = (Cajaextra) object;
        if ((this.cajaextraPK == null && other.cajaextraPK != null) || (this.cajaextraPK != null && !this.cajaextraPK.equals(other.cajaextraPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Cajaextra[ cajaextraPK=" + cajaextraPK + " ]";
    }
    
}
