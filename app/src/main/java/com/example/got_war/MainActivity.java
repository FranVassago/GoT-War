package com.example.got_war;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private Contienda contienda;
    private Jugador jugador1;
    private Jugador jugador2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Se construyen los jugadores
        jugador1 = new Jugador(1, "Vassago", "HUMANO");
        jugador2 = new Jugador(2, "IA00001", "IA");

        //Se construyen los personajes
        Personaje J1P1 = new Personaje(
                "brienne",
                1,
                0,
                "Brienne de Tarth",
                "Tarth",
                100,
                100,
                50,
                50,
                6,
                13,
                7,
                6,
                2,
                findViewById(R.id.ivJ1P1),
                findViewById(R.id.pbJ1P1Salud),
                findViewById(R.id.pbJ1P1Energia)
                new Habilidad("ATACAR","ENEMIGO",Habilidad.INICIATIVA, true, 10, true,  "FISICO", false, 1000, 1f, null),
                new Habilidad("DEFENDER","PERSONAL", Habilidad.INSTANTANEA, true, 10, false, null, true, 1000, 1f, null),
                new Habilidad("TEMPLANZA","PERSONAL",Habilidad.INSTANTANEA, true, -10, false,  null, true, 1000, 1f, 
                   new Modificador("TEMPLANZA",2,1,0,0,5,5,0,5)),
                new Habilidad("BLOQUEADA",null,null, null, null, null,  null, null, null, 0,5f, null),
                new Habilidad("BLOQUEADA",null,null, null, null, null,  null, null, null, 0.5f, null));

        Personaje J2P1 = new Personaje(
                "sandor",
                2,
                0,
                "Sandor Clegane",
                "Clegane",
                70,
                70,
                60,
                60,
                7,
                16,
                5,
                4,
                3,
                findViewById(R.id.ivJ2P1),
                findViewById(R.id.pbJ2P1Salud),
                findViewById(R.id.pbJ2P1Energia));

        //Se asignan los personajes a los jugadores
            jugador1.reclutarPersonaje(J1P1);
            jugador2.reclutarPersonaje(J2P1);

        //Se cargan los floating action buttons
            FloatingActionButton fbH1 = findViewById(R.id.fbH1);
            fbH1.setOnClickListener(habilidadListener);
    
            FloatingActionButton fbH2 = findViewById(R.id.fbH2);
            fbH2.setOnClickListener(habilidadListener);
    
            FloatingActionButton fbH3 = findViewById(R.id.fbH3);
            fbH3.setOnClickListener(habilidadListener);
            
            FloatingActionButton fbH4 = findViewById(R.id.fbH4);
            fbH4.setOnClickListener(habilidadListener);
    
            FloatingActionButton fbH5 = findViewById(R.id.fbH5);
            fbH5.setOnClickListener(habilidadListener);


        ImageView ivJ1P2 = findViewById(R.id.ivJ2P1);
        ivJ1P2.setOnClickListener(objetivoListener);

        TextView tvFase = findViewById(R.id.tvFase);
        tvFase.setOnClickListener(contiendaListener);

        contienda = new Contienda(jugador1, jugador2, fbH1, fbH2, fbH3, fbH4, fbH5);

    }

    private View.OnClickListener habilidadListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (contienda.getFase() == "SELECCIONAR.ACCION" || contienda.getFase() == "SELECCIONAR.OBJETIVO")
                contienda.declararAccion((Habilidad) v.getTag());

        }
    };

    private View.OnClickListener objetivoListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (contienda.getFase() == "SELECCIONAR.ENEMIGO") {
                contienda.seleccionarObjetivo(jugador2, 0);
            }
        }
    };

    private View.OnClickListener contiendaListener = new View.OnClickListener() {
        public void onClick(View v) {
            TextView tvFase = (TextView) v;
            if (contienda.getFase() == null) {
                contienda.iniciarRonda();
            }
        }
    };
}