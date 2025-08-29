/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author USUARIO
 */
public class Datosgenerartxt {
    String codpro;
    String lote;
    int cantE;
    int cantF;

    public String getCodpro() {
        return codpro;
    }

    public void setCodpro(String codpro) {
        this.codpro = codpro;
    }

    public int getCantE() {
        return cantE;
    }

    public void setCantE(int cantE) {
        this.cantE = cantE;
    }

    public int getCantF() {
        return cantF;
    }

    public void setCantF(int cantF) {
        this.cantF = cantF;
    }

    public Datosgenerartxt(String codpro, String lote, int cantE, int cantF) {
        this.codpro = codpro;
        this.lote = lote;
        this.cantE = cantE;
        this.cantF = cantF;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }
}
