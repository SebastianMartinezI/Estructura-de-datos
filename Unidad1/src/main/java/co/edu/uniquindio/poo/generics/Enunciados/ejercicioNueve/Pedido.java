package co.edu.uniquindio.poo.generics.Enunciados.ejercicioNueve;

import java.time.LocalDate;

//GestorPedidos con varios criterios de orden
//Definir Pedido (id, cliente, fecha LocalDate, total). GestorPedidos mantiene LinkedList<Pedido> y debe:
//*Filtrar pedidos de un cliente exacto solo con Iterator.
//*Orden natural por id (Comparable<Pedido>).
//*Comparator por fecha (asc) y, si empata, por total (desc).

public class Pedido implements Comparable<Pedido>{
    private int id;
    private String cliente;
    private LocalDate fecha;
    private double total;

    public Pedido(int id, String cliente, LocalDate fecha, double total) {
        this.id = id;
        this.cliente = cliente;
        this.fecha = fecha;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public int compareTo(Pedido otro) {
        return Integer.compare(this.id, otro.id);
    }

    @Override
    public String toString() {
        return  "El id es: " + id +
                " El cliente es: " + cliente +
                " Fecha del pedido: " + fecha +
                " Total es: " + total;
    }
}
