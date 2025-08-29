/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dto;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author USUARIO
 */
@Embeddable
public class DistritoGeograficoPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "DptoGeoId")
    private String dptoGeoId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "ProvGeoId")
    private String provGeoId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "DistGeoId")
    private String distGeoId;

    public DistritoGeograficoPK() {
    }

    public DistritoGeograficoPK(String dptoGeoId, String provGeoId, String distGeoId) {
        this.dptoGeoId = dptoGeoId;
        this.provGeoId = provGeoId;
        this.distGeoId = distGeoId;
    }

    public String getDptoGeoId() {
        return dptoGeoId;
    }

    public void setDptoGeoId(String dptoGeoId) {
        this.dptoGeoId = dptoGeoId;
    }

    public String getProvGeoId() {
        return provGeoId;
    }

    public void setProvGeoId(String provGeoId) {
        this.provGeoId = provGeoId;
    }

    public String getDistGeoId() {
        return distGeoId;
    }

    public void setDistGeoId(String distGeoId) {
        this.distGeoId = distGeoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dptoGeoId != null ? dptoGeoId.hashCode() : 0);
        hash += (provGeoId != null ? provGeoId.hashCode() : 0);
        hash += (distGeoId != null ? distGeoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DistritoGeograficoPK)) {
            return false;
        }
        DistritoGeograficoPK other = (DistritoGeograficoPK) object;
        if ((this.dptoGeoId == null && other.dptoGeoId != null) || (this.dptoGeoId != null && !this.dptoGeoId.equals(other.dptoGeoId))) {
            return false;
        }
        if ((this.provGeoId == null && other.provGeoId != null) || (this.provGeoId != null && !this.provGeoId.equals(other.provGeoId))) {
            return false;
        }
        if ((this.distGeoId == null && other.distGeoId != null) || (this.distGeoId != null && !this.distGeoId.equals(other.distGeoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.conta.DistritoGeograficoPK[ dptoGeoId=" + dptoGeoId + ", provGeoId=" + provGeoId + ", distGeoId=" + distGeoId + " ]";
    }
    
}
