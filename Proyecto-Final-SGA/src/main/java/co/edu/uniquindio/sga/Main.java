
package co.edu.uniquindio.sga;

import co.edu.uniquindio.sga.interfaz.SgaApplication;
import javafx.application.Application;

/**
 * Punto de entrada principal del SGA.
 */
public final class Main {

    private Main() {
        // Evita la instanciacion de esta clase.
    }

    public static void main(String[] args) {
        Application.launch(SgaApplication.class, args);
    }
}
