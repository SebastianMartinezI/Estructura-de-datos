package co.edu.uniquindio.sga.interfaz;

import co.edu.uniquindio.sga.interfaz.contexto.ContextoAplicacion;
import co.edu.uniquindio.sga.interfaz.controladores.PrincipalController;
import co.edu.uniquindio.sga.interfaz.demo.FabricaContextoDemo;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Inicia JavaFX y construye una única instancia compartida de los servicios. */
public final class SgaApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        URL vista = Objects.requireNonNull(
                getClass().getResource("/co/edu/uniquindio/sga/vistas/principal.fxml"),
                "No se encontró la vista principal.fxml");
        URL estilos = Objects.requireNonNull(
                getClass().getResource("/co/edu/uniquindio/sga/css/estilos.css"),
                "No se encontró la hoja estilos.css");

        ContextoAplicacion contexto = FabricaContextoDemo.crear();
        FXMLLoader cargador = new FXMLLoader(vista);
        cargador.setControllerFactory(tipo -> {
            if (tipo == PrincipalController.class) {
                return new PrincipalController(contexto);
            }
            throw new IllegalArgumentException(
                    "No existe una fábrica de controladores para " + tipo.getName());
        });

        Parent contenido = cargador.load();
        Scene escena = new Scene(contenido, 1180, 820);
        escena.getStylesheets().add(estilos.toExternalForm());

        stage.setTitle("SGA | Sistema de Gestión de Alojamiento · DEMO");
        stage.setMinWidth(1040);
        stage.setMinHeight(720);
        stage.setScene(escena);
        stage.show();
    }
}
