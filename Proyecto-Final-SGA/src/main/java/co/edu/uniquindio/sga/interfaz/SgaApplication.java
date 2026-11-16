
package co.edu.uniquindio.sga.interfaz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Aplicacion grafica del Sistema de Gestion
 * de Alojamiento (SGA).
 *
 * Responsabilidad: inicializar la interfaz JavaFX.
 */
public final class SgaApplication extends Application {

    @Override
    public void start(Stage stage) {

        Label titulo = new Label("SGA - Aparta-Suites UQ");

        titulo.setStyle(
                "-fx-font-size: 28px;" +
                        "-fx-font-weight: bold;"
        );

        StackPane contenedor = new StackPane(titulo);

        Scene escena = new Scene(contenedor, 1000, 650);

        stage.setTitle("SGA | Sistema de Gestion de Alojamiento");
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.setScene(escena);
        stage.show();
    }
}
