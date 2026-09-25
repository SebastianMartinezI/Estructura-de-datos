package co.edu.uniquindio.poo.generics.ejercicioSeis;

public class CajaNumerica<T extends Number> {
    private T valor;


    public CajaNumerica(T valor) {
        this.valor = valor;
    }

    public double doble() {
        return valor.doubleValue() * 2;
        }
}