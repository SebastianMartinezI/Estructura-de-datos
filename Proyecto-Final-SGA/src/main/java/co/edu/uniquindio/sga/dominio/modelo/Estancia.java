package co.edu.uniquindio.sga.dominio.modelo;

import co.edu.uniquindio.sga.excepciones.FechaInvalidaException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Rango de estancia [entrada, salida): la salida no se cobra ni se ocupa.
 * Ejemplo de Iterable/Iterator sin utilizar colecciones de java.util.
 */
public final class Estancia implements Iterable<LocalDate> {
    private final LocalDate entrada;
    private final LocalDate salida;

    public Estancia(LocalDate entrada, LocalDate salida) {
        if (entrada == null || salida == null || !salida.isAfter(entrada)) {
            throw new FechaInvalidaException(
                    "La fecha de salida debe ser posterior a la fecha de entrada.");
        }
        this.entrada = entrada;
        this.salida = salida;
    }

    public LocalDate getEntrada() { return entrada; }
    public LocalDate getSalida() { return salida; }

    public long numeroNoches() {
        return ChronoUnit.DAYS.between(entrada, salida);
    }

    @Override
    public Iterator<LocalDate> iterator() {
        return new Iterator<>() {
            private LocalDate siguiente = entrada;

            @Override
            public boolean hasNext() {
                return siguiente.isBefore(salida);
            }

            @Override
            public LocalDate next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No hay más noches por recorrer.");
                }
                LocalDate noche = siguiente;
                siguiente = siguiente.plusDays(1);
                return noche;
            }
        };
    }
}
