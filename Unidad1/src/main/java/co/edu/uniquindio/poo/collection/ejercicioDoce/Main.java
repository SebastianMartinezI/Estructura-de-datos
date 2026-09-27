package co.edu.uniquindio.poo.collection.ejercicioDoce;

import java.util.TreeSet;

//En una universidad, los nombres de los estudiantes deben mantenerse ordenados alfabéticamente para facilitar su búsqueda. Para ello,
//se utilizará un TreeSet, que automáticamente organizará los nombres de los estudiantes
//a medida que se agregan y permitirá obtener fácilmente el primer y el último nombre de la lista.

public class Main {
    public static void main(String[] args) {
        TreeSet<String> estudiantes = new TreeSet<>();

        estudiantes.add("Juan");
        estudiantes.add("Diego");
        estudiantes.add("Alejandro");
        estudiantes.add("Sebastian");
        estudiantes.add("Veronica");
        estudiantes.add("Diego");

        System.out.println("Estudiantes:");
        for(String nombre : estudiantes){
            System.out.println(nombre);
        }

        if(!estudiantes.isEmpty()){
            System.out.println("Primer nombre: " + estudiantes.first());
            System.out.println("Ultimo nombre: " + estudiantes.last());
        }


    }
}
