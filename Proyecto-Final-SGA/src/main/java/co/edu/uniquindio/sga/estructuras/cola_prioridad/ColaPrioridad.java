package co.edu.uniquindio.sga.estructuras.cola_prioridad;

import co.edu.uniquindio.sga.excepciones.EstructuraVaciaException;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ColaPrioridad<T> implements Iterable<T> {

    private Nodo<T> frente;
    private int cantidad;
    private int modificaciones;

    private final Comparator<? super T> comparador;

    private static class Nodo<E> {
        private final E dato;
        private Nodo<E> siguiente;

        private Nodo(E dato) {
            this.dato = dato;
        }
    }

    public ColaPrioridad(Comparator<? super T> comparador) {
        this.comparador = Objects.requireNonNull(
                comparador,
                "El comparador no puede ser nulo"
        );
    }

    public void encolar(T dato) {
        Objects.requireNonNull(dato, "No se permiten datos nulos");

        Nodo<T> nuevo = new Nodo<>(dato);

        if (frente == null
                || comparador.compare(dato, frente.dato) < 0) {

            nuevo.siguiente = frente;
            frente = nuevo;

        } else {
            Nodo<T> actual = frente;

            while (actual.siguiente != null
                    && comparador.compare(
                    actual.siguiente.dato, dato
            ) <= 0) {

                actual = actual.siguiente;
            }

            nuevo.siguiente = actual.siguiente;
            actual.siguiente = nuevo;
        }

        cantidad++;
        modificaciones++;
    }

    public T desencolar() {
        validarNoVacia();

        T dato = frente.dato;
        frente = frente.siguiente;

        cantidad--;
        modificaciones++;

        return dato;
    }

    public T verFrente() {
        validarNoVacia();
        return frente.dato;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int tamano() {
        return cantidad;
    }

    private void validarNoVacia() {
        if (estaVacia()) {
            throw new EstructuraVaciaException(
                    "La cola de prioridad está vacía"
            );
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new IteradorColaPrioridad();
    }

    private class IteradorColaPrioridad implements Iterator<T> {

        private Nodo<T> actual = frente;
        private final int modificacionesEsperadas = modificaciones;

        private void comprobarModificaciones() {
            if (modificacionesEsperadas != modificaciones) {
                throw new ConcurrentModificationException(
                        "La cola cambió durante el recorrido"
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
                        "No hay más elementos en la cola"
                );
            }

            T dato = actual.dato;
            actual = actual.siguiente;

            return dato;
        }
    }
}