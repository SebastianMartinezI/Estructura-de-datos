package co.edu.uniquindio.sga.servicios.reservas;

import co.edu.uniquindio.sga.dominio.modelo.Apartamento;
import co.edu.uniquindio.sga.dominio.modelo.DetalleNoche;
import co.edu.uniquindio.sga.dominio.modelo.Estancia;
import co.edu.uniquindio.sga.dominio.modelo.LiquidacionReserva;
import co.edu.uniquindio.sga.dominio.modelo.Ocupante;
import co.edu.uniquindio.sga.dominio.modelo.Reserva;
import co.edu.uniquindio.sga.dominio.modelo.Temporada;
import co.edu.uniquindio.sga.estructuras.lista.ListaDoblementeEnlazada;
import co.edu.uniquindio.sga.servicios.tarifas.SelectorTemporada;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Calcula una cotización de una reserva de forma recursiva, noche a noche.
 * No altera el estado de la reserva ni registra cargos o pagos del folio.
 * El origen de temporadas se inyecta y la política de edad es explícita.
 */
public final class ServicioLiquidacion {

    private static final int EDAD_MINIMA_PAGO = 6;

    private final SelectorTemporada selectorTemporada;
    private final CriterioEdad criterioEdad;

    public ServicioLiquidacion(SelectorTemporada selectorTemporada,
                              CriterioEdad criterioEdad) {
        this.selectorTemporada = Objects.requireNonNull(
                selectorTemporada, "El selector de temporada es obligatorio");
        this.criterioEdad = Objects.requireNonNull(
                criterioEdad, "El criterio de edad es obligatorio");
    }

    /**
     * Genera un resultado sin alterar la reserva.
     * El titular solo paga si aparece expresamente en la lista de ocupantes.
     *
     * @param reserva reserva con apartamento, estancia y ocupantes validados
     * @return total y lista doblemente enlazada de noches
     */
    public LiquidacionReserva cotizar(Reserva reserva) {
        Objects.requireNonNull(reserva, "La reserva es obligatoria");

        ListaDoblementeEnlazada<DetalleNoche> detalles =
                new ListaDoblementeEnlazada<>();

        BigDecimal total = liquidarRecursivamente(
                reserva.getApartamento(),
                reserva.getEstancia(),
                reserva.getOcupantes(),
                reserva.getCantidadOcupantes(),
                reserva.getEstancia().getEntrada(),
                detalles
        );

        return new LiquidacionReserva(reserva.getId(), detalles, total);
    }

    /**
     * Calcula el precio total sin crear ni registrar una reserva.
     */
    public BigDecimal cotizarTotal(
            Apartamento apartamento,
            Estancia estancia,
            Iterable<Ocupante> ocupantes) {

        Objects.requireNonNull(apartamento, "El apartamento es obligatorio");
        Objects.requireNonNull(estancia, "La estancia es obligatoria");
        Objects.requireNonNull(ocupantes, "Los ocupantes son obligatorios");

        int cantidadOcupantes = contarOcupantes(ocupantes);

        if (cantidadOcupantes == 0) {
            throw new IllegalArgumentException(
                    "Debe existir al menos un ocupante");
        }

        ListaDoblementeEnlazada<DetalleNoche> detalles =
                new ListaDoblementeEnlazada<>();

        return liquidarRecursivamente(
                apartamento,
                estancia,
                ocupantes,
                cantidadOcupantes,
                estancia.getEntrada(),
                detalles
        );
    }

    /**
     * Caso base: cuando alcanza salida, no se cobra esa noche.
     * Cada llamada recursiva añade una noche en el orden cronológico.
     */
    private BigDecimal liquidarRecursivamente(
            Apartamento apartamento,
            Estancia estancia,
            Iterable<Ocupante> ocupantes,
            int totalOcupantes,
            LocalDate noche,
            ListaDoblementeEnlazada<DetalleNoche> detalles) {

        if (!noche.isBefore(estancia.getSalida())) {
            return BigDecimal.ZERO;
        }

        Temporada temporada = Objects.requireNonNull(
                selectorTemporada.temporadaEn(noche),
                "El selector no puede devolver una temporada nula");

        BigDecimal tarifa = apartamento.tarifaPorPersonaNoche(temporada);

        int pagadores = contarPagadores(
                ocupantes,
                estancia.getEntrada(),
                noche
        );

        BigDecimal subtotal = tarifa.multiply(
                BigDecimal.valueOf(pagadores));

        detalles.agregarUltimo(new DetalleNoche(
                noche,
                temporada,
                tarifa,
                totalOcupantes,
                pagadores,
                subtotal
        ));

        return subtotal.add(liquidarRecursivamente(
                apartamento,
                estancia,
                ocupantes,
                totalOcupantes,
                noche.plusDays(1),
                detalles
        ));
    }

    private int contarOcupantes(Iterable<Ocupante> ocupantes) {
        int cantidad = 0;

        for (Ocupante ocupante : ocupantes) {
            Objects.requireNonNull(ocupante, "Un ocupante no puede ser nulo");
            cantidad++;
        }

        return cantidad;
    }

    private int contarPagadores(
            Iterable<Ocupante> ocupantes,
            LocalDate entrada,
            LocalDate noche) {

        LocalDate fechaReferencia =
                criterioEdad == CriterioEdad.POR_NOCHE
                        ? noche
                        : entrada;

        int pagadores = 0;

        for (Ocupante ocupante : ocupantes) {
            if (ocupante.edadEn(fechaReferencia) >= EDAD_MINIMA_PAGO) {
                pagadores++;
            }
        }

        return pagadores;
    }
}
