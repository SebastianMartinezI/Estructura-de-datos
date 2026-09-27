package co.edu.uniquindio.poo.generics.Enunciados.ejercicioNueve;

//GestorPedidos con varios criterios de orden
//Definir Pedido (id, cliente, fecha LocalDate, total). GestorPedidos mantiene LinkedList<Pedido> y debe:
//*Filtrar pedidos de un cliente exacto solo con Iterator.
//*Orden natural por id (Comparable<Pedido>).
//*Comparator por fecha (asc) y, si empata, por total (desc).

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        GestorPedidos gestor = new GestorPedidos();

        gestor.agregarPedido(new Pedido(002,"Carlos", LocalDate.of(2026,9,16),1500));
        gestor.agregarPedido(new Pedido(004,"Juanita", LocalDate.of(2026,8,20),2500));
        gestor.agregarPedido(new Pedido(001,"Juanita", LocalDate.of(2026,9,20),4500));
        gestor.agregarPedido(new Pedido(003,"Luis", LocalDate.of(2026,7,15),3000));

        System.out.println("Pedidos de Juanita");
        for(Pedido pedido : gestor.filtrarCliente("Juanita")){
            System.out.println(pedido);
        }

        System.out.println("\nPedidos ordenados por id:");
        gestor.ordenarPorId();
        gestor.mostrarPedidos();

        System.out.println("\nPedidos ordenados por fecha y total:");
        gestor.ordenarPorFechaTotal();
        gestor.mostrarPedidos();

    }
}
