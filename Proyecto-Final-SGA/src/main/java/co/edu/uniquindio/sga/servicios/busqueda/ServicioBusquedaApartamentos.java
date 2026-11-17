package co.edu.uniquindio.sga.servicios.busqueda;

import co.edu.uniquindio.sga.dominio.modelo.Apartamento;
import co.edu.uniquindio.sga.dominio.modelo.Estancia;
import co.edu.uniquindio.sga.estructuras.arbol.ArbolBinarioBusqueda;
import co.edu.uniquindio.sga.estructuras.lista.ListaDoblementeEnlazada;
import co.edu.uniquindio.sga.servicios.reservas.ServicioDisponibilidad;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.ToIntFunction;
import java.util.function.Function;

/**
 * Consulta apartamentos del catálogo BST, filtra capacidad y disponibilidad,
 * y entrega resultados en una lista doblemente enlazada ordenada.
 *
 * <p>El ordenamiento se implementa mediante merge sort estable sobre arreglos
 * auxiliares; no utiliza ArrayList, List ni otras colecciones de Java.</p>
 */
public final class ServicioBusquedaApartamentos {

    private final ArbolBinarioBusqueda<Apartamento> catalogo;
    private final ServicioDisponibilidad disponibilidad;

    public ServicioBusquedaApartamentos(
            ArbolBinarioBusqueda<Apartamento> catalogo,
            ServicioDisponibilidad disponibilidad) {
        this.catalogo = Objects.requireNonNull(catalogo, "El catálogo BST es obligatorio");
        this.disponibilidad = Objects.requireNonNull(
                disponibilidad, "El servicio de disponibilidad es obligatorio");
    }

    /**
     * Busca apartamentos libres para las fechas y ocupación indicadas.
     *
     * @param estancia intervalo de noches [entrada, salida)
     * @param personas cantidad total de personas, incluidos menores que no pagan
     * @param orden criterio alternativo de ordenamiento
     * @return lista propia con los apartamentos que cumplen las condiciones
     */
    public ListaDoblementeEnlazada<Apartamento> buscarDisponibles(
            Estancia estancia, int personas, Comparator<? super Apartamento> orden) {
        Objects.requireNonNull(estancia, "La estancia es obligatoria");
        Objects.requireNonNull(orden, "El comparador es obligatorio");
        if (personas <= 0) {
            throw new IllegalArgumentException("La cantidad de personas debe ser positiva");
        }

        Apartamento[] candidatos = new Apartamento[catalogo.tamano()];
        int cantidad = 0;
        for (Apartamento apartamento : catalogo) {
            if (apartamento.getCapacidadMaxima() >= personas
                    && disponibilidad.estaDisponible(apartamento, estancia)) {
                candidatos[cantidad++] = apartamento;
            }
        }

        ordenar(candidatos, new Apartamento[cantidad], 0, cantidad - 1, orden);

        ListaDoblementeEnlazada<Apartamento> resultado = new ListaDoblementeEnlazada<>();
        for (int i = 0; i < cantidad; i++) {
            resultado.agregarUltimo(candidatos[i]);
        }
        return resultado;
    }

    /** Busca y ordena por cotización total ascendente. */
    public ListaDoblementeEnlazada<Apartamento> porPrecio(
            Estancia estancia, int personas,
            Function<? super Apartamento, BigDecimal> cotizacionTotal) {
        return buscarDisponibles(estancia, personas,
                ComparadoresApartamento.porPrecio(cotizacionTotal));
    }

    /** Busca y ordena por capacidad descendente. */
    public ListaDoblementeEnlazada<Apartamento> porCapacidad(
            Estancia estancia, int personas) {
        return buscarDisponibles(estancia, personas,
                ComparadoresApartamento.porCapacidad());
    }

    /** Busca y ordena por número de dormitorios descendente. */
    public ListaDoblementeEnlazada<Apartamento> porDormitorios(
            Estancia estancia, int personas) {
        return buscarDisponibles(estancia, personas,
                ComparadoresApartamento.porDormitorios());
    }

    /** Busca y ordena por cantidad histórica de reservas ascendente. */
    public ListaDoblementeEnlazada<Apartamento> porNumeroReservas(
            Estancia estancia, int personas,
            ToIntFunction<? super Apartamento> cantidadReservas) {
        return buscarDisponibles(estancia, personas,
                ComparadoresApartamento.porNumeroReservas(cantidadReservas));
    }

    private static void ordenar(
            Apartamento[] elementos, Apartamento[] auxiliar,
            int inicio, int fin, Comparator<? super Apartamento> comparador) {
        if (inicio >= fin) {
            return;
        }
        int medio = inicio + (fin - inicio) / 2;
        ordenar(elementos, auxiliar, inicio, medio, comparador);
        ordenar(elementos, auxiliar, medio + 1, fin, comparador);
        mezclar(elementos, auxiliar, inicio, medio, fin, comparador);
    }

    private static void mezclar(
            Apartamento[] elementos, Apartamento[] auxiliar,
            int inicio, int medio, int fin,
            Comparator<? super Apartamento> comparador) {
        for (int indice = inicio; indice <= fin; indice++) {
            auxiliar[indice] = elementos[indice];
        }

        int izquierda = inicio;
        int derecha = medio + 1;
        int destino = inicio;

        while (izquierda <= medio && derecha <= fin) {
            if (comparador.compare(auxiliar[izquierda], auxiliar[derecha]) <= 0) {
                elementos[destino++] = auxiliar[izquierda++];
            } else {
                elementos[destino++] = auxiliar[derecha++];
            }
        }
        while (izquierda <= medio) {
            elementos[destino++] = auxiliar[izquierda++];
        }
        while (derecha <= fin) {
            elementos[destino++] = auxiliar[derecha++];
        }
    }
}
