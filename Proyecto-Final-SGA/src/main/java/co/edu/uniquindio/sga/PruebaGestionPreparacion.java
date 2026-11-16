package co.edu.uniquindio.sga;

import co.edu.uniquindio.sga.servicios.operacion.GestionPreparacionService;
import co.edu.uniquindio.sga.servicios.operacion.PreparacionService;
import co.edu.uniquindio.sga.servicios.operacion.RutaPreparacionService;
import co.edu.uniquindio.sga.servicios.operacion.TurnoService;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

public class PruebaGestionPreparacion {

    public static void main(String[] args) {
        Clock reloj = Clock.systemDefaultZone();

        PreparacionService preparacion =
                new PreparacionService(reloj);

        TurnoService turnos = new TurnoService();
        RutaPreparacionService rutas = new RutaPreparacionService();

        GestionPreparacionService gestion =
                new GestionPreparacionService(
                        preparacion,
                        turnos,
                        rutas
                );

        turnos.registrarPersonal("Ana");
        turnos.registrarPersonal("Luis");

        gestion.registrarPendiente(
                "A-203",
                LocalDateTime.now(reloj).plusHours(5)
        );

        gestion.registrarPendiente(
                "A-101",
                LocalDateTime.now(reloj).plusHours(2)
        );

        gestion.registrarPendiente(
                "A-102",
                null
        );

        System.out.println("=== TAREAS PENDIENTES ===");
        System.out.println("Cantidad: "
                + gestion.cantidadPendientes());

        System.out.println("Orden de recorrido:");
        List<String> orden = gestion.calcularOrdenRecorrido();

        for (String codigo : orden) {
            System.out.println("- " + codigo);
        }

        System.out.println("=== ASIGNACIONES ===");

        String responsableUno = gestion.asignarSiguienteTarea();
        System.out.println("Primera tarea asignada a: "
                + responsableUno);

        String responsableDos = gestion.asignarSiguienteTarea();
        System.out.println("Segunda tarea asignada a: "
                + responsableDos);

        System.out.println("=== PREPARACIONES ===");

        gestion.iniciarPreparacion("A-101");
        System.out.println("Preparación iniciada para A-101");

        gestion.finalizarPreparacion("A-101");
        System.out.println("Preparación finalizada para A-101");

        System.out.println("=== ESTADO FINAL ===");
        System.out.println("Tareas pendientes: "
                + gestion.cantidadPendientes());

        System.out.println("=== FIN DE LA PRUEBA ===");
    }
}