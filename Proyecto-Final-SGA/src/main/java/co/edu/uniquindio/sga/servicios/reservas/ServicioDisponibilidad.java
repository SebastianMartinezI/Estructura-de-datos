package co.edu.uniquindio.sga.servicios.reservas;

import co.edu.uniquindio.sga.dominio.modelo.Apartamento;
import co.edu.uniquindio.sga.dominio.modelo.Estancia;
import co.edu.uniquindio.sga.dominio.modelo.EstadoOperativoApartamento;
import co.edu.uniquindio.sga.dominio.modelo.EstadoReserva;
import co.edu.uniquindio.sga.dominio.modelo.Reserva;
import co.edu.uniquindio.sga.estructuras.arbol.ArbolBinarioBusqueda;
import co.edu.uniquindio.sga.excepciones.ReservaSolapadaException;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Mantiene las reservas por apartamento y verifica las noches disponibles.
 *
 * <p>Usa un BST propio para localizar el índice de cada apartamento, otro BST
 * por apartamento para ordenar sus reservas por entrada/ID, y un tercer BST
 * para impedir ID de reserva duplicados en todo el alojamiento.</p>
 *
 * <p>Las consultas de solapamiento se realizan recursivamente sobre el BST
 * del apartamento solicitado: nunca recorren reservas de otros apartamentos
 * y podan las ramas cuyas entradas son posteriores al rango consultado.
 * Las reservas canceladas, no-show y finalizadas permanecen registradas,
 * aunque ya no bloquean noches según {@link Reserva#bloqueaDisponibilidad()}.</p>
 *
 * <p>Los métodos públicos están sincronizados para impedir que dos canales
 * registren simultáneamente reservas incompatibles en esta instancia.
 * Los modelos Apartamento deben provenir del mismo catálogo compartido.</p>
 *
 * <p>Esta clase NO autoriza cobros, check-in, check-out ni modificaciones de
 * estado. Tales operaciones corresponden a otros servicios.</p>
 */
public final class ServicioDisponibilidad {

    private final ArbolBinarioBusqueda<IndiceApartamento> porApartamento =
            new ArbolBinarioBusqueda<>();
    private final ArbolBinarioBusqueda<IndiceReservaId> porId =
            new ArbolBinarioBusqueda<>();

    /**
     * Consulta disponibilidad por fechas y estado de fuera de servicio.
     * Un apartamento pendiente de preparación puede reservarse para una
     * fecha futura; Cristian comprobará que esté preparado al hacer check-in.
     *
     * @return true si no está fuera de servicio ni tiene noches bloqueadas
     */
    public synchronized boolean estaDisponible(Apartamento apartamento, Estancia estancia) {
        Objects.requireNonNull(apartamento, "El apartamento es obligatorio.");
        Objects.requireNonNull(estancia, "La estancia es obligatoria.");
        return apartamento.getEstadoOperativo() != EstadoOperativoApartamento.FUERA_DE_SERVICIO
                && buscarConflicto(apartamento, estancia) == null;
    }

    /**
     * Devuelve la primera reserva activa que comparte alguna noche con el
     * intervalo [entrada, salida), o null cuando no existe conflicto.
     * La noche de salida no se considera ocupada.
     */
    public synchronized Reserva buscarConflicto(Apartamento apartamento, Estancia estancia) {
        Objects.requireNonNull(apartamento, "El apartamento es obligatorio.");
        Objects.requireNonNull(estancia, "La estancia es obligatoria.");

        IndiceApartamento indice = porApartamento.buscar(
                new IndiceApartamento(apartamento.getCodigo()));
        if (indice == null) {
            return null;
        }
        LocalDate entrada = estancia.getEntrada();
        LocalDate salida = estancia.getSalida();

        return indice.reservas.buscarPrimeroAntesDe(
                salida,
                reserva -> reserva.getEstancia().getEntrada(),
                reserva -> reserva.bloqueaDisponibilidad()
                        && reserva.getEstancia().getSalida().isAfter(entrada));
    }

    /**
     * Registra una reserva pendiente si su ID es único, el apartamento está
     * habilitado y ninguna reserva activa del mismo apartamento se solapa.
     *
     * <p>No modifica el estado de la reserva ni registra pagos. La operación
     * de validación e inserción es atómica dentro de este servicio.</p>
     *
     * @throws ReservaSolapadaException cuando otra reserva bloquea noches
     * @throws IllegalArgumentException cuando el ID ya existe
     * @throws IllegalStateException cuando el apartamento está fuera de servicio
     */
    public synchronized void registrar(Reserva reserva) {
        Objects.requireNonNull(reserva, "La reserva es obligatoria.");
        if (reserva.getEstado() != EstadoReserva.PENDIENTE) {
            throw new IllegalArgumentException("Solo se registran reservas pendientes.");
        }
        if (buscarPorId(reserva.getId()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe una reserva con ID " + reserva.getId());
        }
        if (reserva.getApartamento().getEstadoOperativo()
                == EstadoOperativoApartamento.FUERA_DE_SERVICIO) {
            throw new IllegalStateException("No se puede reservar un apartamento fuera de servicio.");
        }

        Reserva conflicto = buscarConflicto(reserva.getApartamento(), reserva.getEstancia());
        if (conflicto != null) {
            throw new ReservaSolapadaException(
                    "La reserva " + reserva.getId() + " comparte noches con "
                            + conflicto.getId() + " en " + reserva.getApartamento().getCodigo());
        }

        IndiceApartamento indice = porApartamento.buscar(
                new IndiceApartamento(reserva.getApartamento().getCodigo()));
        if (indice == null) {
            indice = new IndiceApartamento(reserva.getApartamento().getCodigo());
            porApartamento.insertar(indice);
        }
        if (!indice.reservas.insertar(reserva)) {
            throw new IllegalStateException("El árbol rechazó la clave de la reserva.");
        }
        porId.insertar(new IndiceReservaId(reserva));
    }

    /** Devuelve una reserva registrada por su ID, aunque esté cancelada. */
    public synchronized Reserva buscarPorId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID de reserva es obligatorio.");
        }
        IndiceReservaId encontrado = porId.buscar(new IndiceReservaId(id.trim()));
        return encontrado == null ? null : encontrado.reserva;
    }

    /** Número de reservas registradas, independientemente de su estado. */
    public synchronized int cantidadReservas() {
        return porId.tamano();
    }

    /** Índice privado ordenado por código de apartamento. */
    private static final class IndiceApartamento implements Comparable<IndiceApartamento> {
        private final String codigo;
        private final ArbolBinarioBusqueda<Reserva> reservas = new ArbolBinarioBusqueda<>();

        private IndiceApartamento(String codigo) {
            this.codigo = codigo;
        }

        @Override
        public int compareTo(IndiceApartamento otro) {
            return codigo.compareTo(otro.codigo);
        }
    }

    /** Índice privado ordenado por ID global de reserva. */
    private static final class IndiceReservaId implements Comparable<IndiceReservaId> {
        private final String id;
        private final Reserva reserva;

        private IndiceReservaId(String id) {
            this.id = id;
            this.reserva = null;
        }

        private IndiceReservaId(Reserva reserva) {
            this.id = reserva.getId();
            this.reserva = reserva;
        }

        @Override
        public int compareTo(IndiceReservaId otro) {
            return id.compareTo(otro.id);
        }
    }
}
