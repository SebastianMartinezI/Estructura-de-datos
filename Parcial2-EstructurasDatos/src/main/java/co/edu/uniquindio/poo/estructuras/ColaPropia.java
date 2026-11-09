package co.edu.uniquindio.poo.estructuras;

import java.util.NoSuchElementException;

public class ColaPropia <T> {
    private static class Nodo<T>{
        private final T dato;
        private Nodo<T> siguiente;

        public Nodo(T dato){
            this.dato=dato;
        }
    }
    private Nodo<T>frente;
    private Nodo<T>finalCola;
    private int cantidad;

    //Agrega una elemento al final de la cola

    public void encolar(T elemento){
        Nodo<T> nuevo=new Nodo<>(elemento);

        if(estaVacio()){
            frente= nuevo;
        }else{
            finalCola.siguiente =nuevo;
        }
        finalCola = nuevo;
        cantidad++;
    }

    //Retira y retrona el elemento del frente

    public T desencolar(){
        if(estaVacio()){
            throw new NoSuchElementException("La colla esta vacia");
        }

        T elemento = frente.dato;
        frente = frente.siguiente;
        cantidad--;

        if(estaVacio()){
            finalCola=null;
        }
        return elemento;
    }

    //Consulta el frente sin retirar el ellemento
    public T verFrente(){
        if(estaVacio()){
            throw new NoSuchElementException("La cola esta vacia");
        }
        return frente.dato;
    }

    //Indica si la cola no contiene elementos

    public boolean estaVacio(){
        return frente == null;
    }

    //Retoorna la cantidad de elementos
    public int tamano(){
        return cantidad;
    }

}
