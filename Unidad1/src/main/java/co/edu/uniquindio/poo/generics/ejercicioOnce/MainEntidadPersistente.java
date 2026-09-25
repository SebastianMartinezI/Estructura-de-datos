package co.edu.uniquindio.poo.generics.ejercicioOnce;

public class MainEntidadPersistente {
    //Clase EntidadPersistente<T extends Number & Comparable<T>>
    // Crear una clase que almacene un valor T y permita
    // compararlo con otros objetos del mismo tipo.
    public static void main(String[] args) {

        EntidadPersistente<Integer> entidad1 = new EntidadPersistente<>(100);
        EntidadPersistente<Integer> entidad2 = new EntidadPersistente<>(75);

        int resultado = entidad1.compararCon(entidad2);

        if (resultado > 0) {
            System.out.println("La entidad 1 tiene un valor mayor");
        } else if (resultado < 0) {
            System.out.println("La entidad 2 tiene un valor mayor");
        } else {
            System.out.println("Los valores son iguales");
        }
    }
}