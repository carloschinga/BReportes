/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.clinica.dto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "view_rc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ViewRc.findAll", query = "SELECT v FROM ViewRc v"),
    @NamedQuery(name = "ViewRc.findByFechaAtencion", query = "SELECT v FROM ViewRc v WHERE v.fechaAtencion = :fechaAtencion"),
    @NamedQuery(name = "ViewRc.findByActoMedico", query = "SELECT v FROM ViewRc v WHERE v.actoMedico = :actoMedico"),
    @NamedQuery(name = "ViewRc.findByPrefactura", query = "SELECT v FROM ViewRc v WHERE v.prefactura = :prefactura"),
    @NamedQuery(name = "ViewRc.findByServicio", query = "SELECT v FROM ViewRc v WHERE v.servicio = :servicio"),
    @NamedQuery(name = "ViewRc.findByMedico", query = "SELECT v FROM ViewRc v WHERE v.medico = :medico"),
    @NamedQuery(name = "ViewRc.findByPaciente", query = "SELECT v FROM ViewRc v WHERE v.paciente = :paciente"),
    @NamedQuery(name = "ViewRc.findByTelfPac", query = "SELECT v FROM ViewRc v WHERE v.telfPac = :telfPac"),
    @NamedQuery(name = "ViewRc.findByPlanAtencion", query = "SELECT v FROM ViewRc v WHERE v.planAtencion = :planAtencion"),
    @NamedQuery(name = "ViewRc.findByChekReceta", query = "SELECT v FROM ViewRc v WHERE v.chekReceta = :chekReceta")})
public class ViewRc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FechaAtencion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAtencion;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ActoMedico")
    private int actoMedico;
    @Column(name = "Prefactura")
    private Integer prefactura;
    @Size(max = 70)
    @Column(name = "Servicio")
    private String servicio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 70)
    @Column(name = "Medico")
    private String medico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 62)
    @Column(name = "Paciente")
    private String paciente;
    @Size(max = 60)
    @Column(name = "TelfPac")
    private String telfPac;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 70)
    @Column(name = "PlanAtencion")
    private String planAtencion;
    @Column(name = "chekReceta")
    private Boolean chekReceta;

    public ViewRc() {
    }

    public Date getFechaAtencion() {
        return fechaAtencion;
    }

    public void setFechaAtencion(Date fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }

    public int getActoMedico() {
        return actoMedico;
    }

    public void setActoMedico(int actoMedico) {
        this.actoMedico = actoMedico;
    }

    public Integer getPrefactura() {
        return prefactura;
    }

    public void setPrefactura(Integer prefactura) {
        this.prefactura = prefactura;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getTelfPac() {
        return telfPac;
    }

    public void setTelfPac(String telfPac) {
        this.telfPac = telfPac;
    }

    public String getPlanAtencion() {
        return planAtencion;
    }

    public void setPlanAtencion(String planAtencion) {
        this.planAtencion = planAtencion;
    }

    public Boolean getChekReceta() {
        return chekReceta;
    }

    public void setChekReceta(Boolean chekReceta) {
        this.chekReceta = chekReceta;
    }
    
}
