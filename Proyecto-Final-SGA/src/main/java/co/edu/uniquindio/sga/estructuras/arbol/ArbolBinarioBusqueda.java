
package co.edu.uniquindio.sga.estructuras.arbol;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Árbol binario de búsqueda genérico, no balanceado.
 *
 * <p>El orden se define por Comparable. Dos elementos cuyo
 * compareTo devuelve cero representan la misma clave y
 * no se insertan duplicados. No se permiten elementos nulos.</p>
 *
 * <p>No utiliza colecciones de java.util para almacenar
 * elementos. La inserción, búsqueda, eliminación, altura
 * y recorridos son recursivos. El iterador en orden usa
 * enlaces propios como pila.</p>
 *
 * @param <T> tipo de elementos con orden natural
 */
public final class ArbolBinarioBusqueda<T extends Comparable<T>>
        implements Iterable<T> {

    private Nodo<T> raiz;
    private int tamano;
    private int modificaciones;

    private static final class Nodo<E> {
        private E valor;
        private Nodo<E> izquierdo;
        private Nodo<E> derecho;

        private Nodo(E valor) {
            this.valor = valor;
        }
    }

    /**
     * Inserta un elemento.
     *
     * @return false si su clave ya existe
     */
    public boolean insertar(T valor) {
        Objects.requireNonNull(
                valor, "No se puede insertar un valor nulo");

        int tamanoAnterior = tamano;
        raiz = insertarRecursivo(raiz, valor);

        if (tamano != tamanoAnterior) {
            modificaciones++;
            return true;
        }
        return false;
    }

    private Nodo<T> insertarRecursivo(
            Nodo<T> actual, T valor) {

        if (actual == null) {
            tamano++;
            return new Nodo<>(valor);
        }

        int comparacion = valor.compareTo(actual.valor);

        if (comparacion < 0) {
            actual.izquierdo =
                    insertarRecursivo(actual.izquierdo, valor);
        } else if (comparacion > 0) {
            actual.derecho =
                    insertarRecursivo(actual.derecho, valor);
        }

        return actual;
    }

    /**
     * Retorna el elemento almacenado con la clave buscada,
     * o null cuando no existe.
     */
    public T buscar(T clave) {
        Objects.requireNonNull(
                clave, "La clave de búsqueda no puede ser nula");

        Nodo<T> encontrado = buscarRecursivo(raiz, clave);
        return encontrado == null ? null : encontrado.valor;
    }

    private Nodo<T> buscarRecursivo(
            Nodo<T> actual, T clave) {

        if (actual == null) {
            return null;
        }

        int comparacion = clave.compareTo(actual.valor);

        if (comparacion == 0) {
            return actual;
        }

        return comparacion < 0
                ? buscarRecursivo(actual.izquierdo, clave)
                : buscarRecursivo(actual.derecho, clave);
    }

    public boolean contiene(T clave) {
        return buscar(clave) != null;
    }

    /**
     * Elimina una clave y retorna true si existía.
     *
     * Cuando un nodo tiene dos hijos, se sustituye
     * por su sucesor en orden.
     */
    public boolean eliminar(T clave) {
        Objects.requireNonNull(
                clave, "La clave a eliminar no puede ser nula");

        if (!contiene(clave)) {
            return false;
        }

        raiz = eliminarRecursivo(raiz, clave);
        tamano--;
        modificaciones++;
        return true;
    }

    private Nodo<T> eliminarRecursivo(
            Nodo<T> actual, T clave) {

        if (actual == null) {
            return null;
        }

        int comparacion = clave.compareTo(actual.valor);

        if (comparacion < 0) {
            actual.izquierdo =
                    eliminarRecursivo(actual.izquierdo, clave);

        } else if (comparacion > 0) {
            actual.derecho =
                    eliminarRecursivo(actual.derecho, clave);

        } else {
            if (actual.izquierdo == null) {
                return actual.derecho;
            }

            if (actual.derecho == null) {
                return actual.izquierdo;
            }

            Nodo<T> sucesor = minimo(actual.derecho);
            actual.valor = sucesor.valor;
            actual.derecho = eliminarMinimo(actual.derecho);
        }

        return actual;
    }

    private Nodo<T> minimo(Nodo<T> actual) {
        return actual.izquierdo == null
                ? actual
                : minimo(actual.izquierdo);
    }

    private Nodo<T> eliminarMinimo(Nodo<T> actual) {
        if (actual.izquierdo == null) {
            return actual.derecho;
        }

        actual.izquierdo =
                eliminarMinimo(actual.izquierdo);
        return actual;
    }

    public int tamano() {
        return tamano;
    }

    public boolean estaVacio() {
        return raiz == null;
    }

    /**
     * Altura medida en niveles.
     * Árbol vacío = 0, hoja = 1.
     */
    public int altura() {
        return alturaRecursiva(raiz);
    }

    private int alturaRecursiva(Nodo<T> actual) {
        if (actual == null) {
            return 0;
        }

        return 1 + Math.max(
                alturaRecursiva(actual.izquierdo),
                alturaRecursiva(actual.derecho));
    }

    /**
     * Recorrido: izquierda, raíz, derecha.
     */
    public void recorrerEnOrden(Consumer<? super T> accion) {
        Objects.requireNonNull(
                accion, "La acción no puede ser nula");

        enOrdenRecursivo(raiz, accion);
    }

    private void enOrdenRecursivo(
            Nodo<T> actual, Consumer<? super T> accion) {

        if (actual != null) {
            enOrdenRecursivo(actual.izquierdo, accion);
            accion.accept(actual.valor);
            enOrdenRecursivo(actual.derecho, accion);
        }
    }

    /**
     * Recorrido: raíz, izquierda, derecha.
     */
    public void recorrerPreOrden(Consumer<? super T> accion) {
        Objects.requireNonNull(
                accion, "La acción no puede ser nula");

        preOrdenRecursivo(raiz, accion);
    }

    private void preOrdenRecursivo(
            Nodo<T> actual, Consumer<? super T> accion) {

        if (actual != null) {
            accion.accept(actual.valor);
            preOrdenRecursivo(actual.izquierdo, accion);
            preOrdenRecursivo(actual.derecho, accion);
        }
    }

    /**
     * Recorrido: izquierda, derecha, raíz.
     */
    public void recorrerPostOrden(Consumer<? super T> accion) {
        Objects.requireNonNull(
                accion, "La acción no puede ser nula");

        postOrdenRecursivo(raiz, accion);
    }

    private void postOrdenRecursivo(
            Nodo<T> actual, Consumer<? super T> accion) {

        if (actual != null) {
            postOrdenRecursivo(actual.izquierdo, accion);
            postOrdenRecursivo(actual.derecho, accion);
            accion.accept(actual.valor);
        }
    }

    // =============================================
    // BUSQUEDA RECURSIVA CON LIMITE EXCLUSIVO
    // =============================================

    /**
     * Busca el primer elemento en orden ascendente
     * cuya clave sea menor al límite exclusivo
     * y que cumpla el criterio indicado.
     *
     * <p>Para que la poda sea válida, la función clave
     * debe conservar el orden definido por compareTo.
     * Si a precede a b en el árbol, clave(a)
     * no puede ser mayor que clave(b).</p>
     *
     * @param limiteExclusivo límite superior no incluido
     * @param clave función que extrae la clave
     * @param criterio condición de búsqueda
     * @param <K> tipo de clave comparable
     * @return primer elemento encontrado o null
     */
    public <K extends Comparable<? super K>> T buscarPrimeroAntesDe(
            K limiteExclusivo,
            Function<? super T, ? extends K> clave,
            Predicate<? super T> criterio) {

        Objects.requireNonNull(
                limiteExclusivo, "El límite no puede ser nulo");

        Objects.requireNonNull(
                clave, "La función clave no puede ser nula");

        Objects.requireNonNull(
                criterio, "El criterio no puede ser nulo");

        return buscarPrimeroAntesDeRecursivo(
                raiz, limiteExclusivo, clave, criterio);
    }

    /**
     * Búsqueda recursiva con poda de ramas.
     */
    private <K extends Comparable<? super K>> T
    buscarPrimeroAntesDeRecursivo(
            Nodo<T> actual,
            K limiteExclusivo,
            Function<? super T, ? extends K> clave,
            Predicate<? super T> criterio) {

        if (actual == null) {
            return null;
        }

        K claveActual = Objects.requireNonNull(
                clave.apply(actual.valor),
                "La clave extraída no puede ser nula");

        // Descarta el nodo y su subárbol derecho
        // cuando la clave supera o iguala el límite.
        if (claveActual.compareTo(limiteExclusivo) >= 0) {
            return buscarPrimeroAntesDeRecursivo(
                    actual.izquierdo,
                    limiteExclusivo,
                    clave,
                    criterio);
        }

        T encontrado = buscarPrimeroAntesDeRecursivo(
                actual.izquierdo,
                limiteExclusivo,
                clave,
                criterio);

        if (encontrado != null) {
            return encontrado;
        }

        if (criterio.test(actual.valor)) {
            return actual.valor;
        }

        return buscarPrimeroAntesDeRecursivo(
                actual.derecho,
                limiteExclusivo,
                clave,
                criterio);
    }

    // =============================================
    // ITERADOR PROPIO EN ORDEN
    // =============================================

    /**
     * Iterador propio que entrega elementos
     * ordenados ascendentemente.
     */
    @Override
    public Iterator<T> iterator() {
        return new IteradorEnOrden();
    }

    private static final class Enlace<E> {
        private final Nodo<E> nodo;
        private final Enlace<E> siguiente;

        private Enlace(Nodo<E> nodo, Enlace<E> siguiente) {
            this.nodo = nodo;
            this.siguiente = siguiente;
        }
    }

    private final class IteradorEnOrden implements Iterator<T> {

        private final int modificacionesEsperadas =
                modificaciones;

        private Enlace<T> cima;

        private IteradorEnOrden() {
            apilarRamaIzquierda(raiz);
        }

        private void apilarRamaIzquierda(Nodo<T> actual) {
            while (actual != null) {
                cima = new Enlace<>(actual, cima);
                actual = actual.izquierdo;
            }
        }

        private void comprobarModificaciones() {
            if (modificacionesEsperadas != modificaciones) {
                throw new ConcurrentModificationException(
                        "El árbol fue modificado durante la iteración");
            }
        }

        @Override
        public boolean hasNext() {
            comprobarModificaciones();
            return cima != null;
        }

        @Override
        public T next() {
            comprobarModificaciones();

            if (cima == null) {
                throw new NoSuchElementException(
                        "No hay más elementos");
            }

            Nodo<T> actual = cima.nodo;
            cima = cima.siguiente;

            apilarRamaIzquierda(actual.derecho);

            return actual.valor;
        }
    }
}
