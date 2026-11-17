package co.edu.uniquindio.sga.servicios.reservas;

import java.util.Objects;

/**
 * Resultado auditable de procesar una solicitud externa en orden FIFO.
 * No representa un pago ni confirma automáticamente una reserva.
 *
 * @param idSolicitud identificador de la solicitud externa
 * @param idReserva identificador de la reserva solicitada
 * @param estado aceptación pendiente o rechazo por solapamiento
 * @param detalle descripción de lo ocurrido
 */
public record ResultadoSolicitudExterna(
        String idSolicitud,
        String idReserva,
        Estado estado,
        String detalle) {

    /** Resultados posibles para una solicitud procesada correctamente. */
    public enum Estado {
        ACEPTADA_PENDIENTE,
        RECHAZADA_POR_CONFLICTO
    }

    public ResultadoSolicitudExterna {
        Objects.requireNonNull(idSolicitud, "El ID de solicitud es obligatorio");
        Objects.requireNonNull(idReserva, "El ID de reserva es obligatorio");
        Objects.requireNonNull(estado, "El estado del resultado es obligatorio");
        Objects.requireNonNull(detalle, "El detalle es obligatorio");
    }
}
