package co.edu.uniquindio.sga.servicios.operacion;

import co.edu.uniquindio.sga.dominio.modelo.TareaPreparacion;
import co.edu.uniquindio.sga.estructuras.cola_prioridad.ColaPrioridad;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Objects;

/** Organiza las tareas pendientes de preparación. */
public final class PreparacionService {

    private final Clock reloj;

    private LocalDate fechaOrden;
    private ColaPrioridad<TareaPreparacion> pendientes;

    public PreparacionService(Clock reloj) {
        this.reloj = Objects.requireNonNull(
                reloj,
                "El reloj es obligatorio."
        );

        fechaOrden = LocalDate.now(reloj);
        pendientes = crearCola(fechaOrden);
    }

    public void registrarTarea(TareaPreparacion tarea) {
        Objects.requireNonNull(
                tarea,
                "La tarea de preparación es obligatoria."
        );

        actualizarOrden();

        for (TareaPreparacion pendiente : pendientes) {
            if (pendiente.getCodigoApartamento()
                    .equals(tarea.getCodigoApartamento())) {

                throw new IllegalArgumentException(
                        "El apartamento ya tiene una tarea pendiente."
                );
            }
        }

        pendientes.encolar(tarea);
    }

    public TareaPreparacion consultarSiguiente() {
        actualizarOrden();
        return pendientes.verFrente();
    }

    public TareaPreparacion retirarSiguiente() {
        actualizarOrden();
        return pendientes.desencolar();
    }

    public int cantidadPendientes() {
        return pendientes.tamano();
    }

    private ColaPrioridad<TareaPreparacion> crearCola(
            LocalDate fecha
    ) {
        Comparator<TareaPreparacion> comparador =
                Comparator
                        .comparingInt(
                                (TareaPreparacion tarea) ->
                                        tarea.tieneEntradaEn(fecha) ? 0 : 1
                        )
                        .thenComparing(
                                tarea -> tarea.tieneEntradaEn(fecha)
                                        ? tarea.getProximaLlegada()
                                        .toLocalTime()
                                        : LocalTime.MAX
                        )
                        .thenComparing(
                                TareaPreparacion::getFechaRegistro
                        )
                        .thenComparing(
                                TareaPreparacion::getCodigoApartamento
                        );

        return new ColaPrioridad<>(comparador);
    }

    private void actualizarOrden() {
        LocalDate hoy = LocalDate.now(reloj);

        if (hoy.equals(fechaOrden)) {
            return;
        }

        ColaPrioridad<TareaPreparacion> reorganizada =
                crearCola(hoy);

        for (TareaPreparacion tarea : pendientes) {
            reorganizada.encolar(tarea);
        }

        pendientes = reorganizada;
        fechaOrden = hoy;
    }
}