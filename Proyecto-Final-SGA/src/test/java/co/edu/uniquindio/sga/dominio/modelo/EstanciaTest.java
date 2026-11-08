package co.edu.uniquindio.sga.dominio.modelo;

import co.edu.uniquindio.sga.excepciones.FechaInvalidaException;
import java.time.LocalDate;
import java.util.Iterator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EstanciaTest {
    @Test
    void del10Al12SonDosNoches() {
        Estancia estancia = new Estancia(LocalDate.of(2026, 3, 10), LocalDate.of(2026, 3, 12));
        assertEquals(2, estancia.numeroNoches());
    }

    @Test
    void iteratorExcluyeLaNocheDeSalida() {
        Estancia estancia = new Estancia(LocalDate.of(2026, 3, 10), LocalDate.of(2026, 3, 12));
        Iterator<LocalDate> noches = estancia.iterator();
        assertEquals(LocalDate.of(2026, 3, 10), noches.next());
        assertEquals(LocalDate.of(2026, 3, 11), noches.next());
        assertFalse(noches.hasNext());
    }

    @Test
    void rechazaSalidaIgualOAnteriorAEntrada() {
        assertThrows(FechaInvalidaException.class, () ->
            new Estancia(LocalDate.of(2026, 3, 10), LocalDate.of(2026, 3, 10)));
        assertThrows(FechaInvalidaException.class, () ->
            new Estancia(LocalDate.of(2026, 3, 12), LocalDate.of(2026, 3, 10)));
    }
}
