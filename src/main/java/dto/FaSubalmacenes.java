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
@Table(name = "fa_subalmacenes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FaSubalmacenes.findAll", query = "SELECT f.codsubalm,f.dessubalm FROM FaSubalmacenes f where f.estado='S'"),
    @NamedQuery(name = "FaSubalmacenes.findByCodsubalm", query = "SELECT f FROM FaSubalmacenes f WHERE f.codsubalm = :codsubalm"),
    @NamedQuery(name = "FaSubalmacenes.findByDessubalm", query = "SELECT f FROM FaSubalmacenes f WHERE f.dessubalm = :dessubalm"),
    @NamedQuery(name = "FaSubalmacenes.findByEstado", query = "SELECT f FROM FaSubalmacenes f WHERE f.estado = :estado")})
public class FaSubalmacenes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codsubalm")
    private Integer codsubalm;
    @Size(max = 20)
    @Column(name = "dessubalm")
    private String dessubalm;
    @Size(max = 1)
    @Column(name = "estado")
    private String estado;

    public FaSubalmacenes() {
    }

    public FaSubalmacenes(Integer codsubalm) {
        this.codsubalm = codsubalm;
    }

    public Integer getCodsubalm() {
        return codsubalm;
    }

    public void setCodsubalm(Integer codsubalm) {
        this.codsubalm = codsubalm;
    }

    public String getDessubalm() {
        return dessubalm;
    }

    public void setDessubalm(String dessubalm) {
        this.dessubalm = dessubalm;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codsubalm != null ? codsubalm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaSubalmacenes)) {
            return false;
        }
        FaSubalmacenes other = (FaSubalmacenes) object;
        if ((this.codsubalm == null && other.codsubalm != null) || (this.codsubalm != null && !this.codsubalm.equals(other.codsubalm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaSubalmacenes[ codsubalm=" + codsubalm + " ]";
    }
    
}
