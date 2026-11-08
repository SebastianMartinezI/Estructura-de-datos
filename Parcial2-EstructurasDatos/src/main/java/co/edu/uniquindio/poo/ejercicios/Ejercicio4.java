package co.edu.uniquindio.poo.ejercicios;

import co.edu.uniquindio.poo.modelos.NodoEquipo;
import co.edu.uniquindio.poo.servicios.ArbolEquipo;

public class Ejercicio4 {
    public static void main(String[] args) {
        ArbolEquipo arbol = new ArbolEquipo();

        NodoEquipo raiz = new NodoEquipo(50);

        raiz.setIzquierdo(new NodoEquipo(30));
        raiz.setDerecho(new NodoEquipo(70));

        raiz.getIzquierdo().setIzquierdo(new NodoEquipo(20));
        raiz.getIzquierdo().setDerecho(new NodoEquipo(40));

        raiz. getDerecho().setIzquierdo(new NodoEquipo(60));
        raiz.getDerecho().setDerecho(new NodoEquipo(80));

        int limiteInferior = 30;
        int limiteSuperior = 70;

        int cantidad = arbol.contarEquiposEnRango(raiz,limiteInferior,limiteSuperior);

        System.out.println("Equipo entra " + limiteInferior + " y " + limiteSuperior + ": " + cantidad);

        System.out.println("Arbol vacio: " + arbol.contarEquiposEnRango(null,30,70));

        System.out.println("Rango invalido: " + arbol.contarEquiposEnRango(raiz, 70,30));

        System.out.println("Rango con un solo codigo: " + arbol.contarEquiposEnRango(raiz, 50,50));

    }
}
