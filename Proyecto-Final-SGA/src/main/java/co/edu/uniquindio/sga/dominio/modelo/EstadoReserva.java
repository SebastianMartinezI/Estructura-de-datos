package co.edu.uniquindio.sga.dominio.modelo;

/**
 * Estados del ciclo de vida de una reserva.
 * Las autorizaciones, el cobro y el check-in se gestionan en servicios.
 */
public enum EstadoReserva {
    PENDIENTE,
    CONFIRMADA,
    EN_CURSO,
    FINALIZADA,
    CANCELADA,
    NO_SHOW
}
