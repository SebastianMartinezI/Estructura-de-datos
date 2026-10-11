package co.edu.uniquindio.poo.estructuras;

/**
 * Define las operaciones básicas de una colección genérica.
 * <T> tipo de elemento almacenado
 */
public interface Coleccion<T> {

    // Agrega un elemento a la colección.
    void agregar(T elemento);

    // Elimina un elemento y retorna true si lo encontró.
    boolean eliminar(T elemento);

    // Obtiene el elemento de una posición.
    T obtener(int posicion);

    // Retorna la cantidad de elementos.
    int tamanio();

    // Indica si no hay elementos almacenados.
    boolean estaVacia();
}