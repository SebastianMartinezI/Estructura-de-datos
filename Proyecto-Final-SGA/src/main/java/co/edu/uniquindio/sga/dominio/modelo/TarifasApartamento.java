package co.edu.uniquindio.sga.dominio.modelo;

import java.math.BigDecimal;

/**
 * Tarifas por persona y noche de un apartamento para cada temporada.
 * Es un objeto de valor inmutable: las actualizaciones generan uno nuevo.
 * No determina las fechas de vigencia de cada temporada.
 */
public final class TarifasApartamento {

    private final BigDecimal baja;
    private final BigDecimal media;
    private final BigDecimal alta;

    /**
     * @param baja tarifa de temporada baja; no puede ser negativa
     * @param media tarifa de temporada media; no puede ser negativa
     * @param alta tarifa de temporada alta; no puede ser negativa
     */
    public TarifasApartamento(BigDecimal baja, BigDecimal media, BigDecimal alta) {
        this.baja = validarTarifa(baja, "baja");
        this.media = validarTarifa(media, "media");
        this.alta = validarTarifa(alta, "alta");
    }

    private static BigDecimal validarTarifa(BigDecimal tarifa, String nombre) {
        if (tarifa == null || tarifa.signum() < 0) {
            throw new IllegalArgumentException("La tarifa " + nombre + " debe ser mayor o igual a cero.");
        }
        return tarifa;
    }

    /** Devuelve la tarifa correspondiente al tipo de temporada solicitado. */
    public BigDecimal paraTemporada(Temporada temporada) {
        if (temporada == null) {
            throw new IllegalArgumentException("La temporada es obligatoria.");
        }
        return switch (temporada) {
            case BAJA -> baja;
            case MEDIA -> media;
            case ALTA -> alta;
        };
    }

    public BigDecimal getBaja() {
        return baja;
    }

    public BigDecimal getMedia() {
        return media;
    }

    public BigDecimal getAlta() {
        return alta;
    }
}
