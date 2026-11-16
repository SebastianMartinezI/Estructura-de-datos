package co.edu.uniquindio.sga.servicios.huespedes;

import co.edu.uniquindio.sga.dominio.modelo.*;
import co.edu.uniquindio.sga.estructuras.lista.ListaDoblementeEnlazada;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

/** Valida favoritos sin duplicados e historial real de estancias finalizadas. */
class ServicioPerfilHuespedTest {

    private static Huesped huesped(String documento) {
        return new Huesped(documento, "Huésped " + documento,
                LocalDate.of(1990, 5, 2), "3001112233", documento + "@ejemplo.com", null);
    }

    private static Apartamento apartamento(String codigo) {
        return new Apartamento(codigo, "Suite " + codigo, "A", 1, 2, 4,
                "", new TarifasApartamento(new BigDecimal("40000"),
                        new BigDecimal("60000"), new BigDecimal("80000")),
                EstadoOperativoApartamento.PREPARADO, null);
    }

    private static Reserva reserva(String id, Huesped titular, Apartamento apto) {
        ListaDoblementeEnlazada<Ocupante> ocupantes = new ListaDoblementeEnlazada<>();
        ocupantes.agregarUltimo(new Ocupante("Ocupante", LocalDate.of(1991, 1, 1)));
        return new Reserva(id, apto, titular,
                new Estancia(LocalDate.of(2027, 1, 10), LocalDate.of(2027, 1, 12)),
                CanalReserva.DIRECTA, LocalDateTime.of(2026, 12, 1, 10, 0), ocupantes);
    }

    private static void finalizar(Reserva reserva) {
        reserva.cambiarEstado(EstadoReserva.CONFIRMADA);
        reserva.cambiarEstado(EstadoReserva.EN_CURSO);
        reserva.cambiarEstado(EstadoReserva.FINALIZADA);
    }

    private static int contar(Iterable<?> valores) {
        int total = 0;
        for (Object ignorado : valores) {
            total++;
        }
        return total;
    }

    @Test void registraHuespedPorDocumento() {
        ServicioPerfilHuesped servicio = new ServicioPerfilHuesped();
        Huesped ana = huesped("101");
        assertTrue(servicio.registrarHuesped(ana));
        assertSame(ana, servicio.buscarHuesped("101"));
        assertEquals(1, servicio.cantidadHuespedes());
    }

    @Test void noRegistraDosHuespedesConMismoDocumento() {
        ServicioPerfilHuesped servicio = new ServicioPerfilHuesped();
        assertTrue(servicio.registrarHuesped(huesped("101")));
        assertFalse(servicio.registrarHuesped(huesped("101")));
        assertEquals(1, servicio.cantidadHuespedes());
    }

    @Test void devuelveNullSiHuespedNoEstaRegistrado() {
        assertNull(new ServicioPerfilHuesped().buscarHuesped("999"));
    }

    @Test void agregaFavoritoUnaSolaVezInclusoConOtraInstanciaMismoCodigo() {
        ServicioPerfilHuesped servicio = new ServicioPerfilHuesped();
        servicio.registrarHuesped(huesped("101"));
        assertTrue(servicio.agregarFavorito("101", apartamento("a-101")));
        assertFalse(servicio.agregarFavorito("101", apartamento("A-101")));
        assertEquals(1, servicio.cantidadFavoritos("101"));
    }

    @Test void listaFavoritosIncluyeLosApartamentosAgregados() {
        ServicioPerfilHuesped servicio = new ServicioPerfilHuesped();
        servicio.registrarHuesped(huesped("101"));
        servicio.agregarFavorito("101", apartamento("A-101"));
        servicio.agregarFavorito("101", apartamento("A-102"));
        assertEquals(2, contar(servicio.consultarFavoritos("101")));
    }

    @Test void eliminaFavoritoYNoAfectaOtros() {
        ServicioPerfilHuesped servicio = new ServicioPerfilHuesped();
        servicio.registrarHuesped(huesped("101"));
        servicio.agregarFavorito("101", apartamento("A-101"));
        servicio.agregarFavorito("101", apartamento("A-102"));
        assertTrue(servicio.eliminarFavorito("101", apartamento("A-101")));
        assertFalse(servicio.eliminarFavorito("101", apartamento("A-101")));
        assertTrue(servicio.esFavorito("101", apartamento("A-102")));
    }

    @Test void favoritosPertenecenAlHuespedCorrecto() {
        ServicioPerfilHuesped servicio = new ServicioPerfilHuesped();
        servicio.registrarHuesped(huesped("101"));
        servicio.registrarHuesped(huesped("202"));
        servicio.agregarFavorito("101", apartamento("A-101"));
        assertEquals(1, servicio.cantidadFavoritos("101"));
        assertEquals(0, servicio.cantidadFavoritos("202"));
    }

