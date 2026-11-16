package co.edu.uniquindio.sga.servicios.reservas;

import co.edu.uniquindio.sga.dominio.modelo.*;
import co.edu.uniquindio.sga.estructuras.arbol.ArbolBinarioBusqueda;
import co.edu.uniquindio.sga.estructuras.lista.ListaDoblementeEnlazada;
import co.edu.uniquindio.sga.excepciones.ReservaSolapadaException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas unitarias de disponibilidad y conflictos por noches. */
class ServicioDisponibilidadTest {

    private static final LocalDateTime CREACION = LocalDateTime.of(2026, 11, 16, 10, 0);

    private static Apartamento apartamento(String codigo) {
        return new Apartamento(codigo, "Apartamento " + codigo, "A", 1, 2, 4, "Balcón",
                new TarifasApartamento(new BigDecimal("60000"), new BigDecimal("80000"),
                        new BigDecimal("100000")), EstadoOperativoApartamento.PREPARADO, null);
    }

    private static Estancia estancia(int entrada, int salida) {
        return new Estancia(LocalDate.of(2027, 3, entrada), LocalDate.of(2027, 3, salida));
    }

    private static Reserva reserva(String id, Apartamento apartamento, int entrada, int salida,
                                  CanalReserva canal) {
        Huesped titular = new Huesped("DOC-1", "Ana Pérez", LocalDate.of(1990, 5, 1),
                "3001234567", "ana@ejemplo.com", null);
        ListaDoblementeEnlazada<Ocupante> ocupantes = new ListaDoblementeEnlazada<>();
        ocupantes.agregarUltimo(new Ocupante("Ana Pérez", LocalDate.of(1990, 5, 1)));
        return new Reserva(id, apartamento, titular, estancia(entrada, salida), canal,
                CREACION, ocupantes);
    }

    @Test
    void apartamentoSinReservasEstaDisponible() {
        ServicioDisponibilidad servicio = new ServicioDisponibilidad();
        assertTrue(servicio.estaDisponible(apartamento("A-101"), estancia(10, 12)));
        assertNull(servicio.buscarConflicto(apartamento("A-101"), estancia(10, 12)));
    }

    @Test
    void detectaSolapamientoYRechazaSegundaReserva() {
        ServicioDisponibilidad servicio = new ServicioDisponibilidad();
        Apartamento apto = apartamento("A-101");
        Reserva primera = reserva("R-1", apto, 10, 13, CanalReserva.PORTAL);
        servicio.registrar(primera);
        Reserva segunda = reserva("R-2", apto, 12, 14, CanalReserva.DIRECTA);
        assertSame(primera, servicio.buscarConflicto(apto, segunda.getEstancia()));
        assertThrows(ReservaSolapadaException.class, () -> servicio.registrar(segunda));
        assertEquals(1, servicio.cantidadReservas());
    }

    @Test
    void permiteReservasConsecutivasSinCompartirNoche() {
        ServicioDisponibilidad servicio = new ServicioDisponibilidad();
        Apartamento apto = apartamento("A-101");
        servicio.registrar(reserva("R-1", apto, 10, 12, CanalReserva.PORTAL));
        servicio.registrar(reserva("R-2", apto, 12, 14, CanalReserva.EXTERNA));
        assertEquals(2, servicio.cantidadReservas());
    }

    @Test
    void lasReservasDeOtrosApartamentosNoBloquean() {
        ServicioDisponibilidad servicio = new ServicioDisponibilidad();
        servicio.registrar(reserva("R-1", apartamento("A-101"), 10, 13, CanalReserva.PORTAL));
        Apartamento otro = apartamento("B-202");
        assertTrue(servicio.estaDisponible(otro, estancia(11, 12)));
        servicio.registrar(reserva("R-2", otro, 11, 12, CanalReserva.EXTERNA));
        assertEquals(2, servicio.cantidadReservas());
    }

    @Test
    void conflictoSeAplicaEntreTodosLosCanales() {
        ServicioDisponibilidad servicio = new ServicioDisponibilidad();
        Apartamento apto = apartamento("A-101");
        servicio.registrar(reserva("R-1", apto, 10, 14, CanalReserva.EXTERNA));
        assertThrows(ReservaSolapadaException.class,
                () -> servicio.registrar(reserva("R-2", apto, 11, 12, CanalReserva.PORTAL)));
        assertThrows(ReservaSolapadaException.class,
                () -> servicio.registrar(reserva("R-3", apto, 13, 15, CanalReserva.DIRECTA)));
    }

