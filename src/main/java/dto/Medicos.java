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
@Table(name = "medicos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Medicos.findAll", query = "SELECT m FROM Medicos m"),
    @NamedQuery(name = "Medicos.findByMedcod", query = "SELECT m FROM Medicos m WHERE m.medcod = :medcod"),
    @NamedQuery(name = "Medicos.findByMednam", query = "SELECT m FROM Medicos m WHERE m.mednam = :mednam"),
    @NamedQuery(name = "Medicos.findByMedcol", query = "SELECT m FROM Medicos m WHERE m.medcol = :medcol"),
    @NamedQuery(name = "Medicos.findByMeddir", query = "SELECT m FROM Medicos m WHERE m.meddir = :meddir"),
    @NamedQuery(name = "Medicos.findByUbicod", query = "SELECT m FROM Medicos m WHERE m.ubicod = :ubicod"),
    @NamedQuery(name = "Medicos.findByMedfin", query = "SELECT m FROM Medicos m WHERE m.medfin = :medfin"),
    @NamedQuery(name = "Medicos.findByMedobs", query = "SELECT m FROM Medicos m WHERE m.medobs = :medobs"),
    @NamedQuery(name = "Medicos.findByMedruc", query = "SELECT m FROM Medicos m WHERE m.medruc = :medruc"),
    @NamedQuery(name = "Medicos.findByMedtel", query = "SELECT m FROM Medicos m WHERE m.medtel = :medtel"),
    @NamedQuery(name = "Medicos.findByMedsta", query = "SELECT m FROM Medicos m WHERE m.medsta = :medsta"),
    @NamedQuery(name = "Medicos.findByCodcon", query = "SELECT m FROM Medicos m WHERE m.codcon = :codcon"),
    @NamedQuery(name = "Medicos.findByHonpar", query = "SELECT m FROM Medicos m WHERE m.honpar = :honpar"),
    @NamedQuery(name = "Medicos.findByTipprs", query = "SELECT m FROM Medicos m WHERE m.tipprs = :tipprs"),
    @NamedQuery(name = "Medicos.findByEstado", query = "SELECT m FROM Medicos m WHERE m.estado = :estado"),
    @NamedQuery(name = "Medicos.findByFeccre", query = "SELECT m FROM Medicos m WHERE m.feccre = :feccre"),
    @NamedQuery(name = "Medicos.findByFecumv", query = "SELECT m FROM Medicos m WHERE m.fecumv = :fecumv"),
    @NamedQuery(name = "Medicos.findByUsecod", query = "SELECT m FROM Medicos m WHERE m.usecod = :usecod"),
    @NamedQuery(name = "Medicos.findByUsenam", query = "SELECT m FROM Medicos m WHERE m.usenam = :usenam"),
    @NamedQuery(name = "Medicos.findByHostname", query = "SELECT m FROM Medicos m WHERE m.hostname = :hostname"),
    @NamedQuery(name = "Medicos.findByMedfir", query = "SELECT m FROM Medicos m WHERE m.medfir = :medfir"),
    @NamedQuery(name = "Medicos.findByUsecodx", query = "SELECT m FROM Medicos m WHERE m.usecodx = :usecodx"),
    @NamedQuery(name = "Medicos.findByRegesp", query = "SELECT m FROM Medicos m WHERE m.regesp = :regesp"),
    @NamedQuery(name = "Medicos.findByMedmail", query = "SELECT m FROM Medicos m WHERE m.medmail = :medmail"),
    @NamedQuery(name = "Medicos.findByMeddoa", query = "SELECT m FROM Medicos m WHERE m.meddoa = :meddoa"),
    @NamedQuery(name = "Medicos.findByMedpic", query = "SELECT m FROM Medicos m WHERE m.medpic = :medpic"),
    @NamedQuery(name = "Medicos.findByMedhuel", query = "SELECT m FROM Medicos m WHERE m.medhuel = :medhuel"),
    @NamedQuery(name = "Medicos.findByNumcit", query = "SELECT m FROM Medicos m WHERE m.numcit = :numcit"),
    @NamedQuery(name = "Medicos.findByMedffi", query = "SELECT m FROM Medicos m WHERE m.medffi = :medffi"),
    @NamedQuery(name = "Medicos.findByMedffn", query = "SELECT m FROM Medicos m WHERE m.medffn = :medffn"),
    @NamedQuery(name = "Medicos.findByProtip", query = "SELECT m FROM Medicos m WHERE m.protip = :protip"),
    @NamedQuery(name = "Medicos.findByStaff", query = "SELECT m FROM Medicos m WHERE m.staff = :staff"),
    @NamedQuery(name = "Medicos.findByInttip", query = "SELECT m FROM Medicos m WHERE m.inttip = :inttip"),
    @NamedQuery(name = "Medicos.findByCompar", query = "SELECT m FROM Medicos m WHERE m.compar = :compar"),
    @NamedQuery(name = "Medicos.findByMedmod", query = "SELECT m FROM Medicos m WHERE m.medmod = :medmod"),
    @NamedQuery(name = "Medicos.findByHonparLa", query = "SELECT m FROM Medicos m WHERE m.honparLa = :honparLa"),
    @NamedQuery(name = "Medicos.findByHonparEa", query = "SELECT m FROM Medicos m WHERE m.honparEa = :honparEa"),
    @NamedQuery(name = "Medicos.findByUsecodAd", query = "SELECT m FROM Medicos m WHERE m.usecodAd = :usecodAd"),
    @NamedQuery(name = "Medicos.findByPasswordAd", query = "SELECT m FROM Medicos m WHERE m.passwordAd = :passwordAd"),
    @NamedQuery(name = "Medicos.findByMedcodQull", query = "SELECT m FROM Medicos m WHERE m.medcodQull = :medcodQull"),
    @NamedQuery(name = "Medicos.findByMedcodArie", query = "SELECT m FROM Medicos m WHERE m.medcodArie = :medcodArie"),
    @NamedQuery(name = "Medicos.findByMeddoc", query = "SELECT m FROM Medicos m WHERE m.meddoc = :meddoc"),
    @NamedQuery(name = "Medicos.findByMedvircitUse", query = "SELECT m FROM Medicos m WHERE m.medvircitUse = :medvircitUse"),
    @NamedQuery(name = "Medicos.findByMedcur", query = "SELECT m FROM Medicos m WHERE m.medcur = :medcur"),
    @NamedQuery(name = "Medicos.findByNamFirm", query = "SELECT m FROM Medicos m WHERE m.namFirm = :namFirm")})
