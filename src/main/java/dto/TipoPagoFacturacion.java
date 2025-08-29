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
@Table(name = "tipo_pago_facturacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoPagoFacturacion.findAll", query = "SELECT t FROM TipoPagoFacturacion t"),
    @NamedQuery(name = "TipoPagoFacturacion.findAllJSON", query = "SELECT t.tpacod,t.tpades FROM TipoPagoFacturacion t where t.estado='S'"),
    @NamedQuery(name = "TipoPagoFacturacion.findByTpacod", query = "SELECT t FROM TipoPagoFacturacion t WHERE t.tpacod = :tpacod"),
    @NamedQuery(name = "TipoPagoFacturacion.findByTpades", query = "SELECT t FROM TipoPagoFacturacion t WHERE t.tpades = :tpades"),
    @NamedQuery(name = "TipoPagoFacturacion.findByTpadias", query = "SELECT t FROM TipoPagoFacturacion t WHERE t.tpadias = :tpadias"),
    @NamedQuery(name = "TipoPagoFacturacion.findByEstado", query = "SELECT t FROM TipoPagoFacturacion t WHERE t.estado = :estado"),
    @NamedQuery(name = "TipoPagoFacturacion.findByFeccre", query = "SELECT t FROM TipoPagoFacturacion t WHERE t.feccre = :feccre"),
    @NamedQuery(name = "TipoPagoFacturacion.findByFecumv", query = "SELECT t FROM TipoPagoFacturacion t WHERE t.fecumv = :fecumv"),
    @NamedQuery(name = "TipoPagoFacturacion.findByUsecod", query = "SELECT t FROM TipoPagoFacturacion t WHERE t.usecod = :usecod"),
    @NamedQuery(name = "TipoPagoFacturacion.findByUsenam", query = "SELECT t FROM TipoPagoFacturacion t WHERE t.usenam = :usenam"),
    @NamedQuery(name = "TipoPagoFacturacion.findByHostname", query = "SELECT t FROM TipoPagoFacturacion t WHERE t.hostname = :hostname")})
public class TipoPagoFacturacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "tpacod")
    private String tpacod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "tpades")
    private String tpades;
    @Column(name = "tpadias")
    private Integer tpadias;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecumv")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecumv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usecod")
    private int usecod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "usenam")
    private String usenam;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "hostname")
    private String hostname;

    public TipoPagoFacturacion() {
    }

    public TipoPagoFacturacion(String tpacod) {
        this.tpacod = tpacod;
    }

    public TipoPagoFacturacion(String tpacod, String tpades, String estado, Date feccre, Date fecumv, int usecod, String usenam, String hostname) {
        this.tpacod = tpacod;
        this.tpades = tpades;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
    }

    public String getTpacod() {
        return tpacod;
    }

    public void setTpacod(String tpacod) {
        this.tpacod = tpacod;
    }

    public String getTpades() {
        return tpades;
    }

    public void setTpades(String tpades) {
        this.tpades = tpades;
    }

    public Integer getTpadias() {
        return tpadias;
    }

    public void setTpadias(Integer tpadias) {
        this.tpadias = tpadias;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public int getUsecod() {
        return usecod;
    }

    public void setUsecod(int usecod) {
        this.usecod = usecod;
    }

    public String getUsenam() {
        return usenam;
    }

    public void setUsenam(String usenam) {
        this.usenam = usenam;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tpacod != null ? tpacod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoPagoFacturacion)) {
            return false;
        }
        TipoPagoFacturacion other = (TipoPagoFacturacion) object;
        if ((this.tpacod == null && other.tpacod != null) || (this.tpacod != null && !this.tpacod.equals(other.tpacod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.TipoPagoFacturacion[ tpacod=" + tpacod + " ]";
    }
    
}
