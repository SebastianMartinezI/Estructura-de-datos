
package co.edu.uniquindio.sga.interfaz.controladores;

import co.edu.uniquindio.sga.dominio.modelo.Apartamento;
import co.edu.uniquindio.sga.dominio.modelo.Estancia;
import co.edu.uniquindio.sga.dominio.modelo.Ocupante;
import co.edu.uniquindio.sga.interfaz.contexto.ContextoAplicacion;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

/**
 * Controlador de la pantalla independiente de disponibilidad.
 *
 * <p>La interfaz captura los criterios de búsqueda y presenta resultados.
 * La disponibilidad, cotización y ordenamiento se delegan en los servicios
 * compartidos de la aplicación.</p>
 *
 * <p>Los datos utilizados pertenecen al escenario temporal DEMO.</p>
 */
public final class DisponibilidadController {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final NumberFormat FORMATO_MONEDA =
            NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

    private static final String ORDEN_PRECIO =
            "Precio total (menor a mayor)";

    private static final String ORDEN_CAPACIDAD =
            "Mayor capacidad";

    private static final String ORDEN_DORMITORIOS =
            "Mayor número de dormitorios";

    private final ContextoAplicacion contexto;

    private final ObservableList<Ocupante> ocupantes =
            FXCollections.observableArrayList();

    private final ObservableList<ResultadoApartamento> resultados =
            FXCollections.observableArrayList();

    @FXML private DatePicker entradaPicker;
    @FXML private DatePicker salidaPicker;
    @FXML private ComboBox<String> ordenCombo;

    @FXML private TextField nombreOcupanteField;
    @FXML private DatePicker nacimientoOcupantePicker;
    @FXML private ListView<Ocupante> ocupantesListView;

    @FXML private Label cantidadPersonasLabel;
    @FXML private Label resultadoLabel;

    @FXML private TableView<ResultadoApartamento> tablaResultados;
    @FXML private TableColumn<ResultadoApartamento, String> codigoColumn;
    @FXML private TableColumn<ResultadoApartamento, String> nombreColumn;
    @FXML private TableColumn<ResultadoApartamento, String> ubicacionColumn;
    @FXML private TableColumn<ResultadoApartamento, String> capacidadColumn;
    @FXML private TableColumn<ResultadoApartamento, String> dormitoriosColumn;
    @FXML private TableColumn<ResultadoApartamento, String> estadoColumn;
    @FXML private TableColumn<ResultadoApartamento, String> precioColumn;

    public DisponibilidadController(ContextoAplicacion contexto) {
        this.contexto = Objects.requireNonNull(
                contexto, "El contexto de aplicación es obligatorio");
    }

    @FXML
    private void initialize() {
        configurarFechasIniciales();
        configurarOrdenamiento();
        configurarListaOcupantes();
        configurarTablaResultados();

        ocupantesListView.setItems(ocupantes);
        tablaResultados.setItems(resultados);

        actualizarCantidadPersonas();
    }

    private void configurarFechasIniciales() {
        LocalDate hoy = LocalDate.now();

        entradaPicker.setValue(hoy.plusDays(2));
        salidaPicker.setValue(hoy.plusDays(4));
    }

    private void configurarOrdenamiento() {
        ordenCombo.setItems(FXCollections.observableArrayList(
                ORDEN_CAPACIDAD,
                ORDEN_PRECIO,
                ORDEN_DORMITORIOS
        ));

        ordenCombo.setValue(ORDEN_CAPACIDAD);
    }

    private void configurarListaOcupantes() {
        ocupantesListView.setCellFactory(lista -> new ListCell<>() {
            @Override
            protected void updateItem(Ocupante ocupante, boolean vacio) {
                super.updateItem(ocupante, vacio);

                setText(vacio || ocupante == null
                        ? null
                        : ocupante.getNombre()
                        + " · Nacimiento: "
                        + ocupante.getFechaNacimiento().format(FORMATO_FECHA));
            }
        });
    }

    private void configurarTablaResultados() {
        codigoColumn.setCellValueFactory(dato ->
                new ReadOnlyStringWrapper(
                        dato.getValue().apartamento().getCodigo()));

        nombreColumn.setCellValueFactory(dato ->
                new ReadOnlyStringWrapper(
                        dato.getValue().apartamento().getNombre()));

        ubicacionColumn.setCellValueFactory(dato -> {
            Apartamento apartamento = dato.getValue().apartamento();

            return new ReadOnlyStringWrapper(
                    apartamento.getBloque()
                            + " · Piso "
                            + apartamento.getPiso());
        });

        capacidadColumn.setCellValueFactory(dato ->
                new ReadOnlyStringWrapper(
                        Integer.toString(
                                dato.getValue().apartamento().getCapacidadMaxima())));

        dormitoriosColumn.setCellValueFactory(dato ->
                new ReadOnlyStringWrapper(
                        Integer.toString(
                                dato.getValue().apartamento().getNumeroDormitorios())));

        estadoColumn.setCellValueFactory(dato ->
                new ReadOnlyStringWrapper(
                        dato.getValue().apartamento()
                                .getEstadoOperativo().name().replace('_', ' ')));

        precioColumn.setCellValueFactory(dato ->
                new ReadOnlyStringWrapper(
                        moneda(dato.getValue().precioTotal())));
    }

