package com.example.got_war;

import java.util.ArrayList;

public class Accion {
    private Float prioridad;
    private Personaje ejecutor;
    private String habilidad;
    private ArrayList<Personaje> victimas;

    public Accion() {
        this.victimas = new ArrayList<Personaje>();
    }

    public float getPrioridad() {
        return this.prioridad;
    }

    public void setPrioridad(float prioridad) {
        this.prioridad = prioridad;
    }

    public Personaje getEjecutor() {
        return this.ejecutor;
    }

    public void setEjecutor(Personaje ejecutor) {
        this.ejecutor = ejecutor;
    }

    public String getHabilidad() {
        return this.habilidad;
    }

    public void setHabilidad(String habilidad) {
        this.habilidad = habilidad;
    }

    public ArrayList<Personaje> getVictimas() {
        return this.victimas;
    }

    public void setVictimas(Personaje victima) {
        this.victimas.add(victima);
    }
}
