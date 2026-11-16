package co.edu.uniquindio.sga.dominio.modelo;

import java.time.LocalDate;

/**
 * Perfil de un huésped registrado en el SGA.
 *
 * <p>El documento identifica al huésped y determina su orden natural en el
 * árbol binario de búsqueda. Esta entidad no realiza reservas ni administra
 * folios; esas responsabilidades pertenecen a los servicios correspondientes.</p>
 *
 * <p>El perfil es inmutable. Si se permite actualizar datos de contacto,
 * el servicio responsable debe definir cómo reemplazar el registro sin
 * modificar su documento identificador.</p>
 */
public final class Huesped implements Comparable<Huesped> {

    private final String documento;
    private final String nombre;
    private final LocalDate fechaNacimiento;
    private final String telefono;
    private final String correo;
    private final String foto;

    /**
     * Crea el perfil de un huésped.
     *
     * @param documento identificador único, obligatorio; se eliminan espacios exteriores
     * @param nombre nombre del huésped, obligatorio
     * @param fechaNacimiento fecha de nacimiento, obligatoria
     * @param telefono teléfono de contacto, obligatorio
     * @param correo correo electrónico, obligatorio
     * @param foto ruta o referencia de la foto opcional; puede ser null
     */
    public Huesped(String documento, String nombre, LocalDate fechaNacimiento,
                   String telefono, String correo, String foto) {
        this.documento = textoObligatorio(documento, "documento");
        this.nombre = textoObligatorio(nombre, "nombre");
        if (fechaNacimiento == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        }
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = textoObligatorio(telefono, "teléfono");
        this.correo = textoObligatorio(correo, "correo electrónico");
        this.foto = foto == null ? "" : foto.trim();
    }

    private static String textoObligatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
        return valor.trim();
    }

    /**
     * Compara por documento. La identidad conserva las mayúsculas y minúsculas:
     * una eventual normalización adicional debe acordarse entre ambos integrantes.
     */
    @Override
    public int compareTo(Huesped otro) {
        if (otro == null) {
            throw new NullPointerException("No se puede comparar con un huésped nulo.");
        }
        return documento.compareTo(otro.documento);
    }

    @Override
    public boolean equals(Object objeto) {
        return this == objeto || (objeto instanceof Huesped otro
                && documento.equals(otro.documento));
    }

    @Override
    public int hashCode() {
        return documento.hashCode();
    }

    @Override
    public String toString() {
        return documento + " - " + nombre;
    }

    public String getDocumento() { return documento; }
    public String getNombre() { return nombre; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }

    /** Devuelve una cadena vacía cuando el perfil no tiene fotografía. */
    public String getFoto() { return foto; }
}
