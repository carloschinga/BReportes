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
@Table(name = "fa_orden_reposicion_detalle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FaOrdenReposicionDetalle.findAll", query = "SELECT f FROM FaOrdenReposicionDetalle f"),
    @NamedQuery(name = "FaOrdenReposicionDetalle.encontrarultnumitm", query = "SELECT f.faOrdenReposicionDetallePK.numitm FROM FaOrdenReposicionDetalle f where f.faOrdenReposicionDetallePK.invnum=:invnum order by f.faOrdenReposicionDetallePK.numitm desc"),
    @NamedQuery(name = "FaOrdenReposicionDetalle.findByInvnum", query = "SELECT f FROM FaOrdenReposicionDetalle f WHERE f.faOrdenReposicionDetallePK.invnum = :invnum"),
    @NamedQuery(name = "FaOrdenReposicionDetalle.findByNumitm", query = "SELECT f FROM FaOrdenReposicionDetalle f WHERE f.faOrdenReposicionDetallePK.numitm = :numitm"),
    @NamedQuery(name = "FaOrdenReposicionDetalle.findByCodpro", query = "SELECT f FROM FaOrdenReposicionDetalle f WHERE f.codpro = :codpro"),
    @NamedQuery(name = "FaOrdenReposicionDetalle.findByCante", query = "SELECT f FROM FaOrdenReposicionDetalle f WHERE f.cante = :cante"),
    @NamedQuery(name = "FaOrdenReposicionDetalle.findByCantf", query = "SELECT f FROM FaOrdenReposicionDetalle f WHERE f.cantf = :cantf"),
    @NamedQuery(name = "FaOrdenReposicionDetalle.findByEstado", query = "SELECT f FROM FaOrdenReposicionDetalle f WHERE f.estado = :estado"),
    @NamedQuery(name = "FaOrdenReposicionDetalle.findByUsecod", query = "SELECT f FROM FaOrdenReposicionDetalle f WHERE f.usecod = :usecod"),
    @NamedQuery(name = "FaOrdenReposicionDetalle.findByFeccre", query = "SELECT f FROM FaOrdenReposicionDetalle f WHERE f.feccre = :feccre")})
public class FaOrdenReposicionDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected FaOrdenReposicionDetallePK faOrdenReposicionDetallePK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "codpro")
    private String codpro;
    @Column(name = "cante")
    private Integer cante;
    @Column(name = "cantf")
    private Integer cantf;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usecod")
    private int usecod;
    @Basic(optional = false)
    @NotNull
    @Column(name = "feccre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feccre;

    public FaOrdenReposicionDetalle() {
    }

    public FaOrdenReposicionDetalle(FaOrdenReposicionDetallePK faOrdenReposicionDetallePK) {
        this.faOrdenReposicionDetallePK = faOrdenReposicionDetallePK;
    }

    public FaOrdenReposicionDetalle(FaOrdenReposicionDetallePK faOrdenReposicionDetallePK, String codpro, String estado, int usecod, Date feccre) {
        this.faOrdenReposicionDetallePK = faOrdenReposicionDetallePK;
        this.codpro = codpro;
        this.estado = estado;
        this.usecod = usecod;
        this.feccre = feccre;
    }

    public FaOrdenReposicionDetalle(int invnum, int numitm) {
        this.faOrdenReposicionDetallePK = new FaOrdenReposicionDetallePK(invnum, numitm);
    }

    public FaOrdenReposicionDetallePK getFaOrdenReposicionDetallePK() {
        return faOrdenReposicionDetallePK;
    }

    public void setFaOrdenReposicionDetallePK(FaOrdenReposicionDetallePK faOrdenReposicionDetallePK) {
        this.faOrdenReposicionDetallePK = faOrdenReposicionDetallePK;
    }

    public String getCodpro() {
        return codpro;
    }

    public void setCodpro(String codpro) {
        this.codpro = codpro;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getUsecod() {
        return usecod;
    }

    public void setUsecod(int usecod) {
        this.usecod = usecod;
    }

    public Date getFeccre() {
        return feccre;
    }

    public void setFeccre(Date feccre) {
        this.feccre = feccre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (faOrdenReposicionDetallePK != null ? faOrdenReposicionDetallePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FaOrdenReposicionDetalle)) {
            return false;
        }
        FaOrdenReposicionDetalle other = (FaOrdenReposicionDetalle) object;
        if ((this.faOrdenReposicionDetallePK == null && other.faOrdenReposicionDetallePK != null) || (this.faOrdenReposicionDetallePK != null && !this.faOrdenReposicionDetallePK.equals(other.faOrdenReposicionDetallePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.FaOrdenReposicionDetalle[ faOrdenReposicionDetallePK=" + faOrdenReposicionDetallePK + " ]";
    }
    
}
