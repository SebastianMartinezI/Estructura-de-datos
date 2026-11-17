package co.edu.uniquindio.sga.interfaz;

import co.edu.uniquindio.sga.interfaz.preparacion.VistaPreparacionController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/** Contenedor principal de la interfaz JavaFX del alojamiento. */
public final class MainFrame extends BorderPane {

    public MainFrame() {
        configurarEncabezado();
        configurarContenido();
    }

    private void configurarEncabezado() {
        Label titulo = new Label(
                "SGA - Sistema de Gestión de Alojamiento"
        );

        titulo.setStyle(
                "-fx-font-size: 24px; -fx-font-weight: bold;"
        );

        Label subtitulo = new Label(
                "Aparta-Suites UQ - Salento, Quindío"
        );

        VBox encabezado = new VBox(8, titulo, subtitulo);
        encabezado.setPadding(new Insets(20));

        setTop(encabezado);
    }

    private void configurarContenido() {
        TabPane pestanas = new TabPane();

        pestanas.setTabClosingPolicy(
                TabPane.TabClosingPolicy.UNAVAILABLE
        );

        Tab inicio = new Tab("Inicio", crearInicio());

        Tab estancia = new Tab(
                "Consulta de noches",
                new PanelEstancia()
        );

        Tab preparacion = crearPestanaPreparacion();

        pestanas.getTabs().add(inicio);
        pestanas.getTabs().add(estancia);
        pestanas.getTabs().add(preparacion);

        setCenter(pestanas);
    }

    private VBox crearInicio() {
        Label bienvenida = new Label(
                "Bienvenido a Aparta-Suites UQ"
        );

        bienvenida.setStyle("-fx-font-size: 20px;");

        Label descripcion = new Label(
                "Consulta las noches de una estancia "
                        + "desde la pestaña Consulta de noches."
        );

        descripcion.setWrapText(true);

        VBox contenido = new VBox(
                12,
                bienvenida,
                descripcion
        );

        contenido.setPadding(new Insets(24));

        return contenido;
    }

    private Tab crearPestanaPreparacion() {
        try {
            FXMLLoader cargador = new FXMLLoader(
                    getClass().getResource("/vistas/VistaPreparacion.fxml")
            );

            Parent contenido = cargador.load();

            return new Tab("Preparación", contenido);

        } catch (Exception e) {
            Label error = new Label(
                    "No fue posible cargar la vista de preparación."
            );

            return new Tab("Preparación", error);
        }
    }
}