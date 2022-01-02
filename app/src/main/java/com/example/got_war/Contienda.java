package com.example.got_war;

import android.app.Activity;
import android.widget.TextView;

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
    private Animacion animacion;
    private Long delayAcciones;

    /* NOTAS

    */

    //Constructor
        public Contienda(Activity context, Jugador jugador1, Jugador jugador2) {
            this.context = context;
            this.jugador1 = jugador1;
            this.jugador2 = jugador2;

            this.acciones = new ArrayList<Accion>();
            this.personajes = new ArrayList<Personaje>();
            this.animacion = new Animacion(context);
            this.delayAcciones = 0L;
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

            faseMantenimiento();
            establecerTurnos();
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
                        if (personajes.get(i - 1).getIniciativaTot() > personajes.get(i).getIniciativaTot()) {
                            personajeAux = personajes.get(i - 1);
                            personajes.set(i - 1, personajes.get(i));
                            personajes.set(i, personajeAux);
                            reordenado = true;
                        }
                }
            }
        }

        public void faseMantenimiento() {
            for (Personaje personaje : personajes) {
                personaje.setDefendiendo(false);
                personaje.actualizarModificadores();
            }
        }

        public void iniciarTurno (Personaje personaje) {

            declaracion = new Accion();

            if (getJugador(personaje).esIA())
            {
                Habilidad J2P1H1 = new Habilidad("ATACAR","ENEMIGO",Habilidad.INICIATIVA, true, 10, true, "FISICO", false, null, 2000);
                declaracion.setPrioridad(getPerSel().getIniciativa());
                declaracion.setEjecutor(personaje);
                declaracion.setHabilidad(J2P1H1);
                declaracion.setObjetivos(jugador1.getEquipo().get(0));
                acciones.add(declaracion);
                siguienteTurno();
            } else {
                animacion.mostrarHabilidades(
                        getPerSel().getImagen(),
                        context.findViewById(R.id.fbH1),
                        context.findViewById(R.id.fbH2),
                        context.findViewById(R.id.fbH3),
                        context.findViewById(R.id.fbH4),
                        context.findViewById(R.id.fbH5),
                        delayAcciones);
                setFase("SELECCIONAR.ACCION");
                actualizarTvFase();
            }
        }

        public void declararAccion (Habilidad habilidad) {

            setFase("DECLARACION");

            declaracion.setPrioridad(habilidad.getPrioridad(getPerSel().getIniciativaTot()));
            declaracion.setEjecutor(getPerSel());
            declaracion.setHabilidad(habilidad);

            switch (habilidad.getTipoObjetivo()) {
                case "PERSONAL":
                    declaracion.setObjetivos(getPerSel());
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
            declaracion.setObjetivos(jugador.getEquipo().get(posicion));
            acciones.add(declaracion);
            siguienteTurno();
        }

        public void siguienteTurno() {
            if (getJugador(getPerSel()).esHumano()) {
                animacion.ocultarHabilidades(
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

            //Se ordenan las acciones por orden de prioridad
            ordenarAcciones();

            //Se ejecutan las acciones
            delayAcciones = 500L;
            for (Accion accion : acciones) {
                delayAcciones = delayAcciones + accion.ejecutar(delayAcciones, animacion);
            }
            delayAcciones = delayAcciones + 1000L;

        }

        public void ordenarAcciones() {
            Boolean reordenado = true;
            Accion accionAux = null;

            while (reordenado) {
                reordenado = false;
                for (int i = 0; i < acciones.size(); i++) {
                    if (i > 0) {
                        if (acciones.get(i - 1).getPrioridad() < acciones.get(i).getPrioridad()) {
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
