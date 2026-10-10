package co.edu.uniquindio.poo.estructuras;

public interface Coleccion<T> {
    void agregar(T elemento);
    boolean eliminar(T elemento);
    T obtener(int posicion);
    int tamanio();
    boolean estaVacia();
}
