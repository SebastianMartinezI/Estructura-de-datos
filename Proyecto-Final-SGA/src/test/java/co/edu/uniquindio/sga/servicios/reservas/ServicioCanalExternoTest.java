package co.edu.uniquindio.sga.servicios.reservas;

import co.edu.uniquindio.sga.dominio.modelo.*;
import co.edu.uniquindio.sga.estructuras.cola.ColaVaciaException;
import co.edu.uniquindio.sga.estructuras.lista.ListaDoblementeEnlazada;
import co.edu.uniquindio.sga.servicios.tarifas.CalendarioTemporadas;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

/** Comprueba FIFO, rechazo de conflictos y comportamiento ante errores. */
class ServicioCanalExternoTest {

    private static final LocalDateTime CREADA = LocalDateTime.of(2026, 11, 16, 10, 0);

    private static ServicioReservas reservas(ServicioDisponibilidad disponibilidad) {
        return new ServicioReservas(disponibilidad,
                new ServicioLiquidacion(new CalendarioTemporadas(), CriterioEdad.POR_NOCHE),
                id -> BigDecimal.ZERO,
                Clock.fixed(Instant.parse("2026-11-16T14:00:00Z"), ZoneOffset.UTC));
    }

    private static Apartamento apartamento(String codigo) {
        return new Apartamento(codigo, "Suite " + codigo, "A", 1, 2, 4, "",
                new TarifasApartamento(new BigDecimal("60000"),
                        new BigDecimal("80000"), new BigDecimal("100000")),
                EstadoOperativoApartamento.PREPARADO, null);
    }

    private static Reserva reserva(String id, Apartamento apartamento,
                                   CanalReserva canal, int entrada, int salida) {
        ListaDoblementeEnlazada<Ocupante> ocupantes = new ListaDoblementeEnlazada<>();
        ocupantes.agregarUltimo(new Ocupante("Ana", LocalDate.of(1990, 1, 1)));
        return new Reserva(id, apartamento,
                new Huesped("1234", "Ana", LocalDate.of(1990, 1, 1),
                        "3001112233", "ana@correo.com", null),
                new Estancia(LocalDate.of(2027, 3, entrada),
                        LocalDate.of(2027, 3, salida)), canal, CREADA, ocupantes);
    }

    private static SolicitudExterna solicitud(String idSolicitud, String idReserva,
                                             Apartamento apartamento, int entrada, int salida) {
        return new SolicitudExterna(idSolicitud,
                reserva(idReserva, apartamento, CanalReserva.EXTERNA, entrada, salida));
    }

    @Test
    void procesaSolicitudesEnOrdenFifo() {
        ServicioDisponibilidad disp = new ServicioDisponibilidad();
        ServicioCanalExterno canal = new ServicioCanalExterno(reservas(disp));
        canal.recibir(solicitud("S-1", "R-1", apartamento("A-101"), 10, 12));
        canal.recibir(solicitud("S-2", "R-2", apartamento("A-102"), 10, 12));
        canal.recibir(solicitud("S-3", "R-3", apartamento("A-103"), 10, 12));

        assertEquals("S-1", canal.consultarSiguiente().idSolicitud());
        assertEquals("S-1", canal.procesarSiguiente().idSolicitud());
        assertEquals("S-2", canal.procesarSiguiente().idSolicitud());
        assertEquals("S-3", canal.procesarSiguiente().idSolicitud());
        assertEquals(0, canal.cantidadPendientes());
        assertEquals(3, canal.cantidadProcesadas());
        assertEquals(3, disp.cantidadReservas());
    }

    @Test
    void guardaAceptadaPendienteSinConfirmarNiCobrar() {
        ServicioDisponibilidad disp = new ServicioDisponibilidad();
        ServicioCanalExterno canal = new ServicioCanalExterno(reservas(disp));
        canal.recibir(solicitud("EXT-1", "R-1", apartamento("A-101"), 10, 12));
        ResultadoSolicitudExterna resultado = canal.procesarSiguiente();
        assertEquals(ResultadoSolicitudExterna.Estado.ACEPTADA_PENDIENTE, resultado.estado());
        assertEquals(EstadoReserva.PENDIENTE, disp.buscarPorId("R-1").getEstado());
        assertEquals(0, canal.cantidadConflictos());
    }

