/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "tiraproductocabecera")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tiraproductocabecera.findAll", query = "SELECT t FROM Tiraproductocabecera t"),
    @NamedQuery(name = "Tiraproductocabecera.findUltCodtir", query = "SELECT t.codtir FROM Tiraproductocabecera t order by t.codtir desc"),
    @NamedQuery(name = "Tiraproductocabecera.findByCodtir", query = "SELECT t FROM Tiraproductocabecera t WHERE t.codtir = :codtir"),
    @NamedQuery(name = "Tiraproductocabecera.findByTirnam", query = "SELECT t FROM Tiraproductocabecera t WHERE t.tirnam = :tirnam"),
    @NamedQuery(name = "Tiraproductocabecera.findByUsecod", query = "SELECT t FROM Tiraproductocabecera t WHERE t.usecod = :usecod"),
    @NamedQuery(name = "Tiraproductocabecera.findByFeccre", query = "SELECT t FROM Tiraproductocabecera t WHERE t.feccre = :feccre")})
public class Tiraproductocabecera implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codtir")
    private Integer codtir;
    @Size(max = 120)
    @Column(name = "tirnam")
    private String tirnam;
    @Column(name = "usecod")
    private Integer usecod;
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;

    public Tiraproductocabecera() {
    }

    public Tiraproductocabecera(Integer codtir) {
        this.codtir = codtir;
    }

    public Integer getCodtir() {
        return codtir;
    }

    public void setCodtir(Integer codtir) {
        this.codtir = codtir;
    }

    public String getTirnam() {
        return tirnam;
    }

    public void setTirnam(String tirnam) {
        this.tirnam = tirnam;
    }

    public Integer getUsecod() {
        return usecod;
    }

    public void setUsecod(Integer usecod) {
        this.usecod = usecod;
    }

    public Date getFeccre() {
        return feccre;
    }

    public void setFeccre(Date feccre) {
        this.feccre = feccre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codtir != null ? codtir.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tiraproductocabecera)) {
            return false;
        }
        Tiraproductocabecera other = (Tiraproductocabecera) object;
        if ((this.codtir == null && other.codtir != null) || (this.codtir != null && !this.codtir.equals(other.codtir))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Tiraproductocabecera[ codtir=" + codtir + " ]";
    }
    
}
