/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.math.BigDecimal;

/**
 *
 * @author USUARIO
 */
public class DistribuirAlmacenCentral {
    public String codpro;
    public String despro;
    public String codlab;
    public int stkfra;
    public int stkalm;
    public int stkalm_m;
    public String codalm;
    //public BigDecimal dif_stock_min;
    public int stock_estab;
    //public BigDecimal stock_min_estab;
    public String codtip;
    public String destip;
    public String tienerojos;

    public String getCodpro() {
        return codpro;
    }

    public void setCodpro(String codpro) {
        this.codpro = codpro;
    }

    public String getDespro() {
        return despro;
    }

    public void setDespro(String despro) {
        this.despro = despro;
    }

    public String getCodlab() {
        return codlab;
    }

    public void setCodlab(String codlab) {
        this.codlab = codlab;
    }

    public int getStkfra() {
        return stkfra;
    }

    public void setStkfra(int stkfra) {
        this.stkfra = stkfra;
    }

    public int getStkalm() {
        return stkalm;
    }

    public void setStkalm(int stkalm) {
        this.stkalm = stkalm;
    }

    public int getStkalm_m() {
        return stkalm_m;
    }

    public void setStkalm_m(int stkalm_m) {
        this.stkalm_m = stkalm_m;
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
    }

    public int getStock_estab() {
        return stock_estab;
    }

    public void setStock_estab(int stock_estab) {
        this.stock_estab = stock_estab;
    }

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

    public DistribuirAlmacenCentral(String codpro, String despro, String codlab, int stkfra, int stkalm, int stkalm_m, String codalm,int stock_estab,  String codtip, String destip,String tienerojos) {
        this.codpro = codpro;
        this.despro = despro;
        this.codlab = codlab;
        this.stkfra = stkfra;
        this.stkalm = stkalm;
        this.stkalm_m = stkalm_m;
        this.codalm = codalm;
        //this.dif_stock_min = dif_stock_min;
        this.stock_estab = stock_estab;
        //this.stock_min_estab = stock_min_estab;
        this.codtip = codtip;
        this.destip = destip;
        this.tienerojos = tienerojos;
    }

}