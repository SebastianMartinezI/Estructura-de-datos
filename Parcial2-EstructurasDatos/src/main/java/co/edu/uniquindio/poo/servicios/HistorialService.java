package co.edu.uniquindio.poo.servicios;
import co.edu.uniquindio.poo.estructuras.PilaPropia;
import java.util.Objects;

/**
 * Servicio encargado de obtener segmentos de una pila
 * sin modificar el historial original.
 */
public class HistorialService {

    /**
     * Obtiene una nueva pila con los elementos entre
     * las posiciones inicio y fin, inclusive.
     *
     * Las posiciones se cuentan desde la cima, iniciando en 1.
     * El historial original conserva su contenido y orden.
     *
     * @param original pila que contiene el historial.
     * @param inicio primera posicion del segmento.
     * @param fin ultima posicion del segmento.
     * @param <T> tipo de elementos del historial.
     * @return pila con el segmento solicitado o una pila
     *         vacia si las posiciones son invalidas.
     */
    public <T> PilaPropia<T> obtenerSegmento(
            PilaPropia<T> original,
            int inicio,
            int fin) {

        Objects.requireNonNull(
                original, "La pila original no puede ser nula");

        PilaPropia<T> resultado = new PilaPropia<>();

        // Validar las posiciones antes de modificar la pila.
        if (inicio < 1 || inicio > fin
                || fin > original.tamano()) {
            return resultado;
        }

        PilaPropia<T> respaldo = new PilaPropia<>();
        PilaPropia<T> seleccionInvertida = new PilaPropia<>();

        int posicion = 1;

        // Recorrer la pila desde la cima hasta el fondo.
        while (!original.estaVacia()) {

            T elemento = original.desapilar();

            // Conservar todos los elementos para restaurarlos.
            respaldo.apilar(elemento);

            // Guardar los elementos del segmento solicitado.
            if (posicion >= inicio && posicion <= fin) {
                seleccionInvertida.apilar(elemento);
            }

            posicion++;
        }

        // Restaurar el historial original.
        while (!respaldo.estaVacia()) {
            original.apilar(respaldo.desapilar());
        }

        // Construir la pila resultado en el orden correcto.
        while (!seleccionInvertida.estaVacia()) {
            resultado.apilar(seleccionInvertida.desapilar());
        }

        return resultado;
    }
}
