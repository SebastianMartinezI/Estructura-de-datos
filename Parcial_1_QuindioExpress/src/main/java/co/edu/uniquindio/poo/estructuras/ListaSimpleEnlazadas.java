package co.edu.uniquindio.poo.estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implementación genérica de una lista simplemente enlazada.
 *
 * @param <T> tipo de elementos almacenados en la lista
 */
public class ListaSimpleEnlazadas<T>
        implements Coleccion<T>, Iterable<T> {

    /**
     * Nodo interno de la lista.
     *
     * @param <T> tipo de valor almacenado
     */
    private static class Nodo<T> {

        private final T valor;
        private Nodo<T> siguiente;

        private Nodo(T valor) {
            this.valor = valor;
        }
    }

    private Nodo<T> primero;
    private Nodo<T> ultimo;
    private int tamanio;

    /**
     * Agrega un elemento al final de la lista.
     *
     * @param elemento elemento a agregar
     */
    @Override
    public void agregar(T elemento) {

        if (elemento == null) {
            throw new IllegalArgumentException(
                    "El elemento no puede ser nulo.");
        }

        Nodo<T> nuevo = new Nodo<>(elemento);

        if (primero == null) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.siguiente = nuevo;
            ultimo = nuevo;
        }

        tamanio++;
    }

    /**
     * Elimina la primera ocurrencia del elemento indicado.
     *
     * @param elemento elemento a eliminar
     * @return true si el elemento fue eliminado
     */
    @Override
    public boolean eliminar(T elemento) {

        if (elemento == null || primero == null) {
            return false;
        }

        if (primero.valor.equals(elemento)) {
            primero = primero.siguiente;

            if (primero == null) {
                ultimo = null;
            }

            tamanio--;
            return true;
        }

        Nodo<T> anterior = primero;
        Nodo<T> actual = primero.siguiente;

        while (actual != null) {

            if (actual.valor.equals(elemento)) {
                anterior.siguiente = actual.siguiente;

                if (actual == ultimo) {
                    ultimo = anterior;
                }

                tamanio--;
                return true;
            }

            anterior = actual;
            actual = actual.siguiente;
        }

        return false;
    }

    /**
     * Obtiene el elemento de una posición de la lista.
     *
     * @param posicion posición que se desea consultar
     * @return elemento almacenado en la posición indicada
     */
    @Override
    public T obtener(int posicion) {

        if (posicion < 0 || posicion >= tamanio) {
            throw new IndexOutOfBoundsException(
                    "Posición fuera de rango: " + posicion);
        }

        Nodo<T> actual = primero;

        for (int i = 0; i < posicion; i++) {
            actual = actual.siguiente;
        }

        return actual.valor;
    }

    /**
     * Retorna la cantidad de elementos almacenados.
     *
     * @return tamaño de la lista
     */
    @Override
    public int tamanio() {
        return tamanio;
    }

    /**
     * Indica si la lista no contiene elementos.
     *
     * @return true si la lista está vacía
     */
    @Override
    public boolean estaVacia() {
        return tamanio == 0;
    }

    /**
     * Retorna un iterador para recorrer los elementos
     * sin exponer los nodos internos.
     *
     * @return iterador de la lista
     */
    @Override
    public Iterator<T> iterator() {

        return new Iterator<T>() {

            private Nodo<T> actual = primero;

            @Override
            public boolean hasNext() {
                return actual != null;
            }

            @Override
            public T next() {

                if (actual == null) {
                    throw new NoSuchElementException();
                }

                T valor = actual.valor;
                actual = actual.siguiente;

                return valor;
            }
        };
    }
}