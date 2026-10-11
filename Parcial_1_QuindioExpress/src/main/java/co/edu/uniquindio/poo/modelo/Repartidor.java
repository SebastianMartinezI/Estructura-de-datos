package co.edu.uniquindio.poo.modelo;

/**
 * Representa un repartidor registrado en QuindíoExpress.
 */
public class Repartidor {

    private final String identificacion;
    private final String nombre;
    private final String zona;
    private boolean disponible;

    public Repartidor(String identificacion,
                      String nombre,
                      String zona,
                      boolean disponible) {

        if (identificacion == null || identificacion.isBlank()) {
            throw new IllegalArgumentException(
                    "La identificación no puede estar vacía.");
        }

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre no puede estar vacío.");
        }

        if (zona == null || zona.isBlank()) {
            throw new IllegalArgumentException(
                    "La zona no puede estar vacía.");
        }

        this.identificacion = identificacion.trim();
        this.nombre = nombre.trim();
        this.zona = zona.trim();
        this.disponible = disponible;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getZona() {
        return zona;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {

        String estado = disponible
                ? "Disponible"
                : "No disponible";

        return String.format(
                "[%-8s] %-22s Zona: %-12s %s",
                identificacion,
                nombre,
                zona,
                estado);
    }
}