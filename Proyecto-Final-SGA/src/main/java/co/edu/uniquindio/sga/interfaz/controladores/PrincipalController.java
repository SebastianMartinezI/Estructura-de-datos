
package co.edu.uniquindio.sga.interfaz.controladores;

import co.edu.uniquindio.sga.interfaz.contexto.ContextoAplicacion;

import java.io.IOException;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Controla la navegación principal y carga las vistas de cada módulo.
 * Las reglas de negocio permanecen en los servicios inyectados
 * en el contexto de aplicación.
 */
public final class PrincipalController {

    private final ContextoAplicacion contexto;

    private Node panelInformativo;
    private Node vistaReservas;
    private Node vistaDisponibilidad;
    private Node vistaCalendario;

    @FXML
    private StackPane contenidoPrincipal;

    @FXML
    private Label tituloModulo;

    @FXML
    private Label descripcionModulo;

    @FXML
    private Label estadoModulo;

    @FXML
    private Label etiquetaSuperior;

    public PrincipalController(ContextoAplicacion contexto) {
        this.contexto = Objects.requireNonNull(
                contexto,
                "El contexto de aplicación es obligatorio"
        );
    }

    @FXML
    private void initialize() {
        panelInformativo = contenidoPrincipal.getChildren().get(0);
        mostrar(Modulo.INICIO);
    }

    /**
     * Atiende las opciones del menú y carga la vista correspondiente.
     */
    @FXML
    private void seleccionarModulo(ActionEvent evento) {

        Button boton = (Button) evento.getSource();
        String codigo = (String) boton.getUserData();

        try {
            mostrar(Modulo.valueOf(codigo));

        } catch (IllegalArgumentException excepcion) {
            mostrarError(
                    "Opción no reconocida",
                    "No se reconoce el módulo seleccionado."
            );
        }
    }

    /**
     * Determina qué pantalla debe mostrarse.
     */
    private void mostrar(Modulo modulo) {

        if (modulo == Modulo.RESERVAS) {
            mostrarVistaReservas();
            return;
        }

        if (modulo == Modulo.DISPONIBILIDAD) {
            mostrarVistaDisponibilidad();
            return;
        }

        if (modulo == Modulo.CALENDARIO) {
            mostrarVistaCalendario();
            return;
        }

        contenidoPrincipal.getChildren().setAll(panelInformativo);

        tituloModulo.setText(modulo.titulo);
        descripcionModulo.setText(modulo.descripcion);

        estadoModulo.setText(
                modulo == Modulo.INICIO
                        ? "Base gráfica operativa · Catálogo DEMO cargado"
                        : "Módulo pendiente de implementación"
        );

        etiquetaSuperior.setText(
                modulo == Modulo.INICIO
                        ? "DATOS TEMPORALES · DEMO"
                        : "MÓDULO EN DESARROLLO"
        );
    }

    /**
     * Carga y muestra la pantalla de disponibilidad.
     */
    private void mostrarVistaDisponibilidad() {

        try {

            if (vistaDisponibilidad == null) {

                FXMLLoader cargador = new FXMLLoader(
                        getClass().getResource(
                                "/co/edu/uniquindio/sga/vistas/disponibilidad.fxml"
                        )
                );

                cargador.setControllerFactory(tipo -> {

                    if (tipo == DisponibilidadController.class) {
                        return new DisponibilidadController(contexto);
                    }

                    throw new IllegalArgumentException(
                            "No existe una fábrica de controladores para "
                                    + tipo.getName()
                    );
                });

                vistaDisponibilidad = cargador.load();
            }

            contenidoPrincipal.getChildren().setAll(vistaDisponibilidad);

            etiquetaSuperior.setText(
                    "DISPONIBILIDAD · DATOS TEMPORALES"
            );

        } catch (IOException | RuntimeException excepcion) {

            excepcion.printStackTrace();

            mostrarError(
                    "No se pudo abrir Disponibilidad",
                    excepcion.getMessage() == null
                            ? "Verifica disponibilidad.fxml y su controlador."
                            : excepcion.getMessage()
            );
        }
    }


