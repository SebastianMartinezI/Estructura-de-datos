package co.edu.uniquindio.poo.menu;

import co.edu.uniquindio.poo.algoritmos.AlgoritmosRecursivos;
import co.edu.uniquindio.poo.algoritmos.BusquedaBinaria;
import co.edu.uniquindio.poo.algoritmos.DivideYVenceras;
import co.edu.uniquindio.poo.comparadores.ComparadoresPaquetes;
import co.edu.uniquindio.poo.modelo.Paquete;
import co.edu.uniquindio.poo.servicios.GestionPaquetes;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Menú gráfico para gestionar y consultar paquetes
 * del centro de distribución QuindíoExpress.
 *
 * @author Juan Martinez
 */
public class MenuPaquetes {

    private static final Dimension TAMANO_RESULTADO =
            new Dimension(500, 360);

    private static final Font FUENTE_NORMAL =
            new Font("SansSerif", Font.PLAIN, 16);

    private static final Font FUENTE_NEGRITA =
            new Font("SansSerif", Font.BOLD, 16);

    private static final int PRIORIDAD_MINIMA = 1;
    private static final int PRIORIDAD_MAXIMA = 5;

    private final GestionPaquetes gestion;

    public MenuPaquetes(GestionPaquetes gestion) {

        if (gestion == null) {
            throw new IllegalArgumentException(
                    "La gestión es obligatoria.");
        }

        this.gestion = gestion;
    }

    /**
     * Ejecuta el menú hasta que el usuario seleccione la
     * opción 0, que corresponde a salir.
     */
    public void ejecutar() {

        String[] opciones = {
                "Registrar paquete",
                "Buscar paquete por código",
                "Mostrar orden de llegada",
                "Mostrar paquetes pendientes",
                "Mostrar municipios únicos",
                "Mostrar municipios ordenados",
                "Agrupar paquetes por destino",
                "Ordenar paquetes",
                "Calcular peso por municipio",
                "Contar paquetes por prioridad",
                "Realizar búsqueda binaria",
                "Encontrar paquete más pesado"
        };

        while (true) {

            int opcion = mostrarMenuYLeerOpcion(
                    "QUINDÍOEXPRESS - GESTIÓN DE PAQUETES",
                    opciones,
                    "Salir");

            if (opcion == 0) {
                break;
            }

            try {
                switch (opcion) {
                    case 1 -> registrarPaquete();
                    case 2 -> buscarPaquete();
                    case 3 -> mostrarLista(
                            "Orden de llegada",
                            gestion.obtenerOrdenLlegada());
                    case 4 -> mostrarLista(
                            "Paquetes pendientes",
                            gestion.obtenerPendientes());
                    case 5 -> mostrarTexto(
                            "Municipios únicos",
                            String.join("\n",
                                    gestion.obtenerMunicipios()));
                    case 6 -> mostrarTexto(
                            "Municipios ordenados",
                            String.join("\n",
                                    gestion.obtenerMunicipiosOrdenados()));
                    case 7 -> mostrarAgrupacion();
                    case 8 -> ordenarPaquetes();
                    case 9 -> calcularPesoMunicipio();
                    case 10 -> contarPrioridades();
                    case 11 -> buscarBinariamente();
                    case 12 -> encontrarMayorPeso();
                    default -> mostrarError("Opción no válida.");
                }

            } catch (IllegalArgumentException e) {
                mostrarError(e.getMessage());
            }
        }
    }

    /**
     * Solicita los datos y registra un paquete.
     * Valida que el peso y el tiempo sean positivos y que
     * la prioridad esté entre 1 y 5.
     */
    private void registrarPaquete() {

        String codigo = leerTexto("Código del paquete:");
        String destino = leerTexto("Destino (municipio):");
        double peso = leerDoublePositivo("Peso en kilogramos:");
        int prioridad = leerEnteroEnRango(
                "Prioridad (1-5):",
                PRIORIDAD_MINIMA,
                PRIORIDAD_MAXIMA);
        int tiempo = leerEnteroPositivo(
                "Tiempo estimado en minutos:");

        Paquete paquete = new Paquete(
                codigo,
                destino,
                peso,
                prioridad,
                tiempo);

        boolean registrado = gestion.registrarPaquete(paquete);

        mostrarTexto("Registro",
                registrado
                        ? "Paquete registrado correctamente."
                        : "Ya existe un paquete con ese código.");
    }

