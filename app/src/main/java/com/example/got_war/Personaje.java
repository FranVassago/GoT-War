package com.example.got_war;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Path;
import android.os.Handler;
import android.text.Layout;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
    private ArrayList<Modificador> modificadores;

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

        //Visual
        this.imagen = imagen;
        this.barraSalud = barraSalud;
        this.barraEnergia = barraEnergia;

        //Modificadores
        this.modIniciativa = 0;
        this.modAtaque = 0;
        this.modDefensa = 0;
        this.modAgilidad = 0;
        this.modVoluntad = 0;

        this.modificadores = new ArrayList<Modificador>();

        actualizarBarraSalud ();
        actualizarBarraEnergia();
    }

    //Getters
        public String getNombre() { return this.nombre; }
        public Integer getJugador() {
            return this.jugador;
        }
        public int getEnergiaRestante() { return this.energiaRestante; }
        public int getSaludRestante() { return this.saludRestante; }
        public Float getIniciativa () { return (float) this.iniciativa + this.modIniciativa + ((this.voluntad + this.modVoluntad) / 100); }
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
        public ImageView getImagen () {
        return this.imagen;
    }

    //Setters


    //Métodos específicos de la mecánica del juego
    public void modificaEnergiaRestante(Integer energia, long delay) {
        this.energiaRestante = this.energiaRestante + energia;
        if (this.energiaRestante > this.energiaTotal)
            this.energiaRestante = this.energiaTotal;

        new Handler().PostDelayed(() -> actualizarBarraEnergia(), delay);
    }

    public void recibirDanio (Integer danio, String tipoDanio, Long delay) {
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

        new Handler().postDelayed(() -> actualizarBarraSalud(), delay);

    }

    public void actualizarBarraSalud () {
        barraSalud.setMax(this.saludTotal);
        barraSalud.setProgress(this.saludRestante);
    }

    public void actualizarBarraEnergia () {
        barraEnergia.setMax(this.energiaTotal);
        barraEnergia.setProgress(this.energiaRestante);
    }

    public Boolean incluirModificador (Modificador modificadorNuevo) {
        int acumulados = 0;
        boolean aplicable = false;

        for (Modificador modificador : this.modificadores) {
            if (modificador.getNombre() == modificadorNuevo.getNombre())
                acumulados++;
        }

        if (acumulados < modificadorNuevo.getAcumulativo())
        {
            modificadores.add(modificadorNuevo);
            aplicable = true;
        }
        
        return aplicable;
    }

    public void actualizarModificadores () {
        for (Modificador modificador : this.modificadores) {
            this.modAtaque = 0;
            this.modDefensa = 0;
            this.modAgilidad = 0;
            this.modVoluntad = 0;
            this.modIniciativa = 0;

            modificador.restarRondas();
            if (modificador.getRondas() == 0) {
                modificadores.remove(modificadores.indexOf(modificador));
            } else {
                this.modAtaque = this.modAtaque + modificador.getModAtaque();
                this.modDefensa = this.modDefensa + modificador.getModDefensa();
                this.modAgilidad = this.modAgilidad + modificador.getModAgilidad();
                this.modVoluntad = this.modVoluntad + modificador.getModVoluntad();
                this.modIniciativa = this.modIniciativa + modificador.getModIniciativa();
            }
        }
    }
}

