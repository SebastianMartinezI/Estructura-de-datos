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

/**
 * Gestiona la preparación de colas, asignación de paquetes,
 * registro de entregas e historial de la jornada.
 */
public class GestionDespachos {

    private final GestionPaquetes gestionPaquetes;
    private final GestionRepartidores gestionRepartidores;

    /**
     * Cola FIFO que conserva el orden de llegada de los paquetes.
     */
    private final Queue<Paquete> colaLlegada;

    /**
     * Cola que organiza paquetes por prioridad de despacho.
     */
    private final PriorityQueue<Paquete> colaPrioridad;

    /**
     * Lista enlazada que conserva el orden real de las entregas.
     */
    private final ListaSimpleEnlazadas<Paquete> historial;

    /**
     * Relación entre código del paquete y repartidor asignado.
     */
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

    /**
     * Limpia y carga ambas colas con los paquetes que permanecen pendientes.
     */
    public void prepararColas() {

        colaLlegada.clear();
        colaPrioridad.clear();

        for (Paquete paquete : gestionPaquetes.obtenerPendientes()) {
            colaLlegada.offer(paquete);
            colaPrioridad.offer(paquete);
        }
    }

    /**
     * Obtiene y retira el siguiente paquete según el orden de llegada.
     *
     * @return paquete pendiente o null si no hay paquetes
     */
    public Paquete siguientePorLlegada() {
        return extraerPendiente(colaLlegada);
    }

    /**
     * Obtiene y retira el siguiente paquete según prioridad de despacho.
     *
     * @return paquete pendiente o null si no hay paquetes
     */
    public Paquete siguientePrioritario() {
        return extraerPendiente(colaPrioridad);
    }

    /**
     * Consulta el siguiente paquete por llegada sin retirarlo.
     *
     * @return paquete pendiente o null si no hay paquetes
     */
    public Paquete verSiguientePorLlegada() {

        descartarEntregados(colaLlegada);

        return colaLlegada.peek();
    }

    /**
     * Consulta el siguiente paquete por prioridad sin retirarlo.
     *
     * @return paquete pendiente o null si no hay paquetes
     */
    public Paquete verSiguientePrioritario() {

        descartarEntregados(colaPrioridad);

        return colaPrioridad.peek();
    }

    /**
     * Elimina de la cabeza de la cola los paquetes que ya fueron entregados.
     */
    private void descartarEntregados(Queue<Paquete> cola) {

        while (!cola.isEmpty()
                && !gestionPaquetes.estaPendiente(
                cola.peek().getCodigo())) {

            cola.poll();
        }
    }

    /**
     * Obtiene y retira el siguiente paquete pendiente de una cola.
     */
    private Paquete extraerPendiente(Queue<Paquete> cola) {

        descartarEntregados(cola);

        return cola.poll();
    }

    /**
     * Asigna un paquete pendiente a un repartidor disponible cuya zona
     * coincide con el destino del paquete.
     *
     * @param codigoPaquete código del paquete
     * @param identificacionRepartidor identificación del repartidor
     */
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

    /**
     * Marca como entregado un paquete que ya tiene repartidor asignado.
     * Lo agrega al historial, elimina su asignación y vuelve disponible
     * al repartidor correspondiente.
     *
     * @param codigoPaquete código del paquete entregado
     * @return true si la entrega se registró correctamente
     */
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

    /**
     * Retorna la cantidad actual de paquetes pendientes.
     *
     * @return total de paquetes sin entregar
     */
    public int obtenerCantidadPendientes() {
        return gestionPaquetes.obtenerPendientes().size();
    }

    /**
     * Consulta el repartidor asignado a un paquete.
     *
     * @param codigoPaquete código del paquete
     * @return repartidor asignado o null si no tiene asignación
     */
    public Repartidor obtenerRepartidorAsignado(String codigoPaquete) {

        if (codigoPaquete == null) {
            return null;
        }

        return asignaciones.get(codigoPaquete.trim());
    }

    /**
     * Retorna una copia de las asignaciones activas.
     *
     * @return mapa de códigos de paquete y repartidores asignados
     */
    public Map<String, Repartidor> obtenerAsignaciones() {
        return new HashMap<>(asignaciones);
    }

    /**
     * Retorna los paquetes entregados en orden de entrega.
     *
     * @return historial de paquetes entregados
     */
    public Iterable<Paquete> obtenerHistorial() {
        return historial;
    }

    /**
     * Obtiene la cantidad de paquetes entregados.
     *
     * @return número de paquetes en el historial
     */
    public int obtenerCantidadEntregados() {
        return historial.tamanio();
    }
}