/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author USUARIO
 */
public class TiposProducto {
    String codtip;
    String destip;

    public String getCodtip() {
        return codtip;
    }

    public void setCodtip(String codtip) {
        this.codtip = codtip;
    }

    public String getDestip() {
        return destip;
    }

    public void setDestip(String destip) {
        this.destip = destip;
    }

    public TiposProducto(String codtip, String destip) {
        this.codtip = codtip;
        this.destip = destip;
    }
}
