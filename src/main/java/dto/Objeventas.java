/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "objeventas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Objeventas.findAll", query = "SELECT o FROM Objeventas o"),
    @NamedQuery(name = "Objeventas.findByCodobj", query = "SELECT o FROM Objeventas o WHERE o.codobj = :codobj"),
    @NamedQuery(name = "Objeventas.obtenerultcodobj", query = "SELECT o.codobj FROM Objeventas o order by o.codobj desc"),
    @NamedQuery(name = "Objeventas.findByDesobj", query = "SELECT o FROM Objeventas o WHERE o.desobj = :desobj"),
    @NamedQuery(name = "Objeventas.findByEntero", query = "SELECT o FROM Objeventas o WHERE o.entero = :entero"),
    @NamedQuery(name = "Objeventas.findBySoles", query = "SELECT o FROM Objeventas o WHERE o.soles = :soles"),
    @NamedQuery(name = "Objeventas.findByUsecod", query = "SELECT o FROM Objeventas o WHERE o.usecod = :usecod"),
    @NamedQuery(name = "Objeventas.findByFeccre", query = "SELECT o FROM Objeventas o WHERE o.feccre = :feccre"),
    @NamedQuery(name = "Objeventas.findByFecumv", query = "SELECT o FROM Objeventas o WHERE o.fecumv = :fecumv"),
    @NamedQuery(name = "Objeventas.findByMesano", query = "SELECT o FROM Objeventas o WHERE o.mesano = :mesano")})
public class Objeventas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "codobj")
    private Integer codobj;
    @Size(max = 100)
    @Column(name = "desobj")
    private String desobj;
    @Column(name = "entero")
    private Integer entero;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "soles")
    private BigDecimal soles;
    @Column(name = "usecod")
    private Integer usecod;
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;
    @Column(name = "fecumv")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecumv;
    @Size(max = 10)
    @Column(name = "mesano")
    private String mesano;
    @Size(max = 1)
    @Column(name = "estado")
    private String estado;
    @Size(max = 1)
    @Column(name = "tipo")
    private String tipo;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Objeventas() {
    }

    public Objeventas(Integer codobj) {
        this.codobj = codobj;
    }

    public Integer getCodobj() {
        return codobj;
    }

    public void setCodobj(Integer codobj) {
        this.codobj = codobj;
    }

    public String getDesobj() {
        return desobj;
    }

    public void setDesobj(String desobj) {
        this.desobj = desobj;
    }

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

    public Date getFecumv() {
        return fecumv;
    }

    public void setFecumv(Date fecumv) {
        this.fecumv = fecumv;
    }

    public String getMesano() {
        return mesano;
    }

    public void setMesano(String mesano) {
        this.mesano = mesano;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codobj != null ? codobj.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Objeventas)) {
            return false;
        }
        Objeventas other = (Objeventas) object;
        if ((this.codobj == null && other.codobj != null) || (this.codobj != null && !this.codobj.equals(other.codobj))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Objeventas[ codobj=" + codobj + " ]";
    }

}
