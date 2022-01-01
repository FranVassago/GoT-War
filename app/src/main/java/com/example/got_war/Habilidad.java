package com.example.got_war;

public class Habilidad {
    private String nombre;
    private String tipoObjetivo; // PERSONAL - ENEMIGO - TODOS_LOS_ENEMIGOS - ALIADO - TODOS_LOS_ALIADOS
    //Energia
    private Boolean modificaEnergia;
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
    
    //Tipos de objetivo
    public static final Integer PERSONAL = 1;
    public static final Integer ENEMIGO = 2;
    public static final Integer TODOS_LOS_ENEMIGOS = 3;
    public static final Integer TODOS_LOS_ALIADOS = 4;
    
    //Tipos de daño
    public static final Integer FISICO = 1;
    public static final Integer MAGICO = 2;
    
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
        this.modificaEnergia = modificaEnergia;
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
        public String getNombre() { return this.nombre; }
        public int getEnergia() { return this.energia; }
        public String getTipoObjetivo() { return this.tipoObjetivo; }
        public String getTipoDanio() { return this.tipoDanio; }
        public Modificador getModificador() { return this.modificador; }
        public long getDelay() { return this.delay; }

    //Booleans
        public Boolean causaDanio() { return this.causaDanio; }
        public Boolean modificaEnergia() { return this.modificaEnergia; }
        public Boolean activaDefensa() { return this.activaDefensa; }

}
