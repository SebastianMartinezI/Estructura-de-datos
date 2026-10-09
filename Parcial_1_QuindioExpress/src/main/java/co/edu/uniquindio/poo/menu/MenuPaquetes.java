package co.edu.uniquindio.poo.menu;

import co.edu.uniquindio.poo.algoritmos.AlgoritmosRecursivos;
import co.edu.uniquindio.poo.algoritmos.BusquedaBinaria;
import co.edu.uniquindio.poo.algoritmos.DivideYVenceras;
import co.edu.uniquindio.poo.comparadores.ComparadoresPaquetes;
import co.edu.uniquindio.poo.modelo.Paquete;
import co.edu.uniquindio.poo.servicios.GestionPaquetes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Menú interactivo para gestionar y consultar paquetes
 * del centro de distribución QuindíoExpress.
 *
 * Esta clase se encarga únicamente de la interacción
 * con el usuario. La lógica de negocio se delega a
 * los servicios y algoritmos correspondientes.
 *
 * @author Juan Martinez
 */
public class MenuPaquetes {

    private final GestionPaquetes gestion;
    private final Scanner scanner;

    /**
     * Crea el menú utilizando un servicio compartido.
     *
     * @param gestion servicio que administra los paquetes
     * @param scanner lector de datos de consola
     */
    public MenuPaquetes(GestionPaquetes gestion, Scanner scanner) {

        if (gestion == null || scanner == null) {
            throw new IllegalArgumentException(
                    "La gestión y el Scanner son obligatorios.");
        }

        this.gestion = gestion;
        this.scanner = scanner;
    }

