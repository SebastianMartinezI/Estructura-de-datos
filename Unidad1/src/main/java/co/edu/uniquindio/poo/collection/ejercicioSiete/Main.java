package co.edu.uniquindio.poo.collection.ejercicioSiete;

public class Main {

    //En un banco, el sistema de atención al cliente debe manejar los turnos de manera
    // ordenada. Para lograrlo, se empleará una LinkedList (String), la cual permitirá
    // agregar clientes en la cola de espera, atender al primero en la lista y ofrecer
    // una funcionalidad especial para insertar clientes con urgencia al inicio de la cola
    // sin afectar el rendimiento.

    public static void main(String[] args) {

        Banco banco = new Banco();

        banco.agregarCliente("Carlos");
        banco.agregarCliente("María");
        banco.agregarCliente("Pedro");

        System.out.println("Cola Inicial");
        banco.mostrarCola();
        System.out.println("\nIngreso de cliente urgente");

        banco.agregarClienteUrgente("Juan - Urgente");
        banco.mostrarCola();
        System.out.println("\nAtencion de cliente");
        banco.atenderCliente();
        banco.atenderCliente();
        System.out.println("\nCola Final");
        banco.mostrarCola();
    }
}