package co.edu.uniquindio.poo.servicios;
import co.edu.uniquindio.poo.estructuras.PilaPropia;
import java.math.BigDecimal;

/**
 * Evalua expresiones matematicas en notacion postfija.
 * Utiliza exclusivamente una pila propia para
 * almacenar y procesar los operandos.
 */
public class EvaluadorExpresionesService {

    /**
     * Contiene el resultado y el seguimiento de la evaluacion.
     *
     * @param valor resultado numerico.
     * @param tabla seguimiento de cada operacion.
     */
    public record ResultadoEvaluacion(
            double valor,
            String tabla) {
    }

    /**
     * Evalua una expresion postfija.
     * Los elementos deben estar separados por espacios.
     *
     * @param expresion expresion en notacion postfija.
     * @return resultado y tabla de operaciones.
     * @throws IllegalArgumentException si la expresion
     *         es invalida o contiene una division por cero.
     */
    public ResultadoEvaluacion evaluarPostfija(
            String expresion) {

        if (expresion == null || expresion.isBlank()) {
            throw new IllegalArgumentException(
                    "La expresion no puede estar vacia");
        }

        PilaPropia<Double> pila = new PilaPropia<>();
        StringBuilder tabla = new StringBuilder();

        tabla.append(String.format(
                "%-12s | %-25s | %s%n",
                "Elemento", "Operacion", "Pila"));

        tabla.append("-".repeat(65))
                .append(System.lineSeparator());

        String[] elementos = expresion.trim().split("\\s+");

        for (String elemento : elementos) {

            String operacion;

            if (esOperador(elemento)) {

                if (pila.tamano() < 2) {
                    throw new IllegalArgumentException(
                            "Faltan operandos para: " + elemento);
                }

                double derecho = pila.desapilar();
                double izquierdo = pila.desapilar();

                double resultado = aplicarOperacion(
                        izquierdo, derecho, elemento);

                pila.apilar(resultado);

                operacion = formatear(izquierdo) + " "
                        + elemento + " "
                        + formatear(derecho) + " = "
                        + formatear(resultado);

            } else {

                double numero;

                try {
                    numero = Double.parseDouble(elemento);
                } catch (NumberFormatException ex) {
                    throw new IllegalArgumentException(
                            "Elemento invalido: " + elemento, ex);
                }

                if (!Double.isFinite(numero)) {
                    throw new IllegalArgumentException(
                            "Numero no valido: " + elemento);
                }

                pila.apilar(numero);
                operacion = "Apilar " + formatear(numero);
            }

            tabla.append(String.format(
                    "%-12s | %-25s | %s%n",
                    elemento,
                    operacion,
                    mostrarPila(pila)));
        }

        if (pila.tamano() != 1) {
            throw new IllegalArgumentException(
                    "La expresion postfija es invalida");
        }

        return new ResultadoEvaluacion(
                pila.verTope(), tabla.toString());
    }

    /**
     * Determina si un elemento es un operador.
     */
    private boolean esOperador(String elemento) {
        return elemento.equals("+")
                || elemento.equals("-")
                || elemento.equals("*")
                || elemento.equals("/");
    }

    /**
     * Ejecuta una operacion aritmetica.
     */
    private double aplicarOperacion(
            double izquierdo,
            double derecho,
            String operador) {

        if (operador.equals("/") && derecho == 0) {
            throw new IllegalArgumentException(
                    "No se permite dividir entre cero");
        }

        double resultado = switch (operador) {
            case "+" -> izquierdo + derecho;
            case "-" -> izquierdo - derecho;
            case "*" -> izquierdo * derecho;
            case "/" -> izquierdo / derecho;
            default -> throw new IllegalArgumentException(
                    "Operador no soportado: " + operador);
        };

        if (!Double.isFinite(resultado)) {
            throw new IllegalArgumentException(
                    "El resultado numerico no es valido");
        }

        return resultado;
    }

    /**
     * Consulta el contenido de la pila sin alterar
     * su estado final.
     */
    private String mostrarPila(PilaPropia<Double> pila) {

        PilaPropia<Double> auxiliar = new PilaPropia<>();
        StringBuilder contenido = new StringBuilder();

        while (!pila.estaVacia()) {
            double valor = pila.desapilar();

            if (contenido.length() > 0) {
                contenido.insert(0, ", ");
            }

            contenido.insert(0, formatear(valor));
            auxiliar.apilar(valor);
        }

        while (!auxiliar.estaVacia()) {
            pila.apilar(auxiliar.desapilar());
        }

        return "[" + contenido + "]";
    }

    /**
     * Elimina decimales innecesarios al mostrar numeros.
     */
    private String formatear(double valor) {
        return BigDecimal.valueOf(valor)
                .stripTrailingZeros()
                .toPlainString();
    }
}
