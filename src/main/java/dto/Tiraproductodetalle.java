/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "tiraproductodetalle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tiraproductodetalle.findAll", query = "SELECT t FROM Tiraproductodetalle t"),
    @NamedQuery(name = "Tiraproductodetalle.findByCodtiritm", query = "SELECT t FROM Tiraproductodetalle t WHERE t.codtiritm = :codtiritm"),
    @NamedQuery(name = "Tiraproductodetalle.findByCodtir", query = "SELECT t FROM Tiraproductodetalle t WHERE t.codtir = :codtir"),
    @NamedQuery(name = "Tiraproductodetalle.findByCodpro", query = "SELECT t FROM Tiraproductodetalle t WHERE t.codpro = :codpro")})
public class Tiraproductodetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codtiritm")
    private Integer codtiritm;
    @Column(name = "codtir")
    private Integer codtir;
    @Size(max = 5)
    @Column(name = "codpro")
    private String codpro;

    public Tiraproductodetalle() {
    }

    public Tiraproductodetalle(Integer codtiritm) {
        this.codtiritm = codtiritm;
    }

    public Integer getCodtiritm() {
        return codtiritm;
    }

    public void setCodtiritm(Integer codtiritm) {
        this.codtiritm = codtiritm;
    }

    public Integer getCodtir() {
        return codtir;
    }

    public void setCodtir(Integer codtir) {
        this.codtir = codtir;
    }

    public String getCodpro() {
        return codpro;
    }

    public void setCodpro(String codpro) {
        this.codpro = codpro;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codtiritm != null ? codtiritm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tiraproductodetalle)) {
            return false;
        }
        Tiraproductodetalle other = (Tiraproductodetalle) object;
        if ((this.codtiritm == null && other.codtiritm != null) || (this.codtiritm != null && !this.codtiritm.equals(other.codtiritm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Tiraproductodetalle[ codtiritm=" + codtiritm + " ]";
    }
    
}
