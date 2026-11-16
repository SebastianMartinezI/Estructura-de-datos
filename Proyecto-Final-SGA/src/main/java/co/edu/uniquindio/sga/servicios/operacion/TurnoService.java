package co.edu.uniquindio.sga.servicios.operacion;

import co.edu.uniquindio.sga.estructuras.circular.ListaCircular;
import java.util.HashMap;
import java.util.Map;

import java.util.Objects;

/** Administra la rotación de turnos del personal de limpieza. */
public final class TurnoService {

    private final ListaCircular<String> personal;
    private final Map<String, String> asignaciones;

    public TurnoService() {
        personal = new ListaCircular<>();
        asignaciones = new HashMap<>();
    }

    public void registrarPersonal(String nombre) {
        Objects.requireNonNull(
                nombre,
                "El nombre del personal es obligatorio."
        );

        String nombreNormalizado = nombre.trim();

        if (nombreNormalizado.isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del personal no puede estar vacío."
            );
        }

        if (personal.contiene(nombreNormalizado)) {
            throw new IllegalArgumentException(
                    "El personal ya está registrado: " + nombreNormalizado
            );
        }

        personal.agregar(nombreNormalizado);
    }

    public String consultarTurnoActual() {
        return personal.verActual();
    }

    public String avanzarTurno() {
        return personal.avanzar();
    }

    public boolean estaRegistrado(String nombre) {
        Objects.requireNonNull(
                nombre,
                "El nombre del personal es obligatorio."
        );

        return personal.contiene(nombre.trim());
    }

    public boolean removerPersonal(String nombre) {
        Objects.requireNonNull(
                nombre,
                "El nombre del personal es obligatorio."
        );

        return personal.eliminar(nombre.trim());
    }

    public int cantidadPersonal() {
        return personal.tamano();
    }

    public boolean noHayPersonal() {
        return personal.estaVacia();
    }
    public String asignarTarea(String codigoApartamento) {
        Objects.requireNonNull(
                codigoApartamento,
                "El código del apartamento es obligatorio."
        );

        if (personal.estaVacia()) {
            throw new IllegalStateException(
                    "No hay personal registrado para asignar tareas."
            );
        }

        String codigo = codigoApartamento.trim();

        if (asignaciones.containsKey(codigo)) {
            throw new IllegalArgumentException(
                    "El apartamento ya tiene una asignación: " + codigo
            );
        }

        String personalAsignado = personal.verActual();

        asignaciones.put(codigo, personalAsignado);
        personal.avanzar();

        return personalAsignado;
    }

    public String consultarAsignacion(String codigoApartamento) {
        Objects.requireNonNull(
                codigoApartamento,
                "El código del apartamento es obligatorio."
        );

        String asignacion = asignaciones.get(codigoApartamento.trim());

        if (asignacion == null) {
            throw new IllegalArgumentException(
                    "El apartamento no tiene una asignación registrada: "
                            + codigoApartamento
            );
        }

        return asignacion;
    }

    public boolean tieneAsignacion(String codigoApartamento) {
        Objects.requireNonNull(
                codigoApartamento,
                "El código del apartamento es obligatorio."
        );

        return asignaciones.containsKey(codigoApartamento.trim());
    }

    public String liberarAsignacion(String codigoApartamento) {
        Objects.requireNonNull(
                codigoApartamento,
                "El código del apartamento es obligatorio."
        );

        String codigo = codigoApartamento.trim();
        String personalAsignado = asignaciones.remove(codigo);

        if (personalAsignado == null) {
            throw new IllegalArgumentException(
                    "El apartamento no tiene una asignación registrada: "
                            + codigo
            );
        }

        return personalAsignado;
    }
}