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
@Table(name = "view_rc_paciente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ViewRcPaciente.findAll", query = "SELECT v FROM ViewRcPaciente v"),
    @NamedQuery(name = "ViewRcPaciente.findByNumdocId", query = "SELECT v FROM ViewRcPaciente v WHERE v.numdocId = :numdocId"),
    @NamedQuery(name = "ViewRcPaciente.findByPaciente", query = "SELECT v FROM ViewRcPaciente v WHERE v.paciente = :paciente"),
    @NamedQuery(name = "ViewRcPaciente.findByMedcod", query = "SELECT v FROM ViewRcPaciente v WHERE v.medcod = :medcod"),
    @NamedQuery(name = "ViewRcPaciente.findByMedico", query = "SELECT v FROM ViewRcPaciente v WHERE v.medico = :medico"),
    @NamedQuery(name = "ViewRcPaciente.findBySercod", query = "SELECT v FROM ViewRcPaciente v WHERE v.sercod = :sercod"),
    @NamedQuery(name = "ViewRcPaciente.findByFechaAtencion", query = "SELECT v FROM ViewRcPaciente v WHERE v.fechaAtencion = :fechaAtencion")})
public class ViewRcPaciente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NumdocId")
    private String numdocId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 62)
    @Column(name = "Paciente")
    private String paciente;
    @Size(max = 10)
    @Column(name = "medcod")
    private String medcod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 70)
    @Column(name = "medico")
    private String medico;
    @Size(max = 10)
    @Column(name = "sercod")
    private String sercod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FechaAtencion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAtencion;

    public ViewRcPaciente() {
    }

    public String getNumdocId() {
        return numdocId;
    }

    public void setNumdocId(String numdocId) {
        this.numdocId = numdocId;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getMedcod() {
        return medcod;
    }

    public void setMedcod(String medcod) {
        this.medcod = medcod;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public String getSercod() {
        return sercod;
    }

    public void setSercod(String sercod) {
        this.sercod = sercod;
    }

    public Date getFechaAtencion() {
        return fechaAtencion;
    }

    public void setFechaAtencion(Date fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }
    
}
