package co.edu.uniquindio.poo.generics.NivelAvanzado.ejercicioQuince;

//Clase CalculadoraAvanzada<T extends Number & Comparable<T>>
//Implementar métodos sumar, restar, maximo y minimo para cualquier tipo numérico comparable (Integer, Double, etc.).

public class Main {
    public static void main(String[] args) {

        CalculadoraAvanzada<Double> decimales = new CalculadoraAvanzada<>();
        
        System.out.println("Suma: " + decimales.sumar(8.5,11.5));
        System.out.println("Resta: " + decimales.restar(25.3,4.0));
        System.out.println("Maximo: " + decimales.maximo(25.1,10.5));
        System.out.println("Minimo: " + decimales.minimo(10.5,8.9));

    }
}
