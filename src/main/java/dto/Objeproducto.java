/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "objeproducto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Objeproducto.findAll", query = "SELECT o FROM Objeproducto o"),
    @NamedQuery(name = "Objeproducto.findByCodobj", query = "SELECT o FROM Objeproducto o WHERE o.objeproductoPK.codobj = :codobj"),
    @NamedQuery(name = "Objeproducto.findByCodpro", query = "SELECT o FROM Objeproducto o WHERE o.objeproductoPK.codpro = :codpro")})
public class Objeproducto implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ObjeproductoPK objeproductoPK;

    public Objeproducto() {
    }

    public Objeproducto(ObjeproductoPK objeproductoPK) {
        this.objeproductoPK = objeproductoPK;
    }

    public Objeproducto(int codobj, int codpro) {
        this.objeproductoPK = new ObjeproductoPK(codobj, codpro);
    }

    public ObjeproductoPK getObjeproductoPK() {
        return objeproductoPK;
    }

    public void setObjeproductoPK(ObjeproductoPK objeproductoPK) {
        this.objeproductoPK = objeproductoPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (objeproductoPK != null ? objeproductoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Objeproducto)) {
            return false;
        }
        Objeproducto other = (Objeproducto) object;
        if ((this.objeproductoPK == null && other.objeproductoPK != null) || (this.objeproductoPK != null && !this.objeproductoPK.equals(other.objeproductoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Objeproducto[ objeproductoPK=" + objeproductoPK + " ]";
    }
    
}
