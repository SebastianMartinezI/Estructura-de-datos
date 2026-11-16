package co.edu.uniquindio.sga.servicios.reservas;

import co.edu.uniquindio.sga.dominio.modelo.*;
import co.edu.uniquindio.sga.estructuras.lista.ListaDoblementeEnlazada;
import co.edu.uniquindio.sga.excepciones.AnticipoInsuficienteException;
import co.edu.uniquindio.sga.excepciones.EstadoReservaInvalidoException;
import co.edu.uniquindio.sga.excepciones.ReservaSolapadaException;
import co.edu.uniquindio.sga.servicios.tarifas.CalendarioTemporadas;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas de integración sin acceso a servicios reales de pago. */
class ServicioReservasTest {

    private static final LocalDateTime CREADA = LocalDateTime.of(2026, 11, 16, 10, 0);
    private static final Clock A_TIEMPO = reloj("2026-11-16T14:00:00Z");

    private static Clock reloj(String instante) {
        return Clock.fixed(Instant.parse(instante), ZoneOffset.UTC);
    }

    private static ServicioReservas servicio(ServicioDisponibilidad disp,
                                             ConsultaAnticipos consulta,
                                             Clock clock) {
        return new ServicioReservas(disp,
                new ServicioLiquidacion(new CalendarioTemporadas(), CriterioEdad.POR_NOCHE),
                consulta, clock);
    }

    private static Apartamento apartamento(String codigo) {
        return new Apartamento(codigo, "Suite", "A", 1, 2, 4, "",
                new TarifasApartamento(new BigDecimal("60000"),
                        new BigDecimal("80000"), new BigDecimal("100000")),
                EstadoOperativoApartamento.PREPARADO, null);
    }

    private static Reserva reserva(String id, Apartamento apt, CanalReserva canal,
                                  int diaEntrada, int diaSalida) {
        ListaDoblementeEnlazada<Ocupante> personas = new ListaDoblementeEnlazada<>();
        personas.agregarUltimo(new Ocupante("Ana", LocalDate.of(1985, 1, 1)));
        return new Reserva(id, apt,
                new Huesped("1234", "Ana", LocalDate.of(1985, 1, 1),
                        "3001112233", "ana@correo.com", null),
                new Estancia(LocalDate.of(2027, 3, diaEntrada),
                        LocalDate.of(2027, 3, diaSalida)), canal, CREADA, personas);
    }

    @Test
    void registraPendienteYCalculaAnticipoExactoDelTreintaPorCiento() {
        ServicioDisponibilidad disp = new ServicioDisponibilidad();
        ServicioReservas servicio = servicio(disp, id -> BigDecimal.ZERO, A_TIEMPO);
        Reserva r = reserva("R-1", apartamento("A-101"), CanalReserva.PORTAL, 10, 12);
        CotizacionAnticipo cotizacion = servicio.registrarSolicitud(r);
        assertEquals(EstadoReserva.PENDIENTE, r.getEstado());
        assertEquals(1, disp.cantidadReservas());
        assertEquals(0, new BigDecimal("120000").compareTo(cotizacion.getTotal()));
        assertEquals(0, new BigDecimal("36000").compareTo(cotizacion.getAnticipoMinimo()));
        assertEquals(0, new BigDecimal("84000").compareTo(cotizacion.getSaldoTrasAnticipoMinimo()));
        assertEquals("R-1", servicio.consultarCotizacion("R-1").getIdReserva());
    }

    @Test
    void anticipoInsuficienteNoConfirma() {
        ServicioReservas servicio = servicio(new ServicioDisponibilidad(),
                id -> new BigDecimal("35999"), A_TIEMPO);
        Reserva r = reserva("R-1", apartamento("A-101"), CanalReserva.PORTAL, 10, 12);
        servicio.registrarSolicitud(r);
        assertThrows(AnticipoInsuficienteException.class, () -> servicio.confirmarPortal("R-1"));
        assertEquals(EstadoReserva.PENDIENTE, r.getEstado());
    }

    @Test
    void confirmaConAnticipoMinimoAcreditado() {
        ServicioReservas servicio = servicio(new ServicioDisponibilidad(),
                id -> new BigDecimal("36000"), A_TIEMPO);
        Reserva r = reserva("R-1", apartamento("A-101"), CanalReserva.PORTAL, 10, 12);
        servicio.registrarSolicitud(r);
        assertEquals(r, servicio.confirmarPortal("R-1"));
        assertEquals(EstadoReserva.CONFIRMADA, r.getEstado());
    }

    @Test
    void confirmaConAbonoSuperiorAlMinimo() {
        ServicioReservas servicio = servicio(new ServicioDisponibilidad(),
                id -> new BigDecimal("120000"), A_TIEMPO);
        Reserva r = reserva("R-1", apartamento("A-101"), CanalReserva.PORTAL, 10, 12);
        servicio.registrarSolicitud(r);
        servicio.confirmarPortal("R-1");
        assertEquals(EstadoReserva.CONFIRMADA, r.getEstado());
    }

    @Test
    void nuncaConfirmaSiFolioDevuelveNuloONegativo() {
        ServicioReservas nulo = servicio(new ServicioDisponibilidad(), id -> null, A_TIEMPO);
        Reserva a = reserva("R-1", apartamento("A-101"), CanalReserva.PORTAL, 10, 12);
        nulo.registrarSolicitud(a);
        assertThrows(NullPointerException.class, () -> nulo.confirmarPortal("R-1"));
        assertEquals(EstadoReserva.PENDIENTE, a.getEstado());
        ServicioReservas negativo = servicio(new ServicioDisponibilidad(),
                id -> BigDecimal.ONE.negate(), A_TIEMPO);
        Reserva b = reserva("R-2", apartamento("A-102"), CanalReserva.PORTAL, 10, 12);
        negativo.registrarSolicitud(b);
        assertThrows(IllegalStateException.class, () -> negativo.confirmarPortal("R-2"));
        assertEquals(EstadoReserva.PENDIENTE, b.getEstado());
    }

