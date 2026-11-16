package co.edu.uniquindio.sga.servicios.tarifas;

import co.edu.uniquindio.sga.dominio.modelo.Estancia;
import co.edu.uniquindio.sga.dominio.modelo.Temporada;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/** Pruebas de límites exclusivos y ausencia de periodos ambiguos. */
class CalendarioTemporadasTest {

    private static LocalDate dia(int dia) { return LocalDate.of(2027, 3, dia); }

    @Test
    void fechasSinPeriodoSonBaja() {
        assertEquals(Temporada.BAJA, new CalendarioTemporadas().temporadaEn(dia(10)));
    }

    @Test
    void periodoIncluyeEntradaPeroNoSalida() {
        CalendarioTemporadas c = new CalendarioTemporadas();
        c.registrarPeriodo(new Estancia(dia(10), dia(12)), Temporada.ALTA);
        assertEquals(Temporada.ALTA, c.temporadaEn(dia(10)));
        assertEquals(Temporada.ALTA, c.temporadaEn(dia(11)));
        assertEquals(Temporada.BAJA, c.temporadaEn(dia(12)));
    }

    @Test
    void permitePeriodosConsecutivos() {
        CalendarioTemporadas c = new CalendarioTemporadas();
        c.registrarPeriodo(new Estancia(dia(10), dia(12)), Temporada.MEDIA);
        c.registrarPeriodo(new Estancia(dia(12), dia(14)), Temporada.ALTA);
        assertEquals(Temporada.MEDIA, c.temporadaEn(dia(11)));
        assertEquals(Temporada.ALTA, c.temporadaEn(dia(12)));
    }

    @Test
    void rechazaPeriodosSuperpuestos() {
        CalendarioTemporadas c = new CalendarioTemporadas();
        c.registrarPeriodo(new Estancia(dia(10), dia(13)), Temporada.ALTA);
        assertThrows(IllegalArgumentException.class,
                () -> c.registrarPeriodo(new Estancia(dia(12), dia(14)), Temporada.MEDIA));
    }

    @Test
    void rechazaFechasOTemporadasNulas() {
        CalendarioTemporadas c = new CalendarioTemporadas();
        assertThrows(NullPointerException.class, () -> c.temporadaEn(null));
        assertThrows(NullPointerException.class, () -> c.registrarPeriodo(null, Temporada.MEDIA));
        assertThrows(NullPointerException.class,
                () -> c.registrarPeriodo(new Estancia(dia(10), dia(11)), null));
    }
}
