/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "ubigeo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ubigeo.findAll", query = "SELECT u FROM Ubigeo u"),
    @NamedQuery(name = "Ubigeo.findByUbicod", query = "SELECT u FROM Ubigeo u WHERE u.ubicod = :ubicod"),
    @NamedQuery(name = "Ubigeo.findByUbides", query = "SELECT u FROM Ubigeo u WHERE u.ubides = :ubides"),
    @NamedQuery(name = "Ubigeo.findByEstado", query = "SELECT u FROM Ubigeo u WHERE u.estado = :estado"),
    @NamedQuery(name = "Ubigeo.findByFeccre", query = "SELECT u FROM Ubigeo u WHERE u.feccre = :feccre"),
    @NamedQuery(name = "Ubigeo.findByFecumv", query = "SELECT u FROM Ubigeo u WHERE u.fecumv = :fecumv"),
    @NamedQuery(name = "Ubigeo.findByUsecod", query = "SELECT u FROM Ubigeo u WHERE u.usecod = :usecod"),
    @NamedQuery(name = "Ubigeo.findByUsenam", query = "SELECT u FROM Ubigeo u WHERE u.usenam = :usenam"),
    @NamedQuery(name = "Ubigeo.findByHostname", query = "SELECT u FROM Ubigeo u WHERE u.hostname = :hostname")})
public class Ubigeo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "ubicod")
    private String ubicod;
    @Size(max = 80)
    @Column(name = "ubides")
    private String ubides;
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
    @OneToMany(mappedBy = "sisubicod")
    private Collection<Sistema> sistemaCollection;

    public Ubigeo() {
    }

    public Ubigeo(String ubicod) {
        this.ubicod = ubicod;
    }

    public Ubigeo(String ubicod, String estado, Date feccre, Date fecumv, int usecod, String usenam, String hostname) {
        this.ubicod = ubicod;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
    }

    public String getUbicod() {
        return ubicod;
    }

    public void setUbicod(String ubicod) {
        this.ubicod = ubicod;
    }

    public String getUbides() {
        return ubides;
    }

    public void setUbides(String ubides) {
        this.ubides = ubides;
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

    @XmlTransient
    public Collection<Sistema> getSistemaCollection() {
        return sistemaCollection;
    }

    public void setSistemaCollection(Collection<Sistema> sistemaCollection) {
        this.sistemaCollection = sistemaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ubicod != null ? ubicod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ubigeo)) {
            return false;
        }
        Ubigeo other = (Ubigeo) object;
        if ((this.ubicod == null && other.ubicod != null) || (this.ubicod != null && !this.ubicod.equals(other.ubicod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Ubigeo[ ubicod=" + ubicod + " ]";
    }
    
}
