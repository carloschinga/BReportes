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
public class FaMovimientoAlmacenPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "invnum")
    private int invnum;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "codpro")
    private String codpro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "det_item")
    private int detItem;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "codalm")
    private String codalm;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "oricod_doc")
    private String oricodDoc;

    public FaMovimientoAlmacenPK() {
    }

    public FaMovimientoAlmacenPK(int invnum, String codpro, int detItem, String codalm, String oricodDoc) {
        this.invnum = invnum;
        this.codpro = codpro;
        this.detItem = detItem;
        this.codalm = codalm;
        this.oricodDoc = oricodDoc;
    }

    public int getInvnum() {
        return invnum;
    }

    public void setInvnum(int invnum) {
        this.invnum = invnum;
    }

    public String getCodpro() {
        return codpro;
    }

    public void setCodpro(String codpro) {
        this.codpro = codpro;
    }

    public int getDetItem() {
        return detItem;
    }

    public void setDetItem(int detItem) {
        this.detItem = detItem;
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
    }

    public String getOricodDoc() {
        return oricodDoc;
    }

    public void setOricodDoc(String oricodDoc) {
        this.oricodDoc = oricodDoc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) invnum;
        hash += (codpro != null ? codpro.hashCode() : 0);
        hash += (int) detItem;
        hash += (codalm != null ? codalm.hashCode() : 0);
        hash += (oricodDoc != null ? oricodDoc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaMovimientoAlmacenPK)) {
            return false;
        }
        FaMovimientoAlmacenPK other = (FaMovimientoAlmacenPK) object;
        if (this.invnum != other.invnum) {
            return false;
        }
        if ((this.codpro == null && other.codpro != null) || (this.codpro != null && !this.codpro.equals(other.codpro))) {
            return false;
        }
        if (this.detItem != other.detItem) {
            return false;
        }
        if ((this.codalm == null && other.codalm != null) || (this.codalm != null && !this.codalm.equals(other.codalm))) {
            return false;
        }
        if ((this.oricodDoc == null && other.oricodDoc != null) || (this.oricodDoc != null && !this.oricodDoc.equals(other.oricodDoc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dao.conta.FaMovimientoAlmacenPK[ invnum=" + invnum + ", codpro=" + codpro + ", detItem=" + detItem + ", codalm=" + codalm + ", oricodDoc=" + oricodDoc + " ]";
    }
    
}
