package co.edu.uniquindio.sga.interfaz;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

/** Marco base: los módulos se incorporarán mediante Pull Requests. */
public final class MainFrame extends JFrame {
    public MainFrame() {
        super("SGA | Aparta-Suites UQ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(920, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel encabezado = new JLabel("SGA · Aparta-Suites UQ", SwingConstants.CENTER);
        encabezado.setFont(encabezado.getFont().deriveFont(Font.BOLD, 24f));
        encabezado.setBorder(BorderFactory.createEmptyBorder(22, 12, 18, 12));
        add(encabezado, BorderLayout.NORTH);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Inicio", pantallaInicio());
        pestanas.addTab("Prueba de estancia", new PanelEstancia());
        pestanas.addTab("Reservas", moduloEnPreparacion("Juan: reservas y calendario"));
        pestanas.addTab("Preparación", moduloEnPreparacion("Cristian: preparación y rutas"));
        pestanas.addTab("Huéspedes", moduloEnPreparacion("Juan: huéspedes y favoritos; Cristian: folios"));
        pestanas.addTab("Mapa", moduloEnPreparacion("Cristian: grafo y mapa interactivo"));
        pestanas.addTab("Reportes", moduloEnPreparacion("Cristian: estadísticas"));
        add(pestanas, BorderLayout.CENTER);
    }

    private JPanel pantallaInicio() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(etiquetaCentro("Base ejecutable del proyecto. La pestaña 'Prueba de estancia' ya permite validar noches."),
                BorderLayout.CENTER);
        return panel;
    }

    private JPanel moduloEnPreparacion(String texto) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(etiquetaCentro("MÓDULO PENDIENTE · " + texto), BorderLayout.CENTER);
        return panel;
    }

    private JLabel etiquetaCentro(String texto) {
        JLabel etiqueta = new JLabel(texto, SwingConstants.CENTER);
        etiqueta.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        return etiqueta;
    }
}
