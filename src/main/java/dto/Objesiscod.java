/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "objesiscod")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Objesiscod.findAll", query = "SELECT o FROM Objesiscod o"),
    @NamedQuery(name = "Objesiscod.findByCodobj", query = "SELECT o FROM Objesiscod o WHERE o.objesiscodPK.codobj = :codobj"),
    @NamedQuery(name = "Objesiscod.findByCodobjAndSiscod", query = "SELECT o FROM Objesiscod o WHERE o.objesiscodPK.codobj = :codobj AND o.objesiscodPK.siscod = :siscod"),
    @NamedQuery(name = "Objesiscod.findBySiscod", query = "SELECT o FROM Objesiscod o WHERE o.objesiscodPK.siscod = :siscod"),
    @NamedQuery(name = "Objesiscod.findByMesano", query = "SELECT o FROM Objesiscod o WHERE o.mesano = :mesano")})
public class Objesiscod implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ObjesiscodPK objesiscodPK;
    @Column(name = "entero")
    private Integer entero;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "soles")
    private BigDecimal soles;
    @Size(max = 10)
    @Column(name = "mesano")
    private String mesano;

    public Integer getEntero() {
        return entero;
    }

    public void setEntero(Integer entero) {
        this.entero = entero;
    }

    public BigDecimal getSoles() {
        return soles;
    }

    public void setSoles(BigDecimal soles) {
        this.soles = soles;
    }
    
    public String getMesano() {
        return mesano;
    }

    public void setMesano(String mesano) {
        this.mesano = mesano;
    }

    public Objesiscod() {
    }

    public Objesiscod(ObjesiscodPK objesiscodPK) {
        this.objesiscodPK = objesiscodPK;
    }

    public Objesiscod(int codobj, int siscod) {
        this.objesiscodPK = new ObjesiscodPK(codobj, siscod);
    }

    public ObjesiscodPK getObjesiscodPK() {
        return objesiscodPK;
    }

    public void setObjesiscodPK(ObjesiscodPK objesiscodPK) {
        this.objesiscodPK = objesiscodPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (objesiscodPK != null ? objesiscodPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Objesiscod)) {
            return false;
        }
        Objesiscod other = (Objesiscod) object;
        if ((this.objesiscodPK == null && other.objesiscodPK != null) || (this.objesiscodPK != null && !this.objesiscodPK.equals(other.objesiscodPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Objesiscod[ objesiscodPK=" + objesiscodPK + " ]";
    }
    
}
