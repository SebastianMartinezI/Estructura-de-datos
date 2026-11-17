package co.edu.uniquindio.sga.servicios.folio;

import co.edu.uniquindio.sga.dominio.modelo.Folio;
import co.edu.uniquindio.sga.dominio.modelo.MovimientoFolio;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** Administra los folios o cuentas de las reservas. */
public final class FolioService {

    private final Map<String, Folio> folios;

    public FolioService() {
        folios = new LinkedHashMap<>();
    }

    public Folio crearFolio(String codigoReserva) {
        Objects.requireNonNull(
                codigoReserva,
                "El código de la reserva es obligatorio."
        );

        String codigo = codigoReserva.trim();

        if (codigo.isEmpty()) {
            throw new IllegalArgumentException(
                    "El código de la reserva no puede estar vacío."
            );
        }

        if (folios.containsKey(codigo)) {
            throw new IllegalArgumentException(
                    "Ya existe un folio para la reserva: " + codigo
            );
        }

        Folio folio = new Folio(codigo);
        folios.put(codigo, folio);

        return folio;
    }

    public Folio consultarFolio(String codigoReserva) {
        Objects.requireNonNull(
                codigoReserva,
                "El código de la reserva es obligatorio."
        );

        Folio folio = folios.get(codigoReserva.trim());

        if (folio == null) {
            throw new IllegalArgumentException(
                    "No existe un folio para la reserva: "
                            + codigoReserva
            );
        }

        return folio;
    }

    public MovimientoFolio registrarCargo(
            String codigoReserva,
            String concepto,
            BigDecimal monto
    ) {
        Folio folio = consultarFolio(codigoReserva);

        return folio.registrarCargo(concepto, monto);
    }

    public MovimientoFolio registrarPago(
            String codigoReserva,
            String concepto,
            BigDecimal monto
    ) {
        Folio folio = consultarFolio(codigoReserva);

        return folio.registrarPago(concepto, monto);
    }

    public MovimientoFolio deshacerUltimaOperacion(String codigoReserva) {
        Folio folio = consultarFolio(codigoReserva);

        if (!folio.puedeDeshacer()) {
            throw new IllegalStateException(
                    "El folio no tiene operaciones para deshacer."
            );
        }

        return folio.deshacerUltimaOperacion();
    }

    public BigDecimal consultarSaldo(String codigoReserva) {
        return consultarFolio(codigoReserva).getSaldo();
    }

    public int cantidadFolios() {
        return folios.size();
    }

    public boolean existeFolio(String codigoReserva) {
        Objects.requireNonNull(
                codigoReserva,
                "El código de la reserva es obligatorio."
        );

        return folios.containsKey(codigoReserva.trim());
    }
}