    @Test
    void rechazaCruceConPortalYNoRegistraReservaRechazada() {
        ServicioDisponibilidad disp = new ServicioDisponibilidad();
        ServicioReservas servicio = reservas(disp);
        Apartamento apt = apartamento("A-101");
        servicio.registrarSolicitud(reserva("P-1", apt, CanalReserva.PORTAL, 10, 13));
        ServicioCanalExterno canal = new ServicioCanalExterno(servicio);
        canal.recibir(solicitud("EXT-1", "R-2", apt, 12, 14));

        ResultadoSolicitudExterna resultado = canal.procesarSiguiente();
        assertEquals(ResultadoSolicitudExterna.Estado.RECHAZADA_POR_CONFLICTO, resultado.estado());
        assertTrue(resultado.detalle().contains("Conflicto de canal"));
        assertEquals(1, canal.cantidadConflictos());
        assertEquals(0, canal.cantidadPendientes());
        assertEquals(1, disp.cantidadReservas());
        assertNull(disp.buscarPorId("R-2"));
    }

    @Test
    void dosSolicitudesExternasEnConflictoNoDobleReservan() {
        ServicioDisponibilidad disp = new ServicioDisponibilidad();
        ServicioCanalExterno canal = new ServicioCanalExterno(reservas(disp));
        Apartamento apt = apartamento("A-101");
        canal.recibir(solicitud("E-1", "R-1", apt, 10, 13));
        canal.recibir(solicitud("E-2", "R-2", apt, 11, 14));
        assertEquals(ResultadoSolicitudExterna.Estado.ACEPTADA_PENDIENTE,
                canal.procesarSiguiente().estado());
        assertEquals(ResultadoSolicitudExterna.Estado.RECHAZADA_POR_CONFLICTO,
                canal.procesarSiguiente().estado());
        assertEquals(1, disp.cantidadReservas());
    }

    @Test
    void permiteFechasConsecutivasSinCompartirNoche() {
        ServicioDisponibilidad disp = new ServicioDisponibilidad();
        ServicioCanalExterno canal = new ServicioCanalExterno(reservas(disp));
        Apartamento apt = apartamento("A-101");
        canal.recibir(solicitud("E-1", "R-1", apt, 10, 12));
        canal.recibir(solicitud("E-2", "R-2", apt, 12, 14));
        assertEquals(ResultadoSolicitudExterna.Estado.ACEPTADA_PENDIENTE,
                canal.procesarSiguiente().estado());
        assertEquals(ResultadoSolicitudExterna.Estado.ACEPTADA_PENDIENTE,
                canal.procesarSiguiente().estado());
        assertEquals(0, canal.cantidadConflictos());
        assertEquals(2, disp.cantidadReservas());
    }

    @Test
    void noPermiteIdDeSolicitudExternaRepetidoAunqueEsteProcesado() {
        ServicioCanalExterno canal = new ServicioCanalExterno(reservas(new ServicioDisponibilidad()));
        canal.recibir(solicitud("E-1", "R-1", apartamento("A-101"), 10, 12));
        assertThrows(IllegalArgumentException.class, () -> canal.recibir(
                solicitud("E-1", "R-2", apartamento("A-102"), 10, 12)));
        assertEquals(1, canal.cantidadPendientes());
        canal.procesarSiguiente();
        assertThrows(IllegalArgumentException.class, () -> canal.recibir(
                solicitud("E-1", "R-3", apartamento("A-103"), 10, 12)));
    }

    @Test
    void noAceptaCanalPortalNiReservaYaConfirmada() {
        Apartamento apt = apartamento("A-101");
        assertThrows(IllegalArgumentException.class, () -> new SolicitudExterna("E-1",
                reserva("R-1", apt, CanalReserva.PORTAL, 10, 12)));
        Reserva confirmada = reserva("R-2", apt, CanalReserva.EXTERNA, 13, 15);
        confirmada.cambiarEstado(EstadoReserva.CONFIRMADA);
        assertThrows(IllegalArgumentException.class, () -> new SolicitudExterna("E-2", confirmada));
        assertThrows(IllegalArgumentException.class, () -> new SolicitudExterna(" ",
                reserva("R-3", apt, CanalReserva.EXTERNA, 16, 18)));
    }

