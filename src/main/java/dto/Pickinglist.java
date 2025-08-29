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
@Table(name = "pickinglist")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pickinglist.findAll", query = "SELECT p FROM Pickinglist p"),
    @NamedQuery(name = "Pickinglist.findultcodpicklist", query = "SELECT p.codpicklist FROM Pickinglist p order by p.codpicklist desc"),
    @NamedQuery(name = "Pickinglist.findByCodpicklist", query = "SELECT p FROM Pickinglist p WHERE p.codpicklist = :codpicklist")})
public class Pickinglist implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codpicklist")
    private Integer codpicklist;
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;
    @Column(name = "usecod")
    private Integer usecod;
    @Size(max = 1)
    @Column(name = "completado")
    private String completado;
    @Size(max = 1)
    @Column(name = "txtdescarga")
    private String txtdescarga;
    @Size(max = 1)
    @Column(name = "fectxt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fectxt;
    @Size(max = 1)
    @Column(name = "chktxt")
    private String chktxt;

    public String getChktxt() {
        return chktxt;
    }

    public void setChktxt(String chktxt) {
        this.chktxt = chktxt;
    }

    public Date getFectxt() {
        return fectxt;
    }

    public void setFectxt(Date fectxt) {
        this.fectxt = fectxt;
    }

    public String getTxtdescarga() {
        return txtdescarga;
    }

    public void setTxtdescarga(String txtdescarga) {
        this.txtdescarga = txtdescarga;
    }

    public Date getFeccre() {
        return feccre;
    }

    public void setFeccre(Date feccre) {
        this.feccre = feccre;
    }

    public Integer getUsecod() {
        return usecod;
    }

    public void setUsecod(Integer usecod) {
        this.usecod = usecod;
    }

    public String getCompletado() {
        return completado;
    }

    public void setCompletado(String completado) {
        this.completado = completado;
    }

    public Pickinglist() {
    }

    public Pickinglist(Integer codpicklist) {
        this.codpicklist = codpicklist;
    }

    public Integer getCodpicklist() {
        return codpicklist;
    }

    public void setCodpicklist(Integer codpicklist) {
        this.codpicklist = codpicklist;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codpicklist != null ? codpicklist.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pickinglist)) {
            return false;
        }
        Pickinglist other = (Pickinglist) object;
        if ((this.codpicklist == null && other.codpicklist != null) || (this.codpicklist != null && !this.codpicklist.equals(other.codpicklist))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Pickinglist[ codpicklist=" + codpicklist + " ]";
    }
    
}
