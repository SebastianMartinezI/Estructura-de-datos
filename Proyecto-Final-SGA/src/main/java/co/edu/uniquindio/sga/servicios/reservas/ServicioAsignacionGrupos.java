package co.edu.uniquindio.sga.servicios.reservas;

import co.edu.uniquindio.sga.dominio.modelo.Apartamento;
import co.edu.uniquindio.sga.dominio.modelo.Estancia;
import co.edu.uniquindio.sga.estructuras.arbol.ArbolBinarioBusqueda;
import co.edu.uniquindio.sga.estructuras.lista.ListaDoblementeEnlazada;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * Busca combinaciones de apartamentos para alojar grupos, mediante
 * backtracking recursivo con poda (branch and bound).
 *
 * <p>La prioridad es usar el menor número de apartamentos; ante un empate,
 * elige menor número de cupos sin usar. El último empate es determinista
 * según el orden de candidatos por capacidad descendente y código.</p>
 *
 * <p>Se consultan fechas sobre el servicio oficial de disponibilidad.
 * No se registra ninguna reserva ni se bloquean noches: antes de confirmar
 * una propuesta, el servicio de reservas deberá revalidar la disponibilidad.
 * El catálogo se inyecta para no duplicar estructuras ni datos compartidos.</p>
 */
public final class ServicioAsignacionGrupos {

    private static final Comparator<Apartamento> ORDEN_CANDIDATOS =
            Comparator.comparingInt(Apartamento::getCapacidadMaxima)
                    .reversed().thenComparing(Apartamento::getCodigo);

    private final ArbolBinarioBusqueda<Apartamento> catalogo;
    private final ServicioDisponibilidad disponibilidad;

    public ServicioAsignacionGrupos(ArbolBinarioBusqueda<Apartamento> catalogo,
                                   ServicioDisponibilidad disponibilidad) {
        this.catalogo = Objects.requireNonNull(catalogo, "El catálogo es obligatorio");
        this.disponibilidad = Objects.requireNonNull(
                disponibilidad, "La disponibilidad es obligatoria");
    }

    /**
     * Retorna la mejor propuesta para las fechas y el tamaño de grupo.
     *
     * @return propuesta o Optional.empty() cuando no hay capacidad suficiente
     * @throws IllegalArgumentException si personas es menor o igual a cero
     */
    public Optional<PropuestaAsignacionGrupo> buscar(Estancia estancia, int personas) {
        Objects.requireNonNull(estancia, "La estancia es obligatoria");
        if (personas <= 0) {
            throw new IllegalArgumentException("La cantidad de personas debe ser positiva");
        }

        Apartamento[] candidatos = new Apartamento[catalogo.tamano()];
        int cantidad = 0;
        for (Apartamento apartamento : catalogo) {
            if (disponibilidad.estaDisponible(apartamento, estancia)) {
                candidatos[cantidad++] = apartamento;
            }
        }
        if (cantidad == 0) {
            return Optional.empty();
        }
        ordenarPorCapacidad(candidatos, cantidad);

        long[] sumaRestante = new long[cantidad + 1];
        for (int i = cantidad - 1; i >= 0; i--) {
            sumaRestante[i] = sumaRestante[i + 1]
                    + candidatos[i].getCapacidadMaxima();
        }
        if (sumaRestante[0] < personas) {
            return Optional.empty();
        }

        // Una selección greedy de mayores capacidades da una cota superior
        // óptima para el número de apartamentos (no para cupos sobrantes).
        int[] seleccion = new int[cantidad];
        long capacidad = 0;
        int cuantos = 0;
        while (capacidad < personas) {
            seleccion[cuantos] = cuantos;
            capacidad += candidatos[cuantos].getCapacidadMaxima();
            cuantos++;
        }
        MejorSolucion mejor = new MejorSolucion(cantidad);
        mejor.guardar(seleccion, cuantos, capacidad);

        buscarRecursivo(candidatos, cantidad, sumaRestante,
                personas, 0, 0, 0L, seleccion, mejor);

        ListaDoblementeEnlazada<Apartamento> elegidos = new ListaDoblementeEnlazada<>();
        for (int i = 0; i < mejor.cantidad; i++) {
            elegidos.agregarUltimo(candidatos[mejor.indices[i]]);
        }
        return Optional.of(new PropuestaAsignacionGrupo(estancia, personas, elegidos));
    }

    /** Exploración incluir/excluir con poda por capacidad y tamaño máximo. */
    private void buscarRecursivo(Apartamento[] candidatos, int cantidad,
                                long[] sumaRestante, int personas,
                                int posicion, int elegidos, long capacidad,
                                int[] seleccion, MejorSolucion mejor) {
        // La cantidad mínima ya es óptima; cero cupos sobrantes también.
        if (mejor.capacidad == personas) {
            return;
        }
        if (capacidad >= personas) {
            if (elegidos < mejor.cantidad
                    || (elegidos == mejor.cantidad && capacidad < mejor.capacidad)) {
                mejor.guardar(seleccion, elegidos, capacidad);
            }
            return;
        }
        if (posicion >= cantidad || elegidos >= mejor.cantidad
                || capacidad + sumaRestante[posicion] < personas) {
            return;
        }

        // Aunque los siguientes sean los más grandes, no alcanzaría con
        // los cupos de apartamentos que todavía permite la solución óptima.
        long capacidadConMejorCupo = capacidad;
        int maxAgregar = mejor.cantidad - elegidos;
        for (int i = posicion;
             i < cantidad && i < posicion + maxAgregar; i++) {
            capacidadConMejorCupo += candidatos[i].getCapacidadMaxima();
        }
        if (capacidadConMejorCupo < personas) {
            return;
        }

        seleccion[elegidos] = posicion;
        buscarRecursivo(candidatos, cantidad, sumaRestante, personas,
                posicion + 1, elegidos + 1,
                capacidad + candidatos[posicion].getCapacidadMaxima(),
                seleccion, mejor);

        buscarRecursivo(candidatos, cantidad, sumaRestante, personas,
                posicion + 1, elegidos, capacidad, seleccion, mejor);
    }

    /** Ordenamiento por inserción propio: no se utilizan colecciones ajenas. */
    private static void ordenarPorCapacidad(Apartamento[] candidatos, int longitud) {
        for (int i = 1; i < longitud; i++) {
            Apartamento actual = candidatos[i];
            int j = i - 1;
            while (j >= 0 && ORDEN_CANDIDATOS.compare(candidatos[j], actual) > 0) {
                candidatos[j + 1] = candidatos[j];
                j--;
            }
            candidatos[j + 1] = actual;
        }
    }

    private static final class MejorSolucion {
        private final int[] indices;
        private int cantidad;
        private long capacidad;

        private MejorSolucion(int maximo) {
            this.indices = new int[maximo];
        }

        private void guardar(int[] seleccion, int cantidad, long capacidad) {
            System.arraycopy(seleccion, 0, indices, 0, cantidad);
            this.cantidad = cantidad;
            this.capacidad = capacidad;
        }
    }
}
