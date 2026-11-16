package co.edu.uniquindio.sga.dominio.modelo;

import co.edu.uniquindio.sga.estructuras.pila.Pila;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public final class Folio implements Iterable<MovimientoFolio> {

    private final String codigo;

    private NodoMovimiento primero;
    private NodoMovimiento ultimo;

    private int cantidadMovimientos;
    private int modificaciones;

    private BigDecimal saldo = BigDecimal.ZERO;

    private final Pila<MovimientoFolio> operacionesDeshacibles =
            new Pila<>();

    private static final class NodoMovimiento {
        private final MovimientoFolio movimiento;
        private NodoMovimiento siguiente;

        private NodoMovimiento(MovimientoFolio movimiento) {
            this.movimiento = movimiento;
        }
    }

    public Folio(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "El folio debe tener un código"
            );
        }

        this.codigo = codigo.trim();
    }

    public MovimientoFolio registrarCargo(
            String concepto,
            BigDecimal monto
    ) {
        validarConceptoYMonto(concepto, monto);

        return registrarOperacion(
                TipoMovimiento.CARGO,
                concepto.trim(),
                monto
        );
    }

    public MovimientoFolio registrarPago(
            String concepto,
            BigDecimal monto
    ) {
        validarConceptoYMonto(concepto, monto);

        return registrarOperacion(
                TipoMovimiento.PAGO,
                concepto.trim(),
                monto.negate()
        );
    }

    private MovimientoFolio registrarOperacion(
            TipoMovimiento tipo,
            String concepto,
            BigDecimal importe
    ) {
        MovimientoFolio movimiento = crearMovimiento(
                tipo, concepto, importe, null
        );

        agregarAlHistorial(movimiento);
        operacionesDeshacibles.apilar(movimiento);

        return movimiento;
    }

    public MovimientoFolio deshacerUltimaOperacion() {
        MovimientoFolio original =
                operacionesDeshacibles.desapilar();

        MovimientoFolio reverso = crearMovimiento(
                TipoMovimiento.REVERSO,
                "Reverso de movimiento #" + original.getNumero()
                        + ": " + original.getConcepto(),
                original.getImporte().negate(),
                original.getNumero()
        );

        agregarAlHistorial(reverso);

        // No apilamos el reverso: evitamos deshacer indefinidamente
        // el mismo movimiento.
        return reverso;
    }

    private MovimientoFolio crearMovimiento(
            TipoMovimiento tipo,
            String concepto,
            BigDecimal importe,
            Long numeroOriginal
    ) {
        return new MovimientoFolio(
                (long) cantidadMovimientos + 1,
                LocalDateTime.now(),
                tipo,
                concepto,
                importe,
                numeroOriginal
        );
    }

    private void agregarAlHistorial(MovimientoFolio movimiento) {
        NodoMovimiento nuevo = new NodoMovimiento(movimiento);

        if (primero == null) {
            primero = nuevo;
        } else {
            ultimo.siguiente = nuevo;
        }

        ultimo = nuevo;

        saldo = saldo.add(movimiento.getImporte());

        cantidadMovimientos++;
        modificaciones++;
    }

    private void validarConceptoYMonto(
            String concepto,
            BigDecimal monto
    ) {
        if (concepto == null || concepto.isBlank()) {
            throw new IllegalArgumentException(
                    "Debes ingresar un concepto"
            );
        }

        Objects.requireNonNull(monto, "Debes ingresar un monto");

        if (monto.signum() <= 0) {
            throw new IllegalArgumentException(
                    "El monto debe ser mayor que cero"
            );
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public int getCantidadMovimientos() {
        return cantidadMovimientos;
    }

    public boolean puedeDeshacer() {
        return !operacionesDeshacibles.estaVacia();
    }

    @Override
    public Iterator<MovimientoFolio> iterator() {
        return new Iterator<MovimientoFolio>() {

            private NodoMovimiento actual = primero;
            private final int version = modificaciones;

            private void comprobarModificaciones() {
                if (version != modificaciones) {
                    throw new ConcurrentModificationException(
                            "El folio cambió durante el recorrido"
                    );
                }
            }

            @Override
            public boolean hasNext() {
                comprobarModificaciones();
                return actual != null;
            }

            @Override
            public MovimientoFolio next() {
                comprobarModificaciones();

                if (actual == null) {
                    throw new NoSuchElementException(
                            "No hay más movimientos"
                    );
                }

                MovimientoFolio movimiento = actual.movimiento;
                actual = actual.siguiente;

                return movimiento;
            }
        };
    }
}