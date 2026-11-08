package co.edu.uniquindio.poo.ejercicios;
import co.edu.uniquindio.poo.estructuras.PilaPropia;
import co.edu.uniquindio.poo.modelos.Caja;
import co.edu.uniquindio.poo.servicios.RetiroCajasService;
import co.edu.uniquindio.poo.servicios.EvaluadorExpresionesService;
import co.edu.uniquindio.poo.servicios.EvaluadorExpresionesService.ResultadoEvaluacion;

/**
 * Ejecuta y verifica el ejercicio 1
 *
 * Incluye el retiro de cajas, su reorganizacion
 * y la evaluacion de la expresion matematica
 */
public class Ejercicio1 {

    public static void main(String[] args) {

        PilaPropia<Caja> pila = new PilaPropia<>();
        RetiroCajasService servicio = new RetiroCajasService();

        // Inicializar las cajas en el orden solicitado.
        pila.apilar(new Caja("C1", 2));
        pila.apilar(new Caja("C2", 4));
        pila.apilar(new Caja("C3", 6));
        pila.apilar(new Caja("C4", 8));
        pila.apilar(new Caja("C5", 3));
        pila.apilar(new Caja("C6", 9));
        pila.apilar(new Caja("C7", 5));

        System.out.println("PILA INICIAL");
        System.out.println(mostrarPila(pila));

        // Ejecutar los tres retiros consecutivos.
        probarRetiro(servicio, pila, "C3",
                "C7:5 -> C5:3 -> C2:4 -> C6:9 -> C4:8 -> C1:2");

        probarRetiro(servicio, pila, "C4",
                "C7:5 -> C2:4 -> C1:2 -> C5:3 -> C6:9");

        probarRetiro(servicio, pila, "C7",
                "C1:2 -> C6:9 -> C2:4 -> C5:3");

        System.out.println("\nPILA FINAL");
        System.out.println(mostrarPila(pila));

        // Verificar que un codigo inexistente no altere la pila.
        String estadoAnterior = mostrarPila(pila);
        boolean inexistente = servicio.retirarCaja(pila, "C99");

        if (inexistente ||
                !estadoAnterior.equals(mostrarPila(pila))) {
            throw new IllegalStateException(
                    "Un codigo inexistente altero la pila");
        }

        // Verificar el comportamiento con una pila vacia.
        if (servicio.retirarCaja(new PilaPropia<>(), "C1")) {
            throw new IllegalStateException(
                    "No se puede retirar de una pila vacia");
        }

        // Evaluar la expresion oficial con las cajas restantes.
        evaluarExpresion(pila);

        System.out.println(
                "\nTodas las verificaciones fueron exitosas.");
    }

    /**
     * Ejecuta un retiro y verifica el resultado obtenido.
     *
     * @param servicio servicio encargado del retiro.
     * @param pila pila que contiene las cajas.
     * @param codigo codigo de la caja que se desea retirar.
     * @param esperado contenido esperado despues del retiro.
     */
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
     * Obtiene los valores de las cuatro cajas finales
     * y evalua la expresion matematica oficial:
     *
     * (A + B) * C - (A - B) * D
     *
     * La evaluacion se realiza en notacion postfija,
     * utilizando una pila propia.
     *
     * La pila original conserva su contenido y orden.
     *
     * @param pila pila con las cuatro cajas restantes.
     */
    private static void evaluarExpresion(PilaPropia<Caja> pila) {

        if (pila.tamano() != 4) {
            throw new IllegalStateException(
                    "La pila debe contener cuatro cajas");
        }

        PilaPropia<Caja> auxiliar = new PilaPropia<>();

        // Obtener las cajas desde la cima hacia el fondo.
        Caja primera = pila.desapilar();
        auxiliar.apilar(primera);

        Caja segunda = pila.desapilar();
        auxiliar.apilar(segunda);

        Caja tercera = pila.desapilar();
        auxiliar.apilar(tercera);

        Caja cuarta = pila.desapilar();
        auxiliar.apilar(cuarta);

        // Restaurar el orden original de la pila.
        while (!auxiliar.estaVacia()) {
            pila.apilar(auxiliar.desapilar());
        }

        // Calcular las variables segun el enunciado.
        double a = primera.getValor();

        if (a == 0) {
            throw new IllegalStateException(
                    "A no puede ser cero por la division de B");
        }

        double b = segunda.getValor() / a;
        double c = tercera.getValor() + a;
        double d = (cuarta.getValor() + c) * a;

        System.out.println("\nVARIABLES DE LA EXPRESION");
        System.out.println("A = " + a);
        System.out.println("B = " + b);
        System.out.println("C = " + c);
        System.out.println("D = " + d);

        // Expresion infija oficial del docente:
        // (A + B) * C - (A - B) * D
        //
        // Conversion a notacion postfija:
        // A B + C * A B - D * -

        System.out.println("\nEXPRESION INFIJA");
        System.out.println("(A + B) * C - (A - B) * D");

        // Sustituir las variables por sus valores.
        String expresionPostfija =
                a + " " + b + " + "
                        + c + " * "
                        + a + " " + b + " - "
                        + d + " * -";

        // Evaluar la expresion mediante una pila propia.
        EvaluadorExpresionesService evaluador =
                new EvaluadorExpresionesService();

        ResultadoEvaluacion evaluacion =
                evaluador.evaluarPostfija(expresionPostfija);

        System.out.println("\nEXPRESION POSTFIJA");
        System.out.println("A B + C * A B - D * -");

        System.out.println("\nPOSTFIJA CON VALORES");
        System.out.println(expresionPostfija);

        // Mostrar el seguimiento de las operaciones.
        System.out.println("\nTABLA DE EVALUACION");
        System.out.println(evaluacion.tabla());

        System.out.println(
                "RESULTADO FINAL: " + evaluacion.valor());

        // Verificar el resultado esperado de la expresion oficial.
        double resultadoEsperado = 84.0;
        double tolerancia = 0.000001;

        if (Math.abs(evaluacion.valor() - resultadoEsperado)
                > tolerancia) {
            throw new IllegalStateException(
                    "El resultado de la expresion es incorrecto");
        }
    }

    /**
     * Muestra la pila desde la cima hasta el fondo
     * sin modificar su contenido original.
     *
     * @param pila pila que se desea mostrar.
     * @return representacion de los elementos en orden.
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

        // Restaurar la pila original.
        while (!auxiliar.estaVacia()) {
            pila.apilar(auxiliar.desapilar());
        }

        return resultado.toString();
    }
}
