package co.edu.uniquindio.sga.dominio.modelo;

/** Estados operativos de un apartamento del alojamiento. */
public enum EstadoOperativo {

    PREPARADO("Preparado"),
    OCUPADO("Ocupado"),
    PENDIENTE_PREPARACION("Pendiente de preparación"),
    EN_PREPARACION("En preparación"),
    FUERA_SERVICIO("Fuera de servicio");

    private final String descripcion;

    EstadoOperativo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}