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
@Table(name = "inventario_almacen_cabecera")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "InventarioAlmacenCabecera.findAll", query = "SELECT i FROM InventarioAlmacenCabecera i"),
        @NamedQuery(name = "InventarioAlmacenCabecera.findByCodinvcab", query = "SELECT i FROM InventarioAlmacenCabecera i WHERE i.codinvcab = :codinvcab"),
        @NamedQuery(name = "InventarioAlmacenCabecera.findByCodinv", query = "SELECT i FROM InventarioAlmacenCabecera i WHERE i.codinv = :codinv"),
        @NamedQuery(name = "InventarioAlmacenCabecera.findByCodalm", query = "SELECT i FROM InventarioAlmacenCabecera i WHERE i.codalm = :codalm") })
public class InventarioAlmacenCabecera implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codinvcab")
    private Integer codinvcab;
    @Column(name = "codinv")
    private Integer codinv;
    @Size(max = 2)
    @Column(name = "codalm")
    private String codalm;
    @Size(max = 1)
    @Column(name = "captura")
    private String captura;

    public InventarioAlmacenCabecera() {
    }

    public InventarioAlmacenCabecera(Integer codinvcab) {
        this.codinvcab = codinvcab;
    }

    public Integer getCodinvcab() {
        return codinvcab;
    }

    public void setCodinvcab(Integer codinvcab) {
        this.codinvcab = codinvcab;
    }

    public Integer getCodinv() {
        return codinv;
    }

    public void setCodinv(Integer codinv) {
        this.codinv = codinv;
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
    }

    public String getCaptura() {
        return captura;
    }

    public void setCaptura(String captura) {
        this.captura = captura;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codinvcab != null ? codinvcab.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InventarioAlmacenCabecera)) {
            return false;
        }
        InventarioAlmacenCabecera other = (InventarioAlmacenCabecera) object;
        if ((this.codinvcab == null && other.codinvcab != null)
                || (this.codinvcab != null && !this.codinvcab.equals(other.codinvcab))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.InventarioAlmacenCabecera[ codinvcab=" + codinvcab + " ]";
    }

}
