package co.edu.uniquindio.sga.dominio.modelo;

import java.util.Objects;

public final class Recepcionista implements Comparable<Recepcionista> {

    private final String documento;
    private final String nombre;
    private boolean activo;

    public Recepcionista(String documento, String nombre) {
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException(
                    "El documento es obligatorio"
            );
        }

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre es obligatorio"
            );
        }

        this.documento = documento.trim();
        this.nombre = nombre.trim();
        this.activo = true;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isActivo() {
        return activo;
    }

    public void activar() {
        activo = true;
    }

    public void desactivar() {
        activo = false;
    }

    @Override
    public int compareTo(Recepcionista otro) {
        Objects.requireNonNull(otro);
        return documento.compareTo(otro.documento);
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof Recepcionista otro)) {
            return false;
        }

        return documento.equals(otro.documento);
    }

    @Override
    public int hashCode() {
        return documento.hashCode();
    }

    @Override
    public String toString() {
        return nombre + " (" + documento + ")";
    }
}