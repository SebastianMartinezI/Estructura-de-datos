package co.edu.uniquindio.poo.ejercicios;
import co.edu.uniquindio.poo.estructuras.PilaPropia;
import co.edu.uniquindio.poo.modelos.Caja;
import co.edu.uniquindio.poo.servicios.RetiroCajasService;

/**
 * Ejecuta y verifica los retiros solicitados
 * en el primer ejercicio del parcial.
 */
public class Ejercicio1 {

    public static void main(String[] args) {

        PilaPropia<Caja> pila = new PilaPropia<>();
        RetiroCajasService servicio = new RetiroCajasService();

        pila.apilar(new Caja("C1", 2));
        pila.apilar(new Caja("C2", 4));
        pila.apilar(new Caja("C3", 6));
        pila.apilar(new Caja("C4", 8));
        pila.apilar(new Caja("C5", 3));
        pila.apilar(new Caja("C6", 9));
        pila.apilar(new Caja("C7", 5));

        System.out.println("PILA INICIAL");
        System.out.println(mostrarPila(pila));

        probarRetiro(servicio, pila, "C3",
                "C7:5 -> C5:3 -> C2:4 -> C6:9 -> C4:8 -> C1:2");

        probarRetiro(servicio, pila, "C4",
                "C7:5 -> C2:4 -> C1:2 -> C5:3 -> C6:9");

        probarRetiro(servicio, pila, "C7",
                "C1:2 -> C6:9 -> C2:4 -> C5:3");

        System.out.println("\nPILA FINAL");
        System.out.println(mostrarPila(pila));

        // Comprobar que un codigo inexistente no altera la pila.
        String estadoAnterior = mostrarPila(pila);
        boolean inexistente = servicio.retirarCaja(pila, "C99");

        if (inexistente || !estadoAnterior.equals(mostrarPila(pila))) {
            throw new IllegalStateException(
                    "Error: un codigo inexistente altero la pila");
        }

        // Comprobar el caso de pila vacia.
        if (servicio.retirarCaja(new PilaPropia<>(), "C1")) {
            throw new IllegalStateException(
                    "Error: se retiro una caja de una pila vacia");
        }

        System.out.println("\nTodas las verificaciones fueron exitosas.");
    }

    private static void probarRetiro(
            RetiroCajasService servicio,
            PilaPropia<Caja> pila,
            String codigo,
            String esperado) {

        boolean resultado = servicio.retirarCaja(pila, codigo);
        String obtenido = mostrarPila(pila);

        System.out.println("\nRetirar " + codigo);
        System.out.println("Retorno: " + resultado);
        System.out.println("Pila: " + obtenido);

        if (!resultado || !esperado.equals(obtenido)) {
            throw new IllegalStateException(
                    "Resultado incorrecto al retirar " + codigo);
        }
    }

    /**
     * Muestra el contenido sin modificar la pila original.
     */
    private static String mostrarPila(PilaPropia<Caja> pila) {

        PilaPropia<Caja> auxiliar = new PilaPropia<>();
        StringBuilder resultado = new StringBuilder();

        while (!pila.estaVacia()) {
            Caja caja = pila.desapilar();

            if (resultado.length() > 0) {
                resultado.append(" -> ");
            }

            resultado.append(caja);
            auxiliar.apilar(caja);
        }

        while (!auxiliar.estaVacia()) {
            pila.apilar(auxiliar.desapilar());
        }

        return resultado.toString();
    }
}
