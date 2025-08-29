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
@Table(name = "inventario_detalle")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "InventarioDetalle.findAll", query = "SELECT i FROM InventarioDetalle i"),
        @NamedQuery(name = "InventarioDetalle.findByCoddeta", query = "SELECT i FROM InventarioDetalle i WHERE i.coddeta = :coddeta"),
        @NamedQuery(name = "InventarioDetalle.findByCodinvalm", query = "SELECT i FROM InventarioDetalle i WHERE i.codinvalm = :codinvalm"),
        @NamedQuery(name = "InventarioDetalle.findrol", query = "SELECT i.codrol FROM InventarioDetalle i WHERE i.codinvalm = :codinvalm and i.usecod=:usecod"),
        @NamedQuery(name = "InventarioDetalle.findcoddeta", query = "SELECT i.coddeta FROM InventarioDetalle i WHERE i.codinvalm = :codinvalm and i.usecod=:usecod"),
        @NamedQuery(name = "InventarioDetalle.findByCodrol", query = "SELECT i FROM InventarioDetalle i WHERE i.codrol = :codrol") })
public class InventarioDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "coddeta")
    private Integer coddeta;
    @Column(name = "codinvalm")
    private Integer codinvalm;
    @Column(name = "usecod")
    private Integer usecod;
    @Size(max = 1)
    @Column(name = "codrol")
    private String codrol;

    public InventarioDetalle() {
    }

    public InventarioDetalle(Integer coddeta) {
        this.coddeta = coddeta;
    }

    public Integer getCoddeta() {
        return coddeta;
    }

    public void setCoddeta(Integer coddeta) {
        this.coddeta = coddeta;
    }

    public Integer getCodinvalm() {
        return codinvalm;
    }

    public void setCodinvalm(Integer codinvalm) {
        this.codinvalm = codinvalm;
    }

    public Integer getUsecod() {
        return usecod;
    }

    public void setUsecod(Integer usecod) {
        this.usecod = usecod;
    }

    public String getCodrol() {
        return codrol;
    }

    public void setCodrol(String codrol) {
        this.codrol = codrol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coddeta != null ? coddeta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InventarioDetalle)) {
            return false;
        }
        InventarioDetalle other = (InventarioDetalle) object;
        if ((this.coddeta == null && other.coddeta != null)
                || (this.coddeta != null && !this.coddeta.equals(other.coddeta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.InventarioDetalle[ coddeta=" + coddeta + " ]";
    }

}
