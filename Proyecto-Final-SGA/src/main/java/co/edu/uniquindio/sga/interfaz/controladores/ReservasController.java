package co.edu.uniquindio.sga.interfaz.controladores;

import co.edu.uniquindio.sga.dominio.modelo.Apartamento;
import co.edu.uniquindio.sga.dominio.modelo.CanalReserva;
import co.edu.uniquindio.sga.dominio.modelo.DetalleNoche;
import co.edu.uniquindio.sga.dominio.modelo.Estancia;
import co.edu.uniquindio.sga.dominio.modelo.Huesped;
import co.edu.uniquindio.sga.dominio.modelo.LiquidacionReserva;
import co.edu.uniquindio.sga.dominio.modelo.Ocupante;
import co.edu.uniquindio.sga.dominio.modelo.Reserva;
import co.edu.uniquindio.sga.estructuras.lista.ListaDoblementeEnlazada;
import co.edu.uniquindio.sga.interfaz.contexto.ContextoAplicacion;
import co.edu.uniquindio.sga.servicios.reservas.CotizacionAnticipo;

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
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Controlador de la primera pantalla funcional de reservas.
 *
 * <p>La vista recoge datos y presenta resultados; la validación de fechas,
 * capacidad, disponibilidad y cotización se delega en el dominio y servicios.
 * Los datos del catálogo son temporales y se reinician al cerrar la aplicación.</p>
 */
public final class ReservasController {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final NumberFormat FORMATO_MONEDA =
            NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

    private final ContextoAplicacion contexto;
    private final ObservableList<Ocupante> acompanantes = FXCollections.observableArrayList();
    private final ObservableList<ResumenReserva> reservasSesion = FXCollections.observableArrayList();
    private long consecutivoReserva = 1;

    @FXML private TextField documentoField;
    @FXML private TextField nombreField;
    @FXML private DatePicker nacimientoTitularPicker;
    @FXML private TextField telefonoField;
    @FXML private TextField correoField;
    @FXML private DatePicker entradaPicker;
    @FXML private DatePicker salidaPicker;
    @FXML private ComboBox<CanalReserva> canalCombo;
    @FXML private ComboBox<Apartamento> apartamentoCombo;
    @FXML private TextField nombreAcompananteField;
    @FXML private DatePicker nacimientoAcompanantePicker;
    @FXML private ListView<Ocupante> acompanantesListView;
    @FXML private Label cantidadPersonasLabel;
    @FXML private Label disponibilidadLabel;
    @FXML private Label totalLabel;
    @FXML private Label anticipoLabel;
    @FXML private Label resultadoLabel;
    @FXML private TableView<DetalleNoche> tablaDesglose;
    @FXML private TableColumn<DetalleNoche, String> nocheColumn;
    @FXML private TableColumn<DetalleNoche, String> temporadaColumn;
    @FXML private TableColumn<DetalleNoche, String> tarifaColumn;
    @FXML private TableColumn<DetalleNoche, String> ocupantesColumn;
    @FXML private TableColumn<DetalleNoche, String> pagadoresColumn;
    @FXML private TableColumn<DetalleNoche, String> subtotalColumn;
    @FXML private TableView<ResumenReserva> tablaReservas;
    @FXML private TableColumn<ResumenReserva, String> idColumn;
    @FXML private TableColumn<ResumenReserva, String> titularColumn;
    @FXML private TableColumn<ResumenReserva, String> apartamentoColumn;
    @FXML private TableColumn<ResumenReserva, String> fechasColumn;
    @FXML private TableColumn<ResumenReserva, String> totalReservaColumn;
    @FXML private TableColumn<ResumenReserva, String> canalReservaColumn;
    @FXML private TableColumn<ResumenReserva, String> estadoColumn;

    public ReservasController(ContextoAplicacion contexto) {
        this.contexto = contexto;
    }

    @FXML
    private void initialize() {
        configurarFechasIniciales();
        configurarCanales();
        configurarListaAcompanantes();
        configurarTablaDesglose();
        configurarTablaReservas();
        acompanantesListView.setItems(acompanantes);
        tablaReservas.setItems(reservasSesion);
        actualizarCantidadPersonas();
        cargarApartamentosIniciales();
    }

