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
@Table(name = "picking_cajas")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "PickingCajas.findAll", query = "SELECT p FROM PickingCajas p"),
        @NamedQuery(name = "PickingCajas.findByPickcajacod", query = "SELECT p FROM PickingCajas p WHERE p.pickcajacod = :pickcajacod"),
        @NamedQuery(name = "PickingCajas.findByPickdetcod", query = "SELECT p FROM PickingCajas p WHERE p.pickdetcod = :pickdetcod"),
        @NamedQuery(name = "PickingCajas.findByCaja", query = "SELECT p FROM PickingCajas p WHERE p.caja = :caja"),
        @NamedQuery(name = "PickingCajas.findByCante", query = "SELECT p FROM PickingCajas p WHERE p.cante = :cante"),
        @NamedQuery(name = "PickingCajas.findByCantf", query = "SELECT p FROM PickingCajas p WHERE p.cantf = :cantf"),
        @NamedQuery(name = "PickingCajas.findByCheckenvio", query = "SELECT p FROM PickingCajas p WHERE p.checkenvio = :checkenvio"),
        @NamedQuery(name = "PickingCajas.findByUsecod", query = "SELECT p FROM PickingCajas p WHERE p.usecod = :usecod"),
        @NamedQuery(name = "PickingCajas.findByFecchk", query = "SELECT p FROM PickingCajas p WHERE p.fecchk = :fecchk"),
        @NamedQuery(name = "PickingCajas.findByCanter", query = "SELECT p FROM PickingCajas p WHERE p.canter = :canter"),
        @NamedQuery(name = "PickingCajas.findByCantfr", query = "SELECT p FROM PickingCajas p WHERE p.cantfr = :cantfr"),
        @NamedQuery(name = "PickingCajas.findByFecinirecep", query = "SELECT p FROM PickingCajas p WHERE p.fecinirecep = :fecinirecep"),
        @NamedQuery(name = "PickingCajas.findByFecfinrecep", query = "SELECT p FROM PickingCajas p WHERE p.fecfinrecep = :fecfinrecep"),
        @NamedQuery(name = "PickingCajas.findByInvnum", query = "SELECT p FROM PickingCajas p WHERE p.invnum = :invnum"),
        @NamedQuery(name = "PickingCajas.findByNumitm", query = "SELECT p FROM PickingCajas p WHERE p.numitm = :numitm") })
public class PickingCajas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pickcajacod")
    private Integer pickcajacod;
    @Column(name = "pickdetcod")
    private Integer pickdetcod;
    @Size(max = 30)
    @Column(name = "caja")
    private String caja;
    @Column(name = "cante")
    private Integer cante;
    @Column(name = "cantf")
    private Integer cantf;
    @Size(max = 1)
    @Column(name = "checkenvio")
    private String checkenvio;
    @Column(name = "usecod")
    private Integer usecod;
    @Column(name = "fecchk")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecchk;
    @Column(name = "canter")
    private Integer canter;
    @Column(name = "cantfr")
    private Integer cantfr;
    @Column(name = "fecinirecep")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecinirecep;
    @Column(name = "fecfinrecep")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecfinrecep;
    @Column(name = "invnum")
    private Integer invnum;
    @Column(name = "numitm")
    private Integer numitm;
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;
    @Column(name = "usecodcree")
    private Integer usecodcree;
    @Column(name = "usecodcreebart")
    private Integer usecodcreebart;
    @Column(name = "ubicacion")
    private String ubicacion;

    public Integer getUsecodcreebart() {
        return usecodcreebart;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setUsecodcreebart(Integer usecodcreebart) {
        this.usecodcreebart = usecodcreebart;
    }

    public Date getFeccre() {
        return feccre;
    }

    public void setFeccre(Date feccre) {
        this.feccre = feccre;
    }

    public Integer getUsecodcree() {
        return usecodcree;
    }

    public void setUsecodcree(Integer usecodcree) {
        this.usecodcree = usecodcree;
    }

    public PickingCajas() {
    }

    public PickingCajas(Integer pickcajacod) {
        this.pickcajacod = pickcajacod;
    }

    public Integer getPickcajacod() {
        return pickcajacod;
    }

    public void setPickcajacod(Integer pickcajacod) {
        this.pickcajacod = pickcajacod;
    }

    public Integer getPickdetcod() {
        return pickdetcod;
    }

    public void setPickdetcod(Integer pickdetcod) {
        this.pickdetcod = pickdetcod;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public Integer getCante() {
        return cante;
    }

    public void setCante(Integer cante) {
        this.cante = cante;
    }

    public Integer getCantf() {
        return cantf;
    }

    public void setCantf(Integer cantf) {
        this.cantf = cantf;
    }

    public String getCheckenvio() {
        return checkenvio;
    }

    public void setCheckenvio(String checkenvio) {
        this.checkenvio = checkenvio;
    }

    public Integer getUsecod() {
        return usecod;
    }

    public void setUsecod(Integer usecod) {
        this.usecod = usecod;
    }

    public Date getFecchk() {
        return fecchk;
    }

    public void setFecchk(Date fecchk) {
        this.fecchk = fecchk;
    }

    public Integer getCanter() {
        return canter;
    }

    public void setCanter(Integer canter) {
        this.canter = canter;
    }

    public Integer getCantfr() {
        return cantfr;
    }

    public void setCantfr(Integer cantfr) {
        this.cantfr = cantfr;
    }

    public Date getFecinirecep() {
        return fecinirecep;
    }

    public void setFecinirecep(Date fecinirecep) {
        this.fecinirecep = fecinirecep;
    }

    public Date getFecfinrecep() {
        return fecfinrecep;
    }

    public void setFecfinrecep(Date fecfinrecep) {
        this.fecfinrecep = fecfinrecep;
    }

    public Integer getInvnum() {
        return invnum;
    }

    public void setInvnum(Integer invnum) {
        this.invnum = invnum;
    }

    public Integer getNumitm() {
        return numitm;
    }

    public void setNumitm(Integer numitm) {
        this.numitm = numitm;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pickcajacod != null ? pickcajacod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PickingCajas)) {
            return false;
        }
        PickingCajas other = (PickingCajas) object;
        if ((this.pickcajacod == null && other.pickcajacod != null)
                || (this.pickcajacod != null && !this.pickcajacod.equals(other.pickcajacod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.PickingCajas[ pickcajacod=" + pickcajacod + " ]";
    }

}