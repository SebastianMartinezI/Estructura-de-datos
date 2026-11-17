package co.edu.uniquindio.sga.interfaz.demo;

import co.edu.uniquindio.sga.dominio.modelo.Apartamento;
import co.edu.uniquindio.sga.dominio.modelo.EstadoOperativoApartamento;
import co.edu.uniquindio.sga.dominio.modelo.Estancia;
import co.edu.uniquindio.sga.dominio.modelo.TarifasApartamento;
import co.edu.uniquindio.sga.dominio.modelo.Temporada;
import co.edu.uniquindio.sga.estructuras.arbol.ArbolBinarioBusqueda;
import co.edu.uniquindio.sga.interfaz.contexto.ContextoAplicacion;
import co.edu.uniquindio.sga.servicios.busqueda.ServicioBusquedaApartamentos;
import co.edu.uniquindio.sga.servicios.huespedes.ServicioPerfilHuesped;
import co.edu.uniquindio.sga.servicios.reservas.CriterioEdad;
import co.edu.uniquindio.sga.servicios.reservas.ServicioCanalExterno;
import co.edu.uniquindio.sga.servicios.reservas.ServicioDisponibilidad;
import co.edu.uniquindio.sga.servicios.reservas.ServicioLiquidacion;
import co.edu.uniquindio.sga.servicios.reservas.ServicioReservas;
import co.edu.uniquindio.sga.servicios.tarifas.CalendarioTemporadas;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;

/**
 * Construye un contexto aislado con datos de demostración para desarrollar la
 * interfaz JavaFX mientras se define y conecta la carga de datos definitiva.
 *
 * <p>Los códigos y nombres de los apartamentos llevan la marca DEMO. Todo el
 * contenido se mantiene en memoria y se pierde al cerrar la aplicación. No
 * debe utilizarse como catálogo real ni como persistencia del alojamiento.</p>
 */
public final class FabricaContextoDemo {

    private FabricaContextoDemo() {
        throw new AssertionError("No se debe instanciar una fábrica estática");
    }

    /** Crea servicios compartidos y un catálogo temporal de seis apartamentos. */
    public static ContextoAplicacion crear() {
        ArbolBinarioBusqueda<Apartamento> catalogo = new ArbolBinarioBusqueda<>();
        registrarApartamentosDemo(catalogo);

        CalendarioTemporadas calendario = new CalendarioTemporadas();
        LocalDate hoy = LocalDate.now();
        calendario.registrarPeriodo(
                new Estancia(hoy.plusDays(30), hoy.plusDays(40)), Temporada.MEDIA);
        calendario.registrarPeriodo(
                new Estancia(hoy.plusDays(70), hoy.plusDays(85)), Temporada.ALTA);

        ServicioDisponibilidad disponibilidad = new ServicioDisponibilidad();
        ServicioLiquidacion liquidacion = new ServicioLiquidacion(
                calendario, CriterioEdad.FECHA_ENTRADA);

        // La demo no simula pagos. Ninguna reserva Portal se confirma hasta
        // conectar la consulta real de anticipos con el módulo de folios.
        ServicioReservas reservas = new ServicioReservas(
                disponibilidad, liquidacion, idReserva -> BigDecimal.ZERO,
                Clock.systemDefaultZone());

        ServicioPerfilHuesped perfiles = new ServicioPerfilHuesped();
        ServicioBusquedaApartamentos busqueda = new ServicioBusquedaApartamentos(
                catalogo, disponibilidad);
        ServicioCanalExterno canalExterno = new ServicioCanalExterno(reservas);

        return new ContextoAplicacion(
                catalogo, disponibilidad, reservas, perfiles, busqueda, canalExterno);
    }

    private static void registrarApartamentosDemo(
            ArbolBinarioBusqueda<Apartamento> catalogo) {
        catalogo.insertar(apartamento(
                "DEMO-101", "Suite Jardín [DEMO]", 1, 2,
                "Una habitación, vista al jardín", "85000", "115000", "155000"));
        catalogo.insertar(apartamento(
                "DEMO-102", "Suite Mirador [DEMO]", 1, 2,
                "Una habitación, balcón", "92000", "125000", "165000"));
        catalogo.insertar(apartamento(
                "DEMO-201", "Apartamento Familiar [DEMO]", 2, 4,
                "Dos habitaciones, sala", "105000", "145000", "190000"));
        catalogo.insertar(apartamento(
                "DEMO-202", "Apartamento Bosque [DEMO]", 2, 5,
                "Dos habitaciones, cocina", "112000", "152000", "205000"));
        catalogo.insertar(apartamento(
                "DEMO-301", "Suite Panorámica [DEMO]", 3, 4,
                "Dos habitaciones, vista panorámica", "118000", "160000", "215000"));
        catalogo.insertar(apartamento(
                "DEMO-302", "Apartamento Premium [DEMO]", 3, 6,
                "Tres habitaciones, sala amplia", "135000", "180000", "245000"));
    }

    private static Apartamento apartamento(
            String codigo, String nombre, int dormitorios, int capacidad,
            String caracteristicas, String baja, String media, String alta) {
        return new Apartamento(
                codigo,
                nombre,
                "BLOQUE-DEMO",
                1,
                dormitorios,
                capacidad,
                caracteristicas,
                new TarifasApartamento(
                        new BigDecimal(baja),
                        new BigDecimal(media),
                        new BigDecimal(alta)),
                EstadoOperativoApartamento.PREPARADO,
                null);
    }
}
