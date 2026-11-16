package co.edu.uniquindio.sga.servicios.operacion;

import co.edu.uniquindio.sga.dominio.modelo.TareaPreparacion;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/** Coordina la preparación, la asignación y las rutas. */
public final class GestionPreparacionService {

    private final PreparacionService preparacionService;
    private final TurnoService turnoService;
    private final RutaPreparacionService rutaPreparacionService;

    public GestionPreparacionService(
            PreparacionService preparacionService,
            TurnoService turnoService,
            RutaPreparacionService rutaPreparacionService
    ) {
        this.preparacionService = Objects.requireNonNull(
                preparacionService,
                "El servicio de preparación es obligatorio."
        );

        this.turnoService = Objects.requireNonNull(
                turnoService,
                "El servicio de turnos es obligatorio."
        );

        this.rutaPreparacionService = Objects.requireNonNull(
                rutaPreparacionService,
                "El servicio de rutas es obligatorio."
        );
    }

    public void registrarPendiente(
            String codigoApartamento,
            LocalDateTime proximaLlegada
    ) {
        Objects.requireNonNull(
                codigoApartamento,
                "El código del apartamento es obligatorio."
        );

        TareaPreparacion tarea = new TareaPreparacion(
                codigoApartamento,
                LocalDateTime.now(
                        preparacionService.obtenerReloj()
                ),
                proximaLlegada
        );

        preparacionService.registrarTarea(tarea);
    }

    public String asignarSiguienteTarea() {
        TareaPreparacion tarea =
                preparacionService.consultarSiguiente();

        String codigoApartamento = tarea.getCodigoApartamento();

        if (turnoService.tieneAsignacion(codigoApartamento)) {
            throw new IllegalArgumentException(
                    "El apartamento ya tiene personal asignado: "
                            + codigoApartamento
            );
        }

        return turnoService.asignarTarea(codigoApartamento);
    }

    public List<String> calcularOrdenRecorrido() {
        return rutaPreparacionService.calcularOrdenRecorrido(
                preparacionService.obtenerCodigosPendientes()
        );
    }

    public void iniciarPreparacion(String codigoApartamento) {
        Objects.requireNonNull(
                codigoApartamento,
                "El código del apartamento es obligatorio."
        );

        String codigo = codigoApartamento.trim();

        if (!turnoService.tieneAsignacion(codigo)) {
            throw new IllegalArgumentException(
                    "El apartamento no tiene personal asignado: "
                            + codigo
            );
        }

        preparacionService.iniciarPreparacion(codigo);
    }

    public void finalizarPreparacion(String codigoApartamento) {
        Objects.requireNonNull(
                codigoApartamento,
                "El código del apartamento es obligatorio."
        );

        String codigo = codigoApartamento.trim();

        if (!turnoService.tieneAsignacion(codigo)) {
            throw new IllegalArgumentException(
                    "El apartamento no tiene personal asignado: "
                            + codigo
            );
        }

        preparacionService.finalizarPreparacion(codigo);
        turnoService.liberarAsignacion(codigo);
    }

    public int cantidadPendientes() {
        return preparacionService.cantidadPendientes();
    }
}