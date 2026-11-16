package co.edu.uniquindio.sga.estructuras.circular;

import co.edu.uniquindio.sga.excepciones.EstructuraVaciaException;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ListaCircular<T> implements Iterable<T> {

    // El siguiente del último es el elemento actual.
    private Nodo<T> ultimo;

    private int cantidad;
    private int modificaciones;

    private static class Nodo<E> {
        private final E dato;
        private Nodo<E> siguiente;

        private Nodo(E dato) {
            this.dato = dato;
        }
    }

    public void agregar(T dato) {
        Objects.requireNonNull(dato, "No se permiten datos nulos");

        Nodo<T> nuevo = new Nodo<>(dato);

        if (estaVacia()) {
            nuevo.siguiente = nuevo;
        } else {
            nuevo.siguiente = ultimo.siguiente;
            ultimo.siguiente = nuevo;
        }

        ultimo = nuevo;

        cantidad++;
        modificaciones++;
    }

    public T verActual() {
        validarNoVacia();
        return ultimo.siguiente.dato;
    }

    public T avanzar() {
        validarNoVacia();

        ultimo = ultimo.siguiente;
        modificaciones++;

        return verActual();
    }

    public boolean contiene(T dato) {
        if (estaVacia()) {
            return false;
        }

        Nodo<T> actual = ultimo.siguiente;

        for (int i = 0; i < cantidad; i++) {
            if (Objects.equals(actual.dato, dato)) {
                return true;
            }

            actual = actual.siguiente;
        }

        return false;
    }

    public boolean eliminar(T dato) {
        if (estaVacia()) {
            return false;
        }

        Nodo<T> anterior = ultimo;
        Nodo<T> actual = ultimo.siguiente;

        for (int i = 0; i < cantidad; i++) {
            if (Objects.equals(actual.dato, dato)) {

                if (cantidad == 1) {
                    ultimo = null;
                } else {
                    anterior.siguiente = actual.siguiente;

                    if (actual == ultimo) {
                        ultimo = anterior;
                    }
                }

                cantidad--;
                modificaciones++;

                return true;
            }

            anterior = actual;
            actual = actual.siguiente;
        }

        return false;
    }

    public boolean estaVacia() {
        return ultimo == null;
    }

    public int tamano() {
        return cantidad;
    }

    private void validarNoVacia() {
        if (estaVacia()) {
            throw new EstructuraVaciaException(
                    "La lista circular está vacía"
            );
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new IteradorCircular();
    }

    private class IteradorCircular implements Iterator<T> {

        private Nodo<T> actual =
                ultimo == null ? null : ultimo.siguiente;

        private int pendientes = cantidad;

        private final int modificacionesEsperadas = modificaciones;

        private void comprobarModificaciones() {
            if (modificacionesEsperadas != modificaciones) {
                throw new ConcurrentModificationException(
                        "La lista circular cambió durante el recorrido"
                );
            }
        }

        @Override
        public boolean hasNext() {
            comprobarModificaciones();
            return pendientes > 0;
        }

        @Override
        public T next() {
            comprobarModificaciones();

            if (pendientes == 0) {
                throw new NoSuchElementException(
                        "No hay más elementos en esta vuelta"
                );
            }

            T dato = actual.dato;

            actual = actual.siguiente;
            pendientes--;

            return dato;
        }
    }
}