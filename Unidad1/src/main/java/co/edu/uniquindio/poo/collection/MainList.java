package co.edu.uniquindio.poo.collection;

import java.util.ArrayList;
import java.util.function.IntUnaryOperator;

public class MainList {
    public static void main(String[] args) {
        testArraylist();
    }

    private static void testArraylist() {
        ArrayList<Integer> notas = new ArrayList<>();
        notas.add(5);
        notas.add(8);
        notas.add(7);
        notas.add(9);
        notas.add(6);
        notas.add(4);
        System.out.println(notas);

        System.out.println(notas.get(0));

        Integer notaEliminar = 9;
        System.out.println(notas.contains(notaEliminar));
        System.out.println(notas.indexOf(notaEliminar));
        notas.remove(notaEliminar);
        System.out.println(notas);

        notas.remove(0);
        System.out.println(notas);
        System.out.println(notas.size());
        System.out.println(notas.contains(notaEliminar));
        System.out.println(notas.indexOf(notaEliminar));
        System.out.println(notas.isEmpty());

    }
}
