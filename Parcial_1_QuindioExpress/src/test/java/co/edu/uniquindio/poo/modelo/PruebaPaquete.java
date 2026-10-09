package co.edu.uniquindio.poo.modelo;

/**
 * Prueba manual del modelo Paquete.
 * Comprueba la creación, el orden natural y las validaciones.
 *
 * @author Juan Martinez
 */
public class PruebaPaquete {

    public static void main(String[] args) {

        Paquete paquete1 = new Paquete(
                "PQ731", "Armenia", 4.5, 5, 30);

        Paquete paquete2 = new Paquete(
                "PQ105", "Salento", 2.3, 3, 20);

        System.out.println("=== PAQUETES REGISTRADOS ===");
        System.out.println(paquete1);
        System.out.println(paquete2);

        System.out.println("\n=== ORDEN NATURAL ===");
        System.out.println("Comparación: "
                + paquete1.compareTo(paquete2));

        System.out.println("¿PQ105 va antes que PQ731? "
                + (paquete2.compareTo(paquete1) < 0));

        System.out.println("\n=== VALIDACIÓN DE PESO ===");

        try {
            new Paquete("PQ999", "Calarcá", -2.0, 3, 15);
            System.out.println("ERROR: se aceptó un peso inválido");
        } catch (IllegalArgumentException e) {
            System.out.println("Validación correcta: "
                    + e.getMessage());
        }

        System.out.println("\n=== VALIDACIÓN DE PRIORIDAD ===");

        try {
            new Paquete("PQ888", "Armenia", 3.0, 8, 25);
            System.out.println("ERROR: se aceptó una prioridad inválida");
        } catch (IllegalArgumentException e) {
            System.out.println("Validación correcta: "
                    + e.getMessage());
        }
    }
}
