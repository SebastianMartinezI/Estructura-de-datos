package co.edu.uniquindio.sga.servicios.reservas;

import co.edu.uniquindio.sga.estructuras.arbol.ArbolBinarioBusqueda;
import co.edu.uniquindio.sga.estructuras.cola.Cola;
import co.edu.uniquindio.sga.estructuras.lista.ListaDoblementeEnlazada;
import co.edu.uniquindio.sga.excepciones.ReservaSolapadaException;

import java.util.Objects;

/**
 * Recibe y procesa solicitudes de plataformas externas en estricto orden FIFO.
 *
 * <p>La cola, el historial y el índice de IDs utilizan estructuras propias.
 * La creación y liquidación de las reservas se delega en {@link ServicioReservas},
 * que ya aplica {@link ServicioDisponibilidad}. Solo los solapamientos son
 * rechazos definitivos de canal. Ante otras fallas, la solicitud permanece
 * en el frente para revisión y posible reintento.</p>
 *
 * <p>Este servicio es de memoria y no realiza llamadas a una plataforma real.
 * Tampoco confirma reservas, registra pagos ni modifica folios de Cristian.
 * Los métodos se sincronizan para preservar el orden durante la ejecución.</p>
 */
public final class ServicioCanalExterno {

    private final ServicioReservas servicioReservas;
    private final Cola<SolicitudExterna> pendientes = new Cola<>();
    private final ListaDoblementeEnlazada<ResultadoSolicitudExterna> historial =
            new ListaDoblementeEnlazada<>();
    private final ArbolBinarioBusqueda<ClaveSolicitud> identificadores =
            new ArbolBinarioBusqueda<>();
    private int conflictos;

    public ServicioCanalExterno(ServicioReservas servicioReservas) {
        this.servicioReservas = Objects.requireNonNull(
                servicioReservas, "El servicio de reservas es obligatorio");
    }

    /**
     * Agrega una solicitud al final de la cola.
     * Rechaza identificadores de plataforma repetidos, incluso si la solicitud
     * anterior ya fue procesada.
     *
     * @throws IllegalArgumentException si ya se recibió ese identificador
     */
    public synchronized void recibir(SolicitudExterna solicitud) {
        Objects.requireNonNull(solicitud, "La solicitud externa es obligatoria");
        ClaveSolicitud clave = new ClaveSolicitud(solicitud.idSolicitud());
        if (!identificadores.insertar(clave)) {
            throw new IllegalArgumentException(
                    "Solicitud externa duplicada: " + solicitud.idSolicitud());
        }
        pendientes.encolar(solicitud);
    }

    /**
     * Procesa únicamente la solicitud más antigua.
     * Si no hay conflicto, registra la reserva como PENDIENTE y la cotiza;
     * si hay solapamiento, registra el conflicto y rechaza esa solicitud.
     *
     * <p>Si se produce otra excepción (por ejemplo, una ID de reserva ya usada
     * o falta de configuración de tarifas), se propaga sin retirar la solicitud
     * ni registrar un resultado; el operador deberá corregir la causa.</p>
     *
     * @return resultado de aceptar la solicitud pendiente o rechazarla
     * @throws co.edu.uniquindio.sga.estructuras.cola.ColaVaciaException si no hay solicitudes
     */
    public synchronized ResultadoSolicitudExterna procesarSiguiente() {
        SolicitudExterna solicitud = pendientes.consultarFrente();
        ResultadoSolicitudExterna resultado;

        try {
            servicioReservas.registrarSolicitud(solicitud.reserva());
            resultado = new ResultadoSolicitudExterna(
                    solicitud.idSolicitud(), solicitud.reserva().getId(),
                    ResultadoSolicitudExterna.Estado.ACEPTADA_PENDIENTE,
                    "Reserva externa registrada en estado PENDIENTE");
        } catch (ReservaSolapadaException conflicto) {
            resultado = new ResultadoSolicitudExterna(
                    solicitud.idSolicitud(), solicitud.reserva().getId(),
                    ResultadoSolicitudExterna.Estado.RECHAZADA_POR_CONFLICTO,
                    "Conflicto de canal: " + conflicto.getMessage());
        }

        pendientes.desencolar();
        historial.agregarUltimo(resultado);
        if (resultado.estado() == ResultadoSolicitudExterna.Estado.RECHAZADA_POR_CONFLICTO) {
            conflictos++;
        }
        return resultado;
    }

    /** Consulta la solicitud que se procesará a continuación, sin retirarla. */
    public synchronized SolicitudExterna consultarSiguiente() {
        return pendientes.consultarFrente();
    }

    public synchronized int cantidadPendientes() {
        return pendientes.tamano();
    }

    public synchronized int cantidadProcesadas() {
        return historial.tamano();
    }

    public synchronized int cantidadConflictos() {
        return conflictos;
    }

    /**
     * Vista recorrible de resultados desde el más antiguo al más reciente.
     * No expone la lista mutable propia.
     */
    public synchronized Iterable<ResultadoSolicitudExterna> consultarHistorial() {
        return historial::iterator;
    }

    /** Clave comparable para impedir mensajes duplicados sin usar HashSet. */
    private record ClaveSolicitud(String id) implements Comparable<ClaveSolicitud> {
        @Override
        public int compareTo(ClaveSolicitud otra) {
            return id.compareTo(otra.id);
        }
    }
}
