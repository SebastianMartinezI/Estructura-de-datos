package co.edu.uniquindio.sga;

import co.edu.uniquindio.sga.interfaz.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/** Punto de entrada de la aplicación de escritorio. */
public final class Main {
    private Main() { }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // La aplicación continúa con el estilo gráfico por defecto.
            }
            new MainFrame().setVisible(true);
        });
    }
}
