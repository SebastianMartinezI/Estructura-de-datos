package co.edu.uniquindio.poo.generics.ejercicioUno;

public class MainCaja {
    //Clase genérica Caja<T>
    // Implementar una clase con un atributo T contenido y métodos guardar(T valor) y obtener().
    public static void main(String[] args) {

        Caja<String> cajaTexto = new Caja<>();

        cajaTexto.guardar("Aprendiendo Generics jajaja");

        System.out.println("Contenido de la caja:");
        System.out.println(cajaTexto.obtener());


        Caja<Integer> cajaNumero = new Caja<>();

        cajaNumero.guardar(100);

        System.out.println("\nContenido de la caja numérica:");
        System.out.println(cajaNumero.obtener());
    }
}