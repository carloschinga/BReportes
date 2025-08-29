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
@Table(name = "fa_orden_reposicion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FaOrdenReposicion.findAll", query = "SELECT f FROM FaOrdenReposicion f"),
    @NamedQuery(name = "FaOrdenReposicion.verificacion", query = "SELECT f.enviado FROM FaOrdenReposicion f where f.invnum=:invnum"),
    @NamedQuery(name = "FaOrdenReposicion.ultimoinvnum", query = "SELECT f.invnum FROM FaOrdenReposicion f order by f.invnum desc"),
    @NamedQuery(name = "FaOrdenReposicion.findByInvnum", query = "SELECT f FROM FaOrdenReposicion f WHERE f.invnum = :invnum"),
    @NamedQuery(name = "FaOrdenReposicion.findBySiscod", query = "SELECT f FROM FaOrdenReposicion f WHERE f.siscod = :siscod"),
    @NamedQuery(name = "FaOrdenReposicion.findByUsecodcre", query = "SELECT f FROM FaOrdenReposicion f WHERE f.usecodcre = :usecodcre"),
    @NamedQuery(name = "FaOrdenReposicion.findByFeccre", query = "SELECT f FROM FaOrdenReposicion f WHERE f.feccre = :feccre"),
    @NamedQuery(name = "FaOrdenReposicion.findByEstado", query = "SELECT f FROM FaOrdenReposicion f WHERE f.estado = :estado"),
    @NamedQuery(name = "FaOrdenReposicion.findByUsecodenv", query = "SELECT f FROM FaOrdenReposicion f WHERE f.usecodenv = :usecodenv"),
    @NamedQuery(name = "FaOrdenReposicion.findByFecenv", query = "SELECT f FROM FaOrdenReposicion f WHERE f.fecenv = :fecenv"),
    @NamedQuery(name = "FaOrdenReposicion.findByEnviado", query = "SELECT f FROM FaOrdenReposicion f WHERE f.enviado = :enviado"),
    @NamedQuery(name = "FaOrdenReposicion.findByAprobado", query = "SELECT f FROM FaOrdenReposicion f WHERE f.aprobado = :aprobado"),
    @NamedQuery(name = "FaOrdenReposicion.findByUsecodapr", query = "SELECT f FROM FaOrdenReposicion f WHERE f.usecodapr = :usecodapr"),
    @NamedQuery(name = "FaOrdenReposicion.findByFecapr", query = "SELECT f FROM FaOrdenReposicion f WHERE f.fecapr = :fecapr")})
public class FaOrdenReposicion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "invnum")
    private Integer invnum;
    @Basic(optional = false)
    @NotNull
    @Column(name = "siscod")
    private int siscod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usecodcre")
    private int usecodcre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado")
    private String estado;
    @Column(name = "usecodenv")
    private Integer usecodenv;
    @Column(name = "fecenv")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecenv;
    @Size(max = 1)
    @Column(name = "enviado")
    private String enviado;
    @Size(max = 1)
    @Column(name = "aprobado")
    private String aprobado;
    @Column(name = "usecodapr")
    private Integer usecodapr;
    @Column(name = "fecapr")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecapr;

    public FaOrdenReposicion() {
    }

    public FaOrdenReposicion(Integer invnum) {
        this.invnum = invnum;
    }

    public FaOrdenReposicion(Integer invnum, int siscod, int usecodcre, Date feccre, String estado) {
        this.invnum = invnum;
        this.siscod = siscod;
        this.usecodcre = usecodcre;
        this.feccre = feccre;
        this.estado = estado;
    }

    public Integer getInvnum() {
        return invnum;
    }

    public void setInvnum(Integer invnum) {
        this.invnum = invnum;
    }

    public int getSiscod() {
        return siscod;
    }

    public void setSiscod(int siscod) {
        this.siscod = siscod;
    }

    public int getUsecodcre() {
        return usecodcre;
    }

    public void setUsecodcre(int usecodcre) {
        this.usecodcre = usecodcre;
    }

    public Date getFeccre() {
        return feccre;
    }

    public void setFeccre(Date feccre) {
        this.feccre = feccre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getUsecodenv() {
        return usecodenv;
    }

    public void setUsecodenv(Integer usecodenv) {
        this.usecodenv = usecodenv;
    }

    public Date getFecenv() {
        return fecenv;
    }

    public void setFecenv(Date fecenv) {
        this.fecenv = fecenv;
    }

    public String getEnviado() {
        return enviado;
    }

    public void setEnviado(String enviado) {
        this.enviado = enviado;
    }

    public String getAprobado() {
        return aprobado;
    }

    public void setAprobado(String aprobado) {
        this.aprobado = aprobado;
    }

    public Integer getUsecodapr() {
        return usecodapr;
    }

    public void setUsecodapr(Integer usecodapr) {
        this.usecodapr = usecodapr;
    }

    public Date getFecapr() {
        return fecapr;
    }

    public void setFecapr(Date fecapr) {
        this.fecapr = fecapr;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invnum != null ? invnum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaOrdenReposicion)) {
            return false;
        }
        FaOrdenReposicion other = (FaOrdenReposicion) object;
        if ((this.invnum == null && other.invnum != null) || (this.invnum != null && !this.invnum.equals(other.invnum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaOrdenReposicion[ invnum=" + invnum + " ]";
    }
    
}
