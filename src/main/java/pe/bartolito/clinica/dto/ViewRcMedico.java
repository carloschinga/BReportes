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
@Table(name = "view_rc_medico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ViewRcMedico.findAll", query = "SELECT v FROM ViewRcMedico v"),
    @NamedQuery(name = "ViewRcMedico.findByMedcod", query = "SELECT v FROM ViewRcMedico v WHERE v.medcod = :medcod"),
    @NamedQuery(name = "ViewRcMedico.findByMedico", query = "SELECT v FROM ViewRcMedico v WHERE v.medico = :medico"),
    @NamedQuery(name = "ViewRcMedico.findBySercod", query = "SELECT v FROM ViewRcMedico v WHERE v.sercod = :sercod"),
    @NamedQuery(name = "ViewRcMedico.findByFechaAtencion", query = "SELECT v FROM ViewRcMedico v WHERE v.fechaAtencion = :fechaAtencion")})
public class ViewRcMedico implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
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

    public ViewRcMedico() {
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
