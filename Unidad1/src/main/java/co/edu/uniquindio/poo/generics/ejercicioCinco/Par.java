package co.edu.uniquindio.poo.generics.ejercicioCinco;

public class Par<T> {
    private T valor1;
    private T valor2;

    public Par(T valor1, T valor2) {
        this.valor1 = valor1;
        this.valor2 = valor2;
    }

    public boolean sonIguales() {
        return valor1.equals(valor2);
    }
}