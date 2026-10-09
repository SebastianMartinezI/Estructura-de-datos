package co.edu.uniquindio.poo.algoritmos;

import co.edu.uniquindio.poo.modelo.Paquete;
import java.util.List;

/**
 * Prueba manual de los algoritmos recursivos.
 *
 * @author Juan Martinez
 */
public class PruebaAlgoritmosRecursivos {

    public static void main(String[] args) {

        List<Paquete> paquetes = List.of(
                new Paquete("PQ101", "Armenia", 4.5, 5, 30),
                new Paquete("PQ203", "Salento", 3.0, 2, 25),
                new Paquete("PQ415", "Armenia", 7.2, 4, 40),
                new Paquete("PQ730", "Armenia", 2.3, 3, 20)
        );

        System.out.println("=== PESO POR MUNICIPIO ===");

        double pesoArmenia =
                AlgoritmosRecursivos.calcularPesoPorMunicipio(
                        paquetes, "Armenia");

        System.out.println("Peso total Armenia: "
                + pesoArmenia + " kg");

        System.out.println("\n=== CONTEO POR PRIORIDAD ===");

        int cantidad =
                AlgoritmosRecursivos.contarPorPrioridad(
                        paquetes, 4);

        System.out.println("Paquetes con prioridad >= 4: "
                + cantidad);

        System.out.println("\n=== CASOS BASE ===");

        System.out.println("Lista vacía, peso: "
                + AlgoritmosRecursivos.calcularPesoPorMunicipio(
                List.of(), "Armenia"));

        System.out.println("Lista vacía, cantidad: "
                + AlgoritmosRecursivos.contarPorPrioridad(
                List.of(), 4));
    }
}
