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
@Table(name = "picking_detalle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PickingDetalle.findAll", query = "SELECT p FROM PickingDetalle p"),
    @NamedQuery(name = "PickingDetalle.findByPickdetcod", query = "SELECT p FROM PickingDetalle p WHERE p.pickdetcod = :pickdetcod"),
    @NamedQuery(name = "PickingDetalle.findByPickcod", query = "SELECT p FROM PickingDetalle p WHERE p.pickcod = :pickcod"),
    @NamedQuery(name = "PickingDetalle.findByCodpro", query = "SELECT p FROM PickingDetalle p WHERE p.codpro = :codpro"),
    @NamedQuery(name = "PickingDetalle.findBySiscod", query = "SELECT p FROM PickingDetalle p WHERE p.siscod = :siscod"),
    @NamedQuery(name = "PickingDetalle.findByLote", query = "SELECT p FROM PickingDetalle p WHERE p.lote = :lote"),
    @NamedQuery(name = "PickingDetalle.findByCante", query = "SELECT p FROM PickingDetalle p WHERE p.cante = :cante"),
    @NamedQuery(name = "PickingDetalle.findByCantf", query = "SELECT p FROM PickingDetalle p WHERE p.cantf = :cantf"),
    @NamedQuery(name = "PickingDetalle.findByUsecod", query = "SELECT p FROM PickingDetalle p WHERE p.usecod = :usecod"),
    @NamedQuery(name = "PickingDetalle.findByInvnum", query = "SELECT p FROM PickingDetalle p WHERE p.invnum = :invnum"),
    @NamedQuery(name = "PickingDetalle.findByNumitm", query = "SELECT p FROM PickingDetalle p WHERE p.numitm = :numitm")})
public class PickingDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pickdetcod")
    private Integer pickdetcod;
    @Column(name = "pickcod")
    private Integer pickcod;
    @Size(max = 5)
    @Column(name = "codpro")
    private String codpro;
    @Column(name = "siscod")
    private Integer siscod;
    @Size(max = 15)
    @Column(name = "lote")
    private String lote;
    @Column(name = "cante")
    private Integer cante;
    @Column(name = "cantf")
    private Integer cantf;
    @Column(name = "usecod")
    private Integer usecod;
    @Column(name = "invnum")
    private Integer invnum;
    @Column(name = "numitm")
    private Integer numitm;
    @Size(max = 1)
    @Column(name = "chkpick")
    private String chkpick;
    @Size(max = 1)
    @Column(name = "chkean13")
    private String chkean13;
    @Column(name = "usuean13")
    private Integer usuean13;
    @Column(name = "usuean13adm")
    private Integer usuean13adm;
    @Column(name = "usupick")
    private Integer usupick;
    @Column(name = "usupickadm")
    private Integer usupickadm;
    @Column(name = "fecipick")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecipick;
    @Column(name = "fecfpick")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecfpick;
    @Column(name = "usudelpick")
    private Integer usudelpick;
    @Column(name = "usudelpickadm")
    private Integer usudelpickadm;
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;

    public Date getFeccre() {
        return feccre;
    }

    public void setFeccre(Date feccre) {
        this.feccre = feccre;
    }

    public Integer getUsudelpick() {
        return usudelpick;
    }

    public void setUsudelpick(Integer usudelpick) {
        this.usudelpick = usudelpick;
    }

    public Integer getUsudelpickadm() {
        return usudelpickadm;
    }

    public void setUsudelpickadm(Integer usudelpickadm) {
        this.usudelpickadm = usudelpickadm;
    }

    public Integer getUsupick() {
        return usupick;
    }

    public void setUsupick(Integer usupick) {
        this.usupick = usupick;
    }

    public Integer getUsupickadm() {
        return usupickadm;
    }

    public void setUsupickadm(Integer usupickadm) {
        this.usupickadm = usupickadm;
    }

    public Date getFecipick() {
        return fecipick;
    }

    public void setFecipick(Date fecipick) {
        this.fecipick = fecipick;
    }

    public Date getFecfpick() {
        return fecfpick;
    }

    public void setFecfpick(Date fecfpick) {
        this.fecfpick = fecfpick;
    }
    
    public Integer getUsuean13() {
        return usuean13;
    }

    public void setUsuean13(Integer usuean13) {
        this.usuean13 = usuean13;
    }

    public Integer getUsuean13adm() {
        return usuean13adm;
    }

    public void setUsuean13adm(Integer usuean13adm) {
        this.usuean13adm = usuean13adm;
    }

    public String getChkean13() {
        return chkean13;
    }

    public void setChkean13(String chkean13) {
        this.chkean13 = chkean13;
    }

    public String getChkpick() {
        return chkpick;
    }

    public void setChkpick(String chkpick) {
        this.chkpick = chkpick;
    }

    public PickingDetalle() {
    }

    public PickingDetalle(Integer pickdetcod) {
        this.pickdetcod = pickdetcod;
    }

    public Integer getPickdetcod() {
        return pickdetcod;
    }

    public void setPickdetcod(Integer pickdetcod) {
        this.pickdetcod = pickdetcod;
    }

    public Integer getPickcod() {
        return pickcod;
    }

    public void setPickcod(Integer pickcod) {
        this.pickcod = pickcod;
    }

    public String getCodpro() {
        return codpro;
    }

    public void setCodpro(String codpro) {
        this.codpro = codpro;
    }

    public Integer getSiscod() {
        return siscod;
    }

    public void setSiscod(Integer siscod) {
        this.siscod = siscod;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
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

    public Integer getUsecod() {
        return usecod;
    }

    public void setUsecod(Integer usecod) {
        this.usecod = usecod;
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
        hash += (pickdetcod != null ? pickdetcod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PickingDetalle)) {
            return false;
        }
        PickingDetalle other = (PickingDetalle) object;
        if ((this.pickdetcod == null && other.pickdetcod != null) || (this.pickdetcod != null && !this.pickdetcod.equals(other.pickdetcod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.PickingDetalle[ pickdetcod=" + pickdetcod + " ]";
    }
    
}
