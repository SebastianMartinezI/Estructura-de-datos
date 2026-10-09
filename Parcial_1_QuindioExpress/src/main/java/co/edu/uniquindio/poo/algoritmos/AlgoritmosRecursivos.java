package co.edu.uniquindio.poo.algoritmos;

import co.edu.uniquindio.poo.modelo.Paquete;
import java.util.List;

/**
 * Implementa operaciones recursivas sobre los paquetes
 * registrados en el centro de distribución QuindíoExpress.
 *
 * Cada algoritmo utiliza un caso base y un caso recursivo.
 * No se utilizan ciclos para realizar las operaciones.
 *
 * @author Juan Martinez
 */
public final class AlgoritmosRecursivos {

    /**
     * Evita crear instancias de esta clase utilitaria.
     */
    private AlgoritmosRecursivos() {
    }

    /**
     * Calcula recursivamente el peso total de los paquetes
     * destinados a un municipio.
     *
     * Complejidad temporal: O(n).
     * Complejidad espacial: O(n), por la pila de llamadas.
     *
     * @param paquetes lista de paquetes registrados
     * @param municipio municipio que se desea consultar
     * @return suma de los pesos correspondientes
     */
    public static double calcularPesoPorMunicipio(
            List<Paquete> paquetes, String municipio) {

        validarLista(paquetes);

        if (municipio == null || municipio.isBlank()) {
            throw new IllegalArgumentException(
                    "El municipio no puede estar vacío.");
        }

        return sumarPesoRecursivo(paquetes, municipio.trim(), 0);
    }

    /**
     * Método auxiliar que recorre recursivamente la lista.
     *
     * Caso base: índice igual al tamaño de la lista.
     * Caso recursivo: peso actual más resultado restante.
     */
    private static double sumarPesoRecursivo(
            List<Paquete> paquetes, String municipio, int indice) {

        if (indice == paquetes.size()) {
            return 0;
        }

        Paquete actual = paquetes.get(indice);

        double pesoActual = 0;

        if (actual.getDestino().equalsIgnoreCase(municipio)) {
            pesoActual = actual.getPeso();
        }

        return pesoActual
                + sumarPesoRecursivo(paquetes, municipio, indice + 1);
    }

    /**
     * Cuenta recursivamente los paquetes cuya prioridad
     * es mayor o igual a la prioridad mínima indicada.
     *
     * Complejidad temporal: O(n).
     * Complejidad espacial: O(n), por la pila de llamadas.
     *
     * @param paquetes lista de paquetes registrados
     * @param prioridadMinima valor mínimo de prioridad
     * @return número de paquetes que cumplen la condición
     */
    public static int contarPorPrioridad(
            List<Paquete> paquetes, int prioridadMinima) {

        validarLista(paquetes);

        if (prioridadMinima < 1 || prioridadMinima > 5) {
            throw new IllegalArgumentException(
                    "La prioridad mínima debe estar entre 1 y 5.");
        }

        return contarRecursivo(paquetes, prioridadMinima, 0);
    }

    /**
     * Método auxiliar para contar paquetes recursivamente.
     *
     * Caso base: se llega al final de la lista.
     * Caso recursivo: se suma 1 si el paquete cumple
     * la condición y se procesa el resto.
     */
    private static int contarRecursivo(
            List<Paquete> paquetes, int prioridadMinima, int indice) {

        if (indice == paquetes.size()) {
            return 0;
        }

        Paquete actual = paquetes.get(indice);

        int cumple = actual.getPrioridad() >= prioridadMinima ? 1 : 0;

        return cumple
                + contarRecursivo(paquetes, prioridadMinima, indice + 1);
    }

    /**
     * Verifica que la lista recibida no sea nula.
     *
     * @param paquetes lista que se desea validar
     */
    private static void validarLista(List<Paquete> paquetes) {

        if (paquetes == null) {
            throw new IllegalArgumentException(
                    "La lista de paquetes no puede ser nula.");
        }
    }
}