    @Test
    void cancelacionLiberaLasNochesSinBorrarLaReserva() {
        ServicioDisponibilidad servicio = new ServicioDisponibilidad();
        Apartamento apto = apartamento("A-101");
        Reserva anterior = reserva("R-1", apto, 10, 14, CanalReserva.PORTAL);
        servicio.registrar(anterior);
        anterior.cambiarEstado(EstadoReserva.CANCELADA);
        assertTrue(servicio.estaDisponible(apto, estancia(11, 13)));
        servicio.registrar(reserva("R-2", apto, 11, 13, CanalReserva.DIRECTA));
        assertSame(anterior, servicio.buscarPorId("R-1"));
    }

    @Test
    void noShowLiberaLasNoches() {
        ServicioDisponibilidad servicio = new ServicioDisponibilidad();
        Apartamento apto = apartamento("A-101");
        Reserva anterior = reserva("R-1", apto, 10, 14, CanalReserva.PORTAL);
        servicio.registrar(anterior);
        anterior.cambiarEstado(EstadoReserva.NO_SHOW);
        assertTrue(servicio.estaDisponible(apto, estancia(11, 13)));
    }

    @Test
    void buscaActivaAunqueExistaUnaCanceladaAnterior() {
        ServicioDisponibilidad servicio = new ServicioDisponibilidad();
        Apartamento apto = apartamento("A-101");
        Reserva cancelada = reserva("R-1", apto, 10, 16, CanalReserva.PORTAL);
        servicio.registrar(cancelada);
        cancelada.cambiarEstado(EstadoReserva.CANCELADA);
        Reserva activa = reserva("R-2", apto, 12, 14, CanalReserva.EXTERNA);
        servicio.registrar(activa);
        assertSame(activa, servicio.buscarConflicto(apto, estancia(13, 15)));
    }

    @Test
    void idEsUnicoParaTodoElAlojamiento() {
        ServicioDisponibilidad servicio = new ServicioDisponibilidad();
        servicio.registrar(reserva("R-1", apartamento("A-101"), 10, 12, CanalReserva.PORTAL));
        assertThrows(IllegalArgumentException.class, () -> servicio.registrar(
                reserva("R-1", apartamento("B-202"), 10, 12, CanalReserva.EXTERNA)));
        assertEquals(1, servicio.cantidadReservas());
    }

    @Test
    void rechazaApartamentosFueraDeServicio() {
        ServicioDisponibilidad servicio = new ServicioDisponibilidad();
        Apartamento apto = apartamento("A-101");
        apto.actualizarEstadoOperativo(EstadoOperativoApartamento.FUERA_DE_SERVICIO, "Reparación de tubería");
        assertFalse(servicio.estaDisponible(apto, estancia(10, 12)));
        assertThrows(IllegalStateException.class, () -> servicio.registrar(
                reserva("R-1", apto, 10, 12, CanalReserva.PORTAL)));
    }

    @Test
    void pendienteDePreparacionNoImpideReservarFechaFutura() {
        ServicioDisponibilidad servicio = new ServicioDisponibilidad();
        Apartamento apto = apartamento("A-101");
        apto.actualizarEstadoOperativo(EstadoOperativoApartamento.PENDIENTE_PREPARACION, null);
        assertTrue(servicio.estaDisponible(apto, estancia(10, 12)));
    }

    @Test
    void rechazaNulos() {
        ServicioDisponibilidad servicio = new ServicioDisponibilidad();
        assertThrows(NullPointerException.class, () -> servicio.registrar(null));
        assertThrows(NullPointerException.class,
                () -> servicio.estaDisponible(null, estancia(10, 12)));
        assertThrows(NullPointerException.class,
                () -> servicio.buscarConflicto(apartamento("A-101"), null));
        assertThrows(IllegalArgumentException.class, () -> servicio.buscarPorId(" "));
    }

    @Test
    void buscaReservaPorIdAunqueNoBloquee() {
        ServicioDisponibilidad servicio = new ServicioDisponibilidad();
        Apartamento apto = apartamento("A-101");
        Reserva reserva = reserva("R-100", apto, 10, 12, CanalReserva.PORTAL);
        servicio.registrar(reserva);
        reserva.cambiarEstado(EstadoReserva.CANCELADA);
        assertSame(reserva, servicio.buscarPorId("R-100"));
        assertNull(servicio.buscarPorId("R-999"));
    }

    @Test
    void bstBuscaConPodaYRecursionSinExaminarReservasFuturas() {
        ArbolBinarioBusqueda<Integer> arbol = new ArbolBinarioBusqueda<>();
        for (int valor : new int[] {30, 10, 20, 40, 50, 60}) {
            arbol.insertar(valor);
        }
        AtomicInteger evaluados = new AtomicInteger();
        Integer encontrado = arbol.buscarPrimeroAntesDe(25, numero -> numero, numero -> {
            evaluados.incrementAndGet();
            return numero == 20;
        });
        assertEquals(20, encontrado);
        assertTrue(evaluados.get() <= 2, "No debe evaluar las claves desde 30 en adelante.");
    }
}
