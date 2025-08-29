/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "estrategico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estrategico.findAll", query = "SELECT e FROM Estrategico e"),
    @NamedQuery(name = "Estrategico.findByCompro", query = "SELECT e FROM Estrategico e WHERE e.compro = :compro"),
    @NamedQuery(name = "Estrategico.findByDescripcion", query = "SELECT e FROM Estrategico e WHERE e.descripcion = :descripcion")})
public class Estrategico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "compro")
    private Integer compro;
    @Size(max = 100)
    @Column(name = "descripcion")
    private String descripcion;

    public Estrategico() {
    }

    public Estrategico(Integer compro) {
        this.compro = compro;
    }

    public Integer getCompro() {
        return compro;
    }

    public void setCompro(Integer compro) {
        this.compro = compro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (compro != null ? compro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estrategico)) {
            return false;
        }
        Estrategico other = (Estrategico) object;
        if ((this.compro == null && other.compro != null) || (this.compro != null && !this.compro.equals(other.compro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Estrategico[ compro=" + compro + " ]";
    }
    
}
