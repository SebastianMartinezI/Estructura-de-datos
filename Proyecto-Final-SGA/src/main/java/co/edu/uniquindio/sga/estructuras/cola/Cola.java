package co.edu.uniquindio.sga.estructuras.cola;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Cola FIFO genérica implementada con nodos simplemente enlazados propios.
 *
 * <p>Las operaciones encolar, desencolar y consultarFrente son O(1).
 * No utiliza colecciones de Java para almacenar los elementos. Se prohíben
 * valores nulos para distinguirlos de la ausencia de elementos.</p>
 *
 * @param <T> tipo de elementos de la cola
 */
public final class Cola<T> implements Iterable<T> {

    private Nodo<T> frente;
    private Nodo<T> finalCola;
    private int tamano;
    private int modificaciones;

    /** Nodo interno; los enlaces nunca se exponen al exterior. */
    private static final class Nodo<E> {
        private final E valor;
        private Nodo<E> siguiente;

        private Nodo(E valor) {
            this.valor = valor;
        }
    }

    /**
     * Agrega un elemento al final, manteniendo el orden de llegada.
     *
     * @param elemento elemento que se incorpora
     * @throws NullPointerException si es nulo
     */
    public void encolar(T elemento) {
        Objects.requireNonNull(elemento, "La cola no admite elementos nulos");
        Nodo<T> nuevo = new Nodo<>(elemento);
        if (estaVacia()) {
            frente = nuevo;
        } else {
            finalCola.siguiente = nuevo;
        }
        finalCola = nuevo;
        tamano++;
        modificaciones++;
    }

    /**
     * Retira y devuelve el elemento que llegó primero.
     *
     * @return elemento retirado
     * @throws ColaVaciaException si la cola está vacía
     */
    public T desencolar() {
        validarNoVacia();
        T elemento = frente.valor;
        frente = frente.siguiente;
        tamano--;
        if (frente == null) {
            finalCola = null;
        }
        modificaciones++;
        return elemento;
    }

    /**
     * Consulta el elemento más antiguo, sin modificar la cola.
     *
     * @return primer elemento en espera
     * @throws ColaVaciaException si la cola está vacía
     */
    public T consultarFrente() {
        validarNoVacia();
        return frente.valor;
    }

    /** @return cantidad de elementos pendientes */
    public int tamano() {
        return tamano;
    }

    /** @return {@code true} si no hay elementos */
    public boolean estaVacia() {
        return tamano == 0;
    }

    /** Elimina todos los elementos. No modifica nada si ya está vacía. */
    public void vaciar() {
        if (estaVacia()) {
            return;
        }
        frente = null;
        finalCola = null;
        tamano = 0;
        modificaciones++;
    }

    /**
     * Permite recorrer la cola desde la solicitud más antigua hasta la nueva.
     * El iterador falla rápidamente ante modificaciones estructurales.
     *
     * @return iterador propio en orden FIFO
     */
    @Override
    public Iterator<T> iterator() {
        return new IteradorCola();
    }

    private void validarNoVacia() {
        if (estaVacia()) {
            throw new ColaVaciaException("No es posible operar sobre una cola vacía");
        }
    }

    private final class IteradorCola implements Iterator<T> {
        private Nodo<T> actual = frente;
        private final int modificacionesEsperadas = modificaciones;

        private void comprobarModificaciones() {
            if (modificaciones != modificacionesEsperadas) {
                throw new ConcurrentModificationException(
                        "La cola fue modificada durante el recorrido");
            }
        }

        @Override
        public boolean hasNext() {
            comprobarModificaciones();
            return actual != null;
        }

        @Override
        public T next() {
            comprobarModificaciones();
            if (actual == null) {
                throw new NoSuchElementException("No hay más elementos en la cola");
            }
            T valor = actual.valor;
            actual = actual.siguiente;
            return valor;
        }
    }
}
