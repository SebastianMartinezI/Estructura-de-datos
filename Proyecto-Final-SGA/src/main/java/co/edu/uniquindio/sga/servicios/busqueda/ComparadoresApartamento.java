package co.edu.uniquindio.sga.servicios.busqueda;

import co.edu.uniquindio.sga.dominio.modelo.Apartamento;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.ToIntFunction;

/**
 * Fábrica de criterios alternativos para ordenar apartamentos.
 *
 * <p>El orden natural de {@link Apartamento} continúa siendo el código.
 * Estos comparadores se usan únicamente cuando el huésped selecciona
 * otro criterio de consulta. Todos incluyen el código como desempate
 * determinista mediante {@code thenComparing}.</p>
 */
public final class ComparadoresApartamento {

    private ComparadoresApartamento() {
        throw new AssertionError("No se debe instanciar una clase de utilidades");
    }

    /**
     * Ordena por el precio total cotizado para la estancia y ocupación
     * solicitadas, de menor a mayor.
     *
     * @param cotizacionTotal función que calcula la cotización completa
     *                        de cada apartamento para una misma búsqueda
     * @return comparador por precio y luego por código
     */
    public static Comparator<Apartamento> porPrecio(
            Function<? super Apartamento, BigDecimal> cotizacionTotal) {
        Objects.requireNonNull(cotizacionTotal, "La función de cotización es obligatoria");
        return Comparator.comparing((Apartamento apartamento) ->
                        Objects.requireNonNull(
                                cotizacionTotal.apply(apartamento),
                                "La cotización no puede ser nula"))
                .thenComparing(Apartamento::getCodigo);
    }

    /**
     * Ordena por capacidad máxima, de mayor a menor; desempata por código.
     */
    public static Comparator<Apartamento> porCapacidad() {
        return Comparator.comparingInt(Apartamento::getCapacidadMaxima)
                .reversed()
                .thenComparing(Apartamento::getCodigo);
    }

    /**
     * Ordena por número de dormitorios, de mayor a menor; desempata por código.
     */
    public static Comparator<Apartamento> porDormitorios() {
        return Comparator.comparingInt(Apartamento::getNumeroDormitorios)
                .reversed()
                .thenComparing(Apartamento::getCodigo);
    }

    /**
     * Ordena por cantidad de reservas registradas, de menor a mayor,
     * para favorecer apartamentos con menor historial de demanda.
     *
     * <p>El contador se inyecta para no acoplar el comparador a un servicio
     * concreto ni almacenar una copia de las reservas.</p>
     *
     * @param cantidadReservas función que devuelve el total histórico
     * @return comparador por cantidad de reservas y luego por código
     */
    public static Comparator<Apartamento> porNumeroReservas(
            ToIntFunction<? super Apartamento> cantidadReservas) {
        Objects.requireNonNull(cantidadReservas, "El contador de reservas es obligatorio");
        return Comparator.comparingInt((Apartamento apartamento) -> {
                    int cantidad = cantidadReservas.applyAsInt(apartamento);
                    if (cantidad < 0) {
                        throw new IllegalArgumentException(
                                "La cantidad de reservas no puede ser negativa");
                    }
                    return cantidad;
                })
                .thenComparing(Apartamento::getCodigo);
    }
}
