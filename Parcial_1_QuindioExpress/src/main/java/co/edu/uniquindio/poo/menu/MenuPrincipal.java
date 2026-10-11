package co.edu.uniquindio.poo.menu;

import co.edu.uniquindio.poo.servicios.GestionDespachos;
import co.edu.uniquindio.poo.servicios.GestionPaquetes;
import co.edu.uniquindio.poo.servicios.GestionRepartidores;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Font;

/**
 * Menú principal de QuindíoExpress.
 */
public class MenuPrincipal {

    private static final Font FUENTE_NORMAL =
            new Font("SansSerif", Font.PLAIN, 16);

    private static final Font FUENTE_NEGRITA =
            new Font("SansSerif", Font.BOLD, 16);

    private final MenuPaquetes menuPaquetes;
    private final MenuRepartidores menuRepartidores;
    private final MenuDespachos menuDespachos;

    public MenuPrincipal(GestionPaquetes gestionPaquetes,
                         GestionRepartidores gestionRepartidores,
                         GestionDespachos gestionDespachos) {

        if (gestionPaquetes == null
                || gestionRepartidores == null
                || gestionDespachos == null) {
            throw new IllegalArgumentException(
                    "Las gestiones son obligatorias.");
        }

        this.menuPaquetes = new MenuPaquetes(gestionPaquetes);
        this.menuRepartidores = new MenuRepartidores(
                gestionRepartidores);
        this.menuDespachos = new MenuDespachos(gestionDespachos);
    }

    public void ejecutar() {

        String[] opciones = {
                "Gestión de paquetes",
                "Gestión de repartidores",
                "Gestión de despachos"
        };

        while (true) {

            int opcion = mostrarMenuYLeerOpcion(
                    "QUINDÍOEXPRESS - MENÚ PRINCIPAL",
                    opciones);

            switch (opcion) {
                case 1 -> menuPaquetes.ejecutar();
                case 2 -> menuRepartidores.ejecutar();
                case 3 -> menuDespachos.ejecutar();
                case 0 -> {
                    mostrarMensaje("Gracias por usar QuindíoExpress.");
                    return;
                }
                default -> mostrarError("Opción no válida.");
            }
        }
    }

    private int mostrarMenuYLeerOpcion(String titulo,
                                       String[] opciones) {

        StringBuilder menu = new StringBuilder();

        for (int i = 0; i < opciones.length; i++) {
            menu.append(i + 1)
                    .append(". ")
                    .append(opciones[i])
                    .append("\n");
        }

        menu.append("0. Salir");

        JTextArea areaMenu = new JTextArea(menu.toString());
        areaMenu.setEditable(false);
        areaMenu.setOpaque(false);
        areaMenu.setFocusable(false);
        areaMenu.setFont(FUENTE_NORMAL);

        JLabel etiqueta = new JLabel("Ingrese una opción:");
        etiqueta.setFont(FUENTE_NEGRITA);

        JTextField campoOpcion = new JTextField(7);
        campoOpcion.setFont(FUENTE_NORMAL);

        JPanel entrada = new JPanel(new BorderLayout(10, 0));
        entrada.add(etiqueta, BorderLayout.WEST);
        entrada.add(campoOpcion, BorderLayout.CENTER);

        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(
                15,
                18,
                15,
                18));
        panel.add(areaMenu, BorderLayout.CENTER);
        panel.add(entrada, BorderLayout.SOUTH);

        while (true) {

            JOptionPane pane = new JOptionPane(
                    panel,
                    JOptionPane.PLAIN_MESSAGE,
                    JOptionPane.OK_CANCEL_OPTION);

            JDialog dialogo = pane.createDialog(null, titulo);
            dialogo.setResizable(false);
            dialogo.setVisible(true);

            Object valor = pane.getValue();

            if (valor == null || !valor.equals(JOptionPane.OK_OPTION)) {
                return 0;
            }

            try {
                return Integer.parseInt(campoOpcion.getText().trim());

            } catch (NumberFormatException e) {
                mostrarError("Escriba un número de opción válido.");
                campoOpcion.setText("");
            }
        }
    }

    private void mostrarMensaje(String mensaje) {

        JOptionPane.showMessageDialog(
                null,
                mensaje,
                "QuindíoExpress",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {

        JOptionPane.showMessageDialog(
                null,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}