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
@Table(name = "TipoGasto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoGasto.findAll", query = "SELECT t FROM TipoGasto t"),
    @NamedQuery(name = "TipoGasto.findByTipoGastold", query = "SELECT t FROM TipoGasto t WHERE t.tipoGastold = :tipoGastold"),
    @NamedQuery(name = "TipoGasto.findByTipoGastoDescripcion", query = "SELECT t FROM TipoGasto t WHERE t.tipoGastoDescripcion = :tipoGastoDescripcion")})

@NamedStoredProcedureQuery(
    name = "TipoGasto.ejecutarAccion",
    procedureName = "sp_TipoGasto",
    parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "Accion"),
        @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "Id"),
        @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "Descripcion")
    },
    resultClasses = TipoGasto.class
)
public class TipoGasto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "TipoGastold")
    private String tipoGastold;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "TipoGastoDescripcion")
    private String tipoGastoDescripcion;

    public TipoGasto() {
    }

    public TipoGasto(String tipoGastold) {
        this.tipoGastold = tipoGastold;
    }

    public TipoGasto(String tipoGastold, String tipoGastoDescripcion) {
        this.tipoGastold = tipoGastold;
        this.tipoGastoDescripcion = tipoGastoDescripcion;
    }

    public String getTipoGastold() {
        return tipoGastold;
    }

    public void setTipoGastold(String tipoGastold) {
        this.tipoGastold = tipoGastold;
    }

    public String getTipoGastoDescripcion() {
        return tipoGastoDescripcion;
    }

    public void setTipoGastoDescripcion(String tipoGastoDescripcion) {
        this.tipoGastoDescripcion = tipoGastoDescripcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tipoGastold != null ? tipoGastold.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoGasto)) {
            return false;
        }
        TipoGasto other = (TipoGasto) object;
        if ((this.tipoGastold == null && other.tipoGastold != null) || (this.tipoGastold != null && !this.tipoGastold.equals(other.tipoGastold))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.bartolito.conta.dto.TipoGasto[ tipoGastold=" + tipoGastold + " ]";
    }
    
}
