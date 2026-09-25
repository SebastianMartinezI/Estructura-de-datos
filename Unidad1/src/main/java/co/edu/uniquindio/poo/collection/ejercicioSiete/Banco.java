package co.edu.uniquindio.poo.collection.ejercicioSiete;

import java.util.LinkedList;

public class Banco {

    private LinkedList<String> colaClientes;

    public Banco() {

        colaClientes = new LinkedList<>();

    }

    // Agregar cliente normal

    public void agregarCliente(String cliente) {

        colaClientes.addLast(cliente);

    }

    // Agregar cliente urgente

    public void agregarClienteUrgente(String cliente) {

        colaClientes.addFirst(cliente);

    }

    // Atender primer cliente

    public void atenderCliente() {

        if(!colaClientes.isEmpty()) {

            String clienteAtendido = colaClientes.removeFirst();

            System.out.println(
                    "Cliente atendido: " + clienteAtendido
            );

        } else {

            System.out.println(
                    "No hay clientes en espera"
            );
        }
    }

    // Mostrar clientes en espera

    public void mostrarCola() {

        System.out.println(
                "Clientes en espera:"
        );

        for(String cliente : colaClientes) {

            System.out.println(cliente);
        }
    }
}