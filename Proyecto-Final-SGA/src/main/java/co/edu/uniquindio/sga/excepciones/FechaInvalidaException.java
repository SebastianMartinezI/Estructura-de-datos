package co.edu.uniquindio.sga.excepciones;

/** Indica un rango de fechas de estancia inválido. */
public class FechaInvalidaException extends IllegalArgumentException {
    public FechaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
