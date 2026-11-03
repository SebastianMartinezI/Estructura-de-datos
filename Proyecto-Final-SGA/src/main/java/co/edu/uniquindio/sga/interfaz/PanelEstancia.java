package co.edu.uniquindio.sga.interfaz;

import co.edu.uniquindio.sga.dominio.modelo.Estancia;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/** Mini demostración funcional del recorrido de noches con Iterator. */
public final class PanelEstancia extends JPanel {
    private final JTextField campoEntrada = new JTextField("2026-11-10", 12);
    private final JTextField campoSalida = new JTextField("2026-11-12", 12);
    private final JTextArea salida = new JTextArea();

    public PanelEstancia() {
        super(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        formulario.add(new JLabel("Entrada (AAAA-MM-DD):"));
        formulario.add(campoEntrada);
        formulario.add(new JLabel("Salida:"));
        formulario.add(campoSalida);
        JButton boton = new JButton("Calcular noches");
        boton.addActionListener(e -> calcular());
        formulario.add(boton);
        add(formulario, BorderLayout.NORTH);

        salida.setEditable(false);
        salida.setLineWrap(true);
        salida.setWrapStyleWord(true);
        add(new JScrollPane(salida), BorderLayout.CENTER);
        calcular();
    }

    private void calcular() {
        try {
            Estancia estancia = new Estancia(
                    LocalDate.parse(campoEntrada.getText().trim()),
                    LocalDate.parse(campoSalida.getText().trim()));
            StringBuilder detalle = new StringBuilder();
            detalle.append("Noches a cobrar: ").append(estancia.numeroNoches()).append("\n\n");
            for (LocalDate noche : estancia) {
                detalle.append("- ").append(noche).append('\n');
            }
            detalle.append("\nLa noche de salida NO se cobra.");
            salida.setText(detalle.toString());
        } catch (DateTimeParseException ex) {
            salida.setText("Error: escribe las fechas con formato AAAA-MM-DD.");
        } catch (IllegalArgumentException ex) {
            salida.setText("Error: " + ex.getMessage());
        }
    }
}
