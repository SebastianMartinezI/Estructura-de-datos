package co.edu.uniquindio.sga.servicios.operacion;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Comparador compartido para la prioridad de preparación del alojamiento.
 *
 * <p>Prioriza primero las tareas cuyo apartamento tiene una entrada
 * programada para hoy; dentro de ese grupo ordena por hora de llegada.
 * Las tareas sin entrada hoy quedan después. El identificador resuelve
 * empates de forma determinista.</p>
 *
 * <p>La clase no define la tarea operativa ni administra la cola: Cristian
 * puede usar este comparador con su modelo y su {@code ColaPrioridad<T>}.</p>
 */
public final class ComparadorPreparacion {

    private ComparadorPreparacion() {
        throw new AssertionError("No se debe instanciar una clase de utilidades");
    }

    /**
     * Crea el orden de preparación exigido por el enunciado.
     *
     * @param tieneEntradaHoy indica si la tarea tiene entrada programada hoy
     * @param horaLlegada obtiene la hora estimada de llegada
     * @param identificador obtiene una clave única para desempatar
     * @param <T> tipo de tarea definido por el módulo de operación
     * @return comparador: entrada hoy, hora de llegada e identificador
     */
    public static <T> Comparator<T> porEntradaHoyYHoraLlegada(
            Predicate<? super T> tieneEntradaHoy,
            Function<? super T, LocalDateTime> horaLlegada,
            Function<? super T, String> identificador) {
        Objects.requireNonNull(tieneEntradaHoy, "El criterio de entrada de hoy es obligatorio");
        Objects.requireNonNull(horaLlegada, "La función de hora de llegada es obligatoria");
        Objects.requireNonNull(identificador, "La función de identificador es obligatoria");

        return Comparator
                .comparingInt((T tarea) -> tieneEntradaHoy.test(tarea) ? 0 : 1)
                .thenComparing(tarea -> tieneEntradaHoy.test(tarea)
                        ? Objects.requireNonNull(horaLlegada.apply(tarea),
                                "Una tarea con entrada hoy requiere hora de llegada")
                        : LocalDateTime.MAX)
                .thenComparing(tarea -> Objects.requireNonNull(
                        identificador.apply(tarea), "El identificador no puede ser nulo"));
    }
}
