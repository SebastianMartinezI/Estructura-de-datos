package co.edu.uniquindio.poo.ejercicios;

import co.edu.uniquindio.poo.estructuras.ColaPropia;
import co.edu.uniquindio.poo.modelos.Paquete;
import co.edu.uniquindio.poo.servicios.PaqueteServico;

public class Ejercicio3 {
    public static void main(String[] args) {
        ColaPropia<Paquete> pendientes = new ColaPropia<>();

        pendientes.encolar(new Paquete("P1", Paquete.TipoEntrega.NORMAL,4));
        pendientes.encolar(new Paquete("P2", Paquete.TipoEntrega.URGENTE,7));
        pendientes.encolar(new Paquete("P3", Paquete.TipoEntrega.NORMAL,3));
        pendientes.encolar(new Paquete("P4", Paquete.TipoEntrega.URGENTE, 5));
        pendientes.encolar(new Paquete("P5", Paquete.TipoEntrega.NORMAL,6));
        pendientes.encolar(new Paquete("P6", Paquete.TipoEntrega.URGENTE,4));
        pendientes.encolar(new Paquete("P7", Paquete.TipoEntrega.NORMAL,2));


        PaqueteServico servicio = new PaqueteServico();
        int capacidad = 14;

        System.out.println("***** SELECCION DE PAQUETES *****");
        System.out.println("Capacidad disponible: " + capacidad + "kg");
        System.out.println();

        ColaPropia<Paquete> seleccionados = servicio.SeleccionarPaquetes(pendientes, capacidad);

        System.out.println("\n***** PAQUETES SELECCIONADOS *****");
        mostrarCola(seleccionados, seleccionados.tamano());

        System.out.println("\n***** PAQUETES PENDIENTES *****");
        mostrarCola(pendientes, pendientes.tamano());
    }
    private static void mostrarCola(ColaPropia<Paquete> cola, int porMostrar){
        if(porMostrar == 0){
            return;
        }
        Paquete paquete = cola.desencolar();
        System.out.println(paquete);
        cola.encolar(paquete);
        mostrarCola(cola, porMostrar-1);
    }
}
