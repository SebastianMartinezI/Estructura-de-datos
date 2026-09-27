package co.edu.uniquindio.poo.Enunciados.ejercicioNueve;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

public class GestorPedidos {
    private LinkedList<Pedido> pedidos = new LinkedList<>();

    public void agregarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }

    public LinkedList<Pedido> filtrarCliente(String clienteBuscado) {
        LinkedList<Pedido> encontrados = new LinkedList<>();
        Iterator<Pedido> iterador = pedidos.iterator();

        while (iterador.hasNext()) {
            Pedido pedido = iterador.next();

            if (pedido.getCliente().equals(clienteBuscado)) {
                encontrados.add(pedido);
            }
        }
        return encontrados;
    }

    public void ordenarPorId(){
        pedidos.sort(null);
    }

    public void ordenarPorFechaTotal() {
        Comparator<Pedido> comparador = (pedido1, pedido2) -> {
            int resultadoFecha =
                    pedido1.getFecha().compareTo(pedido2.getFecha());

            if (resultadoFecha != 0) {
                return resultadoFecha;
            }

            return Double.compare(
                    pedido1.getTotal(),
                    pedido2.getTotal()
            );
        };

        pedidos.sort(comparador);
    }

    public void mostrarPedidos(){
        for(Pedido pedido: pedidos){
            System.out.println(pedido);
        }
    }
}
