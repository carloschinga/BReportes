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
@Table(name = "view_rc_servicio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ViewRcServicio.findAll", query = "SELECT v FROM ViewRcServicio v"),
    @NamedQuery(name = "ViewRcServicio.findBySercod", query = "SELECT v FROM ViewRcServicio v WHERE v.sercod = :sercod"),
    @NamedQuery(name = "ViewRcServicio.findByServicio", query = "SELECT v FROM ViewRcServicio v WHERE v.servicio = :servicio"),
    @NamedQuery(name = "ViewRcServicio.findByFechaAtencion", query = "SELECT v FROM ViewRcServicio v WHERE v.fechaAtencion = :fechaAtencion")})
public class ViewRcServicio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Size(max = 10)
    @Column(name = "sercod")
    private String sercod;
    @Size(max = 70)
    @Column(name = "Servicio")
    private String servicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FechaAtencion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAtencion;

    public ViewRcServicio() {
    }

    public String getSercod() {
        return sercod;
    }

    public void setSercod(String sercod) {
        this.sercod = sercod;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public Date getFechaAtencion() {
        return fechaAtencion;
    }

    public void setFechaAtencion(Date fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }
    
}