    @Test
    void noPermiteConfirmarReservaVencida() {
        ServicioReservas servicio = servicio(new ServicioDisponibilidad(),
                id -> new BigDecimal("36000"), reloj("2026-11-17T10:00:00Z"));
        Reserva r = reserva("R-1", apartamento("A-101"), CanalReserva.PORTAL, 10, 12);
        servicio.registrarSolicitud(r);
        assertThrows(EstadoReservaInvalidoException.class, () -> servicio.confirmarPortal("R-1"));
        assertEquals(EstadoReserva.PENDIENTE, r.getEstado());
    }

    @Test
    void noConfirmaCanalesSinPoliticaDefinida() {
        for (CanalReserva canal : new CanalReserva[]{CanalReserva.DIRECTA, CanalReserva.EXTERNA}) {
            ServicioReservas servicio = servicio(new ServicioDisponibilidad(),
                    id -> new BigDecimal("100000"), A_TIEMPO);
            Reserva r = reserva("R-" + canal, apartamento("A-101"), canal, 10, 12);
            servicio.registrarSolicitud(r);
            assertThrows(IllegalArgumentException.class, () -> servicio.confirmarPortal(r.getId()));
            assertEquals(EstadoReserva.PENDIENTE, r.getEstado());
        }
    }

    @Test
    void noConfirmaDosVeces() {
        ServicioReservas servicio = servicio(new ServicioDisponibilidad(),
                id -> new BigDecimal("36000"), A_TIEMPO);
        Reserva r = reserva("R-1", apartamento("A-101"), CanalReserva.PORTAL, 10, 12);
        servicio.registrarSolicitud(r);
        servicio.confirmarPortal("R-1");
        assertThrows(EstadoReservaInvalidoException.class, () -> servicio.confirmarPortal("R-1"));
    }

    @Test
    void rechazaReservaSolapadaYSoloGuardaLaPrimeraCotizacion() {
        ServicioDisponibilidad disp = new ServicioDisponibilidad();
        ServicioReservas servicio = servicio(disp, id -> BigDecimal.ZERO, A_TIEMPO);
        Apartamento apt = apartamento("A-101");
        servicio.registrarSolicitud(reserva("R-1", apt, CanalReserva.PORTAL, 10, 12));
        assertThrows(ReservaSolapadaException.class, () -> servicio.registrarSolicitud(
                reserva("R-2", apt, CanalReserva.DIRECTA, 11, 13)));
        assertEquals(1, disp.cantidadReservas());
        assertThrows(IllegalArgumentException.class, () -> servicio.consultarCotizacion("R-2"));
    }

    @Test
    void aceptaFechasConsecutivasEnElMismoApartamento() {
        ServicioReservas servicio = servicio(new ServicioDisponibilidad(),
                id -> BigDecimal.ZERO, A_TIEMPO);
        Apartamento apt = apartamento("A-101");
        servicio.registrarSolicitud(reserva("R-1", apt, CanalReserva.PORTAL, 10, 12));
        servicio.registrarSolicitud(reserva("R-2", apt, CanalReserva.PORTAL, 12, 14));
        assertEquals("R-2", servicio.consultarCotizacion("R-2").getIdReserva());
    }

    @Test
    void rechazaIdDuplicadoAunqueSeaOtroApartamento() {
        ServicioReservas servicio = servicio(new ServicioDisponibilidad(),
                id -> BigDecimal.ZERO, A_TIEMPO);
        servicio.registrarSolicitud(reserva("R-1", apartamento("A-101"), CanalReserva.PORTAL, 10, 12));
        assertThrows(IllegalArgumentException.class, () -> servicio.registrarSolicitud(
                reserva("R-1", apartamento("B-202"), CanalReserva.PORTAL, 10, 12)));
    }

    @Test
    void congelaCotizacionAlRegistrarAunqueCambieTarifa() {
        ServicioReservas servicio = servicio(new ServicioDisponibilidad(),
                id -> BigDecimal.ZERO, A_TIEMPO);
        Apartamento apt = apartamento("A-101");
        servicio.registrarSolicitud(reserva("R-1", apt, CanalReserva.PORTAL, 10, 12));
        apt.actualizarTarifas(new TarifasApartamento(
                new BigDecimal("999999"), new BigDecimal("999999"), new BigDecimal("999999")));
        assertEquals(0, new BigDecimal("36000").compareTo(
                servicio.consultarCotizacion("R-1").getAnticipoMinimo()));
    }

    @Test
    void rechazaIdsInexistentesYDependenciasNulas() {
        ServicioReservas servicio = servicio(new ServicioDisponibilidad(), id -> BigDecimal.ZERO, A_TIEMPO);
        assertThrows(IllegalArgumentException.class, () -> servicio.consultarCotizacion("R-X"));
        assertThrows(IllegalArgumentException.class, () -> servicio.confirmarPortal("R-X"));
        assertThrows(IllegalArgumentException.class, () -> servicio.consultarCotizacion(" "));
        assertThrows(NullPointerException.class, () -> servicio.registrarSolicitud(null));
        assertThrows(NullPointerException.class,
                () -> new ServicioReservas(null,
                        new ServicioLiquidacion(new CalendarioTemporadas(), CriterioEdad.POR_NOCHE),
                        id -> BigDecimal.ZERO, A_TIEMPO));
    }
}
