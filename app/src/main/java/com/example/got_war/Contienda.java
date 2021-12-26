package com.example.got_war;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
        public String getFase() { return this.fase; }

        public Jugador getJugador (Personaje personaje) {
            if (personaje.getJugador() == 1)
                return jugador1;
            else
                return jugador2;
        }

    //Setters
        public void setFase (String fase) { this.fase = fase; }


    //Metodos de Gestión del Combate
        public Personaje pSeleccionado () {
            return this.personajes.get(turno);
        }

        public void iniciarRonda () {
            establecerTurnos();
            //actualizarModificadores();
            iniciarTurno(pSeleccionado());
        }

        public void establecerTurnos() {

            personajes.clear();

            //Carga los personajes de los jugadores en la lista de turnos
            for (Personaje personaje : jugador1.getEquipo()) { personajes.add(personaje); }
            for (Personaje personaje : jugador2.getEquipo()) { personajes.add(personaje); }

            //Se ordenan los personajes por orden de iniciativa / voluntad
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

        public void iniciarTurno (Personaje personaje) {

            declaracion = new Accion();

            if (getJugador(personaje).esIA())
            {
                declaracion.setPrioridad(pSeleccionado().getIniciativa());
                declaracion.setEjecutor(personaje);
                declaracion.setHabilidad("ATACAR");
                declaracion.setVictimas(jugador1.getEquipo().get(0));
                acciones.add(declaracion);
                siguienteTurno();
            } else {
                //mostrar floatingButtons
                setFase("SELECCIONAR.ACCION");
                actualizarTvFase();
            }
        }

        public void declararAccion (String habilidad) {

            setFase("DECLARACION");

            declaracion.setEjecutor(pSeleccionado());
            declaracion.setHabilidad(habilidad);

            if (habilidad == "DEFENDER") {
               declaracion.setPrioridad((float) 100 + pSeleccionado().getVoluntad() / 100);
            } else {
               declaracion.setPrioridad(pSeleccionado().getIniciativa());
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

            turno++;
            if (turno >= personajes.size()){
                ejecutarAcciones();
                establecerTurnos();
                acciones.clear();
                turno = 0;
            }

            iniciarTurno(pSeleccionado());
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
            tvFase.setText(pSeleccionado().getNombre() + ": " + this.fase);
        }
}
