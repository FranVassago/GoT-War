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

public class Contienda {

    private Activity context;

    private Jugador jugador1;
    private Jugador jugador2;
    private String fase;
    private Integer turno;
    private Accion declaracion;
    private ArrayList<Accion> acciones;
    private ArrayList<Personaje> personajes;
    private ReproductorAnimaciones reproductorAnimaciones;
    private Long retardoAcciones;


    //Constructor
        public Contienda(Activity context, Jugador jugador1, Jugador jugador2) {
            this.context = context;
            this.jugador1 = jugador1;
            this.jugador2 = jugador2;

            this.acciones = new ArrayList<Accion>();
            this.personajes = new ArrayList<Personaje>();
            this.reproductorAnimaciones = new ReproductorAnimaciones();
            this.retardoAcciones = 0L;
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
            turno = 0;
            acciones.clear();

            establecerTurnos();
            actualizarModificadores();
            iniciarTurno(getPerSel());
        }

        public void establecerTurnos() {

            this.personajes.clear();

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
                Habilidad J2P1H1 = new Habilidad("ATACAR",true, 10, true, "ENEMIGO", "FISICO", false, null, 2000);
                declaracion.setPrioridad(getPerSel().getIniciativa());
                declaracion.setEjecutor(personaje);
                declaracion.setHabilidad(J2P1H1);
                declaracion.setVictimas(jugador1.getEquipo().get(0));
                acciones.add(declaracion);
                siguienteTurno();
            } else {
                reproductorAnimaciones.mostrarHabilidades(
                        getPerSel().getImagen(),
                        context.findViewById(R.id.fbH1),
                        context.findViewById(R.id.fbH2),
                        context.findViewById(R.id.fbH3),
                        context.findViewById(R.id.fbH4),
                        context.findViewById(R.id.fbH5),
                        retardoAcciones);
                setFase("SELECCIONAR.ACCION");
                actualizarTvFase();
            }
        }

        public void declararAccion (Habilidad habilidad) {

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
                    setFase("SELECCIONAR.ENEMIGO");
                    actualizarTvFase();
                    //iniciarModoSeleccion("ENEMIGO");
                    break;
                case "ALIADO":
                    setFase("SELECCIONAR.ALIADO");
                    actualizarTvFase();
                    //iniciarModoSeleccion("ALIADO");
                    break;
            }
        }

        public void seleccionarObjetivo(Jugador jugador, Integer posicion) {
            declaracion.setVictimas(jugador.getEquipo().get(posicion));
            acciones.add(declaracion);
            siguienteTurno();
        }

        public void siguienteTurno() {
            if (getJugador(getPerSel()).esHumano()) {
                reproductorAnimaciones.ocultarHabilidades(
                        getPerSel().getImagen(),
                        context.findViewById(R.id.fbH1),
                        context.findViewById(R.id.fbH2),
                        context.findViewById(R.id.fbH3),
                        context.findViewById(R.id.fbH4),
                        context.findViewById(R.id.fbH5),
                        0L);
            }

            turno++;
            if (turno >= personajes.size()){
                ejecutarAcciones();
                iniciarRonda();
            } else {
                iniciarTurno(getPerSel());
            }

        }

        public void ejecutarAcciones (){
            retardoAcciones = 1000L;

            //Se ordenan las acciones por orden de prioridad
            ordenarAcciones();

            //Se ejecutan las acciones
            for (Accion accion : acciones) {
                retardoAcciones = retardoAcciones + accion.ejecutar(retardoAcciones);
            }

        }

    public void ordenarAcciones() {
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
    }

    //Metodos Gestion Elementos Visuales
        public void actualizarTvFase ()
        {
            TextView tvFase = context.findViewById(R.id.tvFase);
            tvFase.setText(getPerSel().getNombre() + ": " + this.fase);
        }


}
