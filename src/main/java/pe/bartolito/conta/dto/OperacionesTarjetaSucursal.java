/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dto;

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
@Table(name = "Operaciones_Tarjeta_Sucursal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OperacionesTarjetaSucursal.findAll", query = "SELECT o FROM OperacionesTarjetaSucursal o"),
    @NamedQuery(name = "OperacionesTarjetaSucursal.findByCodigo", query = "SELECT o FROM OperacionesTarjetaSucursal o WHERE o.codigo = :codigo"),
    @NamedQuery(name = "OperacionesTarjetaSucursal.findByNombre", query = "SELECT o FROM OperacionesTarjetaSucursal o WHERE o.nombre = :nombre")})
public class OperacionesTarjetaSucursal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Codigo")
    private Integer codigo;
    @Size(max = 50)
    @Column(name = "Nombre")
    private String nombre;

    public OperacionesTarjetaSucursal() {
    }

    public OperacionesTarjetaSucursal(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OperacionesTarjetaSucursal)) {
            return false;
        }
        OperacionesTarjetaSucursal other = (OperacionesTarjetaSucursal) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.bartolito.conta.dto.OperacionesTarjetaSucursal[ codigo=" + codigo + " ]";
    }
    
}
