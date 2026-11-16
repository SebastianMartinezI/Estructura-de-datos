package co.edu.uniquindio.sga.servicios.tarifas;

import co.edu.uniquindio.sga.dominio.modelo.Temporada;
import java.time.LocalDate;

/**
 * Contrato para consultar la temporada aplicable a una noche concreta.
 * Permite sustituir un calendario manual por otra fuente sin modificar
 * el servicio de liquidación (inversión de dependencias).
 */
@FunctionalInterface
public interface SelectorTemporada {

    /** Devuelve la temporada de la fecha indicada, nunca {@code null}. */
    Temporada temporadaEn(LocalDate noche);
}
