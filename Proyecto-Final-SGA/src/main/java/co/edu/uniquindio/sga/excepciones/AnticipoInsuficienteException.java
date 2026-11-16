package co.edu.uniquindio.sga.excepciones;

/** El pago acreditado no alcanza el anticipo mínimo de una reserva Portal. */
public final class AnticipoInsuficienteException extends IllegalStateException {
    public AnticipoInsuficienteException(String mensaje) { super(mensaje); }
}
