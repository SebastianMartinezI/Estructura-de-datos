package co.edu.uniquindio.sga.servicios.reservas;

import co.edu.uniquindio.sga.dominio.modelo.CanalReserva;
import co.edu.uniquindio.sga.dominio.modelo.EstadoReserva;
import co.edu.uniquindio.sga.dominio.modelo.Reserva;
import co.edu.uniquindio.sga.estructuras.arbol.ArbolBinarioBusqueda;
import co.edu.uniquindio.sga.excepciones.AnticipoInsuficienteException;
import co.edu.uniquindio.sga.excepciones.EstadoReservaInvalidoException;
import co.edu.uniquindio.sga.dominio.modelo.Apartamento;
import co.edu.uniquindio.sga.dominio.modelo.Estancia;
import co.edu.uniquindio.sga.dominio.modelo.Ocupante;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Orquesta la creación de solicitudes y la confirmación de reservas Portal.
 *
 * <p>Se apoya en ServicioDisponibilidad para la validación atómica de noches y
 * en ServicioLiquidacion para la cotización. La acreditación monetaria NO se
 * simula: se consulta mediante ConsultaAnticipos, implementada por el folio.</p>
 *
 * <p>Las cotizaciones se guardan en un BST propio por ID para conservar el
 * precio pactado al crear una solicitud. Es almacenamiento en memoria: la
 * persistencia y la recuperación de instantáneas se implementarán aparte.</p>
 *
 * <p>Las reservas Directa y Externa quedan pendientes en este servicio hasta
 * que el equipo acuerde sus reglas de confirmación. El vencimiento automático
 * y la gestión de folios corresponden a Cristian.</p>
 */
public final class ServicioReservas {

    private final ServicioDisponibilidad disponibilidad;
    private final ServicioLiquidacion liquidacion;
    private final ConsultaAnticipos consultaAnticipos;
    private final Clock reloj;
    private final ArbolBinarioBusqueda<CotizacionPorId> cotizaciones =
            new ArbolBinarioBusqueda<>();

    public ServicioReservas(ServicioDisponibilidad disponibilidad,
                           ServicioLiquidacion liquidacion,
                           ConsultaAnticipos consultaAnticipos,
                           Clock reloj) {
        this.disponibilidad = Objects.requireNonNull(disponibilidad, "Disponibilidad obligatoria");
        this.liquidacion = Objects.requireNonNull(liquidacion, "Liquidación obligatoria");
        this.consultaAnticipos = Objects.requireNonNull(consultaAnticipos, "Consulta de folio obligatoria");
        this.reloj = Objects.requireNonNull(reloj, "Reloj obligatorio");
    }

    /**
     * Cotiza una estancia sin registrar una reserva,
     * bloquear noches ni almacenar una cotización.
     */
    public BigDecimal cotizarPrevia(
            Apartamento apartamento,
            Estancia estancia,
            Iterable<Ocupante> ocupantes) {

        return liquidacion.cotizarTotal(
                apartamento,
                estancia,
                ocupantes
        );
    }

    /**
     * Registra una reserva pendiente y conserva su cotización inicial.
     * No confirma, no recibe dinero ni modifica el estado del apartamento.
     *
     * @throws co.edu.uniquindio.sga.excepciones.ReservaSolapadaException ante noches ocupadas
     */
    public synchronized CotizacionAnticipo registrarSolicitud(Reserva reserva) {
        Objects.requireNonNull(reserva, "La reserva es obligatoria");
        if (reserva.getEstado() != EstadoReserva.PENDIENTE) {
            throw new EstadoReservaInvalidoException("Solo pueden registrarse reservas pendientes");
        }
        CotizacionAnticipo presupuesto = new CotizacionAnticipo(liquidacion.cotizar(reserva));
        // Primero se verifica e inserta en el índice oficial. Ante conflicto,
        // la instantánea no se almacena ni se altera ninguna reserva existente.
        disponibilidad.registrar(reserva);
        if (!cotizaciones.insertar(new CotizacionPorId(reserva.getId(), presupuesto))) {
            throw new IllegalStateException("Cotización duplicada para la reserva " + reserva.getId());
        }
        return presupuesto;
    }

    /** Consulta la cotización inmutable tomada al registrar la solicitud. */
    public synchronized CotizacionAnticipo consultarCotizacion(String idReserva) {
        if (idReserva == null || idReserva.isBlank()) {
            throw new IllegalArgumentException("El ID de reserva es obligatorio");
        }
        CotizacionPorId encontrada = cotizaciones.buscar(new CotizacionPorId(idReserva.trim(), null));
        if (encontrada == null) {
            throw new IllegalArgumentException("No existe cotización registrada para " + idReserva);
        }
        return encontrada.presupuesto;
    }

    /**
     * Confirma una reserva del canal Portal solamente cuando:
     * está pendiente, aún no ha vencido y su folio acredita al menos 30%.
     *
     * <p>No cobra ni registra pagos; consulta lo que Cristian tenga en el
     * folio. Nunca confirma si la consulta falla o devuelve un importe inválido.</p>
     *
     * @return la misma reserva con estado CONFIRMADA
     */
    public synchronized Reserva confirmarPortal(String idReserva) {
        CotizacionAnticipo presupuesto = consultarCotizacion(idReserva);
        Reserva reserva = disponibilidad.buscarPorId(presupuesto.getIdReserva());
        if (reserva == null) {
            throw new IllegalStateException("Falta la reserva en el índice de disponibilidad");
        }
        if (reserva.getCanal() != CanalReserva.PORTAL) {
            throw new IllegalArgumentException("Solo se confirma el canal Portal con esta operación");
        }
        if (reserva.getEstado() != EstadoReserva.PENDIENTE) {
            throw new EstadoReservaInvalidoException("La reserva no está pendiente");
        }
        if (reserva.estaPendienteVencida(LocalDateTime.now(reloj))) {
            throw new EstadoReservaInvalidoException(
                    "La reserva alcanzó las 24 horas y requiere proceso de vencimiento");
        }
        BigDecimal acreditado = Objects.requireNonNull(
                consultaAnticipos.totalAcreditado(reserva.getId()),
                "El folio no puede devolver un monto nulo");
        if (acreditado.signum() < 0) {
            throw new IllegalStateException("El folio devolvió un anticipo negativo");
        }
        if (acreditado.compareTo(presupuesto.getAnticipoMinimo()) < 0) {
            throw new AnticipoInsuficienteException(
                    "Anticipo insuficiente para confirmar " + reserva.getId());
        }
        reserva.cambiarEstado(EstadoReserva.CONFIRMADA);
        return reserva;
    }

    private static final class CotizacionPorId implements Comparable<CotizacionPorId> {
        private final String id;
        private final CotizacionAnticipo presupuesto;

        private CotizacionPorId(String id, CotizacionAnticipo presupuesto) {
            this.id = id;
            this.presupuesto = presupuesto;
        }

        @Override
        public int compareTo(CotizacionPorId otro) {
            return id.compareTo(otro.id);
        }
    }
}
