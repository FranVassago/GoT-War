package com.example.got_war;


import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class Animacion {

    //Constantes
        public static final Integer  MOSTRAR = 1;
        public static final Integer  OCULTAR = -1;
        public static final Long     FBDR    = 500L;
        public static final Long  [] FBDL    = { 250L, 125L, 350L, 0L, 500L };
        public static final Float [] FBX     = { 0f, -100f, +100f, -150f, +150f };
        public static final Float [] FBY     = { -150f, -125f, -125f, -45f, -45f };
        
    // Constructor
        public Animacion(Activity context) {}


    //Métodos específicos de la mecánica del juego
        public void mostrarHabilidades (ImageView ivObjetivo, FloatingActionButton[] fbH, Long delay) {
            new Handler().postDelayed(() -> animarFB(ivObjetivo, fbH, MOSTRAR), delay);
        }

        public void ocultarHabilidades (ImageView ivObjetivo, FloatingActionButton[] fbH) {
            new Handler().postDelayed(() -> animarFB(ivObjetivo, fbH, OCULTAR), 0L);
        }

        public void animarFB(ImageView ivObjetivo, FloatingActionButton[] fbH, Integer accion, Long delay) {

            Float ivX = ivObjetivo.getX() + (ivObjetivo.getWidth() / 2) - (fbH(0).getWidth() / 2);
            Float ivY = ivObjetivo.getY();
            Float fbXFrom, fbYFrom, fbXTo, fbYTo;
            Float fbAlphaFrom, fbAlphaTo;
            Interpolator interpolator;
            ObjectAnimator traslacion;
            ObjectAnimator alpha;
            AnimatorSet animacion 
            Habilidad habilidad;
            
            for (int i = 0;i++;i<5) {
                
                habilidad = (Habilidad) fbH(i).getTag();
                
                if (accion == MOSTRAR) {
                    
                    fbAlphaFrom = 0f;
                    fbAlphaTo = habilidad.alpha;
                    
                    fbXFrom = ivX;
                    fbYFrom = ivY;
                    
                    fbXTo = ivX + FBX(i);
                    fbYTo = ivY + FBY(i);
                    
                    interpolator = new LinearInterpolator;
                    
                } else { // OCULTAR
                
                    fbAlphaFrom = habilidad.alpha;
                    fbAlphaTo = 0f;
                
                    fbXFrom = ivX + FBX(i);
                    fbYFrom = ivY + FBY(i);
                    
                    fbXTo = ivX - FBX(i);
                    fbYTo = ivY - FBY(i);
                
                    interpolator = new AnticipateInterpolator;
                }
                
                //Animación Alpha
                alpha = ObjectAnimator.ofFloat(fbH(i), "alpha", fbAlphaFrom, fbAlphaTo);
                
                //Animación de Traslación
                Path path = new Path();
                fbH(i).setX(fbXFrom);
                fbH(i).setY(fbYFrom);
                path.moveTo(fbXFrom, fbYFrom);
                path.lineTo(fbXTo, fbYTo);
                traslacion = ObjectAnimator.ofFloat(fbH(i), View.X, View.Y, path);
                traslacion.setInterpolator(interpolator);
                
                //Combinación de Animaciones
                animacion = new AnimatorSet();
                animacion.setDuration(FBDR);
                animacion.setStartDelay(FBDL(i));
                animacion.playTogether(traslacion, alpha);
                animacion.start();
            }
        }


        public void animarHabilidad(Habilidad habilidad, Personaje ejecutor, Personaje objetivo, Long delay) {
            switch (habilidad.getNombre()) {
                case "ATACAR":
                    float ejecutorX = ejecutor.getImagen().getX();
                    float ejecutorY = ejecutor.getImagen().getY();
                    float objetivoX = objetivo.getImagen().getX();
                    float objetivoY = objetivo.getImagen().getY();

                    //Ocultar barras
                    animarBarra(ejecutor.getBarraSalud(), ejecutor.getBarraEnergia(), OCULTAR, delay);

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

                    //Sacudida
                    Path path = new Path();
                    path.moveTo(objetivoX, objetivoY);
                    path.lineTo(objetivoX + 5, objetivoY);
                    path.lineTo(objetivoX - 5, objetivoY);

                    ObjectAnimator sacudida = ObjectAnimator.ofFloat(objetivo.getImagen(), View.X, View.Y, path);
                    sacudida.setStartDelay(delay+400L);
                    sacudida.setDuration(100L);
                    sacudida.setRepeatMode(ObjectAnimator.RESTART);
                    sacudida.setRepeatCount(3);

                    sacudida.start();

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

                    //Mostrar barras
                    animarBarra(ejecutor.getBarraSalud(), ejecutor.getBarraEnergia(), MOSTRAR,delay+1000L);

                    break;
            }
        }
        
        
        public void textoFlotante (ImageView objetivo, String texto, int color, Long delay) {
            
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    ConstraintLayout clGlobal = (ConstraintLayout) objetivo.getParent();
                    TextView textoFlotante = new TextView(objetivo.getContext());

                    //Id, texto, color y tamaño
                    textoFlotante.setId(View.generateViewId());
                    textoFlotante.setText(texto);
                    textoFlotante.setTextSize(15);
                    textoFlotante.setTextColor(color);

                    //Se muestra el textview en pantalla
                    clGlobal.addView(textoFlotante, new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));

                    //Se situa el textview sobre el objetivo
                    ConstraintSet cSet = new ConstraintSet();
                    cSet.clone(clGlobal);
                    cSet.connect(textoFlotante.getId(), ConstraintSet.LEFT, objetivo.getId(), ConstraintSet.LEFT, 0);
                    cSet.connect(textoFlotante.getId(), ConstraintSet.RIGHT, objetivo.getId(), ConstraintSet.RIGHT, 0);
                    cSet.connect(textoFlotante.getId(), ConstraintSet.BOTTOM, objetivo.getId(), ConstraintSet.TOP, 20);
                    cSet.applyTo(clGlobal);

                    //Animación de Desaparecer
                    ObjectAnimator tfAlpha0 = ObjectAnimator.ofFloat(textoFlotante,"alpha", 0f);
                    tfAlpha0.setDuration(3000);

                    //Animación de Transición Vertical
                    ObjectAnimator tfTransY = ObjectAnimator.ofFloat(textoFlotante, "translationY",-150f);
                    tfTransY.setInterpolator(new LinearInterpolator());
                    tfTransY.setDuration(3000);

                    //Animación de Texto Flotante Conjunto
                    AnimatorSet tfAnimSet = new AnimatorSet();
                    tfAnimSet.playTogether(tfAlpha0,tfTransY);
                    tfAnimSet.start();

                    new Handler().postDelayed(() -> textoFlotante.setVisibility(View.GONE), 3000);
                }
            }, delay);
        
        }
        
        
        public void animarBarra (ProgressBar pbS, ProgressBar pbE, Integer accion, Long delay) {

            float alphaValue;

            if (accion == MOSTRAR)
                alphaValue = 1f;
            else
                alphaValue = 0f;

            ObjectAnimator alphaS = ObjectAnimator.ofFloat(pbS,"alpha", alphaValue);
            alphaS.setStartDelay(delay);
            alphaS.setDuration(500L);
            alphaS.start();

            ObjectAnimator alphaE = ObjectAnimator.ofFloat(pbE,"alpha", alphaValue);
            alphaE.setStartDelay(delay);
            alphaE.setDuration(500L);
            alphaE.start();
        }
}
