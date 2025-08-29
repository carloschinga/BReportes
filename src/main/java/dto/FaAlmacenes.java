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
import javax.persistence.Id;
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
@Table(name = "fa_almacenes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FaAlmacenes.findAll", query = "SELECT f FROM FaAlmacenes f"),
    @NamedQuery(name = "FaAlmacenes.findAlljson", query = "SELECT f.codalm,f.desalm,f.siscod FROM FaAlmacenes f where f.estado='S'"),
    @NamedQuery(name = "FaAlmacenes.findAllcentraljson", query = "SELECT f.codalm,f.desalm,f.siscod FROM FaAlmacenes f where f.estado='S' and f.central='S'"),
    @NamedQuery(name = "FaAlmacenes.findByCodalm", query = "SELECT f FROM FaAlmacenes f WHERE f.codalm = :codalm"),
    @NamedQuery(name = "FaAlmacenes.findByDesalm", query = "SELECT f FROM FaAlmacenes f WHERE f.desalm = :desalm"),
    @NamedQuery(name = "FaAlmacenes.findByEstado", query = "SELECT f FROM FaAlmacenes f WHERE f.estado = :estado"),
    @NamedQuery(name = "FaAlmacenes.findByFeccre", query = "SELECT f FROM FaAlmacenes f WHERE f.feccre = :feccre"),
    @NamedQuery(name = "FaAlmacenes.findByFecumv", query = "SELECT f FROM FaAlmacenes f WHERE f.fecumv = :fecumv"),
    @NamedQuery(name = "FaAlmacenes.findByUsecod", query = "SELECT f FROM FaAlmacenes f WHERE f.usecod = :usecod"),
    @NamedQuery(name = "FaAlmacenes.findByUsenam", query = "SELECT f FROM FaAlmacenes f WHERE f.usenam = :usenam"),
    @NamedQuery(name = "FaAlmacenes.findByHostname", query = "SELECT f FROM FaAlmacenes f WHERE f.hostname = :hostname"),
    @NamedQuery(name = "FaAlmacenes.findBySiscod", query = "SELECT f.codalm, f.desalm FROM FaAlmacenes f WHERE f.siscod = :siscod and f.estado='S'"),
    @NamedQuery(name = "FaAlmacenes.findByValsta", query = "SELECT f FROM FaAlmacenes f WHERE f.valsta = :valsta"),
    @NamedQuery(name = "FaAlmacenes.findByTrasta", query = "SELECT f FROM FaAlmacenes f WHERE f.trasta = :trasta"),
    @NamedQuery(name = "FaAlmacenes.findByDlvalmsta", query = "SELECT f FROM FaAlmacenes f WHERE f.dlvalmsta = :dlvalmsta"),
    @NamedQuery(name = "FaAlmacenes.findByCodestabDigemid", query = "SELECT f FROM FaAlmacenes f WHERE f.codestabDigemid = :codestabDigemid"),
    @NamedQuery(name = "FaAlmacenes.findByRutaDigemid", query = "SELECT f FROM FaAlmacenes f WHERE f.rutaDigemid = :rutaDigemid"),
    @NamedQuery(name = "FaAlmacenes.findByCentral", query = "SELECT f FROM FaAlmacenes f WHERE f.central = :central")})
public class FaAlmacenes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "codalm")
    private String codalm;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "desalm")
    private String desalm;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecumv")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecumv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usecod")
    private int usecod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "usenam")
    private String usenam;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "hostname")
    private String hostname;
    @Basic(optional = false)
    @NotNull
    @Column(name = "siscod")
    private int siscod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "valsta")
    private String valsta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "trasta")
    private String trasta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "dlvalmsta")
    private String dlvalmsta;
    @Size(max = 10)
    @Column(name = "codestab_digemid")
    private String codestabDigemid;
    @Size(max = 100)
    @Column(name = "ruta_digemid")
    private String rutaDigemid;
    @Size(max = 1)
    @Column(name = "central")
    private String central;

    public FaAlmacenes() {
    }

    public FaAlmacenes(String codalm) {
        this.codalm = codalm;
    }

    public FaAlmacenes(String codalm, String desalm, String estado, Date feccre, Date fecumv, int usecod, String usenam, String hostname, int siscod, String valsta, String trasta, String dlvalmsta) {
        this.codalm = codalm;
        this.desalm = desalm;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
        this.siscod = siscod;
        this.valsta = valsta;
        this.trasta = trasta;
        this.dlvalmsta = dlvalmsta;
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
    }

    public String getDesalm() {
        return desalm;
    }

    public void setDesalm(String desalm) {
        this.desalm = desalm;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public String getUsenam() {
        return usenam;
    }

    public void setUsenam(String usenam) {
        this.usenam = usenam;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getSiscod() {
        return siscod;
    }

    public void setSiscod(int siscod) {
        this.siscod = siscod;
    }

    public String getValsta() {
        return valsta;
    }

    public void setValsta(String valsta) {
        this.valsta = valsta;
    }

    public String getTrasta() {
        return trasta;
    }

    public void setTrasta(String trasta) {
        this.trasta = trasta;
    }

    public String getDlvalmsta() {
        return dlvalmsta;
    }

    public void setDlvalmsta(String dlvalmsta) {
        this.dlvalmsta = dlvalmsta;
    }

    public String getCodestabDigemid() {
        return codestabDigemid;
    }

    public void setCodestabDigemid(String codestabDigemid) {
        this.codestabDigemid = codestabDigemid;
    }

    public String getRutaDigemid() {
        return rutaDigemid;
    }

    public void setRutaDigemid(String rutaDigemid) {
        this.rutaDigemid = rutaDigemid;
    }

    public String getCentral() {
        return central;
    }

    public void setCentral(String central) {
        this.central = central;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codalm != null ? codalm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaAlmacenes)) {
            return false;
        }
        FaAlmacenes other = (FaAlmacenes) object;
        if ((this.codalm == null && other.codalm != null) || (this.codalm != null && !this.codalm.equals(other.codalm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaAlmacenes[ codalm=" + codalm + " ]";
    }
    
}
