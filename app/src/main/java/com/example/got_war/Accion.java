package com.example.got_war;

import android.graphics.Color;

import java.util.ArrayList;

public class Accion {
    private Float prioridad;
    private Personaje ejecutor;
    private Habilidad habilidad;
    private ArrayList<Personaje> objetivos;

    //Constructor
        public Accion() {
            this.objetivos = new ArrayList<Personaje>();
        }

    //Getters
        public float getPrioridad() { return this.prioridad; }
        public Habilidad getHabilidad() { return this.habilidad; }
        public ArrayList<Personaje> getObjetivos() { return this.objetivos; }

    //Setters
        public void setPrioridad(float prioridad) { this.prioridad = prioridad; }
        public void setEjecutor(Personaje ejecutor) { this.ejecutor = ejecutor; }
        public void setHabilidad(Habilidad habilidad) { this.habilidad = habilidad; }
        public void setObjetivos(Personaje objetivo) { this.objetivos.add(objetivo); }


    //Métodos con la mecánica de las acciones
        public long ejecutar(long delayTurnos, Animacion animacion) {

            Long delayAccion = 0L;
            Long delayTexto;

            for (Personaje objetivo : objetivos) {

                delayTexto = 0L;

                //La habilidad consume o genera energia
                if (habilidad.modificaEnergia()) {
                    ejecutor.modificarEnergia(habilidad.getEnergia(), delayTurnos);
                }

                //La habilidad activa el modo defensa
                if (habilidad.activaDefensa()) {
                    objetivo.setDefendiendo(true);
                    animacion.textoFlotante(objetivo.getImagen(), "DEFENDIENDO", Color.WHITE, delayTurnos + delayTexto);
                    delayTexto = delayTexto + 500L;
                }

                //La habilidad causa daño
                if (habilidad.causaDanio()) {
                    String resAtaque;

                    resAtaque = objetivo.recibirDanio(ejecutor.generarAtaque(habilidad.getTipoDanio()), ejecutor.getModAtaque(), habilidad.getTipoDanio(), delayTurnos + 400L);
                    animacion.textoFlotante(objetivo.getImagen(), resAtaque, Color.RED, delayTurnos + 400L + delayTexto);
                    delayTexto = delayTexto + 500L;
                }

                //La habilidad aplica modificadores
                if (habilidad.getModificador() != null){
                    Boolean aplicable = objetivo.incluirModificador(habilidad.getModificador());

                    if (aplicable)
                        animacion.textoFlotante(objetivo.getImagen(), habilidad.getModificador().getNombre(), Color.WHITE, delayTurnos + delayTexto);
                    else
                        animacion.textoFlotante(objetivo.getImagen(), "RESISTE " + habilidad.getModificador().getNombre(), Color.WHITE, delayTurnos + delayTexto);
                }

                //Animación de la habilidad
                animacion.animarHabilidad(habilidad, ejecutor, objetivo, delayTurnos);

                delayAccion = habilidad.getDelay();

            }
    
            return delayAccion;

        }
}
