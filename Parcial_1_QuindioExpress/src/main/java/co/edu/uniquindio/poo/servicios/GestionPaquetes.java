package co.edu.uniquindio.poo.servicios;

import co.edu.uniquindio.poo.modelo.Paquete;

import java.util.ArrayList;
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
 * Utiliza:
 * - ArrayList para conservar el orden de llegada.
 * - HashMap para buscar paquetes por código.
 * - HashSet para municipios únicos.
 * - TreeSet para municipios ordenados.
 *
 * @author Juan Martinez
 */
public class GestionPaquetes {

    /**
     * Conserva los paquetes en orden de registro.
     */
    private final List<Paquete> paquetesRegistrados;

    /**
     * Permite consultar paquetes rápidamente por código.
     */
    private final Map<String, Paquete> paquetesPorCodigo;

    /**
     * Contiene los códigos de paquetes que aún no han sido entregados.
     */
    private final Set<String> codigosPendientes;

    /**
     * Conserva municipios sin elementos repetidos.
     */
    private final Set<String> municipios;

    /**
     * Conserva municipios sin repetición y ordenados alfabéticamente.
     */
    private final Set<String> municipiosOrdenados;

    public GestionPaquetes() {

        this.paquetesRegistrados = new ArrayList<>();
        this.paquetesPorCodigo = new HashMap<>();
        this.codigosPendientes = new HashSet<>();
        this.municipios = new HashSet<>();
        this.municipiosOrdenados = new TreeSet<>();
    }

    /**
     * Registra un paquete si no existe otro con su mismo código.
     *
     * @param paquete paquete que se desea registrar
     * @return true si fue registrado, false si el código ya existe
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
        municipios.add(paquete.getDestino());
        municipiosOrdenados.add(paquete.getDestino());

        return true;
    }

    /**
     * Busca un paquete registrado por su código.
     *
     * @param codigo código del paquete
     * @return paquete encontrado o null si no existe
     */
    public Paquete buscarPorCodigo(String codigo) {

        if (codigo == null) {
            return null;
        }

        return paquetesPorCodigo.get(codigo.trim().toUpperCase());
    }

    /**
     * Retorna una copia de los paquetes en orden de llegada.
     *
     * @return lista de paquetes registrados
     */
    public List<Paquete> obtenerOrdenLlegada() {
        return new ArrayList<>(paquetesRegistrados);
    }

    /**
     * Retorna paquetes pendientes conservando el orden de llegada.
     *
     * @return lista de paquetes que aún no se han entregado
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
     * Determina si un paquete todavía está pendiente.
     *
     * @param codigo código del paquete
     * @return true si el paquete está pendiente
     */
    public boolean estaPendiente(String codigo) {

        if (codigo == null) {
            return false;
        }

        return codigosPendientes.contains(codigo.trim().toUpperCase());
    }

    /**
     * Marca como entregado un paquete pendiente.
     *
     * @param codigo código del paquete
     * @return true si pasó de pendiente a entregado
     */
    public boolean marcarEntregado(String codigo) {

        if (codigo == null) {
            return false;
        }

        return codigosPendientes.remove(codigo.trim().toUpperCase());
    }

    /**
     * Retorna municipios únicos sin un orden garantizado.
     *
     * @return copia del conjunto de municipios
     */
    public Set<String> obtenerMunicipios() {
        return new HashSet<>(municipios);
    }

    /**
     * Retorna municipios únicos ordenados alfabéticamente.
     *
     * @return copia ordenada del conjunto de municipios
     */
    public Set<String> obtenerMunicipiosOrdenados() {
        return new TreeSet<>(municipiosOrdenados);
    }

    /**
     * Agrupa los paquetes registrados por destino.
     * El orden de llegada se mantiene en cada grupo.
     *
     * @return mapa con destino como clave y lista de paquetes como valor
     */
    public Map<String, List<Paquete>> agruparPorDestino() {

        Map<String, List<Paquete>> agrupados = new HashMap<>();

        for (Paquete paquete : paquetesRegistrados) {

            agrupados.computeIfAbsent(
                    paquete.getDestino(),
                    destino -> new ArrayList<>()
            ).add(paquete);
        }

        return agrupados;
    }

    /**
     * Retorna la cantidad total de paquetes registrados.
     *
     * @return cantidad de paquetes registrados
     */
    public int obtenerCantidadRegistrados() {
        return paquetesRegistrados.size();
    }

    /**
     * Retorna la cantidad actual de paquetes pendientes.
     *
     * @return cantidad de paquetes pendientes
     */
    public int obtenerCantidadPendientes() {
        return codigosPendientes.size();
    }
}