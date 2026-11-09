package co.edu.uniquindio.poo.servicios;

import co.edu.uniquindio.poo.modelos.NodoEquipo;

public class ArbolEquipo {

    public int contarEquiposEnRango(NodoEquipo raiz, int limiteInterior, int limiteSuperior){

        //Arbol vacio o rango invalido
        if ( raiz == null || limiteInterior > limiteSuperior){
            return 0;
        }

        //El nodo y su subarbol izquierdo estan por debajo del rango
        if (raiz.getCodigo()<limiteInterior){
            return contarEquiposEnRango(raiz.getDerecho(), limiteInterior, limiteSuperior);
        }

        //El nodo y su subarbol derecho esxtan por encima del rango
        if(raiz.getCodigo()>limiteSuperior) {
            return contarEquiposEnRango(raiz.getIzquierdo(), limiteInterior, limiteSuperior);
        }

        //El nodo esta dentro del rango: contamos uno y revisamos sus hijos
        return 1
                + contarEquiposEnRango(raiz.getIzquierdo(), limiteInterior, limiteSuperior)
                + contarEquiposEnRango(raiz.getDerecho(),limiteInterior,limiteSuperior);
    }
}
