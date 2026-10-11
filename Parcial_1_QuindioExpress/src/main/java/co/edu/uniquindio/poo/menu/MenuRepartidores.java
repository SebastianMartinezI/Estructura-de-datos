package co.edu.uniquindio.poo.menu;

import co.edu.uniquindio.poo.modelo.Repartidor;
import co.edu.uniquindio.poo.servicios.GestionRepartidores;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

/**
 * Menú gráfico para gestionar los repartidores
 * del centro de distribución QuindíoExpress.
 *
 * @author Juan Martinez
 */
public class MenuRepartidores {

    private static final Dimension TAMANO_RESULTADO =
            new Dimension(500, 360);

    private static final Font FUENTE_NORMAL =
            new Font("SansSerif", Font.PLAIN, 16);

    private static final Font FUENTE_NEGRITA =
            new Font("SansSerif", Font.BOLD, 16);

    private final GestionRepartidores gestion;

    public MenuRepartidores(GestionRepartidores gestion) {

        if (gestion == null) {
            throw new IllegalArgumentException(
                    "La gestión de repartidores es obligatoria.");
        }

        this.gestion = gestion;
    }

    /**
     * Ejecuta el menú hasta que el usuario seleccione la
     * opción 0, que corresponde a volver.
     */
    public void ejecutar() {

        String[] opciones = {
                "Registrar repartidor",
                "Buscar repartidor por identificación",
                "Mostrar todos los repartidores",
                "Mostrar repartidores disponibles"
        };

        while (true) {

            int opcion = mostrarMenuYLeerOpcion(
                    "QUINDÍOEXPRESS - GESTIÓN DE REPARTIDORES",
                    opciones,
                    "Volver");

            if (opcion == 0) {
                return;
            }

            try {
                switch (opcion) {
                    case 1 -> registrarRepartidor();
                    case 2 -> buscarRepartidor();
                    case 3 -> mostrarLista(
                            "Todos los repartidores",
                            gestion.obtenerTodos());
                    case 4 -> mostrarLista(
                            "Repartidores disponibles",
                            gestion.obtenerDisponibles());
                    default -> mostrarError("Opción no válida.");
                }

            } catch (IllegalArgumentException e) {
                mostrarError(e.getMessage());
            }
        }
    }

    /**
     * Solicita los datos y registra un repartidor.
     * El repartidor se crea disponible.
     */
    private void registrarRepartidor() {

        String identificacion = leerIdentificacion();
        String nombre = leerTexto("Nombre completo:");
        String zona = leerTexto("Zona o municipio:");

        Repartidor repartidor = new Repartidor(
                identificacion,
                nombre,
                zona,
                true);

        boolean registrado = gestion.registrarRepartidor(repartidor);

        mostrarTexto("Registro de repartidor",
                registrado
                        ? "Repartidor registrado correctamente."
                        : "Ya existe un repartidor con esa identificación.");
    }

    /**
     * Busca un repartidor por su identificación.
     */
    private void buscarRepartidor() {

        String identificacion = leerIdentificacion();

        Repartidor repartidor =
                gestion.buscarPorIdentificacion(identificacion);

        mostrarTexto("Resultado de búsqueda",
                repartidor != null
                        ? repartidor.toString()
                        : "No se encontró ningún repartidor.");
    }

    /**
     * Muestra una lista de repartidores.
     */
    private void mostrarLista(String titulo,
                              List<Repartidor> repartidores) {

        if (repartidores == null || repartidores.isEmpty()) {
            mostrarTexto(titulo, "No hay repartidores para mostrar.");
            return;
        }

        StringBuilder texto = new StringBuilder();

        for (Repartidor repartidor : repartidores) {
            texto.append(repartidor).append("\n");
        }

        mostrarTexto(titulo, texto.toString());
    }

    /**
     * Lee una identificación obligatoria compuesta solo por dígitos.
     */
    private String leerIdentificacion() {

        while (true) {

            String identificacion = leerTexto("Identificación:");

            if (identificacion.matches("\\d+")) {
                return identificacion;
            }

            mostrarError(
                    "La identificación debe contener solo dígitos.");
        }
    }

    /**
     * Muestra un menú de texto numerado y permite escribir
     * el número de la opción seleccionada.
     *
     * @param titulo título de la ventana
     * @param opciones opciones del menú, numeradas desde 1
     * @param textoOpcionCero texto para la opción 0
     * @return número seleccionado o 0 si se cancela
     */
    private int mostrarMenuYLeerOpcion(String titulo,
                                       String[] opciones,
                                       String textoOpcionCero) {

        StringBuilder menu = new StringBuilder();

        for (int i = 0; i < opciones.length; i++) {
            menu.append(i + 1)
                    .append(". ")
                    .append(opciones[i])
                    .append("\n");
        }

        menu.append("0. ").append(textoOpcionCero);

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
        panel.setBorder(BorderFactory.createEmptyBorder(15, 18, 15, 18));
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

    /**
     * Muestra texto en una ventana con barra de desplazamiento.
     */
    private void mostrarTexto(String titulo, String mensaje) {

        JTextArea area = new JTextArea(mensaje);
        area.setEditable(false);
        area.setFont(FUENTE_NORMAL);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setCaretPosition(0);

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(TAMANO_RESULTADO);

        JOptionPane.showMessageDialog(
                null,
                scroll,
                titulo,
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Muestra un mensaje de error.
     */
    private void mostrarError(String mensaje) {

        JOptionPane.showMessageDialog(
                null,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Lee un texto obligatorio.
     */
    private String leerTexto(String mensaje) {

        while (true) {

            String valor = mostrarEntrada(mensaje);

            if (valor == null) {
                throw new IllegalArgumentException(
                        "Operación cancelada.");
            }

            valor = valor.trim();

            if (!valor.isEmpty()) {
                return valor;
            }

            mostrarError("Este campo no puede estar vacío.");
        }
    }

    /**
     * Muestra una ventana de entrada con campo de texto.
     *
     * @param mensaje texto que se muestra al usuario
     * @return texto ingresado o null si se cancela
     */
    private String mostrarEntrada(String mensaje) {

        JLabel etiqueta = new JLabel(mensaje);
        etiqueta.setFont(FUENTE_NEGRITA);

        JTextField campoTexto = new JTextField();
        campoTexto.setFont(FUENTE_NORMAL);
        campoTexto.setPreferredSize(new Dimension(300, 34));

        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 18, 15, 18));
        panel.add(etiqueta, BorderLayout.NORTH);
        panel.add(campoTexto, BorderLayout.CENTER);

        JOptionPane pane = new JOptionPane(
                panel,
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION);

        JDialog dialogo = pane.createDialog(null, "Entrada");
        dialogo.setResizable(false);
        dialogo.setVisible(true);

        Object valor = pane.getValue();

        if (valor == null || !valor.equals(JOptionPane.OK_OPTION)) {
            return null;
        }

        return campoTexto.getText();
    }
}