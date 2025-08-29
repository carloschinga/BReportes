/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "documentos_series")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentosSeries.findAll", query = "SELECT d FROM DocumentosSeries d"),
    @NamedQuery(name = "DocumentosSeries.findByTdoser", query = "SELECT d FROM DocumentosSeries d WHERE d.documentosSeriesPK.tdoser = :tdoser"),
    @NamedQuery(name = "DocumentosSeries.findByTdonum", query = "SELECT d FROM DocumentosSeries d WHERE d.tdonum = :tdonum"),
    @NamedQuery(name = "DocumentosSeries.findByDestse", query = "SELECT d FROM DocumentosSeries d WHERE d.destse = :destse"),
    @NamedQuery(name = "DocumentosSeries.findByOrifac", query = "SELECT d FROM DocumentosSeries d WHERE d.orifac = :orifac"),
    @NamedQuery(name = "DocumentosSeries.findByEstado", query = "SELECT d FROM DocumentosSeries d WHERE d.estado = :estado"),
    @NamedQuery(name = "DocumentosSeries.findByFeccre", query = "SELECT d FROM DocumentosSeries d WHERE d.feccre = :feccre"),
    @NamedQuery(name = "DocumentosSeries.findByFecumv", query = "SELECT d FROM DocumentosSeries d WHERE d.fecumv = :fecumv"),
    @NamedQuery(name = "DocumentosSeries.findByUsecod", query = "SELECT d FROM DocumentosSeries d WHERE d.usecod = :usecod"),
    @NamedQuery(name = "DocumentosSeries.findByUsenam", query = "SELECT d FROM DocumentosSeries d WHERE d.usenam = :usenam"),
    @NamedQuery(name = "DocumentosSeries.findByHostname", query = "SELECT d FROM DocumentosSeries d WHERE d.hostname = :hostname"),
    @NamedQuery(name = "DocumentosSeries.findByTdoidser", query = "SELECT d FROM DocumentosSeries d WHERE d.tdoidser = :tdoidser"),
    @NamedQuery(name = "DocumentosSeries.findBySiscod", query = "SELECT d FROM DocumentosSeries d WHERE d.documentosSeriesPK.siscod = :siscod"),
    @NamedQuery(name = "DocumentosSeries.findBySerpre", query = "SELECT d FROM DocumentosSeries d WHERE d.serpre = :serpre"),
    @NamedQuery(name = "DocumentosSeries.findBySerini", query = "SELECT d FROM DocumentosSeries d WHERE d.serini = :serini"),
    @NamedQuery(name = "DocumentosSeries.findBySerfin", query = "SELECT d FROM DocumentosSeries d WHERE d.serfin = :serfin"),
    @NamedQuery(name = "DocumentosSeries.findBySeraux1", query = "SELECT d FROM DocumentosSeries d WHERE d.seraux1 = :seraux1"),
    @NamedQuery(name = "DocumentosSeries.findBySeraux2", query = "SELECT d FROM DocumentosSeries d WHERE d.seraux2 = :seraux2"),
    @NamedQuery(name = "DocumentosSeries.findBySerdat", query = "SELECT d FROM DocumentosSeries d WHERE d.serdat = :serdat"),
    @NamedQuery(name = "DocumentosSeries.findBySertimp", query = "SELECT d FROM DocumentosSeries d WHERE d.sertimp = :sertimp"),
    @NamedQuery(name = "DocumentosSeries.findBySercom", query = "SELECT d FROM DocumentosSeries d WHERE d.sercom = :sercom"),
    @NamedQuery(name = "DocumentosSeries.findBySeraux3", query = "SELECT d FROM DocumentosSeries d WHERE d.seraux3 = :seraux3"),
    @NamedQuery(name = "DocumentosSeries.findByCodtri", query = "SELECT d FROM DocumentosSeries d WHERE d.codtri = :codtri"),
    @NamedQuery(name = "DocumentosSeries.findByTdoemi", query = "SELECT d FROM DocumentosSeries d WHERE d.tdoemi = :tdoemi"),
    @NamedQuery(name = "DocumentosSeries.findByEmiface", query = "SELECT d FROM DocumentosSeries d WHERE d.emiface = :emiface"),
    @NamedQuery(name = "DocumentosSeries.findByTipkarCad", query = "SELECT d FROM DocumentosSeries d WHERE d.tipkarCad = :tipkarCad")})
