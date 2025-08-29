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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "petitorio_tecnico")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "PetitorioTecnico.findAll", query = "SELECT p FROM PetitorioTecnico p"),
        @NamedQuery(name = "PetitorioTecnico.findByCod", query = "SELECT p FROM PetitorioTecnico p WHERE p.cod = :cod"),
        @NamedQuery(name = "PetitorioTecnico.findByCodigo", query = "SELECT p FROM PetitorioTecnico p WHERE p.codigo = :codigo"),
        @NamedQuery(name = "PetitorioTecnico.findByNombre", query = "SELECT p FROM PetitorioTecnico p WHERE p.nombre = :nombre"),
        @NamedQuery(name = "PetitorioTecnico.findByPadre", query = "SELECT p FROM PetitorioTecnico p WHERE p.padre = :padre"),
        @NamedQuery(name = "PetitorioTecnico.findByCncntr", query = "SELECT p FROM PetitorioTecnico p WHERE p.cncntr = :cncntr"),
        @NamedQuery(name = "PetitorioTecnico.findByFormFarma", query = "SELECT p FROM PetitorioTecnico p WHERE p.formFarma = :formFarma"),
        @NamedQuery(name = "PetitorioTecnico.findByPrsntcn", query = "SELECT p FROM PetitorioTecnico p WHERE p.prsntcn = :prsntcn"),
        @NamedQuery(name = "PetitorioTecnico.findByCodigoProducto", query = "SELECT p FROM PetitorioTecnico p WHERE p.codigoProducto = :codigoProducto") })
public class PetitorioTecnico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cod")
    private Integer cod;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "codigo")
    private String codigo;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nombre")
    private String nombre;

    @Size(max = 150)
    @Column(name = "padre")
    private String padre;

    @Size(max = 50)
    @Column(name = "cncntr")
    private String cncntr;

    @Size(max = 50)
    @Column(name = "form_farma")
    private String formFarma;

    @Size(max = 50)
    @Column(name = "prsntcn")
    private String prsntcn;

    @Size(max = 50)
    @Column(name = "codigoProducto")
    private String codigoProducto;

    public PetitorioTecnico() {
    }

    public PetitorioTecnico(Integer cod) {
        this.cod = cod;
    }

    public PetitorioTecnico(Integer cod, String codigo, String nombre) {
        this.cod = cod;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPadre() {
        return padre;
    }

    public void setPadre(String padre) {
        this.padre = padre;
    }

    public String getCncntr() {
        return cncntr;
    }

    public void setCncntr(String cncntr) {
        this.cncntr = cncntr;
    }

    public String getFormFarma() {
        return formFarma;
    }

    public void setFormFarma(String formFarma) {
        this.formFarma = formFarma;
    }

    public String getPrsntcn() {
        return prsntcn;
    }

    public void setPrsntcn(String prsntcn) {
        this.prsntcn = prsntcn;
    }

    public String getCodigoProducto() {
        return this.codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cod != null ? cod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PetitorioTecnico)) {
            return false;
        }
        PetitorioTecnico other = (PetitorioTecnico) object;
        if ((this.cod == null && other.cod != null) || (this.cod != null && !this.cod.equals(other.cod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.PetitorioTecnico[ cod=" + cod + " ]";
    }
}