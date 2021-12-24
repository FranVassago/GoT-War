package com.example.got_war;

import android.widget.ImageView;
import android.widget.ProgressBar;

public class Personaje {
    private String id;
    private Integer jugador;
    private Integer posicion;
    private String nombre;
    private String casa;
    private Boolean estaVivo;
    private Boolean estaDefendiendo;
    private Integer saludTotal;
    private Integer saludRestante;
    private Integer energiaTotal;
    private Integer energiaRestante;
    private Integer iniciativa;
    private Integer modIniciativa;
    private Integer ataque;
    private Integer modAtaque;
    private Integer defensa;
    private Integer modDefensa;
    private Integer agilidad;
    private Integer modAgilidad;
    private Integer voluntad;
    private Integer modVoluntad;
    private ImageView imagen;
    private ProgressBar barraSalud;
    private ProgressBar barraEnergia;

    public Personaje(
            String id,
            Integer jugador,
            Integer posicion,
            String nombre,
            String casa,
            Integer saludTotal,
            Integer saludRestante,
            Integer energiaTotal,
            Integer energiaRestante,
            Integer iniciativa,
            Integer ataque,
            Integer defensa,
            Integer agilidad,
            Integer voluntad,
            ImageView imagen,
            ProgressBar barraSalud,
            ProgressBar barraEnergia) {
        //Básicos
        this.id = id;
        this.jugador = jugador;
        this.posicion = posicion;
        this.nombre = nombre;
        this.casa = casa;

        //Estados
        this.estaVivo = true;
        this.estaDefendiendo = false;

        //Medidores
        this.saludTotal = saludTotal;
        this.saludRestante = saludRestante;
        this.energiaTotal = energiaRestante;
        this.energiaRestante = energiaRestante;

        //Estadísticas
        this.iniciativa = iniciativa;
        this.ataque = ataque;
        this.defensa = defensa;
        this.agilidad = agilidad;
        this.voluntad = voluntad;
        this.imagen = imagen;
        this.barraSalud = barraSalud;
        this.barraEnergia = barraEnergia;

        //Modificadores
        this.modIniciativa = 0;
        this.modAtaque = 0;
        this.modDefensa = 0;
        this.modAgilidad = 0;
        this.modVoluntad = 0;

        actualizarBarraSalud ();
        actualizarBarraEnergia();
    }

    public String getNombre() { return this.nombre; }

    public Integer getJugador() {
        return this.jugador;
    }

    public Float getIniciativa () {
        return (float) this.iniciativa + this.modIniciativa + ((this.voluntad + this.modVoluntad) / 100);
    }

    public Integer getVoluntad() {
        return this.voluntad + this.modVoluntad;
    }

    public Integer getAtaque() {
        return this.ataque + this.modAtaque;
    }

    public Integer getDefensa() {
        return this.defensa + this.modDefensa;
    }

    public void setDefendiendo(Boolean defendiendo) {
        this.estaDefendiendo = defendiendo;
    }

    public void recibirDanio (Integer danio, String tipoDanio) {
        if (this.estaDefendiendo){
            switch (tipoDanio) {
                case "FISICO":
                    danio = danio - getDefensa();
                    break;
                case "MAGICO":
                    danio = danio - getVoluntad();
                    break;
            }
        }

        if (danio < 0) danio = 0;

        saludRestante = saludRestante - danio;

        actualizarBarraSalud();

    }

    public void actualizarBarraSalud () {
        barraSalud.setMax(this.saludTotal);
        barraSalud.setProgress(this.saludRestante);
    }

    public void actualizarBarraEnergia () {
        barraEnergia.setMax(this.saludTotal);
        barraEnergia.setProgress(this.saludRestante);
    }
}
