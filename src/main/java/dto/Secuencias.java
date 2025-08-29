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
@Table(name = "secuencias")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Secuencias.findAll", query = "SELECT s FROM Secuencias s"),
    @NamedQuery(name = "Secuencias.findnumero", query = "SELECT s.tsedes FROM Secuencias s WHERE s.tsecod = :tsecod"),
    @NamedQuery(name = "Secuencias.findByTsecod", query = "SELECT s FROM Secuencias s WHERE s.tsecod = :tsecod"),
    @NamedQuery(name = "Secuencias.findByTsedes", query = "SELECT s FROM Secuencias s WHERE s.tsedes = :tsedes"),
    @NamedQuery(name = "Secuencias.findByTsedef", query = "SELECT s FROM Secuencias s WHERE s.tsedef = :tsedef"),
    @NamedQuery(name = "Secuencias.findByEstado", query = "SELECT s FROM Secuencias s WHERE s.estado = :estado"),
    @NamedQuery(name = "Secuencias.findByFeccre", query = "SELECT s FROM Secuencias s WHERE s.feccre = :feccre"),
    @NamedQuery(name = "Secuencias.findByFecumv", query = "SELECT s FROM Secuencias s WHERE s.fecumv = :fecumv"),
    @NamedQuery(name = "Secuencias.findByUsecod", query = "SELECT s FROM Secuencias s WHERE s.usecod = :usecod"),
    @NamedQuery(name = "Secuencias.findByUsenam", query = "SELECT s FROM Secuencias s WHERE s.usenam = :usenam"),
    @NamedQuery(name = "Secuencias.findByHostname", query = "SELECT s FROM Secuencias s WHERE s.hostname = :hostname")})
public class Secuencias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "tsecod")
    private String tsecod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tsedes")
    private int tsedes;
    @Size(max = 30)
    @Column(name = "tsedef")
    private String tsedef;
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

    public Secuencias() {
    }

    public Secuencias(String tsecod) {
        this.tsecod = tsecod;
    }

    public Secuencias(String tsecod, int tsedes, String estado, Date feccre, Date fecumv, int usecod, String usenam, String hostname) {
        this.tsecod = tsecod;
        this.tsedes = tsedes;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
    }

    public String getTsecod() {
        return tsecod;
    }

    public void setTsecod(String tsecod) {
        this.tsecod = tsecod;
    }

    public int getTsedes() {
        return tsedes;
    }

    public void setTsedes(int tsedes) {
        this.tsedes = tsedes;
    }

    public String getTsedef() {
        return tsedef;
    }

    public void setTsedef(String tsedef) {
        this.tsedef = tsedef;
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
        hash += (tsecod != null ? tsecod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Secuencias)) {
            return false;
        }
        Secuencias other = (Secuencias) object;
        if ((this.tsecod == null && other.tsecod != null) || (this.tsecod != null && !this.tsecod.equals(other.tsecod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Secuencias[ tsecod=" + tsecod + " ]";
    }
    
}
