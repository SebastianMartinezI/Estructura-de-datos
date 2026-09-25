package co.edu.uniquindio.poo.collection.ejercicioDos;

//Cree una pila (Stack) que pueda almacenar objetos de diferentes tipos y que solo
// permite insertar elementos si el tipo del objeto coincide con el tipo del
// elemento en la cima de la pila.

public class Main {
    public static void main(String[] args) {

        Pila pila = new Pila();

        pila.insertar("Java");

        pila.insertar("Python");
        pila.insertar("PHP");
        pila.insertar(100);
        pila.insertar("C++");

        System.out.println("\n Contenido de la pila:");

        pila.mostrar();

        System.out.println("\n Contenido eliminado de la pila:");

        System.out.println(pila.pop());

        System.out.println("\n Nueva pila:");
        pila.mostrar();
    }
}
