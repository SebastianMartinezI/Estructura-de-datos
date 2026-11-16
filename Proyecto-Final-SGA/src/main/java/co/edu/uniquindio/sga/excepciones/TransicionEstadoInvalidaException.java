package co.edu.uniquindio.sga.excepciones;

/** Indica que una operación no es válida para el estado actual. */
public class TransicionEstadoInvalidaException
        extends RuntimeException {

    public TransicionEstadoInvalidaException(String mensaje) {
        super(mensaje);
    }
}