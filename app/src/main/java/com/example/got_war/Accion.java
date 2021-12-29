package com.example.got_war;

import java.util.ArrayList;

public class Accion {
    private Float prioridad;
    private Personaje ejecutor;
    private Habilidad habilidad;
    private ArrayList<Personaje> victimas;
    private int delayAccion;

    //Constructor
    public Accion() {
        this.victimas = new ArrayList<Personaje>();
    }

    //Getters
        public float getPrioridad() {
        return this.prioridad;
    }
        public Habilidad getHabilidad() {
        return this.habilidad;
    }
        public ArrayList<Personaje> getVictimas() {
        return this.victimas;
    }
        public int getDelayAccion() { return this.delayAccion; }

    //Setters
        public void setPrioridad(float prioridad) {
        this.prioridad = prioridad;
    }
        public void setEjecutor(Personaje ejecutor) {
        this.ejecutor = ejecutor;
    }
        public void setHabilidad(Habilidad habilidad) {
        this.habilidad = habilidad;
    }
        public void setVictimas(Personaje victima) {
        this.victimas.add(victima);
    }


    //Métodos con la mecánica de las acciones
    public long ejecutar() {
        long delay = 0;

        if (habilidad.modificaEnergia()) {
            ejecutor.modificaEnergiaRestante(habilidad.getEnergia());
        }

        if (habilidad.causaDanio()) {
            for (Personaje victima : victimas) {
                ejecutor.mostrarAnimacion("ATACAR", victima, delayAccion);
                victima.recibirDanio(ejecutor.getAtaque(), habilidad.getTipoDanio());
            }
        }

        if (habilidad.activaDefensa()) {
            ejecutor.setDefendiendo(true);
        }

        delay = habilidad.getDelay();

        return delay;
    }
}
