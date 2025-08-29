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
@Table(name = "objeestrategico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Objeestrategico.findAll", query = "SELECT o FROM Objeestrategico o"),
    @NamedQuery(name = "Objeestrategico.findByCodobj", query = "SELECT o FROM Objeestrategico o WHERE o.objeestrategicoPK.codobj = :codobj"),
    @NamedQuery(name = "Objeestrategico.findByCompro", query = "SELECT o FROM Objeestrategico o WHERE o.objeestrategicoPK.compro = :compro")})
public class Objeestrategico implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ObjeestrategicoPK objeestrategicoPK;

    public Objeestrategico() {
    }

    public Objeestrategico(ObjeestrategicoPK objeestrategicoPK) {
        this.objeestrategicoPK = objeestrategicoPK;
    }

    public Objeestrategico(int codobj, int compro) {
        this.objeestrategicoPK = new ObjeestrategicoPK(codobj, compro);
    }

    public ObjeestrategicoPK getObjeestrategicoPK() {
        return objeestrategicoPK;
    }

    public void setObjeestrategicoPK(ObjeestrategicoPK objeestrategicoPK) {
        this.objeestrategicoPK = objeestrategicoPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (objeestrategicoPK != null ? objeestrategicoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Objeestrategico)) {
            return false;
        }
        Objeestrategico other = (Objeestrategico) object;
        if ((this.objeestrategicoPK == null && other.objeestrategicoPK != null) || (this.objeestrategicoPK != null && !this.objeestrategicoPK.equals(other.objeestrategicoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Objeestrategico[ objeestrategicoPK=" + objeestrategicoPK + " ]";
    }
    
}
