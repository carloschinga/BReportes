/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.login.dto;

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
@Table(name = "rol")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rol.findAll", query = "SELECT r FROM Rol r"),
    @NamedQuery(name = "Rol.findByCodrol", query = "SELECT r FROM Rol r WHERE r.codrol = :codrol"),
    @NamedQuery(name = "Rol.findByNomrol", query = "SELECT r FROM Rol r WHERE r.nomrol = :nomrol"),
    @NamedQuery(name = "Rol.findBySisrol", query = "SELECT r FROM Rol r WHERE r.sisrol = :sisrol")})
public class Rol implements Serializable {

    @Size(max = 50)
    @Column(name = "nomrol")
    private String nomrol;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codrol")
    private Integer codrol;
    @Column(name = "sisrol")
    private Boolean sisrol;

    public Rol() {
    }

    public Rol(Integer codrol) {
        this.codrol = codrol;
    }

    public Integer getCodrol() {
        return codrol;
    }

    public void setCodrol(Integer codrol) {
        this.codrol = codrol;
    }


    public Boolean getSisrol() {
        return sisrol;
    }

    public void setSisrol(Boolean sisrol) {
        this.sisrol = sisrol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codrol != null ? codrol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rol)) {
            return false;
        }
        Rol other = (Rol) object;
        if ((this.codrol == null && other.codrol != null) || (this.codrol != null && !this.codrol.equals(other.codrol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Rol[ codrol=" + codrol + " ]";
    }

    public String getNomrol() {
        return nomrol;
    }

    public void setNomrol(String nomrol) {
        this.nomrol = nomrol;
    }
    
}
