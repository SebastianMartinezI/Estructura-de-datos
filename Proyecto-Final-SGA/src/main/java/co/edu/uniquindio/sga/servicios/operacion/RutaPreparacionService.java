package co.edu.uniquindio.sga.servicios.operacion;

import co.edu.uniquindio.sga.estructuras.grafo.Grafo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Representa el mapa del edificio y calcula rutas de preparación. */
public final class RutaPreparacionService {

    private static final String RECEPCION = "RECEPCION";

    private final Grafo<String> mapa;

    public RutaPreparacionService() {
        mapa = new Grafo<>();

        construirMapa();
    }

    private void construirMapa() {
        mapa.agregarVertice(RECEPCION);
        mapa.agregarVertice("ESCALERA_1");
        mapa.agregarVertice("ASEO_1");

        mapa.agregarVertice("A-101");
        mapa.agregarVertice("A-102");
        mapa.agregarVertice("A-103");

        mapa.agregarVertice("A-201");
        mapa.agregarVertice("A-202");
        mapa.agregarVertice("A-203");

        mapa.conectar(RECEPCION, "ESCALERA_1", 10);
        mapa.conectar(RECEPCION, "ASEO_1", 8);

        mapa.conectar("ESCALERA_1", "A-101", 6);
        mapa.conectar("A-101", "A-102", 4);
        mapa.conectar("A-102", "A-103", 4);

        mapa.conectar("ESCALERA_1", "A-201", 8);
        mapa.conectar("A-201", "A-202", 4);
        mapa.conectar("A-202", "A-203", 4);

        mapa.conectar("ASEO_1", "A-101", 7);
        mapa.conectar("ASEO_1", "A-201", 9);
    }

    public boolean existeApartamento(String codigoApartamento) {
        Objects.requireNonNull(
                codigoApartamento,
                "El código del apartamento es obligatorio."
        );

        return mapa.contieneVertice(codigoApartamento.trim());
    }

    public Grafo.Ruta<String> calcularRuta(String codigoApartamento) {
        Objects.requireNonNull(
                codigoApartamento,
                "El código del apartamento es obligatorio."
        );

        String destino = codigoApartamento.trim();

        if (!existeApartamento(destino)) {
            throw new IllegalArgumentException(
                    "No existe el apartamento en el mapa: " + destino
            );
        }

        return mapa.rutaMasCorta(RECEPCION, destino);
    }

    public List<String> obtenerRecorrido(String codigoApartamento) {
        Grafo.Ruta<String> ruta = calcularRuta(codigoApartamento);

        if (!ruta.esAlcanzable()) {
            throw new IllegalArgumentException(
                    "No existe una ruta hacia el apartamento: "
                            + codigoApartamento
            );
        }

        List<String> recorrido = new ArrayList<>();

        for (String nodo : ruta) {
            recorrido.add(nodo);
        }

        return recorrido;
    }

    public double obtenerDistancia(String codigoApartamento) {
        Grafo.Ruta<String> ruta = calcularRuta(codigoApartamento);

        if (!ruta.esAlcanzable()) {
            throw new IllegalArgumentException(
                    "No existe una ruta hacia el apartamento: "
                            + codigoApartamento
            );
        }

        return ruta.getDistancia();
    }
}