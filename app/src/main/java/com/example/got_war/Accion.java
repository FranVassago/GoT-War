package com.example.got_war;

import java.util.ArrayList;

public class Accion {
    private Float prioridad;
    private Personaje ejecutor;
    private Habilidad habilidad;
    private ArrayList<Personaje> objetivos;
    private int delayAccion;

    //Constructor
    public Accion() {
        this.objetivos = new ArrayList<Personaje>();
    }

    //Getters
        public float getPrioridad() { return this.prioridad; }
        public Habilidad getHabilidad() { return this.habilidad; }
        public ArrayList<Personaje> getObjetivos() { return this.objetivos; }
        public int getDelayAccion() { return this.delayAccion; }

    //Setters
        public void setPrioridad(float prioridad) { this.prioridad = prioridad; }
        public void setEjecutor(Personaje ejecutor) { this.ejecutor = ejecutor; }
        public void setHabilidad(Habilidad habilidad) { this.habilidad = habilidad; }
        public void setVictimas(Personaje victima) { this.victimas.add(victima); }


    //Métodos con la mecánica de las acciones
        public long ejecutar(long delayTurnos, ReproductorAnimaciones reproductorAnimaciones) {
            if (habilidad.modificaEnergia()) {
                ejecutor.modificaEnergiaRestante(habilidad.getEnergia());
            }
    
            if (habilidad.causaDanio()) {
                String resAtaque;
                for (Personaje objetivo : objetivos) {
                    resAtaque = objetivo.recibirDanio(ejecutor.getAtaque(), ejecutor.getModAtaque(), habilidad.getTipoDanio(), delayTurnos + delayAccion);
                    reproductorAnimaciones.textoFlotante(objetivo, resAtaque, Color.RED, delayTurnos + delayAccion);
                }
            }
    
            if (habilidad.activaDefensa()) {
                objetivo.setDefendiendo(true);
            }
    
            if (habilidad.getModificador() != null){
                objetivo.incluirModificador(habilidad.getModificador());
            }
    
            //Animaciòn de la habilidad
            reproductorAnimaciones.animarHabilidad(habilidad, ejecutor, objetivo, delayTurnos + delayAccion);
                    
    
            return delayTurnos + habilidad.getDelay();
        }
}
