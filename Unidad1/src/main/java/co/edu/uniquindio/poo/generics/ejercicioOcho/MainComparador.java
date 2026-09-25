package co.edu.uniquindio.poo.generics.ejercicioOcho;

public class MainComparador {
    //Clase Comparador<T extends Comparable<T>>
    // Crear una clase genérica con un metodo mayor(T a, T b)
    //  que devuelva el mayor entre dos elementos comparables.
    public static void main(String[] args) {

        Comparador<Integer> comparadorNumero = new Comparador<>();
        System.out.println("Número mayor:");
        System.out.println(comparadorNumero.mayor(15, 40));

        Comparador<String> comparadorTexto = new Comparador<>();

        System.out.println("\nTexto mayor:");
        System.out.println(comparadorTexto.mayor("Java", "Python"));
    }
}