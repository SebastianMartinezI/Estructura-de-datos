package co.edu.uniquindio.sga.servicios.tarifas;

import co.edu.uniquindio.sga.dominio.modelo.Estancia;
import co.edu.uniquindio.sga.dominio.modelo.Temporada;
import co.edu.uniquindio.sga.estructuras.lista.ListaDoblementeEnlazada;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Calendario de temporadas del alojamiento, basado en la lista propia.
 * Las fechas no configuradas usan temporada BAJA (temporada base).
 *
 * <p>No acepta intervalos superpuestos: así no se inventa una prioridad
 * entre MEDIA y ALTA que el enunciado todavía no define. Cada intervalo
 * sigue la convención [entrada, salida), con salida exclusiva.</p>
 */
public final class CalendarioTemporadas implements SelectorTemporada {

    private final ListaDoblementeEnlazada<PeriodoTemporada> periodos =
            new ListaDoblementeEnlazada<>();

    /**
     * Registra un periodo de tarifa sin solaparse con los existentes.
     *
     * @throws IllegalArgumentException si se cruza con otro periodo
     */
    public void registrarPeriodo(Estancia intervalo, Temporada temporada) {
        Objects.requireNonNull(intervalo, "El intervalo es obligatorio");
        Objects.requireNonNull(temporada, "La temporada es obligatoria");

        for (PeriodoTemporada existente : periodos) {
            boolean coincide = intervalo.getEntrada().isBefore(existente.intervalo().getSalida())
                    && existente.intervalo().getEntrada().isBefore(intervalo.getSalida());
            if (coincide) {
                throw new IllegalArgumentException(
                        "El periodo de temporada se solapa con otro ya registrado");
            }
        }
        periodos.agregarUltimo(new PeriodoTemporada(intervalo, temporada));
    }

    /** Consulta qué tarifa corresponde a una noche; por defecto, BAJA. */
    @Override
    public Temporada temporadaEn(LocalDate noche) {
        Objects.requireNonNull(noche, "La fecha de la noche es obligatoria");
        for (PeriodoTemporada periodo : periodos) {
            if (!noche.isBefore(periodo.intervalo().getEntrada())
                    && noche.isBefore(periodo.intervalo().getSalida())) {
                return periodo.temporada();
            }
        }
        return Temporada.BAJA;
    }

    private record PeriodoTemporada(Estancia intervalo, Temporada temporada) { }
}
