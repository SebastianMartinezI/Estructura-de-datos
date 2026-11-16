package co.edu.uniquindio.sga.dominio.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public final class MovimientoFolio {

    private final long numero;
    private final LocalDateTime fecha;
    private final TipoMovimiento tipo;
    private final String concepto;
    private final BigDecimal importe;
    private final Long numeroMovimientoOriginal;

    MovimientoFolio(
            long numero,
            LocalDateTime fecha,
            TipoMovimiento tipo,
            String concepto,
            BigDecimal importe,
            Long numeroMovimientoOriginal
    ) {
        this.numero = numero;
        this.fecha = Objects.requireNonNull(fecha);
        this.tipo = Objects.requireNonNull(tipo);
        this.concepto = Objects.requireNonNull(concepto);
        this.importe = Objects.requireNonNull(importe);
        this.numeroMovimientoOriginal = numeroMovimientoOriginal;
    }

    public long getNumero() {
        return numero;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public String getConcepto() {
        return concepto;
    }

    // Positivo aumenta la deuda; negativo la reduce.
    public BigDecimal getImporte() {
        return importe;
    }

    public Long getNumeroMovimientoOriginal() {
        return numeroMovimientoOriginal;
    }

    @Override
    public String toString() {
        return numero + " | " + tipo
                + " | " + concepto
                + " | " + importe.toPlainString();
    }
}