public class Medicos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "medcod")
    private String medcod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "mednam")
    private String mednam;
    @Size(max = 15)
    @Column(name = "medcol")
    private String medcol;
    @Size(max = 100)
    @Column(name = "meddir")
    private String meddir;
    @Size(max = 12)
    @Column(name = "ubicod")
    private String ubicod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "medfin")
    @Temporal(TemporalType.TIMESTAMP)
    private Date medfin;
    @Size(max = 1024)
    @Column(name = "medobs")
    private String medobs;
    @Size(max = 25)
    @Column(name = "medruc")
    private String medruc;
    @Size(max = 25)
    @Column(name = "medtel")
    private String medtel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "medsta")
    private String medsta;
    @Size(max = 4)
    @Column(name = "codcon")
    private String codcon;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "honpar")
    private String honpar;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "tipprs")
    private String tipprs;
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
    @Size(max = 80)
    @Column(name = "medfir")
    private String medfir;
    @Column(name = "usecodx")
    private Integer usecodx;
    @Size(max = 20)
    @Column(name = "regesp")
    private String regesp;
    @Size(max = 100)
    @Column(name = "medmail")
    private String medmail;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "medcom")
    private String medcom;
    @Size(max = 200)
    @Column(name = "meddoa")
    private String meddoa;
    @Size(max = 80)
    @Column(name = "medpic")
    private String medpic;
    @Size(max = 80)
    @Column(name = "medhuel")
    private String medhuel;
    @Column(name = "numcit")
    private Integer numcit;
    @Column(name = "medffi")
    @Temporal(TemporalType.TIMESTAMP)
    private Date medffi;
    @Column(name = "medffn")
    @Temporal(TemporalType.TIMESTAMP)
    private Date medffn;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "protip")
    private String protip;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "staff")
    private String staff;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "inttip")
    private String inttip;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "compar")
    private String compar;
    @Size(max = 60)
    @Column(name = "medmod")
    private String medmod;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "honpar_la")
    private String honparLa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "honpar_ea")
    private String honparEa;
    @Size(max = 100)
    @Column(name = "usecod_ad")
    private String usecodAd;
    @Size(max = 150)
    @Column(name = "password_ad")
    private String passwordAd;
    @Size(max = 10)
    @Column(name = "medcod_qull")
    private String medcodQull;
    @Size(max = 4)
    @Column(name = "medcod_arie")
    private String medcodArie;
    @Size(max = 15)
    @Column(name = "meddoc")
    private String meddoc;
    @Size(max = 200)
    @Column(name = "medvircit_use")
    private String medvircitUse;
    @Size(max = 200)
    @Column(name = "medcur")
    private String medcur;
    @Size(max = 70)
    @Column(name = "nam_firm")
    private String namFirm;

    public Medicos() {
    }

    public Medicos(String medcod) {
        this.medcod = medcod;
    }

    public Medicos(String medcod, String mednam, Date medfin, String medsta, String honpar, String tipprs, String estado, Date feccre, Date fecumv, int usecod, String usenam, String hostname, String protip, String staff, String inttip, String compar, String honparLa, String honparEa) {
        this.medcod = medcod;
        this.mednam = mednam;
        this.medfin = medfin;
        this.medsta = medsta;
        this.honpar = honpar;
        this.tipprs = tipprs;
        this.estado = estado;
        this.feccre = feccre;
        this.fecumv = fecumv;
        this.usecod = usecod;
        this.usenam = usenam;
        this.hostname = hostname;
        this.protip = protip;
        this.staff = staff;
        this.inttip = inttip;
        this.compar = compar;
        this.honparLa = honparLa;
        this.honparEa = honparEa;
    }

    public String getMedcod() {
        return medcod;
    }

    public void setMedcod(String medcod) {
        this.medcod = medcod;
    }

    public String getMednam() {
        return mednam;
    }

    public void setMednam(String mednam) {
        this.mednam = mednam;
    }

    public String getMedcol() {
        return medcol;
    }

    public void setMedcol(String medcol) {
        this.medcol = medcol;
    }

    public String getMeddir() {
        return meddir;
    }

    public void setMeddir(String meddir) {
        this.meddir = meddir;
    }

    public String getUbicod() {
        return ubicod;
    }

    public void setUbicod(String ubicod) {
        this.ubicod = ubicod;
    }

    public Date getMedfin() {
        return medfin;
    }

    public void setMedfin(Date medfin) {
        this.medfin = medfin;
    }

    public String getMedobs() {
        return medobs;
    }

    public void setMedobs(String medobs) {
        this.medobs = medobs;
    }

    public String getMedruc() {
        return medruc;
    }

    public void setMedruc(String medruc) {
        this.medruc = medruc;
    }

    public String getMedtel() {
        return medtel;
    }

    public void setMedtel(String medtel) {
        this.medtel = medtel;
    }

    public String getMedsta() {
        return medsta;
    }

    public void setMedsta(String medsta) {
        this.medsta = medsta;
    }

    public String getCodcon() {
        return codcon;
    }

    public void setCodcon(String codcon) {
        this.codcon = codcon;
    }

    public String getHonpar() {
        return honpar;
    }

    public void setHonpar(String honpar) {
        this.honpar = honpar;
    }

    public String getTipprs() {
        return tipprs;
    }

    public void setTipprs(String tipprs) {
        this.tipprs = tipprs;
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

    public String getMedfir() {
        return medfir;
    }

    public void setMedfir(String medfir) {
        this.medfir = medfir;
    }

    public Integer getUsecodx() {
        return usecodx;
    }

    public void setUsecodx(Integer usecodx) {
        this.usecodx = usecodx;
    }

    public String getRegesp() {
        return regesp;
    }

    public void setRegesp(String regesp) {
        this.regesp = regesp;
    }

    public String getMedmail() {
        return medmail;
    }

    public void setMedmail(String medmail) {
        this.medmail = medmail;
    }

    public String getMedcom() {
        return medcom;
    }

    public void setMedcom(String medcom) {
        this.medcom = medcom;
    }

    public String getMeddoa() {
        return meddoa;
    }

    public void setMeddoa(String meddoa) {
        this.meddoa = meddoa;
    }

    public String getMedpic() {
        return medpic;
    }

    public void setMedpic(String medpic) {
        this.medpic = medpic;
    }

    public String getMedhuel() {
        return medhuel;
    }

    public void setMedhuel(String medhuel) {
        this.medhuel = medhuel;
    }

    public Integer getNumcit() {
        return numcit;
    }

    public void setNumcit(Integer numcit) {
        this.numcit = numcit;
    }

    public Date getMedffi() {
        return medffi;
    }

    public void setMedffi(Date medffi) {
        this.medffi = medffi;
    }

    public Date getMedffn() {
        return medffn;
    }

    public void setMedffn(Date medffn) {
        this.medffn = medffn;
    }

    public String getProtip() {
        return protip;
    }

    public void setProtip(String protip) {
        this.protip = protip;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getInttip() {
        return inttip;
    }

    public void setInttip(String inttip) {
        this.inttip = inttip;
    }

    public String getCompar() {
        return compar;
    }

    public void setCompar(String compar) {
        this.compar = compar;
    }

    public String getMedmod() {
        return medmod;
    }

    public void setMedmod(String medmod) {
        this.medmod = medmod;
    }

    public String getHonparLa() {
        return honparLa;
    }

    public void setHonparLa(String honparLa) {
        this.honparLa = honparLa;
    }

    public String getHonparEa() {
        return honparEa;
    }

    public void setHonparEa(String honparEa) {
        this.honparEa = honparEa;
    }

    public String getUsecodAd() {
        return usecodAd;
    }

    public void setUsecodAd(String usecodAd) {
        this.usecodAd = usecodAd;
    }

    public String getPasswordAd() {
        return passwordAd;
    }

    public void setPasswordAd(String passwordAd) {
        this.passwordAd = passwordAd;
    }

    public String getMedcodQull() {
        return medcodQull;
    }

    public void setMedcodQull(String medcodQull) {
        this.medcodQull = medcodQull;
    }

    public String getMedcodArie() {
        return medcodArie;
    }

    public void setMedcodArie(String medcodArie) {
        this.medcodArie = medcodArie;
    }

    public String getMeddoc() {
        return meddoc;
    }

    public void setMeddoc(String meddoc) {
        this.meddoc = meddoc;
    }

    public String getMedvircitUse() {
        return medvircitUse;
    }

    public void setMedvircitUse(String medvircitUse) {
        this.medvircitUse = medvircitUse;
    }

    public String getMedcur() {
        return medcur;
    }

    public void setMedcur(String medcur) {
        this.medcur = medcur;
    }

    public String getNamFirm() {
        return namFirm;
    }

    public void setNamFirm(String namFirm) {
        this.namFirm = namFirm;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (medcod != null ? medcod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Medicos)) {
            return false;
        }
        Medicos other = (Medicos) object;
        if ((this.medcod == null && other.medcod != null) || (this.medcod != null && !this.medcod.equals(other.medcod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.Medicos[ medcod=" + medcod + " ]";
    }
    
}
