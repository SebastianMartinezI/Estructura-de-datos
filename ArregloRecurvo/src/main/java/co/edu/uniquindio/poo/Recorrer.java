package co.edu.uniquindio.poo;

public class Recorrer {
    public static void recorrer(int[] arreglo, int posicion) {

        // Caso base
        if (posicion == arreglo.length) {
            return;
        }

        // Mostrar el elemento actual
        System.out.println(arreglo[posicion]);

        // Llamada recursiva
        recorrer(arreglo, posicion + 1);

        System.out.println(arreglo[posicion]);
    }

}
