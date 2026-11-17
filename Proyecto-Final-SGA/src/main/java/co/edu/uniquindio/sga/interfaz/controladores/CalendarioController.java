
package co.edu.uniquindio.sga.interfaz.controladores;

import co.edu.uniquindio.sga.dominio.modelo.Apartamento;
import co.edu.uniquindio.sga.dominio.modelo.EstadoOperativoApartamento;
import co.edu.uniquindio.sga.dominio.modelo.EstadoReserva;
import co.edu.uniquindio.sga.dominio.modelo.Estancia;
import co.edu.uniquindio.sga.dominio.modelo.Reserva;
import co.edu.uniquindio.sga.interfaz.contexto.ContextoAplicacion;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

/**
 * Controlador del calendario mensual de ocupación.
 *
 * Consulta las reservas mediante ServicioDisponibilidad y obtiene
 * los apartamentos del catálogo compartido.
 *
 * No contiene reglas de negocio ni modifica reservas.
 */
public final class CalendarioController {

    private static final Locale IDIOMA = Locale.forLanguageTag("es-CO");

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy", IDIOMA);

    private final ContextoAplicacion contexto;

    private YearMonth mesActual = YearMonth.now();

    @FXML
    private Label mesAnioLabel;

    @FXML
    private Label resumenLabel;

    @FXML
    private Label detalleSeleccionLabel;

    @FXML
    private GridPane cuadriculaCalendario;

    public CalendarioController(ContextoAplicacion contexto) {
        this.contexto = Objects.requireNonNull(
                contexto,
                "El contexto de aplicación es obligatorio"
        );
    }

    @FXML
    private void initialize() {
        actualizarCalendario();
    }

    /**
     * Retrocede un mes en el calendario.
     */
    @FXML
    private void mesAnterior() {
        mesActual = mesActual.minusMonths(1);
        actualizarCalendario();
    }

    /**
     * Avanza un mes en el calendario.
     */
    @FXML
    private void mesSiguiente() {
        mesActual = mesActual.plusMonths(1);
        actualizarCalendario();
    }

    /**
     * Reconstruye la cuadrícula con la información del mes seleccionado.
     */
    @FXML
    private void actualizarCalendario() {

        cuadriculaCalendario.getChildren().clear();
        cuadriculaCalendario.getColumnConstraints().clear();

        mesAnioLabel.setText(nombreMes(mesActual));

        int diasDelMes = mesActual.lengthOfMonth();

        configurarColumnas(diasDelMes);
        agregarEncabezados(diasDelMes);

        int fila = 1;
        int cantidadApartamentos = 0;

        try {

            for (Apartamento apartamento : contexto.consultarApartamentos()) {

                cantidadApartamentos++;

                agregarInformacionApartamento(apartamento, fila);

                for (int dia = 1; dia <= diasDelMes; dia++) {

                    LocalDate fecha = mesActual.atDay(dia);

                    Reserva conflicto = contexto.getServicioDisponibilidad()
                            .buscarConflicto(
                                    apartamento,
                                    new Estancia(fecha, fecha.plusDays(1))
                            );

                    EstadoCelda estado = determinarEstado(
                            apartamento,
                            conflicto
                    );

                    Button celda = crearCelda(
                            apartamento,
                            fecha,
                            conflicto,
                            estado
                    );

                    cuadriculaCalendario.add(
                            celda,
                            dia + 1,
                            fila
                    );
                }

                fila++;
            }

            cuadriculaCalendario.setMinWidth(
                    355 + diasDelMes * 38
            );

            resumenLabel.setText(
                    cantidadApartamentos
                            + " apartamentos · "
                            + diasDelMes
                            + " noches · "
                            + nombreMes(mesActual)
            );

            if (cantidadApartamentos == 0) {
                detalleSeleccionLabel.setText(
                        "No hay apartamentos registrados en el catálogo."
                );
            } else {
                detalleSeleccionLabel.setText(
                        "Selecciona una noche para consultar su información."
                );
            }

        } catch (RuntimeException excepcion) {

            excepcion.printStackTrace();

            resumenLabel.setText("No se pudo cargar el calendario.");

            detalleSeleccionLabel.setText(
                    excepcion.getMessage() == null
                            ? "Ocurrió un error al consultar la ocupación."
                            : excepcion.getMessage()
            );
        }
    }

    /**
     * Configura las columnas fijas de apartamentos y las columnas diarias.
     */
    private void configurarColumnas(int diasDelMes) {

        cuadriculaCalendario.getColumnConstraints().add(
                new ColumnConstraints(210, 210, 210)
        );

        cuadriculaCalendario.getColumnConstraints().add(
                new ColumnConstraints(145, 145, 145)
        );

        for (int dia = 0; dia < diasDelMes; dia++) {
            cuadriculaCalendario.getColumnConstraints().add(
                    new ColumnConstraints(38, 38, 38)
            );
        }
    }

    /**
     * Agrega los encabezados de apartamentos, estados y fechas.
     */
    private void agregarEncabezados(int diasDelMes) {

        agregarEncabezado("Apartamento", 0);
        agregarEncabezado("Estado operativo", 1);

        for (int dia = 1; dia <= diasDelMes; dia++) {

            LocalDate fecha = mesActual.atDay(dia);

            Label encabezado = new Label(
                    dia + "\n"
                            + fecha.getDayOfWeek().getDisplayName(
                            TextStyle.SHORT,
                            IDIOMA
                    )
            );

            encabezado.setWrapText(true);
            encabezado.setAlignment(javafx.geometry.Pos.CENTER);
            encabezado.getStyleClass().add("calendario-encabezado");

            cuadriculaCalendario.add(
                    encabezado,
                    dia + 1,
                    0
            );
        }
    }

