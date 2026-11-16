package co.edu.uniquindio.sga.dominio.modelo;

/**
 * Niveles de temporada que el SGA utiliza al liquidar cada noche.
 * El calendario que determina la temporada de una fecha corresponde
 * al servicio de tarifas; este enum solo representa su clasificación.
 */
public enum Temporada {
    BAJA,
    MEDIA,
    ALTA
}
