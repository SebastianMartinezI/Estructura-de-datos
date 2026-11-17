package co.edu.uniquindio.sga.dominio.modelo;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas del perfil, validación de datos e identidad del huésped. */
class HuespedTest {

    private static Huesped nuevo(String documento) {
        return new Huesped(documento, "Ana Pérez", LocalDate.of(1992, 4, 8),
                "3001234567", "ana@example.com", null);
    }

    @Test
    void creaHuespedValidoYNormalizaEspaciosExteriores() {
        Huesped huesped = new Huesped(" 102030 ", " Ana Pérez ",
                LocalDate.of(1992, 4, 8), " 3001234567 ",
                " ana@example.com ", " foto.png ");
        assertAll(
                () -> assertEquals("102030", huesped.getDocumento()),
                () -> assertEquals("Ana Pérez", huesped.getNombre()),
                () -> assertEquals(LocalDate.of(1992, 4, 8), huesped.getFechaNacimiento()),
                () -> assertEquals("3001234567", huesped.getTelefono()),
                () -> assertEquals("ana@example.com", huesped.getCorreo()),
                () -> assertEquals("foto.png", huesped.getFoto()));
    }

    @Test
    void fotoEsOpcional() {
        assertEquals("", nuevo("10").getFoto());
    }

    @Test
    void rechazaDocumentoVacioONulo() {
        assertThrows(IllegalArgumentException.class, () -> nuevo(" "));
        assertThrows(IllegalArgumentException.class, () -> nuevo(null));
    }

    @Test
    void rechazaNombreVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Huesped("10", " ", LocalDate.of(1990, 1, 1),
                        "300", "a@b.co", null));
    }

    @Test
    void rechazaFechaNacimientoNula() {
        assertThrows(IllegalArgumentException.class,
                () -> new Huesped("10", "Ana", null, "300", "a@b.co", null));
    }

    @Test
    void rechazaTelefonoYCorreoVacios() {
        assertThrows(IllegalArgumentException.class,
                () -> new Huesped("10", "Ana", LocalDate.of(1990, 1, 1),
                        " ", "a@b.co", null));
        assertThrows(IllegalArgumentException.class,
                () -> new Huesped("10", "Ana", LocalDate.of(1990, 1, 1),
                        "300", " ", null));
    }

    @Test
    void comparaPorDocumentoParaElArbol() {
        assertTrue(nuevo("100").compareTo(nuevo("200")) < 0);
        assertEquals(0, nuevo("100").compareTo(nuevo(" 100 ")));
        assertThrows(NullPointerException.class, () -> nuevo("100").compareTo(null));
    }

    @Test
    void identidadSoloDependeDelDocumento() {
        Huesped primero = nuevo("123");
        Huesped segundo = new Huesped(" 123 ", "Otro nombre",
                LocalDate.of(1980, 1, 1), "111", "otro@example.com", "f.png");
        assertEquals(primero, segundo);
        assertEquals(primero.hashCode(), segundo.hashCode());
        assertNotEquals(primero, nuevo("124"));
    }
}
