package co.edu.uniquindio.sga.servicios.reservas;

import co.edu.uniquindio.sga.dominio.modelo.*;
import co.edu.uniquindio.sga.estructuras.arbol.ArbolBinarioBusqueda;
import co.edu.uniquindio.sga.estructuras.lista.ListaDoblementeEnlazada;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas JUnit del backtracking de grupos grandes. */
class ServicioAsignacionGruposTest {
    private static final Estancia FECHAS = new Estancia(
            LocalDate.of(2027, 3, 10), LocalDate.of(2027, 3, 12));

    private static Apartamento apto(String codigo, int capacidad) {
        return new Apartamento(codigo, "Apto " + codigo, "A", 1, 1,
                capacidad, "", new TarifasApartamento(
                new BigDecimal("50000"), new BigDecimal("70000"),
                new BigDecimal("90000")), EstadoOperativoApartamento.PREPARADO, null);
    }

    private static ArbolBinarioBusqueda<Apartamento> catalogo(Apartamento... apartamentos) {
        ArbolBinarioBusqueda<Apartamento> resultado = new ArbolBinarioBusqueda<>();
        for (Apartamento apto : apartamentos) {
            assertTrue(resultado.insertar(apto));
        }
        return resultado;
    }

    private static PropuestaAsignacionGrupo propuesta(int personas, Apartamento... apartamentos) {
        ServicioAsignacionGrupos servicio = new ServicioAsignacionGrupos(
                catalogo(apartamentos), new ServicioDisponibilidad());
        return servicio.buscar(FECHAS, personas).orElseThrow();
    }

    private static Set<String> codigos(PropuestaAsignacionGrupo propuesta) {
        Set<String> resultado = new HashSet<>(); // Solo en pruebas, no en el núcleo.
        for (Apartamento apartamento : propuesta.getApartamentos()) {
            resultado.add(apartamento.getCodigo());
        }
        return resultado;
    }

    @Test
    void unaUnidadSuficienteEvitaDividirElGrupo() {
        PropuestaAsignacionGrupo r = propuesta(6,
                apto("A-01", 2), apto("A-02", 3), apto("A-03", 6));
        assertEquals(1, r.getCantidadApartamentos());
        assertEquals(Set.of("A-03"), codigos(r));
    }

    @Test
    void usaMinimoNumeroApartamentos() {
        PropuestaAsignacionGrupo r = propuesta(10,
                apto("A-01", 7), apto("A-02", 4), apto("A-03", 3), apto("A-04", 2));
        assertEquals(2, r.getCantidadApartamentos());
        assertEquals(10, r.getCapacidadTotal());
        assertEquals(0, r.getCuposDisponibles());
    }

    @Test
    void desempataPorMenorCapacidadSobrante() {
        PropuestaAsignacionGrupo r = propuesta(10,
                apto("A-01", 8), apto("A-02", 5), apto("A-03", 5));
        assertEquals(2, r.getCantidadApartamentos());
        assertEquals(10, r.getCapacidadTotal());
        assertEquals(Set.of("A-02", "A-03"), codigos(r));
    }

    @Test
    void noProponeUnaCombinacionSinCuposSuficientes() {
        ServicioAsignacionGrupos servicio = new ServicioAsignacionGrupos(
                catalogo(apto("A-01", 3), apto("A-02", 4)), new ServicioDisponibilidad());
        assertTrue(servicio.buscar(FECHAS, 8).isEmpty());
    }

    @Test
    void catalogoVacioNoTienePropuesta() {
        ServicioAsignacionGrupos servicio = new ServicioAsignacionGrupos(
                new ArbolBinarioBusqueda<>(), new ServicioDisponibilidad());
        assertTrue(servicio.buscar(FECHAS, 1).isEmpty());
    }

    @Test
    void ignoraApartamentoFueraDeServicio() {
        Apartamento fuera = apto("A-01", 20);
        fuera.actualizarEstadoOperativo(
                EstadoOperativoApartamento.FUERA_DE_SERVICIO, "Mantenimiento");
        PropuestaAsignacionGrupo r = propuesta(6, fuera,
                apto("A-02", 4), apto("A-03", 3));
        assertEquals(Set.of("A-02", "A-03"), codigos(r));
    }

