package com.example.got_war;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Path;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.constraintlayout.widget.ConstraintLayout;

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
    private LinearLayout imagen;
    private ProgressBar barraSalud;
    private ProgressBar barraEnergia;
    private TextView textoFlotante;

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
            LinearLayout imagen,
            ProgressBar barraSalud,
            ProgressBar barraEnergia,
            TextView textoFlotante) {
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
        this.textoFlotante = textoFlotante;

        //Modificadores
        this.modIniciativa = 0;
        this.modAtaque = 0;
        this.modDefensa = 0;
        this.modAgilidad = 0;
        this.modVoluntad = 0;

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
        public LinearLayout getImagen () {
        return this.imagen;
    }

    //Setters


    //Métodos específicos de la mecánica del juego
    public void modificaEnergiaRestante(Integer energia) {
        this.energiaRestante = this.energiaRestante + energia;
        if (this.energiaRestante > this.energiaTotal)
            this.energiaRestante = this.energiaTotal;
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

        mostrarTextoFlotante(danio.toString(),3000);

    }

    public void actualizarBarraSalud () {
        barraSalud.setMax(this.saludTotal);
        barraSalud.setProgress(this.saludRestante);
    }

    public void actualizarBarraEnergia () {
        barraEnergia.setMax(this.saludTotal);
        barraEnergia.setProgress(this.saludRestante);
    }

    public void mostrarTextoFlotante(String texto, long delay) {

        textoFlotante.setX(imagen.getX() + imagen.getWidth() / 2);
        textoFlotante.setY(imagen.getY());
        textoFlotante.setText(texto);

        textoFlotante.setVisibility(View.VISIBLE);
        //Visualizar
        ObjectAnimator tfAlpha1 = ObjectAnimator.ofFloat(textoFlotante,"alpha", 1f);
        tfAlpha1.setDuration(0);
        tfAlpha1.start();

        //Animación de Desaparecer
        ObjectAnimator tfAlpha0 = ObjectAnimator.ofFloat(textoFlotante,"alpha", 0f);
        tfAlpha0.setDuration(delay);

        //Animación de Transición Vertical
        ObjectAnimator tfTransY = ObjectAnimator.ofFloat(textoFlotante, "translationY",-40f);
        tfTransY.setDuration(delay);

        //Animación de Texto Flotante Conjunto
        AnimatorSet tfAnimSet = new AnimatorSet();
        tfAnimSet.playTogether(tfAlpha0,tfTransY);
        tfAnimSet.start();

    }

    public void mostrarAnimacion(String habilidad, Personaje victima, long delay) {
        switch (habilidad) {
            case "ATACAR":
                float ejecutorX = imagen.getX();
                float ejecutorY = imagen.getY();
                float victimaX = victima.getImagen().getX();
                float victimaY = victima.getImagen().getY();

                Path path = new Path();
                path.moveTo(ejecutorX, ejecutorY);
                path.lineTo(victimaX, victimaY);
                path.lineTo(ejecutorX, ejecutorY);
                ObjectAnimator animator = ObjectAnimator.ofFloat(imagen, View.X, View.Y, path);
                animator.setStartDelay(delay);
                animator.setDuration(1000);
                animator.start();
                break;
        }
    }
}

