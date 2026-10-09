package co.edu.uniquindio.poo.algoritmos;

import co.edu.uniquindio.poo.modelo.Paquete;
import java.util.List;

/**
 * Prueba manual del algoritmo divide y vencerás.
 *
 * @author Juan Martinez
 */
public class PruebaDivideYVenceras {

    public static void main(String[] args) {

        List<Paquete> paquetes = List.of(
                new Paquete("PQ101", "Armenia", 4.5, 3, 30),
                new Paquete("PQ203", "Salento", 12.0, 5, 25),
                new Paquete("PQ415", "Calarcá", 3.2, 2, 40),
                new Paquete("PQ730", "Montenegro", 18.7, 4, 20),
                new Paquete("PQ812", "Filandia", 7.4, 3, 35),
                new Paquete("PQ926", "Armenia", 5.1, 5, 45)
        );

        System.out.println("=== MAYOR PESO ===");

        Paquete mayor =
                DivideYVenceras.encontrarMayorPeso(paquetes);

        System.out.println("Código: " + mayor.getCodigo());
        System.out.println("Peso: " + mayor.getPeso() + " kg");

        System.out.println("\n=== LISTA VACÍA ===");

        Paquete resultadoVacio =
                DivideYVenceras.encontrarMayorPeso(List.of());

        System.out.println(resultadoVacio == null
                ? "Correcto: no hay paquetes"
                : "ERROR: resultado inesperado");

        System.out.println("\n=== UN SOLO PAQUETE ===");

        Paquete unico =
                DivideYVenceras.encontrarMayorPeso(
                        List.of(paquetes.get(0)));

        System.out.println("Código: " + unico.getCodigo());

        System.out.println("\n=== EMPATE DE PESO ===");

        List<Paquete> empatados = List.of(
                new Paquete("PQ900", "Armenia", 10.0, 3, 20),
                new Paquete("PQ100", "Salento", 10.0, 4, 30)
        );

        Paquete desempate =
                DivideYVenceras.encontrarMayorPeso(empatados);

        System.out.println("Código seleccionado: "
                + desempate.getCodigo());
    }
}
