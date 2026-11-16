package co.edu.uniquindio.sga;

import co.edu.uniquindio.sga.servicios.operacion.GestionPreparacionService;
import co.edu.uniquindio.sga.servicios.operacion.PreparacionService;
import co.edu.uniquindio.sga.servicios.operacion.RutaPreparacionService;
import co.edu.uniquindio.sga.servicios.operacion.TurnoService;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.Clock;
import java.time.LocalDateTime;

public class PruebaVentanaPreparacion extends Application {

    private GestionPreparacionService gestion;
    private ListView<String> listaPendientes;
    private TextArea areaResultado;

    @Override
    public void start(Stage escenario) {
        prepararServicios();
        cargarDatosDePrueba();

        Label titulo = new Label("Preparación de apartamentos");
        titulo.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        listaPendientes = new ListView<>();
        listaPendientes.setPrefHeight(180);

        Button botonActualizar = new Button("Actualizar pendientes");
        Button botonAsignar = new Button("Asignar siguiente tarea");
        Button botonOrden = new Button("Calcular orden de recorrido");

        areaResultado = new TextArea();
        areaResultado.setEditable(false);
        areaResultado.setPrefHeight(160);

        botonActualizar.setOnAction(evento -> actualizarPendientes());
        botonAsignar.setOnAction(evento -> asignarSiguiente());
        botonOrden.setOnAction(evento -> calcularOrden());

        VBox contenido = new VBox(
                12,
                titulo,
                listaPendientes,
                botonActualizar,
                botonAsignar,
                botonOrden,
                areaResultado
        );

        contenido.setPadding(new Insets(18));

        Scene escena = new Scene(contenido, 520, 620);

        escenario.setTitle("SGA - Preparación");
        escenario.setScene(escena);
        escenario.show();

        actualizarPendientes();
    }

    private void prepararServicios() {
        Clock reloj = Clock.systemDefaultZone();

        PreparacionService preparacion =
                new PreparacionService(reloj);

        TurnoService turnos = new TurnoService();
        RutaPreparacionService rutas = new RutaPreparacionService();

        gestion = new GestionPreparacionService(
                preparacion,
                turnos,
                rutas
        );

        turnos.registrarPersonal("Ana");
        turnos.registrarPersonal("Luis");
    }

    private void cargarDatosDePrueba() {
        gestion.registrarPendiente(
                "A-101",
                LocalDateTime.now().plusHours(2)
        );

        gestion.registrarPendiente(
                "A-102",
                null
        );

        gestion.registrarPendiente(
                "A-203",
                LocalDateTime.now().plusHours(5)
        );
    }

    private void actualizarPendientes() {
        listaPendientes.getItems().clear();

        listaPendientes.getItems().addAll(
                gestion.obtenerCodigosPendientes()
        );

        areaResultado.setText(
                "Tareas pendientes: "
                        + gestion.cantidadPendientes()
        );
    }

    private void asignarSiguiente() {
        try {
            String responsable = gestion.asignarSiguienteTarea();

            areaResultado.setText(
                    "Tarea asignada a: " + responsable
            );

            actualizarPendientes();

        } catch (Exception excepcion) {
            areaResultado.setText(
                    "No se pudo asignar la tarea: "
                            + excepcion.getMessage()
            );
        }
    }

    private void calcularOrden() {
        try {
            areaResultado.setText(
                    "Orden de recorrido:\n"
                            + String.join(
                            "\n",
                            gestion.calcularOrdenRecorrido()
                    )
            );

        } catch (Exception excepcion) {
            areaResultado.setText(
                    "No se pudo calcular el recorrido: "
                            + excepcion.getMessage()
            );
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}