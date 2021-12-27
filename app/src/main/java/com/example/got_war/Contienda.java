package com.example.got_war;

import android.app.Activity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Contienda {

    private Activity context;

    private Jugador jugador1;
    private Jugador jugador2;
    private String fase;
    private Integer turno;
    private Accion declaracion;
    private ArrayList<Accion> acciones;
    private ArrayList<Personaje> personajes;


    //Constructor
        public Contienda(Activity context, Jugador jugador1, Jugador jugador2, String fase) {
            this.context = context;
            this.jugador1 = jugador1;
            this.jugador2 = jugador2;
            this.fase = fase;
            this.turno = 0;

            this.acciones = new ArrayList<Accion>();
            this.personajes = new ArrayList<Personaje>();
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
        public void iniciarRonda () {
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

        public void iniciarTurno (Personaje personaje) {

            declaracion = new Accion();

            if (getJugador(personaje).esIA())
            {
                declaracion.setPrioridad(getPerSel().getIniciativa());
                declaracion.setEjecutor(personaje);
                declaracion.setHabilidad("ATACAR");
                declaracion.setVictimas(jugador1.getEquipo().get(0));
                acciones.add(declaracion);
                siguienteTurno();
            } else {
                mostrarHabilidades(getPerSel(),3000);
                setFase("SELECCIONAR.ACCION");
                actualizarTvFase();
            }
        }

        public void declararAccion (String habilidad) {

            setFase("DECLARACION");

            declaracion.setEjecutor(getPerSel());
            declaracion.setHabilidad(habilidad);

            if (habilidad == "DEFENDER") {
               declaracion.setPrioridad((float) 100 + getPerSel().getVoluntad() / 100);
            } else {
               declaracion.setPrioridad(getPerSel().getIniciativa());
            }

            if (habilidad == "ATACAR") {
               setFase("SELECCIONAR.OBJETIVO");
               //iniciarModoSeleccion();
            } else {
               acciones.add(declaracion);
               siguienteTurno();
            }
        }

        public void seleccionarObjetivo(Jugador jugador, Integer posicion) {
            declaracion.setVictimas(jugador.getEquipo().get(posicion));
            acciones.add(declaracion);
            siguienteTurno();
        }

        public void siguienteTurno() {
            //ocultarHabilidades(getPerSel());

            turno++;
            if (turno >= personajes.size()){
                ejecutarAcciones();
                establecerTurnos();
                acciones.clear();
                turno = 0;
            }

            iniciarTurno(getPerSel());
        }

        public void ejecutarAcciones (){

            //Se ordenan las acciones por orden de prioridad
            Boolean reordenado = true;
            Accion accionAux = null;

            while (reordenado) {
                reordenado = false;
                for (int i = 0; i < acciones.size(); i++) {
                    if (i > 0) {
                        if (acciones.get(i - 1).getPrioridad() > acciones.get(i).getPrioridad()) {
                            accionAux = acciones.get(i - 1);
                            acciones.set(i - 1, acciones.get(i));
                            acciones.set(i, accionAux);
                            reordenado = true;
                        }
                    }
                }
            }

            //Se ejecutan las acciones
            long delay = 0;

            for (Accion accion : acciones) {
               switch (accion.getHabilidad()) {
                  case "ATACAR":
                     for (Personaje victima : accion.getVictimas()) {
                        accion.getEjecutor().mostrarAnimacion("ATACAR", victima, delay);
                        victima.recibirDanio(accion.getEjecutor().getAtaque(), "FISICO");
                     }
                     break;

                  case "DEFENDER":
                     accion.getEjecutor().setDefendiendo(true);
                     break;
               }
               delay = delay + 3000;
            }
        }

    //Metodos Gestion Elementos Visuales
        public void actualizarTvFase ()
        {
            TextView tvFase = context.findViewById(R.id.tvFase);
            tvFase.setText(getPerSel().getNombre() + ": " + this.fase);
        }

        public void mostrarHabilidades (Personaje personaje, long delay) {

            FloatingActionButton fbH1, fbH2, fbH3, fbH4, fbH5;

            Animation animH1 = AnimationUtils.loadAnimation(context, R.anim.mostrarhabilidad);
            Animation animH2 = AnimationUtils.loadAnimation(context, R.anim.mostrarhabilidad);
            Animation animH3 = AnimationUtils.loadAnimation(context, R.anim.mostrarhabilidad);
            Animation animH4 = AnimationUtils.loadAnimation(context, R.anim.mostrarhabilidad);
            Animation animH5 = AnimationUtils.loadAnimation(context, R.anim.mostrarhabilidad);

            fbH1 = context.findViewById(R.id.fbJ1P1H1);
            fbH2 = context.findViewById(R.id.fbJ1P1H2);
            fbH3 = context.findViewById(R.id.fbJ1P1H3);
            fbH4 = context.findViewById(R.id.fbJ1P1H4);
            fbH5 = context.findViewById(R.id.fbJ1P1H5);

            animH1.setStartOffset(delay + 0);
            animH2.setStartOffset(delay + 200);
            animH3.setStartOffset(delay + 200);
            animH4.setStartOffset(delay + 400);
            animH5.setStartOffset(delay + 400);

            fbH1.startAnimation(animH1);
            fbH2.startAnimation(animH2);
            fbH3.startAnimation(animH3);
            fbH4.startAnimation(animH4);
            fbH5.startAnimation(animH5);

        }

    public void ocultarHabilidades (Personaje personaje) {

        FloatingActionButton fbH1, fbH2, fbH3, fbH4, fbH5;

        Animation animH1 = AnimationUtils.loadAnimation(context, R.anim.ocultarhabilidad);
        Animation animH2 = AnimationUtils.loadAnimation(context, R.anim.ocultarhabilidad);
        Animation animH3 = AnimationUtils.loadAnimation(context, R.anim.ocultarhabilidad);
        Animation animH4 = AnimationUtils.loadAnimation(context, R.anim.ocultarhabilidad);
        Animation animH5 = AnimationUtils.loadAnimation(context, R.anim.ocultarhabilidad);

        fbH1 = context.findViewById(R.id.fbJ1P1H1);
        fbH2 = context.findViewById(R.id.fbJ1P1H2);
        fbH3 = context.findViewById(R.id.fbJ1P1H3);
        fbH4 = context.findViewById(R.id.fbJ1P1H4);
        fbH5 = context.findViewById(R.id.fbJ1P1H5);

        animH1.setStartOffset(0);
        animH2.setStartOffset(200);
        animH3.setStartOffset(200);
        animH4.setStartOffset(400);
        animH5.setStartOffset(400);

        fbH1.startAnimation(animH1);
        fbH2.startAnimation(animH2);
        fbH3.startAnimation(animH3);
        fbH4.startAnimation(animH4);
        fbH5.startAnimation(animH5);

    }
}
