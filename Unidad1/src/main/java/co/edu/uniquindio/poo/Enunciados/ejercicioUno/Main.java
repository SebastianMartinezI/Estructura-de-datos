package co.edu.uniquindio.poo.Enunciados.ejercicioUno;

import java.util.ArrayList;

public class Main {


    public static void main(String[] args) {

        InventarioCaja<Integer> inventario = new InventarioCaja<>();

        inventario.agregar(4);
        inventario.agregar(6);
        inventario.agregar(7);
        inventario.agregar(9);
        inventario.agregar(10);

        ArrayList<Integer>mayores = inventario.mayoresQue(6);
        System.out.println("Mayores que 6: " + mayores);
    }

}