    @Test void historialSoloAceptaEstanciasFinalizadas() {
        ServicioPerfilHuesped servicio = new ServicioPerfilHuesped();
        Huesped titular = huesped("101");
        servicio.registrarHuesped(titular);
        Reserva pendiente = reserva("R1", titular, apartamento("A-101"));
        assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarEstanciaFinalizada(pendiente));
        assertEquals(0, servicio.cantidadEstancias("101"));
    }

    @Test void registraEstanciaFinalizadaUnaSolaVez() {
        ServicioPerfilHuesped servicio = new ServicioPerfilHuesped();
        Huesped titular = huesped("101");
        servicio.registrarHuesped(titular);
        Reserva r = reserva("R1", titular, apartamento("A-101"));
        finalizar(r);
        assertTrue(servicio.registrarEstanciaFinalizada(r));
        assertFalse(servicio.registrarEstanciaFinalizada(r));
        assertEquals(1, servicio.cantidadEstancias("101"));
    }

    @Test void historialConservaOrdenDeRegistro() {
        ServicioPerfilHuesped servicio = new ServicioPerfilHuesped();
        Huesped titular = huesped("101");
        servicio.registrarHuesped(titular);
        Reserva primera = reserva("R1", titular, apartamento("A-101"));
        Reserva segunda = reserva("R2", titular, apartamento("A-102"));
        finalizar(primera);
        finalizar(segunda);
        servicio.registrarEstanciaFinalizada(primera);
        servicio.registrarEstanciaFinalizada(segunda);
        Iterator<Reserva> it = servicio.consultarHistorial("101").iterator();
        assertEquals("R1", it.next().getId());
        assertEquals("R2", it.next().getId());
        assertFalse(it.hasNext());
    }

    @Test void historialEsIndependientePorTitular() {
        ServicioPerfilHuesped servicio = new ServicioPerfilHuesped();
        Huesped ana = huesped("101");
        Huesped beto = huesped("202");
        servicio.registrarHuesped(ana);
        servicio.registrarHuesped(beto);
        Reserva r = reserva("R1", ana, apartamento("A-101"));
        finalizar(r);
        servicio.registrarEstanciaFinalizada(r);
        assertEquals(1, servicio.cantidadEstancias("101"));
        assertEquals(0, servicio.cantidadEstancias("202"));
    }

    @Test void historialRechazaTitularNoRegistrado() {
        ServicioPerfilHuesped servicio = new ServicioPerfilHuesped();
        Reserva r = reserva("R1", huesped("101"), apartamento("A-101"));
        finalizar(r);
        assertThrows(IllegalArgumentException.class,
                () -> servicio.registrarEstanciaFinalizada(r));
    }

    @Test void noPermiteAccederAFavoritosDeHuespedInexistente() {
        ServicioPerfilHuesped servicio = new ServicioPerfilHuesped();
        assertThrows(IllegalArgumentException.class,
                () -> servicio.agregarFavorito("999", apartamento("A-101")));
    }

    @Test void rechazaDocumentoVacioYValoresNulos() {
        ServicioPerfilHuesped servicio = new ServicioPerfilHuesped();
        assertThrows(IllegalArgumentException.class, () -> servicio.buscarHuesped("  "));
        assertThrows(NullPointerException.class, () -> servicio.registrarHuesped(null));
        servicio.registrarHuesped(huesped("101"));
        assertThrows(NullPointerException.class, () -> servicio.agregarFavorito("101", null));
    }

    @Test void consultarFavoritosNoExponeConjuntoMutableInterno() {
        ServicioPerfilHuesped servicio = new ServicioPerfilHuesped();
        servicio.registrarHuesped(huesped("101"));
        Iterable<Apartamento> copia = servicio.consultarFavoritos("101");
        servicio.agregarFavorito("101", apartamento("A-101"));
        assertEquals(0, contar(copia));
        assertEquals(1, contar(servicio.consultarFavoritos("101")));
    }

    @Test void consultarHistorialNoExponeListaMutableInterna() {
        ServicioPerfilHuesped servicio = new ServicioPerfilHuesped();
        Huesped titular = huesped("101");
        servicio.registrarHuesped(titular);
        Iterable<Reserva> copia = servicio.consultarHistorial("101");
        Reserva r = reserva("R1", titular, apartamento("A-101"));
        finalizar(r);
        servicio.registrarEstanciaFinalizada(r);
        assertEquals(0, contar(copia));
        assertEquals(1, contar(servicio.consultarHistorial("101")));
    }
}
