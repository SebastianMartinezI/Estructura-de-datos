package co.edu.uniquindio.poo.algoritmos;

import co.edu.uniquindio.poo.modelo.Paquete;
import java.util.List;

/**
 * Implementa algoritmos mediante la estrategia divide y vencerás
 * para el sistema de distribución QuindíoExpress.
 *
 * El problema se divide en dos subproblemas, los cuales se
 * resuelven recursivamente y posteriormente se combinan.
 *
 * @author Juan Martinez
 */
public final class DivideYVenceras {

    /**
     * Evita crear objetos de esta clase utilitaria.
     */
    private DivideYVenceras() {
    }

    /**
     * Encuentra el paquete de mayor peso mediante
     * la estrategia divide y vencerás.
     *
     * Complejidad temporal: O(n).
     * Complejidad espacial adicional: O(log n),
     * debido a la profundidad de la recursividad.
     *
     * @param paquetes lista de paquetes a analizar
     * @return paquete de mayor peso o null si la lista está vacía
     * @throws IllegalArgumentException si la lista es nula
     */
    public static Paquete encontrarMayorPeso(List<Paquete> paquetes) {

        if (paquetes == null) {
            throw new IllegalArgumentException(
                    "La lista de paquetes no puede ser nula.");
        }

        if (paquetes.isEmpty()) {
            return null;
        }

        return buscarMayorRecursivo(
                paquetes, 0, paquetes.size() - 1);
    }

    /**
     * Resuelve recursivamente el problema sobre
     * el intervalo delimitado por inicio y fin.
     *
     * Caso base: el intervalo contiene un único paquete.
     *
     * Caso recursivo: se divide el intervalo en dos
     * mitades y se combinan los resultados.
     */
    private static Paquete buscarMayorRecursivo(
            List<Paquete> paquetes, int inicio, int fin) {

        // Caso base: un solo elemento.
        if (inicio == fin) {
            return paquetes.get(inicio);
        }

        // División del problema.
        int medio = inicio + (fin - inicio) / 2;

        // Resolución del subproblema izquierdo.
        Paquete mayorIzquierdo = buscarMayorRecursivo(
                paquetes, inicio, medio);

        // Resolución del subproblema derecho.
        Paquete mayorDerecho = buscarMayorRecursivo(
                paquetes, medio + 1, fin);

        // Combinación de los resultados.
        if (mayorIzquierdo.getPeso() > mayorDerecho.getPeso()) {
            return mayorIzquierdo;
        }

        if (mayorDerecho.getPeso() > mayorIzquierdo.getPeso()) {
            return mayorDerecho;
        }

        // Si tienen el mismo peso, se elige el menor código.
        return mayorIzquierdo.getCodigo()
                .compareTo(mayorDerecho.getCodigo()) <= 0
                ? mayorIzquierdo
                : mayorDerecho;
    }
}
