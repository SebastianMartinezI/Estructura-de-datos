package co.edu.uniquindio.poo.ejercicios;
import co.edu.uniquindio.poo.estructuras.PilaPropia;
import co.edu.uniquindio.poo.servicios.HistorialService;

/**
 * Ejecuta y verifica el ejercicio 2 del parcial.
 *
 * Comprueba la obtencion de segmentos, los casos
 * invalidos y la conservacion de la pila original.
 */
public class Ejercicio2 {

    public static void main(String[] args) {

        HistorialService servicio = new HistorialService();
        PilaPropia<String> historial = new PilaPropia<>();

        // Registrar las acciones de la mas antigua a la reciente.
        for (int i = 1; i <= 6; i++) {
            historial.apilar("A" + i);
        }

        System.out.println("HISTORIAL ORIGINAL");
        System.out.println(mostrarPila(historial));

        // Caso solicitado por el profesor.
        verificarSegmento(
                servicio, historial, 2, 4,
                "A5 -> A4 -> A3");

        // Segmento de una sola posicion.
        verificarSegmento(
                servicio, historial, 3, 3,
                "A4");

        // Obtener todos los elementos.
        verificarSegmento(
                servicio, historial, 1, 6,
                "A6 -> A5 -> A4 -> A3 -> A2 -> A1");

        // Posicion inicial invalida.
        verificarSegmento(
                servicio, historial, 0, 4,
                "(vacia)");

        // Inicio mayor que fin.
        verificarSegmento(
                servicio, historial, 4, 2,
                "(vacia)");

        // Fin mayor que el numero de elementos.
        verificarSegmento(
                servicio, historial, 2, 7,
                "(vacia)");

        // Historial vacio.
        PilaPropia<String> vacia = new PilaPropia<>();

        verificarSegmento(
                servicio, vacia, 1, 1,
                "(vacia)");

        System.out.println("\nHISTORIAL ORIGINAL AL FINAL");
        System.out.println(mostrarPila(historial));

        System.out.println(
                "\nTodas las verificaciones fueron exitosas.");
    }

    /**
     * Ejecuta una prueba y comprueba que el resultado
     * sea correcto y que la pila original no cambie.
     */
    private static void verificarSegmento(
            HistorialService servicio,
            PilaPropia<String> original,
            int inicio,
            int fin,
            String esperado) {

        String estadoAnterior = mostrarPila(original);

        PilaPropia<String> segmento =
                servicio.obtenerSegmento(original, inicio, fin);

        String obtenido = mostrarPila(segmento);
        String estadoPosterior = mostrarPila(original);

        System.out.println(
                "\nSegmento: inicio = " + inicio
                        + ", fin = " + fin);

        System.out.println("Resultado: " + obtenido);

        if (!esperado.equals(obtenido)) {
            throw new IllegalStateException(
                    "El segmento obtenido es incorrecto");
        }

        if (!estadoAnterior.equals(estadoPosterior)) {
            throw new IllegalStateException(
                    "La pila original fue modificada");
        }
    }

    /**
     * Muestra una pila desde la cima hasta el fondo
     * sin modificar sus elementos.
     */
    private static <T> String mostrarPila(PilaPropia<T> pila) {

        PilaPropia<T> auxiliar = new PilaPropia<>();
        StringBuilder contenido = new StringBuilder();

        while (!pila.estaVacia()) {

            T elemento = pila.desapilar();

            if (contenido.length() > 0) {
                contenido.append(" -> ");
            }

            contenido.append(elemento);
            auxiliar.apilar(elemento);
        }

        // Restaurar la pila.
        while (!auxiliar.estaVacia()) {
            pila.apilar(auxiliar.desapilar());
        }

        return contenido.length() == 0
                ? "(vacia)"
                : contenido.toString();
    }
}
