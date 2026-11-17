package co.edu.uniquindio.sga.servicios.operacion;

import co.edu.uniquindio.sga.dominio.modelo.EstadoOperativo;
import co.edu.uniquindio.sga.excepciones.TransicionEstadoInvalidaException;

/** Valida los cambios del ciclo operativo de un apartamento. */
public final class EstadoOperativoService {

    public EstadoOperativo estadoTrasIngreso(EstadoOperativo actual) {
        validarEstado(
                actual,
                EstadoOperativo.PREPARADO,
                "Solo se puede registrar el ingreso "
                        + "si el apartamento está Preparado."
        );

        return EstadoOperativo.OCUPADO;
    }

    public EstadoOperativo estadoTrasSalida(EstadoOperativo actual) {
        validarEstado(
                actual,
                EstadoOperativo.OCUPADO,
                "Solo se puede registrar la salida "
                        + "si el apartamento está Ocupado."
        );

        return EstadoOperativo.PENDIENTE_PREPARACION;
    }

    public EstadoOperativo estadoAlIniciarPreparacion(
            EstadoOperativo actual
    ) {
        validarEstado(
                actual,
                EstadoOperativo.PENDIENTE_PREPARACION,
                "Solo se puede iniciar la preparación "
                        + "si el apartamento está Pendiente de preparación."
        );

        return EstadoOperativo.EN_PREPARACION;
    }

    public EstadoOperativo estadoAlFinalizarPreparacion(
            EstadoOperativo actual
    ) {
        validarEstado(
                actual,
                EstadoOperativo.EN_PREPARACION,
                "Solo se puede finalizar la preparación "
                        + "si el apartamento está En preparación."
        );

        return EstadoOperativo.PREPARADO;
    }

    private void validarEstado(
            EstadoOperativo actual,
            EstadoOperativo esperado,
            String mensaje
    ) {
        if (actual == null) {
            throw new IllegalArgumentException(
                    "El estado operativo es obligatorio."
            );
        }

        if (actual != esperado) {
            throw new TransicionEstadoInvalidaException(
                    mensaje + " Estado actual: "
                            + actual.getDescripcion()
            );
        }
    }
}