    @Test
    void excluyeApartamentoConReservaSolapada() {
        Apartamento ocupado = apto("A-01", 20);
        Apartamento libre = apto("A-02", 4);
        ServicioDisponibilidad disponibilidad = new ServicioDisponibilidad();
        Huesped titular = new Huesped("DOC-A", "Ana", LocalDate.of(1990, 1, 1),
                "3001001001", "ana@example.com", null);
        ListaDoblementeEnlazada<Ocupante> ocupantes = new ListaDoblementeEnlazada<>();
        ocupantes.agregarUltimo(new Ocupante("Ana", LocalDate.of(1990, 1, 1)));
        Reserva reserva = new Reserva("R-1", ocupado, titular,
                new Estancia(LocalDate.of(2027, 3, 11), LocalDate.of(2027, 3, 13)),
                CanalReserva.PORTAL, LocalDateTime.of(2027, 3, 1, 10, 0), ocupantes);
        disponibilidad.registrar(reserva);
        ServicioAsignacionGrupos servicio = new ServicioAsignacionGrupos(
                catalogo(ocupado, libre), disponibilidad);
        assertTrue(servicio.buscar(FECHAS, 5).isEmpty());
        assertEquals(Set.of("A-02"), codigos(servicio.buscar(FECHAS, 4).orElseThrow()));
    }

    @Test
    void reservaDeSalidaEnEntradaNoBloqueaNoches() {
        Apartamento apto = apto("A-01", 6);
        ServicioDisponibilidad disponibilidad = new ServicioDisponibilidad();
        Huesped titular = new Huesped("DOC-B", "Beto", LocalDate.of(1985, 2, 1),
                "3002002002", "beto@example.com", null);
        ListaDoblementeEnlazada<Ocupante> ocupantes = new ListaDoblementeEnlazada<>();
        ocupantes.agregarUltimo(new Ocupante("Beto", LocalDate.of(1985, 2, 1)));
        disponibilidad.registrar(new Reserva("R-2", apto, titular,
                new Estancia(LocalDate.of(2027, 3, 8), LocalDate.of(2027, 3, 10)),
                CanalReserva.DIRECTA, LocalDateTime.of(2027, 3, 1, 10, 0), ocupantes));
        ServicioAsignacionGrupos servicio = new ServicioAsignacionGrupos(
                catalogo(apto), disponibilidad);
        assertEquals(1, servicio.buscar(FECHAS, 5).orElseThrow().getCantidadApartamentos());
    }

    @Test
    void noModificaElCatalogoNiRegistraReservas() {
        ArbolBinarioBusqueda<Apartamento> catalogo = catalogo(
                apto("A-01", 4), apto("A-02", 4));
        ServicioDisponibilidad disponibilidad = new ServicioDisponibilidad();
        ServicioAsignacionGrupos servicio = new ServicioAsignacionGrupos(catalogo, disponibilidad);
        var propuesta = servicio.buscar(FECHAS, 7).orElseThrow();
        assertEquals(2, propuesta.getCantidadApartamentos());
        assertEquals(2, catalogo.tamano());
        assertEquals(0, disponibilidad.cantidadReservas());
        assertEquals(FECHAS, propuesta.getEstancia());
    }

    @Test
    void validaArgumentos() {
        ServicioAsignacionGrupos servicio = new ServicioAsignacionGrupos(
                catalogo(apto("A-01", 4)), new ServicioDisponibilidad());
        assertThrows(IllegalArgumentException.class, () -> servicio.buscar(FECHAS, 0));
        assertThrows(IllegalArgumentException.class, () -> servicio.buscar(FECHAS, -1));
        assertThrows(NullPointerException.class, () -> servicio.buscar(null, 2));
        assertThrows(NullPointerException.class,
                () -> new ServicioAsignacionGrupos(null, new ServicioDisponibilidad()));
    }

    @Test
    void busquedaEsReproducibleYRespetaDosApartamentosEnEmpate() {
        ServicioAsignacionGrupos servicio = new ServicioAsignacionGrupos(
                catalogo(apto("C", 4), apto("B", 4), apto("A", 4)),
                new ServicioDisponibilidad());
        PropuestaAsignacionGrupo a = servicio.buscar(FECHAS, 7).orElseThrow();
        PropuestaAsignacionGrupo b = servicio.buscar(FECHAS, 7).orElseThrow();
        assertEquals(2, a.getCantidadApartamentos());
        assertEquals(codigos(a), codigos(b));
    }
}
