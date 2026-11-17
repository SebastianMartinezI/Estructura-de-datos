package co.edu.uniquindio.sga.interfaz;

import co.edu.uniquindio.sga.dominio.modelo.Estancia;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.time.LocalDate;

/** Panel JavaFX para consultar las noches de una estancia. */
public final class PanelEstancia extends BorderPane {

    private final DatePicker campoEntrada =
            new DatePicker(LocalDate.now());

    private final DatePicker campoSalida =
            new DatePicker(LocalDate.now().plusDays(1));

    private final TextArea salida = new TextArea();

    public PanelEstancia() {
        setPadding(new Insets(24));

        // Se seleccionan las fechas desde el calendario.
        campoEntrada.setEditable(false);
        campoSalida.setEditable(false);

        Button botonCalcular = new Button("Calcular noches");
        botonCalcular.setOnAction(evento -> calcular());

        HBox formulario = new HBox(
                12,
                new Label("Entrada:"),
                campoEntrada,
                new Label("Salida:"),
                campoSalida,
                botonCalcular
        );

        formulario.setPadding(new Insets(0, 0, 16, 0));

        salida.setEditable(false);
        salida.setWrapText(true);

        setTop(formulario);
        setCenter(salida);

        calcular();
    }

    private void calcular() {
        LocalDate entrada = campoEntrada.getValue();
        LocalDate fechaSalida = campoSalida.getValue();

        if (entrada == null || fechaSalida == null) {
            salida.setText("Selecciona las fechas de entrada y salida.");
            return;
        }

        try {
            Estancia estancia = new Estancia(entrada, fechaSalida);

            StringBuilder detalle = new StringBuilder();

            detalle.append("Noches a cobrar: ")
                    .append(estancia.numeroNoches())
                    .append("\n\n");

            for (LocalDate noche : estancia) {
                detalle.append("- ")
                        .append(noche)
                        .append('\n');
            }

            detalle.append("\nLa noche de salida no se cobra.");

            salida.setText(detalle.toString());

        } catch (IllegalArgumentException ex) {
            salida.setText("Error: " + ex.getMessage());
        }
    }
}