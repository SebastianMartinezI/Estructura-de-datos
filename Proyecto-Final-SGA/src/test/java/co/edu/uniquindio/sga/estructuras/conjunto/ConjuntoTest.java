package co.edu.uniquindio.sga.estructuras.conjunto;

import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/** Pruebas unitarias del conjunto genérico implementado con nodos propios. */
class ConjuntoTest {

    @Test
    void comienzaVacio() {
        Conjunto<String> conjunto = new Conjunto<>();
        assertTrue(conjunto.estaVacio());
        assertEquals(0, conjunto.tamano());
        assertFalse(conjunto.iterator().hasNext());
    }

    @Test
    void agregaElementosDistintos() {
        Conjunto<String> conjunto = new Conjunto<>();
        assertTrue(conjunto.agregar("A-101"));
        assertTrue(conjunto.agregar("B-202"));
        assertEquals(2, conjunto.tamano());
        assertTrue(conjunto.contiene("A-101"));
        assertTrue(conjunto.contiene("B-202"));
    }

    @Test
    void noAgregaDuplicados() {
        Conjunto<String> conjunto = new Conjunto<>();
        assertTrue(conjunto.agregar("A-101"));
        assertFalse(conjunto.agregar("A-101"));
        assertEquals(1, conjunto.tamano());
    }

    @Test
    void detectaValoresEquivalentesSinExigirMismaReferencia() {
        Conjunto<String> conjunto = new Conjunto<>();
        assertTrue(conjunto.agregar(new String("A-101")));
        assertFalse(conjunto.agregar(new String("A-101")));
        assertTrue(conjunto.contiene(new String("A-101")));
        assertEquals(1, conjunto.tamano());
    }

    @Test
    void eliminaExistentesYRespetaAusentes() {
        Conjunto<Integer> conjunto = new Conjunto<>();
        assertTrue(conjunto.agregar(10));
        assertTrue(conjunto.agregar(20));
        assertTrue(conjunto.eliminar(10));
        assertFalse(conjunto.eliminar(10));
        assertFalse(conjunto.eliminar(99));
        assertFalse(conjunto.contiene(10));
        assertTrue(conjunto.contiene(20));
        assertEquals(1, conjunto.tamano());
    }

    @Test
    void prohibeValoresNulos() {
        Conjunto<String> conjunto = new Conjunto<>();
        assertThrows(NullPointerException.class, () -> conjunto.agregar(null));
        assertThrows(NullPointerException.class, () -> conjunto.contiene(null));
        assertThrows(NullPointerException.class, () -> conjunto.eliminar(null));
        assertEquals(0, conjunto.tamano());
    }

    @Test
    void manejaColisionesDeHash() {
        Conjunto<Clave> conjunto = new Conjunto<>();
        assertTrue(conjunto.agregar(new Clave("A")));
        assertTrue(conjunto.agregar(new Clave("B")));
        assertTrue(conjunto.agregar(new Clave("C")));
        assertFalse(conjunto.agregar(new Clave("B")));
        assertTrue(conjunto.contiene(new Clave("A")));
        assertTrue(conjunto.eliminar(new Clave("B")));
        assertFalse(conjunto.contiene(new Clave("B")));
        assertTrue(conjunto.contiene(new Clave("C")));
        assertEquals(2, conjunto.tamano());
    }

    @Test
    void conservaElementosTrasRedimensionar() {
        Conjunto<Integer> conjunto = new Conjunto<>();
        for (int i = 0; i < 200; i++) {
            assertTrue(conjunto.agregar(i));
        }
        assertEquals(200, conjunto.tamano());
        for (int i = 0; i < 200; i++) {
            assertTrue(conjunto.contiene(i));
            assertFalse(conjunto.agregar(i));
        }
        assertEquals(200, conjunto.tamano());
    }

    @Test
    void iteradorVisitaTodosUnaVezSinOrdenGarantizado() {
        Conjunto<String> conjunto = new Conjunto<>();
        conjunto.agregar("A");
        conjunto.agregar("B");
        conjunto.agregar("C");
        int a = 0, b = 0, c = 0;
        for (String elemento : conjunto) {
            switch (elemento) {
                case "A" -> a++;
                case "B" -> b++;
                case "C" -> c++;
                default -> fail("Elemento inesperado: " + elemento);
            }
        }
        assertEquals(1, a);
        assertEquals(1, b);
        assertEquals(1, c);
    }

    @Test
    void iteradorAgotadoLanzaExcepcion() {
        Conjunto<Integer> conjunto = new Conjunto<>();
        conjunto.agregar(7);
        Iterator<Integer> iterador = conjunto.iterator();
        assertEquals(7, iterador.next());
        assertFalse(iterador.hasNext());
        assertThrows(NoSuchElementException.class, iterador::next);
    }

    @Test
    void iteradorDetectaInsercionYEliminacion() {
        Conjunto<Integer> conjunto = new Conjunto<>();
        conjunto.agregar(1);
        Iterator<Integer> primero = conjunto.iterator();
        conjunto.agregar(2);
        assertThrows(ConcurrentModificationException.class, primero::hasNext);

        Iterator<Integer> segundo = conjunto.iterator();
        conjunto.eliminar(1);
        assertThrows(ConcurrentModificationException.class, segundo::next);
    }

    @Test
    void duplicadoNoInvalidaIterador() {
        Conjunto<String> conjunto = new Conjunto<>();
        conjunto.agregar("A");
        Iterator<String> iterador = conjunto.iterator();
        assertFalse(conjunto.agregar("A"));
        assertTrue(iterador.hasNext());
        assertEquals("A", iterador.next());
    }

    @Test
    void iteradorNoPermiteEliminarDirectamente() {
        Conjunto<Integer> conjunto = new Conjunto<>();
        conjunto.agregar(1);
        assertThrows(UnsupportedOperationException.class,
                () -> conjunto.iterator().remove());
    }

    @Test
    void vaciarYVolverAUtilizar() {
        Conjunto<String> conjunto = new Conjunto<>();
        conjunto.agregar("A");
        conjunto.agregar("B");
        Iterator<String> iterador = conjunto.iterator();
        conjunto.vaciar();
        assertTrue(conjunto.estaVacio());
        assertEquals(0, conjunto.tamano());
        assertThrows(ConcurrentModificationException.class, iterador::hasNext);
        assertTrue(conjunto.agregar("C"));
        assertTrue(conjunto.contiene("C"));
    }

    private record Clave(String id) {
        @Override
        public int hashCode() {
            return 1; // Fuerza colisiones para verificar el encadenamiento.
        }
    }
}
