package co.edu.uniquindio.sga.estructuras.arbol;

import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ArbolBinarioBusquedaTest {

    @Test
    void arbolVacioTieneAlturaCero() {
        ArbolBinarioBusqueda<Integer> arbol = new ArbolBinarioBusqueda<>();
        assertTrue(arbol.estaVacio());
        assertEquals(0, arbol.tamano());
        assertEquals(0, arbol.altura());
        assertNull(arbol.buscar(10));
        assertFalse(arbol.eliminar(10));
        assertFalse(arbol.iterator().hasNext());
    }

    @Test
    void insertaBuscaYNoDuplicaClaves() {
        ArbolBinarioBusqueda<Integer> arbol = crearArbol();
        assertEquals(7, arbol.tamano());
        assertTrue(arbol.insertar(8));
        assertFalse(arbol.insertar(8));
        assertEquals(8, arbol.tamano());
        assertEquals(Integer.valueOf(8), arbol.buscar(8));
        assertNull(arbol.buscar(99));
        assertTrue(arbol.contiene(4));
        assertFalse(arbol.contiene(99));
        assertEquals(4, arbol.altura());
    }

    @Test
    void eliminaHojaNodoConUnHijoYRaizConDosHijos() {
        ArbolBinarioBusqueda<Integer> arbol = crearArbol();
        assertTrue(arbol.eliminar(1));   // hoja
        assertTrue(arbol.eliminar(2));   // ahora tiene solo hijo derecho (3)
        assertTrue(arbol.eliminar(4));   // raíz, dos hijos: se sustituye con sucesor
        assertEquals(4, arbol.tamano());
        assertFalse(arbol.contiene(1));
        assertFalse(arbol.contiene(2));
        assertFalse(arbol.contiene(4));
        assertFalse(arbol.eliminar(200));
        assertEquals("3,5,6,7,", elementos(arbol));
    }

    @Test
    void recorreEnOrdenYConIteradorPropio() {
        ArbolBinarioBusqueda<Integer> arbol = crearArbol();
        StringBuilder porRecursion = new StringBuilder();
        arbol.recorrerEnOrden(n -> porRecursion.append(n).append(','));
        assertEquals("1,2,3,4,5,6,7,", porRecursion.toString());
        assertEquals(porRecursion.toString(), elementos(arbol));
    }

    @Test
    void recorrePreOrdenYPostOrden() {
        ArbolBinarioBusqueda<Integer> arbol = crearArbol();
        StringBuilder pre = new StringBuilder();
        StringBuilder post = new StringBuilder();
        arbol.recorrerPreOrden(n -> pre.append(n).append(','));
        arbol.recorrerPostOrden(n -> post.append(n).append(','));
        assertEquals("4,2,1,3,6,5,7,", pre.toString());
        assertEquals("1,3,2,5,7,6,4,", post.toString());
    }

    @Test
    void iteradorRechazaModificacionesDuranteRecorrido() {
        ArbolBinarioBusqueda<Integer> arbol = crearArbol();
        Iterator<Integer> iterador = arbol.iterator();
        assertEquals(Integer.valueOf(1), iterador.next());
        arbol.insertar(8);
        assertThrows(ConcurrentModificationException.class, iterador::hasNext);
    }

    @Test
    void iteradorTerminadoLanzaExcepcion() {
        ArbolBinarioBusqueda<Integer> arbol = new ArbolBinarioBusqueda<>();
        Iterator<Integer> iterador = arbol.iterator();
        assertThrows(NoSuchElementException.class, iterador::next);
    }

    @Test
    void noAceptaNulos() {
        ArbolBinarioBusqueda<String> arbol = new ArbolBinarioBusqueda<>();
        assertThrows(NullPointerException.class, () -> arbol.insertar(null));
        assertThrows(NullPointerException.class, () -> arbol.buscar(null));
        assertThrows(NullPointerException.class, () -> arbol.eliminar(null));
    }

    private ArbolBinarioBusqueda<Integer> crearArbol() {
        ArbolBinarioBusqueda<Integer> arbol = new ArbolBinarioBusqueda<>();
        int[] valores = {4, 2, 6, 1, 3, 5, 7};
        for (int valor : valores) {
            arbol.insertar(valor);
        }
        return arbol;
    }

    private String elementos(ArbolBinarioBusqueda<Integer> arbol) {
        StringBuilder resultado = new StringBuilder();
        for (int valor : arbol) {
            resultado.append(valor).append(',');
        }
        return resultado.toString();
    }
}
