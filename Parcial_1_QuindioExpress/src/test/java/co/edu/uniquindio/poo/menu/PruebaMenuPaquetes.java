package co.edu.uniquindio.poo.menu;

import co.edu.uniquindio.poo.modelo.Paquete;
import co.edu.uniquindio.poo.servicios.GestionPaquetes;

/**
 * Permite ejecutar de manera independiente
 * el menú de paquetes durante el desarrollo.
 *
 * @author Juan Martinez
 */
public class PruebaMenuPaquetes {

    public static void main(String[] args) {

        GestionPaquetes gestion = new GestionPaquetes();

        gestion.registrarPaquete(new Paquete(
                "PQ731", "Armenia", 4.5, 5, 30));

        gestion.registrarPaquete(new Paquete(
                "PQ105", "Salento", 3.0, 3, 25));

        gestion.registrarPaquete(new Paquete(
                "PQ942", "Armenia", 7.2, 4, 45));

        gestion.registrarPaquete(new Paquete(
                "PQ318", "Calarcá", 2.5, 2, 20));

        MenuPaquetes menu = new MenuPaquetes(gestion);

        menu.ejecutar();
    }
}