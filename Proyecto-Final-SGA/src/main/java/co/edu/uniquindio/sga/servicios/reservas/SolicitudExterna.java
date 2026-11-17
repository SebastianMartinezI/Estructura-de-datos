package co.edu.uniquindio.sga.servicios.reservas;

import co.edu.uniquindio.sga.dominio.modelo.CanalReserva;
import co.edu.uniquindio.sga.dominio.modelo.EstadoReserva;
import co.edu.uniquindio.sga.dominio.modelo.Reserva;

import java.util.Objects;

/**
 * Solicitud recibida desde una plataforma externa simulada.
 *
 * <p>El identificador de la plataforma evita el procesamiento duplicado de un
 * mismo mensaje. La reserva asociada debe pertenecer al canal EXTERNA y
 * comenzar en estado PENDIENTE. Este objeto no registra ni confirma la reserva.</p>
 *
 * @param idSolicitud identificador único de la solicitud en el canal externo
 * @param reserva reserva propuesta por la plataforma
 */
public record SolicitudExterna(String idSolicitud, Reserva reserva) {

    public SolicitudExterna {
        if (idSolicitud == null || idSolicitud.isBlank()) {
            throw new IllegalArgumentException("El ID de la solicitud externa es obligatorio");
        }
        idSolicitud = idSolicitud.trim();
        Objects.requireNonNull(reserva, "La reserva externa es obligatoria");
        if (reserva.getCanal() != CanalReserva.EXTERNA) {
            throw new IllegalArgumentException("La solicitud debe pertenecer al canal EXTERNA");
        }
        if (reserva.getEstado() != EstadoReserva.PENDIENTE) {
            throw new IllegalArgumentException("La solicitud externa debe estar PENDIENTE");
        }
    }
}