    /**
     * Busca un paquete por código.
     */
    private void buscarPaquete() {

        String codigo = leerTexto("Código a buscar:");
        Paquete paquete = gestion.buscarPorCodigo(codigo);

        mostrarTexto("Resultado de búsqueda",
                paquete != null
                        ? paquete.toString()
                        : "No se encontró ningún paquete.");
    }

    /**
     * Muestra los paquetes agrupados por destino.
     */
    private void mostrarAgrupacion() {

        Map<String, List<Paquete>> agrupados =
                gestion.agruparPorDestino();

        if (agrupados.isEmpty()) {
            mostrarTexto("Paquetes por destino",
                    "No hay paquetes registrados.");
            return;
        }

        StringBuilder texto = new StringBuilder();

        for (Map.Entry<String, List<Paquete>> entrada
                : agrupados.entrySet()) {

            texto.append("Municipio: ")
                    .append(entrada.getKey())
                    .append("\n");

            for (Paquete paquete : entrada.getValue()) {
                texto.append("   ")
                        .append(paquete)
                        .append("\n");
            }

            texto.append("\n");
        }

        mostrarTexto("Paquetes por destino", texto.toString());
    }

    /**
     * Ordena temporalmente una copia de los paquetes
     * según el criterio seleccionado.
     */
    private void ordenarPaquetes() {

        String[] criterios = {
                "Código ascendente",
                "Prioridad descendente",
                "Peso descendente",
                "Tiempo ascendente",
                "Orden de despacho"
        };

        int criterio = mostrarMenuYLeerOpcion(
                "ORDENAR PAQUETES",
                criterios,
                "Volver");

        if (criterio == 0) {
            return;
        }

        List<Paquete> paquetes =
                new ArrayList<>(gestion.obtenerOrdenLlegada());

        switch (criterio) {
            case 1 -> paquetes.sort(
                    ComparadoresPaquetes.POR_CODIGO);
            case 2 -> paquetes.sort(
                    ComparadoresPaquetes.POR_PRIORIDAD);
            case 3 -> paquetes.sort(
                    ComparadoresPaquetes.POR_PESO);
            case 4 -> paquetes.sort(
                    ComparadoresPaquetes.POR_TIEMPO);
            case 5 -> paquetes.sort(
                    ComparadoresPaquetes.POR_DESPACHO);
            default -> {
                mostrarError("Criterio no válido.");
                return;
            }
        }

        mostrarLista("Paquetes ordenados", paquetes);
    }

    /**
     * Calcula recursivamente el peso total de los paquetes
     * destinados a un municipio.
     */
    private void calcularPesoMunicipio() {

        String municipio = leerTexto("Municipio a consultar:");
        List<Paquete> paquetes = gestion.obtenerOrdenLlegada();

        double total = AlgoritmosRecursivos.calcularPesoPorMunicipio(
                paquetes,
                municipio);

        StringBuilder texto = new StringBuilder();
        texto.append("Paquetes destinados a ")
                .append(municipio)
                .append(":\n\n");

        int cantidad = 0;

        for (Paquete paquete : paquetes) {

            if (paquete.getDestino().equalsIgnoreCase(municipio)) {
                texto.append(paquete.getCodigo())
                        .append(" | Peso: ")
                        .append(paquete.getPeso())
                        .append(" kg\n");
                cantidad++;
            }
        }

        if (cantidad == 0) {
            texto.append(
                    "No hay paquetes registrados para este municipio.\n");
        }

        texto.append("\n--------------------------------\n")
                .append("Peso total en ")
                .append(municipio)
                .append(": ")
                .append(String.format("%.2f", total))
                .append(" kg");

        mostrarTexto("Peso por municipio", texto.toString());
    }

