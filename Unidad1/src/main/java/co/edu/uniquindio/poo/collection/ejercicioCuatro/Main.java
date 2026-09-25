package co.edu.uniquindio.poo.collection.ejercicioCuatro;

public class Main {

    //Cree una cola (Queue) que almacene objetos de tipo "Tarea" que tengan
    // una prioridad asociada. Implemente la cola usando un PriorityQueue y
    // defina la prioridad de cada tarea según su importancia.

    public static void main(String[] args) {


        GestorTareas gestor = new GestorTareas();


        gestor.agregarTarea(new Tarea("Resolver error crítico del sistema", 1));


        gestor.agregarTarea(new Tarea("Realizar informe financiero", 2));


        gestor.agregarTarea(new Tarea("Actualizar documentación", 3));


        gestor.agregarTarea(new Tarea("Reunión con clientes", 2));


        System.out.println("COLA DE TAREAS POR PRIORIDAD");
        System.out.println("--------------------------------");


        gestor.mostrarTareas();

    }
}