package com.example.got_war;

public class Habilidad {
    private Integer prioridad;
    private String nombre;
    private String tipoObjetivo; // INDIVIDUAL - TODOS_LOS_ENEMIGOS - TODOS_LOS_ALIADOS
    private Integer danio;
    private String tipoDanio; // FISICO - MAGICO
    private Boolean activarDefensa;

    public Habilidad(Integer prioridad, String nombre, String tipoObjetivo, Integer danio, String tipoDanio, Boolean activarDefensa) {
        this.prioridad = prioridad;
        this.nombre = nombre;
        this.tipoObjetivo = tipoObjetivo;
        this.danio = danio;
        this.tipoDanio = tipoDanio;
        this.activarDefensa = activarDefensa;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoObjetivo() {
        return tipoObjetivo;
    }

    public void setObjetivo(String tipoObjetivo) {
        this.tipoObjetivo = tipoObjetivo;
    }

    public Integer getDanio() {
        return danio;
    }

    public void setDanio(Integer danio) {
        this.danio = danio;
    }

    public String getTipoDanio() {
        return tipoDanio;
    }

    public void setTipoDanio(String tipoDanio) {
        this.tipoDanio = tipoDanio;
    }

    public Boolean getActivarDefensa() {
        return activarDefensa;
    }

    public void setActivarDefensa(Boolean activarDefensa) {
        this.activarDefensa = activarDefensa;
    }
}
