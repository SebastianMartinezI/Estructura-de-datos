package co.edu.uniquindio.poo.servicios;

import co.edu.uniquindio.poo.modelo.Paquete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Administra el registro y la consulta de paquetes
 * del centro de distribución QuindíoExpress.
 *
 * Utiliza diferentes colecciones según las necesidades:
 * - ArrayList: conservar el orden de llegada.
 * - HashMap: consultar rápidamente por código.
 * - HashSet: obtener municipios sin duplicados.
 * - TreeSet: obtener municipios ordenados.
 *
 * @author Juan Martinez
 */
public class GestionPaquetes {

    /**
     * Conserva los paquetes en el orden original de registro.
     */
    private final List<Paquete> paquetesRegistrados;

    /**
     * Permite acceder a un paquete mediante su código.
     */
    private final Map<String, Paquete> paquetesPorCodigo;

    /**
     * Identifica los paquetes que siguen pendientes
     * de entrega mediante su código.
     */
    private final Set<String> codigosPendientes;

    /**
     * Inicializa las estructuras utilizadas por el sistema.
     */
    public GestionPaquetes() {
        paquetesRegistrados = new ArrayList<>();
        paquetesPorCodigo = new HashMap<>();
        codigosPendientes = new HashSet<>();
    }

    /**
     * Registra un paquete si su código no existe previamente.
     *
     * Complejidad temporal esperada: O(1) amortizado.
     * Complejidad espacial adicional: O(1) por registro.
     *
     * @param paquete paquete que se desea registrar
     * @return true si se registró; false si el código ya existe
     * @throws IllegalArgumentException si el paquete es nulo
     */
    public boolean registrarPaquete(Paquete paquete) {

        if (paquete == null) {
            throw new IllegalArgumentException(
                    "El paquete no puede ser nulo.");
        }

        String codigo = paquete.getCodigo();

        if (paquetesPorCodigo.containsKey(codigo)) {
            return false;
        }

        paquetesRegistrados.add(paquete);
        paquetesPorCodigo.put(codigo, paquete);
        codigosPendientes.add(codigo);

        return true;
    }

    /**
     * Busca un paquete registrado mediante su código.
     *
     * Complejidad temporal esperada: O(1).
     *
     * @param codigo identificador del paquete
     * @return paquete encontrado o null si no existe
     */
    public Paquete buscarPorCodigo(String codigo) {

        if (codigo == null) {
            return null;
        }

        return paquetesPorCodigo.get(codigo.trim());
    }

    /**
     * Obtiene una copia de los paquetes en el orden
     * original en que fueron registrados.
     *
     * Complejidad temporal: O(n).
     *
     * @return lista con los paquetes registrados
     */
    public List<Paquete> obtenerOrdenLlegada() {
        return new ArrayList<>(paquetesRegistrados);
    }

    /**
     * Consulta los paquetes que todavía no han sido
     * entregados, conservando su orden de registro.
     *
     * Complejidad temporal esperada: O(n).
     *
     * @return lista de paquetes pendientes
     */
    public List<Paquete> obtenerPendientes() {

        List<Paquete> pendientes = new ArrayList<>();

        for (Paquete paquete : paquetesRegistrados) {

            if (codigosPendientes.contains(paquete.getCodigo())) {
                pendientes.add(paquete);
            }
        }

        return pendientes;
    }

    /**
     * Determina si un paquete permanece pendiente.
     *
     * Complejidad temporal esperada: O(1).
     *
     * @param codigo identificador del paquete
     * @return true si está pendiente
     */
    public boolean estaPendiente(String codigo) {

        if (codigo == null) {
            return false;
        }

        return codigosPendientes.contains(codigo.trim());
    }

    /**
     * Marca como entregado un paquete que estaba pendiente.
     *
     * Este método será utilizado posteriormente por
     * GestionDespachos al confirmar una entrega.
     *
     * Complejidad temporal esperada: O(1).
     *
     * @param codigo identificador del paquete
     * @return true si cambió de pendiente a entregado
     */
    public boolean marcarEntregado(String codigo) {

        if (codigo == null) {
            return false;
        }

        return codigosPendientes.remove(codigo.trim());
    }

    /**
     * Obtiene los municipios destino sin duplicados.
     *
     * Complejidad temporal esperada: O(n).
     *
     * @return conjunto de municipios registrados
     */
    public Set<String> obtenerMunicipios() {

        Set<String> municipios = new HashSet<>();

        for (Paquete paquete : paquetesRegistrados) {
            municipios.add(paquete.getDestino());
        }

        return municipios;
    }

    /**
     * Obtiene los municipios destino ordenados
     * alfabéticamente y sin duplicados.
     *
     * Complejidad temporal: O(n log m),
     * donde m es el número de municipios distintos.
     *
     * @return conjunto ordenado de municipios
     */
    public Set<String> obtenerMunicipiosOrdenados() {

        Set<String> municipios = new TreeSet<>();

        for (Paquete paquete : paquetesRegistrados) {
            municipios.add(paquete.getDestino());
        }

        return municipios;
    }

    /**
     * Agrupa los paquetes registrados según su municipio.
     *
     * Conserva el orden de llegada dentro de cada grupo.
     *
     * Complejidad temporal esperada: O(n).
     *
     * @return mapa que relaciona cada municipio
     *         con sus paquetes registrados
     */
    public Map<String, List<Paquete>> agruparPorDestino() {

        Map<String, List<Paquete>> agrupados = new HashMap<>();

        for (Paquete paquete : paquetesRegistrados) {

            String destino = paquete.getDestino();

            agrupados.computeIfAbsent(
                    destino, clave -> new ArrayList<>()
            ).add(paquete);
        }

        return agrupados;
    }

    /**
     * Obtiene el total de paquetes registrados.
     *
     * Complejidad temporal: O(1).
     *
     * @return cantidad de paquetes
     */
    public int obtenerCantidadRegistrados() {
        return paquetesRegistrados.size();
    }

    /**
     * Obtiene la cantidad de paquetes pendientes.
     *
     * Complejidad temporal esperada: O(1).
     *
     * @return cantidad de pendientes
     */
    public int obtenerCantidadPendientes() {
        return codigosPendientes.size();
    }
}
