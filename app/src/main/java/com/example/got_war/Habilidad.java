package com.example.got_war;

public class Habilidad {
    //Básicos
    private String nombre;
    private String tipoObjetivo; // PERSONAL - ENEMIGO - TODOS_LOS_ENEMIGOS - ALIADO - TODOS_LOS_ALIADOS
    private Integer tipoPrioridad; // INSTANTANEA - INICIATIVA
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
    private Float alpha;

    //Constantes
        //Tipos de objetivo
        public static final Integer PERSONAL = 1;
        public static final Integer ENEMIGO = 2;
        public static final Integer TODOS_LOS_ENEMIGOS = 3;
        public static final Integer TODOS_LOS_ALIADOS = 4;
    
        //Tipo de Prioridad
        public static final Integer INSTANTANEA = 1;
        public static final Integer INICIATIVA = 2;
    
        //Tipos de daño
        public static final Integer FISICO = 1;
        public static final Integer MAGICO = 2;
    
    public Habilidad(
            //Básicos
            String nombre,
            String tipoObjetivo,
            Integer tipoPrioridad,
            //Energía
            Boolean modificaEnergia,
            Integer energia,
            //Ataque
            Boolean causaDanio,
            String tipoDanio,
            //Defensa
            Boolean activaDefensa,
            //Modificadores
            Modificador modificador,
            //Visual
            long delay,
            Float alpha) {
        this.nombre = nombre;
        this.tipoObjetivo = tipoObjetivo;
        this.tipoPrioridad = tipoPrioridad;
        //Energia
        this.modificaEnergia = modificaEnergia;
        this.energia = energia;
        //Ataque
        this.causaDanio = causaDanio;
        this.tipoDanio = tipoDanio;
        //Defensa
        this.activaDefensa = activaDefensa;
        //Modificadores
        this.modificador = modificador;
        //Visual
        this.delay = delay;
        this.alpha = alpha;
    }

    //Getters
        public String getNombre() { return this.nombre; }
        public int getEnergia() { return this.energia; }
        public String getTipoObjetivo() { return this.tipoObjetivo; }
        public String getTipoDanio() { return this.tipoDanio; }
        public Modificador getModificador() { return this.modificador; }
        public long getDelay() { return this.delay; }
        public Float getPrioridad(Float iniciativa) {
            if (this.tipoPrioridad == INSTANTANEA)
                return 9999f;
            else
                return iniciativa;
        }

    //Booleans
        public Boolean causaDanio() { return this.causaDanio; }
        public Boolean modificaEnergia() { return this.modificaEnergia; }
        public Boolean activaDefensa() { return this.activaDefensa; }

}
