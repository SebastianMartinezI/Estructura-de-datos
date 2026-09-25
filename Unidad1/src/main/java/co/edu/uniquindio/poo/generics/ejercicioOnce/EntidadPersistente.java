package co.edu.uniquindio.poo.generics.ejercicioOnce;

public class EntidadPersistente<T extends Number & Comparable<T>> {
    private T valor;

    public EntidadPersistente(T valor) {
        this.valor = valor;
    }

    public T getValor() {
        return valor;
    }

    public int compararCon(EntidadPersistente<T> otro) {
        return valor.compareTo(otro.getValor());
    }
}