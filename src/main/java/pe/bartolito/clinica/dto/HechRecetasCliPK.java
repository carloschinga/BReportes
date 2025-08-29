/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pe.bartolito.clinica.dto;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author USUARIO
 */
@Embeddable
public class HechRecetasCliPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "ActoMedico")
    private int actoMedico;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Coditm")
    private int coditm;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "CodproLolcli")
    private String codproLolcli;

    public HechRecetasCliPK() {
    }

    public HechRecetasCliPK(int actoMedico, int coditm, String codproLolcli) {
        this.actoMedico = actoMedico;
        this.coditm = coditm;
        this.codproLolcli = codproLolcli;
    }

    public int getActoMedico() {
        return actoMedico;
    }

    public void setActoMedico(int actoMedico) {
        this.actoMedico = actoMedico;
    }

    public int getCoditm() {
        return coditm;
    }

    public void setCoditm(int coditm) {
        this.coditm = coditm;
    }

    public String getCodproLolcli() {
        return codproLolcli;
    }

    public void setCodproLolcli(String codproLolcli) {
        this.codproLolcli = codproLolcli;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) actoMedico;
        hash += (int) coditm;
        hash += (codproLolcli != null ? codproLolcli.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HechRecetasCliPK)) {
            return false;
        }
        HechRecetasCliPK other = (HechRecetasCliPK) object;
        if (this.actoMedico != other.actoMedico) {
            return false;
        }
        if (this.coditm != other.coditm) {
            return false;
        }
        if ((this.codproLolcli == null && other.codproLolcli != null) || (this.codproLolcli != null && !this.codproLolcli.equals(other.codproLolcli))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pe.bartolito.clinica.dto.HechRecetasCliPK[ actoMedico=" + actoMedico + ", coditm=" + coditm + ", codproLolcli=" + codproLolcli + " ]";
    }
    
}
