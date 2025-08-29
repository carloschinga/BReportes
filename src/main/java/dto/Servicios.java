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
@Table(name = "servicios")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Servicios.findAll", query = "SELECT s.sercod,s.serdes FROM Servicios s where s.estado='S' order by s.serdes"),
    @NamedQuery(name = "Servicios.findBySercod", query = "SELECT s FROM Servicios s WHERE s.sercod = :sercod"),
    @NamedQuery(name = "Servicios.findBySerdes", query = "SELECT s FROM Servicios s WHERE s.serdes = :serdes"),
    @NamedQuery(name = "Servicios.findByEstado", query = "SELECT s FROM Servicios s WHERE s.estado = :estado"),
    @NamedQuery(name = "Servicios.findByFeccre", query = "SELECT s FROM Servicios s WHERE s.feccre = :feccre"),
    @NamedQuery(name = "Servicios.findByFecumv", query = "SELECT s FROM Servicios s WHERE s.fecumv = :fecumv"),
    @NamedQuery(name = "Servicios.findByUsecod", query = "SELECT s FROM Servicios s WHERE s.usecod = :usecod"),
    @NamedQuery(name = "Servicios.findByUsenam", query = "SELECT s FROM Servicios s WHERE s.usenam = :usenam"),
    @NamedQuery(name = "Servicios.findByHostname", query = "SELECT s FROM Servicios s WHERE s.hostname = :hostname"),
    @NamedQuery(name = "Servicios.findByCencos", query = "SELECT s FROM Servicios s WHERE s.cencos = :cencos"),
    @NamedQuery(name = "Servicios.findBySercodA", query = "SELECT s FROM Servicios s WHERE s.sercodA = :sercodA"),
    @NamedQuery(name = "Servicios.findByDismon", query = "SELECT s FROM Servicios s WHERE s.dismon = :dismon"),
    @NamedQuery(name = "Servicios.findBySercodAd", query = "SELECT s FROM Servicios s WHERE s.sercodAd = :sercodAd"),
    @NamedQuery(name = "Servicios.findBySercodQull", query = "SELECT s FROM Servicios s WHERE s.sercodQull = :sercodQull"),
    @NamedQuery(name = "Servicios.findBySerdesL", query = "SELECT s FROM Servicios s WHERE s.serdesL = :serdesL"),
    @NamedQuery(name = "Servicios.findByTarcodL", query = "SELECT s FROM Servicios s WHERE s.tarcodL = :tarcodL"),
    @NamedQuery(name = "Servicios.findByTarcodLCv", query = "SELECT s FROM Servicios s WHERE s.tarcodLCv = :tarcodLCv"),
    @NamedQuery(name = "Servicios.findByDurmedocu", query = "SELECT s FROM Servicios s WHERE s.durmedocu = :durmedocu")})
public class Servicios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "sercod")
    private String sercod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "serdes")
    private String serdes;
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
    @Size(min = 1, max = 15)
    @Column(name = "cencos")
    private String cencos;
    @Size(max = 6)
    @Column(name = "sercod_a")
    private String sercodA;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "dismon")
    private String dismon;
    @Size(max = 21)
    @Column(name = "sercod_ad")
    private String sercodAd;
    @Size(max = 10)
    @Column(name = "sercod_qull")
    private String sercodQull;
    @Size(max = 100)
    @Column(name = "serdes_l")
    private String serdesL;
    @Size(max = 8)
    @Column(name = "tarcod_l")
    private String tarcodL;
    @Size(max = 8)
    @Column(name = "tarcod_l_cv")
    private String tarcodLCv;
    @Basic(optional = false)
    @NotNull
    @Column(name = "durmedocu")
    private int durmedocu;

    public Servicios() {
    }

    public Servicios(String sercod) {
        this.sercod = sercod;
    }

    public Servicios(String sercod, String serdes, String estado, Date feccre, Date fecumv, int usecod, String usenam, String hostname, String cencos, String dismon, int durmedocu) {
        this.sercod = sercod;
        this.serdes = serdes;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
        this.cencos = cencos;
        this.dismon = dismon;
        this.durmedocu = durmedocu;
    }

    public String getSercod() {
        return sercod;
    }

    public void setSercod(String sercod) {
        this.sercod = sercod;
    }

    public String getSerdes() {
        return serdes;
    }

    public void setSerdes(String serdes) {
        this.serdes = serdes;
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

    public String getCencos() {
        return cencos;
    }

    public void setCencos(String cencos) {
        this.cencos = cencos;
    }

    public String getSercodA() {
        return sercodA;
    }

    public void setSercodA(String sercodA) {
        this.sercodA = sercodA;
    }

    public String getDismon() {
        return dismon;
    }

    public void setDismon(String dismon) {
        this.dismon = dismon;
    }

    public String getSercodAd() {
        return sercodAd;
    }

    public void setSercodAd(String sercodAd) {
        this.sercodAd = sercodAd;
    }

    public String getSercodQull() {
        return sercodQull;
    }

    public void setSercodQull(String sercodQull) {
        this.sercodQull = sercodQull;
    }

    public String getSerdesL() {
        return serdesL;
    }

    public void setSerdesL(String serdesL) {
        this.serdesL = serdesL;
    }

    public String getTarcodL() {
        return tarcodL;
    }

    public void setTarcodL(String tarcodL) {
        this.tarcodL = tarcodL;
    }

    public String getTarcodLCv() {
        return tarcodLCv;
    }

    public void setTarcodLCv(String tarcodLCv) {
        this.tarcodLCv = tarcodLCv;
    }

    public int getDurmedocu() {
        return durmedocu;
    }

    public void setDurmedocu(int durmedocu) {
        this.durmedocu = durmedocu;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (sercod != null ? sercod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Servicios)) {
            return false;
        }
        Servicios other = (Servicios) object;
        if ((this.sercod == null && other.sercod != null) || (this.sercod != null && !this.sercod.equals(other.sercod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Servicios[ sercod=" + sercod + " ]";
    }
    
}
