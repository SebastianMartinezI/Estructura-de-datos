package co.edu.uniquindio.sga.servicios.reservas;

/**
 * Política para evaluar la gratuidad de los menores de seis años.
 * El enunciado no especifica cuál debe usar el alojamiento; el equipo
 * debe acordarla antes de la integración definitiva.
 */
public enum CriterioEdad {
    /** La edad se toma al llegar, igual para toda la estancia. */
    FECHA_ENTRADA,
    /** La edad se vuelve a evaluar en la fecha de cada noche. */
    POR_NOCHE
}
