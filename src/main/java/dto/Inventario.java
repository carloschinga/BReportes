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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "inventario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Inventario.findAll", query = "SELECT i FROM Inventario i"),
    @NamedQuery(name = "Inventario.findByCodinv", query = "SELECT i FROM Inventario i WHERE i.codinv = :codinv"),
    @NamedQuery(name = "Inventario.findByCodusu", query = "SELECT i FROM Inventario i WHERE i.codusu = :codusu"),
    @NamedQuery(name = "Inventario.findByDesinv", query = "SELECT i FROM Inventario i WHERE i.desinv = :desinv"),
    @NamedQuery(name = "Inventario.findByFeccre", query = "SELECT i FROM Inventario i WHERE i.feccre = :feccre"),
    @NamedQuery(name = "Inventario.findByEstado", query = "SELECT i FROM Inventario i WHERE i.estado = :estado"),
    @NamedQuery(name = "Inventario.findByEstinv", query = "SELECT i FROM Inventario i WHERE i.estinv = :estinv"),
    @NamedQuery(name = "Inventario.findByUsecier", query = "SELECT i FROM Inventario i WHERE i.usecier = :usecier"),
    @NamedQuery(name = "Inventario.findByCaptura", query = "SELECT i FROM Inventario i WHERE i.captura = :captura"),
    @NamedQuery(name = "Inventario.findByDireccionado", query = "SELECT i FROM Inventario i WHERE i.direccionado = :direccionado")})
public class Inventario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codinv")
    private Integer codinv;
    @Column(name = "codusu")
    private Integer codusu;
    @Size(max = 120)
    @Column(name = "desinv")
    private String desinv;
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;
    @Size(max = 1)
    @Column(name = "estado")
    private String estado;
    @Size(max = 1)
    @Column(name = "estinv")
    private String estinv;
    @Column(name = "usecier")
    private Integer usecier;
    @Size(max = 1)
    @Column(name = "captura")
    private String captura;
    @Basic(optional = false)
    @NotNull
    @Column(name = "direccionado")
    private boolean direccionado;

    public Inventario() {
    }

    public Inventario(Integer codinv) {
        this.codinv = codinv;
    }

    public Inventario(Integer codinv, boolean direccionado) {
        this.codinv = codinv;
        this.direccionado = direccionado;
    }

    public Integer getCodinv() {
        return codinv;
    }

    public void setCodinv(Integer codinv) {
        this.codinv = codinv;
    }

    public Integer getCodusu() {
        return codusu;
    }

    public void setCodusu(Integer codusu) {
        this.codusu = codusu;
    }

    public String getDesinv() {
        return desinv;
    }

    public void setDesinv(String desinv) {
        this.desinv = desinv;
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

    public String getEstinv() {
        return estinv;
    }

    public void setEstinv(String estinv) {
        this.estinv = estinv;
    }

    public Integer getUsecier() {
        return usecier;
    }

    public void setUsecier(Integer usecier) {
        this.usecier = usecier;
    }

    public String getCaptura() {
        return captura;
    }

    public void setCaptura(String captura) {
        this.captura = captura;
    }

    public boolean getDireccionado() {
        return direccionado;
    }

    public void setDireccionado(boolean direccionado) {
        this.direccionado = direccionado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codinv != null ? codinv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Inventario)) {
            return false;
        }
        Inventario other = (Inventario) object;
        if ((this.codinv == null && other.codinv != null) || (this.codinv != null && !this.codinv.equals(other.codinv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Inventario[ codinv=" + codinv + " ]";
    }
    
}