    private void agregarEncabezado(String texto, int columna) {

        Label encabezado = new Label(texto);

        encabezado.setWrapText(true);
        encabezado.getStyleClass().add("calendario-encabezado");

        cuadriculaCalendario.add(
                encabezado,
                columna,
                0
        );
    }

    /**
     * Muestra el apartamento y su estado operativo actual.
     */
    private void agregarInformacionApartamento(
            Apartamento apartamento,
            int fila) {

        Label identificacion = new Label(
                apartamento.getCodigo()
                        + "\n"
                        + apartamento.getNombre()
        );

        identificacion.setWrapText(true);
        identificacion.getStyleClass().add(
                "calendario-apartamento"
        );

        cuadriculaCalendario.add(
                identificacion,
                0,
                fila
        );

        Label estado = new Label(
                textoEstadoOperativo(apartamento.getEstadoOperativo())
        );

        estado.setWrapText(true);

        estado.getStyleClass().addAll(
                "calendario-estado-operativo",
                claseEstadoOperativo(apartamento.getEstadoOperativo())
        );

        cuadriculaCalendario.add(
                estado,
                1,
                fila
        );
    }

    /**
     * Determina el estado visual de una noche.
     *
     * Las reservas activas tienen prioridad sobre el estado operativo.
     * Si no existe reserva, se comprueba si el apartamento está fuera
     * de servicio.
     */
    private EstadoCelda determinarEstado(
            Apartamento apartamento,
            Reserva reserva) {

        if (reserva != null) {

            return switch (reserva.getEstado()) {
                case PENDIENTE -> EstadoCelda.PENDIENTE;
                case CONFIRMADA -> EstadoCelda.CONFIRMADA;
                case EN_CURSO -> EstadoCelda.EN_CURSO;
                default -> EstadoCelda.DISPONIBLE;
            };
        }

        if (apartamento.getEstadoOperativo()
                == EstadoOperativoApartamento.FUERA_DE_SERVICIO) {

            return EstadoCelda.FUERA_SERVICIO;
        }

        return EstadoCelda.DISPONIBLE;
    }

    /**
     * Construye una celda interactiva del calendario.
     */
    private Button crearCelda(
            Apartamento apartamento,
            LocalDate fecha,
            Reserva reserva,
            EstadoCelda estado) {

        Button boton = new Button(estado.simbolo);

        boton.setMinSize(36, 32);
        boton.setPrefSize(36, 32);
        boton.setMaxSize(36, 32);

        boton.getStyleClass().addAll(
                "calendario-celda",
                estado.claseCss
        );

        boton.setTooltip(
                new Tooltip(
                        construirDetalle(
                                apartamento,
                                fecha,
                                reserva,
                                estado
                        )
                )
        );

        boton.setOnAction(evento ->
                mostrarDetalle(
                        apartamento,
                        fecha,
                        reserva,
                        estado
                )
        );

        return boton;
    }

    /**
     * Presenta la información de la noche seleccionada.
     */
    private void mostrarDetalle(
            Apartamento apartamento,
            LocalDate fecha,
            Reserva reserva,
            EstadoCelda estado) {

        detalleSeleccionLabel.setText(
                construirDetalle(
                        apartamento,
                        fecha,
                        reserva,
                        estado
                )
        );
    }

    private String construirDetalle(
            Apartamento apartamento,
            LocalDate fecha,
            Reserva reserva,
            EstadoCelda estado) {

        String base = "Apartamento: "
                + apartamento.getCodigo()
                + " · Fecha: "
                + fecha.format(FORMATO_FECHA);

        if (reserva != null) {

            return base
                    + " · Reserva: "
                    + reserva.getId()
                    + " · Estado: "
                    + reserva.getEstado()
                    + " · Entrada: "
                    + reserva.getEstancia().getEntrada().format(FORMATO_FECHA)
                    + " · Salida: "
                    + reserva.getEstancia().getSalida().format(FORMATO_FECHA);
        }

        if (estado == EstadoCelda.FUERA_SERVICIO) {

            return base
                    + " · Fuera de servicio: "
                    + apartamento.getMotivoFueraDeServicio();
        }

        return base + " · Noche disponible.";
    }

    private String nombreMes(YearMonth mes) {

        String nombre = mes.getMonth().getDisplayName(
                TextStyle.FULL,
                IDIOMA
        );

        return nombre.substring(0, 1).toUpperCase(IDIOMA)
                + nombre.substring(1)
                + " "
                + mes.getYear();
    }

    private String textoEstadoOperativo(
            EstadoOperativoApartamento estado) {

        return estado.name().replace('_', ' ');
    }

    private String claseEstadoOperativo(
            EstadoOperativoApartamento estado) {

        return "estado-operativo-"
                + estado.name()
                .toLowerCase(Locale.ROOT)
                .replace('_', '-');
    }

    /**
     * Estados visuales de las celdas.
     */
    private enum EstadoCelda {

        DISPONIBLE("·", "celda-disponible"),
        PENDIENTE("P", "celda-pendiente"),
        CONFIRMADA("C", "celda-confirmada"),
        EN_CURSO("E", "celda-en-curso"),
        FUERA_SERVICIO("X", "celda-fuera-servicio");

        private final String simbolo;
        private final String claseCss;

        EstadoCelda(String simbolo, String claseCss) {
            this.simbolo = simbolo;
            this.claseCss = claseCss;
        }
    }
}
