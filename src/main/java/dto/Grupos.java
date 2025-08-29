/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "grupos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grupos.findAll", query = "SELECT g FROM Grupos g"),
    @NamedQuery(name = "Grupos.findByGrucod", query = "SELECT g FROM Grupos g WHERE g.grucod = :grucod"),
    @NamedQuery(name = "Grupos.findByGrudes", query = "SELECT g FROM Grupos g WHERE g.grudes = :grudes"),
    @NamedQuery(name = "Grupos.findByGruobj", query = "SELECT g FROM Grupos g WHERE g.gruobj = :gruobj"),
    @NamedQuery(name = "Grupos.findByEstado", query = "SELECT g FROM Grupos g WHERE g.estado = :estado"),
    @NamedQuery(name = "Grupos.findByFeccre", query = "SELECT g FROM Grupos g WHERE g.feccre = :feccre"),
    @NamedQuery(name = "Grupos.findByFecumv", query = "SELECT g FROM Grupos g WHERE g.fecumv = :fecumv"),
    @NamedQuery(name = "Grupos.findByUsecod", query = "SELECT g FROM Grupos g WHERE g.usecod = :usecod"),
    @NamedQuery(name = "Grupos.findByUsenam", query = "SELECT g FROM Grupos g WHERE g.usenam = :usenam"),
    @NamedQuery(name = "Grupos.findByHostname", query = "SELECT g FROM Grupos g WHERE g.hostname = :hostname")})
public class Grupos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "grucod")
    private String grucod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "grudes")
    private String grudes;
    @Size(max = 50)
    @Column(name = "gruobj")
    private String gruobj;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grucod")
    private List<Usuarios> usuariosList;

    public Grupos() {
    }

    public Grupos(String grucod) {
        this.grucod = grucod;
    }

    public Grupos(String grucod, String grudes, String estado, Date feccre, Date fecumv, int usecod, String usenam, String hostname) {
        this.grucod = grucod;
        this.grudes = grudes;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
    }

    public String getGrucod() {
        return grucod;
    }

    public void setGrucod(String grucod) {
        this.grucod = grucod;
    }

    public String getGrudes() {
        return grudes;
    }

    public void setGrudes(String grudes) {
        this.grudes = grudes;
    }

    public String getGruobj() {
        return gruobj;
    }

    public void setGruobj(String gruobj) {
        this.gruobj = gruobj;
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
    public List<Usuarios> getUsuariosList() {
        return usuariosList;
    }

    public void setUsuariosList(List<Usuarios> usuariosList) {
        this.usuariosList = usuariosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (grucod != null ? grucod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grupos)) {
            return false;
        }
        Grupos other = (Grupos) object;
        if ((this.grucod == null && other.grucod != null) || (this.grucod != null && !this.grucod.equals(other.grucod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Grupos[ grucod=" + grucod + " ]";
    }
    
}
