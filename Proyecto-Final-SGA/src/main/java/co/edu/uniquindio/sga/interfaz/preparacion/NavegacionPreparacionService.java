package co.edu.uniquindio.sga.interfaz.preparacion;

import co.edu.uniquindio.sga.estructuras.pila.Pila;

public final class NavegacionPreparacionService {

    private final Pila<String> pantallasAnteriores;

    public NavegacionPreparacionService() {
        this.pantallasAnteriores = new Pila<>();
    }

    public void guardarPantalla(String pantalla) {
        pantallasAnteriores.apilar(pantalla);
    }

    public String volverAtras() {
        if (pantallasAnteriores.estaVacia()) {
            return "VistaPrincipal";
        }
        return pantallasAnteriores.desapilar();
    }

    public boolean hayPantallaAnterior() {
        return !pantallasAnteriores.estaVacia();
    }
}