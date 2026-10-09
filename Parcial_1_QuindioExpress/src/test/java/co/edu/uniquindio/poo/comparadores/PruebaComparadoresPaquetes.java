package co.edu.uniquindio.poo.comparadores;

import co.edu.uniquindio.poo.modelo.Paquete;
import java.util.ArrayList;
import java.util.List;

/**
 * Prueba manual de los criterios de comparación.
 *
 * @author Juan Martinez
 */
public class PruebaComparadoresPaquetes {

    public static void main(String[] args) {

        List<Paquete> paquetes = new ArrayList<>();

        paquetes.add(new Paquete(
                "PQ420", "Armenia", 4.5, 3, 30));

        paquetes.add(new Paquete(
                "PQ731", "Salento", 8.0, 5, 45));

        paquetes.add(new Paquete(
                "PQ105", "Calarcá", 3.0, 5, 25));

        paquetes.add(new Paquete(
                "PQ942", "Montenegro", 6.0, 4, 20));

        mostrar("ORDEN NATURAL POR CÓDIGO",
                paquetes, null);

        mostrar("POR PRIORIDAD",
                paquetes, ComparadoresPaquetes.POR_PRIORIDAD);

        mostrar("POR PESO",
                paquetes, ComparadoresPaquetes.POR_PESO);

        mostrar("POR TIEMPO",
                paquetes, ComparadoresPaquetes.POR_TIEMPO);

        mostrar("ORDEN DE DESPACHO",
                paquetes, ComparadoresPaquetes.POR_DESPACHO);
    }

    /**
     * Muestra los códigos de los paquetes ordenados
     * según el comparador recibido.
     */
    private static void mostrar(
            String titulo,
            List<Paquete> originales,
            java.util.Comparator<Paquete> comparador) {

        List<Paquete> copia = new ArrayList<>(originales);

        if (comparador == null) {
            copia.sort(null);
        } else {
            copia.sort(comparador);
        }

        System.out.println("\n=== " + titulo + " ===");

        for (Paquete paquete : copia) {
            System.out.println(
                    paquete.getCodigo()
                            + " | Prioridad: " + paquete.getPrioridad()
                            + " | Peso: " + paquete.getPeso()
                            + " | Tiempo: " + paquete.getTiempoEstimado());
        }
    }
}
