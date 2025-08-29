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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "CuentaCorriente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CuentaCorriente.findAll", query = "SELECT c FROM CuentaCorriente c"),
    @NamedQuery(name = "CuentaCorriente.findByCtaCteId", query = "SELECT c FROM CuentaCorriente c WHERE c.ctaCteId = :ctaCteId"),
    @NamedQuery(name = "CuentaCorriente.findByCtaCteDescripcion", query = "SELECT c FROM CuentaCorriente c WHERE c.ctaCteDescripcion = :ctaCteDescripcion"),
    @NamedQuery(name = "CuentaCorriente.findByCtaCteBeneficiario", query = "SELECT c FROM CuentaCorriente c WHERE c.ctaCteBeneficiario = :ctaCteBeneficiario"),
    @NamedQuery(name = "CuentaCorriente.findByCtaCteCodGen", query = "SELECT c FROM CuentaCorriente c WHERE c.ctaCteCodGen = :ctaCteCodGen"),
    @NamedQuery(name = "CuentaCorriente.findByCtaCteEstado", query = "SELECT c FROM CuentaCorriente c WHERE c.ctaCteEstado = :ctaCteEstado"),
    @NamedQuery(name = "CuentaCorriente.findByDocIdenId", query = "SELECT c FROM CuentaCorriente c WHERE c.docIdenId = :docIdenId"),
    @NamedQuery(name = "CuentaCorriente.findByUsrSistema", query = "SELECT c FROM CuentaCorriente c WHERE c.usrSistema = :usrSistema"),
    @NamedQuery(name = "CuentaCorriente.findByFecSistema", query = "SELECT c FROM CuentaCorriente c WHERE c.fecSistema = :fecSistema"),
    @NamedQuery(name = "CuentaCorriente.findByHrsSistema", query = "SELECT c FROM CuentaCorriente c WHERE c.hrsSistema = :hrsSistema")})
public class CuentaCorriente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CtaCteId")
    private Long ctaCteId;
    @Size(max = 60)
    @Column(name = "CtaCteDescripcion")
    private String ctaCteDescripcion;
    @Size(max = 60)
    @Column(name = "CtaCteBeneficiario")
    private String ctaCteBeneficiario;
    @Size(max = 12)
    @Column(name = "CtaCteCodGen")
    private String ctaCteCodGen;
    @Column(name = "CtaCteEstado")
    private Character ctaCteEstado;
    @Column(name = "DocIdenId")
    private Character docIdenId;
    @Size(max = 10)
    @Column(name = "UsrSistema")
    private String usrSistema;
    @Column(name = "FecSistema")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecSistema;
    @Size(max = 10)
    @Column(name = "HrsSistema")
    private String hrsSistema;
    @OneToMany(mappedBy = "ctaCteId")
    private List<DiarioDetalle> diarioDetalleList;
    @JoinColumn(name = "PersonaId", referencedColumnName = "PersonaId")
    @ManyToOne
    private TipoPersona personaId;

    public CuentaCorriente() {
    }

    public CuentaCorriente(Long ctaCteId) {
        this.ctaCteId = ctaCteId;
    }

    public Long getCtaCteId() {
        return ctaCteId;
    }

    public void setCtaCteId(Long ctaCteId) {
        this.ctaCteId = ctaCteId;
    }

    public String getCtaCteDescripcion() {
        return ctaCteDescripcion;
    }

    public void setCtaCteDescripcion(String ctaCteDescripcion) {
        this.ctaCteDescripcion = ctaCteDescripcion;
    }

    public String getCtaCteBeneficiario() {
        return ctaCteBeneficiario;
    }

    public void setCtaCteBeneficiario(String ctaCteBeneficiario) {
        this.ctaCteBeneficiario = ctaCteBeneficiario;
    }

    public String getCtaCteCodGen() {
        return ctaCteCodGen;
    }

    public void setCtaCteCodGen(String ctaCteCodGen) {
        this.ctaCteCodGen = ctaCteCodGen;
    }

    public Character getCtaCteEstado() {
        return ctaCteEstado;
    }

    public void setCtaCteEstado(Character ctaCteEstado) {
        this.ctaCteEstado = ctaCteEstado;
    }

    public Character getDocIdenId() {
        return docIdenId;
    }

    public void setDocIdenId(Character docIdenId) {
        this.docIdenId = docIdenId;
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
    public List<DiarioDetalle> getDiarioDetalleList() {
        return diarioDetalleList;
    }

    public void setDiarioDetalleList(List<DiarioDetalle> diarioDetalleList) {
        this.diarioDetalleList = diarioDetalleList;
    }

    public TipoPersona getPersonaId() {
        return personaId;
    }

    public void setPersonaId(TipoPersona personaId) {
        this.personaId = personaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ctaCteId != null ? ctaCteId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuentaCorriente)) {
            return false;
        }
        CuentaCorriente other = (CuentaCorriente) object;
        if ((this.ctaCteId == null && other.ctaCteId != null) || (this.ctaCteId != null && !this.ctaCteId.equals(other.ctaCteId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.conta.CuentaCorriente[ ctaCteId=" + ctaCteId + " ]";
    }
    
}
