/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.login.dto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "usuarios_bartolito")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "UsuariosBartolito.findAll", query = "SELECT u.usecod,u.useusr,u.usenam,u.codalm FROM UsuariosBartolito u where u.estado='S'"),
        @NamedQuery(name = "UsuariosBartolito.findByUsecod", query = "SELECT u FROM UsuariosBartolito u WHERE u.usecod = :usecod"),
        @NamedQuery(name = "UsuariosBartolito.findByUsenam", query = "SELECT u FROM UsuariosBartolito u WHERE u.usenam = :usenam"),
        @NamedQuery(name = "UsuariosBartolito.findByUseusr", query = "SELECT u FROM UsuariosBartolito u WHERE u.useusr = :useusr"),
        @NamedQuery(name = "UsuariosBartolito.findByFeccre", query = "SELECT u FROM UsuariosBartolito u WHERE u.feccre = :feccre"),
        @NamedQuery(name = "UsuariosBartolito.findByFecumv", query = "SELECT u FROM UsuariosBartolito u WHERE u.fecumv = :fecumv"),
        @NamedQuery(name = "UsuariosBartolito.findByEstado", query = "SELECT u FROM UsuariosBartolito u WHERE u.estado = :estado"),
        @NamedQuery(name = "UsuariosBartolito.findByCodalm", query = "SELECT u FROM UsuariosBartolito u WHERE u.codalm = :codalm"),
        @NamedQuery(name = "UsuariosBartolito.findByCodrol", query = "SELECT u FROM UsuariosBartolito u WHERE u.codrol = :codrol") })
public class UsuariosBartolito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "usecod")
    private Integer usecod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "usenam")
    private String usenam;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "useusr")
    private String useusr;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "usepas")
    private byte[] usepas;
    @Basic(optional = false)
    @NotNull
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;
    @Column(name = "fecumv")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecumv;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "codalm")
    private String codalm;
    @Column(name = "codrol")
    private Integer codrol;
    // Nuevo campo grucod
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "grucod")
    private String grucod = "OPE"; // Valor por defecto

    public UsuariosBartolito() {
    }

    public UsuariosBartolito(Integer usecod) {
        this.usecod = usecod;
    }

    public UsuariosBartolito(Integer usecod, String usenam, String useusr, byte[] usepas, Date feccre, String estado,
            String codalm) {
        this.usecod = usecod;
        this.usenam = usenam;
        this.useusr = useusr;
        this.usepas = usepas;
        this.feccre = feccre;
        this.estado = estado;
        this.codalm = codalm;
        this.grucod = "OPE";
    }

    // Métodos getter y setter para grucod
    public String getGrucod() {
        return grucod;
    }

    public void setGrucod(String grucod) {
        this.grucod = grucod;
    }

    public Integer getUsecod() {
        return usecod;
    }

    public void setUsecod(Integer usecod) {
        this.usecod = usecod;
    }

    public String getUsenam() {
        return usenam;
    }

    public void setUsenam(String usenam) {
        this.usenam = usenam;
    }

    public String getUseusr() {
        return useusr;
    }

    public void setUseusr(String useusr) {
        this.useusr = useusr;
    }

    public byte[] getUsepas() {
        return usepas;
    }

    public void setUsepas(byte[] usepas) {
        this.usepas = usepas;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
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
        hash += (usecod != null ? usecod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuariosBartolito)) {
            return false;
        }
        UsuariosBartolito other = (UsuariosBartolito) object;
        if ((this.usecod == null && other.usecod != null)
                || (this.usecod != null && !this.usecod.equals(other.usecod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.UsuariosBartolito[ usecod=" + usecod + " ]";
    }

}
