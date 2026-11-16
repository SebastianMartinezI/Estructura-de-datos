package co.edu.uniquindio.sga.estructuras.cola;

/**
 * Excepción propia para operaciones que requieren una cola no vacía.
 */
public final class ColaVaciaException extends IllegalStateException {

    /**
     * Construye la excepción con una descripción del problema.
     *
     * @param mensaje razón de la operación inválida
     */
    public ColaVaciaException(String mensaje) {
        super(mensaje);
    }
}