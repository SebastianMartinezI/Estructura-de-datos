package co.edu.uniquindio.sga.servicios.reservas;

import co.edu.uniquindio.sga.dominio.modelo.Apartamento;
import co.edu.uniquindio.sga.dominio.modelo.Estancia;
import co.edu.uniquindio.sga.estructuras.lista.ListaDoblementeEnlazada;

import java.util.Objects;

/**
 * Propuesta de alojamiento para un grupo, sin bloquear ni crear reservas.
 * La lista propia queda encapsulada y no puede modificarse desde el exterior.
 */
public final class PropuestaAsignacionGrupo {
    private final Estancia estancia;
    private final int personas;
    private final long capacidadTotal;
    private final ListaDoblementeEnlazada<Apartamento> apartamentos;

    PropuestaAsignacionGrupo(Estancia estancia, int personas,
                            ListaDoblementeEnlazada<Apartamento> seleccionados) {
        this.estancia = Objects.requireNonNull(estancia, "La estancia es obligatoria");
        if (personas <= 0) {
            throw new IllegalArgumentException("El número de personas debe ser positivo");
        }
        Objects.requireNonNull(seleccionados, "Los apartamentos son obligatorios");
        if (seleccionados.estaVacia()) {
            throw new IllegalArgumentException("La propuesta requiere apartamentos");
        }
        this.personas = personas;
        this.apartamentos = new ListaDoblementeEnlazada<>();
        long suma = 0;
        for (Apartamento apartamento : seleccionados) {
            Objects.requireNonNull(apartamento, "No se permiten apartamentos nulos");
            this.apartamentos.agregarUltimo(apartamento);
            suma += apartamento.getCapacidadMaxima();
        }
        if (suma < personas) {
            throw new IllegalArgumentException("La capacidad de la propuesta es insuficiente");
        }
        this.capacidadTotal = suma;
    }

    public Estancia getEstancia() { return estancia; }
    public int getPersonas() { return personas; }
    public int getCantidadApartamentos() { return apartamentos.tamano(); }
    public long getCapacidadTotal() { return capacidadTotal; }
    public long getCuposDisponibles() { return capacidadTotal - personas; }

    /** Vista iterable de la selección; no expone los métodos mutables de la lista. */
    public Iterable<Apartamento> getApartamentos() {
        return apartamentos::iterator;
    }
}
