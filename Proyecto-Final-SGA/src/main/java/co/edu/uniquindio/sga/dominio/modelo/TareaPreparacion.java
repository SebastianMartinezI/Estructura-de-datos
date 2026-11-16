package co.edu.uniquindio.sga.dominio.modelo;

import co.edu.uniquindio.sga.excepciones.TransicionEstadoInvalidaException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** Información de una tarea pendiente de preparación. */
public final class TareaPreparacion {

    private final String codigoApartamento;
    private final LocalDateTime fechaRegistro;
    private final LocalDateTime proximaLlegada;

    private LocalDateTime inicioPreparacion;
    private LocalDateTime finPreparacion;

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

    public void iniciarPreparacion(LocalDateTime fechaInicio) {
        if (fechaInicio == null) {
            throw new IllegalArgumentException(
                    "La fecha de inicio es obligatoria."
            );
        }

        if (inicioPreparacion != null) {
            throw new TransicionEstadoInvalidaException(
                    "La preparación ya fue iniciada."
            );
        }

        if (fechaInicio.isBefore(fechaRegistro)) {
            throw new IllegalArgumentException(
                    "El inicio no puede ser anterior al registro de la tarea."
            );
        }

        inicioPreparacion = fechaInicio;
    }

    public void finalizarPreparacion(LocalDateTime fechaFin) {
        if (fechaFin == null) {
            throw new IllegalArgumentException(
                    "La fecha de finalización es obligatoria."
            );
        }

        if (inicioPreparacion == null) {
            throw new TransicionEstadoInvalidaException(
                    "Primero debes iniciar la preparación."
            );
        }

        if (finPreparacion != null) {
            throw new TransicionEstadoInvalidaException(
                    "La preparación ya fue finalizada."
            );
        }

        if (fechaFin.isBefore(inicioPreparacion)) {
            throw new IllegalArgumentException(
                    "La finalización no puede ser anterior al inicio."
            );
        }

        finPreparacion = fechaFin;
    }

    public Duration calcularDuracion() {
        if (finPreparacion == null) {
            throw new TransicionEstadoInvalidaException(
                    "La preparación debe finalizar para calcular su duración."
            );
        }

        return Duration.between(inicioPreparacion, finPreparacion);
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

    public LocalDateTime getInicioPreparacion() {
        return inicioPreparacion;
    }

    public LocalDateTime getFinPreparacion() {
        return finPreparacion;
    }
}