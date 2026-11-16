package co.edu.uniquindio.sga.estructuras.conjunto;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Conjunto genérico sin elementos repetidos, implementado mediante una tabla
 * de dispersión y nodos enlazados propios.
 *
 * <p>La estructura compara valores usando {@link Object#equals(Object)} y
 * {@link Object#hashCode()}. No utiliza {@code HashSet}, {@code ArrayList}
 * ni otras colecciones de Java para almacenar elementos. No acepta nulos.</p>
 *
 * <p>Las operaciones de alta, búsqueda y eliminación cuestan O(1) en promedio
 * si los valores tienen una función {@code hashCode()} adecuada; en el peor
 * caso, ante colisiones, pueden costar O(n). El orden de recorrido no está
 * definido. Los iteradores detectan modificaciones estructurales.</p>
 *
 * @param <T> tipo de elementos del conjunto
 */
public final class Conjunto<T> implements Iterable<T> {

    private static final int CAPACIDAD_INICIAL = 16;

    private Nodo<T>[] cubetas = crearCubetas(CAPACIDAD_INICIAL);
    private int tamano;
    private int modificaciones;

    /** Nodo interno; sus enlaces no se exponen fuera de la estructura. */
    private static final class Nodo<E> {
        private final E valor;
        private Nodo<E> siguiente;

        private Nodo(E valor, Nodo<E> siguiente) {
            this.valor = valor;
            this.siguiente = siguiente;
        }
    }

    /**
     * Inserta el valor si aún no existe uno equivalente en el conjunto.
     *
     * @param valor elemento no nulo que se desea agregar
     * @return {@code true} si se agregó; {@code false} si ya existía
     * @throws NullPointerException si el valor es nulo
     */
    public boolean agregar(T valor) {
        Objects.requireNonNull(valor, "El conjunto no admite valores nulos");
        if (contiene(valor)) {
            return false;
        }
        // Mantener factor de carga <= 0,75 mediante redimensionamiento.
        if (tamano + 1 > (cubetas.length * 3) / 4) {
            redimensionar();
        }
        int indice = indiceDe(valor, cubetas.length);
        cubetas[indice] = new Nodo<>(valor, cubetas[indice]);
        tamano++;
        modificaciones++;
        return true;
    }

    /**
     * Comprueba si existe un elemento equivalente según {@code equals}.
     *
     * @param valor elemento no nulo que se desea buscar
     * @return {@code true} si existe
     * @throws NullPointerException si el valor es nulo
     */
    public boolean contiene(T valor) {
        Objects.requireNonNull(valor, "El valor buscado no puede ser nulo");
        int indice = indiceDe(valor, cubetas.length);
        for (Nodo<T> actual = cubetas[indice]; actual != null; actual = actual.siguiente) {
            if (actual.valor.equals(valor)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Elimina un elemento equivalente, si existe.
     *
     * @param valor elemento no nulo que se desea eliminar
     * @return {@code true} si se eliminó; {@code false} si no estaba presente
     * @throws NullPointerException si el valor es nulo
     */
    public boolean eliminar(T valor) {
        Objects.requireNonNull(valor, "El valor a eliminar no puede ser nulo");
        int indice = indiceDe(valor, cubetas.length);
        Nodo<T> anterior = null;
        Nodo<T> actual = cubetas[indice];
        while (actual != null) {
            if (actual.valor.equals(valor)) {
                if (anterior == null) {
                    cubetas[indice] = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }
                tamano--;
                modificaciones++;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return false;
    }

    /** @return cantidad de elementos distintos en el conjunto */
    public int tamano() {
        return tamano;
    }

    /** @return {@code true} si el conjunto no contiene elementos */
    public boolean estaVacio() {
        return tamano == 0;
    }

    /**
     * Retira todos los valores y restablece la capacidad inicial.
     * Si ya está vacío, no registra una modificación estructural.
     */
    public void vaciar() {
        if (estaVacio()) {
            return;
        }
        cubetas = crearCubetas(CAPACIDAD_INICIAL);
        tamano = 0;
        modificaciones++;
    }

    /**
     * Permite recorrer los elementos sin garantizar un orden específico.
     *
     * @return iterador propio que detecta modificaciones estructurales
     */
    @Override
    public Iterator<T> iterator() {
        return new IteradorConjunto();
    }

    private static int indiceDe(Object valor, int capacidad) {
        int hash = valor.hashCode();
        hash ^= hash >>> 16;
        return hash & (capacidad - 1); // Capacidad siempre potencia de dos.
    }

    @SuppressWarnings("unchecked")
    private static <E> Nodo<E>[] crearCubetas(int capacidad) {
        return (Nodo<E>[]) new Nodo<?>[capacidad];
    }

    /** Redistribuye los nodos existentes sin crear colecciones auxiliares. */
    private void redimensionar() {
        Nodo<T>[] nuevas = crearCubetas(cubetas.length * 2);
        for (Nodo<T> primero : cubetas) {
            Nodo<T> actual = primero;
            while (actual != null) {
                Nodo<T> siguiente = actual.siguiente;
                int indice = indiceDe(actual.valor, nuevas.length);
                actual.siguiente = nuevas[indice];
                nuevas[indice] = actual;
                actual = siguiente;
            }
        }
        cubetas = nuevas;
    }

    private final class IteradorConjunto implements Iterator<T> {
        private final int modificacionesEsperadas = modificaciones;
        private int indiceCubeta = -1;
        private Nodo<T> siguiente;

        private IteradorConjunto() {
            avanzarACubetaSiguiente();
        }

        private void comprobarModificaciones() {
            if (modificacionesEsperadas != modificaciones) {
                throw new ConcurrentModificationException(
                        "El conjunto fue modificado durante el recorrido");
            }
        }

        private void avanzarACubetaSiguiente() {
            siguiente = null;
            while (++indiceCubeta < cubetas.length) {
                if (cubetas[indiceCubeta] != null) {
                    siguiente = cubetas[indiceCubeta];
                    return;
                }
            }
        }

        @Override
        public boolean hasNext() {
            comprobarModificaciones();
            return siguiente != null;
        }

        @Override
        public T next() {
            comprobarModificaciones();
            if (siguiente == null) {
                throw new NoSuchElementException("No hay más elementos en el conjunto");
            }
            T valor = siguiente.valor;
            if (siguiente.siguiente != null) {
                siguiente = siguiente.siguiente;
            } else {
                avanzarACubetaSiguiente();
            }
            return valor;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException(
                    "Use eliminar(valor) para retirar elementos del conjunto");
        }
    }
}
