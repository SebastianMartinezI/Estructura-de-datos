package co.edu.uniquindio.poo.estructuras;


public class PruebaListaSimpleEnlazadas {
    public static void main(String[] args) {
        ListaSimpleEnlazadas<String> lista = new ListaSimpleEnlazadas<>();

        System.out.println("LISTA VACIA");
        System.out.println("¿Vacia? " + lista.estaVacia());
        System.out.println("Eliminar en vacia: " + lista.eliminar("A"));

        System.out.println("\nUN SOLO ELEMENTO");
        lista.agregar("X");
        mostrar(lista);
        System.out.println("Eliminar X: " + lista.eliminar("X"));
        System.out.println("¿Vacía? " + lista.estaVacia());
        lista.agregar("Y");
        mostrar(lista);
        lista.eliminar("Y");

        System.out.println("\nAGREGAR A, B, C, D");
        lista.agregar("A");
        lista.agregar("B");
        lista.agregar("C");
        lista.agregar("D");
        mostrar(lista);

        System.out.println("\nELIMINAR PRIMERO (A)");
        System.out.println(lista.eliminar("A"));
        mostrar(lista);

        System.out.println("\nELIMINAR INTERMEDIO (C)");
        System.out.println(lista.eliminar("C"));
        mostrar(lista);

        System.out.println("\nELIMINAR ULTIMO (D)");
        System.out.println(lista.eliminar("D"));
        mostrar(lista);

        System.out.println("\nELIMINAR INEXISTENTE (Z)");
        System.out.println(lista.eliminar("Z"));
        mostrar(lista);

        System.out.println("\nAGREGAR DESPUES DE ELIMINAR EL ULTIMO");
        lista.agregar("E");
        mostrar(lista);

        System.out.println("\nOBTENER POSICION 1");
        System.out.println(lista.obtener(1));
    }

    private static void mostrar(ListaSimpleEnlazadas<String> lista) {
        StringBuilder texto = new StringBuilder();
        for (String valor : lista) {
            texto.append(valor).append(" ");
        }
        System.out.println("Contenido: " + texto
                + "| Tamaño: " + lista.tamanio());
    }
}
