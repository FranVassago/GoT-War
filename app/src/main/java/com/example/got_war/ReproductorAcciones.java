package com.example.got_war;

import android.app.Activity;

import java.util.ArrayList;

public class ReproductorAcciones extends Thread {

    private Activity context;
    private ArrayList<Accion> acciones;
    private long delayUltimaRondaAcciones;

    //Constructor
        public ReproductorAcciones(Activity context) {
            this.context = context;
            this.acciones = new ArrayList<Accion>();
            this.delayUltimaRondaAcciones = 0;
        }

    //Getters
        public long getDelayUltimaRondaAcciones() { return delayUltimaRondaAcciones; }

    //Setters
        public void setAcciones(ArrayList<Accion> acciones) { this.acciones = acciones; }

    //Métodos específicos de la mecánica del juego
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

        @Override
        public void run() {
            context.runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        //Se ejecutan las acciones
                        try {
                            //Esperamos 1 seg antes de comenzar con las acciones
                            Thread.sleep(1000);

                            //Iniciamos la secuencia de acciones
                            long delay;
                            for (Accion accion : acciones) {

                                delay = accion.ejecutar();
                                Thread.sleep(delay);
                            }
                        } catch (InterruptedException e) { e.printStackTrace(); }
                    }
                }
            );

        }
}
