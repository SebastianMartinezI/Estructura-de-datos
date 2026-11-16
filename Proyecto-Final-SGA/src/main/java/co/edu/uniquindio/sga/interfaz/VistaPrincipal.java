package co.edu.uniquindio.sga.interfaz;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/** Contenedor principal de la interfaz JavaFX del SGA. */
public final class VistaPrincipal extends BorderPane {

    public VistaPrincipal() {
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

        pestanas.getTabs().add(crearPestana(
                "Inicio",
                "Estado de los apartamentos",
                "Pendiente de conectar con los apartamentos del alojamiento."
        ));

        pestanas.getTabs().add(crearPestana(
                "Preparación",
                "Preparación de apartamentos",
                "Pendiente de conectar con la cola de preparación."
        ));

        pestanas.getTabs().add(crearPestana(
                "Mapa",
                "Mapa del edificio",
                "Pendiente de conectar con el grafo y las rutas."
        ));

        pestanas.getTabs().add(crearPestana(
                "Folios",
                "Cargos y pagos",
                "Pendiente de conectar con las operaciones de los folios."
        ));

        pestanas.getTabs().add(crearPestana(
                "Turnos",
                "Turnos de recepción",
                "Pendiente de conectar con el servicio de turnos."
        ));

        pestanas.getTabs().add(crearPestana(
                "Reportes",
                "Estadísticas del alojamiento",
                "Pendiente de conectar con los reportes del sistema."
        ));

        setCenter(pestanas);
    }

    private Tab crearPestana(
            String nombre,
            String titulo,
            String descripcion
    ) {
        Label encabezado = new Label(titulo);

        encabezado.setStyle(
                "-fx-font-size: 20px; -fx-font-weight: bold;"
        );

        Label detalle = new Label(descripcion);
        detalle.setWrapText(true);

        VBox contenido = new VBox(12, encabezado, detalle);
        contenido.setPadding(new Insets(24));

        return new Tab(nombre, contenido);
    }
}