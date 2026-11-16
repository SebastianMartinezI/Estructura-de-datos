package co.edu.uniquindio.sga.servicios.reservas;

import java.math.BigDecimal;

/**
 * Consulta de pagos de anticipo acreditados en el folio de una reserva.
 *
 * <p>Contrato de integración con el módulo de folios de Cristian. Esta interfaz
 * no registra cobros ni inventa pagos. La implementación real debe consultar
 * los movimientos vigentes (incluidos contramovimientos) de un folio.</p>
 */
@FunctionalInterface
public interface ConsultaAnticipos {

    /**
     * @param idReserva identificador de la reserva
     * @return suma neta de anticipos efectivamente acreditados, no nula
     */
    BigDecimal totalAcreditado(String idReserva);
}
