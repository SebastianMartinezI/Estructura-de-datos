package co.edu.uniquindio.sga.estructuras.pila;

import co.edu.uniquindio.sga.excepciones.EstructuraVaciaException;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Pila<T> implements Iterable<T> {

    private Nodo<T> cima;
    private int cantidad;
    private int modificaciones;

    private static class Nodo<E> {
        private final E dato;
        private final Nodo<E> siguiente;

        private Nodo(E dato, Nodo<E> siguiente) {
            this.dato = dato;
            this.siguiente = siguiente;
        }
    }

    public void apilar(T dato) {
        Objects.requireNonNull(dato, "No se permiten datos nulos");

        cima = new Nodo<>(dato, cima);
        cantidad++;
        modificaciones++;
    }

    public T desapilar() {
        validarNoVacia();

        T dato = cima.dato;
        cima = cima.siguiente;
        cantidad--;
        modificaciones++;

        return dato;
    }

    public T verCima() {
        validarNoVacia();
        return cima.dato;
    }

    public boolean estaVacia() {
        return cima == null;
    }

    public int tamano() {
        return cantidad;
    }

    private void validarNoVacia() {
        if (estaVacia()) {
            throw new EstructuraVaciaException("La pila está vacía");
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new IteradorPila();
    }

    private class IteradorPila implements Iterator<T> {

        private Nodo<T> actual = cima;
        private final int modificacionesEsperadas = modificaciones;

        private void comprobarModificaciones() {
            if (modificacionesEsperadas != modificaciones) {
                throw new ConcurrentModificationException(
                        "La pila cambió durante el recorrido"
                );
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
                throw new NoSuchElementException(
                        "No hay más elementos en la pila"
                );
            }

            T dato = actual.dato;
            actual = actual.siguiente;
            return dato;
        }
    }
}