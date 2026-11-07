package co.edu.uniquindio.poo.modelos;

/**
 * Representa una caja almacenada en el deposito.
 * Cada caja tiene un codigo identificador y un valor entero.
 */
public final class Caja {

    private final String codigo;
    private final int valor;

    /**
     * Construye una caja con sus datos.
     *
     * @param codigo identificador de la caja.
     * @param valor valor numerico asociado.
     */
    public Caja(String codigo, int valor) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "El codigo de la caja no puede estar vacio"
            );
        }

        this.codigo = codigo;
        this.valor = valor;
    }

    public String getCodigo() {
        return codigo;
    }

    public int getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return codigo + ":" + valor;
    }
}
