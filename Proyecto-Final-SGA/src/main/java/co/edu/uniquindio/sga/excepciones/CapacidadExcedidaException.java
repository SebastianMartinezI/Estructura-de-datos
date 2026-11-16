package co.edu.uniquindio.sga.excepciones;

/** Error de dominio cuando un grupo supera la capacidad del apartamento. */
public final class CapacidadExcedidaException extends IllegalArgumentException {

    public CapacidadExcedidaException(String mensaje) {
        super(mensaje);
    }
}
