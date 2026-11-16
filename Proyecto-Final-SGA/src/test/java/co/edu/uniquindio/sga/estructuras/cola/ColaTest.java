package co.edu.uniquindio.sga.estructuras.cola;

import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas unitarias de los contratos de la cola FIFO genérica. */
class ColaTest {

    @Test
    void inicialmenteEstaVacia() {
        Cola<String> cola = new Cola<>();
        assertTrue(cola.estaVacia());
        assertEquals(0, cola.tamano());
        assertFalse(cola.iterator().hasNext());
    }

    @Test
    void respetaOrdenFifo() {
        Cola<Integer> cola = new Cola<>();
        cola.encolar(10);
        cola.encolar(20);
        cola.encolar(30);
        assertEquals(10, cola.desencolar());
        assertEquals(20, cola.desencolar());
        assertEquals(30, cola.desencolar());
        assertTrue(cola.estaVacia());
    }

    @Test
    void consultarFrenteNoRetiraElemento() {
        Cola<String> cola = new Cola<>();
        cola.encolar("solicitud-A");
        cola.encolar("solicitud-B");
        assertEquals("solicitud-A", cola.consultarFrente());
        assertEquals(2, cola.tamano());
        assertEquals("solicitud-A", cola.desencolar());
    }

    @Test
    void encolarDespuesDeVaciarConDesencolar() {
        Cola<Integer> cola = new Cola<>();
        cola.encolar(1);
        assertEquals(1, cola.desencolar());
        cola.encolar(2);
        assertEquals(2, cola.consultarFrente());
        assertEquals(2, cola.desencolar());
    }

    @Test
    void permiteDuplicados() {
        Cola<String> cola = new Cola<>();
        cola.encolar("A");
        cola.encolar("A");
        assertEquals(2, cola.tamano());
        assertEquals("A", cola.desencolar());
        assertEquals("A", cola.desencolar());
    }

    @Test
    void prohibeNulos() {
        Cola<String> cola = new Cola<>();
        assertThrows(NullPointerException.class, () -> cola.encolar(null));
        assertEquals(0, cola.tamano());
    }

    @Test
    void desencolarVaciaLanzaExcepcionPropia() {
        assertThrows(ColaVaciaException.class, () -> new Cola<Integer>().desencolar());
    }

    @Test
    void consultarFrenteVaciaLanzaExcepcionPropia() {
        assertThrows(ColaVaciaException.class, () -> new Cola<Integer>().consultarFrente());
    }

    @Test
    void vaciarRestableceEstado() {
        Cola<String> cola = new Cola<>();
        cola.encolar("A");
        cola.encolar("B");
        cola.vaciar();
        assertTrue(cola.estaVacia());
        assertEquals(0, cola.tamano());
        cola.encolar("C");
        assertEquals("C", cola.desencolar());
    }

    @Test
    void iteradorRecorreEnOrdenFifo() {
        Cola<Integer> cola = new Cola<>();
        cola.encolar(3);
        cola.encolar(4);
        cola.encolar(5);
        StringBuilder resultado = new StringBuilder();
        for (Integer valor : cola) {
            resultado.append(valor);
        }
        assertEquals("345", resultado.toString());
        assertEquals(3, cola.tamano());
    }

    @Test
    void iteradorAgotadoLanzaExcepcion() {
        Cola<Integer> cola = new Cola<>();
        cola.encolar(1);
        Iterator<Integer> iterador = cola.iterator();
        assertEquals(1, iterador.next());
        assertThrows(NoSuchElementException.class, iterador::next);
    }

    @Test
    void iteradorDetectaModificacion() {
        Cola<Integer> cola = new Cola<>();
        cola.encolar(1);
        Iterator<Integer> iterador = cola.iterator();
        cola.encolar(2);
        assertThrows(ConcurrentModificationException.class, iterador::hasNext);
        assertThrows(ConcurrentModificationException.class, iterador::next);
    }
}
