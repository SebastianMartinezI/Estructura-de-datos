package co.edu.uniquindio.sga.servicios.operacion;

import co.edu.uniquindio.sga.dominio.modelo.TareaPreparacion;
import co.edu.uniquindio.sga.estructuras.cola_prioridad.ColaPrioridad;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
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

        if (buscarTarea(tarea.getCodigoApartamento()) != null) {
            throw new IllegalArgumentException(
                    "El apartamento ya tiene una tarea pendiente."
            );
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

    public void iniciarPreparacion(String codigoApartamento) {
        Objects.requireNonNull(
                codigoApartamento,
                "El código del apartamento es obligatorio."
        );

        TareaPreparacion tarea = buscarTarea(codigoApartamento);

        if (tarea == null) {
            throw new IllegalArgumentException(
                    "No existe una tarea pendiente para ese apartamento."
            );
        }

        tarea.iniciarPreparacion(LocalDateTime.now(reloj));
    }

    public void finalizarPreparacion(String codigoApartamento) {
        Objects.requireNonNull(
                codigoApartamento,
                "El código del apartamento es obligatorio."
        );

        TareaPreparacion tarea = buscarTarea(codigoApartamento);

        if (tarea == null) {
            throw new IllegalArgumentException(
                    "No existe una tarea para ese apartamento."
            );
        }

        tarea.finalizarPreparacion(LocalDateTime.now(reloj));
    }

    private TareaPreparacion buscarTarea(String codigoApartamento) {
        String codigo = codigoApartamento.trim();

        for (TareaPreparacion tarea : pendientes) {
            if (tarea.getCodigoApartamento().equals(codigo)) {
                return tarea;
            }
        }

        return null;
    }

    private ColaPrioridad<TareaPreparacion> crearCola(LocalDate fecha) {
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
    public List<String> obtenerCodigosPendientes() {
        actualizarOrden();

        List<String> codigos = new ArrayList<>();

        for (TareaPreparacion tarea : pendientes) {
            codigos.add(tarea.getCodigoApartamento());
        }

        return codigos;
    }
}