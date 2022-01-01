package com.example.got_war;


import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ReproductorAnimaciones {
    
    private Activity context;

    public ReproductorAnimaciones(Activity context) {
        this.context = context;
    }

    //Métodos específicos de la mecánica del juego
        public void mostrarHabilidades (ImageView imagenObjetivo, FloatingActionButton fbH1, FloatingActionButton fbH2,FloatingActionButton fbH3, FloatingActionButton fbH4, FloatingActionButton fbH5,Long delay) {

            Long duration = 500L;
            Float iX = imagenObjetivo.getX() + (imagenObjetivo.getWidth() / 2) - (fbH1.getWidth() / 2);
            Float iY = imagenObjetivo.getY();

            new Handler().postDelayed(() -> animarFB(fbH1,iX, iY, +0f,  -150f, 250L,   duration, "MOSTRAR"), delay);
            new Handler().postDelayed(() -> animarFB(fbH2,iX, iY, -100f,-125f, 125L, duration, "MOSTRAR"), delay);
            new Handler().postDelayed(() -> animarFB(fbH3,iX, iY, +100f,-125f, 350L, duration, "MOSTRAR"), delay);
            new Handler().postDelayed(() -> animarFB(fbH4,iX, iY, -150f,-45f , 0L, duration, "MOSTRAR"), delay);
            new Handler().postDelayed(() -> animarFB(fbH5,iX, iY, +150f,-45f , 500L, duration, "MOSTRAR"), delay);

        }

        public void ocultarHabilidades (ImageView imagenObjetivo, FloatingActionButton fbH1, FloatingActionButton fbH2,FloatingActionButton fbH3, FloatingActionButton fbH4, FloatingActionButton fbH5,Long delay) {

            Long duration = 500L;
            Float iX = imagenObjetivo.getX() + (imagenObjetivo.getWidth() / 2) - (fbH1.getWidth() / 2);
            Float iY = imagenObjetivo.getY();

            new Handler().postDelayed(() -> animarFB(fbH1,iX+0f  , iY-150f, +0f,  +150f, 250L,   duration, "OCULTAR"), delay);
            new Handler().postDelayed(() -> animarFB(fbH2,iX-100f, iY-125f, +100f,+125f, 350L, duration, "OCULTAR"), delay);
            new Handler().postDelayed(() -> animarFB(fbH3,iX+100f, iY-125f, -100f,+125f, 125L, duration, "OCULTAR"), delay);
            new Handler().postDelayed(() -> animarFB(fbH4,iX-150f, iY-45f , +150f,+45f , 500L, duration, "OCULTAR"), delay);
            new Handler().postDelayed(() -> animarFB(fbH5,iX+150f, iY-45f , -150f,+45f , 0L, duration, "OCULTAR"), delay);
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

        public void animarHabilidad(Habilidad habilidad, Personaje ejecutor, Personaje objetivo, Long delay) {
            switch (habilidad.getNombre()) {
                case "ATACAR":
                    float ejecutorX = ejecutor.getImagen().getX();
                    float ejecutorY = ejecutor.getImagen().getY();
                    float objetivoX = objetivo.getImagen().getX();
                    float objetivoY = objetivo.getImagen().getY();

                    //Ida
                    ObjectAnimator idaX;
                    ObjectAnimator idaY;
                    AnimatorSet animacionIda = new AnimatorSet();

                    idaX = ObjectAnimator.ofFloat(ejecutor.getImagen(), "translationX", objetivoX - ejecutorX);
                    idaY = ObjectAnimator.ofFloat(ejecutor.getImagen(), "translationY", objetivoY - ejecutorY);
                    idaX.setupStartValues();
                    idaY.setupStartValues();
                    animacionIda.playTogether(idaX, idaY);
                    animacionIda.setStartDelay(delay);
                    animacionIda.setDuration(400L);

                    animacionIda.start();

                    //Daño objetivo
                    ValueAnimator colorAnimation = new ValueAnimator();

                    colorAnimation.setIntValues(Color.parseColor("#B2DC0606"), Color.parseColor("#B2FFFFFF"));
                    colorAnimation.setEvaluator(new ArgbEvaluator());
                    colorAnimation.addUpdateListener(valueAnimator -> objetivo.getImagen().setColorFilter((Integer) valueAnimator.getAnimatedValue()));
                    colorAnimation.setStartDelay(delay+400L);
                    colorAnimation.setDuration(50L);
                    colorAnimation.setRepeatMode(ValueAnimator.RESTART);
                    colorAnimation.setRepeatCount(3);

                    colorAnimation.start();

                    new Handler().postDelayed(() -> objetivo.getImagen().setColorFilter(Color.parseColor("#00FFFFFF")),delay+650L);

                    //Vuelta
                    ObjectAnimator vueltaX;
                    ObjectAnimator vueltaY;
                    AnimatorSet animacionVuelta = new AnimatorSet();


                    vueltaX = ObjectAnimator.ofFloat(ejecutor.getImagen(), "translationX", 0);
                    vueltaY = ObjectAnimator.ofFloat(ejecutor.getImagen(), "translationY", 0);
                    animacionVuelta.playTogether(vueltaX, vueltaY);
                    animacionVuelta.setStartDelay(delay+600L);
                    animacionVuelta.setDuration(400L);

                    animacionVuelta.start();

                    break;
            }
        }
        
        
        public void mostrarTextoFlotante (ImageView objetivo, String texto, Color color, Long delay) {
            
            new Handler().postDelayed(() -> generarTextoFlotante(objetivo, texto, color), delay);
        
        }
        
        
        public void generarTextoFlotante (ImageView objetivo, String texto, Color color) {
            
            TextView textoFlotante = new TextView(context);

            //Id, texto, color y tamaño
            textoFlotante.setId(View.generateViewId());
            textoFlotante.setText(texto);
            textoFlotante.setTextSize(15);
            textoFlotante.setTextColor(color);
            
            //Se situa el textview sobre el objetivo
            ConstraintLayout clGlobal = (ConstraintLayout) context.findViewById(R.id.clGlobal);
            ConstraintSet cSet = new ConstraintSet();
            cSet.clone(clGlobal);
            cSet.connect(textoFlotante.getId(), ConstraintSet.LEFT, objetivo.getId(), ConstraintSet.LEFT, 0);      
            cSet.connect(textoFlotante.getId(), ConstraintSet.RIGHT, objetivo.getId(), ConstraintSet.RIGHT, 0);      
            cSet.connect(textoFlotante.getId(), ConstraintSet.TOP, objetivo.getId(), ConstraintSet.TOP, 0);
            cSet.applyTo(textoFlotante);
            
            //Se muestra el textview en pantalla
            clGlobal.addView(textoFlotante, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            
            //Animación de Desaparecer
            ObjectAnimator tfAlpha0 = ObjectAnimator.ofFloat(textoFlotante,"alpha", 0f);
            tfAlpha0.setDuration(3000);
    
            //Animación de Transición Vertical
            ObjectAnimator tfTransY = ObjectAnimator.ofFloat(textoFlotante, "translationY",-40f);
            tfTransY.setDuration(3000);
    
            //Animación de Texto Flotante Conjunto
            AnimatorSet tfAnimSet = new AnimatorSet();
            tfAnimSet.playTogether(tfAlpha0,tfTransY);
            tfAnimSet.start();
            
            new Handler().postDelayed(() -> textoFlotante.setVisibility(View.GONE), 3000);
            
        }
}
