/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "HechCuotaVentaMes")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "HechCuotaVentaMes.findAll", query = "SELECT h FROM HechCuotaVentaMes h"),
        @NamedQuery(name = "HechCuotaVentaMes.findByCuotVtaMesId", query = "SELECT h FROM HechCuotaVentaMes h WHERE h.cuotVtaMesId = :cuotVtaMesId"),
        @NamedQuery(name = "HechCuotaVentaMes.findBySucurId", query = "SELECT h FROM HechCuotaVentaMes h WHERE h.sucurId = :sucurId"),
        @NamedQuery(name = "HechCuotaVentaMes.findByCuotVtaId", query = "SELECT h FROM HechCuotaVentaMes h WHERE h.cuotVtaId = :cuotVtaId"),
        @NamedQuery(name = "HechCuotaVentaMes.findByCuotVtaMeta", query = "SELECT h FROM HechCuotaVentaMes h WHERE h.cuotVtaMeta = :cuotVtaMeta"),
        @NamedQuery(name = "HechCuotaVentaMes.findByCuotVtaCant", query = "SELECT h FROM HechCuotaVentaMes h WHERE h.cuotVtaCant = :cuotVtaCant") })
public class HechCuotaVentaMes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CuotVtaMesId")
    private Integer cuotVtaMesId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SucurId")
    private int sucurId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CuotVtaId")
    private int cuotVtaId;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Column(name = "CuotVtaMeta")
    private BigDecimal cuotVtaMeta;
    @Column(name = "CuotVtaCant")
    private BigDecimal cuotVtaCant;
    @Column(name = "porc_estra")
    private BigDecimal porc_estra;

    public HechCuotaVentaMes() {
    }

    public HechCuotaVentaMes(Integer cuotVtaMesId) {
        this.cuotVtaMesId = cuotVtaMesId;
    }

    public HechCuotaVentaMes(Integer cuotVtaMesId, int sucurId, int cuotVtaId) {
        this.cuotVtaMesId = cuotVtaMesId;
        this.sucurId = sucurId;
        this.cuotVtaId = cuotVtaId;
    }

    public Integer getCuotVtaMesId() {
        return cuotVtaMesId;
    }

    public void setCuotVtaMesId(Integer cuotVtaMesId) {
        this.cuotVtaMesId = cuotVtaMesId;
    }

    public int getSucurId() {
        return sucurId;
    }

    public void setSucurId(int sucurId) {
        this.sucurId = sucurId;
    }

    public int getCuotVtaId() {
        return cuotVtaId;
    }

    public void setCuotVtaId(int cuotVtaId) {
        this.cuotVtaId = cuotVtaId;
    }

    public BigDecimal getCuotVtaMeta() {
        return cuotVtaMeta;
    }

    public void setCuotVtaMeta(BigDecimal cuotVtaMeta) {
        this.cuotVtaMeta = cuotVtaMeta;
    }

    public BigDecimal getCuotVtaCant() {
        return cuotVtaCant;
    }

    public void setPorc_estra(BigDecimal porc_estra) {
        this.porc_estra = porc_estra;
    }

    public BigDecimal getPorc_estra() {
        return porc_estra;
    }

    public void setCuotVtaCant(BigDecimal cuotVtaCant) {
        this.cuotVtaCant = cuotVtaCant;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cuotVtaMesId != null ? cuotVtaMesId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HechCuotaVentaMes)) {
            return false;
        }
        HechCuotaVentaMes other = (HechCuotaVentaMes) object;
        if ((this.cuotVtaMesId == null && other.cuotVtaMesId != null)
                || (this.cuotVtaMesId != null && !this.cuotVtaMesId.equals(other.cuotVtaMesId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.HechCuotaVentaMes[ cuotVtaMesId=" + cuotVtaMesId + " ]";
    }

}
