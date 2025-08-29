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
public class ProvinciaGeograficaPK implements Serializable {

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

    public ProvinciaGeograficaPK() {
    }

    public ProvinciaGeograficaPK(String dptoGeoId, String provGeoId) {
        this.dptoGeoId = dptoGeoId;
        this.provGeoId = provGeoId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dptoGeoId != null ? dptoGeoId.hashCode() : 0);
        hash += (provGeoId != null ? provGeoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProvinciaGeograficaPK)) {
            return false;
        }
        ProvinciaGeograficaPK other = (ProvinciaGeograficaPK) object;
        if ((this.dptoGeoId == null && other.dptoGeoId != null) || (this.dptoGeoId != null && !this.dptoGeoId.equals(other.dptoGeoId))) {
            return false;
        }
        if ((this.provGeoId == null && other.provGeoId != null) || (this.provGeoId != null && !this.provGeoId.equals(other.provGeoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.conta.ProvinciaGeograficaPK[ dptoGeoId=" + dptoGeoId + ", provGeoId=" + provGeoId + " ]";
    }
    
}
