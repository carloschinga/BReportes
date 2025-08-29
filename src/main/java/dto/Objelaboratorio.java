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
@Table(name = "objelaboratorio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Objelaboratorio.findAll", query = "SELECT o FROM Objelaboratorio o"),
    @NamedQuery(name = "Objelaboratorio.findByCodobj", query = "SELECT o FROM Objelaboratorio o WHERE o.objelaboratorioPK.codobj = :codobj"),
    @NamedQuery(name = "Objelaboratorio.findByCodlab", query = "SELECT o FROM Objelaboratorio o WHERE o.objelaboratorioPK.codlab = :codlab")})
public class Objelaboratorio implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ObjelaboratorioPK objelaboratorioPK;

    public Objelaboratorio() {
    }

    public Objelaboratorio(ObjelaboratorioPK objelaboratorioPK) {
        this.objelaboratorioPK = objelaboratorioPK;
    }

    public Objelaboratorio(int codobj, String codlab) {
        this.objelaboratorioPK = new ObjelaboratorioPK(codobj, codlab);
    }

    public ObjelaboratorioPK getObjelaboratorioPK() {
        return objelaboratorioPK;
    }

    public void setObjelaboratorioPK(ObjelaboratorioPK objelaboratorioPK) {
        this.objelaboratorioPK = objelaboratorioPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (objelaboratorioPK != null ? objelaboratorioPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Objelaboratorio)) {
            return false;
        }
        Objelaboratorio other = (Objelaboratorio) object;
        if ((this.objelaboratorioPK == null && other.objelaboratorioPK != null) || (this.objelaboratorioPK != null && !this.objelaboratorioPK.equals(other.objelaboratorioPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Objelaboratorio[ objelaboratorioPK=" + objelaboratorioPK + " ]";
    }
    
}
