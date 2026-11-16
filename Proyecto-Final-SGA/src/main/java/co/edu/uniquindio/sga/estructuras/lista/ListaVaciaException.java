package co.edu.uniquindio.sga.estructuras.lista;

/**
 * Excepción propia para operaciones que requieren elementos en una lista vacía.
 */
public final class ListaVaciaException extends IllegalStateException {

    /**
     * Crea la excepción con un mensaje que explica la operación inválida.
     *
     * @param mensaje explicación del problema
     */
    public ListaVaciaException(String mensaje) {
        super(mensaje);
    }
}