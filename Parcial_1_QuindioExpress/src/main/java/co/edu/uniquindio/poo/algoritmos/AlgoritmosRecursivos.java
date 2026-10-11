package co.edu.uniquindio.poo.algoritmos;

import co.edu.uniquindio.poo.modelo.Paquete;

import java.util.List;

/**
 * Algoritmos recursivos para paquetes.
 */
public final class AlgoritmosRecursivos {

    private AlgoritmosRecursivos() {
    }

    /**
     * Calcula el peso total de un municipio.
     */
    public static double calcularPesoPorMunicipio(
            List<Paquete> paquetes,
            String municipio) {

        validarLista(paquetes);

        if (municipio == null || municipio.isBlank()) {
            throw new IllegalArgumentException(
                    "El municipio no puede estar vacío.");
        }

        return sumarPesoRecursivo(paquetes, municipio.trim(), 0);
    }

    private static double sumarPesoRecursivo(
            List<Paquete> paquetes,
            String municipio,
            int indice) {

        // Caso base.
        if (indice >= paquetes.size()) {
            return 0;
        }

        Paquete actual = paquetes.get(indice);

        double pesoActual = actual.getDestino()
                .equalsIgnoreCase(municipio)
                ? actual.getPeso()
                : 0;

        return pesoActual
                + sumarPesoRecursivo(paquetes, municipio, indice + 1);
    }

    /**
     * Cuenta paquetes con prioridad mayor o igual a la indicada.
     */
    public static int contarPorPrioridad(
            List<Paquete> paquetes,
            int prioridadMinima) {

        validarLista(paquetes);

        if (prioridadMinima < 1 || prioridadMinima > 5) {
            throw new IllegalArgumentException(
                    "La prioridad mínima debe estar entre 1 y 5.");
        }

        return contarRecursivo(paquetes, prioridadMinima, 0);
    }

    private static int contarRecursivo(
            List<Paquete> paquetes,
            int prioridadMinima,
            int indice) {

        // Caso base.
        if (indice >= paquetes.size()) {
            return 0;
        }

        Paquete actual = paquetes.get(indice);

        int cumple = actual.getPrioridad() >= prioridadMinima
                ? 1
                : 0;

        return cumple
                + contarRecursivo(paquetes, prioridadMinima, indice + 1);
    }

    private static void validarLista(List<Paquete> paquetes) {

        if (paquetes == null) {
            throw new IllegalArgumentException(
                    "La lista de paquetes no puede ser nula.");
        }
    }
}