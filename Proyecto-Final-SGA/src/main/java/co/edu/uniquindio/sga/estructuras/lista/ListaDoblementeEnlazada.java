package co.edu.uniquindio.sga.estructuras.lista;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Lista doblemente enlazada genérica, implementada con nodos propios.
 *
 * <p>Conserva el orden de inserción y admite valores repetidos, pero no nulos.
 * No utiliza colecciones de {@code java.util}. Ofrece recorridos desde la
 * cabeza y desde la cola mediante iteradores propios.</p>
 *
 * <p>Agregar o retirar elementos de los extremos cuesta O(1). Buscar o acceder
 * por posición cuesta O(n) en el peor caso. Los iteradores detectan cambios
 * estructurales efectuados después de su creación.</p>
 *
 * @param <T> tipo de elementos almacenados
 */
public final class ListaDoblementeEnlazada<T> implements Iterable<T> {

    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamano;
    private int modificaciones;

    /** Nodo de uso exclusivo de la estructura; no expone sus enlaces. */
    private static final class Nodo<E> {
        private final E valor;
        private Nodo<E> anterior;
        private Nodo<E> siguiente;

        private Nodo(E valor) {
            this.valor = valor;
        }
    }

    /** Agrega un elemento al inicio de la lista. */
    public void agregarPrimero(T valor) {
        Objects.requireNonNull(valor, "La lista no admite valores nulos");
        Nodo<T> nuevo = new Nodo<>(valor);
        nuevo.siguiente = cabeza;
        if (cabeza == null) {
            cola = nuevo;
        } else {
            cabeza.anterior = nuevo;
        }
        cabeza = nuevo;
        tamano++;
        modificaciones++;
    }

    /** Agrega un elemento al final de la lista. */
    public void agregarUltimo(T valor) {
        Objects.requireNonNull(valor, "La lista no admite valores nulos");
        Nodo<T> nuevo = new Nodo<>(valor);
        nuevo.anterior = cola;
        if (cola == null) {
            cabeza = nuevo;
        } else {
            cola.siguiente = nuevo;
        }
        cola = nuevo;
        tamano++;
        modificaciones++;
    }

    /**
     * Obtiene el primer elemento sin retirarlo.
     *
     * @throws ListaVaciaException si no hay elementos
     */
    public T obtenerPrimero() {
        validarNoVacia();
        return cabeza.valor;
    }

    /**
     * Obtiene el último elemento sin retirarlo.
     *
     * @throws ListaVaciaException si no hay elementos
     */
    public T obtenerUltimo() {
        validarNoVacia();
        return cola.valor;
    }

    /**
     * Obtiene el elemento en una posición, contando desde cero.
     * Se recorre desde el extremo más cercano.
     *
     * @param indice posición solicitada
     * @return elemento almacenado
     * @throws IndexOutOfBoundsException si el índice es inválido
     */
    public T obtener(int indice) {
        return nodoEn(indice).valor;
    }

    /**
     * Elimina y devuelve el primer elemento.
     *
     * @throws ListaVaciaException si no hay elementos
     */
    public T eliminarPrimero() {
        validarNoVacia();
        return desenlazar(cabeza);
    }

    /**
     * Elimina y devuelve el último elemento.
     *
     * @throws ListaVaciaException si no hay elementos
     */
    public T eliminarUltimo() {
        validarNoVacia();
        return desenlazar(cola);
    }

    /**
     * Elimina la primera aparición de un valor.
     *
     * @param valor valor no nulo que se desea eliminar
     * @return {@code true} si se eliminó alguna aparición
     */
    public boolean eliminar(T valor) {
        Objects.requireNonNull(valor, "El valor a eliminar no puede ser nulo");
        for (Nodo<T> actual = cabeza; actual != null; actual = actual.siguiente) {
            if (actual.valor.equals(valor)) {
                desenlazar(actual);
                return true;
            }
        }
        return false;
    }

    /** Indica si existe al menos una aparición del valor. */
    public boolean contiene(T valor) {
        Objects.requireNonNull(valor, "El valor a buscar no puede ser nulo");
        for (T elemento : this) {
            if (elemento.equals(valor)) {
                return true;
            }
        }
        return false;
    }

    /** Devuelve el número de elementos. */
    public int tamano() {
        return tamano;
    }

    /** Indica si la lista está vacía. */
    public boolean estaVacia() {
        return tamano == 0;
    }

    /** Vacía la lista. No altera el contador de cambios si ya estaba vacía. */
    public void vaciar() {
        if (estaVacia()) {
            return;
        }
        cabeza = null;
        cola = null;
        tamano = 0;
        modificaciones++;
    }

    /** Crea un iterador propio desde el primer elemento hasta el último. */
    @Override
    public Iterator<T> iterator() {
        return new IteradorLista(cabeza, false);
    }

    /** Crea un iterador propio desde el último elemento hasta el primero. */
    public Iterator<T> iteradorInverso() {
        return new IteradorLista(cola, true);
    }

    private void validarNoVacia() {
        if (estaVacia()) {
            throw new ListaVaciaException("No se puede operar sobre una lista vacía");
        }
    }

    private Nodo<T> nodoEn(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException(
                    "Índice " + indice + " fuera de rango para tamaño " + tamano);
        }
        if (indice < tamano / 2) {
            Nodo<T> actual = cabeza;
            for (int i = 0; i < indice; i++) {
                actual = actual.siguiente;
            }
            return actual;
        }
        Nodo<T> actual = cola;
        for (int i = tamano - 1; i > indice; i--) {
            actual = actual.anterior;
        }
        return actual;
    }

    /** Desconecta un nodo, actualizando ambos vecinos y ambos extremos. */
    private T desenlazar(Nodo<T> nodo) {
        Nodo<T> anterior = nodo.anterior;
        Nodo<T> siguiente = nodo.siguiente;

        if (anterior == null) {
            cabeza = siguiente;
        } else {
            anterior.siguiente = siguiente;
        }
        if (siguiente == null) {
            cola = anterior;
        } else {
            siguiente.anterior = anterior;
        }

        nodo.anterior = null;
        nodo.siguiente = null;
        tamano--;
        modificaciones++;
        return nodo.valor;
    }

    private final class IteradorLista implements Iterator<T> {
        private final boolean inverso;
        private final int modificacionesEsperadas;
        private Nodo<T> siguiente;

        private IteradorLista(Nodo<T> inicio, boolean inverso) {
            this.siguiente = inicio;
            this.inverso = inverso;
            this.modificacionesEsperadas = modificaciones;
        }

        private void comprobarModificaciones() {
            if (modificacionesEsperadas != modificaciones) {
                throw new ConcurrentModificationException(
                        "La lista fue modificada durante la iteración");
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
                throw new NoSuchElementException("No hay más elementos");
            }
            T valor = siguiente.valor;
            siguiente = inverso ? siguiente.anterior : siguiente.siguiente;
            return valor;
        }
    }
}