    /**
     * Carga y muestra la pantalla de reservas.
     */
    private void mostrarVistaReservas() {

        try {

            if (vistaReservas == null) {

                FXMLLoader cargador = new FXMLLoader(
                        getClass().getResource(
                                "/co/edu/uniquindio/sga/vistas/reservas.fxml"
                        )
                );

                cargador.setControllerFactory(tipo -> {

                    if (tipo == ReservasController.class) {
                        return new ReservasController(contexto);
                    }

                    throw new IllegalArgumentException(
                            "No existe una fábrica de controladores para "
                                    + tipo.getName()
                    );
                });

                Parent vista = cargador.load();
                vistaReservas = vista;
            }

            contenidoPrincipal.getChildren().setAll(vistaReservas);

            etiquetaSuperior.setText(
                    "RESERVAS · DATOS TEMPORALES"
            );

        } catch (IOException | RuntimeException excepcion) {

            excepcion.printStackTrace();

            mostrarError(
                    "No se pudo abrir Reservas",
                    excepcion.getMessage() == null
                            ? "Verifica reservas.fxml y su controlador."
                            : excepcion.getMessage()
            );
        }
    }

    /**
     * Carga y muestra el calendario mensual de ocupación.
     */
    private void mostrarVistaCalendario() {

        try {

            if (vistaCalendario == null) {

                FXMLLoader cargador = new FXMLLoader(
                        getClass().getResource(
                                "/co/edu/uniquindio/sga/vistas/calendario.fxml"
                        )
                );

                cargador.setControllerFactory(tipo -> {

                    if (tipo == CalendarioController.class) {
                        return new CalendarioController(contexto);
                    }

                    throw new IllegalArgumentException(
                            "No existe una fábrica de controladores para "
                                    + tipo.getName()
                    );
                });

                vistaCalendario = cargador.load();
            }

            contenidoPrincipal.getChildren().setAll(vistaCalendario);

            etiquetaSuperior.setText(
                    "CALENDARIO · DATOS TEMPORALES"
            );

        } catch (IOException | RuntimeException excepcion) {

            excepcion.printStackTrace();

            mostrarError(
                    "No se pudo abrir Calendario",
                    excepcion.getMessage() == null
                            ? "Verifica calendario.fxml y su controlador."
                            : excepcion.getMessage()
            );
        }
    }

    /**
     * Muestra mensajes de error de la interfaz.
     */
    private void mostrarError(String titulo, String mensaje) {

        Alert alerta = new Alert(Alert.AlertType.ERROR);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }

    /**
     * Secciones del menú principal.
     */
    private enum Modulo {

        INICIO(
                "Inicio",
                "Base de navegación JavaFX del SGA. "
                        + "El catálogo de apartamentos está cargado "
                        + "con datos temporales de demostración."
        ),

        RESERVAS(
                "Reservas",
                "Registro y cotización de solicitudes."
        ),

        DISPONIBILIDAD(
                "Disponibilidad",
                "Juan: búsqueda de apartamentos libres "
                        + "para fechas y número de ocupantes."
        ),

        CALENDARIO(
                "Calendario de ocupación",
                "Juan: visualización mensual de noches libres, "
                        + "ocupadas y bloqueadas."
        ),

        HUESPEDES(
                "Huéspedes",
                "Juan: perfiles, historial de estancias "
                        + "y apartamentos favoritos."
        ),

        TABLERO(
                "Tablero principal",
                "Cristian: estados operativos y resumen del alojamiento."
        ),

        PREPARACION(
                "Preparación",
                "Cristian: cola de prioridad de limpieza "
                        + "y seguimiento de apartamentos."
        ),

        MAPA(
                "Mapa del edificio",
                "Cristian: grafo ponderado y rutas de preparación."
        ),

        FOLIOS(
                "Folios",
                "Cristian: cargos, pagos, saldos "
                        + "y reversión de movimientos."
        ),

        ESTADISTICAS(
                "Estadísticas",
                "Cristian: indicadores de ocupación, "
                        + "ingresos y operación."
        );

        private final String titulo;
        private final String descripcion;

        Modulo(String titulo, String descripcion) {
            this.titulo = titulo;
            this.descripcion = descripcion;
        }
    }
}
