package co.edu.uniquindio.sga.servicios.busqueda;

import co.edu.uniquindio.sga.dominio.modelo.*;
import co.edu.uniquindio.sga.estructuras.arbol.ArbolBinarioBusqueda;
import co.edu.uniquindio.sga.estructuras.lista.ListaDoblementeEnlazada;
import co.edu.uniquindio.sga.servicios.reservas.ServicioDisponibilidad;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ServicioBusquedaApartamentosTest {

    private static Apartamento apartamento(
            String codigo, int dormitorios, int capacidad, EstadoOperativoApartamento estado) {
        TarifasApartamento tarifas = new TarifasApartamento(
                new BigDecimal("50000"), new BigDecimal("70000"), new BigDecimal("90000"));
        String motivo = estado == EstadoOperativoApartamento.FUERA_DE_SERVICIO
                ? "Mantenimiento" : null;
        return new Apartamento(codigo, codigo, "A", 1, dormitorios, capacidad,
                "", tarifas, estado, motivo);
    }

    private static Estancia estancia() {
        return new Estancia(LocalDate.of(2027, 3, 10), LocalDate.of(2027, 3, 12));
    }

    private static ServicioBusquedaApartamentos crearServicio(
            ArbolBinarioBusqueda<Apartamento> catalogo,
            ServicioDisponibilidad disponibilidad) {
        return new ServicioBusquedaApartamentos(catalogo, disponibilidad);
    }

    private static String[] codigos(Iterable<Apartamento> apartamentos, int cantidad) {
        String[] resultado = new String[cantidad];
        int indice = 0;
        for (Apartamento apartamento : apartamentos) {
            resultado[indice++] = apartamento.getCodigo();
        }
        assertEquals(cantidad, indice);
        return resultado;
    }

    @Test
    void filtraPorCapacidadYOrdenaPorPrecio() {
        ArbolBinarioBusqueda<Apartamento> catalogo = new ArbolBinarioBusqueda<>();
        catalogo.insertar(apartamento("A-101", 1, 2, EstadoOperativoApartamento.PREPARADO));
        catalogo.insertar(apartamento("A-102", 2, 4, EstadoOperativoApartamento.PREPARADO));
        catalogo.insertar(apartamento("A-103", 3, 6, EstadoOperativoApartamento.PREPARADO));

        ServicioBusquedaApartamentos servicio = crearServicio(catalogo, new ServicioDisponibilidad());
        ListaDoblementeEnlazada<Apartamento> resultado = servicio.porPrecio(
                estancia(), 3,
                apartamento -> apartamento.getCodigo().equals("A-102")
                        ? new BigDecimal("120000") : new BigDecimal("90000"));

        assertArrayEquals(new String[]{"A-103", "A-102"}, codigos(resultado, 2));
    }

    @Test
    void excluyeApartamentosFueraDeServicio() {
        ArbolBinarioBusqueda<Apartamento> catalogo = new ArbolBinarioBusqueda<>();
        catalogo.insertar(apartamento("A-101", 1, 2, EstadoOperativoApartamento.PREPARADO));
        catalogo.insertar(apartamento("A-102", 2, 4, EstadoOperativoApartamento.FUERA_DE_SERVICIO));

        ServicioBusquedaApartamentos servicio = crearServicio(catalogo, new ServicioDisponibilidad());
        assertArrayEquals(new String[]{"A-101"}, codigos(
                servicio.porCapacidad(estancia(), 1), 1));
    }

    @Test
    void ordenaPorCapacidadYDormitorios() {
        ArbolBinarioBusqueda<Apartamento> catalogo = new ArbolBinarioBusqueda<>();
        catalogo.insertar(apartamento("A-101", 1, 2, EstadoOperativoApartamento.PREPARADO));
        catalogo.insertar(apartamento("A-102", 3, 4, EstadoOperativoApartamento.PREPARADO));
        catalogo.insertar(apartamento("A-103", 2, 6, EstadoOperativoApartamento.PREPARADO));

        ServicioBusquedaApartamentos servicio = crearServicio(catalogo, new ServicioDisponibilidad());
        assertArrayEquals(new String[]{"A-103", "A-102", "A-101"},
                codigos(servicio.porCapacidad(estancia(), 1), 3));
        assertArrayEquals(new String[]{"A-102", "A-103", "A-101"},
                codigos(servicio.porDormitorios(estancia(), 1), 3));
    }

    @Test
    void ordenaPorCantidadDeReservasConContadorInyectado() {
        ArbolBinarioBusqueda<Apartamento> catalogo = new ArbolBinarioBusqueda<>();
        catalogo.insertar(apartamento("A-101", 1, 2, EstadoOperativoApartamento.PREPARADO));
        catalogo.insertar(apartamento("A-102", 2, 4, EstadoOperativoApartamento.PREPARADO));
        catalogo.insertar(apartamento("A-103", 3, 6, EstadoOperativoApartamento.PREPARADO));

        ServicioBusquedaApartamentos servicio = crearServicio(catalogo, new ServicioDisponibilidad());
        assertArrayEquals(new String[]{"A-102", "A-103", "A-101"}, codigos(
                servicio.porNumeroReservas(estancia(), 1,
                        apartamento -> switch (apartamento.getCodigo()) {
                            case "A-101" -> 5;
                            case "A-102" -> 1;
                            default -> 3;
                        }), 3));
    }

    @Test
    void devuelveListaVaciaCuandoNoHayCoincidencias() {
        ArbolBinarioBusqueda<Apartamento> catalogo = new ArbolBinarioBusqueda<>();
        catalogo.insertar(apartamento("A-101", 1, 2, EstadoOperativoApartamento.PREPARADO));
        ServicioBusquedaApartamentos servicio = crearServicio(catalogo, new ServicioDisponibilidad());

        assertTrue(servicio.porCapacidad(estancia(), 5).estaVacia());
    }

    @Test
    void rechazaCantidadDePersonasNoPositiva() {
        ServicioBusquedaApartamentos servicio = crearServicio(
                new ArbolBinarioBusqueda<>(), new ServicioDisponibilidad());
        assertThrows(IllegalArgumentException.class,
                () -> servicio.porCapacidad(estancia(), 0));
    }

    @Test
    void rechazaArgumentosNulos() {
        ServicioBusquedaApartamentos servicio = crearServicio(
                new ArbolBinarioBusqueda<>(), new ServicioDisponibilidad());
        assertThrows(NullPointerException.class,
                () -> servicio.buscarDisponibles(estancia(), 1, null));
        assertThrows(NullPointerException.class,
                () -> servicio.buscarDisponibles(null, 1, ComparadoresApartamento.porCapacidad()));
    }
}
