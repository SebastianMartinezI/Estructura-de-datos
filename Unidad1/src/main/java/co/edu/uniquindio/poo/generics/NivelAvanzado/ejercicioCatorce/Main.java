package co.edu.uniquindio.poo.generics.NivelAvanzado.ejercicioCatorce;

import java.util.LinkedList;
import java.util.List;

//14. Clase Ordenador<T extends Comparable<T>>
//Implementar un método ordenar(List<T> lista) que ordene una lista usando el método compareTo.


public class Main {
    public static void main(String[] args) {
        List<Integer> numeros = new LinkedList<>();

        numeros.add(8);
        numeros.add(7);
        numeros.add(3);
        numeros.add(4);
        numeros.add(6);

        System.out.println("Antes: " + numeros);

        Ordenar<Integer> ordenar = new Ordenar<>();
        ordenar.ordenar(numeros);

        System.out.println("Despues: " + numeros);

    }
}
