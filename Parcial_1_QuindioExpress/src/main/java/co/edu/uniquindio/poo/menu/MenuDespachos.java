package co.edu.uniquindio.poo.menu;

import co.edu.uniquindio.poo.modelo.Paquete;
import co.edu.uniquindio.poo.modelo.Repartidor;
import co.edu.uniquindio.poo.servicios.GestionDespachos;

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
import java.util.Map;

/**
 * Menú gráfico para preparar colas, asignar repartidores
 * y registrar las entregas de QuindíoExpress.
 */
public class MenuDespachos {

    private static final Dimension TAMANO_RESULTADO =
            new Dimension(500, 360);

    private static final Font FUENTE_NORMAL =
            new Font("SansSerif", Font.PLAIN, 16);

    private static final Font FUENTE_NEGRITA =
            new Font("SansSerif", Font.BOLD, 16);

    private final GestionDespachos gestion;

    public MenuDespachos(GestionDespachos gestion) {

        if (gestion == null) {
            throw new IllegalArgumentException(
                    "La gestión de despachos es obligatoria.");
        }

        this.gestion = gestion;
    }

    /**
     * Ejecuta el menú hasta que el usuario elija volver.
     */
    public void ejecutar() {

        String[] opciones = {
                "Preparar colas de atención y despacho",
                "Obtener siguiente por orden de llegada",
                "Obtener siguiente por prioridad",
                "Asignar paquete a repartidor",
                "Registrar entrega",
                "Mostrar paquetes asignados",
                "Mostrar historial de entregas",
                "Mostrar cantidad de entregas"
        };

        while (true) {

            int opcion = mostrarMenuYLeerOpcion(
                    "QUINDÍOEXPRESS - DESPACHOS",
                    opciones,
                    "Volver");

            if (opcion == 0) {
                return;
            }

            try {
                switch (opcion) {
                    case 1 -> prepararColas();
                    case 2 -> siguientePorLlegada();
                    case 3 -> siguientePorPrioridad();
                    case 4 -> asignarPaquete();
                    case 5 -> registrarEntrega();
                    case 6 -> mostrarAsignaciones();
                    case 7 -> mostrarHistorial();
                    case 8 -> mostrarCantidadEntregas();
                    default -> mostrarError("Opción no válida.");
                }

            } catch (IllegalArgumentException e) {
                mostrarError(e.getMessage());
            }
        }
    }

    /**
     * Carga las dos colas con paquetes pendientes y muestra
     * una previsualización sin retirar paquetes.
     */
    private void prepararColas() {

        gestion.prepararColas();

        Paquete primeroLlegada = gestion.verSiguientePorLlegada();
        Paquete primeroPrioridad = gestion.verSiguientePrioritario();

        StringBuilder texto = new StringBuilder();

        texto.append("Las colas fueron preparadas correctamente.\n\n")
                .append("Paquetes pendientes cargados: ")
                .append(gestion.obtenerCantidadPendientes())
                .append("\n\n");

        texto.append("Siguiente por orden de llegada:\n");

        if (primeroLlegada != null) {
            texto.append(primeroLlegada);
        } else {
            texto.append("No hay paquetes pendientes.");
        }

        texto.append("\n\n")
                .append("Siguiente por prioridad:\n");

        if (primeroPrioridad != null) {
            texto.append(primeroPrioridad);
        } else {
            texto.append("No hay paquetes pendientes.");
        }

        mostrarTexto("Colas preparadas", texto.toString());
    }

    /**
     * Retira y muestra el siguiente paquete de la cola FIFO.
     */
    private void siguientePorLlegada() {

        Paquete paquete = gestion.siguientePorLlegada();

        mostrarTexto("Atención por orden de llegada",
                paquete != null
                        ? paquete.toString()
                        : "No hay paquetes pendientes en la cola.");
    }

    /**
     * Retira y muestra el siguiente paquete de la cola prioritaria.
     */
    private void siguientePorPrioridad() {

        Paquete paquete = gestion.siguientePrioritario();

        mostrarTexto("Despacho prioritario",
                paquete != null
                        ? paquete.toString()
                        : "No hay paquetes pendientes en la cola.");
    }

    /**
     * Solicita un código de paquete y una identificación
     * para crear una asignación.
     */
    private void asignarPaquete() {

        String codigo = leerTexto("Código del paquete:");
        String identificacion =
                leerTexto("Identificación del repartidor:");

        gestion.asignarPaquete(codigo, identificacion);

        mostrarTexto("Asignación",
                "El paquete " + codigo
                        + " fue asignado correctamente al repartidor "
                        + identificacion + ".");
    }

    /**
     * Registra la entrega de un paquete asignado.
     */
    private void registrarEntrega() {

        String codigo = leerTexto("Código del paquete entregado:");

        boolean entregado = gestion.registrarEntrega(codigo);

        mostrarTexto("Entrega",
                entregado
                        ? "La entrega de " + codigo
                        + " se registró correctamente."
                        : "No fue posible registrar la entrega.");
    }

    /**
     * Muestra los paquetes que tienen repartidor asignado.
     */
    private void mostrarAsignaciones() {

        Map<String, Repartidor> asignaciones =
                gestion.obtenerAsignaciones();

        if (asignaciones.isEmpty()) {
            mostrarTexto("Paquetes asignados",
                    "No hay paquetes asignados actualmente.");
            return;
        }

        StringBuilder texto = new StringBuilder();

        for (Map.Entry<String, Repartidor> entrada
                : asignaciones.entrySet()) {

            Repartidor repartidor = entrada.getValue();

            texto.append("Paquete: ")
                    .append(entrada.getKey())
                    .append("\nRepartidor: ")
                    .append(repartidor.getNombre())
                    .append(" (")
                    .append(repartidor.getIdentificacion())
                    .append(")")
                    .append("\nZona: ")
                    .append(repartidor.getZona())
                    .append("\nEstado: ")
                    .append(repartidor.isDisponible()
                            ? "Disponible"
                            : "En entrega")
                    .append("\n\n");
        }

        mostrarTexto("Paquetes asignados", texto.toString());
    }

    /**
     * Muestra el historial de paquetes entregados.
     */
    private void mostrarHistorial() {

        StringBuilder texto = new StringBuilder();

        for (Paquete paquete : gestion.obtenerHistorial()) {
            texto.append(paquete).append("\n");
        }

        if (texto.isEmpty()) {
            texto.append("No hay entregas registradas.");
        }

        mostrarTexto("Historial de entregas", texto.toString());
    }

    /**
     * Muestra la cantidad de entregas registradas.
     */
    private void mostrarCantidadEntregas() {

        mostrarTexto("Cantidad de entregas",
                "Total entregado: "
                        + gestion.obtenerCantidadEntregados());
    }

    /**
     * Muestra un menú de opciones y lee una selección numérica.
     *
     * @return opción seleccionada o 0 si se cancela
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
     * Muestra un mensaje con área de texto y desplazamiento.
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
     * Lee texto obligatorio desde una ventana.
     *
     * @return texto limpio y no vacío
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
     * Muestra una ventana de entrada de texto.
     *
     * @return texto ingresado o null si el usuario cancela
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