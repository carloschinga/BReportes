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
@Table(name = "objetipo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Objetipo.findAll", query = "SELECT o FROM Objetipo o"),
    @NamedQuery(name = "Objetipo.findByCodobj", query = "SELECT o FROM Objetipo o WHERE o.objetipoPK.codobj = :codobj"),
    @NamedQuery(name = "Objetipo.findByCodtip", query = "SELECT o FROM Objetipo o WHERE o.objetipoPK.codtip = :codtip")})
public class Objetipo implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ObjetipoPK objetipoPK;

    public Objetipo() {
    }

    public Objetipo(ObjetipoPK objetipoPK) {
        this.objetipoPK = objetipoPK;
    }

    public Objetipo(int codobj, String codtip) {
        this.objetipoPK = new ObjetipoPK(codobj, codtip);
    }

    public ObjetipoPK getObjetipoPK() {
        return objetipoPK;
    }

    public void setObjetipoPK(ObjetipoPK objetipoPK) {
        this.objetipoPK = objetipoPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (objetipoPK != null ? objetipoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Objetipo)) {
            return false;
        }
        Objetipo other = (Objetipo) object;
        if ((this.objetipoPK == null && other.objetipoPK != null) || (this.objetipoPK != null && !this.objetipoPK.equals(other.objetipoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Objetipo[ objetipoPK=" + objetipoPK + " ]";
    }
    
}
