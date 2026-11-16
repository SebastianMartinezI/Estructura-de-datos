package co.edu.uniquindio.sga.dominio.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** Información de una tarea pendiente de preparación. */
public final class TareaPreparacion {

    private final String codigoApartamento;
    private final LocalDateTime fechaRegistro;
    private final LocalDateTime proximaLlegada;

    public TareaPreparacion(
            String codigoApartamento,
            LocalDateTime fechaRegistro,
            LocalDateTime proximaLlegada
    ) {
        if (codigoApartamento == null
                || codigoApartamento.isBlank()) {
            throw new IllegalArgumentException(
                    "El código del apartamento es obligatorio."
            );
        }

        if (fechaRegistro == null) {
            throw new IllegalArgumentException(
                    "La fecha de registro es obligatoria."
            );
        }

        this.codigoApartamento = codigoApartamento.trim();
        this.fechaRegistro = fechaRegistro;
        this.proximaLlegada = proximaLlegada;
    }

    public boolean tieneEntradaEn(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException(
                    "La fecha de consulta es obligatoria."
            );
        }

        return proximaLlegada != null
                && proximaLlegada.toLocalDate().equals(fecha);
    }

    public String getCodigoApartamento() {
        return codigoApartamento;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public LocalDateTime getProximaLlegada() {
        return proximaLlegada;
    }
}