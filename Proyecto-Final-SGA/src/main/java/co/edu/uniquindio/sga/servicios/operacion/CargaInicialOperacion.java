package co.edu.uniquindio.sga.servicios.operacion;

import co.edu.uniquindio.sga.servicios.folio.FolioService;

import java.math.BigDecimal;
import java.util.Objects;

/** Carga los datos iniciales necesarios para operar el alojamiento. */
public final class CargaInicialOperacion {

    private final TurnoService turnoService;
    private final RutaPreparacionService rutaPreparacionService;
    private final FolioService folioService;

    public CargaInicialOperacion(
            TurnoService turnoService,
            RutaPreparacionService rutaPreparacionService,
            FolioService folioService
    ) {
        this.turnoService = Objects.requireNonNull(
                turnoService,
                "El servicio de turnos es obligatorio."
        );

        this.rutaPreparacionService = Objects.requireNonNull(
                rutaPreparacionService,
                "El servicio de rutas es obligatorio."
        );

        this.folioService = Objects.requireNonNull(
                folioService,
                "El servicio de folios es obligatorio."
        );
    }

    public void cargarDatosIniciales() {
        cargarPersonal();
        cargarMapa();
        cargarFolioEjemplo();
    }

    private void cargarPersonal() {
        turnoService.registrarPersonal("Ana");
        turnoService.registrarPersonal("Luis");
        turnoService.registrarPersonal("María");
    }

    private void cargarMapa() {
        rutaPreparacionService.existeApartamento("A-101");
        rutaPreparacionService.existeApartamento("A-102");
        rutaPreparacionService.existeApartamento("A-103");
        rutaPreparacionService.existeApartamento("A-201");
        rutaPreparacionService.existeApartamento("A-202");
        rutaPreparacionService.existeApartamento("A-203");
    }

    private void cargarFolioEjemplo() {
        String codigoReserva = "RES-001";

        if (!folioService.existeFolio(codigoReserva)) {
            folioService.crearFolio(codigoReserva);

            folioService.registrarCargo(
                    codigoReserva,
                    "Alojamiento",
                    new BigDecimal("300000")
            );

            folioService.registrarPago(
                    codigoReserva,
                    "Abono inicial",
                    new BigDecimal("90000")
            );
        }
    }
}