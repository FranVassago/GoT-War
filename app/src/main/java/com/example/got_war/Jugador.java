package com.example.got_war;

import java.util.ArrayList;

public class Jugador {
    private Integer numero;
    private String nombre;
    private String tipo; //HUMANO o IA
    private ArrayList<Personaje> equipo;

    public Jugador(Integer numero, String nombre, String tipo) {
        this.numero = numero;
        this.nombre = nombre;
        this.tipo = tipo;
        this.equipo = new ArrayList<Personaje>();
    }

    public void reclutarPersonaje (Personaje personaje) {
        equipo.add(personaje);
    }

    public boolean esHumano () {
        if (tipo == "HUMANO") return true; else return false;
    }

    public boolean esIA () {
        if (tipo == "IA") return true; else return false;
    }

    public ArrayList<Personaje> getEquipo() {
        return equipo;
    }

}
