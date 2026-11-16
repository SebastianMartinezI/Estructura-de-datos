package co.edu.uniquindio.sga.interfaz.controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controla únicamente la navegación visual de la pantalla principal.
 * Los módulos reales tendrán sus propios controladores y servicios.
 */
public final class PrincipalController {

    @FXML
    private Label tituloModulo;

    @FXML
    private Label descripcionModulo;

    @FXML
    private Label estadoModulo;

    /** Inicializa la sección presentada cuando se abre la aplicación. */
    @FXML
    private void initialize() {
        mostrar(Modulo.INICIO);
    }

    /** Atiende las opciones del menú sin ejecutar reglas de negocio. */
    @FXML
    private void seleccionarModulo(ActionEvent evento) {
        Button boton = (Button) evento.getSource();
        String codigo = (String) boton.getUserData();
        mostrar(Modulo.valueOf(codigo));
    }

    private void mostrar(Modulo modulo) {
        tituloModulo.setText(modulo.titulo);
        descripcionModulo.setText(modulo.descripcion);
        estadoModulo.setText(modulo == Modulo.INICIO
                ? "Base gráfica operativa"
                : "Módulo pendiente de implementación");
    }

    /** Opciones visuales disponibles en este prototipo de navegación. */
    private enum Modulo {
        INICIO("Inicio", "Base de navegación JavaFX del SGA. Las funciones se conectarán a servicios cuando estén implementadas."),
        RESERVAS("Reservas", "Juan: registro, confirmación, cancelación y canal externo de reservas."),
        DISPONIBILIDAD("Disponibilidad", "Juan: búsqueda de apartamentos libres para fechas y número de ocupantes."),
        CALENDARIO("Calendario de ocupación", "Juan: visualización mensual de noches libres, ocupadas y bloqueadas."),
        HUESPEDES("Huéspedes", "Juan: perfiles, historial de estancias y apartamentos favoritos."),
        TABLERO("Tablero principal", "Cristian: estados operativos y resumen del alojamiento."),
        PREPARACION("Preparación", "Cristian: cola de prioridad de limpieza y seguimiento de apartamentos."),
        MAPA("Mapa del edificio", "Cristian: grafo ponderado y rutas de preparación."),
        FOLIOS("Folios", "Cristian: cargos, pagos, saldos y reversión de movimientos."),
        ESTADISTICAS("Estadísticas", "Cristian: indicadores de ocupación, ingresos y operación.");

        private final String titulo;
        private final String descripcion;

        Modulo(String titulo, String descripcion) {
            this.titulo = titulo;
            this.descripcion = descripcion;
        }
    }
}
