/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USUARIO
 */
@Entity
@Table(name = "almacenes_bartolito_detalle")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AlmacenesBartolitoDetalle.findAll", query = "SELECT a FROM AlmacenesBartolitoDetalle a"),
    @NamedQuery(name = "AlmacenesBartolitoDetalle.findByCodalm", query = "SELECT a FROM AlmacenesBartolitoDetalle a WHERE a.almacenesBartolitoDetallePK.codalm = :codalm"),
    @NamedQuery(name = "AlmacenesBartolitoDetalle.findByCodpro", query = "SELECT a FROM AlmacenesBartolitoDetalle a WHERE a.almacenesBartolitoDetallePK.codpro = :codpro"),
    @NamedQuery(name = "AlmacenesBartolitoDetalle.findByUbicacion", query = "SELECT a FROM AlmacenesBartolitoDetalle a WHERE a.almacenesBartolitoDetallePK.ubicacion = :ubicacion"),
    @NamedQuery(name = "AlmacenesBartolitoDetalle.findByLote", query = "SELECT a FROM AlmacenesBartolitoDetalle a WHERE a.almacenesBartolitoDetallePK.lote = :lote"),
    @NamedQuery(name = "AlmacenesBartolitoDetalle.findByCante", query = "SELECT a FROM AlmacenesBartolitoDetalle a WHERE a.cante = :cante"),
    @NamedQuery(name = "AlmacenesBartolitoDetalle.findByCantf", query = "SELECT a FROM AlmacenesBartolitoDetalle a WHERE a.cantf = :cantf")})
public class AlmacenesBartolitoDetalle implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AlmacenesBartolitoDetallePK almacenesBartolitoDetallePK;
    @Column(name = "cante")
    private Integer cante;
    @Column(name = "cantf")
    private Integer cantf;

    public AlmacenesBartolitoDetalle() {
    }

    public AlmacenesBartolitoDetalle(AlmacenesBartolitoDetallePK almacenesBartolitoDetallePK) {
        this.almacenesBartolitoDetallePK = almacenesBartolitoDetallePK;
    }

    public AlmacenesBartolitoDetalle(String codalm, String codpro, String ubicacion, String lote) {
        this.almacenesBartolitoDetallePK = new AlmacenesBartolitoDetallePK(codalm, codpro, ubicacion, lote);
    }

    public AlmacenesBartolitoDetallePK getAlmacenesBartolitoDetallePK() {
        return almacenesBartolitoDetallePK;
    }

    public void setAlmacenesBartolitoDetallePK(AlmacenesBartolitoDetallePK almacenesBartolitoDetallePK) {
        this.almacenesBartolitoDetallePK = almacenesBartolitoDetallePK;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (almacenesBartolitoDetallePK != null ? almacenesBartolitoDetallePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AlmacenesBartolitoDetalle)) {
            return false;
        }
        AlmacenesBartolitoDetalle other = (AlmacenesBartolitoDetalle) object;
        if ((this.almacenesBartolitoDetallePK == null && other.almacenesBartolitoDetallePK != null) || (this.almacenesBartolitoDetallePK != null && !this.almacenesBartolitoDetallePK.equals(other.almacenesBartolitoDetallePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.AlmacenesBartolitoDetalle[ almacenesBartolitoDetallePK=" + almacenesBartolitoDetallePK + " ]";
    }
    
}