    @Test
    void colaVaciaLanzaExcepcionPropia() {
        ServicioCanalExterno canal = new ServicioCanalExterno(reservas(new ServicioDisponibilidad()));
        assertThrows(ColaVaciaException.class, canal::consultarSiguiente);
        assertThrows(ColaVaciaException.class, canal::procesarSiguiente);
    }

    @Test
    void errorDeIdReservaDuplicadoNoDesencolaNiRegistraConflicto() {
        ServicioDisponibilidad disp = new ServicioDisponibilidad();
        ServicioReservas gestor = reservas(disp);
        gestor.registrarSolicitud(reserva("R-1", apartamento("A-101"), CanalReserva.PORTAL, 10, 12));
        ServicioCanalExterno canal = new ServicioCanalExterno(gestor);
        canal.recibir(solicitud("E-1", "R-1", apartamento("A-102"), 13, 15));
        assertThrows(IllegalArgumentException.class, canal::procesarSiguiente);
        assertEquals(1, canal.cantidadPendientes());
        assertEquals(0, canal.cantidadProcesadas());
        assertEquals(0, canal.cantidadConflictos());
    }

    @Test
    void apartamentoFueraDeServicioConservaSolicitudParaRevision() {
        ServicioDisponibilidad disp = new ServicioDisponibilidad();
        ServicioCanalExterno canal = new ServicioCanalExterno(reservas(disp));
        Apartamento apt = apartamento("A-101");
        apt.actualizarEstadoOperativo(EstadoOperativoApartamento.FUERA_DE_SERVICIO, "Mantenimiento");
        canal.recibir(solicitud("E-1", "R-1", apt, 10, 12));
        assertThrows(IllegalStateException.class, canal::procesarSiguiente);
        assertEquals(1, canal.cantidadPendientes());
        assertEquals(0, canal.cantidadConflictos());
    }

    @Test
    void conservaHistorialEnElOrdenProcesadoYCuentaConflictos() {
        ServicioDisponibilidad disp = new ServicioDisponibilidad();
        ServicioCanalExterno canal = new ServicioCanalExterno(reservas(disp));
        Apartamento apt = apartamento("A-101");
        canal.recibir(solicitud("E-1", "R-1", apt, 10, 13));
        canal.recibir(solicitud("E-2", "R-2", apt, 12, 14));
        canal.recibir(solicitud("E-3", "R-3", apartamento("B-201"), 10, 12));
        canal.procesarSiguiente();
        canal.procesarSiguiente();
        canal.procesarSiguiente();
        StringBuilder ids = new StringBuilder();
        for (ResultadoSolicitudExterna resultado : canal.consultarHistorial()) {
            ids.append(resultado.idSolicitud()).append(" ");
        }
        assertEquals("E-1 E-2 E-3 ", ids.toString());
        assertEquals(3, canal.cantidadProcesadas());
        assertEquals(1, canal.cantidadConflictos());
    }

    @Test
    void permiteReservarTrasCancelarReservaPrevia() {
        ServicioDisponibilidad disp = new ServicioDisponibilidad();
        ServicioReservas gestor = reservas(disp);
        Apartamento apt = apartamento("A-101");
        Reserva anterior = reserva("P-1", apt, CanalReserva.PORTAL, 10, 12);
        gestor.registrarSolicitud(anterior);
        anterior.cambiarEstado(EstadoReserva.CANCELADA);
        ServicioCanalExterno canal = new ServicioCanalExterno(gestor);
        canal.recibir(solicitud("E-1", "R-2", apt, 10, 12));
        assertEquals(ResultadoSolicitudExterna.Estado.ACEPTADA_PENDIENTE,
                canal.procesarSiguiente().estado());
        assertEquals(2, disp.cantidadReservas());
    }

    @Test
    void rechazaDependenciasNulas() {
        assertThrows(NullPointerException.class, () -> new ServicioCanalExterno(null));
        ServicioCanalExterno canal = new ServicioCanalExterno(reservas(new ServicioDisponibilidad()));
        assertThrows(NullPointerException.class, () -> canal.recibir(null));
        assertThrows(NullPointerException.class, () -> new SolicitudExterna("E-1", null));
    }
}