public class DocumentosSeries implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DocumentosSeriesPK documentosSeriesPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tdonum")
    private int tdonum;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "destse")
    private String destse;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "orifac")
    private String orifac;
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
    @Size(min = 1, max = 4)
    @Column(name = "tdoidser")
    private String tdoidser;
    @Size(max = 4)
    @Column(name = "serpre")
    private String serpre;
    @Column(name = "serini")
    private Integer serini;
    @Column(name = "serfin")
    private Integer serfin;
    @Size(max = 40)
    @Column(name = "seraux_1")
    private String seraux1;
    @Size(max = 40)
    @Column(name = "seraux_2")
    private String seraux2;
    @Column(name = "serdat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date serdat;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "sertimp")
    private String sertimp;
    @Size(max = 300)
    @Column(name = "sercom")
    private String sercom;
    @Size(max = 200)
    @Column(name = "seraux_3")
    private String seraux3;
    @Size(max = 2)
    @Column(name = "codtri")
    private String codtri;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "tdoemi")
    private String tdoemi;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "emiface")
    private String emiface;
    @Size(max = 150)
    @Column(name = "tipkar_cad")
    private String tipkarCad;

    public DocumentosSeries() {
    }

    public DocumentosSeries(DocumentosSeriesPK documentosSeriesPK) {
        this.documentosSeriesPK = documentosSeriesPK;
    }

    public DocumentosSeries(DocumentosSeriesPK documentosSeriesPK, int tdonum, String destse, String orifac, String estado, Date feccre, Date fecumv, int usecod, String usenam, String hostname, String tdoidser, String sertimp, String tdoemi, String emiface) {
        this.documentosSeriesPK = documentosSeriesPK;
        this.tdonum = tdonum;
        this.destse = destse;
        this.orifac = orifac;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
        this.tdoidser = tdoidser;
        this.sertimp = sertimp;
        this.tdoemi = tdoemi;
        this.emiface = emiface;
    }

    public DocumentosSeries(String tdoser, int siscod) {
        this.documentosSeriesPK = new DocumentosSeriesPK(tdoser, siscod);
    }

    public DocumentosSeriesPK getDocumentosSeriesPK() {
        return documentosSeriesPK;
    }

    public void setDocumentosSeriesPK(DocumentosSeriesPK documentosSeriesPK) {
        this.documentosSeriesPK = documentosSeriesPK;
    }

    public int getTdonum() {
        return tdonum;
    }

    public void setTdonum(int tdonum) {
        this.tdonum = tdonum;
    }

    public String getDestse() {
        return destse;
    }

    public void setDestse(String destse) {
        this.destse = destse;
    }

    public String getOrifac() {
        return orifac;
    }

    public void setOrifac(String orifac) {
        this.orifac = orifac;
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

    public String getTdoidser() {
        return tdoidser;
    }

    public void setTdoidser(String tdoidser) {
        this.tdoidser = tdoidser;
    }

    public String getSerpre() {
        return serpre;
    }

    public void setSerpre(String serpre) {
        this.serpre = serpre;
    }

    public Integer getSerini() {
        return serini;
    }

    public void setSerini(Integer serini) {
        this.serini = serini;
    }

    public Integer getSerfin() {
        return serfin;
    }

    public void setSerfin(Integer serfin) {
        this.serfin = serfin;
    }

    public String getSeraux1() {
        return seraux1;
    }

    public void setSeraux1(String seraux1) {
        this.seraux1 = seraux1;
    }

    public String getSeraux2() {
        return seraux2;
    }

    public void setSeraux2(String seraux2) {
        this.seraux2 = seraux2;
    }

    public Date getSerdat() {
        return serdat;
    }

    public void setSerdat(Date serdat) {
        this.serdat = serdat;
    }

    public String getSertimp() {
        return sertimp;
    }

    public void setSertimp(String sertimp) {
        this.sertimp = sertimp;
    }

    public String getSercom() {
        return sercom;
    }

    public void setSercom(String sercom) {
        this.sercom = sercom;
    }

    public String getSeraux3() {
        return seraux3;
    }

    public void setSeraux3(String seraux3) {
        this.seraux3 = seraux3;
    }

    public String getCodtri() {
        return codtri;
    }

    public void setCodtri(String codtri) {
        this.codtri = codtri;
    }

    public String getTdoemi() {
        return tdoemi;
    }

    public void setTdoemi(String tdoemi) {
        this.tdoemi = tdoemi;
    }

    public String getEmiface() {
        return emiface;
    }

    public void setEmiface(String emiface) {
        this.emiface = emiface;
    }

    public String getTipkarCad() {
        return tipkarCad;
    }

    public void setTipkarCad(String tipkarCad) {
        this.tipkarCad = tipkarCad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (documentosSeriesPK != null ? documentosSeriesPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocumentosSeries)) {
            return false;
        }
        DocumentosSeries other = (DocumentosSeries) object;
        if ((this.documentosSeriesPK == null && other.documentosSeriesPK != null) || (this.documentosSeriesPK != null && !this.documentosSeriesPK.equals(other.documentosSeriesPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.DocumentosSeries[ documentosSeriesPK=" + documentosSeriesPK + " ]";
    }
    
}
