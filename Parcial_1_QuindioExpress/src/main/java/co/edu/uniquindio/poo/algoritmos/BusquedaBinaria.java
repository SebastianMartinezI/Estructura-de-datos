package co.edu.uniquindio.poo.algoritmos;

import co.edu.uniquindio.poo.modelo.Paquete;

import java.util.List;

/**
 * Búsqueda binaria de paquetes por código.
 *
 * La lista debe estar ordenada por código.
 */
public final class BusquedaBinaria {

    private BusquedaBinaria() {
    }

    public static Paquete buscarPorCodigo(
            List<Paquete> paquetes,
            String codigo) {

        if (paquetes == null) {
            throw new IllegalArgumentException(
                    "La lista de paquetes no puede ser nula.");
        }

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "El código no puede estar vacío.");
        }

        String codigoBuscado = codigo.trim().toUpperCase();

        int izquierda = 0;
        int derecha = paquetes.size() - 1;

        while (izquierda <= derecha) {

            int medio = izquierda + (derecha - izquierda) / 2;

            Paquete paqueteMedio = paquetes.get(medio);

            int comparacion = codigoBuscado.compareTo(
                    paqueteMedio.getCodigo());

            if (comparacion == 0) {
                return paqueteMedio;
            }

            if (comparacion < 0) {
                derecha = medio - 1;
            } else {
                izquierda = medio + 1;
            }
        }

        return null;
    }
}