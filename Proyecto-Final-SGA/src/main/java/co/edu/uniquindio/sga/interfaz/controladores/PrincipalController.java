package co.edu.uniquindio.sga.interfaz.controladores;

import co.edu.uniquindio.sga.interfaz.contexto.ContextoAplicacion;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Controla la navegación principal y carga las vistas de cada módulo.
 *
 * Las reglas de negocio permanecen en los servicios inyectados
 * mediante el contexto de aplicación.
 *
 * Conserva los módulos de Juan y la vista de preparación de Cristian.
 */
public final class PrincipalController {

    private final ContextoAplicacion contexto;

    private Node panelInformativo;
    private Node vistaReservas;
    private Node vistaDisponibilidad;
    private Node vistaCalendario;
    private Node vistaHuespedes;
    private Node vistaPreparacion;

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

    @FXML
    private void seleccionarModulo(ActionEvent evento) {
        Button boton = (Button) evento.getSource();
        Object dato = boton.getUserData();

        if (!(dato instanceof String codigo) || codigo.isBlank()) {
            mostrarError(
                    "Opción no reconocida",
                    "No se ha definido correctamente el módulo seleccionado."
            );
            return;
        }

        try {
            mostrar(Modulo.valueOf(codigo));
        } catch (IllegalArgumentException excepcion) {
            mostrarError(
                    "Opción no reconocida",
                    "No se reconoce el módulo seleccionado."
            );
        }
    }

    private void mostrar(Modulo modulo) {
        switch (modulo) {
            case RESERVAS:
                mostrarVistaReservas();
                return;

            case DISPONIBILIDAD:
                mostrarVistaDisponibilidad();
                return;

            case CALENDARIO:
                mostrarVistaCalendario();
                return;

            case HUESPEDES:
                mostrarVistaHuespedes();
                return;

            case PREPARACION:
                mostrarVistaPreparacion();
                return;

            default:
                mostrarPanelInformativo(modulo);
        }
    }

    private void mostrarPanelInformativo(Modulo modulo) {
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
     * Carga y muestra la pantalla de reservas de Juan.
     */
    private void mostrarVistaReservas() {
        try {
            if (vistaReservas == null) {
                FXMLLoader cargador = new FXMLLoader(
                        buscarVista(
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

                vistaReservas = cargador.load();
            }

            contenidoPrincipal.getChildren().setAll(vistaReservas);
            etiquetaSuperior.setText(
                    "RESERVAS · DATOS TEMPORALES"
            );

        } catch (IOException | RuntimeException excepcion) {
            excepcion.printStackTrace();

            mostrarError(
                    "No se pudo abrir Reservas",
                    obtenerMensajeError(
                            excepcion,
                            "Verifica reservas.fxml y su controlador."
                    )
            );
        }
    }

    /**
     * Carga y muestra la pantalla de disponibilidad de Juan.
     */
    private void mostrarVistaDisponibilidad() {
        try {
            if (vistaDisponibilidad == null) {
                FXMLLoader cargador = new FXMLLoader(
                        buscarVista(
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
                    obtenerMensajeError(
                            excepcion,
                            "Verifica disponibilidad.fxml y su controlador."
                    )
            );
        }
    }

    /**
     * Carga y muestra el calendario mensual de Juan.
     */
    private void mostrarVistaCalendario() {
        try {
            if (vistaCalendario == null) {
                FXMLLoader cargador = new FXMLLoader(
                        buscarVista(
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
                    obtenerMensajeError(
                            excepcion,
                            "Verifica calendario.fxml y su controlador."
                    )
            );
        }
    }

    /**
     * Carga y muestra el módulo de huéspedes de Juan.
     */
    private void mostrarVistaHuespedes() {
        try {
            if (vistaHuespedes == null) {
                FXMLLoader cargador = new FXMLLoader(
                        buscarVista(
                                "/co/edu/uniquindio/sga/vistas/huespedes.fxml"
                        )
                );

                cargador.setControllerFactory(tipo -> {
                    if (tipo == HuespedController.class) {
                        return new HuespedController(contexto);
                    }

                    throw new IllegalArgumentException(
                            "No existe una fábrica de controladores para "
                                    + tipo.getName()
                    );
                });

                vistaHuespedes = cargador.load();
            }

            contenidoPrincipal.getChildren().setAll(vistaHuespedes);
            etiquetaSuperior.setText(
                    "HUÉSPEDES · DATOS TEMPORALES"
            );

        } catch (IOException | RuntimeException excepcion) {
            excepcion.printStackTrace();

            mostrarError(
                    "No se pudo abrir Huéspedes",
                    obtenerMensajeError(
                            excepcion,
                            "Verifica huespedes.fxml y su controlador."
                    )
            );
        }
    }

    /**
     * Carga y muestra el módulo de preparación de Cristian.
     */
    private void mostrarVistaPreparacion() {
        try {
            if (vistaPreparacion == null) {
                FXMLLoader cargador = new FXMLLoader(
                        buscarVista(
                                "/co/edu/uniquindio/sga/vistas/VistaPreparacion.fxml"
                        )
                );

                vistaPreparacion = cargador.load();
            }

            contenidoPrincipal.getChildren().setAll(vistaPreparacion);
            etiquetaSuperior.setText(
                    "PREPARACIÓN · CRISTIAN"
            );

        } catch (IOException | RuntimeException excepcion) {
            excepcion.printStackTrace();

            mostrarError(
                    "No se pudo abrir Preparación",
                    obtenerMensajeError(
                            excepcion,
                            "Verifica VistaPreparacion.fxml "
                                    + "y VistaPreparacionController."
                    )
            );
        }
    }

    /**
     * Comprueba la existencia del recurso antes de cargarlo.
     */
    private URL buscarVista(String ruta) {
        URL recurso = getClass().getResource(ruta);

        if (recurso == null) {
            throw new IllegalStateException(
                    "No se encontró el archivo FXML:\n"
                            + ruta
                            + "\n\nDebe estar dentro de src/main/resources"
                            + " conservando esa estructura de carpetas."
            );
        }

        return recurso;
    }

    private String obtenerMensajeError(
            Exception excepcion,
            String mensajePredeterminado
    ) {
        return excepcion.getMessage() == null
                ? mensajePredeterminado
                : excepcion.getMessage();
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }

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