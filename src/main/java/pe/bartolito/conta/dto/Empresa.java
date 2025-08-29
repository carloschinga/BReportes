/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.conta.dto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
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
@Table(name = "Empresa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empresa.findAll", query = "SELECT e FROM Empresa e"),
    @NamedQuery(name = "Empresa.findByEmpresaId", query = "SELECT e FROM Empresa e WHERE e.empresaId = :empresaId"),
    @NamedQuery(name = "Empresa.findByEmpresaDescripcion", query = "SELECT e FROM Empresa e WHERE e.empresaDescripcion = :empresaDescripcion"),
    @NamedQuery(name = "Empresa.findByEmpresaRazSocial", query = "SELECT e FROM Empresa e WHERE e.empresaRazSocial = :empresaRazSocial"),
    @NamedQuery(name = "Empresa.findByEmpresaTelef01", query = "SELECT e FROM Empresa e WHERE e.empresaTelef01 = :empresaTelef01"),
    @NamedQuery(name = "Empresa.findByEmpresaTelef02", query = "SELECT e FROM Empresa e WHERE e.empresaTelef02 = :empresaTelef02"),
    @NamedQuery(name = "Empresa.findByEmpresaTelef03", query = "SELECT e FROM Empresa e WHERE e.empresaTelef03 = :empresaTelef03"),
    @NamedQuery(name = "Empresa.findByEmpresaRUC", query = "SELECT e FROM Empresa e WHERE e.empresaRUC = :empresaRUC"),
    @NamedQuery(name = "Empresa.findByEmpresaFax", query = "SELECT e FROM Empresa e WHERE e.empresaFax = :empresaFax"),
    @NamedQuery(name = "Empresa.findByEmpresaDirecc", query = "SELECT e FROM Empresa e WHERE e.empresaDirecc = :empresaDirecc"),
    @NamedQuery(name = "Empresa.findByEmpresaAbrev", query = "SELECT e FROM Empresa e WHERE e.empresaAbrev = :empresaAbrev"),
    @NamedQuery(name = "Empresa.findByEmpresaFicEle", query = "SELECT e FROM Empresa e WHERE e.empresaFicEle = :empresaFicEle"),
    @NamedQuery(name = "Empresa.findByEmpresaEst", query = "SELECT e FROM Empresa e WHERE e.empresaEst = :empresaEst"),
    @NamedQuery(name = "Empresa.findByUsrSistema", query = "SELECT e FROM Empresa e WHERE e.usrSistema = :usrSistema"),
    @NamedQuery(name = "Empresa.findByFecSistema", query = "SELECT e FROM Empresa e WHERE e.fecSistema = :fecSistema"),
    @NamedQuery(name = "Empresa.findByHrsSistema", query = "SELECT e FROM Empresa e WHERE e.hrsSistema = :hrsSistema"),
    @NamedQuery(name = "Empresa.findByEmpresaMail", query = "SELECT e FROM Empresa e WHERE e.empresaMail = :empresaMail"),
    @NamedQuery(name = "Empresa.findByEmpFileNameFirma", query = "SELECT e FROM Empresa e WHERE e.empFileNameFirma = :empFileNameFirma"),
    @NamedQuery(name = "Empresa.findByEmpRepLegal", query = "SELECT e FROM Empresa e WHERE e.empRepLegal = :empRepLegal")})
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "EmpresaId")
    private String empresaId;
    @Size(max = 50)
    @Column(name = "EmpresaDescripcion")
    private String empresaDescripcion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "EmpresaRazSocial")
    private String empresaRazSocial;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "EmpresaTelef01")
    private String empresaTelef01;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "EmpresaTelef02")
    private String empresaTelef02;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    @Column(name = "EmpresaTelef03")
    private String empresaTelef03;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "EmpresaRUC")
    private String empresaRUC;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "EmpresaFax")
    private String empresaFax;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "EmpresaDirecc")
    private String empresaDirecc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "EmpresaAbrev")
    private String empresaAbrev;
    @Size(max = 15)
    @Column(name = "EmpresaFicEle")
    private String empresaFicEle;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "EmpresaRubro")
    private String empresaRubro;
    @Basic(optional = false)
    @NotNull
    @Column(name = "EmpresaEst")
    private boolean empresaEst;
    @Size(max = 10)
    @Column(name = "UsrSistema")
    private String usrSistema;
    @Column(name = "FecSistema")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecSistema;
    @Size(max = 10)
    @Column(name = "HrsSistema")
    private String hrsSistema;
    @Size(max = 200)
    @Column(name = "EmpresaMail")
    private String empresaMail;
    @Size(max = 20)
    @Column(name = "EmpFileNameFirma")
    private String empFileNameFirma;
    @Size(max = 100)
    @Column(name = "EmpRepLegal")
    private String empRepLegal;
    @JoinColumns({
        @JoinColumn(name = "DptoGeoId", referencedColumnName = "DptoGeoId"),
        @JoinColumn(name = "ProvGeoId", referencedColumnName = "ProvGeoId"),
        @JoinColumn(name = "DistGeoId", referencedColumnName = "DistGeoId")})
    @ManyToOne
    private DistritoGeografico distritoGeografico;

    public Empresa() {
    }

    public Empresa(String empresaId) {
        this.empresaId = empresaId;
    }

    public Empresa(String empresaId, String empresaRazSocial, String empresaTelef01, String empresaTelef02, String empresaTelef03, String empresaRUC, String empresaFax, String empresaDirecc, String empresaAbrev, boolean empresaEst) {
        this.empresaId = empresaId;
        this.empresaRazSocial = empresaRazSocial;
        this.empresaTelef01 = empresaTelef01;
        this.empresaTelef02 = empresaTelef02;
        this.empresaTelef03 = empresaTelef03;
        this.empresaRUC = empresaRUC;
        this.empresaFax = empresaFax;
        this.empresaDirecc = empresaDirecc;
        this.empresaAbrev = empresaAbrev;
        this.empresaEst = empresaEst;
    }

    public String getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(String empresaId) {
        this.empresaId = empresaId;
    }

    public String getEmpresaDescripcion() {
        return empresaDescripcion;
    }

    public void setEmpresaDescripcion(String empresaDescripcion) {
        this.empresaDescripcion = empresaDescripcion;
    }

    public String getEmpresaRazSocial() {
        return empresaRazSocial;
    }

    public void setEmpresaRazSocial(String empresaRazSocial) {
        this.empresaRazSocial = empresaRazSocial;
    }

    public String getEmpresaTelef01() {
        return empresaTelef01;
    }

    public void setEmpresaTelef01(String empresaTelef01) {
        this.empresaTelef01 = empresaTelef01;
    }

    public String getEmpresaTelef02() {
        return empresaTelef02;
    }

    public void setEmpresaTelef02(String empresaTelef02) {
        this.empresaTelef02 = empresaTelef02;
    }

    public String getEmpresaTelef03() {
        return empresaTelef03;
    }

    public void setEmpresaTelef03(String empresaTelef03) {
        this.empresaTelef03 = empresaTelef03;
    }

    public String getEmpresaRUC() {
        return empresaRUC;
    }

    public void setEmpresaRUC(String empresaRUC) {
        this.empresaRUC = empresaRUC;
    }

    public String getEmpresaFax() {
        return empresaFax;
    }

    public void setEmpresaFax(String empresaFax) {
        this.empresaFax = empresaFax;
    }

    public String getEmpresaDirecc() {
        return empresaDirecc;
    }

    public void setEmpresaDirecc(String empresaDirecc) {
        this.empresaDirecc = empresaDirecc;
    }

    public String getEmpresaAbrev() {
        return empresaAbrev;
    }

    public void setEmpresaAbrev(String empresaAbrev) {
        this.empresaAbrev = empresaAbrev;
    }

    public String getEmpresaFicEle() {
        return empresaFicEle;
    }

    public void setEmpresaFicEle(String empresaFicEle) {
        this.empresaFicEle = empresaFicEle;
    }

    public String getEmpresaRubro() {
        return empresaRubro;
    }

    public void setEmpresaRubro(String empresaRubro) {
        this.empresaRubro = empresaRubro;
    }

    public boolean getEmpresaEst() {
        return empresaEst;
    }

    public void setEmpresaEst(boolean empresaEst) {
        this.empresaEst = empresaEst;
    }

    public String getUsrSistema() {
        return usrSistema;
    }

    public void setUsrSistema(String usrSistema) {
        this.usrSistema = usrSistema;
    }

    public Date getFecSistema() {
        return fecSistema;
    }

    public void setFecSistema(Date fecSistema) {
        this.fecSistema = fecSistema;
    }

    public String getHrsSistema() {
        return hrsSistema;
    }

    public void setHrsSistema(String hrsSistema) {
        this.hrsSistema = hrsSistema;
    }

    public String getEmpresaMail() {
        return empresaMail;
    }

    public void setEmpresaMail(String empresaMail) {
        this.empresaMail = empresaMail;
    }

    public String getEmpFileNameFirma() {
        return empFileNameFirma;
    }

    public void setEmpFileNameFirma(String empFileNameFirma) {
        this.empFileNameFirma = empFileNameFirma;
    }

    public String getEmpRepLegal() {
        return empRepLegal;
    }

    public void setEmpRepLegal(String empRepLegal) {
        this.empRepLegal = empRepLegal;
    }

    public DistritoGeografico getDistritoGeografico() {
        return distritoGeografico;
    }

    public void setDistritoGeografico(DistritoGeografico distritoGeografico) {
        this.distritoGeografico = distritoGeografico;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (empresaId != null ? empresaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) object;
        if ((this.empresaId == null && other.empresaId != null) || (this.empresaId != null && !this.empresaId.equals(other.empresaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.conta.Empresa[ empresaId=" + empresaId + " ]";
    }
    
}
