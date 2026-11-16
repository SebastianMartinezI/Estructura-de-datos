package co.edu.uniquindio.sga.dominio.modelo;

import co.edu.uniquindio.sga.estructuras.lista.ListaDoblementeEnlazada;
import co.edu.uniquindio.sga.excepciones.CapacidadExcedidaException;
import co.edu.uniquindio.sga.excepciones.EstadoReservaInvalidoException;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Reserva de un apartamento completo para un grupo durante una estancia.
 *
 * <p>Su identidad es el ID de reserva y su orden natural, la fecha de entrada
 * y luego el ID. Este desempate permite guardar varias reservas con la misma
 * fecha de entrada en un BST genérico.</p>
 *
 * <p>La lista de ocupantes representa a TODAS las personas que se hospedan:
 * incluye al titular únicamente si también se aloja. No se añade de forma
 * automática, porque esa política se decide explícitamente en el servicio.
 * El precio, pagos, anticipo, conflictos de calendario y check-in se gestionan
 * fuera de esta entidad.</p>
 */
public final class Reserva implements Comparable<Reserva> {

    private final String id;
    private final Apartamento apartamento;
    private final Huesped titular;
    private final Estancia estancia;
    private final CanalReserva canal;
    private final LocalDateTime fechaCreacion;
    private final ListaDoblementeEnlazada<Ocupante> ocupantes;
    private EstadoReserva estado;

    /**
     * Crea una reserva pendiente con una copia defensiva de sus ocupantes.
     *
     * @param id identificador único de reserva, obligatorio
     * @param apartamento apartamento reservado, obligatorio
     * @param titular huésped responsable de la reserva, obligatorio
     * @param estancia fechas [entrada, salida), obligatorias
     * @param canal canal por el que se solicita la reserva
     * @param fechaCreacion fecha y hora de registro para gestionar vencimientos
     * @param ocupantes personas que efectivamente se alojarán; mínimo una
     * @throws CapacidadExcedidaException si hay más personas que cupos
     * @throws IllegalArgumentException si falta algún dato obligatorio
     */
    public Reserva(String id, Apartamento apartamento, Huesped titular,
                   Estancia estancia, CanalReserva canal,
                   LocalDateTime fechaCreacion,
                   ListaDoblementeEnlazada<Ocupante> ocupantes) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El identificador de la reserva es obligatorio.");
        }
        this.id = id.trim();
        this.apartamento = obligatorio(apartamento, "apartamento");
        this.titular = obligatorio(titular, "titular");
        this.estancia = obligatorio(estancia, "estancia");
        this.canal = obligatorio(canal, "canal");
        this.fechaCreacion = obligatorio(fechaCreacion, "fecha de creación");
        obligatorio(ocupantes, "ocupantes");

        if (ocupantes.estaVacia()) {
            throw new IllegalArgumentException("La reserva requiere al menos un ocupante.");
        }
        if (ocupantes.tamano() > apartamento.getCapacidadMaxima()) {
            throw new CapacidadExcedidaException(
                    "La reserva excede la capacidad de " + apartamento.getCapacidadMaxima() + " personas.");
        }

        this.ocupantes = new ListaDoblementeEnlazada<>();
        for (Ocupante ocupante : ocupantes) {
            if (ocupante.getFechaNacimiento().isAfter(estancia.getEntrada())) {
                throw new IllegalArgumentException(
                        "Ningún ocupante puede nacer después de la fecha de entrada.");
            }
            this.ocupantes.agregarUltimo(ocupante);
        }
        this.estado = EstadoReserva.PENDIENTE;
    }

    private static <T> T obligatorio(T valor, String campo) {
        if (valor == null) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
        return valor;
    }

    /**
     * Valida y registra una transición de estado.
     *
     * <p>Solo se aplica el flujo estructural del proyecto. Los requisitos de
     * anticipo, preparación, presencia del huésped, permisos y vencimiento se
     * comprueban en servicios de reserva u operación.</p>
     *
     * @throws EstadoReservaInvalidoException cuando no se permite la transición
     */
    public void cambiarEstado(EstadoReserva nuevoEstado) {
        obligatorio(nuevoEstado, "nuevo estado");
        boolean permitido = switch (estado) {
            case PENDIENTE -> nuevoEstado == EstadoReserva.CONFIRMADA
                    || nuevoEstado == EstadoReserva.CANCELADA
                    || nuevoEstado == EstadoReserva.NO_SHOW;
            case CONFIRMADA -> nuevoEstado == EstadoReserva.EN_CURSO
                    || nuevoEstado == EstadoReserva.CANCELADA
                    || nuevoEstado == EstadoReserva.NO_SHOW;
            case EN_CURSO -> nuevoEstado == EstadoReserva.FINALIZADA;
            case FINALIZADA, CANCELADA, NO_SHOW -> false;
        };
        if (!permitido) {
            throw new EstadoReservaInvalidoException(
                    "Transición de reserva no permitida: " + estado + " -> " + nuevoEstado);
        }
        this.estado = nuevoEstado;
    }

    /**
     * Las reservas pendientes, confirmadas y en curso bloquean las noches.
     * Una pendiente vencida solo deja de bloquear tras ser cancelada por el
     * servicio responsable del vencimiento.
     */
    public boolean bloqueaDisponibilidad() {
        return estado == EstadoReserva.PENDIENTE
                || estado == EstadoReserva.CONFIRMADA
                || estado == EstadoReserva.EN_CURSO;
    }

    /**
     * Comprueba si ambas reservas son del mismo apartamento y comparten alguna
     * noche, sin tener en cuenta sus estados. El recorrido recursivo del árbol
     * de reservas se implementará más adelante en el servicio de disponibilidad.
     */
    public boolean comparteNochesCon(Reserva otra) {
        obligatorio(otra, "otra reserva");
        return apartamento.equals(otra.apartamento)
                && estancia.getEntrada().isBefore(otra.estancia.getSalida())
                && otra.estancia.getEntrada().isBefore(estancia.getSalida());
    }

    /** Fecha límite de las reservas pendientes: 24 horas desde su creación. */
    public LocalDateTime getFechaVencimiento() {
        return fechaCreacion.plusHours(24);
    }

    /** Determina si una reserva aún pendiente ya alcanzó su vencimiento. */
    public boolean estaPendienteVencida(LocalDateTime referencia) {
        obligatorio(referencia, "fecha de referencia");
        return estado == EstadoReserva.PENDIENTE
                && !referencia.isBefore(getFechaVencimiento());
    }

    /**
     * Retorna una vista recorrible de los ocupantes, sin exponer la lista
     * mutable utilizada internamente.
     */
    public Iterable<Ocupante> getOcupantes() {
        return ocupantes::iterator;
    }

    public int getCantidadOcupantes() {
        return ocupantes.tamano();
    }

    @Override
    public int compareTo(Reserva otra) {
        Objects.requireNonNull(otra, "No se puede comparar con una reserva nula.");
        int comparacion = estancia.getEntrada().compareTo(otra.estancia.getEntrada());
        return comparacion != 0 ? comparacion : id.compareTo(otra.id);
    }

    @Override
    public boolean equals(Object objeto) {
        return this == objeto || (objeto instanceof Reserva otra && id.equals(otra.id));
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id + " - " + apartamento.getCodigo() + " - " + estado;
    }

    public String getId() { return id; }
    public Apartamento getApartamento() { return apartamento; }
    public Huesped getTitular() { return titular; }
    public Estancia getEstancia() { return estancia; }
    public CanalReserva getCanal() { return canal; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public EstadoReserva getEstado() { return estado; }
}
