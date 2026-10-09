package co.edu.uniquindio.poo.servicios;

import co.edu.uniquindio.poo.modelo.Paquete;

/**
 * Prueba manual del registro y consulta de paquetes.
 *
 * @author Juan Martinez
 */
public class PruebaGestionPaquetes {

    public static void main(String[] args) {

        GestionPaquetes gestion = new GestionPaquetes();

        Paquete p1 = new Paquete(
                "PQ731", "Armenia", 4.5, 5, 30);

        Paquete p2 = new Paquete(
                "PQ105", "Salento", 3.0, 3, 25);

        Paquete p3 = new Paquete(
                "PQ942", "Armenia", 7.2, 4, 45);

        System.out.println("=== REGISTRO ===");

        System.out.println(gestion.registrarPaquete(p1));
        System.out.println(gestion.registrarPaquete(p2));
        System.out.println(gestion.registrarPaquete(p3));

        System.out.println("Duplicado: "
                + gestion.registrarPaquete(p1));

        System.out.println("\n=== BÚSQUEDA POR CÓDIGO ===");
        System.out.println(gestion.buscarPorCodigo("PQ105"));

        System.out.println("\n=== ORDEN DE LLEGADA ===");
        gestion.obtenerOrdenLlegada()
                .forEach(System.out::println);

        System.out.println("\n=== MUNICIPIOS ÚNICOS ===");
        System.out.println(gestion.obtenerMunicipios());

        System.out.println("\n=== MUNICIPIOS ORDENADOS ===");
        System.out.println(gestion.obtenerMunicipiosOrdenados());

        System.out.println("\n=== AGRUPACIÓN POR DESTINO ===");
        gestion.agruparPorDestino().forEach(
                (destino, paquetes) ->
                        System.out.println(destino + ": " + paquetes)
        );

        System.out.println("\n=== PENDIENTES ===");
        System.out.println(gestion.obtenerCantidadPendientes());

        System.out.println("\n=== REGISTRAR ENTREGA ===");
        System.out.println(gestion.marcarEntregado("PQ105"));

        System.out.println("Pendientes restantes: "
                + gestion.obtenerCantidadPendientes());

        System.out.println("¿PQ105 está pendiente? "
                + gestion.estaPendiente("PQ105"));

        System.out.println("Total registrados: "
                + gestion.obtenerCantidadRegistrados());
    }
}
