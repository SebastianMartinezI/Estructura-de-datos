package co.edu.uniquindio.sga.dominio.modelo;

import co.edu.uniquindio.sga.estructuras.lista.ListaDoblementeEnlazada;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Objects;

/**
 * Resultado de una cotización: total y desglose recorriendo noches.
 * No registra pagos ni movimientos del folio; eso pertenece a Cristian.
 * Almacena una copia defensiva del desglose, con elementos inmutables.
 */
public final class LiquidacionReserva implements Iterable<DetalleNoche> {

    private final String idReserva;
    private final ListaDoblementeEnlazada<DetalleNoche> desglose;
    private final BigDecimal total;

    /**
     * Crea una liquidación consistente: la suma del desglose debe ser el total.
     */
    public LiquidacionReserva(String idReserva,
                             ListaDoblementeEnlazada<DetalleNoche> noches,
                             BigDecimal total) {
        if (idReserva == null || idReserva.isBlank()) {
            throw new IllegalArgumentException("El ID de la reserva es obligatorio");
        }
        Objects.requireNonNull(noches, "El desglose es obligatorio");
        Objects.requireNonNull(total, "El total es obligatorio");
        if (total.signum() < 0 || noches.estaVacia()) {
            throw new IllegalArgumentException("La liquidación debe tener noches y total no negativo");
        }
        this.idReserva = idReserva;
        this.desglose = new ListaDoblementeEnlazada<>();
        BigDecimal suma = BigDecimal.ZERO;
        for (DetalleNoche detalle : noches) {
            this.desglose.agregarUltimo(Objects.requireNonNull(detalle));
            suma = suma.add(detalle.subtotal());
        }
        if (suma.compareTo(total) != 0) {
            throw new IllegalArgumentException("El total no coincide con la suma del desglose");
        }
        this.total = total;
    }

    public String getIdReserva() { return idReserva; }

    public BigDecimal getTotal() { return total; }

    public int getNumeroNoches() { return desglose.tamano(); }

    /** Itera las noches de entrada a salida, sin exponer la lista mutable. */
    @Override
    public Iterator<DetalleNoche> iterator() {
        return desglose.iterator();
    }
}
