/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "parametros_bartolito")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParametrosBartolito.findAll", query = "SELECT p FROM ParametrosBartolito p"),
    @NamedQuery(name = "ParametrosBartolito.findByCodparam", query = "SELECT p FROM ParametrosBartolito p WHERE p.codparam = :codparam"),
    @NamedQuery(name = "ParametrosBartolito.findDisPro", query = "SELECT p.valor FROM ParametrosBartolito p WHERE p.codparam = 'dipro'"),
    @NamedQuery(name = "ParametrosBartolito.findByDesparam", query = "SELECT p FROM ParametrosBartolito p WHERE p.desparam = :desparam"),
    @NamedQuery(name = "ParametrosBartolito.findByValor", query = "SELECT p FROM ParametrosBartolito p WHERE p.valor = :valor")})
public class ParametrosBartolito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "codparam")
    private String codparam;
    @Size(max = 50)
    @Column(name = "desparam")
    private String desparam;
    @Column(name = "valor")
    private Integer valor;

    public ParametrosBartolito() {
    }

    public ParametrosBartolito(String codparam) {
        this.codparam = codparam;
    }

    public String getCodparam() {
        return codparam;
    }

    public void setCodparam(String codparam) {
        this.codparam = codparam;
    }

    public String getDesparam() {
        return desparam;
    }

    public void setDesparam(String desparam) {
        this.desparam = desparam;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codparam != null ? codparam.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ParametrosBartolito)) {
            return false;
        }
        ParametrosBartolito other = (ParametrosBartolito) object;
        if ((this.codparam == null && other.codparam != null) || (this.codparam != null && !this.codparam.equals(other.codparam))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.ParametrosBartolito[ codparam=" + codparam + " ]";
    }
    
}
