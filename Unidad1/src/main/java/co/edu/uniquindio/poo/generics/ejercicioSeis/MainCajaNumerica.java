package co.edu.uniquindio.poo.generics.ejercicioSeis;

public class MainCajaNumerica {
    //Clase CajaNumerica<T extends Number>
    // Crear una clase genérica que almacene un
    // número y tenga un metodo doble() que devuelva el doble de su valor.
    public static void main(String[] args) {


        CajaNumerica<Integer> numeroEntero =
                new CajaNumerica<>(25);


        System.out.println("Doble del número entero:");
        System.out.println(numeroEntero.doble());


        CajaNumerica<Double> numeroDecimal =
                new CajaNumerica<>(7.5);


        System.out.println("\nDoble del número decimal:");
        System.out.println(numeroDecimal.doble());

    }
}