package co.edu.uniquindio.sga.dominio.modelo;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * Unidad de alojamiento exclusiva para un grupo de huéspedes.
 *
 * <p>Identidad y orden natural: el código único. Las tarifas y el estado
 * operativo se almacenan aquí; los cálculos de disponibilidad, las reservas
 * y las transiciones de check-in/check-out pertenecen a los servicios.</p>
 */
public final class Apartamento implements Comparable<Apartamento> {

    private final String codigo;
    private final String nombre;
    private final String bloque;
    private final int piso;
    private final int numeroDormitorios;
    private final int capacidadMaxima;
    private final String caracteristicas;

    private TarifasApartamento tarifas;
    private EstadoOperativoApartamento estadoOperativo;
    private String motivoFueraDeServicio;

    /**
     * Construye un apartamento validando sus datos y su estado inicial.
     *
     * @param codigo identificador único (se normaliza en mayúsculas)
     * @param nombre nombre visible del apartamento
     * @param bloque bloque al que pertenece
     * @param piso nivel en el edificio, incluidos posibles sótanos
     * @param numeroDormitorios cantidad de dormitorios (mayor que cero)
     * @param capacidadMaxima personas máximas, incluidos menores que no pagan
     * @param caracteristicas descripción opcional de características
     * @param tarifas tarifas por persona/noche según temporada
     * @param estadoInicial estado operativo al registrar el apartamento
     * @param motivoFueraDeServicio obligatorio solo si está fuera de servicio
     */
    public Apartamento(String codigo, String nombre, String bloque, int piso,
                       int numeroDormitorios, int capacidadMaxima,
                       String caracteristicas, TarifasApartamento tarifas,
                       EstadoOperativoApartamento estadoInicial, String motivoFueraDeServicio) {
        this.codigo = textoObligatorio(codigo, "código").toUpperCase(Locale.ROOT);
        this.nombre = textoObligatorio(nombre, "nombre");
        this.bloque = textoObligatorio(bloque, "bloque");
        if (numeroDormitorios <= 0) {
            throw new IllegalArgumentException("El número de dormitorios debe ser positivo.");
        }
        if (capacidadMaxima <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser positiva.");
        }
        if (tarifas == null) {
            throw new IllegalArgumentException("Las tarifas del apartamento son obligatorias.");
        }
        this.piso = piso;
        this.numeroDormitorios = numeroDormitorios;
        this.capacidadMaxima = capacidadMaxima;
        this.caracteristicas = caracteristicas == null ? "" : caracteristicas.trim();
        this.tarifas = tarifas;
        actualizarEstadoOperativo(estadoInicial, motivoFueraDeServicio);
    }

    private static String textoObligatorio(String valor, String nombreCampo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo " + nombreCampo + " es obligatorio.");
        }
        return valor.trim();
    }

    /** Devuelve la tarifa por persona y noche, sin multiplicarla por ocupantes. */
    public BigDecimal tarifaPorPersonaNoche(Temporada temporada) {
        return tarifas.paraTemporada(temporada);
    }

    /**
     * Sustituye las tarifas del apartamento. La autorización del administrador
     * debe comprobarse en el servicio que invoca esta operación.
     */
    public void actualizarTarifas(TarifasApartamento nuevasTarifas) {
        if (nuevasTarifas == null) {
            throw new IllegalArgumentException("Las nuevas tarifas son obligatorias.");
        }
        this.tarifas = nuevasTarifas;
    }

    /**
     * Guarda un estado operativo y valida su motivo cuando corresponda.
     * Las transiciones permitidas se comprueban en el servicio de operación
     * a cargo de check-in, check-out y preparación.
     */
    public void actualizarEstadoOperativo(EstadoOperativoApartamento nuevoEstado, String motivo) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El estado operativo es obligatorio.");
        }
        if (nuevoEstado == EstadoOperativoApartamento.FUERA_DE_SERVICIO) {
            this.motivoFueraDeServicio = textoObligatorio(motivo, "motivo fuera de servicio");
        } else {
            if (motivo != null && !motivo.isBlank()) {
                throw new IllegalArgumentException("Solo los apartamentos fuera de servicio tienen motivo.");
            }
            this.motivoFueraDeServicio = "";
        }
        this.estadoOperativo = nuevoEstado;
    }

    @Override
    public int compareTo(Apartamento otro) {
        if (otro == null) {
            throw new NullPointerException("No se puede comparar con un apartamento nulo.");
        }
        return this.codigo.compareTo(otro.codigo);
    }

    @Override
    public boolean equals(Object objeto) {
        return this == objeto || (objeto instanceof Apartamento otro
                && codigo.equals(otro.codigo));
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getBloque() { return bloque; }
    public int getPiso() { return piso; }
    public int getNumeroDormitorios() { return numeroDormitorios; }
    public int getCapacidadMaxima() { return capacidadMaxima; }
    public String getCaracteristicas() { return caracteristicas; }
    public TarifasApartamento getTarifas() { return tarifas; }
    public EstadoOperativoApartamento getEstadoOperativo() { return estadoOperativo; }
    /** Devuelve una cadena vacía si el apartamento no está fuera de servicio. */
    public String getMotivoFueraDeServicio() { return motivoFueraDeServicio; }
}
