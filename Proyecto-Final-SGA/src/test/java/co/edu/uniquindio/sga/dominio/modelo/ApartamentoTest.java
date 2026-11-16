package co.edu.uniquindio.sga.dominio.modelo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas de invariantes e identidad de los modelos de apartamento. */
class ApartamentoTest {

    private static TarifasApartamento tarifas() {
        return new TarifasApartamento(new BigDecimal("40000"),
                new BigDecimal("55000"), new BigDecimal("70000"));
    }

    private static Apartamento apartamento(String codigo) {
        return new Apartamento(codigo, "Suite Mirador", "A", 2,
                2, 5, "Cocina equipada", tarifas(),
                EstadoOperativoApartamento.PREPARADO, null);
    }

    @Test
    void creaApartamentoConDatosValidos() {
        Apartamento apartamento = apartamento(" a-101 ");
        assertEquals("A-101", apartamento.getCodigo());
        assertEquals("Suite Mirador", apartamento.getNombre());
        assertEquals("A", apartamento.getBloque());
        assertEquals(2, apartamento.getPiso());
        assertEquals(2, apartamento.getNumeroDormitorios());
        assertEquals(5, apartamento.getCapacidadMaxima());
        assertEquals("Cocina equipada", apartamento.getCaracteristicas());
        assertEquals(EstadoOperativoApartamento.PREPARADO, apartamento.getEstadoOperativo());
        assertEquals("", apartamento.getMotivoFueraDeServicio());
    }

    @Test
    void comparaPorCodigoNatural() {
        assertTrue(apartamento("A-101").compareTo(apartamento("A-102")) < 0);
        assertTrue(apartamento("B-101").compareTo(apartamento("A-102")) > 0);
        assertEquals(0, apartamento("a-101").compareTo(apartamento("A-101")));
    }

    @Test
    void identidadPorCodigoCoincideConHashCode() {
        Apartamento primero = apartamento("a-101");
        Apartamento segundo = apartamento("A-101");
        assertEquals(primero, segundo);
        assertEquals(primero.hashCode(), segundo.hashCode());
        assertNotEquals(primero, apartamento("A-102"));
    }

    @Test
    void rechazaCodigoYNombreInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> apartamento("  "));
        assertThrows(IllegalArgumentException.class, () ->
                new Apartamento("A-101", " ", "A", 2, 2, 5, "", tarifas(),
                        EstadoOperativoApartamento.PREPARADO, null));
    }

    @Test
    void rechazaBloqueNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                new Apartamento("A-101", "Suite", null, 2, 2, 5, "", tarifas(),
                        EstadoOperativoApartamento.PREPARADO, null));
    }

    @Test
    void rechazaDormitoriosNoPositivos() {
        assertThrows(IllegalArgumentException.class, () ->
                new Apartamento("A-101", "Suite", "A", 2, 0, 5, "", tarifas(),
                        EstadoOperativoApartamento.PREPARADO, null));
    }

    @Test
    void rechazaCapacidadNoPositiva() {
        assertThrows(IllegalArgumentException.class, () ->
                new Apartamento("A-101", "Suite", "A", 2, 2, 0, "", tarifas(),
                        EstadoOperativoApartamento.PREPARADO, null));
    }

    @Test
    void consultaTarifasDeLasTresTemporadas() {
        Apartamento apartamento = apartamento("A-101");
        assertEquals(0, new BigDecimal("40000").compareTo(apartamento.tarifaPorPersonaNoche(Temporada.BAJA)));
        assertEquals(0, new BigDecimal("55000").compareTo(apartamento.tarifaPorPersonaNoche(Temporada.MEDIA)));
        assertEquals(0, new BigDecimal("70000").compareTo(apartamento.tarifaPorPersonaNoche(Temporada.ALTA)));
    }

    @Test
    void rechazaTarifaNegativaONula() {
        assertThrows(IllegalArgumentException.class, () ->
                new TarifasApartamento(new BigDecimal("-1"), BigDecimal.ONE, BigDecimal.TEN));
        assertThrows(IllegalArgumentException.class, () ->
                new TarifasApartamento(null, BigDecimal.ONE, BigDecimal.TEN));
    }

    @Test
    void permiteActualizarTarifasSinModificarCodigo() {
        Apartamento apartamento = apartamento("A-101");
        apartamento.actualizarTarifas(new TarifasApartamento(
                BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN));
        assertEquals("A-101", apartamento.getCodigo());
        assertEquals(BigDecimal.TEN, apartamento.tarifaPorPersonaNoche(Temporada.ALTA));
    }

    @Test
    void exigeMotivoParaFueraDeServicio() {
        Apartamento apartamento = apartamento("A-101");
        assertThrows(IllegalArgumentException.class, () ->
                apartamento.actualizarEstadoOperativo(EstadoOperativoApartamento.FUERA_DE_SERVICIO, " "));
        assertEquals(EstadoOperativoApartamento.PREPARADO, apartamento.getEstadoOperativo());
    }

    @Test
    void registraMotivoYLoBorraAlSalirDeFueraDeServicio() {
        Apartamento apartamento = apartamento("A-101");
        apartamento.actualizarEstadoOperativo(EstadoOperativoApartamento.FUERA_DE_SERVICIO, "Fuga de agua");
        assertEquals("Fuga de agua", apartamento.getMotivoFueraDeServicio());
        apartamento.actualizarEstadoOperativo(EstadoOperativoApartamento.EN_PREPARACION, null);
        assertEquals("", apartamento.getMotivoFueraDeServicio());
    }

    @Test
    void constructorValidaEstadoInicialFueraDeServicio() {
        assertThrows(IllegalArgumentException.class, () ->
                new Apartamento("A-101", "Suite", "A", 2, 2, 5, "", tarifas(),
                        EstadoOperativoApartamento.FUERA_DE_SERVICIO, null));
    }

    @Test
    void rechazaMotivoEnEstadoNormal() {
        Apartamento apartamento = apartamento("A-101");
        assertThrows(IllegalArgumentException.class, () ->
                apartamento.actualizarEstadoOperativo(EstadoOperativoApartamento.OCUPADO, "Fuga"));
        assertEquals(EstadoOperativoApartamento.PREPARADO, apartamento.getEstadoOperativo());
    }

    @Test
    void rechazaArgumentosNulosEnActualizaciones() {
        Apartamento apartamento = apartamento("A-101");
        assertThrows(IllegalArgumentException.class, () -> apartamento.actualizarTarifas(null));
        assertThrows(IllegalArgumentException.class, () -> apartamento.actualizarEstadoOperativo(null, null));
        assertThrows(IllegalArgumentException.class, () -> apartamento.tarifaPorPersonaNoche(null));
    }
}
