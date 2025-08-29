/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.login.dto;

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
@Table(name = "pagina")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pagina.findAll", query = "SELECT p FROM Pagina p"),
    @NamedQuery(name = "Pagina.findByCodpag", query = "SELECT p FROM Pagina p WHERE p.codpag = :codpag"),
    @NamedQuery(name = "Pagina.findByNompag", query = "SELECT p FROM Pagina p WHERE p.nompag = :nompag"),
    @NamedQuery(name = "Pagina.findByUrlpag", query = "SELECT p FROM Pagina p WHERE p.urlpag = :urlpag"),
    @NamedQuery(name = "Pagina.findByGrupag", query = "SELECT p FROM Pagina p WHERE p.grupag = :grupag"),
    @NamedQuery(name = "Pagina.findByCodrol", query = "SELECT p FROM Pagina p WHERE p.codrol = :codrol")})
public class Pagina implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codpag")
    private Integer codpag;
    @Size(max = 50)
    @Column(name = "nompag")
    private String nompag;
    @Size(max = 50)
    @Column(name = "urlpag")
    private String urlpag;
    @Column(name = "grupag")
    private Boolean grupag;
    @Column(name = "codrol")
    private Integer codrol;

    public Pagina() {
    }

    public Pagina(Integer codpag) {
        this.codpag = codpag;
    }

    public Integer getCodpag() {
        return codpag;
    }

    public void setCodpag(Integer codpag) {
        this.codpag = codpag;
    }

    public String getNompag() {
        return nompag;
    }

    public void setNompag(String nompag) {
        this.nompag = nompag;
    }

    public String getUrlpag() {
        return urlpag;
    }

    public void setUrlpag(String urlpag) {
        this.urlpag = urlpag;
    }

    public Boolean getGrupag() {
        return grupag;
    }

    public void setGrupag(Boolean grupag) {
        this.grupag = grupag;
    }

    public Integer getCodrol() {
        return codrol;
    }

    public void setCodrol(Integer codrol) {
        this.codrol = codrol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codpag != null ? codpag.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pagina)) {
            return false;
        }
        Pagina other = (Pagina) object;
        if ((this.codpag == null && other.codpag != null) || (this.codpag != null && !this.codpag.equals(other.codpag))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Pagina[ codpag=" + codpag + " ]";
    }
    
}
