/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dto;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureParameter;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sbdeveloperw
 */
@Entity
@Table(name = "UnidadOperativa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadOperativa.findAll", query = "SELECT u FROM UnidadOperativa u"),
    @NamedQuery(name = "UnidadOperativa.findByUnidOperald", query = "SELECT u FROM UnidadOperativa u WHERE u.unidOperald = :unidOperald"),
    @NamedQuery(name = "UnidadOperativa.findByUnidOperaDescripcion", query = "SELECT u FROM UnidadOperativa u WHERE u.unidOperaDescripcion = :unidOperaDescripcion")})

@NamedStoredProcedureQuery(
    name = "UnidadOperativa.ejecutarAccion",
    procedureName = "sp_UnidadOperativa",
    parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "Accion"),
        @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "Id"),
        @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "Descripcion")
    },
    resultClasses = UnidadOperativa.class
)
public class UnidadOperativa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "UnidOperald")
    private String unidOperald;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "UnidOperaDescripcion")
    private String unidOperaDescripcion;

    public UnidadOperativa() {
    }

    public UnidadOperativa(String unidOperald) {
        this.unidOperald = unidOperald;
    }

    public UnidadOperativa(String unidOperald, String unidOperaDescripcion) {
        this.unidOperald = unidOperald;
        this.unidOperaDescripcion = unidOperaDescripcion;
    }

    public String getUnidOperald() {
        return unidOperald;
    }

    public void setUnidOperald(String unidOperald) {
        this.unidOperald = unidOperald;
    }

    public String getUnidOperaDescripcion() {
        return unidOperaDescripcion;
    }

    public void setUnidOperaDescripcion(String unidOperaDescripcion) {
        this.unidOperaDescripcion = unidOperaDescripcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (unidOperald != null ? unidOperald.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UnidadOperativa)) {
            return false;
        }
        UnidadOperativa other = (UnidadOperativa) object;
        if ((this.unidOperald == null && other.unidOperald != null) || (this.unidOperald != null && !this.unidOperald.equals(other.unidOperald))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.bartolito.conta.dto.UnidadOperativa[ unidOperald=" + unidOperald + " ]";
    }
    
}
