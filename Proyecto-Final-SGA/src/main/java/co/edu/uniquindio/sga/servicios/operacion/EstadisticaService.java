package co.edu.uniquindio.sga.servicios.operacion;

import co.edu.uniquindio.sga.dominio.modelo.Folio;
import co.edu.uniquindio.sga.dominio.modelo.MovimientoFolio;
import co.edu.uniquindio.sga.dominio.modelo.TipoMovimiento;
import co.edu.uniquindio.sga.servicios.folio.FolioService;

import java.math.BigDecimal;
import java.util.Objects;

/** Calcula estadísticas básicas de operación. */
public final class EstadisticaService {

    private final FolioService folioService;

    public EstadisticaService(FolioService folioService) {
        this.folioService = Objects.requireNonNull(
                folioService,
                "El servicio de folios es obligatorio."
        );
    }

    public BigDecimal calcularTotalCargos(String codigoReserva) {
        Folio folio = folioService.consultarFolio(codigoReserva);

        BigDecimal total = BigDecimal.ZERO;

        for (MovimientoFolio movimiento : folio) {
            if (movimiento.getTipo() == TipoMovimiento.CARGO) {
                total = total.add(movimiento.getImporte());
            }
        }

        return total;
    }

    public BigDecimal calcularTotalPagos(String codigoReserva) {
        Folio folio = folioService.consultarFolio(codigoReserva);

        BigDecimal total = BigDecimal.ZERO;

        for (MovimientoFolio movimiento : folio) {
            if (movimiento.getTipo() == TipoMovimiento.PAGO) {
                total = total.add(
                        movimiento.getImporte().abs()
                );
            }
        }

        return total;
    }

    public BigDecimal calcularSaldoPendiente(String codigoReserva) {
        return folioService.consultarSaldo(codigoReserva);
    }

    public int calcularCantidadFolios() {
        return folioService.cantidadFolios();
    }

    public boolean tieneSaldoPendiente(String codigoReserva) {
        BigDecimal saldo = calcularSaldoPendiente(codigoReserva);

        return saldo.compareTo(BigDecimal.ZERO) > 0;
    }
}