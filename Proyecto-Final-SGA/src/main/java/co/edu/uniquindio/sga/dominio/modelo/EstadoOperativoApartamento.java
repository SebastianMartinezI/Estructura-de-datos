package co.edu.uniquindio.sga.dominio.modelo;

/**
 * Estados operativos contemplados por el alojamiento.
 * Las reglas de transición (entrada, salida y preparación) se implementan
 * en los servicios de operación del alojamiento, no en la interfaz gráfica.
 */
public enum EstadoOperativoApartamento {
    PREPARADO,
    OCUPADO,
    PENDIENTE_PREPARACION,
    EN_PREPARACION,
    FUERA_DE_SERVICIO
}
