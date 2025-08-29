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
@Table(name = "moneda")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Moneda.findAll", query = "SELECT m FROM Moneda m"),
    @NamedQuery(name = "Moneda.findAllJSON", query = "SELECT m.moncod,m.mondes FROM Moneda m where m.estado='S' order by m.mondes desc"),
    @NamedQuery(name = "Moneda.findByMoncod", query = "SELECT m FROM Moneda m WHERE m.moncod = :moncod"),
    @NamedQuery(name = "Moneda.findByMondes", query = "SELECT m FROM Moneda m WHERE m.mondes = :mondes"),
    @NamedQuery(name = "Moneda.findByEstado", query = "SELECT m FROM Moneda m WHERE m.estado = :estado"),
    @NamedQuery(name = "Moneda.findByFeccre", query = "SELECT m FROM Moneda m WHERE m.feccre = :feccre"),
    @NamedQuery(name = "Moneda.findByFecumv", query = "SELECT m FROM Moneda m WHERE m.fecumv = :fecumv"),
    @NamedQuery(name = "Moneda.findByUsecod", query = "SELECT m FROM Moneda m WHERE m.usecod = :usecod"),
    @NamedQuery(name = "Moneda.findByUsenam", query = "SELECT m FROM Moneda m WHERE m.usenam = :usenam"),
    @NamedQuery(name = "Moneda.findByHostname", query = "SELECT m FROM Moneda m WHERE m.hostname = :hostname")})
public class Moneda implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "moncod")
    private String moncod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "mondes")
    private String mondes;
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

    public Moneda() {
    }

    public Moneda(String moncod) {
        this.moncod = moncod;
    }

    public Moneda(String moncod, String mondes, String estado, Date feccre, Date fecumv, int usecod, String usenam, String hostname) {
        this.moncod = moncod;
        this.mondes = mondes;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
    }

    public String getMoncod() {
        return moncod;
    }

    public void setMoncod(String moncod) {
        this.moncod = moncod;
    }

    public String getMondes() {
        return mondes;
    }

    public void setMondes(String mondes) {
        this.mondes = mondes;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (moncod != null ? moncod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Moneda)) {
            return false;
        }
        Moneda other = (Moneda) object;
        if ((this.moncod == null && other.moncod != null) || (this.moncod != null && !this.moncod.equals(other.moncod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Moneda[ moncod=" + moncod + " ]";
    }
    
}
