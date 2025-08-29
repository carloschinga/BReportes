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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "inventario_lista_productos")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "InventarioListaProductos.findAll", query = "SELECT i FROM InventarioListaProductos i"),
        @NamedQuery(name = "InventarioListaProductos.findByCodinvlis", query = "SELECT i FROM InventarioListaProductos i WHERE i.codinvlis = :codinvlis"),
        @NamedQuery(name = "InventarioListaProductos.findByCodinvalm", query = "SELECT i FROM InventarioListaProductos i WHERE i.codinvalm = :codinvalm"),
        @NamedQuery(name = "InventarioListaProductos.findByCodpro", query = "SELECT i FROM InventarioListaProductos i WHERE i.codpro = :codpro") })
public class InventarioListaProductos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codinvlis")
    private Integer codinvlis;
    @Basic(optional = false)
    @NotNull
    @Column(name = "codinvalm")
    private int codinvalm;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "codpro")
    private String codpro;

    public InventarioListaProductos() {
    }

    public InventarioListaProductos(Integer codinvlis) {
        this.codinvlis = codinvlis;
    }

    public InventarioListaProductos(Integer codinvlis, int codinvalm, String codpro) {
        this.codinvlis = codinvlis;
        this.codinvalm = codinvalm;
        this.codpro = codpro;
    }

    public InventarioListaProductos(int codinvalm, String codpro) {
        this.codinvalm = codinvalm;
        this.codpro = codpro;
    }

    public Integer getCodinvlis() {
        return codinvlis;
    }

    public void setCodinvlis(Integer codinvlis) {
        this.codinvlis = codinvlis;
    }

    public int getCodinvalm() {
        return codinvalm;
    }

    public void setCodinvalm(int codinvalm) {
        this.codinvalm = codinvalm;
    }

    public String getCodpro() {
        return codpro;
    }

    public void setCodpro(String codpro) {
        this.codpro = codpro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codinvlis != null ? codinvlis.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InventarioListaProductos)) {
            return false;
        }
        InventarioListaProductos other = (InventarioListaProductos) object;
        if ((this.codinvlis == null && other.codinvlis != null)
                || (this.codinvlis != null && !this.codinvlis.equals(other.codinvlis))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.InventarioListaProductos[ codinvlis=" + codinvlis + " ]";
    }

}
