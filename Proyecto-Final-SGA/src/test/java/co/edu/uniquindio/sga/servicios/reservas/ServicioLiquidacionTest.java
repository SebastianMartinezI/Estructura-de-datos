package co.edu.uniquindio.sga.servicios.reservas;

import co.edu.uniquindio.sga.dominio.modelo.*;
import co.edu.uniquindio.sga.estructuras.lista.ListaDoblementeEnlazada;
import co.edu.uniquindio.sga.servicios.tarifas.CalendarioTemporadas;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas del cálculo recursivo, tarifas, gratuidad e invariantes. */
class ServicioLiquidacionTest {

    private static LocalDate dia(int dia) {
        return LocalDate.of(2027, 3, dia);
    }

    private static Reserva reserva(LocalDate entrada, LocalDate salida, Ocupante... personas) {
        Apartamento apartamento = new Apartamento(
                "A-101", "Apartamento 101", "A", 1, 2, 5, "Balcón",
                new TarifasApartamento(new BigDecimal("60000"),
                        new BigDecimal("80000"), new BigDecimal("100000")),
                EstadoOperativoApartamento.PREPARADO, null);
        Huesped titular = new Huesped("1234", "Titular", LocalDate.of(1990, 1, 1),
                "3001234567", "titular@correo.com", null);
        ListaDoblementeEnlazada<Ocupante> ocupantes = new ListaDoblementeEnlazada<>();
        for (Ocupante persona : personas) {
            ocupantes.agregarUltimo(persona);
        }
        return new Reserva("R-001", apartamento, titular, new Estancia(entrada, salida),
                CanalReserva.PORTAL, LocalDateTime.of(2026, 11, 16, 10, 0), ocupantes);
    }

    private static Ocupante adulto(String nombre) {
        return new Ocupante(nombre, LocalDate.of(1980, 1, 1));
    }

    private static ServicioLiquidacion servicio(CalendarioTemporadas calendario,
                                              CriterioEdad criterio) {
        return new ServicioLiquidacion(calendario, criterio);
    }

    @Test
    void cobraDosNochesExcluyendoSalida() {
        Reserva r = reserva(dia(10), dia(12), adulto("Ana"));
        LiquidacionReserva l = servicio(new CalendarioTemporadas(), CriterioEdad.POR_NOCHE)
                .cotizar(r);
        assertEquals(2, l.getNumeroNoches());
        assertEquals(0, new BigDecimal("120000").compareTo(l.getTotal()));
        Iterator<DetalleNoche> noches = l.iterator();
        assertEquals(dia(10), noches.next().noche());
        assertEquals(dia(11), noches.next().noche());
        assertFalse(noches.hasNext());
    }

    @Test
    void tarifaCambiaEnMedioDeLaEstancia() {
        CalendarioTemporadas calendario = new CalendarioTemporadas();
        calendario.registrarPeriodo(new Estancia(dia(11), dia(12)), Temporada.MEDIA);
        calendario.registrarPeriodo(new Estancia(dia(12), dia(13)), Temporada.ALTA);
        Reserva r = reserva(dia(10), dia(13), adulto("Ana"));
        LiquidacionReserva l = servicio(calendario, CriterioEdad.POR_NOCHE).cotizar(r);
        assertEquals(3, l.getNumeroNoches());
        assertEquals(0, new BigDecimal("240000").compareTo(l.getTotal()));
        Iterator<DetalleNoche> it = l.iterator();
        assertEquals(Temporada.BAJA, it.next().temporada());
        assertEquals(Temporada.MEDIA, it.next().temporada());
        assertEquals(Temporada.ALTA, it.next().temporada());
    }

    @Test
    void menorDeCincoOcupaCupoPeroNoPaga() {
        Reserva r = reserva(dia(10), dia(12), adulto("Ana"), adulto("Juan"),
                new Ocupante("Niño", LocalDate.of(2022, 4, 1)));
        LiquidacionReserva l = servicio(new CalendarioTemporadas(), CriterioEdad.POR_NOCHE).cotizar(r);
        assertEquals(0, new BigDecimal("240000").compareTo(l.getTotal()));
        for (DetalleNoche d : l) {
            assertEquals(3, d.totalOcupantes());
            assertEquals(2, d.ocupantesQuePagan());
        }
    }

