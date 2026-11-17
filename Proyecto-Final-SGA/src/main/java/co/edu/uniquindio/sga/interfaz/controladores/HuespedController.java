package co.edu.uniquindio.sga.interfaz.controladores;

import co.edu.uniquindio.sga.dominio.modelo.Apartamento;
import co.edu.uniquindio.sga.dominio.modelo.Huesped;
import co.edu.uniquindio.sga.dominio.modelo.Reserva;
import co.edu.uniquindio.sga.interfaz.contexto.ContextoAplicacion;
import co.edu.uniquindio.sga.servicios.huespedes.ServicioPerfilHuesped;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Controlador de la gestión de huéspedes.
 *
 * Centraliza las interacciones de la interfaz y delega
 * las operaciones del perfil en ServicioPerfilHuesped.
 *
 * No contiene estructuras de almacenamiento ni reglas
 * de persistencia propias.
 */
public final class HuespedController {

    private final ContextoAplicacion contexto;
    private final ServicioPerfilHuesped servicioPerfil;

    private Huesped huespedActual;

    // Formulario de registro
    @FXML
    private TextField documentoField;

    @FXML
    private TextField nombreField;

    @FXML
    private DatePicker fechaNacimientoPicker;

    @FXML
    private TextField telefonoField;

    @FXML
    private TextField correoField;

    @FXML
    private TextField fotoField;

    // Búsqueda de huéspedes
    @FXML
    private TextField documentoConsultaField;

    // Información del huésped encontrado
    @FXML
    private Label documentoResultadoLabel;

    @FXML
    private Label nombreResultadoLabel;

    @FXML
    private Label nacimientoResultadoLabel;

    @FXML
    private Label telefonoResultadoLabel;

    @FXML
    private Label correoResultadoLabel;

    @FXML
    private Label estadoResultadoLabel;

    // Gestión de apartamentos favoritos
    @FXML
    private ComboBox<Apartamento> apartamentoCombo;

    @FXML
    private ListView<Apartamento> favoritosListView;

    @FXML
    private Label favoritosCantidadLabel;

    // Historial de estancias
    @FXML
    private ListView<Reserva> historialListView;

    @FXML
    private Label historialCantidadLabel;

    public HuespedController(ContextoAplicacion contexto) {

        this.contexto = Objects.requireNonNull(
                contexto,
                "El contexto de aplicación es obligatorio"
        );

        this.servicioPerfil = contexto.getServicioPerfilHuesped();
    }

    /**
     * Inicializa los controles y carga el catálogo
     * de apartamentos disponible para favoritos.
     */
    @FXML
    private void initialize() {

        cargarApartamentos();

        limpiarInformacionHuesped();

        estadoResultadoLabel.setText(
                "Busca un huésped por su número de documento."
        );
    }

    /**
     * Carga los apartamentos del catálogo compartido.
     */
    private void cargarApartamentos() {

        ObservableList<Apartamento> apartamentos =
                FXCollections.observableArrayList();

        for (Apartamento apartamento : contexto.consultarApartamentos()) {
            apartamentos.add(apartamento);
        }

        apartamentoCombo.setItems(apartamentos);
    }

    /**
     * Registra un nuevo huésped utilizando el servicio de perfiles.
     */
    @FXML
    private void registrarHuesped() {

        try {

            LocalDate nacimiento = fechaNacimientoPicker.getValue();

            if (nacimiento == null) {
                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Fecha obligatoria",
                        "Selecciona la fecha de nacimiento del huésped."
                );
                return;
            }

            if (nacimiento.isAfter(LocalDate.now())) {
                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Fecha inválida",
                        "La fecha de nacimiento no puede ser futura."
                );
                return;
            }

            Huesped nuevo = new Huesped(
                    documentoField.getText(),
                    nombreField.getText(),
                    nacimiento,
                    telefonoField.getText(),
                    correoField.getText(),
                    fotoField.getText()
            );

            boolean registrado = servicioPerfil.registrarHuesped(nuevo);

            if (!registrado) {

                mostrarAlerta(
                        Alert.AlertType.WARNING,
                        "Huésped existente",
                        "Ya existe un huésped registrado con ese documento."
                );

                return;
            }

            documentoConsultaField.setText(nuevo.getDocumento());

            huespedActual = nuevo;

            mostrarInformacionHuesped(nuevo);

            actualizarColecciones();

            limpiarFormulario();

