package co.edu.uniquindio.sga;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/** Punto de entrada de la aplicación de escritorio con JavaFX. */
public final class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label mensaje = new Label("SGA - Sistema de Gestion de Alojamiento ");

        StackPane raiz = new StackPane();
        raiz.getChildren().add(mensaje);

        Scene escena = new Scene(raiz, 600, 400);

        primaryStage.setTitle("Bienvenido a Aparta-Suite UQ");
        primaryStage.setScene(escena);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}