    private void configurarFechasIniciales() {
        LocalDate hoy = LocalDate.now();
        entradaPicker.setValue(hoy.plusDays(2));
        salidaPicker.setValue(hoy.plusDays(4));
        nacimientoTitularPicker.setValue(hoy.minusYears(30));
        nacimientoAcompanantePicker.setValue(hoy.minusYears(10));
    }

    private void configurarCanales() {
        canalCombo.setItems(FXCollections.observableArrayList(
                CanalReserva.DIRECTA, CanalReserva.PORTAL));
        canalCombo.setValue(CanalReserva.DIRECTA);
        canalCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(CanalReserva canal) {
                if (canal == null) return "";
                return canal == CanalReserva.PORTAL ? "Portal" : "Directa";
            }

            @Override
            public CanalReserva fromString(String texto) {
                return "Portal".equalsIgnoreCase(texto)
                        ? CanalReserva.PORTAL : CanalReserva.DIRECTA;
            }
        });
    }

    private void configurarListaAcompanantes() {
        acompanantesListView.setCellFactory(lista -> new ListCell<>() {
            @Override
            protected void updateItem(Ocupante ocupante, boolean vacio) {
                super.updateItem(ocupante, vacio);
                setText(vacio || ocupante == null ? null
                        : ocupante.getNombre() + " · nacimiento: "
                        + ocupante.getFechaNacimiento().format(FORMATO_FECHA));
            }
        });
    }

    private void configurarTablaDesglose() {
        nocheColumn.setCellValueFactory(dato -> new ReadOnlyStringWrapper(
                dato.getValue().noche().format(FORMATO_FECHA)));
        temporadaColumn.setCellValueFactory(dato -> new ReadOnlyStringWrapper(
                dato.getValue().temporada().name()));
        tarifaColumn.setCellValueFactory(dato -> new ReadOnlyStringWrapper(
                moneda(dato.getValue().tarifaPorPersona())));
        ocupantesColumn.setCellValueFactory(dato -> new ReadOnlyStringWrapper(
                Integer.toString(dato.getValue().totalOcupantes())));
        pagadoresColumn.setCellValueFactory(dato -> new ReadOnlyStringWrapper(
                Integer.toString(dato.getValue().ocupantesQuePagan())));
        subtotalColumn.setCellValueFactory(dato -> new ReadOnlyStringWrapper(
                moneda(dato.getValue().subtotal())));
    }

    private void configurarTablaReservas() {
        idColumn.setCellValueFactory(dato -> new ReadOnlyStringWrapper(dato.getValue().id()));
        titularColumn.setCellValueFactory(dato -> new ReadOnlyStringWrapper(dato.getValue().titular()));
        apartamentoColumn.setCellValueFactory(dato -> new ReadOnlyStringWrapper(dato.getValue().apartamento()));
        fechasColumn.setCellValueFactory(dato -> new ReadOnlyStringWrapper(dato.getValue().fechas()));
        totalReservaColumn.setCellValueFactory(dato -> new ReadOnlyStringWrapper(dato.getValue().total()));
        canalReservaColumn.setCellValueFactory(dato -> new ReadOnlyStringWrapper(dato.getValue().canal()));
        estadoColumn.setCellValueFactory(dato -> new ReadOnlyStringWrapper(dato.getValue().estado()));
    }

    private void cargarApartamentosIniciales() {
        int cantidadCatalogo = 0;
        for (Apartamento apartamento : contexto.consultarApartamentos()) {
            cantidadCatalogo++;
        }
        // No se ofrece un apartamento para selección hasta filtrar por fechas
        // y ocupación mediante el servicio de búsqueda.
        apartamentoCombo.setItems(FXCollections.observableArrayList());
        disponibilidadLabel.setText(cantidadCatalogo
                + " apartamentos temporales en el catálogo. Consulta fechas y ocupación para ver opciones.");
    }

    @FXML
    private void agregarAcompanante() {
        String nombre = nombreAcompananteField.getText();
        LocalDate nacimiento = nacimientoAcompanantePicker.getValue();
        if (nombre == null || nombre.isBlank() || nacimiento == null) {
            mostrarError("Datos incompletos", "Escribe el nombre y la fecha de nacimiento del acompañante.");
            return;
        }
        acompanantes.add(new Ocupante(nombre, nacimiento));
        nombreAcompananteField.clear();
        actualizarCantidadPersonas();
    }

    @FXML
    private void quitarAcompanante() {
        Ocupante seleccionado = acompanantesListView.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            acompanantes.remove(seleccionado);
            actualizarCantidadPersonas();
        }
    }

    @FXML
    private void buscarApartamentosDisponibles() {
        try {
            actualizarApartamentosDisponibles(true);
        } catch (RuntimeException excepcion) {
            mostrarError("No se pudo consultar disponibilidad", mensaje(excepcion));
        }
    }

    private void actualizarApartamentosDisponibles(boolean mostrarMensaje) {
        Estancia estancia = crearEstancia();
        int personas = cantidadPersonas();
        Iterable<Apartamento> encontrados = contexto.getServicioBusquedaApartamentos()
                .porCapacidad(estancia, personas);

        ObservableList<Apartamento> opciones = FXCollections.observableArrayList();
        for (Apartamento apartamento : encontrados) {
            opciones.add(apartamento);
        }
        apartamentoCombo.setItems(opciones);
        if (!opciones.isEmpty()) {
            apartamentoCombo.getSelectionModel().selectFirst();
        }

        String texto = opciones.size() == 1
                ? "Se encontró 1 apartamento disponible. Orden: mayor capacidad primero."
                : "Se encontraron " + opciones.size()
                + " apartamentos disponibles. Orden: mayor capacidad primero.";
        if (opciones.isEmpty()) {
            texto = "No hay apartamentos disponibles para esas fechas y ocupación.";
        }
        disponibilidadLabel.setText(texto);
        if (mostrarMensaje && !opciones.isEmpty()) {
            resultadoLabel.setText("Selecciona un apartamento y registra la solicitud.");
        }
    }

    @FXML
    private void registrarReserva() {
        try {
            Reserva reserva = construirReserva();
            CotizacionAnticipo cotizacion = contexto.getServicioReservas()
                    .registrarSolicitud(reserva);
            // El perfil se incorpora después de registrar correctamente la
            // solicitud para no dejar huéspedes huérfanos ante un conflicto.
            contexto.getServicioPerfilHuesped().registrarHuesped(reserva.getTitular());
            mostrarCotizacion(cotizacion.getLiquidacion());
            agregarResumen(reserva, cotizacion);
            actualizarApartamentosDisponibles(false);

            String mensaje = "Solicitud " + reserva.getId()
                    + " registrada correctamente en estado PENDIENTE. "
                    + "Los datos de esta demostración son temporales.";
            if (reserva.getCanal() == CanalReserva.PORTAL) {
                mensaje += " Anticipo mínimo de referencia: "
                        + moneda(cotizacion.getAnticipoMinimo()) + ". No se registró ningún pago.";
            } else {
                mensaje += " La política de anticipo del canal Directa está pendiente de acuerdo.";
            }
            resultadoLabel.setText(mensaje);
            acompanantes.clear();
            actualizarCantidadPersonas();
        } catch (RuntimeException excepcion) {
            mostrarError("No se pudo registrar la reserva", mensaje(excepcion));
        }
    }

    private Reserva construirReserva() {
        String documento = textoObligatorio(documentoField.getText(), "documento");
        String nombre = textoObligatorio(nombreField.getText(), "nombre del huésped");
        String telefono = textoObligatorio(telefonoField.getText(), "teléfono");
        String correo = textoObligatorio(correoField.getText(), "correo electrónico");
        LocalDate nacimiento = nacimientoTitularPicker.getValue();
        if (nacimiento == null) {
            throw new IllegalArgumentException("Selecciona la fecha de nacimiento del titular.");
        }

        Estancia estancia = crearEstancia();
        Apartamento apartamento = apartamentoCombo.getValue();
        if (apartamento == null) {
            throw new IllegalArgumentException("Primero consulta y selecciona un apartamento disponible.");
        }
        CanalReserva canal = canalCombo.getValue();
        if (canal == null) {
            throw new IllegalArgumentException("Selecciona el canal de la reserva.");
        }
        if (cantidadPersonas() > apartamento.getCapacidadMaxima()) {
            throw new IllegalArgumentException("El grupo supera la capacidad máxima del apartamento.");
        }
        if (!contexto.getServicioDisponibilidad().estaDisponible(apartamento, estancia)) {
            throw new IllegalStateException(
                    "El apartamento dejó de estar disponible para las fechas seleccionadas. Consulta nuevamente.");
        }

        Huesped titular = contexto.getServicioPerfilHuesped().buscarHuesped(documento);
        if (titular == null) {
            titular = new Huesped(documento, nombre, nacimiento, telefono, correo, null);
        } else if (!titular.getNombre().equalsIgnoreCase(nombre)) {
            throw new IllegalArgumentException(
                    "El documento ya está registrado con otro nombre. Verifica los datos del huésped.");
        }

        ListaDoblementeEnlazada<Ocupante> ocupantes = new ListaDoblementeEnlazada<>();
        // La regla del modelo es explícita: el titular solo se aloja si se añade
        // a la lista de ocupantes. En esta pantalla se asume que el titular viaja.
        ocupantes.agregarUltimo(new Ocupante(titular.getNombre(), titular.getFechaNacimiento()));
        for (Ocupante acompanante : acompanantes) {
            ocupantes.agregarUltimo(acompanante);
        }

        String id = String.format(Locale.ROOT, "DEMO-R-%04d", consecutivoReserva++);
        return new Reserva(id, apartamento, titular, estancia, canal,
                LocalDateTime.now(), ocupantes);
    }

    private Estancia crearEstancia() {
        LocalDate entrada = entradaPicker.getValue();
        LocalDate salida = salidaPicker.getValue();
        if (entrada == null || salida == null) {
            throw new IllegalArgumentException("Selecciona las fechas de entrada y salida.");
        }
        return new Estancia(entrada, salida);
    }

    private void mostrarCotizacion(LiquidacionReserva liquidacion) {
        ObservableList<DetalleNoche> detalles = FXCollections.observableArrayList();
        for (DetalleNoche detalle : liquidacion) {
            detalles.add(detalle);
        }
        tablaDesglose.setItems(detalles);
        totalLabel.setText(moneda(liquidacion.getTotal()));
        CotizacionAnticipo cotizacion = contexto.getServicioReservas()
                .consultarCotizacion(liquidacion.getIdReserva());
        if (canalCombo.getValue() == CanalReserva.PORTAL) {
            anticipoLabel.setText(moneda(cotizacion.getAnticipoMinimo()) + " (30 % de referencia)");
        } else {
            anticipoLabel.setText("Pendiente de política para canal Directa");
        }
    }

    private void agregarResumen(Reserva reserva, CotizacionAnticipo cotizacion) {
        String fechas = reserva.getEstancia().getEntrada().format(FORMATO_FECHA)
                + " – " + reserva.getEstancia().getSalida().format(FORMATO_FECHA);
        reservasSesion.add(new ResumenReserva(
                reserva.getId(),
                reserva.getTitular().getNombre(),
                reserva.getApartamento().getCodigo(),
                fechas,
                moneda(cotizacion.getTotal()),
                reserva.getCanal().name(),
                reserva.getEstado().name()));
    }

    private void actualizarCantidadPersonas() {
        cantidadPersonasLabel.setText("Personas alojadas: " + cantidadPersonas()
                + " (titular incluido)");
    }

    private int cantidadPersonas() {
        return 1 + acompanantes.size();
    }

    private static String textoObligatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
        return valor.trim();
    }

    private static String moneda(BigDecimal valor) {
        return FORMATO_MONEDA.format(valor);
    }

    private static String mensaje(RuntimeException excepcion) {
        String mensaje = excepcion.getMessage();
        return mensaje == null || mensaje.isBlank()
                ? "Ocurrió un error inesperado. Revisa los datos e inténtalo nuevamente."
                : mensaje;
    }

    private void mostrarError(String titulo, String contenido) {
        resultadoLabel.setText(contenido);
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }

    /** Fila de presentación; no sustituye la entidad de dominio Reserva. */
    public record ResumenReserva(String id, String titular, String apartamento,
                                 String fechas, String total, String canal, String estado) { }
}
