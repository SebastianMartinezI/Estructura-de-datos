package co.edu.uniquindio.sga.estructuras.grafo;

import co.edu.uniquindio.sga.estructuras.pila.Pila;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

public class Grafo<T> implements Iterable<T> {

    private Object[] vertices;
    private double[][] pesos;

    private int cantidad;
    private int modificaciones;

    public Grafo() {
        vertices = new Object[8];
        pesos = crearMatriz(8);
    }

    private double[][] crearMatriz(int capacidad) {
        double[][] matriz = new double[capacidad][capacidad];

        for (int i = 0; i < capacidad; i++) {
            for (int j = 0; j < capacidad; j++) {
                matriz[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        return matriz;
    }

    public boolean agregarVertice(T dato) {
        Objects.requireNonNull(dato, "El vértice no puede ser nulo");

        if (contieneVertice(dato)) {
            return false;
        }

        asegurarCapacidad();

        vertices[cantidad] = dato;
        cantidad++;
        modificaciones++;

        return true;
    }

    private void asegurarCapacidad() {
        if (cantidad < vertices.length) {
            return;
        }

        int nuevaCapacidad = vertices.length * 2;

        Object[] nuevosVertices = new Object[nuevaCapacidad];
        double[][] nuevosPesos = crearMatriz(nuevaCapacidad);

        for (int i = 0; i < cantidad; i++) {
            nuevosVertices[i] = vertices[i];

            for (int j = 0; j < cantidad; j++) {
                nuevosPesos[i][j] = pesos[i][j];
            }
        }

        vertices = nuevosVertices;
        pesos = nuevosPesos;
    }

    public boolean contieneVertice(T dato) {
        return indiceDe(dato) >= 0;
    }

    private int indiceDe(T dato) {
        for (int i = 0; i < cantidad; i++) {
            if (Objects.equals(vertices[i], dato)) {
                return i;
            }
        }

        return -1;
    }

    private int exigirIndice(T dato) {
        int indice = indiceDe(dato);

        if (indice < 0) {
            throw new IllegalArgumentException(
                    "No existe el vértice: " + dato
            );
        }

        return indice;
    }

    @SuppressWarnings("unchecked")
    private T verticeEn(int indice) {
        return (T) vertices[indice];
    }

    public void conectar(T origen, T destino, double peso) {
        if (!Double.isFinite(peso) || peso < 0) {
            throw new IllegalArgumentException(
                    "El peso debe ser finito y no negativo"
            );
        }

        int i = exigirIndice(origen);
        int j = exigirIndice(destino);

        if (i == j) {
            throw new IllegalArgumentException(
                    "No se permiten conexiones al mismo vértice"
            );
        }

        pesos[i][j] = peso;
        pesos[j][i] = peso;
        modificaciones++;
    }

    public void desconectar(T origen, T destino) {
        int i = exigirIndice(origen);
        int j = exigirIndice(destino);

        if (Double.isFinite(pesos[i][j])) {
            pesos[i][j] = Double.POSITIVE_INFINITY;
            pesos[j][i] = Double.POSITIVE_INFINITY;
            modificaciones++;
        }
    }

    public boolean estanConectados(T origen, T destino) {
        int i = exigirIndice(origen);
        int j = exigirIndice(destino);

        return Double.isFinite(pesos[i][j]);
    }

    public double obtenerPeso(T origen, T destino) {
        int i = exigirIndice(origen);
        int j = exigirIndice(destino);

        return pesos[i][j];
    }

    public int tamano() {
        return cantidad;
    }

    public boolean estaVacio() {
        return cantidad == 0;
    }

    public void recorrerBfs(T origen, Consumer<? super T> accion) {
        Objects.requireNonNull(accion, "La acción no puede ser nula");

        int inicio = exigirIndice(origen);
        int version = modificaciones;

        boolean[] visitados = new boolean[cantidad];
        int[] cola = new int[cantidad];

        int frente = 0;
        int fin = 0;

        cola[fin++] = inicio;
        visitados[inicio] = true;

        while (frente < fin) {
            int actual = cola[frente++];

            accion.accept(verticeEn(actual));
            comprobarVersion(version);

            for (int vecino = 0; vecino < cantidad; vecino++) {
                if (Double.isFinite(pesos[actual][vecino])
                        && !visitados[vecino]) {

                    visitados[vecino] = true;
                    cola[fin++] = vecino;
                }
            }
        }
    }

    public void recorrerDfs(T origen, Consumer<? super T> accion) {
        Objects.requireNonNull(accion, "La acción no puede ser nula");

        int inicio = exigirIndice(origen);
        boolean[] visitados = new boolean[cantidad];

        dfsRecursivo(inicio, visitados, accion, modificaciones);
    }

    private void dfsRecursivo(
            int actual,
            boolean[] visitados,
            Consumer<? super T> accion,
            int version
    ) {
        visitados[actual] = true;

        accion.accept(verticeEn(actual));
        comprobarVersion(version);

        for (int vecino = 0; vecino < cantidad; vecino++) {
            if (Double.isFinite(pesos[actual][vecino])
                    && !visitados[vecino]) {

                dfsRecursivo(
                        vecino, visitados, accion, version
                );
            }
        }
    }

    public Ruta<T> rutaMasCorta(T origen, T destino) {
        int inicio = exigirIndice(origen);
        int fin = exigirIndice(destino);

        double[] distancias = new double[cantidad];
        int[] anteriores = new int[cantidad];
        boolean[] visitados = new boolean[cantidad];

        for (int i = 0; i < cantidad; i++) {
            distancias[i] = Double.POSITIVE_INFINITY;
            anteriores[i] = -1;
        }

        distancias[inicio] = 0;

        for (int paso = 0; paso < cantidad; paso++) {
            int actual = seleccionarMenor(distancias, visitados);

            if (actual == -1) {
                break;
            }

            visitados[actual] = true;

            if (actual == fin) {
                break;
            }

            for (int vecino = 0; vecino < cantidad; vecino++) {
                if (visitados[vecino]
                        || !Double.isFinite(pesos[actual][vecino])) {
                    continue;
                }

                double candidata =
                        distancias[actual] + pesos[actual][vecino];

                if (candidata < distancias[vecino]) {
                    distancias[vecino] = candidata;
                    anteriores[vecino] = actual;
                }
            }
        }

        Pila<T> camino = new Pila<>();

        if (Double.isFinite(distancias[fin])) {
            int actual = fin;

            while (actual != -1) {
                camino.apilar(verticeEn(actual));
                actual = anteriores[actual];
            }
        }

        return new Ruta<>(camino, distancias[fin]);
    }

    private int seleccionarMenor(
            double[] distancias,
            boolean[] visitados
    ) {
        int seleccionado = -1;
        double menor = Double.POSITIVE_INFINITY;

        for (int i = 0; i < cantidad; i++) {
            if (!visitados[i] && distancias[i] < menor) {
                menor = distancias[i];
                seleccionado = i;
            }
        }

        return seleccionado;
    }

    private void comprobarVersion(int esperada) {
        if (esperada != modificaciones) {
            throw new ConcurrentModificationException(
                    "El grafo cambió durante el recorrido"
            );
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private int posicion;
            private final int version = modificaciones;

            @Override
            public boolean hasNext() {
                comprobarVersion(version);
                return posicion < cantidad;
            }

            @Override
            public T next() {
                comprobarVersion(version);

                if (posicion >= cantidad) {
                    throw new NoSuchElementException(
                            "No hay más vértices"
                    );
                }

                return verticeEn(posicion++);
            }
        };
    }

    public static final class Ruta<E> implements Iterable<E> {

        private final Pila<E> camino;
        private final double distancia;

        private Ruta(Pila<E> camino, double distancia) {
            this.camino = camino;
            this.distancia = distancia;
        }

        public boolean esAlcanzable() {
            return Double.isFinite(distancia);
        }

        public double getDistancia() {
            return distancia;
        }

        @Override
        public Iterator<E> iterator() {
            return camino.iterator();
        }
    }
}