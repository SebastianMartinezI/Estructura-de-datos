package co.edu.uniquindio.poo.collection.ejercicioQuince;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        HashMap<String, String> directorio = new HashMap<>();

        directorio.put("Ana", "3205711220");
        directorio.put("Jhan", "3001417585");
        directorio.put("Ricardo", "3182507448");
        directorio.put("Victoria", "3062507414");

        String buscarNombre = "Ricardo";

        if (directorio.containsKey(buscarNombre)){
            System.out.println("El teleono de: " + buscarNombre + " es: " + directorio.get(buscarNombre));
        }else{

            System.out.println(buscarNombre + " no esta en el directorio");
        }
    }
}
