package co.edu.uniquindio.sga.interfaz.contexto;

import co.edu.uniquindio.sga.dominio.modelo.Apartamento;
import co.edu.uniquindio.sga.estructuras.arbol.ArbolBinarioBusqueda;
import co.edu.uniquindio.sga.servicios.busqueda.ServicioBusquedaApartamentos;
import co.edu.uniquindio.sga.servicios.huespedes.ServicioPerfilHuesped;
import co.edu.uniquindio.sga.servicios.reservas.ServicioCanalExterno;
import co.edu.uniquindio.sga.servicios.reservas.ServicioDisponibilidad;
import co.edu.uniquindio.sga.servicios.reservas.ServicioReservas;

import java.util.Objects;

/**
 * Agrupa las dependencias compartidas de la aplicación.
 *
 * <p>La interfaz recibe este contexto mediante inyección de dependencias y
 * delega las reglas de negocio en los servicios. No contiene lógica de reserva.</p>
 */
public final class ContextoAplicacion {

    private final ArbolBinarioBusqueda<Apartamento> catalogoApartamentos;
    private final ServicioDisponibilidad servicioDisponibilidad;
    private final ServicioReservas servicioReservas;
    private final ServicioPerfilHuesped servicioPerfilHuesped;
    private final ServicioBusquedaApartamentos servicioBusquedaApartamentos;
    private final ServicioCanalExterno servicioCanalExterno;

    public ContextoAplicacion(
            ArbolBinarioBusqueda<Apartamento> catalogoApartamentos,
            ServicioDisponibilidad servicioDisponibilidad,
            ServicioReservas servicioReservas,
            ServicioPerfilHuesped servicioPerfilHuesped,
            ServicioBusquedaApartamentos servicioBusquedaApartamentos,
            ServicioCanalExterno servicioCanalExterno) {
        this.catalogoApartamentos = Objects.requireNonNull(
                catalogoApartamentos, "El catálogo de apartamentos es obligatorio");
        this.servicioDisponibilidad = Objects.requireNonNull(
                servicioDisponibilidad, "El servicio de disponibilidad es obligatorio");
        this.servicioReservas = Objects.requireNonNull(
                servicioReservas, "El servicio de reservas es obligatorio");
        this.servicioPerfilHuesped = Objects.requireNonNull(
                servicioPerfilHuesped, "El servicio de huéspedes es obligatorio");
        this.servicioBusquedaApartamentos = Objects.requireNonNull(
                servicioBusquedaApartamentos, "El servicio de búsqueda es obligatorio");
        this.servicioCanalExterno = Objects.requireNonNull(
                servicioCanalExterno, "El servicio de canal externo es obligatorio");
    }

    /** Catálogo temporal recorrible; no expone operaciones de modificación desde la vista. */
    public Iterable<Apartamento> consultarApartamentos() {
        return catalogoApartamentos;
    }

    public ServicioDisponibilidad getServicioDisponibilidad() {
        return servicioDisponibilidad;
    }

    public ServicioReservas getServicioReservas() {
        return servicioReservas;
    }

    public ServicioPerfilHuesped getServicioPerfilHuesped() {
        return servicioPerfilHuesped;
    }

    public ServicioBusquedaApartamentos getServicioBusquedaApartamentos() {
        return servicioBusquedaApartamentos;
    }

    public ServicioCanalExterno getServicioCanalExterno() {
        return servicioCanalExterno;
    }
}
