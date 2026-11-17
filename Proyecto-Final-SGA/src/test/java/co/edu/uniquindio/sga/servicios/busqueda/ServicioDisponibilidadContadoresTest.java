package co.edu.uniquindio.sga.servicios.busqueda;

import co.edu.uniquindio.sga.dominio.modelo.*;
import co.edu.uniquindio.sga.estructuras.lista.ListaDoblementeEnlazada;
import co.edu.uniquindio.sga.servicios.reservas.ServicioDisponibilidad;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServicioDisponibilidadContadoresTest {

    @Test
    void cuentaReservasRegistradasPorApartamento() {
        ServicioDisponibilidad servicio = new ServicioDisponibilidad();
        Apartamento apartamento = new Apartamento("A-101", "A-101", "A", 1, 2, 4, "",
                new TarifasApartamento(new BigDecimal("50000"), new BigDecimal("70000"),
                        new BigDecimal("90000")), EstadoOperativoApartamento.PREPARADO, null);
        Huesped titular = new Huesped("DOC-1", "Ana", LocalDate.of(1990, 1, 1),
                "3000000000", "ana@example.com", null);
        ListaDoblementeEnlazada<Ocupante> ocupantes = new ListaDoblementeEnlazada<>();
        ocupantes.agregarUltimo(new Ocupante("Ana", LocalDate.of(1990, 1, 1)));
        Reserva reserva = new Reserva("R-1", apartamento, titular,
                new Estancia(LocalDate.of(2027, 3, 10), LocalDate.of(2027, 3, 12)),
                CanalReserva.DIRECTA, LocalDateTime.of(2026, 11, 16, 10, 0), ocupantes);

        assertEquals(0, servicio.cantidadReservasPorApartamento(apartamento));
        servicio.registrar(reserva);
        assertEquals(1, servicio.cantidadReservasPorApartamento(apartamento));
        assertEquals(0, servicio.cantidadReservasPorApartamento(
                new Apartamento("B-202", "B-202", "B", 2, 1, 2, "",
                        new TarifasApartamento(new BigDecimal("40000"), new BigDecimal("60000"),
                                new BigDecimal("80000")), EstadoOperativoApartamento.PREPARADO, null)));
    }
}
