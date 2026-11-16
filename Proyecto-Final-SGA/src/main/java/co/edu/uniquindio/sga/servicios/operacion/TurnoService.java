package co.edu.uniquindio.sga.servicios.operacion;

import co.edu.uniquindio.sga.dominio.modelo.Recepcionista;
import co.edu.uniquindio.sga.estructuras.circular.ListaCircular;
import co.edu.uniquindio.sga.excepciones.EstructuraVaciaException;

import java.util.Iterator;
import java.util.Objects;

public final class TurnoService implements Iterable<Recepcionista> {

    private final ListaCircular<Recepcionista> rotacion =
            new ListaCircular<>();

    public void agregarRecepcionista(Recepcionista recepcionista) {
        Objects.requireNonNull(
                recepcionista,
                "El recepcionista no puede ser nulo"
        );

        if (rotacion.contiene(recepcionista)) {
            throw new IllegalArgumentException(
                    "El recepcionista ya está en la rotación"
            );
        }

        if (!recepcionista.isActivo()) {
            throw new IllegalArgumentException(
                    "No puedes agregar un recepcionista inactivo"
            );
        }

        rotacion.agregar(recepcionista);
    }

    public Recepcionista consultarTurnoActual() {
        validarDisponibilidad();

        for (int i = 0; i < rotacion.tamano(); i++) {
            Recepcionista actual = rotacion.verActual();

            if (actual.isActivo()) {
                return actual;
            }

            rotacion.avanzar();
        }

        throw new IllegalStateException(
                "No hay recepcionistas activos en la rotación"
        );
    }

    public Recepcionista avanzarTurno() {
        // Normaliza el turno por si el actual fue desactivado.
        consultarTurnoActual();

        rotacion.avanzar();

        return consultarTurnoActual();
    }

    public boolean retirarDeRotacion(String documento) {
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException(
                    "El documento es obligatorio"
            );
        }

        Recepcionista encontrado = null;
        String documentoBuscado = documento.trim();

        for (Recepcionista recepcionista : rotacion) {
            if (recepcionista.getDocumento().equals(documentoBuscado)) {
                encontrado = recepcionista;
                break;
            }
        }

        // Se elimina después de terminar el recorrido.
        return encontrado != null && rotacion.eliminar(encontrado);
    }

    public int cantidadEnRotacion() {
        return rotacion.tamano();
    }

    public int cantidadActivos() {
        int cantidad = 0;

        for (Recepcionista recepcionista : rotacion) {
            if (recepcionista.isActivo()) {
                cantidad++;
            }
        }

        return cantidad;
    }

    private void validarDisponibilidad() {
        if (rotacion.estaVacia()) {
            throw new EstructuraVaciaException(
                    "No hay recepcionistas registrados en la rotación"
            );
        }
    }

    @Override
    public Iterator<Recepcionista> iterator() {
        return rotacion.iterator();
    }
}