    @Test
    void ninoQueCumpleSeisCambiaPagadoresPorNoche() {
        Reserva r = reserva(dia(10), dia(12), adulto("Ana"),
                new Ocupante("Niño", LocalDate.of(2021, 3, 11)));
        LiquidacionReserva l = servicio(new CalendarioTemporadas(), CriterioEdad.POR_NOCHE).cotizar(r);
        Iterator<DetalleNoche> it = l.iterator();
        assertEquals(1, it.next().ocupantesQuePagan());
        assertEquals(2, it.next().ocupantesQuePagan());
        assertEquals(0, new BigDecimal("180000").compareTo(l.getTotal()));
    }

    @Test
    void politicaEdadEnEntradaEsConstanteDuranteEstancia() {
        Reserva r = reserva(dia(10), dia(12), adulto("Ana"),
                new Ocupante("Niño", LocalDate.of(2021, 3, 11)));
        LiquidacionReserva l = servicio(new CalendarioTemporadas(), CriterioEdad.FECHA_ENTRADA)
                .cotizar(r);
        assertEquals(0, new BigDecimal("120000").compareTo(l.getTotal()));
        for (DetalleNoche d : l) {
            assertEquals(1, d.ocupantesQuePagan());
        }
    }

    @Test
    void titularNoSeCobraSiNoEstaEnOcupantes() {
        Reserva r = reserva(dia(10), dia(11), new Ocupante("Bebé", LocalDate.of(2026, 1, 1)));
        assertEquals(0, BigDecimal.ZERO.compareTo(
                servicio(new CalendarioTemporadas(), CriterioEdad.POR_NOCHE).cotizar(r).getTotal()));
    }

    @Test
    void noModificaEstadoDeReservaNiTarifas() {
        Reserva r = reserva(dia(10), dia(11), adulto("Ana"));
        servicio(new CalendarioTemporadas(), CriterioEdad.POR_NOCHE).cotizar(r);
        assertEquals(EstadoReserva.PENDIENTE, r.getEstado());
        assertEquals(0, new BigDecimal("60000").compareTo(
                r.getApartamento().tarifaPorPersonaNoche(Temporada.BAJA)));
    }

    @Test
    void liquidacionGuardaCopiaDefensivaDelDesglose() {
        ListaDoblementeEnlazada<DetalleNoche> detalles = new ListaDoblementeEnlazada<>();
        detalles.agregarUltimo(new DetalleNoche(dia(10), Temporada.BAJA,
                new BigDecimal("60000"), 1, 1, new BigDecimal("60000")));
        LiquidacionReserva l = new LiquidacionReserva("R-X", detalles, new BigDecimal("60000"));
        detalles.vaciar();
        assertEquals(1, l.getNumeroNoches());
        assertTrue(l.iterator().hasNext());
    }

    @Test
    void rechazaLiquidacionConTotalInconsistente() {
        ListaDoblementeEnlazada<DetalleNoche> detalles = new ListaDoblementeEnlazada<>();
        detalles.agregarUltimo(new DetalleNoche(dia(10), Temporada.BAJA,
                new BigDecimal("60000"), 1, 1, new BigDecimal("60000")));
        assertThrows(IllegalArgumentException.class,
                () -> new LiquidacionReserva("R-X", detalles, new BigDecimal("90000")));
    }

    @Test
    void rechazaDetalleConSubtotalMalCalculado() {
        assertThrows(IllegalArgumentException.class,
                () -> new DetalleNoche(dia(10), Temporada.BAJA,
                        new BigDecimal("60000"), 2, 1, new BigDecimal("120000")));
    }

    @Test
    void rechazaDependenciasNulas() {
        CalendarioTemporadas calendario = new CalendarioTemporadas();
        assertThrows(NullPointerException.class, () -> new ServicioLiquidacion(null, CriterioEdad.POR_NOCHE));
        assertThrows(NullPointerException.class, () -> new ServicioLiquidacion(calendario, null));
        assertThrows(NullPointerException.class, () -> servicio(calendario, CriterioEdad.POR_NOCHE).cotizar(null));
    }

    @Test
    void rechazaSelectorQueDevuelveTemporadaNula() {
        ServicioLiquidacion servicio = new ServicioLiquidacion(fecha -> null, CriterioEdad.POR_NOCHE);
        Reserva r = reserva(dia(10), dia(11), adulto("Ana"));
        assertThrows(NullPointerException.class, () -> servicio.cotizar(r));
    }
}
