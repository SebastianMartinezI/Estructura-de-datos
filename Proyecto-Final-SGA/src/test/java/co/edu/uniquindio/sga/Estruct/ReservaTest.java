package co.edu.uniquindio.sga.dominio.modelo;

import co.edu.uniquindio.sga.estructuras.arbol.ArbolBinarioBusqueda;
import co.edu.uniquindio.sga.estructuras.lista.ListaDoblementeEnlazada;
import co.edu.uniquindio.sga.excepciones.CapacidadExcedidaException;
import co.edu.uniquindio.sga.excepciones.EstadoReservaInvalidoException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas de identidad, fechas, ocupantes, estados e integración con BST. */
class ReservaTest {

    private static final LocalDateTime CREACION = LocalDateTime.of(2026, 11, 16, 9, 30);

    private static Apartamento apartamento(int capacidad) {
        return new Apartamento("A-101", "Apartamento 101", "A", 1,
                2, capacidad, "Balcón",
                new TarifasApartamento(new BigDecimal("60000"),
                        new BigDecimal("80000"), new BigDecimal("100000")),
                EstadoOperativoApartamento.PREPARADO, null);
    }

    private static Huesped titular() {
        return new Huesped("123456", "Ana Pérez", LocalDate.of(1995, 4, 2),
                "3001234567", "ana@ejemplo.com", null);
    }

    private static ListaDoblementeEnlazada<Ocupante> grupo(int tamano) {
        ListaDoblementeEnlazada<Ocupante> lista = new ListaDoblementeEnlazada<>();
        for (int i = 0; i < tamano; i++) {
            lista.agregarUltimo(new Ocupante("Persona " + i, LocalDate.of(2000, 1, 1)));
        }
        return lista;
    }

    private static Reserva reserva(String id, Apartamento apto,
                                  LocalDate entrada, LocalDate salida,
                                  ListaDoblementeEnlazada<Ocupante> ocupantes) {
        return new Reserva(id, apto, titular(), new Estancia(entrada, salida),
                CanalReserva.PORTAL, CREACION, ocupantes);
    }

