/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "TipoPersona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoPersona.findAll", query = "SELECT t FROM TipoPersona t"),
    @NamedQuery(name = "TipoPersona.findByPersonaId", query = "SELECT t FROM TipoPersona t WHERE t.personaId = :personaId"),
    @NamedQuery(name = "TipoPersona.findByPersonaDescripcion", query = "SELECT t FROM TipoPersona t WHERE t.personaDescripcion = :personaDescripcion"),
    @NamedQuery(name = "TipoPersona.findByUsrSistema", query = "SELECT t FROM TipoPersona t WHERE t.usrSistema = :usrSistema"),
    @NamedQuery(name = "TipoPersona.findByFecSistema", query = "SELECT t FROM TipoPersona t WHERE t.fecSistema = :fecSistema"),
    @NamedQuery(name = "TipoPersona.findByHrsSistema", query = "SELECT t FROM TipoPersona t WHERE t.hrsSistema = :hrsSistema")})
public class TipoPersona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "PersonaId")
    private String personaId;
    @Size(max = 30)
    @Column(name = "PersonaDescripcion")
    private String personaDescripcion;
    @Size(max = 10)
    @Column(name = "UsrSistema")
    private String usrSistema;
    @Column(name = "FecSistema")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecSistema;
    @Size(max = 10)
    @Column(name = "HrsSistema")
    private String hrsSistema;
    @OneToMany(mappedBy = "personaId")
    private List<CuentaCorriente> cuentaCorrienteList;

    public TipoPersona() {
    }

    public TipoPersona(String personaId) {
        this.personaId = personaId;
    }

    public String getPersonaId() {
        return personaId;
    }

    public void setPersonaId(String personaId) {
        this.personaId = personaId;
    }

    public String getPersonaDescripcion() {
        return personaDescripcion;
    }

    public void setPersonaDescripcion(String personaDescripcion) {
        this.personaDescripcion = personaDescripcion;
    }

    public String getUsrSistema() {
        return usrSistema;
    }

    public void setUsrSistema(String usrSistema) {
        this.usrSistema = usrSistema;
    }

    public Date getFecSistema() {
        return fecSistema;
    }

    public void setFecSistema(Date fecSistema) {
        this.fecSistema = fecSistema;
    }

    public String getHrsSistema() {
        return hrsSistema;
    }

    public void setHrsSistema(String hrsSistema) {
        this.hrsSistema = hrsSistema;
    }

    @XmlTransient
    public List<CuentaCorriente> getCuentaCorrienteList() {
        return cuentaCorrienteList;
    }

    public void setCuentaCorrienteList(List<CuentaCorriente> cuentaCorrienteList) {
        this.cuentaCorrienteList = cuentaCorrienteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (personaId != null ? personaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoPersona)) {
            return false;
        }
        TipoPersona other = (TipoPersona) object;
        if ((this.personaId == null && other.personaId != null) || (this.personaId != null && !this.personaId.equals(other.personaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.conta.TipoPersona[ personaId=" + personaId + " ]";
    }
    
}
