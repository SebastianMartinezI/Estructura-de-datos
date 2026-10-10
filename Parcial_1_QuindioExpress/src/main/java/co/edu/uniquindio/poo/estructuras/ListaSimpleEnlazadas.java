package co.edu.uniquindio.poo.estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListaSimpleEnlazadas <T> implements Coleccion<T>, Iterable<T> {

    private static class Nodo<T>{
        private final T valor;
        private Nodo<T> siguiente;

        private Nodo(T valor){
            this.valor=valor;
        }
    }
    private Nodo<T> primero;
    private Nodo<T> ultimo;
    private int tamanio;

    @Override
    public void agregar (T elemento){
        if(elemento==null){
            throw new IllegalArgumentException("El elmento no puede ser nulo");
        }
        Nodo<T> nuevo = new Nodo<>(elemento);

        if(primero==null){
            primero=nuevo;
            ultimo=nuevo;
        }else{
            ultimo.siguiente=nuevo;
            ultimo=nuevo;
        }
        tamanio++;
    }
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

    @Override
    public int tamanio() {
        return tamanio;
    }

    @Override
    public boolean estaVacia() {
        return tamanio == 0;
    }

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
