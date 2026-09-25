package co.edu.uniquindio.poo.collection.ejercicioCuatro;

import java.util.PriorityQueue;

public class GestorTareas {

    private PriorityQueue<Tarea> cola;

    public GestorTareas() {
        cola = new PriorityQueue<>();
    }

    public void agregarTarea(Tarea tarea) {
        cola.add(tarea);
    }

    public void mostrarTareas() {
        while (!cola.isEmpty()) {
           System.out.println(cola.poll());
        }
    }
}
