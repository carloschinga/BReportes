/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "capturastocksinventario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Capturastocksinventario.findAll", query = "SELECT c FROM Capturastocksinventario c"),
    @NamedQuery(name = "Capturastocksinventario.findById", query = "SELECT c FROM Capturastocksinventario c WHERE c.id = :id"),
    @NamedQuery(name = "Capturastocksinventario.findByCodinvcab", query = "SELECT c FROM Capturastocksinventario c WHERE c.codinvcab = :codinvcab"),
    @NamedQuery(name = "Capturastocksinventario.findByCodpro", query = "SELECT c FROM Capturastocksinventario c WHERE c.codpro = :codpro"),
    @NamedQuery(name = "Capturastocksinventario.findByCodlot", query = "SELECT c FROM Capturastocksinventario c WHERE c.codlot = :codlot"),
    @NamedQuery(name = "Capturastocksinventario.findByCante", query = "SELECT c FROM Capturastocksinventario c WHERE c.cante = :cante"),
    @NamedQuery(name = "Capturastocksinventario.findByCantf", query = "SELECT c FROM Capturastocksinventario c WHERE c.cantf = :cantf")})
public class Capturastocksinventario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "codinvcab")
    private Integer codinvcab;
    @Size(max = 5)
    @Column(name = "codpro")
    private String codpro;
    @Size(max = 15)
    @Column(name = "codlot")
    private String codlot;
    @Column(name = "cante")
    private Integer cante;
    @Column(name = "cantf")
    private Integer cantf;

    public Capturastocksinventario() {
    }

    public Capturastocksinventario(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCodinvcab() {
        return codinvcab;
    }

    public void setCodinvcab(Integer codinvcab) {
        this.codinvcab = codinvcab;
    }

    public String getCodpro() {
        return codpro;
    }

    public void setCodpro(String codpro) {
        this.codpro = codpro;
    }

    public String getCodlot() {
        return codlot;
    }

    public void setCodlot(String codlot) {
        this.codlot = codlot;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Capturastocksinventario)) {
            return false;
        }
        Capturastocksinventario other = (Capturastocksinventario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Capturastocksinventario[ id=" + id + " ]";
    }
    
}
