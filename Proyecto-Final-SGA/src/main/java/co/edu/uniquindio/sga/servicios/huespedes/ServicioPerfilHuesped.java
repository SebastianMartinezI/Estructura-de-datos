package co.edu.uniquindio.sga.servicios.huespedes;

import co.edu.uniquindio.sga.dominio.modelo.Apartamento;
import co.edu.uniquindio.sga.dominio.modelo.EstadoReserva;
import co.edu.uniquindio.sga.dominio.modelo.Huesped;
import co.edu.uniquindio.sga.dominio.modelo.Reserva;
import co.edu.uniquindio.sga.estructuras.arbol.ArbolBinarioBusqueda;
import co.edu.uniquindio.sga.estructuras.conjunto.Conjunto;
import co.edu.uniquindio.sga.estructuras.lista.ListaDoblementeEnlazada;

import java.util.Objects;

/**
 * Administra el perfil comercial de cada huésped titular: catálogo por
 * documento, apartamentos favoritos e historial de estancias finalizadas.
 *
 * <p>Usa exclusivamente estructuras propias para almacenar datos. No gestiona
 * pagos, estados de reservas, operaciones de check-in/check-out ni interfaces
 * JavaFX. El historial se registra cuando otro servicio ya ha finalizado la
 * reserva y no se alimenta de reservas pendientes o canceladas.</p>
 *
 * <p>La implementación está en memoria: la persistencia se integrará después.
 * Las operaciones públicas se sincronizan para evitar altas duplicadas desde
 * diferentes controladores en la misma instancia.</p>
 */
public final class ServicioPerfilHuesped {

    private final ArbolBinarioBusqueda<Perfil> perfiles = new ArbolBinarioBusqueda<>();

    /**
     * Registra un huésped si su documento no existe en el catálogo.
     *
     * @return true si se registró; false si el documento ya existía
     */
    public synchronized boolean registrarHuesped(Huesped huesped) {
        Objects.requireNonNull(huesped, "El huésped es obligatorio");
        return perfiles.insertar(new Perfil(huesped.getDocumento(), huesped));
    }

    /** Obtiene el huésped registrado por documento, o null si no existe. */
    public synchronized Huesped buscarHuesped(String documento) {
        Perfil perfil = perfiles.buscar(Perfil.clave(validarDocumento(documento)));
        return perfil == null ? null : perfil.huesped;
    }

    /** Añade un apartamento a favoritos; nunca guarda duplicados por código. */
    public synchronized boolean agregarFavorito(String documento, Apartamento apartamento) {
        Objects.requireNonNull(apartamento, "El apartamento es obligatorio");
        return perfilExistente(documento).favoritos.agregar(apartamento);
    }

    /** Retira un favorito si se encontraba registrado. */
    public synchronized boolean eliminarFavorito(String documento, Apartamento apartamento) {
        Objects.requireNonNull(apartamento, "El apartamento es obligatorio");
        return perfilExistente(documento).favoritos.eliminar(apartamento);
    }

    /** Indica si el apartamento está entre los favoritos del huésped. */
    public synchronized boolean esFavorito(String documento, Apartamento apartamento) {
        Objects.requireNonNull(apartamento, "El apartamento es obligatorio");
        return perfilExistente(documento).favoritos.contiene(apartamento);
    }

    /** Retorna la cantidad de apartamentos favoritos del huésped. */
    public synchronized int cantidadFavoritos(String documento) {
        return perfilExistente(documento).favoritos.tamano();
    }

    /**
     * Devuelve una instantánea recorrible de favoritos.
     *
     * <p>El orden no es significativo, como corresponde a un conjunto. No se
     * expone la estructura interna mutable del perfil.</p>
     */
    public synchronized Iterable<Apartamento> consultarFavoritos(String documento) {
        Conjunto<Apartamento> copia = new Conjunto<>();
        for (Apartamento apartamento : perfilExistente(documento).favoritos) {
            copia.agregar(apartamento);
        }
        return copia;
    }

    /**
     * Registra una estancia terminada en el historial del huésped titular.
     * Una misma reserva solo puede registrarse una vez por ID.
     *
     * @param reserva reserva en estado FINALIZADA y con titular registrado
     * @return true si se añadió; false si ya estaba registrada
     * @throws IllegalArgumentException si la reserva no está finalizada o
     *                                  el titular no está en el catálogo
     */
    public synchronized boolean registrarEstanciaFinalizada(Reserva reserva) {
        Objects.requireNonNull(reserva, "La reserva es obligatoria");
        if (reserva.getEstado() != EstadoReserva.FINALIZADA) {
            throw new IllegalArgumentException("Solo se registran estancias FINALIZADAS");
        }
        Perfil perfil = perfilExistente(reserva.getTitular().getDocumento());
        if (!perfil.idsEnHistorial.agregar(reserva.getId())) {
            return false;
        }
        perfil.historial.agregarUltimo(reserva);
        return true;
    }

    /** Número de estancias finalizadas registradas para un huésped titular. */
    public synchronized int cantidadEstancias(String documento) {
        return perfilExistente(documento).historial.tamano();
    }

    /**
     * Devuelve una instantánea recorrible de estancias finalizadas, en orden
     * de registro. No devuelve la lista interna modificable.
     */
    public synchronized Iterable<Reserva> consultarHistorial(String documento) {
        ListaDoblementeEnlazada<Reserva> copia = new ListaDoblementeEnlazada<>();
        for (Reserva reserva : perfilExistente(documento).historial) {
            copia.agregarUltimo(reserva);
        }
        return copia;
    }

    /** Cantidad de perfiles registrados en el catálogo. */
    public synchronized int cantidadHuespedes() {
        return perfiles.tamano();
    }

    private Perfil perfilExistente(String documento) {
        String clave = validarDocumento(documento);
        Perfil perfil = perfiles.buscar(Perfil.clave(clave));
        if (perfil == null) {
            throw new IllegalArgumentException("No existe un huésped con documento " + clave);
        }
        return perfil;
    }

    private static String validarDocumento(String documento) {
        if (documento == null || documento.isBlank()) {
            throw new IllegalArgumentException("El documento es obligatorio");
        }
        return documento.trim();
    }

    /** Entrada interna del BST; su orden natural depende solo del documento. */
    private static final class Perfil implements Comparable<Perfil> {
        private final String documento;
        private final Huesped huesped;
        private final Conjunto<Apartamento> favoritos = new Conjunto<>();
        private final Conjunto<String> idsEnHistorial = new Conjunto<>();
        private final ListaDoblementeEnlazada<Reserva> historial = new ListaDoblementeEnlazada<>();

        private Perfil(String documento, Huesped huesped) {
            this.documento = documento;
            this.huesped = huesped;
        }

        /** Clave transitoria para buscar en el BST; no se inserta. */
        private static Perfil clave(String documento) {
            return new Perfil(documento, null);
        }

        @Override
        public int compareTo(Perfil otro) {
            return documento.compareTo(otro.documento);
        }
    }
}
