package co.edu.uniquindio.sga.interfaz.preparacion;

import co.edu.uniquindio.sga.dominio.modelo.TareaPreparacion;
import co.edu.uniquindio.sga.servicios.operacion.PreparacionService;
import co.edu.uniquindio.sga.servicios.operacion.RutaPreparacionService;
import co.edu.uniquindio.sga.servicios.operacion.TurnoService;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public final class VistaPreparacionController {

    private final Clock reloj = Clock.systemDefaultZone();

    private final PreparacionService preparacion =
            new PreparacionService(reloj);

    private final TurnoService turnos = new TurnoService();

    private final RutaPreparacionService rutas =
            new RutaPreparacionService();

    private final DateTimeFormatter formato =
            DateTimeFormatter.ofPattern("dd/MM HH:mm");

    @FXML
    private ComboBox<String> cmbApartamento;

    @FXML
    private DatePicker fechaLlegada;

    @FXML
    private TextField txtHora;

    @FXML
    private TableView<TareaPreparacion> tablaTareas;

    @FXML
    private TableColumn<TareaPreparacion, String> colApartamento;

    @FXML
    private TableColumn<TareaPreparacion, String> colPrioridad;

    @FXML
    private TableColumn<TareaPreparacion, String> colLlegada;

    @FXML
    private TableColumn<TareaPreparacion, String> colEstado;

    @FXML
    private TableColumn<TareaPreparacion, String> colPersonal;

    @FXML
    private Label lblResumen;

    @FXML
    private Label lblRuta;

    @FXML
    private Label lblMensaje;

    @FXML
    private void initialize() {
        // Códigos existentes en el grafo de RutaPreparacionService.
        cmbApartamento.getItems().setAll(
                "A-101", "A-102", "A-103",
                "A-201", "A-202", "A-203"
        );
        cmbApartamento.getSelectionModel().selectFirst();

        turnos.registrarPersonal("Ana");
        turnos.registrarPersonal("Luis");
        turnos.registrarPersonal("María");

        txtHora.setText("14:00");

        colApartamento.setCellValueFactory(c ->
                texto(c.getValue().getCodigoApartamento()));

        colPrioridad.setCellValueFactory(c ->
                texto(c.getValue().tieneEntradaEn(LocalDate.now(reloj))
                        ? "1 · Entrada hoy"
                        : "2 · Sin entrada hoy"));

        colLlegada.setCellValueFactory(c ->
                texto(c.getValue().getProximaLlegada() == null
                        ? "Sin programar"
                        : formato.format(
                        c.getValue().getProximaLlegada()
                )));

        colEstado.setCellValueFactory(c ->
                texto(estado(c.getValue())));

        colPersonal.setCellValueFactory(c ->
                texto(responsable(c.getValue())));

        tablaTareas.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, anterior, actual) ->
                        mostrarRuta(actual));

        actualizar();
    }

    private ReadOnlyStringWrapper texto(String valor) {
        return new ReadOnlyStringWrapper(valor);
    }

    private String estado(TareaPreparacion tarea) {
        if (tarea.getFinPreparacion() != null) {
            return "FINALIZADA";
        }

        if (tarea.getInicioPreparacion() != null) {
            return "EN PREPARACIÓN";
        }

        return "PENDIENTE";
    }

    private String responsable(TareaPreparacion tarea) {
        String codigo = tarea.getCodigoApartamento();

        if (turnos.tieneAsignacion(codigo)) {
            return turnos.consultarAsignacion(codigo);
        }

        return "Sin asignar";
    }

    @FXML
    private void registrarPendiente() {
        ejecutar(() -> {
            String codigo = cmbApartamento.getValue();

            if (codigo == null || !rutas.existeApartamento(codigo)) {
                throw new IllegalArgumentException(
                        "Selecciona un apartamento del mapa."
                );
            }

            LocalDateTime llegada = null;

            if (fechaLlegada.getValue() != null) {
                String hora = txtHora.getText().trim();

                if (!hora.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
                    throw new IllegalArgumentException(
                            "Escribe la hora en formato HH:mm, por ejemplo 14:00."
                    );
                }

                llegada = LocalDateTime.of(
                        fechaLlegada.getValue(),
                        LocalTime.parse(hora)
                );
            }

            preparacion.registrarTarea(
                    new TareaPreparacion(
                            codigo,
                            LocalDateTime.now(reloj),
                            llegada
                    )
            );

            return "Tarea registrada para " + codigo + ".";
        });
    }

    @FXML
    private void asignarSiguiente() {
        ejecutar(() -> {
            for (TareaPreparacion tarea : preparacion.consultarTareas()) {
                String codigo = tarea.getCodigoApartamento();

                if (tarea.getFinPreparacion() == null
                        && !turnos.tieneAsignacion(codigo)) {

                    String nombre = turnos.asignarTarea(codigo);
                    tablaTareas.getSelectionModel().select(tarea);

                    return codigo + " asignado a " + nombre + ".";
                }
            }

            throw new IllegalStateException(
                    "No hay tareas sin asignar."
            );
        });
    }

    @FXML
    private void iniciarPreparacion() {
        ejecutar(() -> {
            TareaPreparacion tarea = seleccionada();

            if (tarea.getFinPreparacion() != null) {
                throw new IllegalStateException(
                        "La tarea ya está finalizada."
                );
            }

            if (tarea.getInicioPreparacion() != null) {
                throw new IllegalStateException(
                        "La preparación ya está iniciada."
                );
            }

            // Evita iniciar una tarea antes de otra pendiente de mayor prioridad.
            for (TareaPreparacion pendiente : preparacion.consultarTareas()) {
                if (pendiente.getInicioPreparacion() == null
                        && pendiente.getFinPreparacion() == null) {

                    if (pendiente != tarea) {
                        throw new IllegalStateException(
                                "Primero inicia la tarea prioritaria: "
                                        + pendiente.getCodigoApartamento()
                        );
                    }
                    break;
                }
            }

            String codigo = tarea.getCodigoApartamento();

            if (!turnos.tieneAsignacion(codigo)) {
                throw new IllegalStateException(
                        "Primero pulsa Asignar siguiente."
                );
            }

            preparacion.iniciarPreparacion(codigo);

            return "Preparación iniciada: " + codigo + ".";
        });
    }

    @FXML
    private void marcarPreparado() {
        ejecutar(() -> {
            TareaPreparacion tarea = seleccionada();
            String codigo = tarea.getCodigoApartamento();

            preparacion.finalizarPreparacion(codigo);

            if (turnos.tieneAsignacion(codigo)) {
                turnos.liberarAsignacion(codigo);
            }

            return "Tarea finalizada: " + codigo
                    + ". Duración: "
                    + tarea.calcularDuracion().toSeconds()
                    + " segundos.";
        });
    }

    @FXML
    private void actualizar() {
        TareaPreparacion seleccion =
                tablaTareas.getSelectionModel().getSelectedItem();

        tablaTareas.getItems().clear();

        int pendientes = 0;
        int enPreparacion = 0;
        int finalizadas = 0;

        for (TareaPreparacion tarea : preparacion.consultarTareas()) {
            tablaTareas.getItems().add(tarea);

            if (tarea.getFinPreparacion() != null) {
                finalizadas++;
            } else if (tarea.getInicioPreparacion() != null) {
                enPreparacion++;
            } else {
                pendientes++;
            }
        }

        lblResumen.setText(
                "Pendientes: " + pendientes
                        + "   |   En preparación: " + enPreparacion
                        + "   |   Finalizadas: " + finalizadas
        );

        if (seleccion != null
                && tablaTareas.getItems().contains(seleccion)) {
            tablaTareas.getSelectionModel().select(seleccion);
        }

        tablaTareas.refresh();
    }

    private TareaPreparacion seleccionada() {
        TareaPreparacion tarea =
                tablaTareas.getSelectionModel().getSelectedItem();

        if (tarea == null) {
            throw new IllegalStateException(
                    "Selecciona una tarea de la tabla."
            );
        }

        return tarea;
    }

    private void mostrarRuta(TareaPreparacion tarea) {
        if (tarea == null) {
            lblRuta.setText(
                    "Selecciona una tarea para consultar la ruta."
            );
            return;
        }

        try {
            String codigo = tarea.getCodigoApartamento();

            lblRuta.setText(
                    String.join(" → ", rutas.obtenerRecorrido(codigo))
                            + " | Peso total: "
                            + rutas.obtenerDistancia(codigo)
            );
        } catch (RuntimeException excepcion) {
            lblRuta.setText(
                    "No se pudo calcular la ruta: "
                            + excepcion.getMessage()
            );
        }
    }

    private void ejecutar(Operacion operacion) {
        try {
            String mensaje = operacion.realizar();
            actualizar();

            lblMensaje.setStyle("-fx-text-fill: #176b3a;");
            lblMensaje.setText(mensaje);

        } catch (RuntimeException excepcion) {
            lblMensaje.setStyle("-fx-text-fill: #b42318;");
            lblMensaje.setText(
                    excepcion.getMessage() == null
                            ? "No fue posible completar la operación."
                            : excepcion.getMessage()
            );
        }
    }

    @FunctionalInterface
    private interface Operacion {
        String realizar();
    }
}