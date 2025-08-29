/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author USER
 */
@Embeddable
public class MovimientosIzipayPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Codigo")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Voucher")
    private String voucher;

    public MovimientosIzipayPK() {
    }

    public MovimientosIzipayPK(String codigo, String voucher) {
        this.codigo = codigo;
        this.voucher = voucher;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        hash += (voucher != null ? voucher.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MovimientosIzipayPK)) {
            return false;
        }
        MovimientosIzipayPK other = (MovimientosIzipayPK) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        if ((this.voucher == null && other.voucher != null) || (this.voucher != null && !this.voucher.equals(other.voucher))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dto.MovimientosIzipayPK[ codigo=" + codigo + ", voucher=" + voucher + " ]";
    }
    
}
