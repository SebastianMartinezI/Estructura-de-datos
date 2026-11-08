package co.edu.uniquindio.poo.modelos;

public class NodoEquipo {
    private int codigo;
    private NodoEquipo izquierdo;
    private NodoEquipo derecho;

    public NodoEquipo(int codigo){
        this.codigo=codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public NodoEquipo getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(NodoEquipo izquierdo) {
        this.izquierdo = izquierdo;
    }

    public NodoEquipo getDerecho() {
        return derecho;
    }

    public void setDerecho(NodoEquipo derecho) {
        this.derecho = derecho;
    }
}
