package co.edu.uniquindio.poo.estructuras;

import java.util.NoSuchElementException;

/**
 * Implementacion generica de una pila mediante nodos propios.
 * Sigue el principio LIFO (Last In, First Out).
 *
 * @param <T> tipo de elementos almacenados.
 */
public class PilaPropia<T> {

    private static class Nodo<T> {
        private final T dato;
        private Nodo<T> siguiente;

        public Nodo(T dato, Nodo<T> siguiente) {
            this.dato = dato;
            this.siguiente = siguiente;
        }
    }

    private Nodo<T> cima;
    private int cantidad;

    /**
     * Agrega un elemento a la cima de la pila.
     */
    public void apilar(T elemento) {
        cima = new Nodo<>(elemento, cima);
        cantidad++;
    }

    /**
     * Retira y retorna el elemento de la cima.
     */
    public T desapilar() {
        if (estaVacia()) {
            throw new NoSuchElementException("La pila esta vacia");
        }

        T elemento = cima.dato;
        cima = cima.siguiente;
        cantidad--;

        return elemento;
    }

    /**
     * Consulta la cima sin retirar el elemento.
     */
    public T verTope() {
        if (estaVacia()) {
            throw new NoSuchElementException("La pila esta vacia");
        }

        return cima.dato;
    }

    /**
     * Indica si la pila no contiene elementos.
     */
    public boolean estaVacia() {
        return cima == null;
    }

    /**
     * Retorna la cantidad de elementos.
     */
    public int tamano() {
        return cantidad;
    }
}
