package co.edu.uniquindio.poo.generics.NivelAvanzado.ejercicioQuince;

public class CalculadoraAvanzada<T extends Number & Comparable<T>> {

    public double sumar(T primero, T segundo) {
        return primero.doubleValue() + segundo.doubleValue();
    }

    public double restar(T primero, T segundo) {
        return primero.doubleValue() - segundo.doubleValue();
    }

    public T maximo(T primero, T segundo) {
        if (primero.compareTo(segundo) >= 0) {
            return primero;
        }
        return segundo;
    }

    public T minimo(T primero, T segundo) {
        if (primero.compareTo(segundo) <= 0) {
            return primero;
        }
        return segundo;

    }
}
