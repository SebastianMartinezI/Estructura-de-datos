package co.edu.uniquindio.poo.generics.Enunciados.ejercicioUno;

import java.util.ArrayList;

//InventarioCaja<T> con filtro por Comparable
//Diseñar una clase genérica InventarioCaja<T extends Comparable<T>> que almacene elementos en una ArrayList<T>.
// Implementar un método que, usando únicamente un Iterator, devuelva una nueva lista con los elementos mayores que un valor dado T umbral.
//  Se debe prohibir el uso de for-each.

public class Main {
    public static void main(String[] args) {

        InventarioCaja<Integer> inventario = new InventarioCaja<>();

        inventario.agregar(4);
        inventario.agregar(6);
        inventario.agregar(7);
        inventario.agregar(9);
        inventario.agregar(10);

        ArrayList<Integer>mayores = inventario.mayoresQue(6);
        System.out.println("Mayores que 6: " + mayores);
    }

}
