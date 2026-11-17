package co.edu.uniquindio.sga.servicios.reservas;

import co.edu.uniquindio.sga.dominio.modelo.LiquidacionReserva;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Instantánea de cotización y anticipo requerido para una reserva.
 * Se conserva el total acordado al registrarse la solicitud; una posterior
 * actualización de tarifas no modifica este resultado.
 */
public final class CotizacionAnticipo {

    private static final BigDecimal PORCENTAJE_ANTICIPO = new BigDecimal("0.30");

    private final LiquidacionReserva liquidacion;
    private final BigDecimal anticipoMinimo;
    private final BigDecimal saldoTrasAnticipoMinimo;

    public CotizacionAnticipo(LiquidacionReserva liquidacion) {
        this.liquidacion = Objects.requireNonNull(liquidacion, "La liquidación es obligatoria");
        this.anticipoMinimo = liquidacion.getTotal().multiply(PORCENTAJE_ANTICIPO);
        this.saldoTrasAnticipoMinimo = liquidacion.getTotal().subtract(anticipoMinimo);
    }

    public LiquidacionReserva getLiquidacion() { return liquidacion; }
    public String getIdReserva() { return liquidacion.getIdReserva(); }
    public BigDecimal getTotal() { return liquidacion.getTotal(); }
    public BigDecimal getAnticipoMinimo() { return anticipoMinimo; }
    public BigDecimal getSaldoTrasAnticipoMinimo() { return saldoTrasAnticipoMinimo; }
}
