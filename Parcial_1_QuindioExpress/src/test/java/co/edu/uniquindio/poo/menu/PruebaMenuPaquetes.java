package co.edu.uniquindio.poo.menu;

import co.edu.uniquindio.poo.modelo.Paquete;
import co.edu.uniquindio.poo.servicios.GestionPaquetes;

import java.util.Scanner;

/**
 * Permite ejecutar de manera independiente
 * el menú de paquetes durante el desarrollo.
 *
 * No modifica el Main principal del proyecto.
 *
 * @author Juan Martinez
 */
public class PruebaMenuPaquetes {

    public static void main(String[] args) {

        GestionPaquetes gestion = new GestionPaquetes();

        // Datos iniciales para probar las operaciones.
        gestion.registrarPaquete(new Paquete(
                "PQ731", "Armenia", 4.5, 5, 30));

        gestion.registrarPaquete(new Paquete(
                "PQ105", "Salento", 3.0, 3, 25));

        gestion.registrarPaquete(new Paquete(
                "PQ942", "Armenia", 7.2, 4, 45));

        gestion.registrarPaquete(new Paquete(
                "PQ318", "Calarcá", 2.5, 2, 20));

        // El menú utiliza la misma gestión de paquetes.
        Scanner scanner = new Scanner(System.in);

        MenuPaquetes menu =
                new MenuPaquetes(gestion, scanner);

        menu.ejecutar();

        // No cerramos System.in porque posteriormente
        // será compartido con el menú principal.
    }
}
