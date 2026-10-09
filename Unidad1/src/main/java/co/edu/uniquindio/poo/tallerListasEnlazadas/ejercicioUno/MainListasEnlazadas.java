package co.edu.uniquindio.poo.tallerListasEnlazadas.ejercicioUno;

public class MainListasEnlazadas {

    public static void main(String[] args) {

        Lista lista1 = new Lista();
        Lista lista2 = new Lista();

        lista1.agregar(1);
        lista1.agregar(3);
        lista1.agregar(5);
        lista1.agregar(7);

        lista2.agregar(2);
        lista2.agregar(4);
        lista2.agregar(6);
        lista2.agregar(8);

        System.out.println("Lista 1:");
        lista1.mostrar();

        System.out.println("Lista 2:");
        lista2.mostrar();

        Lista lista3 = Lista.intercalar(lista1, lista2);

        System.out.println("Lista intercalada:");
        lista3.mostrar();
    }
}