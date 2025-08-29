/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

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
public class DocumentosSeriesPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "tdoser")
    private String tdoser;
    @Basic(optional = false)
    @NotNull
    @Column(name = "siscod")
    private int siscod;

    public DocumentosSeriesPK() {
    }

    public DocumentosSeriesPK(String tdoser, int siscod) {
        this.tdoser = tdoser;
        this.siscod = siscod;
    }

    public String getTdoser() {
        return tdoser;
    }

    public void setTdoser(String tdoser) {
        this.tdoser = tdoser;
    }

    public int getSiscod() {
        return siscod;
    }

    public void setSiscod(int siscod) {
        this.siscod = siscod;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tdoser != null ? tdoser.hashCode() : 0);
        hash += (int) siscod;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentosSeriesPK)) {
            return false;
        }
        DocumentosSeriesPK other = (DocumentosSeriesPK) object;
        if ((this.tdoser == null && other.tdoser != null) || (this.tdoser != null && !this.tdoser.equals(other.tdoser))) {
            return false;
        }
        if (this.siscod != other.siscod) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.DocumentosSeriesPK[ tdoser=" + tdoser + ", siscod=" + siscod + " ]";
    }
    
}
