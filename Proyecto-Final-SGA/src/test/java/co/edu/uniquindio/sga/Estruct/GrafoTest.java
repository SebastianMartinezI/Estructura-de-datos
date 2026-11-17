package co.edu.uniquindio.sga.Estruct;

import co.edu.uniquindio.sga.estructuras.grafo.Grafo;
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class GrafoTest {

    private Grafo<String> crearGrafo() {
        Grafo<String> grafo = new Grafo<>();

        grafo.agregarVertice("R");
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarVertice("X");

        grafo.conectar("R", "A", 10);
        grafo.conectar("R", "B", 2);
        grafo.conectar("B", "A", 3);
        grafo.conectar("A", "C", 1);
        grafo.conectar("B", "C", 20);

        return grafo;
    }

    @Test
    void nuevoGrafoEstaVacio() {
        Grafo<String> grafo = new Grafo<>();

        assertTrue(grafo.estaVacio());
        assertEquals(0, grafo.tamano());
    }

    @Test
    void noRepiteVertices() {
        Grafo<String> grafo = new Grafo<>();

        assertTrue(grafo.agregarVertice("A"));
        assertFalse(grafo.agregarVertice("A"));
        assertEquals(1, grafo.tamano());
    }

    @Test
    void conexionesSonBidireccionales() {
        Grafo<String> grafo = crearGrafo();

        assertTrue(grafo.estanConectados("R", "B"));
        assertTrue(grafo.estanConectados("B", "R"));
        assertEquals(2.0, grafo.obtenerPeso("B", "R"), 0.0001);
    }

    @Test
    void dijkstraEligeMenorPesoNoMenosAristas() {
        Grafo<String> grafo = crearGrafo();

        Grafo.Ruta<String> ruta = grafo.rutaMasCorta("R", "C");

        StringBuilder camino = new StringBuilder();

        for (String punto : ruta) {
            camino.append(punto);
        }

        assertTrue(ruta.esAlcanzable());
        assertEquals(6.0, ruta.getDistancia(), 0.0001);
        assertEquals("RBAC", camino.toString());
    }

    @Test
    void destinoAisladoNoTieneRuta() {
        Grafo<String> grafo = crearGrafo();

        Grafo.Ruta<String> ruta = grafo.rutaMasCorta("R", "X");

        assertFalse(ruta.esAlcanzable());
        assertEquals(
                Double.POSITIVE_INFINITY,
                ruta.getDistancia()
        );
        assertFalse(ruta.iterator().hasNext());
    }

    @Test
    void rutaAlMismoPuntoTieneDistanciaCero() {
        Grafo<String> grafo = crearGrafo();
        Grafo.Ruta<String> ruta = grafo.rutaMasCorta("R", "R");

        assertTrue(ruta.esAlcanzable());
        assertEquals(0.0, ruta.getDistancia(), 0.0001);

        Iterator<String> iterador = ruta.iterator();

        assertEquals("R", iterador.next());
        assertFalse(iterador.hasNext());
    }

    @Test
    void cerrarPasilloCambiaLaRuta() {
        Grafo<String> grafo = crearGrafo();

        grafo.desconectar("B", "A");

        assertFalse(grafo.estanConectados("B", "A"));
        assertFalse(grafo.estanConectados("A", "B"));

        assertEquals(
                11.0,
                grafo.rutaMasCorta("R", "C").getDistancia(),
                0.0001
        );
    }

    @Test
    void bfsVisitaSoloLaComponenteAlcanzable() {
        Grafo<String> grafo = crearGrafo();
        StringBuilder recorrido = new StringBuilder();

        grafo.recorrerBfs(
                "R", punto -> recorrido.append(punto)
        );

        assertEquals("RABC", recorrido.toString());
    }

    @Test
    void dfsEsRecursivoYNoRepitePorLosCiclos() {
        Grafo<String> grafo = crearGrafo();
        StringBuilder recorrido = new StringBuilder();

        grafo.recorrerDfs(
                "R", punto -> recorrido.append(punto)
        );

        assertEquals("RABC", recorrido.toString());
    }

    @Test
    void creceYConservaLasConexiones() {
        Grafo<Integer> grafo = new Grafo<>();

        grafo.agregarVertice(0);
        grafo.agregarVertice(1);
        grafo.conectar(0, 1, 7);

        for (int i = 2; i < 20; i++) {
            grafo.agregarVertice(i);
        }

        assertEquals(20, grafo.tamano());
        assertEquals(7.0, grafo.obtenerPeso(0, 1), 0.0001);
        assertTrue(grafo.contieneVertice(19));
    }

    @Test
    void rechazaPesosInvalidos() {
        Grafo<String> grafo = crearGrafo();

        assertThrows(
                IllegalArgumentException.class,
                () -> grafo.conectar("R", "A", -1)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> grafo.conectar("R", "A", Double.NaN)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> grafo.conectar(
                        "R", "A", Double.POSITIVE_INFINITY
                )
        );
    }

    @Test
    void permitePasillosConPesoCero() {
        Grafo<String> grafo = crearGrafo();

        grafo.conectar("R", "A", 0);

        assertEquals(
                1.0,
                grafo.rutaMasCorta("R", "C").getDistancia(),
                0.0001
        );
    }

    @Test
    void rechazaVerticeNuloOInexistente() {
        Grafo<String> grafo = crearGrafo();

        assertThrows(
                NullPointerException.class,
                () -> grafo.agregarVertice(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> grafo.rutaMasCorta("R", "NO_EXISTE")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> grafo.conectar("R", "NO_EXISTE", 1)
        );
    }

    @Test
    void iteradorRecorreEnOrdenDeInsercion() {
        Grafo<String> grafo = crearGrafo();
        StringBuilder resultado = new StringBuilder();

        for (String vertice : grafo) {
            resultado.append(vertice);
        }

        assertEquals("RABCX", resultado.toString());
    }

    @Test
    void iteradorAgotadoLanzaExcepcion() {
        Grafo<String> grafo = new Grafo<>();
        Iterator<String> iterador = grafo.iterator();

        assertFalse(iterador.hasNext());

        assertThrows(
                NoSuchElementException.class,
                () -> iterador.next()
        );
    }

    @Test
    void iteradorDetectaCambios() {
        Grafo<String> grafo = crearGrafo();
        Iterator<String> iterador = grafo.iterator();

        grafo.desconectar("R", "A");

        assertThrows(
                ConcurrentModificationException.class,
                () -> iterador.next()
        );
    }
}