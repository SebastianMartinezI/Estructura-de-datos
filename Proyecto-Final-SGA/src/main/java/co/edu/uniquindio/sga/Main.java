package co.edu.uniquindio.sga;

import co.edu.uniquindio.sga.interfaz.VistaPrincipal;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Punto de entrada de la aplicación JavaFX. */
public final class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        VistaPrincipal vistaPrincipal = new VistaPrincipal();

        Scene escena = new Scene(
                vistaPrincipal,
                1100,
                700
        );

        primaryStage.setTitle("SGA - Aparta-Suites UQ");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.setScene(escena);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}