package co.edu.uniquindio.poo.servicios;

import co.edu.uniquindio.poo.comparadores.ComparadoresPaquetes;
import co.edu.uniquindio.poo.estructuras.ListaSimpleEnlazadas;
import co.edu.uniquindio.poo.modelo.Paquete;
import co.edu.uniquindio.poo.modelo.Repartidor;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class GestionDespachos {

    private final GestionPaquetes gestionPaquetes;
    private final GestionRepartidores gestionRepartidores;
    private final Queue<Paquete> colaLlegada;
    private final PriorityQueue<Paquete> colaPrioridad;
    private final ListaSimpleEnlazadas<Paquete> historial;
    private final Map<String, Repartidor> asignaciones;

    public GestionDespachos(GestionPaquetes gestionPaquetes,
                            GestionRepartidores gestionRepartidores) {
        if (gestionPaquetes == null || gestionRepartidores == null) {
            throw new IllegalArgumentException(
                    "Las gestiones son obligatorias.");
        }
        this.gestionPaquetes = gestionPaquetes;
        this.gestionRepartidores = gestionRepartidores;
        this.colaLlegada = new ArrayDeque<>();
        this.colaPrioridad =
                new PriorityQueue<>(ComparadoresPaquetes.POR_DESPACHO);
        this.historial = new ListaSimpleEnlazadas<>();
        this.asignaciones = new HashMap<>();
    }

    public void prepararColas() {
        colaLlegada.clear();
        colaPrioridad.clear();

        for (Paquete paquete : gestionPaquetes.obtenerPendientes()) {
            colaLlegada.offer(paquete);
            colaPrioridad.offer(paquete);
        }
    }

    public Paquete siguientePorLlegada() {
        return extraerPendiente(colaLlegada);
    }

    public Paquete siguientePrioritario() {
        return extraerPendiente(colaPrioridad);
    }

    private Paquete extraerPendiente(Queue<Paquete> cola) {
        Paquete siguiente = cola.poll();

        while (siguiente != null
                && !gestionPaquetes.estaPendiente(siguiente.getCodigo())) {
            siguiente = cola.poll();
        }

        return siguiente;
    }

    public void asignarPaquete(String codigoPaquete,
                               String identificacionRepartidor) {

        Paquete paquete = gestionPaquetes.buscarPorCodigo(codigoPaquete);

        if (paquete == null) {
            throw new IllegalArgumentException("No existe el paquete.");
        }
        if (!gestionPaquetes.estaPendiente(paquete.getCodigo())) {
            throw new IllegalArgumentException(
                    "El paquete ya fue entregado.");
        }
        if (asignaciones.containsKey(paquete.getCodigo())) {
            throw new IllegalArgumentException(
                    "El paquete ya tiene repartidor asignado.");
        }

        Repartidor repartidor = gestionRepartidores
                .buscarPorIdentificacion(identificacionRepartidor);

        if (repartidor == null) {
            throw new IllegalArgumentException("No existe el repartidor.");
        }
        if (!repartidor.isDisponible()) {
            throw new IllegalArgumentException(
                    "El repartidor no está disponible.");
        }
        if (!repartidor.getZona().equalsIgnoreCase(paquete.getDestino())) {
            throw new IllegalArgumentException(
                    "La zona del repartidor no coincide con el destino.");
        }

        repartidor.setDisponible(false);
        asignaciones.put(paquete.getCodigo(), repartidor);
    }

    public boolean registrarEntrega(String codigoPaquete) {

        Paquete paquete = gestionPaquetes.buscarPorCodigo(codigoPaquete);

        if (paquete == null) {
            throw new IllegalArgumentException("No existe el paquete.");
        }

        Repartidor repartidor = asignaciones.get(paquete.getCodigo());

        if (repartidor == null) {
            throw new IllegalArgumentException(
                    "El paquete no tiene repartidor asignado.");
        }

        if (!gestionPaquetes.marcarEntregado(paquete.getCodigo())) {
            return false;
        }

        historial.agregar(paquete);
        asignaciones.remove(paquete.getCodigo());
        repartidor.setDisponible(true);
        return true;
    }

    public Iterable<Paquete> obtenerHistorial() {
        return historial;
    }

    public int obtenerCantidadEntregados() {
        return historial.tamanio();
    }
}