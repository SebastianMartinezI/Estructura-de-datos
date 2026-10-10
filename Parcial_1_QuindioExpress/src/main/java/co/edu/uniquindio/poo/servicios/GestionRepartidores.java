package co.edu.uniquindio.poo.servicios;

import co.edu.uniquindio.poo.modelo.Repartidor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestionRepartidores {

    private final Map<String, Repartidor> repartidores;

    public GestionRepartidores() {
        repartidores = new HashMap<>();
    }

    public boolean registrarRepartidor(Repartidor repartidor) {
        if (repartidor == null) {
            throw new IllegalArgumentException(
                    "El repartidor no puede ser nulo.");
        }

        String id = repartidor.getIdentificacion();

        if (repartidores.containsKey(id)) {
            return false;
        }

        repartidores.put(id, repartidor);
        return true;
    }

    public Repartidor buscarPorIdentificacion(String identificacion) {
        if (identificacion == null) {
            return null;
        }
        return repartidores.get(identificacion.trim());
    }

    public List<Repartidor> obtenerTodos() {
        return new ArrayList<>(repartidores.values());
    }
}