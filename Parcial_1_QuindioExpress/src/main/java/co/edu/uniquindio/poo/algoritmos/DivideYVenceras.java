package co.edu.uniquindio.poo.algoritmos;

import co.edu.uniquindio.poo.modelo.Paquete;

import java.util.List;

/**
 * Encuentra el paquete de mayor peso con divide y vencerás.
 */
public final class DivideYVenceras {

    private DivideYVenceras() {
    }

    public static Paquete encontrarMayorPeso(List<Paquete> paquetes) {

        if (paquetes == null) {
            throw new IllegalArgumentException(
                    "La lista de paquetes no puede ser nula.");
        }

        if (paquetes.isEmpty()) {
            return null;
        }

        return buscarMayorRecursivo(
                paquetes,
                0,
                paquetes.size() - 1);
    }

    private static Paquete buscarMayorRecursivo(
            List<Paquete> paquetes,
            int inicio,
            int fin) {

        // Caso base.
        if (inicio == fin) {
            return paquetes.get(inicio);
        }

        int medio = inicio + (fin - inicio) / 2;

        Paquete mayorIzquierdo = buscarMayorRecursivo(
                paquetes,
                inicio,
                medio);

        Paquete mayorDerecho = buscarMayorRecursivo(
                paquetes,
                medio + 1,
                fin);

        // Combina los resultados de ambas mitades.
        if (mayorIzquierdo.getPeso() > mayorDerecho.getPeso()) {
            return mayorIzquierdo;
        }

        if (mayorDerecho.getPeso() > mayorIzquierdo.getPeso()) {
            return mayorDerecho;
        }

        return mayorIzquierdo.getCodigo()
                .compareTo(mayorDerecho.getCodigo()) <= 0
                ? mayorIzquierdo
                : mayorDerecho;
    }
}