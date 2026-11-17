package co.edu.uniquindio.sga.Estruct;

import co.edu.uniquindio.sga.estructuras.circular.ListaCircular;
import co.edu.uniquindio.sga.excepciones.EstructuraVaciaException;
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ListaCircularTest {

    @Test
    void nuevaListaEstaVacia() {
        ListaCircular<String> lista = new ListaCircular<>();

        assertTrue(lista.estaVacia());
        assertEquals(0, lista.tamano());
    }

    @Test
    void agregarConservaElActual() {
        ListaCircular<String> lista = new ListaCircular<>();

        lista.agregar("Ana");
        lista.agregar("Luis");
        lista.agregar("Marta");

        assertEquals("Ana", lista.verActual());
        assertEquals(3, lista.tamano());
    }

    @Test
    void turnosVuelvenAlInicio() {
        ListaCircular<String> lista = new ListaCircular<>();

        lista.agregar("Ana");
        lista.agregar("Luis");
        lista.agregar("Marta");

        assertEquals("Ana", lista.verActual());
        assertEquals("Luis", lista.avanzar());
        assertEquals("Marta", lista.avanzar());
        assertEquals("Ana", lista.avanzar());
    }

    @Test
    void unSoloElementoConservaSuTurno() {
        ListaCircular<String> lista = new ListaCircular<>();
        lista.agregar("Ana");

        assertEquals("Ana", lista.avanzar());
        assertEquals("Ana", lista.avanzar());
        assertEquals(1, lista.tamano());
    }

    @Test
    void consultarOAvanzarVaciaLanzaExcepcion() {
        ListaCircular<String> lista = new ListaCircular<>();

        assertThrows(
                EstructuraVaciaException.class,
                () -> lista.verActual()
        );

        assertThrows(
                EstructuraVaciaException.class,
                () -> lista.avanzar()
        );
    }

    @Test
    void contieneEncuentraElementos() {
        ListaCircular<String> lista = new ListaCircular<>();

        lista.agregar("Ana");
        lista.agregar("Luis");

        assertTrue(lista.contiene("Ana"));
        assertTrue(lista.contiene("Luis"));
        assertFalse(lista.contiene("Marta"));
    }

    @Test
    void eliminaElActualYDejaAlSiguiente() {
        ListaCircular<String> lista = new ListaCircular<>();

        lista.agregar("Ana");
        lista.agregar("Luis");
        lista.agregar("Marta");

        assertTrue(lista.eliminar("Ana"));
        assertEquals("Luis", lista.verActual());
        assertEquals(2, lista.tamano());

        assertEquals("Marta", lista.avanzar());
        assertEquals("Luis", lista.avanzar());
    }

    @Test
    void eliminaElUltimoConservandoElCirculo() {
        ListaCircular<String> lista = new ListaCircular<>();

        lista.agregar("Ana");
        lista.agregar("Luis");
        lista.agregar("Marta");

        assertTrue(lista.eliminar("Marta"));
        assertEquals("Ana", lista.verActual());
        assertEquals("Luis", lista.avanzar());
        assertEquals("Ana", lista.avanzar());
    }

    @Test
    void eliminaUnElementoIntermedio() {
        ListaCircular<String> lista = new ListaCircular<>();

        lista.agregar("Ana");
        lista.agregar("Luis");
        lista.agregar("Marta");

        assertTrue(lista.eliminar("Luis"));
        assertEquals("Ana", lista.verActual());
        assertEquals("Marta", lista.avanzar());
        assertEquals("Ana", lista.avanzar());
    }

    @Test
    void eliminaElUnicoYPermiteAgregarDeNuevo() {
        ListaCircular<String> lista = new ListaCircular<>();
        lista.agregar("Ana");

        assertTrue(lista.eliminar("Ana"));
        assertTrue(lista.estaVacia());
        assertEquals(0, lista.tamano());

        lista.agregar("Luis");

        assertEquals("Luis", lista.verActual());
        assertEquals("Luis", lista.avanzar());
    }

    @Test
    void eliminarInexistenteNoModificaLaLista() {
        ListaCircular<String> lista = new ListaCircular<>();

        assertFalse(lista.eliminar("Ana"));

        lista.agregar("Luis");

        assertFalse(lista.eliminar("Ana"));
        assertEquals("Luis", lista.verActual());
        assertEquals(1, lista.tamano());
    }

    @Test
    void iteradorDaUnaVueltaSinCambiarTurno() {
        ListaCircular<String> lista = new ListaCircular<>();

        lista.agregar("A");
        lista.agregar("B");
        lista.agregar("C");

        lista.avanzar();

        StringBuilder resultado = new StringBuilder();

        for (String dato : lista) {
            resultado.append(dato);
        }

        assertEquals("BCA", resultado.toString());
        assertEquals("B", lista.verActual());
        assertEquals(3, lista.tamano());
    }

    @Test
    void iteradorVacioNoTieneElementos() {
        ListaCircular<String> lista = new ListaCircular<>();
        Iterator<String> iterador = lista.iterator();

        assertFalse(iterador.hasNext());

        assertThrows(
                NoSuchElementException.class,
                () -> iterador.next()
        );
    }

    @Test
    void iteradorSeAgotaDespuesDeUnaVuelta() {
        ListaCircular<String> lista = new ListaCircular<>();
        lista.agregar("Ana");

        Iterator<String> iterador = lista.iterator();

        assertEquals("Ana", iterador.next());
        assertFalse(iterador.hasNext());

        assertThrows(
                NoSuchElementException.class,
                () -> iterador.next()
        );
    }

    @Test
    void iteradorDetectaCambioDeTurno() {
        ListaCircular<String> lista = new ListaCircular<>();

        lista.agregar("Ana");
        lista.agregar("Luis");

        Iterator<String> iterador = lista.iterator();

        lista.avanzar();

        assertThrows(
                ConcurrentModificationException.class,
                () -> iterador.next()
        );
    }

    @Test
    void iteradorDetectaInsercionYEliminacion() {
        ListaCircular<String> lista = new ListaCircular<>();
        lista.agregar("Ana");

        Iterator<String> antesDeAgregar = lista.iterator();
        lista.agregar("Luis");

        assertThrows(
                ConcurrentModificationException.class,
                () -> antesDeAgregar.next()
        );

        Iterator<String> antesDeEliminar = lista.iterator();
        lista.eliminar("Ana");

        assertThrows(
                ConcurrentModificationException.class,
                () -> antesDeEliminar.next()
        );
    }

    @Test
    void rechazaNulos() {
        ListaCircular<String> lista = new ListaCircular<>();

        assertThrows(
                NullPointerException.class,
                () -> lista.agregar(null)
        );

        assertTrue(lista.estaVacia());
    }
}