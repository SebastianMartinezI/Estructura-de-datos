package co.edu.uniquindio.poo.collection.ejercicioOnce;

import java.util.LinkedHashSet;

//En una aplicación de música, los usuarios pueden marcar canciones como favoritas. Para garantizar que las canciones favoritas
//se mantengan en el orden en que fueron añadidas sin permitir duplicados,
//se empleará un LinkedHashSet, el cual conservará la secuencia de inserción y asegurará que no haya repeticiones.

public class Main {
    public static void main(String[] args) {
        LinkedHashSet<String> favoritas = new LinkedHashSet<>();

        favoritas.add("Devuelveme a mi chica");
        favoritas.add("Dejame");
        favoritas.add("Amiga mia");
        favoritas.add("Adan y eva");
        favoritas.add("Calma");
        favoritas.add("Te amo");
        favoritas.add("Dejame"); //Cancion que se repite, no se añade otra vez

        System.out.println("Canciones favoritas: ");
        for (String cancion : favoritas) {
            System.out.println(cancion);
        }

//        Aca quitamos la cancion de la lista de favoritas
        favoritas.remove("Amiga mia");

        System.out.println("\nDespues de quitar una cancion: ");
        for (String cancion : favoritas) {
            System.out.println(cancion);
        }


    }
}
