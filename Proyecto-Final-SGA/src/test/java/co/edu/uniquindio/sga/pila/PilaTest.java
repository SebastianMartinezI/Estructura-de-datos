package co.edu.uniquindio.sga.pila;

import co.edu.uniquindio.sga.estructuras.pila.Pila;
import co.edu.uniquindio.sga.excepciones.EstructuraVaciaException;
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class PilaTest {

    @Test
    void nuevaPilaEstaVacia() {
        Pila<String> pila = new Pila<>();

        assertTrue(pila.estaVacia());
        assertEquals(0, pila.tamano());
    }

    @Test
    void retiraEnOrdenLifo() {
        Pila<String> pila = new Pila<>();

        pila.apilar("Cargo");
        pila.apilar("Pago");

        assertEquals("Pago", pila.desapilar());
        assertEquals("Cargo", pila.desapilar());
        assertTrue(pila.estaVacia());
        assertEquals(0, pila.tamano());
    }

    @Test
    void consultarCimaNoRetiraElDato() {
        Pila<Integer> pila = new Pila<>();
        pila.apilar(50);

        assertEquals(Integer.valueOf(50), pila.verCima());
        assertEquals(1, pila.tamano());
    }

    @Test
    void pilaVaciaLanzaExcepcionPropia() {
        Pila<String> pila = new Pila<>();

        assertThrows(
                EstructuraVaciaException.class,
                () -> pila.desapilar()
        );

        assertThrows(
                EstructuraVaciaException.class,
                () -> pila.verCima()
        );
    }

    @Test
    void iteradorRecorreSinModificarLaPila() {
        Pila<String> pila = new Pila<>();
        pila.apilar("A");
        pila.apilar("B");
        pila.apilar("C");

        StringBuilder resultado = new StringBuilder();

        for (String dato : pila) {
            resultado.append(dato);
        }

        assertEquals("CBA", resultado.toString());
        assertEquals(3, pila.tamano());
        assertEquals("C", pila.verCima());
    }

    @Test
    void iteradorAgotadoLanzaExcepcion() {
        Pila<String> pila = new Pila<>();
        Iterator<String> iterador = pila.iterator();

        assertFalse(iterador.hasNext());

        assertThrows(
                NoSuchElementException.class,
                () -> iterador.next()
        );
    }

    @Test
    void iteradorDetectaCambiosEnLaPila() {
        Pila<String> pila = new Pila<>();
        pila.apilar("A");

        Iterator<String> iterador = pila.iterator();
        pila.apilar("B");

        assertThrows(
                ConcurrentModificationException.class,
                () -> iterador.next()
        );
    }

    @Test
    void noPermiteDatosNulos() {
        Pila<String> pila = new Pila<>();

        assertThrows(
                NullPointerException.class,
                () -> pila.apilar(null)
        );

        assertTrue(pila.estaVacia());
    }
}