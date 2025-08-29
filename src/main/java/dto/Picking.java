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
@Table(name = "picking")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Picking.findAll", query = "SELECT p FROM Picking p"),
    @NamedQuery(name = "Picking.obtenerultpickcod", query = "SELECT p.pickcod FROM Picking p order by p.pickcod desc"),
    @NamedQuery(name = "Picking.findByPickcod", query = "SELECT p FROM Picking p WHERE p.pickcod = :pickcod"),
    @NamedQuery(name = "Picking.findByDespick", query = "SELECT p FROM Picking p WHERE p.despick = :despick")})
public class Picking implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pickcod")
    private Integer pickcod;
    @Size(max = 20)
    @Column(name = "despick")
    private String despick;
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;
    @Column(name = "usecod")
    private Integer usecod;
    @Size(max = 1)
    @Column(name = "completado")
    private String completado;
    @Size(max = 2)
    @Column(name = "codalm")
    private String codalm;
    @Column(name = "codpicklist")
    private Integer codpicklist;
    @Size(max = 1)
    @Column(name = "chkcomrep")
    private String chkcomrep;
    @Column(name = "usecodcom")
    private Integer usecodcom;
    @Column(name = "feccom")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccom;
    @Size(max = 1)
    @Column(name = "estado")
    private String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getUsecodcom() {
        return usecodcom;
    }

    public void setUsecodcom(Integer usecodcom) {
        this.usecodcom = usecodcom;
    }

    public Date getFeccom() {
        return feccom;
    }

    public void setFeccom(Date feccom) {
        this.feccom = feccom;
    }

    public String getChkcomrep() {
        return chkcomrep;
    }

    public void setChkcomrep(String chkcomrep) {
        this.chkcomrep = chkcomrep;
    }

    public Integer getCodpicklist() {
        return codpicklist;
    }

    public void setCodpicklist(Integer codpicklist) {
        this.codpicklist = codpicklist;
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
    }

    public String getCompletado() {
        return completado;
    }

    public void setCompletado(String completado) {
        this.completado = completado;
    }

    public Date getFeccre() {
        return feccre;
    }

    public void setFeccre(Date feccre) {
        this.feccre = feccre;
    }

    public Integer getUsecod() {
        return usecod;
    }

    public void setUsecod(Integer usecod) {
        this.usecod = usecod;
    }
    
    
    public Picking() {
    }

    public Picking(Integer pickcod) {
        this.pickcod = pickcod;
    }

    public Integer getPickcod() {
        return pickcod;
    }

    public void setPickcod(Integer pickcod) {
        this.pickcod = pickcod;
    }

    public String getDespick() {
        return despick;
    }

    public void setDespick(String despick) {
        this.despick = despick;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pickcod != null ? pickcod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Picking)) {
            return false;
        }
        Picking other = (Picking) object;
        if ((this.pickcod == null && other.pickcod != null) || (this.pickcod != null && !this.pickcod.equals(other.pickcod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Picking[ pickcod=" + pickcod + " ]";
    }
    
}
