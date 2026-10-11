package co.edu.uniquindio.poo;

import co.edu.uniquindio.poo.menu.MenuPrincipal;
import co.edu.uniquindio.poo.modelo.Paquete;
import co.edu.uniquindio.poo.modelo.Repartidor;
import co.edu.uniquindio.poo.servicios.GestionDespachos;
import co.edu.uniquindio.poo.servicios.GestionPaquetes;
import co.edu.uniquindio.poo.servicios.GestionRepartidores;

import javax.swing.JOptionPane;

/**
 * Punto de inicio de la aplicación QuindíoExpress.
 * Carga los datos iniciales y abre el menú principal.
 */
public class Main {

    public static void main(String[] args) {

        GestionPaquetes gestionPaquetes = new GestionPaquetes();
        GestionRepartidores gestionRepartidores =
                new GestionRepartidores();

        GestionDespachos gestionDespachos = new GestionDespachos(
                gestionPaquetes,
                gestionRepartidores);

        cargarPaquetes(gestionPaquetes);
        cargarRepartidores(gestionRepartidores);
        cargarAsignacionesIniciales(gestionDespachos);

        mostrarBienvenida(gestionPaquetes, gestionRepartidores);

        MenuPrincipal menuPrincipal = new MenuPrincipal(
                gestionPaquetes,
                gestionRepartidores,
                gestionDespachos);

        menuPrincipal.ejecutar();
    }

    /**
     * Carga los 30 paquetes iniciales de la jornada.
     */
    private static void cargarPaquetes(GestionPaquetes gestion) {

        String[][] datos = {
                {"PQ731", "Armenia",  "4.5", "5", "45"},
                {"PQ105", "Salento",  "3.0", "5", "25"},
                {"PQ942", "Armenia",  "7.2", "4", "20"},
                {"PQ318", "Calarca",  "2.3", "3", "60"},
                {"PQ567", "Salento",  "5.1", "2", "35"},
                {"PQ829", "Armenia",  "1.8", "5", "15"},
                {"PQ214", "Circasia", "6.4", "1", "50"},
                {"PQ421", "Filandia", "8.9", "4", "30"},
                {"PQ613", "Quimbaya", "2.7", "3", "40"},
                {"PQ780", "Armenia",  "9.3", "5", "55"},
                {"PQ129", "Salento",  "4.2", "2", "25"},
                {"PQ356", "Calarca",  "7.6", "4", "45"},
                {"PQ584", "Circasia", "3.1", "3", "20"},
                {"PQ692", "Filandia", "5.8", "5", "35"},
                {"PQ847", "Quimbaya", "6.9", "2", "60"},
                {"PQ903", "Armenia",  "1.5", "4", "10"},
                {"PQ238", "Salento",  "8.4", "3", "50"},
                {"PQ471", "Calarca",  "2.9", "5", "30"},
                {"PQ519", "Circasia", "7.1", "1", "45"},
                {"PQ664", "Filandia", "4.7", "4", "25"},
                {"PQ735", "Quimbaya", "9.8", "3", "55"},
                {"PQ812", "Armenia",  "3.6", "5", "20"},
                {"PQ957", "Salento",  "6.2", "2", "40"},
                {"PQ146", "Calarca",  "5.4", "4", "35"},
                {"PQ289", "Circasia", "8.1", "3", "30"},
                {"PQ394", "Filandia", "2.4", "5", "15"},
                {"PQ528", "Quimbaya", "7.9", "1", "60"},
                {"PQ641", "Armenia",  "4.9", "4", "25"},
                {"PQ773", "Salento",  "3.3", "3", "45"},
                {"PQ856", "Calarca",  "6.7", "5", "30"}
        };

        for (String[] fila : datos) {
            Paquete paquete = new Paquete(
                    fila[0],
                    fila[1],
                    Double.parseDouble(fila[2]),
                    Integer.parseInt(fila[3]),
                    Integer.parseInt(fila[4])
            );

            gestion.registrarPaquete(paquete);
        }
    }

    /**
     * Carga los seis repartidores iniciales.
     */
    private static void cargarRepartidores(GestionRepartidores gestion) {

        gestion.registrarRepartidor(
                new Repartidor("2530", "Carlos Gomez", "Armenia", true));

        gestion.registrarRepartidor(
                new Repartidor("3045", "Ana Martinez", "Salento", true));

        gestion.registrarRepartidor(
                new Repartidor("8025", "Luis Rodriguez", "Calarca", true));

        gestion.registrarRepartidor(
                new Repartidor("9026", "Maria Lopez", "Circasia", true));

        gestion.registrarRepartidor(
                new Repartidor("3034", "Jorge Ramirez", "Filandia", true));

        gestion.registrarRepartidor(
                new Repartidor("7247", "Sofia Torres", "Quimbaya", true));
    }

    /**
     * Deja cuatro paquetes asignados desde el inicio.
     *
     * Los paquetes siguen pendientes porque no han sido entregados.
     * Los repartidores asignados quedan no disponibles.
     */
    private static void cargarAsignacionesIniciales(
            GestionDespachos gestionDespachos) {

        // Armenia: PQ731 -> Carlos Gomez.
        gestionDespachos.asignarPaquete("PQ731", "2530");

        // Salento: PQ105 -> Ana Martinez.
        gestionDespachos.asignarPaquete("PQ105", "3045");

        // Calarca: PQ318 -> Luis Rodriguez.
        gestionDespachos.asignarPaquete("PQ318", "8025");

        // Circasia: PQ214 -> Maria Lopez.
        gestionDespachos.asignarPaquete("PQ214", "9026");
    }

    /**
     * Muestra el mensaje de bienvenida con datos iniciales.
     */
    private static void mostrarBienvenida(
            GestionPaquetes gestionPaquetes,
            GestionRepartidores gestionRepartidores) {

        JOptionPane.showMessageDialog(
                null,
                "<html><div style='width:280px;'>"
                        + "<b>Bienvenido a QuindíoExpress.</b><br><br>"
                        + "Se cargaron "
                        + gestionPaquetes.obtenerCantidadRegistrados()
                        + " paquetes y "
                        + gestionRepartidores.obtenerCantidadRepartidores()
                        + " repartidores.<br><br>"
                        + "Hay 4 paquetes asignados inicialmente."
                        + "</div></html>",
                "QuindíoExpress",
                JOptionPane.INFORMATION_MESSAGE);
    }
}