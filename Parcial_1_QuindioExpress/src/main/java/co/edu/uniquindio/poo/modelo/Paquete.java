package co.edu.uniquindio.poo.modelo;

import java.util.Objects;

/**
 * Representa un paquete registrado en el centro de distribución
 * QuindíoExpress.
 * <p>
 * El orden natural de los paquetes se establece mediante su código
 * en orden alfabético ascendente.
 *
 * @author Juan Martinez
 */
public class Paquete implements Comparable<Paquete> {

    private final String codigo;
    private final String destino;
    private final double peso;
    private final int prioridad;
    private final int tiempoEstimado;

    /**
     * Construye un paquete validando sus datos.
     *
     * @param codigo         identificador único del paquete
     * @param destino        municipio de entrega
     * @param peso           peso del paquete en kilogramos
     * @param prioridad      nivel de prioridad entre 1 y 5
     * @param tiempoEstimado tiempo de entrega en minutos
     * @throws IllegalArgumentException si algún dato no es válido
     */
    public Paquete(String codigo, String destino, double peso,
                   int prioridad, int tiempoEstimado) {

        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "El código no puede estar vacío.");
        }

        if (destino == null || destino.isBlank()) {
            throw new IllegalArgumentException(
                    "El destino no puede estar vacío.");
        }

        if (!Double.isFinite(peso) || peso <= 0) {
            throw new IllegalArgumentException(
                    "El peso debe ser mayor que cero y finito.");
        }

        if (prioridad < 1 || prioridad > 5) {
            throw new IllegalArgumentException(
                    "La prioridad debe estar entre 1 y 5.");
        }

        if (tiempoEstimado <= 0) {
            throw new IllegalArgumentException(
                    "El tiempo estimado debe ser mayor que cero.");
        }

        this.codigo = codigo.trim();
        this.destino = destino.trim();
        this.peso = peso;
        this.prioridad = prioridad;
        this.tiempoEstimado = tiempoEstimado;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDestino() {
        return destino;
    }

    public double getPeso() {
        return peso;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public int getTiempoEstimado() {
        return tiempoEstimado;
    }

    /**
     * Compara paquetes según su código de forma ascendente.
     *
     * @param otro paquete con el cual se realiza la comparación
     * @return valor negativo, cero o positivo según el orden
     */
    @Override
    public int compareTo(Paquete otro) {
        Objects.requireNonNull(otro, "El paquete no puede ser nulo");
        return this.codigo.compareTo(otro.codigo);
    }

    /**
     * Devuelve la información del paquete en formato legible.
     *
     * @return descripción del paquete
     */
    @Override
    public String toString() {
        return String.format("[%-6s] %-12s %5.1f kg | Prioridad: %d | %3d min",
                codigo, destino, peso, prioridad, tiempoEstimado);
    }
}
