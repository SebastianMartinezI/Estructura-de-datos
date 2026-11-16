package co.edu.uniquindio.sga.interfaz;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Inicia la interfaz gráfica JavaFX del Sistema de Gestión de Alojamiento.
 * No contiene reglas de negocio ni dependencias de las estructuras propias.
 */
public final class SgaApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        URL vista = Objects.requireNonNull(
                getClass().getResource("/co/edu/uniquindio/sga/vistas/principal.fxml"),
                "No se encontró la vista principal.fxml");
        URL estilos = Objects.requireNonNull(
                getClass().getResource("/co/edu/uniquindio/sga/css/estilos.css"),
                "No se encontró la hoja estilos.css");

        Parent contenido = FXMLLoader.load(vista);
        Scene escena = new Scene(contenido, 1100, 700);
        escena.getStylesheets().add(estilos.toExternalForm());

        stage.setTitle("SGA | Sistema de Gestión de Alojamiento");
        stage.setMinWidth(960);
        stage.setMinHeight(680);
        stage.setScene(escena);
        stage.show();
    }
}
