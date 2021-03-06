package com.example.got_war;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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

        jugador1 = new Jugador(1, "Vassago", "HUMANO");
        jugador2 = new Jugador(2, "IA00001", "IA");

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
                findViewById(R.id.llJ1P1),
                findViewById(R.id.pbJ1P1Salud),
                findViewById(R.id.pbJ1P1Energia),
                findViewById(R.id.tfJ1P1));

        Personaje J2P1 = new Personaje(
                "sandor",
                2,
                0,
                "Sandor Clegane",
                "Clegane",
                100,
                100,
                60,
                60,
                7,
                15,
                5,
                4,
                3,
                findViewById(R.id.llJ2P1),
                findViewById(R.id.pbJ2P1Salud),
                findViewById(R.id.pbJ2P1Energia),
                findViewById(R.id.tfJ2P1));

        jugador1.reclutarPersonaje(J1P1);
        jugador2.reclutarPersonaje(J2P1);

        Habilidad J1P1H1 = new Habilidad("ATACAR",true, 10, true, "ENEMIGO", "FISICO", false, null, 2000);
        Habilidad J1P1H2 = new Habilidad("DEFENDER",true, 15, false, "PERSONAL", null, true, null, 1000);
        Habilidad J1P1H3 = new Habilidad("TEMPLANZA",true, -10, false, "PERSONAL", null, true, null, 1000);

        FloatingActionButton fbJ1P1H1;
        fbJ1P1H1 = findViewById(R.id.fbJ1P1H1);
        fbJ1P1H1.setTag(J1P1H1);
        fbJ1P1H1.setOnClickListener(habilidadListener);

        FloatingActionButton fbJ1P1H2;
        fbJ1P1H2 = findViewById(R.id.fbJ1P1H2);
        fbJ1P1H2.setTag(J1P1H2);
        fbJ1P1H2.setOnClickListener(habilidadListener);

        FloatingActionButton fbJ1P1H3;
        fbJ1P1H3 = findViewById(R.id.fbJ1P1H3);
        fbJ1P1H3.setTag(J1P1H3);
        fbJ1P1H3.setOnClickListener(habilidadListener);

        ImageView ivJ1P2;
        ivJ1P2 = findViewById(R.id.ivJ2P1);
        ivJ1P2.setOnClickListener(objetivoListener);

        contienda = new Contienda(this,jugador1, jugador2);

        contienda.iniciarRonda(0);

    }


    private View.OnClickListener habilidadListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (contienda.getFase() == "SELECCIONAR.ACCION" || contienda.getFase() == "SELECCIONAR.OBJETIVO")
                contienda.declararAccion((Habilidad) v.getTag());

        }
    };

    private View.OnClickListener objetivoListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (contienda.getFase() == "SELECCIONAR.OBJETIVO") {
                contienda.seleccionarObjetivo(jugador2, 0);
            }
        }
    };
}