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
@Table(name = "fa_transportistas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FaTransportistas.findAll", query = "SELECT f FROM FaTransportistas f"),
    @NamedQuery(name = "FaTransportistas.findAllJSON", query = "SELECT f.codtrans,f.namtrans,f.placavehic,f.pattrans,f.mattrans,f.nomtrans,f.codmodt FROM FaTransportistas f where f.estado='S'"),
    @NamedQuery(name = "FaTransportistas.findByCodtrans", query = "SELECT f FROM FaTransportistas f WHERE f.codtrans = :codtrans"),
    @NamedQuery(name = "FaTransportistas.findByNamtrans", query = "SELECT f FROM FaTransportistas f WHERE f.namtrans = :namtrans"),
    @NamedQuery(name = "FaTransportistas.findByDoctrans", query = "SELECT f FROM FaTransportistas f WHERE f.doctrans = :doctrans"),
    @NamedQuery(name = "FaTransportistas.findByLicentrans", query = "SELECT f FROM FaTransportistas f WHERE f.licentrans = :licentrans"),
    @NamedQuery(name = "FaTransportistas.findByPlacavehic", query = "SELECT f FROM FaTransportistas f WHERE f.placavehic = :placavehic"),
    @NamedQuery(name = "FaTransportistas.findByMarcavehic", query = "SELECT f FROM FaTransportistas f WHERE f.marcavehic = :marcavehic"),
    @NamedQuery(name = "FaTransportistas.findByObstrans", query = "SELECT f FROM FaTransportistas f WHERE f.obstrans = :obstrans"),
    @NamedQuery(name = "FaTransportistas.findByEstado", query = "SELECT f FROM FaTransportistas f WHERE f.estado = :estado"),
    @NamedQuery(name = "FaTransportistas.findByFeccre", query = "SELECT f FROM FaTransportistas f WHERE f.feccre = :feccre"),
    @NamedQuery(name = "FaTransportistas.findByFecumv", query = "SELECT f FROM FaTransportistas f WHERE f.fecumv = :fecumv"),
    @NamedQuery(name = "FaTransportistas.findByUsecod", query = "SELECT f FROM FaTransportistas f WHERE f.usecod = :usecod"),
    @NamedQuery(name = "FaTransportistas.findByUsenam", query = "SELECT f FROM FaTransportistas f WHERE f.usenam = :usenam"),
    @NamedQuery(name = "FaTransportistas.findByHostname", query = "SELECT f FROM FaTransportistas f WHERE f.hostname = :hostname"),
    @NamedQuery(name = "FaTransportistas.findByPattrans", query = "SELECT f FROM FaTransportistas f WHERE f.pattrans = :pattrans"),
    @NamedQuery(name = "FaTransportistas.findByMattrans", query = "SELECT f FROM FaTransportistas f WHERE f.mattrans = :mattrans"),
    @NamedQuery(name = "FaTransportistas.findByNomtrans", query = "SELECT f FROM FaTransportistas f WHERE f.nomtrans = :nomtrans"),
    @NamedQuery(name = "FaTransportistas.findByCodmodt", query = "SELECT f FROM FaTransportistas f WHERE f.codmodt = :codmodt")})
public class FaTransportistas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "codtrans")
    private String codtrans;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "namtrans")
    private String namtrans;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "doctrans")
    private String doctrans;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "licentrans")
    private String licentrans;
    @Size(max = 20)
    @Column(name = "placavehic")
    private String placavehic;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "marcavehic")
    private String marcavehic;
    @Size(max = 60)
    @Column(name = "obstrans")
    private String obstrans;
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "pattrans")
    private String pattrans;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "mattrans")
    private String mattrans;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nomtrans")
    private String nomtrans;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "codmodt")
    private String codmodt;

    public FaTransportistas() {
    }

    public FaTransportistas(String codtrans) {
        this.codtrans = codtrans;
    }

    public FaTransportistas(String codtrans, String namtrans, String doctrans, String licentrans, String marcavehic, String estado, Date feccre, Date fecumv, int usecod, String usenam, String hostname, String pattrans, String mattrans, String nomtrans, String codmodt) {
        this.codtrans = codtrans;
        this.namtrans = namtrans;
        this.doctrans = doctrans;
        this.licentrans = licentrans;
        this.marcavehic = marcavehic;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
        this.pattrans = pattrans;
        this.mattrans = mattrans;
        this.nomtrans = nomtrans;
        this.codmodt = codmodt;
    }

    public String getCodtrans() {
        return codtrans;
    }

    public void setCodtrans(String codtrans) {
        this.codtrans = codtrans;
    }

    public String getNamtrans() {
        return namtrans;
    }

    public void setNamtrans(String namtrans) {
        this.namtrans = namtrans;
    }

    public String getDoctrans() {
        return doctrans;
    }

    public void setDoctrans(String doctrans) {
        this.doctrans = doctrans;
    }

    public String getLicentrans() {
        return licentrans;
    }

    public void setLicentrans(String licentrans) {
        this.licentrans = licentrans;
    }

    public String getPlacavehic() {
        return placavehic;
    }

    public void setPlacavehic(String placavehic) {
        this.placavehic = placavehic;
    }

    public String getMarcavehic() {
        return marcavehic;
    }

    public void setMarcavehic(String marcavehic) {
        this.marcavehic = marcavehic;
    }

    public String getObstrans() {
        return obstrans;
    }

    public void setObstrans(String obstrans) {
        this.obstrans = obstrans;
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

    public String getPattrans() {
        return pattrans;
    }

    public void setPattrans(String pattrans) {
        this.pattrans = pattrans;
    }

    public String getMattrans() {
        return mattrans;
    }

    public void setMattrans(String mattrans) {
        this.mattrans = mattrans;
    }

    public String getNomtrans() {
        return nomtrans;
    }

    public void setNomtrans(String nomtrans) {
        this.nomtrans = nomtrans;
    }

    public String getCodmodt() {
        return codmodt;
    }

    public void setCodmodt(String codmodt) {
        this.codmodt = codmodt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codtrans != null ? codtrans.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaTransportistas)) {
            return false;
        }
        FaTransportistas other = (FaTransportistas) object;
        if ((this.codtrans == null && other.codtrans != null) || (this.codtrans != null && !this.codtrans.equals(other.codtrans))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaTransportistas[ codtrans=" + codtrans + " ]";
    }
    
}
