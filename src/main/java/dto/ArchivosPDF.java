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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "ArchivosPDF")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ArchivosPDF.findAll", query = "SELECT a FROM ArchivosPDF a"),
    @NamedQuery(name = "ArchivosPDF.findById", query = "SELECT a FROM ArchivosPDF a WHERE a.id = :id"),
    @NamedQuery(name = "ArchivosPDF.findByNombreOriginal", query = "SELECT a FROM ArchivosPDF a WHERE a.nombreOriginal = :nombreOriginal"),
    @NamedQuery(name = "ArchivosPDF.findByNombreGuardado", query = "SELECT a FROM ArchivosPDF a WHERE a.nombreGuardado = :nombreGuardado"),
    @NamedQuery(name = "ArchivosPDF.findByDescripcion", query = "SELECT a FROM ArchivosPDF a WHERE a.descripcion = :descripcion"),
    @NamedQuery(name = "ArchivosPDF.findByFechaSubida", query = "SELECT a FROM ArchivosPDF a WHERE a.fechaSubida = :fechaSubida")})
public class ArchivosPDF implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "nombre_original")
    private String nombreOriginal;
    @Size(max = 255)
    @Column(name = "nombre_guardado")
    private String nombreGuardado;
    @Size(max = 500)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "fecha_subida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSubida;

    public ArchivosPDF() {
    }

    public ArchivosPDF(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreOriginal() {
        return nombreOriginal;
    }

    public void setNombreOriginal(String nombreOriginal) {
        this.nombreOriginal = nombreOriginal;
    }

    public String getNombreGuardado() {
        return nombreGuardado;
    }

    public void setNombreGuardado(String nombreGuardado) {
        this.nombreGuardado = nombreGuardado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(Date fechaSubida) {
        this.fechaSubida = fechaSubida;
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
        if (!(object instanceof ArchivosPDF)) {
            return false;
        }
        ArchivosPDF other = (ArchivosPDF) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.ArchivosPDF[ id=" + id + " ]";
    }
    
}
