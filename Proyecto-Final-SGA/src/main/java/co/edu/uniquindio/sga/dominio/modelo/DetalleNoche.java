package co.edu.uniquindio.sga.dominio.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Renglón inmutable del desglose de una noche: tarifa por persona,
 * personas que ocupan cupo, personas que pagan y subtotal.
 */
public record DetalleNoche(LocalDate noche, Temporada temporada,
                           BigDecimal tarifaPorPersona, int totalOcupantes,
                           int ocupantesQuePagan, BigDecimal subtotal) {

    public DetalleNoche {
        Objects.requireNonNull(noche, "La noche es obligatoria");
        Objects.requireNonNull(temporada, "La temporada es obligatoria");
        Objects.requireNonNull(tarifaPorPersona, "La tarifa es obligatoria");
        Objects.requireNonNull(subtotal, "El subtotal es obligatorio");
        if (tarifaPorPersona.signum() < 0 || totalOcupantes < 1
                || ocupantesQuePagan < 0 || ocupantesQuePagan > totalOcupantes) {
            throw new IllegalArgumentException("Valores de cobro u ocupación inválidos");
        }
        BigDecimal calculado = tarifaPorPersona.multiply(
                BigDecimal.valueOf(ocupantesQuePagan));
        if (subtotal.compareTo(calculado) != 0) {
            throw new IllegalArgumentException("El subtotal no coincide con la tarifa y pagadores");
        }
    }
}