            mostrarAlerta(
                    Alert.AlertType.INFORMATION,
                    "Registro exitoso",
                    "El huésped fue registrado correctamente."
            );

        } catch (IllegalArgumentException excepcion) {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error de registro",
                    excepcion.getMessage()
            );
        }
    }

    /**
     * Busca un huésped mediante su documento.
     */
    @FXML
    private void buscarHuesped() {

        String documento = documentoConsultaField.getText();

        if (documento == null || documento.isBlank()) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Documento obligatorio",
                    "Ingresa el documento del huésped que deseas consultar."
            );

            return;
        }

        Huesped encontrado = servicioPerfil.buscarHuesped(documento);

        if (encontrado == null) {

            huespedActual = null;

            limpiarInformacionHuesped();

            estadoResultadoLabel.setText(
                    "No se encontró ningún huésped con ese documento."
            );

            mostrarAlerta(
                    Alert.AlertType.INFORMATION,
                    "Sin resultados",
                    "No existe un huésped registrado con ese documento."
            );

            return;
        }

        huespedActual = encontrado;

        mostrarInformacionHuesped(encontrado);

        actualizarColecciones();
    }

    /**
     * Presenta los datos del huésped encontrado.
     */
    private void mostrarInformacionHuesped(Huesped huesped) {

        documentoResultadoLabel.setText(huesped.getDocumento());

        nombreResultadoLabel.setText(huesped.getNombre());

        nacimientoResultadoLabel.setText(
                huesped.getFechaNacimiento().toString()
        );

        telefonoResultadoLabel.setText(huesped.getTelefono());

        correoResultadoLabel.setText(huesped.getCorreo());

        estadoResultadoLabel.setText(
                "Perfil consultado correctamente."
        );
    }

    /**
     * Agrega el apartamento seleccionado a los favoritos.
     */
    @FXML
    private void agregarFavorito() {

        if (!hayHuespedSeleccionado()) {
            return;
        }

        Apartamento apartamento = apartamentoCombo.getValue();

        if (apartamento == null) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Apartamento requerido",
                    "Selecciona un apartamento para agregarlo a favoritos."
            );

            return;
        }

        boolean agregado = servicioPerfil.agregarFavorito(
                huespedActual.getDocumento(),
                apartamento
        );

        actualizarColecciones();

        mostrarAlerta(
                Alert.AlertType.INFORMATION,
                agregado ? "Favorito agregado" : "Favorito existente",
                agregado
                        ? "El apartamento se agregó correctamente."
                        : "Este apartamento ya está registrado como favorito."
        );
    }

    /**
     * Elimina el apartamento seleccionado de favoritos.
     */
    @FXML
    private void eliminarFavorito() {

        if (!hayHuespedSeleccionado()) {
            return;
        }

        Apartamento seleccionado = favoritosListView.getSelectionModel()
                .getSelectedItem();

        if (seleccionado == null) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Selección requerida",
                    "Selecciona un apartamento de la lista de favoritos."
            );

            return;
        }

        boolean eliminado = servicioPerfil.eliminarFavorito(
                huespedActual.getDocumento(),
                seleccionado
        );

        actualizarColecciones();

        mostrarAlerta(
                Alert.AlertType.INFORMATION,
                eliminado ? "Favorito eliminado" : "Sin cambios",
                eliminado
                        ? "El apartamento se eliminó de favoritos."
                        : "No se encontró el apartamento seleccionado."
        );
    }

    /**
     * Actualiza las listas de favoritos e historial
     * correspondientes al huésped actual.
     */
    private void actualizarColecciones() {

        if (huespedActual == null) {
            return;
        }

        String documento = huespedActual.getDocumento();

        ObservableList<Apartamento> favoritos =
                FXCollections.observableArrayList();

        for (Apartamento apartamento :
                servicioPerfil.consultarFavoritos(documento)) {

            favoritos.add(apartamento);
        }

        favoritosListView.setItems(favoritos);

        favoritosCantidadLabel.setText(
                "Apartamentos favoritos: "
                        + servicioPerfil.cantidadFavoritos(documento)
        );

        ObservableList<Reserva> historial =
                FXCollections.observableArrayList();

        for (Reserva reserva :
                servicioPerfil.consultarHistorial(documento)) {

            historial.add(reserva);
        }

        historialListView.setItems(historial);

        historialCantidadLabel.setText(
                "Estancias finalizadas: "
                        + servicioPerfil.cantidadEstancias(documento)
        );
    }

    /**
     * Comprueba que exista un huésped seleccionado.
     */
    private boolean hayHuespedSeleccionado() {

        if (huespedActual == null) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Huésped no seleccionado",
                    "Primero debes buscar o registrar un huésped."
            );

            return false;
        }

        return true;
    }

    /**
     * Limpia los datos del formulario de registro.
     */
    private void limpiarFormulario() {

        documentoField.clear();
        nombreField.clear();
        fechaNacimientoPicker.setValue(null);
        telefonoField.clear();
        correoField.clear();
        fotoField.clear();
    }

    /**
     * Limpia la información del huésped y sus colecciones.
     */
    private void limpiarInformacionHuesped() {

        documentoResultadoLabel.setText("—");
        nombreResultadoLabel.setText("—");
        nacimientoResultadoLabel.setText("—");
        telefonoResultadoLabel.setText("—");
        correoResultadoLabel.setText("—");

        favoritosListView.setItems(
                FXCollections.observableArrayList()
        );

        historialListView.setItems(
                FXCollections.observableArrayList()
        );

        favoritosCantidadLabel.setText("Apartamentos favoritos: 0");

        historialCantidadLabel.setText("Estancias finalizadas: 0");
    }

    /**
     * Presenta mensajes de información, advertencia o error.
     */
    private void mostrarAlerta(
            Alert.AlertType tipo,
            String titulo,
            String mensaje) {

        Alert alerta = new Alert(tipo);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }
}
