/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import javax.persistence.Column;
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
@Table(name = "fa_inmovilizados")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FaInmovilizados.findAll", query = "SELECT f FROM FaInmovilizados f"),
    @NamedQuery(name = "FaInmovilizados.findByCodpro", query = "SELECT f FROM FaInmovilizados f WHERE f.faInmovilizadosPK.codpro = :codpro"),
    @NamedQuery(name = "FaInmovilizados.findByCodlot", query = "SELECT f FROM FaInmovilizados f WHERE f.faInmovilizadosPK.codlot = :codlot"),
    @NamedQuery(name = "FaInmovilizados.findByCodsubalm", query = "SELECT f FROM FaInmovilizados f WHERE f.faInmovilizadosPK.codsubalm = :codsubalm"),
    @NamedQuery(name = "FaInmovilizados.findByCantE", query = "SELECT f FROM FaInmovilizados f WHERE f.cantE = :cantE"),
    @NamedQuery(name = "FaInmovilizados.findByCantF", query = "SELECT f FROM FaInmovilizados f WHERE f.cantF = :cantF"),
    @NamedQuery(name = "FaInmovilizados.findByCodalm", query = "SELECT f FROM FaInmovilizados f WHERE f.faInmovilizadosPK.codalm = :codalm")})
public class FaInmovilizados implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FaInmovilizadosPK faInmovilizadosPK;
    @Column(name = "cantE")
    private Integer cantE;
    @Column(name = "cantF")
    private Integer cantF;

    public FaInmovilizados() {
    }

    public FaInmovilizados(FaInmovilizadosPK faInmovilizadosPK) {
        this.faInmovilizadosPK = faInmovilizadosPK;
    }

    public FaInmovilizados(String codpro, String codlot, int codsubalm, String codalm) {
        this.faInmovilizadosPK = new FaInmovilizadosPK(codpro, codlot, codsubalm, codalm);
    }

    public FaInmovilizadosPK getFaInmovilizadosPK() {
        return faInmovilizadosPK;
    }

    public void setFaInmovilizadosPK(FaInmovilizadosPK faInmovilizadosPK) {
        this.faInmovilizadosPK = faInmovilizadosPK;
    }

    public Integer getCantE() {
        return cantE;
    }

    public void setCantE(Integer cantE) {
        this.cantE = cantE;
    }

    public Integer getCantF() {
        return cantF;
    }

    public void setCantF(Integer cantF) {
        this.cantF = cantF;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (faInmovilizadosPK != null ? faInmovilizadosPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaInmovilizados)) {
            return false;
        }
        FaInmovilizados other = (FaInmovilizados) object;
        if ((this.faInmovilizadosPK == null && other.faInmovilizadosPK != null) || (this.faInmovilizadosPK != null && !this.faInmovilizadosPK.equals(other.faInmovilizadosPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaInmovilizados[ faInmovilizadosPK=" + faInmovilizadosPK + " ]";
    }
    
}
