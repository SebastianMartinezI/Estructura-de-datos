package co.edu.uniquindio.poo.comparadores;

import co.edu.uniquindio.poo.modelo.Paquete;
import java.util.Comparator;

/**
 * Define criterios alternativos de comparación para los paquetes
 * registrados en el centro de distribución QuindíoExpress.
 *
 * Estos comparadores no modifican el orden natural establecido
 * mediante Comparable en la clase Paquete.
 *
 * @author Juan Martinez
 */
public final class ComparadoresPaquetes {

    /**
     * Impide crear objetos de esta clase utilitaria.
     */
    private ComparadoresPaquetes() {
    }

    /**
     * Ordena los paquetes por prioridad de mayor a menor.
     * En caso de empate, utiliza el código ascendente.
     */
    public static final Comparator<Paquete> POR_PRIORIDAD =
            Comparator.comparingInt(Paquete::getPrioridad)
                    .reversed()
                    .thenComparing(Paquete::getCodigo);

    /**
     * Ordena los paquetes por peso de mayor a menor.
     * En caso de empate, utiliza el código ascendente.
     */
    public static final Comparator<Paquete> POR_PESO =
            Comparator.comparingDouble(Paquete::getPeso)
                    .reversed()
                    .thenComparing(Paquete::getCodigo);

    /**
     * Ordena los paquetes por tiempo estimado de menor a mayor.
     * En caso de empate, utiliza el código ascendente.
     */
    public static final Comparator<Paquete> POR_TIEMPO =
            Comparator.comparingInt(Paquete::getTiempoEstimado)
                    .thenComparing(Paquete::getCodigo);

    /**
     * Establece el orden de despacho:
     *
     * 1. Mayor prioridad.
     * 2. Menor tiempo estimado.
     * 3. Código en orden alfabético ascendente.
     *
     * Puede utilizarse con PriorityQueue.
     */
    public static final Comparator<Paquete> POR_DESPACHO =
            Comparator.comparingInt(Paquete::getPrioridad)
                    .reversed()
                    .thenComparingInt(Paquete::getTiempoEstimado)
                    .thenComparing(Paquete::getCodigo);
}
