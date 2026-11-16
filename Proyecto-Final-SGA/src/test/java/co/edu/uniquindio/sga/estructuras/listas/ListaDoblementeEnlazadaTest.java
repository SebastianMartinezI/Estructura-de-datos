package co.edu.uniquindio.sga.estructuras.lista;

import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ListaDoblementeEnlazadaTest {

    @Test
    void comienzaVacia() {
        ListaDoblementeEnlazada<String> lista = new ListaDoblementeEnlazada<>();
        assertTrue(lista.estaVacia());
        assertEquals(0, lista.tamano());
        assertFalse(lista.iterator().hasNext());
        assertFalse(lista.iteradorInverso().hasNext());
    }

    @Test
    void agregaEnAmbosExtremosYConservaOrden() {
        ListaDoblementeEnlazada<String> lista = new ListaDoblementeEnlazada<>();
        lista.agregarUltimo("noche 2");
        lista.agregarPrimero("noche 1");
        lista.agregarUltimo("noche 3");
        assertEquals("noche 1", lista.obtenerPrimero());
        assertEquals("noche 3", lista.obtenerUltimo());
        assertEquals("noche 1,noche 2,noche 3,", recorrer(lista.iterator()));
        assertEquals("noche 3,noche 2,noche 1,", recorrer(lista.iteradorInverso()));
    }

    @Test
    void permiteDuplicadosYEliminaSoloPrimeraAparicion() {
        ListaDoblementeEnlazada<Integer> lista = new ListaDoblementeEnlazada<>();
        lista.agregarUltimo(7);
        lista.agregarUltimo(4);
        lista.agregarUltimo(7);
        assertTrue(lista.eliminar(7));
        assertEquals(2, lista.tamano());
        assertEquals("4,7,", recorrer(lista.iterator()));
        assertFalse(lista.eliminar(99));
    }

    @Test
    void eliminaExtremosYSeRecuperaTrasQuedarVacia() {
        ListaDoblementeEnlazada<Integer> lista = new ListaDoblementeEnlazada<>();
        lista.agregarUltimo(1);
        lista.agregarUltimo(2);
        lista.agregarUltimo(3);
        assertEquals(Integer.valueOf(1), lista.eliminarPrimero());
        assertEquals(Integer.valueOf(3), lista.eliminarUltimo());
        assertEquals(Integer.valueOf(2), lista.eliminarUltimo());
        assertTrue(lista.estaVacia());
        lista.agregarPrimero(9);
        assertEquals(Integer.valueOf(9), lista.obtenerUltimo());
        assertEquals(Integer.valueOf(9), lista.eliminarPrimero());
        assertTrue(lista.estaVacia());
    }

    @Test
    void eliminaValorDeLaMitadYConservaAmbosEnlaces() {
        ListaDoblementeEnlazada<Integer> lista = new ListaDoblementeEnlazada<>();
        for (int i = 1; i <= 5; i++) {
            lista.agregarUltimo(i);
        }
        assertTrue(lista.eliminar(3));
        assertEquals("1,2,4,5,", recorrer(lista.iterator()));
        assertEquals("5,4,2,1,", recorrer(lista.iteradorInverso()));
    }

    @Test
    void obtienePorIndiceDesdeAmbosExtremos() {
        ListaDoblementeEnlazada<Integer> lista = new ListaDoblementeEnlazada<>();
        for (int i = 0; i < 6; i++) {
            lista.agregarUltimo(i * 10);
        }
        assertEquals(Integer.valueOf(0), lista.obtener(0));
        assertEquals(Integer.valueOf(20), lista.obtener(2));
        assertEquals(Integer.valueOf(50), lista.obtener(5));
        assertThrows(IndexOutOfBoundsException.class, () -> lista.obtener(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> lista.obtener(6));
    }

    @Test
    void contieneDetectaValoresPresentesYAusentes() {
        ListaDoblementeEnlazada<String> lista = new ListaDoblementeEnlazada<>();
        lista.agregarUltimo("Ana");
        lista.agregarUltimo("Luis");
        assertTrue(lista.contiene("Ana"));
        assertFalse(lista.contiene("Eva"));
    }

    @Test
    void vaciarReiniciaListaYPermiteVolverAInsertar() {
        ListaDoblementeEnlazada<Integer> lista = new ListaDoblementeEnlazada<>();
        lista.agregarPrimero(5);
        lista.agregarUltimo(6);
        lista.vaciar();
        assertEquals(0, lista.tamano());
        assertTrue(lista.estaVacia());
        lista.agregarUltimo(8);
        assertEquals(Integer.valueOf(8), lista.obtenerPrimero());
    }

    @Test
    void operacionesSobreListaVaciaLanzanExcepcionPropia() {
        ListaDoblementeEnlazada<Integer> lista = new ListaDoblementeEnlazada<>();
        assertThrows(ListaVaciaException.class, lista::eliminarPrimero);
        assertThrows(ListaVaciaException.class, lista::eliminarUltimo);
        assertThrows(ListaVaciaException.class, lista::obtenerPrimero);
        assertThrows(ListaVaciaException.class, lista::obtenerUltimo);
    }

    @Test
    void iteradorDetectaModificacionEstructural() {
        ListaDoblementeEnlazada<String> lista = new ListaDoblementeEnlazada<>();
        lista.agregarUltimo("a");
        Iterator<String> iterador = lista.iterator();
        lista.agregarUltimo("b");
        assertThrows(ConcurrentModificationException.class, iterador::hasNext);
        assertThrows(ConcurrentModificationException.class, iterador::next);
    }

    @Test
    void iteradorAgotadoLanzaExcepcionEstandar() {
        ListaDoblementeEnlazada<Integer> lista = new ListaDoblementeEnlazada<>();
        Iterator<Integer> iterador = lista.iterator();
        assertThrows(NoSuchElementException.class, iterador::next);
    }

    @Test
    void rechazaValoresNulos() {
        ListaDoblementeEnlazada<String> lista = new ListaDoblementeEnlazada<>();
        assertThrows(NullPointerException.class, () -> lista.agregarPrimero(null));
        assertThrows(NullPointerException.class, () -> lista.agregarUltimo(null));
        assertThrows(NullPointerException.class, () -> lista.contiene(null));
        assertThrows(NullPointerException.class, () -> lista.eliminar(null));
    }

    private static <T> String recorrer(Iterator<T> iterador) {
        StringBuilder resultado = new StringBuilder();
        while (iterador.hasNext()) {
            resultado.append(iterador.next()).append(',');
        }
        return resultado.toString();
    }
}
