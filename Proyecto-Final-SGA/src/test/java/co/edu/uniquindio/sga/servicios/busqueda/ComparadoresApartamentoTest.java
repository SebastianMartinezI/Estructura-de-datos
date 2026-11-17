package co.edu.uniquindio.sga.servicios.busqueda;

import co.edu.uniquindio.sga.dominio.modelo.Apartamento;
import co.edu.uniquindio.sga.dominio.modelo.EstadoOperativoApartamento;
import co.edu.uniquindio.sga.dominio.modelo.TarifasApartamento;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ComparadoresApartamentoTest {

    private static Apartamento apartamento(
            String codigo, int dormitorios, int capacidad) {
        TarifasApartamento tarifas = new TarifasApartamento(
                new BigDecimal("50000"), new BigDecimal("70000"), new BigDecimal("90000"));
        return new Apartamento(codigo, codigo, "A", 1, dormitorios, capacidad,
                "", tarifas, EstadoOperativoApartamento.PREPARADO, null);
    }

    @Test
    void precioOrdenaAscendenteYDesempataPorCodigo() {
        Apartamento a = apartamento("A-101", 1, 2);
        Apartamento b = apartamento("A-102", 2, 4);
        var comparador = ComparadoresApartamento.porPrecio(apartamento ->
                apartamento.getCodigo().equals("A-101")
                        ? new BigDecimal("100000") : new BigDecimal("80000"));

        assertTrue(comparador.compare(b, a) < 0);
        assertTrue(comparador.compare(a, b) > 0);
    }

    @Test
    void capacidadOrdenaDescendenteYDesempataPorCodigo() {
        Apartamento menorCodigo = apartamento("A-101", 2, 4);
        Apartamento mayorCodigo = apartamento("A-102", 2, 4);
        Apartamento mayorCapacidad = apartamento("A-103", 2, 6);
        var comparador = ComparadoresApartamento.porCapacidad();

        assertTrue(comparador.compare(mayorCapacidad, menorCodigo) < 0);
        assertTrue(comparador.compare(menorCodigo, mayorCodigo) < 0);
    }

    @Test
    void dormitoriosOrdenaDescendenteYDesempataPorCodigo() {
        Apartamento uno = apartamento("A-101", 1, 2);
        Apartamento tres = apartamento("A-103", 3, 6);
        assertTrue(ComparadoresApartamento.porDormitorios().compare(tres, uno) < 0);
    }

    @Test
    void reservasOrdenaDeMenorAMayorYDesempataPorCodigo() {
        Apartamento uno = apartamento("A-101", 1, 2);
        Apartamento dos = apartamento("A-102", 2, 4);
        var comparador = ComparadoresApartamento.porNumeroReservas(
                apartamento -> apartamento.getCodigo().equals("A-101") ? 3 : 1);

        assertTrue(comparador.compare(dos, uno) < 0);
    }

    @Test
    void rechazaFuncionesNulas() {
        assertThrows(NullPointerException.class,
                () -> ComparadoresApartamento.porPrecio(null));
        assertThrows(NullPointerException.class,
                () -> ComparadoresApartamento.porNumeroReservas(null));
    }

    @Test
    void rechazaConteoNegativoDeReservas() {
        Apartamento apartamento = apartamento("A-101", 1, 2);
        var comparador = ComparadoresApartamento.porNumeroReservas(a -> -1);
        assertThrows(IllegalArgumentException.class,
                () -> comparador.compare(apartamento, apartamento));
    }
}
