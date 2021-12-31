package com.example.got_war;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReproductorAnimaciones {

    public ReproductorAnimaciones() {}

    //Métodos específicos de la mecánica del juego
        public void mostrarHabilidades (ImageView imagenObjetivo, FloatingActionButton fbH1, FloatingActionButton fbH2,FloatingActionButton fbH3, FloatingActionButton fbH4, FloatingActionButton fbH5,Long delay) {

            Long duration = 500L;
            Float iX = imagenObjetivo.getX() + (imagenObjetivo.getWidth() / 2) - (fbH1.getWidth() / 2);
            Float iY = imagenObjetivo.getY();


            new Handler().postDelayed(() -> animarFB(fbH1,iX, iY, +0f,  -150f, 0L,   duration, "MOSTRAR"), delay);
            new Handler().postDelayed(() -> animarFB(fbH2,iX, iY, -100f,-125f, 125L, duration, "MOSTRAR"), delay);
            new Handler().postDelayed(() -> animarFB(fbH3,iX, iY, +100f,-125f, 250L, duration, "MOSTRAR"), delay);
            new Handler().postDelayed(() -> animarFB(fbH4,iX, iY, -150f,-45f , 350L, duration, "MOSTRAR"), delay);
            new Handler().postDelayed(() -> animarFB(fbH5,iX, iY, +150f,-45f , 500L, duration, "MOSTRAR"), delay);

        }

        public void ocultarHabilidades (ImageView imagenObjetivo, FloatingActionButton fbH1, FloatingActionButton fbH2,FloatingActionButton fbH3, FloatingActionButton fbH4, FloatingActionButton fbH5,Long delay) {

            Long duration = 500L;
            Float iX = imagenObjetivo.getX() + (imagenObjetivo.getWidth() / 2) - (fbH1.getWidth() / 2);
            Float iY = imagenObjetivo.getY();

            new Handler().postDelayed(() -> animarFB(fbH1,iX+0f  , iY-150f, +0f,  +150f, 0L,   duration, "OCULTAR"), delay);
            new Handler().postDelayed(() -> animarFB(fbH2,iX-100f, iY-125f, +100f,+125f, 125L, duration, "OCULTAR"), delay);
            new Handler().postDelayed(() -> animarFB(fbH3,iX+100f, iY-125f, -100f,+125f, 250L, duration, "OCULTAR"), delay);
            new Handler().postDelayed(() -> animarFB(fbH4,iX-150f, iY-45f , +150f,+45f , 350L, duration, "OCULTAR"), delay);
            new Handler().postDelayed(() -> animarFB(fbH5,iX+150f, iY-45f , -150f,+45f , 500L, duration, "OCULTAR"), delay);
        }

        public void animarFB(FloatingActionButton fbH, Float iX, Float iY, Float oX, Float oY, Long fbDelay, Long duration, String accion) {

            AnimatorSet animacion = new AnimatorSet();
            ObjectAnimator traslacion;
            ObjectAnimator alpha;
            Path path = new Path();
            Habilidad habilidad;
            String nombreHabilidad;

            if (fbH.getTag() != null){
                habilidad = (Habilidad) fbH.getTag();
                nombreHabilidad = habilidad.getNombre();
            } else {
                nombreHabilidad = "NULL";
            }

            //Se lleva el botón al personaje objetivo
            fbH.setX(iX);
            fbH.setY(iY);

            //Establecemos la animación de traslación
            path.moveTo(iX, iY);
            path.lineTo(iX + oX, iY + oY);
            traslacion = ObjectAnimator.ofFloat(fbH, View.X, View.Y, path);

            if (accion == "MOSTRAR") {
                traslacion.setInterpolator(new OvershootInterpolator());
                if (nombreHabilidad == "BLOQUEADO" || nombreHabilidad == "NULL")
                    alpha = ObjectAnimator.ofFloat(fbH, "alpha", 0f, 0.5f);
                else
                    alpha = ObjectAnimator.ofFloat(fbH, "alpha", 0f, 1f);
            } else { // "OCULTAR"
                traslacion.setInterpolator(new AnticipateInterpolator());
                if (nombreHabilidad == "BLOQUEADO" || nombreHabilidad == "NULL")
                    alpha = ObjectAnimator.ofFloat(fbH, "alpha", 0.5f, 0f);
                else
                    alpha = ObjectAnimator.ofFloat(fbH, "alpha", 1f, 0f);
            }

            //Duración y retardo
            animacion.setDuration(duration);
            animacion.setStartDelay(fbDelay);

            //Mostramos la animación
            animacion.playTogether(traslacion,alpha);
            animacion.start();

        }
}