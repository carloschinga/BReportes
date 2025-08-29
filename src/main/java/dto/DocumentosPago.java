/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "documentos_pago")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentosPago.findAll", query = "SELECT d FROM DocumentosPago d"),
    @NamedQuery(name = "DocumentosPago.findByDocpag", query = "SELECT d FROM DocumentosPago d WHERE d.docpag = :docpag"),
    @NamedQuery(name = "DocumentosPago.findByDocdes", query = "SELECT d FROM DocumentosPago d WHERE d.docdes = :docdes"),
    @NamedQuery(name = "DocumentosPago.findByEstado", query = "SELECT d FROM DocumentosPago d WHERE d.estado = :estado"),
    @NamedQuery(name = "DocumentosPago.findByFeccre", query = "SELECT d FROM DocumentosPago d WHERE d.feccre = :feccre"),
    @NamedQuery(name = "DocumentosPago.findByFecumv", query = "SELECT d FROM DocumentosPago d WHERE d.fecumv = :fecumv"),
    @NamedQuery(name = "DocumentosPago.findByUsecod", query = "SELECT d FROM DocumentosPago d WHERE d.usecod = :usecod"),
    @NamedQuery(name = "DocumentosPago.findByUsenam", query = "SELECT d FROM DocumentosPago d WHERE d.usenam = :usenam"),
    @NamedQuery(name = "DocumentosPago.findByHostname", query = "SELECT d FROM DocumentosPago d WHERE d.hostname = :hostname"),
    @NamedQuery(name = "DocumentosPago.findByDoctip", query = "SELECT d FROM DocumentosPago d WHERE d.doctip = :doctip"),
    @NamedQuery(name = "DocumentosPago.findByDocopertarj", query = "SELECT d FROM DocumentosPago d WHERE d.docopertarj = :docopertarj"),
    @NamedQuery(name = "DocumentosPago.findByDocpinpadsta", query = "SELECT d FROM DocumentosPago d WHERE d.docpinpadsta = :docpinpadsta"),
    @NamedQuery(name = "DocumentosPago.findByTipFormPagId", query = "SELECT d FROM DocumentosPago d WHERE d.tipFormPagId = :tipFormPagId")})
public class DocumentosPago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "docpag")
    private String docpag;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "docdes")
    private String docdes;
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
    @Size(min = 1, max = 2)
    @Column(name = "doctip")
    private String doctip;
    @Size(max = 4)
    @Column(name = "docopertarj")
    private String docopertarj;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "docpinpadsta")
    private String docpinpadsta;
    @Column(name = "TipFormPagId")
    private Integer tipFormPagId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sisforpagVen")
    private Collection<Sistema> sistemaCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sisforpagcrecliVen")
    private Collection<Sistema> sistemaCollection1;

    public DocumentosPago() {
    }

    public DocumentosPago(String docpag) {
        this.docpag = docpag;
    }

    public DocumentosPago(String docpag, String docdes, String estado, Date feccre, Date fecumv, int usecod, String usenam, String hostname, String doctip, String docpinpadsta) {
        this.docpag = docpag;
        this.docdes = docdes;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
        this.doctip = doctip;
        this.docpinpadsta = docpinpadsta;
    }

    public String getDocpag() {
        return docpag;
    }

    public void setDocpag(String docpag) {
        this.docpag = docpag;
    }

    public String getDocdes() {
        return docdes;
    }

    public void setDocdes(String docdes) {
        this.docdes = docdes;
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

    public String getDoctip() {
        return doctip;
    }

    public void setDoctip(String doctip) {
        this.doctip = doctip;
    }

    public String getDocopertarj() {
        return docopertarj;
    }

    public void setDocopertarj(String docopertarj) {
        this.docopertarj = docopertarj;
    }

    public String getDocpinpadsta() {
        return docpinpadsta;
    }

    public void setDocpinpadsta(String docpinpadsta) {
        this.docpinpadsta = docpinpadsta;
    }

    public Integer getTipFormPagId() {
        return tipFormPagId;
    }

    public void setTipFormPagId(Integer tipFormPagId) {
        this.tipFormPagId = tipFormPagId;
    }

    @XmlTransient
    public Collection<Sistema> getSistemaCollection() {
        return sistemaCollection;
    }

    public void setSistemaCollection(Collection<Sistema> sistemaCollection) {
        this.sistemaCollection = sistemaCollection;
    }

    @XmlTransient
    public Collection<Sistema> getSistemaCollection1() {
        return sistemaCollection1;
    }

    public void setSistemaCollection1(Collection<Sistema> sistemaCollection1) {
        this.sistemaCollection1 = sistemaCollection1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (docpag != null ? docpag.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentosPago)) {
            return false;
        }
        DocumentosPago other = (DocumentosPago) object;
        if ((this.docpag == null && other.docpag != null) || (this.docpag != null && !this.docpag.equals(other.docpag))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.DocumentosPago[ docpag=" + docpag + " ]";
    }
    
}
