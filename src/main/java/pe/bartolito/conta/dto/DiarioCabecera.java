/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "DiarioCabecera")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DiarioCabecera.findAll", query = "SELECT d FROM DiarioCabecera d"),
    @NamedQuery(name = "DiarioCabecera.findByDiaCabCompId", query = "SELECT d FROM DiarioCabecera d WHERE d.diaCabCompId = :diaCabCompId"),
    @NamedQuery(name = "DiarioCabecera.findByDiaCabAno", query = "SELECT d FROM DiarioCabecera d WHERE d.diaCabAno = :diaCabAno"),
    @NamedQuery(name = "DiarioCabecera.findByDiaCabMes", query = "SELECT d FROM DiarioCabecera d WHERE d.diaCabMes = :diaCabMes"),
    @NamedQuery(name = "DiarioCabecera.findByDiaCabFec", query = "SELECT d FROM DiarioCabecera d WHERE d.diaCabFec = :diaCabFec"),
    @NamedQuery(name = "DiarioCabecera.findByDiaCabAccImp", query = "SELECT d FROM DiarioCabecera d WHERE d.diaCabAccImp = :diaCabAccImp"),
    @NamedQuery(name = "DiarioCabecera.findByDiaCabAccEdi", query = "SELECT d FROM DiarioCabecera d WHERE d.diaCabAccEdi = :diaCabAccEdi"),
    @NamedQuery(name = "DiarioCabecera.findByUsrSistema", query = "SELECT d FROM DiarioCabecera d WHERE d.usrSistema = :usrSistema"),
    @NamedQuery(name = "DiarioCabecera.findByFecSistema", query = "SELECT d FROM DiarioCabecera d WHERE d.fecSistema = :fecSistema"),
    @NamedQuery(name = "DiarioCabecera.findByHrsSistema", query = "SELECT d FROM DiarioCabecera d WHERE d.hrsSistema = :hrsSistema")})
public class DiarioCabecera implements Serializable {

    @Lob
    @Size(max = 2147483647)
    @Column(name = "DiaCabGlosa")
    private String diaCabGlosa;
    @Size(max = 10)
    @Column(name = "UsrSistema")
    private String usrSistema;
    @Size(max = 10)
    @Column(name = "HrsSistema")
    private String hrsSistema;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "diarioCabecera")
    private List<DiarioDetalle> diarioDetalleList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "DiaCabCompId")
    private Long diaCabCompId;
    @Column(name = "DiaCabAno")
    private Integer diaCabAno;
    @Column(name = "DiaCabMes")
    private Integer diaCabMes;
    @Column(name = "DiaCabFec")
    @Temporal(TemporalType.TIMESTAMP)
    private Date diaCabFec;
    @Column(name = "DiaCabAccImp")
    private Character diaCabAccImp;
    @Column(name = "DiaCabAccEdi")
    private Character diaCabAccEdi;
    @Column(name = "FecSistema")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecSistema;
    @JoinColumn(name = "EmpresaId", referencedColumnName = "EmpresaId")
    @ManyToOne
    private Empresa empresaId;
    @JoinColumns({
        @JoinColumn(name = "MovConId", referencedColumnName = "MovConId"),
        @JoinColumn(name = "SubMovId", referencedColumnName = "SubMovId")})
    @ManyToOne
    private SubMovimientoContable subMovimientoContable;

    public DiarioCabecera() {
    }

    public DiarioCabecera(Long diaCabCompId) {
        this.diaCabCompId = diaCabCompId;
    }

    public Long getDiaCabCompId() {
        return diaCabCompId;
    }

    public void setDiaCabCompId(Long diaCabCompId) {
        this.diaCabCompId = diaCabCompId;
    }

    public Integer getDiaCabAno() {
        return diaCabAno;
    }

    public void setDiaCabAno(Integer diaCabAno) {
        this.diaCabAno = diaCabAno;
    }

    public Integer getDiaCabMes() {
        return diaCabMes;
    }

    public void setDiaCabMes(Integer diaCabMes) {
        this.diaCabMes = diaCabMes;
    }

    public Date getDiaCabFec() {
        return diaCabFec;
    }

    public void setDiaCabFec(Date diaCabFec) {
        this.diaCabFec = diaCabFec;
    }


    public Character getDiaCabAccImp() {
        return diaCabAccImp;
    }

    public void setDiaCabAccImp(Character diaCabAccImp) {
        this.diaCabAccImp = diaCabAccImp;
    }

    public Character getDiaCabAccEdi() {
        return diaCabAccEdi;
    }

    public void setDiaCabAccEdi(Character diaCabAccEdi) {
        this.diaCabAccEdi = diaCabAccEdi;
    }


    public Date getFecSistema() {
        return fecSistema;
    }

    public void setFecSistema(Date fecSistema) {
        this.fecSistema = fecSistema;
    }


    public Empresa getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Empresa empresaId) {
        this.empresaId = empresaId;
    }

    public SubMovimientoContable getSubMovimientoContable() {
        return subMovimientoContable;
    }

    public void setSubMovimientoContable(SubMovimientoContable subMovimientoContable) {
        this.subMovimientoContable = subMovimientoContable;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (diaCabCompId != null ? diaCabCompId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DiarioCabecera)) {
            return false;
        }
        DiarioCabecera other = (DiarioCabecera) object;
        if ((this.diaCabCompId == null && other.diaCabCompId != null) || (this.diaCabCompId != null && !this.diaCabCompId.equals(other.diaCabCompId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.conta.DiarioCabecera[ diaCabCompId=" + diaCabCompId + " ]";
    }

    public String getDiaCabGlosa() {
        return diaCabGlosa;
    }

    public void setDiaCabGlosa(String diaCabGlosa) {
        this.diaCabGlosa = diaCabGlosa;
    }

    public String getUsrSistema() {
        return usrSistema;
    }

    public void setUsrSistema(String usrSistema) {
        this.usrSistema = usrSistema;
    }

    public String getHrsSistema() {
        return hrsSistema;
    }

    public void setHrsSistema(String hrsSistema) {
        this.hrsSistema = hrsSistema;
    }

    @XmlTransient
    public List<DiarioDetalle> getDiarioDetalleList() {
        return diarioDetalleList;
    }

    public void setDiarioDetalleList(List<DiarioDetalle> diarioDetalleList) {
        this.diarioDetalleList = diarioDetalleList;
    }
    
}
