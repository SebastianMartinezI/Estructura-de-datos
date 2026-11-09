package co.edu.uniquindio.poo.modelos;

public class Paquete {

    public enum TipoEntrega{
        URGENTE,
        NORMAL
    }

    private final String codigo;
    private final TipoEntrega tipoEntrega;
    private final int peso;

    public Paquete (String codigo, TipoEntrega tipoEntrega, int peso){
        if(codigo == null || codigo.trim().isEmpty()){
            throw new IllegalArgumentException("El codigo no puede estar vacio");
        }
        if(peso<=0){
            throw new IllegalArgumentException("El peso deber ser mayor que cero");
        }
        this.codigo=codigo;
        this.tipoEntrega=tipoEntrega;
        this.peso=peso;
    }

    public String getCodigo() {
        return codigo;
    }

    public TipoEntrega getTipoEntrega() {
        return tipoEntrega;
    }

    public int getPeso() {
        return peso;
    }

    @Override
    public String toString() {
        return "Codigo: " + codigo + " | Tipo: " + tipoEntrega + " | Peso: " + peso + "kg";
    }
}
