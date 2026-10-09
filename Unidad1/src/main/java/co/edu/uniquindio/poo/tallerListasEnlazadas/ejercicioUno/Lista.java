package co.edu.uniquindio.poo.tallerListasEnlazadas.ejercicioUno;

public class Lista {

    // Nodo de la lista
    private class Nodo {
        int dato;
        Nodo siguiente;

        public Nodo(int dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private Nodo cabeza;

    // Agregar un elemento al final
    public void agregar(int dato) {
        Nodo nuevo = new Nodo(dato);

        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo actual = cabeza;

            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }

            actual.siguiente = nuevo;
        }
    }

    // Mostrar la lista
    public void mostrar() {
        Nodo actual = cabeza;

        while (actual != null) {
            System.out.print(actual.dato + " → ");
            actual = actual.siguiente;
        }

        System.out.println("null");
    }

    // Intercalar dos listas
    public static Lista intercalar(Lista lista1, Lista lista2) {

        Lista resultado = new Lista();

        Nodo actual1 = lista1.cabeza;
        Nodo actual2 = lista2.cabeza;

        while (actual1 != null || actual2 != null) {

            if (actual1 != null) {
                resultado.agregar(actual1.dato);
                actual1 = actual1.siguiente;
            }

            if (actual2 != null) {
                resultado.agregar(actual2.dato);
                actual2 = actual2.siguiente;
            }
        }

        return resultado;
    }
}