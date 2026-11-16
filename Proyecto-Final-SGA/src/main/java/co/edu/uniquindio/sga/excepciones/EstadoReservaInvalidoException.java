package co.edu.uniquindio.sga.excepciones;

/** Error de dominio cuando se solicita una transición de reserva no permitida. */
public final class EstadoReservaInvalidoException extends IllegalStateException {

    public EstadoReservaInvalidoException(String mensaje) {
        super(mensaje);
    }
}
