/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.util.List;

/**
 *
 * @author USUARIO
 */
public class generardatosdetalle {
    int siscod;
    public generardatosdetalle() {
    }

    public int getSiscod() {
        return siscod;
    }

    public void setSiscod(int siscod) {
        this.siscod = siscod;
    }

    public List<Datosgenerartxt> getDatos() {
        return datos;
    }

    public void setDatos(List<Datosgenerartxt> datos) {
        this.datos = datos;
    }
    List<Datosgenerartxt> datos;
}
