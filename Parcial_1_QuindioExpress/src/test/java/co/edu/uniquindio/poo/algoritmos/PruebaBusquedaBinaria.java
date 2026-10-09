package co.edu.uniquindio.poo.algoritmos;

import co.edu.uniquindio.poo.modelo.Paquete;

import java.util.ArrayList;
import java.util.List;

/**
 * Prueba manual del algoritmo de búsqueda binaria.
 *
 * @author Juan Martinez
 */
public class PruebaBusquedaBinaria {

    public static void main(String[] args) {

        List<Paquete> paquetes = new ArrayList<>();

        paquetes.add(new Paquete(
                "PQ731", "Armenia", 4.5, 5, 30));

        paquetes.add(new Paquete(
                "PQ105", "Salento", 3.0, 3, 25));

        paquetes.add(new Paquete(
                "PQ942", "Armenia", 7.2, 4, 45));

        paquetes.add(new Paquete(
                "PQ318", "Calarcá", 2.5, 2, 20));

        paquetes.add(new Paquete(
                "PQ567", "Montenegro", 6.0, 5, 35));

        // Ordenamiento previo por código utilizando Comparable.
        paquetes.sort(null);

        System.out.println("=== LISTA ORDENADA ===");

        for (Paquete paquete : paquetes) {
            System.out.println(paquete.getCodigo());
        }

        System.out.println("\n=== BÚSQUEDA EXISTENTE ===");

        Paquete encontrado =
                BusquedaBinaria.buscarPorCodigo(
                        paquetes, "PQ567");

        System.out.println(encontrado != null
                ? "Encontrado: " + encontrado.getCodigo()
                : "Paquete no encontrado");

        System.out.println("\n=== BÚSQUEDA INEXISTENTE ===");

        Paquete inexistente =
                BusquedaBinaria.buscarPorCodigo(
                        paquetes, "PQ999");

        System.out.println(inexistente == null
                ? "Correcto: paquete inexistente"
                : "ERROR: resultado inesperado");

        System.out.println("\n=== LISTA VACÍA ===");

        Paquete resultadoVacio =
                BusquedaBinaria.buscarPorCodigo(
                        new ArrayList<>(), "PQ105");

        System.out.println(resultadoVacio == null
                ? "Correcto: lista vacía"
                : "ERROR: resultado inesperado");

        System.out.println("\n=== PRIMER Y ÚLTIMO ELEMENTO ===");

        System.out.println(
                BusquedaBinaria.buscarPorCodigo(
                        paquetes, "PQ105").getCodigo());

        System.out.println(
                BusquedaBinaria.buscarPorCodigo(
                        paquetes, "PQ942").getCodigo());
    }
}
