package co.edu.uniquindio.sga.interfaz.demo;

import co.edu.uniquindio.sga.dominio.modelo.Apartamento;
import co.edu.uniquindio.sga.dominio.modelo.EstadoOperativoApartamento;
import co.edu.uniquindio.sga.dominio.modelo.Estancia;
import co.edu.uniquindio.sga.interfaz.contexto.ContextoAplicacion;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Pruebas del catálogo y los servicios temporales usados por la interfaz. */
class FabricaContextoDemoTest {

    @Test
    void debeCrearSeisApartamentosClaramenteIdentificadosComoDemo() {
        ContextoAplicacion contexto = FabricaContextoDemo.crear();
        int cantidad = 0;

        for (Apartamento apartamento : contexto.consultarApartamentos()) {
            cantidad++;
            assertTrue(apartamento.getCodigo().startsWith("DEMO-"));
            assertTrue(apartamento.getNombre().contains("[DEMO]"));
            assertEquals(EstadoOperativoApartamento.PREPARADO,
                    apartamento.getEstadoOperativo());
        }

        assertEquals(6, cantidad);
    }

    @Test
    void debePermitirConsultarDisponibilidadInicialSinReservasPrevias() {
        ContextoAplicacion contexto = FabricaContextoDemo.crear();
        Estancia estancia = new Estancia(
                LocalDate.now().plusDays(2), LocalDate.now().plusDays(4));
        int disponibles = 0;

        for (Apartamento apartamento : contexto.consultarApartamentos()) {
            if (contexto.getServicioDisponibilidad().estaDisponible(apartamento, estancia)) {
                disponibles++;
            }
        }

        assertEquals(6, disponibles);
    }

    @Test
    void debeCrearServiciosDeBusquedaYReservasCompartiendoDisponibilidad() {
        ContextoAplicacion contexto = FabricaContextoDemo.crear();

        assertTrue(contexto.getServicioBusquedaApartamentos() != null);
        assertTrue(contexto.getServicioReservas() != null);
        assertTrue(contexto.getServicioPerfilHuesped() != null);
        assertTrue(contexto.getServicioCanalExterno() != null);
    }
}