    /**
     * Cuenta recursivamente los paquetes cuya prioridad
     * sea mayor o igual a la prioridad mínima indicada.
     */
    private void contarPrioridades() {

        int minima = leerEnteroEnRango(
                "Prioridad mínima (1-5):",
                PRIORIDAD_MINIMA,
                PRIORIDAD_MAXIMA);

        int cantidad = AlgoritmosRecursivos.contarPorPrioridad(
                gestion.obtenerOrdenLlegada(),
                minima);

        mostrarTexto("Conteo por prioridad",
                "Paquetes con prioridad >= " + minima
                        + ": " + cantidad);
    }

    /**
     * Busca un paquete con búsqueda binaria sobre una copia
     * ordenada previamente por código.
     */
    private void buscarBinariamente() {

        String codigo = leerTexto("Código a buscar:");

        List<Paquete> paquetes =
                new ArrayList<>(gestion.obtenerOrdenLlegada());

        paquetes.sort(ComparadoresPaquetes.POR_CODIGO);

        Paquete resultado =
                BusquedaBinaria.buscarPorCodigo(paquetes, codigo);

        mostrarTexto("Búsqueda binaria",
                resultado != null
                        ? "Encontrado: " + resultado
                        : "No se encontró el paquete " + codigo);
    }

    /**
     * Encuentra el paquete de mayor peso usando divide y vencerás.
     */
    private void encontrarMayorPeso() {

        List<Paquete> paquetes = gestion.obtenerOrdenLlegada();

        if (paquetes.isEmpty()) {
            mostrarTexto("Paquete más pesado",
                    "No hay paquetes registrados.");
            return;
        }

        Paquete mayor = DivideYVenceras.encontrarMayorPeso(paquetes);

        mostrarTexto("Paquete más pesado", mayor.toString());
    }

    /**
     * Muestra una lista de paquetes.
     */
    private void mostrarLista(String titulo,
                              List<Paquete> paquetes) {

        if (paquetes == null || paquetes.isEmpty()) {
            mostrarTexto(titulo, "No hay paquetes para mostrar.");
            return;
        }

        StringBuilder texto = new StringBuilder();

        for (Paquete paquete : paquetes) {
            texto.append(paquete).append("\n");
        }

        mostrarTexto(titulo, texto.toString());
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
     * Lee un número entero válido.
     */
    private int leerEntero(String mensaje) {

        while (true) {

            String entrada = mostrarEntrada(mensaje);

            if (entrada == null) {
                throw new IllegalArgumentException(
                        "Operación cancelada.");
            }

            try {
                return Integer.parseInt(entrada.trim());

            } catch (NumberFormatException e) {
                mostrarError("Debe ingresar un número entero válido.");
            }
        }
    }

    /**
     * Lee un número entero mayor que cero.
     */
    private int leerEnteroPositivo(String mensaje) {

        while (true) {

            int valor = leerEntero(mensaje);

            if (valor > 0) {
                return valor;
            }

            mostrarError("El valor debe ser mayor que cero.");
        }
    }

    /**
     * Lee un número entero dentro de un rango.
     */
    private int leerEnteroEnRango(String mensaje,
                                  int minimo,
                                  int maximo) {

        while (true) {

            int valor = leerEntero(mensaje);

            if (valor >= minimo && valor <= maximo) {
                return valor;
            }

            mostrarError("Ingrese un valor entre "
                    + minimo + " y " + maximo + ".");
        }
    }

    /**
     * Lee un número decimal válido.
     * Acepta coma o punto como separador decimal.
     */
    private double leerDouble(String mensaje) {

        while (true) {

            String entrada = mostrarEntrada(mensaje);

            if (entrada == null) {
                throw new IllegalArgumentException(
                        "Operación cancelada.");
            }

            try {
                double valor = Double.parseDouble(
                        entrada.trim().replace(',', '.'));

                if (!Double.isFinite(valor)) {
                    throw new NumberFormatException();
                }

                return valor;

            } catch (NumberFormatException e) {
                mostrarError("Debe ingresar un número decimal válido.");
            }
        }
    }

    /**
     * Lee un número decimal mayor que cero.
     */
    private double leerDoublePositivo(String mensaje) {

        while (true) {

            double valor = leerDouble(mensaje);

            if (valor > 0) {
                return valor;
            }

            mostrarError("El valor debe ser mayor que cero.");
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