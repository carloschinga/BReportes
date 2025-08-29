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
@Table(name = "fa_orden_trabajo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FaOrdenTrabajo.findAll", query = "SELECT f FROM FaOrdenTrabajo f"),
    @NamedQuery(name = "FaOrdenTrabajo.codpikmax", query = "SELECT f.faOrdenTrabajoPK.ortrcod FROM FaOrdenTrabajo f order by f.faOrdenTrabajoPK.ortrcod desc"),
    @NamedQuery(name = "FaOrdenTrabajo.findByOrtrcod", query = "SELECT f FROM FaOrdenTrabajo f WHERE f.faOrdenTrabajoPK.ortrcod = :ortrcod"),
    @NamedQuery(name = "FaOrdenTrabajo.findByInvnum", query = "SELECT f FROM FaOrdenTrabajo f WHERE f.faOrdenTrabajoPK.invnum = :invnum and f.estado='S'"),
    @NamedQuery(name = "FaOrdenTrabajo.findByUsecodortr", query = "SELECT f FROM FaOrdenTrabajo f WHERE f.usecodortr = :usecodortr"),
    @NamedQuery(name = "FaOrdenTrabajo.findByFeccre", query = "SELECT f FROM FaOrdenTrabajo f WHERE f.feccre = :feccre"),
    @NamedQuery(name = "FaOrdenTrabajo.findByFecumv", query = "SELECT f FROM FaOrdenTrabajo f WHERE f.fecumv = :fecumv"),
    @NamedQuery(name = "FaOrdenTrabajo.findByUsecod", query = "SELECT f FROM FaOrdenTrabajo f WHERE f.usecod = :usecod"),
    @NamedQuery(name = "FaOrdenTrabajo.findByEstado", query = "SELECT f FROM FaOrdenTrabajo f WHERE f.estado = :estado")})
public class FaOrdenTrabajo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FaOrdenTrabajoPK faOrdenTrabajoPK;
    @Column(name = "usecodortr")
    private Integer usecodortr;
    @Basic(optional = false)
    @NotNull
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;
    @Column(name = "fecumv")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecumv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usecod")
    private int usecod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado")
    private String estado;
    @Size(min = 1, max = 1)
    @Column(name = "completado")
    private String completado;
    @Size(min = 1, max = 1)
    @Column(name = "chkcomrep")
    private String chkcomrep;
    @Column(name = "feccom")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccom;
    @Column(name = "usecodcom")
    private int usecodcom;

    public String getChkcomrep() {
        return chkcomrep;
    }

    public void setChkcomrep(String chkcomrep) {
        this.chkcomrep = chkcomrep;
    }

    public Date getFeccom() {
        return feccom;
    }

    public void setFeccom(Date feccom) {
        this.feccom = feccom;
    }

    public int getUsecodcom() {
        return usecodcom;
    }

    public void setUsecodcom(int usecodcom) {
        this.usecodcom = usecodcom;
    }

    public String getCompletado() {
        return completado;
    }

    public void setCompletado(String completado) {
        this.completado = completado;
    }

    public FaOrdenTrabajo() {
    }

    public FaOrdenTrabajo(FaOrdenTrabajoPK faOrdenTrabajoPK) {
        this.faOrdenTrabajoPK = faOrdenTrabajoPK;
    }

    public FaOrdenTrabajo(FaOrdenTrabajoPK faOrdenTrabajoPK, Date feccre, int usecod, String estado) {
        this.faOrdenTrabajoPK = faOrdenTrabajoPK;
        this.feccre = feccre;
        this.usecod = usecod;
        this.estado = estado;
    }

    public FaOrdenTrabajo(int ortrcod, int invnum) {
        this.faOrdenTrabajoPK = new FaOrdenTrabajoPK(ortrcod, invnum);
    }

    public FaOrdenTrabajoPK getFaOrdenTrabajoPK() {
        return faOrdenTrabajoPK;
    }

    public void setFaOrdenTrabajoPK(FaOrdenTrabajoPK faOrdenTrabajoPK) {
        this.faOrdenTrabajoPK = faOrdenTrabajoPK;
    }

    public Integer getUsecodortr() {
        return usecodortr;
    }

    public void setUsecodortr(Integer usecodortr) {
        this.usecodortr = usecodortr;
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

    public int getUsecod() {
        return usecod;
    }

    public void setUsecod(int usecod) {
        this.usecod = usecod;
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
        hash += (faOrdenTrabajoPK != null ? faOrdenTrabajoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaOrdenTrabajo)) {
            return false;
        }
        FaOrdenTrabajo other = (FaOrdenTrabajo) object;
        if ((this.faOrdenTrabajoPK == null && other.faOrdenTrabajoPK != null) || (this.faOrdenTrabajoPK != null && !this.faOrdenTrabajoPK.equals(other.faOrdenTrabajoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaOrdenTrabajo[ faOrdenTrabajoPK=" + faOrdenTrabajoPK + " ]";
    }
    
}
