package com.example.got_war;

import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;

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
    private ImageView imagen;
    private ProgressBar barraSalud;
    private ProgressBar barraEnergia;
    private ArrayList<Modificador> modificadores;
    private Habilidad[] habilidades;

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
            ImageView imagen,
            ProgressBar barraSalud,
            ProgressBar barraEnergia,
            Habilidad H1,
            Habilidad H2,
            Habilidad H3,
            Habilidad H4,
            Habilidad H5) {
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

        //Modificadores
        this.modIniciativa = 0;
        this.modAtaque = 0;
        this.modDefensa = 0;
        this.modAgilidad = 0;
        this.modVoluntad = 0;

        this.modificadores = new ArrayList<Modificador>();
        this.habilidades = new Habilidad[] { H1, H2, H3, H4, H5 };

        actualizarBarraSalud ();
        actualizarBarraEnergia();
    }

    //Getters
        //Básicos
        public String  getNombre() { return this.nombre; }
        public Integer getJugador() { return this.jugador; }

        //Habilidades
        public Habilidad[] getHabilidades() { return this.habilidades; }
        public Habilidad getHabilidad(Integer pos) { return this.habilidades(pos); }

        //Estadísticas
        public Integer getEnergiaRestante() { return this.energiaRestante; }
        public Integer getSaludRestante() { return this.saludRestante; }

        public Integer getIniciativa() { return this.iniciativa; }
        public Integer getAtaque() { return this.ataque; }
        public Integer getDefensa() { return this.defensa; }
        public Integer getAgilidad() { return this.agilidad; }
        public Integer getVoluntad() { return this.voluntad; }

        public Integer getModIniciativa() { return this.modIniciativa; }
        public Integer getModAtaque() { return this.modAtaque; }
        public Integer getModDefensa() { return this.modDefensa; }
        public Integer getModAgilidad() { return this.modAgilidad; }
        public Integer getModVoluntad() { return this.modVoluntad; }

        public Float   getIniciativaTot() { return (float) this.iniciativa + this.modIniciativa + ((this.voluntad + this.modVoluntad) / 100); }
        public Integer getAtaqueTot() { return this.ataque + this.modAtaque; }
        public Integer getDefensaTot() { return this.defensa + this.modDefensa; }
        public Integer getAgilidadTot() { return this.agilidad + this.modAgilidad; }
        public Integer getVoluntadTot() { return this.voluntad + this.modVoluntad; }

        //Visuales
        public ImageView getImagen () { return this.imagen; }
        public ProgressBar getBarraSalud () { return this.barraSalud; }
        public ProgressBar getBarraEnergia () { return this.barraEnergia; }

    //Setters
       public void setDefendiendo(Boolean defendiendo) {
            this.estaDefendiendo = defendiendo;
        }

    //Métodos específicos de la mecánica del juego
        public void addHabilidad (Habilidad habilidad) {
            this.habilidades.add(habilidad);
        }
        
        public Integer generarAtaque (String tipoDanio) {
            Integer max;
            Integer min;

            if (tipoDanio == "FISICO") {
                max = this.ataque;
                min = Math.round(this.ataque / 2);
            } else { //"MAGICO"
                max = this.voluntad;
                min = Math.round(this.voluntad / 2);
            }
            
            return (int) ((Math.random() * (max - min)) + min);
        }

        public Boolean evitarAtaque(String tipoEvitacion) {

            Boolean evitado = false;
            Integer tirada = (int) ((Math.random() * (100 - 1)) + 1);


            if (tipoEvitacion == "AGILIDAD" && tirada <= this.agilidad)
                evitado = true;

            if (tipoEvitacion == "VOLUNTAD" && tirada <= this.voluntad)
                evitado = true;

            return evitado;
        }
        
        public void modificarEnergia(Integer energia, long delay) {
            this.energiaRestante = this.energiaRestante + energia;
            if (this.energiaRestante > this.energiaTotal)
                this.energiaRestante = this.energiaTotal;
    
            new Handler().postDelayed(() -> actualizarBarraEnergia(), delay);
        }

        public void modificarSalud(Integer salud, long delay) {
            this.saludRestante = this.saludRestante + salud;
            if (this.saludRestante > this.saludTotal)
                this.saludRestante = this.saludTotal;

            if (this.saludRestante < 0)
                this.saludRestante = 0;

            new Handler().postDelayed(() -> actualizarBarraSalud(), delay);
        }
    
        public String recibirDanio (Integer danio, Integer modDanio, String tipoDanio, Long delay) {

            String resAtaque;
            Integer danioTotal = danio + modDanio;
            
            if (this.estaDefendiendo){
                switch (tipoDanio) {
                    case "FISICO":
                        danioTotal = danio + modDanio - getDefensaTot();
                        break;
                    case "MAGICO":
                        danioTotal = danio + modDanio - getVoluntadTot();
                        break;
                }
            }
    
            if (danioTotal < 0) {
                //El excedente defendido se convierte en energía
                modificarEnergia(-danioTotal, delay);

                //Se anula el daño
                danioTotal = 0;
            }

            //Se aplica el ataque a la salud
            modificarSalud(-danioTotal, delay);

            //Se genera el texto de la tirada
            resAtaque = danioTotal.toString() + ": [" + danio.toString() + "] (+" + modDanio.toString() + ")";
            if (this.estaDefendiendo)
               resAtaque = resAtaque + "(-" + this.defensa + ") (-" + this.modDefensa + ")";

            return resAtaque;
        }
    
        public void actualizarBarraSalud () {
            barraSalud.setMax(this.saludTotal);
            barraSalud.setProgress(this.saludRestante);
        }
    
        public void actualizarBarraEnergia () {
            barraEnergia.setMax(this.energiaTotal);
            barraEnergia.setProgress(this.energiaRestante);
        }
    
        public Boolean incluirModificador (Modificador modificadorNuevo) {
            int acumulados = 0;
            boolean aplicable = false;
    
            for (Modificador modificador : this.modificadores) {
                if (modificador.getNombre() == modificadorNuevo.getNombre())
                    acumulados++;
            }
    
            if (acumulados < modificadorNuevo.getAcumulativo())
            {
                modificadores.add(modificadorNuevo);
                aplicable = true;
            }
            
            return aplicable;
        }
    
        public void actualizarModificadores () {
            for (Modificador modificador : this.modificadores) {
                this.modAtaque = 0;
                this.modDefensa = 0;
                this.modAgilidad = 0;
                this.modVoluntad = 0;
                this.modIniciativa = 0;
    
                modificador.restarRondas();
                if (modificador.getRondas() == 0) {
                    modificadores.remove(modificadores.indexOf(modificador));
                } else {
                    this.modAtaque = this.modAtaque + modificador.getModAtaque();
                    this.modDefensa = this.modDefensa + modificador.getModDefensa();
                    this.modAgilidad = this.modAgilidad + modificador.getModAgilidad();
                    this.modVoluntad = this.modVoluntad + modificador.getModVoluntad();
                    this.modIniciativa = this.modIniciativa + modificador.getModIniciativa();
                }
            }
        }
}