    /**
     * Ejecuta el menú hasta que el usuario seleccione
     * la opción cero.
     */
    public void ejecutar() {

        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ");

            try {
                switch (opcion) {
                    case 1 -> registrarPaquete();
                    case 2 -> buscarPaquete();
                    case 3 -> mostrarOrdenLlegada();
                    case 4 -> mostrarPendientes();
                    case 5 -> mostrarMunicipios();
                    case 6 -> mostrarMunicipiosOrdenados();
                    case 7 -> mostrarAgrupacion();
                    case 8 -> ordenarPaquetes();
                    case 9 -> calcularPesoMunicipio();
                    case 10 -> contarPrioridades();
                    case 11 -> buscarBinariamente();
                    case 12 -> encontrarMayorPeso();
                    case 0 -> System.out.println(
                            "Regresando al menú principal...");
                    default -> System.out.println(
                            "Opción no válida.");
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (opcion != 0);
    }

    /**
     * Presenta las funcionalidades disponibles.
     */
    private void mostrarMenu() {

        System.out.println("\n========== QUINDÍOEXPRESS ==========");
        System.out.println("       GESTIÓN DE PAQUETES");
        System.out.println("1. Registrar paquete");
        System.out.println("2. Buscar paquete por código");
        System.out.println("3. Mostrar orden de llegada");
        System.out.println("4. Mostrar paquetes pendientes");
        System.out.println("5. Mostrar municipios únicos");
        System.out.println("6. Mostrar municipios ordenados");
        System.out.println("7. Agrupar paquetes por destino");
        System.out.println("8. Ordenar paquetes");
        System.out.println("9. Calcular peso por municipio");
        System.out.println("10. Contar paquetes por prioridad");
        System.out.println("11. Realizar búsqueda binaria");
        System.out.println("12. Encontrar paquete más pesado");
        System.out.println("0. Volver al menú principal");
        System.out.println("====================================");
    }

    /**
     * Solicita información y registra un nuevo paquete.
     */
    private void registrarPaquete() {

        System.out.println("\n--- REGISTRAR PAQUETE ---");

        String codigo = leerTexto("Código: ");
        String destino = leerTexto("Destino: ");
        double peso = leerDouble("Peso en kg: ");
        int prioridad = leerEntero("Prioridad (1-5): ");
        int tiempo = leerEntero("Tiempo estimado (min): ");

        Paquete paquete = new Paquete(
                codigo, destino, peso, prioridad, tiempo);

        boolean registrado = gestion.registrarPaquete(paquete);

        System.out.println(registrado
                ? "Paquete registrado correctamente."
                : "Ya existe un paquete con ese código.");
    }

    /**
     * Busca un paquete mediante su código utilizando HashMap.
     */
    private void buscarPaquete() {

        String codigo = leerTexto("Código a buscar: ");
        Paquete paquete = gestion.buscarPorCodigo(codigo);

        mostrarResultado(paquete);
    }

    /**
     * Muestra todos los paquetes en su orden original.
     */
    private void mostrarOrdenLlegada() {

        System.out.println("\n--- ORDEN DE LLEGADA ---");

        mostrarLista(gestion.obtenerOrdenLlegada());
    }

    /**
     * Muestra los paquetes que todavía no se han entregado.
     */
    private void mostrarPendientes() {

        System.out.println("\n--- PAQUETES PENDIENTES ---");

        mostrarLista(gestion.obtenerPendientes());
    }

    /**
     * Muestra los municipios sin elementos repetidos.
     */
    private void mostrarMunicipios() {

        System.out.println("\n--- MUNICIPIOS ÚNICOS ---");

        gestion.obtenerMunicipios()
                .forEach(System.out::println);
    }

    /**
     * Muestra los municipios ordenados alfabéticamente.
     */
    private void mostrarMunicipiosOrdenados() {

        System.out.println("\n--- MUNICIPIOS ORDENADOS ---");

        gestion.obtenerMunicipiosOrdenados()
                .forEach(System.out::println);
    }

    /**
     * Muestra los paquetes agrupados según su destino.
     */
    private void mostrarAgrupacion() {

        System.out.println("\n--- PAQUETES POR DESTINO ---");

        Map<String, List<Paquete>> agrupados =
                gestion.agruparPorDestino();

        for (Map.Entry<String, List<Paquete>> entrada
                : agrupados.entrySet()) {

            System.out.println("\nMunicipio: " + entrada.getKey());

            mostrarLista(entrada.getValue());
        }
    }

    /**
     * Permite ordenar temporalmente los paquetes.
     *
     * Se utiliza una copia para no modificar
     * el orden original de registro.
     */
    private void ordenarPaquetes() {

        System.out.println("\n--- ORDENAR PAQUETES ---");
        System.out.println("1. Código ascendente");
        System.out.println("2. Prioridad descendente");
        System.out.println("3. Peso descendente");
        System.out.println("4. Tiempo ascendente");
        System.out.println("5. Orden de despacho");

        int criterio = leerEntero("Seleccione criterio: ");

        List<Paquete> paquetes =
                new ArrayList<>(gestion.obtenerOrdenLlegada());

        switch (criterio) {
            case 1 -> paquetes.sort(null);
            case 2 -> paquetes.sort(
                    ComparadoresPaquetes.POR_PRIORIDAD);
            case 3 -> paquetes.sort(
                    ComparadoresPaquetes.POR_PESO);
            case 4 -> paquetes.sort(
                    ComparadoresPaquetes.POR_TIEMPO);
            case 5 -> paquetes.sort(
                    ComparadoresPaquetes.POR_DESPACHO);
            default -> {
                System.out.println("Criterio no válido.");
                return;
            }
        }

        mostrarLista(paquetes);
    }

    /**
     * Calcula recursivamente el peso total por municipio.
     */
    private void calcularPesoMunicipio() {

        String municipio = leerTexto("Municipio: ");

        double total =
                AlgoritmosRecursivos.calcularPesoPorMunicipio(
                        gestion.obtenerOrdenLlegada(), municipio);

        System.out.printf(
                "Peso total en %s: %.2f kg%n", municipio, total);
    }

    /**
     * Cuenta recursivamente los paquetes con prioridad
     * mayor o igual a la indicada.
     */
    private void contarPrioridades() {

        int minima = leerEntero("Prioridad mínima (1-5): ");

        int cantidad =
                AlgoritmosRecursivos.contarPorPrioridad(
                        gestion.obtenerOrdenLlegada(), minima);

        System.out.println(
                "Paquetes que cumplen la prioridad: " + cantidad);
    }

    /**
     * Realiza una búsqueda binaria sobre una copia
     * ordenada por código.
     */
    private void buscarBinariamente() {

        String codigo = leerTexto("Código a buscar: ");

        List<Paquete> paquetes =
                new ArrayList<>(gestion.obtenerOrdenLlegada());

        // Precondición necesaria para búsqueda binaria.
        paquetes.sort(null);

        Paquete resultado =
                BusquedaBinaria.buscarPorCodigo(paquetes, codigo);

        mostrarResultado(resultado);
    }

    /**
     * Encuentra el paquete más pesado aplicando
     * divide y vencerás.
     */
    private void encontrarMayorPeso() {

        Paquete mayor =
                DivideYVenceras.encontrarMayorPeso(
                        gestion.obtenerOrdenLlegada());

        System.out.println("\n--- PAQUETE MÁS PESADO ---");

        mostrarResultado(mayor);
    }

    /**
     * Muestra el resultado de una búsqueda.
     */
    private void mostrarResultado(Paquete paquete) {

        if (paquete == null) {
            System.out.println("No se encontró ningún paquete.");
        } else {
            System.out.println(paquete);
        }
    }

    /**
     * Muestra una colección de paquetes.
     */
    private void mostrarLista(List<Paquete> paquetes) {

        if (paquetes.isEmpty()) {
            System.out.println("No hay paquetes para mostrar.");
            return;
        }

        for (Paquete paquete : paquetes) {
            System.out.println(paquete);
        }
    }

    /**
     * Lee un texto obligatorio desde la consola.
     */
    private String leerTexto(String mensaje) {

        String valor;

        do {
            System.out.print(mensaje);
            valor = scanner.nextLine().trim();

            if (valor.isEmpty()) {
                System.out.println(
                        "Este campo no puede estar vacío.");
            }

        } while (valor.isEmpty());

        return valor;
    }

    /**
     * Lee un número entero validando su formato.
     */
    private int leerEntero(String mensaje) {

        while (true) {
            System.out.print(mensaje);

            try {
                return Integer.parseInt(
                        scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(
                        "Debe ingresar un número entero válido.");
            }
        }
    }

    /**
     * Lee un número decimal validando su formato.
     */
    private double leerDouble(String mensaje) {

        while (true) {
            System.out.print(mensaje);

            try {
                double valor = Double.parseDouble(
                        scanner.nextLine().trim().replace(',', '.'));

                if (!Double.isFinite(valor)) {
                    throw new NumberFormatException();
                }

                return valor;

            } catch (NumberFormatException e) {
                System.out.println(
                        "Debe ingresar un número decimal válido.");
            }
        }
    }
}
