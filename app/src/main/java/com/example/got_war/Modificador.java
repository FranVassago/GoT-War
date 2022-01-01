package com.example.got_war;

public class Modificador {
    private String nombre;
    private int rondas;
    private int acumulativo;
    private int danio;
    private Integer modIniciativa;
    private Integer modAtaque;
    private Integer modDefensa;
    private Integer modAgilidad;
    private Integer modVoluntad;

    public Modificador(String nombre, int rondas, int acumulativo, int danio, Integer modIniciativa, Integer modAtaque, Integer modDefensa, Integer modAgilidad, Integer modVoluntad) {
        this.nombre = nombre;
        this.rondas = rondas;
        this.acumulativo = acumulativo;
        this.danio = danio;
        this.modIniciativa = modIniciativa;
        this.modAtaque = modAtaque;
        this.modDefensa = modDefensa;
        this.modAgilidad = modAgilidad;
        this.modVoluntad = modVoluntad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getRondas() {
        return rondas;
    }

    public void setRondas(int rondas) {
        this.rondas = rondas;
    }

    public int getAcumulativo() {
        return acumulativo;
    }

    public void setAcumulativo(int acumulativo) {
        this.acumulativo = acumulativo;
    }

    public int getDanio() {
        return danio;
    }

    public void setDanio(int danio) {
        this.danio = danio;
    }

    public Integer getModIniciativa() {
        return modIniciativa;
    }

    public void setModIniciativa(Integer modIniciativa) {
        this.modIniciativa = modIniciativa;
    }

    public Integer getModAtaque() {
        return modAtaque;
    }

    public void setModAtaque(Integer modAtaque) {
        this.modAtaque = modAtaque;
    }

    public Integer getModDefensa() {
        return modDefensa;
    }

    public void setModDefensa(Integer modDefensa) {
        this.modDefensa = modDefensa;
    }

    public Integer getModAgilidad() {
        return modAgilidad;
    }

    public void setModAgilidad(Integer modAgilidad) {
        this.modAgilidad = modAgilidad;
    }

    public Integer getModVoluntad() {
        return modVoluntad;
    }

    public void setModVoluntad(Integer modVoluntad) {
        this.modVoluntad = modVoluntad;
    }

    public void restarRondas() {
        this.rondas--;
    }
}
