package com.example.got_war;

public class Habilidad {
    private String nombre;
    private String tipoObjetivo; // PERSONAL - ENEMIGO - TODOS_LOS_ENEMIGOS - ALIADO - TODOS_LOS_ALIADOS
    //Energia
    private Boolean consumeEnergia;
    private Boolean generaEnergia;
    private int energia;
    //Ataque
    private Boolean causaDanio;
    private String tipoDanio; // FISICO - MAGICO
    //Defensa
    private Boolean activaDefensa;
    //Modificadores
    private Modificador modificador;
    //Visual
    private long delay;

    public Habilidad(
            String nombre,
            //Energía
            Boolean modificaEnergia,
            int energia,
            //Ataque
            Boolean causaDanio,
            String tipoObjetivo,
            String tipoDanio,
            //Defensa
            Boolean activaDefensa,
            //Modificadores
            Modificador modificador,
            //Visual
            long delay) {
        this.nombre = nombre;
        //Energia
        this.consumeEnergia = modificaEnergia;
        this.energia = energia;
        //Ataque
        this.causaDanio = causaDanio;
        this.tipoObjetivo = tipoObjetivo;
        this.tipoDanio = tipoDanio;
        //Defensa
        this.activaDefensa = activaDefensa;
        //Modificadores
        this.modificador = modificador;
        //Visual
        this.delay = delay;
    }

    //Getters
        public String getNombre() {
        return nombre;
    }
        public int getEnergia() {
        return energia;
    }
        public String getTipoObjetivo() {
        return tipoObjetivo;
    }
        public String getTipoDanio() {
        return tipoDanio;
    }
        public Modificador getModificador() { return modificador; }
        public long getDelay() { return delay; }

    //Booleans
        public Boolean causaDanio() { return this.causaDanio; }
        public Boolean modificaEnergia() { return this.modificaEnergia(); }
        public Boolean activaDefensa() {
            return activaDefensa;
        }

}
