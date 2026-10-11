package co.edu.uniquindio.poo.servicios;

import co.edu.uniquindio.poo.modelo.Repartidor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestiona el registro, la consulta y la disponibilidad
 * de los repartidores de QuindíoExpress.
 */
public class GestionRepartidores {

    /**
     * Asocia identificaciones con repartidores.
     * LinkedHashMap conserva el orden de registro.
     */
    private final Map<String, Repartidor> repartidores;

    public GestionRepartidores() {
        this.repartidores = new LinkedHashMap<>();
    }

    /**
     * Registra un repartidor si su identificación no existe.
     *
     * @param repartidor repartidor a registrar
     * @return true si se registró, false si la identificación ya existe
     */
    public boolean registrarRepartidor(Repartidor repartidor) {

        if (repartidor == null) {
            throw new IllegalArgumentException(
                    "El repartidor no puede ser nulo.");
        }

        String identificacion = repartidor.getIdentificacion();

        if (repartidores.containsKey(identificacion)) {
            return false;
        }

        repartidores.put(identificacion, repartidor);

        return true;
    }

    /**
     * Busca un repartidor por identificación.
     *
     * @param identificacion identificación del repartidor
     * @return repartidor encontrado o null si no existe
     */
    public Repartidor buscarPorIdentificacion(String identificacion) {

        if (identificacion == null) {
            return null;
        }

        return repartidores.get(identificacion.trim());
    }

    /**
     * Retorna repartidores en el orden en que fueron registrados.
     *
     * @return lista de repartidores
     */
    public List<Repartidor> obtenerTodos() {
        return new ArrayList<>(repartidores.values());
    }

    /**
     * Retorna únicamente repartidores disponibles.
     *
     * @return lista de repartidores disponibles
     */
    public List<Repartidor> obtenerDisponibles() {

        List<Repartidor> disponibles = new ArrayList<>();

        for (Repartidor repartidor : repartidores.values()) {

            if (repartidor.isDisponible()) {
                disponibles.add(repartidor);
            }
        }

        return disponibles;
    }

    /**
     * Retorna la cantidad total de repartidores registrados.
     *
     * @return cantidad de repartidores
     */
    public int obtenerCantidadRepartidores() {
        return repartidores.size();
    }
}