    @Test
    void creaReservaPendienteConDatosCorrectos() {
        Reserva reserva = reserva("R-001", apartamento(3),
                LocalDate.of(2027, 3, 10), LocalDate.of(2027, 3, 12), grupo(3));
        assertEquals("R-001", reserva.getId());
        assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());
        assertEquals(CanalReserva.PORTAL, reserva.getCanal());
        assertEquals(3, reserva.getCantidadOcupantes());
        assertEquals(2, reserva.getEstancia().numeroNoches());
    }

    @Test
    void aceptaTresCanales() {
        for (CanalReserva canal : CanalReserva.values()) {
            Reserva reserva = new Reserva("R-" + canal, apartamento(2), titular(),
                    new Estancia(LocalDate.of(2027, 3, 10), LocalDate.of(2027, 3, 12)),
                    canal, CREACION, grupo(1));
            assertEquals(canal, reserva.getCanal());
            assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());
        }
    }

    @Test
    void rechazaIdNuloOVacio() {
        assertThrows(IllegalArgumentException.class, () -> reserva(" ", apartamento(2),
                LocalDate.of(2027, 3, 10), LocalDate.of(2027, 3, 12), grupo(1)));
        assertThrows(IllegalArgumentException.class, () -> reserva(null, apartamento(2),
                LocalDate.of(2027, 3, 10), LocalDate.of(2027, 3, 12), grupo(1)));
    }

    @Test
    void rechazaDependenciasNulas() {
        Estancia estancia = new Estancia(LocalDate.of(2027, 3, 10), LocalDate.of(2027, 3, 12));
        ListaDoblementeEnlazada<Ocupante> ocupantes = grupo(1);
        assertThrows(IllegalArgumentException.class, () ->
                new Reserva("R-1", null, titular(), estancia, CanalReserva.PORTAL, CREACION, ocupantes));
        assertThrows(IllegalArgumentException.class, () ->
                new Reserva("R-1", apartamento(2), null, estancia, CanalReserva.PORTAL, CREACION, ocupantes));
        assertThrows(IllegalArgumentException.class, () ->
                new Reserva("R-1", apartamento(2), titular(), null, CanalReserva.PORTAL, CREACION, ocupantes));
        assertThrows(IllegalArgumentException.class, () ->
                new Reserva("R-1", apartamento(2), titular(), estancia, null, CREACION, ocupantes));
        assertThrows(IllegalArgumentException.class, () ->
                new Reserva("R-1", apartamento(2), titular(), estancia, CanalReserva.PORTAL, null, ocupantes));
        assertThrows(IllegalArgumentException.class, () ->
                new Reserva("R-1", apartamento(2), titular(), estancia, CanalReserva.PORTAL, CREACION, null));
    }

    @Test
    void exigeMinimoUnOcupante() {
        assertThrows(IllegalArgumentException.class, () -> reserva("R-1", apartamento(3),
                LocalDate.of(2027, 3, 10), LocalDate.of(2027, 3, 12), grupo(0)));
    }

    @Test
    void rechazaCapacidadExcedida() {
        assertThrows(CapacidadExcedidaException.class, () -> reserva("R-1", apartamento(2),
                LocalDate.of(2027, 3, 10), LocalDate.of(2027, 3, 12), grupo(3)));
    }

    @Test
    void listaDeOcupantesEsCopiaDefensiva() {
        ListaDoblementeEnlazada<Ocupante> ocupantes = grupo(1);
        Reserva reserva = reserva("R-1", apartamento(3),
                LocalDate.of(2027, 3, 10), LocalDate.of(2027, 3, 12), ocupantes);
        ocupantes.agregarUltimo(new Ocupante("Agregado después", LocalDate.of(1999, 1, 1)));
        assertEquals(1, reserva.getCantidadOcupantes());
        int contados = 0;
        for (Ocupante ocupante : reserva.getOcupantes()) {
            assertNotNull(ocupante);
            contados++;
        }
        assertEquals(1, contados);
    }

    @Test
    void rechazaOcupanteNacidoDespuesDeLaEntrada() {
        ListaDoblementeEnlazada<Ocupante> grupo = new ListaDoblementeEnlazada<>();
        grupo.agregarUltimo(new Ocupante("Bebé futuro", LocalDate.of(2027, 3, 11)));
        assertThrows(IllegalArgumentException.class, () -> reserva("R-1", apartamento(2),
                LocalDate.of(2027, 3, 10), LocalDate.of(2027, 3, 12), grupo));
    }

    @Test
    void permiteTransicionesDelCicloNormal() {
        Reserva reserva = reserva("R-1", apartamento(2),
                LocalDate.of(2027, 3, 10), LocalDate.of(2027, 3, 12), grupo(1));
        reserva.cambiarEstado(EstadoReserva.CONFIRMADA);
        reserva.cambiarEstado(EstadoReserva.EN_CURSO);
        reserva.cambiarEstado(EstadoReserva.FINALIZADA);
        assertEquals(EstadoReserva.FINALIZADA, reserva.getEstado());
        assertFalse(reserva.bloqueaDisponibilidad());
    }

    @Test
    void permiteCancelarPendientesYConfirmadas() {
        Reserva pendiente = reserva("R-1", apartamento(2), LocalDate.of(2027, 3, 10),
                LocalDate.of(2027, 3, 12), grupo(1));
        pendiente.cambiarEstado(EstadoReserva.CANCELADA);
        assertFalse(pendiente.bloqueaDisponibilidad());

        Reserva confirmada = reserva("R-2", apartamento(2), LocalDate.of(2027, 3, 10),
                LocalDate.of(2027, 3, 12), grupo(1));
        confirmada.cambiarEstado(EstadoReserva.CONFIRMADA);
        confirmada.cambiarEstado(EstadoReserva.CANCELADA);
        assertEquals(EstadoReserva.CANCELADA, confirmada.getEstado());
    }

    @Test
    void permiteNoShowDesdePendienteOConfirmada() {
        Reserva pendiente = reserva("R-1", apartamento(2), LocalDate.of(2027, 3, 10),
                LocalDate.of(2027, 3, 12), grupo(1));
        pendiente.cambiarEstado(EstadoReserva.NO_SHOW);
        assertFalse(pendiente.bloqueaDisponibilidad());

        Reserva confirmada = reserva("R-2", apartamento(2), LocalDate.of(2027, 3, 10),
                LocalDate.of(2027, 3, 12), grupo(1));
        confirmada.cambiarEstado(EstadoReserva.CONFIRMADA);
        confirmada.cambiarEstado(EstadoReserva.NO_SHOW);
        assertEquals(EstadoReserva.NO_SHOW, confirmada.getEstado());
    }

    @Test
    void impideSaltosInvalidosYEstadosTerminales() {
        Reserva reserva = reserva("R-1", apartamento(2), LocalDate.of(2027, 3, 10),
                LocalDate.of(2027, 3, 12), grupo(1));
        assertThrows(EstadoReservaInvalidoException.class,
                () -> reserva.cambiarEstado(EstadoReserva.EN_CURSO));
        assertThrows(EstadoReservaInvalidoException.class,
                () -> reserva.cambiarEstado(EstadoReserva.PENDIENTE));
        reserva.cambiarEstado(EstadoReserva.CANCELADA);
        assertThrows(EstadoReservaInvalidoException.class,
                () -> reserva.cambiarEstado(EstadoReserva.CONFIRMADA));
        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
    }

    @Test
    void venceEn24HorasYNoAntes() {
        Reserva reserva = reserva("R-1", apartamento(2), LocalDate.of(2027, 3, 10),
                LocalDate.of(2027, 3, 12), grupo(1));
        assertEquals(CREACION.plusHours(24), reserva.getFechaVencimiento());
        assertFalse(reserva.estaPendienteVencida(CREACION.plusHours(23).plusMinutes(59)));
        assertTrue(reserva.estaPendienteVencida(CREACION.plusHours(24)));
        reserva.cambiarEstado(EstadoReserva.CONFIRMADA);
        assertFalse(reserva.estaPendienteVencida(CREACION.plusHours(30)));
    }

    @Test
    void identificaSolapeSinContarLaNocheDeSalida() {
        Apartamento apto = apartamento(4);
        Reserva una = reserva("R-1", apto, LocalDate.of(2027, 3, 10),
                LocalDate.of(2027, 3, 12), grupo(1));
        Reserva seguida = reserva("R-2", apto, LocalDate.of(2027, 3, 12),
                LocalDate.of(2027, 3, 14), grupo(1));
        Reserva cruzada = reserva("R-3", apto, LocalDate.of(2027, 3, 11),
                LocalDate.of(2027, 3, 13), grupo(1));
        assertFalse(una.comparteNochesCon(seguida));
        assertTrue(una.comparteNochesCon(cruzada));
    }

    @Test
    void diferentesApartamentosNoCompartenNoches() {
        Apartamento otro = new Apartamento("B-202", "Otro apartamento", "B", 2,
                1, 2, "", new TarifasApartamento(BigDecimal.ONE, BigDecimal.TEN,
                new BigDecimal("12")), EstadoOperativoApartamento.PREPARADO, null);
        Reserva una = reserva("R-1", apartamento(4), LocalDate.of(2027, 3, 10),
                LocalDate.of(2027, 3, 12), grupo(1));
        Reserva otraReserva = reserva("R-2", otro, LocalDate.of(2027, 3, 11),
                LocalDate.of(2027, 3, 13), grupo(1));
        assertFalse(una.comparteNochesCon(otraReserva));
    }

    @Test
    void comparableOrdenaPorFechaYDesempataPorIdEnBst() {
        Apartamento apto = apartamento(4);
        Reserva ultima = reserva("R-3", apto, LocalDate.of(2027, 3, 15),
                LocalDate.of(2027, 3, 16), grupo(1));
        Reserva empateA = reserva("R-1", apto, LocalDate.of(2027, 3, 10),
                LocalDate.of(2027, 3, 12), grupo(1));
        Reserva empateB = reserva("R-2", apto, LocalDate.of(2027, 3, 10),
                LocalDate.of(2027, 3, 11), grupo(1));
        ArbolBinarioBusqueda<Reserva> arbol = new ArbolBinarioBusqueda<>();
        assertTrue(arbol.insertar(ultima));
        assertTrue(arbol.insertar(empateA));
        assertTrue(arbol.insertar(empateB));
        assertEquals(3, arbol.tamano());
        assertEquals("R-1", arbol.iterator().next().getId());
        assertTrue(empateA.compareTo(empateB) < 0);
    }

    @Test
    void identidadDependeSoloDelId() {
        Reserva primera = reserva(" R-1 ", apartamento(3), LocalDate.of(2027, 3, 10),
                LocalDate.of(2027, 3, 12), grupo(1));
        Reserva segunda = reserva("R-1", apartamento(3), LocalDate.of(2027, 3, 11),
                LocalDate.of(2027, 3, 13), grupo(1));
        assertEquals(primera, segunda);
        assertEquals(primera.hashCode(), segunda.hashCode());
    }
}
