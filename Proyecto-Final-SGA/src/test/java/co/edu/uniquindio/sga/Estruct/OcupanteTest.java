package co.edu.uniquindio.sga.dominio.modelo;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas del modelo de ocupantes y cálculo de edad por referencia. */
class OcupanteTest {

    @Test
    void creaOcupanteYNormalizaNombre() {
        Ocupante ocupante = new Ocupante(" María ", LocalDate.of(2020, 3, 10));
        assertEquals("María", ocupante.getNombre());
        assertEquals(LocalDate.of(2020, 3, 10), ocupante.getFechaNacimiento());
    }

    @Test
    void rechazaNombreNuloOVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Ocupante(null, LocalDate.of(2019, 1, 1)));
        assertThrows(IllegalArgumentException.class,
                () -> new Ocupante(" ", LocalDate.of(2019, 1, 1)));
    }

    @Test
    void rechazaNacimientoNulo() {
        assertThrows(IllegalArgumentException.class, () -> new Ocupante("María", null));
    }

    @Test
    void calculaEdadAntesYDespuesDelCumpleanos() {
        Ocupante ocupante = new Ocupante("María", LocalDate.of(2020, 3, 10));
        assertEquals(5, ocupante.edadEn(LocalDate.of(2026, 3, 9)));
        assertEquals(6, ocupante.edadEn(LocalDate.of(2026, 3, 10)));
    }

    @Test
    void devuelveCeroEnDiaDelNacimiento() {
        Ocupante ocupante = new Ocupante("Bebé", LocalDate.of(2026, 4, 2));
        assertEquals(0, ocupante.edadEn(LocalDate.of(2026, 4, 2)));
    }

    @Test
    void rechazaFechaAnteriorAlNacimientoONula() {
        Ocupante ocupante = new Ocupante("María", LocalDate.of(2020, 3, 10));
        assertThrows(IllegalArgumentException.class,
                () -> ocupante.edadEn(LocalDate.of(2020, 3, 9)));
        assertThrows(IllegalArgumentException.class, () -> ocupante.edadEn(null));
    }

    @Test
    void noConfundeAIntegrantesConMismoNombreYNacimiento() {
        Ocupante primero = new Ocupante("Gemelo", LocalDate.of(2017, 5, 7));
        Ocupante segundo = new Ocupante("Gemelo", LocalDate.of(2017, 5, 7));
        assertNotSame(primero, segundo);
        assertNotEquals(primero, segundo);
    }
}
