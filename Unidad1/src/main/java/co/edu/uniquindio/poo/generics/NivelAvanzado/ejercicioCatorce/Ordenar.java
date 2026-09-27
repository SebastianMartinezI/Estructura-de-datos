package co.edu.uniquindio.poo.generics.NivelAvanzado.ejercicioCatorce;

import java.util.List;

public class Ordenar<T extends Comparable<T>> {

    public void ordenar(List<T> lista) {
        int cantidad = lista.size();

        for (int pasada = 0; pasada < cantidad - 1; pasada++) {
            for (int posicion = 0; posicion < cantidad - 1 - pasada; posicion++) {
                T actual = lista.get(posicion);
                T siguiente = lista.get(posicion + 1);

                if (actual.compareTo(siguiente) > 0) {
                    lista.set(posicion, siguiente);
                    lista.set(posicion + 1, actual);
                }
            }
        }
    }
}
