package co.edu.uniquindio.sga.servicios.operacion;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class ComparadorPreparacionTest {

    private record Tarea(String codigo, boolean entradaHoy, LocalDateTime hora) { }

    @Test
    void priorizaEntradasDeHoySobreLasDemas() {
        Tarea hoy = new Tarea("A-101", true, LocalDateTime.of(2027, 3, 10, 15, 0));
        Tarea manana = new Tarea("A-102", false, LocalDateTime.of(2027, 3, 11, 9, 0));
        Comparator<Tarea> comparador = ComparadorPreparacion.porEntradaHoyYHoraLlegada(
                Tarea::entradaHoy, Tarea::hora, Tarea::codigo);

        assertTrue(comparador.compare(hoy, manana) < 0);
    }

    @Test
    void ordenaEntradasDeHoyPorHoraAscendente() {
        Tarea tarde = new Tarea("A-101", true, LocalDateTime.of(2027, 3, 10, 16, 0));
        Tarea manana = new Tarea("A-102", true, LocalDateTime.of(2027, 3, 10, 9, 0));
        Comparator<Tarea> comparador = ComparadorPreparacion.porEntradaHoyYHoraLlegada(
                Tarea::entradaHoy, Tarea::hora, Tarea::codigo);

        assertTrue(comparador.compare(manana, tarde) < 0);
    }

    @Test
    void desempataPorIdentificadorYNoPriorizaHoraDeLasTareasSinEntradaHoy() {
        Tarea b = new Tarea("B-202", false, LocalDateTime.of(2027, 3, 10, 8, 0));
        Tarea a = new Tarea("A-101", false, LocalDateTime.of(2027, 3, 10, 18, 0));
        Comparator<Tarea> comparador = ComparadorPreparacion.porEntradaHoyYHoraLlegada(
                Tarea::entradaHoy, Tarea::hora, Tarea::codigo);

        assertTrue(comparador.compare(a, b) < 0);
    }

    @Test
    void rechazaFuncionesNulas() {
        assertThrows(NullPointerException.class,
                () -> ComparadorPreparacion.porEntradaHoyYHoraLlegada(null, t -> null, Object::toString));
        assertThrows(NullPointerException.class,
                () -> ComparadorPreparacion.porEntradaHoyYHoraLlegada(t -> true, null, Object::toString));
        assertThrows(NullPointerException.class,
                () -> ComparadorPreparacion.porEntradaHoyYHoraLlegada(t -> true, t -> null, null));
    }
}
