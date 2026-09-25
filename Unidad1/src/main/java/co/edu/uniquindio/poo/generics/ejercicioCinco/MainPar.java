package co.edu.uniquindio.poo.generics.ejercicioCinco;

public class MainPar {
    //Clase Par<T>
    // Implementar una clase que guarde dos valores de tipo T
    // y un metodo para verificar si ambos son iguales.
    public static void main(String[] args) {

        Par<Integer> parNumeros = new Par<>(20, 20);

        System.out.println("¿Los números son iguales?");
        System.out.println(parNumeros.sonIguales());

        Par<String> parTexto = new Par<>("Java", "Generics");

        System.out.println("\n¿Los textos son iguales?");
        System.out.println(parTexto.sonIguales());

    }
}