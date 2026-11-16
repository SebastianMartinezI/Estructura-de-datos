package co.edu.uniquindio.sga.dominio.modelo;

import java.time.LocalDate;
import java.time.Period;

/**
 * Persona registrada como ocupante de una reserva.
 *
 * <p>La identidad de un ocupante NO se deduce del nombre y la fecha de
 * nacimiento: dos personas diferentes pueden compartir ambos datos.
 * La relación con una reserva y el control de capacidad se realizan
 * en los servicios de reservas.</p>
 */
public final class Ocupante {

    private final String nombre;
    private final LocalDate fechaNacimiento;

    /**
     * @param nombre nombre del ocupante, obligatorio
     * @param fechaNacimiento fecha de nacimiento, obligatoria
     */
    public Ocupante(String nombre, LocalDate fechaNacimiento) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del ocupante es obligatorio.");
        }
        if (fechaNacimiento == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        }
        this.nombre = nombre.trim();
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Calcula la edad en una fecha de referencia explícita.
     *
     * <p>El servicio de cotización decidirá si la fecha de referencia
     * corresponde al ingreso o a cada noche, porque esa política aún
     * debe acordarse con el equipo.</p>
     *
     * @param fechaReferencia fecha en la que se consulta la edad
     * @return años completos cumplidos
     * @throws IllegalArgumentException si la fecha es nula o anterior al nacimiento
     */
    public int edadEn(LocalDate fechaReferencia) {
        if (fechaReferencia == null || fechaReferencia.isBefore(fechaNacimiento)) {
            throw new IllegalArgumentException(
                    "La fecha de referencia debe ser igual o posterior al nacimiento.");
        }
        return Period.between(fechaNacimiento, fechaReferencia).getYears();
    }

    public String getNombre() { return nombre; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
}
