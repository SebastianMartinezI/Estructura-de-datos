package co.edu.uniquindio.poo.algoritmos;

import co.edu.uniquindio.poo.modelo.Paquete;

import java.util.List;

/**
 * Implementa la búsqueda binaria de paquetes por código.
 *
 * Precondición: la lista debe encontrarse ordenada
 * ascendentemente por el código de los paquetes.
 *
 * Complejidad temporal: O(log n).
 * Complejidad espacial adicional: O(1).
 *
 * @author Juan Martinez
 */
public final class BusquedaBinaria {

    /**
     * Evita crear instancias de la clase utilitaria.
     */
    private BusquedaBinaria() {
    }

    /**
     * Busca un paquete mediante su código utilizando
     * el algoritmo de búsqueda binaria.
     *
     * En cada iteración se descarta aproximadamente
     * la mitad del intervalo restante.
     *
     * @param paquetes lista ordenada por código ascendente
     * @param codigo código del paquete que se busca
     * @return paquete encontrado o null si no existe
     * @throws IllegalArgumentException si los argumentos son inválidos
     */
    public static Paquete buscarPorCodigo(
            List<Paquete> paquetes, String codigo) {

        if (paquetes == null) {
            throw new IllegalArgumentException(
                    "La lista de paquetes no puede ser nula.");
        }

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "El código no puede estar vacío.");
        }

        String codigoBuscado = codigo.trim();

        int izquierda = 0;
        int derecha = paquetes.size() - 1;

        while (izquierda <= derecha) {

            int medio = izquierda + (derecha - izquierda) / 2;

            Paquete paqueteMedio = paquetes.get(medio);

            int comparacion =
                    codigoBuscado.compareTo(paqueteMedio.getCodigo());

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
