package com.example.got_war;

import android.app.Activity;
import android.app.VoiceInteractor;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Contienda extends Thread{

    private Activity context;

    private Jugador jugador1;
    private Jugador jugador2;
    private String fase;
    private Integer turno;
    private Accion declaracion;
    private ArrayList<Accion> acciones;
    private ArrayList<Personaje> personajes;
    private ReproductorAcciones reproductorAcciones;


    //Constructor
        public Contienda(Activity context, Jugador jugador1, Jugador jugador2) {
            this.context = context;
            this.jugador1 = jugador1;
            this.jugador2 = jugador2;

            this.acciones = new ArrayList<Accion>();
            this.personajes = new ArrayList<Personaje>();

            this.reproductorAcciones = new ReproductorAcciones(context);
        }

    //Getters
        public String getFase() {
            return this.fase;
        }

        public Jugador getJugador (Personaje personaje) {
            if (personaje.getJugador() == 1)
                return this.jugador1;
            else
                return this.jugador2;
        }

        public Personaje getPerSel() {
            return this.personajes.get(turno);
        }


    //Setters
        public void setFase (String fase) { this.fase = fase; }


    //Metodos de Gestión del Combate
        public void iniciarRonda ()  {
            this.turno = 0;
            acciones.clear();

            establecerTurnos();
            actualizarModificadores();
            iniciarTurno(getPerSel());
        }

        public void establecerTurnos() {

            personajes.clear();

            //Carga los personajes de los jugadores en la lista de turnos
            for (Personaje personaje : jugador1.getEquipo()) { personajes.add(personaje); }
            for (Personaje personaje : jugador2.getEquipo()) { personajes.add(personaje); }

            //Se ordenan los personajes por orden de iniciativa / voluntad
            ordenarTurnos();
        }

        public void ordenarTurnos() {
            Boolean reordenado = true;
            Personaje personajeAux = null;

            while (reordenado) {
                reordenado = false;
                for (int i = 0; i < personajes.size(); i++) {
                    if (i > 0)
                        if (personajes.get(i - 1).getIniciativa() > personajes.get(i).getIniciativa()) {
                            personajeAux = personajes.get(i - 1);
                            personajes.set(i - 1, personajes.get(i));
                            personajes.set(i, personajeAux);
                            reordenado = true;
                        }
                }
            }
        }

        public void actualizarModificadores() {
            for (Personaje personaje : personajes) {
                personaje.setDefendiendo(false);
            }
        }

        public void iniciarTurno (Personaje personaje)  {

            declaracion = new Accion();

            if (getJugador(personaje).esIA())
            {
                Habilidad J2P1H1 = new Habilidad("ATACAR",true, 10, true, "ENEMIGO", "FISICO", false, null, 2000);
                declaracion.setPrioridad(getPerSel().getIniciativa());
                declaracion.setEjecutor(personaje);
                declaracion.setHabilidad(J2P1H1);
                declaracion.setVictimas(jugador1.getEquipo().get(0));
                acciones.add(declaracion);
                siguienteTurno();
            } else {
                mostrarHabilidades(getPerSel(),0);
                setFase("SELECCIONAR.ACCION");
                actualizarTvFase();
            }
        }

        public void declararAccion (Habilidad habilidad)  {

            setFase("DECLARACION");

            declaracion.setEjecutor(getPerSel());
            declaracion.setHabilidad(habilidad);

            if (habilidad.getNombre() == "DEFENDER") {
               declaracion.setPrioridad((float) 100 + getPerSel().getVoluntad() / 100);
            } else {
               declaracion.setPrioridad(getPerSel().getIniciativa());
            }

            switch (habilidad.getTipoObjetivo()) {
                case "PERSONAL":
                    declaracion.setVictimas(getPerSel());
                    acciones.add(declaracion);
                    siguienteTurno();
                    break;
                case "ENEMIGO":
                    setFase("SELECCIONAR.OBJETIVO.ENEMIGO");
                    actualizarTvFase();
                    //iniciarModoSeleccion();
                    break;
                case "ALIADO":
                    setFase("SELECCIONAR.OBJETIVO.ALIADO");
                    actualizarTvFase();
                    //iniciarModoSeleccion();
                    break;
            }
        }

        public void seleccionarObjetivo(Jugador jugador, Integer posicion)  {
            declaracion.setVictimas(jugador.getEquipo().get(posicion));
            acciones.add(declaracion);
            siguienteTurno();
        }

        public void siguienteTurno()  {
            ocultarHabilidades(getPerSel());

            turno++;
            if (turno >= personajes.size()){
                reproducirAcciones();
                iniciarRonda();
            } else {
                iniciarTurno(getPerSel());
            }

        }
        
        public void reproducirAcciones () {

            reproductorAcciones.setAcciones(acciones);

            Thread threadAcciones = new Thread(reproductorAcciones);

            try {
                threadAcciones.start();
                threadAcciones.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run(){

        }

    //Metodos Gestion Elementos Visuales
        public void actualizarTvFase ()
        {
            TextView tvFase = context.findViewById(R.id.tvFase);
            tvFase.setText(getPerSel().getNombre() + ": " + this.fase);
        }

        public void mostrarHabilidades (Personaje personaje, long delay) {

            FloatingActionButton fbH1, fbH2, fbH3, fbH4, fbH5;

            Animation animH1 = AnimationUtils.loadAnimation(context, R.anim.mostrarhabilidadh1);
            Animation animH2 = AnimationUtils.loadAnimation(context, R.anim.mostrarhabilidadh2);
            Animation animH3 = AnimationUtils.loadAnimation(context, R.anim.mostrarhabilidadh3);
            Animation animH4 = AnimationUtils.loadAnimation(context, R.anim.mostrarhabilidadh4);
            Animation animH5 = AnimationUtils.loadAnimation(context, R.anim.mostrarhabilidadh5);

            fbH1 = context.findViewById(R.id.fbJ1P1H1);
            fbH2 = context.findViewById(R.id.fbJ1P1H2);
            fbH3 = context.findViewById(R.id.fbJ1P1H3);
            fbH4 = context.findViewById(R.id.fbJ1P1H4);
            fbH5 = context.findViewById(R.id.fbJ1P1H5);

            animH4.setStartOffset(delay + 0);
            animH2.setStartOffset(delay + 125);
            animH1.setStartOffset(delay + 250);
            animH3.setStartOffset(delay + 350);
            animH5.setStartOffset(delay + 500);

            fbH1.startAnimation(animH1);
            fbH2.startAnimation(animH2);
            fbH3.startAnimation(animH3);
            fbH4.startAnimation(animH4);
            fbH5.startAnimation(animH5);

            fbH1.setVisibility(View.VISIBLE);
            fbH2.setVisibility(View.VISIBLE);
            fbH3.setVisibility(View.VISIBLE);
            fbH4.setVisibility(View.VISIBLE);
            fbH5.setVisibility(View.VISIBLE);

            fbH1.setClickable(true);
            fbH2.setClickable(true);
            fbH3.setClickable(true);
            fbH4.setClickable(true);
            fbH5.setClickable(true);


        }

    public void ocultarHabilidades (Personaje personaje) {

        FloatingActionButton fbH1, fbH2, fbH3, fbH4, fbH5;

        Animation animH1 = AnimationUtils.loadAnimation(context, R.anim.ocultarhabilidadh1);
        Animation animH2 = AnimationUtils.loadAnimation(context, R.anim.ocultarhabilidadh2);
        Animation animH3 = AnimationUtils.loadAnimation(context, R.anim.ocultarhabilidadh3);
        Animation animH4 = AnimationUtils.loadAnimation(context, R.anim.ocultarhabilidadh4);
        Animation animH5 = AnimationUtils.loadAnimation(context, R.anim.ocultarhabilidadh5);

        fbH1 = context.findViewById(R.id.fbJ1P1H1);
        fbH2 = context.findViewById(R.id.fbJ1P1H2);
        fbH3 = context.findViewById(R.id.fbJ1P1H3);
        fbH4 = context.findViewById(R.id.fbJ1P1H4);
        fbH5 = context.findViewById(R.id.fbJ1P1H5);

        fbH1.setClickable(false);
        fbH2.setClickable(false);
        fbH3.setClickable(false);
        fbH4.setClickable(false);
        fbH5.setClickable(false);

        animH4.setStartOffset(500);
        animH2.setStartOffset(325);
        animH1.setStartOffset(250);
        animH3.setStartOffset(125);
        animH5.setStartOffset(0);

        fbH1.startAnimation(animH1);
        fbH2.startAnimation(animH2);
        fbH3.startAnimation(animH3);
        fbH4.startAnimation(animH4);
        fbH5.startAnimation(animH5);

        fbH1.setVisibility(View.INVISIBLE);
        fbH2.setVisibility(View.INVISIBLE);
        fbH3.setVisibility(View.INVISIBLE);
        fbH4.setVisibility(View.INVISIBLE);
        fbH5.setVisibility(View.INVISIBLE);

    }
}
