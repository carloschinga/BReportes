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
public class DistribuirAlmacenEstablecimientos {

    private int siscod;
    private String sisent;
    private String codalm;
    private String codpro;
    private int stkalm;
    private int stkalm_m;
    private int stkmin;
    private int cant_e;
    private int cant_f;
    private int stkfra;
    private BigDecimal stkmin2;
    private BigDecimal coscom;
    private BigDecimal igvpro;
    private String lote;
    private String fecven;
    private String restringido;
    private String aplicfrac;
    private String inmov;
    private Integer blister;
    private Integer masterpack;
    private String aplicmastpack;
    private int transE;
    private int transF;

    public int getTransE() {
        return transE;
    }

    public void setTransE(int transE) {
        this.transE = transE;
    }

    public int getTransF() {
        return transF;
    }

    public void setTransF(int transF) {
        this.transF = transF;
    }

    public Integer getBlister() {
        return blister;
    }

    public void setBlister(Integer blister) {
        this.blister = blister;
    }

    public Integer getMasterpack() {
        return masterpack;
    }

    public void setMasterpack(Integer masterpack) {
        this.masterpack = masterpack;
    }

    public String getAplicmastpack() {
        return aplicmastpack;
    }

    public void setAplicmastpack(String aplicmastpack) {
        this.aplicmastpack = aplicmastpack;
    }

    public String getInmov() {
        return inmov;
    }

    public void setInmov(String inmov) {
        this.inmov = inmov;
    }

    public String getAplicfrac() {
        return aplicfrac;
    }

    public void setAplicfrac(String aplicfrac) {
        this.aplicfrac = aplicfrac;
    }

    public String getRestringido() {
        return restringido;
    }

    public void setRestringido(String restringido) {
        this.restringido = restringido;
    }

    public int getSiscod() {
        return siscod;
    }

    public void setSiscod(int siscod) {
        this.siscod = siscod;
    }

    public String getSisent() {
        return sisent;
    }

    public void setSisent(String sisent) {
        this.sisent = sisent;
    }

    public String getCodalm() {
        return codalm;
    }

    public void setCodalm(String codalm) {
        this.codalm = codalm;
    }

    public String getCodpro() {
        return codpro;
    }

    public void setCodpro(String codpro) {
        this.codpro = codpro;
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

    public int getStkmin() {
        return stkmin;
    }

    public void setStkmin(int stkmin) {
        this.stkmin = stkmin;
    }

    public int getCant_e() {
        return cant_e;
    }

    public void setCant_e(int cant_e) {
        this.cant_e = cant_e;
    }

    public int getCant_f() {
        return cant_f;
    }

    public void setCant_f(int cant_f) {
        this.cant_f = cant_f;
    }

    public int getStkfra() {
        return stkfra;
    }

    public void setStkfra(int stkfra) {
        this.stkfra = stkfra;
    }

    public BigDecimal getStkmin2() {
        return stkmin2;
    }

    public void setStkmin2(BigDecimal stkmin2) {
        this.stkmin2 = stkmin2;
    }

    public BigDecimal getCoscom() {
        return coscom;
    }

    public void setCoscom(BigDecimal coscom) {
        this.coscom = coscom;
    }

    public BigDecimal getIgvpro() {
        return igvpro;
    }

    public void setIgvpro(BigDecimal igvpro) {
        this.igvpro = igvpro;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getFecven() {
        return fecven;
    }

    public void setFecven(String fecven) {
        this.fecven = fecven;
    }

    public int getQtymov() {
        return qtymov;
    }

    public void setQtymov(int qtymov) {
        this.qtymov = qtymov;
    }

    public int getQtymov_m() {
        return qtymov_m;
    }

    public void setQtymov_m(int qtymov_m) {
        this.qtymov_m = qtymov_m;
    }

    public DistribuirAlmacenEstablecimientos(int siscod, String sisent, String codalm, String codpro, int stkalm, int stkalm_m, int stkmin, int cant_e, int cant_f, int stkfra, BigDecimal stkmin2, BigDecimal coscom, BigDecimal igvpro, String lote, String fecven, int qtymov, int qtymov_m) {
        this.siscod = siscod;
        this.sisent = sisent;
        this.codalm = codalm;
        this.codpro = codpro;
        this.stkalm = stkalm;
        this.stkalm_m = stkalm_m;
        this.stkmin = stkmin;
        this.cant_e = cant_e;
        this.cant_f = cant_f;
        this.stkfra = stkfra;
        this.stkmin2 = stkmin2;
        this.coscom = coscom;
        this.igvpro = igvpro;
        this.lote = lote;
        this.fecven = fecven;
        this.qtymov = qtymov;
        this.qtymov_m = qtymov_m;
    }

    public DistribuirAlmacenEstablecimientos(int siscod, String sisent, String codalm, String codpro, int stkalm, int stkalm_m, int stkmin, int cant_e, int cant_f, int stkfra, BigDecimal stkmin2, BigDecimal coscom, BigDecimal igvpro, String lote, String fecven, int qtymov, int qtymov_m, String restringido) {
        this.siscod = siscod;
        this.sisent = sisent;
        this.codalm = codalm;
        this.codpro = codpro;
        this.stkalm = stkalm;
        this.stkalm_m = stkalm_m;
        this.stkmin = stkmin;
        this.cant_e = cant_e;
        this.cant_f = cant_f;
        this.stkfra = stkfra;
        this.stkmin2 = stkmin2;
        this.coscom = coscom;
        this.igvpro = igvpro;
        this.lote = lote;
        this.fecven = fecven;
        this.restringido = restringido;
        this.qtymov = qtymov;
        this.qtymov_m = qtymov_m;
    }
    private int qtymov;
    private int qtymov_m;
}
