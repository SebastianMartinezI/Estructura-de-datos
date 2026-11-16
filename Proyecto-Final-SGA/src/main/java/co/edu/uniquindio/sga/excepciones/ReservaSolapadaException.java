package co.edu.uniquindio.sga.excepciones;

/**
 * Se produce cuando una solicitud intenta ocupar al menos una noche
 * bloqueada por otra reserva activa del mismo apartamento.
 */
public final class ReservaSolapadaException extends IllegalStateException {

    public ReservaSolapadaException(String mensaje) {
        super(mensaje);
    }
}