    @FXML
    private void agregarOcupante() {
        String nombre = nombreOcupanteField.getText();
        LocalDate nacimiento = nacimientoOcupantePicker.getValue();

        if (nombre == null || nombre.isBlank() || nacimiento == null) {
            mostrarError(
                    "Datos incompletos",
                    "Escribe el nombre y la fecha de nacimiento del ocupante.");
            return;
        }

        if (nacimiento.isAfter(LocalDate.now())) {
            mostrarError(
                    "Fecha inválida",
                    "La fecha de nacimiento no puede ser futura.");
            return;
        }

        ocupantes.add(new Ocupante(nombre.trim(), nacimiento));

        nombreOcupanteField.clear();
        nacimientoOcupantePicker.setValue(null);

        actualizarCantidadPersonas();
        limpiarResultados();
    }

    @FXML
    private void quitarOcupante() {
        Ocupante seleccionado =
                ocupantesListView.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            ocupantes.remove(seleccionado);
            actualizarCantidadPersonas();
            limpiarResultados();
        }
    }

    @FXML
    private void consultarDisponibilidad() {
        try {
            Estancia estancia = crearEstancia();

            if (ocupantes.isEmpty()) {
                throw new IllegalArgumentException(
                        "Agrega al menos una persona para consultar disponibilidad.");
            }

            String criterio = ordenCombo.getValue();

            if (criterio == null) {
                throw new IllegalArgumentException(
                        "Selecciona un criterio de ordenamiento.");
            }

            int cantidadPersonas = ocupantes.size();

            Iterable<Apartamento> encontrados;

            if (ORDEN_PRECIO.equals(criterio)) {

                encontrados = contexto.getServicioBusquedaApartamentos()
                        .porPrecio(
                                estancia,
                                cantidadPersonas,
                                apartamento -> cotizar(apartamento, estancia)
                        );

            } else if (ORDEN_CAPACIDAD.equals(criterio)) {

                encontrados = contexto.getServicioBusquedaApartamentos()
                        .porCapacidad(estancia, cantidadPersonas);

            } else if (ORDEN_DORMITORIOS.equals(criterio)) {

                encontrados = contexto.getServicioBusquedaApartamentos()
                        .porDormitorios(estancia, cantidadPersonas);

            } else {
                throw new IllegalArgumentException(
                        "El criterio de ordenamiento seleccionado no es válido.");
            }

            resultados.clear();

            for (Apartamento apartamento : encontrados) {
                BigDecimal precio = cotizar(apartamento, estancia);

                resultados.add(
                        new ResultadoApartamento(apartamento, precio));
            }

            actualizarMensajeResultados(criterio);

        } catch (RuntimeException excepcion) {
            mostrarError(
                    "No se pudo consultar disponibilidad",
                    mensaje(excepcion));
        }
    }

    /**
     * Calcula la cotización sin registrar ni modificar una reserva.
     */
    private BigDecimal cotizar(Apartamento apartamento, Estancia estancia) {
        return contexto.getServicioReservas().cotizarPrevia(
                apartamento,
                estancia,
                ocupantes
        );
    }

    private Estancia crearEstancia() {
        LocalDate entrada = entradaPicker.getValue();
        LocalDate salida = salidaPicker.getValue();

        if (entrada == null || salida == null) {
            throw new IllegalArgumentException(
                    "Selecciona las fechas de entrada y salida.");
        }

        return new Estancia(entrada, salida);
    }

    private void actualizarCantidadPersonas() {
        int cantidad = ocupantes.size();

        cantidadPersonasLabel.setText(
                "Personas alojadas: " + cantidad
                        + (cantidad == 1 ? " persona" : " personas"));
    }

    private void actualizarMensajeResultados(String criterio) {
        int cantidad = resultados.size();

        if (cantidad == 0) {
            resultadoLabel.setText(
                    "No se encontraron apartamentos disponibles "
                            + "para las fechas y ocupación seleccionadas.");
            return;
        }

        resultadoLabel.setText(
                "Se encontraron " + cantidad
                        + (cantidad == 1
                        ? " apartamento disponible."
                        : " apartamentos disponibles.")
                        + " Orden: " + criterio + "."
                        + " Los valores son temporales DEMO.");
    }

    private void limpiarResultados() {
        resultados.clear();
        resultadoLabel.setText(
                "Actualiza la búsqueda para consultar los nuevos criterios.");
    }

    private static String moneda(BigDecimal valor) {
        return FORMATO_MONEDA.format(valor);
    }

    private static String mensaje(RuntimeException excepcion) {
        String contenido = excepcion.getMessage();

        return contenido == null || contenido.isBlank()
                ? "Ocurrió un error inesperado. Verifica los datos."
                : contenido;
    }

    private void mostrarError(String titulo, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    /**
     * Modelo exclusivo de presentación para la tabla JavaFX.
     * No sustituye ni modifica la entidad Apartamento.
     */
    public record ResultadoApartamento(
            Apartamento apartamento,
            BigDecimal precioTotal) {
    }
}
